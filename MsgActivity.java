package com.example.ccy.assistant;

import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * 消息
 * Created by ccy on 2017/5/20.
 */

public class MsgActivity extends AppCompatActivity {
    private List<String> msgs;
    private Toolbar toolbar;
    private MsgAdapter adapter;
    private ListView listView;
    private List<Integer> record;
    private List<MsgListBean> msgList;
    private EditText et_name;
    private EditText et_msg;
    private RadioButton rb_char_send;
    private RadioButton rb_16_send;
    private View view;
    private static MsgActivity main;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.msg_activity);
        main = this;
        initView();
        initActionBar();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {//创造的menu
        getMenuInflater().inflate(R.menu.msg_toolbar_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    private void initActionBar() {
        toolbar = (Toolbar) findViewById(R.id.activity_msg_toolbar);
        toolbar.setTitle("消息列表");
        toolbar.inflateMenu(R.menu.msg_toolbar_menu);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.action_new_msg:
                        showAddMsgDialog();
                        break;
                    case R.id.action_delete:
                        if (record.size() > 0)
                            showDeleteDialog();
                        break;
                }
                return false;
            }
        });
    }

    private void showAddMsgDialog() {
        view = LayoutInflater.from(this).inflate(R.layout.add_msg_dialog, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("消息编辑");
        builder.setView(view);
        et_name = (EditText) view.findViewById(R.id.et_msg_name);
        et_msg = (EditText) view.findViewById(R.id.et_msg_send);
        final CustomTextWatcher watcher = new CustomTextWatcher(et_msg);
        et_msg.addTextChangedListener(watcher);
        rb_char_send = (RadioButton) view.findViewById(R.id.rb_char_send);
        rb_16_send = (RadioButton) view.findViewById(R.id.rb_16_send);
        rb_16_send.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                watcher.setFormat(isChecked);
                et_msg.setText("");
            }

        });
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String msg = et_msg.getText().toString().trim();
                String name = et_name.getText().toString();
                boolean ischar = rb_char_send.isChecked();
                boolean is16 = rb_16_send.isChecked();
                if (name.length() > 0 && msg.length() > 0) {
                    MsgListBean bean = new MsgListBean();
                    bean.setName(name);
                    bean.setMsg(msg);
                    bean.setIs_16_send(is16);
                    bean.setIs_char_send(ischar);
                    msgList.add(bean);
                    msgs.add(name);
                    adapter.notifyDataSetChanged();
                } else {
                    Toast.makeText(MsgActivity.this, "消息不能为空", Toast.LENGTH_LONG).show();
                }
            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.show();
    }

    private void initView() {
        msgs = new ArrayList<>();
        msgList = new ArrayList<>();
        record = new ArrayList<>();
        listView = (ListView) findViewById(R.id.lv_msg_list);
        adapter = new MsgAdapter(this, msgs);
        listView.setAdapter(adapter);
        restoreDatas();

    }

    private void showDeleteDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("删除消息");
        builder.setMessage("你想要删除吗?");
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Iterator i = record.iterator();
                while (i.hasNext()) {
                    int a = (int) i.next();
                    if (a < msgs.size()) {
                        msgs.remove(a);//为什么会这样
                        msgList.remove(a);
                    }
                }
                adapter.notifyDataSetChanged();
                record.clear();
            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.show();
    }

    public void setRecord(int a) {//真是完美啊
        record.add(a);
    }

    public void remove(int a) {
        record.remove(record.indexOf(a));
    }

    public static MsgActivity getMain() {
        return main;
    }

    static class MsgListBean {
        private String name;
        private String msg;
        private boolean is_char_send;
        private boolean isIs_16_send;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getMsg() {
            return msg;
        }

        public void setMsg(String msg) {
            this.msg = msg;
        }

        public boolean is_char_send() {
            return is_char_send;
        }

        public void setIs_char_send(boolean is_char_send) {
            this.is_char_send = is_char_send;
        }

        public boolean is_16_send() {
            return isIs_16_send;
        }

        public void setIs_16_send(boolean is_16_send) {
            isIs_16_send = is_16_send;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        saveDatas();
    }

    private void saveDatas() {
        Iterator iterator = msgList.iterator();
        SharedPreferences preferences = getApplicationContext().getSharedPreferences("datas", MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt("msg_list_size", msgList.size());
        int n = 0;
        while (iterator.hasNext()) {
            MsgListBean bean = (MsgListBean) iterator.next();
            editor.putString("msg_list" + n + "name", bean.getName());
            editor.putString("msg_list" + n + "msg", bean.getMsg());
            editor.putBoolean("msg_list" + n + "ischar", bean.is_char_send());
            editor.putBoolean("msg_list" + n + "is16", bean.is_16_send());
            n++;
        }
        editor.apply();
    }

    private void restoreDatas() {
        SharedPreferences preferences = getApplicationContext().getSharedPreferences("datas", MODE_PRIVATE);
        msgList.clear();
        int n = preferences.getInt("msg_list_size", 0);
        for (int i = 0; i < n; i++) {
            MsgListBean bean = new MsgListBean();
            bean.setName(preferences.getString("msg_list" + i + "name", ""));
            bean.setMsg(preferences.getString("msg_list" + i + "msg", ""));
            bean.setIs_char_send(preferences.getBoolean("msg_list" + i + "ischar", false));
            bean.setIs_16_send(preferences.getBoolean("msg_list" + i + "is16", false));
            msgs.add(bean.getName());
            msgList.add(bean);
        }
    }
}
