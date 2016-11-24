package com.icedcap.QJMeter;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import com.icedcap.QJMeter.DigitalTrans;
import com.icedcap.QJMeter.R;
//import com.icedcap.gridviewtest.Community.SendThread;
//import com.icedcap.gridviewtest.Community.clientThread;
//import com.icedcap.gridviewtest.Community.readThread;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

public class Community extends Activity {
	

	
	 private ListView lv = null;
	 private Button startButton = null;
	 private Button conButton = null;
	 private Button failButton = null;
	 private TextView titleText = null;
	 
	 private TextView stateText = null;
	 private Handler handler;
	 
//	 private String strlastData="";
	 
	 private String  strhex="";
	 private Handler handler2;
	 
	 String strChannel ="" ;
	 String strchanleCode="";
	 List<String> allItem = new ArrayList<String>();//һ����������Ϣ
	 
	 List<String> sortItem = new ArrayList<String>();//���ظ���ͨ����
	 List<String> Itemshow = new ArrayList<String>();//��ʾ����Ϣ
	 List<String> failItem = new ArrayList<String>();//ʧ����Ϣ
	  
	 ArrayAdapter<String> adapter;
	 
	 private String strMeterAddr="";
		 
	 boolean recvFinish = false;
	 boolean showAllInfo = false;
	 boolean BTConnnect = true;
	 boolean breadchannel = true;
	 
	 private SQLiteOpenHelper helper =null;
     private MyOperator mytab=null;
	 
	 String Uptownname ;
	 private Yhxx  curYh ;
	 //private int curmonth;
	 
   @Override
public void onCreate(Bundle savedInstanceState) {
       super.onCreate(savedInstanceState);
       setContentView(R.layout.community);
       
//     final Calendar c = Calendar.getInstance();
//
//     curmonth = c.get(Calendar.MONTH)+1;//��ȡ��ǰ�·�
     
       
       
       this.helper=new SQLiteHelper(this);//���ݿ����������
       
       titleText = (TextView)findViewById(R.id.title);
       stateText = (TextView)findViewById(R.id.state);
       failButton = (Button)findViewById(R.id.failed);
       
       stateText.setTextColor(android.graphics.Color.RED);
       
       ReceiveMsg();
       
       lv = (ListView) findViewById(R.id.lv);
       adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1, Itemshow);   
       lv.setAdapter(adapter);
   
       Bundle bundle=getIntent().getExtras();
       Uptownname=bundle.getString("name");
       Uptownname +=".txt";
        	
       try {
       	    readSDFileLine(Uptownname);
          } catch (IOException e) {
       }
       
       Itemshow.clear();
       
     //  String[] stritem = (String[])allItem.toArray(new String[allItem.size()]);
    		   
    //   String detail[] =stritem[0].split("\\$");
    //   strchanleCode= detail[8];
       

       
       startButton = (Button)findViewById(R.id.read);
       
       startButton.setOnClickListener(new View.OnClickListener() {  
    	   @Override  
    	   public void onClick(View v) {

    		   failItem.clear();
    		   strChannel="";
    		   strhex="";
    		   readchannel();

    	   }
    	});
       

       
       failButton.setOnClickListener(new View.OnClickListener() {  
    	   @Override  
    	   public void onClick(View v) {
    		   if(showAllInfo == true)
    		   {		   
    			      Itemshow.clear();
    	   			  failButton.setText("ʧ����Ϣ");
    	   			  showAllInfo = false;
    		   }else//��ʾʧ����Ϣ
    		   {
          		  failButton.setText("����");
         		  titleText.setText("�쳣��Ϣ");
          		  showAllInfo = true;
          		  
  		    	  for(String tmp:failItem){
		    		Itemshow.add(tmp);
		    	  }       		 
    		   }
    		        		  
    		  adapter.notifyDataSetChanged();
    	   }
    	}); 
       
       
       
        conButton = (Button)findViewById(R.id.connect);
       
