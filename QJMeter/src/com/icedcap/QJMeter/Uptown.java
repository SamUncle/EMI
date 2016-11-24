package com.icedcap.QJMeter;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import com.icedcap.QJMeter.R;
import com.icedcap.QJMeter.Uptown;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;

public class Uptown extends Activity {
    String readString = null; 
 	 private Button clearButton = null;
 	 ArrayAdapter<String> adapter;
 	List<String> data = new ArrayList<String>();
 	
//	 private SQLiteOpenHelper helper =null;
//     private MyOperator mytab=null;
	 private ListView lv;
	 @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.uptownlist);
        
//       
//        this.helper=new SQLiteHelper(this);//数据库操作辅助类
//        
////        mytab = new MyOperator(helper.getWritableDatabase());
////        int state = mytab.getstatebyID(1);
//       
//       mytab = new MyOperator(helper.getWritableDatabase());
//        
//        Yhxx  yhxx =new Yhxx();
//        yhxx.accountnum="333333";
//        yhxx.meteraddr="22222";
//        yhxx.curdata = 40;
//        yhxx.lastdata =20;
//        yhxx.curyl=20;
//        yhxx.readmonth =6;
//        yhxx.useraddr ="经开区百乐门名品广场3#104";
//        yhxx.state=2;
//        mytab.insert(yhxx);
//        mytab.insert(20,1);
        
   //     Log.e("database", String.valueOf(state));
        

        

        
    clearButton = (Button)findViewById(R.id.clear);
        	        
    //    TBSDDirFile("tmp/翼迈测试.txt");
    ScanSDDirFile("Android");
    
    lv = (ListView) findViewById(R.id.lv);//得到ListView对象的引用 /*为ListView设置Adapter来绑定数据*/ 
//    lv.setAdapter(new ArrayAdapter<String>(this,
//    	                android.R.layout.simple_list_item_1, data));
    
    adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1, data);   
    lv.setAdapter(adapter);
    	
    lv.setOnItemClickListener(new OnItemClickListener() {
    @Override
    public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,long arg3) {         
		try {
			//String s[] = data.get(arg2).split(",");
			String item = data.get(arg2);
			setTitle("你点击了第"+arg2+"行" + item);
			Bundle data = new Bundle();
			data.putString("name", item);
			
		    Intent intent=new Intent(Uptown.this,Community.class);
		    intent.putExtras(data);
	        startActivityForResult(intent, 0);
	       
		}catch(Exception e) {}	
    	}
    });
    
    clearButton.setOnClickListener(new View.OnClickListener() {  
  	   @Override  
  	   public void onClick(View v) {
  		 new AlertDialog.Builder(Uptown.this).setTitle("温馨提示")//设置对话框标题  
  		  
  	     .setMessage("清空列表后需要重新下载")//设置显示的内容  
  	  
  	     .setPositiveButton("确定",new DialogInterface.OnClickListener() {//添加确定按钮  
  	  
  	          
  	  
  	         @Override  
  	  
  	         public void onClick(DialogInterface dialog, int which) {//确定按钮的响应事件  
  	  
  	             // TODO Auto-generated method stub  
  	  
  	          ClearSDDirFile("Android");
  	  		  data.clear();
  	  		  adapter.notifyDataSetChanged();
  	  
  	         }  
  	  
  	     }).setNegativeButton("返回",new DialogInterface.OnClickListener() {//添加返回按钮  
  	  
  	          
  	  
  	         @Override  
  	  
  	         public void onClick(DialogInterface dialog, int which) {//响应事件  
  	  
  	             // TODO Auto-generated method stub  
  	  
  	  
  	         }  
  	  
  	     }).show();//在按键响应事件中显示此对话框  
  		  
//  		  SimpleAdapter sAdapter = (SimpleAdapter)lv.getAdapter();  
//          sAdapter.notifyDataSetChanged(); 

  	   }
  	});
   
  
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
		    	//   int start = fileName.lastIndexOf("/");
		    	   int end   = fileName.lastIndexOf(".");
		    	   if(end!=-1)
		    	   {
		    		   String strname = fileName.substring(0,end);
		    		   data.add(strname);
		    	   }
		    	   
		       }  
		   }
	} 
	
	private void ClearSDDirFile(String dir)
	{
		String fileName;
		String SDPATH = Environment.getExternalStorageDirectory().getPath(); 
		fileName = SDPATH + "/" + dir;
		File file = new File(fileName); 
		   		
		   File files[] = file.listFiles();  
		   
		   for (File f : files){  
		       if(f.isDirectory()){  
		           
		       }else{  
		    	   fileName = f.getName();
		    	   String strname =fileName.substring(fileName.length()-4, fileName.length());
		    	   if(strname.equals(".txt"))
		    	   {
		    		   //data.add(fileName);
		    		   f.delete();
		    	   }
		       }  
		   }
	}      

}
