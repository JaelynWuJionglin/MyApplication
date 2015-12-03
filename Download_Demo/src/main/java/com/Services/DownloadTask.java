package com.Services;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.Download.db.ThreadDAO;
import com.Download.db.ThreadDAOImpl;
import com.jaelyn.entities.FileInfo;
import com.jaelyn.entities.ThreadInfo;

import org.apache.http.HttpStatus;

import java.io.File;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 下载任务类
 * Created by Jaelyn on 2015/12/2.
 */
public class DownloadTask {
    private Context mContext = null;
    private FileInfo fileInfo = null;
    private ThreadDAO mDao = null;
    private int mFinishde = 0;
    public boolean isPause = false;//下载暂停开关
    private int mThreadCount = 1;//线程数量
    private List<DownloadThread> mThreadList = null;//下载暂停线程管理集合
    public static ExecutorService mExecutorService = Executors.newCachedThreadPool();//线程池

    public DownloadTask(Context mContext, FileInfo fileInfo,int mThreadCount) {
        this.mContext = mContext;
        this.fileInfo = fileInfo;
        this.mThreadCount = mThreadCount;
        mDao = new ThreadDAOImpl(mContext);
    }

    //
    public void download(){
       //读取数据库的线程信息
        List<ThreadInfo> list = mDao.getThread(fileInfo.getUrl());
       /* ThreadInfo th = null;
        if(list.size() == 0){
            //初始化线程信息
            th = new ThreadInfo(0,0,fileInfo.getLength(),0,fileInfo.getUrl());
        }else {
             th = list.get(0);
        }
        //创建子线程
        new DownloadThread(th).start();*/
        if(list.size() == 0){
            //获得每个线程的下载长度
            int lenght = fileInfo.getLength()/mThreadCount;
            for(int i=0;i<mThreadCount;i++){
                ThreadInfo threadInfo = new ThreadInfo(i,lenght*i,(i+1)*lenght-1,0,fileInfo.getUrl());
                if (i == mThreadCount -1){
                    //可能不能整除
                    threadInfo.setEnd(fileInfo.getLength());//最后一个文件结尾的位置为文件长度
                }
                //添加线程信息到集合中
                list.add(threadInfo);
                //向数据库插入线程信息
                mDao.insertThread(threadInfo);
            }
        }
        mThreadList = new ArrayList<DownloadThread>();
        //启动多个线程下载
        for(ThreadInfo info : list){
            DownloadThread thread = new DownloadThread(info);
            //thread.start();
            DownloadTask.mExecutorService.execute(thread);
            //添加线程到集合中,管理（暂停）
            mThreadList.add(thread);
        }

    }
    /**
     * 判断线程是否执行完毕
     * synchronized 同步方法，保证同一时间段只有一个线程能访问
     */
    private synchronized void checkAllTheardFinished(){
        boolean allFinishde = true;
        //遍历线程集合，判断线程是否执行完毕
        for(DownloadThread thread : mThreadList){
            if(!thread.isFinished){
                allFinishde = false;
                break;
            }
        }
        if (allFinishde){
            //删除线程信息
            mDao.deleteThread(fileInfo.getUrl());
            //下载任务执行结束，发送广播通知UI
            Intent intent = new Intent(DownloadService.ACTION_FINISH);
            intent.putExtra("fileInfo",fileInfo);
            mContext.sendBroadcast(intent);
        }
    }

    /**
     *下载线程
     */
    class DownloadThread extends Thread{
        private ThreadInfo threadInfo = null;
        public boolean isFinished = false;//线程是否执行完毕

        public DownloadThread(ThreadInfo threadInfo) {
            this.threadInfo = threadInfo;
        }

        @Override
        public void run() {

            HttpURLConnection conn = null;
            InputStream input = null;
            RandomAccessFile raf = null;
            try {
                URL url = new URL(threadInfo.getUrl());
                conn = (HttpURLConnection) url.openConnection();
                conn.setConnectTimeout(3000);
                conn.setRequestMethod("GET");

                //设置下载位置
                int start = threadInfo.getStart() + threadInfo.getFinished();//之前开始的位置加上结束时的下载进度
                Log.i("test","start:"+start+"   getStart():"+threadInfo.getStart()+"   end:"+threadInfo.getFinished());

                conn.setRequestProperty("Range", "bytes=" + start + "-" + threadInfo.getEnd());

                //设置文件写入位置
                File file =  new File(DownloadService.DOWNLOAD_PATH,fileInfo.getFileName());
                raf = new RandomAccessFile(file,"rwd");
                raf.seek(start);//seek()方法在读写的时候跳过设置好的字节，从下一个字节开始读写。

                Intent intent = new Intent(DownloadService.ACTION_UPDATE);
                mFinishde += threadInfo.getFinished();
                //开始下载
                if (conn.getResponseCode()== HttpStatus.SC_PARTIAL_CONTENT){
                    //读取数据
                    input = conn.getInputStream();//得到输入流
                    byte[] buffer = new byte[1024 * 4];
                    int len = -1;
                    long time = System.currentTimeMillis();
                    while ((len = input.read(buffer)) != -1){
                        //写入文件
                        raf.write(buffer, 0, len);
                        //累加整个文件完成进度
                        mFinishde += len;
                        //累加每个线程完成的进度
                        threadInfo.setFinished(threadInfo.getFinished()+len);
                        //把下载进度发送广播给Activity
                        if (System.currentTimeMillis() - time >1000){//每500毫秒发送一次
                            time = System.currentTimeMillis();
                            intent.putExtra("finished", mFinishde * 100 / fileInfo.getLength());
                            intent.putExtra("id", fileInfo.getId());
                            mContext.sendBroadcast(intent);
                        }
                        //在下载暂停时，保存下载进度
                        if (isPause){
                            Log.i("test", "mFinishde:" + mFinishde);
                            mDao.updateThread(threadInfo.getUrl(), threadInfo.getId(), threadInfo.getFinished());
                            return;
                        }
                    }
                    //标示线程执行完毕
                    isFinished = true;

                    //检查下载任务是否执行完毕
                    checkAllTheardFinished();
                }

            } catch (Exception e) {
                e.printStackTrace();
            }finally {
                try {
                    input.close();
                    conn.disconnect();
                    raf.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }



        }
    }
}