        conButton.setOnClickListener(new View.OnClickListener() {  
    	   @Override  
    	   public void onClick(View v) {
    		     		  
    		   if(BTConnnect == true){
    			  conButton.setText("�����Ͽ�");
    			  BTConnnect = false;
    			  ConnectBTDevice();
    		   }
    		   else
    		   {
    			  
    			  try {
    				  if(socket!=null)
    				  {
    					  socket.close();
    				  }
					stateText.setText("����δ����");
					stateText.setTextColor(android.graphics.Color.RED);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
    			  conButton.setText("��������");
    			  BTConnnect = true;
    		   }
    		  
    	   }
    	});
   }
   
   private BluetoothDevice device = null;
	private BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
	private clientThread clientConnectThread = null;
	private BluetoothSocket socket = null;
	public readThread mreadThread = null;
	public SendThread mSendThread = null;
   String address=null;
   String name = null;
   
	private void ConnectBTDevice()
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
					BluetoothDevice device = iterator.next();
					//�õ�Զ�������豸�ĵ�ַ
					Log.d("mytag",device.getAddress());
					address = device.getAddress();
					name = device.getName();
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
	 
	private void startClient()
	{
		 device = mBluetoothAdapter.getRemoteDevice(address); 
		 clientConnectThread = new clientThread();
		 clientConnectThread.start();
	}
	
	 //�����ͻ���
	 private class clientThread extends Thread {   
	  @Override
	public void run() {
	   try {
	    //����һ��Socket���ӣ�ֻ��Ҫ��������ע��ʱ��UUID��
	    socket = device.createRfcommSocketToServiceRecord(UUID.fromString("00001101-0000-1000-8000-00805F9B34FB"));
	    
	    //����    
	    socket.connect();
	    
	    Message message = new Message();
       message.what = 3;
       handler.sendMessage(message);
	    //������������
//	    mreadThread = new readThread();
//	    mreadThread.start();
	   } 
	   catch (IOException e) 
	   {
		   
	   } 
	  }
	 };	 
	 public class SendThread extends Thread { 
	        @Override
			public void run() {
	         
	        	readMeter(strChannel);
	           
	        }
	 }
	 
