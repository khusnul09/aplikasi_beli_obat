package com.example.mynotes;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
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
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class PengirimanActivity extends AppCompatActivity implements MyAdapter.ICart{

    private static final String urlSimpanPesananObat = "https://obats.000webhostapp.com//api/user/simpanpesantanparesep";

    Button buatPesanan;
    RecyclerView mRecyclerViewCart;
    PengirimanAdapter myAdapter;
    ImageView kembali;
    TextView subtotal, total;
    String Total, email_user;
    int subtotalInt, totalInt;
    EditText namaPenerima, handphone, alamat, detailAlamat;
    String strNamaPenerima, strHandphone, strAlamat, strDetailAlamat;

    String time, name="", invoice;

    Gson gson;

    ArrayList<Model> listObatToCart = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pengiriman);

        email_user = SharedPreferenceManager.getStringPreferences(getApplicationContext(), "user_email");

        namaPenerima = findViewById(R.id.et_nama);
        handphone = findViewById(R.id.et_hp);
        alamat = findViewById(R.id.et_alamat);
        detailAlamat = findViewById(R.id.et_detail);

        buatPesanan = findViewById(R.id.btn_buat_pesanan);
        buatPesanan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*Total = total.getText().toString();
                Intent intent = new Intent(PengirimanActivity.this, PembayaranActivity.class);
                intent.putExtra("Total",Total);
                startActivity(intent);*/

                simpanPesananObat();
            }
        });

//        kembali = findViewById(R.id.iv_kembali7);
//        kembali.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(getApplicationContext(), KeranjangActivity.class);
//                startActivity(intent);
//            }
//        });

        listObatToCart = (ArrayList<Model>) getIntent().getSerializableExtra("CART");
        gson = new Gson();

        mRecyclerViewCart = findViewById(R.id.recyclerViewCart);
        mRecyclerViewCart.setLayoutManager(new LinearLayoutManager(mRecyclerViewCart.getContext(), RecyclerView.VERTICAL, false)); // i will create in linearlayout
        myAdapter = new PengirimanAdapter(this, listObatToCart, false, this::onItemSelected);
        mRecyclerViewCart.setAdapter(myAdapter);
        //mRecyclerViewCart.setItemViewCacheSize(listObatToCart.size());

        subtotal = findViewById(R.id.tv_nominal_subtotal);
        total = findViewById(R.id.tv_nominal_total_pengiriman);

        subtotalInt = 0;
        for (Model model : listObatToCart) {
            subtotalInt+=(model.getHargaJual()*model.getQuantity());

        }
        totalInt = 0;
            totalInt+=subtotalInt+(30000);

        subtotal.setText(subtotalInt+ ",-");
        total.setText(totalInt+",-");

    }

    @Override
    public void onItemSelected(String title, Integer quantity) {

    }

    private void simpanPesananObat() {
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Mengirim...");
        progressDialog.show();

        //buat invoice
        Calendar c = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
        time = dateFormat.format(c.getTime());
        for ( int i=0; i < 5; i++) {
            char a = email_user.charAt(i);
            name += a;
        }
        invoice = "itr"+name+time;
        Log.i("khatima", invoice);

        String listnya = gson.toJson(listObatToCart);

        //ambil nilai parameter
        strNamaPenerima = namaPenerima.getText().toString();
        strHandphone = handphone.getText().toString();
        strAlamat = alamat.getText().toString();
        strDetailAlamat = detailAlamat.getText().toString();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, urlSimpanPesananObat,
                (Response.Listener<String>) response -> {
                    Log.i("khatima", response);
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        String respon = jsonObject.getString("status");
                        Log.i("Khatima", respon);
                        if (jsonObject.getString("status").equals("berhasil")) {
                            Total = total.getText().toString();
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
                }, (Response.ErrorListener) error -> Toast.makeText(getApplicationContext(), "error: "+ error.toString(), Toast.LENGTH_LONG).show()) {
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
                params.put("list_obat", listnya);
                return  params;

            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(PengirimanActivity.this);
        requestQueue.add(stringRequest);
    }
}