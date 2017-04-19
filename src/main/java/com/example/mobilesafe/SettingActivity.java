package com.example.mobilesafe;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.example.mobilesafe.ui.SettingItem;

public class SettingActivity extends AppCompatActivity {
    private SettingItem settingItem;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
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
                if (settingItem.checked()) {
                    settingItem.setChecked(false);
                    editor.putBoolean("update", false);
                    // edit.apply();
                } else {
                    settingItem.setChecked(true);
                    editor.putBoolean("update", true);
                }
                editor.commit();
            }
        });
    }
}
