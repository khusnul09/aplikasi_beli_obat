package com.example.mynotes;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

public class PesanObatActivity extends AppCompatActivity {
    private CardView OpenImage, PilihObat, Riwayat;
    ImageView Akun;


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

        Riwayat = findViewById(R.id.riwayat);
        Riwayat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PesanObatActivity.this, RiwayatActivity.class);
                startActivity(intent);
            }
        });
        Akun = findViewById(R.id.btn_akun);
        Akun.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PesanObatActivity.this, ProfilActivity.class);
                startActivity(intent);
            }
        });

    }

    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this)
                .setTitle("Keluar dari aplikasi?")
                .setMessage("Apakah Anda ingin keluar dari aplikasi?")
                .setPositiveButton("Ya", (dialog, which) -> finish())
                .setNegativeButton("Tidak", null)
                .show();
    }
}
