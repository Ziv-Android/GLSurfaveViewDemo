package com.ziv.glsurfaceviewdemo;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import java.io.File;

import static com.ziv.glsurfaceviewdemo.DemoApplication.isAppExit;

public class MainActivity extends AppCompatActivity {
    public static final String SD_CARD = Environment.getExternalStorageDirectory().getPath();
    public static final String assertFolderName = "testFolder";
    public static final String targetFolderPath = SD_CARD + "/" + assertFolderName;
//    public static final String assertFolderName = "testFolder";
//    public static final String targetFolderPath = SD_CARD + "/" + assertFolderName;
    public static final int REQUEST_STORAGE_PERMISSION = 1000;
    public String filePath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        copyApkFromAssest();
        if (!isAppExit()){
            installApp(filePath);
        }
    }

    private void installApp(String filePath) {
        Intent intent = new Intent();
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setAction(Intent.ACTION_VIEW);
        String type = "application/vnd.android.package-archive";
        File file = new File(filePath);
        intent.setDataAndType(Uri.fromFile(file),type);
        startActivity(intent);
    }

    private void copyApkFromAssest() {
        int versionCode = Build.VERSION.SDK_INT;
        if (versionCode >= Build.VERSION_CODES.M) {
            int checkPermission = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
            // 如果没有相关权限，返回值为 check Permission value = -1
            Log.e("ziv", "check Permission value = " + checkPermission);

            if (checkPermission != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_STORAGE_PERMISSION);
            } else {
                FileHelper.copyFileFromAssets(this, assertFolderName, targetFolderPath);
            }
        }else {
            FileHelper.copyFolderFromAssets(this, assertFolderName, targetFolderPath);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_STORAGE_PERMISSION:
                Log.e("ziv", "onRequestPermissionsResult = " + REQUEST_STORAGE_PERMISSION);
                Log.e("ziv", "grantResults[0] = " + grantResults[0] + " " + (grantResults[0] == PackageManager.PERMISSION_GRANTED));
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    FileHelper.copyFolderFromAssets(this, assertFolderName, targetFolderPath);
                }else {
                    Toast.makeText(this,"权限获取失败",Toast.LENGTH_SHORT).show();
                }
                break;
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
}
