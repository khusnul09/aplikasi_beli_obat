package com.example.mynotes;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        int waktuLoad = 4000;

        Log.i("Khatima", String.valueOf(SharedPreferenceManager.getBooleanPreferences(getApplicationContext(), "is_login")));
        new Handler().postDelayed(() -> {
            if (SharedPreferenceManager.getBooleanPreferences(getApplicationContext(), "is_login")) {
                switch (SharedPreferenceManager.getStringPreferences(getApplicationContext(), "user_role")) {
                    case "user":
                        Intent home = new Intent(SplashActivity.this, PesanObatActivity.class);
                        startActivity(home);
                        finish();
                        break;
                    case "admin":
                        Intent homeAdmin = new Intent(SplashActivity.this, AdminActivity.class);
                        startActivity(homeAdmin);
                        finish();
                        break;
                }

            } else {
                Intent login = new Intent(SplashActivity.this, LoginActivity.class);
                startActivity(login);
                finish();
            }
        }, waktuLoad);
    }
}