package com.example.mynotes;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

public class AnotherActivity extends AppCompatActivity {

    TextView mTitleTv, mDescTv, sSatuanTv;
    ImageView mImageIv;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_another);

        ActionBar actionBar = getSupportActionBar();


        mTitleTv = findViewById(R.id.title);
        mDescTv = findViewById(R.id.description);
        mImageIv = findViewById(R.id.imageView);
        sSatuanTv = findViewById(R.id.tv_satuan_obat);

        //now get our data from intent in which we put our data

        Intent intent = getIntent();

        String mTitle = intent.getStringExtra("iTitle");
        String mDescription = intent.getStringExtra("iDesc");
        String sSatuan = intent.getStringExtra("satuan");
        String gambar = intent.getStringExtra("gambar");

        byte[] mBytes = getIntent().getByteArrayExtra("iImage");
        //now decode image because from previous activity we get our image in bytes
        Bitmap bitmap = BitmapFactory.decodeByteArray(mBytes, 0, mBytes.length);

        actionBar.setTitle(mTitle); //which title we get from previous activity that will set in our action bar

        //now set our data in our view, which we get in our previous activity
        mTitleTv.setText(mTitle);
        mDescTv.setText(mDescription);
        sSatuanTv.setText(sSatuan);

        try {
            //mImageIv.setImageBitmap(bitmap);
            Glide.with(this).load(gambar).into(mImageIv);
        } catch (Exception e) {
            mImageIv.setImageResource(R.drawable.ic_drugs);
        }
    }
}
