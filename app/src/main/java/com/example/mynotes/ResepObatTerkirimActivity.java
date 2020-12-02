package com.example.mynotes;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

public class ResepObatTerkirimActivity extends AppCompatActivity {

    Button selesai;

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
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(ResepObatTerkirimActivity.this, PesanObatActivity.class);
        startActivity(intent);
        finish();
    }
}