package com.example.mynotes;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.textfield.TextInputEditText;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class AlamatPasienActivity extends AppCompatActivity {

    private static final String url = "https://obats.000webhostapp.com//api/user/update_penerima";

    Button kirim;
    ProgressDialog progressDialog;
    private EditText NamaLengkapPasien, NoHpPasien, AlamatPasien, DetaiAlamatPasien;
    String namaLengkapPasien, noHpPasien, alamatPasien, detailAlamatPasien;
    String invoice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alamat_pasien);

        NamaLengkapPasien = findViewById(R.id.et_nama_pasien);
        NoHpPasien = findViewById(R.id.et_hp_pasien);
        AlamatPasien = findViewById(R.id.et_alamat_pasien);
        DetaiAlamatPasien = findViewById(R.id.et_det_alamat_pasien);

        kirim = findViewById(R.id.btn_kirim);
        kirim.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {alamatPasien();}
        });

        invoice = getIntent().getStringExtra("invoice");
    }
    private void alamatPasien() {
        progressDialog = new ProgressDialog(AlamatPasienActivity.this);
        progressDialog.show();
        progressDialog.setContentView(R.layout.progress_dialog);
        progressDialog.setCancelable(false);
        progressDialog.getWindow().setBackgroundDrawableResource(
                android.R.color.transparent
        );

        namaLengkapPasien = Objects.requireNonNull(NamaLengkapPasien.getText()).toString().trim();
        noHpPasien = Objects.requireNonNull(NoHpPasien.getText()).toString().trim();
        alamatPasien = Objects.requireNonNull(AlamatPasien.getText()).toString().trim();
        detailAlamatPasien = Objects.requireNonNull(DetaiAlamatPasien.getText()).toString().trim();

        StringRequest request = new StringRequest(Request.Method.POST, url,
                response -> {
                    Toast.makeText(getApplicationContext(), "Berhasil", Toast.LENGTH_SHORT).show();
                    try {
                        JSONObject object = new JSONObject(response);
                        progressDialog.dismiss();
                        if (object.getString("text").equals("berhasil")) {
                            Intent keResepTerkirim = new Intent(AlamatPasienActivity.this, ResepObatTerkirimActivity.class);
                            startActivity(keResepTerkirim);
                            finish();
                        } else {
                            Toast.makeText(getApplicationContext(), object.getString("text"), Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    progressDialog.dismiss();
                },
                error -> {

                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> param = new HashMap<>();
                param.put("invoice",invoice);
                param.put("nama_penerima", namaLengkapPasien);
                param.put("handphone", noHpPasien);
                param.put("alamat", alamatPasien);
                param.put("detail_alamat", detailAlamatPasien);
                return param;
            }
        };
        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        queue.add(request);
    }
}
