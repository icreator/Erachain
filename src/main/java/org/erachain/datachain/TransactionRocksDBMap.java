package org.erachain.datachain;

import org.erachain.dbs.rocksDB.integration.DBMapDB;
import org.erachain.dbs.rocksDB.integration.DBRocksDBTable;
import org.mapdb.DB;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Iterator;

public class TransactionRocksDBMap extends TransactionMapImpl
{

    static Logger logger = LoggerFactory.getLogger(TransactionRocksDBMap.class.getSimpleName());

    int TIMESTAMP_INDEX = 0; // in Family List

    public TransactionRocksDBMap(DCSet databaseSet, DB database) {
        super(databaseSet, database);
    }


    @Override
    protected void getMap() {
        map = new org.erachain.dbs.rocksDB.TransactionRocksDBMap(databaseSet, database);
    }

    @Override
    protected void createIndexes() {
    }

    @Override
    Iterable typeKeys(String address, Long timestamp, Integer type) {
        /*
        IndexByteableTuple3StringLongInteger indexByteableTuple3StringLongInteger = new IndexByteableTuple3StringLongInteger();
        return ((DBRocksDBTable) map).filterAppropriateValuesAsKeys(
                indexByteableTuple3StringLongInteger.toBytes(new Fun.Tuple3<>(address, timestamp, type), null),
                ((DBRocksDBTable) map).list);

         */
        return null;
    }

    @Override
    Iterable senderKeys(String sender) {
        /*
        //byte[] key = null;
        byte[] key = null;
        try {
            IndexByteableTuple3StringLongInteger indexByteableTuple3StringLongInteger = new IndexByteableTuple3StringLongInteger();
            key = indexByteableTuple3StringLongInteger.toBytes(
                new Fun.Tuple3<String, Long, Integer>(sender, null, null, null), null);
        } catch (Exception e) {

        }
        return ((DBRocksDBTable) map).filterAppropriateValuesAsKeys(
                key,
                ((org.erachain.dbs.rocksDB.TransactionRocksDBMap)map).getSenderIndex());

         */
        return null;
    }

    //@Override
    Iterable sendKeys(byte[] recipient) {
        return ((DBRocksDBTable) map).filterAppropriateValuesAsKeys(recipient,
                ((org.erachain.dbs.rocksDB.TransactionRocksDBMap)map).getRecientIndex());
    }

    @Override
    Iterable recipientKeys(String recipient) {
        return (Iterable) ((DBMapDB) map).getIterator(false);
    }


    @Override
    public Iterator<Long> getTimestampIterator() {
        return  ((org.erachain.dbs.rocksDB.TransactionRocksDBMap) map).getIterator(0, false);
        ///IndexDB timestampIndex = ((org.erachain.dbs.rocksDB.TransactionRocksDBMap) map).getTimestampIndex();
    }

    @Override
    public Iterator<Long> getCeatorIterator() {
        return null;
    }
}
