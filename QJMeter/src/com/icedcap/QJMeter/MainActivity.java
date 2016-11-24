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
	private String[] iconName = { "�Զ�����", "��ѯ",  "����ά��","����", "����", "����", "�汾��Ϣ" };
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		gview = (GridView) findViewById(R.id.gview);
		data_list = new ArrayList<Map<String, Object>>();//�½�List
		getData();   	//��ȡ����
		String [] from ={"image","text"};   //�½�������
		int [] to = {R.id.image,R.id.text};
		sim_adapter = new SimpleAdapter(this, data_list, R.layout.item, from, to);//����������
		gview.setAdapter(sim_adapter);
		//gview.setNumColumns(3);  //����GridViewһ�������ɵĸ���
		gview.setOnItemClickListener(new AdapterView.OnItemClickListener() {     // ��gv��Ӽ�����
			@Override
			public void onItemClick(AdapterView<?> parent, View view,int position, long id)			
			{
				@SuppressWarnings("unchecked")
				HashMap<String, Object> item = (HashMap<String, Object>) parent.getItemAtPosition(position);
				String strvalue = (String)item.get("text");
				if(strvalue.trim().equals("�Զ�����"))
				{
				    Intent intent=new Intent(MainActivity.this,Uptown.class);
				    startActivityForResult(intent, 0);
				} 
				
				else if(strvalue.trim().equals("��ѯ"))
				{
				    Intent intent=new Intent(MainActivity.this,Query.class);
				    startActivityForResult(intent, 0);
				}
				else if(strvalue.trim().equals("����ά��"))
				{
				    Intent intent=new Intent(MainActivity.this,SingleMeter.class);
				    startActivityForResult(intent, 0);
				}
				else if(strvalue.trim().equals("�汾��Ϣ"))
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
//		gridItem.put("itemText", ("   �Զ�����"));
//		gridItemList.add(gridItem);
//		
//		HashMap<String, Object> gridItem1 = new HashMap<String, Object>();
//		gridItem1.put("itemImage", R.drawable.query);
//	//	gridItem.put("itemText", ("NO." + String.valueOf(i)));
//		gridItem1.put("itemText", ("       ��ѯ"));
//		gridItemList.add(gridItem1);
//		
//		HashMap<String, Object> gridItem2 = new HashMap<String, Object>();
//		gridItem2.put("itemImage", R.drawable.qianfei);
//		//	gridItem.put("itemText", ("NO." + String.valueOf(i)));
//		gridItem2.put("itemText", ("       Ƿ��"));
//		gridItemList.add(gridItem2);
//		
//		HashMap<String, Object> gridItem3 = new HashMap<String, Object>();
//		gridItem3.put("itemImage", R.drawable.maintain);
//			//	gridItem.put("itemText", ("NO." + String.valueOf(i)));
//		gridItem3.put("itemText", ("    ����ά��"));
//		gridItemList.add(gridItem3);
//			
//		HashMap<String, Object> gridItem4 = new HashMap<String, Object>();
//		gridItem4.put("itemImage", R.drawable.system);
//				//	gridItem.put("itemText", ("NO." + String.valueOf(i)));
//		gridItem4.put("itemText", ("       ϵͳ"));
//		gridItemList.add(gridItem4);
//		
//		HashMap<String, Object> gridItem5 = new HashMap<String, Object>();
//		gridItem5.put("itemImage", R.drawable.download);
//		//	gridItem.put("itemText", ("NO." + String.valueOf(i)));
//		gridItem5.put("itemText", ("    ��������"));
//		gridItemList.add(gridItem5);
//		
//		HashMap<String, Object> gridItem6 = new HashMap<String, Object>();
//		gridItem6.put("itemImage", R.drawable.shebei);
//		//	gridItem.put("itemText", ("NO." + String.valueOf(i)));
//		gridItem6.put("itemText", ("    �豸ά��"));
//		
//		gridItemList.add(gridItem6);
//
//		SimpleAdapter simpleAdapter = new SimpleAdapter(this, //��Ҫ��ӵ�context
//				gridItemList,//��Ҫ��ӵ�����Դ��������Ĳ�������ΪList<? extends Map<String, ?>>
//				R.layout.gridview_item,//resource Resource identifier of a view layout that defines the views for this list item.
//				new String[] { "itemImage", "itemText" },//����һ����������Ӧ�ģ������Դ�ģ�����������Դ��������Ĳ���String[] from
//				new int[] { R.id.imageview, R.id.textvew1 });//�����Դ������ؼ��ϵ�λ�ã�����������Դ��������Ĳ���int[] to 
//
//		// ���adapter
//		gv.setAdapter(simpleAdapter);
//		//����GridViewһ�������ɵĸ���
//		gv.setNumColumns(3);
//
//		// ��gv��Ӽ�����
//		gv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//			@Override
//			public void onItemClick(AdapterView<?> parent, View view,
//					int position, long id) {
//				HashMap<String, Object> item = (HashMap<String, Object>) parent
//						.getItemAtPosition(position);
//				
//				
//				
//				setTitle("�������" + item.get("itemText"));
//				String strvalue = (String)item.get("itemText");
//				if(strvalue.trim().equals("�Զ�����"))
//				{
//				    Intent intent=new Intent(MainActivity.this,Uptown.class);
//					//    intent.putExtras(data);
//				    startActivityForResult(intent, 0);
//				}
//				else if(strvalue.trim().equals("��ѯ"))
//				{
//				    Intent intent=new Intent(MainActivity.this,Query.class);
//					//    intent.putExtras(data);
//				    startActivityForResult(intent, 0);
//				}
//				else if(strvalue.trim().equals("����ά��"))
//				{
//				    Intent intent=new Intent(MainActivity.this,SingleMeter.class);
//					//    intent.putExtras(data);
//				    startActivityForResult(intent, 0);
//				}
////				Bundle data = new Bundle();
////				data.putString("name", item);
//
////				Toast.makeText(MainActivity.this,
////						"�������" + item.get("itemText"), Toast.LENGTH_LONG)
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
