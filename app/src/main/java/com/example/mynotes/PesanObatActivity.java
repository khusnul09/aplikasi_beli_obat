package com.example.mynotes;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

public class PesanObatActivity extends AppCompatActivity {
    private CardView OpenImage, PilihObat;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pesan_obat);
        OpenImage = findViewById(R.id.ada_resep);
        OpenImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PesanObatActivity.this, PesanDenganResepActivity.class);
                startActivity(intent);
            }
        });
        PilihObat = findViewById(R.id.no_resep);
        PilihObat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PesanObatActivity.this, PesanTanpaResepActivity.class);
                startActivity(intent);
            }
        });
    }
}
