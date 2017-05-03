package com.example.mobilesafe.receiver;

import android.app.admin.DevicePolicyManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;
import android.text.TextUtils;

import com.example.mobilesafe.R;
import com.example.mobilesafe.Service.GPSService;

public class SmsReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        SharedPreferences sharedPreferences = context
                .getSharedPreferences("config", Context.MODE_PRIVATE);
        DevicePolicyManager devicePolicyManager = (DevicePolicyManager) context.
                getSystemService(Context.DEVICE_POLICY_SERVICE);
        Object[] objects = (Object[]) intent.getExtras().get("pdus");
        for (Object o : objects) {
            SmsMessage smsMessage = SmsMessage.createFromPdu((byte[]) o);
            String body = smsMessage.getMessageBody();
            String sender = smsMessage.getOriginatingAddress();
            System.out.println(body + "----" + sender);
            if ("#*location*#".equals(body)) {
                System.out.println("定位");
                Intent intent2 = new Intent(context, GPSService.class);
                context.startService(intent2);

                String latitude = sharedPreferences.getString("latitude", "");
                String longitude = sharedPreferences.getString("longitude", "");
                System.out.println(latitude);
                System.out.println(longitude);
                if (!TextUtils.isEmpty(latitude)
                        && !TextUtils.isEmpty(longitude)) {
                    SmsManager smsManager = SmsManager.getDefault();
                    smsManager.sendTextMessage(sharedPreferences.getString("safenum", "5556"),
                            null, "latitude:" + latitude + "....longtitude:"
                                    + longitude, null, null);
                }
                abortBroadcast();
            }else if("#*alarm*#".equals(body)){
                System.out.println("播放报警音乐");
                AudioManager audioManager = (AudioManager) context.
                        getSystemService(Context.AUDIO_SERVICE);
                int max = audioManager
                        .getStreamMaxVolume(AudioManager.STREAM_MUSIC);
                // 参数1 声音的类型 参数2 声音的大小
                audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, max, 0);
                // 创建了一个音乐资源
                MediaPlayer mediaPlayer = MediaPlayer.create(context, R.raw.ylzs);
                mediaPlayer.start();
            }else if ("#*wipe*#".equals(body)){
                System.out.println("远程擦除数据");
            }else if ("#*lockscreen*#".equals(body)) {
                System.out.println("远程锁屏");
                ComponentName componentName = new ComponentName(context,
                        Admin.class);
                if (devicePolicyManager.isAdminActive(componentName)) {
                    devicePolicyManager.lockNow();// 锁屏了
                }
                abortBroadcast();
            }

        }

        // TODO: This method is called when the BroadcastReceiver is receiving
        // an Intent broadcast.
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
