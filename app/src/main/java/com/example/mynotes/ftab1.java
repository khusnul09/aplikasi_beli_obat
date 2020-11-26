package com.example.mynotes;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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

public class ftab1 extends Fragment {

    String url = "https://obats.000webhostapp.com/api/user/riwayatresep";
    AdapterRiwayatResep adapterRiwayarResep;
    private List<ModelRiwayatResep> listmodelresep;
    RecyclerView recyclerView;
    TextView kosong;
    String email;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_ftab1, container, false);

        email = SharedPreferenceManager.getStringPreferences(getContext(), "user_email");
        adapterRiwayarResep = new AdapterRiwayatResep(getContext());

        kosong = view.findViewById(R.id.teks_kosong);

        recyclerView = view.findViewById(R.id.recyclerViewResep);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
        recyclerView.setAdapter(adapterRiwayarResep);

        adapterRiwayarResep.setClickListener(data -> {
            Intent intent = new Intent(getContext(), DetailRiwayatResepActivity.class);
            intent.putExtra("nama", data.getNama_penerima());
            intent.putExtra("handphone",data.getHandphone());
            intent.putExtra("alamat",data.getAlamat());
            intent.putExtra("detail_alamat", data.getDetail_alamat());
            intent.putExtra("status", data.getStatus());
            intent.putExtra("invoice", data.getInvoice());
            intent.putExtra("gambar", data.getGambar_resep());
            intent.putExtra("total_harga", data.getHarga());
            startActivity(intent);
        });

        listmodelresep = new ArrayList<>();

        riwayatResepReq();

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    public void riwayatResepReq(){
        StringRequest request = new StringRequest(Request.Method.POST, url,
                response -> {
                    Log.i("khatima", response);
                    try {
                        Log.i("khatima", "try dijalankan");
                        JSONObject objectResponse = new JSONObject(response);
                        JSONArray array = objectResponse.getJSONArray("data");
                        if (objectResponse.getString("data").equals("kosong")) {
                            kosong.setVisibility(View.VISIBLE);
                            recyclerView.setVisibility(View.GONE);
                        } else {
                            kosong.setVisibility(View.GONE);
                            recyclerView.setVisibility(View.VISIBLE);

                            for (int i = 0; i < array.length(); i++) {
                                ModelRiwayatResep modelRiwayatResep = new ModelRiwayatResep();
                                modelRiwayatResep.setWaktu(array.getJSONObject(i).optString("waktu"));
                                modelRiwayatResep.setNama_penerima(array.getJSONObject(i).optString("nama_penerima"));
                                modelRiwayatResep.setHandphone(array.getJSONObject(i).optString("handphone"));
                                modelRiwayatResep.setAlamat(array.getJSONObject(i).optString("alamat"));
                                modelRiwayatResep.setInvoice(array.getJSONObject(i).optString("invoice"));
                                modelRiwayatResep.setDetail_alamat(array.getJSONObject(i).optString("detail_alamat"));
                                modelRiwayatResep.setStatus(array.getJSONObject(i).optInt("status"));
                                modelRiwayatResep.setGambar_resep(array.getJSONObject(i).optString("gambar_resep"));
                                modelRiwayatResep.setHarga(array.getJSONObject(i).optString("total_harga"));
                                Log.i("khatima", array.getJSONObject(i).optString("waktu"));
                                adapterRiwayarResep.add(modelRiwayatResep);
                            }
                            adapterRiwayarResep.addAll(listmodelresep);
                            adapterRiwayarResep.notifyDataSetChanged();
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