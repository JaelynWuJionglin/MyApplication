package com.Download.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Jaelyn on 2015/12/1.
 * 数据库帮助类
 */
public class DBHaper extends SQLiteOpenHelper{

    private static final String DB_NAME = "download.db";//数据库名
    private static final int VERSION = 1;  //数据库版本
    //创建表
    private static final String SQL_CREATE = "create table thread_info(_id integer primary key autoincrement,thread_id integer,url text,start integer,end integer,finished integer)";
    //删除表
    private static final String SQL_DROP = "drop table if exists thread_info";

    private static DBHaper sHelper = null;//静态对象引用

    private DBHaper(Context context) {
        super(context, DB_NAME, null, VERSION);
    }

    /**
     * 实现数据库单例模式
     * 获得类的对象
     */
    public static DBHaper getsInstance(Context context){
        if (sHelper == null){
            sHelper = new DBHaper(context);
        }
        return sHelper;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
         db.execSQL(SQL_CREATE);//创建
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //更新数据库
        db.execSQL(SQL_DROP);//删除数据库
        db.execSQL(SQL_CREATE);//创建
    }
}
