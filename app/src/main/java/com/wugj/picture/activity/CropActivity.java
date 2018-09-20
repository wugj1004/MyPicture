package com.wugj.picture.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;

import com.wugj.picture.R;

/**
 * 照片裁剪
 */
public class CropActivity extends AppCompatActivity implements View.OnClickListener {

    private static final int PHOTO_RECT = 2;// 从相册中选择
    private static final int PHOTO_RECT_CHANGE = 3;// 从相册中选择
    private static final int PHOTO_CIRCLE = 4;// 从相册中选择

    private static final int PHOTO_REQUEST_CUT = 5;// 结果

    ImageView iv1,iv2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crop);

        findViewById(R.id.pic5).setOnClickListener(this);
        findViewById(R.id.pic6).setOnClickListener(this);
        findViewById(R.id.pic7).setOnClickListener(this);

        iv1 = findViewById(R.id.iv1);
        iv2 = findViewById(R.id.iv2);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.pic5:

                break;
            case R.id.pic6:
                Intent intent6 = new Intent(Intent.ACTION_PICK, null);
                intent6.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");//限制图片类型，可写"image/jpg"或"image/png"
                startActivityForResult(intent6, PHOTO_RECT);
                break;
            case R.id.pic7:
                Intent intent7 = new Intent(Intent.ACTION_PICK, null);
                intent7.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");//限制图片类型，可写"image/jpg"或"image/png"
                startActivityForResult(intent7, PHOTO_RECT_CHANGE);
                break;
            case R.id.pic8:
                Intent intent8 = new Intent(Intent.ACTION_PICK, null);
                intent8.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");//限制图片类型，可写"image/jpg"或"image/png"
                startActivityForResult(intent8, PHOTO_CIRCLE);
                break;
        }
    }


    protected void cropClick(){

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PHOTO_RECT) {
            // 从相册返回的数据
            if (data != null) {
                // 得到图片的全路径
                Uri uri = data.getData();
                cropRect(uri);
            }

        } else if (requestCode == PHOTO_RECT_CHANGE) {
            // 从相册返回的数据
            if (data != null) {
                // 得到图片的全路径
                Uri uri = data.getData();
                cropRectChange(uri);
            }

        } else if (requestCode == PHOTO_CIRCLE) {
            // 从相册返回的数据
            if (data != null) {
                // 得到图片的全路径
                Uri uri = data.getData();
                cropCircle(uri);
            }

        } else if (requestCode == PHOTO_REQUEST_CUT) {
            // 从剪切图片返回的数据
            if (data != null) {
                Bitmap bitmap = data.getParcelableExtra("data");
                iv2.setImageBitmap(bitmap);
            }

        }
    }


    /**
     * 矩形裁剪后不改变原图
     * @param uri
     */
    private void cropRect(Uri uri) {
        // 裁剪图片意图
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");


        // 同时设置aspectX，aspectY圆形截图
        //intent.putExtra("aspectX", 1);//X方向上的比例
        //intent.putExtra("aspectY", 1);//Y方向上的比例
        // 不会裁剪原图-裁剪后输出图片的尺寸大小
        intent.putExtra("outputX", 250);
        intent.putExtra("outputY", 250);

        //原图会被裁剪-设置MediaStore.EXTRA_OUTPUT后原图会被裁剪
        //intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);


        intent.putExtra("scale", true);//是否保留比例
        intent.putExtra("crop", "true");
        intent.putExtra("outputFormat", "JPEG");// 图片格式
        intent.putExtra("return-data", true);//如果binder中数据过大会抛出android.os.TransactionTooLargeException异常
        intent.putExtra("noFaceDetection", true);

        // 开启一个带有返回值的Activity，请求码为PHOTO_REQUEST_CUT
        startActivityForResult(intent, PHOTO_REQUEST_CUT);
    }

    /**
     * 矩形裁剪后改变原图
     * @param uri
     */
    private void cropRectChange(Uri uri) {
        // 裁剪图片意图
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");


        // 同时设置aspectX，aspectY圆形截图
        //intent.putExtra("aspectX", 1);//X方向上的比例
        //intent.putExtra("aspectY", 1);//Y方向上的比例
        // 不会裁剪原图-裁剪后输出图片的尺寸大小
        intent.putExtra("outputX", 250);
        intent.putExtra("outputY", 250);

        //原图会被裁剪-设置MediaStore.EXTRA_OUTPUT后原图会被裁剪
        intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);


        intent.putExtra("scale", true);//是否保留比例
        intent.putExtra("crop", "true");
        intent.putExtra("outputFormat", "JPEG");// 图片格式
        intent.putExtra("return-data", true);//如果binder中数据过大会抛出android.os.TransactionTooLargeException异常
        intent.putExtra("noFaceDetection", true);

        // 开启一个带有返回值的Activity，请求码为PHOTO_REQUEST_CUT
        startActivityForResult(intent, PHOTO_REQUEST_CUT);
    }



    /*
     * 圆形裁剪
     */
    private void cropCircle(Uri uri) {
        // 裁剪图片意图
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");


        // 同时设置aspectX，aspectY圆形截图
        intent.putExtra("aspectX", 1);//X方向上的比例
        intent.putExtra("aspectY", 1);//Y方向上的比例
        // 不会裁剪原图-裁剪后输出图片的尺寸大小
        intent.putExtra("outputX", 250);
        intent.putExtra("outputY", 250);

        //原图会被裁剪-设置MediaStore.EXTRA_OUTPUT后原图会被裁剪
        //intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);


        intent.putExtra("scale", true);//是否保留比例
        intent.putExtra("crop", "true");
        intent.putExtra("outputFormat", "JPEG");// 图片格式
        intent.putExtra("return-data", true);//如果binder中数据过大会抛出android.os.TransactionTooLargeException异常
        intent.putExtra("noFaceDetection", true);

        // 开启一个带有返回值的Activity，请求码为PHOTO_REQUEST_CUT
        startActivityForResult(intent, PHOTO_REQUEST_CUT);
    }

}
