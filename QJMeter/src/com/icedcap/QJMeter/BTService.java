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
    /****service����***/
    static final int CMD_STOP_SERVICE = 0x01;
    static final int CMD_SEND_DATA = 0x02;
    static final int CMD_SYSTEM_EXIT = 0x03;
    static final int CMD_SHOW_TOAST = 0x04;
    /**��������**/
    private BluetoothAdapter mBluetoothAdapter = null;
    private BluetoothSocket btSocket = null;
    private OutputStream outStream = null;
    private InputStream inStream = null;
    public boolean bluetoothFlag = true;
    private static final UUID MY_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
    String BT_Addr = null;//���ӵ���������MAC��ַ
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
		filter.addAction("android.intent.action.cmd");//ע��һ���㲥���ڽ���Activity���������������service��Ϊ
	    registerReceiver(cmdReceiver, filter);
	    //���÷��������߳�;
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
   //ֹͣ����
   public void stopService() {
	threadFlag = false;
	stopSelf();
   }
   public void doJob(){      
       mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();  
      if (mBluetoothAdapter == null) {   
          DisplayToast("�����豸�����ã����������");  
          bluetoothFlag  = false;  
          return;  
      }  

      if (!mBluetoothAdapter.isEnabled()) {  
          DisplayToast("����������������г���");  
          bluetoothFlag  = false;  
          stopService();  
          showToast("����������������г���");  
          return;  
      }        
      showToast("�����������豸!");        
      threadFlag = true;   
      //�����߳���������
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
   
   
//�½��߳�
   public class MyThread extends Thread{
	   @Override 
	   public void run(){
		   super.run();
		   //���������豸
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
       DisplayToast("���ڳ������������豸�����Ժ󡤡�����");  
       //�õ������Ѿ���Ե���������������
		Set<BluetoothDevice> devices = mBluetoothAdapter.getBondedDevices();
		if(devices.size()>0)    
		{//�õ���
			for(Iterator<BluetoothDevice> iterator = devices.iterator();iterator.hasNext();)
			{
				//�õ�BluetoothDevice����,Ҳ����˵�õ���Ե�����������
				BluetoothDevice device = (BluetoothDevice)iterator.next();
				//�õ�Զ�������豸�ĵ�ַ
				Log.d("mytag",device.getAddress());
				BT_Addr = device.getAddress();
				BT_Name = device.getName();
				if(BT_Name.equals("EMI0001")){
					//��������
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
             } //�󶨶��ӿ�  
               
          try {  
               outStream = btSocket.getOutputStream();  
              } catch (IOException e) {  
               e.printStackTrace();  
              } //��д�ӿ�  
             
       }  
   } 
 //�����ͻ���
 	public class clientThread extends Thread {   
 		  public void run() {
 			 BluetoothDevice device = mBluetoothAdapter.getRemoteDevice(BT_Addr);  
 		       try {  
 		          btSocket = device.createRfcommSocketToServiceRecord(MY_UUID);  
 		       } catch (IOException e) {  
 		           DisplayToast("�׽��ִ���ʧ�ܣ�");  
 		           bluetoothFlag = false;  
 		       }  
 		       DisplayToast("�ɹ����������豸��");  
 		       mBluetoothAdapter.cancelDiscovery();  
 		       try {  
 		               btSocket.connect();  
 		               DisplayToast("���ӳɹ����������Կ�ʼ�ٿ���!");  
 		               showToast("���ӳɹ����������Կ�ʼ�ٿ���!");  
 		               bluetoothFlag = true;  
 		       } catch (IOException e) {  
 		              try {  
 		                   btSocket.close();  
 		                   bluetoothFlag = false;  
 		               } catch (IOException e2) {                          
 		                  DisplayToast("����û�н������޷��ر��׽��֣�");  
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
   public void sendCmd(byte cmd, int value)//���ڷ�������  
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
	//�½�һ���㲥������
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
