package org.erachain.dapp.epoch.shibaverse;

import com.google.common.primitives.Ints;
import com.google.common.primitives.Longs;
import org.apache.commons.net.util.Base64;
import org.erachain.core.account.Account;
import org.erachain.core.account.PublicKeyAccount;
import org.erachain.core.block.Block;
import org.erachain.core.item.assets.AssetCls;
import org.erachain.core.item.assets.AssetVenture;
import org.erachain.core.transaction.RSend;
import org.erachain.core.transaction.Transaction;
import org.erachain.core.transaction.TransferredBalances;
import org.erachain.dapp.DApp;
import org.erachain.dapp.DAppFactory;
import org.erachain.dapp.DAppTimed;
import org.erachain.dapp.epoch.EpochDAppJson;
import org.erachain.dapp.epoch.shibaverse.server.Farm_01;
import org.erachain.datachain.CreditAddressesMap;
import org.erachain.datachain.DCSet;
import org.erachain.datachain.ItemAssetBalanceMap;
import org.erachain.datachain.SmartContractValues;
import org.erachain.dbs.IteratorCloseable;
import org.erachain.webserver.WebResource;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.mapdb.Fun;
import org.mapdb.Fun.Tuple2;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.text.DecimalFormat;
import java.util.*;

import static org.erachain.core.item.assets.AssetTypes.AS_INSIDE_ASSETS;

public class ShibaVerseDApp extends EpochDAppJson implements DAppTimed {

    int WAIT_RAND = 3;

    static public final int ID = 1001;
    static public final String NAME = "Shiba Verse";

    final public static HashSet<PublicKeyAccount> accounts = new HashSet<>();

    final public static byte[] HASH = crypto.digest(Longs.toByteArray(ID));
    // APPC45p29ZjcEEvSzhgUe8RfUzMZ1i2GFG
    final public static PublicKeyAccount MAKER = PublicKeyAccount.makeForDApp(crypto.digest(Longs.toByteArray(ID)));

    // APPBttBTR6pSEg6FBny3reRG4rkdp8dtG8
    final public static PublicKeyAccount FARM_01_PUBKEY = noncePubKey(HASH, (byte) 1);

    static {
        accounts.add(MAKER);
        accounts.add(FARM_01_PUBKEY);
    }

    private static JSONObject farm_01_settings = new JSONObject();

    public static Farm_01 FARM_01_SERVER = null;

    static {
        farm_01_settings.put("account", FARM_01_PUBKEY.getAddress());

        if (DAppFactory.settingsJSON.containsKey("shiba")) {
            boolean farm_01 = (boolean) ((JSONObject) DAppFactory.settingsJSON.get("shiba")).getOrDefault("farm_01", false);
            if (false && farm_01)
                FARM_01_SERVER = new Farm_01(farm_01_settings);
        }
    }


    /**
     * admin account
     */
    final static public Account adminAddress = new Account("7NhZBb8Ce1H2S2MkPerrMnKLZNf9ryNYtP");

    final static public String COMMAND_WITHDRAW = "withdraw";
    final static public String COMMAND_CATH_COMET = "catch comets";
    /**
     * in Title: buy
     * in message - asset key
     */
    final static public String COMMAND_BUY = "buy";
    final static public long buster01Key = 11111L;
    /**
     * use as JSONArray in TX message. Title will be ignoged.
     * ["set price", { "shop assetKey1": {"price assetKey1": "price value", ...}}]<br>For example:
     * ["set price",{"1": {"2":"0.1","18":2}}]
     */
    final static public String COMMAND_SET_PRICE = "set price";

    final static public String COMMAND_STAKE = "stake";

    /**
     * make random from future
     */
    final static public String COMMAND_RANDOM = "random";

    final static public String COMMAND_FARM = "farm";
    final static public String COMMAND_CHARGE = "charge";
    final static public String COMMAND_PICK_UP = "pick up";
    /**
     * GRAVUTA KEY
     */
    static final private Tuple2 INIT_KEY = new Tuple2(ID, "i");

    private Long gravitaKey;

    private ShibaVerseDApp() {
        super(ID, MAKER);
    }

    public ShibaVerseDApp(String dataStr, String status) {
        super(ID, MAKER, dataStr, status);
    }

    public ShibaVerseDApp(String dataStr, Transaction commandTx, Block block) {
        super(ID, MAKER, dataStr, "", commandTx, block);
    }

