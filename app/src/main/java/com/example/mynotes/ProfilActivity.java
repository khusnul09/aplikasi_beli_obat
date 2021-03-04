package com.example.mynotes;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class ProfilActivity extends AppCompatActivity {

    LinearLayout EditProfil;
    TextView NamaLengkap, NoHp, Alamat, Email, editPassword;
    ImageView backProfil;
    Button logout;

    String url = "https://obats.000webhostapp.com/api/user/profil";
    String email;

    @Override
    public void onBackPressed() {
        Intent kembali = new Intent(this, PesanObatActivity.class);
        startActivity(kembali);
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profil);

        email = SharedPreferenceManager.getStringPreferences(getApplicationContext(), "user_email");

        NamaLengkap = (TextView) findViewById(R.id.tv_nama_profil);
        NoHp = (TextView) findViewById(R.id.tv_hp_profil);
        Alamat = (TextView) findViewById(R.id.tv_alamat_profil);
        Email = (TextView) findViewById(R.id.tv_email_profil);
        EditProfil = findViewById(R.id.ll_edit_akun);
        logout = findViewById(R.id.btn_logout);
        backProfil = findViewById(R.id.iv_back_profil);
        editPassword = findViewById(R.id.tv_edit_password);

        EditProfil.setOnClickListener(v -> {
            Intent intent = new Intent(ProfilActivity.this, EditProfilActivity.class);
            startActivity(intent);
        });

        logout.setOnClickListener(v -> {
            SharedPreferenceManager.saveBooleanPreferences(getApplicationContext(), "is_login", false);
            Intent logout = new Intent(getApplicationContext(), LoginActivity.class);
            startActivity(logout);
            finish();
        });

        backProfil.setOnClickListener(v -> {
            Intent intent = new Intent (getApplicationContext(), PesanObatActivity.class);
            startActivity(intent);
            finish();
        });

        editPassword.setOnClickListener(view -> {
            Intent intentEditPassword = new Intent(ProfilActivity.this, GantiPasswordActivity.class);
            startActivity(intentEditPassword);
        });

        getDataProfil();
    }

    public void getDataProfil() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, response -> {
            try {
                JSONObject jsonObject = new JSONObject(response);
                JSONArray jsonArray = jsonObject.getJSONArray("data");
                for (int i = 0; i < jsonArray.length(); i++ ) {
                    NamaLengkap.setText(jsonArray.getJSONObject(i).optString("nama"));
                    NoHp.setText(jsonArray.getJSONObject(i).optString("handphone"));
                    Alamat.setText(jsonArray.getJSONObject(i).optString("alamat"));
                    Email.setText(jsonArray.getJSONObject(i).optString("email"));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }, Throwable::printStackTrace) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> param = new HashMap<>();
                param.put("email", email);
                return param;
            }
        };
        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        queue.add(stringRequest);
    }
}