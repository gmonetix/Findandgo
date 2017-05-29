package com.gmonetix.findandgo.user_login;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class OtpVerificationActivity extends AppCompatActivity {

    private Button verify;
    private EditText etOtp;

    private String number;
    private Utils utils;

    private String otp;
    int result ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp_verification);

        verify = (Button)  findViewById(R.id.verify_otp);
        etOtp = (EditText) findViewById(R.id.et_otp);
        utils = new Utils(OtpVerificationActivity.this,OtpVerificationActivity.this);

        if (getIntent().hasExtra("number"))
            number = getIntent().getExtras().getString("number");
        else number = "0";

        if (getIntent().hasExtra("otp"))
            otp = getIntent().getExtras().getString("otp");

        verify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (verify() == 1){
                    Intent i = new Intent(OtpVerificationActivity.this,HomeActivity.class);
                    i.putExtra("number",number);
                    startActivity(i);
                    finish();
                } else if (verify() == 2){
                    Intent i = new Intent(OtpVerificationActivity.this,LoginActivity.class);
                    i.putExtra("number",number);
                    startActivity(i);
                    finish();
                } else if (verify() == 3){
//                    Toast.makeText(OtpVerificationActivity.this,"Errors",Toast.LENGTH_SHORT).show();
                } else if (verify() == 4){
                    Toast.makeText(OtpVerificationActivity.this,"Enter the correct OTP",Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    private int verify() {
        if (etOtp.getText().toString().equals(otp)) {
            StringRequest rqst = new StringRequest(Request.Method.POST, "http://www.findandgo.in/server/checkUser.php",
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                JSONArray jsonArray = new JSONArray(response);
                                JSONObject object = jsonArray.getJSONObject(0);
                                if (object.getString("code").equals("exist")) {
                                    result = 1;
                                    utils.setName(object.getString("name"));
                                    utils.setGender(object.getString("gender"));
                                    utils.setEmail(object.getString("email"));
                                    utils.setNumber(object.getString("phone_number"));
                                    utils.setImageUrl(object.getString("image_url"));
                                    utils.setLoginStatus(true);
                                } else if (object.getString("code").equals("not_exist")) {
                                    result = 2;
                                } else {
                                    result = 3;
                                    Toast.makeText(OtpVerificationActivity.this,"Some Error Occured",Toast.LENGTH_LONG).show();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                                Toast.makeText(OtpVerificationActivity.this,"Error : "+e.toString(),Toast.LENGTH_LONG).show();
                                result = 3;
                            }
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    error.printStackTrace();
                    result = 3;
                    Toast.makeText(OtpVerificationActivity.this,"Error : "+error.toString(),Toast.LENGTH_LONG).show();
                }
            }){
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String,String> params = new HashMap<String, String>();
                    params.put("phone_number",number);
                    return params;
                }
            };
            RequestQueue queue = Volley.newRequestQueue(OtpVerificationActivity.this);
            queue.add(rqst);

        } else {
            result = 4;
        }
        return result;
    }
}
