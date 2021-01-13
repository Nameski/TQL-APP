package com.e.tql;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.icu.util.TimeZone;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import com.bumptech.glide.Glide;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import pub.devrel.easypermissions.EasyPermissions;


public class Transfer extends AppCompatActivity implements View.OnClickListener, EasyPermissions.PermissionCallbacks {
    private String[] permissions = {Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE};
    private File cameraSavePath;//拍照照片路径
    private Uri uri;//照片uri
    private ImageView image_show;
    public String photoPath = null;
    public boolean photoselected = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transfer);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide(); //隐藏标题栏
        }


        BottomMenuView bmv_list = (BottomMenuView) findViewById(R.id.bmv_list2);
        //设置bottom数据
        bmv_list.setBottomItem(getData());
        bmv_list.setBottomItemOnClickListener(new BottomMenuView.BottomItemOnClickListener() {
            @Override
            public void bottomItemOnClick(View view, int i, BottomItem item) {
                switch (i) {
                    case 0:
                        //跳转
                        Intent intent1_0 = new Intent(Transfer.this, Homepage.class);
                        startActivity(intent1_0);
                        finish();
                        overridePendingTransition(R.anim.activity_open, R.anim.activity_stay);
                        break;
                    case 2:
                        //跳转
                        break;
                    case 3:
                        //跳转
                        Intent intent1_3 = new Intent(Transfer.this, My.class);
                        startActivity(intent1_3);
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
        bmv_list.setShowIndex(1);

        image_show = findViewById(R.id.image_show2);

        DisplayMetrics mDisplayMetrics = getResources().getDisplayMetrics();
        int screenWidth = mDisplayMetrics.widthPixels;
        int screenHeight = mDisplayMetrics.heightPixels;

        String[] transfer_name = this.getResources().getStringArray(R.array.transfer_name);

        for (int i = 0; i < transfer_name.length; i++) {
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            );
            params.weight = 1;
            params.height = screenHeight / 10;
            params.width = screenWidth / 5;
            Button btn = new Button(this);
            btn.setId(i);
            int id = Transfer.getResId(transfer_name[i], R.drawable.class);
            btn.setBackgroundResource(id);
            LinearLayout linear = (LinearLayout) findViewById(R.id.btn_style);
            linear.addView(btn, params);

            btn.setOnClickListener(this);
        }

        Button btn_right = findViewById(R.id.toolbar_right_btn);
        Button btn_left = findViewById(R.id.toolbar_left_btn);
        Button save2 = findViewById(R.id.btn_save2);
        Button reset2 = findViewById(R.id.btn_reset2);
        btn_right.setOnClickListener(this);
        btn_left.setOnClickListener(this);
        save2.setOnClickListener(this);
        reset2.setOnClickListener(this);

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
            case R.id.btn_save2:
                if (photoselected) {
                    Bitmap bitmap = ((BitmapDrawable) image_show.getDrawable()).getBitmap();
                    saveToSystemGallery(this, bitmap);
                }
            case R.id.btn_reset2:
                if (photoselected) {
                    Glide.with(Transfer.this).load(photoPath).into(image_show);
                }

        }
    }

    public List<BottomItem> getData() {                                            //获取底部按钮名词和图标
        List<BottomItem> items = new ArrayList<>();
        items.add(new BottomItem("图库", R.mipmap.ic_tuku));
        items.add(new BottomItem("风格转换", R.mipmap.ic_transfer));
        items.add(new BottomItem("图图空间", R.mipmap.ic_space));
        items.add(new BottomItem("我的", R.mipmap.ic_my));
        return items;
    }

    public static int getResId(String variableName, Class<?> c) {                    //动态获取资源id
        try {
            Field idField = c.getDeclaredField(variableName);
            return idField.getInt(idField);
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }

    //获取权限
    private void getPermission() {                                                    //获取用户储存权限、相机权限
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
            uri = FileProvider.getUriForFile(Transfer.this, "com.e.tql.Homepage", cameraSavePath);
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        } else {
            uri = Uri.fromFile(cameraSavePath);
        }
        intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
        Transfer.this.startActivityForResult(intent, 1);
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
            Glide.with(Transfer.this).load(photoPath).into(image_show);
        } else if (requestCode == 2 && resultCode == RESULT_OK) {
            photoPath = getPhotoFromPhotoAlbum.getRealPathFromUri(this, data.getData());
            Glide.with(Transfer.this).load(photoPath).into(image_show);
        }

        super.onActivityResult(requestCode, resultCode, data);
        photoselected = true;
    }


}