    @Override
    public DApp of(String dataStr, Transaction commandTx, Block block) {
        if (commandTx instanceof TransferredBalances) {

            RSend rSend = (RSend) commandTx;
            // dataStr = null
            if (dataStr == null || dataStr.isEmpty())
                dataStr = rSend.getTitle();

            if (dataStr == null || dataStr.isEmpty())
                return null;

            Account recipent = rSend.getRecipient();

            if (recipent.equals(FARM_01_PUBKEY)) {
                if (rSend.balancePosition() == Account.BALANCE_POS_DEBT && rSend.hasAmount()) {
                    return new ShibaVerseDApp(rSend.isBackward() ? COMMAND_PICK_UP : COMMAND_FARM, commandTx, block);
                } else if (rSend.balancePosition() == Account.BALANCE_POS_OWN) {
                    return new ShibaVerseDApp(COMMAND_CHARGE, commandTx, block);
                }
            }

            return new ShibaVerseDApp(dataStr, commandTx, block);
        }
        return null;
    }

    @Override
    public DApp of(Transaction commandTx, Block block) {
        throw new RuntimeException("Wrong OF(...)");
    }

    public static void setDAppFactory() {
        DApp instance = new ShibaVerseDApp();
        for (Account account : accounts) {
            DAppFactory.STOCKS.put(account, instance);
        }
        DAppFactory.DAPP_BY_ID.put(ID, instance);
    }

    public String getName() {
        return NAME;
    }

    private boolean isAdminCommand(Transaction transaction) {
        return transaction.getCreator().equals(adminAddress);
    }

    @Override
    public boolean isValid() {
        if (isAdminCommand(commandTx)) {
            return true;
        }

        if (!dcSet.getSmartContractValues().contains(INIT_KEY)) {
            fail("not initated yet");
            return false;
        }

        if (gravitaKey == null)
            gravitaKey = (Long) dcSet.getSmartContractValues().get(INIT_KEY);

        if (gravitaKey == null) {
            fail("not initated yet");
            return false;
        }

        return true;

    }

    /// PARSE / TOBYTES

    public static ShibaVerseDApp Parse(byte[] bytes, int pos, int forDeal) {

        // skip ID
        pos += 4;

        String data;
        String status;
        if (forDeal == Transaction.FOR_DB_RECORD) {
            byte[] statusSizeBytes = Arrays.copyOfRange(bytes, pos, pos + 4);
            int statusLen = Ints.fromByteArray(statusSizeBytes);
            pos += 4;
            byte[] statusBytes = Arrays.copyOfRange(bytes, pos, pos + statusLen);
            pos += statusLen;
            status = new String(statusBytes, StandardCharsets.UTF_8);

            byte[] dataSizeBytes = Arrays.copyOfRange(bytes, pos, pos + 4);
            int dataSize = Ints.fromByteArray(dataSizeBytes);
            pos += 4;
            byte[] dataBytes = Arrays.copyOfRange(bytes, pos, pos + dataSize);
            pos += dataSize;
            data = new String(dataBytes, StandardCharsets.UTF_8);

        } else {
            data = "";
            status = "";
        }

        return new ShibaVerseDApp(data, status);
    }

    ///////// COMMANDS

    public static byte[] getRandHash(Block block, Transaction transaction, int nonce) {

        byte[] hash = new byte[32];
        System.arraycopy(block.getSignature(), 0, hash, 0, 14);
        System.arraycopy(Ints.toByteArray(nonce), 0, hash, 14, 4);
        System.arraycopy(transaction.getSignature(), 0, hash, 18, 14);

        hash = crypto.digest(hash);
        int slot = 0;
        int slotRare;
        int slotRareLvl;

        byte[] randomArray = new byte[8];

        // GET 4 rabdom levels of Rarity
        int index = 0;
        do {
            slotRare = Ints.fromBytes((byte) 0, (byte) 0, hash[index++], hash[index++]);
            if ((slotRare >> 11) == 0) {
                slotRareLvl = 5;
            } else if ((slotRare >> 12) == 0) {
                slotRareLvl = 4;
            } else if ((slotRare >> 13) == 0) {
                slotRareLvl = 3;
            } else if ((slotRare >> 14) == 0) {
                slotRareLvl = 2;
            } else if ((slotRare >> 15) == 0) {
                slotRareLvl = 1;
            } else {
                slotRareLvl = 0;
            }
            randomArray[slot] = (byte) slotRareLvl;

        } while (slot++ < 3);

        // GET 4 rabdom values
        do {
            randomArray[slot] = hash[index++];
        } while (slot++ < 7);

        return randomArray;

    }

