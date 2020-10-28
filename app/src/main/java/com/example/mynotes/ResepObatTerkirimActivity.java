package com.example.mynotes;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class ResepObatTerkirimActivity extends AppCompatActivity {

    Button selesai;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resep_obat_terkirim);

        selesai = findViewById(R.id.btn_selesai);
        selesai.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ResepObatTerkirimActivity.this, PesanObatActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(ResepObatTerkirimActivity.this, PesanObatActivity.class);
        startActivity(intent);
        finish();
    }
}