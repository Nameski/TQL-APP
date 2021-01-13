package com.e.tql;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

public class Landpage extends AppCompatActivity {
    private TextView tv1;
    Button button = null;

    @Override

    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_landpage);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide(); //隐藏标题栏
        }
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        tv1 = (TextView) findViewById(R.id.count);
        handler.post(waitSendsRunnable);
        button = (Button) findViewById(R.id.count);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handler.removeCallbacks(waitSendsRunnable);
                Intent intent = new Intent(Landpage.this, Login.class);
                startActivity(intent);
                finish();
                overridePendingTransition(R.anim.activity_open, R.anim.activity_stay);
            }
        });
    }


    //启用一个Handler
    Handler handler = new Handler() {

        @SuppressLint("HandlerLeak")
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    Intent intent = new Intent(Landpage.this, Login.class);
                    startActivity(intent);
                    finish();
                    overridePendingTransition(R.anim.activity_open, R.anim.activity_stay);
                    break;
                case 1:
                    tv1.setText("继续（" + index + "s）");
                    break;
                default:
                    break;
            }
        }
    };


    int index = 5;
    Runnable waitSendsRunnable = new Runnable() {

        public void run() {
            if (index > 0) {
                index--;
                try {
                    Thread.sleep(1000);
                    handler.sendEmptyMessage(1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                handler.post(waitSendsRunnable);
            } else {
                try {
                    Thread.sleep(1000);
                    handler.sendEmptyMessage(0);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }

    };
}


