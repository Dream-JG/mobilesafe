package com.example.mobilesafe;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class LostFindActivity extends AppCompatActivity {
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sharedPreferences = getSharedPreferences("config",MODE_PRIVATE);
        if(sharedPreferences.getBoolean("first",true)){
            Intent intent = new Intent(this,Setup2Activity.class);
            startActivity(intent);
           // finish();
        }else {

            setContentView(R.layout.activity_lost_find);
        }
    }
}
