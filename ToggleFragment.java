package com.example.ccy.assistant;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.ToggleButton;


/**
 * 为什么会这样呢
 * Created by ccy on 2017/5/13.
 */

public class ToggleFragment extends Fragment implements View.OnClickListener, View.OnLongClickListener {

    private ToggleButton tb1;
    private ToggleButton tb2;
    private ToggleButton tb3;
    private ToggleButton tb4;
    private ToggleButton tb5;
    private ToggleButton tb6;
    private ToggleButton tb7;
    private ToggleButton tb8;
    private ToggleButton tb9;
    private ToggleButton tb10;
    private ToggleButton tb11;
    private ToggleButton tb12;
    private ToggleBean[][] toggleBeans = new ToggleBean[4][3];
    private View layout;
    private AlertDialog.Builder builder;
    private RadioButton rb_char_on;
    private RadioButton rb_16_on;
    private RadioButton rb_char_off;
    private RadioButton rb_16_off;
    private EditText et_on;
    private EditText et_on_msg;
    private EditText et_off;
    private EditText et_off_msg;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_toggle, container, false);
        initView(view);
        restoreStateFromArguments();
        return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tb_1:
                writeCom(tb1, 0);
                break;
            case R.id.tb_2:
                writeCom(tb2, 1);
                break;
            case R.id.tb_3:
                writeCom(tb3, 2);
                break;
            case R.id.tb_4:
                writeCom(tb4, 3);
                break;
            case R.id.tb_5:
                writeCom(tb5, 4);
                break;
            case R.id.tb_6:
                writeCom(tb6, 5);
                break;
            case R.id.tb_7:
                writeCom(tb7, 6);
                break;
            case R.id.tb_8:
                writeCom(tb8, 7);
                break;
            case R.id.tb_9:
                writeCom(tb9, 8);
                break;
            case R.id.tb_10:
                writeCom(tb10, 9);
                break;
            case R.id.tb_11:
                writeCom(tb11, 10);
                break;
            case R.id.tb_12:
                writeCom(tb12, 11);
                break;
        }
    }

    @Override
    public boolean onLongClick(View v) {
        switch (v.getId()) {

            case R.id.tb_1:
                v.setTag(0);
                showEditDialog(v);
                break;
            case R.id.tb_2:
                v.setTag(1);
                showEditDialog(v);
                break;
            case R.id.tb_3:
                v.setTag(2);
                showEditDialog(v);
                break;
            case R.id.tb_4:
                v.setTag(3);
                showEditDialog(v);
                break;
            case R.id.tb_5:
                v.setTag(4);
                showEditDialog(v);
                break;
            case R.id.tb_6:
                v.setTag(5);
                showEditDialog(v);
                break;
            case R.id.tb_7:
                v.setTag(6);
                showEditDialog(v);
                break;
            case R.id.tb_8:
                v.setTag(7);
                showEditDialog(v);
                break;
            case R.id.tb_9:
                v.setTag(8);
                showEditDialog(v);
                break;
            case R.id.tb_10:
                v.setTag(9);
                showEditDialog(v);
                break;
            case R.id.tb_11:
                v.setTag(10);
                showEditDialog(v);
                break;
            case R.id.tb_12:
                v.setTag(11);
                showEditDialog(v);
                break;
        }
        return false;
    }

    private void showEditDialog(final View v) {

        layout = View.inflate(getContext(), R.layout.toggle_dialog, null);
        rb_char_on = (RadioButton) layout.findViewById(R.id.rb_char);
        rb_16_on = (RadioButton) layout.findViewById(R.id.rb_16);
        rb_char_off = (RadioButton) layout.findViewById(R.id.rb_char1);
        rb_16_off = (RadioButton) layout.findViewById(R.id.rb_161);
        et_on = (EditText) layout.findViewById(R.id.et_on);

        et_on_msg = (EditText) layout.findViewById(R.id.et_on_msg);
        final CustomTextWatcher watcher = new CustomTextWatcher(et_on_msg);
        et_on_msg.addTextChangedListener(watcher);
        et_off = (EditText) layout.findViewById(R.id.et_off);
        et_off_msg = (EditText) layout.findViewById(R.id.et_off_msg);
        final CustomTextWatcher watcher1 = new CustomTextWatcher(et_off_msg);
        et_off_msg.addTextChangedListener(watcher1);
        builder = new AlertDialog.Builder(getContext());
        builder.setTitle("按钮编辑");
        builder.setView(layout);
        if (toggleBeans[(Integer) v.getTag() / 3][(Integer) v.getTag() % 3] != null) {
            ToggleBean t = toggleBeans[(Integer) v.getTag() / 3][(Integer) v.getTag() % 3];
            et_off.setText(t.getOff());
            et_on.setText(t.getOn());
            et_on_msg.setText(t.getMsg_on());
            et_off_msg.setText(t.getMsg_off());
            if (t.isCharCheckOff()) {
                rb_char_off.setChecked(true);
                watcher1.setFormat(false);
                rb_16_off.setChecked(false);
            } else {
                rb_char_off.setChecked(false);
                watcher1.setFormat(true);
                rb_16_off.setChecked(true);
            }
            if (t.isCharCheckOn()) {
                rb_char_on.setChecked(true);
                watcher.setFormat(false);
                rb_16_on.setChecked(false);
            } else {
                rb_char_on.setChecked(false);
                watcher.setFormat(true);
                rb_16_on.setChecked(true);
            }
        }
        rb_16_off.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                watcher1.setFormat(isChecked);
                et_off_msg.setText("");
            }
        });
        rb_16_on.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                watcher.setFormat(isChecked);
                et_on_msg.setText("");
            }
        });
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {//完成一些事情
            @Override
            public void onClick(DialogInterface dialog, int which) {
                ToggleBean toggleBean = new ToggleBean();
                toggleBean.setOff(et_off.getText().toString());
                toggleBean.setOn(et_on.getText().toString());
                toggleBean.setMsg_on(et_on_msg.getText().toString());
                toggleBean.setMsg_off(et_off_msg.getText().toString());
                toggleBean.setCharCheckOff(rb_char_off.isChecked());
                toggleBean.setCharCheckOn(rb_char_on.isChecked());
                toggleBeans[(Integer) v.getTag() / 3][(Integer) v.getTag() % 3] = toggleBean;
                ((ToggleButton) v).setTextOff(toggleBeans[(Integer) v.getTag() / 3][(Integer) v.getTag() % 3].getOff());
                ((ToggleButton) v).setTextOn(toggleBeans[(Integer) v.getTag() / 3][(Integer) v.getTag() % 3].getOn());
                if (toggleBean.isCharCheckOn()) {
                    ((ToggleButton) v).setText(toggleBeans[(Integer) v.getTag() / 3][(Integer) v.getTag() % 3].getOn());
                } else {
                    ((ToggleButton) v).setText(toggleBeans[(Integer) v.getTag() / 3][(Integer) v.getTag() % 3].getOff());
                }

            }
        });

        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();//关闭窗口
            }
        });
        builder.show();


    }

    public void initView(View v) {
        tb1 = (ToggleButton) v.findViewById(R.id.tb_1);
        tb2 = (ToggleButton) v.findViewById(R.id.tb_2);
        tb3 = (ToggleButton) v.findViewById(R.id.tb_3);
        tb4 = (ToggleButton) v.findViewById(R.id.tb_4);
        tb5 = (ToggleButton) v.findViewById(R.id.tb_5);
        tb6 = (ToggleButton) v.findViewById(R.id.tb_6);
        tb7 = (ToggleButton) v.findViewById(R.id.tb_7);
        tb8 = (ToggleButton) v.findViewById(R.id.tb_8);
        tb9 = (ToggleButton) v.findViewById(R.id.tb_9);
        tb10 = (ToggleButton) v.findViewById(R.id.tb_10);
        tb11 = (ToggleButton) v.findViewById(R.id.tb_11);
        tb12 = (ToggleButton) v.findViewById(R.id.tb_12);
        tb1.setOnClickListener(this);
        tb2.setOnClickListener(this);
        tb3.setOnClickListener(this);
        tb4.setOnClickListener(this);
        tb5.setOnClickListener(this);
        tb6.setOnClickListener(this);
        tb7.setOnClickListener(this);
        tb8.setOnClickListener(this);
        tb9.setOnClickListener(this);
        tb10.setOnClickListener(this);
        tb11.setOnClickListener(this);
        tb12.setOnClickListener(this);
        tb1.setOnLongClickListener(this);
        tb2.setOnLongClickListener(this);
        tb3.setOnLongClickListener(this);
        tb4.setOnLongClickListener(this);
        tb5.setOnLongClickListener(this);
        tb6.setOnLongClickListener(this);
        tb7.setOnLongClickListener(this);
        tb8.setOnLongClickListener(this);
        tb9.setOnLongClickListener(this);
        tb10.setOnLongClickListener(this);
        tb11.setOnLongClickListener(this);
        tb12.setOnLongClickListener(this);

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }


    private void restoreState(ToggleBean toggleBean, int i) {

        switch (i) {
            case 0:
                tb1.setTextOn(toggleBean.getOn());
                tb1.setTextOff(toggleBean.getOff());
                if (tb1.isChecked()) {
                    tb1.setText(toggleBean.getOn());
                } else {
                    tb1.setText(toggleBean.getOff());
                }
                break;
            case 1:
                tb2.setTextOn(toggleBean.getOn());
                tb2.setTextOff(toggleBean.getOff());
                if (tb2.isChecked()) {
                    tb2.setText(toggleBean.getOn());
                } else {
                    tb2.setText(toggleBean.getOff());
                }
                break;
            case 2:
                tb3.setTextOn(toggleBean.getOn());
                tb3.setTextOff(toggleBean.getOff());
                if (tb3.isChecked()) {
                    tb3.setText(toggleBean.getOn());
                } else {
                    tb3.setText(toggleBean.getOff());
                }
                break;
            case 3:
                tb4.setTextOn(toggleBean.getOn());
                tb4.setTextOff(toggleBean.getOff());
                if (tb4.isChecked()) {
                    tb4.setText(toggleBean.getOn());
                } else {
                    tb4.setText(toggleBean.getOff());
                }
                break;
            case 4:
                tb5.setTextOn(toggleBean.getOn());
                tb5.setTextOff(toggleBean.getOff());
                if (tb5.isChecked()) {
                    tb5.setText(toggleBean.getOn());
                } else {
                    tb5.setText(toggleBean.getOff());
                }
                break;
            case 5:
                tb6.setTextOn(toggleBean.getOn());
                tb6.setTextOff(toggleBean.getOff());
                if (tb6.isChecked()) {
                    tb6.setText(toggleBean.getOn());
                } else {
                    tb6.setText(toggleBean.getOff());
                }
                break;
            case 6:
                tb7.setTextOn(toggleBean.getOn());
                tb7.setTextOff(toggleBean.getOff());
                if (tb7.isChecked()) {
                    tb7.setText(toggleBean.getOn());
                } else {
                    tb7.setText(toggleBean.getOff());
                }
                break;
            case 7:
                tb8.setTextOn(toggleBean.getOn());
                tb8.setTextOff(toggleBean.getOff());
                if (tb8.isChecked()) {
                    tb8.setText(toggleBean.getOn());
                } else {
                    tb8.setText(toggleBean.getOff());
                }
                break;
            case 8:
                tb9.setTextOn(toggleBean.getOn());
                tb9.setTextOff(toggleBean.getOff());
                if (tb9.isChecked()) {
                    tb9.setText(toggleBean.getOn());
                } else {
                    tb9.setText(toggleBean.getOff());
                }
                break;
            case 9:
                tb10.setTextOn(toggleBean.getOn());
                tb10.setTextOff(toggleBean.getOff());
                if (tb10.isChecked()) {
                    tb10.setText(toggleBean.getOn());
                } else {
                    tb10.setText(toggleBean.getOff());
                }
                break;
            case 10:
                tb11.setTextOn(toggleBean.getOn());
                tb11.setTextOff(toggleBean.getOff());
                if (tb11.isChecked()) {
                    tb11.setText(toggleBean.getOn());
                } else {
                    tb11.setText(toggleBean.getOff());
                }
                break;
            case 11:
                tb12.setTextOn(toggleBean.getOn());
                tb12.setTextOff(toggleBean.getOff());
                if (tb12.isChecked()) {
                    tb12.setText(toggleBean.getOn());
                } else {
                    tb12.setText(toggleBean.getOff());
                }
                break;
        }

    }

    private void saveStateToArguments() {
        SharedPreferences.Editor editor = getActivity().getPreferences(Context.MODE_PRIVATE).edit();
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 3; j++) {
                if (toggleBeans[i][j] == null) {
                    break;
                }
                editor.putString("on" + i * 3 + j, toggleBeans[i][j].getOn());
                editor.putString("off" + i * 3 + j, toggleBeans[i][j].getOff());
                editor.putString("msg_on" + i * 3 + j, toggleBeans[i][j].getMsg_on());
                editor.putString("msg_off" + i * 3 + j, toggleBeans[i][j].getMsg_off());
                editor.putBoolean("is_char_check_on" + i * 3 + j, toggleBeans[i][j].isCharCheckOn());
                editor.putBoolean("is_char_check_off" + i * 3 + j, toggleBeans[i][j].isCharCheckOff());
            }
        }
        editor.apply();

    }

    private void restoreStateFromArguments() {

        SharedPreferences preferences = getActivity().getPreferences(Context.MODE_PRIVATE);
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 3; j++) {
                ToggleBean toggleBean = new ToggleBean();
                toggleBean.setOff(preferences.getString("off" + i * 3 + j, ""));
                toggleBean.setOn(preferences.getString("on" + i * 3 + j, ""));
                toggleBean.setMsg_on(preferences.getString("msg_on" + i * 3 + j, ""));
                toggleBean.setMsg_off(preferences.getString("msg_off" + i * 3 + j, ""));
                toggleBean.setCharCheckOff(preferences.getBoolean("is_char_check_off" + i * 3 + j, false));
                toggleBean.setCharCheckOn(preferences.getBoolean("is_char_check_on" + i * 3 + j, false));
                toggleBeans[i][j] = toggleBean;
                restoreState(toggleBeans[i][j], i * 3 + j);//显示按钮的信息

            }
        }

    }

    @Override
    public void onDestroy() {

        super.onDestroy();
    }

    @Override
    public void onStop() {

        super.onStop();

    }

    @Override
    public void onStart() {
        restoreStateFromArguments();

        super.onStart();
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {

        super.onActivityCreated(savedInstanceState);

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        saveStateToArguments();
    }

    public void writeCom(ToggleButton t, int i) {
        if (t.isChecked() && toggleBeans[i / 3][i % 3] != null) {
            if (!toggleBeans[i / 3][i % 3].getMsg_on().equals("")) {
                if (toggleBeans[i / 3][i % 3].isCharCheckOn()) {
                    ChatActivity.getMain().setIsCharSend(true);
                } else {
                    ChatActivity.getMain().setIsCharSend(false);

                }
                ChatActivity.getMain().sendMsg(toggleBeans[i / 3][i % 3].getMsg_on());
            }

        } else if (!t.isChecked() && toggleBeans[i / 3][i % 3] != null) {
            if (!toggleBeans[i / 3][i % 3].getMsg_off().equals("")) {
                if (toggleBeans[i / 3][i % 3].isCharCheckOff()) {
                    ChatActivity.getMain().setIsCharSend(true);
                } else {
                    ChatActivity.getMain().setIsCharSend(false);

                }
                ChatActivity.getMain().sendMsg(toggleBeans[i / 3][i % 3].getMsg_off());
            }
        }
    }

}