    /**
     * @param asOrphan
     */
    private void catchComets(boolean asOrphan) {
        // рождение комет

        if (gravitaKey == null)
            gravitaKey = (Long) dcSet.getSmartContractValues().get(INIT_KEY);

        RSend rSend = (RSend) commandTx;
        if (commandTx.getAssetKey() != gravitaKey) {
            fail("Wrong asset key. Need " + gravitaKey);
            return;
        } else if (!rSend.hasAmount() || !rSend.hasPacket() && commandTx.getAmount().signum() <= 0) {
            fail("Wrong amount. Need > 0");
            return;
        } else if (rSend.isBackward()) {
            fail("Wrong direction - backward");
            return;
        } else if (rSend.balancePosition() != Account.BALANCE_POS_OWN) {
            fail("Wrong balance position. Need OWN[1]");
            return;
        }

        SmartContractValues valuesMap = dcSet.getSmartContractValues();
        PublicKeyAccount creator = commandTx.getCreator();
        int count = 5 * commandTx.getAmount().intValue();

        // need select direction by asOrphan, else decrementDelete will not work!
        int nonce;
        if (asOrphan)
            nonce = 1;
        else
            nonce = count;

        AssetVenture comet;
        Long assetKey;
        do {

            // GET RANDOM
            byte[] randomArray = getRandHash(block, commandTx, nonce);
            if (asOrphan)
                nonce++;
            else
                nonce--;

            // make object name: "c" - comet, "0" - era, Rarity1,2, Value1,2,
            int value1 = Byte.toUnsignedInt(randomArray[7]) >>> 5;
            int value2 = Byte.toUnsignedInt(randomArray[6]) >>> 5;
            String name = "c0" + randomArray[0] + randomArray[1] + value1 + value2;
            Tuple2 keyID = new Tuple2(ID, name);

            if (asOrphan) {
                assetKey = (Long) valuesMap.get(keyID);

                AssetCls asset = dcSet.getItemAssetMap().get(assetKey);
                if (asset.getReleased(dcSet).equals(BigDecimal.ONE)) {
                    // DELETE FROM BLOCKCHAIN DATABASE
                    dcSet.getItemAssetMap().decrementDelete(assetKey);

                    // DELETE FROM CONTRACT DATABASE
                    valuesMap.delete(keyID);

                }

            } else {
                // seek if already exist
                if (valuesMap.contains(keyID)) {
                    assetKey = (Long) valuesMap.get(keyID);
                } else {
                    // make new COMET
                    JSONObject json = new JSONObject();
                    json.put("value1", value1);
                    json.put("value2", value2);
                    json.put("rare1", Byte.toUnsignedInt(randomArray[6]));
                    json.put("rare2", Byte.toUnsignedInt(randomArray[7]));
                    json.put("type", "Comet");
                    json.put("random", Base64.encodeBase64StringUnChunked(randomArray));
                    String description = json.toJSONString();

                    comet = new AssetVenture(null, stock, name, null, null,
                            description, AS_INSIDE_ASSETS, 0, 0);
                    comet.setReference(commandTx.getSignature(), commandTx.getDBRef());

                    //INSERT INTO BLOCKCHAIN DATABASE
                    assetKey = dcSet.getItemAssetMap().incrementPut(comet);
                    //INSERT INTO CONTRACT DATABASE
                    dcSet.getSmartContractValues().put(keyID, assetKey);
                }
            }

            // TRANSFER ASSET
            transfer(dcSet, block, commandTx, stock, creator, BigDecimal.ONE, assetKey, asOrphan, null, "catchComets");

        } while (--count > 0);

        if (asOrphan)
            status = "wait";
        else
            status = "done";

        return;

    }

    private static Tuple2<Integer, String> priceKey(long shopAssetKey, long priceAssetKey) {
        return new Tuple2(ID, "pr" + ("" + shopAssetKey + priceAssetKey).hashCode());
    }

    /**
     * get current price
     *
     * @param shopAssetKey
     * @param priceAssetKey
     * @return
     */
    private BigDecimal shopPrice(long shopAssetKey, long priceAssetKey) {

        SmartContractValues map = dcSet.getSmartContractValues();
        BigDecimal price = (BigDecimal) map.get(priceKey(shopAssetKey, priceAssetKey));

        if (price != null)
            return price;

        switch ((int) shopAssetKey) {
            case (int) buster01Key:
                switch ((int) priceAssetKey) {
                    case 18:
                        return new BigDecimal("0.1");
                    default:
                        // for TEST ONLY
                        return new BigDecimal("0.01");
                }
        }
        if (true) {
            // for TEST ONLY
            return new BigDecimal("0.1");
        }

        return BigDecimal.ZERO;
    }

