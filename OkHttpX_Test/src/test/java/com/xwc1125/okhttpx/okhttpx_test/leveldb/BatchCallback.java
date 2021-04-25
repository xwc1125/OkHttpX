package com.xwc1125.okhttpx.okhttpx_test.leveldb;

import org.iq80.leveldb.WriteBatch;

/**
 * @Description:
 * @Author: xwc1125
 * @Date: 2020/12/10 14:34
 * @Copyright Copyright@2020
 */

public interface BatchCallback {
    /**
     * 批量处理的回调
     * <p>
     * 最终的写入已经在底层实现
     *
     * @param batch
     */
    void batch(WriteBatch batch);
}
