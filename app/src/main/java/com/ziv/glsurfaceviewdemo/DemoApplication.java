package com.ziv.glsurfaceviewdemo;

import android.app.Application;
import android.content.pm.PackageManager;
import android.os.Build;

/**
 * Application类
 * Created by ziv on 16-9-29.
 */

public class DemoApplication extends Application {
    public static final String CHECKED_PACKAGE_NAME = "com";
    private static boolean isAppExit = false;

    public static boolean isAppExit() {
        return isAppExit;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        isAppExit = checkAppExist(CHECKED_PACKAGE_NAME);
    }

    /**
     * 检查App是否已安装
     *
     * @param packageName 需要检查的App包名
     * @return true 已安装 false 未安装
     */
    private boolean checkAppExist(String packageName) {
        if (packageName.isEmpty()) {
            return false;
        }
        PackageManager packageManager = getApplicationContext().getPackageManager();
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                packageManager.getApplicationInfo(packageName, PackageManager.MATCH_UNINSTALLED_PACKAGES);
            } else {
                packageManager.getApplicationInfo(packageName, PackageManager.GET_UNINSTALLED_PACKAGES);
            }
            return true;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return false;
        }
    }
}
/**
 * 使用raw或asset存放的文件大小不能超过1048576字节
 *    报错信息Data exceeds UNCOMPRESS_DATA_MAX (2580997 vs 1048576)
 */