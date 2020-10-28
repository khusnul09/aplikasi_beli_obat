package com.example.mynotes;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class PembayaranActivity extends AppCompatActivity {

    Button UploadSekarang, UploadNanti;
    TextView NomorRekening;
    ImageView Copy;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pembayaran);

        UploadSekarang = findViewById(R.id.btn_upload_sekarang);
        UploadSekarang.setOnClickListener(v -> {
            Intent intent = new Intent(getApplicationContext(), KonfirmasiPembayaranActivity.class);
            startActivity(intent);
        });

        UploadNanti = findViewById(R.id.btn_upload_nanti);
        UploadNanti.setOnClickListener(v -> {
            Intent intent = new Intent(getApplicationContext(), RiwayatActivity.class); //pindah
            intent.putExtra("fragmentItem", 1);
            startActivity(intent);
        });

        NomorRekening = findViewById(R.id.tv_no_rekening);
        Copy = findViewById(R.id.iv_copy);
        Copy.setOnClickListener(v -> {
            ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
            ClipData clip = ClipData.newPlainText("simple text", NomorRekening.getText());
            clipboard.setPrimaryClip(clip);

            Toast.makeText(PembayaranActivity.this, "Nomor rekening berhasil disalin", Toast.LENGTH_SHORT).show();

        });
    }
}