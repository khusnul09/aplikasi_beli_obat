package com.example.mynotes;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class AlamatPasienActivity extends AppCompatActivity {

    Button lanjutkan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alamat_pasien);

        lanjutkan = findViewById(R.id.btn_lanjutkan);
        lanjutkan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AlamatPasienActivity.this, ResepObatTerkirimActivity.class);
                startActivity(intent);
            }
        });
    }
}
