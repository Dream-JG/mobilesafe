package com.example.mobilesafe;

import android.content.DialogInterface;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.example.mobilesafe.db.dao.BlackNumDao;
import com.example.mobilesafe.domain.BlackNumInfo;

import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.List;


public class CallSmsSafeActivity extends AppCompatActivity {
    @ViewInject(R.id.lv_black_num)
    private ListView lvBlackNum;
    @ViewInject(R.id.black_num_progress)
    private ProgressBar progressBar;
    private BlackNumDao dao;
    private List<BlackNumInfo> blackNumInfoList;
    private BlackNumAdapter blackNumAdapter;
    private AlertDialog dialogAdd;
    final  static int MAX_NUM = 20;
    private int startIndex = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_call_sms_safe);
        x.view().inject(this);
        dao = new BlackNumDao(this);
        new LoadData().execute();
        lvBlackNum.setOnScrollListener(new AbsListView.OnScrollListener() {
            //当ListView滑动状态发生改变的时候调用
            //三种状态  静止 慢慢滑动  快速滑动
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                if(scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE){
                    // 当ListView 是一个静止状态 当看到的条目时最后一个条目的时候 加载剩余的数据
                    // 获取到最后一个可见的条目
                    int lastVisiblePosition = lvBlackNum
                            .getLastVisiblePosition(); // 0
                    // 最后可见的条目 确实是集合最后的一个条目
                    if (lastVisiblePosition == blackNumInfoList.size() - 1) {
                        // 加载下一波数据
                        startIndex += MAX_NUM;
                         new LoadData().execute();
                    }
                }

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

            }
        });


    }

    private class LoadData extends AsyncTask{

        @Override
        protected Object doInBackground(Object[] params) {
            if (blackNumInfoList == null){
            blackNumInfoList = dao.getPartBlackNum(startIndex,MAX_NUM);
            }else {
                blackNumInfoList.addAll(dao.getPartBlackNum(startIndex,MAX_NUM));
            }
            return null;
        }

        @Override
        protected void onPreExecute() {
            progressBar.setVisibility(View.VISIBLE);
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(Object o) {
            if (blackNumAdapter == null) {
                blackNumAdapter = new BlackNumAdapter();
                lvBlackNum.setAdapter(blackNumAdapter);
            }else {
                blackNumAdapter.notifyDataSetChanged();// 如果Adapter已经创建了 直接更新界面 不需要重新设置Adapter
            }
            progressBar.setVisibility(View.INVISIBLE);
            super.onPostExecute(o);
        }
    }


    public void add(View v){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View view = View.inflate(getApplicationContext(),R.layout.dialog_add_black_num,null);
        final EditText etBlackNum = (EditText) view.findViewById(R.id.et_black_num);
        final RadioGroup rgMode = (RadioGroup) view.findViewById(R.id.rg_mode);
        Button btOk = (Button) view.findViewById(R.id.bt_add_ok);
        Button btCancel = (Button) view.findViewById(R.id.bt_add_cancel);
        btCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogAdd.dismiss();
            }
        });
        btOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String blackNum = etBlackNum.getText().toString().trim();
                int mode = 0;
                int checkedRadioButtonId = rgMode.getCheckedRadioButtonId();
                if(checkedRadioButtonId==R.id.rb_tel){
                    mode = BlackNumDao.TEL;
                }else if(checkedRadioButtonId==R.id.rb_sms){
                    mode = BlackNumDao.SMS;
                }else if (checkedRadioButtonId==R.id.rb_all){
                    mode = BlackNumDao.ALL;
                }
                dao.addBlackNum(blackNum,mode);
                blackNumInfoList.add(0,new BlackNumInfo(blackNum,mode));
                blackNumAdapter.notifyDataSetChanged();
                dialogAdd.dismiss();
            }
        });

        builder.setView(view);
        dialogAdd = builder.create();
        dialogAdd.show();


    }





    public class BlackNumAdapter extends BaseAdapter{

        @Override
        public int getCount() {
            return blackNumInfoList.size();
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            final BlackNumInfo blackNumInfo = blackNumInfoList.get(position);
            View view;
            ViewHolder holder;
            if(convertView!=null){
                view = convertView;
                holder = (ViewHolder) view.getTag();
                System.out.println("复用了view"+position);
            }else {
                System.out.println("创建了view"+position);
                view = View.inflate(getApplicationContext(),R.layout.item_black_num,null);
                holder = new ViewHolder();
                holder.tvNum = (TextView) view.findViewById(R.id.tv_black_num);
                holder.tvMode = (TextView) view.findViewById(R.id.tv_mode);
                holder.ivDel = (ImageView) view.findViewById(R.id.iv_del);
                view.setTag(holder);
            }
            holder.ivDel.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(
                            CallSmsSafeActivity.this);
                    builder.setTitle("是否删除黑名单" + blackNumInfo.getBlackNum());
                    builder.setPositiveButton("确定",
                            new DialogInterface.OnClickListener() {

                                @Override
                                public void onClick(DialogInterface dialog,
                                                    int which) {

                                    blackNumInfoList.remove(blackNumInfo);// 从界面中删除
                                    blackNumAdapter.notifyDataSetChanged();// 刷新界面
                                    dao.deleteBlackNum(blackNumInfo
                                            .getBlackNum());// 在数据库中删除
                                    dialog.dismiss();
                                }
                            });
                    builder.setNegativeButton("取消", null);
                    builder.show();
                }
            });
            holder.tvNum.setText(blackNumInfo.getBlackNum());
            int mode = blackNumInfo.getMode();
            switch (mode){
                case BlackNumDao.TEL:
                    holder.tvMode.setText("电话拦截");
                    break;
                case BlackNumDao.SMS:
                    holder.tvMode.setText("短信拦截");
                    break;
                case BlackNumDao.ALL:
                    holder.tvMode.setText("全部拦截");
                    break;

            }
            return view;
        }

        @Override
        public Object getItem(int position) {
            return blackNumInfoList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }
    }
    class ViewHolder{
        private TextView tvNum;
        private TextView tvMode;
        private ImageView ivDel;
    }
}
