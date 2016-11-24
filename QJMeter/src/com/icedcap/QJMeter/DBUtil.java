package com.icedcap.QJMeter;

import java.util.ArrayList;
import java.util.List;

public class DBUtil {

	private ArrayList<String> arrayList = new ArrayList<String>();
	private ArrayList<String> brrayList = new ArrayList<String>();
	private ArrayList<String> crrayList = new ArrayList<String>();
	
	private HttpConnSoap Soap = new HttpConnSoap();

	public List<String> HelloWorld() 
	{
		arrayList.clear();
		brrayList.clear();
		crrayList.clear();

		List<String> list = Soap.GetWebServre("HelloWorld", arrayList, brrayList);

		return list;
	}
	
	public List<String> GetAllUser() {
		arrayList.clear();
		brrayList.clear();
		crrayList.clear();
		
		crrayList = Soap.GetWebServre("GetAllUserPwd", arrayList, brrayList);
		
		return crrayList;
	}
	
	//��ȡ���б��
	public List<String> GetAllHandBook(String user) {
		arrayList.clear();
		brrayList.clear();
		crrayList.clear();
		
		arrayList.add("userID");
		brrayList.add(user);
		
		crrayList = Soap.GetWebServre("GetAllHandBook", arrayList, brrayList);
		return crrayList;
	}
	
	//��ȡ��������û�
	public List<String> GetAllConsumer(String Name) {
		arrayList.clear();
		brrayList.clear();
		crrayList.clear();
		
		arrayList.add("HandBookName");
		brrayList.add(Name);
			
		crrayList = Soap.GetWebServre("GetAllConsumer", arrayList, brrayList);

		return crrayList;
		}
	
	//��ȡ��������û�
	public ArrayList<String> GetConsumer(String Name) {
		arrayList.clear();
		brrayList.clear();
		crrayList.clear();
		
		arrayList.add("HandBookName");
		brrayList.add(Name);
			
		crrayList = Soap.GetWebServre("GetConsumer", arrayList, brrayList);

		return crrayList;
		}

	//////////////////////////////////////////////////////
	//��ȡ�û�������
	public List<String> getUserPwd() {
		arrayList.clear();
		brrayList.clear();
		crrayList.clear();
		
		crrayList = Soap.GetWebServre("getUserPwd", arrayList, brrayList);
		return crrayList;
	}
	
	//��ȡ����·��
	public List<String> GetRoute (String usr) 
	{
		arrayList.clear();
		brrayList.clear();
		crrayList.clear();
		
		arrayList.add("user");
		brrayList.add(usr);
	
		crrayList = Soap.GetWebServre("GetRoute", arrayList, brrayList);
		return crrayList;
		
		/*arrayList.clear();
		brrayList.clear();
		crrayList.clear();

		arrayList.add("userName");
		brrayList.add("user");
		
		crrayList = Soap.GetWebServre("getDevInfo", arrayList, brrayList);
		return crrayList;*/
	}
	
	//��ȡ�����豸�б�
	public List<String> GetLocalDev(String lng,String lat) 
	{
		arrayList.clear();
		brrayList.clear();
		crrayList.clear();
		
		arrayList.add("lng");
		brrayList.add(lng);
		
		arrayList.add("lat");
		brrayList.add(lat);
		
		crrayList = Soap.GetWebServre("GetLocalDev", arrayList, brrayList);
		return crrayList;
		/*arrayList.clear();
		brrayList.clear();
		crrayList.clear();
		
		arrayList.add("userName");
		brrayList.add("user");
		
		crrayList = Soap.GetWebServre("getDevInfo", arrayList, brrayList);
		return crrayList;*/
	}
	
	//�ϴ�����
	public List<String> UploadNum(String UserCode,String num,String user) 
	{
		arrayList.clear();
		brrayList.clear();
		crrayList.clear();
		
		arrayList.add("UserCode");
		brrayList.add(UserCode);
		
		arrayList.add("num");
		brrayList.add(num);
		
		arrayList.add("user");
		brrayList.add(user);

		
		crrayList = Soap.GetWebServre("UploadNum", arrayList, brrayList);
		return crrayList;
	}
	
	//�ϴ�ͼ��
	public List<String> UploadImage(String devID,String image,String user) 
	{
		arrayList.clear();
		brrayList.clear();
		crrayList.clear();
		
		arrayList.add("devID");
		brrayList.add(devID);
		
		arrayList.add("image");
		brrayList.add(image);
		
		arrayList.add("user");
		brrayList.add(user);
		
		crrayList = Soap.GetWebServre("UploadImage", arrayList, brrayList);
		return crrayList;
	}
}