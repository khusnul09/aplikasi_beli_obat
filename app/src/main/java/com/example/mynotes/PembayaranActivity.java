package com.example.mynotes;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
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

public class PembayaranActivity extends AppCompatActivity {

    private static final String urlUpdateStatus = "https://obats.000webhostapp.com//api/user/updatestatusresi";

    Button UploadSekarang, UploadNanti;
    TextView NomorRekening;
    ImageView Copy ;
    TextView TotalBayar;
    String invoice, setStatus, Total;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pembayaran);

        Intent intent = getIntent();

        Total = intent.getStringExtra("Total");
        Log.d("khatimatotal", Total);
        invoice = intent.getStringExtra("invoice");

        UploadSekarang = findViewById(R.id.btn_upload_sekarang);
        UploadSekarang.setOnClickListener(v -> {
            Intent intentUploadSekarang = new Intent(getApplicationContext(), KonfirmasiPembayaranActivity.class);
            intentUploadSekarang.putExtra("invoice", invoice);
            intentUploadSekarang.putExtra("Total", Total);
            intentUploadSekarang.putExtra("invoice", invoice);
            startActivity(intentUploadSekarang);
        });

        UploadNanti = findViewById(R.id.btn_upload_nanti);
        UploadNanti.setOnClickListener(v -> {
            setStatus = "1";
            updateStatus();
        });

        NomorRekening = findViewById(R.id.tv_no_rekening);
        Copy = findViewById(R.id.iv_copy);
        Copy.setOnClickListener(v -> {
            ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
            ClipData clip = ClipData.newPlainText("simple text", NomorRekening.getText());
            clipboard.setPrimaryClip(clip);

            Toast.makeText(PembayaranActivity.this, "Nomor rekening berhasil disalin", Toast.LENGTH_SHORT).show();

        });


        TotalBayar = findViewById(R.id.tv_total_pembayaran_info);

//        TotalBayar.setText(Total);
        TotalBayar.setText(Rupiah.formatUangId(getApplicationContext(), Double.parseDouble(String.valueOf(Total))));

    }

    @Override
    public void onBackPressed() {
        Toast.makeText(getApplicationContext(), "Selesaikan metode pembayaran lebih dahulu", Toast.LENGTH_SHORT).show();
    }

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
                            Intent intentUploadNanti = new Intent(getApplicationContext(), RiwayatActivity.class); //pindah
                            intentUploadNanti.putExtra("fragmentItem", 1);
                            startActivity(intentUploadNanti);
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
                params.put("Total", Total);
                return  params;

            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(PembayaranActivity.this);
        requestQueue.add(stringRequest);
    }
}