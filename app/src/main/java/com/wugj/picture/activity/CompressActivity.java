package com.wugj.picture.activity;

import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.wugj.picture.R;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

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

    private static final int IMAGE_REQUEST_CODE = 2;// 相册
    private CompressType defaultType =CompressType.SIZE;
    private ImageView iv1,iv2,iv3,iv4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compress);

        findViewById(R.id.pic8).setOnClickListener(this);
        findViewById(R.id.pic9).setOnClickListener(this);
        iv1=findViewById(R.id.iv1);
        iv2=findViewById(R.id.iv2);
        iv3=findViewById(R.id.iv3);
        iv4=findViewById(R.id.iv4);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.pic8:
                defaultType =CompressType.SIZE;
                break;
            case R.id.pic9:
                defaultType =CompressType.MASS;
                break;
        }

        Intent intent = new Intent(
                Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        // 打开手机相册,设置请求码
        startActivityForResult(intent, IMAGE_REQUEST_CODE);
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == IMAGE_REQUEST_CODE) {
            Uri selectedUri = data.getData(); //获取系统返回的照片的Uri

            if (defaultType == CompressType.SIZE){
                sizeCompress(selectedUri);

            }else if (defaultType == CompressType.MASS){
                massCompress(selectedUri);
            }

        }
    }

    private void sizeCompress(Uri selectedUri){

        //通过Uri获取bitmap设置图片
        Bitmap bit = null;
        try {
            bit = getBitmap(getContentResolver(),selectedUri);
        } catch (IOException e) {
            e.printStackTrace();
        }
        //显示原图
        iv1.setImageBitmap(bit);
        //压缩图片
        String imagePath = Uri2Path.getImageAbsolutePath(this,selectedUri);
        Bitmap bitmap=BitmapFactory.decodeFile(imagePath,getBitmapOption(2)); //将图片的长和宽缩小味原来的1/2
        iv3.setImageBitmap(bitmap);
        Toast.makeText(this, "原图大小"+bit.getHeight()+"...."+bit.getWidth()+bit.getByteCount()+"\n"+
                "压缩图大小"+bitmap.getHeight()+"...."+bitmap.getWidth()+bitmap.getByteCount(), Toast.LENGTH_SHORT).show();
    }

    private BitmapFactory.Options getBitmapOption(int inSampleSize){
        System.gc();
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inPurgeable = true;
        options.inSampleSize = inSampleSize;
        return options;
    }

    private void massCompress(Uri selectedUri){
        //通过Uri获取bitmap设置图片
        Bitmap bit = null;
        try {
            bit = getBitmap(getContentResolver(),selectedUri);
        } catch (IOException e) {
            e.printStackTrace();
        }
        //显示原图
        iv2.setImageBitmap(bit);

        //压缩图片
    }

    private final Bitmap getBitmap(ContentResolver cr, Uri url)
            throws FileNotFoundException, IOException {
        InputStream input = cr.openInputStream(url);
        Bitmap bitmap = BitmapFactory.decodeStream(input);
        input.close();
        return bitmap;
    }


    enum CompressType{
        SIZE,MASS
    }
}
