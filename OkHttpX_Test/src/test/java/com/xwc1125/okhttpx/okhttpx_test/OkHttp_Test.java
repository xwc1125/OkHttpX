package com.xwc1125.okhttpx.okhttpx_test;

import com.xwc1125.okhttpx.OkHttpX;
import com.xwc1125.okhttpx.download_mgr.AbstractDownloadMgr;
import com.xwc1125.okhttpx.okhttpx_test.leveldb.LevelDB;
import okhttp3.OkHttpClient;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import static com.xwc1125.okhttpx.download_mgr.AbstractDownloadMgr.DEFAULT_TASK_STATUS_START;

/**
 * @Description:
 * @Author: xwc1125
 * @Date: 2021/4/16 19:47
 * @Copyright Copyright@2021
 */
public class OkHttp_Test {

    private LevelDB levelDB;

    @Before
    public void setup() {
        try {
            levelDB = LevelDB.NewLevelDB("./logs/db");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testDownload() {
        //log拦截器
//        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
//        logging.setLevel(HttpLoggingInterceptor.Level.BODY);

        //自定义OkHttp
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .connectTimeout(10000L, TimeUnit.MILLISECONDS)
                .readTimeout(10000L, TimeUnit.MILLISECONDS)
//                .cookieJar(cookieJar)       //设置开启cookie
//                .addInterceptor(logging)            //设置开启log
                .build();
        OkHttpX mOkHttpX = new OkHttpX(okHttpClient);
        DownloadMgr.levelDB = levelDB;
        //默认
        DownloadMgr mDownloadMgr = (DownloadMgr) new DownloadMgr.Builder()
                .myOkHttp(mOkHttpX)
                .maxDownloadIngNum(5)       //设置最大同时下载数量（不设置默认5）
                .saveProgressBytes(50 * 1204)   //设置每50kb触发一次saveProgress保存进度 （不能在onProgress每次都保存 过于频繁） 不设置默认50kb
                .build();
        mDownloadMgr.resumeTasks();     //恢复本地所有未完成的任务

        AbstractDownloadMgr.Task task = new AbstractDownloadMgr.Task();
        task.setUrl("http://st.chaoyindj.com/zwmtv2016/%E5%A4%9C%E5%BA%97DJ%E5%97%A8%E6%9B%B2%E8%A7%86%E9%A2%91%20-%20%E9%82%93%E7%B4%AB%E6%A3%8B%20-%20%E6%9C%89%E5%BF%83%E4%BA%BA%EF%BC%88%E7%B2%A4%E8%AF%AD%EF%BC%89%20-%20DJwingx.mp4");
        String taskId = UUID.randomUUID().toString();
        task.setFilePath("./logs/" + taskId + ".mp4");
        task.setTaskId(taskId);
        task.setCompleteBytes(0);
        task.setDefaultStatus(DEFAULT_TASK_STATUS_START);
        String json = DownloadMgr.gson.toJson(task);
        levelDB.put(DownloadMgr.PrefixTaskKey + task.getTaskId(), json);
        mDownloadMgr.addTask(task);
//        mDownloadMgr.startAllTask();

        while (true) {

        }
    }
}