    private void random(boolean asOrphan) {
    }

    /**
     * shop for sell items. Example of message: ["buy", 1001]
     *
     * @param asOrphan
     */
    private void shopBuy(boolean asOrphan) {
        PublicKeyAccount creator = commandTx.getCreator();

        if (asOrphan) {
            long priceAssetKey = commandTx.getAssetKey();
            long shopAssetKey = Long.parseLong(pars.get(1).toString());
            Object[] result = removeState(commandTx.getDBRef());
            if (result.length > 0) {
                BigDecimal amountToSell = (BigDecimal) result[0];
                transfer(dcSet, null, commandTx, creator, stock, amountToSell, shopAssetKey, true, null, null);

                BigDecimal leftAmount = (BigDecimal) result[1];
                if (leftAmount.signum() > 0) {
                    transfer(dcSet, null, commandTx, creator, stock, leftAmount, priceAssetKey, true, null, null);
                }
            }
        } else {

            long shopAssetKey;
            AssetCls shopAsset;
            RSend rSend = (RSend) commandTx;
            if (!rSend.hasAmount()) {
                fail("Has not amount");
                return;
            } else if (commandTx.getAssetKey() != 1 && commandTx.getAssetKey() != 18) {
                fail("Wrong asset key. Need 1 or 18");
                return;
            } else if (rSend.balancePosition() != Account.BALANCE_POS_OWN) {
                fail("Wrong balance position. Need OWN[1]");
                return;
            } else if (!rSend.hasPacket() && commandTx.getAmount().signum() <= 0) {
                fail("Wrong amount. Need > 0");
                return;
            } else if (rSend.isBackward()) {
                fail("Wrong direction - backward");
                return;
            } else if (pars == null) {
                fail("Empty pars");
                return;
            } else {
                try {
                    shopAssetKey = Long.parseLong(pars.get(1).toString());
                } catch (Exception e) {
                    fail("Wrong asset key: " + pars.get(1));
                    return;
                }

                if (false && shopAssetKey != buster01Key) {
                    fail("Wrong asset key");
                    return;
                }
            }

            long priceAssetKey = commandTx.getAssetKey();
            BigDecimal sellPrice = shopPrice(shopAssetKey, priceAssetKey);
            if (sellPrice == null || sellPrice.signum() < 1) {
                fail("not priced");
                return;
            }

            shopAsset = dcSet.getItemAssetMap().get(shopAssetKey);
            if (shopAsset == null) {
                fail("Shop asset not exist");
                return;
            }

            AssetCls priceAsset = commandTx.getAsset();
            BigDecimal leftAmount = commandTx.getAmount();

            BigDecimal amountToSell = leftAmount.multiply(sellPrice).setScale(shopAsset.getScale(), BigDecimal.ROUND_HALF_DOWN);
            if (amountToSell.signum() > 0 && !priceAsset.isUnlimited(stock, false)) {
                Tuple2<BigDecimal, BigDecimal> stockBal = stock.getBalance(dcSet, shopAssetKey, Account.BALANCE_POS_OWN);
                if (amountToSell.compareTo(stockBal.b) > 0) {
                    // if not enought amount
                    if (stockBal.b.signum() > 0) {
                        amountToSell = stockBal.b;
                    } else {
                        amountToSell = BigDecimal.ZERO;
                    }
                }
            }

            if (amountToSell.signum() > 0) {
                // TRANSFER ASSET
                transfer(dcSet, block, commandTx, stock, creator, amountToSell, shopAssetKey, false, null, "buy");

                // RETURN change
                leftAmount = leftAmount.subtract(amountToSell.divide(sellPrice, priceAsset.getScale(), BigDecimal.ROUND_DOWN));
            }

            status = "done x" + sellPrice.toPlainString();

            if (leftAmount.signum() > 0) {
                transfer(dcSet, block, commandTx, stock, creator, leftAmount, priceAssetKey, false, null, "change");
                status += ", change: " + leftAmount.toPlainString();
            }

            // store results for orphan
            putState(commandTx.getDBRef(), new Object[]{amountToSell, leftAmount});

        }

    }

    private void stakeAction(boolean asOrphan) {

        RSend rSend = (RSend) commandTx;
        if (commandTx.getAssetKey() != gravitaKey) {
            fail("Wrong asset key. Need " + gravitaKey);
            return;
        } else if (!rSend.hasAmount() || !rSend.hasPacket() && commandTx.getAmount().signum() <= 0) {
            fail("Wrong amount. Need > 0");
            return;
        } else if (rSend.isBackward()) {
            fail("Wrong direction - backward");
            return;
        } else if (rSend.balancePosition() != Account.BALANCE_POS_OWN) {
            fail("Wrong balance position. Need [1]OWN");
            return;
        }

        fail("-");
    }

