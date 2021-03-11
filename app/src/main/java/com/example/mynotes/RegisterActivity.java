package com.example.mynotes;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioGroup;
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

public class RegisterActivity extends AppCompatActivity {

    private static final String url = "https://obats.000webhostapp.com//api/user/adduser";

    ProgressDialog progressDialog;
    Button buttonDaftar;
    ImageView kembali;
    private TextInputEditText namaLengkap, noHp, alamat, emailReg, passwordReg;
    String dftNamaLengkap, dftNoHp, dftAlamat, dftEmail, dftPassword ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        namaLengkap = findViewById(R.id.et_nama);
        noHp = findViewById(R.id.et_handphone);
        alamat = findViewById(R.id.et_alamat);
        emailReg = findViewById(R.id.et_reg_email);
        passwordReg = findViewById(R.id.et_reg_password);

        buttonDaftar = findViewById(R.id.btn_daftar);

        buttonDaftar.setOnClickListener(v -> {
            if (!Utils.inputValidation(namaLengkap)) {
                namaLengkap.setError("Masukkan nama lengkap");
                return;
            }
            if (!Utils.inputValidation(noHp)) {
                noHp.setError("Masukkan nomor hp");
                return;
            }
            if (!Utils.inputValidation(alamat)) {
                alamat.setError("Masukkan alamat");
                return;
            }
            if (!Utils.inputValidation(emailReg)) {
                emailReg.setError("Masukkan email");
                return;
            }
            if (!Utils.inputValidation(passwordReg)) {
                passwordReg.setError("Masukkan password");
                return;
            }

            login();
        });

        kembali = findViewById(R.id.iv_kembali1);
        kembali.setOnClickListener(v -> {
            Intent intent = new Intent (getApplicationContext(), LoginActivity.class);
            startActivity(intent);

        });
    }

    private void login() {
        progressDialog = new ProgressDialog(RegisterActivity.this);
        progressDialog.show();
        progressDialog.setContentView(R.layout.progress_dialog);
        progressDialog.getWindow().setBackgroundDrawableResource(
                android.R.color.transparent
        );

        dftNamaLengkap = Objects.requireNonNull(namaLengkap.getText()).toString().trim();
        dftNoHp = Objects.requireNonNull(noHp.getText()).toString().trim();
        dftAlamat = Objects.requireNonNull(alamat.getText()).toString().trim();
        dftEmail = Objects.requireNonNull(emailReg.getText()).toString().trim();
        dftPassword = Objects.requireNonNull(passwordReg.getText()).toString().trim();

        StringRequest request = new StringRequest(Request.Method.POST, url,
                response -> {
                    try {
                        JSONObject object = new JSONObject(response);
                        progressDialog.dismiss();
                        if (object.getString("text").equals("berhasil")) {
                            Toast.makeText(getApplicationContext(), "Data Berhasil Ditambah", Toast.LENGTH_SHORT).show();
                            SharedPreferenceManager.saveStringPreferences(getApplicationContext(), "user_email", dftEmail);
                            SharedPreferenceManager.saveStringPreferences(getApplicationContext(), "user_role", "user");
//                            SharedPreferenceManager.saveStringPreferences(getApplicationContext(),"token", token);
                            Intent keLogin = new Intent(RegisterActivity.this, SyaratDanKetentuanActivity.class);
                            startActivity(keLogin);
                        } else {
                            Toast.makeText(getApplicationContext(), object.getString("text"), Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                },
                error -> {

                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> param = new HashMap<>();
                param.put("nama", dftNamaLengkap);
                param.put("handphone", dftNoHp);
                param.put("alamat", dftAlamat);
                param.put("email", dftEmail);
                param.put("password", dftPassword);
                param.put("role", "user");
                param.put("snk", "belum");
//                param.put("token", token);
                return param;
            }
        };
        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        queue.add(request);
    }
}
