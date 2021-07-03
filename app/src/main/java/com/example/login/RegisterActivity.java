package com.example.login;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class RegisterActivity extends AppCompatActivity {
    private EditText etUsername;
    private EditText firstName;
    private EditText lastName;
    private EditText etPassword;
    private EditText etConfirmedPassword;
    Button btnRegister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        etUsername = (EditText) findViewById(R.id.activity_register_et_username);
        firstName = (EditText) findViewById(R.id.activity_register_et_first_name);
        lastName = (EditText) findViewById(R.id.activity_register_et_last_name);
        etPassword = (EditText) findViewById(R.id.activity_register_et_password);
        etConfirmedPassword = (EditText) findViewById(R.id.activity_register_et_confirmed_password);
        btnRegister = (Button) findViewById(R.id.activity_register_btn_register);

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                register();
            }
        });
    }

    public void register() {
        DatabaseHandler db = new DatabaseHandler(this);
        int state = db.register(etUsername.getText().toString(), etPassword.getText().toString(), etConfirmedPassword.getText().toString(), firstName.getText().toString(), lastName.getText().toString());
        if (state == 1)
            makeToast("Tên tài khoản đã tồn tại, thử lại tên tài khoản khác");
        else if (state == 2)
            makeToast("Mật khẩu và nhập lại mật khẩu không trùng khớp");
        else
            openLoginActivity();
    }

    private void openLoginActivity() {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }

    private void makeToast(String message) {
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
    }
}