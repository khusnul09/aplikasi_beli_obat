package com.example.mynotes;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class DetailRiwayatResepActivity extends AppCompatActivity {

    String urlSelesaikan = "https://obats.000webhostapp.com/api/user/selesaikan";

    TextView namaPenerima, handphone, alamat, detailAlamat, status, invoice, TotalHarga;
    String NamaPenerima, HandphonePenerima, AlamatPenerima, DetailAlamatPenerima, statusdesc, Invoice,
            Gambar, Harga;
    int Status;
    LinearLayout TotalPembayaranResep;
    View Viewresep;
    Button InfoRekBank, pesananDiterima;
    ImageView img, Kembali;

    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_riwayat_resep);

        namaPenerima = findViewById(R.id.tv_nama_riwayat_resep_belum);
        handphone = findViewById(R.id.tv_hp_riwayat_resep_belum);
        alamat = findViewById(R.id.tv_alamat_riwayat_resep_belum);
        detailAlamat = findViewById(R.id.tv_det_riwayat_resep_belum);
        status = findViewById(R.id.tv_status_resep);
        invoice = findViewById(R.id.kode_resep);
        img = findViewById(R.id.showImgResepRiwayatBelum);
        InfoRekBank = findViewById(R.id.btn_info_rek_bank);
        TotalHarga = findViewById(R.id.tv_nominal_tot_riwayat_resep);
        TotalPembayaranResep = findViewById(R.id.ll_total_pembayaran_resep);
        Viewresep = findViewById(R.id.view_resep);

        pesananDiterima = findViewById(R.id.btn_pesanan_diterima);

        Kembali = findViewById(R.id.iv_kembali_fragmant_resep);
        Kembali.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), RiwayatActivity.class); //kembali ke fragment resep
                intent.putExtra("fragmentItem", 0);
                startActivity(intent);
            }
        });

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

        if (Status == 0) {
            statusdesc = "Menunggu Konfirmasi Apotek";
            InfoRekBank.setVisibility(View.GONE);
            pesananDiterima.setVisibility(View.GONE);
        } else if (Status == 1) {
            statusdesc = "Menunggu Pembayaran";
            pesananDiterima.setVisibility(View.GONE);
            if (Harga.equals("0")) {
                InfoRekBank.setVisibility(View.GONE);
                TotalPembayaranResep.setVisibility(View.GONE);
                Viewresep.setVisibility(View.GONE);
            } else {
                TotalPembayaranResep.setVisibility(View.VISIBLE);
                Viewresep.setVisibility(View.VISIBLE);
                InfoRekBank.setVisibility(View.VISIBLE);
                InfoRekBank.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(DetailRiwayatResepActivity.this, InfoPembayaranResepActivity.class);
                        intent.putExtra("harganya", Harga);
                        intent.putExtra("invoice", Invoice);
                        startActivity(intent);
                    }
                });

            }
        } else if (Status == 2) {
            statusdesc = "Pesanan dikemas";
            InfoRekBank.setVisibility(View.GONE);
            pesananDiterima.setVisibility(View.GONE);
        } else if (Status == 3) {
            statusdesc = "Pesanan dikirim";
            InfoRekBank.setVisibility(View.GONE);
            pesananDiterima.setVisibility(View.VISIBLE);
            pesananDiterima.setOnClickListener(v -> {
                selesaikan();
            });
        }else if (Status == 4) {
            statusdesc = "Pesanan diterima";
            InfoRekBank.setVisibility(View.GONE);
        }
        status.setText(statusdesc);

        Picasso.get()
                .load(Gambar)
                .into(img);

    }

    private void selesaikan() {
        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Menyelesaikan Order...");
        progressDialog.show();

        StringRequest request = new StringRequest(Request.Method.POST, urlSelesaikan,
                response -> {
                    Log.i("khatima", response);
                    try {
                        JSONObject objectResponse = new JSONObject(response);
                        if (objectResponse.getString("respon").equals("berhasil")) {
                            Intent intentSelesai= new Intent(getApplicationContext(), RiwayatActivity.class); //kembali ke fragment 1
                            intentSelesai.putExtra("fragmentItem", 2);
                            startActivity(intentSelesai);
                        } else {
                            Toast.makeText(getApplicationContext(), "Gagal", Toast.LENGTH_SHORT).show();
                        }
                        progressDialog.dismiss();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }, error -> Log.i("Khusnul", String.valueOf(error))) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> param = new HashMap<>();
                param.put("invoice", Invoice);
                return param;
            }
        };
        RequestQueue queue = Volley.newRequestQueue(Objects.requireNonNull(getApplicationContext()));
        queue.add(request);
    }
}