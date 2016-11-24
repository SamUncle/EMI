package com.icedcap.QJMeter;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Iterator;
import java.util.Set;
import java.util.UUID;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import com.icedcap.QJMeter.R;

import com.icedcap.QJMeter.Community.SendThread;


import android.widget.EditText;

public class SingleMeter extends Activity{
	TextView myTextView;  
    Button sendButton;  
//    MyReceiver receiver;  
//    IBinder serviceBinder;  
//    BTService mService;  
//    Intent _intent;  
    int value = 0;  
      
    /**************service ����*********/   
//    static final int CMD_STOP_SERVICE = 0x01;  
//    static final int CMD_SEND_DATA = 0x02;  
//    static final int CMD_SYSTEM_EXIT =0x03;  
//    static final int CMD_SHOW_TOAST =0x04; 
	 private Button connectBtn = null;
	 private Button readBtn = null;
	 private Button clearBtn = null;
	 private CheckBox select = null;
	 private EditText textmeteraddr = null; 
	 private TextView textfirmcode = null;
	 private TextView textmeterdata = null;
	 private TextView stateText = null;
	 private Handler handler;
	 public BluetoothDevice device = null;
	 public BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
	 public clientThread clientConnectThread = null;
	 public BluetoothSocket socket = null;
	 public readThread mreadThread = null;
	 public SendThread mSendThread = null;
	 
	 public boolean BTConnnect = false;
	 String address=null;
	 String name = null;
	 
	 private String  strhex="";
	 String strMeterAddr ="" ;
	 String strFirmCode = "" ;
	 String strMeterData = "" ;
	 String strChanleCode="" ;
	 String strChanleDate = "" ;
	 byte addr[] = {0,0,0,0,0,0,0};
	 boolean breadaddr = false ;
	 boolean autoflag = true ;
	 boolean connectflag = false ;
	 
	 
	 public void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        setContentView(R.layout.singlemeter); 
	        
//	        _intent = new Intent(SingleMeter.this,BTService.class);  
//	        startService(_intent); 
	        
	        connectBtn = (Button) findViewById(R.id.connect);
			readBtn = (Button) findViewById(R.id.read);
			clearBtn = (Button) findViewById(R.id.clear);
			textmeteraddr = (EditText) findViewById(R.id.textmeteraddr);
			textfirmcode = (TextView) findViewById(R.id.textfirmcode);
			textmeterdata = (TextView) findViewById(R.id.textmeterdata);
			stateText = (TextView)findViewById(R.id.constate);
			select = (CheckBox)findViewById(R.id.checkBox1);
			stateText.setTextColor(android.graphics.Color.RED);
			
			ReceiveMsg(); 
	 

