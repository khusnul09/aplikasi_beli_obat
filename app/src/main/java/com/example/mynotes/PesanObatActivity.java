package com.example.mynotes;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.messaging.FirebaseMessaging;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class PesanObatActivity extends AppCompatActivity {

    String url = "https://obats.000webhostapp.com/api/user/cektoken";
    final String url_tokenadmin = "https://obats.000webhostapp.com/api/tokenadmin";
    private CardView OpenImage, PilihObat, Riwayat;
    ImageView Akun;
    String tokensekarang, tokenbaru, email, nama;
    String tokenA;
    TextView tvNama;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pesan_obat);

        email = SharedPreferenceManager.getStringPreferences(getApplicationContext(), "user_email");
        nama = SharedPreferenceManager.getStringPreferences(getApplicationContext(), "user_name");
        Log.d("khatima", email);

        tvNama = findViewById(R.id.tv_nama_user);
        tvNama.setText(nama);


        //tokensekarang = SharedPreferenceManager.getStringPreferences(getApplicationContext(),"token");
        FirebaseMessaging.getInstance().getToken().addOnCompleteListener(task -> {
            tokensekarang = task.getResult();
            Log.w("khatima", tokensekarang);
            cektoken();


            /*if (tokensekarang.equals(tokenbaru)) {
                Log.d("khatima", "token sama");
            }
            else {
                tokensekarang = tokenbaru;
                Log.d("khatima", tokensekarang);

            }*/
        });

        OpenImage = findViewById(R.id.ada_resep);
        OpenImage.setOnClickListener(v -> {
            Intent intent = new Intent(PesanObatActivity.this, PesanDenganResepActivity.class);
            startActivity(intent);
        });
        PilihObat = findViewById(R.id.no_resep);
        PilihObat.setOnClickListener(v -> {
            Intent intent = new Intent(PesanObatActivity.this, PesanTanpaResepActivity.class);
            startActivity(intent);
        });

        Riwayat = findViewById(R.id.riwayat);
        Riwayat.setOnClickListener(v -> {
            Intent intent = new Intent(PesanObatActivity.this, RiwayatActivity.class);
            startActivity(intent);
        });
        Akun = findViewById(R.id.btn_akun);
        Akun.setOnClickListener(v -> {
            Intent intent = new Intent(PesanObatActivity.this, ProfilActivity.class);
            startActivity(intent);
        });

        tokenadmin();
    }

    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this)
                .setTitle("Keluar dari aplikasi?")
                .setMessage("Apakah Anda ingin keluar dari aplikasi?")
                .setPositiveButton("Ya", (dialog, which) -> finish())
                .setNegativeButton("Tidak", null)
                .show();
    }

    private void cektoken() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                response -> {
                    Log.i("khatima", response);
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        String respon = jsonObject.getString("response");
                        Log.i("Khatima", respon);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }, error -> Toast.makeText(getApplicationContext(), "error: "+ error.toString(), Toast.LENGTH_LONG).show()) {
            @Override
            protected Map<String, String> getParams(){
                Map<String, String> params = new HashMap<>();
                params.put("email", email);
                params.put("token", tokensekarang);
                return  params;

            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(PesanObatActivity.this);
        requestQueue.add(stringRequest);
    }

    private void tokenadmin() {
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url_tokenadmin,
                response -> {
                   // Log.i("khatima", response);
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        String respon = jsonObject.getString("text");
                        JSONArray array = jsonObject.getJSONArray("text");
                        tokenA = array.getJSONObject(0).optString("token");
                        SharedPreferenceManager.saveStringPreferences(getApplicationContext(), "tokenadmin", tokenA);

                        Log.i("KhatimaTOKEN", tokenA);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }, error -> Toast.makeText(getApplicationContext(), "error: "+ error.toString(), Toast.LENGTH_LONG).show()) {
        };
        RequestQueue requestQueue = Volley.newRequestQueue(PesanObatActivity.this);
        requestQueue.add(stringRequest);
    }
}
