package com.example.mynotes;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;

import org.json.JSONArray;
import org.json.JSONException;

public class AnotherActivity extends AppCompatActivity {

    TextView mTitleTv, mDescTv, sSatuanTv, tvTitleApriori, tvDescApriori, tvSatuanApriori;
    ImageView mImageIv, ivApriori;

    RelativeLayout bawah, atas, dalamnyaBawah, dalamnyaAtas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_another);

        ActionBar actionBar = getSupportActionBar();

        actionBar.setDisplayHomeAsUpEnabled(true);

        mTitleTv = findViewById(R.id.title);
        mDescTv = findViewById(R.id.description);
        mImageIv = findViewById(R.id.imageView);
        sSatuanTv = findViewById(R.id.tv_satuan_obat);

        ivApriori = findViewById(R.id.iv_apriori);
        tvTitleApriori = findViewById(R.id.tv_title_apriori);
        tvDescApriori = findViewById(R.id.tv_harga_apriori);
        tvSatuanApriori = findViewById(R.id.tv_satuan_apriori);

        bawah = findViewById(R.id.layoutBawah);
        atas = findViewById(R.id.layoutAtas);
        dalamnyaBawah = findViewById(R.id.dalamnyaLayoutBawah);
        dalamnyaAtas = findViewById(R.id.dalamnyaLayoutAtas);

        //now get our data from intent in which we put our data

        Intent intent = getIntent();

        String mTitle = intent.getStringExtra("iTitle");
        String mDescription = intent.getStringExtra("iDesc");
        String sSatuan = intent.getStringExtra("satuan");
        String gambar = intent.getStringExtra("gambar");
        Integer idApriori = intent.getIntExtra("id", 0);

        Log.d("khatima", idApriori.toString());

        //byte[] mBytes = getIntent().getByteArrayExtra("iImage");
        //now decode image because from previous activity we get our image in bytes
        //Bitmap bitmap = BitmapFactory.decodeByteArray(mBytes, 0, mBytes.length);

        actionBar.setTitle(mTitle); //which title we get from previous activity that will set in our action bar

        //now set our data in our view, which we get in our previous activity
        mTitleTv.setText(mTitle);
//        mDescTv.setText(mDescription);
        sSatuanTv.setText(sSatuan);
        mDescTv.setText(Rupiah.formatUangId(getApplicationContext(), Double.parseDouble(String.valueOf(mDescription))));

        try {
            //mImageIv.setImageBitmap(bitmap);
            Glide.with(this).load(gambar).into(mImageIv);
        } catch (Exception e) {
            mImageIv.setImageResource(R.drawable.ic_drugs);
        }

        getApriori(idApriori);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void getApriori(Integer idObat) {
        int colorWhite = Color.parseColor("#F2F2F2");
        int colorRed = Color.parseColor("#D9303E");
        if (idObat == 0) {
            dalamnyaAtas.setBackgroundColor(colorWhite);
            dalamnyaBawah.setVisibility(View.INVISIBLE);
        } else {
            dalamnyaAtas.setBackgroundColor(colorRed);
            dalamnyaBawah.setVisibility(View.VISIBLE);
        }

        String url = "https://obats.000webhostapp.com//api/user/apriori/" + idObat;

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        @SuppressLint("CheckResult") JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                response -> {
                    try {
                        JSONArray jsonArray = response.getJSONArray("data");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            Glide.with(this).load(jsonArray.getJSONObject(i).optString("gambar")).into(ivApriori);
                            tvTitleApriori.setText(jsonArray.getJSONObject(i).optString("nama_obat"));
                            tvDescApriori.setText(Rupiah.formatUangId(this, Double.parseDouble(jsonArray.getJSONObject(i).optString("harga_jual"))));
                            tvSatuanApriori.setText(jsonArray.getJSONObject(i).optString("satuan"));

                            Log.d("khatima", jsonArray.getJSONObject(i).optString("gambar"));
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }, error -> {
            error.printStackTrace();
            Toast.makeText(this, "Terjadi masalah.", Toast.LENGTH_SHORT).show();
        });
        requestQueue.add(jsonObjectRequest);
    }
}
