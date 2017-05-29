package com.gmonetix.findandgo.user_profile;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.gmonetix.findandgo.R;
import com.gmonetix.findandgo.helper.Const;
import com.gmonetix.findandgo.helper.CustomDialog;
import com.gmonetix.findandgo.helper.Utils;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class ProfileActivity extends AppCompatActivity implements View.OnClickListener {

    private Toolbar toolbar;
    private ImageView profileImage, editName, editEmail;
    private TextView tvName, tvEmail,  tvPhone;
    private FloatingActionButton editPic;
    private Switch numberVisibility;

    private int numberVisibilityState = 0;
    private Utils utils;

    private String imageString;

    private final static int IMAGE_CHOOSER_CODE = 100;

    private static final String TAG = ProfileActivity.class.getSimpleName();

    private Uri filePath = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ProfileActivity.this.setTitle("Profile");

        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(this).build();
        ImageLoader.getInstance().init(config);

        init();
        tvName.setText(utils.getName());
        tvPhone.setText(utils.getNumber());
        tvEmail.setText(utils.getEmail());

        if (utils.getImageBitmap() != null) {
            profileImage.setImageBitmap(utils.getImageBitmap());
        } else {
            ImageLoader.getInstance().displayImage(utils.getImageUrl(), profileImage, new ImageLoadingListener() {
                @Override
                public void onLoadingStarted(String s, View view) {
                }

                @Override
                public void onLoadingFailed(String s, View view, FailReason failReason) {
                    profileImage.setImageDrawable(getResources().getDrawable(R.drawable.gth));
                }

                @Override
                public void onLoadingComplete(String s, View view, Bitmap bitmap) {
                    profileImage.setImageBitmap(bitmap);
                    utils.saveProfileImage(bitmap);
                }

                @Override
                public void onLoadingCancelled(String s, View view) {

                }
            });
        }
        numberVisibility.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                updateNumberVisibility(isChecked);
            }
        });

    }

    private void init() {
        profileImage = (ImageView) findViewById(R.id.profileImage);
        tvName = (TextView) findViewById(R.id.profile_activity_name);
        tvEmail = (TextView) findViewById(R.id.profile_activity_email);
        tvPhone = (TextView) findViewById(R.id.profile_activity_phone);
        editPic = (FloatingActionButton) findViewById(R.id.profile_activity_change_pic);
        editName = (ImageView) findViewById(R.id.profile_activity_edit_name);
        editEmail = (ImageView) findViewById(R.id.profile_activity_edit_email);
        numberVisibility = (Switch) findViewById(R.id.profile_activity_number_visibility);

        profileImage.setOnClickListener(this);
        editPic.setOnClickListener(this);
        editName.setOnClickListener(this);
        editEmail.setOnClickListener(this);

        utils = new Utils(ProfileActivity.this,ProfileActivity.this);

    }

    @Override
    public void onClick(View v) {
        CustomDialog customDialog = new CustomDialog(ProfileActivity.this);
        switch (v.getId()){
            case R.id.profileImage:
                customDialog.imageViewDialog(utils.getImageBitmap());
                break;
            case R.id.profile_activity_change_pic:
                utils.showImageChooser(IMAGE_CHOOSER_CODE);
                break;
            case R.id.profile_activity_edit_name:
                String name = customDialog.updateNameDialog("Enter your new name");
                if (name != null && !name.equals("")) {
                    tvName.setText(name);
                }
                break;
            case R.id.profile_activity_edit_email:
                String email = customDialog.updateEmailDialog("Enter your new email");
                if (email != null && !email.equals("")) {
                    tvEmail.setText(email);
                }
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == IMAGE_CHOOSER_CODE && resultCode == RESULT_OK && data != null && data.getData() != null) {
            filePath = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(),filePath);
                uploadProfilePicInServer(bitmap);
                utils.saveProfileImage(bitmap);
                profileImage.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void uploadProfilePicInServer(Bitmap bitmap) {

        final ProgressDialog loading = ProgressDialog.show(this,"Updating...","Please wait...",false,false);

        imageString = "";
        if (bitmap != null)
        {
            imageString = utils.getStringImage(bitmap);
        } else {
//            if (gender.equals("MALE")) {
//                bitmap  = BitmapFactory.decodeResource(getResources(),R.drawable.male);
//                image = getStringImage(bitmap);
//            } else {
//                bitmap  = BitmapFactory.decodeResource(getResources(),R.drawable.female);
//                image = getStringImage(bitmap);
//            }
        }
        StringRequest stringRequest = new StringRequest(Request.Method.POST, "http://www.findandgo.in/server/uploadProfilePic.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        loading.dismiss();
                        try {
                            JSONArray array = new JSONArray(response);
                            JSONObject object = array.getJSONObject(0);
                            if (object.getString("code").equals("success")) {
                                Toast.makeText(ProfileActivity.this,object.getString("message"),Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(ProfileActivity.this,object.getString("message"),Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(ProfileActivity.this,e.toString(),Toast.LENGTH_SHORT).show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                loading.dismiss();
                error.printStackTrace();
                Toast.makeText(ProfileActivity.this,error.toString(),Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params = new HashMap<String, String>();
                params.put("image",imageString);
                params.put("phone_number",utils.getNumber());
                return  params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(ProfileActivity.this);
        requestQueue.add(stringRequest);
    }

    private void updateNumberVisibility(boolean value) {
        if (value) {
            numberVisibilityState = 1;
        } else {
            numberVisibilityState = 0;
        }

        final ProgressDialog loading = ProgressDialog.show(this,"Updating...","Please wait...",false,false);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, "http://www.findandgo.in/server/number_visibility.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        loading.dismiss();
                        try {
                            JSONArray array = new JSONArray(response);
                            JSONObject object = array.getJSONObject(0);
                            if (object.getString("code").equals("success")) {
                                Toast.makeText(ProfileActivity.this,object.getString("message"),Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(ProfileActivity.this,object.getString("message"),Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(ProfileActivity.this,e.toString(),Toast.LENGTH_SHORT).show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                loading.dismiss();
                Toast.makeText(ProfileActivity.this,error.toString(),Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params = new HashMap<String, String>();
                params.put("phone_number",utils.getNumber());
                params.put("phone_visibility",String.valueOf(numberVisibilityState));
                return  params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(ProfileActivity.this);
        requestQueue.add(stringRequest);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.profile_activity_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.delete_account_menu:
                new AlertDialog.Builder(this)
                        .setTitle("Warning")
                        .setIcon(R.drawable.ic_warning)
                        .setMessage("Are you sure you want to delete your account?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                deleteAccount(utils.getNumber());
                            }
                        })
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        })
                        .show();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public void deleteAccount(final String number) {
        final ProgressDialog loading = ProgressDialog.show(this,"Deleting...","Please wait...",false,false);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, "http://www.findandgo.in/server/deleteAccount.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        loading.dismiss();
                        try {
                            JSONArray array = new JSONArray(response);
                            JSONObject object = array.getJSONObject(0);
                            if (object.getString("code").equals("success")) {
                                Toast.makeText(ProfileActivity.this,object.getString("message"),Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(ProfileActivity.this,object.getString("message"),Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(ProfileActivity.this,e.toString(),Toast.LENGTH_SHORT).show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                loading.dismiss();
                Toast.makeText(ProfileActivity.this,error.toString(),Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params = new HashMap<String, String>();
                params.put("phone_number",number);
                return  params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(ProfileActivity.this);
        requestQueue.add(stringRequest);

    }

}
