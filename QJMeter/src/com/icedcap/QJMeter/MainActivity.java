package com.icedcap.QJMeter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.icedcap.QJMeter.MainActivity;
import com.icedcap.QJMeter.R;
import com.icedcap.QJMeter.Uptown;
import com.icedcap.QJMeter.Query;
import com.icedcap.QJMeter.SingleMeter;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import android.widget.SimpleAdapter;


public class MainActivity extends Activity {
	private GridView gview;
	private List<Map<String, Object>> data_list;
	private SimpleAdapter sim_adapter;
	
	
	private int[] icon = { 
			R.drawable.meter,  R.drawable.query,
			R.drawable.maintain,R.drawable.qianfei, 
			R.drawable.system, R.drawable.download,
            R.drawable.shebei};
	private String[] iconName = { "自动抄表", "查询",  "单表维护","待定", "待定", "待定", "版本信息" };
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		gview = (GridView) findViewById(R.id.gview);
		data_list = new ArrayList<Map<String, Object>>();//新建List
		getData();   	//获取数据
		String [] from ={"image","text"};   //新建适配器
		int [] to = {R.id.image,R.id.text};
		sim_adapter = new SimpleAdapter(this, data_list, R.layout.item, from, to);//配置适配器
		gview.setAdapter(sim_adapter);
		//gview.setNumColumns(3);  //设置GridView一行所容纳的个数
		gview.setOnItemClickListener(new AdapterView.OnItemClickListener() {     // 给gv添加监听器
			@Override
			public void onItemClick(AdapterView<?> parent, View view,int position, long id)			
			{
				@SuppressWarnings("unchecked")
				HashMap<String, Object> item = (HashMap<String, Object>) parent.getItemAtPosition(position);
				String strvalue = (String)item.get("text");
				if(strvalue.trim().equals("自动抄表"))
				{
				    Intent intent=new Intent(MainActivity.this,Uptown.class);
				    startActivityForResult(intent, 0);
				} 
				
				else if(strvalue.trim().equals("查询"))
				{
				    Intent intent=new Intent(MainActivity.this,Query.class);
				    startActivityForResult(intent, 0);
				}
				else if(strvalue.trim().equals("单表维护"))
				{
				    Intent intent=new Intent(MainActivity.this,SingleMeter.class);
				    startActivityForResult(intent, 0);
				}
				else if(strvalue.trim().equals("版本信息"))
				{
				    Intent intent=new Intent(MainActivity.this,VersionInfo.class);
				    startActivityForResult(intent, 0);
				}
				
				
			}
		});
	}

	
	
	public List<Map<String, Object>> getData(){		
		for(int i=0;i<icon.length;i++){
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("image", icon[i]);
			map.put("text", iconName[i]);
			data_list.add(map);
		}		
		return data_list;
	}
}
//	private GridView gv;
//	private ImageView iv;
//	private TextView tv;
//
//	@Override
//	protected void onCreate(Bundle savedInstanceState) {
//		super.onCreate(savedInstanceState);
//		setContentView(R.layout.activity_main);
//		
//
//		initView();
//		List<HashMap<String, Object>> gridItemList = new ArrayList<HashMap<String, Object>>();
//	
//		HashMap<String, Object> gridItem = new HashMap<String, Object>();
//		gridItem.put("itemImage", R.drawable.meter);
//	//	gridItem.put("itemText", ("NO." + String.valueOf(i)));
//		gridItem.put("itemText", ("   自动抄表"));
//		gridItemList.add(gridItem);
//		
//		HashMap<String, Object> gridItem1 = new HashMap<String, Object>();
//		gridItem1.put("itemImage", R.drawable.query);
//	//	gridItem.put("itemText", ("NO." + String.valueOf(i)));
//		gridItem1.put("itemText", ("       查询"));
//		gridItemList.add(gridItem1);
//		
//		HashMap<String, Object> gridItem2 = new HashMap<String, Object>();
//		gridItem2.put("itemImage", R.drawable.qianfei);
//		//	gridItem.put("itemText", ("NO." + String.valueOf(i)));
//		gridItem2.put("itemText", ("       欠费"));
//		gridItemList.add(gridItem2);
//		
//		HashMap<String, Object> gridItem3 = new HashMap<String, Object>();
//		gridItem3.put("itemImage", R.drawable.maintain);
//			//	gridItem.put("itemText", ("NO." + String.valueOf(i)));
//		gridItem3.put("itemText", ("    单表维护"));
//		gridItemList.add(gridItem3);
//			
//		HashMap<String, Object> gridItem4 = new HashMap<String, Object>();
//		gridItem4.put("itemImage", R.drawable.system);
//				//	gridItem.put("itemText", ("NO." + String.valueOf(i)));
//		gridItem4.put("itemText", ("       系统"));
//		gridItemList.add(gridItem4);
//		
//		HashMap<String, Object> gridItem5 = new HashMap<String, Object>();
//		gridItem5.put("itemImage", R.drawable.download);
//		//	gridItem.put("itemText", ("NO." + String.valueOf(i)));
//		gridItem5.put("itemText", ("    重载任务"));
//		gridItemList.add(gridItem5);
//		
//		HashMap<String, Object> gridItem6 = new HashMap<String, Object>();
//		gridItem6.put("itemImage", R.drawable.shebei);
//		//	gridItem.put("itemText", ("NO." + String.valueOf(i)));
//		gridItem6.put("itemText", ("    设备维护"));
//		
//		gridItemList.add(gridItem6);
//
//		SimpleAdapter simpleAdapter = new SimpleAdapter(this, //即要添加的context
//				gridItemList,//即要添加的内容源代码给出的参数类型为List<? extends Map<String, ?>>
//				R.layout.gridview_item,//resource Resource identifier of a view layout that defines the views for this list item.
//				new String[] { "itemImage", "itemText" },//雨下一个参数所对应的，添加资源的，数组容器，源代码给出的参数String[] from
//				new int[] { R.id.imageview, R.id.textvew1 });//添加资源到具体控件上的位置，数组容器，源代码给出的参数int[] to 
//
//		// 添加adapter
//		gv.setAdapter(simpleAdapter);
//		//设置GridView一行所容纳的个数
//		gv.setNumColumns(3);
//
//		// 给gv添加监听器
//		gv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//			@Override
//			public void onItemClick(AdapterView<?> parent, View view,
//					int position, long id) {
//				HashMap<String, Object> item = (HashMap<String, Object>) parent
//						.getItemAtPosition(position);
//				
//				
//				
//				setTitle("您点击了" + item.get("itemText"));
//				String strvalue = (String)item.get("itemText");
//				if(strvalue.trim().equals("自动抄表"))
//				{
//				    Intent intent=new Intent(MainActivity.this,Uptown.class);
//					//    intent.putExtras(data);
//				    startActivityForResult(intent, 0);
//				}
//				else if(strvalue.trim().equals("查询"))
//				{
//				    Intent intent=new Intent(MainActivity.this,Query.class);
//					//    intent.putExtras(data);
//				    startActivityForResult(intent, 0);
//				}
//				else if(strvalue.trim().equals("单表维护"))
//				{
//				    Intent intent=new Intent(MainActivity.this,SingleMeter.class);
//					//    intent.putExtras(data);
//				    startActivityForResult(intent, 0);
//				}
////				Bundle data = new Bundle();
////				data.putString("name", item);
//
////				Toast.makeText(MainActivity.this,
////						"您点击了" + item.get("itemText"), Toast.LENGTH_LONG)
////						.show();
//			}
//
//		});
//	}
//
//	private void initView() {
//		gv = (GridView) findViewById(R.id.gridview);
//		iv = (ImageView) findViewById(R.id.imageview);
//		tv = (TextView) findViewById(R.id.textvew1);
//	}
//
//	@Override
//	public boolean onCreateOptionsMenu(Menu menu) {
//		// Inflate the menu; this adds items to the action bar if it is present.
//		getMenuInflater().inflate(R.menu.main, menu);
//		return true;
//	}
//
//}
