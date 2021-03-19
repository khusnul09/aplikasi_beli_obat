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
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class KonfirmasiPembayaranResepActivity extends AppCompatActivity {

    private ImageView showGambar;
    private Button KirimBuktiPembayaran;
    final int CODE_GALLERY_RQEUEST = 999;
    Bitmap bitmap;
    LinearLayout llNprb, llMnra, llBtn;
    ImageView Kembali;
    TextView TotalBayar;
    String Total, Invoice;
    EditText etNprb, etRekening;
    String email_user, namagambar, narek, norek, waktuBayar;

    ProgressDialog progressDialog;

    String filePath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_konfirmasi_pembayaran_resep);

        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Loading...");
        progressDialog.setCancelable(false);

        showGambar = findViewById(R.id.showGambarResep);
        Button pilihGambarKonfirm = findViewById(R.id.btn_pilih_gambar_konfirm_resep);
        llBtn = findViewById(R.id.ll_mnra_resep);
        KirimBuktiPembayaran = findViewById(R.id.btn_kirim_bukti_pembayaran_resep);
        llNprb = findViewById(R.id.ll_nprb_resep);
        etNprb = findViewById(R.id.et_npdrb_resep);
        llMnra = findViewById(R.id.ll_mnrar);
        etRekening = findViewById(R.id.et_rekening_resep);

        email_user = SharedPreferenceManager.getStringPreferences(getApplicationContext(), "user_email");

        Kembali = findViewById(R.id.iv_back_konfirmasi_resep);
        Kembali.setOnClickListener(v -> onBackPressed());

        llNprb.setOnClickListener(v -> {
            llNprb.setVisibility(View.GONE);
            etNprb.setVisibility(View.VISIBLE);
        });
        llMnra.setOnClickListener(v -> {
            llMnra.setVisibility(View.GONE);
            etRekening.setVisibility(View.VISIBLE);
        });


        Calendar c = Calendar.getInstance();

        SimpleDateFormat formatnya = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        waktuBayar = formatnya.format(c.getTime());

        Total = getIntent().getStringExtra("Harga");
        Invoice = getIntent().getStringExtra("invoice");

        TotalBayar = findViewById(R.id.tv_nominal_totbyr_konfirmasi_resep);
//        TotalBayar.setText(Total);
        TotalBayar.setText(Rupiah.formatUangId(getApplicationContext(), Double.parseDouble(String.valueOf(Total))));

        ActivityCompat.requestPermissions(KonfirmasiPembayaranResepActivity.this,new
                String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA}, CODE_GALLERY_RQEUEST);

        pilihGambarKonfirm.setOnClickListener(v -> ImagePicker.Companion.with(this)
                //Crop image(Optional), Check Customization for more option
                .compress(1024)			//Final image size will be less than 1 MB(Optional)
                .maxResultSize(1080, 1080)	//Final image resolution will be less than 1080 x 1080(Optional)
                .start());

        KirimBuktiPembayaran.setOnClickListener(v -> uploadToCloudinary(filePath));
    }
    @Override
    protected void onActivityResult (int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data.getData() != null) {
            try {
                Uri path = data.getData();
                InputStream inputStream = getContentResolver().openInputStream(path);
                bitmap = BitmapFactory.decodeStream(inputStream);
                showGambar.setImageBitmap(bitmap);
                showGambar.setVisibility(View.VISIBLE);
                llBtn.setVisibility(View.VISIBLE);
                Toast.makeText(KonfirmasiPembayaranResepActivity.this, "Resep berhasil dipilih", Toast.LENGTH_SHORT).show();
            } catch (Exception e) {
                Toast.makeText(KonfirmasiPembayaranResepActivity.this, "Resep gagal dipilih, coba lagi", Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }

            //get the image's file location
            filePath = getRealPathFromUri(data.getData(), KonfirmasiPembayaranResepActivity.this);
        } else {
            Toast.makeText(this, "Tidak ada Foto dipilih", Toast.LENGTH_SHORT).show();
        }
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
        final String urlUploadBukti = "https://obats.000webhostapp.com/index.php/api/Upload_bukti";

        narek = etNprb.getText().toString();
        norek = etRekening.getText().toString();

        String tokenAdmin = SharedPreferenceManager.getStringPreferences(getApplicationContext(), "tokenadmin");

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
                            SharedPreferenceManager.saveBooleanPreferences(getApplicationContext(), "isInit", false);
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
                params.put("waktu_bayar", waktuBayar);
                params.put("token_tujuan", tokenAdmin);
                params.put("title", "Pembayaran Resep User");
                params.put("message", "User telah melakukan pembayaran");
                return params;
            }
        };
        RequestQueue queue = Volley.newRequestQueue(KonfirmasiPembayaranResepActivity.this);
        queue.add(request);
    }
}