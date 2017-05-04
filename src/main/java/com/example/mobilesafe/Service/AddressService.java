package com.example.mobilesafe.Service;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.PixelFormat;
import android.os.IBinder;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.example.mobilesafe.R;
import com.example.mobilesafe.db.dao.AddressDao;
import com.lidroid.xutils.view.annotation.ViewInject;

public class AddressService extends Service {
    private TelephonyManager manager;
    private AddressDao dao;
    private SharedPreferences sharedPreferences;
    private WindowManager wm;
    private View view;
    private WindowManager.LayoutParams params;
    private MyPhoneStateListener myPhoneStareLierner;
    private OutGoingCallReceive callReceive;

    public AddressService() {
    }


    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        //throw new UnsupportedOperationException("Not yet implemented");
        return null;
    }


    private class OutGoingCallReceive extends BroadcastReceiver{

        @Override
        public void onReceive(Context context, Intent intent) {
            System.out.println("广播接受者");
            String resultData = getResultData();
            System.out.println(resultData);
            String address = dao.queryAddress(getApplicationContext(),resultData);
            showMyToast(address);

        }
    }

    @Override
    public void onCreate() {
        super.onCreate();
        sharedPreferences = getSharedPreferences("config",MODE_PRIVATE);
        dao = new AddressDao();
        callReceive = new OutGoingCallReceive();
        IntentFilter filter = new IntentFilter();
        filter.addAction("android.intent.action.NEW_OUTGOING_CALL");
        registerReceiver(callReceive,filter);

        manager = (TelephonyManager) getSystemService(TELECOM_SERVICE);
        myPhoneStareLierner = new MyPhoneStateListener();
        manager.listen(myPhoneStareLierner,PhoneStateListener.LISTEN_CALL_STATE);


    }

    private class MyPhoneStateListener extends PhoneStateListener {
        //如果监听电话状态  会在该方法回调
        //incomingNumber 来电号码
        //state 电话状态
        @Override
        public void onCallStateChanged(int state, String incomingNumber) {
            super.onCallStateChanged(state, incomingNumber);
            switch (state) {
                case TelephonyManager.CALL_STATE_IDLE: //电话空闲的状态
                    hideToast();
                    break;
                case TelephonyManager.CALL_STATE_RINGING: //响铃状态
                    String address = dao.queryAddress(getApplicationContext(), incomingNumber);
                    showMyToast(address);
                    break;
                case TelephonyManager.CALL_STATE_OFFHOOK:  //通话的状态
                    break;
                default:
                    break;

            }
        }
    }

        public void showMyToast(String address) {
//            int[] bgcolor = new int[]{R.drawable.call_locate_white,
//                    R.drawable.call_locate_orange, R.drawable.call_locate_blue,
//                    R.drawable.call_locate_gray, R.drawable.call_locate_green};
            int [] bgcolor = new int[]{
            };
            wm = (WindowManager) getSystemService(WINDOW_SERVICE);
            view = View.inflate(getApplicationContext(), R.layout.toast_custom, null);
            view.setBackgroundResource(bgcolor[sharedPreferences.getInt("toastcolor", 0)]);
            TextView tvAddress = (TextView) view.findViewById(R.id.tv_address);
            tvAddress.setText(address);
            touch();
            params = new WindowManager.LayoutParams();
            params.height = WindowManager.LayoutParams.WRAP_CONTENT;
            params.width = WindowManager.LayoutParams.WRAP_CONTENT;
            params.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE //没有焦点
                    | WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON; // 保持屏幕长亮
            params.format = PixelFormat.TRANSLUCENT;//半透明
            params.type = WindowManager.LayoutParams.TYPE_PRIORITY_PHONE; // 高优先级的弹出框
            params.gravity = Gravity.LEFT | Gravity.TOP; // 以左上角对齐
            params.x = sharedPreferences.getInt("x", 100);
            params.y = sharedPreferences.getInt("y", 100);
            wm.addView(view, params);

        }




    private void touch() {
        view.setOnTouchListener(new View.OnTouchListener() {
            private int startX;
            private int startY;
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()){
                    case MotionEvent.ACTION_DOWN://手指按下的事件
                        startX = (int) event.getRawX();
                        startX = (int) event.getRawY();
                        break;
                    case MotionEvent.ACTION_MOVE:   //手指移动的事件
                        int newX = (int) event.getRawX();
                        int newY = (int) event.getRawY();
                        //计算偏移量
                        int dX = newX - startX;
                        int dY = newY - startY;
                        params.x+=dX;
                        params.y+=dY;
                        wm.updateViewLayout(view,params);
                        startX = newX;
                        startY = newY;
                        break;

                    case MotionEvent.ACTION_UP: //手指抬起的事件
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putInt("x",params.x);
                        editor.putInt("y",params.y);
                        editor.commit();
                        break;
                    default:
                        break;

                }
                return true;
            }
        });
    }

    private void hideToast() {
        if(wm!=null&&view!=null){
            wm.removeView(view);
            wm = null;
            view = null;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

    }
}
