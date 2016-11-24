package com.icedcap.QJMeter;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class SQLiteHelper extends SQLiteOpenHelper {

	private static final String DATABASE_NAME="fpp.db";
    private static final int  DATABASE_VERSION=32;//更改版本后数据库将重新创建
    private static final String TABLE_NAME="yhxx";
	
    public SQLiteHelper(Context context, String name, CursorFactory factory,
			int version) {
		super(context, name, factory, version);
		// TODO Auto-generated constructor stub
	}
    
    public SQLiteHelper(Context context) {
    	 super(context, DATABASE_NAME, null, DATABASE_VERSION);//继承父类
		// TODO Auto-generated constructor stub
	}


     
    /**
     * 在SQLiteOpenHelper的子类当中，必须有这个构造函数
     * @param context     当前的Activity
     * @param name        表的名字（而不是数据库的名字，这个类是用来操作数据库的）
     * @param factory      用来在查询数据库的时候返回Cursor的子类，传空值
     * @param version      当前的数据库的版本，整数且为递增的数
     * @return 
     */
//    public  SQLitedata(Context context)
//    {
//        super(context, DATABASE_NAME, null, DATABASE_VERSION);//继承父类
//        // TODO Auto-generated constructor stub
//    }
	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		
	     String sql = "CREATE TABLE " + TABLE_NAME + "("
	                + "accountnum VARCHAR(12),"
	                + "meteraddr VARCHAR(10),"
	                + "curdata INTEGER,"
	                + "useraddr VARCHAR(50),"
	                + "state INTEGER,"
	                + "readtime INTEGER"
	                + ")";
	        db.execSQL(sql);
	       // Log.e("create","数据库创建成功");

	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        String sql = "DROP TABLE IF EXISTS " + TABLE_NAME; 
        db.execSQL(sql);
        this.onCreate(db);
        // TODO Auto-generated method stub
        System.out.println("数据库已经更新");
        /**
         * 在此添加更新数据库是要执行的操作
         */
    }

}
