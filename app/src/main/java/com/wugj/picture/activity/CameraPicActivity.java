package com.wugj.picture.activity;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.wugj.picture.BuildConfig;
import com.wugj.picture.R;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 拍照选择照片
 */
public class CameraPicActivity extends AppCompatActivity implements View.OnClickListener {

    private static final int PHOTO_REQUEST_CAMERA = 1;// 拍照


    public static final int CAMERA_PERMISSION = 2;
    public static final int STORAGE_PERMISSION = 3;

    private CameraPicActivity instance;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);
        instance = this;
        findViewById(R.id.cam1).setOnClickListener(this);
        findViewById(R.id.cam2).setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.cam1:

                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {//检查相机权限
                    requestCameraPermission();
                } else {
                    requestStoragePermission();
                }
                break;

            case R.id.cam2:

                break;
        }
    }

    /**
     * 申请相机权限
     */
    private void requestCameraPermission() {
        ActivityCompat.requestPermissions(instance, new String[]{Manifest.permission.CAMERA}, CAMERA_PERMISSION);
    }

    /**
     * 判断sdcard是否被挂载
     * @return
     */
    private boolean hasSdcard() {
        if (Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED)) {
            return true;
        } else {
            return false;
        }
    }


    /**
     * 申请storage写入权限
     */
    private void requestStoragePermission() {
        if (hasSdcard()) {
            ActivityCompat.requestPermissions(instance, new String[]{android
                    .Manifest.permission.WRITE_EXTERNAL_STORAGE}, STORAGE_PERMISSION);
        }
    }

    File picFile = null;

    /**
     * 权限回调
     * @param requestCode
     * @param permissions
     * @param grantResults
     */
    @Override
    public void onRequestPermissionsResult(final int requestCode, final @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        for (int i = 0; i < grantResults.length; i++) {
            boolean isTip = ActivityCompat.shouldShowRequestPermissionRationale(this, permissions[i]);
            if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                if (isTip) {//表明用户没有彻底禁止弹出权限请求
                    new AlertDialog.Builder(this)
                            .setTitle("title")
                            .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    // 请求授权
                                    ActivityCompat.requestPermissions(instance, permissions, requestCode);
                                }
                            })
                            .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                }
                            })
                            .create()
                            .show();
                } else {//表明用户已经彻底禁止弹出权限请求
                    Toast.makeText(instance, "前往设置->app->myapp->permission打开", Toast.LENGTH_SHORT).show();

                }
                return;
            }
        }


        switch (requestCode) {
            case CAMERA_PERMISSION:
                requestStoragePermission();
                break;
            case STORAGE_PERMISSION:
                File storageDir;
                //文件路径是公共的DCIM目录下的/camerademo目录
                String storagePath = Environment
                        .getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM)
                        .getAbsolutePath() + File.separator + "camera";
                storageDir = new File(storagePath);

                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    //创建文件夹
                    if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
                        if (!storageDir.exists()) {
                            storageDir.mkdirs();
                        }


                        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());

                        try {
                            picFile = File.createTempFile(timeStamp, ".jpg", storageDir);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
                            // 从文件中创建uri
                            Uri uri = Uri.fromFile(picFile);
                            intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
                        } else {
                            //兼容android7.0 使用共享文件的形式
                            Uri uri = FileProvider.getUriForFile(instance,
                                    BuildConfig.APPLICATION_ID + ".fileProvider", picFile);

                            intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
                        }
                        startActivityForResult(intent, PHOTO_REQUEST_CAMERA);


                    }
                    break;
                }
        }
    }

    /**
     * 拍照选择照片回调
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == PHOTO_REQUEST_CAMERA) {

            if (hasSdcard()) {
                ImageView iv1 = findViewById(R.id.iv1);
                iv1.setImageURI(Uri.fromFile(picFile));
            } else {
                Toast.makeText(instance, "未找到存储卡，无法存储照片！", Toast.LENGTH_SHORT).show();
            }
        } else {
            //照片大小为0k删除照片
            if (picFile.length() <= 0) {
                picFile.delete();
            }

        }
    }


}
