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
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.cloudinary.Cloudinary;
import com.cloudinary.android.MediaManager;
import com.cloudinary.android.callback.ErrorInfo;
import com.cloudinary.android.callback.UploadCallback;
import com.cloudinary.utils.ObjectUtils;
import com.github.dhaval2404.imagepicker.ImagePicker;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public class PesanDenganResepActivity extends AppCompatActivity {

    public Button btnKirimResep, btnPilihGambar;
    public ImageView ivShowImage;
    final int CODE_GALLERY_RQEUEST = 999;
    ImageView backPesanResep;
    Bitmap bitmap;
    String email_user, namagambar;

    ProgressDialog progressDialog;

    String filePath;
    /*Map config = new HashMap();

    private void configCloudinary() {
        config.put("cloud_name", "beliobatid");
        config.put("api_key", "832196155542743");
        config.put("api_secret", "bwnHoGmtO2Li9tq42rDckhd_5BE");
        MediaManager.init(PesanDenganResepActivity.this, config);
    }*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pesan_dengan_resep);

        email_user = SharedPreferenceManager.getStringPreferences(getApplicationContext(), "user_email");

        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Loading...");
        progressDialog.setCancelable(false);

        btnPilihGambar = findViewById(R.id.btn_pilih_gambar);
        btnKirimResep = findViewById(R.id.btn_lanjutkan);
        ivShowImage = findViewById(R.id.showImg);

        ActivityCompat.requestPermissions(PesanDenganResepActivity.this,new
                String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA}, CODE_GALLERY_RQEUEST);

        btnPilihGambar.setOnClickListener(v -> ImagePicker.Companion.with(this)
                //Crop image(Optional), Check Customization for more option
                .compress(1024)			//Final image size will be less than 1 MB(Optional)
                .maxResultSize(1080, 1080)	//Final image resolution will be less than 1080 x 1080(Optional)
                .start());

        backPesanResep = findViewById(R.id.iv_kembali5);
        backPesanResep.setOnClickListener(v -> {
            Intent intent = new Intent (getApplicationContext(), PesanObatActivity.class);
            startActivity(intent);

        });

        btnKirimResep.setOnClickListener(v -> uploadToCloudinary(filePath));
    }

    @Override
    protected void onActivityResult (int requestCode, int resultCode, @Nullable Intent data){
            try {
                Uri path = data.getData();
                InputStream inputStream = getContentResolver().openInputStream(path);
                bitmap = BitmapFactory.decodeStream(inputStream);
                ivShowImage.setImageBitmap(bitmap);
                ivShowImage.setVisibility(View.VISIBLE);
                btnKirimResep.setVisibility(View.VISIBLE);
                Toast.makeText(PesanDenganResepActivity.this, "Resep berhasil dipilih", Toast.LENGTH_SHORT).show();
            } catch (Exception e) {
                Toast.makeText(PesanDenganResepActivity.this, "Resep gagal dipilih, coba lagi", Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }
        super.onActivityResult(requestCode, resultCode, data);

        //get the image's file location
        filePath = getRealPathFromUri(data.getData(), PesanDenganResepActivity.this);
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
                Intent alamatPasien = new Intent(PesanDenganResepActivity.this, AlamatPasienActivity.class);
                alamatPasien.putExtra("namagambar", namagambar);
                startActivity(alamatPasien);
                progressDialog.dismiss();
            }

            @Override
            public void onError(String requestId, ErrorInfo error) {
                Toast.makeText(getApplicationContext(), "Gagal!", Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            }

            @Override
            public void onReschedule(String requestId, ErrorInfo error) {
                Log.i("khatima", error.getDescription());
                progressDialog.dismiss();
            }
        }).dispatch();
    }
}