	 public class readThread extends Thread { 
	        @Override
			public void run() {
	         
	            byte[] buffer = new byte[1024];
	            int bytes;
	          
	            InputStream mmInStream = null;
	            
	        try {
	        	 	mmInStream = socket.getInputStream();
	        	} catch (IOException e1) {
	    // TODO Auto-generated catch block
	        		e1.printStackTrace();
	        	} 
		while (true) {
		    try {
		        // Read from the InputStream
		    		if( (bytes = mmInStream.read(buffer)) > 0 )
		    		{ 
		    			System.out.println("success");
		    			
		    			byte[] buf_data = new byte[bytes];
		    			for(int i=0; i<bytes; i++)
		    			{
		    				buf_data[i] = buffer[i];
		    			} 

		    			strhex = strhex+ DigitalTrans.byte2hex(buf_data);
		    			
		    			if(breadchannel==true)
		    			{
		    				if(strhex.length()>=30)
		    				{
		    				
		    					byte[] channelinfo =  DigitalTrans.hex2byte(strhex);
		    					int n=0;
		    					while(channelinfo[n++]!=0x68) 
		    					{
		    						if(n>15)
		    						{
		    							break;
		    						}
		    					}
			    				
						    	if(n<15)
						    	{
						    		int channelnum =0;
				    				for(int l=0;l<6;l++)
							    	 {			    				
				    					channelnum +=(int) ((channelinfo[4+n+l]-0x30) * (Math.pow(10, l)));
							    	 }	

				    				strChannel =String.valueOf(channelnum);
				    				strhex ="";
				    				breadchannel =false;
								    Message message = new Message();
							        message.what = 2;
							        handler.sendMessage(message);
						    	}
		    					
		    				}
		    			}
		    			else
		    			{
		    				if(strhex.length()>=74)
			    			{
			    				byte[] Meterinfo =  DigitalTrans.hex2byte(strhex);
			    				
			    				int m=0;
			    				while(Meterinfo[m++]!=0x68) ;
			    				byte[] addr =new  byte[5];
						    	 
			    				for(int j=0;j<5;j++)
						    	 {
							    	addr[4-j]=Meterinfo[1+m+j];
						    	 }
						    	 
						    	// String strMeterAddr  = DigitalTrans.byte2hex(addr);
						    	 byte[]  ReadNum=new  byte[2];
						    	 
						    	 for(int k=0;k<2;k++)
						    	 {
						    		 ReadNum[1-k]=Meterinfo[m+14+k];
						    	 }
						    	 
						    	
	  				    	     String strReadNum =DigitalTrans.byte2hex(ReadNum);
	  				    	     

						    	 int curdata = Integer.parseInt(strReadNum);
						    	 
						    	 curYh.curdata =curdata ;
						    	 
		
//						    	 int curysl =curdata - curYh.lastdata;
//	  				    	     if((curysl>50)||(curysl<0))
//	  				    	     {
//	  				    	    	curYh.state =2 ;
//	  				    	    	curYh.curyl =curysl;
//	  				    	     } 
//	  				    	     else 
//	  				    	     {
//	  				    	    	curYh.state =1 ;
//	  				    	    	curYh.curyl =curysl;
//	  				    	     }
						    	 curYh.state =1 ;
	  				    	     mytab = new MyOperator(helper.getWritableDatabase());	  				    	       
	  				    	boolean sameflag = mytab.check_same(curYh.accountnum);
	  				    	if(sameflag == false)
	  				    	{
	  				    		 mytab.insert(curYh);
	  				    	}else {
								mytab.updateCurdata(curYh.accountnum, curYh.curdata);
							}
	  				    	    
							    	 
//			    	
//			    				 String strfileName ="";
//			    				 strfileName+=Uptownname;
//			    				 writeSDFile(strfileName,"tmp",strContent);	
			    				 strhex="";
			    				 recvFinish = true;
			    				
			    	
			    			}
		    			}
		    			
		    			
		    			
		    
		    		}
		    	} catch (IOException e) {
		    try {		    		
		    		mmInStream.close();
		    	} catch (IOException e1) {
		  // TODO Auto-generated catch block
		    		e1.printStackTrace();
		    	}
		  //break;
		 }
		}
	  }
}
	 

   //��������   scy modified  2016-6-21
//// private void sendMessageHandle(String msg) 
	  private void sendBTMessage(byte[] cmd) 
	  {  
			 if (socket == null) 
			 {	   
			  return;
			 }
			 try {    
			  OutputStream os = socket.getOutputStream(); 
			  
			//  byte cmd[] = msg.getBytes();      
			  os.write(cmd);
			  os.flush();
			 } catch (IOException e) {
			  // TODO Auto-generated catch block
			   e.printStackTrace();
			}   
	  }
 
   
   //���ļ�  
	public void readSDFileLine(String fileName) throws IOException {	
		String sdStatus = Environment.getExternalStorageState();
		if (!sdStatus.equals(Environment.MEDIA_MOUNTED)) {
			Log.v("TestFile","SD card is not avaiable/writeable right now.");
			return;
		}		
		String SDPATH = Environment.getExternalStorageDirectory().getPath(); 
		fileName = SDPATH + "/Android/" + fileName;    	
       File file = new File(fileName);
       FileInputStream fis = new FileInputStream(file);     
       InputStreamReader inputreader = new InputStreamReader(fis,"utf-8");
       BufferedReader buffreader = new BufferedReader(inputreader);
       String line;
       //���ж�ȡi
       
       while (( line = buffreader.readLine()) != null) {
       	allItem.add(line);
       }
       fis.close();
       
       Iterator<String> it1 = allItem.iterator();
       while(it1.hasNext()){
       	String strLine = it1.next();
       	String s0[] = strLine.split("\\$");
       	
       	boolean find = false;
       	for(String tmp:sortItem){
      		
       		if(s0[3].equals(tmp)){
       			find = true;
       			break;
       		}
       	}
       	if(find == false)
       		sortItem.add(s0[3]);   
       }

   }
   
	
	private void delay(int ms){  
       try {  
           Thread.currentThread();  
           Thread.sleep(ms);  
       } catch (InterruptedException e) {  
           e.printStackTrace();  
       }   
    }  
	
