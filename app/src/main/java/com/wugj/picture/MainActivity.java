package com.wugj.picture;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.wugj.picture.activity.CameraPicActivity;
import com.wugj.picture.activity.CompressActivity;
import com.wugj.picture.activity.CropActivity;
import com.wugj.picture.activity.PhotoCheckActivity;
import com.wugj.picture.activity.PhotoSelectActivity;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private MainActivity instance;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        instance = this;

        findViewById(R.id.pic1).setOnClickListener(this);
        findViewById(R.id.pic3).setOnClickListener(this);
        findViewById(R.id.pic4).setOnClickListener(this);
        findViewById(R.id.pic6).setOnClickListener(this);
        findViewById(R.id.pic8).setOnClickListener(this);


    }

    @Override
    public void onClick(View view) {
        Intent intent = new Intent();
        switch (view.getId()) {

            case R.id.pic1:
                intent.setClass(instance, CameraPicActivity.class);

                break;
            case R.id.pic3:
                intent.setClass(instance, PhotoSelectActivity.class);

                break;
            case R.id.pic4:
                intent.setClass(instance, PhotoCheckActivity.class);

                break;
            case R.id.pic6:
                intent.setClass(instance, CropActivity.class);

                break;
            case R.id.pic8:
                intent.setClass(instance, CompressActivity.class);

                break;
        }
        startActivity(intent);
    }
}