    // CHARGE all from FARM 01
    private void farmChargeAction(boolean asOrphan) {
        SmartContractValues mapValues = dcSet.getSmartContractValues();
        Tuple2<Integer, String> farmKeyValue;
        BigDecimal farmedValue;
        try (IteratorCloseable iterator = mapValues.getIterator()) {
            while (iterator.hasNext()) {
                Object key = iterator.next();
                farmKeyValue = new Tuple2<>(ID, "farm1" + key.toString());
                farmedValue = (BigDecimal) mapValues.get(farmKeyValue);
            }
        } catch (IOException e) {
        }

    }

    private void farmAction(boolean asOrphan) {

        fail("-");

        RSend rSend = (RSend) commandTx;
        if (asOrphan) {
            return;

        } else {
            AssetCls asset = dcSet.getItemAssetMap().get(commandTx.getAssetKey());
            if (!asset.getMaker().equals(MAKER)) {
                fail("Wrong asset maker. Need " + MAKER.getAddress());
                return;
            } else if (!rSend.hasAmount()) {
                fail("Wrong amount. Need > 0");
                return;
            } else if (rSend.isBackward()) {
                fail("Wrong direction - backward");
                return;
            } else if (rSend.balancePosition() != Account.BALANCE_POS_DEBT) {
                fail("Wrong balance position. Need [2]DEBT");
                return;
            }
        }

        if (rSend.isBackward()) {
            // WITHDRAW
            farmChargeAction(asOrphan);
        } else {
            // DEPOSITE

        }

    }

    private static void farm(DCSet dcSet, Block block, boolean asOrphan) {
        CreditAddressesMap map = dcSet.getCreditAddressesMap();
        SmartContractValues mapValues = dcSet.getSmartContractValues();
        Fun.Tuple3<String, Long, String> key;
        BigDecimal credit;
        Tuple2<Integer, String> farmKeyValue;
        BigDecimal farmedValue;
        HashMap<String, BigDecimal> results = new HashMap<>();
        try (IteratorCloseable<Fun.Tuple3<String, Long, String>> iterator = map.getDebitorsIterator(FARM_01_PUBKEY.getAddress())) {
            while (iterator.hasNext()) {
                key = iterator.next();
                credit = map.get(key);
                if (credit.signum() < 1)
                    continue;

                farmKeyValue = new Tuple2<>(ID, "farm1" + key.a);
                farmedValue = (BigDecimal) mapValues.get(farmKeyValue);
                if (farmedValue.signum() > 0) {
                    // not charged yet
                    continue;
                }

                if (key.b == 1048579L || key.b == 1048587) {
                    farmedValue = results.get(key.a);
                    if (farmedValue == null)
                        farmedValue = BigDecimal.ZERO;

                    farmedValue = farmedValue.add(BigDecimal.ONE);
                    results.put(key.a, farmedValue);
                }
            }
        } catch (IOException e) {
        }

        // SAVE RESULTS
        for (String address : results.keySet()) {
            farmKeyValue = new Tuple2<>(ID, "farm1" + address);
            mapValues.put(farmKeyValue, results.get(address));
        }


    }

    //////// PROCESSES
    public static void blockAction(DCSet dcSet, Block block, boolean asOrphan) {
        if (block.heightBlock % 100 == 0) {
            farm(dcSet, block, asOrphan);
        }
    }

    //////////////////// ADMIN PROCESS

