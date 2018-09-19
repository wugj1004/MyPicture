package com.wugj.picture.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.wugj.picture.R;

/**
 * 照片裁剪
 */
public class CropActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crop);

        findViewById(R.id.pic5).setOnClickListener(this);
        findViewById(R.id.pic6).setOnClickListener(this);
        findViewById(R.id.pic7).setOnClickListener(this);


    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.pic5:

                break;
            case R.id.pic6:

                break;
            case R.id.pic7:

                break;
        }
    }
}
