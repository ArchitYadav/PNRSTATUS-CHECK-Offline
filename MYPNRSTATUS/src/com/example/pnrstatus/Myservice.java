package com.example.pnrstatus;

import java.util.Calendar;


import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.ComponentName;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

public class Myservice extends Service
{

	NotificationManager nm;
	@Override
	public void onCreate() {
		// TODO Auto-generated method stub

		super.onCreate();
	}
	@SuppressWarnings("deprecation")
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		
		int startHr=intent.getIntExtra("startHr", 0);
		int startMin=intent.getIntExtra("startMin", 0);
		
		Log.e("my services", "on start");
		String msg= "Train Departure"+""+startHr+":"+startMin;
		
		Intent i = new Intent(this,StatusDisplay.class);
		   
	PendingIntent pending=PendingIntent.getActivity(this, 101, i, PendingIntent.FLAG_UPDATE_CURRENT);
	nm=(NotificationManager) getSystemService(NOTIFICATION_SERVICE);
	
	Notification notification=new Notification(R.drawable.train, "Train Departure", System.currentTimeMillis());
	
	notification.setLatestEventInfo(Myservice.this, ""+"Train Departure", ""+msg, pending);
	Log.e("my services", "notification set");

	nm.notify(101, notification);
		
	return super.onStartCommand(intent, flags, startId);
	}
	
	@Override
	public void onDestroy() 
	{
		super.onDestroy();
		
	}
	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}

}
