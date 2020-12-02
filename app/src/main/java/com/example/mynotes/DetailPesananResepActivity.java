package com.example.mynotes;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class DetailPesananResepActivity extends AppCompatActivity {

    //NO

    String urlAntarPesanan = "https://obats.000webhostapp.com/api/user/antarpesananresep";
    String urlKrmHarga = "https://obats.000webhostapp.com/api/user/edittotalbayarresep";

    TextView namaPenerima, handphone, alamat, detailAlamat, status, invoice, waktu, waktuBayar, totalBayar;
    String NamaPenerima, HandphonePenerima, AlamatPenerima, DetailAlamatPenerima, statusdesc, Invoice,
            Gambar,BuktiBayar, Harga, Waktu, WaktuBayar, WaktuKirim, totalHarga, strHarga;
    int Status;
    LinearLayout TextBuktiPembayaran, BuktiPembayaran, TotalHarusBayar;
    Button Kirim, Antar;
    View view;
    ImageView img, buktiBayar, Kembali;
    EditText etharga;

    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_pesanan_resep);

        namaPenerima = findViewById(R.id.tv_nama_penerima_resep);
        handphone = findViewById(R.id.tv_hp_penerima_resep);
        alamat = findViewById(R.id.tv_alamat_penerima_resep);
        detailAlamat = findViewById(R.id.tv_det_penerima_resep);
        invoice = findViewById(R.id.tv_invoice_pesanan_resep);
        img = findViewById(R.id.showImgResepAdmin);
        buktiBayar = findViewById(R.id.showImgBuktiBayarResepAdmin);
        etharga = findViewById(R.id.et_total_harga_admin);
        TextBuktiPembayaran = findViewById(R.id.ll_text_bukti_bayar_resep);
        BuktiPembayaran = findViewById(R.id.ll_bukti_bayar_resep_admin);
        TotalHarusBayar = findViewById(R.id.ll_total_bayar_resep);
        totalBayar = findViewById(R.id.tv_nominal_totbyr_resep_admin);
        view = findViewById(R.id.view_resep_admin);


        Kirim = findViewById(R.id.btn_krm_total_bayar_admin);
        Kirim.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                KirimHarga();
            }
        });

        Antar = findViewById(R.id.btn_antar_pesanan_resep);
        Antar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AntarPesananObat();
            }
        });

        Kembali = findViewById(R.id.iv_kembali_pesanan_resep);
        Kembali.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent (getApplicationContext(), PesananResepAdminActivity.class);
                startActivity(intent);
            }
        });

        etharga.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (etharga.getText().toString()=="") {
                    Kirim.setVisibility(View.INVISIBLE);
                } else {
                    Kirim.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        Calendar c = Calendar.getInstance();

        SimpleDateFormat formatnya = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        WaktuKirim = formatnya.format(c.getTime());

        Intent intent = getIntent();
        NamaPenerima = intent.getStringExtra("nama");
        HandphonePenerima = intent.getStringExtra("handphone");
        AlamatPenerima = intent.getStringExtra("alamat");
        DetailAlamatPenerima = intent.getStringExtra("detail_alamat");
        Invoice = intent.getStringExtra("invoice");
        Gambar = intent.getStringExtra("gambar");
        Status = intent.getIntExtra("status",0);
        BuktiBayar = intent.getStringExtra("bukti_bayar");
        Harga = intent.getStringExtra("total_harga");

        Log.i("khatima", BuktiBayar);

        namaPenerima.setText(NamaPenerima);
        handphone.setText(HandphonePenerima);
        alamat.setText(AlamatPenerima);
        detailAlamat.setText(DetailAlamatPenerima);
        invoice.setText("#"+Invoice);
        totalBayar.setText(Harga+",-");

        switch (Status){
            case 0:
                etharga.setVisibility(View.VISIBLE);
                TotalHarusBayar.setVisibility(View.GONE);
                TextBuktiPembayaran.setVisibility(View.GONE);
                BuktiPembayaran.setVisibility(View.GONE);
                Antar.setVisibility(View.GONE);
                break;

            case 1: //MENUNGGU PEMBAYARAN
                etharga.setVisibility(View.GONE);
                Kirim.setVisibility(View.GONE);
                TotalHarusBayar.setVisibility(View.VISIBLE);
                BuktiPembayaran.setVisibility(View.GONE);
                TextBuktiPembayaran.setVisibility(View.GONE);
                Antar.setVisibility(View.GONE);
                view.setVisibility(View.INVISIBLE);
                break;

            case 2: //KEMAS
                etharga.setVisibility(View.GONE);
                TotalHarusBayar.setVisibility(View.VISIBLE);
                Kirim.setVisibility(View.GONE);
                BuktiPembayaran.setVisibility(View.VISIBLE);
                TextBuktiPembayaran.setVisibility(View.VISIBLE);
                Antar.setVisibility(View.VISIBLE);
                break;

            case 3:
                etharga.setVisibility(View.GONE);
                TotalHarusBayar.setVisibility(View.VISIBLE);
                Kirim.setVisibility(View.GONE);
                BuktiPembayaran.setVisibility(View.VISIBLE);
                TextBuktiPembayaran.setVisibility(View.VISIBLE);
                Antar.setVisibility(View.GONE);
                break;
        }

//        if (Status == 0) {
//            statusdesc = "Menunggu Konfirmasi Apotek";
//            TextBuktiPembayaran.setVisibility(View.GONE);
//            BuktiPembayaran.setVisibility(View.GONE);
//            Antar.setVisibility(View.GONE);
//        }


        Picasso.get()
                .load(Gambar)
                .into(img);

        Picasso.get()
                .load(BuktiBayar)
                .into(buktiBayar);

    }

    private void KirimHarga() {
        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Kirim...");
        progressDialog.setCancelable(false);
        progressDialog.show();

        strHarga = etharga.getText().toString();
        int hargamentah = Integer.parseInt(strHarga);
        totalHarga = String.valueOf(hargamentah + 30000);

        StringRequest request = new StringRequest(Request.Method.POST, urlKrmHarga,
                response -> {
                    Log.i("khatima", response);
                    try {
                        JSONObject objectResponse = new JSONObject(response);
                        if (objectResponse.getString("respon").equals("berhasil")) {
                            Toast.makeText(getApplicationContext(), "harga dikirim", Toast.LENGTH_SHORT).show();
                            etharga.setText("");
                            Intent intent= new Intent(getApplicationContext(), PesananResepAdminActivity.class);
                            startActivity(intent);
                            finish();

                        } else {
                            Toast.makeText(getApplicationContext(), "Gagal", Toast.LENGTH_SHORT).show();
                        }
                        progressDialog.dismiss();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }, error -> Log.i("Khusnul", String.valueOf(error))) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> param = new HashMap<>();
                param.put("invoice", Invoice);
                param.put("harga", strHarga);
                param.put("status", "1");
                param.put("total_harga", totalHarga);
                return param;
            }
        };
        RequestQueue queue = Volley.newRequestQueue(Objects.requireNonNull(getApplicationContext()));
        queue.add(request);
    }

    private void AntarPesananObat() {
        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Kirim...");
        progressDialog.setCancelable(false);
        progressDialog.show();

        StringRequest request = new StringRequest(Request.Method.POST, urlAntarPesanan,
                response -> {
                    Log.i("khatima", response);
                    try {
                        JSONObject objectResponse = new JSONObject(response);
                        if (objectResponse.getString("respon").equals("berhasil")) {
                            Toast.makeText(getApplicationContext(), "Pengantaran Berhasil", Toast.LENGTH_SHORT).show();
                            Intent intent= new Intent(getApplicationContext(), PesananResepAdminActivity.class);
                            startActivity(intent);
                            finish();

                        } else {
                            Toast.makeText(getApplicationContext(), "Gagal", Toast.LENGTH_SHORT).show();
                        }
                        progressDialog.dismiss();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }, error -> Log.i("Khusnul", String.valueOf(error))) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> param = new HashMap<>();
                param.put("invoice", Invoice);
                param.put("status", "3");
                param.put("waktu_pengiriman", WaktuKirim);
                return param;
            }
        };
        RequestQueue queue = Volley.newRequestQueue(Objects.requireNonNull(getApplicationContext()));
        queue.add(request);
    }
}