package com.xwc1125.okhttpx.okhttpx_test.leveldb;

/**
 * @Description:
 * @Author: xwc1125
 * @Date: 2020/12/10 14:37
 * @Copyright Copyright@2020
 */
public interface Iterator {
    /**
     * 循环时，每一个值的返回
     *
     * @param key
     * @param value
     */
    void item(byte[] key, byte[] value);
}
