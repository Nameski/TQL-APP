package com.e.tql;

import android.app.Activity;

import android.graphics.BitmapFactory;
import android.os.Bundle;

import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;

import com.isseiaoki.simplecropview.CropImageView;


public class Test extends Activity {

private CropImageView mCropView;
    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_test);

        final CropImageView cropImageView = (CropImageView)findViewById(R.id.cropImageView);
        final ImageView croppedImageView = (ImageView)findViewById(R.id.croppedImageView);
        // Set image for cropping
        cropImageView.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.bg));
        Button cropButton = (Button)findViewById(R.id.crop_button);
        cropButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get cropped image, and show result.
                croppedImageView.setImageBitmap(cropImageView.getCroppedBitmap());
            }
        });
    }

}
