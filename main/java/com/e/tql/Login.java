package com.e.tql;

import android.content.Intent;
import android.os.Bundle;

import android.util.DisplayMetrics;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.TranslateAnimation;

import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

public class Login extends AppCompatActivity implements View.OnClickListener {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide(); //隐藏标题栏
        }

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        ImageView bg_round = (ImageView) findViewById(R.id.bg_round);
        ImageView char_tql = (ImageView) findViewById(R.id.char_tql);
        EditText phone_num = (EditText) findViewById(R.id.phone_num);
        EditText psw = (EditText) findViewById(R.id.psw);
        Button register = (Button) findViewById(R.id.register);
        Button login = (Button) findViewById(R.id.login);
        Button direct_in = (Button) findViewById(R.id.direct_in);


        DisplayMetrics mDisplayMetrics = getResources().getDisplayMetrics(); //获取屏幕高宽
        int screenWidth = mDisplayMetrics.widthPixels;
        int screenHeight = mDisplayMetrics.heightPixels;

        int[] location_round = new int[2];                   //获取view在window中的位置
        bg_round.getLocationOnScreen(location_round);

        int[] location_char = new int[2];
        char_tql.getLocationOnScreen(location_char);

        Animation animation = AnimationUtils.loadAnimation(this, R.anim.gradually);//编辑框淡入
        psw.startAnimation(animation);
        phone_num.startAnimation(animation);
        register.startAnimation(animation);
        login.startAnimation(animation);
        direct_in.startAnimation(animation);


        TranslateAnimation animation_round = new TranslateAnimation(location_round[0], location_round[0], (float) 0.2 * screenHeight, location_round[1]);//图片移动
        TranslateAnimation animation_char = new TranslateAnimation(location_char[0], location_char[0], (float) -0.2 * screenHeight, location_char[1]);

        animation_round.setRepeatCount(0);
        animation_round.setDuration(800);
        bg_round.setAnimation(animation_round);

        animation_char.setRepeatCount(0);
        animation_char.setDuration(800);
        char_tql.setAnimation(animation_char);

        register.setOnClickListener(this);
        login.setOnClickListener(this);
        direct_in.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.register:

            case R.id.login:

            case R.id.direct_in:
                Intent intent = new Intent(Login.this, Homepage.class);
                startActivity(intent);
                finish();
            default:
        }
    }

}