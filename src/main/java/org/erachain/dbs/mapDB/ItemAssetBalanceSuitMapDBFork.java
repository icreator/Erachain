package org.erachain.dbs.mapDB;

import com.google.common.primitives.Longs;
import lombok.extern.slf4j.Slf4j;
import org.erachain.controller.Controller;
import org.erachain.core.BlockChain;
import org.erachain.core.account.Account;
import org.erachain.core.crypto.Crypto;
import org.erachain.database.DBASet;
import org.erachain.datachain.ItemAssetBalanceMap;
import org.erachain.datachain.ItemAssetBalanceSuit;
import org.erachain.dbs.DBTab;
import org.erachain.dbs.IteratorCloseable;
import org.erachain.dbs.IteratorCloseableImpl;
import org.mapdb.*;
import org.mapdb.Fun.Tuple2;
import org.mapdb.Fun.Tuple5;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.List;

// TODO SOFT HARD TRUE

@Slf4j
public class ItemAssetBalanceSuitMapDBFork extends DBMapSuitFork<byte[], Tuple5<
        Tuple2<BigDecimal, BigDecimal>, Tuple2<BigDecimal, BigDecimal>, Tuple2<BigDecimal, BigDecimal>,
        Tuple2<BigDecimal, BigDecimal>, Tuple2<BigDecimal, BigDecimal>>>
        implements ItemAssetBalanceSuit {

    protected BTreeMap assetKeyMap;

    public ItemAssetBalanceSuitMapDBFork(ItemAssetBalanceMap parent, DBASet databaseSet, DBTab cover) {
        super(parent, databaseSet, logger, false, cover);
    }

    @SuppressWarnings({"unchecked"})
    @Override
    public void openMap() {

        //OPEN MAP
        BTreeMap<byte[], Tuple5<
                Tuple2<BigDecimal, BigDecimal>, Tuple2<BigDecimal, BigDecimal>, Tuple2<BigDecimal, BigDecimal>,
                Tuple2<BigDecimal, BigDecimal>, Tuple2<BigDecimal, BigDecimal>>> treeMap;
        HTreeMap<byte[], Tuple5<
                Tuple2<BigDecimal, BigDecimal>, Tuple2<BigDecimal, BigDecimal>, Tuple2<BigDecimal, BigDecimal>,
                Tuple2<BigDecimal, BigDecimal>, Tuple2<BigDecimal, BigDecimal>>> hashMap;

        if (true) {
            hashMap = database.createHashMap("balances")
                    .keySerializer(SerializerBase.BYTE_ARRAY)
                    .hasher(Hasher.BYTE_ARRAY)
                    .makeOrGet();
            map = hashMap;
        } else {

            treeMap = database.createTreeMap("balances")
                    //.keySerializer(BTreeKeySerializer.TUPLE2)
                    .keySerializer(BTreeKeySerializer.BASIC)
                    //.keySerializer(new BTreeKeySerializer.Tuple2KeySerializer(
                    //        UnsignedBytes.lexicographicalComparator(), // Fun.BYTE_ARRAY_COMPARATOR,
                    //        Serializer.BYTE_ARRAY,
                    //        Serializer.LONG))
                    //.comparator(Fun.TUPLE2_COMPARATOR)
                    .comparator(Fun.BYTE_ARRAY_COMPARATOR)
                    //.comparator(UnsignedBytes.lexicographicalComparator())
                    .makeOrGet();
            map = treeMap;
        }

        if (BlockChain.HOLD_ROYALTY_PERIOD_DAYS > 0 || !Controller.getInstance().onlyProtocolIndexing) {
            // TODO сделать потом отдельную таблицу только для заданного Актива - для ускорения
            // если включены выплаты - то нужно этот индекс тоже делать - хотя можно отдельно по одному Активу только - нужному

            //BIND ASSET KEY
            /// так как основной Индекс не сравниваемы - byte[] то во Вторичном индексе делаем Строку
            // - иначе она не сработает так как тут дерево с поиском
            this.assetKeyMap = database.createTreeMap("balances_key_asset_bal_address")
                    .comparator(Fun.COMPARATOR)
                    //.valuesOutsideNodesEnable()
                    .makeOrGet();

            Bind.secondaryKey(hashMap, this.assetKeyMap, new Fun.Function2<Tuple2<Tuple2<Long, BigDecimal>, String>,
                    byte[],
                    Tuple5<
                            Tuple2<BigDecimal, BigDecimal>, Tuple2<BigDecimal, BigDecimal>, Tuple2<BigDecimal, BigDecimal>,
                            Tuple2<BigDecimal, BigDecimal>, Tuple2<BigDecimal, BigDecimal>>>
                    () {
                @Override
                public Tuple2<Tuple2<Long, BigDecimal>, String>
                run(byte[] key, Tuple5<
                        Tuple2<BigDecimal, BigDecimal>, Tuple2<BigDecimal, BigDecimal>, Tuple2<BigDecimal, BigDecimal>,
                        Tuple2<BigDecimal, BigDecimal>, Tuple2<BigDecimal, BigDecimal>> value) {

                    // Address
                    byte[] shortAddress = new byte[20];
                    System.arraycopy(key, 0, shortAddress, 0, 20);
                    // ASSET KEY
                    byte[] assetKeyBytes = new byte[8];
                    System.arraycopy(key, 20, assetKeyBytes, 0, 8);

                    return new Tuple2<Tuple2<Long, BigDecimal>, String>(
                            new Tuple2<>(Longs.fromByteArray(assetKeyBytes), value.a.b.negate()),
                            Crypto.getInstance().getAddressFromShort(shortAddress)
                    );
                }
            });

        }

    }

    @Override
    public Collection<byte[]> assetKeys(long assetKey) {
        //FILTER ALL KEYS
        return this.assetKeyMap == null ? null : this.assetKeyMap.subMap(
                Fun.t2(Fun.t2(assetKey, null), null),
                Fun.t2(Fun.t2(assetKey, Fun.HI()), Fun.HI())).values();
    }

    @Override
    public IteratorCloseable<byte[]> assetIterator(long assetKey) {
        return this.assetKeyMap == null ? null : new IteratorCloseableImpl(assetKeys(assetKey).iterator());
    }

    @Override
    // NOT used in FORK
    public IteratorCloseable<byte[]> accountIterator(Account account) {
        return null;
    }

    @Override
    // NOT used in FORK
    public List<byte[]> accountKeys(Account account) {
        return null;
    }

}
