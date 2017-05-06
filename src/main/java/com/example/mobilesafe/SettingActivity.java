package com.example.mobilesafe;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.app.NotificationCompat;
import android.view.View;

import com.example.mobilesafe.Service.AddressService;
import com.example.mobilesafe.ui.SettingClickItem;
import com.example.mobilesafe.ui.SettingItem;
import com.example.mobilesafe.untils.ServiceUtils;

import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

public class SettingActivity extends AppCompatActivity {
    @ViewInject(R.id.si_update)
    private SettingItem siUpdate;
    @ViewInject(R.id.si_sercice)
    private SettingItem siService;
    @ViewInject(R.id.sci_location)
    private SettingClickItem sciLocation;
    @ViewInject(R.id.sci_address_bg)
    private SettingClickItem sciAddressBg;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        x.view().inject(this);
        sharedPreferences = getSharedPreferences("config", Context.MODE_PRIVATE);
        update();
        changeBg();
        changeLoaction();
    }

    private void changeLoaction() {
        sciLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), DragViewActivity.class);
                startActivity(intent);
            }
        });
    }

    private void changeBg() {
        final String[] bgItems = {"半透明", "活力橙", "卫士蓝", "金属灰", "苹果绿"};
        sciAddressBg.setDes(bgItems[sharedPreferences.getInt("toastcolor", 0)]);
        sciAddressBg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(SettingActivity.this);
                builder.setIcon(R.mipmap.ic_launcher);
                builder.setTitle("归属地提示框风格");
                builder.setSingleChoiceItems(bgItems, sharedPreferences.getInt("toastcolor", 0), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putInt("toastcolor", which);
                        editor.commit();
                        dialog.dismiss();
                        sciAddressBg.setDes(bgItems[sharedPreferences.getInt("toastcolor", 0)]);
                    }
                });
                builder.setNegativeButton("取消", null);
                builder.show();
            }
        });

    }

    private void update() {
        if (sharedPreferences.getBoolean("update", true)) {
            siUpdate.setChecked(true);
        }
        {
            siUpdate.setChecked(false);
        }
        siUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences.Editor editor = sharedPreferences.edit();
                if (siUpdate.checked()) {
                    //  handler.sendEmptyMessage(0);
                    siUpdate.setChecked(false);

                    editor.putBoolean("update", false);
                    // edit.apply();
                } else {
                    //handler.sendEmptyMessage(1);
                    siUpdate.setChecked(true);

                    editor.putBoolean("update", true);
                }
                editor.commit();
            }
        });
    }


    private void addressSercive() {
        if (ServiceUtils.isServiceRunning("com.example.mobilesafe.Service.AddressService", SettingActivity.this)) {
            siService.setChecked(true);
        } else {
            siService.setChecked(false);
        }
        siService.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), AddressService.class);
                if (siService.checked()) {
                    stopService(intent);
                    System.out.println("服务关闭了");
                    siService.setChecked(false);
                } else {
                    startService(intent);
                    System.out.println("服务开启了");
                    boolean flag = ServiceUtils.isServiceRunning("com.example.mobilesafe.Service.AddressService", SettingActivity.this);
                    System.out.println(flag);
                    siService.setChecked(true);
                }
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        addressSercive();
    }
}
