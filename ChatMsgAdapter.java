package com.example.ccy.assistant;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;


class ChatMsgAdapter extends BaseAdapter {
    private List<ChatMsgEntity> coll;
    private Context context;
    float mRawX;
    float mRawY;
    private View mView;
    private List<String> popupMenuItemList = new ArrayList<>();
    private int longPosition;

    public ChatMsgAdapter(Context context, List<ChatMsgEntity> coll) {
        this.coll = coll;
        this.context = context;
        popupMenuItemList.add("删除");
        popupMenuItemList.add("复制");
        popupMenuItemList.add("重发");
    }

    public int getCount() {

        return coll.size();
    }

    @Override
    public Object getItem(int position) {

        return coll.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getViewTypeCount() {
        return 2;
    }

    @Override
    public int getItemViewType(int position) {
        ChatMsgEntity entity = (ChatMsgEntity) getItem(position);
        if (entity == null) {
            return -1;
        }
        if (entity.getMsgType() == 1) {
            return 1;
        }
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ChatMsgEntity entity = coll.get(position);
        boolean isComMsg;
        ViewHolder holder;
        if (convertView == null) {
            if (getItemViewType(position) == 1) {
                convertView = LayoutInflater.from(context).inflate(R.layout.list_say_to_he, null);
                isComMsg = true;
            } else {
                convertView = LayoutInflater.from(context).inflate(R.layout.list_say_to_me, null);
                isComMsg = false;
            }
            holder = new ViewHolder();
            holder.tvSendTime = (TextView) convertView.findViewById(R.id.messagedetail_row_date);
            holder.tvUserName = (TextView) convertView.findViewById(R.id.messagedetail_row_name);
            holder.tvContent = (TextView) convertView.findViewById(R.id.messagedetail_row_text);
            holder.icon = (ImageView) convertView.findViewById(R.id.messagegedetail_rov_icon);
            holder.isComMsg = isComMsg;
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.tvContent.setText(entity.getText());
        holder.tvContent.setTag(position);
        holder.tvSendTime.setText(entity.getDate());
        holder.tvUserName.setText(entity.getName());
        holder.tvContent.setTag(R.id.messagedetail_row_text, convertView);
        holder.tvContent.setTag(R.id.messagegedetail_rov_icon, holder.isComMsg);
        holder.tvContent.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                mRawX = event.getRawX();
                mRawY = event.getRawY();
                return false;
            }
        });

        holder.tvContent.setOnLongClickListener(new OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                mView = v;
                longPosition = (Integer) v.getTag();
                PopupList popupList = new PopupList(v.getContext());
                popupList.showPopupListWindow(v, (Integer) v.getTag(), mRawX, mRawY, popupMenuItemList, new PopupList.PopupListListener() {
                    @Override
                    public boolean showPopupList(View adapterView, View contextView, int contextPosition) {
                        return true;
                    }

                    @Override
                    public void onPopupListClick(View contextView, int contextPosition, int position) {
                        switch (position) {
                            case 0:

                                if (coll.size() > 1) {
                                    removeListItem();
                                } else {
                                    coll.remove(longPosition);
                                    ChatMsgAdapter.this.notifyDataSetChanged();
                                }
                                break;
                            case 1:
                                ClipboardManager cm = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);//完成这个功能
                                String str = coll.get(longPosition).getText().toString();
                                if (!ChatActivity.getMain().getIsCharSend()) {
                                    str = str.replaceAll("0x", "");
                                    str = str.replaceAll(" ", "");
                                }
                                ClipData myClip;//数据对象
                                myClip = ClipData.newPlainText("text", str);//这顶格式
                                cm.setPrimaryClip(myClip);
                                break;
                            case 2:
                                String msg = coll.get(longPosition).getText().toString();
                                if (!ChatActivity.getMain().getIsCharSend()) {
                                    msg = msg.replaceAll("0x", "");
                                    msg = msg.replaceAll(" ", "");
                                }
                                ChatActivity.getMain().sendMsg(msg);
                                break;
                        }

                    }
                });
                return true;
            }
        });

        return convertView;
    }

    static class ViewHolder {
        public TextView tvSendTime;
        public TextView tvUserName;
        public TextView tvContent;
        public ImageView icon;
        public boolean isComMsg = true;
    }


    protected void removeListItem() {//删除是动画

        final Animation animation;
        if ((boolean) mView.getTag(R.id.messagegedetail_rov_icon))
            animation = AnimationUtils.loadAnimation(context, R.anim.item_delete);
        else
            animation = AnimationUtils.loadAnimation(context, R.anim.anim_delete2);

        ((View) mView.getTag(R.id.messagedetail_row_text)).startAnimation(animation);

        animation.setAnimationListener(new Animation.AnimationListener() {

            public void onAnimationStart(Animation animation) {
            }

            public void onAnimationRepeat(Animation animation) {
            }

            public void onAnimationEnd(Animation animation) {//动画播放完后才删除
                coll.remove(longPosition);
                ChatMsgAdapter.this.notifyDataSetChanged();
                animation.cancel();
            }
        });


    }
}