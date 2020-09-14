package com.example.mynotes;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

import java.util.Objects;

public class RegisterActivity extends AppCompatActivity {
    Button buttonDaftar;
    EditText namaLengkap, noHp, alamat, emailReg, passwordReg;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        auth = FirebaseAuth.getInstance();

        namaLengkap = findViewById(R.id.et_name);
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
                namaLengkap.setError("Masukkan alamat");
                return;
            }
            if (!Utils.inputValidation(emailReg)) {
                noHp.setError("Masukkan email");
                return;
            }
            if (!Utils.inputValidation(passwordReg)) {
                noHp.setError("Masukkan password");
                return;
            }

            String email = Objects.requireNonNull(emailReg.getText()).toString();
            String password = Objects.requireNonNull(passwordReg.getText()).toString();
            //if everything is okay
            auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(this, task -> {
                if (!task.isSuccessful()) {
                    Toast.makeText(this, "Gagal mendaftarkan user", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "Berhasil mendaftarkan user!", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                    startActivity(intent);
                }
            });
        });
    }
}
