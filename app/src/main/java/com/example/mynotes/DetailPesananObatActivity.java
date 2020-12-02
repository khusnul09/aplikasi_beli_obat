package com.example.mynotes;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
import com.bumptech.glide.Glide;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class DetailPesananObatActivity extends AppCompatActivity {

    String urlDetail = "https://obats.000webhostapp.com/api/user/detail";
    String urlAntarPesananObat = "https://obats.000webhostapp.com/api/user/antarpesananresep";

    TextView namaPenerima, handphone, alamat, detailAlamat, status, invoice, totalHarga, waktu, waktuBayar;
    String NamaPenerima, HandphonePenerima, AlamatPenerima, DetailAlamatPenerima, statusdesc, Invoice,
            Gambar, BuktiBayar, Harga, Waktu, WaktuBayar, TotalHarga, WaktuKirim, strHarga;
    String Status;
    LinearLayout TextBuktiPembayaran, BuktiPembayaran,  WaktuPembayaran, WaktuPengiriman;
    Button Kemas, Antar;
    View view;
    ImageView img, buktiBayar, Kembali;

    AdapterDetailTanpaResep adapterDetailTanpaResep;
    List<ModelDetailTanpaResep> listObatTanpaResep;
    RecyclerView recyclerView;

    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_pesanan_obat);

        Kembali = findViewById(R.id.iv_kembali_pesanan_obat);
        Kembali.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), PesananObatAdminActivity.class);
                startActivity(intent);

            }
        });

