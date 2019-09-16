package org.erachain.datachain;

import com.google.common.collect.Iterables;
import com.google.common.collect.Sets;
import com.google.common.primitives.Longs;
import org.erachain.controller.Controller;
import org.erachain.core.BlockChain;
import org.erachain.core.account.Account;
import org.erachain.core.transaction.Transaction;
import org.erachain.dbs.DBTab;
import org.erachain.dbs.DBTabImpl;
import org.erachain.dbs.mapDB.TransactionSuitMapDB;
import org.erachain.dbs.nativeMemMap.nativeMapTreeMapFork;
import org.erachain.dbs.rocksDB.TransactionSuitRocksDB;
import org.erachain.utils.ObserverMessage;
import org.mapdb.DB;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

/**
 * Храним неподтвержденные транзакции - memory pool for unconfirmed transaction.
 * Signature (as Long) -> Transaction
 * <hr>
 * Здесь вторичные индексы создаются по несколько для одной записи путем создания массива ключей,
 * см. typeKey и recipientKey. Они используются для API RPC block explorer.
 * Нужно огрничивать размер выдаваемого списка чтобы не перегружать ноду.
 * <br>
 * Так же вторичный индекс по времени, который используется в ГУИ TIMESTAMP_INDEX = 0 (default)
 * - он оргнизыется внутри DCMap в списке индексов для сортировок в ГУИ
 *
 * Также хранит инфо каким пирам мы уже разослали транзакцию неподтвержденную так что бы при подключении делать автоматически broadcast
 *
 *  <hr>
 *  (!!!) для создания уникальных ключей НЕ нужно добавлять + val.viewTimestamp(), и так работант, а почему в Ордерах не работало?
 *  <br>в БИНДЕ внутри уникальные ключи создаются добавлением основного ключа
 */
