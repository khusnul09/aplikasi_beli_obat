package com.example.mynotes;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class DetailRiwayatObatBelumBayarActivity extends AppCompatActivity {

    TextView waktu, namaPenerima, handphone, alamat, detailAlamat, harga, totalHarga, status, invoice, waktuBayar, waktuKirim;
    String Invoice, Waktu, NamaPenerima, HandphonePenerima, AlamatPenerima, DetailAlamatPenerima, Harga, TotalHarga, Status, WaktuBayar, WaktuKirim;
    ImageView Kembali, EditDataPenerima;
    LinearLayout WaktuPembayaran, WaktuPengiriman;
    Button Bayar, PesananDiterima;

    AdapterDetailTanpaResep adapterDetailTanpaResep;
    List<ModelDetailTanpaResep> listObatTanpaResep;
    RecyclerView recyclerView;

    ProgressDialog progressDialog;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail__riwayat__obat__belum__bayar);

        EditDataPenerima = findViewById(R.id.iv_edit_data_penerima);
        EditDataPenerima.setOnClickListener(v -> { //data yg akan dibawa ke halaman edit data penerima obat
            Intent intentdata = new Intent(getApplicationContext(), EditDataPenerimaObatActivity.class);
            intentdata.putExtra("invoice", Invoice);
            intentdata.putExtra("handphone", HandphonePenerima);
            intentdata.putExtra("nama_penerima", NamaPenerima);
            intentdata.putExtra("alamat", AlamatPenerima);
            intentdata.putExtra("detail_alamat", DetailAlamatPenerima);
            startActivity(intentdata);
        });

        listObatTanpaResep = new ArrayList<>();
        adapterDetailTanpaResep = new AdapterDetailTanpaResep(getApplicationContext());

        Intent intent = getIntent();
        Invoice = intent.getStringExtra("invoice");
        Log.i("Khatima", Invoice);
        Waktu = intent.getStringExtra("waktu");
        NamaPenerima = intent.getStringExtra("nama");
        HandphonePenerima = intent.getStringExtra("handphone");
        AlamatPenerima = intent.getStringExtra("alamat");
        DetailAlamatPenerima = intent.getStringExtra("detail_alamat");
        Harga = intent.getStringExtra("harga");
        TotalHarga = intent.getStringExtra("total_harga");
        Status = intent.getStringExtra("status");
        WaktuBayar = intent.getStringExtra("waktu_bayar");
        WaktuKirim = intent.getStringExtra("waktu_pengiriman");


        waktu = findViewById(R.id.tv_waktu_pemesanan_obb);
        namaPenerima = findViewById(R.id.tv_nama_riwayat_obb);
        handphone = findViewById(R.id.tv_hp_riwayat_obb);
        alamat = findViewById(R.id.tv_alamat_riwayat_obb);
        detailAlamat = findViewById(R.id.tv_det_riwayat_obb);
        harga = findViewById(R.id.tv_nominal_tot_pesanan_blm);
        totalHarga = findViewById(R.id.tv_nominal_tot_harus_byr_blm);
        status = findViewById(R.id.tv_status_riwayat_obb);
        invoice = findViewById(R.id.tv_kode_obb);
        Bayar = findViewById(R.id.btn_bayar);
        PesananDiterima = findViewById(R.id.btn_pesanan_obat_diterima);
        waktuBayar = findViewById(R.id.tv_waktu_pembayaran_obb);
        WaktuPembayaran = findViewById(R.id.ll_waktu_pembayaran_obb);
        WaktuPengiriman = findViewById(R.id.ll_waktu_pengiriman_obb);
        waktuKirim = findViewById(R.id.tv_waktu_pengiriman_obb);


        waktu.setText(Waktu);
        waktuBayar.setText(WaktuBayar);
        waktuKirim.setText(WaktuKirim);
        namaPenerima.setText(NamaPenerima);
        handphone.setText(HandphonePenerima);
        alamat.setText(AlamatPenerima);
        detailAlamat.setText(DetailAlamatPenerima);
        harga.setText(Rupiah.formatUangId(getApplicationContext(), Double.parseDouble(Harga)));
        totalHarga.setText(Rupiah.formatUangId(getApplicationContext(), Double.parseDouble(TotalHarga)));
        invoice.setText("#"+Invoice);
        switch (Status) {
            case "1":
                status.setText("Menunggu Pembayaran");
                Bayar.setVisibility(View.VISIBLE);
                PesananDiterima.setVisibility(View.GONE);
                WaktuPembayaran.setVisibility(View.GONE);
                WaktuPengiriman.setVisibility(View.GONE);
                break;
            case "2":
                status.setText("Pesanan dikemas");
                Bayar.setVisibility(View.GONE);
                PesananDiterima.setVisibility(View.GONE);
                WaktuPengiriman.setVisibility(View.GONE);
                EditDataPenerima.setVisibility(View.GONE);
                break;
            case "3":
                status.setText("Pesanan dikirim");
                Bayar.setVisibility(View.GONE);
                EditDataPenerima.setVisibility(View.GONE);
                PesananDiterima.setVisibility(View.VISIBLE);
                EditDataPenerima.setVisibility(View.GONE);
                break;
            case "4":
                status.setText("Pesanan diterima");
                break;
        }

        reqDetail();

        recyclerView = findViewById(R.id.recyclerViewRiwayatObatBelumBayar);
       // recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        recyclerView.setAdapter(adapterDetailTanpaResep);

        Bayar.setOnClickListener(view -> {
            Intent intent1 = new Intent(DetailRiwayatObatBelumBayarActivity.this, BayarActivity.class);
            intent1.putExtra("invoice", Invoice);
            intent1.putExtra("total_harga", TotalHarga);
            startActivity(intent1);
        });

        PesananDiterima.setOnClickListener(v -> selesaikan2());

        Kembali = findViewById(R.id.iv_kembali_fragmant_belum_bayar);
        Kembali.setOnClickListener(v -> {
            Intent intent12 = new Intent(getApplicationContext(), RiwayatActivity.class); //kembali ke fragment resep
            intent12.putExtra("fragmentItem", 1);
            startActivity(intent12);
        });

        Log.i("DetailRiwayat: invoice", Invoice);
    }

    public void reqDetail(){
        String urlDetail = "https://obats.000webhostapp.com/index.php/api/Detail?invoice=" + Invoice;

        StringRequest request = new StringRequest(Request.Method.GET, urlDetail,
                response -> {
                    Log.i("DetailRiwayat: response", response);
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
                }, error -> Log.i("Khusnul", String.valueOf(error)));
        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        queue.add(request);
    }

    private void selesaikan2() {
        String urlSelesaikan = "https://obats.000webhostapp.com/index.php/api/Selesaikan";

        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Menyelesaikan Order...");
        progressDialog.show();

        String tokenAdmin = SharedPreferenceManager.getStringPreferences(getApplicationContext(), "tokenadmin");

        StringRequest request = new StringRequest(Request.Method.POST, urlSelesaikan,
                response -> {
                    Log.i("khatima", response);
                    try {
                        JSONObject objectResponse = new JSONObject(response);
                        if (objectResponse.getString("respon").equals("berhasil")) {
                            Intent intentSelesai = new Intent(getApplicationContext(), RiwayatActivity.class); //kembali ke fragment 1
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
                param.put("status", "4");
                param.put("token_tujuan", tokenAdmin);
                param.put("title", "Pesanan paket telah diterima");
                param.put("message", "Terima Kasih");
                return param;
            }
        };
        RequestQueue queue = Volley.newRequestQueue(Objects.requireNonNull(getApplicationContext()));
        queue.add(request);
    }
}
