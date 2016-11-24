package com.icedcap.QJMeter;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import com.icedcap.QJMeter.R;

import android.app.Activity;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

public class UserInfo extends Activity {
	
	 private SQLiteOpenHelper helper =null;
     private MyOperator mytab=null;
     
     private Button preButton = null;
     
     private Button  saveButton = null;
     private Button  afterButton = null;
     private EditText editmeterdata = null;
     int time=0;
    
//    private TextView view ;
     List<String> Itemshow = new ArrayList<String>();//显示的信息
     
     List<HashMap<String,Object>> data =new ArrayList<HashMap<String,Object>>();
     
     private int curindex =0;
     private String uptownName;

    private ArrayAdapter<String> adapter;
    
//    List<HashMap<String,Object>> data =new ArrayList<HashMap<String,Object>>();
    
	 @Override
	    protected void onCreate(Bundle savedInstanceState) {
		    super.onCreate(savedInstanceState);
		    setContentView(R.layout.userinfo);
		    this.helper=new SQLiteHelper(this);//数据库操作辅助类
		    ListView list = (ListView) findViewById(R.id.listView2); 
		    saveButton = (Button) findViewById(R.id.save);
		    editmeterdata = (EditText) findViewById(R.id.editmeterdata);
		    final Calendar c = Calendar.getInstance();

//	          curmonth = c.get(Calendar.MONTH)+1;//获取当前月份
	   
		    time = (c.get(Calendar.YEAR)*100)+(c.get(Calendar.MONTH)+1);
		    
		    
		    Bundle bundle=getIntent().getExtras();
		    String strsate =bundle.getString("state");
		    uptownName  = bundle.getString("uptown");
		    curindex = Integer.parseInt(bundle.getString("index"));
		 
		
		//       Uptownname +=".txt";
		    
		    
   	        mytab = new MyOperator(helper.getWritableDatabase());	   		        
	            List<Yhxx> yhxxlist = new ArrayList<Yhxx>();
//	            yhxxlist =mytab.find();
	            yhxxlist =mytab.find(uptownName,Integer.parseInt(strsate),time);	   	       
	            for(Yhxx yhxx:yhxxlist)
	            {
	            	HashMap<String,Object> item= new HashMap<String,Object>();
	            	item.put("accountnum", yhxx.accountnum);
	            	item.put("meteraddr", yhxx.meteraddr);
	            	
	            	item.put("curdata", String.valueOf(yhxx.curdata));
	            	item.put("readtime", String.valueOf(yhxx.readtime));
	        
	   	         
	            	item.put("useraddr",yhxx.useraddr);	   	            	
	            	data.add(item);
	            }	
	            
	            Itemshow.add("用户号：" + (String)data.get(curindex).get("accountnum")) ;
	            Itemshow.add("表地址：" + (String)data.get(curindex).get("meteraddr")) ;
	            Itemshow.add("用户地址：" + (String)data.get(curindex).get("useraddr")) ;

	            Itemshow.add("本次读数：" + (String)data.get(curindex).get("curdata")) ;
	            Itemshow.add("抄表月份：" + (String)data.get(curindex).get("readtime")) ;
		 
		       adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1, Itemshow);   
		       list.setAdapter(adapter);
		       
		       
		        //添加事件Spinner事件监听  	        
		       afterButton = (Button)findViewById(R.id.after);
		       afterButton.setOnClickListener(new View.OnClickListener() {  
		     	   @Override  
		     	   public void onClick(View v) {

		     		  Itemshow.clear();
		     		 curindex =curindex+1;
		     		  int  length =data.size();
		     		  if(curindex>=length)
		     		  {
		     			 curindex=0;
		     		  }
		     	       Itemshow.add("用户号：" + (String)data.get(curindex).get("accountnum")) ;
		     	      Itemshow.add("表地址：" + (String)data.get(curindex).get("meteraddr")) ;
			            Itemshow.add("用户地址：" + (String)data.get(curindex).get("useraddr")) ;
			 
			            Itemshow.add("本次读数：" + (String)data.get(curindex).get("curdata")) ;
			           
		   	            
		   	         adapter.notifyDataSetChanged();

		     	   }
		     	   
		     	   
		     	});
		       //zsm
		       saveButton.setOnClickListener(new View.OnClickListener() {  
			    	 @Override  
			    	 public void onClick(View v) {
                   editmeterdata();
			    	 }
					
			    });
		        //添加事件Spinner事件监听  	        
		       preButton = (Button)findViewById(R.id.pre);
		       preButton.setOnClickListener(new View.OnClickListener() {  
		     	   @Override  
		     	   public void onClick(View v) {
		     		   
		     		   if(curindex>=1)
		     		   {
		     			   	  Itemshow.clear();
				     		  curindex =curindex-1;
				     		  int  length =data.size();
				     		  if(curindex>=length)
				     		  {
				     			 curindex=0;
				     		  }
				     	       Itemshow.add("用户号：" + (String)data.get(curindex).get("accountnum")) ;
				     	       Itemshow.add("表地址：" + (String)data.get(curindex).get("meteraddr")) ;
					           Itemshow.add("用户地址：" + (String)data.get(curindex).get("useraddr")) ;
					            Itemshow.add("本次读数：" + (String)data.get(curindex).get("curdata")) ;
				     		  
				   	            
				   	         adapter.notifyDataSetChanged();
		     			   
		     		   }

		     

		     	   }
		     	   
		     	   
		     	});
		       
		       
		    
	 }
	 private void editmeterdata() {
			// TODO Auto-generated method stub
		 
 	 String editbuf = editmeterdata.getText().toString();
			int a = editbuf.length();
			if(a>0)
			{
			     int meterdata = Integer.parseInt(editbuf);
			     String account = (String)data.get(curindex).get("accountnum");
			     System.out.println(account);
			     mytab = new MyOperator(helper.getWritableDatabase());
			     mytab.Update(account,meterdata );
			     System.out.println("OK");
			     
			     Toast t = Toast.makeText(this, "水表数据保存成功",
						Toast.LENGTH_LONG);
				t.setGravity(Gravity.CENTER, 0, 0);
				t.show();

				//("本次读数：" + (String)data.get(curindex).get("curdata")) ;
			   //  stateview.setText("保存成功");
			}
						
		}
	 

}
