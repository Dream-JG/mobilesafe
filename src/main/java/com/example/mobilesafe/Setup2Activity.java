package com.example.mobilesafe;

import android.content.Intent;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.os.Bundle;

public class Setup2Activity extends SetupBaseActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup2);
    }

    @Override
    public void next_activity() {
        Intent intent = new Intent(this,Setup3Activity.class);
        startActivity(intent);
       // finish();
       // overridePendingTransition();
    }

    @Override
    public void pre_activity() {
        Intent intent = new Intent(this, Setup1Activity.class);
        startActivity(intent);
      //  finish();
        //overridePendingTransition();

    }
}
