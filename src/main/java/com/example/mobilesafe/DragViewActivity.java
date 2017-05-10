package com.example.mobilesafe;

import android.content.SharedPreferences;
import android.os.SystemClock;
import android.support.annotation.IntegerRes;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

public class DragViewActivity extends AppCompatActivity {
    @ViewInject(R.id.ll_toast)
    private LinearLayout llToast;
    private SharedPreferences sharedPreferences;
    private int widthPixels;
    private int heightPixels;
    @ViewInject(R.id.tv_top)
    private TextView tvTop;
    @ViewInject(R.id.tv_bottom)
    private TextView tvBottom;
    long[] mHits = new long[2];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drag_view);
        sharedPreferences = getSharedPreferences("config", MODE_PRIVATE);
        x.view().inject(this);

        WindowManager windowManager = (WindowManager) getSystemService(WINDOW_SERVICE);
        DisplayMetrics outMetrics = new DisplayMetrics();
        windowManager.getDefaultDisplay().getMetrics(outMetrics);
        widthPixels = outMetrics.widthPixels;
        heightPixels = outMetrics.heightPixels;

        int x = sharedPreferences.getInt("x", 0);
        int y = sharedPreferences.getInt("y", 0);
        System.out.println(x + "---" + y);
        if(x<0||y>widthPixels||y<0||y>heightPixels-25){
            x=widthPixels/2;
            y=0;
        }

        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) llToast.getLayoutParams();
        params.leftMargin = x;
        params.topMargin = y;
        llToast.setLayoutParams(params);

        
        touch();
        click();

    }

    private void click() {
        llToast.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.arraycopy(mHits,1,mHits,0,mHits.length-1);
                mHits[mHits.length-1] = SystemClock.uptimeMillis();
                if(mHits[0]>=(SystemClock.uptimeMillis()-500)){
                    int l=(widthPixels-llToast.getWidth())/2;
                    int t=(heightPixels-25-llToast.getHeight())/2;
                    llToast.layout(l, t, l+llToast.getWidth(),t+llToast.getHeight());
                    SharedPreferences.Editor edit = sharedPreferences.edit();
                    edit.putInt("x", l);
                    edit.putInt("y", t);
                    edit.commit();
                }
            }
        });
    }

    private void touch() {
        llToast.setOnTouchListener(new View.OnTouchListener() {

            private int startX;
            private int startY;

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:  // 手指按下的事件
                        //步骤1
                        startX = (int) event.getRawX();
                        startY = (int) event.getRawY();
                        break;
                    case MotionEvent.ACTION_MOVE://  手指移动的事件
                        // 步骤2  记录一个新的位置
                        int newX=(int) event.getRawX();
                        int newY = (int) event.getRawY();
                        // 步骤3   计算移动的偏移量
                        int dX=newX-startX;
                        int dY=newY-startY;
                        // 步骤 4  让控件移动偏移量  重新分配位置
                        int l=llToast.getLeft();
                        int t=llToast.getTop();
                        l=l+dX;
                        t=t+dY;
                        int r=l+llToast.getWidth();
                        int b=t+llToast.getHeight();
                        if(l<0||r>widthPixels||t<0||b>heightPixels-25){
                            break;
                        }
                        llToast.layout(l, t, r,b);
                        int top = llToast.getTop();
                        if(top>heightPixels/2){
                            tvTop.setVisibility(View.VISIBLE);
                            tvBottom.setVisibility(View.INVISIBLE);
                        }else{
                            tvTop.setVisibility(View.INVISIBLE);
                            tvBottom.setVisibility(View.VISIBLE);
                        }
                        // 步骤5  更换开始的位置
                        startX=newX;
                        startY=newY;

                        break;
                    case MotionEvent.ACTION_UP://  手指抬起的事件
                        SharedPreferences.Editor edit = sharedPreferences.edit();
                        edit.putInt("x", llToast.getLeft());
                        edit.putInt("y", llToast.getTop());
                        edit.commit();
                        break;
                    default:
                        break;
                }
                //True if the listener has consumed the event, false otherwise
                // true的时候 把事件给处理掉了  false 没有处理掉
                return false;
            }
        });
    }
}
