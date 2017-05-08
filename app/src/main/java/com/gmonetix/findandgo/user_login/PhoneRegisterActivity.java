package com.gmonetix.findandgo.user_login;

import android.content.Intent;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.gmonetix.findandgo.HomeActivity;
import com.gmonetix.findandgo.R;
import com.gmonetix.findandgo.helper.Utils;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class PhoneRegisterActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText etNumber;
    private Button submit;
    private TextView tv1, tvPrivacyPolicy;

    private String number;
    private Utils utils;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone_register);
        if (Build.VERSION.SDK_INT >= 21) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        }
        utils = new Utils(PhoneRegisterActivity.this,PhoneRegisterActivity.this);
        if (utils.getLoginStatus()) {
            startActivity(new Intent(PhoneRegisterActivity.this, HomeActivity.class));
            finish();
        }
        init();
    }

    private void init() {
        etNumber = (EditText) findViewById(R.id.et_phone_register_number);
        submit = (Button) findViewById(R.id.phone_register_submit_number);
        tv1 = (TextView) findViewById(R.id.phone_register_text1);
        tvPrivacyPolicy = (TextView) findViewById(R.id.phone_register_privacyPolicy);

        submit.setOnClickListener(this);
        tvPrivacyPolicy.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.phone_register_submit_number:
                if (validate()){
                    submit();
                }
                break;
            case R.id.phone_register_privacyPolicy:
                MaterialDialog privacyDialog = new MaterialDialog.Builder(this)
                        .title("Privacy Policy")
                        .icon(getResources().getDrawable(R.drawable.help_icon))
                        .content("You consent to the use of your personal information by Gmonetix.com and/or its third party providers and distributors in accordance with the terms of and for the purposes set forth in our Privacy Policy. Gmonetix does not rent, sell, or share personal information about you with other people or non affiliated companies except to provide products or services you've requested, when we have your permission, or under the following circumstances:\n" +
                                "\n" +
                                "Gmonetix.com provides the information to trusted partners who work on behalf of or with Gmonetix under extremely strict confidentiality agreements. These companies may use your personal information to help Gmonetix communicate with you about offers from Gmonetix and our marketing partners. However, these companies do not have any independent right to share this information.\n" +
                                "\n" +
                                "You may choose to give us personal information, such as your name, contact number, address or e-mail id that may be needed, for example, to correspond with you, to participate on the site, forum, contests, use of softwares, articles, white papers or to provide you with a subscription. If you tell us that you do not want us to use this information as a basis for further contact with you, we will respect your wishes. We intend to protect the quality and integrity of your personally identifiable information.\n" +
                                "\n" +
                                "Gmonetix may share your information with its business partners who sponsor an event, white paper downloads, contest, or promotion; or who jointly offer a service or feature with Gmonetix.com.\n" +
                                "\n" +
                                "We will make a sincere effort to respond in a timely manner to your requests to correct inaccuracies in your personal information. To correct inaccuracies in your personal information please return the message containing the inaccuracies to the sender email id with details of the correction requested.\n" +
                                "\n" +
                                "We sometimes collect anonymous information from visits to our sites to help us provide better customer service. For example, we keep track of the domains from which people visit and we also measure visitor activity on Gmonetix web site, but we do so in ways that keep the information anonymous. Gmonetix or its affiliates or vendors may use this data to analyze trends and statistics and to help us provide better customer service. We maintain the highest levels of confidentiality for this information; our affiliates and vendors follow the same high levels of confidentiality. This anonymous information is used and analyzed only at an aggregate level to help us understand trends and patterns. None of this information is reviewed at an individual level. If you do not want your transaction details used in this manner, you can either disable your cookies or opt-out by sending an email to editor at Gmonetix.com.\n" +
                                "\n" +
                                "Gmonetix may, if you so choose, send direct mailers to you at the address given by you. You have the option to 'opt-out' of this direct mailer by way of links provided at the bottom of each mailer. We respect your privacy and in the event that you choose to not receive such mailers, we will take all steps to remove you from the list.\n" +
                                "\n" +
                                "We transfer information about you if Gmonetix is acquired by or merged with another company.\n" +
                                "\n" +
                                "We believe it is necessary to share information in order to investigate, prevent, or take action regarding illegal activities, suspected fraud, situations involving potential threats to the physical safety of any person, or as otherwise required by law.")
                        .positiveText("Ok")
                        .onPositive(new MaterialDialog.SingleButtonCallback() {
                            @Override
                            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                dialog.dismiss();
                            }
                        })
                        .show();
                break;
        }
    }

    private void submit() {
        // send otp to this number

        Intent i = new Intent(PhoneRegisterActivity.this,OtpVerificationActivity.class);
        i.putExtra("number",number);
        i.putExtra("otp",generateOTP());
        startActivity(i);
        finish();
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

    public String generateOTP() {

        String n = "0123456789";
        Random random = new Random();
        char[] otp_arr = new char[4];

        for (int i=0;i<4;i++) {
            otp_arr[i] = n.charAt(random.nextInt(n.length()));
        }

        String otp = new String(otp_arr);
        Toast.makeText(getApplicationContext(),"OTP : " + otp,Toast.LENGTH_LONG).show();
        /*StringRequest request = new StringRequest(Request.Method.POST, "uel",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                return super.getParams();
            }
        };
        RequestQueue queue = Volley.newRequestQueue(PhoneRegisterActivity.this);
        queue.add(request);*/

        return otp;
    }

}
