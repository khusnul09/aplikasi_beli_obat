package com.example.mynotes;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import pl.aprilapps.easyphotopicker.EasyImage;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.io.File;

public class KonfirmasiPembayaranActivity extends AppCompatActivity {

    private ImageView showGambar;
    private Button PilihGambarKonfirm;
    LinearLayout llNprb, llMnra;
    EditText etNprb, etRekening;


    public static final int REQUEST_CODE_CAMERA=000;
    public static final int REQUEST_CODE_GALLERY=002;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_konfirmasi_pembayaran);
        showGambar = findViewById(R.id.showGambar);
        PilihGambarKonfirm = findViewById(R.id.btn_pilih_gambar_konfirm);
        llNprb = findViewById(R.id.ll_nprb);
        etNprb = findViewById(R.id.et_npdrb);
        llMnra = findViewById(R.id.ll_mnra);
        etRekening = findViewById(R.id.et_rekening);
        PilihGambarKonfirm.setOnClickListener((new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setRequestImage();
            }
        }));
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
    }
    private void setRequestImage(){
        CharSequence[] item = {"Kamera","Galeri"};
        AlertDialog.Builder request = new AlertDialog.Builder(this)
                .setTitle("Add Image")
                .setItems(item, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        switch (i){
                            case 0:
                                EasyImage.openCamera(KonfirmasiPembayaranActivity.this,REQUEST_CODE_CAMERA);
                                break;
                            case 1:
                                EasyImage.openGallery(KonfirmasiPembayaranActivity.this,REQUEST_CODE_GALLERY);
                                break;
                        }

                    }
                });
        request.create();
        request.show();
    }
    @Override
    protected void onActivityResult (int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode,resultCode,data);
        EasyImage.handleActivityResult(requestCode, resultCode, data, this, new EasyImage.Callbacks() {
            @Override
            public void onImagePickerError(Exception e, EasyImage.ImageSource source, int type) {

            }

            @Override
            public void onImagePicked(File imageFile, EasyImage.ImageSource source, int type) {
                switch (type){
                    case REQUEST_CODE_CAMERA:
                        Glide.with(KonfirmasiPembayaranActivity.this)
                                .load (imageFile)
                                .diskCacheStrategy(DiskCacheStrategy.ALL)
                                .into (showGambar);
                        break;
                    case REQUEST_CODE_GALLERY:
                        Glide.with(KonfirmasiPembayaranActivity.this)
                                .load(imageFile)
                                .diskCacheStrategy(DiskCacheStrategy.ALL)
                                .into (showGambar);
                        break;
                }

            }

            @Override
            public void onCanceled(EasyImage.ImageSource source, int type) {

            }
        });
    }
}