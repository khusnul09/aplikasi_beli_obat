package com.example.mynotes;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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
import java.util.List;
import java.util.Objects;

public class ftab2 extends Fragment {

    String email;
    TextView kosong;
    AdapterRiwayatObat adapterRiwayatObat;
    private SwipeRefreshLayout SwipeRefresh;
    private List<ModelRiwayatObat> listmodelobat;
    RecyclerView recyclerView;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_ftab2, container, false);

        email = SharedPreferenceManager.getStringPreferences(getContext(), "user_email");
        adapterRiwayatObat = new AdapterRiwayatObat(getContext());

        SwipeRefresh = view.findViewById(R.id.swipe_obat);
        SwipeRefresh.setColorSchemeResources(R.color.colorAccent, R.color.colorPrimary);
        SwipeRefresh.setOnRefreshListener(() -> new Handler().postDelayed(() -> {
            SwipeRefresh.setRefreshing(false);

            riwayatObatReq();
        }, 4000));

        kosong = view.findViewById(R.id.teks_kosong);

        recyclerView = view.findViewById(R.id.recyclerViewRiwayatBelumBayarObat);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
        recyclerView.setAdapter(adapterRiwayatObat);

        adapterRiwayatObat.setClickListener(data -> {
            Log.i("Khatima", "CardView di klik");
            Intent intent = new Intent(getContext(), DetailRiwayatObatBelumBayarActivity.class);
            intent.putExtra("invoice", data.getInvoice());
            intent.putExtra("waktu", data.getWaktu());
            intent.putExtra("nama", data.getNama_penerima());
            intent.putExtra("handphone", data.getHandphone());
            intent.putExtra("alamat", data.getAlamat());
            intent.putExtra("detail_alamat", data.getDetail_alamat());
            intent.putExtra("total_harga", data.getTotal_harga());
            intent.putExtra("harga", data.getHarga()); //total pesanan
            intent.putExtra("status", data.getStatus());
            intent.putExtra("waktu_bayar", data.getWaktu_bayar());
            intent.putExtra("waktu_pengiriman", data.getWaktu_kirim());
            startActivity(intent);
        });

        listmodelobat = new ArrayList<>();

        riwayatObatReq();

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated( view, savedInstanceState);
    }

    public void riwayatObatReq(){
        String url = "https://obats.000webhostapp.com/index.php/api/Riwayat_tanpa_resep?email=" + email;

        StringRequest request = new StringRequest(Request.Method.GET, url,
                response -> {
                    Log.i("khatima", response);
                    try {
                        Log.i("khatima", "try dijalankan");
                        JSONObject objectResponse = new JSONObject(response);
                        JSONArray array = objectResponse.getJSONArray("data");
                        adapterRiwayatObat.clear();
                        if (objectResponse.getString("data").equals("kosong")) {
                            kosong.setVisibility(View.VISIBLE);
                            recyclerView.setVisibility(View.GONE);
                        } else {
                            kosong.setVisibility(View.GONE);
                            recyclerView.setVisibility(View.VISIBLE);

                            for (int i = 0; i < array.length(); i++) {
                                ModelRiwayatObat modelRiwayatObat = new ModelRiwayatObat();
                                modelRiwayatObat.setWaktu(array.getJSONObject(i).optString("waktu"));
                                modelRiwayatObat.setNama_penerima(array.getJSONObject(i).optString("nama_penerima"));
                                modelRiwayatObat.setHandphone(array.getJSONObject(i).optString("handphone"));
                                modelRiwayatObat.setAlamat(array.getJSONObject(i).optString("alamat"));
                                modelRiwayatObat.setDetail_alamat(array.getJSONObject(i).optString("detail_alamat"));
                                modelRiwayatObat.setHarga(array.getJSONObject(i).optString("harga"));
                                modelRiwayatObat.setInvoice(array.getJSONObject(i).optString("invoice"));
                                modelRiwayatObat.setTotal_harga(array.getJSONObject(i).optString("total_harga"));
                                modelRiwayatObat.setJenis_pesan(array.getJSONObject(i).optString("jenis_pesan"));
                                modelRiwayatObat.setStatus(array.getJSONObject(i).optString("status"));
                                modelRiwayatObat.setNama_user(array.getJSONObject(i).optString("nama_user"));
                                modelRiwayatObat.setWaktu_bayar(array.getJSONObject(i).optString("waktu_pembayaran"));
                                modelRiwayatObat.setWaktu_kirim(array.getJSONObject(i).optString("waktu_pengiriman"));
                                Log.i("khatima", array.getJSONObject(i).optString("waktu"));
                                adapterRiwayatObat.add(modelRiwayatObat);
                            }
                            adapterRiwayatObat.addAll(listmodelobat);
                            adapterRiwayatObat.notifyDataSetChanged();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }, error -> Log.i("Khusnul", String.valueOf(error)));
        RequestQueue queue = Volley.newRequestQueue(Objects.requireNonNull(getContext()));
        queue.add(request);
    }
}