			connectBtn.setOnClickListener(new View.OnClickListener() {  
				
		    	 @Override  
		    	 
		    	 public void onClick(View v) {
		    		 if(!BTConnnect){
//		    			 BTConnnect = true;//11.21
		    			 ConnectBTDevice();
		    		  }
		    		  else
		    		  {
		    			  try {
		    			    if(BTConnnect)
							socket.close();
							stateText.setText("����δ����");
							stateText.setTextColor(android.graphics.Color.RED);
						      } catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
					   }
		    			     connectBtn.setText("��������");
		    			     BTConnnect = false;
		    		   }
		    	   }
		    	
		    });
	
	 
			 readBtn.setOnClickListener(new View.OnClickListener() {  
		    	 @Override  
		    	 public void onClick(View v) {
		    		 if(select.isChecked())
		    		 {
		    			 readappointMeter();
		    		 }
		    		 else
		    	         readMeterAddr();
		    	   }
		    });
			 clearBtn.setOnClickListener(new View.OnClickListener() {  
		    	 @Override  
		    	 public void onClick(View v) {
		    	   Message message = new Message();
		  	       message.what = 3;
		  	       handler.sendMessage(message);
		    	   }
		    });
	 }
	 
		protected void readappointMeter() {
		// TODO Auto-generated method stub
			byte  hexcheck=0x00;
			breadaddr = true;
			//byte[] cmd = {(byte)0xFE,(byte)0xFE,(byte)0xFE,(byte)0xFE,0x68,0x10,0x00,0x00,0x00,0x00,0x00,0x01,0x10,0x01,0x03,(byte)0x90,0x1F,0x00,0x00,0x16};//�Ϸ�
			byte[] cmd = {(byte)0xFE,(byte)0xFE,(byte)0xFE,(byte)0xFE,0x68,0x10,0x00,0x00,0x00,0x00,0x00,0x01,0x10,0x01,0x03,0x1F,(byte)0x90,0x00,0x00,0x16};
			String editbuf = textmeteraddr.getText().toString();
			int a = editbuf.length();
			
			if(a>0)
			{
				for(;a<10;a++){
					editbuf = "0"+editbuf;
				}
				System.out.println(editbuf);
				byte[] srtbyte ={0,0,0,0,0};
				srtbyte =stringToBytes(getHexString(editbuf));
				
				
				
				
				cmd[6] = srtbyte[4];
				cmd[7] = srtbyte[3];
				cmd[8] = srtbyte[2];
				cmd[9] = srtbyte[1];
				cmd[10] = srtbyte[0];

				
				
				
				for(int k=4;k<=17;k++)
				{
					hexcheck+=cmd[k];
				}
				cmd[18]=(byte)hexcheck;
				strhex="";
				  
				sendBTMessage(cmd);
				delay(600);	
			}
		
	}
		 public byte[] stringToBytes(String s) {
		        byte[] buf = new byte[s.length() / 2];
		        for (int i = 0; i < buf.length; i++) {
		            try {
		                buf[i] = (byte) Integer.parseInt(s.substring(i * 2, i * 2 + 2), 16);
		            } catch (NumberFormatException e) {
		                e.printStackTrace();
		            }
		        }
		        return buf;
		    }
	
		private String getHexString(String buf) {
	        String s = buf;
	        StringBuilder sb = new StringBuilder();
	        for (int i = 0; i < s.length(); i++) {
	            char c = s.charAt(i);
	            if (('0' <= c && c <= '9') || ('a' <= c && c <= 'f') ||
	                    ('A' <= c && c <= 'F')) {
	                sb.append(c);
	            }
	        }
	        if ((sb.length() % 2) != 0) {
	            sb.deleteCharAt(sb.length());
	        }
	        return sb.toString();
	    }
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
		   BTConnnect = true;
		   
		   mreadThread = new readThread();
	       mreadThread.start();
	       
	       Message message = new Message();
	       message.what = 2;
	       handler.sendMessage(message);
		   } catch (IOException e) 
		   { 
			   try {
				socket.close();
				socket = null;
				BTConnnect = false;	
		        } catch (Exception e2) {
				// TODO: handle exception
			}
			   Log.i("�쳣��Ϣ",e.toString());
		   } 
		}
	};	 
			
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
	 

	 

	 public class readThread extends Thread { 
	        public void run() {
	        	//System.out.println("success");
	         
	        byte[] buffer = new byte[1024];
	        int bytes;
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
		    			
		    			byte[] buf_data = new byte[bytes];
		    			for(int i=0; i<bytes; i++)
		    			{
		    				buf_data[i] = buffer[i];
		    			}
		    			strhex = strhex+ DigitalTrans.byte2hex(buf_data);
		    			
		    			if(breadaddr == false)
		    			{
		    				if(strhex.length()>=36)
		    				{
		    					byte[] addrinfo =  DigitalTrans.hex2byte(strhex);
		    					int n=0;
		    					while(addrinfo[n++]!=0x68) 
		    					{
		    						if(n>18)
		    						{
		    							break;
		    						}
		    					}
						    	if(n<22)
						    	{
						    		strMeterAddr = "" ;
						    		strFirmCode = "" ;
				    				for(int l=0;l<5;l++)
							    	 {
				    					String str1 = DigitalTrans.algorismToHEXString(addrinfo[1+n+l]);
				    					addr[l] = addrinfo[1+n+l];
				    					strMeterAddr=str1+strMeterAddr;
							    	 }	
				    				for(int l=0;l<2;l++)
							    	 {
				    					String str2 = DigitalTrans.algorismToHEXString(addrinfo[6+n+l]);
				    					addr[5+l] = addrinfo[6+n+l];
				    					strFirmCode=str2+strFirmCode;
							    	 }
				    				strhex ="";
				    				breadaddr = true;
				    				
				    				Message message = new Message();
							        message.what = 0;
							        handler.sendMessage(message);

				    				
						    	}
						    	bytes = 0;
		    				}
		    			}
		    			else
		    			{
		    				if(strhex.length()>=48)
			    			{
		    					strFirmCode = "" ;
		    					strMeterData = "";
			    				byte[] Meterinfo =  DigitalTrans.hex2byte(strhex);
			    				int m=0;
			    				while(Meterinfo[m++]!=0x68) ;
			    				byte[] addr =new  byte[5];
			    				for(int j=0;j<5;j++)
						    	{
							        addr[4-j]=Meterinfo[1+m+j];
						    	}
						    	 byte[]  ReadNum=new  byte[2];
						    	 
						    	 for(int k=0;k<2;k++)
						    	 {
						    		 ReadNum[1-k]=Meterinfo[m+14+k];
						    	 }
						    	 strMeterData =DigitalTrans.byte2hex(ReadNum);
						    	 
						    	 for(int l=0;l<2;l++)
						    	 {
			    					String str2 = DigitalTrans.algorismToHEXString(Meterinfo[m+6+l]);
			    					strFirmCode=str2+strFirmCode;
						    	 }
			    				 strhex="";
			    				 //str2 ="";
			    				 breadaddr = false;
			    				 Message message = new Message();
							     message.what = 1;
						         handler.sendMessage(message); 
			    			}
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
	 private void readMeterAddr() {
		   //byte[] cmd = {(byte)0xFE,(byte)0xFE,0x68,0x10,(byte)0xAA,(byte)0xAA,(byte)0xAA,(byte)0xAA,(byte)0xAA,(byte)0xAA,(byte)0xAA,0x03,0x03,(byte)0x81,(byte)0x0A,0x01,(byte)0xB0,0x16};	//�Ϸ�	 
		 byte[] cmd = {(byte)0xFE,(byte)0xFE,0x68,0x10,(byte)0xAA,(byte)0xAA,(byte)0xAA,(byte)0xAA,(byte)0xAA,(byte)0xAA,(byte)0xAA,0x03,0x03,(byte)0x0A,(byte)0x81,0x01,(byte)0xB0,0x16};  
		 sendBTMessage(cmd);
		 delay(600);
	}
	 private boolean isBackCliecked = false;

		@SuppressLint("NewApi")
		@Override
		public boolean onKeyDown(int keyCode, KeyEvent event) {
			if (keyCode == KeyEvent.KEYCODE_BACK) {
				if (isBackCliecked) {
					try {
						if(clientConnectThread!=null)
						socket.close();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					this.finish();
				} else {
					isBackCliecked = true;
					Toast t = Toast.makeText(this, "�ٰ����ؼ����Ͽ��������ӣ��˳��ý���",
							Toast.LENGTH_LONG);
					t.setGravity(Gravity.CENTER, 0, 0);
					t.show();
				}
			}
			return true;
		}
	 public void  ReceiveMsg(){
		    handler = new Handler() {
		        @Override
		        public void handleMessage(Message msg) {
		            switch (msg.what) {
		            case 0:
		            	textmeteraddr.setText(strMeterAddr);
		            	//textfirmcode.setText(strFirmCode);
		            	readSingleMeter(addr) ;
		                break;
		            case 1:
		            	textmeterdata.setText(strMeterData);
		            	textfirmcode.setText(strFirmCode);
		            	break;
		            case 2:
		            	stateText.setText("����������");
		            	stateText.setTextColor(android.graphics.Color.BLUE);
		            	connectBtn.setText("�ر������豸");//11.22
		            	break;
		            case 3:
		            	textmeteraddr.setText("");
		    			textfirmcode.setText("");
		    			textmeterdata.setText("");
		            	break;
		            }
		        }

		        private void readSingleMeter(byte[] addr) {
					// TODO Auto-generated method stub
					byte  hexcheck=0x00;
					//byte[] cmd = {(byte)0xFE,(byte)0xFE,(byte)0xFE,(byte)0xFE,0x68,0x10,0x00,0x00,0x00,0x00,0x00,0x33,0x78,0x01,0x03,(byte)0x90,0x1F,0x00,0x00,0x16};//�Ϸ�
					byte[] cmd = {(byte)0xFE,(byte)0xFE,(byte)0xFE,(byte)0xFE,0x68,0x10,0x00,0x00,0x00,0x00,0x00,0x01,0x10,0x01,0x03,0x1F,(byte) 0x90,0x00,0x00,0x16};
					for(int i=0;i<5;i++)
					{
						cmd[6+i]=addr[i];
				   	}
					for(int j=0;j<2;j++)
				    {
						cmd[11+j]=addr[j+5];
					}
					for(int k=4;k<=17;k++)
					{
						hexcheck+=cmd[k];
					}
					cmd[18]=(byte)hexcheck;
					strhex="";
					sendBTMessage(cmd);
					delay(600);					
				}
		  };
	 }
//	 public class  MyReceiver extends BroadcastReceiver {
//			@Override
//			public void onReceive(Context context,Intent intent){
//				if (intent.getAction().equals("android.intent.action.1xx")) {
//					Bundle bundle = intent.getExtras();
//					int cmd = bundle.getInt("cmd");
//					if (cmd == CMD_SHOW_TOAST) {
//						String str = bundle.getString("str");
//						//showToast(str);
//					}else if (cmd == CMD_SYSTEM_EXIT) {
//						System.exit(0);
//						
//					}
//				}
//			}
//		}
//	   
//	      
//	    @Override  
//	    protected void onDestroy() {  
//	        // TODO Auto-generated method stub  
//	        super.onDestroy();  
//	          
//	        if(receiver!=null){  
//	            SingleMeter.this.unregisterReceiver(receiver);  
//	        }  
//	    }  
//	  
//	     
//	  
//	  
//	    @Override  
//	    protected void onResume() {  
//	        // TODO Auto-generated method stub  
//	        super.onResume();  
//	        receiver = new MyReceiver();  
//	        IntentFilter filter=new IntentFilter();  
//	        filter.addAction("android.intent.action.lxx");  
//	        SingleMeter.this.registerReceiver(receiver,filter);  
//	    }  
//	  
//	    public void showToast(String str){//��ʾ��ʾ��Ϣ  
//	        Toast.makeText(getApplicationContext(), str, Toast.LENGTH_SHORT).show();      
//	    }  
//	  
//	  
//	   
//	  
//	    public void sendCmd(byte command, int value){  
//	        Intent intent = new Intent();//����Intent����  
//	        intent.setAction("android.intent.action.cmd");  
//	        intent.putExtra("cmd", CMD_SEND_DATA);  
//	        intent.putExtra("command", command);  
//	        intent.putExtra("value", value);  
//	        sendBroadcast(intent);//���͹㲥      
//	    }  
}    
		    
	 
	 
	 



