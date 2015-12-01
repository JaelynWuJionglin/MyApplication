package com.Services;

import android.app.Service;
import android.content.Intent;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;

import com.jaelyn.entities.ThreadInfo;

import org.apache.http.HttpStatus;

import java.io.File;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Jaelyn on 2015/12/1.
 */
public class DownloadService extends Service{

    public static final String ACTION_START = "ACTION_START";
    public static final String ACTION_STOP = "ACTION_STOP";
    public static final String ACTION_UPDATE = "ACTION_UPDATE";

    public static final String DOWNLOAD_PATH = Environment.getExternalStorageDirectory().
            getAbsolutePath() + "/downloads/";  //存文件路径

    public static final int MSG_INT = 0;
    private  DownloadTask mTask = null;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        //获得activity传来的参数
        if (ACTION_START.equals(intent.getAction())){
             ThreadInfo.FileInfo fileInfo = (ThreadInfo.FileInfo) intent.getSerializableExtra("fileInfo");
             Log.i("test","start" + fileInfo.toString());
            //启动初始化线程
            new InitThread(fileInfo).start();
        }else if (ACTION_STOP.equals(intent.getAction())){
            ThreadInfo.FileInfo fileInfo = (ThreadInfo.FileInfo) intent.getSerializableExtra("fileInfo");
            Log.i("test", "stop" + fileInfo.toString());
            if (mTask != null){
                mTask.isPause = true;
            }
        }

        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    Handler mhandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case MSG_INT:
                    ThreadInfo.FileInfo fileinfo = (ThreadInfo.FileInfo) msg.obj;//获得下面发送过来的FileInfo对象
                    Log.i("test","init"+fileinfo);
                    //启动下载任务
                    mTask = new DownloadTask(DownloadService.this,fileinfo);
                    mTask.download();
                    break;
            }
        }
    };

    /*
    * 初始化子线程
    */
    class InitThread extends Thread{
        private ThreadInfo.FileInfo mFileInfo = null;

        public InitThread(ThreadInfo.FileInfo mFileInfo) {
            this.mFileInfo = mFileInfo;
        }

        public void run(){
            HttpURLConnection conn = null;
            RandomAccessFile raf = null;
            try{
                //链接网络文件
                URL url = new URL(mFileInfo.getUrl());
                conn = (HttpURLConnection) url.openConnection();
                conn.setConnectTimeout(3000);//链接超时时间
                conn.setRequestMethod("GET");//请求方法。（从服务器下载用get，其他用post）

                //获得文件长度
                int length = -1;
                if(conn.getResponseCode() == HttpStatus.SC_OK){
                    //链接上去了,获取文件长度
                    length = conn.getContentLength();
                    Log.i("test",length+"");
                }
                if (length<=0){
                    Log.i("test","---------------nono");
                    return;
                }
                File dir = new File(DOWNLOAD_PATH);
                if(!dir.exists()){
                    dir.mkdir();//判断文件目录是否存在,这里是不存在就给他创建一个
                }
                //在本地创建文件
                File file = new File(dir,mFileInfo.getFileName());
                raf = new RandomAccessFile(file,"rwd");//随机访问的文件流写入文件
                //设置文件长度
                mFileInfo.setLength(length);
                mhandler.obtainMessage(MSG_INT, mFileInfo).sendToTarget();//把mFileInfo对象发送给Handler
            }catch (Exception e){
                e.printStackTrace();
            }finally {
                try {
                    conn.disconnect();
                    raf.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
