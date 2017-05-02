package com.example.mobilesafe;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.mobilesafe.domain.ContactsInfo;
import com.example.mobilesafe.engine.ContactsEngine;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

import java.util.List;

public class ContactsActivity extends AppCompatActivity {
    @ViewInject(R.id.lv_contacts)
    private ListView lvContacts;
    @ViewInject(R.id.pb_progress)
    private ProgressBar progressBar;
    private List<ContactsInfo> contactsInfoList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contacts);
        ViewUtils.inject(this);
        new MyAsyncTask().execute();
        lvContacts.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent data = new Intent();
                data.putExtra("num",contactsInfoList.get(position).getNum());
                setResult(0,data);
                finish();
            }
        });
    }
    public class MyAsyncTask extends AsyncTask{

        @Override
        protected void onPreExecute() {
            progressBar.setVisibility(View.VISIBLE);
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(Object o) {
            lvContacts.setAdapter(new ContactsAdapter());
            progressBar.setVisibility(View.INVISIBLE);
            super.onPostExecute(o);
        }


        @Override
        protected Object doInBackground(Object[] params) {
            contactsInfoList = ContactsEngine.getAllContactsInfos(ContactsActivity.this);
            return null;
        }
    }
    private class ContactsAdapter extends BaseAdapter{

        @Override
        public int getCount() {
            return contactsInfoList.size();
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
            ContactsInfo info = contactsInfoList.get(position);
            View view = View.inflate(getApplicationContext(),R.layout.item_contacts,null);
            TextView tvName = (TextView) view.findViewById(R.id.tv_name);
            TextView tvNum = (TextView) view.findViewById(R.id.tv_tel);
            tvName.setText(info.getName());
            tvNum.setText(info.getNum());
            return view;
        }
    }
}
