package com.example.mynotes;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class SyaratDanKetentuanActivity extends AppCompatActivity {
    Button buttonSetuju;
    Button buttonTidakSetuju;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_syarat_dan_ketentuan);

        buttonSetuju = findViewById(R.id.setuju);
        buttonSetuju.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SyaratDanKetentuanActivity.this, PesanObatActivity.class);
                startActivity(intent);
            }
        });

        buttonTidakSetuju = findViewById(R.id.tidak_setuju);
        buttonTidakSetuju.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SyaratDanKetentuanActivity.this, LoginActivity.class);
                startActivity(intent);


            }
        });






    }
}
