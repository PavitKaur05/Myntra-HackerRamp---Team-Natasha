package com.example.virtualtryonapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                //SharedPreferences preferences = getSharedPreferences("tokenFile", Context.MODE_PRIVATE);
                //String typeofuser = preferences.getString("TypeOfUser","");
                Intent i;
                i = new Intent(MainActivity.this, Options.class);
                startActivity(i);
                finish();


            }
        }, 2000);
    }
}