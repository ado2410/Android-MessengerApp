package com.example.login;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Debug;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class LoginActivity extends AppCompatActivity {
    private TextView tvUsername;
    private TextView tvPassword;
    private Button btnLogin;
    private Button btnRegister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        getSupportActionBar().hide();

        tvUsername = (TextView) findViewById(R.id.activity_login_et_username);
        tvPassword = (TextView) findViewById(R.id.activity_login_et_password);
        btnLogin = (Button) findViewById(R.id.activity_login_btn_login);
        btnRegister = (Button) findViewById(R.id.activity_login_btn_register);

        DatabaseHandler db = new DatabaseHandler(this);

        if (isLoggedIn()) {
            login();
        } else {
            btnLogin.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    rememberUser();
                    boolean success = login();
                    if (!success)
                        makeToast("Sai tên tài khoản hoặc mật khẩu!");
                }
            });
        }

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openRegisterActivity();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        DatabaseHandler db = new DatabaseHandler(this);
        if (db.isLoggedIn())
            openMainActivity();
    }

    private boolean isLoggedIn() {
        SharedPreferences sharedPreferences = this.getSharedPreferences("user", Context.MODE_PRIVATE);
        return sharedPreferences.getString("username", "") != "" && sharedPreferences.getString("password", "") != "null";
    }

    private void rememberUser() {
        String username = tvUsername.getText().toString();
        String password = tvPassword.getText().toString();
        SharedPreferences sharedPreferences = this.getSharedPreferences("user", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("username", username);
        editor.putString("password", password);
        editor.apply();
    }

    private void logout() {
        logout(this);
    }

    public static void logout(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("user", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();

        DatabaseHandler db = new DatabaseHandler(context);
        db.logout();
        db.close();
    }

    private boolean login() {
        SharedPreferences sharedPreferences = this.getSharedPreferences("user", Context.MODE_PRIVATE);

        if (sharedPreferences != null) {
            String username = sharedPreferences.getString("username", "");
            String password = sharedPreferences.getString("password", "");

            DatabaseHandler db = new DatabaseHandler(this);
            if (db.login(username, password)) {
                openMainActivity();
                return true;
            } else {
                logout();
                return false;
            }
        }
        return false;
    }

    private void openMainActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    private void openRegisterActivity() {
        Intent intent = new Intent(this, RegisterActivity.class);
        startActivity(intent);
    }

    private void makeToast(String message) {
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
    }
}