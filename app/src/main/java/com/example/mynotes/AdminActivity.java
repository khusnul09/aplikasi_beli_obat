package com.example.mynotes;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingService;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class AdminActivity extends AppCompatActivity {
    String url = "https://obats.000webhostapp.com/api/user/cektoken";

    private CardView PesananResep, PesananTanpaResep;
    ImageView logout;
    String tokensekarang, tokenbaru, email;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        email = SharedPreferenceManager.getStringPreferences(getApplicationContext(), "user_email");

        tokensekarang = SharedPreferenceManager.getStringPreferences(getApplicationContext(),"token");

        FirebaseMessaging.getInstance().getToken().addOnCompleteListener(new OnCompleteListener<String>() {
            @Override
            public void onComplete(@NonNull Task<String> task) {
                tokenbaru = task.getResult();
                Log.w("khatima", tokenbaru);

                if (tokensekarang.equals(tokenbaru)) {
                    Log.d("khatima", "token sama");
                }
                else {
                    tokensekarang = tokenbaru;
                    cektoken();
                }
            }
        });

        PesananResep = findViewById(R.id.pesanan_resep);
        PesananResep.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminActivity.this, PesananResepAdminActivity.class);
                startActivity(intent);
            }
        });

        PesananTanpaResep = findViewById(R.id.pesanan_tanpa_resep);
        PesananTanpaResep.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminActivity.this, PesananObatAdminActivity.class);
                startActivity(intent);
            }
        });

        logout = findViewById(R.id.iv_logout);
        logout.setOnClickListener(v -> {
            SharedPreferenceManager.saveBooleanPreferences(getApplicationContext(), "is_login", false);
            Intent logout = new Intent(getApplicationContext(), LoginActivity.class);
            startActivity(logout);
            finish();
        });
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
        RequestQueue requestQueue = Volley.newRequestQueue(AdminActivity.this);
        requestQueue.add(stringRequest);
    }
}
