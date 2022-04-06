package com.kang.autologin;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class MyDBOpenHelper extends SQLiteOpenHelper {
    // 定义数据库名和版本号
    private static final String DBNAME="user.db";
    private static final int VERSION=1;
    public MyDBOpenHelper(Context context) {
        super(context, DBNAME, null, VERSION);
    }
    // 创建数据库
    @Override
    public void onCreate(SQLiteDatabase db) {
        // 创建数据表
        db.execSQL("create table userInfo(id INTEGER primary key autoincrement, account int(11), password varchar(32))");
    }
    // 升级数据库
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

}
