package com.example.mobilesafe.db.dao;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.io.File;

/**
 * Created by li on 2017/5/3.
 */

public class AddressDao {
    public String queryAddress(Context context, String num) {
        File file = new File(context.getExternalFilesDir("/"), "address.db");
        String location = "";

        SQLiteDatabase sqLiteDatabase = SQLiteDatabase.
                openDatabase(file.getAbsolutePath(), null, SQLiteDatabase.OPEN_READONLY);
        if (num.matches("1[34578]\\d{9}")) {
            Cursor cursor = sqLiteDatabase.
                    rawQuery("select location from data2 where id=(select outkey from data1 where id =?)",
                            new String[]{num.substring(0, 7)});
            if (cursor.moveToNext()) {
                location = cursor.getString(0);
            }else{
                location="未知号码";
            }
            cursor.close();
        } else {
            switch (num.length()) {
                case 3:
                    location = "特殊号码";
                    break;
                case 4:
                    location = "虚拟号码";
                    break;
                case 5:
                    location = "客服电话";
                    break;
                case 7:
                case 8:
                    location = "本地电话";
                    break;
                default:
                    if (num.length() >= 10 && num.startsWith("0")) {
                        String result = num.substring(1, 3);
                        Cursor cursor = sqLiteDatabase.
                                rawQuery("select location from data2 where area=?", new String[]{result});
                        if ((cursor.moveToNext())) {
                            String string = cursor.getString(0);
                            location = string.substring(0, string.length() - 2);
                            cursor.close();
                        } else {
                            result = num.substring(1, 4);
                            cursor = sqLiteDatabase.
                                    rawQuery("select location from data2 where area=?", new String[]{result});
                            if ((cursor.moveToNext())) {
                                String string = cursor.getString(0);
                                location = string.substring(0, string.length() - 2);
                                cursor.close();
                            }

                        }

                    }
                    break;
            }

        }
        sqLiteDatabase.close();
        return location;
    }
}
