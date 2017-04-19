package com.example.mobilesafe;


import android.content.Intent;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.os.Bundle;


/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class Setup1Activity extends SetupBaseActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup1);
    }

    @Override
    public void next_activity() {
        Intent intent = new Intent(this,Setup2Activity.class);
        startActivity(intent);
        finish();
        //overridePendingTransition();
    }

    @Override
    public void pre_activity() {
        finish();

    }
}
