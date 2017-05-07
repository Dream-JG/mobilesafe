package com.example.mobilesafe.db.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.SystemClock;

import com.example.mobilesafe.db.BlackNumOpenHelper;
import com.example.mobilesafe.domain.BlackNumInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by li on 2017/5/6.
 */

public class BlackNumDao {
    public static final int TEL = 0;
    public static final int SMS = 1;
    public static final int ALL = 2;

    private BlackNumOpenHelper blackNumOpenHelper;

    public BlackNumDao(Context context) {
        blackNumOpenHelper = new BlackNumOpenHelper(context);
    }


    /**
     * 添加黑名单
     *
     * @param blackNum 号码
     * @param mode     拦截模式
     */
    public void addBlackNum(String blackNum, int mode) {
        //加锁的数据库
        SQLiteDatabase sqLiteDatabase = blackNumOpenHelper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("blacknum", blackNum);
        contentValues.put("mode", mode);
        sqLiteDatabase.insert(BlackNumOpenHelper.TABLE, null, contentValues);
        sqLiteDatabase.close();

    }

    /**
     * 修改黑名单号码的拦截模式
     *
     * @param blackNum 黑名单号码
     * @param newMode  新的拦截模式
     */

    public void updateBlackNum(String blackNum, int newMode) {
        SQLiteDatabase sqLiteDatabase = blackNumOpenHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("mode", newMode);
        sqLiteDatabase.update(BlackNumOpenHelper.TABLE, values,
                "blacknum=?", new String[]{blackNum});
        sqLiteDatabase.close();

    }

    /**
     * 查询黑名单的拦截模式
     *
     * @param blackNum
     * @return
     */


    public int queryBlackNumMode(String blackNum) {
        int mode = -1;
        SQLiteDatabase sqLiteDatabase = blackNumOpenHelper.getReadableDatabase();
        Cursor cursor = sqLiteDatabase.query(BlackNumOpenHelper.TABLE,
                new String[]{"mode"}, "blacknum=?", new String[]{blackNum}, null, null, null);
        if (cursor.moveToNext()) {
            mode = cursor.getInt(0);
        }
        cursor.close();
        sqLiteDatabase.close();
        return mode;
    }

    /**
     * 删除黑名单号码
     *
     * @param blackNum 要删除的黑名单号码
     */

    public void deleteBlackNum(String blackNum) {
        SQLiteDatabase sqLiteDatabase = blackNumOpenHelper.getWritableDatabase();
        sqLiteDatabase.delete(BlackNumOpenHelper.TABLE, "blacknum=?"
                , new String[]{blackNum});
        sqLiteDatabase.close();
    }

    /**
     * 查询所有的黑名单号码
     *
     * @return
     */

    public List<BlackNumInfo> getAllBlackNum() {
        SystemClock.sleep(200);
        List<BlackNumInfo> infos = new ArrayList<BlackNumInfo>();
        SQLiteDatabase sqLiteDatabase = blackNumOpenHelper.getReadableDatabase();
        Cursor cursor = sqLiteDatabase.query(BlackNumOpenHelper.TABLE,
                new String[]{"blacknum", "mode"}, null, null, null, null, "id desc");
        while (cursor.moveToNext()) {
            String blacknum = cursor.getString(0);
            int mode = cursor.getInt(1);
            BlackNumInfo info = new BlackNumInfo(blacknum, mode);
            infos.add(info);
        }
        cursor.close();
        sqLiteDatabase.close();

        return infos;
    }

    /**
     * 查询部分黑名单
     * @param strtIndex 起始位置
     * @param maxNum  最多查询的数量
     * @return
     */

    public List<BlackNumInfo> getPartBlackNum(int strtIndex, int maxNum) {
        SystemClock.sleep(200);
        List<BlackNumInfo> blackNumInfos = new ArrayList<BlackNumInfo>();
        SQLiteDatabase sqLiteDatabase = blackNumOpenHelper.getReadableDatabase();
        Cursor c = sqLiteDatabase.rawQuery("select blacknum,mode from info order by id desc limit ? offset ?;"
                , new String[]{maxNum + "", strtIndex + ""});
        while (c.moveToNext()) {
            String blacknum = c.getString(0);
            int mode = c.getInt(1);
            BlackNumInfo info = new BlackNumInfo(blacknum, mode);
            blackNumInfos.add(info);
        }
        c.close();
        sqLiteDatabase.close();
        return blackNumInfos;
    }


}
