package com.icedcap.QJMeter;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import com.icedcap.QJMeter.R;

import android.app.Activity;
import android.content.Intent;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;

import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Spinner;

public class Query extends Activity {
	
	// private static final String[] uptownlist =new String[20];
	 
	 	List<String> uptownlist = new ArrayList<String>();
	 	 private SQLiteOpenHelper helper =null;
	     private MyOperator mytab=null;
	     int time=0 ;
	     
	     private Button queryButton = null;
	     
	     private Button  normalButton = null;
	     private Button  abnormalButton = null;
	     private Button  downloadButton = null;
	    
	//    private TextView view ;
	    private int    state=0;
	    private Spinner spinner;
	    String  uptownname ="";
	    private ArrayAdapter<String> adapter;
	    
	    private SimpleAdapter listItemAdapter;
	     List<HashMap<String,Object>> data =new ArrayList<HashMap<String,Object>>();
	    @Override
	    protected void onCreate(Bundle savedInstanceState) {
	        // TODO Auto-generated method stub
	        super.onCreate(savedInstanceState);
	        setContentView(R.layout.query);
	        this.helper=new SQLiteHelper(this);//数据库操作辅助类
	        
		    final Calendar c = Calendar.getInstance();

//	          curmonth = c.get(Calendar.MONTH)+1;//获取当前月份
	   
		    time = (c.get(Calendar.YEAR)*100)+(c.get(Calendar.MONTH)+1);
	         
	   //     view = (TextView) findViewById(R.id.spinnerText);
	        spinner = (Spinner) findViewById(R.id.spinner1);
	        ListView list = (ListView) findViewById(R.id.listView1); 
	        //将可选内容与ArrayAdapter连接起来
	        ScanSDDirFile("Android");
	        final int size =uptownlist.size();
	        if(size>0)
	        {
	        	String[]  arruptown = uptownlist.toArray(new String[size]);
		        adapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,arruptown);
		         
		        //设置下拉列表的风格
		        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		         
		        
		        //将adapter 添加到spinner中
		        spinner.setAdapter(adapter);
		        
		        spinner.setOnItemSelectedListener(new SpinnerSelectedListener());
		        
		        //设置默认值
		        spinner.setVisibility(View.VISIBLE);
	        }
	        
	     listItemAdapter = new SimpleAdapter(this,data,//数据源   
   	                R.layout.vlist,//ListItem的XML实现  
   	                //动态数组与ImageItem对应的子项          
   	                new String[] {"title", "info"},   
   	                //ImageItem的XML文件里面的一个ImageView,两个TextView ID  
   	                new int[] {R.id.title,R.id.info}  
	                );  
	            list.setAdapter(listItemAdapter);  
	            list.setOnItemClickListener(new OnItemClickListener(){  
	                @Override
					public void onItemClick(AdapterView<?> parent, View view,int position, long id) {  
	                //	HashMap<String,Object> item = data.get(position);   //通过position获取所点击的对象  	                    
	                  //  String title =(String)item.get("title");//通过键名来取    
	          //  	    Intent intent=new Intent(Query.this,UserInfo.class);
	            	    
	        	
	        			Bundle bundle = new Bundle();
	        			
	        			bundle.putString("state", String.valueOf(state));
	        			bundle.putString("uptown", uptownname);
	        			bundle.putString("index", String.valueOf(position));//zsm

	        			Intent intent=new Intent(Query.this,UserInfo.class);
	        		    intent.putExtras(bundle);
	        	        startActivityForResult(intent, 0);

				
	                 
	                } 
	            });
	        //添加事件Spinner事件监听  	        
	        queryButton = (Button)findViewById(R.id.query);
	        queryButton.setOnClickListener(new View.OnClickListener() {  
	     	   @Override  
	     	   public void onClick(View v) {
	     		   
	     		  data.clear();

	   	        mytab = new MyOperator(helper.getWritableDatabase());	   		        
	   	            List<Yhxx> yhxxlist = new ArrayList<Yhxx>();
//	   	            yhxxlist =mytab.find();
	   	            state =0;
	   	            yhxxlist =mytab.find(uptownname,time);	
	   	            
	   	            int num = yhxxlist.size();
	   	            
	   	            setTitle("本小区本月抄表总个数为"+String.valueOf(num));
	   	            for(Yhxx yhxx:yhxxlist)
	   	            {
	   	            	HashMap<String,Object> item= new HashMap<String,Object>();
	   	            	item.put("title", yhxx.accountnum);
	   	            	String info = "";
	   	            	
	   	            	info+=yhxx.useraddr;
	   	            	
   	            	info+=", 本次读数：";
   	            	info+= String.valueOf(yhxx.curdata) ;

   	            	info+=", 抄表时间：";
   	            	info+=  String.valueOf(yhxx.readtime);
	   	           // 	info+=String.valueOf(value)
	   	            	item.put("info",info);	   	            	
	   	            	data.add(item);
	   	            }	
	   	            
	   	         listItemAdapter.notifyDataSetChanged();

	     	   }
	     	   
	     	   
	     	});
	        
