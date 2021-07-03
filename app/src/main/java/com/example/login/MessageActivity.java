package com.example.login;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.example.login.model.Message;
import com.example.login.model.User;

import java.util.ArrayList;

public class MessageActivity extends AppCompatActivity {
    LinearLayout content;
    TextView tvName;
    EditText etChatbox;
    Button btnSend;
    ScrollView scrollView;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);
        getSupportActionBar().hide();

        content = (LinearLayout) findViewById(R.id.activity_message_content);
        tvName = (TextView) findViewById(R.id.activity_message_tv_name);
        etChatbox = (EditText) findViewById(R.id.activity_message_tv_chatbox);
        btnSend = (Button) findViewById(R.id.activity_message_btn_sending_message);
        scrollView = (ScrollView) findViewById((R.id.activity_message_scrolling_content));

        scrollView.scrollTo(0, Integer.MAX_VALUE);

        DatabaseHandler db = new DatabaseHandler(this);
        Intent intent = this.getIntent();
        int receiverId = intent.getIntExtra("receiverId", 0);

        tvName.setText(intent.getStringExtra("receiverLastName"));

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

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void updateContent() {
        Intent intent = this.getIntent();
        int receiverId = intent.getIntExtra("receiverId", 0);
        DatabaseHandler db = new DatabaseHandler(this);
        User user = db.getUser();
        ArrayList<Message> list = (ArrayList<Message>) db.getMessages(receiverId);

        FragmentManager fragMan = getFragmentManager();
        FragmentTransaction fragTransaction = fragMan.beginTransaction();

        for (Fragment fragment : fragMan.getFragments())
            fragTransaction.remove(fragment);
        fragTransaction.commit();

        fragTransaction = fragMan.beginTransaction();

        int[] index = { 0 };
        FragmentTransaction finalFragTransaction = fragTransaction;
        list.forEach(item -> {
            MyMessageFragment myMessageFragment = new MyMessageFragment();
            OtherMessageFragment otherMessageFragment = new OtherMessageFragment();
            Bundle bundleItem = new Bundle();
            bundleItem.putString("message", item.getMessage());
            myMessageFragment.setArguments(bundleItem);
            otherMessageFragment.setArguments(bundleItem);
            finalFragTransaction.add(content.getId(), item.getSenderId() == user.getId() ? myMessageFragment : otherMessageFragment, "MessageList" + index[0]);
            index[0]++;
        });
        fragTransaction.commit();
    }
}