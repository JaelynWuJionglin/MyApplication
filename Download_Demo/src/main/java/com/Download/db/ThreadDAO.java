package com.Download.db;

import com.jaelyn.entities.ThreadInfo;

import java.util.List;

/**
 * 数据库接口
 * Created by Jaelyn on 2015/12/2.
 */
public interface ThreadDAO {
    /**
     *插入线程信息
     */
    public void insertThread(ThreadInfo threadInfo);

    /**
     *删除线程
     * @url
     * @thread_id
     */
    public void deleteThread(String url,int thread_id);

    /**
     *更新线程下载进度
     * @url
     * @thread_id
     * @finished
     */
    public void updateThread(String url,int thread_id,int finished);

    /**
     *查询线程文件信息，以集合的方式返回
     * @url
     */
    public List<ThreadInfo> getThread(String url);

    /**
     *线程信息是否存在
     * @url
     * @thread_id
     */
    public Boolean isExists(String url,int thread_id);
}
