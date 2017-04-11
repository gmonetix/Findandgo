package com.gmonetix.findandgo.user_login;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.gmonetix.findandgo.MapsActivity;
import com.gmonetix.findandgo.R;
import com.gmonetix.findandgo.user_register.RegistrationActivity;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText etEmail, etPassword;
    private LinearLayout signInFb, signInGoogle;
    private Button signIn;
    private TextView forgotPassword, signUp;

    private String email, password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        init();
    }

    private void init() {
        etEmail =(EditText) findViewById(R.id.et_user_login_email);
        etPassword = (EditText) findViewById(R.id.et_user_login_password);
        signInFb = (LinearLayout) findViewById(R.id.user_login_via_facebook);
        signInGoogle = (LinearLayout) findViewById(R.id.user_login_via_google);
        signIn = (Button) findViewById(R.id.sign_in);
        forgotPassword = (TextView) findViewById(R.id.user_login_forgot_password);
        signUp = (TextView) findViewById(R.id.user_login_registration);

        signInFb.setOnClickListener(this);
        signInGoogle.setOnClickListener(this);
        signIn.setOnClickListener(this);
        forgotPassword.setOnClickListener(this);
        signUp.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.user_login_via_facebook:

                break;
            case R.id.user_login_via_google:

                break;
            case R.id.sign_in:
                if (validate()){
                    login();
                }
                break;
            case R.id.user_login_forgot_password:
                startActivity(new Intent(LoginActivity.this,ForgotPasswordActivity.class));
                LoginActivity.this.finish();
                break;
            case R.id.user_login_registration:
                startActivity(new Intent(LoginActivity.this,RegistrationActivity.class));
                LoginActivity.this.finish();
                break;
        }
    }

    private void login() {
        //login here
        startActivity(new Intent(LoginActivity.this, MapsActivity.class));
    }

    public boolean validate() {
        boolean valid = true;

        email = etEmail.getText().toString().trim();
        password = etPassword.getText().toString().trim();

        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            etEmail.setError(getResources().getString(R.string.email_error));
            valid = false;
        } else {
            etEmail.setError(null);
        }

        if (password.isEmpty() || password.length() < 6) {
            etPassword.setError(getResources().getString(R.string.password_error));
            valid = false;
        } else {
            etPassword.setError(null);
        }
        return valid;
    }

}
