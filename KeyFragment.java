package com.example.ccy.assistant;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.*;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.*;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Switch;
import android.widget.Toast;
import android.widget.ToggleButton;

/**
 * Created by ccy on 2017/5/13.
 */


public class KeyFragment extends Fragment implements View.OnTouchListener {
    private Button bt1;
    private Button bt2;
    private Button bt3;
    private Button bt4;
    private Button bt5;
    private Button bt6;
    private Button bt7;
    private Button bt8;
    private Button bt9;
    private Button bt10;
    private Button bt11;
    private Button bt12;
    private Switch switch_edit;
    private RadioButton rb_char_p;
    private RadioButton rb_16_p;
    private RadioButton rb_char_u;
    private RadioButton rb_16_u;
    private EditText et_name;
    private EditText et_p_msg;
    private EditText et_u_msg;
    private View layout;
    private AlertDialog.Builder builder;
    private KeyBean[][] keyBeans = new KeyBean[4][3];

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_key, container, false);
        initView(view);
        restoreStateFromArguments();
        return view;
    }

    public void initView(View v) {
        bt1 = (Button) v.findViewById(R.id.bt_1);
        bt2 = (Button) v.findViewById(R.id.bt_2);
        bt3 = (Button) v.findViewById(R.id.bt_3);
        bt4 = (Button) v.findViewById(R.id.bt_4);
        bt5 = (Button) v.findViewById(R.id.bt_5);
        bt6 = (Button) v.findViewById(R.id.bt_6);
        bt7 = (Button) v.findViewById(R.id.bt_7);
        bt8 = (Button) v.findViewById(R.id.bt_8);
        bt9 = (Button) v.findViewById(R.id.bt_9);
        bt10 = (Button) v.findViewById(R.id.bt_10);
        bt11 = (Button) v.findViewById(R.id.bt_11);
        bt12 = (Button) v.findViewById(R.id.bt_12);
        switch_edit = (Switch) v.findViewById(R.id.switch_edit);

        bt1.setOnTouchListener(this);
        bt2.setOnTouchListener(this);
        bt3.setOnTouchListener(this);
        bt4.setOnTouchListener(this);
        bt5.setOnTouchListener(this);
        bt6.setOnTouchListener(this);
        bt7.setOnTouchListener(this);
        bt8.setOnTouchListener(this);
        bt9.setOnTouchListener(this);
        bt10.setOnTouchListener(this);
        bt11.setOnTouchListener(this);
        bt12.setOnTouchListener(this);
    }

    private void showEditDialog(final View v) {

        layout = View.inflate(getContext(), R.layout.key_dialog, null);
        rb_char_p = (RadioButton) layout.findViewById(R.id.rb_key_char);
        rb_16_p = (RadioButton) layout.findViewById(R.id.rb_key_16);
        rb_char_u = (RadioButton) layout.findViewById(R.id.rb_key_char1);
        rb_16_u = (RadioButton) layout.findViewById(R.id.rb_key_161);
        et_name = (EditText) layout.findViewById(R.id.et_name);
        et_p_msg = (EditText) layout.findViewById(R.id.et_p_msg);
        final CustomTextWatcher watcher = new CustomTextWatcher(et_p_msg);
        et_p_msg.addTextChangedListener(watcher);
        et_u_msg = (EditText) layout.findViewById(R.id.et_u_msg);
        final CustomTextWatcher watcher1 = new CustomTextWatcher(et_u_msg);
        et_u_msg.addTextChangedListener(watcher1);
        builder = new AlertDialog.Builder(getContext());
        builder.setTitle("按钮编辑");
        builder.setView(layout);
        if (keyBeans[(Integer) v.getTag() / 3][(Integer) v.getTag() % 3] != null) {
            KeyBean t = keyBeans[(Integer) v.getTag() / 3][(Integer) v.getTag() % 3];
            et_name.setText(t.getEt_name());
            et_p_msg.setText(t.getEt_p_msg());
            et_u_msg.setText(t.getEt_u_msg());
            if (t.isRb_char_p()) {
                rb_char_p.setChecked(true);
                watcher.setFormat(false);
                rb_16_p.setChecked(false);
            } else {
                rb_char_p.setChecked(false);
                watcher.setFormat(true);
                rb_16_p.setChecked(true);
            }
            if (t.isRb_char_u()) {
                rb_char_u.setChecked(true);
                watcher1.setFormat(false);
                rb_16_u.setChecked(false);
            } else {
                rb_char_u.setChecked(false);
                watcher1.setFormat(true);
                rb_16_u.setChecked(true);
            }
        }
        rb_16_p.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                watcher.setFormat(isChecked);
                et_p_msg.setText("");
            }
        });
        rb_16_u.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                watcher1.setFormat(isChecked);
                et_u_msg.setText("");
            }
        });
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {//完成一些事情
            @Override
            public void onClick(DialogInterface dialog, int which) {
                KeyBean keyBean = new KeyBean();
                keyBean.setEt_name(et_name.getText().toString());
                keyBean.setEt_p_msg(et_p_msg.getText().toString());
                keyBean.setEt_u_msg(et_u_msg.getText().toString());
                keyBean.setRb_char_p(rb_char_p.isChecked());
                keyBean.setRb_char_u(rb_char_u.isChecked());
                keyBeans[(Integer) v.getTag() / 3][(Integer) v.getTag() % 3] = null;
                keyBeans[(Integer) v.getTag() / 3][(Integer) v.getTag() % 3] = keyBean;
                ((Button) v).setText(keyBeans[(Integer) v.getTag() / 3][(Integer) v.getTag() % 3].getEt_name());

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

    public void writeComP(int i) {
        if (keyBeans[i / 3][i % 3] != null) {
            if (!keyBeans[i / 3][i % 3].getEt_p_msg().equals("")) {
                if (keyBeans[i / 3][i % 3].isRb_char_p()) {
                    ChatActivity.getMain().setIsCharSend(true);
                } else {
                    ChatActivity.getMain().setIsCharSend(false);
                }
                ChatActivity.getMain().sendMsg(keyBeans[i / 3][i % 3].getEt_p_msg());
            }
        }
    }

    public void writeComU(int i) {
        if (keyBeans[i / 3][i % 3] != null) {
            if (!keyBeans[i / 3][i % 3].getEt_u_msg().equals("")) {
                if (keyBeans[i / 3][i % 3].isRb_char_u()) {
                    ChatActivity.getMain().setIsCharSend(true);
                } else {
                    ChatActivity.getMain().setIsCharSend(false);
                }
                ChatActivity.getMain().sendMsgD(keyBeans[i / 3][i % 3].getEt_u_msg());
            }
        }
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        int action = event.getAction();
        switch (v.getId()) {

            case R.id.bt_1:

                if (action == MotionEvent.ACTION_DOWN) { // 按下
                    if (switch_edit.isChecked()) {
                        v.setTag(0);
                        showEditDialog(v);
                    } else {
                        writeComP(0);

                    }
                } else if (action == MotionEvent.ACTION_UP) { // 松开
                    if (!switch_edit.isChecked())
                        writeComU(0);
                }

                break;
            case R.id.bt_2:
                if (action == MotionEvent.ACTION_DOWN) { // 按下
                    if (switch_edit.isChecked()) {
                        v.setTag(1);
                        showEditDialog(v);
                    } else {
                        writeComP(1);

                    }
                } else if (action == MotionEvent.ACTION_UP) { // 松开
                    if (!switch_edit.isChecked())
                        writeComU(1);
                }
                break;
            case R.id.bt_3:
                if (action == MotionEvent.ACTION_DOWN) { // 按下
                    if (switch_edit.isChecked()) {
                        v.setTag(2);
                        showEditDialog(v);
                    } else {
                        writeComP(2);

                    }
                } else if (action == MotionEvent.ACTION_UP) { // 松开
                    if (!switch_edit.isChecked())
                        writeComU(2);
                }
                break;
            case R.id.bt_4:
                if (action == MotionEvent.ACTION_DOWN) { // 按下
                    if (switch_edit.isChecked()) {
                        v.setTag(3);
                        showEditDialog(v);
                    } else {
                        writeComP(3);

                    }
                } else if (action == MotionEvent.ACTION_UP) { // 松开
                    if (!switch_edit.isChecked())
                        writeComU(3);
                }
                break;
            case R.id.bt_5:
                if (action == MotionEvent.ACTION_DOWN) { // 按下
                    if (switch_edit.isChecked()) {
                        v.setTag(4);
                        showEditDialog(v);
                    } else {
                        writeComP(4);
                    }
                } else if (action == MotionEvent.ACTION_UP) { // 松开
                    if (!switch_edit.isChecked())
                        writeComU(4);
                }
                break;
            case R.id.bt_6:
                if (action == MotionEvent.ACTION_DOWN) { // 按下
                    if (switch_edit.isChecked()) {
                        v.setTag(5);
                        showEditDialog(v);
                    } else {
                        writeComP(5);

                    }
                } else if (action == MotionEvent.ACTION_UP) { // 松开
                    if (!switch_edit.isChecked())
                        writeComU(5);
                }
                break;
            case R.id.bt_7:
                if (action == MotionEvent.ACTION_DOWN) { // 按下
                    if (switch_edit.isChecked()) {
                        v.setTag(6);
                        showEditDialog(v);
                    } else {
                        writeComP(6);

                    }
                } else if (action == MotionEvent.ACTION_UP) { // 松开
                    if (!switch_edit.isChecked())
                        writeComU(6);
                }
                break;
            case R.id.bt_8:
                if (action == MotionEvent.ACTION_DOWN) { // 按下
                    if (switch_edit.isChecked()) {
                        v.setTag(7);
                        showEditDialog(v);
                    } else {
                        writeComP(7);

                    }
                } else if (action == MotionEvent.ACTION_UP) { // 松开
                    if (!switch_edit.isChecked())
                        writeComU(7);
                }
                break;
            case R.id.bt_9:
                if (action == MotionEvent.ACTION_DOWN) { // 按下
                    if (switch_edit.isChecked()) {
                        v.setTag(8);
                        showEditDialog(v);
                    } else {
                        writeComP(8);

                    }
                } else if (action == MotionEvent.ACTION_UP) { // 松开
                    if (!switch_edit.isChecked())
                        writeComU(8);
                }
                break;
            case R.id.bt_10:
                if (action == MotionEvent.ACTION_DOWN) { // 按下
                    if (switch_edit.isChecked()) {
                        v.setTag(9);
                        showEditDialog(v);
                    } else {
                        writeComP(9);

                    }
                } else if (action == MotionEvent.ACTION_UP) { // 松开
                    if (!switch_edit.isChecked())
                        writeComU(9);
                }
                break;
            case R.id.bt_11:
                if (action == MotionEvent.ACTION_DOWN) { // 按下
                    if (switch_edit.isChecked()) {
                        v.setTag(10);
                        showEditDialog(v);
                    } else {

                        writeComP(10);
                    }
                } else if (action == MotionEvent.ACTION_UP) { // 松开
                    if (!switch_edit.isChecked())
                        writeComU(10);
                }
                break;
            case R.id.bt_12:
                if (action == MotionEvent.ACTION_DOWN) { // 按下
                    if (switch_edit.isChecked()) {
                        v.setTag(11);
                        showEditDialog(v);
                    } else {

                        writeComP(11);
                    }
                } else if (action == MotionEvent.ACTION_UP) { // 松开
                    if (!switch_edit.isChecked())
                        writeComU(11);
                }
                break;
        }
        return false;
    }

    private void saveStateToArguments() {
        SharedPreferences.Editor editor = getActivity().getPreferences(Context.MODE_PRIVATE).edit();
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 3; j++) {
                if (keyBeans[i][j] == null) {
                    break;
                }
                editor.putString("name" + i * 3 + j, keyBeans[i][j].getEt_name());
                editor.putString("msg_p" + i * 3 + j, keyBeans[i][j].getEt_p_msg());
                editor.putString("msg_u" + i * 3 + j, keyBeans[i][j].getEt_u_msg());
                editor.putBoolean("is_char_check_p" + i * 3 + j, keyBeans[i][j].isRb_char_p());
                editor.putBoolean("is_char_check_u" + i * 3 + j, keyBeans[i][j].isRb_char_u());
            }
        }
        editor.apply();

    }

    private void restoreStateFromArguments() {

        SharedPreferences preferences = getActivity().getPreferences(Context.MODE_PRIVATE);
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 3; j++) {
                KeyBean keyBean = new KeyBean();
                keyBean.setEt_name(preferences.getString("name" + i * 3 + j, ""));
                keyBean.setEt_p_msg(preferences.getString("msg_p" + i * 3 + j, ""));
                keyBean.setEt_u_msg(preferences.getString("msg_u" + i * 3 + j, ""));
                keyBean.setRb_char_p(preferences.getBoolean("is_char_check_p" + i * 3 + j, false));
                keyBean.setRb_char_u(preferences.getBoolean("is_char_check_u" + i * 3 + j, false));
                keyBeans[i][j] = keyBean;
                restoreState(keyBeans[i][j], i * 3 + j);//显示按钮的信息

            }
        }

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onDestroyView() {
        saveStateToArguments();
        super.onDestroyView();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {

        super.onActivityCreated(savedInstanceState);
    }

    private void restoreState(KeyBean toggleBean, int i) {

        switch (i) {
            case 0:
                bt1.setText(toggleBean.getEt_name());

                break;
            case 1:
                bt2.setText(toggleBean.getEt_name());

                break;
            case 2:
                bt3.setText(toggleBean.getEt_name());

                break;
            case 3:
                bt4.setText(toggleBean.getEt_name());

                break;
            case 4:
                bt5.setText(toggleBean.getEt_name());

                break;
            case 5:
                bt6.setText(toggleBean.getEt_name());

                break;
            case 6:
                bt7.setText(toggleBean.getEt_name());

                break;
            case 7:
                bt8.setText(toggleBean.getEt_name());

                break;
            case 8:
                bt9.setText(toggleBean.getEt_name());

                break;
            case 9:
                bt10.setText(toggleBean.getEt_name());

                break;
            case 10:
                bt11.setText(toggleBean.getEt_name());

                break;
            case 11:
                bt12.setText(toggleBean.getEt_name());

                break;
        }

    }
}
