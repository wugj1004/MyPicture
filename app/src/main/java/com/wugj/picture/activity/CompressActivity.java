package com.wugj.picture.activity;

import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.wugj.picture.R;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
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
                defaultType =CompressType.QUALITY;
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

            }else if (defaultType == CompressType.QUALITY){
                qualityCompress(selectedUri);
            }

        }
    }

    /**----------------------------尺寸压缩---------开始----------------------------**/
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


        String imagePath = Uri2Path.getImageAbsolutePath(this,selectedUri);
//        Bitmap bitmap = compressImage(imagePath,2);
        Bitmap bitmap = compressImage(imagePath,800,480);
        //显示压缩图
        iv3.setImageBitmap(bitmap);


        Toast.makeText(this, "原图大小"+bit.getHeight()+"...."+bit.getWidth()+bit.getByteCount()/1024+"\n"+
                "压缩图大小"+bitmap.getHeight()+"...."+bitmap.getWidth()+bitmap.getByteCount()/1024, Toast.LENGTH_SHORT).show();
    }

    /**
     * 指定压缩比例压缩
     * @param imagePath
     * @param inSampleSize
     * @return
     */
    private Bitmap compressImage(String imagePath,int inSampleSize){
        System.gc();
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inPurgeable = true;
        options.inSampleSize = inSampleSize;
        //压缩图片
        Bitmap bitmap=BitmapFactory.decodeFile(imagePath,options); //将图片的长和宽缩小味原来的1/2
        return bitmap;
    }

    /**
     * 指定尺寸压缩
     * @param srcPath
     * @param hh
     * @param ww
     * @return
     */
    private Bitmap compressImage(String srcPath, float hh, float ww) {
        BitmapFactory.Options newOpts = new BitmapFactory.Options();
        newOpts.inJustDecodeBounds = true;//只读边,不读内容
        Bitmap bitmap = BitmapFactory.decodeFile(srcPath, newOpts);

        newOpts.inJustDecodeBounds = false;
        int w = newOpts.outWidth;
        int h = newOpts.outHeight;

        int be = 1;
        if (w > h && w > ww) {
            be = (int) (newOpts.outWidth / ww);
        } else if (w < h && h > hh) {
            be = (int) (newOpts.outHeight / hh);
        }
        if (be <= 0)
            be = 1;
        newOpts.inSampleSize = be;//设置采样率

        newOpts.inPreferredConfig = Bitmap.Config.ARGB_8888;//该模式是默认的,可不设
        newOpts.inPurgeable = true;// 同时设置才会有效
        newOpts.inInputShareable = true;//。当系统内存不够时候图片自动被回收

        bitmap = BitmapFactory.decodeFile(srcPath, newOpts);
        //其实是无效的,大家尽管尝试
        return bitmap;
    }


    /**----------------------------尺寸压缩---------结束----------------------------**/

    /**----------------------------质量压缩---------开始----------------------------**/

    private void qualityCompress(Uri selectedUri){
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
        Bitmap bitmap = qualityCompress2Storage(selectedUri);
//        Bitmap bitmap = qualityCompress2File(selectedUri);
        iv4.setImageBitmap(bitmap);
    }


    /**
     * 质量压缩-内存中流文件变小
     * @param selectedUri
     * @return
     */
    private Bitmap qualityCompress2Storage(Uri selectedUri){
        //压缩图片
        Bitmap bitmap = null;
        try {
            bitmap = getBitmap(getContentResolver(),selectedUri);
        } catch (IOException e) {
            e.printStackTrace();
        }

        /*format是压缩后的图片的格式，可取值：Bitmap.CompressFormat .JPEG、~.PNG、~.WEBP。
        quality的取值范围为[0,100]，值越小，经过压缩后图片失真越严重，当然图片文件也会越小。（PNG格式的图片会忽略这个值的设定）
        stream指定压缩的图片输出的地方，比如某文件。*/
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        int quality = 100;
        bitmap.compress(Bitmap.CompressFormat.JPEG, quality, baos);


        byte[] bytes = baos.toByteArray();
        bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
        Log.i("CompressActivity", " 111 zx 原始图片大小图片大小：" + bitmap.getByteCount()
                + " 宽度：" + bitmap.getWidth() + " 高度：" + bitmap.getHeight()
                + " bytes.length= " + (bytes.length / 1024) + "KB"
                + " quality=" + quality);



        while (baos.toByteArray().length / 1024 > 100) {
            baos.reset();
            quality -= 10;
            bitmap.compress(Bitmap.CompressFormat.JPEG, quality, baos);
        }

        byte[] byteCompress = baos.toByteArray();
        bitmap = BitmapFactory.decodeByteArray(byteCompress, 0, byteCompress.length);
        Log.i("CompressActivity", " 111 zx 压缩后图片大小：" + bitmap.getByteCount()
                + " 宽度：" + bitmap.getWidth() + " 高度：" + bitmap.getHeight()
                + " bytes.length= " + (byteCompress.length / 1024) + "KB"
                + " quality=" + quality);


        ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());
        Bitmap bitmap1 = BitmapFactory.decodeStream(isBm, null, null);

        return bitmap1;
    }

    /**
     * 质量压缩-压缩后保存在本地file中
     * @param selectedUri
     * @return
     */
    private Bitmap qualityCompress2File(Uri selectedUri){
        //压缩图片
        Bitmap bitmap = null;
        try {
            bitmap = getBitmap(getContentResolver(),selectedUri);
        } catch (IOException e) {
            e.printStackTrace();
        }

        /*format是压缩后的图片的格式，可取值：Bitmap.CompressFormat .JPEG、~.PNG、~.WEBP。
        quality的取值范围为[0,100]，值越小，经过压缩后图片失真越严重，当然图片文件也会越小。（PNG格式的图片会忽略这个值的设定）
        stream指定压缩的图片输出的地方，比如某文件。*/
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        int quality = 100;
        bitmap.compress(Bitmap.CompressFormat.JPEG, quality, baos);

        byte[] bytes = baos.toByteArray();
        bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
        Log.e("CompressActivity", " 111 zx 原始图片大小图片大小：" + bitmap.getByteCount()
                + " 宽度：" + bitmap.getWidth() + " 高度：" + bitmap.getHeight()
                + " bytes.length= " + (bytes.length / 1024) + "KB"
                + " quality=" + quality);


        //保存本地file保存地址
        String savePath = Environment.getExternalStorageDirectory()+"/test_scaled100.jpeg";
        FileOutputStream outputStream = null;
        try {
            //循环压缩
            while (baos.toByteArray().length / 1024 > 100) {
                baos.reset();
                quality -= 10;
                bitmap.compress(Bitmap.CompressFormat.JPEG, quality, baos);
            }
            //文件压缩后保存在file中
            File file = new File(savePath);
            outputStream = new FileOutputStream(file);
            outputStream.write(baos.toByteArray());
            //压缩后打印压缩文件所占内存
            Log.e("CompressActivity", " 111 zx 压缩后图片大小：" + bitmap.getByteCount()
                    + " 宽度：" + bitmap.getWidth() + " 高度：" + bitmap.getHeight()
                    + " bytes.length= " + (file.length() / 1024) + "KB"
                    + " quality=" + quality);

            outputStream.flush();
            outputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            if(outputStream != null){
                try {
                    outputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return bitmap;
    }

    /**----------------------------质量压缩---------结束----------------------------**/






    private final Bitmap getBitmap(ContentResolver cr, Uri url)
            throws FileNotFoundException, IOException {
        InputStream input = cr.openInputStream(url);
        Bitmap bitmap = BitmapFactory.decodeStream(input);
        input.close();
        return bitmap;
    }


    enum CompressType{
        SIZE,QUALITY
    }
}
