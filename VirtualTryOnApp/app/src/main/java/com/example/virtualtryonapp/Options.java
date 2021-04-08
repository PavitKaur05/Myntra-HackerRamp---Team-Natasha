package com.example.virtualtryonapp;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.File;
import java.util.ArrayList;

public class Options extends AppCompatActivity {
    Button cloth,makeup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.options);

        cloth = findViewById(R.id.clothing);
        makeup = findViewById(R.id.makeup);

        cloth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Toast.makeText(getApplicationContext(),"see clothing options",Toast.LENGTH_LONG).show();
                Intent i;
                i = new Intent(Options.this, ContentPage.class);
                startActivity(i);
            }

        });

        makeup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Toast.makeText(getApplicationContext(),"see makeup options",Toast.LENGTH_LONG).show();
                Intent i;
                i = new Intent(Options.this, LipSticks.class);
                startActivity(i);

            }

        });

    }
}
