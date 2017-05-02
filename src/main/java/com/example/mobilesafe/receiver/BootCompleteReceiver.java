package com.example.mobilesafe.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.telephony.SmsManager;
import android.telephony.TelephonyManager;

/**
 * Created by li on 2017/4/20.
 */

public class BootCompleteReceiver extends BroadcastReceiver {
    private SharedPreferences sharedPreferences;
    @Override
    public void onReceive(Context context, Intent intent) {
        System.out.println("手机开机了。。。。。");
        sharedPreferences= context.getSharedPreferences("config",Context.MODE_PRIVATE);
        String sim = sharedPreferences.getString("sim","");
        TelephonyManager telManager = (TelephonyManager) context.getSystemService(Context.TELECOM_SERVICE);
        String simSrialNuber = telManager.getSimSerialNumber();
        if(!sim.equals(simSrialNuber)){
            // 获取短信的管理者 用来 发送短信
            // 4.4 以后 不能这么发短信了
            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage(sharedPreferences.getString("safenum","18838107889"),null,
                    "shou ji bei tou le !!",null,null);

        }


    }
}
