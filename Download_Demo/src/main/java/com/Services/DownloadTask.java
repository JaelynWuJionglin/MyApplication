package com.Services;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.Download.db.ThreadDAO;
import com.Download.db.ThreadDAOImpl;
import com.jaelyn.entities.ThreadInfo;

import org.apache.http.HttpStatus;

import java.io.File;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

/**
 * 下载任务类
 * Created by Jaelyn on 2015/12/2.
 */
public class DownloadTask {
    private Context mContext = null;
    private ThreadInfo.FileInfo fileInfo = null;
    private ThreadDAO mDao = null;
    private int mFinishde = 0;
    public boolean isPause = false;//下载暂停开关

    public DownloadTask(Context mContext, ThreadInfo.FileInfo fileInfo) {
        this.mContext = mContext;
        this.fileInfo = fileInfo;
        mDao = new ThreadDAOImpl(mContext);
    }

    //
    public void download(){
       //读取数据库的线程信息
        List<ThreadInfo> list = mDao.getThread(fileInfo.getUrl());
        ThreadInfo th = null;
        if(list.size() == 0){
            //初始化线程信息
            th = new ThreadInfo(0,0,fileInfo.getLength(),0,fileInfo.getUrl());
        }else {
             th = list.get(0);
        }
        //创建子线程
        new DownloadThread(th).start();
    }

    /**
     *下载线程
     */
    class DownloadThread extends Thread{
        private ThreadInfo threadInfo = null;

        public DownloadThread(ThreadInfo threadInfo) {
            this.threadInfo = threadInfo;
        }

        @Override
        public void run() {
            //向数据库插入线程信息
            if (!mDao.isExists(threadInfo.getUrl(),threadInfo.getId())){
                mDao.insertThread(threadInfo);
            }
            HttpURLConnection conn = null;
            InputStream input = null;
            RandomAccessFile raf = null;
            try {
                URL url = new URL(threadInfo.getUrl());
                conn = (HttpURLConnection) url.openConnection();
                conn.setConnectTimeout(3000);
                conn.setRequestMethod("GET");

                //设置下载位置
                int start = threadInfo.getStart() + threadInfo.getFinished();//之前开始的位置加上结束的位置
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
                        //把下载进度发送广播给Activity
                        mFinishde += len;
                        if (System.currentTimeMillis() - time >200){//每200毫秒发送一次
                            time = System.currentTimeMillis();
                            intent.putExtra("finished", mFinishde * 200 / fileInfo.getLength());
                            mContext.sendBroadcast(intent);
                        }
                        //在下载暂停时，保存下载进度
                        if (isPause){
                            Log.i("test", "mFinishde:" + mFinishde);
                            mDao.updateThread(threadInfo.getUrl(), threadInfo.getId(), mFinishde);
                            return;
                        }
                    }
                    //删除线程信息
                    mDao.deleteThread(threadInfo.getUrl(),threadInfo.getId());
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
