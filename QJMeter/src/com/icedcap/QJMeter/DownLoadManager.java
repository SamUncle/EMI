package com.icedcap.QJMeter;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Environment;


public class DownLoadManager {
	public static File getFileFromServer(String path) throws Exception{  
	    //如果相等的话表示当前的sdcard挂载在手机上并且是可用的  
	    if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){  
	        URL url = new URL(path);  
	        HttpURLConnection conn =  (HttpURLConnection) url.openConnection();  
	        conn.setConnectTimeout(5000);  
	        //获取到文件的大小   
	        InputStream is = conn.getInputStream(); 
	        
	        String file_path = Environment.getExternalStorageDirectory().getPath();//
            File file = new File(file_path, "QJMeter.apk");
            
	        FileOutputStream fos = new FileOutputStream(file);  
	        BufferedInputStream bis = new BufferedInputStream(is);  
	        byte[] buffer = new byte[1024];  
	        int len ;  
//	        int total=0;  
	        while((len =bis.read(buffer))!=-1){  
	            fos.write(buffer, 0, len);  
//	            total+= len;  
	            //获取当前下载量  
//	            pd.setProgress(total);  
	        }  
	        fos.close();  
	        bis.close();  
	        is.close();  
	        return file;  
	    }  
	    else{  
	        return null;  
	    }  
	}
	/** 
     * 获取apk的版本号 currentVersionCode 
     *  
     * @param ctx 
     * @return 
     */  
    public static int getAPPVersionCode(Context ctx) {  
        int currentVersionCode = 0;  
        PackageManager manager = ctx.getPackageManager();  
        try {  
            PackageInfo info = manager.getPackageInfo(ctx.getPackageName(), 0);  
            String appVersionName = info.versionName; // 版本名  
            currentVersionCode = info.versionCode; // 版本号  
            System.out.println(currentVersionCode + " " + appVersionName);  
        } catch (NameNotFoundException e) {  
            e.printStackTrace();  
        }  
        return currentVersionCode;  
    }  
    
    
}

