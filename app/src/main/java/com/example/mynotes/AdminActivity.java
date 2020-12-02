package com.example.mynotes;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

public class AdminActivity extends AppCompatActivity {

    private CardView PesananResep, PesananTanpaResep;
    ImageView logout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

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
}