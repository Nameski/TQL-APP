package com.e.tql;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.PixelFormat;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import pub.devrel.easypermissions.EasyPermissions;


public class Homepage extends AppCompatActivity implements View.OnClickListener, EasyPermissions.PermissionCallbacks {
    private String[] permissions = {Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE};
    private File cameraSavePath;//拍照照片路径
    private Uri uri;//照片uri
    private ImageView image_show;
    public String photoPath = null;
    public boolean photoselected = false;
    private Bitmap re_bitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homepage);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide(); //隐藏标题栏
        }


        image_show = findViewById(R.id.image_show1);

        Intent intent = getIntent();
        if (intent.getByteArrayExtra("PFC") != null) {
            byte buff[] = intent.getByteArrayExtra("PFC");
            re_bitmap = BitmapFactory.decodeByteArray(buff, 0, buff.length);
            image_show.setImageBitmap(re_bitmap);
            photoselected = true;
        }
        else{
            image_show.setImageResource(R.drawable.intro);
        }


        BottomMenuView bmv_list = (BottomMenuView) findViewById(R.id.bmv_list1);
        //设置bottom数据
        bmv_list.setBottomItem(getData());
        bmv_list.setBottomItemOnClickListener(new BottomMenuView.BottomItemOnClickListener() {
            @Override
            public void bottomItemOnClick(View view, int i, BottomItem item) {
                switch (i) {
                    case 1:
                        //跳转
                        Intent intent0_1 = new Intent(Homepage.this, Transfer.class);
                        startActivity(intent0_1);
                        finish();
                        overridePendingTransition(R.anim.activity_open, R.anim.activity_stay);
                        break;
                    case 2:
                        //跳转
                        break;
                    case 3:
                        //跳转
                        Intent intent0_3 = new Intent(Homepage.this, My.class);
                        startActivity(intent0_3);
                        finish();
                        overridePendingTransition(R.anim.activity_open, R.anim.activity_stay);
                        break;
                    default:
                        //不响应
                        break;
                }
            }
        });
        //默认选择第几个
        bmv_list.setShowIndex(0);


        Button btn_right = findViewById(R.id.toolbar_right_btn);
        Button btn_left = findViewById(R.id.toolbar_left_btn);
        btn_right.setOnClickListener(this);
        btn_left.setOnClickListener(this);

        Button save1 = findViewById(R.id.btn_save1);
        Button reset1 = findViewById(R.id.btn_reset1);
        save1.setOnClickListener(this);
        reset1.setOnClickListener(this);

        image_show.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.toolbar_right_btn:
                if (!EasyPermissions.hasPermissions(this, permissions))
                    getPermission();
                goCamera();
                break;
            case R.id.toolbar_left_btn:
                goPhotoAlbum();
                break;
            case R.id.btn_save1:
                if (photoselected) {
                    Bitmap bitmap = ((BitmapDrawable) image_show.getDrawable()).getBitmap();
                    saveToSystemGallery(this, bitmap);
                }
                break;
            case R.id.btn_reset1:
                if (photoselected) {
                    Glide.with(Homepage.this).load(photoPath).into(image_show);
                }
                break;
            case R.id.image_show1:
                if (photoselected) {
                    Intent intent = new Intent(this, Cut.class);

                    image_show.setDrawingCacheEnabled(true);
                    Bitmap pfh = Bitmap.createBitmap(image_show.getDrawingCache());
                    image_show.setDrawingCacheEnabled(false);

                    byte[] PFH = Bitmap2Bytes(pfh);

                    intent.putExtra("PFH", PFH);
                    startActivity(intent);
                    finish();
                    break;
                }
        }
    }

    private byte[] Bitmap2Bytes(Bitmap bm) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.PNG, 100, baos);
        return baos.toByteArray();
    }


    public List<BottomItem> getData() {
        List<BottomItem> items = new ArrayList<>();
        items.add(new BottomItem("图库", R.mipmap.ic_tuku));
        items.add(new BottomItem("风格转换", R.mipmap.ic_transfer));
        items.add(new BottomItem("图图空间", R.mipmap.ic_space));
        items.add(new BottomItem("我的", R.mipmap.ic_my));
        return items;
    }

    public static void saveToSystemGallery(Context context, Bitmap bmp) {
        // 首先保存图片
        File appDir = new File("sdcard/TQL");
        if (!appDir.exists()) {
            appDir.mkdir();
        }
        String fileName = System.currentTimeMillis() + ".jpg";
        File file = new File(appDir, fileName);
        try {
            FileOutputStream fos = new FileOutputStream(file);
            bmp.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.flush();
            fos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // 其次把文件插入到系统图库
        try {
            MediaStore.Images.Media.insertImage(context.getContentResolver(),
                    file.getAbsolutePath(), fileName, null);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        // 最后通知图库更新
        context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.parse(file.getAbsolutePath())));
    }

    //获取权限
    private void getPermission() {
        if (EasyPermissions.hasPermissions(this, permissions)) {
            //已经打开权限
            Toast.makeText(this, "已经申请相关权限", Toast.LENGTH_SHORT).show();
        } else {
            //没有打开相关权限、申请权限
            EasyPermissions.requestPermissions(this, "需要获取您的相册、照相使用权限", 1, permissions);
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        //框架要求必须这么写
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }


    //成功打开权限
    @Override
    public void onPermissionsGranted(int requestCode, @NonNull List<String> perms) {

        Toast.makeText(this, "相关权限获取成功", Toast.LENGTH_SHORT).show();
    }

    //用户未同意权限
    @Override
    public void onPermissionsDenied(int requestCode, @NonNull List<String> perms) {
        Toast.makeText(this, "请同意相关权限，否则功能无法使用", Toast.LENGTH_SHORT).show();
    }


    //激活相册操作
    private void goPhotoAlbum() {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, 2);
    }

    //激活相机操作
    private void goCamera() {
        cameraSavePath = new File(Environment.getExternalStorageDirectory().getPath() + "/" + System.currentTimeMillis() + ".jpg");


        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            //第二个参数为 包名.fileprovider
            uri = FileProvider.getUriForFile(Homepage.this, "com.e.tql.Homepage", cameraSavePath);
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        } else {
            uri = Uri.fromFile(cameraSavePath);
        }
        intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
        Homepage.this.startActivityForResult(intent, 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1 && resultCode == RESULT_OK) {

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                photoPath = String.valueOf(cameraSavePath);
            } else {
                photoPath = uri.getEncodedPath();
            }
            Log.d("拍照返回图片路径:", photoPath);
            Glide.with(Homepage.this).load(photoPath).into(image_show);
        } else if (requestCode == 2 && resultCode == RESULT_OK) {
            photoPath = getPhotoFromPhotoAlbum.getRealPathFromUri(this, data.getData());
            Glide.with(Homepage.this).load(photoPath).into(image_show);
        }

        super.onActivityResult(requestCode, resultCode, data);
        photoselected = true;
    }


}
