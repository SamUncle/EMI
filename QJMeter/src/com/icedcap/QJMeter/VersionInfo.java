package com.icedcap.QJMeter;

import java.io.File;

import com.icedcap.QJMeter.Community.SendThread;
import com.icedcap.QJMeter.Community.readThread;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;



public class VersionInfo extends Activity {
	static final int Message_Newed = 0;//软件更新已是最新
	static final int Message_Update = 1;//更新版本提示下载路径
	Button downBtn;
	Button checkVerBtn;
	static Handler handler;
	 public void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        setContentView(R.layout.version_info); 
	        initView();
	        btnClickListener();
			//btn_checkVer
	        ReceiveMsg();
	 }
	//处理消息
		@SuppressLint("HandlerLeak")
		private void  ReceiveMsg()
		{
		        handler = new Handler() {
		        @SuppressLint("ShowToast")
				@Override
		        public void handleMessage(Message msg) {
		            switch (msg.what) {
		            case Message_Newed:
		            	Toast.makeText(VersionInfo.this, "已经是最新版,无需下载!", Toast.LENGTH_LONG).show();
		                break;
		            case Message_Update:
		            	Toast.makeText(VersionInfo.this, "下载完成!", Toast.LENGTH_LONG).show();
		            	break;
		           
		           
		            }
		        }
		    };
		}
	 private void initView() {
		 downBtn = (Button)findViewById(R.id.btn_downVer);
		 checkVerBtn = (Button)findViewById(R.id.btn_checkVer);
		}
	 private void btnClickListener() {
		 //检查版本监听
		 checkVerBtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				//创建警告框显示版本信息
				AlertDialog.Builder builder = new Builder(VersionInfo.this);
				builder.setTitle("软件信息");
				try {
					builder.setMessage("QJ手抄器当前版本:"+getVersionName());
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				builder.setPositiveButton("确认", new OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						dialog.dismiss();
						VersionInfo.this.finish();
					}
				});
				builder.create().show();
			}
		});
		 //下载监听事件
		 downBtn.setOnClickListener(new View.OnClickListener() {  
	    	   @Override  
	    	   public void onClick(View v) {
	    	   new Thread(){
  			       public void run()
  			       {
  			           //访问网络代码
  			    	   try {
  			    			String d_Url = "http://192.168.0.141:90/ApkDownload/QJMeter.apk";//ymtest 
  			    			//String d_Url = "http://220.178.85.242:90/ApkDownload/QJMeter.apk";
  							File down_file= DownLoadManager.getFileFromServer(d_Url);
  							if(down_file.exists()){
  								Message msg = Message.obtain();
  	  		            		msg.what = Message_Update;
  	  		  				    handler.sendMessage(msg);
  						        Intent aIntent=new Intent();
  						        aIntent.setAction(Intent.ACTION_VIEW);
  						        aIntent.setDataAndType(Uri.fromFile(down_file), "application/vnd.android.package-archive");
  		            	        startActivity(aIntent);
  							}
  		            	        
  		            	} catch (Exception e) {
  							// TODO Auto-generated catch block
  		            	//创建提示框提示版本信息已是最新版.
  		            		Message msg = Message.obtain();
  		            		msg.what = Message_Newed;
  		  				    handler.sendMessage(msg);
  							Log.i("异常信息",e.toString());
  						}
  			       }
  			   }.start();
	    		  
	    	   }
	    	});
		 
	}
	 private String getVersionName() throws Exception  
	 {  
	         // 获取packagemanager的实例  
	         PackageManager packageManager = getPackageManager();  
	         // getPackageName()是你当前类的包名，0代表是获取版本信息  
	         PackageInfo packInfo = packageManager.getPackageInfo(getPackageName(),0);  
	         String version = packInfo.versionName;  
	         return version;  
	 }  
	
}
