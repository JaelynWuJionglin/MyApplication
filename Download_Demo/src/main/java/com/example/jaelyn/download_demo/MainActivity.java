package com.example.jaelyn.download_demo;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.Toast;

import com.Services.DownloadService;
import com.jaelyn.entities.FileInfo;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends Activity {

    private ListView listView = null;
    private List<FileInfo> mFilelist = null;
    private FilelistAdapter adapter = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listView = (ListView) findViewById(R.id.list_item);

        //创建文件集合
        mFilelist = new ArrayList<FileInfo>();
        //创建文件
        //String url1 = "http://192.168.1.101/download/1.mp3";
        String url1 = "http://download.kugou.com/download/kugou_android";
        String url2 = "http://dldir1.qq.com/qqfile/qq/QQ7.9/16621/QQ7.9.exe";
        String url3 = "http://d.hiphotos.baidu.com/zhidao/pic/item/b3119313b07eca807e7845fe922397dda14483ad.jpg";
        String url4 = "http://192.168.1.101/download/eyes.ape";
        String url5 = "http://192.168.1.101/download/非你莫属.ape";
        String name1 = url1.substring(url1.lastIndexOf("/")+1);
        String name2 = url2.substring(url2.lastIndexOf("/")+1);
        String name3 = url3.substring(url3.lastIndexOf("/")+1);
        String name4 = url4.substring(url4.lastIndexOf("/")+1);
        String name5 = url5.substring(url5.lastIndexOf("/")+1);

        FileInfo fileInfo1 = new FileInfo(0,getUrlString(url1),name1,0,0);
        FileInfo fileInfo2 = new FileInfo(1,getUrlString(url2),name2,0,0);
        FileInfo fileInfo3 = new FileInfo(2,getUrlString(url3),name3,0,0);
        FileInfo fileInfo4 = new FileInfo(3,getUrlString(url4),name4,0,0);
        FileInfo fileInfo5 = new FileInfo(4,getUrlString(url5),name5,0,0);
        mFilelist.add(fileInfo1);
        mFilelist.add(fileInfo2);
        mFilelist.add(fileInfo3);
        mFilelist.add(fileInfo4);
        mFilelist.add(fileInfo5);
        //创建适配器
        adapter = new FilelistAdapter(this,mFilelist);
        //设置ListView
        listView.setAdapter(adapter);

        //注册广播接收器
        IntentFilter filter = new IntentFilter();
        filter.addAction(DownloadService.ACTION_UPDATE);
        filter.addAction(DownloadService.ACTION_FINISH);
        registerReceiver(mReceiver,filter);
    }
    //处理中文编码
    private String getUrlString(String url){
        String mUrl = "";
        try {
            mUrl = new String(url.getBytes("utf-8"),"iso-8859-1");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return mUrl;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(mReceiver);
    }

    /*
        * 更新进度条广播接收器
        */
    BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if(DownloadService.ACTION_UPDATE.equals(intent.getAction())){
                int finished = intent.getIntExtra("finished",0);
                int id = intent.getIntExtra("id",0);
                adapter.updateProgress(id,finished);
            }else if (DownloadService.ACTION_FINISH.equals(intent.getAction())){
                //下载完毕设置进度条为0
                FileInfo fileInfo = (FileInfo) intent.getSerializableExtra("fileInfo");
                Toast.makeText(MainActivity.this,fileInfo.getFileName()+"下载完成",Toast.LENGTH_SHORT).show();
                adapter.updateProgress(fileInfo.getId(),0);
            }
        }
    };
}
