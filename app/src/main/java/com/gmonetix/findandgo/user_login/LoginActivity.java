package com.gmonetix.findandgo.user_login;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.gmonetix.findandgo.HomeActivity;
import com.gmonetix.findandgo.MapsActivity;
import com.gmonetix.findandgo.R;
import com.gmonetix.findandgo.User;
import com.gmonetix.findandgo.helper.Utils;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.internal.Util;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener, GoogleApiClient.OnConnectionFailedListener {

    private EditText etName, etEmail;
    private LinearLayout loginFb, loginGoogle;
    private Button login;
    private TextView loginWithAnotherNumber;
    private Spinner spinnerGender;

    private String email, name,gender,number;

    private Utils utils;

    GoogleApiClient mGoogleApiClient;

    private DatabaseReference dbReference;
    private FirebaseDatabase mFirebaseInstance;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        init();

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken("1059170450090-ad3iqrnu1slm0gls0evb0tmesu89vr61.apps.googleusercontent.com")
                .requestEmail()
                .requestProfile()
                .build();

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this /* FragmentActivity */, this /* OnConnectionFailedListener */)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

        if (getIntent().hasExtra("number"))
            number = getIntent().getExtras().getString("number");
        else number = "0";

        SignInButton signInButton = (SignInButton) findViewById(R.id.sign_in_button);
        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signIn();
            }
        });

        spinnerGender.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (position){
                    case 0:
                        break;
                    case 1:
                        gender = "MALE";
                        break;
                    case 2:
                        gender = "FEMALE";
                        break;
                    case 3:
                        gender = "OTHERS";
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        List<String> genderCategories = new ArrayList<>();
        genderCategories.add("Please specify your gender");
        genderCategories.add("Male");
        genderCategories.add("Female");
        genderCategories.add("Other");
        ArrayAdapter<String> genderAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, genderCategories);
        genderAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerGender.setAdapter(genderAdapter);

    }

    private void signIn() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, 1);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == 1) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSignInResult(result);
        }
    }

    private void handleSignInResult(GoogleSignInResult result) {
        Log.d("Tag", "handleSignInResult:" + result.isSuccess());
        if (result.isSuccess()) {
            // Signed in successfully, show authenticated UI.
            GoogleSignInAccount acct = result.getSignInAccount();
            Toast.makeText(getApplicationContext(),acct.getDisplayName(),Toast.LENGTH_LONG).show();
//            mStatusTextView.setText(getString(R.string.signed_in_fmt, acct.getDisplayName()));
            updateUI(true);
        } else {
            // Signed out, show unauthenticated UI.
            updateUI(false);
        }
    }

    private void updateUI(boolean b) {

    }


    private void init() {

        utils = new Utils(LoginActivity.this,LoginActivity.this);

        loginFb = (LinearLayout) findViewById(R.id.user_login_via_facebook);
        loginGoogle= (LinearLayout) findViewById(R.id.user_login_via_google);
        login = (Button) findViewById(R.id.sign_in);
        loginWithAnotherNumber = (TextView) findViewById(R.id.user_login_with_another_number);
        spinnerGender = (Spinner) findViewById(R.id.spinner);
        etName = (EditText) findViewById(R.id.et_user_login_name);
        etEmail = (EditText) findViewById(R.id.et_user_login_email);

        loginFb.setOnClickListener(this);
        loginGoogle.setOnClickListener(this);
        login.setOnClickListener(this);
        loginWithAnotherNumber.setOnClickListener(this);

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
                    startActivity(new Intent(LoginActivity.this,HomeActivity.class));
                    finish();
                }
                break;
            case R.id.user_login_with_another_number:
                break;
        }
    }

    private void login() {
        //login here
        /*User user = new User(name,email);
        mFirebaseInstance = FirebaseDatabase.getInstance();
        dbReference = mFirebaseInstance.getReference();
        DatabaseReference child1 = dbReference.child("users");
        DatabaseReference child2 = child1.child(number);
        child2.setValue(user);*/


        RequestQueue queue = Volley.newRequestQueue(LoginActivity.this);
        StringRequest request = new StringRequest(Request.Method.POST, "http://www.findandgo.in/server/login.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONArray jsonArray = new JSONArray(response);
                            JSONObject object = jsonArray.getJSONObject(0);
                            if(object.getString("code").equals("success")){
                                Toast.makeText(getApplicationContext(),object.getString("message"),Toast.LENGTH_SHORT).show();
                                utils.setName(name);
                                utils.setEmail(email);
                                utils.setGender(gender);
                                utils.setNumber(number);
                                utils.setLoginStatus(true);
                            } else {
                                Toast.makeText(getApplicationContext(),object.getString("message"),Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(),error.toString(),Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params = new HashMap<String, String>();
                params.put("name",name);
                params.put("email",email);
                params.put("phone_number",number);
                params.put("gender",gender);
                return params;
            }
        };
        queue.add(request);
    }

    public boolean validate() {
        boolean valid = true;

        email = etEmail.getText().toString().trim();
        name = etName.getText().toString().trim();

        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            etEmail.setError(getResources().getString(R.string.email_error));
            valid = false;
        } else {
            etEmail.setError(null);
        }

        if (name.isEmpty() || name.length() < 4) {
            etName.setError(getResources().getString(R.string.password_error));
            valid = false;
        } else {
            etName.setError(null);
        }
        return valid;
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
}
