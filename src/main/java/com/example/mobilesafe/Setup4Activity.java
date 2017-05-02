package com.example.mobilesafe;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.Nullable;
import android.os.Bundle;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

public class Setup4Activity extends SetupBaseActivity {
    private SharedPreferences sharedPreferences;
    @ViewInject(R.id.cb_lostfind)
    private CheckBox cbLostfind;

    @Override
    protected void onCreate(@Nullable final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sharedPreferences= getSharedPreferences("config",MODE_PRIVATE);
        setContentView(R.layout.activity_setup4);
        ViewUtils.inject(this);
        if (sharedPreferences.getBoolean("protected", false)) {
            cbLostfind.setChecked(true);
            cbLostfind.setText("你开启了手机防盗保护");
        }else {
            cbLostfind.setChecked(false);
            cbLostfind.setText("你没有开启手机防盗保护");
        }
        cbLostfind.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                SharedPreferences.Editor editor = sharedPreferences.edit();
                if(isChecked){
                    cbLostfind.setText("你开启了手机防盗保护");
                    editor.putBoolean("protected",true);
                }else{
                    cbLostfind.setText("你没有开启手机防盗保护");
                    editor.putBoolean("protected",false);
                }
                editor.commit();
            }
        });

    }

    @Override
    public void next_activity() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("first",false);
        editor.commit();
        Intent intent = new Intent(this,LostFindActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.tran_next_enter,R.anim.tran_next_exit);
        finish();
    }

    @Override
    public void pre_activity() {
        Intent intent = new Intent(this,Setup3Activity.class);
        startActivity(intent);
        finish();
        overridePendingTransition(R.anim.tran_pre_enter,R.anim.tran_pre_exit);

    }
}
