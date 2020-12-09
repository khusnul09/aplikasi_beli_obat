package com.example.mynotes;

import androidx.appcompat.app.AppCompatActivity;

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
import com.google.android.material.textfield.TextInputEditText;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class EditDataPenerimaResepActivity extends AppCompatActivity {

    ProgressDialog progressDialog;
    TextInputEditText editNamaPenerima, editNoPenerima, editAlamatPenerima, editDetailAlamatPenerima;
    ImageView backEditProfil;
    private static String url_update_data_penerima = "https://obats.000webhostapp.com/api/user/updatedatapenerima";
    Button SimpanProfil;
    String invoice, email, namaPenerimaEdit, handphonePenerimaEdit, alamatPenerimaEdit, detailPenerimaEdit;
    String NamaPenerimaResepBaru, NoPenerimaResepBaru,AlamatPenerimaResepBaru, DetailAlamatPenerimaResepBaru;
    String GambarResep, NamaPenerimaResep, NoPenerimaResep, AlamatPenerimaResep, DetailAlamatPenerimaResep, Invoice, Waktu, TotalHarga, Harga, Status, WaktuBayar, WaktuPengiriman;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_data_penerima_resep);

        backEditProfil = findViewById(R.id.iv_kembali_edit_data_resep);
        backEditProfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent (getApplicationContext(), DetailRiwayatResepActivity.class);
//                intent.putExtra("fragmentItem", 1);
                startActivity(intent);
                finish();

            }
        });

        SimpanProfil = findViewById(R.id.btn_simpan_edit_data_penerima_resep);
        SimpanProfil.setOnClickListener(v -> editData());

        email = SharedPreferenceManager.getStringPreferences(getApplicationContext(), "user_email");

        editNamaPenerima = findViewById(R.id.et_nama_penerima_resep_edit);
        editNoPenerima = findViewById(R.id.et_no_penerima_resep_edit);
        editAlamatPenerima = findViewById(R.id.et_alamat_penerima_resep_edit);
        editDetailAlamatPenerima = findViewById(R.id.et_det_alamat_penerima_resep_edit);

        Intent intent = getIntent();//diambil dari intent dari detailriwayatresep
        namaPenerimaEdit = intent.getStringExtra("nama_penerima");
        alamatPenerimaEdit = intent.getStringExtra("alamat");
        detailPenerimaEdit = intent.getStringExtra("detail_alamat");
        handphonePenerimaEdit = intent.getStringExtra("handphone");
        invoice = intent.getStringExtra("invoice");

        editNamaPenerima.setText(namaPenerimaEdit);
        editNoPenerima.setText(handphonePenerimaEdit);
        editAlamatPenerima.setText(alamatPenerimaEdit);
        editDetailAlamatPenerima.setText(detailPenerimaEdit);

    }

    private void editData() {

        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Update Data Penerima...");
        progressDialog.show();

        NamaPenerimaResepBaru = editNamaPenerima.getText().toString();
        NoPenerimaResepBaru = editNoPenerima.getText().toString();
        AlamatPenerimaResepBaru = editAlamatPenerima.getText().toString();
        DetailAlamatPenerimaResepBaru = editDetailAlamatPenerima.getText().toString();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url_update_data_penerima, response -> {
            Log.i("khatima", response);
            try {
                JSONObject objectResponse = new JSONObject(response);
                if (objectResponse.getString("respon").equals("berhasil")) {
                JSONArray array = objectResponse.getJSONArray("data");
                NamaPenerimaResep = array.getJSONObject(0).optString("nama_penerima"); //sesuai database
                NoPenerimaResep = array.getJSONObject(0).optString("handphone");
                AlamatPenerimaResep = array.getJSONObject(0).optString("alamat");
                DetailAlamatPenerimaResep = array.getJSONObject(0).optString("detail_alamat");
                Invoice = array.getJSONObject(0).optString("invoice");
                Waktu = array.getJSONObject(0).optString("waktu");
                TotalHarga = array.getJSONObject(0).optString("total_harga");
                Harga = array.getJSONObject(0).optString("harga");
                Status = array.getJSONObject(0).optString("status");
                WaktuBayar = array.getJSONObject(0).optString("waktu_bayar");
                WaktuPengiriman = array.getJSONObject(0).optString("waktu_pengiriman");
                GambarResep = array.getJSONObject(0).optString("gambar_resep");

                Intent intentUpdate = new Intent(getApplicationContext(), DetailRiwayatResepActivity.class);
                intentUpdate.putExtra("nama", NamaPenerimaResep); //sama dengan putExra di ftab1
                intentUpdate.putExtra("handphone", NoPenerimaResep);
                intentUpdate.putExtra("alamat", AlamatPenerimaResep);
                intentUpdate.putExtra("detail_alamat", DetailAlamatPenerimaResep);
                intentUpdate.putExtra("invoice", Invoice);
                intentUpdate.putExtra("waktu", Waktu);
                intentUpdate.putExtra("total_harga", TotalHarga);
                intentUpdate.putExtra("harga", Harga);
                intentUpdate.putExtra("status", Status);
                intentUpdate.putExtra("waktu_bayar", WaktuBayar);
                intentUpdate.putExtra("waktu_pengiriman", WaktuPengiriman);
                intentUpdate.putExtra("gambar", GambarResep);
                startActivity(intentUpdate);

                Toast.makeText(getApplicationContext(), "Berhasil", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    Toast.makeText(getApplicationContext(), "Gagal", Toast.LENGTH_SHORT).show();
                }
                progressDialog.dismiss();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }, error -> Log.i("Khusnul", String.valueOf(error))) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> param = new HashMap<>();
                param.put("invoice", invoice);
                param.put("nama_penerima", NamaPenerimaResepBaru);
                param.put("handphone", NoPenerimaResepBaru);
                param.put("alamat", AlamatPenerimaResepBaru);
                param.put("detail_alamat", DetailAlamatPenerimaResepBaru);
                return param;
            }
        };
        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        queue.add(stringRequest);
    }
}