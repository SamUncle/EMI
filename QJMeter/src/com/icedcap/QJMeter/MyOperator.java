package com.icedcap.QJMeter;

import java.util.ArrayList;
import java.util.List;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class MyOperator {
	
	 private static final String TABLE_NAME = "yhxx";//要操作的数据表的名称
	    private SQLiteDatabase db=null; //数据库操作
	     
	    //构造函数
	    public MyOperator(SQLiteDatabase db)
	    {
	        this.db=db;
	    }
	     
	//  //插入操作
	//  public void insert(int id,String nid,String sid,int type,
//	          String stime,String etime,String desc,String locate_main,String locate_detail,int state)
	//  {
//	      String sql = "INSERT INTO " + TABLE_NAME + " (id,nid,sid,type,stime,etime,desc,locate_main,locate_detail,state)"
//	              + " VALUES(?,?,?,?,?,?,?,?,?,?)";
//	      Object args[]=new Object[]{id,nid,sid,type,stime,etime,desc,locate_main,locate_detail,state};
//	      this.db.execSQL(sql, args);
//	      this.db.close();
	//  }
	    //插入重载操作
//	    public void insert(int id,int state)
//	    {
//	        String sql = "INSERT INTO " + TABLE_NAME + " (id,state)" +" VALUES(?,?)";
//	        Object args[]=new Object[]{id,state};
//	        this.db.execSQL(sql, args);
//	        this.db.close();
//	    }
	     
	    //插入重载操作
	    public void insert(Yhxx yhxx)
	    {
	    	
	        String sql = "INSERT INTO " + TABLE_NAME + " (accountnum,meteraddr,curdata,useraddr,state,readtime)" +" VALUES(?,?,?,?,?,?)";
	        Object args[]=new Object[]{yhxx.accountnum,yhxx.meteraddr,yhxx.curdata,yhxx.useraddr,yhxx.state,yhxx.readtime};

	        this.db.execSQL(sql, args);
	        this.db.close();
	    }
	     
//	    //更新操作
//	    public void update(int id,int state)
//	    {
//	        String sql = "UPDATE " + TABLE_NAME + " SET state=? WHERE id=?";
//	        Object args[]=new Object[]{state,id};
//	        this.db.execSQL(sql, args);
//	        this.db.close();
//	    }
	    //更新操作  scy add 2016-10-22
	    public void Update(String accountnum,int data)
	    {
	    	        String sql = "UPDATE " + TABLE_NAME + " SET curdata=? WHERE accountnum=?";
			        Object args[]=new Object[]{data,accountnum};
			        this.db.execSQL(sql, args);
			        this.db.close();
	    }
//	    //更新操作
	    public void updateCurdata(String accountid,int data)
	    {
	        String sql = "UPDATE " + TABLE_NAME + " SET curdata=? WHERE accountnum=?";
	        Object args[]=new Object[]{data,accountid};
	        this.db.execSQL(sql, args);
	        this.db.close();
	    }
	     
	    //删除操作,删除
	    public void delete(int id)
	    {
	        String sql = "DELETE FROM " + TABLE_NAME +" WHERE id=?";
	        Object args[]=new Object[]{id};
	        this.db.execSQL(sql, args);
	        this.db.close();
	    }
	     
	    //查询操作,查询表中所有的记录返回列表
//	    public List<String> find()
//	    {
//	        List<String> all = new ArrayList<String>(); //此时只是String
//	        String sql = "SELECT * FROM " + TABLE_NAME;
//	        Cursor result = this.db.rawQuery(sql, null);    //执行查询语句
//	        for(result.moveToFirst();!result.isAfterLast();result.moveToNext()  )   //采用循环的方式查询数据
//	        {
//	            all.add(result.getInt(0)+","+result.getString(1)+","+result.getString(2)+","+result.getInt(3)+","
//	                    +result.getString(4)+","+result.getString(5)+","+result.getString(6)+","+result.getString(7)+","
//	                    +result.getString(8));
//	        } 
//	        this.db.close();
//	        return all;
//	    }
	    
	    //查询操作,查询表中所有的记录返回列表
	    public List<Yhxx> find(int time1)
	    {
	        List<Yhxx> all = new ArrayList<Yhxx>(); //此时只是String
	        String sql = "select t1.accountnum,t1.meteraddr,t1.curdata,t1.useraddr,t1.state,t1.readtime from yhxx t1";
	        sql += " , (select accountnum,MAX(readtime) as time from yhxx group by accountnum) t2 ";
	        sql += " where t1.accountnum=t2.accountnum and t1.readtime =t2.time";
	        
	        sql += " and t1.readtime  like '" +String.valueOf(time1)+"%'";
	        Cursor result = this.db.rawQuery(sql, null);    //执行查询语句
	        for(result.moveToFirst();!result.isAfterLast();result.moveToNext()  )   //采用循环的方式查询数据
	        {
	        	Yhxx yhxx =new Yhxx();
//	            all.add(result.getInt(0)+","+result.getString(1)+","+result.getString(2)+","+result.getInt(3)+","
//	                    +result.getString(4)+","+result.getString(5)+","+result.getString(6)+","+result.getString(7)+","
//	                    +result.getString(8));
	        	yhxx.accountnum =result.getString(result.getColumnIndex("accountnum"));
	        	yhxx.meteraddr =result.getString(result.getColumnIndex("meteraddr"));
	        	
	        	yhxx.curdata =result.getInt(result.getColumnIndex("curdata"));
//	        	yhxx.lastdata =result.getInt(result.getColumnIndex("lastdata"));
//	        	yhxx.curyl =result.getInt(result.getColumnIndex("curyl"));
//	        	yhxx.readmonth = result.getInt(result.getColumnIndex("readmonth"));
	        	yhxx.useraddr =result.getString(result.getColumnIndex("useraddr"));
	        	yhxx.state =result.getInt(result.getColumnIndex("state"));
	        	yhxx.readtime =result.getInt(result.getColumnIndex("readtime"));
	        	
	        	all.add(yhxx);
	        	
	        } 
	        this.db.close();
	        return all;
	    }
	    
	    
	    //查询操作,查询表中所有的记录返回列表
	    public List<Yhxx> find(String uptownname,int time)
	    {
	        List<Yhxx> all = new ArrayList<Yhxx>(); //此时只是String
	        //String sql = "SELECT * FROM " + TABLE_NAME +" where useraddr like  '%" +uptownname+"%'";
	        
	        String sql = "select t1.accountnum,t1.meteraddr,t1.curdata,t1.useraddr,t1.state,t1.readtime from yhxx t1";
	        sql += " , (select accountnum,MAX(readtime) as time from yhxx group by accountnum) t2 ";
	        sql += " where t1.accountnum=t2.accountnum and t1.readtime =t2.time and t1.useraddr like '%" ;
	        sql += uptownname +"%'";
	        
	        sql += " and t1.readtime  like '" +String.valueOf(time)+"%'";
	        Cursor result = this.db.rawQuery(sql, null);    //执行查询语句 
	        for(result.moveToFirst();!result.isAfterLast();result.moveToNext()  )   //采用循环的方式查询数据
	        {
	        	Yhxx yhxx =new Yhxx();
	        	yhxx.accountnum =result.getString(result.getColumnIndex("accountnum"));
	        	yhxx.meteraddr =result.getString(result.getColumnIndex("meteraddr"));    	
	        	yhxx.curdata =result.getInt(result.getColumnIndex("curdata"));
	        	yhxx.useraddr =result.getString(result.getColumnIndex("useraddr"));
	        	yhxx.state =result.getInt(result.getColumnIndex("state"));
	        	yhxx.readtime =result.getInt(result.getColumnIndex("readtime"));
	        	all.add(yhxx);
	        } 
	        this.db.close();
	        return all;
	    }
	    //查询操作,查询表中所有的记录返回列表
	    public List<Yhxx> find(String uptownname,int state,int time)
	    {
	    	if(state==0)
	    	{
	    		return find(uptownname,time);
	    	}
	    	else
	    	{
	            List<Yhxx> all = new ArrayList<Yhxx>(); //此时只是String
	            
	            String sql = "select t1.accountnum,t1.meteraddr,t1.curdata,t1.useraddr,t1.state,t1.readtime from yhxx t1";
		        sql += " , (select accountnum,MAX(readtime) as time from yhxx group by accountnum) t2 ";
		        sql += " where t1.accountnum=t2.accountnum and t1.readtime =t2.time and t1.useraddr like '%" ;
		        sql += uptownname +"%'";
		        sql += " and t1.state = " +String.valueOf(state);
		        sql += " and t1.readtime  like '" +String.valueOf(time)+"%'";
		     //   String sql = "SELECT * FROM " + TABLE_NAME +" where useraddr like  '%" +uptownname+"%'  and state =" +String.valueOf(state);
		        Cursor result = this.db.rawQuery(sql, null);    //执行查询语句
		        for(result.moveToFirst();!result.isAfterLast();result.moveToNext()  )   //采用循环的方式查询数据
		        {
		        	Yhxx yhxx =new Yhxx();
		        	yhxx.accountnum =result.getString(result.getColumnIndex("accountnum"));
		        	yhxx.meteraddr =result.getString(result.getColumnIndex("meteraddr"));
		        	yhxx.curdata =result.getInt(result.getColumnIndex("curdata"));
//		        	yhxx.lastdata =result.getInt(result.getColumnIndex("lastdata"));
//		        	yhxx.curyl =result.getInt(result.getColumnIndex("curyl"));
//		        	yhxx.readmonth = result.getInt(result.getColumnIndex("readmonth"));
		        	yhxx.useraddr =result.getString(result.getColumnIndex("useraddr"));
		        	yhxx.state =result.getInt(result.getColumnIndex("state"));
		        	yhxx.readtime =result.getInt(result.getColumnIndex("readtime"));
		        	all.add(yhxx);
		        	
		        } 
		        this.db.close();
		        return all;	    		
	    	}
	
	    }	     
	    //查询操作虫重载函数，返回指定ID的列表
	    public int getstatebyID(int id)
	    {
	        int num=-1;//错误状态-1
	   
	        String sql = "SELECT state FROM " + TABLE_NAME + " where id=?" ;
	        String args[] = new String[]{String.valueOf(id)};
	        Cursor result = this.db.rawQuery(sql, args);
	        for(result.moveToFirst();!result.isAfterLast();result.moveToNext()  )
	        {
	            num=result.getInt(0);
	        }
	         
	    //    Log.e("database", "图片状态state"+ String.valueOf(num));
	        this.db.close();
	        return num;
	    }
	    
	    //根据用户号，查询该用户所有信息
//	    public List<String> getAllinfobyID(int id)
//	    {
//	        
//	        List<String> all = new ArrayList<String>(); //此时只是String
//	        String sql = "SELECT state FROM " + TABLE_NAME + " where id=?" ;
//	        String args[] = new String[]{String.valueOf(id)};
//	        Cursor result = this.db.rawQuery(sql, args);
//	        for(result.moveToFirst();!result.isAfterLast();result.moveToNext()  )
//	        {
//	        	 all.add(result.getInt(0)+","+result.getString(1)+","+result.getString(2)+","+result.getInt(3)+","
//		                    +result.getString(4)+","+result.getString(5)+","+result.getString(6)+","+result.getString(7)+","
//		                    +result.getString(8));
//	        }
//	         
//	    //    Log.e("database", "图片状态state"+ String.valueOf(num));
//	        this.db.close();
//	        return all;
//	    }
	    //zsm 10.31
	    public boolean check_same(String accountnum)
	    {
	        String sql="SELECT accountnum from " + TABLE_NAME + " where accountnum = ?";
	        String args[] =new String[]{accountnum};
	        Cursor result=this.db.rawQuery(sql,args);
	        if(result.getCount()==0)//判断得到的返回数据是否为空
	        {
	           // this.db.close();
	            return false;
	        }
	        else
	        {
	            //this.db.close();
	            return true;
	        }
	    }

	    //判断插入数据的ID是否已经存在数据库中。
//	    public boolean check_same(int id)
//	    {
//	        String sql="SELECT id from " + TABLE_NAME + " where id = ?";
//	        String args[] =new String[]{String.valueOf(id)};
//	        Cursor result=this.db.rawQuery(sql,args);
////	        Log.e("database", "the sql has been excuate");
////	         
////	        Log.e("database","the hang count" + String.valueOf(result.getCount()));
//	         
//	        if(result.getCount()==0)//判断得到的返回数据是否为空
//	        {
//	         //   Log.e("database", "return false and not exist the same result" + String.valueOf(result.getCount()));
//	            this.db.close();
//	            return false;
//	        }
//	        else
//	        {
//	     //       Log.e("database", "return true and exist the same result"+ String.valueOf(result.getCount()));
//	            this.db.close();
//	            return true;
//	        }
//	    }

}
