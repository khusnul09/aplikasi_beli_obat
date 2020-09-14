package com.example.mynotes;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class LoginActivity extends AppCompatActivity {

    private TextInputLayout etEmail, etPassword;
    TextView tvDaftar;
    Button btnLogin;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        auth = FirebaseAuth.getInstance();

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("message");

        myRef.setValue("Hello, World!");

        etEmail = findViewById(R.id.email);
        etPassword = (TextInputLayout) findViewById(R.id.password);
        btnLogin = (Button) findViewById(R.id.btn_login);
        tvDaftar = (TextView) findViewById(R.id.tv_daftar);


        tvDaftar.setOnClickListener(v -> {
            Intent i = new Intent(LoginActivity.this,
                    RegisterActivity.class);
            startActivity(i);
        });

        btnLogin. setOnClickListener(v -> {
            loginValidation();
        });
    }

    private void loginValidation() {
        if (Utils.inputValidation(etEmail)) {
            etEmail.setErrorEnabled(false);
            if (Utils.inputValidation(etPassword)) {
                SystemUtils.hideKeyBoard(this);
                etPassword.setErrorEnabled(false);
                //if everything is okay
                auth.signInWithEmailAndPassword(etEmail.getEditText().getText().toString(), etPassword.getEditText().getText().toString())
                        .addOnCompleteListener(task -> {
                            if (!task.isSuccessful()) {
                                Toast.makeText(this, "Gagal login karena: "+ task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(this, "Berhasil login!", Toast.LENGTH_SHORT).show();
                                SharedPreferenceManager.saveBooleanPreferences(this, "is_login", true);
                                SharedPreferenceManager.saveStringPreferences(this, "user_email", etEmail.getEditText().getText().toString());
                                Intent intent = new Intent(this, SyaratDanKetentuanActivity.class);
                                startActivity(intent);
                            }
                        });
            } else {
                etPassword.getEditText().setError("Please enter your password");
            }
        } else {
            etEmail.getEditText().setError("Please enter your username");
        }
    }
}
