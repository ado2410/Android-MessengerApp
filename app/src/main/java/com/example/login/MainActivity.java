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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.login.model.Message;
import com.example.login.model.MessageList;
import com.example.login.model.User;

import java.util.List;

public class MainActivity extends AppCompatActivity {
    TextView tvName;
    ImageView ivAvatar;
    ImageView ivSearchingClear;
    EditText etSearching;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();

        tvName = (TextView) findViewById(R.id.activity_main_tv_name);
        ivAvatar = (ImageView) findViewById(R.id.activity_main_iv_avatar);
        etSearching = (EditText) findViewById(R.id.activity_main_et_searching);
        ivSearchingClear = (ImageView) findViewById(R.id.activity_main_iv_searching_clear);

        DatabaseHandler db = new DatabaseHandler(this);

        //generateSampleData();

        //Đăng nhập
        //db.login("trucgiang", "123");

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
            updateMessageList(etSearching.getText().toString());
        }

        etSearching.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN) {
                    switch (keyCode) {
                        case KeyEvent.KEYCODE_ENTER:
                            updateMessageList(etSearching.getText().toString());
                            return true;
                    }
                }
                return false;
            }
        });

        ivSearchingClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                etSearching.setText("");
                updateMessageList();
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onResume() {
        super.onResume();
        updateMessageList(etSearching.getText().toString());
    }

    private void openOptionActivity() {
        Intent intent = new Intent(this, OptionActivity.class);
        startActivity(intent);
    }

    private void openLoginActivity() {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    protected void updateMessageList() {
        updateMessageList("");
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    protected void updateMessageList(String keyword) {
        DatabaseHandler db = new DatabaseHandler(this);
        List<MessageList> messageList = db.getMessageList(keyword);
        LinearLayout layout = (LinearLayout) findViewById(R.id.activity_main_message_list);
        FragmentManager fragMan = getFragmentManager();
        FragmentTransaction fragTransaction = fragMan.beginTransaction();

        for (Fragment fragment : fragMan.getFragments())
            fragTransaction.remove(fragment);
        fragTransaction.commit();

        fragTransaction = fragMan.beginTransaction();

        int[] index = { 0 };
        FragmentTransaction finalFragTransaction = fragTransaction;
        messageList.forEach(item -> {
            ListFragment listFragment = new ListFragment();
            Bundle bundle = new Bundle();
            bundle.putInt("id", item.getId());
            bundle.putString("firstName", item.getFirstName());
            bundle.putString("lastName", item.getLastName());
            bundle.putString("lastMessage", item.getLastMessage());
            bundle.putString("time", item.getTime().toString());
            listFragment.setArguments(bundle);
            finalFragTransaction.add(layout.getId(), listFragment, "MessageList" + index[0]);
            index[0]++;
        });
        fragTransaction.commit();
    }
}