package test;
// 30/03
import static org.junit.Assert.assertEquals;

import java.io.File;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.junit.Test;
import org.mapdb.DB;
import org.mapdb.DBMaker;
import org.mapdb.Fun.Tuple2;

import core.BlockChain;
import core.account.Account;
import core.block.Block;
import core.block.GenesisBlock;
import core.blockexplorer.BlockExplorer;
import core.blockexplorer.BlockExplorer.Stopwatch;
import core.crypto.Base58;
import core.item.assets.AssetVenture;
import core.transaction.Transaction;
import datachain.DCSet;
import settings.Settings;
import utils.Pair;


public class BlockExplorerTest {


	static Logger LOGGER = Logger.getLogger(BlockExplorerTest.class.getName());

	private byte[] icon = new byte[]{1,3,4,5,6,9}; // default value
	private byte[] image = new byte[]{4,11,32,23,45,122,11,-45}; // default value

	public void maxBalance()
	{
		byte[] amountBytes = new byte[]{127, 127, 127, 127, 127, 127, 127, 127};
		BigDecimal amount = new BigDecimal(new BigInteger(amountBytes), BlockChain.AMOUNT_DEDAULT_SCALE);
		LOGGER.error(amount.toPlainString());
		amountBytes = new byte[]{-128, -128, -128, -128, -128, -128, -128, -128};
		amount = new BigDecimal(new BigInteger(amountBytes), BlockChain.AMOUNT_DEDAULT_SCALE);
		LOGGER.error(amount.toPlainString());
	}

	public static DCSet createRealEmptyDatabaseSet() {
		//OPEN DB
		File dbFile = new File(Settings.getInstance().getDataDir(), "data2.dat");
		dbFile.getParentFile().mkdirs();

		//CREATE DATABASE
		DB database = DBMaker.newFileDB(dbFile)
				.closeOnJvmShutdown()
				.cacheSize(2048)
				.checksumEnable()
				.mmapFileEnableIfSupported()
				.make();

		//CREATE INSTANCE
		return new DCSet(database, false, false);
	}

