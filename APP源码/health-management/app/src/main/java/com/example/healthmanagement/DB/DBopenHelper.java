package com.example.healthmanagement.DB;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class DBopenHelper extends SQLiteOpenHelper {
    final static String DB_Name = "hm.db"; //数据库名
    final static int VERSION = 1;//版本号

    public DBopenHelper(@Nullable Context context) {
        super(context, DB_Name, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //用户表
        db.execSQL(
                "CREATE TABLE `user`(" +
                        "`u_id` int primary key," +  //用户id
                        "`u_name` char(10) not null," +                          //用户名
                        "`u_password` not null," +                               //用户密码
                        "`u_mb` int default 5000)"                                        //目标步数
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
