package com.example.mobilesafe.engine;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;

import com.example.mobilesafe.domain.AppInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by li on 2017/5/7.
 */

public class AppEngine {

    public static List<AppInfo> getAllAppInfos(Context context){
        List<AppInfo> appInfoList = new ArrayList<AppInfo>();

        //获取到包的管理者  获取到 所有的应用程序清单文件信息
        PackageManager manager = context.getPackageManager();
        List<PackageInfo> packageInfoList = manager.getInstalledPackages(0);
        for(PackageInfo info: packageInfoList){
            String packageName = info.packageName;
            String versionName = info.versionName;
            String name = info.applicationInfo.loadLabel(manager).toString();
            Drawable icon = info.applicationInfo.loadIcon(manager);
            boolean isUser;
            boolean isSd;
            int flag = info.applicationInfo.flags;
            if((flag& ApplicationInfo.FLAG_SYSTEM)==ApplicationInfo.FLAG_SYSTEM){
                isUser = false;
            }else {
                isUser = true;
            }
            if((flag&ApplicationInfo.FLAG_EXTERNAL_STORAGE)==ApplicationInfo.FLAG_EXTERNAL_STORAGE){
                isSd=true;
            }else {
                isSd=false;
            }
            AppInfo appInfo = new AppInfo(name,versionName,icon,packageName,isSd,isUser);
            appInfoList.add(appInfo);


        }
        return appInfoList;
    }
}
