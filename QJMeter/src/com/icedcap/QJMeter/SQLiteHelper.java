package com.icedcap.QJMeter;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class SQLiteHelper extends SQLiteOpenHelper {

	private static final String DATABASE_NAME="fpp.db";
    private static final int  DATABASE_VERSION=32;//���İ汾�����ݿ⽫���´���
    private static final String TABLE_NAME="yhxx";
	
    public SQLiteHelper(Context context, String name, CursorFactory factory,
			int version) {
		super(context, name, factory, version);
		// TODO Auto-generated constructor stub
	}
    
    public SQLiteHelper(Context context) {
    	 super(context, DATABASE_NAME, null, DATABASE_VERSION);//�̳и���
		// TODO Auto-generated constructor stub
	}


     
    /**
     * ��SQLiteOpenHelper�����൱�У�������������캯��
     * @param context     ��ǰ��Activity
     * @param name        ������֣����������ݿ�����֣�������������������ݿ�ģ�
     * @param factory      �����ڲ�ѯ���ݿ��ʱ�򷵻�Cursor�����࣬����ֵ
     * @param version      ��ǰ�����ݿ�İ汾��������Ϊ��������
     * @return 
     */
//    public  SQLitedata(Context context)
//    {
//        super(context, DATABASE_NAME, null, DATABASE_VERSION);//�̳и���
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
	       // Log.e("create","���ݿⴴ���ɹ�");

	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        String sql = "DROP TABLE IF EXISTS " + TABLE_NAME; 
        db.execSQL(sql);
        this.onCreate(db);
        // TODO Auto-generated method stub
        System.out.println("���ݿ��Ѿ�����");
        /**
         * �ڴ���Ӹ������ݿ���Ҫִ�еĲ���
         */
    }

}
