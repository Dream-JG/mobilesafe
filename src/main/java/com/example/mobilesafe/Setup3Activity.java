package com.example.mobilesafe;

import android.content.ContentResolver;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.SystemClock;
import android.provider.Contacts;
import android.support.annotation.Nullable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;

public class Setup3Activity extends SetupBaseActivity {
    EditText etSafeNum;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup3);
        etSafeNum = (EditText) findViewById(R.id.et_safenum);
        String num = sharedPreferences.getString("safenum", "");
        etSafeNum.setText(num);

    }

    public void selectContacts(View v) {
        Intent intent = new Intent(this,ContactsActivity.class);
        startActivityForResult(intent,0);
//        Intent intent = new Intent();
//        intent.setAction("android.intent.action.PICK");
//        intent.addCategory("android.intent.category.DEFAULT");
//        intent.setType("vnd.adnroid.cursor.dir/phone_v2");
//        Intent intent = new Intent();
//        intent.setAction(Intent.ACTION_PICK);
//        intent.setData(Contacts.People.CONTENT_URI);
//        //startActivity(intent);
//        startActivityForResult(intent, 1);
//        Intent intent = new Intent(Intent.ACTION_INSERT_OR_EDIT);
//        intent.setType("vnd.android.cursor.item/person");
//        intent.setType("vnd.android.cursor.item/contact");
//        intent.setType("vnd.android.cursor.item/raw_contact");
//        intent.putExtra(android.provider.ContactsContract.Intents.Insert.NAME,"");
//        intent.putExtra(android.provider.ContactsContract.Intents.Insert.COMPANY,"");
//        intent.putExtra(android.provider.ContactsContract.Intents.Insert.PHONE, "");
//        intent.putExtra(android.provider.ContactsContract.Intents.Insert.PHONE_TYPE, 3);
//        startActivityForResult(intent, 1);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(data!=null){
            String num = data.getStringExtra("num");
            etSafeNum.setText(num);
        }
//        if(data!=null){
//            Uri uri = data.getData();
//            String num = null;
//            ContentResolver contentResolver =getContentResolver();
//            Cursor cursor = contentResolver.query(uri,null,null,null,null);
//            while (cursor.moveToNext()){
//                num=cursor.getString(cursor.getColumnIndex("data1"));
//            }
//            cursor.close();
//            num=num.replaceAll("-","");
//            System.out.println("1111111"+num);
//            etSafeNum.setText(num);
//        }
    }

    @Override
    public void next_activity() {
        String sNum = etSafeNum.getText().toString().trim();
        if (TextUtils.isEmpty(sNum)) {
            return;
        }
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("safenum", sNum);
        editor.commit();

        Intent intent = new Intent(this, Setup4Activity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.tran_next_enter, R.anim.tran_next_exit);
        finish();
    }

    @Override
    public void pre_activity() {
        Intent intent = new Intent(this, Setup2Activity.class);
        startActivity(intent);
        finish();
        overridePendingTransition(R.anim.tran_pre_enter, R.anim.tran_pre_exit);

    }
}
