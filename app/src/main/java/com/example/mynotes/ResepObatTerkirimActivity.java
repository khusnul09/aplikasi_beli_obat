package com.example.mynotes;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;

public class ResepObatTerkirimActivity extends AppCompatActivity {

    Button selesai;
    ImageView Kembali;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resep_obat_terkirim);

        selesai = findViewById(R.id.btn_selesai);
        selesai.setOnClickListener(v -> {
            Intent intentSelesai = new Intent(getApplicationContext(), RiwayatActivity.class); //pindah
            intentSelesai.putExtra("fragmentItem", 0);
            startActivity(intentSelesai);
            finish();
        });

        Kembali = findViewById(R.id.iv_back_resep_terkirim);
        Kembali.setOnClickListener(v -> {
            Intent intent = new Intent (getApplicationContext(), PesanObatActivity.class);
            startActivity(intent);

        });

    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(ResepObatTerkirimActivity.this, PesanObatActivity.class);
        startActivity(intent);
        finish();
    }
}