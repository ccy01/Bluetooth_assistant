package com.example.ccy.assistant;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.Switch;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ccy on 2017/5/12.
 */

public class MyFragment extends Fragment {


    private List<MsgActivity.MsgListBean> msglist;
    private Dialog dialog1;
    private boolean is_16_send;
    private boolean is_16_rec;
    private CustomTextWatcher textWatcher;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View contextView = inflater.inflate(R.layout.activity_chat, container, false);
        chatView = (ListView) contextView.findViewById(R.id.list);
        sendMsgEdit = (EditText) contextView.findViewById(R.id.etMsg);
        sendMsgEdit.clearFocus();
        sendMsgEdit.setOnEditorActionListener(mWriteListener);
        textWatcher = new CustomTextWatcher(sendMsgEdit);
        sendMsgEdit.addTextChangedListener(textWatcher);
        sendMsgbButton = (LinearLayout) contextView.findViewById(R.id.sendBtn);
        moreBtn = (LinearLayout) contextView.findViewById(R.id.more);
        sendMsgbButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                String message = sendMsgEdit.getText().toString().trim();
                if (message.isEmpty()) {
                    showMsgDialog();
                } else
                    ChatActivity.getMain().sendMsg(message);

            }
        });
        moreBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                showDialog(v);
            }
        });
        SharedPreferences preferences = getActivity().getSharedPreferences("data", Context.MODE_PRIVATE);
        is_16_send = preferences.getBoolean("m_is_16_send", false);
        is_16_rec = preferences.getBoolean("m_is_16_rec", false);
        ChatActivity.getMain().setIsCharRec(!is_16_rec);
        ChatActivity.getMain().setIsCharSend(!is_16_send);
        textWatcher.setFormat(is_16_send);
        return contextView;

    }

    private ListView chatView;
    private LinearLayout sendMsgbButton;
    private EditText sendMsgEdit;
    private ChatMsgAdapter chatAdapter;
    private LinearLayout moreBtn;
    private List<ChatMsgEntity> chatMsg;

    @Override
    public void onStart() {
        initView();
        super.onStart();
    }

    @Override
    public synchronized void onResume() {
        super.onResume();
    }


    @Override
    public synchronized void onPause() {
        super.onPause();

    }

    @Override
    public void onStop() {//不可见的时候
        super.onStop();

    }

    @Override
    public void onDestroy() {
        super.onDestroy();

    }

    private void initView() {
        chatMsg = ChatActivity.getMain().getChatMsg();

        chatAdapter = new ChatMsgAdapter(getContext(), chatMsg);//显示消息机制
        chatView.setAdapter(chatAdapter);
        chatView.setSelection(chatView.getCount() - 1);

    }

    private void restore() {
        chatAdapter = new ChatMsgAdapter(getContext(), chatMsg);//显示消息机制
        chatView = (ListView) getView().findViewById(R.id.list);
        chatView.setAdapter(chatAdapter);
        chatView.setSelection(chatView.getCount() - 1);
    }

    public void displayMsg() {
        if (chatAdapter != null && chatView != null) {
            chatAdapter.notifyDataSetChanged();
            chatView.setSelection(chatView.getCount() - 1);
        } else {
            restore();
        }
    }

    private TextView.OnEditorActionListener mWriteListener = new TextView.OnEditorActionListener() {//设置输入完成的监听
        public boolean onEditorAction(TextView view, int actionId, KeyEvent event) {//

            if (actionId == EditorInfo.IME_NULL && event.getAction() == KeyEvent.ACTION_UP) {
                String message = view.getText().toString();
                ChatActivity.getMain().sendMsg(message);
            }

            return true;
        }
    };

    public void clearMsg() {

        chatMsg.clear();
        chatAdapter.notifyDataSetChanged();

    }

    private void showDialog(View v) {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.select_popupwindow, null);

        PopupWindow popupWindow = new PopupWindow(view, LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        popupWindow.setAnimationStyle(R.style.OverflowMenuStyle);
        popupWindow.setTouchable(true);
        popupWindow.setOutsideTouchable(true);
        Switch send_16 = (Switch) view.findViewById(R.id.send_16);
        Switch rec_16 = (Switch) view.findViewById(R.id.rec_16);
        send_16.setChecked(is_16_send);
        rec_16.setChecked(is_16_rec);
        send_16.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                is_16_send = isChecked;
                textWatcher.setFormat(true);
                sendMsgEdit.setText("");
                if (isChecked) {
                    ChatActivity.getMain().setIsCharSend(false);
                } else {
                    ChatActivity.getMain().setIsCharSend(true);
                    textWatcher.setFormat(false);
                }

            }
        });
        rec_16.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                is_16_rec = isChecked;
                if (isChecked) {
                    ChatActivity.getMain().setIsCharRec(false);
                } else
                    ChatActivity.getMain().setIsCharRec(true);

            }
        });
        popupWindow.setBackgroundDrawable(new BitmapDrawable());
        popupWindow.setFocusable(true);
        int[] location = new int[2];
        v.getLocationOnScreen(location);
        popupWindow.showAtLocation(v, Gravity.NO_GRAVITY, location[0], location[1] - v.getHeight());

    }

    public boolean isMsgEmpty() {
        return chatMsg.isEmpty();

    }

    private void showMsgDialog() {
        msglist = new ArrayList<>();
        SharedPreferences preferences = getActivity().getSharedPreferences("datas", Context.MODE_PRIVATE);
        int n = preferences.getInt("msg_list_size", 0);
        if (n > 0) {
            final View view = LayoutInflater.from(getContext()).inflate(R.layout.msg_dialog, null);
            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
            builder.setTitle("选择消息");
            builder.setView(view);
            ListView listView = (ListView) view.findViewById(R.id.mag_list);
            ArrayAdapter adapter = new ArrayAdapter(getContext(), R.layout.msg_item);
            listView.setAdapter(adapter);
            getDatas(adapter, preferences, n);
            builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            dialog1 = builder.show();
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    MsgActivity.MsgListBean bean = msglist.get(position);
                    ChatActivity.getMain().setIsCharSend(bean.is_char_send());
                    ChatActivity.getMain().sendMsg(bean.getMsg());
                    dialog1.dismiss();
                }
            });
        } else {
            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
            builder.setTitle("暂无消息");
            builder.setMessage("快去添加吧？");
            builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Intent intent = new Intent(getActivity(), MsgActivity.class);
                    startActivity(intent);
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

    }

    private void getDatas(ArrayAdapter adapter, SharedPreferences preferences, int n) {
        msglist.clear();
        for (int i = 0; i < n; i++) {
            MsgActivity.MsgListBean bean = new MsgActivity.MsgListBean();
            bean.setName(preferences.getString("msg_list" + i + "name", ""));
            bean.setMsg(preferences.getString("msg_list" + i + "msg", ""));
            bean.setIs_char_send(preferences.getBoolean("msg_list" + i + "ischar", false));
            bean.setIs_16_send(preferences.getBoolean("msg_list" + i + "is16", false));
            adapter.add(bean.getName());
            msglist.add(bean);
        }
        adapter.notifyDataSetChanged();
    }

    private void saveDatas() {
        SharedPreferences.Editor editor = getActivity().getSharedPreferences("data", Context.MODE_PRIVATE).edit();
        editor.putBoolean("m_is_16_send", is_16_send);
        editor.putBoolean("m_is_16_rec", is_16_rec);
        editor.apply();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        saveDatas();
    }


}


