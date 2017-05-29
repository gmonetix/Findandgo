package com.gmonetix.findandgo;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;

public class SettingsActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private Button changeNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        SettingsActivity.this.setTitle("Settings");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        getFragmentManager().beginTransaction().replace(R.id.settings_frame_layout,new SettingsFragment()).commit();

        changeNumber = (Button) findViewById(R.id.change_number_settings_activity);
        changeNumber.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //change the number
                dialogChangeNumber(SettingsActivity.this);
            }
        });

    }

    public void dialogChangeNumber(Context context) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Change Number");
        builder.setMessage("Do yu want to proceed ?");
        builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                changeNumber();
            }
        });
        builder.setNegativeButton("NO",null);
        builder.show();
    }

    private void changeNumber() {
        //make the layouts
    }

}
