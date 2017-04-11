package com.gmonetix.findandgo.user_register;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.gmonetix.findandgo.R;

public class PhoneRegisterActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText etNumber;
    private Button submit;
    private TextView registration;

    private String number;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone_register);
        init();
    }

    private void init() {
        etNumber = (EditText) findViewById(R.id.et_phone_register_number);
        submit = (Button) findViewById(R.id.submit_number);
        registration = (TextView) findViewById(R.id.phone_register_login);

        submit.setOnClickListener(this);
        registration.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.submit_number:
                if (validate()){
                    submit();
                }
                break;
            case R.id.phone_register_login:
                startActivity(new Intent(PhoneRegisterActivity.this,RegistrationActivity.class));
                PhoneRegisterActivity.this.finish();
                break;
        }
    }

    private void submit() {
        // submit the number in DB
    }

    public boolean validate() {
        boolean valid = true;

        number = etNumber.getText().toString().trim();
        if (number.isEmpty() || number.length() < 10 || number.length() > 10) {
            etNumber.setError(getResources().getString(R.string.number_error));
            valid = false;
        } else {
            etNumber.setError(null);
        }

        return valid;
    }

}
