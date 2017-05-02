package com.example.mobilesafe;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.Nullable;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import com.example.mobilesafe.ui.SettingItem;

public class Setup2Activity extends SetupBaseActivity {
    private SettingItem siBind;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup2);
        siBind= (SettingItem) findViewById(R.id.si_bind);
        String string = sharedPreferences.getString("sim","");
        if(TextUtils.isEmpty(string)){
            siBind.setChecked(false);
        }else{
            siBind.setChecked(true);
        }
        siBind.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences.Editor editor = sharedPreferences.edit();
                if(!(siBind.checked())){
                    //获取SIM卡的串号
                   // TelephonyManager telManager = (TelephonyManager) getSystemService(TELECOM_SERVICE);
                    //String simSerialNumber = telManager.getSimSerialNumber();
                    //System.out.println(simSerialNumber);
                    //editor.putString("sim",simSerialNumber);
                    editor.putString("sim","111");
                    siBind.setChecked(true);
                }else {
                    siBind.setChecked(false);
                    editor.putString("sim","");
                }
                editor.commit();
            }
        });
    }

    @Override
    public void next_activity() {
        if (siBind.checked()){
        Intent intent = new Intent(this,Setup3Activity.class);
        startActivity(intent);
        finish();
        overridePendingTransition(R.anim.tran_next_enter,R.anim.tran_next_exit);
        }else {
            Toast.makeText(getApplicationContext(),"请先绑定sim卡",Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void pre_activity() {
        Intent intent = new Intent(this, Setup1Activity.class);
        startActivity(intent);
        finish();
        overridePendingTransition(R.anim.tran_pre_enter,R.anim.tran_pre_exit);

    }
}
