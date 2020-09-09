package com.example.mynotes;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import pl.aprilapps.easyphotopicker.EasyImage;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.io.File;

public class PesanDenganResepActivity extends AppCompatActivity {
    private ImageView setImage;
    private ImageView OpenImage;

    public static final int REQUEST_CODE_CAMERA=000;
    public static final int REQUEST_CODE_GALLERY=002;

    public Button kirimBtn;
    public ImageView openImageBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pesan_dengan_resep);
        setImage = findViewById(R.id.showImg);
        OpenImage = findViewById(R.id.open_image);
        OpenImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setRequestImage();

            }
        });
        kirimBtn = findViewById(R.id.kirimBtn); //memunculkan tombol kirim setelah gambar muncul
        kirimBtn.setVisibility(View.INVISIBLE); //memunculkan tombol kirim.............
        openImageBtn = findViewById(R.id.open_image);
    }
    public void Kirim (View view) {
        Intent intent = new Intent(PesanDenganResepActivity.this, AlamatPasienActivity.class);
        startActivity(intent);

    }

    private void setRequestImage() {
        CharSequence[] item = {"Kamera", "Galeri"};
        AlertDialog.Builder request = new AlertDialog.Builder(this)
                .setTitle("Add Image")
                .setItems(item, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        switch (i) {
                            case 0:
                                EasyImage.openCamera(PesanDenganResepActivity.this, REQUEST_CODE_CAMERA);
                                break;
                            case 1:
                                EasyImage.openGallery(PesanDenganResepActivity.this, REQUEST_CODE_GALLERY);
                                break;
                        }

                    }
                });
        request.create();
        request.show();
    }
    @Override
    protected void onActivityResult (int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        EasyImage.handleActivityResult(requestCode, resultCode, data, this, new EasyImage.Callbacks() {
            @Override
            public void onImagePickerError(Exception e, EasyImage.ImageSource source, int type) {
            }
            @Override
            public void onImagePicked(File imageFile, EasyImage.ImageSource source, int type) {
                kirimBtn.setVisibility(View.VISIBLE); //mengganti open image bila gambar sudah muncul
                // openImageBtn.setText("Ambil Lagi"); // mengganti open image bila gambar sudah muncul
                Glide.with(PesanDenganResepActivity.this)
                        .load(imageFile)
                        .fitCenter()
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .into(setImage);
            }

            @Override
            public void onCanceled (EasyImage.ImageSource source, int type) {

            }
        });
    }
}
