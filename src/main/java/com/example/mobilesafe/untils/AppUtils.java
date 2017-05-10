package com.example.mobilesafe.untils;

import android.content.Context;
import android.os.Build;
import android.os.Environment;
import android.os.StatFs;
import android.support.annotation.RequiresApi;
import android.text.format.Formatter;

import java.io.File;

/**
 * Created by li on 2017/5/10.
 */

@RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
public class AppUtils {

    /**
     * 获取SD卡的可用空间
     * @param c
     * @return
     */

    public static String getSDAvilable(Context c){
        //获取SD卡路径
        File path = Environment.getExternalStorageDirectory();
        StatFs stast = new StatFs(path.getPath());
        long blockSize = stast.getBlockSizeLong(); // 获取每块的大小
        long availableBlocksLong = stast.getAvailableBlocksLong();//获取可用的块数
        return   Formatter.formatFileSize(c,blockSize * availableBlocksLong);
    }

    /**
     * 获取手机内部可用空间
     * @param c
     * @return
     */

    public static String getRomAvailable(Context c){
        File path = Environment.getDataDirectory();
        StatFs stast = new StatFs(path.getPath());
        long blockSize = stast.getBlockSizeLong(); // 获取每块的大小
        long availableBlocksLong = stast.getAvailableBlocksLong();// 获取可用的块数
        return   Formatter.formatFileSize(c,blockSize * availableBlocksLong);
    }

}
