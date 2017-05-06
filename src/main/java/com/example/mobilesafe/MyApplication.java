package com.example.mobilesafe;

import android.app.Application;

import org.xutils.x;

/**
 * Created by li on 2017/5/5.
 */

public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        x.Ext.init(this);
    }
}
