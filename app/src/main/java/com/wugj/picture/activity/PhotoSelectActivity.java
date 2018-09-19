package com.wugj.picture.activity;

import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;

import com.wugj.picture.R;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

/**
 * 照片单选
 */
public class PhotoSelectActivity extends AppCompatActivity {

    private static final int IMAGE_REQUEST_CODE = 2;// 相册

    private ImageView iv1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_select);
        iv1 = findViewById(R.id.iv1);
        findViewById(R.id.bt1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(
                        Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                // 打开手机相册,设置请求码
                startActivityForResult(intent, IMAGE_REQUEST_CODE);
            }
        });

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == IMAGE_REQUEST_CODE) {
            try {
                Uri selectedUri = data.getData(); //获取系统返回的照片的Uri
                //通过Uri获取bitmap设置图片
                Bitmap bit = getBitmap(this.getContentResolver(),selectedUri);
                iv1.setImageBitmap(bit);

                //通过Uri获取图片
                /*iv1.setImageURI(selectedUri);*/

            } catch (Exception e) {
                // TODO Auto-generatedcatch block
                e.printStackTrace();
            }

        }
    }




    public static final Bitmap getBitmap(ContentResolver cr, Uri url)
            throws FileNotFoundException, IOException {
        InputStream input = cr.openInputStream(url);
        Bitmap bitmap = BitmapFactory.decodeStream(input);
        input.close();
        return bitmap;
    }

}
