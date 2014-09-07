package com.example.pnrstatus;

import java.util.ArrayList;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class PNRController 
{
	MyDataHelper helper;
	public PNRController(Context context)
	{
		helper = new MyDataHelper(context, MyDataHelper.DBName, null, MyDataHelper.VERSION);
	}
	public long insertPNR(PnrDetails pnr)
	{
		SQLiteDatabase db = helper.getWritableDatabase();
		ContentValues cv = new ContentValues();
		cv.put("PNR", pnr.getPnrNo());
		cv.put("Train", pnr.getTrainName());
		cv.put("Dt", pnr.getDateOfJ());
		cv.put("CurrStat", pnr.getCurrStat());
		cv.put("SchdDeptTime", pnr.getTime());
		
		long result = db.insert("pnrDetails", null,cv);
		db.close();
		return result;
	}
	public ArrayList<PnrDetails> getAllPNR()
	{
		ArrayList<PnrDetails> listPNR = new ArrayList<PnrDetails>();
		//--------------------------------------------------------
		
		SQLiteDatabase db = helper.getWritableDatabase();
		//select data
		String q = "select * from pnrDetails";
		Cursor cursor = db.rawQuery(q, null);
		//rows in the cursor
		while(cursor.moveToNext())
		{
			String PNR = cursor.getString(0);
			String Train=cursor.getString(1);
			String Dt=cursor.getString(2);
			String CurrStat = cursor.getString(3);
			String SchdDeptTime=cursor.getString(4);
			
			PnrDetails pnrD=new PnrDetails(PNR, Train, Dt, CurrStat, SchdDeptTime);
			listPNR.add(pnrD);
		}		
		cursor.close();
		db.close();		
		
		return listPNR;
	}
}
