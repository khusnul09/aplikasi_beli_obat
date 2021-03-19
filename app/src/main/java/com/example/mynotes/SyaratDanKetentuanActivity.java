package com.example.mynotes;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class SyaratDanKetentuanActivity extends AppCompatActivity {

    ProgressDialog progressDialog;
    Button buttonSetuju;
    Button buttonTidakSetuju;
    String userEmail, snk, nilaiSnk;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_syarat_dan_ketentuan);

        userEmail = SharedPreferenceManager.getStringPreferences(getApplicationContext(), "user_email");

        buttonSetuju = findViewById(R.id.setuju);
        buttonSetuju.setOnClickListener(v -> {
            snk = "setuju";
            cekSnK();
        });

        buttonTidakSetuju = findViewById(R.id.tidak_setuju);
        buttonTidakSetuju.setOnClickListener(v -> {
            snk = "belum";
            cekSnK();
        });
    }

    private void cekSnK() {
        final String urlsnk = "https://obats.000webhostapp.com/index.php/api/Snk";

        progressDialog = new ProgressDialog(SyaratDanKetentuanActivity.this);
        progressDialog.show();
        progressDialog.setContentView(R.layout.progress_dialog);
        progressDialog.getWindow().setBackgroundDrawableResource(
                android.R.color.transparent
        );
        StringRequest request = new StringRequest(Request.Method.POST, urlsnk,
                response -> {
                    Log.i("khatima", response);
                    try {
                        JSONObject object = new JSONObject(response);
                        JSONArray array = object.getJSONArray("data");
                        for (int i = 0; i < array.length(); i++) {
                            JSONObject obj = array.getJSONObject(i);
                            nilaiSnk = obj.getString("snk");
                        }
                        Log.i("khatima", nilaiSnk);
                        if (nilaiSnk.equals("setuju")) {
                            String role = SharedPreferenceManager.getStringPreferences(getApplicationContext(), "user_role");
                            if (role.equals("user")) {
                                Intent intent = new Intent(SyaratDanKetentuanActivity.this, PesanObatActivity.class);
                                startActivity(intent);
                                finish();
                            } else if (role.equals("admin")) {
                                Toast.makeText(getApplicationContext(), "ke halaman admin nanti", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            SharedPreferenceManager.saveStringPreferences(getApplicationContext(), "user_email", "");
                            SharedPreferenceManager.saveStringPreferences(getApplicationContext(), "user_role", "");
                            Intent intent = new Intent(SyaratDanKetentuanActivity.this, LoginActivity.class);
                            startActivity(intent);
                            finish();
                        }
                        progressDialog.dismiss();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                },
                error -> {

                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("email", userEmail);
                params.put("snk", snk);
                return params;
            }
        };
        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        queue.add(request);
    }
}
