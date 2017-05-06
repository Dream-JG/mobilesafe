package com.example.mobilesafe.untils;

import android.app.ActivityManager;
import android.content.Context;

import java.util.List;

/**
 * Created by li on 2017/5/5.
 */

public class ServiceUtils {
    /**
     * 动态的判断服务有没有运行
     * @param className   服务名字
     * @param context
     * @return
     */
    public static boolean isServiceRunning (String className, Context context){
        //创建一个活动管理者
        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningServiceInfo> runningServiceInfos = activityManager.getRunningServices(1000);
        for(ActivityManager.RunningServiceInfo runningServiceInfo : runningServiceInfos){

            String className2 = runningServiceInfo.service.getClassName();
            System.out.println(className2);
            if (className2.equals(className)) {
                return  true;
            }
        }
        return false;

    }


}
