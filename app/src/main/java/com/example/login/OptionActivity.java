package com.example.login;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;

public class OptionActivity extends AppCompatActivity {
    RelativeLayout rlLogout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_option);
        getSupportActionBar().hide();

        rlLogout = (RelativeLayout) findViewById(R.id.activity_option_logout);

        rlLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logout();
            }
        });
    }

    private void logout() {
        LoginActivity.logout(this);

        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }
}