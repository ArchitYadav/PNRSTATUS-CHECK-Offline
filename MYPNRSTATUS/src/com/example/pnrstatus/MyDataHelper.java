package com.example.pnrstatus;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class MyDataHelper extends SQLiteOpenHelper 
{
	public static final int VERSION = 1;
	public static final String DBName = "PNRDB";

	public MyDataHelper(Context context, String name, CursorFactory factory,int version) 
	{
		super(context, name, factory, version);
	}

	@Override
	public void onCreate(SQLiteDatabase db) 
	{
		String q = "create table pnrDetails(PNR text,Train text,Dt text,CurrStat text,SchdDeptTime text)";
		db.execSQL(q);
		/*String q1 = "insert into pnrDetails values('?','?','?','?','?')";
		db.execSQL(q1);*/
		Log.e("DB:", "on create");
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) 
	{
		
	
	}
}
