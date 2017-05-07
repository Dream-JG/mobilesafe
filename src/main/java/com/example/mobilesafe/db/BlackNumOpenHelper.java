package com.example.mobilesafe.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by li on 2017/5/6.
 */

public class BlackNumOpenHelper extends SQLiteOpenHelper {
    public static final String TABLE = "info";

    public BlackNumOpenHelper(Context context) {
        super(context, "blacknum.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + TABLE + "(id integer primary key autoincrement," +
                "blacknum varchar(20),mode varchar(2));");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {


    }
}
