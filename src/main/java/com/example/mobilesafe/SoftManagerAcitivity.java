package com.example.mobilesafe;

import android.graphics.Color;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.mobilesafe.domain.AppInfo;
import com.example.mobilesafe.engine.AppEngine;

import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;

public class SoftManagerAcitivity extends AppCompatActivity {
    @ViewInject(R.id.lv_apps)
    private ListView lvApps;
    @ViewInject(R.id.pb_apps)
    private ProgressBar progress;
    @ViewInject(R.id.tv_count)
    private TextView tvCount;
    private List<AppInfo> allAppInfoList;
    private List<AppInfo> userInfos;
    private List<AppInfo> systemInfos;
    AppInfo appInfo;
    private PopupWindow window;
    private AppAdapter adapter;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_soft_manager_acitivity);
        x.view().inject(this);
        new LoadData().execute();
        scoll();



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
            allAppInfoList = AppEngine.getAllAppInfos(SoftManagerAcitivity.this);
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
}
