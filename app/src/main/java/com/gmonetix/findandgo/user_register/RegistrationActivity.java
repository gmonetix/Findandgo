package com.gmonetix.findandgo.user_register;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.gmonetix.findandgo.R;
import com.gmonetix.findandgo.user_login.LoginActivity;

public class RegistrationActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText etName, etEmail, etNumber, etPassword;
    private Button register;
    private TextView loginHere;

    String name, email, number, password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        init();

    }

    private void init() {
        etName = (EditText) findViewById(R.id.et_user_register_name);
        etEmail = (EditText) findViewById(R.id.et_user_register_email);
        etNumber = (EditText) findViewById(R.id.et_user_register_number);
        etPassword = (EditText) findViewById(R.id.et_user_register_password);
        register = (Button) findViewById(R.id.register);
        loginHere = (TextView) findViewById(R.id.user_register_login);

        register.setOnClickListener(this);
        loginHere.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.register:
                if (validate()){
                    register();
                }
                break;
            case R.id.user_register_login:
                startActivity(new Intent(RegistrationActivity.this, LoginActivity.class));
                RegistrationActivity.this.finish();
                break;
        }
    }

    private void register() {
        //register new suer here
    }

    public boolean validate() {
        boolean valid = true;

        name = etName.getText().toString().trim();
        email = etEmail.getText().toString().trim();
        number = etNumber.getText().toString().trim();
        password = etPassword.getText().toString().trim();

        if (name.isEmpty() || name.length()<5) {
            etName.setError(getResources().getString(R.string.name_error));
            valid = false;
        } else {
            etName.setError(null);
        }

        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            etEmail.setError(getResources().getString(R.string.email_error));
            valid = false;
        } else {
            etEmail.setError(null);
        }

        if (number.isEmpty() || number.length() < 10 || number.length() > 10) {
            etNumber.setError(getResources().getString(R.string.number_error));
            valid = false;
        } else {
            etNumber.setError(null);
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
