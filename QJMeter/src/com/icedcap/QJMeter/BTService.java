package com.icedcap.QJMeter;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Iterator;
import java.util.Set;
import java.util.UUID;

import com.icedcap.QJMeter.Bluetooth.clientThread;
import com.icedcap.QJMeter.SingleMeter.readThread;

import android.R.integer;
import android.R.string;
import android.app.Service;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;


public class BTService extends Service{
    public boolean threadFlag = true;
    CommandReceiver cmdReceiver;
    MyThread mythread;
    public clientThread clientConnectThread = null;
    /****service命令***/
    static final int CMD_STOP_SERVICE = 0x01;
    static final int CMD_SEND_DATA = 0x02;
    static final int CMD_SYSTEM_EXIT = 0x03;
    static final int CMD_SHOW_TOAST = 0x04;
    /**蓝牙变量**/
    private BluetoothAdapter mBluetoothAdapter = null;
    private BluetoothSocket btSocket = null;
    private OutputStream outStream = null;
    private InputStream inStream = null;
    public boolean bluetoothFlag = true;
    private static final UUID MY_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
    String BT_Addr = null;//连接的蓝牙链接MAC地址
    String BT_Name = null;
	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public void onCreate()
	{
		super.onCreate();
		
	}
	@Override
	public int onStartCommand(Intent intent,int flags,int startId){
		cmdReceiver = new CommandReceiver();
		IntentFilter filter = new IntentFilter();
		filter.addAction("android.intent.action.cmd");//注册一个广播用于接收Activity传过来的命令控制service行为
	    registerReceiver(cmdReceiver, filter);
	    //调用方法启用线程;
	    doJob();
	    return super.onStartCommand(intent, flags, startId);
	}
   @Override
   public void onDestroy()
   {
	   super.onDestroy();
	   this.unregisterReceiver(cmdReceiver);
	   threadFlag = false;
	   boolean retry = true;
	   while (retry) {
		 try {
			mythread.join();
			retry = false;
		} catch (Exception e) {
			// TODO: handle exception
		}
		
	}
   }
   //停止服务
   public void stopService() {
	threadFlag = false;
	stopSelf();
   }
   public void doJob(){      
       mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();  
      if (mBluetoothAdapter == null) {   
          DisplayToast("蓝牙设备不可用，请打开蓝牙！");  
          bluetoothFlag  = false;  
          return;  
      }  

      if (!mBluetoothAdapter.isEnabled()) {  
          DisplayToast("请打开蓝牙并重新运行程序！");  
          bluetoothFlag  = false;  
          stopService();  
          showToast("请打开蓝牙并重新运行程序！");  
          return;  
      }        
      showToast("搜索到蓝牙设备!");        
      threadFlag = true;   
      //启动线程连接蓝牙
      mythread = new MyThread();  
      mythread.start();  
        
  }  
   private void DisplayToast(String string) {
	// TODO Auto-generated method stub
	Log.d("Season", string);
   }
   public void showToast(String str)
   {
	   Intent intent = new Intent();
	   intent.putExtra("cmd", CMD_SHOW_TOAST);
	   intent.putExtra("str", str);
	   intent.setAction("android.intent.action.1xx");
	   sendBroadcast(intent);
	   
   }
   
   
//新建线程
   public class MyThread extends Thread{
	   @Override 
	   public void run(){
		   super.run();
		   //连接蓝牙设备
		   connectDevice();
		   while (threadFlag) {
			int value = readByte();
			if(value!=-1){
				DisplayToast(value+"");
			}
			try {
				Thread.sleep(50);
			} catch (Exception e) {
				// TODO: handle exception
			}
		}
	   }
   }
   public  void connectDevice(){   
       DisplayToast("正在尝试连接蓝牙设备，请稍后····");  
       //得到所有已经配对的蓝牙适配器对象
		Set<BluetoothDevice> devices = mBluetoothAdapter.getBondedDevices();
		if(devices.size()>0)    
		{//用迭代
			for(Iterator<BluetoothDevice> iterator = devices.iterator();iterator.hasNext();)
			{
				//得到BluetoothDevice对象,也就是说得到配对的蓝牙适配器
				BluetoothDevice device = (BluetoothDevice)iterator.next();
				//得到远程蓝牙设备的地址
				Log.d("mytag",device.getAddress());
				BT_Addr = device.getAddress();
				BT_Name = device.getName();
				if(BT_Name.equals("EMI0001")){
					//开启事物
					clientConnectThread = new clientThread();
					clientConnectThread.start();
				}
					
			}     
		}
          
         
       if(bluetoothFlag){  
           try {  
               inStream = btSocket.getInputStream();  
               } catch (IOException e) {  
                 e.printStackTrace();  
             } //绑定读接口  
               
          try {  
               outStream = btSocket.getOutputStream();  
              } catch (IOException e) {  
               e.printStackTrace();  
              } //绑定写接口  
             
       }  
   } 
 //开启客户端
 	public class clientThread extends Thread {   
 		  public void run() {
 			 BluetoothDevice device = mBluetoothAdapter.getRemoteDevice(BT_Addr);  
 		       try {  
 		          btSocket = device.createRfcommSocketToServiceRecord(MY_UUID);  
 		       } catch (IOException e) {  
 		           DisplayToast("套接字创建失败！");  
 		           bluetoothFlag = false;  
 		       }  
 		       DisplayToast("成功连接蓝牙设备！");  
 		       mBluetoothAdapter.cancelDiscovery();  
 		       try {  
 		               btSocket.connect();  
 		               DisplayToast("连接成功建立，可以开始操控了!");  
 		               showToast("连接成功建立，可以开始操控了!");  
 		               bluetoothFlag = true;  
 		       } catch (IOException e) {  
 		              try {  
 		                   btSocket.close();  
 		                   bluetoothFlag = false;  
 		               } catch (IOException e2) {                          
 		                  DisplayToast("连接没有建立，无法关闭套接字！");  
 		               }  
 		       } 
 		   
 		  
 		}
 	};	 
 		
