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
			//�õ�BluetoothAdapter����
			BluetoothAdapter adapter = BluetoothAdapter.getDefaultAdapter();
			//�ж�BluetoothAdapter�����Ƿ�Ϊ�գ����Ϊ�գ����������û�������豸
			if(adapter != null)
			{
				System.out.println("����ӵ�������豸");
				//����isEnabled()�����жϵ�ǰ�����豸�Ƿ����
				if(!adapter.isEnabled())
				{     
				//��������豸�����õĻ�,����һ��intent����,�ö�����������һ��Activity,��ʾ�û���������������
				Intent intent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
				startActivity(intent);
				}
				//�õ������Ѿ���Ե���������������
				Set<BluetoothDevice> devices = adapter.getBondedDevices();
				if(devices.size()>0)    
				{//�õ���
					for(Iterator<BluetoothDevice> iterator = devices.iterator();iterator.hasNext();)
					{
						//�õ�BluetoothDevice����,Ҳ����˵�õ���Ե�����������
						BluetoothDevice device = (BluetoothDevice)iterator.next();
						//�õ�Զ�������豸�ĵ�ַ
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
				System.out.println("û�������豸");
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
		
		 //�����ͻ���
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
