package com.example.mobilesafe;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.example.mobilesafe.ui.SettingItem;

public class SettingActivity extends AppCompatActivity {
    private SettingItem settingItem;
    private SharedPreferences sharedPreferences;

//    Handler handler = new Handler(){
//        @Override
//        public void handleMessage(Message msg) {
//            switch (msg.what){
//                case 0:
//                    settingItem.setChecked(false);
//                    break;
//                case 1:
//                    settingItem.setChecked(true);
//            }
//        }
//    };
    @Override
    protected void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        settingItem= (SettingItem) findViewById(R.id.si_update);
        sharedPreferences=getSharedPreferences("config", Context.MODE_PRIVATE);
        if(sharedPreferences.getBoolean("update",true)){
            settingItem.setChecked(true);
        }{
            settingItem.setChecked(false);
        }
        settingItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences.Editor editor = sharedPreferences.edit();
                System.out.println("!!!!!!!!!!!!!"+settingItem.checked());
                if (settingItem.checked()) {
                  //  handler.sendEmptyMessage(0);
                    settingItem.setChecked(false);

                    editor.putBoolean("update", false);
                    // edit.apply();
                } else {
                    //handler.sendEmptyMessage(1);
                    settingItem.setChecked(true);

                    editor.putBoolean("update", true);
                }
                editor.commit();
            }
        });
    }
}