    /**
     * Example of command: ["set price", {"1001": {"1": 0.1, "18":"0.01"}}]
     *
     * @param asOrphan
     */
    private void shopSetPrices(boolean asOrphan) {

        SmartContractValues map = dcSet.getSmartContractValues();
        if (asOrphan) {
            Object[] result = removeState(commandTx.getDBRef());
            if (result.length > 0) {
                for (Fun.Tuple3<Long, Long, BigDecimal> item : (Fun.Tuple3<Long, Long, BigDecimal>[]) result) {
                    map.put(priceKey(item.a, item.b), item.c);
                }
            }

        } else {

            Long shopAssetKey;
            Long priceAssetKey;
            JSONObject prices;
            BigDecimal price;

            if (pars == null) {
                fail("Wrong JSON params");
                return;
            } else if (pars.size() < 2) {
                fail("Wrong params size <2");
                return;
            } else {
                prices = (JSONObject) pars.get(1);
                for (Map.Entry<String, Object> item : (Set<Map.Entry<String, Object>>) prices.entrySet()) {
                    try {
                        long assetKey = Long.parseLong(item.getKey());
                        if (!dcSet.getItemAssetMap().contains(assetKey)) {
                            fail("Asset not exist for Key: " + item.getKey());
                            return;
                        }
                    } catch (Exception e) {
                        fail("Wrong assetKey: " + item.getKey());
                        return;
                    }
                    if (!(item.getValue() instanceof JSONObject)) {
                        fail("Not JSON: " + item.getValue().toString());
                        return;
                    }

                    for (Map.Entry<String, Object> priceItem : (Set<Map.Entry<String, Object>>) ((JSONObject) item.getValue()).entrySet()) {
                        try {
                            long assetKey = Long.parseLong(priceItem.getKey());
                            if (!dcSet.getItemAssetMap().contains(assetKey)) {
                                fail("Asset not exist for Key: " + priceItem.getKey());
                                return;
                            }
                        } catch (Exception e) {
                            fail("Wrong assetKey: " + priceItem.getKey());
                            return;
                        }

                        try {
                            new BigDecimal(priceItem.getValue().toString());
                        } catch (Exception e) {
                            fail("Wrong price value: " + priceItem.getValue());
                            return;
                        }

                    }
                }
            }

            List<Fun.Tuple3<Long, Long, BigDecimal>> oldPrices = new ArrayList();
            for (Map.Entry<String, Object> item : (Set<Map.Entry<String, Object>>) prices.entrySet()) {
                shopAssetKey = Long.parseLong(item.getKey());

                for (Map.Entry<String, Object> priceItem : (Set<Map.Entry<String, Object>>) ((JSONObject) item.getValue()).entrySet()) {
                    priceAssetKey = Long.parseLong(priceItem.getKey());

                    // OLD PRICE SAVE
                    price = (BigDecimal) map.get(priceKey(shopAssetKey, priceAssetKey));
                    if (price != null) {
                        oldPrices.add(new Fun.Tuple3<>(shopAssetKey, priceAssetKey, price));
                    }

                    // NEW PRICE
                    price = new BigDecimal(priceItem.getValue().toString());
                    map.put(priceKey(shopAssetKey, priceAssetKey), price);

                }
            }

            // store results for orphan
            putState(commandTx.getDBRef(), oldPrices.toArray());

            status = "done";
        }

    }

    private void adminWithdraw(Account admin, boolean asOrphan) {
        if (asOrphan) {
            // restore results for orphan
            List<Tuple2<Long, BigDecimal>> results = (List<Tuple2<Long, BigDecimal>>) removeState(commandTx.getDBRef())[0];

            for (Tuple2<Long, BigDecimal> row : results) {
                // RE-TRANSFER ASSET from ADMIN
                transfer(dcSet, null, commandTx, admin, stock, row.b, row.a, true, null, null);
            }

        } else {

            if (!dcSet.getSmartContractValues().contains(INIT_KEY)) {
                fail("not initated yet");
                return;
            }

            ItemAssetBalanceMap map = DCSet.getInstance().getAssetBalanceMap();
            try (IteratorCloseable<byte[]> iterator = map.getIteratorByAccount(stock)) {
                List<Tuple2<byte[], Fun.Tuple5<
                        Tuple2<BigDecimal, BigDecimal>, Tuple2<BigDecimal, BigDecimal>, Tuple2<BigDecimal, BigDecimal>,
                        Tuple2<BigDecimal, BigDecimal>, Tuple2<BigDecimal, BigDecimal>>>> assetsBalances
                        = map.getBalancesList(stock);
                byte[] key;
                long assetKey;
                List<Tuple2<Long, BigDecimal>> results = new ArrayList<>();
                while (iterator.hasNext()) {
                    key = iterator.next();

                    assetKey = ItemAssetBalanceMap.getAssetKeyFromKey(key);
                    if (assetKey == AssetCls.LIA_KEY) {
                        continue;
                    }

                    Fun.Tuple5<Tuple2<BigDecimal, BigDecimal>, Tuple2<BigDecimal, BigDecimal>, Tuple2<BigDecimal, BigDecimal>, Tuple2<BigDecimal, BigDecimal>, Tuple2<BigDecimal, BigDecimal>>
                            itemBals = map.get(key);

                    if (itemBals == null)
                        continue;

                    // TRANSFER ASSET to ADMIN
                    transfer(dcSet, block, commandTx, stock, admin, itemBals.a.b, assetKey, false, null, "adminWithdraw");

                    results.add(new Tuple2(assetKey, itemBals.a.b));

                }

                // store results for orphan
                putState(commandTx.getDBRef(), new Object[]{results});

                status = "done";

            } catch (IOException e) {
                fail(e.getMessage());
            }
        }

    }

