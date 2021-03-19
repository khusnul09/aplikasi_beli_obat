package com.example.mynotes;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
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
            new AlertDialog.Builder(this)
                    .setMessage("Apakah Anda ingin logout dari akun ini?")
                    .setPositiveButton("Ya", (dialog, which) -> logout())
                    .setNegativeButton("Tidak", null)
                    .show();
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
        String url = "https://obats.000webhostapp.com/index.php/api/Profil?email=" + email;

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, response -> {
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

        }, Throwable::printStackTrace);
        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        queue.add(stringRequest);
    }

    public void logout() {
        String url_logout = "https://obats.000webhostapp.com/index.php/api/Hapus_token";

        StringRequest request = new StringRequest(Request.Method.POST, url_logout, response -> {
            Log.d("logout", response);
            try {
                JSONObject object = new JSONObject(response);
                if (object.getString("status").equals("token dihapus")) {
                    SharedPreferenceManager.saveBooleanPreferences(getApplicationContext(), "is_login", false);
                    Intent logout = new Intent(getApplicationContext(), LoginActivity.class);
                    startActivity(logout);
                    finish();
                } else {
                    Toast.makeText(this, object.optString("status"), Toast.LENGTH_SHORT).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }, error -> Log.i("khatima", error.toString())){
            @Override
            protected Map< String, String> getParams() {
                Map<String, String> param = new HashMap<>();
                param.put("email", email);
                return param;
            }
        };
        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(request);
    }
}