package com.example.mynotes;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class InfoPembayaranResepActivity extends AppCompatActivity {

    private static final String urlUpdateStatus = "https://obats.000webhostapp.com//api/user/updatestatusresi";

    TextView NomorRekening, tvHarga;
    ImageView Copy, Kembali;
    TextView TotalBayar;
    Button UploadNantiResep, UploadSekarang;
    String Harga, invoice, setStatus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info_pembayaran_resep);

        Intent intent = getIntent();
        Harga = intent.getStringExtra("harganya");
        invoice = intent.getStringExtra("invoice");

        tvHarga = findViewById(R.id.tv_nominal_pembayaran_resep);
        tvHarga.setText(Rupiah.formatUangId(getApplicationContext(), Double.parseDouble(String.valueOf(Harga))));
        Log.i("khatima", Harga);

        NomorRekening = findViewById(R.id.tv_no_rekening_resep);
        Copy = findViewById(R.id.iv_copy_resep);
        Copy.setOnClickListener(v -> {
            ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
            ClipData clip = ClipData.newPlainText("simple text", NomorRekening.getText());
            clipboard.setPrimaryClip(clip);

            Toast.makeText(InfoPembayaranResepActivity.this, "Nomor rekening berhasil disalin", Toast.LENGTH_SHORT).show();

        });

        Kembali = findViewById(R.id.iv_kembali_det_resep);
        Kembali.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { ;
                onBackPressed();
            }
        });

        UploadSekarang = findViewById(R.id.btn_upload_sekarang_resep);
        UploadSekarang.setOnClickListener(v -> {
            Intent intentUploadSekarang = new Intent(getApplicationContext(), KonfirmasiPembayaranResepActivity.class);
            intentUploadSekarang.putExtra("invoice", invoice);
            intentUploadSekarang.putExtra("Harga", Harga);
            intentUploadSekarang.putExtra("invoice", invoice);
            startActivity(intentUploadSekarang);
        });

        UploadNantiResep = findViewById(R.id.btn_upload_nanti_resep);
        UploadNantiResep.setOnClickListener(v -> {
            setStatus = "1";
            updateStatus();
        });

        TotalBayar = findViewById(R.id.tv_nominal_pembayaran_resep);

        TotalBayar.setText(Rupiah.formatUangId(getApplicationContext(), Double.parseDouble(String.valueOf(Harga))));
    }
//Apabila sebelum back harus memilih upload sekarang atau upload nanti. Tidak dapat back apabila tdk memilih salah satunya
//    @Override
//    public void onBackPressed() {
//        Toast.makeText(getApplicationContext(), "Selesaikan metode pembayaran lebih dahulu", Toast.LENGTH_SHORT).show();
//    }

    private void updateStatus() {
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Loading...");
        progressDialog.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, urlUpdateStatus,
                (Response.Listener<String>) response -> {
                    Log.i("khatima", response);
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        String respon = jsonObject.getString("text");
                        Log.i("Khatima", respon);
                        if (jsonObject.getString("text").equals("berhasil")) {
                            Intent intentUploadNantiResep = new Intent(getApplicationContext(), RiwayatActivity.class); //pindah
                            intentUploadNantiResep.putExtra("fragmentItem", 0);
                            startActivity(intentUploadNantiResep);
                            finish();
                        }
                        Toast.makeText(getApplicationContext(), respon, Toast.LENGTH_LONG).show();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    progressDialog.dismiss();
                }, (Response.ErrorListener) error -> Toast.makeText(getApplicationContext(), "error: "+ error.toString(), Toast.LENGTH_LONG).show()) {
            @Override
            protected Map<String, String> getParams(){
                Map<String, String> params = new HashMap<>();
                params.put("invoice", invoice);
                params.put("status", setStatus);
                params.put("Harga", Harga);
                return  params;

            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(InfoPembayaranResepActivity.this);
        requestQueue.add(stringRequest);
    }
}