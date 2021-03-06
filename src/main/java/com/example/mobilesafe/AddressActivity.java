package com.example.mobilesafe;

import android.os.Vibrator;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.ViewUtils;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.Interpolator;
import android.widget.EditText;

import android.widget.TextView;

import com.example.mobilesafe.db.dao.AddressDao;

import org.xutils.view.annotation.ViewInject;
import org.xutils.x;


public class AddressActivity extends AppCompatActivity {
    @ViewInject(R.id.et_phonenum)
    private EditText etPhonenum;
    @ViewInject(R.id.tv_address)
    private TextView tvAddress;
    private AddressDao addressDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_address);
        x.view().inject(this);
       // ViewUtils.inject(this);
        addressDao = new AddressDao();
        etPhonenum.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String phonenum = s.toString();
                String address = addressDao.queryAddress(getApplicationContext(), phonenum);
                if (!TextUtils.isEmpty(phonenum)) {
                    tvAddress.setText(address);
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    public void query(View v) {
        String phonenum = etPhonenum.getText().toString().trim();
        if (!TextUtils.isEmpty(phonenum)) {
            String address = addressDao.queryAddress(getApplicationContext(), phonenum);
            tvAddress.setText(address);
        } else {
            Animation shake = AnimationUtils.loadAnimation(AddressActivity.this, R.anim.shake);
            etPhonenum.startAnimation(shake);
            shake.setInterpolator(new Interpolator() {
                @Override
                public float getInterpolation(float x) {
                    return x;  //y=x^2
                }
            });
        }

    }
}
