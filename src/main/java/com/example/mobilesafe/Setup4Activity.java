package com.example.mobilesafe;

import android.content.Intent;
import android.support.annotation.Nullable;
import android.os.Bundle;

public class Setup4Activity extends SetupBaseActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup4);
    }

    @Override
    public void next_activity() {
        Intent intent = new Intent(this,LostFindActivity.class);
        startActivity(intent);
        finish();
       // overridePendingTransition();
    }

    @Override
    public void pre_activity() {
        Intent intent = new Intent(this,Setup3Activity.class);
        startActivity(intent);
        finish();
     //   overridePendingTransition();

    }
}