	        normalButton = (Button)findViewById(R.id.normal);
	        normalButton.setOnClickListener(new View.OnClickListener() {  
	     	   @Override  
	     	   public void onClick(View v) {
	     		   
	     		    data.clear();

	   	            mytab = new MyOperator(helper.getWritableDatabase());	   		        
	   	            List<Yhxx> yhxxlist = new ArrayList<Yhxx>();
//	   	            yhxxlist =mytab.find();
	   	            state =1;

	
	   	            yhxxlist =mytab.find(uptownname,state,time);	   	   
	   	            int num = yhxxlist.size();
	   	            
	   	            setTitle("本小区本月总共"+String.valueOf(num)+"块表抄表成功");
	   	            for(Yhxx yhxx:yhxxlist)
	   	            {
	   	            	HashMap<String,Object> item= new HashMap<String,Object>();
	   	            	item.put("title", yhxx.accountnum);
	   	            	String info = "";
	   	            	
	   	            	info+=yhxx.useraddr;

	   	            	info+=", 本次读数：";
	   	            	info+= String.valueOf(yhxx.curdata) ;
	   	            	
	   	            	info+=", 抄表时间：";
	   	            	info+=  String.valueOf(yhxx.readtime);
	   	            //	info+= String.valueOf(yhxx.readmonth) ;
	   	           // 	info+=String.valueOf(value)
	   	            	item.put("info",info);	   	            	
	   	            	data.add(item);
	   	            }	
	   	            
	   	         listItemAdapter.notifyDataSetChanged();

	     	   }	     	   
	     	   
	     	});
	        
	        abnormalButton = (Button)findViewById(R.id.abnormal);
	        abnormalButton.setOnClickListener(new View.OnClickListener() {  
	     	   @Override  
	     	   public void onClick(View v) {
	     		   
	     		  data.clear();

	   	        mytab = new MyOperator(helper.getWritableDatabase());	   		        
	   	            List<Yhxx> yhxxlist = new ArrayList<Yhxx>();
//	   	            yhxxlist =mytab.find();
	   	            state=2;
	   	            yhxxlist =mytab.find(uptownname,state,time);	 
	   	            
	   	            int num = yhxxlist.size();
	   	            
	   	            setTitle("本小区本月总共"+String.valueOf(num)+"块表抄表失败");
	   	            for(Yhxx yhxx:yhxxlist)
	   	            {
	   	            	HashMap<String,Object> item= new HashMap<String,Object>();
	   	            	item.put("title", yhxx.accountnum);
	   	            	String info = "";
	   	            	
	   	            	info+=yhxx.useraddr;
	   	            	

	   	            	info+=", 本次读数：";
	   	            	info+= String.valueOf(yhxx.curdata) ;
	   	            	
	   	            	info+=", 抄表时间：";
	   	            	info+=  String.valueOf(yhxx.readtime);
	  // 	            	info+= String.valueOf(yhxx.readmonth) ;
	   	           // 	info+=String.valueOf(value)
	   	            	item.put("info",info);	   	            	
	   	            	data.add(item);
	   	            }	
	   	            
	   	         listItemAdapter.notifyDataSetChanged();

	     	   }
	     	   
	     	   
	     	});
	        
