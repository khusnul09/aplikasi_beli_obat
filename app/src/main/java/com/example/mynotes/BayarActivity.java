package com.example.mynotes;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class BayarActivity extends AppCompatActivity {

    TextView NomorRekening, totalHarga;
    ImageView Copy, Kembali;
    Button UploadNantiObat, uploadSekarang;
    String TotalHarga, Invoice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bayar);

        Intent intent = getIntent();
        TotalHarga = intent.getStringExtra("total_harga");
        Invoice = intent.getStringExtra("invoice");

        NomorRekening = findViewById(R.id.tv_no_rekening_bayar);
        Copy = findViewById(R.id.iv_copy_bayar);
        Copy.setOnClickListener(v -> {
            ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
            ClipData clip = ClipData.newPlainText("simple text", NomorRekening.getText());
            clipboard.setPrimaryClip(clip);

            Toast.makeText(BayarActivity.this, "Nomor rekening berhasil disalin", Toast.LENGTH_SHORT).show();

        });

        uploadSekarang = findViewById(R.id.btn_upload_sekarang_obat);
        uploadSekarang.setOnClickListener(v -> {
            Intent intentSekarang = new Intent(getApplicationContext(), KonfirmasiPembayaranActivity.class);
            intentSekarang.putExtra("Total", TotalHarga);
            intentSekarang.putExtra("invoice", Invoice);
            startActivity(intentSekarang);
        });

        UploadNantiObat = findViewById(R.id.btn_upload_nanti_obat);
        UploadNantiObat.setOnClickListener(v -> {
            Intent intentUploadNanti = new Intent(getApplicationContext(), RiwayatActivity.class); //kembali ke fragment 1
            intentUploadNanti.putExtra("fragmentItem", 1);
            startActivity(intentUploadNanti);
        });

        Kembali = findViewById(R.id.iv_kembali_fragmant_obb);
        Kembali.setOnClickListener(v -> {
//                Intent intentUploadNanti = new Intent(getApplicationContext(), RiwayatActivity.class); //kembali ke fragment 1
//                intentUploadNanti.putExtra("fragmentItem", 1);
//                startActivity(intentUploadNanti);
            onBackPressed();
        });

        totalHarga = findViewById(R.id.tv_nominal_tot_bayar_obb);

        totalHarga.setText(Rupiah.formatUangId(getApplicationContext(), Double.parseDouble(String.valueOf(TotalHarga))));
    }
}