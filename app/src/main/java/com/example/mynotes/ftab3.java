package com.example.mynotes;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import java.util.Objects;

public class ftab3 extends Fragment {

    String urlRiwayatSelesai = "https://obats.000webhostapp.com/api/user/riwayatselesai";

    TextView kosong;
    String email;
    AdapterRiwayatSelesai adapterRiwayatSelesai;
    private SwipeRefreshLayout SwipeRefresh;
    private List<ModelRiwayatSelesai> modelRiwayatObatList;
    RecyclerView recyclerView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_ftab3, container, false);

        email = SharedPreferenceManager.getStringPreferences(getContext(), "user_email");
        adapterRiwayatSelesai = new AdapterRiwayatSelesai(getContext());

        SwipeRefresh = view.findViewById(R.id.swipe_selesai);
        SwipeRefresh.setColorSchemeResources(R.color.colorAccent, R.color.colorPrimary);
        SwipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        SwipeRefresh.setRefreshing(false);

                        riwayatSelesai();
                    }
                }, 4000);
            }
        });

        kosong = view.findViewById(R.id.teks_kosong);

        recyclerView = view.findViewById(R.id.recyclerViewSelesai);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
        recyclerView.setAdapter(adapterRiwayatSelesai);

        adapterRiwayatSelesai.setClickListener(data -> {
            Log.i("Khatima", "CardView di klik");
            switch (data.getJenis()) {
                case "1":
                    Intent intent = new Intent(getContext(), DetailRiwayatResepSelesaiActivity.class);
                    intent.putExtra("invoice", data.getInvoice());
                    intent.putExtra("waktu", data.getWaktu());
                    intent.putExtra("nama", data.getNama_penerima());
                    intent.putExtra("handphone", data.getHandphone());
                    intent.putExtra("alamat", data.getAlamat());
                    intent.putExtra("detail_alamat", data.getDetail_alamat());
                    intent.putExtra("total_harga", data.getTotalHarga());
                    intent.putExtra("harga", data.getHarga()); //total pesanan
                    intent.putExtra("status", data.getStatus());
                    intent.putExtra("gambar", data.getGambar_resep());
                    intent.putExtra("waktu_bayar", data.getWaktu_bayar());
                    intent.putExtra("waktu_pengiriman", data.getWaktu_kirim());
                    startActivity(intent);
//                    Toast.makeText(getContext(), "ke Halaman Detail Resep", Toast.LENGTH_SHORT).show();
                    break;
                case "2":
                    Intent intentObat = new Intent(getContext(), DetailRiwayatObatSelesaiActivity.class);
                    intentObat.putExtra("invoice", data.getInvoice());
                    intentObat.putExtra("waktu", data.getWaktu());
                    intentObat.putExtra("nama", data.getNama_penerima());
                    intentObat.putExtra("handphone", data.getHandphone());
                    intentObat.putExtra("alamat", data.getAlamat());
                    intentObat.putExtra("detail_alamat", data.getDetail_alamat());
                    intentObat.putExtra("total_harga", data.getTotalHarga());
                    intentObat.putExtra("harga", data.getHarga());
                    intentObat.putExtra("status", data.getStatus());
                    intentObat.putExtra("waktu_bayar", data.getWaktu_bayar());
                    intentObat.putExtra("waktu_pengiriman", data.getWaktu_kirim());
                    startActivity(intentObat);
//                    Toast.makeText(getContext(), "ke Halaman Detail Obat", Toast.LENGTH_SHORT).show();
                    break;
            }
        });

        modelRiwayatObatList = new ArrayList<>();

        riwayatSelesai();

        return view;
    }

    public void riwayatSelesai(){
        StringRequest request = new StringRequest(Request.Method.POST, urlRiwayatSelesai,
                response -> {
                    Log.i("khatima", response);
                    try {
                        Log.i("khatima", "try dijalankan");
                        JSONObject objectResponse = new JSONObject(response);
                        JSONArray array = objectResponse.getJSONArray("data");
                        adapterRiwayatSelesai.clear();
                        if (objectResponse.getString("data").equals("kosong")) {
                            kosong.setVisibility(View.VISIBLE);
                            recyclerView.setVisibility(View.GONE);
                        } else {
                            kosong.setVisibility(View.GONE);
                            recyclerView.setVisibility(View.VISIBLE);

                            for (int i = 0; i < array.length(); i++) {
                                ModelRiwayatSelesai modelRiwayatSelesai = new ModelRiwayatSelesai();
                                modelRiwayatSelesai.setWaktu(array.getJSONObject(i).optString("waktu"));
                                modelRiwayatSelesai.setNama_penerima(array.getJSONObject(i).optString("nama_penerima"));
                                modelRiwayatSelesai.setHandphone(array.getJSONObject(i).optString("handphone"));
                                modelRiwayatSelesai.setAlamat(array.getJSONObject(i).optString("alamat"));
                                modelRiwayatSelesai.setDetail_alamat(array.getJSONObject(i).optString("detail_alamat"));
                                modelRiwayatSelesai.setHarga(array.getJSONObject(i).optString("harga"));
                                modelRiwayatSelesai.setInvoice(array.getJSONObject(i).optString("invoice"));
                                modelRiwayatSelesai.setTotalHarga(array.getJSONObject(i).optString("total_harga"));
                                modelRiwayatSelesai.setJenis(array.getJSONObject(i).optString("jenis_pesan"));
                                modelRiwayatSelesai.setStatus(array.getJSONObject(i).optString("status"));
                                modelRiwayatSelesai.setNama_user(array.getJSONObject(i).optString("nama_user"));
                                modelRiwayatSelesai.setGambar_resep(array.getJSONObject(i).optString("gambar_resep"));
                                modelRiwayatSelesai.setWaktu_bayar(array.getJSONObject(i).optString("waktu_pembayaran"));
                                modelRiwayatSelesai.setWaktu_kirim(array.getJSONObject(i).optString("waktu_pengiriman"));
                                adapterRiwayatSelesai.add(modelRiwayatSelesai);
                            }
                            adapterRiwayatSelesai.addAll(modelRiwayatObatList);
                            adapterRiwayatSelesai.notifyDataSetChanged();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }, error -> Log.i("Khusnul", String.valueOf(error))) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> param = new HashMap<>();
                param.put("email", email);
                return param;
            }
        };
        RequestQueue queue = Volley.newRequestQueue(Objects.requireNonNull(getContext()));
        queue.add(request);
    }
}