package com.example.pnrstatus;

import java.util.ArrayList;

import android.app.Activity;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ListView;

public class DetailsActivity extends Activity
{
	ListView listView;
	ArrayAdapter<PnrDetails> pnrAdapter;
	ArrayList<PnrDetails> listPNR = new ArrayList<PnrDetails>();
	PNRController pnrController;
	MyDataHelper helper;
	@Override
	public void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.detailactivity);
		
		listView = (ListView) findViewById(R.id.listView1);
		
		pnrController=new PNRController(this);
		listPNR=pnrController.getAllPNR();
		
		pnrAdapter=new ArrayAdapter<PnrDetails>(this, android.R.layout.simple_spinner_item, listPNR);
		listView.setAdapter(pnrAdapter);
		
		
	}
	class viewDetail extends BaseAdapter
	{

		@Override
		public int getCount() 
		{
			return 0;
		}

		@Override
		public Object getItem(int position) 
		{
			return null;
		}

		@Override
		public long getItemId(int position) 
		{
			return 0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) 
		{		
			return null;
		}
	}
}
