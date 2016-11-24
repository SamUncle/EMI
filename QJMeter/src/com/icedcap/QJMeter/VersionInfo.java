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
	static final int Message_Newed = 0;//���������������
	static final int Message_Update = 1;//���°汾��ʾ����·��
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
	//������Ϣ
		@SuppressLint("HandlerLeak")
		private void  ReceiveMsg()
		{
		        handler = new Handler() {
		        @SuppressLint("ShowToast")
				@Override
		        public void handleMessage(Message msg) {
		            switch (msg.what) {
		            case Message_Newed:
		            	Toast.makeText(VersionInfo.this, "�Ѿ������°�,��������!", Toast.LENGTH_LONG).show();
		                break;
		            case Message_Update:
		            	Toast.makeText(VersionInfo.this, "�������!", Toast.LENGTH_LONG).show();
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
		 //���汾����
		 checkVerBtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				//�����������ʾ�汾��Ϣ
				AlertDialog.Builder builder = new Builder(VersionInfo.this);
				builder.setTitle("�����Ϣ");
				try {
					builder.setMessage("QJ�ֳ�����ǰ�汾:"+getVersionName());
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				builder.setPositiveButton("ȷ��", new OnClickListener() {
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
		 //���ؼ����¼�
		 downBtn.setOnClickListener(new View.OnClickListener() {  
	    	   @Override  
	    	   public void onClick(View v) {
	    	   new Thread(){
  			       public void run()
  			       {
  			           //�����������
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
  		            	//������ʾ����ʾ�汾��Ϣ�������°�.
  		            		Message msg = Message.obtain();
  		            		msg.what = Message_Newed;
  		  				    handler.sendMessage(msg);
  							Log.i("�쳣��Ϣ",e.toString());
  						}
  			       }
  			   }.start();
	    		  
	    	   }
	    	});
		 
	}
	 private String getVersionName() throws Exception  
	 {  
	         // ��ȡpackagemanager��ʵ��  
	         PackageManager packageManager = getPackageManager();  
	         // getPackageName()���㵱ǰ��İ�����0�����ǻ�ȡ�汾��Ϣ  
	         PackageInfo packInfo = packageManager.getPackageInfo(getPackageName(),0);  
	         String version = packInfo.versionName;  
	         return version;  
	 }  
	
}