   public int readByte(){
	   int ret = -1;
	   if(!bluetoothFlag){
		   return ret;
	   }
	   try {
		ret = inStream.read();		
	} catch (Exception e) {
		// TODO: handle exception
	}
	   return ret;
   }
   public void sendCmd(byte cmd, int value)//串口发送数据  
   {     
       if(!bluetoothFlag){  
           return;  
       }  
       byte[] msgBuffer = new byte[5];                                   
         msgBuffer[0] = cmd;  
         msgBuffer[1] = (byte)(value >> 0  & 0xff);  
         msgBuffer[2] = (byte)(value >> 8  & 0xff);  
         msgBuffer[3] = (byte)(value >> 16 & 0xff);  
         msgBuffer[4] = (byte)(value >> 24 & 0xff);  
           
         try {  
           outStream.write(msgBuffer, 0, 5);  
           outStream.flush();  
         } catch (IOException e) {  
             e.printStackTrace();  
         }           
   }   
   public void sendBTMessage(byte[] cmd) 
	 {  
			 if (btSocket == null) 
			 {	   
			  return;
			 }
			 try {    
			  OutputStream os = btSocket.getOutputStream();   
			  os.write(cmd);
			  os.flush();
			 } catch (IOException e) {
			   e.printStackTrace();
			}   
	 }
	//新建一个广播接收类
	private class CommandReceiver extends BroadcastReceiver{
		@Override
		public void onReceive(Context context,Intent intent){
			if (intent.getAction().equals("android.intent.action.cmd")) {
				int cmd = intent.getIntExtra("cmd", -1);
				if (cmd==CMD_STOP_SERVICE) {
					stopService();
				}
				if (cmd == CMD_SEND_DATA) {
					byte command = intent.getByteExtra("command", (byte) 0);  
                    int value =  intent.getIntExtra("value", 0);  
                    sendCmd(command,value); 
				}
			}
		}
	}
}
