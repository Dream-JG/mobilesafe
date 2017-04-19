package com.example.mobilesafe;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;


import com.example.mobilesafe.untils.StreamUtils;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;


import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class SplashActivity extends AppCompatActivity {
    private SharedPreferences sharedPreferences;
    private static final int MSG_ENTER_HOME = 10;
    private static final int MSG_UPDATE_DIALOG = 20;
    private static final int MSG_SERVER_ERROR = 30;
    protected static final int MSG_IO_ERROR = 40;
    protected static final int MSG_URL_ERROR = 50;
    protected static final int MSG_JSON_ERROR = 60;
    private String code; // 版本号
    private String des; // 版本描述
    private String apkurl;// 新版本的下载地址
    private TextView tvVersionName;
    //String path=Environment.getExternalStorageDirectory().getAbsolutePath();
    private String path ;
//    String path1 = Environment.getDataDirectory().getPath();

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MSG_UPDATE_DIALOG:
                    showUpdateAialog();
                    break;
                case MSG_ENTER_HOME:
                    enterHome();
                    break;
                case MSG_SERVER_ERROR:
                    Toast.makeText(getApplicationContext(),"连接服务器失败",0).show();
                    enterHome();
                case MSG_IO_ERROR:
                    Toast.makeText(getApplicationContext(), "您当前手机网络不给力", Toast.LENGTH_SHORT).show();
                    enterHome();
                    break;
                case MSG_URL_ERROR:
                    Toast.makeText(getApplicationContext(), "错误号:" + MSG_URL_ERROR,
                            Toast.LENGTH_SHORT).show();
                    enterHome();
                    break;
                case MSG_JSON_ERROR:
                    Toast.makeText(getApplicationContext(),
                            "错误号:" + MSG_JSON_ERROR, 0).show();
                    enterHome();
                    break;
                default:
                    break;

            }
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        tvVersionName = (TextView) findViewById(R.id.tv_splash_version);
        tvVersionName.setText("版本号：" + getVersionName());
        path = getExternalFilesDir("/").getAbsolutePath();

//        File file = new File(path);
//        if (!file.exists()) {
//            file.mkdir();
//        }
        sharedPreferences=getSharedPreferences("config",MODE_PRIVATE);
        if (sharedPreferences.getBoolean("update",true)) {
            update();
        }else {
            new Thread(){
                @Override
                public void run() {
                    SystemClock.sleep(2000);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            enterHome();
                        }
                    });
                }
            }.start();
        }
//        Log.i("codecraeer", "getExternalFilesDir = " + getExternalFilesDir("exter_test").getAbsolutePath());
//        Log.i("codecraeer", "getDownloadCacheDirectory = " + Environment.getDownloadCacheDirectory().getAbsolutePath());
//        Log.i("codecraeer", "getDataDirectory = " + Environment.getDataDirectory().getAbsolutePath());
//        Log.i("codecraeer", "getExternalStorageDirectory = " + Environment.getExternalStorageDirectory().getAbsolutePath());
//        Log.i("codecraeer", "getExternalStoragePublicDirectory = " + Environment.getExternalStoragePublicDirectory("pub_test"));
    }

    //弹出对话框
    private void showUpdateAialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(SplashActivity.this);
        builder.setTitle("应用更新了:" + code);
        builder.setIcon(R.mipmap.ic_launcher);
        builder.setMessage(des);
        builder.setCancelable(false);//设置不能直接取消
        builder.setPositiveButton("立刻升级", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                download();
            }
        });
        builder.setNegativeButton("稍后升级", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                enterHome();
            }
        });
        builder.show();
    }

    private void enterHome() {
        Intent intent = new Intent(this, HomeActivity.class);
        startActivity(intent);
        finish();
    }

    //下载新本版
    private void download() {
        HttpUtils httpUtils = new HttpUtils();
        System.out.println("------------" + path);
        httpUtils.download(apkurl, path + "/mobilesafe2.apk", new RequestCallBack<File>() {
            @Override
            public void onSuccess(ResponseInfo<File> responseInfo) {
                Toast.makeText(getApplicationContext(), "下载成功", 0).show();
                installApk();
            }

            @Override
            public void onFailure(HttpException e, String s) {
                e.printStackTrace();
            }

            @Override
            public void onLoading(long total, long current, boolean isUploading) {
                super.onLoading(total, current, isUploading);
                AlertDialog.Builder dBuilder = new AlertDialog.Builder(SplashActivity.this);
                dBuilder.setTitle("正在下载");
                dBuilder.setMessage(current + "/" + total);
            }
        });
    }

    private void installApk() {
//        Intent intent = new Intent();
//        intent.setAction("android.intent.VIEW");
//        intent.addCategory("android.intent.category.DEFAULT");
//        intent.setDataAndType(Uri.fromFile(
//                new File(path+"/mobilesafe2.apk")),"application/vnd.android.package-archive");
//        startActivityForResult(intent,0);
        Intent intent = new Intent();
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setAction(android.content.Intent.ACTION_VIEW);
        intent.setDataAndType(Uri.fromFile(new File(path+"/mobilesafe2.apk")),
                "application/vnd.android.package-archive");
        startActivity(intent);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        enterHome();
    }

    //检查更新
    private void update() {
        new Thread() {
            @Override
            public void run() {
                Message msg = Message.obtain();
                long startTime = System.currentTimeMillis();
                try {
                    URL url = new URL("http://192.168.0.3:8080/updateinfo.html");
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    connection.setConnectTimeout(5000);
                    connection.setRequestMethod("GET");
                    if ((connection.getResponseCode()) == 200) {
                        InputStream inputStream = connection.getInputStream();
                        String json = StreamUtils.parserStream(inputStream);
                        JSONObject jsonObject = new JSONObject(json);
                        code = jsonObject.getString("code");
                        des = jsonObject.getString("des");
                        apkurl = jsonObject.getString("apkurl");
                        System.out.println(code + "---" + des + "---" + apkurl);
                        if (code.equals(getVersionName())) {
                            msg.what = MSG_ENTER_HOME;
                        } else {
                            msg.what = MSG_UPDATE_DIALOG;
                        }
                    } else {
                        msg.what = MSG_SERVER_ERROR;
                    }
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                    msg.what=MSG_URL_ERROR;
                } catch (IOException e) {
                    e.printStackTrace();
                    msg.what=MSG_IO_ERROR;
                } catch (JSONException e) {
                    e.printStackTrace();
                    msg.what=MSG_JSON_ERROR;
                } finally {
                    long ensTime = System.currentTimeMillis();
                    long dTime = ensTime - startTime;
                    if (dTime < 2000) {
                        SystemClock.sleep(2000 - dTime);//保证用户至少看到两秒钟的splash界面
                    }
                    handler.sendMessage(msg);
                }

            }
        }.start();
    }

    //获取当前版本名字
    public String getVersionName() {
        PackageManager packageManager = getPackageManager();
        try {
            PackageInfo packageInfo = packageManager.getPackageInfo(getPackageName(), 0);
            String versionName = packageInfo.versionName;
            return versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return null;
        }

    }
}
