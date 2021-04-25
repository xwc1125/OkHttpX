package com.xwc1125.okhttpx.okhttpx_test;

import com.google.gson.Gson;
import com.xwc1125.okhttpx.download_mgr.AbstractDownloadMgr;
import com.xwc1125.okhttpx.download_mgr.DownloadStatus;
import com.xwc1125.okhttpx.okhttpx_test.leveldb.LevelDB;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;

import org.iq80.leveldb.DBIterator;
import org.iq80.leveldb.impl.Iq80DBFactory;

/**
 * @Description:
 * @Author: xwc1125
 * @Date: 2021/4/16 19:57
 * @Copyright Copyright@2021
 */
@Slf4j
public class DownloadMgr extends AbstractDownloadMgr {

    public static Gson gson = new Gson();
    public static LevelDB levelDB;
    public static String PrefixTaskKey = "xwc1125_download_task_";
    public static HashMap<String, Task> tasks = new HashMap<>();

    private DownloadMgr(Builder builder) {
        super(builder);
    }

    private synchronized Task get(String taskId) {
        return tasks.get(taskId);
    }

    private synchronized void put(String taskId, Task task) {
        tasks.put(taskId, task);
    }

    /**
     * 初始进入app 恢复所有未完成的任务
     */
    @Override
    public void resumeTasks() {
        log.info(TAG + "start resumeTasks");
        // 获取所有未完成的任务（已完成的不需要添加）
        DBIterator iterator = levelDB.getDB().iterator();
        while (iterator.hasNext()) {
            Map.Entry<byte[], byte[]> next = iterator.next();
            String key = Iq80DBFactory.asString(next.getKey());
            if (key.startsWith(PrefixTaskKey)) {
                String value = Iq80DBFactory.asString(next.getValue());
                if (value != null && value != "" && value.trim().length() > 0) {
                    Task task = gson.fromJson(value, Task.class);
                    task.setDefaultStatus(DEFAULT_TASK_STATUS_PAUSE);       //所有任务初始设置为暂停
                    put(task.getTaskId(), task);
                    this.addTask(task);
                }
            }
        }
        this.startAllTask();
    }

    /**
     * 保存进度
     *
     * @param taskId       taskId
     * @param currentBytes 已经下载的bytes
     * @param totalBytes   总共bytes
     */
    @Override
    protected void saveProgress(String taskId, long currentBytes, long totalBytes) {
        log.info("保存进度,taskId=" + taskId + ",currentBytes=" + currentBytes + ",totalBytes=" + totalBytes);
        Task task = get(taskId);
        if (task == null) {
            return;
        }
        task.setCompleteBytes(currentBytes);
        put(taskId, task);
        try {
            String toJson = gson.toJson(task);
            levelDB.put(PrefixTaskKey + taskId, toJson);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 下载任务开始
     *
     * @param taskId task id
     */
    @Override
    protected void onTaskStart(String taskId) {
        log.info("下载任务开始:" + taskId);
        // startTask(taskId);
        Task task = get(taskId);
        if (task == null) {
            return;
        }
        task.setDefaultStatus(DownloadStatus.STATUS_DOWNLOADING.code);
        put(taskId, task);
    }

    /**
     * 下载任务暂停
     *
     * @param taskId task id
     */
    @Override
    protected void onTaskPause(String taskId) {
        log.info("下载任务暂停:" + taskId);
        // pauseTask(taskId);
        Task task = get(taskId);
        task.setDefaultStatus(DownloadStatus.STATUS_PAUSE.code);
        put(taskId, task);
    }

    /**
     * 下载任务完成
     *
     * @param taskId task id
     */
    @Override
    protected void onTaskFinish(String taskId) {
        log.info("下载任务完成:" + taskId);
        // finishTask(taskId);
        try {
            Task task = get(taskId);
            if (task == null) {
                return;
            }
            task.setDefaultStatus(DownloadStatus.STATUS_FINISH.code);
            put(taskId, task);
            levelDB.del(PrefixTaskKey + taskId);
            tasks.remove(taskId);
        } catch (Exception e) {
            log.error("删除出错");
        }
    }

    /**
     * 下载任务失败
     *
     * @param taskId task id
     */
    @Override
    protected void onTaskFail(String taskId) {
        //失败设置为暂停 允许用户再次尝试开始
        log.error("下载任务失败:" + taskId);
        // pauseTask(taskId);
        Task task = get(taskId);
        task.setDefaultStatus(DownloadStatus.STATUS_FAIL.code);
        put(taskId, task);
    }

    //实现Builder
    public static class Builder extends AbstractDownloadMgr.Builder {

        @Override
        public AbstractDownloadMgr build() {
            return new DownloadMgr(this);
        }
    }
}
