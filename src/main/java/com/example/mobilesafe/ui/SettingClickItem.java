package com.example.mobilesafe.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.mobilesafe.R;


/**
 * Created by li on 2017/4/7.
 */

public class SettingClickItem extends RelativeLayout {
    private static final String NAMESPACE ="http://schemas.android.com/apk/res-auto";
    private TextView tvTitle;
    private TextView tvDes;
    private ImageView imageView;
    private String cliecktitle;
    private String des;

    public SettingClickItem(Context context) {
        super(context);
        init();
    }


    public SettingClickItem(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
        cliecktitle=attrs.getAttributeValue(NAMESPACE,"cliecktitle");
        des=attrs.getAttributeValue(NAMESPACE,"des");

        tvTitle.setText(cliecktitle);
        tvDes.setText(des);


    }

    public SettingClickItem(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }
    private void init(){
        View view= View.inflate(getContext(), R.layout.setting_item_clickview,this);
        tvTitle= (TextView) view.findViewById(R.id.tv_setting_title);
        tvDes= (TextView) view.findViewById(R.id.tv_setting_des);
        imageView= (ImageView) view.findViewById(R.id.iv_setting);

    }

    public void cliecktitle(String title) {
        tvTitle.setText(title);
    }

    public void setDes(String des) {
        tvDes.setText(des);

    }
}
