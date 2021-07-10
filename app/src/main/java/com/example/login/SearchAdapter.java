package com.example.login;

import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.login.model.Message;
import com.example.login.model.User;

import java.util.ArrayList;
import java.util.List;

public class SearchAdapter extends BaseAdapter {
    SearchActivity context;
    List<User> list = new ArrayList<>();

    public SearchAdapter(SearchActivity context, List<User> list) {
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
        User user = list.get(position);

        convertView = View.inflate(context, R.layout.fragment_search, null);

        RelativeLayout rlContainer = (RelativeLayout) convertView.findViewById(R.id.fragment_search_rl_container);
        TextView tvName = (TextView) convertView.findViewById(R.id.fragment_search_tv_name);

        tvName.setText(user.getFirstName() + " " + user.getLastName());

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
