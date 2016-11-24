package com.icedcap.QJMeter;

public class Yhxx {
	public String accountnum;
	public String meteraddr;
	public int curdata;
	public String useraddr;
	public int state;
	public int  readtime;
	public Yhxx(){
		
	}
	public Yhxx(String accountnum,String meteraddr,int curdata,String useraddr,int state,int time)
	{
		this.accountnum =accountnum ;
		this.meteraddr =meteraddr;
		this.curdata =curdata ;
		this.useraddr =useraddr;
		this.state =state;
		this.readtime =time;
		
	}
	


}
