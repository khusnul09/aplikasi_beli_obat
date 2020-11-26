package com.example.mynotes;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class DetailRiwayatResepSelesaiActivity extends AppCompatActivity {

    TextView namaPenerima, handphone, alamat, detailAlamat, status, invoice, TotalHarga;
    String NamaPenerima, HandphonePenerima, AlamatPenerima, DetailAlamatPenerima, statusdesc, Invoice,
            Gambar, Harga;
    int Status;
    ImageView gambar;
    Button Selesai;
    ImageView img, Kembali;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_riwayat_resep_selesai);

        namaPenerima = findViewById(R.id.tv_nama_riwayat_resep_selesai);
        handphone = findViewById(R.id.tv_tlf_riwayat_resep_selesai);
        alamat = findViewById(R.id.tv_alamat_riwayat_resep_selesai);
        detailAlamat = findViewById(R.id.tv_detai_alamat_riwayat_resep_selesai);
        status = findViewById(R.id.tv_status_resep_selesai);
        invoice = findViewById(R.id.tv_kode_resep_selesai);
        img = findViewById(R.id.showImgResepRiwayatSelesai);
        TotalHarga = findViewById(R.id.tv_nominal_total_resep_selesai);
        gambar = findViewById(R.id.showImgResepRiwayatSelesai);

        Intent intent = getIntent();
        NamaPenerima = intent.getStringExtra("nama");
        HandphonePenerima = intent.getStringExtra("handphone");
        AlamatPenerima = intent.getStringExtra("alamat");
        DetailAlamatPenerima = intent.getStringExtra("detail_alamat");
        Invoice = intent.getStringExtra("invoice");
        Status = intent.getIntExtra("status", 0);
        Gambar = intent.getStringExtra("gambar");
        Harga = intent.getStringExtra("total_harga");

        namaPenerima.setText(NamaPenerima);
        handphone.setText(HandphonePenerima);
        alamat.setText(AlamatPenerima);
        detailAlamat.setText(DetailAlamatPenerima);
        invoice.setText("#"+Invoice);
        TotalHarga.setText(Harga);

        Picasso.get().load(Gambar).into(gambar);
        //Log.i("khatima", Gambar);
    }
}