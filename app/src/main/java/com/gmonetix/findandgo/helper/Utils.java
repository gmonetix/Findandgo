package com.gmonetix.findandgo.helper;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;

public class Utils {

    private Activity activity;
    private Context context;

    private SharedPreferences sharedPreference;
    private SharedPreferences.Editor editor;

    private static final String LOGIN_STATUS_SP = "login_status_SP";
    private static final String LOGIN_STATUS = "login_status";
    private static final String USER_NUMBER_SP = "user_number_SP";
    private static final String USER_NUMBER = "user_number";
    private static final String USER_NAME_SP = "user_name_SP";
    private static final String USER_NAME = "user_name";
    private static final String USER_EMAIL_SP = "user_email_SP";
    private static final String USER_EMAIL = "user_email";
    private static final String USER_GENDER_SP = "user_gender_SP";
    private static final String USER_GENDER = "user_gender";
    private static final String USER_IMAGE_SP = "user_image_SP";
    private static final String USER_IMAGE = "user_image";

    public Utils(Activity activity, Context context) {
        this.activity = activity;
        this.context = context;
    }

    /*public void setFont(Context _context, TextView textView) {
        Typeface roboto = Typeface.createFromAsset(_context.getAssets(), "font.otf");
        textView.setTypeface(roboto);
    }*/

    public void showImageChooser(int REQUEST_CODE) {
        Intent intent = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        activity.startActivityForResult(Intent.createChooser(intent, "Select Picture"), REQUEST_CODE);
    }

    public String getStringImage(Bitmap bmp){
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 50, baos); // 50% size & quality is reduced for faster uploading
        byte[] imageBytes = baos.toByteArray();
        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return encodedImage;
    }

    public void saveProfileImage(Bitmap bitmap) {
        FileOutputStream out;
        try {
            out = context.openFileOutput("profile.png", Context.MODE_PRIVATE);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 90, out);
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Bitmap getImageBitmap(){
        try{
            FileInputStream fis = context.openFileInput("profile.png");
            Bitmap b = BitmapFactory.decodeStream(fis);
            fis.close();
            return b;
        }
        catch(Exception e){
            e.printStackTrace();
        }
        return null;
    }

    public boolean getLoginStatus() {

        boolean status = false;
        sharedPreference = activity.getSharedPreferences(LOGIN_STATUS_SP , Context.MODE_PRIVATE);
        if(sharedPreference.contains(LOGIN_STATUS)){
            status = sharedPreference.getBoolean(LOGIN_STATUS,false);
        }
        return status;
    }

    public void setLoginStatus(boolean status) {
        sharedPreference = activity.getSharedPreferences(LOGIN_STATUS_SP , Context.MODE_PRIVATE);
        editor = sharedPreference.edit();
        editor.putBoolean(LOGIN_STATUS,status);
        editor.commit();
    }

    public void setNumber(String number) {
        sharedPreference = activity.getSharedPreferences(USER_NUMBER_SP , Context.MODE_PRIVATE);
        editor = sharedPreference.edit();
        editor.putString(USER_NUMBER,number);
        editor.commit();
    }

    public String getNumber() {
        String number = "";
        sharedPreference = activity.getSharedPreferences(USER_NUMBER_SP , Context.MODE_PRIVATE);
        if(sharedPreference.contains(USER_NUMBER)){
            number = sharedPreference.getString(USER_NUMBER,"");
        }
        return number;
    }

    public void setName(String name) {
        sharedPreference = activity.getSharedPreferences(USER_NAME_SP , Context.MODE_PRIVATE);
        editor = sharedPreference.edit();
        editor.putString(USER_NAME,name);
        editor.commit();
    }

    public String getName() {
        String name = "";
        sharedPreference = activity.getSharedPreferences(USER_NAME_SP , Context.MODE_PRIVATE);
        if(sharedPreference.contains(USER_NAME)){
            name = sharedPreference.getString(USER_NAME,"");
        }
        return name;
    }

    public void setEmail(String email) {
        sharedPreference = activity.getSharedPreferences(USER_EMAIL_SP , Context.MODE_PRIVATE);
        editor = sharedPreference.edit();
        editor.putString(USER_EMAIL,email);
        editor.commit();
    }

    public String getEmail() {
        String email = "";
        sharedPreference = activity.getSharedPreferences(USER_EMAIL_SP , Context.MODE_PRIVATE);
        if(sharedPreference.contains(USER_EMAIL)){
            email = sharedPreference.getString(USER_EMAIL,"");
        }
        return email;
    }

    public void setGender(String gender) {
        sharedPreference = activity.getSharedPreferences(USER_GENDER_SP , Context.MODE_PRIVATE);
        editor = sharedPreference.edit();
        editor.putString(USER_GENDER,gender);
        editor.commit();
    }

    public String getGender() {
        String gender = "";
        sharedPreference = activity.getSharedPreferences(USER_GENDER_SP , Context.MODE_PRIVATE);
        if(sharedPreference.contains(USER_GENDER)){
            gender = sharedPreference.getString(USER_GENDER,"MALE");
        }
        return gender;
    }

    public void setImageUrl(String url) {
        sharedPreference = activity.getSharedPreferences(USER_IMAGE_SP , Context.MODE_PRIVATE);
        editor = sharedPreference.edit();
        editor.putString(USER_IMAGE,url);
        editor.commit();
    }

    public String getImageUrl() {
        String url = "";
        sharedPreference = activity.getSharedPreferences(USER_IMAGE_SP , Context.MODE_PRIVATE);
        if(sharedPreference.contains(USER_IMAGE)){
            url = sharedPreference.getString(USER_IMAGE,"");
        }
        return url;
    }

}
