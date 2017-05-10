package com.example.mobilesafe.untils;

import android.app.ActivityManager;
import android.app.ActivityManager.MemoryInfo;
import android.content.Context;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

public class ProcessUtils {


	public static int getProcessCount(Context context){
		ActivityManager am=(ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
		return am.getRunningAppProcesses().size();
	}

	public  static long getAvailableRam(Context context){
		ActivityManager am=(ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
		MemoryInfo outInfo=new MemoryInfo();
		am.getMemoryInfo(outInfo);
		return outInfo.availMem;
	}

	public  static long getTotalRam(Context context){
		ActivityManager am=(ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
		MemoryInfo outInfo=new MemoryInfo();
		am.getMemoryInfo(outInfo);
		return outInfo.totalMem;
	}

	public static long getTotalRam(){
		File file=new File("/proc/meminfo");
		try {
			BufferedReader br=new BufferedReader(new FileReader(file));
			String readLine = br.readLine();
			StringBuilder sb=new StringBuilder();

			char[] charArray = readLine.toCharArray();
			for(char c:charArray){
				if(c>='0'&&c<='9'){
					sb.append(c);
				}
			}
			String string = sb.toString();
			long parseInt = Integer.parseInt(string);// ���ַ�����ʽ��������
			br.close();
			return parseInt*1024;
			
		} catch (Exception e) {
			e.printStackTrace();
			return 0;
		}
		
		
	}
	
}
