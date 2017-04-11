package com.gmonetix.findandgo.user_login;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.gmonetix.findandgo.R;
import com.gmonetix.findandgo.user_register.RegistrationActivity;

public class ForgotPasswordActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText etEmail;
    private Button getPassword;
    private TextView login, signUp;

    private String email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);
        init();
    }

    private void init() {
        etEmail = (EditText) findViewById(R.id.et_forgot_password_email);
        getPassword = (Button) findViewById(R.id.send_password);
        login = (TextView) findViewById(R.id.forgot_password_login);
        signUp = (TextView) findViewById(R.id.forgot_password_registration);

        getPassword.setOnClickListener(this);
        login.setOnClickListener(this);
        signUp.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.send_password:
                if (validate()){
                    sendPassword();
                }
                break;
            case R.id.forgot_password_login:
                startActivity(new Intent(ForgotPasswordActivity.this,LoginActivity.class));
                ForgotPasswordActivity.this.finish();
                break;
            case R.id.forgot_password_registration:
                startActivity(new Intent(ForgotPasswordActivity.this,RegistrationActivity.class));
                ForgotPasswordActivity.this.finish();
                break;
        }
    }

    private void sendPassword() {
        //send password to email
    }

    public boolean validate() {
        boolean valid = true;

        email = etEmail.getText().toString().trim();

        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            etEmail.setError(getResources().getString(R.string.email_error));
            valid = false;
        } else {
            etEmail.setError(null);
        }

        return valid;
    }

}
