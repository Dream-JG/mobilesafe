package com.example.mobilesafe;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class LostFindActivity extends AppCompatActivity {
    private SharedPreferences sharedPreferences;
    private TextView tvSafeNUm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sharedPreferences = getSharedPreferences("config",MODE_PRIVATE);
        if(sharedPreferences.getBoolean("first",true)){
            Intent intent = new Intent(this,Setup1Activity.class);
            startActivity(intent);
            finish();
        }else {
            String safeNum = sharedPreferences.getString("safenum","110");
            setContentView(R.layout.activity_lost_find);
            tvSafeNUm= (TextView) findViewById(R.id.tv_safenum);
            tvSafeNUm.setText(safeNum);
        }
    }
    public void resetup(View v){
        Intent intent=new Intent(this, Setup1Activity.class);
        startActivity(intent);
        finish();
    }
}
