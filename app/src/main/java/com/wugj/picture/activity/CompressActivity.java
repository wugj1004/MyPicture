package com.wugj.picture.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.wugj.picture.R;

/**
 * description:
 * </br>
 * author: wugj
 * </br>
 * date: 2018/9/19
 * </br>
 * version:
 */
public class CompressActivity  extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compress);

        findViewById(R.id.pic8).setOnClickListener(this);
        findViewById(R.id.pic9).setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.pic8:

                break;
            case R.id.pic9:

                break;
        }
    }
}
