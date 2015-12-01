package com.example.jaelyn.download_demo;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.Services.DownloadService;
import com.jaelyn.entities.ThreadInfo;

public class MainActivity extends Activity {
    private TextView textView;
    private ProgressBar progressBar;
    Button buttont,buttonj;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textView = (TextView) findViewById(R.id.textView);
        buttonj = (Button) findViewById(R.id.buttonj);
        buttont = (Button) findViewById(R.id.buttont);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        progressBar.setMax(100);

        //创建文件信息对象
        String url = "http://192.168.1.101/download/1.mp3";
        //String url = "http://dldir1.qq.com/qqfile/qq/QQ7.9/16621/QQ7.9.exe";
        String name = url.substring(url.lastIndexOf("/")+1);
        textView.setText(name);
        final ThreadInfo.FileInfo fileInfo = new ThreadInfo.FileInfo(0,url,name,0,0);

        //事件监听
        //开始
        buttonj.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //通过Intent传递参数给Service
                Intent intent = new Intent(MainActivity.this, DownloadService.class);
                intent.setAction(DownloadService.ACTION_START);
                intent.putExtra("fileInfo", fileInfo);
                startService(intent);
            }
        });
        //暂停
        buttont.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //通过Intent传递参数给Service
                Intent intent = new Intent(MainActivity.this, DownloadService.class);
                intent.setAction(DownloadService.ACTION_STOP);
                intent.putExtra("fileInfo", fileInfo);
                startService(intent);
            }
        });

        //注册广播接收器
        IntentFilter filter = new IntentFilter();
        filter.addAction(DownloadService.ACTION_UPDATE);
        registerReceiver(mReceiver,filter);
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
                progressBar.setProgress(finished);
            }
        }
    };
}
