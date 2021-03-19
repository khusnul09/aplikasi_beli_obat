package com.example.mynotes;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

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
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class KeranjangActivity extends AppCompatActivity {
    SwipeRefreshLayout mSwipeLayout;
    RecyclerView mRecyclerView;
    TextView tambahProduk;
    Button lanjutBayar;
    ImageView kembali;
    TextView totalTV;
    LinearLayout  keranjangKosong, boxButtonLanjut;

    ArrayList<ModelKeranjang> listCart = new ArrayList<>();
    AdapterKeranjang adapterKeranjang;

    int totalInt = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_keranjang);

        String user = SharedPreferenceManager.getStringPreferences(getApplicationContext(), "user_email");

        keranjangKosong = findViewById(R.id.ll_keranjang_kosong);
        boxButtonLanjut = findViewById(R.id.bottombt2);
        lanjutBayar = findViewById(R.id.btn_lanjut_bayar);
        kembali = findViewById(R.id.iv_kembali6);
        tambahProduk = findViewById(R.id.tv_tambah_produk_lain);
        mRecyclerView = findViewById(R.id.recyclerView);
        totalTV = findViewById(R.id.tv_nominal_total);
        mSwipeLayout = findViewById(R.id.swipeLayout);

        lanjutBayar.setOnClickListener(v -> {
            Intent intent = new Intent(getApplicationContext(), PengirimanActivity.class);
            intent.putParcelableArrayListExtra("CARTS", listCart);
//                intent.putExtra("CART", listObatToCart);
            // Intent intentLanjut = intent.putExtra("CARTS", listCart);
            startActivity(intent);
        });

        kembali.setOnClickListener(v -> {
            Intent intent = new Intent(this, PesanTanpaResepActivity.class);
            startActivity(intent);
            finish();
        });

        tambahProduk.setOnClickListener(v -> {
            Intent intent = new Intent(this, PesanTanpaResepActivity.class);
            startActivity(intent);
            // onBackPressed(); //kembali ke pilih obat
        });

        /*listObatToCart = (ArrayList<Model>) getIntent().getSerializableExtra("CART");
        Log.d("khatima", listObatToCart.toString());
        mRecyclerView = findViewById(R.id.recyclerView);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mRecyclerView.getContext(), RecyclerView.VERTICAL, false)); // i will create in linearlayout
        myAdapter = new MyCartAdapter(this, listObatToCart, false, this);
        mRecyclerView.setAdapter(myAdapter);
        mRecyclerView.setItemViewCacheSize(listObatToCart.size());*/

        adapterKeranjang = new AdapterKeranjang(this, listCart);

        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        mRecyclerView.setAdapter(adapterKeranjang);
        adapterKeranjang.setClickListener((data, position) -> {
            Log.d("khatima", "dihapus");
            deleteKeranjang(user, data, position);
            // finish();
            // startActivity(getIntent());
        });

        mSwipeLayout.setOnRefreshListener(() -> {
            mRecyclerView.setVisibility(View.GONE);
            totalInt = 0;
            listCart.clear();
            getKeranjang(user);
        });

        mSwipeLayout.setRefreshing(true);
        getKeranjang(user);

    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, PesanTanpaResepActivity.class);
        startActivity(intent);
        finish();
    }

    public void getKeranjang(String user) {
        String url = "https://obats.000webhostapp.com/index.php/api/Get_keranjang?user=" + user;

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                response -> {
                    mSwipeLayout.setRefreshing(false);
                    try {
                        JSONArray jsonArray = response.getJSONArray("data");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            Log.d("khatima", jsonArray.toString());
                            ModelKeranjang model = new ModelKeranjang();
                            model.setNama_obat(jsonArray.getJSONObject(i).optString("nama_obat"));
                            model.setHarga(jsonArray.getJSONObject(i).optString("harga_jual"));
                            model.setQuantity(jsonArray.getJSONObject(i).optString("qty"));
                            model.setGambar(jsonArray.getJSONObject(i).optString("gambar"));
                            model.setId_obat(jsonArray.getJSONObject(i).optString("id_obat"));
                            totalInt += model.getTotalHarga();
                            listCart.add(model);
                        }
                        totalTV.setText(Rupiah.formatUangId(this, Double.parseDouble(String.valueOf(totalInt))));
                        adapterKeranjang.notifyDataSetChanged();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    if (totalInt == 0) {
                        keranjangKosong.setVisibility(View.VISIBLE);
                        mRecyclerView.setVisibility(View.GONE);
                        boxButtonLanjut.setVisibility(View.GONE);
                    } else {
                        keranjangKosong.setVisibility(View.GONE);
                        mRecyclerView.setVisibility(View.VISIBLE);
                        boxButtonLanjut.setVisibility(View.VISIBLE);
                    }
                }, error -> {
                    mSwipeLayout.setRefreshing(false);
                    mRecyclerView.setVisibility(View.VISIBLE);
                    error.printStackTrace();
                    Toast.makeText(this, "Terjadi masalah.", Toast.LENGTH_SHORT).show();
                }
        );
        requestQueue.add(jsonObjectRequest);

    }

    public void deleteKeranjang(String user, ModelKeranjang model, int position) {
        String urlDelete = "https://obats.000webhostapp.com/index.php/api/Delete_keranjang";
        Log.d("khatima", urlDelete);
        ProgressDialog progress = new ProgressDialog(this);
        progress.setCancelable(false);
        progress.setMessage("Sedang menghapus..");
        progress.show();

        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        StringRequest delete = new StringRequest(Request.Method.POST, urlDelete, response -> {
            progress.dismiss();
            listCart.remove(position);
            adapterKeranjang.notifyItemRemoved(position);
            totalInt -= model.getTotalHarga();
            totalTV.setText(Rupiah.formatUangId(this, Double.parseDouble(String.valueOf(totalInt))));

            if (totalInt == 0) {
                keranjangKosong.setVisibility(View.VISIBLE);
                mRecyclerView.setVisibility(View.GONE);
                boxButtonLanjut.setVisibility(View.GONE);
            } else {
                keranjangKosong.setVisibility(View.GONE);
                mRecyclerView.setVisibility(View.VISIBLE);
                boxButtonLanjut.setVisibility(View.VISIBLE);
            }

            Log.d("khatima", response);
        }, error -> {
            progress.dismiss();
            error.printStackTrace();
        }) {
            @Override
            protected Map<String, String> getParams(){
                Map<String, String> params = new HashMap<>();
                params.put("id_user", user);
                params.put("id_obat", model.getId_obat());
                return  params;
            }
        };

        queue.add(delete);
    }
}