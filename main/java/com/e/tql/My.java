package com.e.tql;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.List;

public class My extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide(); //隐藏标题栏
        }


        BottomMenuView bmv_list = (BottomMenuView) findViewById(R.id.bmv_list_my);
        //设置bottom数据
        bmv_list.setBottomItem(getData());
        bmv_list.setBottomItemOnClickListener(new BottomMenuView.BottomItemOnClickListener() {
            @Override
            public void bottomItemOnClick(View view, int i, BottomItem item) {
                switch (i) {
                    case 0:
                        //跳转
                        Intent intent3_0 = new Intent(My.this, Homepage.class);
                        startActivity(intent3_0);
                        finish();
                        overridePendingTransition(R.anim.activity_open, R.anim.activity_stay);
                        break;
                    case 1:
                        //跳转
                        Intent intent3_1 = new Intent(My.this, Transfer.class);
                        startActivity(intent3_1);
                        finish();
                        overridePendingTransition(R.anim.activity_open, R.anim.activity_stay);
                        break;
                    case 2:
                        //跳转
                        break;
                    default:
                        //不响应
                        break;
                }
            }
        });
        //默认选择第几个
        bmv_list.setShowIndex(3);


        Button intent_login = findViewById(R.id.intent_login);
        intent_login.setOnClickListener(this);
    }

    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.intent_login:
                Intent intent = new Intent(My.this, Login.class);
                startActivity(intent);
                finish();
                overridePendingTransition(R.anim.activity_open, R.anim.activity_stay);
                break;

        }
    }

    public List<BottomItem> getData() {
        List<BottomItem> items = new ArrayList<>();
        items.add(new BottomItem("图库", R.mipmap.ic_tuku));
        items.add(new BottomItem("风格转换", R.mipmap.ic_transfer));
        items.add(new BottomItem("图图空间", R.mipmap.ic_space));
        items.add(new BottomItem("我的", R.mipmap.ic_my));
        return items;
    }
}