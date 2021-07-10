package com.example.login;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.example.login.model.Message;
import com.example.login.model.User;

import java.util.ArrayList;

public class MessageActivity extends AppCompatActivity {
    ListView lvList;
    TextView tvName;
    EditText etChatbox;
    Button btnSend;

    private ArrayList<Message> list;
    private MessageAdapter adapter;
    private int messageId;
    private int position;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);
        getSupportActionBar().hide();

        lvList = (ListView) findViewById(R.id.activity_main_lv_list);
        tvName = (TextView) findViewById(R.id.activity_message_tv_name);
        etChatbox = (EditText) findViewById(R.id.activity_message_tv_chatbox);
        btnSend = (Button) findViewById(R.id.activity_message_btn_sending_message);

        DatabaseHandler db = new DatabaseHandler(this);
        Intent intent = this.getIntent();
        int receiverId = intent.getIntExtra("receiverId", 0);

        User receiver = db.getUser(receiverId);

        tvName.setText(receiver.getLastName());

        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!etChatbox.getText().toString().isEmpty()) {
                    db.sendMessage(receiverId, etChatbox.getText().toString());
                    etChatbox.setText("");
                    updateContent();
                }
            }
        });

        updateContent();
    }

    private void updateContent() {
        Intent intent = this.getIntent();
        int receiverId = intent.getIntExtra("receiverId", 0);
        DatabaseHandler db = new DatabaseHandler(this);
        User user = db.getUser();
        list = (ArrayList<Message>) db.getMessages(receiverId);

        adapter = new MessageAdapter(this, list);

        lvList.setAdapter(adapter);
    }

    public boolean recallMessage() {
        DatabaseHandler db = new DatabaseHandler(this);
        if(db.recallMessage(messageId)) {
            list.remove(position);
            adapter.notifyDataSetChanged();
            return true;
        } else
            return false;
    }

    public void openMenu(int messageId, int position) {
        this.messageId = messageId;
        this.position = position;
        MessageMenuFragment menu = new MessageMenuFragment();
        menu.show(getSupportFragmentManager(), "MESSAGE_MENU");
    }
}