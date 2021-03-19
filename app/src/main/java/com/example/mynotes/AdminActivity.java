package com.example.mynotes;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.messaging.FirebaseMessaging;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class AdminActivity extends AppCompatActivity {
    String url = "https://obats.000webhostapp.com/index.php/api/Cek_token";

    ImageView logout;
    String tokenbaru, email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        email = SharedPreferenceManager.getStringPreferences(getApplicationContext(), "user_email");

        FirebaseMessaging.getInstance().getToken().addOnCompleteListener(task -> {
            tokenbaru = task.getResult();
            Log.w("AdminActivityLog: token", tokenbaru);
            Log.w("AdminActivityLog: email", email);

            cektoken();
        });

        CardView pesananResep = findViewById(R.id.pesanan_resep);
        pesananResep.setOnClickListener(v -> {
            Intent intent = new Intent(AdminActivity.this, PesananResepAdminActivity.class);
            startActivity(intent);
        });

        CardView pesananTanpaResep = findViewById(R.id.pesanan_tanpa_resep);
        pesananTanpaResep.setOnClickListener(v -> {
            Intent intent = new Intent(AdminActivity.this, PesananObatAdminActivity.class);
            startActivity(intent);
        });

        logout = findViewById(R.id.iv_logout);
        logout.setOnClickListener(v -> {
            new AlertDialog.Builder(this)
                    .setMessage("Apakah Anda ingin logout dari akun ini?")
                    .setPositiveButton("Ya", (dialog, which) -> logoutadmin())
                    .setNegativeButton("Tidak", null)
                    .show();
        });
    }

    private void cektoken() {

        /*RequestQueue queue = Volley.newRequestQueue(this);

        StringRequest request = new StringRequest(Request.Method.PUT, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.i("AdminActivityLog: Res", response.toString());
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i("AdminActivityLog: err", error.getMessage());
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> param = new HashMap<String, String>();
                param.put("email", email);
                param.put("token", tokenbaru);

                param.put("Content-Type", "application/json; charset=utf-8");
                return param;
            }
        };

        queue.add(request);*/

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                response -> {
                    Log.i("AdminActivity: Response", response);
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        String respon = jsonObject.getString("text");
                        Log.i("Khatima", respon);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }, error -> Toast.makeText(getApplicationContext(), "error: "+ error.toString(), Toast.LENGTH_LONG).show()) {
            @Override
            protected Map<String, String> getParams(){
                Map<String, String> params = new HashMap<>();
                params.put("email", email);
                params.put("token", tokenbaru);
                return  params;

            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(AdminActivity.this);
        requestQueue.add(stringRequest);
    }
    public void logoutadmin() {
        String url_logoutadmin = "https://obats.000webhostapp.com/index.php/api/Hapus_token";

        StringRequest request = new StringRequest(Request.Method.POST, url_logoutadmin, response -> {
            Log.d("logoutadmin", response);
            try {
                JSONObject object = new JSONObject(response);
                if (object.getString("status").equals("token dihapus")) {
                    SharedPreferenceManager.saveBooleanPreferences(getApplicationContext(), "is_login", false);
                    Intent logoutadmin = new Intent(getApplicationContext(), LoginActivity.class);
                    startActivity(logoutadmin);
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