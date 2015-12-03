package com.Download.db;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.jaelyn.entities.ThreadInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * 数据访问接口实现
 * Created by Jaelyn on 2015/12/2.
 */
public class ThreadDAOImpl implements ThreadDAO{

    private DBHaper mHaper = null;

    public ThreadDAOImpl(Context context) {
        mHaper = DBHaper.getsInstance(context);
    }

    //synchronized 为同步方法，同时只能一个线程调用，保证线程安全
    @Override
    public synchronized void insertThread(ThreadInfo threadInfo) {
        SQLiteDatabase db = mHaper.getWritableDatabase();//获取一个可以写的数据库
        db.execSQL("insert into thread_info(thread_id,url,start,end,finished) values(?,?,?,?,?)",
                new Object[]{threadInfo.getId(),
                             threadInfo.getUrl(),
                             threadInfo.getStart(),
                             threadInfo.getEnd(),
                             threadInfo.getFinished()});
        db.close();
    }

    @Override
    public synchronized void deleteThread(String url) {
        SQLiteDatabase db = mHaper.getWritableDatabase();//获取一个可以写的数据库
        db.execSQL("delete from thread_info where url = ?",
                new Object[]{url});
        db.close();
    }

    @Override
    public synchronized void updateThread(String url, int thread_id, int finished) {
        SQLiteDatabase db = mHaper.getWritableDatabase();//获取一个可以写的数据库
        db.execSQL("update thread_info set finished = ? where url = ? and thread_id = ?",
                new Object[]{finished,url,thread_id});
        db.close();
    }

    @Override
    public List<ThreadInfo> getThread(String url) {
        SQLiteDatabase db = mHaper.getReadableDatabase();//获取一个只读的数据库
        List<ThreadInfo> list = new ArrayList<ThreadInfo>();
        Cursor cursor = db.rawQuery("select * from thread_info where url = ?",new String[]{url});
        while (cursor.moveToNext()){
            ThreadInfo threadInfo = new ThreadInfo();
            threadInfo.setId(cursor.getInt(cursor.getColumnIndex("thread_id")));
            threadInfo.setUrl(cursor.getString(cursor.getColumnIndex("url")));
            threadInfo.setStart(cursor.getInt(cursor.getColumnIndex("start")));
            threadInfo.setEnd(cursor.getInt(cursor.getColumnIndex("end")));
            threadInfo.setFinished(cursor.getInt(cursor.getColumnIndex("finished")));
            list.add(threadInfo);
        }
        cursor.close();
        db.close();
        return list;
    }

    @Override
    public Boolean isExists(String url, int thread_id) {
        SQLiteDatabase db = mHaper.getReadableDatabase();//获取一个只读的数据库
        Cursor cursor = db.rawQuery("select * from thread_info where url = ? and thread_id = ?", new String[]{url, thread_id + ""});
        boolean exists = cursor.moveToNext();
        cursor.close();
        db.close();
        return exists;
    }
}
