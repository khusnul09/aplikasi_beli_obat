package com.example.mynotes;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class LoginActivity extends AppCompatActivity {

    ProgressDialog progressDialog;
    private TextInputLayout etEmail, etPassword;
    TextView tvDaftar;
    Button btnLogin;
    String role, statsnk, token, nama;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        etEmail = findViewById(R.id.email);
        etPassword = findViewById(R.id.password);
        btnLogin = findViewById(R.id.btn_login);
        tvDaftar = findViewById(R.id.tv_daftar_disini);

        tvDaftar.setOnClickListener(v -> {
            Intent i = new Intent(LoginActivity.this,
                    RegisterActivity.class);
            startActivity(i);
        });

        btnLogin. setOnClickListener(v -> loginValidation());
    }

    private void loginValidation() {
        final String url = "https://obats.000webhostapp.com/index.php/api/Login";

        if (Utils.inputValidation(etEmail)) {
            Log.i("Khatima", "if Utils.inputValidation(etEmail) dijalankan");
            etEmail.setErrorEnabled(false);
            if (Utils.inputValidation(etPassword)) {
                Log.i("Khatima", "if Utils.inputValidation(etPassword) dijalankan");
                SystemUtils.hideKeyBoard(this);
                etPassword.setErrorEnabled(false);
                //if everything is okay
                progressDialog = new ProgressDialog(LoginActivity.this);
                progressDialog.show();
                progressDialog.setContentView(R.layout.progress_dialog);
                Objects.requireNonNull(progressDialog.getWindow()).setBackgroundDrawableResource(
                        android.R.color.transparent
                );
                StringRequest request = new StringRequest(Request.Method.POST, url,
                        response -> {
                            Log.i("Khatima", response);
                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                Log.i("Khatima", String.valueOf(jsonObject));
                                if (jsonObject.getString("status").equals("sukses")) {
                                    Log.i("Khatima", "if dijalankan");
                                    JSONArray array = jsonObject.getJSONArray("data");
                                    for (int i = 0; i < array.length(); i++) {
                                        JSONObject objData = array.getJSONObject(i);
                                        role = objData.getString("role");
                                        token = objData.getString("token");
                                        statsnk = objData.getString("snk");
                                        nama = objData.getString("nama");
                                    }
                                    Log.i("khatima", role);
                                    SharedPreferenceManager.saveStringPreferences(getApplicationContext(), "user_email", Objects.requireNonNull(etEmail.getEditText()).getText().toString());
                                    SharedPreferenceManager.saveStringPreferences(getApplicationContext(), "user_role", role);
                                    SharedPreferenceManager.saveStringPreferences(getApplicationContext(),"token", token);
                                    SharedPreferenceManager.saveStringPreferences(getApplicationContext(), "user_name", nama);
                                    switch (role) {
                                        case "user":
                                            if (statsnk.equals("setuju")) {
                                                Toast.makeText(getApplicationContext(), "Berhasil login!", Toast.LENGTH_SHORT).show();
                                                SharedPreferenceManager.saveBooleanPreferences(getApplicationContext(), "is_login", true);

                                                Intent intent = new Intent(LoginActivity.this, PesanObatActivity.class);
                                                startActivity(intent);
                                                finish();
                                            } else if (statsnk.equals("belum")) {
                                                Intent intent = new Intent(LoginActivity.this, SyaratDanKetentuanActivity.class);
                                                startActivity(intent);
                                                finish();
                                            }
                                            break;
                                        case "admin":
                                            Toast.makeText(getApplicationContext(), "Berhasil login!", Toast.LENGTH_SHORT).show();
                                            SharedPreferenceManager.saveBooleanPreferences(getApplicationContext(), "is_login", true);

                                            Intent intentAdmin = new Intent(LoginActivity.this, AdminActivity.class);
                                            startActivity(intentAdmin);
                                            finish();
                                            break;
                                    }
                                } else if (jsonObject.getString("status").equals("0")){
                                    Log.i("Khatima", "else dijalankan");
                                    Toast.makeText(getApplicationContext(), "Email atau Password salah", Toast.LENGTH_SHORT).show();
                                }
                                progressDialog.dismiss();

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        },
                        error -> {

                        }) {
                    @Override
                    protected Map<String, String> getParams() {
                        Map<String, String> param = new HashMap<>();
                        param.put("email", Objects.requireNonNull(etEmail.getEditText()).getText().toString());
                        param.put("password", Objects.requireNonNull(etPassword.getEditText()).getText().toString());
                        return param;
                    }
                };
                RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
                queue.add(request);

                /*auth.signInWithEmailAndPassword(etEmail.getEditText().getText().toString(), etPassword.getEditText().getText().toString())
                        .addOnCompleteListener(task -> {
                            if (!task.isSuccessful()) {
                                Toast.makeText(this, "Gagal login karena: "+ task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            } else {

                            }
                        });*/
            } else {
                Objects.requireNonNull(etPassword.getEditText()).setError("Please enter your password");
            }
        } else {
            Objects.requireNonNull(etEmail.getEditText()).setError("Please enter your username");
        }
    }
}
