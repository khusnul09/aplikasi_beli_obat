package com.example.mynotes;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class DetailRiwayatResepActivity extends AppCompatActivity {

    TextView namaPenerima, handphone, alamat, detailAlamat;
    String NamaPenerima, HandphonePenerima, AlamatPenerima, DetailAlamatPenerima;
    Button InfoRekBank;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_riwayat_resep);

        InfoRekBank = findViewById(R.id.btn_info_rek_bank);
        InfoRekBank.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DetailRiwayatResepActivity.this, InfoPembayaranResepActivity.class);
                startActivity(intent);
            }
        });

        namaPenerima = findViewById(R.id.tv_nama_riwayat_resep_belum);
        handphone = findViewById(R.id.tv_hp_riwayat_resep_belum);
        alamat = findViewById(R.id.tv_alamat_riwayat_resep_belum);
        detailAlamat = findViewById(R.id.tv_det_riwayat_resep_belum);

        Intent intent = getIntent();
        NamaPenerima = intent.getStringExtra("nama");
        HandphonePenerima = intent.getStringExtra("handphone");
        AlamatPenerima = intent.getStringExtra("alamat");
        DetailAlamatPenerima = intent.getStringExtra("detail_alamat");

        namaPenerima.setText(NamaPenerima);
        handphone.setText(HandphonePenerima);
        alamat.setText(AlamatPenerima);
        detailAlamat.setText(DetailAlamatPenerima);


    }
}