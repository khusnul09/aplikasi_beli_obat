package com.example.mynotes;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.textfield.TextInputEditText;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class    EditProfilActivity extends AppCompatActivity {

    ProgressDialog progressDialog;
    TextInputEditText editNama, editNo, editAlamat;
    ImageView backEditProfil;
    Button SimpanProfil;
    String email, namaEdit, handphoneEdit, alamatEdit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profil);

        backEditProfil = findViewById(R.id.iv_kembali_edit_profil);
        backEditProfil.setOnClickListener(v -> {
            Intent intent = new Intent (getApplicationContext(), ProfilActivity.class);
            startActivity(intent);

        });

        SimpanProfil = findViewById(R.id.bt_simpan_edit_profil);
        SimpanProfil.setOnClickListener(v -> updateUser());

        email = SharedPreferenceManager.getStringPreferences(getApplicationContext(), "user_email");

        editNama = findViewById(R.id.et_nama_edit);
        editNo = findViewById(R.id.et_no_edit);
        editAlamat = findViewById(R.id.et_alamat_edit);

        editProfil();
    }

    private void editProfil() {
        String url_read = "https://obats.000webhostapp.com/index.php/api/Profil?email=" + email;

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url_read, response -> {
            try {
                JSONObject jsonObject = new JSONObject(response);
                JSONArray jsonArray = jsonObject.getJSONArray("data");
                for (int i = 0; i < jsonArray.length(); i++) {
                    editNama.setText(jsonArray.getJSONObject(i).optString("nama"));
                    editNo.setText(jsonArray.getJSONObject(i).optString("handphone"));
                    editAlamat.setText(jsonArray.getJSONObject(i).optString("alamat"));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }, error -> {
        });
        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        queue.add(stringRequest);
    }

    private void updateUser() {
        String url_edit_user = "https://obats.000webhostapp.com/index.php/api/Edit_user";

        progressDialog = new ProgressDialog(EditProfilActivity.this);
        progressDialog.show();
        progressDialog.setCancelable(false);
        progressDialog.setContentView(R.layout.progress_dialog);
        Objects.requireNonNull(progressDialog.getWindow()).setBackgroundDrawableResource(
                android.R.color.transparent
        );

        namaEdit = Objects.requireNonNull(editNama.getText()).toString().trim();
        handphoneEdit = Objects.requireNonNull(editNo.getText()).toString().trim();
        alamatEdit = Objects.requireNonNull(editAlamat.getText()).toString().trim();

        StringRequest request = new StringRequest(Request.Method.POST, url_edit_user, response -> {
            Log.i("EditProfil: response", response);
            try {
                JSONObject object = new JSONObject(response);
                if (object.optString("respon").equals("berhasil")) {
                    Toast.makeText(getApplicationContext(), "Profil diperbarui", Toast.LENGTH_SHORT).show();
                    SharedPreferenceManager.saveStringPreferences(this, "user_name", namaEdit);
                    Intent intent = new Intent(EditProfilActivity.this, ProfilActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(getApplicationContext(), "Gagal memperbarui profil", Toast.LENGTH_SHORT).show();
                }
                progressDialog.dismiss();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }, error -> {

        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> param = new HashMap<>();
                param.put("nama", namaEdit);
                param.put("handphone", handphoneEdit);
                param.put("alamat", alamatEdit);
                param.put("emailuser", email);
                return param;
            }
        };
        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        queue.add(request);
    }

}