package com.e.tql;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.isseiaoki.simplecropview.CropImageView;

import java.io.ByteArrayOutputStream;

public class Cut extends AppCompatActivity implements View.OnClickListener {
    private Bitmap bitmap;
    private ImageView croppedImageView;
    private CropImageView cropImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cut);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide(); //隐藏标题栏
        }

        croppedImageView = (ImageView) findViewById(R.id.croppedImageView);
        cropImageView = (CropImageView) findViewById(R.id.cropImageView);

        Intent intent = getIntent();
        if (intent != null) {
            byte buff[]=intent.getByteArrayExtra("PFH");
            bitmap = BitmapFactory.decodeByteArray(buff, 0, buff.length);

            cropImageView.setImageBitmap(bitmap);
        }

        Button cropButton = (Button) findViewById(R.id.crop_button);
        cropButton.setOnClickListener(this);

    }


    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.crop_button:
                croppedImageView.setImageBitmap(cropImageView.getCroppedBitmap());

                Intent intent_back = new Intent(this, Homepage.class);

                croppedImageView.setDrawingCacheEnabled(true);
                Bitmap pfc = Bitmap.createBitmap(croppedImageView.getDrawingCache());
                croppedImageView.setDrawingCacheEnabled(false);

                byte[] PFC = Bitmap2Bytes(pfc);

                intent_back.putExtra("PFC", PFC);
                startActivity(intent_back);
                finish();
                break;
            default:
                break;

        }
    }
    private byte[] Bitmap2Bytes(Bitmap bm){
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.PNG, 100, baos);
        return baos.toByteArray();
    }

}