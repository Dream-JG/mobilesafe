package com.example.mobilesafe.engine;

import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.os.Debug;

import com.example.mobilesafe.domain.TaskInfo;
import com.jaredrummler.android.processes.AndroidProcesses;
import com.jaredrummler.android.processes.models.AndroidAppProcess;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by li on 2017/5/10.
 */

public class TaskEngine {
    /**适用Android5.0以前
     * 返回手机所有进程的信息
     * @param context
     * @return
     */

    public static List<TaskInfo> getOldTaskInfos(Context context){
        List<TaskInfo> taskInfoList = new ArrayList<TaskInfo>();
        PackageManager packageManager = context.getPackageManager();
        //创建活动管理者
        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> runningAppProcessInfoList = activityManager.getRunningAppProcesses();
        for(ActivityManager.RunningAppProcessInfo runningAppProcessInfo :runningAppProcessInfoList){
            TaskInfo taskInfo = new TaskInfo();
            String processName = runningAppProcessInfo.processName;
            taskInfo.setPackageName(processName);
            Debug.MemoryInfo[] processMemoryInfo = activityManager.getProcessMemoryInfo(new int[]{runningAppProcessInfo.pid});
            long memory = processMemoryInfo[0].getTotalPss() * 1024;
            taskInfo.setMemory(memory);
            try {
                ApplicationInfo applicationInfo = packageManager.getApplicationInfo(processName, 0);
                String name = applicationInfo.loadLabel(packageManager).toString();
                taskInfo.setName(name);
                Drawable icon =applicationInfo.loadIcon(packageManager);
                taskInfo.setIcon(icon);
                boolean isUser;
                if((applicationInfo.flags&ApplicationInfo.FLAG_SYSTEM)==ApplicationInfo.FLAG_SYSTEM){
                    isUser=false;
                }else{
                    isUser=true;
                }
                taskInfo.setUser(isUser);

            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
            }

            taskInfoList.add(taskInfo);

        }
        return  taskInfoList;
    }

    /**
     * 适用Android 5.0以前
     * 获取手机所有进程信息
     * @param context
     * @return TaskInfo 进程信息
     */


    public static List<TaskInfo> getTaskInfos(Context context) {
        List<AndroidAppProcess> processInfos = AndroidProcesses.getRunningAppProcesses();

        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);

        PackageManager packageManager = context.getPackageManager();
        List<TaskInfo> taskInfoList = new ArrayList<TaskInfo>();
        // 遍历运行的程序,并且获取其中的信息
        for (AndroidAppProcess processInfo : processInfos) {
            TaskInfo taskInfo = new TaskInfo();
            // 应用程序的包名
            String packname = processInfo.name;
            taskInfo.setPackageName(packname);
            // 获取应用程序的内存 信息
            android.os.Debug.MemoryInfo[] memoryInfos = activityManager
                    .getProcessMemoryInfo(new int[] { processInfo.pid });
            long memsize = memoryInfos[0].getTotalPrivateDirty() * 1024L;
            taskInfo.setMemory(memsize);
            taskInfo.setPackageName(processInfo.getPackageName());
            try {
                // 获取应用程序信息
                ApplicationInfo applicationInfo = packageManager.getApplicationInfo(
                        packname, 0);
                Drawable icon = applicationInfo.loadIcon(packageManager);
                taskInfo.setIcon(icon);
                String name = applicationInfo.loadLabel(packageManager).toString();
                taskInfo.setName(name);

                if ((applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) == 0) {
                    // 用户进程
                    taskInfo.setUser(true);
                } else {
                    // 系统进程
                    taskInfo.setUser(false);
                }
            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
                // 系统内核进程 没有名称
                taskInfo.setName(packname);
            }

                taskInfoList.add(taskInfo);

        }
        return taskInfoList;
    }
}
