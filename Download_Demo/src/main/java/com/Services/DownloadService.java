package com.Services;

import android.app.Service;
import android.content.Intent;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;

import com.jaelyn.entities.FileInfo;

import org.apache.http.HttpStatus;

import java.io.File;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by Jaelyn on 2015/12/1.
 */
public class DownloadService extends Service{

    public static final String ACTION_START = "ACTION_START";
    public static final String ACTION_STOP = "ACTION_STOP";
    public static final String ACTION_UPDATE = "ACTION_UPDATE";
    public static final String ACTION_FINISH = " ACTION_FINISH";

    public static final String DOWNLOAD_PATH = Environment.getExternalStorageDirectory().
            getAbsolutePath() + "/downloads/";  //存文件路径

    public static final int MSG_INT = 0;
    //下载任务集合
    private Map<Integer,DownloadTask> mTask = new LinkedHashMap<Integer, DownloadTask>();
    private InitThread initThread = null;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        //获得activity传来的参数
        if (ACTION_START.equals(intent.getAction())){
             FileInfo fileInfo = (FileInfo) intent.getSerializableExtra("fileInfo");
             //Log.i("test","start" + fileInfo.toString());
             //启动初始化线程
             initThread = new InitThread(fileInfo);
             //initThread.start();
             DownloadTask.mExecutorService.execute(initThread);
        }else if (ACTION_STOP.equals(intent.getAction())){
            //暂停下载
            FileInfo fileInfo = (FileInfo) intent.getSerializableExtra("fileInfo");
            //Log.i("test", "stop" + fileInfo.toString());
            //从集合中取出下载任务
            DownloadTask task = mTask.get(fileInfo.getId());
            if (task != null){
                task.isPause = true;
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
                    FileInfo fileinfo = (FileInfo) msg.obj;//获得下面发送过来的FileInfo对象
                    Log.i("test", "init" + fileinfo);
                    //启动下载任务
                    DownloadTask task = new DownloadTask(DownloadService.this,fileinfo,2);
                    task.download();
                    //把下载任务添加到集合中
                    mTask.put(fileinfo.getId(),task);
                    break;
            }
        }
    };

    /*
    * 初始化子线程
    */
    class InitThread extends Thread{
        private FileInfo mFileInfo = null;

        public InitThread(FileInfo mFileInfo) {
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
