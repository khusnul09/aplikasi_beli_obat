package com.example.mynotes;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.textfield.TextInputEditText;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class GantiPasswordBaruFragment extends Fragment {

    ProgressDialog progressDialog;

    String email, passwordBaru, passwordBaruLagi;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View viewGantiPasswordBaru = inflater.inflate(R.layout.fragment_ganti_password_baru, container, false);

        email = SharedPreferenceManager.getStringPreferences(getContext(), "user_email");

        TextInputEditText tietPassBaru = viewGantiPasswordBaru.findViewById(R.id.tiet_password_baru);
        TextInputEditText tietPassBaruLagi = viewGantiPasswordBaru.findViewById(R.id.tiet_password_baru_lagi);
        Button btnGantiPassword = viewGantiPasswordBaru.findViewById(R.id.btn_ganti_password);

        progressDialog = new ProgressDialog(getContext());
        progressDialog.setTitle("Tunggu Sebentar...");
        progressDialog.setCancelable(false);

        btnGantiPassword.setOnClickListener(view -> {

            passwordBaru = Objects.requireNonNull(tietPassBaru.getText()).toString();
            passwordBaruLagi = Objects.requireNonNull(tietPassBaruLagi.getText()).toString();

            if (passwordBaru.isEmpty()) {
                Toast.makeText(getContext(), "Password tidak boleh kosong", Toast.LENGTH_SHORT).show();
            } else if (passwordBaru.equals(passwordBaruLagi)){
                progressDialog.show();
                gantiPassword();
            } else {
                Toast.makeText(getContext(), "Password yang diulangi tidak sama", Toast.LENGTH_SHORT).show();
            }
        });

        return viewGantiPasswordBaru;
    }

    public void gantiPassword() {
        String urlGantiPassword = "https://obats.000webhostapp.com/index.php/api/Ganti_password";

        StringRequest stringRequest = new StringRequest(Request.Method.POST, urlGantiPassword, response -> {
            try {
                JSONObject jsonObject = new JSONObject(response);
                Log.d("khatima", response);
                if (jsonObject.getString("status").equals("sukses")) {
                    Intent intent = new Intent(getContext(), ProfilActivity.class);
                    startActivity(intent);
                    Toast.makeText(getContext(), "Password telah diubah", Toast.LENGTH_SHORT).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            progressDialog.dismiss();
        }, error -> {}) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> param = new HashMap<>();
                param.put("email", email);
                param.put("password_baru", passwordBaru);
                return param;
            }
        };
        RequestQueue queue = Volley.newRequestQueue(Objects.requireNonNull(getContext()));
        queue.add(stringRequest);
    }
}