    private void init(Account admin, boolean asOrphan) {

        /**
         * issue main currency
         */
        if (asOrphan) {
            // need to remove INIT_KEY - for reinit after orphans
            gravitaKey = (Long) dcSet.getSmartContractValues().remove(INIT_KEY);

            // orphan GRAVITA ASSET
            dcSet.getItemAssetMap().decrementDelete(gravitaKey);

        } else {

            if (dcSet.getSmartContractValues().contains(INIT_KEY)) {
                fail("already initated");
                return;
            }

            AssetVenture gravita = new AssetVenture(null, stock, "GR", null, null,
                    null, AS_INSIDE_ASSETS, 6, 0);
            gravita.setReference(commandTx.getSignature(), commandTx.getDBRef());

            //INSERT INTO DATABASE
            gravitaKey = dcSet.getItemAssetMap().incrementPut(gravita);
            dcSet.getSmartContractValues().put(INIT_KEY, gravitaKey);

            // TRANSFER GRAVITA to ADMIN
            BigDecimal amount = new BigDecimal("10000");
            transfer(dcSet, block, commandTx, stock, admin, amount, gravitaKey, false, null, "init");

            status = "done";
        }

    }

    /**
     * admin commands
     *
     * @param admin
     * @return
     */
    public void processAdminCommands(Account admin) {
        if ("init".equals(command)) {
            init(admin, false);
        } else if (command.startsWith("emite")) {
        } else if (command.startsWith(COMMAND_WITHDRAW)) {
            adminWithdraw(admin, false);
        } else if (COMMAND_SET_PRICE.equals(command)) {
            shopSetPrices(false);
        } else {
            fail("unknown command");
        }
    }

    @Override
    public void process() {

        if (!isValid())
            return;

        if (commandTx instanceof RSend) {
            RSend rsend = (RSend) commandTx;
            if (isAdminCommand(commandTx)) {
                processAdminCommands(
                        rsend.getCreator() // need for TEST - not adminAddress
                );
            } else {
                if (COMMAND_CATH_COMET.equals(command) || COMMAND_RANDOM.equals(command)
                        // это не проверка вне блока - в ней блока нет
                        && block != null) {
                    // рождение комет
                    dcSet.getTimeTXWaitMap().put(commandTx.getDBRef(), block.heightBlock + WAIT_RAND);
                    status = "wait";
                    return;
                } else if (COMMAND_BUY.equals(command)) {
                    shopBuy(false);
                } else if (COMMAND_STAKE.equals(command)) {
                    stakeAction(false);
                } else if (COMMAND_FARM.equals(command) || COMMAND_PICK_UP.equals(command)) {
                    farmAction(false);
                }
            }
            return;
        }

        fail("unknown command");

    }

    @Override
    public void processByTime() {

        if (COMMAND_CATH_COMET.equals(command)) {
            catchComets(false);
        } else if (COMMAND_RANDOM.equals(command)) {
            random(false);
        } else {
            fail("unknown command");
        }
    }

    public void orphanAdminCommands(Account admin) {
        if ("init".equals(command)) {
            init(admin, true);
        } else if (command.startsWith(COMMAND_WITHDRAW)) {
            adminWithdraw(admin, true);
        } else if (COMMAND_SET_PRICE.equals(command)) {
            shopSetPrices(true);
        }

    }

    @Override
    public void orphanBody() {

        super.orphanBody();

        if (isAdminCommand(commandTx)) {
            orphanAdminCommands(
                    commandTx.getCreator() // need for TEST - not adminAddress
            );
        }

        if (COMMAND_CATH_COMET.equals(command) || COMMAND_RANDOM.equals(command)) {
            // отмена рождения комет
            dcSet.getTimeTXWaitMap().remove(commandTx.getDBRef());
        } else if (COMMAND_BUY.equals(command)) {
            shopBuy(true);
        } else if (COMMAND_STAKE.equals(command)) {
            farmAction(true);
        }

    }

    @Override
    public void orphanByTime() {
        if (COMMAND_CATH_COMET.equals(command)) {
            catchComets(true);
        } else if (COMMAND_RANDOM.equals(command)) {
            random(true);
        }

    }

    private static String[][][] imgsStr;