	public void minBalance()
	{

		Block block = new GenesisBlock();

		DCSet databaseSet = createRealEmptyDatabaseSet();

		List<Pair<Block, BigDecimal>> balancesBlocks =  new ArrayList<>();

		Stopwatch stopwatchAll = new Stopwatch();

		//ADD ERM ASSET
		AssetVenture ermAsset = new AssetVenture(block.getCreator(), "ERM", icon, image, ".",
				8, 0, 100000000l);
		databaseSet.getIssueAssetMap().set(block.getSignature(), 0l);
		databaseSet.getItemAssetMap().set(0l, ermAsset);

		do {

			try {
				block.process(databaseSet);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			if ( block.getHeight(DCSet.getInstance())%2000 == 0 )
			{
				LOGGER.error(block.getHeight(DCSet.getInstance()));
			}

			balancesBlocks.add(new Pair<>(block, block.getCreator().getBalance(databaseSet, Transaction.FEE_KEY).a.b));

			block = block.getChild(DCSet.getInstance());

		} while (block != null);

		LOGGER.error(stopwatchAll.elapsedTime()/1000 + " secs");

		Collections.sort(balancesBlocks, new BalancesBlocksComparator());

		for (int i = 0; i < 400; i++) {
			System.out.print(
					Base58.encode(balancesBlocks.get(i).getA().getSignature())
					);
			System.out.print( " " +
					balancesBlocks.get(i).getA().getCreator().getAddress());
			LOGGER.error(" " + balancesBlocks.get(i).getB().toPlainString());
		}


	}

	public class BalancesBlocksComparator implements Comparator<Pair<Block, BigDecimal>> {

		@Override
		public int compare(Pair<Block, BigDecimal> one, Pair<Block, BigDecimal> two)
		{
			return one.getB().compareTo(two.getB());
		}
	}

	@Test
	@SuppressWarnings({"rawtypes", "unchecked"})
	public void blockExplorer() throws Exception {

		DCSet.getInstance();

		ArrayList<String> addrs = new ArrayList();
		addrs.add("QXncuwPehVZ21ymE1jawXg1Uv3sZZ4TvYk");
		addrs.add("QRZ5Ggk6o5wwEgzL4Wo3xmueXuDEgwLeyQ");
		addrs.add("QYsLsfwMRBPnunmuWmFkM4hvGsfooY8ssU");
		addrs.add("QQPsGx3khgEboJXWPiDBMVDG5ngu9wDo3k");
		addrs.add("Qd9jQKZSXoYgFypTQySJUSbXcZvjgdiemn");
		addrs.add("QfyocFSGghfpANqUmQFpoG2sk5TVg8LvEm");
		addrs.add("QMu6HXfZCnwaNmyFjjhWTYAUW7k1x7PoVr");
		addrs.add("QdrhixdevE7ZJqSHAfV19yVYrYsys8VLgz");
		addrs.add("QPVknSmwDryB98Hh8xB7E6U75dGFYwNkJ4");

		/*
		Cancel Order:
		Payment:
		Name Registration:
		Name Update:
		Name Sale:
		Cancel Name	Sale:
		Name Purchase:
		Poll Creation:
		Arbitrary Transaction:
		Asset Transfer:
		Poll Vote:
		Asset Issue:
		Order Creation:
		Multi Payment:
		Message:
		Deploy AT:
		Genesis:
		//+Trades:
		//Generated blocks:
		//AT Transactions:

		17 tx type
		 */

		int start = -1;
		int txOnPage = 10;
		String filter = "standart";
		boolean allOnOnePage = false;
		String showOnly = "";
		String showWithout = "";

		DCSet.getInstance();

		for(int i = 0; i < addrs.size(); i++) {

			String addr = addrs.get(i);
			List<String> listaddr = new ArrayList<>();
			listaddr.add(addr);

			Map<Object, Map> output = BlockExplorer.getInstance().jsonQueryAddress(listaddr, 1, start, txOnPage, filter, allOnOnePage, showOnly, showWithout);

			Map<Long, String> totalBalance = (Map<Long, String>) output.get("balance").get("total");

			Account account = new Account(addr);

			LOGGER.error(addr);
			for(Map.Entry<Long, String> e : totalBalance.entrySet())
			{
				Long key = e.getKey();

				BigDecimal blockExplorerBalance =  new BigDecimal(e.getValue());

				System.out.print("(" + key + ") " + " BlockExplorerBalance: " + blockExplorerBalance);

				BigDecimal nativeBalance = account.getBalanceUSE(key);

				System.out.print("; NantiveBalance: " + nativeBalance);

				if(blockExplorerBalance.equals(nativeBalance))
				{
					LOGGER.error(" OK.");
				}
				else
				{
					LOGGER.error(" Fail!!!");
				}

				assertEquals(blockExplorerBalance, nativeBalance);
			}
		}

		DCSet.getInstance().close();
	}

	public void getTransactionsByAddress() {

		DCSet.getInstance().getTransactionFinalMap().contains(new Tuple2<Integer, Integer>(1, 1));

		Stopwatch stopwatchAll = new Stopwatch();

		List<Object> all = new ArrayList<Object>();

		all.addAll(DCSet.getInstance().getTransactionFinalMap().getTransactionsByAddress("QPVknSmwDryB98Hh8xB7E6U75dGFYwNkJ4"));

		LOGGER.error("getTransactionsByAddress QPVknSmwDryB98Hh8xB7E6U75dGFYwNkJ4. " + all.size() + " " + stopwatchAll.elapsedTime());

		all.clear();
		stopwatchAll = new Stopwatch();

		all.addAll(DCSet.getInstance().getTransactionFinalMap().getTransactionsByAddress("QYsLsfwMRBPnunmuWmFkM4hvGsfooY8ssU"));

		LOGGER.error("getTransactionsByAddress QYsLsfwMRBPnunmuWmFkM4hvGsfooY8ssU. " + all.size() + " " + stopwatchAll.elapsedTime());

		all.clear();

	}

	public void getTransactionsByTypeAndAddress() {

		DCSet.getInstance().getTransactionFinalMap().contains(new Tuple2<Integer, Integer>(1, 1));

		Stopwatch stopwatchAll = new Stopwatch();

		List<Object> all = new ArrayList<Object>();

		List<Transaction> transactions = new ArrayList<Transaction>();
		for (int type = 1; type <= 23; type++) {  // 17 - The number of transaction types. 23 - for the future
			transactions.addAll(DCSet.getInstance().getTransactionFinalMap().getTransactionsByTypeAndAddress("QPVknSmwDryB98Hh8xB7E6U75dGFYwNkJ4", type, 0));
		}

		Map<String, Boolean> signatures = new LinkedHashMap<String, Boolean>();

		for (Transaction transaction : transactions){
			byte[] signature = transaction.getSignature();
			if(!signatures.containsKey( new String(signature) ))
			{
				signatures.put(new String(signature), true);
				all.add(transaction);
			}
		}

		LOGGER.error("getTransactionsByTypeAndAddress QPVknSmwDryB98Hh8xB7E6U75dGFYwNkJ4. " + all.size() + " " + stopwatchAll.elapsedTime());

		all.clear();
		stopwatchAll = new Stopwatch();

		transactions = new ArrayList<Transaction>();
		for (int type = 1; type <= 23; type++) {  // 17 - The number of transaction types. 23 - for the future
			transactions.addAll(DCSet.getInstance().getTransactionFinalMap().getTransactionsByTypeAndAddress("QYsLsfwMRBPnunmuWmFkM4hvGsfooY8ssU", type, 0));
		}

		signatures = new LinkedHashMap<String, Boolean>();

		for (Transaction transaction : transactions){
			byte[] signature = transaction.getSignature();
			if(!signatures.containsKey( new String(signature) ))
			{
				signatures.put(new String(signature), true);
				all.add(transaction);
			}
		}

		LOGGER.error("getTransactionsByTypeAndAddress QYsLsfwMRBPnunmuWmFkM4hvGsfooY8ssU. " + all.size() + " " + stopwatchAll.elapsedTime());

		all.clear();


		stopwatchAll = new Stopwatch();

		transactions = new ArrayList<Transaction>();
		for (int type = 1; type <= 23; type++) {  // 17 - The number of transaction types. 23 - for the future
			transactions.addAll(DCSet.getInstance().getTransactionFinalMap().getTransactionsByTypeAndAddress("QRZ5Ggk6o5wwEgzL4Wo3xmueXuDEgwLeyQ", type, 0));
		}

		for (Transaction transaction : transactions) {
			LOGGER.error(Base58.encode(transaction.getSignature()));
		}

		LOGGER.error("getTransactionsByTypeAndAddress QRZ5Ggk6o5wwEgzL4Wo3xmueXuDEgwLeyQ. " + transactions.size() + " " + stopwatchAll.elapsedTime());

		all.clear();

		stopwatchAll = new Stopwatch();

		all.addAll(DCSet.getInstance().getTransactionFinalMap().getTransactionsByAddress("QRZ5Ggk6o5wwEgzL4Wo3xmueXuDEgwLeyQ"));

		LOGGER.error("getTransactionsByAddress QRZ5Ggk6o5wwEgzL4Wo3xmueXuDEgwLeyQ. " + all.size() + " " + stopwatchAll.elapsedTime());

		stopwatchAll = new Stopwatch();
		all.clear();
		all.addAll(DCSet.getInstance().getTransactionFinalMap().getTransactionsBySender("QRZ5Ggk6o5wwEgzL4Wo3xmueXuDEgwLeyQ"));

		for (Object transaction : all) {
			LOGGER.error(Base58.encode(((Transaction)transaction).getSignature()));
		}

		LOGGER.error("getTransactionsByAddress QRZ5Ggk6o5wwEgzL4Wo3xmueXuDEgwLeyQ. " + all.size() + " " + stopwatchAll.elapsedTime());

		all.clear();

		all.addAll(DCSet.getInstance().getTransactionFinalMap().getTransactionsByRecipient("QRZ5Ggk6o5wwEgzL4Wo3xmueXuDEgwLeyQ"));

		for (Object transaction : all) {
			LOGGER.error(Base58.encode(((Transaction)transaction).getSignature()));
		}

		LOGGER.error("getTransactionsByAddress QRZ5Ggk6o5wwEgzL4Wo3xmueXuDEgwLeyQ. " + all.size() + " " + stopwatchAll.elapsedTime());

	}

	public void txItSelf() throws Exception {

		Transaction transaction = getTransaction(Base58.decode("4JXPXqdP7GT743AoX2m8vHBeWNrKvBcf71TcDLfLeMn6rmV5uyVRDcV5gLspNquZyatY4tHB9RXDWKahEM85oTJv"));
		Account account = new Account("QRZ5Ggk6o5wwEgzL4Wo3xmueXuDEgwLeyQ");
		LOGGER.error(transaction.getAmount(account));

		transaction = getTransaction(Base58.decode("4JXPXqdP7GT743AoX2m8vHBeWNrKvBcf71TcDLfLeMn6rmV5uyVRDcV5gLspNquZyatY4tHB9RXDWKahEM85oTJv"));
		account = new Account("QRZ5Ggk6o5wwEgzL4Wo3xmueXuDEgwLeyQ");
		LOGGER.error(transaction.getAmount(account));
	}

	public Transaction getTransaction(byte[] signature) {

		return getTransaction(signature, DCSet.getInstance());
	}

	public Transaction getTransaction(byte[] signature, DCSet database) {

		// CHECK IF IN BLOCK
		Tuple2<Integer, Integer> tuple_Tx = database.getTransactionFinalMapSigns().get(signature);
		if (tuple_Tx != null)	{
			return database.getTransactionFinalMap().get(tuple_Tx);

		}

		// CHECK IF IN TRANSACTION DATABASE
		return DCSet.getInstance().getTransactionMap().get(signature);
	}
}
