package com.example.mynotes;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.github.dhaval2404.imagepicker.ImagePicker;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public class PesanDenganResepActivity extends AppCompatActivity {

    public Button btnKirimResep, btnPilihGambar;
    public ImageView ivShowImage;
    final int CODE_GALLERY_RQEUEST = 999;
    String url = "https://obats.000webhostapp.com//api/user/addimage";
    Bitmap bitmap;
    String email_user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pesan_dengan_resep);

        email_user = SharedPreferenceManager.getStringPreferences(getApplicationContext(), "user_email");

        btnPilihGambar = findViewById(R.id.btn_pilih_gambar);
        btnKirimResep = findViewById(R.id.btn_lanjutkan);
        ivShowImage = findViewById(R.id.showImg);

        ActivityCompat.requestPermissions(PesanDenganResepActivity.this,new
                String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA}, CODE_GALLERY_RQEUEST);

        btnPilihGambar.setOnClickListener(v -> {
            ImagePicker.Companion.with(this)
                    //Crop image(Optional), Check Customization for more option
                    .compress(1024)			//Final image size will be less than 1 MB(Optional)
                    .maxResultSize(1080, 1080)	//Final image resolution will be less than 1080 x 1080(Optional)
                    .start();
        });

        btnKirimResep.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadImage();
            }
        });
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
    }
    private void uploadImage() {
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Mengirim resep...");
        progressDialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.i("khatima", response);
                        //Toast.makeText(getApplicationContext(), response, Toast.LENGTH_SHORT).show();
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String respon = jsonObject.getString("response");
                            Log.i("Khatima", respon);
                            if (jsonObject.getString("response").equals("berhasil")) {
                                String invoice = jsonObject.getString("invoice");
                                Intent alamatPasien = new Intent(PesanDenganResepActivity.this, AlamatPasienActivity.class);
                                alamatPasien.putExtra("invoice", invoice);
                                startActivity(alamatPasien);
                            }
                            Toast.makeText(getApplicationContext(), respon, Toast.LENGTH_LONG).show();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        progressDialog.dismiss();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), "error: "+ error.toString(), Toast.LENGTH_LONG).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams(){
                Map<String, String> params = new HashMap<>();
                String gambar = imagetoString(bitmap);
                params.put("invoice","");
                params.put("nama_user",email_user);
                params.put("nama_penerima","");
                params.put("handphone","0");
                params.put("alamat","");
                params.put("detail_alamat","");
                params.put("gambar_resep",gambar);
                return  params;

            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(PesanDenganResepActivity.this);
        requestQueue.add(stringRequest);
    }
    private String imagetoString (Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
        byte[] imageType = byteArrayOutputStream.toByteArray();
        return Base64.encodeToString(imageType, Base64.DEFAULT);
    }
}