	        downloadButton = (Button)findViewById(R.id.download);
	        downloadButton.setOnClickListener(new View.OnClickListener() {  
	     	   @Override  
	     	   public void onClick(View v) {
	   	        mytab = new MyOperator(helper.getWritableDatabase());	   		        
	   	            List<Yhxx> yhxxlist = new ArrayList<Yhxx>();
//	   	            yhxxlist =mytab.find();
	   	       //     state=2;
	   	            yhxxlist =mytab.find(time);	 
//	   	            Calendar c = Calendar.getInstance();
//	   	            int year = c.get(Calendar.YEAR);
//	   	            int month = c.get(Calendar.MONTH)+1;
//	   	            int day = c.get(Calendar.DATE);

					 String strfileName = getStringDate();
  //  				 strfileName+=uptownname;
    				 strfileName+=".txt";
    				 String strContent ="";
	   	            for(Yhxx yhxx:yhxxlist)
	   	            {
					     strContent+=yhxx.meteraddr ;
				    	 strContent+="$";				    	 
				    	 strContent+=String.valueOf(yhxx.curdata) ;			    	 
				    	 strContent+="$";				    		
				    	 strContent+=  String.valueOf(yhxx.readtime);
				    	 strContent+="\r\n"; 	

	   	            }
	   	            if(strContent != "")
	   	            {
			   			 try {
								writeSDFile(strfileName,"tmp",strContent);
							} catch (IOException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}	
			   			 setTitle("下载成功");
	   	            }
	   	            else
	   	            {
	   	            	setTitle("下载数据为空，请先抄表");
	   	            }	   	        
	   	          
	   	          TBSDDirFile(strfileName);

	     	   }
	     	   
	     	   
	     	});
	
	    }
	        
	    class SpinnerSelectedListener implements OnItemSelectedListener{
	        	 
	        @Override
			public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2,
	                    long arg3) {
	        	//uptownname =m[arg2];
	        	
	        	uptownname = uptownlist.get(arg2);
	             //  view.setText("你的血型是："+m[arg2]);
	            }
	        @Override
			public void onNothingSelected(AdapterView<?> arg0) {
	        }
	    }
	    
		private void ScanSDDirFile(String dir)
		{
			   String fileName;
			  // File path = Environment.getExternalStorageDirectory();
			   
			String SDPATH = Environment.getExternalStorageDirectory().getPath(); 
			fileName = SDPATH + "/" + dir;
			File file = new File(fileName); 
			   		
			   File files[] = file.listFiles();  
			   
			   for (File f : files){  
			       if(f.isDirectory()){  
			           
			       }else{  
			    	   fileName = f.getName();
			    	// int start = fileName.lastIndexOf("/");
			    	   int end   = fileName.lastIndexOf(".");
			    	   if(end!=-1)
			    	   {
			    		   String strname = fileName.substring(0,end);
			    		   uptownlist.add(strname);
			    	   }
			    	   
			       }  
			   }
		}
		   public void writeSDFile(String fileName,String dir, String write_str) throws IOException{
				String sdStatus = Environment.getExternalStorageState();
				if (!sdStatus.equals(Environment.MEDIA_MOUNTED)) { //检测sd是否可用
					Log.v("TestFile","SD card is not avaiable/writeable right now.");
					return;
				}
				
				String SDPATH = Environment.getExternalStorageDirectory().getPath(); 
				fileName = SDPATH + "/" +dir+ "/" + fileName;
			//	fileName = SDPATH + "/aaaa.txt";
				File file = new File(fileName);   
				
			       if (!file.exists()) {
			    	   file.createNewFile();//创建文件
		          }
			
		       FileOutputStream fos = new FileOutputStream(file,false);    // zsm 10.31 true -- false
		       byte [] bytes = write_str.getBytes();   
		       fos.write(bytes);   
		       fos.close();
		   }
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
			public static String getStringDate(){
				Date currentTime = new Date();
				SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
				String dateString =formatter.format(currentTime);
				return dateString;
			}

}
