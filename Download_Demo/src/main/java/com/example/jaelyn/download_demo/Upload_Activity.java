package com.example.jaelyn.download_demo;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;

import com.Services.DownloadTask;
import com.Services.UploadThread;
import com.Uitli.ProcessImageView;

import java.io.File;

public class Upload_Activity extends Activity{

    private ProcessImageView processImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload);

        /*Button button = (Button) findViewById(R.id.button);
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
        });*/

        final String url = "http://192.168.1.101/upload/Upload";
        String name = "meinv1.jpg";
        final String path = Environment.getExternalStorageDirectory().
                getAbsolutePath() + "/Download/";
        final File mfile = new File(path,name);

        processImageView = (ProcessImageView) findViewById(R.id.imageView);
        Bitmap imageBitmap = BitmapFactory.decodeFile(mfile.getAbsolutePath());
        processImageView.setImageBitmap(imageBitmap);
        //processImageView.setBackgroundResource(R.drawable.meinv);
        processImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //启动线程开始上传
                UploadThread thread = new UploadThread(mfile.getAbsolutePath(),url);
                DownloadTask.mExecutorService.execute(thread);
                processImageView.setProgress(50);
            }
        });

    }
}















