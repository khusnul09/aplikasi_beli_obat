package com.example.mynotes;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
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
import java.util.List;
import java.util.Objects;

public class PesananResepAdminActivity extends AppCompatActivity {

    AdapterPesananResepAdmin adapterPesananResepAdmin;
    List<ModelPesananResepAdmin> listmodelresepadmin;
    RecyclerView recyclerView;
    private SwipeRefreshLayout SwipeRefresh;
    TextView kosong;
    ImageView Kembali;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pesanan_resep_admin);

        Kembali = findViewById(R.id.iv_kembai_home_admin);
        Kembali.setOnClickListener(v -> {
            Intent intent = new Intent (getApplicationContext(), AdminActivity.class);
            startActivity(intent);

        });

        SwipeRefresh = findViewById(R.id.swipe_pesanan_resep); //refresh
        SwipeRefresh.setColorSchemeResources(R.color.colorAccent, R.color.colorPrimary);
        SwipeRefresh.setOnRefreshListener(() -> new Handler().postDelayed(() -> {
            SwipeRefresh.setRefreshing(false);

            dataPesananAdmin();
        }, 4000));


        Log.i("khatima", "ini halaman pesanan resep");

        adapterPesananResepAdmin = new AdapterPesananResepAdmin(getApplicationContext());

        kosong = findViewById(R.id.teks_kosong_resep);

        recyclerView = findViewById(R.id.recyclerViewPesananResepAdmin);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        recyclerView.setAdapter(adapterPesananResepAdmin);

        adapterPesananResepAdmin.setClickListener(data -> {
            Intent intent = new Intent(getApplicationContext(), DetailPesananResepActivity.class);
            intent.putExtra("nama", data.getNama_penerima());
            intent.putExtra("handphone",data.getHandphone());
            intent.putExtra("alamat",data.getAlamat());
            intent.putExtra("detail_alamat", data.getDetail_alamat());
            intent.putExtra("invoice", data.getInvoice());
            intent.putExtra("waktu", data.getWaktu());
            intent.putExtra("gambar", data.getGambar_resep());
            intent.putExtra("status", data.getStatus());
            intent.putExtra("total_harga", data.getHarga());
            intent.putExtra("waktu_bayar", data.getWaktu_bayar());
            intent.putExtra("bukti_bayar", data.getBukti_bayar());
            intent.putExtra("harga", data.getHargasementara());
            intent.putExtra("token", data.getToken());
            startActivity(intent);
        });

        listmodelresepadmin = new ArrayList<>();

        dataPesananAdmin();
    }

    private void dataPesananAdmin() {
        String urlpesanresepanadmin = "https://obats.000webhostapp.com/index.php/api/Data_pesanan_resep";

        StringRequest request = new StringRequest(Request.Method.GET, urlpesanresepanadmin,
                response -> {
                    Log.i("khatima", response);
                    try {
                        Log.i("khatima", "try dijalankan");
                        JSONObject objectResponse = new JSONObject(response);
                        JSONArray array = objectResponse.getJSONArray("data");
                        adapterPesananResepAdmin.clear(); //refresh
                        if (objectResponse.getString("data").equals("kosong")){
                            kosong.setVisibility(View.VISIBLE);
                            recyclerView.setVisibility(View.GONE);
                        }else {
                            kosong.setVisibility(View.GONE);
                            recyclerView.setVisibility(View.VISIBLE);

                            for (int i = 0; i < array.length(); i++) {
                                ModelPesananResepAdmin modelPesananResepAdmin = new ModelPesananResepAdmin();
                                modelPesananResepAdmin.setWaktu(array.getJSONObject(i).optString("waktu"));
                                modelPesananResepAdmin.setNama_penerima(array.getJSONObject(i).optString("nama_penerima"));
                                modelPesananResepAdmin.setHandphone(array.getJSONObject(i).optString("handphone"));
                                modelPesananResepAdmin.setAlamat(array.getJSONObject(i).optString("alamat"));
                                modelPesananResepAdmin.setInvoice(array.getJSONObject(i).optString("invoice"));
                                modelPesananResepAdmin.setDetail_alamat(array.getJSONObject(i).optString("detail_alamat"));
                                modelPesananResepAdmin.setStatus(array.getJSONObject(i).optInt("status"));
                                modelPesananResepAdmin.setGambar_resep(array.getJSONObject(i).optString("gambar_resep"));
                                modelPesananResepAdmin.setHarga(array.getJSONObject(i).optString("total_harga"));
                                modelPesananResepAdmin.setWaktu_bayar(array.getJSONObject(i).optString("waktu_pembayaran"));
                                modelPesananResepAdmin.setBukti_bayar(array.getJSONObject(i).optString("bukti_bayar"));
                                modelPesananResepAdmin.setHargasementara(array.getJSONObject(i).optString("harga"));
                                modelPesananResepAdmin.setToken(array.getJSONObject(i).optString("token"));
                                adapterPesananResepAdmin.add(modelPesananResepAdmin);
                            }
                            adapterPesananResepAdmin.addAll(listmodelresepadmin);
                            adapterPesananResepAdmin.notifyDataSetChanged();
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }, error -> Log.i("Khatima", String.valueOf(error)));
        RequestQueue queue = Volley.newRequestQueue(Objects.requireNonNull(getApplicationContext()));
        queue.add(request);
    }
}