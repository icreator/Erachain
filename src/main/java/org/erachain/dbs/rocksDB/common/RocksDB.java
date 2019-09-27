package org.erachain.dbs.rocksDB.common;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.erachain.dbs.rocksDB.indexes.IndexDB;
import org.erachain.settings.Settings;
import org.rocksdb.ColumnFamilyHandle;
import org.rocksdb.RocksDBException;

import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.erachain.dbs.rocksDB.utils.ConstantsRocksDB.ROCKS_DB_FOLDER;

/**
 * TODO зачем выделен этот файл, какой функционал он несет, почему нельзя было его встрогить в супер
 * Этот класс позаимствовани из проекта "tron". Скорее всего он использовался для разделения функционала.
 * Это средняя прослойка между верхним и нижним интерфейсом. Используется для инициализации БД и перенаправления индексов.
 * Можно обойтись без этой прослойки
 * Встроить можно все что угодно куда угодно
 * ЯФ так опнял это Обертка база данных как файл с обработкой закрыть открыть сохранить.
 */
@Slf4j
public class RocksDB implements DB<byte[], byte[]>, Flusher
{

    @Getter
    @Setter

    // TODO ?? зачем эта переменная? какой функционал
    //эта переменная позаимствована из проекта "tron" нужна для создания каких-то настроек
    private boolean dbSync = true;

    @Getter
    public RocksDbDataSourceImpl db;

    private WriteOptionsWrapper optionsWrapper;

    public RocksDB(String name) {
        db = new RocksDbDataSourceImpl(Settings.getInstance().getDataDir() + ROCKS_DB_FOLDER, name);
        optionsWrapper = WriteOptionsWrapper.getInstance().sync(dbSync);
        db.initDB(new ArrayList<>());
    }

    public RocksDB(String name, List<IndexDB> indexes, RocksDbSettings settings, String root) {
        db = new RocksDbDataSourceImpl(
                Paths.get(root).toString(), name);
        optionsWrapper = WriteOptionsWrapper.getInstance().sync(dbSync);
        db.initDB(settings, indexes);
    }

    @Override
    public byte[] get(byte[] key) {
        return db.getData(key);
    }

    public byte[] get(ColumnFamilyHandle columnFamilyHandle, byte[] key) {
        return db.getData(columnFamilyHandle, key);
    }

    @Override
    public void put(byte[] key, byte[] value) {
        db.putData(key, value);
    }

    public void put(ColumnFamilyHandle columnFamilyHandle, byte[] key, byte[] value) {
        db.putData(columnFamilyHandle, key, value);
    }

    @Override
    public int size() {
        return db.size();
    }

    @Override
    public boolean isEmpty() {
        return db.size() == 0;
    }

    @Override
    public void remove(byte[] key) {
        db.deleteData(key);
    }

    public void remove(ColumnFamilyHandle columnFamilyHandle, byte[] key) {
        db.deleteData(columnFamilyHandle, key);
    }

    @Override
    public Set<byte[]> keySet() throws RuntimeException {
        return db.allKeys();
    }

    public List<byte[]> values() throws RuntimeException {
        return db.allValues();
    }

    public DBIterator iterator(boolean descending) {
        return db.iterator(descending);
    }

    public DBIterator indexIterator(boolean descending, int index) {
        return db.indexIterator(descending, index);
    }

    public DBIterator indexIterator(boolean descending, ColumnFamilyHandle index) {
        return db.indexIterator(descending, index);
    }
    public DBIterator indexIteratorFilter(boolean descending, byte[] filter) {
        return db.indexIteratorFilter(descending, filter);
    }
    public DBIterator indexIteratorFilter(boolean descending, ColumnFamilyHandle index, byte[] filter) {
        return db.indexIteratorFilter(descending, index, filter);
    }


    @Override
    public void flush(Map<byte[], byte[]> rows) {
        db.updateByBatch(rows, optionsWrapper);

    }

    @Override
    public void flush() {
        try {
            db.flush();
        } catch (RocksDBException e) {
            logger.error(e.getMessage(), e);
        }
    }

    @Override
    public void close() {
        db.close();
    }

    // TODO найти реализацию
    @Override
    public void commit() {
        db.commit();
    }

    // TODO найти реализацию
    @Override
    public void rollback() {
        db.rollback();
    }

    @Override
    public void reset() {
        db.reset();
    }

    @Override
    public List<byte[]> filterAppropriateValuesAsKeys(byte[] filter, int indexDB) {
        return db.filterApprropriateValues(filter, indexDB);
    }

    @Override
    public List<byte[]> filterAppropriateValuesAsKeys(byte[] filter, ColumnFamilyHandle indexDB) {
        return db.filterApprropriateValues(filter, indexDB);
    }

    @Override
    public List<byte[]> filterAppropriateValuesAsKeys(byte[] filter) {
        return db.filterApprropriateValues(filter);
    }

    public List<byte[]> filterAppropriateValues(byte[] filter) {
        return db.filterApprropriateValues(filter);
    }

    public List<byte[]> getLatestValues(long limit) {
        return db.getLatestValues(limit);
    }

    public List<byte[]> getValuesPrevious(byte[] key, long limit) {
        return db.getValuesPrevious(key, limit);
    }

    public List<byte[]> getValuesNext(byte[] key, long limit) {
        return db.getValuesNext(key, limit);
    }

    public Set<byte[]> getKeysNext(byte[] key, long limit) {
        return db.getKeysNext(key, limit);
    }

    public Set<byte[]> getKeysNext(byte[] key, long limit, IndexDB index) {
        return db.getKeysNext(key, limit, index.getColumnFamilyHandle());
    }

    public List<ColumnFamilyHandle> getColumnFamilyHandles() {
        return db.getColumnFamilyHandles();
    }

    public ColumnFamilyHandle getColumnFamilyHandle(int index) {
        return db.getColumnFamilyHandles().get(index);
    }

}