package com.example.mynotes;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

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

    String urlpesanresepanadmin = "https://obats.000webhostapp.com/api/datapesananresep";

    AdapterPesananResepAdmin adapterPesananResepAdmin;
    List<ModelPesananResepAdmin> listmodelresepadmin;
    RecyclerView recyclerView;
    ImageView Kembali;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pesanan_resep_admin);

        Kembali = findViewById(R.id.iv_kembai_home_admin);
        Kembali.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent (getApplicationContext(), AdminActivity.class);
                startActivity(intent);

            }
        });
        Log.i("khatima", "ini halaman pesanan resep");

        adapterPesananResepAdmin = new AdapterPesananResepAdmin(getApplicationContext());

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
            startActivity(intent);
        });

        listmodelresepadmin = new ArrayList<>();

        dataPesananAdmin();
    }

    private void dataPesananAdmin() {
        StringRequest request = new StringRequest(Request.Method.GET, urlpesanresepanadmin,
                response -> {
                    Log.i("khatima", response);
                    try {
                        Log.i("khatima", "try dijalankan");
                        JSONObject objectResponse = new JSONObject(response);
                        JSONArray array = objectResponse.getJSONArray("data");

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
                            adapterPesananResepAdmin.add(modelPesananResepAdmin);
                        }
                        adapterPesananResepAdmin.addAll(listmodelresepadmin);
                        adapterPesananResepAdmin.notifyDataSetChanged();

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }, error -> Log.i("Khatima", String.valueOf(error)));
        RequestQueue queue = Volley.newRequestQueue(Objects.requireNonNull(getApplicationContext()));
        queue.add(request);
    }
}