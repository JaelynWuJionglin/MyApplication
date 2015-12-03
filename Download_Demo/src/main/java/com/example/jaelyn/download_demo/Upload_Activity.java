package com.example.jaelyn.download_demo;

import android.app.Activity;
import android.content.Intent;
import android.os.Environment;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.Services.DownloadTask;
import com.Services.UploadThread;

import java.io.File;

public class Upload_Activity extends Activity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload);

        Button button = (Button) findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = "http://192.168.1.101/upload/Upload";
                String name = "gh.jpg";
                String DOWNLOAD_PATH = Environment.getExternalStorageDirectory().
                        getAbsolutePath() + "/Download/";
                File dir = new File(DOWNLOAD_PATH);
                File mfile = new File(dir,name);
                Log.i("file",mfile.getAbsolutePath());
                //启动线程开始上传
                UploadThread thread = new UploadThread(mfile.getAbsolutePath(),url);
                DownloadTask.mExecutorService.execute(thread);
            }
        });
    }
}















