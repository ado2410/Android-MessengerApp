package com.example.login;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.login.model.MessageList;
import com.example.login.model.User;

import java.util.List;

public class SearchActivity extends AppCompatActivity {
    ImageView ivSearchClear;
    EditText etSearching;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        getSupportActionBar().hide();

        etSearching = (EditText) findViewById(R.id.activity_search_et_search);
        ivSearchClear = (ImageView) findViewById(R.id.activity_search_iv_search_clear);

        updateUserList();

        etSearching.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN) {
                    switch (keyCode) {
                        case KeyEvent.KEYCODE_ENTER:
                            updateUserList(etSearching.getText().toString());
                            return true;
                    }
                }
                return false;
            }
        });

        ivSearchClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                etSearching.setText("");
                updateUserList();
            }
        });
    }

    protected void updateUserList() {
        updateUserList("");
    }

    protected void updateUserList(String keyword) {
        DatabaseHandler db = new DatabaseHandler(this);
        List<User> users = db.getUsers(keyword);
        ListView layout = (ListView) findViewById(R.id.activity_search_lv_user_list);

        SearchAdapter adapter = new SearchAdapter(this, users);
        layout.setAdapter(adapter);
    }
}