	private void readchannel(){  
		 breadchannel =true;
//		   byte[] cmd = {(byte)0xFE,(byte)0xFE,(byte)0xFE,(byte)0xFE,0x68,0x10,0x00,0x00,0x00,0x00,0x00,0x33,0x78,0x01,0x03,0x1F,(byte)0x90,0x13,0x00,0x16};
//		   byte[] cmd = {(byte)0xFE,(byte)0xFE,(byte)0xFE,(byte)0xFE,0x68,0x10,0x00,0x00,0x00,0x00,0x00,0x33,0x78,0x01,0x03,(byte)0x90,0x1F,0x00,0x00,0x16};
//	       byte[] cmd = {(byte)0xFE,(byte)0xFE,0x68,0x00,0x00,0x00,0x00,0x00,0x00,0x42,0x00,0x31,0x03,0x01,(byte)0x89,0x01,0x69,0x16};
		 
		   byte[] cmd = {0x6A,0x10,0x02,(byte)0xAA,0x01,0x27,0x16};
		 
//		   for(int j=0;j<hexCode.length;j++)
//		   {
//			   cmd[9+j]=hexCode[j];
//		   }
//		   
//		   for(int k=2;k<=15;k++)
//		   {
//			   hexcheck+=cmd[k];
//		   }
//		   cmd[16]=(byte)hexcheck;
		 sendBTMessage(cmd);
		 delay(600);//2000		

    } 
	
	private void readMeter(String ch)
	{
		for(String tmp:allItem){
			String s[] = tmp.split("\\$");
			if(s[3].equals(ch) ){
				//setTitle(tmp);
				
				   curYh =new Yhxx();
				   curYh.accountnum = s[0];
				   curYh.meteraddr =s[4];
//				   curYh.lastdata = Integer.parseInt(s[3].trim());
				   curYh.useraddr = s[2];
				
//				   long time = System.currentTimeMillis();
//				   int timm1 = (int)time;
				   
			       final Calendar c = Calendar.getInstance();

//				          curmonth = c.get(Calendar.MONTH)+1;//��ȡ��ǰ�·�
				   
				   int time = (c.get(Calendar.YEAR)*10000)+((c.get(Calendar.MONTH)+1)*100)+c.get(Calendar.DAY_OF_MONTH);
				   curYh.readtime =  time;
		//		   String addrtest = curYh.useraddr;
		
				   
				
				//   strlastData =s[3];
				
				   String strAddr = s[4]; 
				   String strSendAddr  = DigitalTrans.patchHexString(strAddr,10);
//				   String strCode=  s[8];
//				   
				   byte[] hexAddr  = DigitalTrans.hex2byte(strSendAddr);
//				  // byte[] hexAddr  = DigitalTrans.hexStringToByte(strSendAddr);
//				   byte[] hexCode  = DigitalTrans.hexStringToByte(strCode);
				   byte  hexcheck=0x00;
				   
				  //String strSendAddr=  DigitalTrans.algorismToHEXString(Integer.parseInt(strAddr),10);
				   
				  //String strSendCode=  DigitalTrans.algorismToHEXString(Integer.parseInt(strCode),10);
				   
				  //String strCmd=  "FEFE6810190000000033780103901F00EF16";
				  //����ʽˮ�� 
		//		   byte[] cmd = {(byte)0xFE,(byte)0xFE,(byte)0xFE,(byte)0xFE,0x68,0x10,0x00,0x00,0x00,0x00,0x00,0x33,0x78,0x01,0x03,0x1F,(byte)0x90,0x13,0x00,0x16};
				   byte[] cmd = {(byte)0xFE,(byte)0xFE,(byte)0xFE,(byte)0xFE,0x68,0x10,0x00,0x00,0x00,0x00,0x00,0x33,0x78,0x01,0x03,0x1F,(byte)0x90,0x00,0x00,0x16};
				   
				   for(int i=0;i<hexAddr.length;i++)
				   {
					   cmd[6+i]=hexAddr[hexAddr.length-1-i];
				   }
//				   for(int j=0;j<hexCode.length;j++)
//				   {
//					   cmd[11+j]=hexCode[j];
//				   }
				   
				   for(int k=4;k<=17;k++)
				   {
					   hexcheck+=cmd[k];
				   }
				   cmd[18]=hexcheck;

				    strMeterAddr =strAddr;
				    Message message = new Message();
			        message.what = 0;
			        handler.sendMessage(message);

			        
				   recvFinish =false;
				   strhex="";
				   sendBTMessage(cmd);
				    delay(600);//2000 			
				//sendBTMessage(tmp);
				//wait for receive finish.	
				int count = 0;
				while(recvFinish == false){					
					if(++count > 3){	  				    	     
						mytab = new MyOperator(helper.getWritableDatabase());	  				    	       
						curYh.state=2 ;
						curYh.curdata =-1;
			    	    mytab.insert(curYh);
					//	strMeterAddr =strAddr;
						Message message1 = new Message();
				        message1.what = 2;
				        Bundle bundle= new Bundle();
				        bundle.putString("key", strAddr);
				        message1.setData(bundle);
				        handler2.sendMessage(message1);   
				       // failItem.add(strAddr);
						break;
					}else{
					 delay(600);
					}
				}
			}
		}
		
	    TBSDDirFile(Uptownname);
	    Message message = new Message();
       message.what = 1;
       handler.sendMessage(message);
		
	}
	
