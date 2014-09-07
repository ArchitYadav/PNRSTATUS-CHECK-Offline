package com.example.pnrstatus;

import java.util.Timer;
import java.util.TimerTask;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.telephony.SmsManager;

public class TimerService extends Service
{
	
	private Timer timer = new Timer();
	Intent intent1;
	public void onCreate() 
	{
		super.onCreate();
	}
	
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) 
	{
		intent1=intent;
		timer.scheduleAtFixedRate( new TimerTask() 
		{
			public void run()
			{	String pnr=intent1.getStringExtra("pnr");
				System.out.println("PNR=="+pnr);
				SmsManager sms = SmsManager.getDefault();
				sms.sendTextMessage("139", null,"PNR "+pnr, null, null);	
			}
		}, 0, 10000);
		stopSelf();
		return super.onStartCommand(intent, flags, startId);
	} 
	
	/*@Override
	public boolean stopService(Intent name) 
	{
		
		return super.stopService(name);
	}*/
	
	@Override
	public IBinder onBind(Intent intent) 
	{
		return null;
	}
}
