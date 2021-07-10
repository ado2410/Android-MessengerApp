package com.example.login;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.login.model.Message;

import java.util.ArrayList;
import java.util.List;

public class MessageAdapter extends BaseAdapter {
    MessageActivity context;
    List<Message> list = new ArrayList<>();

    public MessageAdapter(MessageActivity context, List<Message> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        DatabaseHandler db = new DatabaseHandler(context);
        Message message = list.get(position);

        View myMessageView = View.inflate(context, R.layout.fragment_my_message, null);
        View otherMessageView = View.inflate(context, R.layout.fragment_other_message, null);

        if (message.getSenderId() == db.getUser().getId()) {
            convertView = myMessageView;
            TextView tvText = (TextView) convertView.findViewById(R.id.fragment_my_message_tv_text);
            tvText.setText(message.getMessage());
            tvText.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    context.openMenu(message.getId(), position);
                    return true;
                }
            });

        } else {
            convertView = otherMessageView;
            TextView tvText = (TextView) convertView.findViewById(R.id.fragment_other_message_tv_text);
            tvText.setText(message.getMessage());
        }

        return convertView;
    }
}