   //д�ļ�  
   public void writeSDFile(String fileName,String dir, String write_str) throws IOException{
		String sdStatus = Environment.getExternalStorageState();
		if (!sdStatus.equals(Environment.MEDIA_MOUNTED)) { //���sd�Ƿ����
			Log.v("TestFile","SD card is not avaiable/writeable right now.");
			return;
		}
		
		String SDPATH = Environment.getExternalStorageDirectory().getPath(); 
		fileName = SDPATH + "/" +dir+ "/" + fileName;
	//	fileName = SDPATH + "/aaaa.txt";
		File file = new File(fileName);   
	       if (!file.exists()) {
	    	   file.createNewFile();//�����ļ�
          }
	
       FileOutputStream fos = new FileOutputStream(file,true);    
       byte [] bytes = write_str.getBytes();   
       fos.write(bytes);   
       fos.close();
       
       

   }
   //MTPͬ��
	private void TBSDDirFile(String dir)
	{
		String fileName;
		String SDPATH = Environment.getExternalStorageDirectory().getPath(); 
		fileName = SDPATH + "/tmp/" + dir;
		File file = new File(fileName); 
		Uri uri =Uri.fromFile(file);
		Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE,uri);
		sendBroadcast(intent);
	}
   //������Ϣ
	private void  ReceiveMsg()
	{
	    handler = new Handler() {
	        @Override
	        public void handleMessage(Message msg) {
	            switch (msg.what) {
	            case 0:
	            	setTitle(strMeterAddr + " ���ڳ�����������");
	                break;
	            case 1:
	            	setTitle("�������");
	            	break;
	            case 2:
	                strhex="";
	     		    mSendThread = new SendThread();
	     		    mSendThread.start();
	            	//setTitle("����ʧ��");
	            	break;
	            case 3:
	            	stateText.setText("����������");
	            	stateText.setTextColor(android.graphics.Color.BLUE);
	        	    mreadThread = new readThread();
	        	    mreadThread.start();
	            	break;
	            }
	        }
	    };
	    
	    handler2 = new Handler() {
	        @Override
	        public void handleMessage(Message msg) {
	            switch (msg.what) {
	            case 0:
	            	setTitle("====��Ϣ");
	                break;
	            case 1:
	            	setTitle("�������");
	            	
	            	break;
	            case 2:
	            	
	            	titleText.setText(msg.getData().getString("key")+ "����ʧ��");
	            	failItem.add("���ַ��"+msg.getData().getString("key")+ "����ʧ��");
	            	break;
	            }
	        }
	    };  
	    
	
	}


}
