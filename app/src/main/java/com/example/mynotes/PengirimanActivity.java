package com.example.mynotes;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class PengirimanActivity extends AppCompatActivity {

    private static final String urlSimpanPesananObat = "https://obats.000webhostapp.com//api/user/simpanpesantanparesep";

    Button buatPesanan;
    RecyclerView mRecyclerViewCart;
    PengirimanAdapter mAdapter;
    ImageView kembali;
    TextView subtotal, total;
    String Total, email_user;
    int subtotalInt, totalInt;
    EditText namaPenerima, handphone, alamat, detailAlamat;
    String strNamaPenerima, strHandphone, strAlamat, strDetailAlamat;

    String time, name = "", invoice, waktuKirim;

    Gson gson;

    ArrayList<ModelKeranjang> listObatToCart = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pengiriman);

        getApriori();

        email_user = SharedPreferenceManager.getStringPreferences(getApplicationContext(), "user_email");

        namaPenerima = findViewById(R.id.et_nama);
        handphone = findViewById(R.id.et_hp);
        alamat = findViewById(R.id.et_alamat);
        detailAlamat = findViewById(R.id.et_detail);

        buatPesanan = findViewById(R.id.btn_buat_pesanan);
        buatPesanan.setOnClickListener(v -> {
            /*Total = total.getText().toString();
            Intent intent = new Intent(PengirimanActivity.this, PembayaranActivity.class);
            intent.putExtra("Total",Total);
            startActivity(intent);*/

            simpanPesananObat();
        });

        kembali = findViewById(R.id.iv_kembali7);
        kembali.setOnClickListener(v -> onBackPressed());

        listObatToCart = getIntent().getParcelableArrayListExtra("CARTS");
        gson = new Gson();
        String list = gson.toJson(listObatToCart);
        Log.d("khatimaLIST", list);

        mAdapter = new PengirimanAdapter(this, listObatToCart);
        mRecyclerViewCart = findViewById(R.id.recyclerViewCart);
        mRecyclerViewCart.setAdapter(mAdapter);
        mRecyclerViewCart.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false)); // i will create in linearlayout
        // mRecyclerViewCart.setItemViewCacheSize(listObatToCart.size());

        subtotal = findViewById(R.id.tv_nominal_subtotal);
        total = findViewById(R.id.tv_nominal_total_pengiriman);

        subtotalInt = 0;
        for (ModelKeranjang model : listObatToCart) {
            subtotalInt += model.getTotalHarga();
        }

        totalInt = 0;
        totalInt += subtotalInt + (35000);

        subtotal.setText(Rupiah.formatUangId(getApplicationContext(), Double.parseDouble(String.valueOf(subtotalInt))));
        total.setText(Rupiah.formatUangId(getApplicationContext(), Double.parseDouble(String.valueOf(totalInt))));
    }

    private void simpanPesananObat() {
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Mengirim...");
        progressDialog.setCancelable(false);

        // Buat invoice
        Calendar c = Calendar.getInstance();
        @SuppressLint("SimpleDateFormat") SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
        time = dateFormat.format(c.getTime());
        for ( int i = 0; i < 5; i++) {
            char a = email_user.charAt(i);
            name += a;
        }
        invoice = "itr"+name+time;
        Log.i("khatima", invoice);

        @SuppressLint("SimpleDateFormat") SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        waktuKirim = format.format(c.getTime());

        String listnya = gson.toJson(listObatToCart);

        // Ambil nilai parameter
        strNamaPenerima = namaPenerima.getText().toString();
        strHandphone = handphone.getText().toString();
        strAlamat = alamat.getText().toString();
        strDetailAlamat = detailAlamat.getText().toString();

        String tokenAdmin = SharedPreferenceManager.getStringPreferences(getApplicationContext(), "tokenadmin");

        if (strNamaPenerima.isEmpty() || strHandphone.isEmpty() || strAlamat.isEmpty() || strDetailAlamat.isEmpty()) {
            Toast.makeText(this, "Form tidak boleh kosong", Toast.LENGTH_SHORT).show();
        } else {
            progressDialog.show();
            StringRequest stringRequest = new StringRequest(Request.Method.POST, urlSimpanPesananObat,
                    response -> {
                        Log.i("khatima", response);
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String respon = jsonObject.getString("status");
                            Log.i("Khatima", respon);
                            if (jsonObject.getString("status").equals("berhasil")) {
                                Total = String.valueOf(totalInt);
                                Intent intent = new Intent(PengirimanActivity.this, PembayaranActivity.class);
                                intent.putExtra("Total", Total);
                                intent.putExtra("invoice", jsonObject.getString("invoice"));
                                startActivity(intent);
                                finish();
                            }
                            Toast.makeText(getApplicationContext(), respon, Toast.LENGTH_LONG).show();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        progressDialog.dismiss();
                    }, error -> Toast.makeText(getApplicationContext(), "error: "+ error.toString(), Toast.LENGTH_LONG).show()) {
                @Override
                protected Map<String, String> getParams(){
                    Map<String, String> params = new HashMap<>();
                    params.put("invoice", invoice);
                    params.put("nama_user", email_user);
                    params.put("nama_penerima", strNamaPenerima);
                    params.put("handphone", strHandphone);
                    params.put("alamat",strAlamat);
                    params.put("detail_alamat", strDetailAlamat);
                    params.put("gambar_resep", "-");
                    params.put("harga", String.valueOf(subtotalInt));
                    params.put("total_harga", String.valueOf(totalInt));
                    params.put("jenis_pesan", "2");
                    params.put("status", "1");
                    params.put("bukti_bayar", "-");
                    params.put("nama_rek", "-");
                    params.put("no_rek", "-");
                    params.put("waktu", waktuKirim);
                    params.put("list_obat", listnya);
                    params.put("token_tujuan", tokenAdmin);
                    params.put("title", "Pesanan Tanpa Resep");
                    params.put("message", "Ada pesanan obat...");
                    return params;
                }
            };
            RequestQueue requestQueue = Volley.newRequestQueue(PengirimanActivity.this);
            requestQueue.add(stringRequest);
        }
    }

    public void getApriori() {

        String url = "https://obats.000webhostapp.com//api/user/tokenxadmin";

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        @SuppressLint("CheckResult") JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                response -> {
            Log.d("khatima", response.optString("token"));
                }, error -> {
            error.printStackTrace();
            Toast.makeText(this, "Terjadi masalah.", Toast.LENGTH_SHORT).show();
        });
        requestQueue.add(jsonObjectRequest);
    }
}