    {
        imgsStr = new String[][][]{
                new String[][]{
                        new String[]{"1050868", WebResource.TYPE_IMAGE.toString()},
                        new String[]{"1050867", WebResource.TYPE_IMAGE.toString()},
                },
                new String[][]{
                        new String[]{"1050864", WebResource.TYPE_IMAGE.toString()},
                        new String[]{"1050862", WebResource.TYPE_IMAGE.toString()},
                        null,
                        new String[]{"1050863", WebResource.TYPE_IMAGE.toString()},
                },
                new String[][]{
                        new String[]{"1050860", WebResource.TYPE_IMAGE.toString()},
                },
                new String[][]{
                        null,
                        new String[]{"1050866", WebResource.TYPE_IMAGE.toString()},
                },
                new String[][]{
                        new String[]{"1050857", WebResource.TYPE_IMAGE.toString()},
                        new String[]{"1050859", WebResource.TYPE_IMAGE.toString()},
                        new String[]{"1050858", WebResource.TYPE_IMAGE.toString()},
                },
                new String[][]{
                        new String[]{"1050856", WebResource.TYPE_IMAGE.toString()},
                        new String[]{"1050855", WebResource.TYPE_IMAGE.toString()},
                        null,
                        new String[]{"1050854", WebResource.TYPE_IMAGE.toString()},
                },
                null,
                new String[][]{
                        null,
                        null,
                        new String[]{"1050852", WebResource.TYPE_IMAGE.toString()},
                        null,
                        new String[]{"1050851", WebResource.TYPE_IMAGE.toString()},
                },
        };
    }

    static int confirms = 10;
    static int deploy_period = 3;

    public static String getImageURL(AssetCls asset) {

        JSONArray arrayJson = new JSONArray();
        JSONObject item;


        int height = Transaction.parseHeightDBRef(asset.getDBref());

        if (contr.getMyHeight() < height + deploy_period + confirms) {
            item = new JSONObject();
            item.put("url", "/apiasset/image/1050869");
            item.put("type", WebResource.TYPE_IMAGE.toString());
            arrayJson.add(item);
            return arrayJson.toJSONString();
        }

        Block.BlockHead blockHead = DCSet.getInstance().getBlocksHeadsMap().get(height + deploy_period);

        byte[] hash = blockHead.signature;
        byte[] hash2 = Ints.toByteArray((int) asset.getKey());
        System.arraycopy(hash2, 0, hash, 0, hash2.length);

        hash = crypto.digest(hash);
        int slot = 0;
        int slotRare;
        int slotRareLvl;

        String[][] slotArray;
        do {
            slotRare = Ints.fromBytes((byte) 0, (byte) 0, hash[slot << 1], hash[(slot << 1) + 1]);
            if ((slotRare >> 11) == 0) {
                slotRareLvl = 5;
            } else if ((slotRare >> 12) == 0) {
                slotRareLvl = 4;
            } else if ((slotRare >> 13) == 0) {
                slotRareLvl = 3;
            } else if ((slotRare >> 14) == 0) {
                slotRareLvl = 2;
            } else if ((slotRare >> 15) == 0) {
                slotRareLvl = 1;
            } else {
                slotRareLvl = 0;
            }

            slotArray = imgsStr[slot];
            if (slotArray == null)
                continue;

            if (slotArray.length <= slotRareLvl) {
                slotRareLvl = slotArray.length - 1;
            }

            String[] itemArray;
            do {
                itemArray = slotArray[slotRareLvl];
            } while (itemArray == null && slotRareLvl-- > 0);

            if (itemArray == null)
                continue;

            item = new JSONObject();
            item.put("url", "/apiasset/image/" + itemArray[0]);
            item.put("type", itemArray[1]);
            arrayJson.add(item);

        } while (slot++ < 7);

        item = new JSONObject();
        item.put("url", "/apiasset/image/1050853");
        item.put("type", WebResource.TYPE_IMAGE.toString());
        arrayJson.add(item);
        item = new JSONObject();
        item.put("url", "/apiasset/image/1050865");
        item.put("type", WebResource.TYPE_IMAGE.toString());
        arrayJson.add(item);

        return arrayJson.toJSONString();

    }

    static DecimalFormat format2 = new DecimalFormat("#.##");

    public static String viewDescription(AssetCls asset, String description) {
        int released = asset.getReleased(DCSet.getInstance()).intValue();
        double rary = Math.sqrt(1.0d / released);
        return "<html>RARY: <b>" + format2.format(rary) + "</b><br>" + description + "</html>";
    }

}
