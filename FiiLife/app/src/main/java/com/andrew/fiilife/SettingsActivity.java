package com.andrew.fiilife;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.andrew.fiilife.data.SharedPref;

public class SettingsActivity extends AppCompatActivity {


    private TextView logOutButton;
    private SharedPref manager = new SharedPref();
    private Context context;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        context = SettingsActivity.this;
        findViewById(R.id.back_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });


        logOutButton = (TextView) findViewById(R.id.logOutButton);
        logOutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                manager.setUserID(context,0);
                manager.setUserEmail(context, "0");
                manager.setUserToken(context,"0");
                manager.setUserType(context,0);
                manager.setUserName(context,"0");
                Intent intent = new Intent(SettingsActivity.this, LogInActivity.class);
                startActivity(intent);
                finishAffinity();


            }
        });


    }
}
