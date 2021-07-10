package com.example.login;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.media.Image;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.example.login.model.Message;
import com.example.login.model.MessageList;
import com.example.login.model.User;

import java.util.List;

public class MainActivity extends AppCompatActivity {
    TextView tvName;
    ImageView ivAvatar;
    EditText etSearch;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();

        tvName = (TextView) findViewById(R.id.activity_main_tv_name);
        ivAvatar = (ImageView) findViewById(R.id.activity_main_iv_avatar);
        etSearch = (EditText) findViewById(R.id.activity_main_et_search);

        DatabaseHandler db = new DatabaseHandler(this);

        //Chưa đăng nhập
        if (!db.isLoggedIn()) {
            openLoginActivity();
        } else {
            tvName.setText(db.getUser().getFirstName() + " " + db.getUser().getLastName());
            ivAvatar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    openOptionActivity();
                }
            });
            updateMessageList();
        }

        etSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), SearchActivity.class);
                startActivity(intent);
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onResume() {
        super.onResume();
        updateMessageList(etSearch.getText().toString());
    }

    private void openOptionActivity() {
        Intent intent = new Intent(this, OptionActivity.class);
        startActivity(intent);
    }

    private void openLoginActivity() {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }

    protected void updateMessageList() {
        updateMessageList("");
    }

    protected void updateMessageList(String keyword) {
        DatabaseHandler db = new DatabaseHandler(this);
        List<MessageList> messageList = db.getMessageList(keyword);
        ListView layout = (ListView) findViewById(R.id.activity_main_lv_message_list);

        MessageListAdapter adapter = new MessageListAdapter(this, messageList);
        layout.setAdapter(adapter);
    }
}