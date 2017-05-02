package com.example.mobilesafe.engine;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

import com.example.mobilesafe.domain.ContactsInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by li on 2017/4/23.
 */

public class ContactsEngine {
    public static List<ContactsInfo> getAllContactsInfos(Context context) {
        List<ContactsInfo> contactsInfos = new ArrayList<ContactsInfo>();
        //获取内容解析者
        ContentResolver resolver = context.getContentResolver();
        //内容提供者的路径
        //com.android.contacts 内容提供者的主机地址 raw_contact 代表查询 raw_contantcs data
        Uri rawContactsUri = Uri.parse("content://com.android.contacts/raw_contacts");
        Uri dataContactsUri = Uri.parse("content://com.android.contacts/data");
        Cursor cursor = resolver.query(rawContactsUri, new String[]{"contact_id"},null , null, null);
        int i=0;
        while (cursor.moveToNext()) {
            String id = cursor.getString(0);
            if (id != null) {
                Cursor data = resolver.query(dataContactsUri
                        , new String[]{"data1", "mimetype"},
                        "raw_contact_id=?", new String[]{id}, null);
                ContactsInfo info = new ContactsInfo();
                System.out.println(data);
                    while (data.moveToNext()) {
                        i++;
                        String mimetype = data.getString(data.
                                getColumnIndex("mimetype"));
                        String data1 = data.getString(0);
                        if (mimetype.equals("vnd.android.cursor.item/name")) {
                            // data1 代表是名字
                            System.out.println(data1);
                            info.setName(data1);
                        } else if (mimetype
                                .equals("vnd.android.cursor.item/phone_v2")) {
                            // data1 代表是电话号码
                            System.out.println(data1);
                            info.setNum(data1);
                        }else{
                            System.out.println("*********"+i);
                        }
                    }
                data.close();
                contactsInfos.add(info);
            }
            System.out.println("-----"+i);
        }
        cursor.close();
        return contactsInfos;

    }
}
