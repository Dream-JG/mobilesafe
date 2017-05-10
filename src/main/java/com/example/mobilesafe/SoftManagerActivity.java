package com.example.mobilesafe;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.ScaleAnimation;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mobilesafe.domain.AppInfo;
import com.example.mobilesafe.engine.AppEngine;
import com.example.mobilesafe.untils.AppUtils;

import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;

public class SoftManagerActivity extends AppCompatActivity implements View.OnClickListener {
    @ViewInject(R.id.lv_apps)
    private ListView lvApps;
    @ViewInject(R.id.pb_apps)
    private ProgressBar progress;
    @ViewInject(R.id.tvProgress)
    private TextView tvCount;
    @ViewInject(R.id.tv_rom)
    private TextView tvRom;
    @ViewInject(R.id.tv_sd)
    private TextView tvSD;
    private List<AppInfo> allAppInfoList;
    private List<AppInfo> userInfos;
    private List<AppInfo> systemInfos;
    AppInfo appInfo;
    private PopupWindow window;
    private AppAdapter adapter;



    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_soft_manager);
        x.view().inject(this);
        new LoadData().execute();
        scoll();
        setClickListenet();
        tvRom.setText("手机运行内存:"+ AppUtils.getRomAvailable(getApplication()));
        tvSD.setText("手机存储空间:"+AppUtils.getSDAvilable(getApplication()));




    }

    private void setClickListenet() {
        lvApps.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0|| position==userInfos.size()){
                    return;
                }
                if (position <= userInfos.size()) {
                    appInfo = userInfos.get(position - 1); // 在用户程序的集合中取出来一个对象
                } else {
                    // 从系统程序的集合中取出来一个对象
                    appInfo = systemInfos.get(position - userInfos.size() - 2);
                }
                //隐藏窗口
                hidePopupWindow();
                View contentView = View.inflate(getApplicationContext(),R.layout.popu_window,null);
                LinearLayout llUnintall = (LinearLayout) contentView.findViewById(R.id.ll_uninstall);
                LinearLayout llStart = (LinearLayout) contentView.findViewById(R.id.ll_start);
                LinearLayout llShare = (LinearLayout) contentView.findViewById(R.id.ll_share);
                LinearLayout llDetail = (LinearLayout) contentView.findViewById(R.id.ll_detail);
                llUnintall.setOnClickListener(SoftManagerActivity.this);
                llStart.setOnClickListener(SoftManagerActivity.this);
                llShare.setOnClickListener(SoftManagerActivity.this);
                llDetail.setOnClickListener(SoftManagerActivity.this);
                //创建PopupWindow
                window = new PopupWindow(contentView, WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT);
                //添加透明背景
                window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                 int[] location = new int[2];
                view.getLocationInWindow(location);
                int x= location[0];
                int y = location[1];
                window.showAtLocation(parent, Gravity.LEFT|Gravity.TOP,x+200,y);
                // 前四个参数 x和y 分别从0变成控件本身的大小 后四个参数 基于左面中间的点缩放
                ScaleAnimation animation = new ScaleAnimation(0,1,0,1,Animation.RELATIVE_TO_SELF
                ,0,Animation.RELATIVE_TO_SELF,0.5f);
                //渐变的动画
                animation.setDuration(500);
                AlphaAnimation alphaAnimation = new AlphaAnimation(0.4f,1);
                alphaAnimation.setDuration(500);
                //创建了一个动画的集合
                AnimationSet animationSet = new AnimationSet(true);
                animationSet.addAnimation(animation);
                animationSet.addAnimation(alphaAnimation);
                contentView.startAnimation(animationSet);

            }
        });


    }

    private void hidePopupWindow() {
        if (window != null) {
            window.dismiss();// 隐藏;
            window = null;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
           case  R.id.ll_uninstall:
               uninstall();
            //卸载
            break;
            case R.id.ll_share:
                //分享
                share();
                break;
            case R.id.ll_start:
                start();
                //启动
                break;
            case R.id.ll_detail:
                //详细信息
                detail();
                break;

        }

    }

    private void share() {
        Intent intent = new Intent("android.intent.action.SEND");
        intent.setType("text/plain");
        intent.addCategory("android.intent.category.DEFAULT");
        intent.putExtra(Intent.EXTRA_TEXT, "推荐您使用一款应用程序:" + appInfo.getName()
                + "下载地址,google市场");

        startActivity(intent);
    }

    private void detail() {
        Intent intent = new Intent(
                "android.settings.APPLICATION_DETAILS_SETTINGS");
        intent.setData(Uri.parse("package:" + appInfo.getPackageName()));
        startActivity(intent);
    }

    private void start() {
        PackageManager pm = getPackageManager();
        Intent launchIntentForPackage = pm.getLaunchIntentForPackage(appInfo.getPackageName());
        if (launchIntentForPackage!=null){
            startActivity(launchIntentForPackage);
        }else{
            Toast.makeText(getApplicationContext(),"系统程序无法启动",Toast.LENGTH_SHORT).show();
        }
    }

    private void uninstall() {
        if(appInfo.isUser()){
            if(appInfo.getPackageName().equals(getPackageName())){

                Toast.makeText(getApplicationContext(),"卸载当前应用就不能保护你的手机了",Toast.LENGTH_SHORT).show();
            }else {
                Intent intent = new Intent();
                intent.setAction("android.intent.action.DELETE");
                intent.addCategory("android.intent.category.DEFAULT");
                intent.setData(Uri.parse("package:" + appInfo.getPackageName()));
                startActivityForResult(intent, 0);
            }
        }else{
            Toast.makeText(getApplicationContext(),"系统应用不能卸载",Toast.LENGTH_SHORT).show();
        }

    }

    public class LoadData extends AsyncTask {


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progress.setVisibility(View.VISIBLE);
        }

        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);
            progress.setVisibility(View.INVISIBLE);
            if (adapter == null) {
                adapter = new AppAdapter();
                lvApps.setAdapter(adapter);
            } else {
                adapter.notifyDataSetChanged();
            }
        }

        @Override
        protected Object doInBackground(Object[] params) {
            allAppInfoList = AppEngine.getAllAppInfos(SoftManagerActivity.this);
            userInfos = new ArrayList<AppInfo>();
            systemInfos = new ArrayList<AppInfo>();
            for (AppInfo appInfo : allAppInfoList) {
                if (appInfo.isUser()) {
                    userInfos.add(appInfo);
                } else {
                    systemInfos.add(appInfo);
                }
            }

            return null;
        }
    }

    public void scoll(){
        lvApps.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                hidePopupWindow();

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if(userInfos!=null && systemInfos!=null){
                    if(firstVisibleItem>=userInfos.size()+1){
                        tvCount.setText("系统程序"+systemInfos.size()+"个");
                    }else{
                        tvCount.setText("用户程序"+userInfos.size()+"个");
                    }
                }

            }
        });
    }




    private class AppAdapter extends BaseAdapter{

        @Override
        public int getCount() {
            return systemInfos.size()+userInfos.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view;
            ViewHolder viewHolder;
            if (position == 0) {
                TextView tv = new TextView(getApplicationContext());
                tv.setBackgroundColor(Color.GRAY);
                tv.setTextColor(Color.WHITE);
                tv.setText("用户程序" + userInfos.size() + "个");
                return tv;
            } else if (position == userInfos.size() + 1) {
                TextView tv = new TextView(getApplicationContext());
                tv.setBackgroundColor(Color.GRAY);
                tv.setTextColor(Color.WHITE);
                tv.setText("系统程序" + systemInfos.size() + "个");
                return tv;
            }
            if(convertView!=null&& convertView instanceof RelativeLayout){
                view = convertView;
                viewHolder = (ViewHolder) view.getTag();
            }else{
                view = View.inflate(getApplicationContext(),
                        R.layout.item_apps_info, null);
                viewHolder = new ViewHolder();
                viewHolder.ivIcon = (ImageView) view.findViewById(R.id.iv_icon);
                viewHolder.tvName = (TextView) view.findViewById(R.id.tv_name);
                viewHolder.tvIsSd = (TextView) view.findViewById(R.id.tv_is_sd);
                viewHolder.tvVersion = (TextView) view
                        .findViewById(R.id.tv_version);
                view.setTag(viewHolder);
            }
            AppInfo appInfo;
            if (position <= userInfos.size()) {
                appInfo = userInfos.get(position-1 ); // 在用户程序的集合中取出来一个对象
            } else {
                // 从系统程序的集合中取出来一个对象
                appInfo = systemInfos.get(position - userInfos.size()-2 );
            }

            viewHolder.ivIcon.setImageDrawable(appInfo.getIcon());// nullPoint
            // null.方法()
            viewHolder.tvName.setText(appInfo.getName());
            viewHolder.tvVersion.setText(appInfo.getVersionName());
            if (appInfo.isSd()) {
                viewHolder.tvIsSd.setText("SD内存");
            } else {
                viewHolder.tvIsSd.setText("手机内存");
            }

            return view;
        }
    }
    static class ViewHolder {
        ImageView ivIcon;
        TextView tvName, tvIsSd, tvVersion;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        hidePopupWindow();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        hidePopupWindow();
        new LoadData().execute();
    }


}
