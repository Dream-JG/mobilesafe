package com.example.mobilesafe;

import android.content.Intent;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class Setup3Activity extends SetupBaseActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup3);
    }

    @Override
    public void next_activity() {
        Intent intent = new Intent(this,Setup4Activity.class);
        startActivity(intent);
       // finish();
        //overridePendingTransition();
    }

    @Override
    public void pre_activity() {
        Intent intent = new Intent(this,Setup2Activity.class);
        startActivity(intent);
        //finish();
       // overridePendingTransition();

    }
}