class TransactionTabImpl extends DBTabImpl<Long, Transaction>
        implements TransactionTab
{

    static Logger logger = LoggerFactory.getLogger(TransactionTabImpl.class.getName());

    //public int TIMESTAMP_INDEX = 1;

    public int totalDeleted = 0;

    public TransactionTabImpl(DCSet databaseSet, DB database) {
        super(databaseSet, database);

        DEFAULT_INDEX = TransactionSuit.TIMESTAMP_INDEX;

        if (databaseSet.isWithObserver()) {
            this.observableData.put(DBTab.NOTIFY_RESET, ObserverMessage.RESET_UNC_TRANSACTION_TYPE);
            this.observableData.put(DBTab.NOTIFY_LIST, ObserverMessage.LIST_UNC_TRANSACTION_TYPE);
            this.observableData.put(DBTab.NOTIFY_ADD, ObserverMessage.ADD_UNC_TRANSACTION_TYPE);
            this.observableData.put(DBTab.NOTIFY_REMOVE, ObserverMessage.REMOVE_UNC_TRANSACTION_TYPE);
        }

    }

    public TransactionTabImpl(TransactionTab parent, DCSet databaseSet) {
        super(parent, databaseSet);
    }

    // TODO вставить настройки выбора СУБД
    @Override
    protected void getMap()
    {
        if (parent == null) {
            String dbs = "MapDB";
            if (dbs.equals("MapDB")) {
                map = new TransactionSuitMapDB(databaseSet, database);
            } else if (dbs.equals("RocksDB")) {
                map = new TransactionSuitRocksDB(databaseSet, database);
            } else {
                map = new TransactionSuitMapDB(databaseSet, database);
            }
        } else {
            String dbs = "mem";
            if (dbs.equals("MapDB")) {
                ; //map = new TransactionSuitMapDB((TransactionTab)parent, databaseSet);
            } else if (dbs.equals("RocksDB")) {
                ; //map = new TransactionSuitRocksDB(databaseSet, database);
            } else {
                map = new nativeMapTreeMapFork(parent, databaseSet, TransactionSuit.DEFAULT_VALUE);
            }

        }
    }

    /**
     * Используется для получения транзакций для сборки блока
     * Поидее нужно братьв се что есть без учета времени протухания для сборки блока своего
     * @param timestamp
     * @param notSetDCSet
     * @param cutDeadTime true is need filter by Dead Time
     * @return
     */
    public List<Transaction> getSubSet(long timestamp, boolean notSetDCSet, boolean cutDeadTime) {

        List<Transaction> values = new ArrayList<Transaction>();
        Iterator<Long> iterator = this.getIterator(TransactionSuit.TIMESTAMP_INDEX, false);
        Transaction transaction;
        int count = 0;
        int bytesTotal = 0;
        Long key;
        while (iterator.hasNext()) {
            key = iterator.next();
            transaction = this.map.get(key);

            if (cutDeadTime && transaction.getDeadline() < timestamp)
                continue;
            if (transaction.getTimestamp() > timestamp)
                // мы используем отсортированный индекс, поэтому можно обрывать
                break;

            if (++count > BlockChain.MAX_BLOCK_SIZE_GEN)
                break;

            bytesTotal += transaction.getDataLength(Transaction.FOR_NETWORK, true);
            if (bytesTotal > BlockChain.MAX_BLOCK_SIZE_BYTES_GEN
                ///+ (BlockChain.MAX_BLOCK_SIZE_BYTE >> 3)
            ) {
                break;
            }

            if (!notSetDCSet)
                transaction.setDC((DCSet)databaseSet);

            values.add(transaction);

        }

        return values;
    }

    public void setTotalDeleted(int value) { totalDeleted = value; }
    public int getTotalDeleted() { return totalDeleted; }

    private static long MAX_DEADTIME = 1000 * 60 * 60 * 1;

    private boolean clearProcessed = false;
    private synchronized boolean isClearProcessedAndSet() {

        if (clearProcessed)
            return true;

        clearProcessed = true;

        return false;
    }

    /**
     * очищает  только по признаку протухания и ограничения на размер списка - без учета валидности
     * С учетом валидности очистка идет в Генераторе после каждого запоминания блока
     * @param timestamp
     * @param cutDeadTime
     */
    protected long pointClear;
    public void clearByDeadTimeAndLimit(long timestamp, boolean cutDeadTime) {

        // займем просецц или установим флаг
        if (isClearProcessedAndSet())
            return;

        long keepTime = BlockChain.VERS_30SEC_TIME < timestamp? 600000 : 240000;
        try {
            long realTime = System.currentTimeMillis();

            if (realTime - pointClear < keepTime) {
                return;
            }

            int count = 0;
            long tickerIter = realTime;

            timestamp -= (keepTime >> 1) + (keepTime << (5 - Controller.HARD_WORK >> 1));

            /**
             * по несколько секунд итератор берется - при том что таблица пустая -
             * - дале COMPACT не помогает
             */
            //Iterator<Long> iterator = this.getIterator(TIMESTAMP_INDEX, false);
            //Iterator<Tuple2<?, Long>> iterator = map.getIterator(TIMESTAMP_INDEX, false);
            Iterator<Long> iterator = ((TransactionSuit)map).getTimestampIterator();
            tickerIter = System.currentTimeMillis() - tickerIter;
            if (tickerIter > 10) {
                LOGGER.debug("TAKE ITERATOR: " + tickerIter + " ms");
            }

            Transaction transaction;

            tickerIter = System.currentTimeMillis();
            long size = this.size();
            tickerIter = System.currentTimeMillis() - tickerIter;
            if (tickerIter > 10) {
                LOGGER.debug("TAKE ITERATOR.SIZE: " + tickerIter + " ms");
            }
            while (iterator.hasNext()) {
                Long key = iterator.next();
                transaction = this.map.get(key);
                if (transaction == null) {
                    // такая ошибка уже было
                    break;
                }

                long deadline = transaction.getDeadline();
                if (realTime - deadline > 86400000 // позде на день удаляем в любом случае
                        || ((Controller.HARD_WORK > 3
                        || cutDeadTime)
                        && deadline < timestamp)
                        || Controller.HARD_WORK <= 3
                        && deadline + MAX_DEADTIME < timestamp // через сутки удалять в любом случае
                        || size - count > BlockChain.MAX_UNCONFIGMED_MAP_SIZE) {
                    this.remove(key);
                    count++;
                } else {
                    break;
                }
            }

            long ticker = System.currentTimeMillis() - realTime;
            if (ticker > 1000 || count > 0) {
                LOGGER.debug("------ CLEAR DEAD UTXs: " + ticker + " ms, for deleted: " + count);
            }

        } finally {
            // освободим процесс
            pointClear = System.currentTimeMillis();
            clearProcessed = false;
        }
    }

    public boolean set(byte[] signature, Transaction transaction) {

        Long key = Longs.fromByteArray(signature);

        return this.set(key, transaction);

    }

    public boolean add(Transaction transaction) {

        return this.set(transaction.getSignature(), transaction);

    }

    public void delete(Transaction transaction) {
        this.delete(transaction.getSignature());
    }

    public Transaction delete(byte[] signature) {
        return this.remove(Longs.fromByteArray(signature));
    }


    /**
     * synchronized - потому что почемуто вызывало ошибку в unconfirmedMap.delete(transactionSignature) в процессе блока.
     * Head Zero - data corrupted
     * @param key
     * @return
     */
    public /* synchronized */ Transaction remove(Long key) {
        Transaction transaction = super.remove(key);
        if (transaction != null) {
            // DELETE only if DELETED
            totalDeleted++;
        }

        return transaction;

    }

    public boolean contains(byte[] signature) {
        return this.contains(Longs.fromByteArray(signature));
    }

    public boolean contains(Transaction transaction) {
        return this.contains(transaction.getSignature());
    }

    public Transaction get(byte[] signature) {
        return this.get(Longs.fromByteArray(signature));
    }


    public Collection<Long> getFromToKeys(long fromKey, long toKey) {

        return ((TransactionSuit)map).getFromToKeys(fromKey, toKey);

    }

    @Override
    public Iterator<Long> getTimestampIterator() {
        return ((TransactionSuit)map).getTimestampIterator();
    }

    /**
     * Find all unconfirmed transaction by address, sender or recipient.
     * Need set only one parameter(address, sender,recipient)
     *
     * @param address   - address
     * @param sender    - sender
     * @param recipient - recipient
     * @param type      - type transaction
     * @param desc      - order by transaction
     * @param offset    -
     * @param limit     - count transaction
     * @return Key transactions
     */
    @SuppressWarnings({"rawtypes", "unchecked"})

    public Iterable findTransactionsKeys(String address, String sender, String recipient,
                                         int type, boolean desc, int offset, int limit, long timestamp) {
        Iterable senderKeys = null;
        Iterable recipientKeys = null;
        TreeSet<Object> treeKeys = new TreeSet<>();

        if (address != null) {
            sender = address;
            recipient = address;
        }

        if (sender == null && recipient == null) {
            return treeKeys;
        }
        //  timestamp = null;
        if (sender != null) {
            if (type > 0) {
                senderKeys = ((TransactionSuit)map).typeKeys(sender, timestamp, type);
            } else {
                senderKeys = ((TransactionSuit)map).senderKeys(sender);
            }
        }

        if (recipient != null) {
            if (type > 0) {
                //recipientKeys = Fun.filter(this.typeKey, new Fun.Tuple3<String, Long, Integer>(recipient, timestamp, type));
                recipientKeys = ((TransactionSuit)map).typeKeys(recipient, timestamp, type);
            } else {
                //recipientKeys = Fun.filter(this.recipientKey, recipient);
                recipientKeys = ((TransactionSuit)map).recipientKeys(recipient);
            }
        }

        if (address != null) {
            treeKeys.addAll(Sets.newTreeSet(senderKeys));
            treeKeys.addAll(Sets.newTreeSet(recipientKeys));
        } else if (sender != null && recipient != null) {
            treeKeys.addAll(Sets.newTreeSet(senderKeys));
            treeKeys.retainAll(Sets.newTreeSet(recipientKeys));
        } else if (sender != null) {
            treeKeys.addAll(Sets.newTreeSet(senderKeys));
        } else if (recipient != null) {
            treeKeys.addAll(Sets.newTreeSet(recipientKeys));
        }

        Iterable keys;
        if (desc) {
            keys = ((TreeSet) treeKeys).descendingSet();
        } else {
            keys = treeKeys;
        }

        if (offset > 0) {
            keys = Iterables.skip(keys, offset);
        }

        if (limit > 0) {
            keys = Iterables.limit(keys, limit);
        }

        return  keys;
    }

    public List<Transaction> findTransactions(String address, String sender, String recipient,
                                              int type, boolean desc, int offset, int limit, long timestamp) {

        Iterable keys = findTransactionsKeys(address, sender, recipient,
                type, desc, offset, limit, timestamp);
        return getUnconfirmedTransaction(keys);

    }


    public List<Transaction> getUnconfirmedTransaction(Iterable keys) {
        Iterator iter = keys.iterator();
        List<Transaction> transactions = new ArrayList<>();
        Transaction item;
        Long key;

        while (iter.hasNext()) {
            key = (Long) iter.next();
            item = this.map.get(key);
            transactions.add(item);
        }
        return transactions;
    }

    // TODO выдает ошибку на шаге treeKeys.addAll(Sets.newTreeSet(senderKeys));
    public List<Transaction> getTransactionsByAddressFast100(String address) {

        Iterable senderKeys = null;
        Iterable recipientKeys = null;
        TreeSet<Object> treeKeys = new TreeSet<>();

        senderKeys = Iterables.limit(((TransactionSuit)map).senderKeys(address), 100);
        recipientKeys = Iterables.limit(((TransactionSuit)map).recipientKeys(address), 100);

        treeKeys.addAll(Sets.newTreeSet(senderKeys));
        treeKeys.addAll(Sets.newTreeSet(recipientKeys));

        return getUnconfirmedTransaction(Iterables.limit(treeKeys, 100));

    }

    // slow?? without index
    public List<Transaction> getTransactionsByAddress(String address) {

        ArrayList<Transaction> values = new ArrayList<Transaction>();
        Iterator<Long> iterator = ((TransactionSuit)map).getTimestampIterator();
        Account account = new Account(address);

        Transaction transaction;
        boolean ok = false;

        int i = 0;
        while (iterator.hasNext()) {

            transaction = map.get(iterator.next());
            if (transaction.getCreator().equals(address))
                ok = true;
            else
                ok = false;

            if (!ok) {
                transaction.setDC((DCSet)databaseSet);
                HashSet<Account> recipients = transaction.getRecipientAccounts();

                if (recipients == null || recipients.isEmpty() || !recipients.contains(account)) {
                    continue;
                }

            }

            // SET LIMIT
            if (++i > 100)
                break;

            values.add(transaction);

        }
        return values;
    }

    public List<Transaction> getTransactions(int count, boolean descending) {

        ArrayList<Transaction> values = new ArrayList<Transaction>();

        //LOGGER.debug("get ITERATOR");
        Iterator<Long> iterator = this.getIterator(TransactionSuit.TIMESTAMP_INDEX, descending);
        //LOGGER.debug("get ITERATOR - DONE"); / for merge

        Transaction transaction;
        for (int i = 0; i < count; i++) {
            if (!iterator.hasNext())
                break;

            transaction = this.get(iterator.next());
            transaction.setDC((DCSet)databaseSet);
            values.add(transaction);
        }
        return values;
    }

    public List<Transaction> getIncomedTransactions(String address, int type, long timestamp, int count, boolean descending) {

        ArrayList<Transaction> values = new ArrayList<>();
        Iterator<Long> iterator = this.getIterator(TransactionSuit.TIMESTAMP_INDEX, descending);
        Account account = new Account(address);

        int i = 0;
        Transaction transaction;
        while (iterator.hasNext()) {
            transaction = map.get(iterator.next());
            if (type != 0 && type != transaction.getType())
                continue;

            transaction.setDC((DCSet)databaseSet);
            HashSet<Account> recipients = transaction.getRecipientAccounts();
            if (recipients == null || recipients.isEmpty())
                continue;
            if (recipients.contains(account) && transaction.getTimestamp() >= timestamp) {
                values.add(transaction);
                i++;
                if (count > 0 && i > count)
                    break;
            }
        }
        return values;
    }

}
