package com.example.mynotes;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.cloudinary.android.MediaManager;
import com.cloudinary.android.callback.ErrorInfo;
import com.cloudinary.android.callback.UploadCallback;
import com.github.dhaval2404.imagepicker.ImagePicker;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public class KonfirmasiPembayaranResepActivity extends AppCompatActivity {

    private static final String urlUploadBukti = "https://obats.000webhostapp.com//api/user/uploadbukti";

    private ImageView showGambar;
    private Button PilihGambarKonfirm, KirimBuktiPembayaran;
    final int CODE_GALLERY_RQEUEST = 999;
    Bitmap bitmap;
    LinearLayout llNprb, llMnra;
    TextView TotalBayar;
    String Total, Invoice;
    EditText etNprb, etRekening;
    String email_user, namagambar, narek, norek;

    ProgressDialog progressDialog;

    String filePath;
    /*Map config = new HashMap();

    private void configCloudinary() {
        config.put("cloud_name", "beliobatid");
        config.put("api_key", "832196155542743");
        config.put("api_secret", "bwnHoGmtO2Li9tq42rDckhd_5BE");
        MediaManager.init(KonfirmasiPembayaranActivity.this, config);
    }*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_konfirmasi_pembayaran_resep);

        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Loading...");

        showGambar = findViewById(R.id.showGambarResep);
        PilihGambarKonfirm = findViewById(R.id.btn_pilih_gambar_konfirm_resep);
        KirimBuktiPembayaran = findViewById(R.id.btn_kirim_bukti_pembayaran_resep);
        llNprb = findViewById(R.id.ll_nprb_resep);
        etNprb = findViewById(R.id.et_npdrb_resep);
        llMnra = findViewById(R.id.ll_mnra_resep);
        etRekening = findViewById(R.id.et_rekening_resep);

        email_user = SharedPreferenceManager.getStringPreferences(getApplicationContext(), "user_email");

//        configCloudinary();

        llNprb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                llNprb.setVisibility(View.GONE);
                etNprb.setVisibility(View.VISIBLE);
            }
        });
        llMnra.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                llMnra.setVisibility(View.GONE);
                etRekening.setVisibility(View.VISIBLE);
            }
        });

        Total = getIntent().getStringExtra("Harga");
        Invoice = getIntent().getStringExtra("invoice");

        TotalBayar = findViewById(R.id.tv_nominal_totbyr_konfirmasi_resep);
        TotalBayar.setText(Total);

        ActivityCompat.requestPermissions(KonfirmasiPembayaranResepActivity.this,new
                String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA}, CODE_GALLERY_RQEUEST);

        PilihGambarKonfirm.setOnClickListener(v -> ImagePicker.Companion.with(this)
                //Crop image(Optional), Check Customization for more option
                .compress(1024)			//Final image size will be less than 1 MB(Optional)
                .maxResultSize(1080, 1080)	//Final image resolution will be less than 1080 x 1080(Optional)
                .start());

        KirimBuktiPembayaran.setOnClickListener(v -> uploadToCloudinary(filePath));
    }
    @Override
    protected void onActivityResult (int requestCode, int resultCode, @Nullable Intent data){
        try {
            Uri path = data.getData();
            InputStream inputStream = getContentResolver().openInputStream(path);
            bitmap = BitmapFactory.decodeStream(inputStream);
            showGambar.setImageBitmap(bitmap);
            showGambar.setVisibility(View.VISIBLE);
            KirimBuktiPembayaran.setVisibility(View.VISIBLE);
            Toast.makeText(KonfirmasiPembayaranResepActivity.this, "Resep berhasil dipilih", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Toast.makeText(KonfirmasiPembayaranResepActivity.this, "Resep gagal dipilih, coba lagi", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
        super.onActivityResult(requestCode, resultCode, data);

        //get the image's file location
        filePath = getRealPathFromUri(data.getData(), KonfirmasiPembayaranResepActivity.this);
    }

    private String getRealPathFromUri(Uri imageUri, Activity activity){
        @SuppressLint("Recycle") Cursor cursor = activity.getContentResolver().query(imageUri, null, null, null, null);

        if(cursor==null) {
            return imageUri.getPath();
        }else{
            cursor.moveToFirst();
            int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
            return cursor.getString(idx);
        }
    }

    private void uploadToCloudinary(String filePath) {
        progressDialog.show();

        Log.d("A", "sign up uploadToCloudinary- ");
        MediaManager.get().upload(filePath).callback(new UploadCallback() {
            @Override
            public void onStart(String requestId) {
                Toast.makeText(getApplicationContext(), "Mengunggah Gambar", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onProgress(String requestId, long bytes, long totalBytes) {
                Log.i("khatima", "mengirim");
            }

            @Override
            public void onSuccess(String requestId, Map resultData) {
                namagambar = resultData.get("url").toString();
                Log.i("khatima", namagambar);
                uploadBukti();
            }

            @Override
            public void onError(String requestId, ErrorInfo error) {
                Toast.makeText(getApplicationContext(), "Gagal!", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onReschedule(String requestId, ErrorInfo error) {
                Log.i("khatima", error.getDescription());
            }
        }).dispatch();
    }

    private void uploadBukti() {

        narek = etNprb.getText().toString();
        norek = etRekening.getText().toString();

        StringRequest request = new StringRequest(Request.Method.POST, urlUploadBukti,
                (Response.Listener<String>) response-> {
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        String respon = jsonObject.getString("respon");
                        Log.i("Khatima", respon);
                        if (jsonObject.getString("respon").equals("berhasil")) {
                            Intent alamatPasien = new Intent(KonfirmasiPembayaranResepActivity.this, RiwayatActivity.class);
                            alamatPasien.putExtra("fragmentItem", 0);
                            alamatPasien.putExtra("namagambar", namagambar);
                            startActivity(alamatPasien);
                            finish();
                        }
                        progressDialog.dismiss();
                        Toast.makeText(getApplicationContext(), respon, Toast.LENGTH_LONG).show();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }, (Response.ErrorListener) error-> Toast.makeText(getApplicationContext(), "error: "+ error.toString(), Toast.LENGTH_LONG).show()) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("invoice", Invoice);
                params.put("gambar", namagambar);
                params.put("status", "2");
                params.put("narek", narek);
                params.put("norek", norek);
                return params;
            }
        };
        RequestQueue queue = Volley.newRequestQueue(KonfirmasiPembayaranResepActivity.this);
        queue.add(request);
    }
}