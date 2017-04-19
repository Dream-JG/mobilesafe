package com.example.mobilesafe.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.CheckBox;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.mobilesafe.R;

/**
 * Created by li on 2017/4/7.
 */

public class SettingItem extends RelativeLayout {
    private static final String NAMESPACE ="http://schemas.android.com/apk/res-auto";
    private TextView tvTitle;
    private TextView tvDes;
    private CheckBox checkBox;
    private String title;
    private String desOn;
    private String desOff;
    public SettingItem(Context context) {
        super(context);
        init();
    }


    public SettingItem(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
        title=attrs.getAttributeValue(NAMESPACE,"title");
        desOn=attrs.getAttributeValue(NAMESPACE,"desOn");
        desOff=attrs.getAttributeValue(NAMESPACE,"desOff");
        tvTitle.setText(title);
        if(checkBox.isChecked()){
            tvDes.setText(desOn);
        }else {
            tvDes.setText(desOff);
        }

    }

    public SettingItem(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }
    private void init(){
        View view= View.inflate(getContext(), R.layout.setting_item_view,this);
        tvTitle= (TextView) view.findViewById(R.id.tv_setting_title);
        tvDes= (TextView) findViewById(R.id.tv_setting_des);
        checkBox= (CheckBox) findViewById(R.id.cb_setting);
    }

    public void setTvTitle(String title) {
        tvTitle.setText(title);
    }

    public void setTvDes(String des) {
        tvDes.setText(des);

    }

    public void setChecked(boolean flag) {
     checkBox.setSelected(flag);
        if (checkBox.isChecked()){
            tvDes.setText(desOn);
        }else {
            tvDes.setText(desOff);
        }
    }
    public boolean checked(){
        return checkBox.isChecked();
    }
}
