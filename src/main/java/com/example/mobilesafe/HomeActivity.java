package com.example.mobilesafe;


import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mobilesafe.untils.MD5Utils;


public class HomeActivity extends AppCompatActivity {
    private SharedPreferences sharedPreferences;
    private AlertDialog dialog;
    private GridView gridView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        sharedPreferences = getSharedPreferences("config", MODE_PRIVATE);
        gridView = (GridView) findViewById(R.id.gv_home);
        MyGridViewAdapter adapter = new MyGridViewAdapter();
        gridView.setAdapter(adapter);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:
                       if (TextUtils.isEmpty(sharedPreferences.getString("password", ""))) {
                            showSetupDiolog();
                        } else {
                            showEnterDiolog();
                        }
                        break;
                    case 1:
                        Intent intent1 = new Intent(HomeActivity.this,CallSmsSafeActivity.class);
                        startActivity(intent1);
                        break;
                    case 2:
                        Intent intent2 = new Intent(HomeActivity.this,SoftManagerActivity.class);
                        startActivity(intent2);
                        break;
                    case 3:
                        Intent intent3 = new Intent(HomeActivity.this,TaskManagerActivity.class);
                        startActivity(intent3);
                        break;
                    case 7:
                        Intent intent7 = new Intent(HomeActivity.this,AToolsActivity.class);
                        startActivity(intent7);
                        break;
                    case 8:
                        Intent intent8 = new Intent(HomeActivity.this, SettingActivity.class);
                        startActivity(intent8);
                        break;
                    default:
                        break;

                }

            }
        });
    }
    int flag = 0;
    private void showEnterDiolog() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View view = View.inflate(getApplicationContext(), R.layout.dialog_enter, null);
        final EditText pwd = (EditText) view.findViewById(R.id.et_pwd);
        ImageView imageView = (ImageView) view.findViewById(R.id.iv_show);
        Button btOk = (Button) view.findViewById(R.id.ok);
        Button btCancle = (Button) view.findViewById(R.id.cancel);
        imageView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                flag++;
                if (flag % 2 == 0) {
                    pwd.setInputType(129);
                } else {
                    pwd.setInputType(1);
                }

            }
        });
        btOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String password = sharedPreferences.getString("password", "");
                String inPwd = pwd.getText().toString().trim();
                if (password.equals(MD5Utils.digestPwd(inPwd))) {
                    dialog.dismiss();
                    Intent intent = new Intent(HomeActivity.this,LostFindActivity.class);
                    startActivity(intent);
                    Toast.makeText(getApplicationContext(), "密码正确", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getApplicationContext(), "输入密码不正确", Toast.LENGTH_SHORT).show();
                }
            }
        });
        btCancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();

            }
        });
        builder.setView(view);
        dialog = builder.create();
        dialog.show();


    }

    private void showSetupDiolog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View view = View.inflate(getApplicationContext(), R.layout.dialog_setup, null);
        final EditText etPwd = (EditText) view.findViewById(R.id.et_password);
        final EditText etPwdCon = (EditText) view.findViewById(R.id.et_password_confirm);
        Button btOk = (Button) view.findViewById(R.id.ok);
        Button btCancle = (Button) view.findViewById(R.id.cancel);
        btOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String pwd = etPwd.getText().toString().trim();
                String pwdCon = etPwdCon.getText().toString().trim();
                if (TextUtils.isEmpty(pwd)) {
                    Toast.makeText(getApplicationContext(), "密码不能为空", Toast.LENGTH_SHORT).show();
                }
                if (pwd.equals(pwdCon)) {
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("password", MD5Utils.digestPwd(pwd));
                    editor.commit();
                    Toast.makeText(getApplicationContext(), "密码设置成功", Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                } else {
                    Toast.makeText(getApplicationContext(), "两次密码不一致", Toast.LENGTH_SHORT).show();
                }
            }
        });
        btCancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        builder.setView(view);
        dialog = builder.create();
        dialog.show();
        dialog.setCancelable(false);
    }

    private class MyGridViewAdapter extends BaseAdapter {
        private ImageView ivIcon;
        private TextView tvName;
        int[] imageId = {R.drawable.safe, R.drawable.callmsgsafe,
                R.drawable.app, R.drawable.taskmanager, R.drawable.netmanager,
                R.drawable.trojan, R.drawable.sysoptimize, R.drawable.atools,
                R.drawable.settings};
        String[] names = {"手机防盗", "通讯卫士", "软件管理", "进程管理", "流量统计", "手机杀毒",
                "缓存清理", "高级工具", "设置中心"};

        @Override
        public int getCount() {
            return imageId.length;
        }

        @Override
        public Object getItem(int position) {
            return position;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = View.inflate(getApplicationContext(), R.layout.item_home, null);
                ivIcon = (ImageView) convertView.findViewById(R.id.iv_icon);
                tvName = (TextView) convertView.findViewById(R.id.tv_name);
            }
            AbsListView.LayoutParams params = new AbsListView.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT, 250
            );
            convertView.setLayoutParams(params);
            ivIcon.setImageResource(imageId[position]);
            tvName.setText(names[position]);
            return convertView;

        }
    }

}



