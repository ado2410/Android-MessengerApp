package com.example.login;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.login.model.MessageList;

import java.util.Date;
import java.util.List;

public class MessageListAdapter extends BaseAdapter {
    Context context;
    List<MessageList> list;

    public MessageListAdapter(Context context, List<MessageList> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public MessageList getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = View.inflate(context, R.layout.fragment_message_list, null);

        RelativeLayout rlContainer = (RelativeLayout) convertView.findViewById(R.id.fagment_message_list_rl_container);
        ImageView ivImage = (ImageView) convertView.findViewById(R.id.fragment_message_list_iv_avatar);
        TextView tvName = (TextView) convertView.findViewById(R.id.fragment_message_list_tv_name);
        TextView tvLastMessage = (TextView) convertView.findViewById(R.id.fragment_message_list_tv_last_message);
        TextView tvTime = (TextView) convertView.findViewById(R.id.fragment_message_list_tv_time);

        tvName.setText(list.get(position).getFirstName() + " " + list.get(position).getLastName());
        tvLastMessage.setText(list.get(position).getLastMessage());
        Date time = new Date(String.valueOf(list.get(position).getTime()));
        tvTime.setText(time.getHours() + ":" + (time.getMinutes() < 10 ? "0" + time.getMinutes() : time.getMinutes()));

        rlContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context.getApplicationContext(), MessageActivity.class);
                intent.putExtra("receiverId", list.get(position).getId());
                context.startActivity(intent);
            }
        });

        return convertView;
    }
}
