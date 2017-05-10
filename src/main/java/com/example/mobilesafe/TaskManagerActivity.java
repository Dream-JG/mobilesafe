package com.example.mobilesafe;

import android.app.ActivityManager;
import android.graphics.Color;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.format.Formatter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mobilesafe.domain.TaskInfo;
import com.example.mobilesafe.engine.TaskEngine;
import com.example.mobilesafe.untils.ProcessUtils;


import org.xutils.view.annotation.ViewInject;
import org.xutils.x;


import java.util.ArrayList;
import java.util.List;


public class TaskManagerActivity extends AppCompatActivity {
    @ViewInject(R.id.lv_task)
    private ListView lvTask;
    @ViewInject(R.id.pb_task)
    private ProgressBar progressBar;
    @ViewInject(R.id.tv_count)
    private TextView tvCount;
    @ViewInject(R.id.tv_process)
    private TextView tvprogress;
    @ViewInject(R.id.tv_memory)
    private TextView tvMemory;
    private List<TaskInfo> allTaskInfoList;
    private List<TaskInfo> userTaskInfoList;
    private List<TaskInfo> systemTaskInfoList;
    private TaskAdapter taskAdapter;
    private int processCount;
    private long availableRam;
    private String total;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_manager);
        x.view().inject(this);
        processCount = ProcessUtils.getProcessCount(this);
        tvprogress.setText("正在运行进程的个数:\n" + processCount + "个");

        availableRam = ProcessUtils.getAvailableRam(this);
        // 当前运行sdk的版本
        int sdkInt = android.os.Build.VERSION.SDK_INT;
        long totalRam;
        if (sdkInt >= 16) {
            totalRam = ProcessUtils.getTotalRam(this);
        } else {
            totalRam = ProcessUtils.getTotalRam();
        }
        total = Formatter.formatFileSize(this, totalRam);
        String available = Formatter.formatFileSize(this, availableRam);
        tvMemory.setText("剩余/总内存:\n" + available + "/" + total);


        new LoadData().execute();
        scroll();
        itemOnClick();
        lvTask.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0 || position == userTaskInfoList.size() - 1) {
                    return;
                }
                TaskInfo info;
                if (position <= userTaskInfoList.size()) {
                    info = userTaskInfoList.get(position - 1);
                } else {
                    info = systemTaskInfoList.get(position - userTaskInfoList.size() - 2);
                }
                if (info.isChecked()) {
                    info.setChecked(false);
                    System.out.println("点击了：" + position + info.isChecked());
                } else {
                    if (info.getPackageName().equals(getPackageName())) {

                        return;
                    }
                    info.setChecked(true);
                    System.out.println("点击了：" + position + info.isChecked());
                }
                System.out.println("点击了：" + position + "");
                //taskAdapter.notifyDataSetChanged();
                ViewHolder holder = (ViewHolder) view.getTag();
                holder.checkBox.setChecked(info.isChecked());
            }
        });

    }

    private void itemOnClick() {

    }

    private void scroll() {
        lvTask.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

                if (userTaskInfoList != null && systemTaskInfoList != null) {
                    if (firstVisibleItem >= userTaskInfoList.size() + 1) {
                        tvCount.setText("系统进程" + systemTaskInfoList.size() + "个");
                    } else {
                        tvCount.setText("用户进程" + userTaskInfoList.size() + "个");
                    }
                }

            }
        });
    }

    public class LoadData extends AsyncTask {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected void onPostExecute(Object o) {
            progressBar.setVisibility(View.INVISIBLE);
            tvCount.setVisibility(View.VISIBLE);
            if (taskAdapter == null) {
                taskAdapter = new TaskAdapter();
                lvTask.setAdapter(taskAdapter);
            } else {
                taskAdapter.notifyDataSetChanged();
            }
            super.onPostExecute(o);
        }

        @Override
        protected Object doInBackground(Object[] params) {
            allTaskInfoList = TaskEngine.getTaskInfos(getApplicationContext());
            userTaskInfoList = new ArrayList<>();
            systemTaskInfoList = new ArrayList<>();
            for (TaskInfo taskInfo : allTaskInfoList) {
                if (taskInfo.isUser()) {
                    userTaskInfoList.add(taskInfo);
                    System.out.println("****用户应用" + taskInfo.toString());
                } else {
                    systemTaskInfoList.add(taskInfo);
                    System.out.println("****系统应用" + taskInfo.toString());
                }

            }
            System.out.println("*********doInBackground");
            return null;
        }
    }

    private class TaskAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return systemTaskInfoList.size() + userTaskInfoList.size() + 2;
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
            if (position == 0) {
                TextView textView = new TextView(getApplicationContext());
                textView.setBackgroundColor(Color.GRAY);
                textView.setTextColor(Color.WHITE);
                textView.setTextSize(16);
                textView.setText("用户进程" + userTaskInfoList.size() + "个");
                return textView;
            } else if (position == userTaskInfoList.size() + 1) {
                TextView textView = new TextView(getApplicationContext());
                textView.setBackgroundColor(Color.GRAY);
                textView.setText("系统进程" + systemTaskInfoList.size() + "个");
                textView.setTextSize(16);
                return textView;
            }
            View view;
            ViewHolder viewHolder;
            if (convertView != null && convertView instanceof RelativeLayout) {
                view = convertView;
                viewHolder = (ViewHolder) view.getTag();
            } else {
                view = View.inflate(getApplicationContext(), R.layout.item_task_manager, null);
                viewHolder = new ViewHolder();
                viewHolder.ivIcon = (ImageView) view.findViewById(R.id.iv_icon);
                viewHolder.tvName = (TextView) view.findViewById(R.id.tv_name);
                viewHolder.tvMemory = (TextView) view.findViewById(R.id.tv_memory);
                viewHolder.checkBox = (CheckBox) view.findViewById(R.id.cb_task);
                view.setTag(viewHolder);
            }
            TaskInfo taskInfo;
            if (position <= userTaskInfoList.size()) {
                taskInfo = userTaskInfoList.get(position - 1);
            } else {
                taskInfo = systemTaskInfoList.get(position - userTaskInfoList.size() - 2);
            }
            if (taskInfo.getIcon() == null) {
                viewHolder.ivIcon.setImageResource(R.drawable.ic_launcher_default);
            } else {
                viewHolder.ivIcon.setImageDrawable(taskInfo.getIcon());
            }
            if (taskInfo.getName() == null) {
                viewHolder.tvName.setText(taskInfo.getPackageName());
            } else {
                viewHolder.tvName.setText(taskInfo.getName());
            }
            if (taskInfo.isChecked()) {
                viewHolder.checkBox.setChecked(true);
            } else {
                viewHolder.checkBox.setChecked(false);
            }
            if (taskInfo.getPackageName().equals(getPackageName())) {
                viewHolder.checkBox.setVisibility(View.INVISIBLE);
            }
            viewHolder.tvMemory.setText(android.text.format.Formatter.formatFileSize(
                    getApplicationContext(), taskInfo.getMemory()));
            return view;
        }
    }

    private class ViewHolder {
        ImageView ivIcon;
        TextView tvName, tvMemory;
        CheckBox checkBox;
    }

    /**
     * 全选的点击事件
     *
     * @param v
     */
    public void all(View v) {
        for (TaskInfo info : userTaskInfoList) {
            if (!info.getPackageName().equals(getPackageName())) {
                info.setChecked(true);
            }
        }
        for (TaskInfo info : systemTaskInfoList) {
            info.setChecked(true);
        }
        taskAdapter.notifyDataSetChanged();
    }

    /**
     * 取消
     *
     * @param v
     */
    public void cancel(View v) {
        for (TaskInfo info : userTaskInfoList) {
            info.setChecked(false);
        }
        for (TaskInfo info : systemTaskInfoList) {
            info.setChecked(false);
        }
        taskAdapter.notifyDataSetChanged();
    }

    /**
     * 清理
     *
     * @param v
     */
    public void clear(View v) {
        ActivityManager am = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
        List<TaskInfo> deleteInfos = new ArrayList<TaskInfo>();
        for (TaskInfo info : userTaskInfoList) {
            if (info.isChecked()) {
                am.killBackgroundProcesses(info.getPackageName());// 只能杀死后台进程和空进程
                deleteInfos.add(info);
            }
        }
        for (TaskInfo info : systemTaskInfoList) {
            if (info.isChecked()) {
                am.killBackgroundProcesses(info.getPackageName());
                deleteInfos.add(info);// 先把要删除的对象 添加到 deleteInfos
            }
            long clearMemory = 0;
            for (TaskInfo delInfo : deleteInfos) {
                if (delInfo.isUser()) {
                    userTaskInfoList.remove(delInfo);
                } else {
                    systemTaskInfoList.remove(delInfo);
                }
                clearMemory += delInfo.getMemory();
            }
            // 把数字格式化成 字符串 比如 1024 格式化成 1kb
            String clear = Formatter.formatFileSize(getApplicationContext(),
                    clearMemory);
            Toast.makeText(getApplicationContext(),
                    "清理了" + deleteInfos.size() + "进程,释放了" + clear + "内存", Toast.LENGTH_SHORT).show();
            processCount = processCount - deleteInfos.size();
            tvprogress.setText("正在运行进程的个数:\n" + processCount + "个");
            availableRam += clearMemory;
            String available = Formatter.formatFileSize(this, availableRam);
            tvMemory.setText("剩余/总内存:\n" + available + "/" + total);

            deleteInfos.clear();
            deleteInfos = null;

            taskAdapter.notifyDataSetChanged();
        }

    }

    /**
     * 设置
     *
     * @param v
     */
    public void setting(View v) {
//        isShowSystem=!isShowSystem;
//        adapter.notifyDataSetChanged();
    }


}
