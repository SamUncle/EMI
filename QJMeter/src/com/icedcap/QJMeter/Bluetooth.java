package com.icedcap.QJMeter;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Iterator;
import java.util.Set;
import java.util.UUID;
import com.icedcap.QJMeter.DataBuffer;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.os.Handler;
import android.util.Log;

public class Bluetooth extends Activity{
		 public BluetoothDevice device = null;
		 public BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
		 public clientThread clientConnectThread = null;
		 public BluetoothSocket socket = null;
		 public readThread mreadThread = null;
//		 private Handler handler;
//		 private BluetoothGatt mBluetoothGatt;
		 //private Context context;
		 String address=null;
		 String name = null;
		 private int bytes;
		 public byte[] buf_data = new byte[bytes];
//		 private DataBuffer dataBuffer;
//		 private DataBuffer sendDataBuffer;
		 
		/* public Bluetooth(Context context, Handler handler) {
		//        this.context = context;
		        this.handler = handler;
		        
		        dataBuffer = new DataBuffer(4096);
		        sendDataBuffer = new DataBuffer(4096);
		     
		    } 
	    */
		public void ConnectBTDevice()
		{
			//得到BluetoothAdapter对象
			BluetoothAdapter adapter = BluetoothAdapter.getDefaultAdapter();
			//判断BluetoothAdapter对象是否为空，如果为空，则表明本机没有蓝牙设备
			if(adapter != null)
			{
				System.out.println("本机拥有蓝牙设备");
				//调用isEnabled()方法判断当前蓝牙设备是否可用
				if(!adapter.isEnabled())
				{     
				//如果蓝牙设备不可用的话,创建一个intent对象,该对象用于启动一个Activity,提示用户启动蓝牙适配器
				Intent intent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
				startActivity(intent);
				}
				//得到所有已经配对的蓝牙适配器对象
				Set<BluetoothDevice> devices = adapter.getBondedDevices();
				if(devices.size()>0)    
				{//用迭代
					for(Iterator<BluetoothDevice> iterator = devices.iterator();iterator.hasNext();)
					{
						//得到BluetoothDevice对象,也就是说得到配对的蓝牙适配器
						BluetoothDevice device = (BluetoothDevice)iterator.next();
						//得到远程蓝牙设备的地址
						Log.d("mytag",device.getAddress());
						address = device.getAddress();
						name = device.getName();
						if(name.equals("EMI0001"))
							startClient();
					}     
				}
			}
			else
			{
				System.out.println("没有蓝牙设备");
			}
		}
		 


		public void delay(int ms){  
		       try {  
		           Thread.currentThread();  
		           Thread.sleep(ms);  
		       } catch (InterruptedException e) {  
		           e.printStackTrace();  
		       }   
		    }  



		public void startClient()
		{
			 device = mBluetoothAdapter.getRemoteDevice(address); 
			 clientConnectThread = new clientThread();
			 clientConnectThread.start();
		}
		
		 //开启客户端
		public class clientThread extends Thread {   
		  public void run() {
		   try {
		   socket = device.createRfcommSocketToServiceRecord(UUID.fromString("00001101-0000-1000-8000-00805F9B34FB"));
		   socket.connect();
		  // mreadThread = new readThread();
   	       //mreadThread.start();
	       
		   
		   } catch (IOException e) 
		   { 
			   e.printStackTrace();
		   } 
		}
	};	 
			
		 
	public class readThread extends Thread { 
        public void run() {
         
        byte[] buffer = new byte[1024];
        //int bytes;
        InputStream mmInStream = null;   
        try {
        	 	mmInStream = socket.getInputStream();
        	} catch (IOException e1) {
        		e1.printStackTrace();
        	} 
	    while (true) {
	    try {
	    		if( (bytes = mmInStream.read(buffer)) > 0 )
	    		{ 
	    			System.out.println("success");
	    			
	    			//byte[] buf_data = new byte[bytes];
	    			for(int i=0; i<bytes; i++)
	    			{
	    				buf_data[i] = buffer[i];
	    			}

	    		}
	    	} catch (IOException e) {
	    try {		    		
	    		mmInStream.close();
	    	} catch (IOException e1) {
	    		e1.printStackTrace();
	    	}
	    }
	}
}
}
		 
		 public void sendBTMessage(byte[] cmd) 
		  {  
				 if (socket == null) 
				 {	   
				  return;
				 }
				 try {    
				  OutputStream os = socket.getOutputStream();   
				  os.write(cmd);
				  os.flush();
				 } catch (IOException e) {
				   e.printStackTrace();
				}   
		  }
	 

}
