package com.example.mynotes;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DetailRiwayatObatSelesaiActivity extends AppCompatActivity {

    TextView namaPenerima, handphone, alamat, detailAlamat, status, invoice, totalHarga, waktu, harga, waktuBayar, waktuKirim;
    String NamaPenerima, HandphonePenerima, AlamatPenerima, DetailAlamatPenerima, Invoice,
            Harga, TotalHarga, Waktu, WaktuBayar, WaktuKirim;
    int Status;
    ImageView Kembali;

    AdapterDetailTanpaResep adapterDetailTanpaResep;
    List<ModelDetailTanpaResep> listObatTanpaResep;
    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_riwayat_obat_selesai);


        Kembali = findViewById(R.id.iv_kembali_riwayat_obat_selesai);
        Kembali.setOnClickListener(v -> {
            Intent intent = new Intent(getApplicationContext(), RiwayatActivity.class); //kembali ke fragment selesai
            intent.putExtra("fragmentItem", 2);
            startActivity(intent);
            finish();
        });

        listObatTanpaResep = new ArrayList<>();
        adapterDetailTanpaResep = new AdapterDetailTanpaResep(getApplicationContext());

        namaPenerima = findViewById(R.id.tv_nama_penerima_riwayat_obat_selesai);
        handphone = findViewById(R.id.tv_tlf_riwayat_obat_selesai);
        alamat = findViewById(R.id.tv_alamat_riwayat_obat_selesai);
        detailAlamat = findViewById(R.id.tv_detai_alamat_riwayat_obat_selesai);
        status = findViewById(R.id.tv_status_riwayat_obat_selesai);
        invoice = findViewById(R.id.tv_invoice_riwayat_obat_selesai);
        harga = findViewById(R.id.tv_nominal_subtotal_riwayat_obat_selesai);
        totalHarga = findViewById(R.id.tv_nominal_total_riwayat_obat_selesai);
        waktu = findViewById(R.id.tv_waktu_pemesanan_riwayat_obat_selesai);
        waktuBayar = findViewById(R.id.tv_waktu_pembayaran_riwayat_obat_selesai);
        waktuKirim = findViewById(R.id.tv_waktu_pengiriman_riwayat_oabat_selesai);


        Intent intent = getIntent();
        NamaPenerima = intent.getStringExtra("nama");
        HandphonePenerima = intent.getStringExtra("handphone");
        AlamatPenerima = intent.getStringExtra("alamat");
        DetailAlamatPenerima = intent.getStringExtra("detail_alamat");
        Invoice = intent.getStringExtra("invoice");
        Status = intent.getIntExtra("status", 0);
        Harga = intent.getStringExtra("harga");
        TotalHarga = intent.getStringExtra("total_harga");
        Waktu = intent.getStringExtra("waktu");
        WaktuBayar = intent.getStringExtra("waktu_bayar");
        WaktuKirim = intent.getStringExtra("waktu_pengiriman");

        namaPenerima.setText(NamaPenerima);
        handphone.setText(HandphonePenerima);
        alamat.setText(AlamatPenerima);
        waktu.setText(Waktu);
        waktuBayar.setText(WaktuBayar);
        waktuKirim.setText(WaktuKirim);
        detailAlamat.setText(DetailAlamatPenerima);
        invoice.setText("#"+Invoice);
//        totalHarga.setText(TotalHarga+",-");
//        harga.setText(Harga + ",-");
        totalHarga.setText(Rupiah.formatUangId(getApplicationContext(), Double.parseDouble(String.valueOf(TotalHarga))));
        harga.setText(Rupiah.formatUangId(getApplicationContext(), Double.parseDouble(String.valueOf(Harga))));


        reqDetail();

        recyclerView = findViewById(R.id.recyclerViewRiwayatObatSelesai);
        // recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        recyclerView.setAdapter(adapterDetailTanpaResep);
    }

    public void reqDetail(){

        String urlDetail = "https://obats.000webhostapp.com/index.php/api/Detail?invoice=" + Invoice;

        StringRequest request = new StringRequest(Request.Method.GET, urlDetail,
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

}
