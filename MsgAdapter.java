package com.example.ccy.assistant;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.TextView;

import java.util.List;

/**
 * 消息窗口
 * Created by ccy on 2017/5/20.
 */

public class MsgAdapter extends BaseAdapter {
    List<String> coll;
    Context context;
    public MsgAdapter(Context context,List<String> coll)
    {
        this.coll = coll;
        this.context = context;
    }
    @Override
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
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if(convertView==null)
        {
            holder = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.msg_item_add,null);
            holder.tv = (TextView) convertView.findViewById(R.id.tv);
            holder.rb = (RadioButton) convertView.findViewById(R.id.rb);
            convertView.setTag(holder);

        }else
        {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.tv.setText(coll.get(position));
        holder.rb.setTag(position);
        holder.rb.setChecked(false);
        holder.rb.setOnClickListener(new View.OnClickListener() {
            private int n =0;
            @Override
            public void onClick(View v) {
                n++;
                if(n==1) {
                    if (((RadioButton) v).isChecked())
                        MsgActivity.getMain().setRecord((Integer) v.getTag());
                }else {
                    ((RadioButton) v).setChecked(false);
                    MsgActivity.getMain().remove((Integer) v.getTag());
                    n = 0;
                }

            }
        });

        return convertView;
    }
    static class ViewHolder{
        TextView tv;
        RadioButton rb;
    }
}
