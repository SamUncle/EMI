package com.icedcap.QJMeter;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class HttpConnSoap {
	//private String ServerUrl = "http://220.178.85.242:86/Service1.asmx";
	//private String ServerUrl = "http://220.178.85.242:2306/Service1.asmx";//ymtest
	//private String ServerUrl = "http://localhost:1192/Service1.asmx";
    private String ServerUrl = "http://117.69.252.27:9999/Service1.asmx";//淮北
	private URL _url;
	public void setUrl(URL url )
	{
		_url = url;
	}
	public URL getUrl()
	{
		return _url;
	}
	public  HttpConnSoap() {
		try {
			_url = new URL(ServerUrl);
		} catch (Exception e) {
			// TODO: handle exception
		}
	}
	public ArrayList<String> GetWebServre(String methodName, ArrayList<String> Parameters, ArrayList<String> ParValues) 
	{
		ArrayList<String> Values = new ArrayList<String>(1024);
		
		
		//String ServerUrl = "http://220.178.85.242:86/Service1.asmx";//合肥
		
		String soapAction = "http://tempuri.org/" + methodName;
		String soap = "<?xml version=\"1.0\" encoding=\"utf-8\"?>"
				+ "<soap:Envelope xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:soap=\"http://schemas.xmlsoap.org/soap/envelope/\">"
				+ "<soap:Body />";
		String tps, vps, ts;
		String mreakString = "";

		mreakString = "<" + methodName + " xmlns=\"http://tempuri.org/\">";
		for (int i = 0; i < Parameters.size(); i++) {
			tps = Parameters.get(i).toString();
			vps = ParValues.get(i).toString();
			ts = "<" + tps + ">" + vps + "</" + tps + ">";
			mreakString = mreakString + ts;
		}
		mreakString = mreakString + "</" + methodName + ">";

		String soap2 = "</soap:Envelope>";
		String requestData = soap + mreakString + soap2;

		try {
//			URL url = new URL(ServerUrl);
			HttpURLConnection con = (HttpURLConnection) _url.openConnection();
			byte[] bytes = requestData.getBytes("utf-8");
			con.setDoInput(true);
			con.setDoOutput(true);
			con.setUseCaches(false);
			con.setConnectTimeout(60000);
			con.setRequestMethod("POST");
			con.setRequestProperty("Content-Type", "text/xml;charset=utf-8");
			con.setRequestProperty("SOAPAction", soapAction);
			con.setRequestProperty("Content-Length", "" + bytes.length);
			OutputStream outStream = con.getOutputStream();
			outStream.write(bytes);
			outStream.flush();
			outStream.close();
			InputStream inStream = con.getInputStream();
			Values = inputStreamtovaluelist(inStream, methodName);
			return Values;

		} 
		catch (Exception e) {
			System.out.print("网络连异常:2221");
			return null;
		}
	}

	public ArrayList<String> inputStreamtovaluelist(InputStream in, String MonthsName) throws IOException {
		StringBuffer out = new StringBuffer();
		String s1 = "";
		//byte[] b = new byte[4096];
		ArrayList<String> Values = new ArrayList<String>();
		Values.clear();
		//stream to string
		BufferedReader bReader =new BufferedReader(new InputStreamReader(in, "utf-8"));
		while ((s1=bReader.readLine())!=null) {
			out.append(s1);
		}//修改原来部分中文出现问号菱形乱码的问题
//		for (int n; (n = in.read(b)) != -1;) {
//			s1 = new String(b, 0, n);
//			out.append(s1);
//		}//此方法废弃不用
		
		String ifString = MonthsName + "Result";
		int start = out.indexOf(ifString) + ifString.length()+1;
		int end   = out.lastIndexOf(ifString)-2;
		String valuestring = out.substring(start, end);
		String[] valueArray = valuestring.split("</string>");
		for (int i = 0; i < valueArray.length; i++) 
		{
			valueArray[i] = valueArray[i].substring(8);
			Values.add(valueArray[i]);
		}

		return Values;
	}
}
