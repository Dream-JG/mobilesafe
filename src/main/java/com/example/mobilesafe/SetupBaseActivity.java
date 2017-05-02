package com.example.mobilesafe;

import android.app.Activity;
import android.content.SharedPreferences;
import android.content.SyncStatusObserver;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.GestureDetector;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;
import android.view.GestureDetector.SimpleOnGestureListener;

/**
 * Created by li on 2017/4/18.
 */

public abstract class SetupBaseActivity extends AppCompatActivity {
    protected SharedPreferences sharedPreferences;
    private GestureDetector detector;//手势识别器

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sharedPreferences = getSharedPreferences("config",MODE_PRIVATE);
        detector= new GestureDetector(this,new MySimpleOnGestureListener());
    }

    // @Override
//    public void onCreate(@Nullable Bundle savedInstanceState, @Nullable PersistableBundle persistentState) {
//        super.onCreate(savedInstanceState, persistentState);
//        sharedPreferences = getSharedPreferences("config",MODE_PRIVATE);
//        detector= new GestureDetector(this,new MySimpleOnGestureListener());
//    }
    private class MySimpleOnGestureListener extends SimpleOnGestureListener {
        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            float startY = e1.getRawY();
            float startX = e1.getRawX();
            float endY = e2.getRawY();
            float endX = e2.getRawX();
            if (Math.abs(endY-startY)>50){
                Toast.makeText(getApplicationContext(),"手势错误",Toast.LENGTH_SHORT).show();
                return  true;
            }
            if(endX-startX>100){
                pre_activity();
            }else  if(startX-endX>100){
                next_activity();
            }

            return true;
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
     //   System.out.println("***************"+event);
        detector.onTouchEvent(event);//把手势识别注册到屏幕的触摸事件里
        //super.onTouchEvent(event);
        return super.onTouchEvent(event);
    }

    public void pre(View v){
        pre_activity();
    }
    public void next(View v){
        next_activity();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode==KeyEvent.KEYCODE_BACK){
            pre_activity();
            return true;
        }
        return super.onKeyDown(keyCode, event);

    }

    public  abstract void next_activity();
    public  abstract void pre_activity();
}
