package com.example.mynotes;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class AlamatPasienActivity extends AppCompatActivity {

    private static final String url = "https://obats.000webhostapp.com//api/user/addimage";

    Button kirim;
    ImageView backAlamatPasien;
    private EditText NamaLengkapPasien, NoHpPasien, AlamatPasien, DetaiAlamatPasien;
    String namaLengkapPasien, noHpPasien, alamatPasien, detailAlamatPasien, email_user, alamatGambar;

    String time, name="", invoice, waktuKirim;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alamat_pasien);

        email_user = SharedPreferenceManager.getStringPreferences(getApplicationContext(), "user_email");

        NamaLengkapPasien = findViewById(R.id.et_nama_pasien);
        NoHpPasien = findViewById(R.id.et_hp_pasien);
        AlamatPasien = findViewById(R.id.et_alamat_pasien);
        DetaiAlamatPasien = findViewById(R.id.et_det_alamat_pasien);

        //buat invoice
        Calendar c = Calendar.getInstance();
        @SuppressLint("SimpleDateFormat") SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
        time = dateFormat.format(c.getTime());
        for ( int i=0; i < 5; i++) {
            char a = email_user.charAt(i);
            name += a;
        }
        invoice = "inv"+name+time;
        Log.i("khatima", invoice);

        SimpleDateFormat formatnya = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        waktuKirim = formatnya.format(c.getTime());

        alamatGambar = getIntent().getStringExtra("namagambar");

        backAlamatPasien = findViewById(R.id.iv_kembali8);
        backAlamatPasien.setOnClickListener(v -> {
            Intent intent = new Intent (getApplicationContext(), PesanDenganResepActivity.class);
            startActivity(intent);

        });

        kirim = findViewById(R.id.btn_kirim);
        kirim.setOnClickListener(v -> {
            if (!Utils.inputValidation(NamaLengkapPasien)) {
                NamaLengkapPasien.setError("Masukkan nama lengkap penerima pesanan");
                return;
            }
            if (!Utils.inputValidation(NoHpPasien)) {
                NoHpPasien.setError("Masukkan nomor hp penerima pesanan");
                return;
            }
            if (!Utils.inputValidation(AlamatPasien)) {
                AlamatPasien.setError("Masukkan alamat penerima pesanan");
                return;
            }
            if (!Utils.inputValidation(DetaiAlamatPasien)) {
                DetaiAlamatPasien.setError("Masukkan detail alamat penerima pesanan");
                return;
            }

            alamatPasien();
        });

    }

    private void alamatPasien() {
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Mengirim resep...");
        progressDialog.setCancelable(false);
        progressDialog.show();

        namaLengkapPasien = Objects.requireNonNull(NamaLengkapPasien.getText()).toString().trim();
        noHpPasien = Objects.requireNonNull(NoHpPasien.getText()).toString().trim();
        alamatPasien = Objects.requireNonNull(AlamatPasien.getText()).toString().trim();
        detailAlamatPasien = Objects.requireNonNull(DetaiAlamatPasien.getText()).toString().trim();

        String tokenAdmin = SharedPreferenceManager.getStringPreferences(getApplicationContext(), "tokenadmin");

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                response -> {
                    Log.i("khatima", response);
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        String respon = jsonObject.getString("response");
                        Log.i("Khatima", respon);
                        if (jsonObject.getString("response").equals("berhasil")) {
                            Intent keResepTerkirim = new Intent(AlamatPasienActivity.this, ResepObatTerkirimActivity.class);
                            startActivity(keResepTerkirim);
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
                params.put("nama_penerima", namaLengkapPasien);
                params.put("handphone", noHpPasien);
                params.put("alamat", alamatPasien);
                params.put("detail_alamat", detailAlamatPasien);
                params.put("gambar_resep", alamatGambar);
                params.put("waktu", waktuKirim);
                params.put("token_tujuan", tokenAdmin);
                params.put("title", "Pesanan Resep");
                params.put("message", "Ada pesanan...");
                return  params;

            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(AlamatPasienActivity.this);
        requestQueue.add(stringRequest);
    }
}
