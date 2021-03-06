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

public class PesananObatAdminActivity extends AppCompatActivity {

    ImageView Kembali;
    AdapterPesananObatAdmin adapterPesananObatAdmin;
    List<ModelPesananObatAdmin> listmodelobatadmin;
    private SwipeRefreshLayout SwipeRefresh;
    RecyclerView recyclerView;
    TextView kosong;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pesanan_obat_admin);

        Kembali = findViewById(R.id.iv_back_home_admin);
        Kembali.setOnClickListener(v -> {
            Intent intent = new Intent (getApplicationContext(), AdminActivity.class);
            startActivity(intent);
        });

        SwipeRefresh = findViewById(R.id.swipe_pesanan_obat); //refresh
        SwipeRefresh.setColorSchemeResources(R.color.colorAccent, R.color.colorPrimary);
        SwipeRefresh.setOnRefreshListener(() -> new Handler().postDelayed(() -> {
            SwipeRefresh.setRefreshing(false);

            dataPesananAdmin();
        }, 4000));


        adapterPesananObatAdmin = new AdapterPesananObatAdmin(getApplicationContext());

        kosong = findViewById(R.id.teks_kosong_obat);

        recyclerView = findViewById(R.id.recyclerViewPesananObatAdmin);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        recyclerView.setAdapter(adapterPesananObatAdmin);

        adapterPesananObatAdmin.setClickListener(data -> {
            Intent intent = new Intent(getApplicationContext(), DetailPesananObatActivity.class);
            intent.putExtra("nama", data.getNama_penerima());
            intent.putExtra("handphone",data.getHandphone());
            intent.putExtra("alamat",data.getAlamat());
            intent.putExtra("detail_alamat", data.getDetail_alamat());
            intent.putExtra("invoice", data.getInvoice());
            intent.putExtra("waktu", data.getWaktu());
            intent.putExtra("status", data.getStatus());
            intent.putExtra("gambar", data.getGambar_resep());
            intent.putExtra("total_harga", data.getHarga());
            intent.putExtra("waktu_bayar", data.getWaktu_bayar());
            intent.putExtra("bukti_bayar", data.getBukti_bayar());
            intent.putExtra("token", data.getToken());
            startActivity(intent);
        });

        listmodelobatadmin = new ArrayList<>();

        dataPesananAdmin();
    }

    private void dataPesananAdmin() {
        String urlpesananoabtadmin = "https://obats.000webhostapp.com/index.php/api/Data_pesanan_obat";

        StringRequest request = new StringRequest(Request.Method.GET, urlpesananoabtadmin,
                response -> {
                    Log.i("khatima", response);
                    try {
                        Log.i("khatima", "try dijalankan");
                        JSONObject objectResponse = new JSONObject(response);
                        JSONArray array = objectResponse.getJSONArray("data");
                        adapterPesananObatAdmin.clear(); //refresh
                        if (objectResponse.getString("data").equals("kosong")){
                            kosong.setVisibility(View.VISIBLE);
                            recyclerView.setVisibility(View.GONE);
                        }else {
                            kosong.setVisibility(View.GONE);
                            recyclerView.setVisibility(View.VISIBLE);

                            for (int i = 0; i < array.length(); i++) {
                                ModelPesananObatAdmin modelPesananObatAdmin = new ModelPesananObatAdmin();
                                modelPesananObatAdmin.setWaktu(array.getJSONObject(i).optString("waktu"));
                                modelPesananObatAdmin.setNama_penerima(array.getJSONObject(i).optString("nama_penerima"));
                                modelPesananObatAdmin.setHandphone(array.getJSONObject(i).optString("handphone"));
                                modelPesananObatAdmin.setAlamat(array.getJSONObject(i).optString("alamat"));
                                modelPesananObatAdmin.setInvoice(array.getJSONObject(i).optString("invoice"));
                                modelPesananObatAdmin.setDetail_alamat(array.getJSONObject(i).optString("detail_alamat"));
                                modelPesananObatAdmin.setHarga(array.getJSONObject(i).optString("total_harga"));
                                modelPesananObatAdmin.setStatus(array.getJSONObject(i).optString("status"));
                                modelPesananObatAdmin.setWaktu_bayar(array.getJSONObject(i).optString("waktu_pembayaran"));
                                modelPesananObatAdmin.setBukti_bayar(array.getJSONObject(i).optString("bukti_bayar"));
                                modelPesananObatAdmin.setToken(array.getJSONObject(i).optString("token"));

                                adapterPesananObatAdmin.add(modelPesananObatAdmin);
                            }
                            adapterPesananObatAdmin.addAll(listmodelobatadmin);
                            adapterPesananObatAdmin.notifyDataSetChanged();
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }, error -> Log.i("Khatima", String.valueOf(error)));
        RequestQueue queue = Volley.newRequestQueue(Objects.requireNonNull(getApplicationContext()));
        queue.add(request);
    }
}