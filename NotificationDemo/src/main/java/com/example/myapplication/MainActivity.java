package com.example.myapplication;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends Activity implements View.OnClickListener{

    NotificationManager manager;//通知控制类
    int notification_ID ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        findViewById(R.id.but_1).setOnClickListener(this);
        findViewById(R.id.but_2).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){
            case R.id.but_1:
                sendNotification();
                break;

            case R.id.but_2:
                manager.cancel(notification_ID);
                break;
        }
    }

    /*
    * 构造Notification() 并发送到通知栏
    * */
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    private void sendNotification(){
        Notification.Builder builder = new Notification.Builder(this);
        builder.setSmallIcon(R.mipmap.ic_launcher);//设置图标
        builder.setTicker("hello");//手机状态栏的提示
        builder.setWhen(System.currentTimeMillis());//设置时间
        builder.setContentTitle("通知栏通知");//设置标题
        builder.setContentText("我来自NotificationDemo");//设置通知栏类容

        Intent intent = new Intent(this,MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this,0,intent,0);
        builder.setContentIntent(pendingIntent);//设置点击后的意图

        //builder.setDefaults(Notification.DEFAULT_SOUND);//设置提示声音
        //builder.setDefaults(Notification.DEFAULT_LIGHTS);//设置指示灯 (需要权限)
        //builder.setDefaults(Notification.DEFAULT_VIBRATE);//设置震动效果 (需要权限)
        builder.setDefaults(Notification.DEFAULT_ALL);//以上三种全有

        /*
         * 获取 notification
         * 4.1以上用这种方法。4.1以下用builder.getNotification()
         */
        Notification notification = builder.build();
        manager.notify(notification_ID,notification);


    }
}