//        Kemas = findViewById(R.id.btn_kemas_pesanan_obat);
//        Kemas.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                KemasPesananObat();
//            }
//        });

        Antar = findViewById(R.id.btn_antar_pesanan_obat);
        Antar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AntarPesananObat();
            }
        });

        Calendar c = Calendar.getInstance();

        SimpleDateFormat formatnya = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        WaktuKirim = formatnya.format(c.getTime());

        listObatTanpaResep = new ArrayList<>();
        adapterDetailTanpaResep = new AdapterDetailTanpaResep(getApplicationContext());

        Intent intent = getIntent();
        Invoice = intent.getStringExtra("invoice");
        Log.i("Khatima", Invoice);
        NamaPenerima = intent.getStringExtra("nama");
        HandphonePenerima = intent.getStringExtra("handphone");
        AlamatPenerima = intent.getStringExtra("alamat");
        DetailAlamatPenerima = intent.getStringExtra("detail_alamat");
        BuktiBayar = intent.getStringExtra("bukti_bayar");
        TotalHarga = intent.getStringExtra("total_harga");
        Status = intent.getStringExtra("status");

        Log.i("khatima", BuktiBayar);

        namaPenerima = findViewById(R.id.tv_nama_penerima_pesanan_obat_admin);
        handphone = findViewById(R.id.tv_hp_penerima_pesanan_obat);
        alamat = findViewById(R.id.tv_alamat_penerima_pesanan_obat);
        detailAlamat = findViewById(R.id.tv_det_penerima_pesanan_obat);
        invoice = findViewById(R.id.tv_invoice_pesanan_obat_admin);
        buktiBayar = findViewById(R.id.showImgBuktiBayarObatpAdmin);
        totalHarga = findViewById(R.id.tv_nominal_totbyr_obat_admin);
        view = findViewById(R.id.view_total_bayar);
        TextBuktiPembayaran = findViewById(R.id.ll_foto_bukti_bayar_obat);
        BuktiPembayaran = findViewById(R.id.ll_bukti_bayar_obat_admin);


        Picasso.get()
                .load(BuktiBayar)
                .into(buktiBayar);

        namaPenerima.setText(NamaPenerima);
        handphone.setText(HandphonePenerima);
        alamat.setText(AlamatPenerima);
        detailAlamat.setText(DetailAlamatPenerima);
        totalHarga.setText(TotalHarga + ",-");
        invoice.setText("#" + Invoice);

        Log.i("khatima status", String.valueOf(Status));

        switch (Status){
            case "1": //MENUNGGU PEMBAYARAN
                view.setVisibility(View.INVISIBLE);
                Antar.setVisibility(View.GONE);
                TextBuktiPembayaran.setVisibility(View.GONE);
                BuktiPembayaran.setVisibility(View.GONE);
                break;

            case "2": //PESANAN DIKEMAS
                Antar.setVisibility(View.VISIBLE);
                view.setVisibility(View.VISIBLE);
                TextBuktiPembayaran.setVisibility(View.VISIBLE);
                BuktiPembayaran.setVisibility(View.VISIBLE);
                break;

            case "3": //PESANAN DIKIRIM
                Antar.setVisibility(View.GONE);
                break;
        }


        reqDetail();

        recyclerView = findViewById(R.id.recyclerViewPesananObatAdmin);
        // recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        recyclerView.setAdapter(adapterDetailTanpaResep);

    }

    public void reqDetail() {
        StringRequest request = new StringRequest(Request.Method.POST, urlDetail,
                response -> {
                    Log.i("khatima", response);
                    try {
                        Log.i("khatima", "try dijalankan");
                        JSONObject objectResponse = new JSONObject(response);
                        JSONArray array = objectResponse.getJSONArray("data");
                        for (int i = 0; i < array.length(); i++) {
                            ModelDetailTanpaResep modelDetailTanpaResep = new ModelDetailTanpaResep();
                            modelDetailTanpaResep.setNamaObat(array.getJSONObject(i).optString("nama_obat"));
                            modelDetailTanpaResep.setHargaObat(array.getJSONObject(i).optString("harga_jual"));
                            modelDetailTanpaResep.setKuantitasObat(array.getJSONObject(i).optString("kuantitas"));
                            modelDetailTanpaResep.setGambar(array.getJSONObject(i).optString("gambar"));
                            Log.i("khatima", array.getJSONObject(i).optString("nama_obat"));
                            adapterDetailTanpaResep.add(modelDetailTanpaResep);
                        }
                        adapterDetailTanpaResep.addAll(listObatTanpaResep);
                        adapterDetailTanpaResep.notifyDataSetChanged();

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
        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        queue.add(request);
    }

//    private void KemasPesananObat() {
//        progressDialog = new ProgressDialog(this);
//        progressDialog.setTitle("Kirim...");
//        progressDialog.setCancelable(false);
//        progressDialog.show();
//
//        StringRequest request = new StringRequest(Request.Method.POST, urlSelesaikan,
//                response -> {
//                    Log.i("khatima", response);
//                    try {
//                        JSONObject objectResponse = new JSONObject(response);
//                        if (objectResponse.getString("respon").equals("berhasil")) {
//                            Toast.makeText(getApplicationContext(), "Pengemasan Berhasil", Toast.LENGTH_SHORT).show();
//                        } else {
//                            Toast.makeText(getApplicationContext(), "Gagal", Toast.LENGTH_SHORT).show();
//                        }
//                        progressDialog.dismiss();
//                    } catch (JSONException e) {
//                        e.printStackTrace();
//                    }
//                }, error -> Log.i("Khusnul", String.valueOf(error))) {
//            @Override
//            protected Map<String, String> getParams() {
//                Map<String, String> param = new HashMap<>();
//                param.put("invoice", Invoice);
//                param.put("status", "2");
//                return param;
//            }
//        };
//        RequestQueue queue = Volley.newRequestQueue(Objects.requireNonNull(getApplicationContext()));
//        queue.add(request);
//    }

    private void AntarPesananObat() {
        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Kirim...");
        progressDialog.setCancelable(false);
        progressDialog.show();

        StringRequest request = new StringRequest(Request.Method.POST, urlAntarPesananObat,
                response -> {
                    Log.i("khatima", response);
                    try {
                        JSONObject objectResponse = new JSONObject(response);
                        if (objectResponse.getString("respon").equals("berhasil")) {
                            Toast.makeText(getApplicationContext(), "Pengantaran Berhasil", Toast.LENGTH_SHORT).show();
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
                param.put("status", "3");
                param.put("waktu_pengiriman", WaktuKirim);
                return param;
            }
        };
        RequestQueue queue = Volley.newRequestQueue(Objects.requireNonNull(getApplicationContext()));
        queue.add(request);
    }

}