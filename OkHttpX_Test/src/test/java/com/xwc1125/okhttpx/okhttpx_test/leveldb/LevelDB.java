package com.xwc1125.okhttpx.okhttpx_test.leveldb;

import org.iq80.leveldb.*;

import java.io.File;
import java.io.IOException;

import static org.iq80.leveldb.impl.Iq80DBFactory.*;

/**
 * @Description:
 * @Author: xwc1125
 * @Date: 2020/12/10 13:49
 * @Copyright Copyright@2020
 */
public class LevelDB {

    private DB db;

    private LevelDB(DB db) {
        this.db = db;
    }

    public static LevelDB NewLevelDB(String dbDir) throws IOException {
        Options options = new Options();
        options.createIfMissing(true);
        DB db = factory.open(new File(dbDir), options);
        return new LevelDB(db);
    }

    public DB getDB() {
        return this.db;
    }

    public void put(byte[] key, byte[] value, WriteOptions ops) throws DBException {
        if (ops == null) {
            this.db.put(key, value);
        } else {
            this.db.put(key, value, ops);
        }
    }

    public void put(byte[] key, byte[] value) throws DBException {
        put(key, value, null);
    }

    public void put(String key, String value) throws DBException {
        put(bytes(key), bytes(value));
    }

    public void put(String key, String value, WriteOptions ops) throws DBException {
        put(bytes(key), bytes(value), ops);
    }

    public byte[] get(byte[] key, ReadOptions options) throws DBException {
        if (options == null) {
            return this.db.get(key);
        }
        return this.db.get(key, options);
    }

    public byte[] get(byte[] key) throws DBException {
        return get(key, null);
    }

    public String get(String key) throws DBException {
        byte[] bytes = get(bytes(key));
        return asString(bytes);
    }

    public String get(String key, ReadOptions options) throws DBException {
        byte[] bytes = get(bytes(key), options);
        return asString(bytes);
    }

    public void del(String key) throws DBException {
        this.db.delete(bytes(key));
    }

    /**
     * 可以使用delete等操作，最终需要db.write，并需要将关闭连接
     *
     * @return
     */
    public void batch(BatchCallback callback) throws Exception {
        if (callback != null) {
            WriteBatch batch = this.db.createWriteBatch();
            try {
                callback.batch(batch);
                db.write(batch);
            } catch (Exception e) {
                throw new Exception(e);
            } finally {
                batch.close();
            }
        }
    }

    public void iterator(Iterator callback) throws Exception {
        if (callback != null) {
            DBIterator iterator = db.iterator();
            try {
                for (iterator.seekToFirst(); iterator.hasNext(); iterator.next()) {
                    callback.item(iterator.peekNext().getKey(), iterator.peekNext().getValue());
                }
            } catch (Exception e) {
                throw new Exception(e);
            } finally {
                iterator.close();
            }
        }
    }

    public void close() throws IOException {
        this.db.close();
    }

    public static String toString(byte[] bytes) {
        return asString(bytes);
    }

    public static byte[] toBytes(String s) {
        return bytes(s);
    }
}
