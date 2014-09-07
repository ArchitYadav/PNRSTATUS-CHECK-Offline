package com.example.pnrstatus;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.TimeZone;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.provider.CalendarContract.Calendars;
import android.provider.CalendarContract.Events;
import android.provider.CalendarContract.Reminders;
import android.util.Log;
import android.widget.ListView;

public class CalenderEntry extends Activity {

	long calID = 0;
	long startMillis = 0;
	long beforeMillis = 0;
	long endMillis = 0;
	AlarmManager am;

	public static final String[] EVENT_PROJECTION = new String[] {
		Calendars._ID, // 0
		Calendars.ACCOUNT_NAME, // 1
		Calendars.CALENDAR_DISPLAY_NAME, // 2
		Calendars.OWNER_ACCOUNT // 3
	};

	// The indices for the projection array above.
	private static final int PROJECTION_ID_INDEX = 0;
	private static final int PROJECTION_ACCOUNT_NAME_INDEX = 1;
	private static final int PROJECTION_DISPLAY_NAME_INDEX = 2;
	private static final int PROJECTION_OWNER_ACCOUNT_INDEX = 3;

	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);

		Intent in = getIntent();
		String pnr = in.getStringExtra("pnr");
		String train = in.getStringExtra("train");
		String date = in.getStringExtra("date");
		String time = in.getStringExtra("time");

		String[] tokens = date.split("-");
		int eDay = Integer.parseInt(tokens[0]);
		int eMonth = Integer.parseInt(tokens[1]);
		int eYear = Integer.parseInt(tokens[2]);

		String[] hm = time.split(":");
		int startHr = Integer.parseInt(tokens[0]);
		int startMin = Integer.parseInt(tokens[1]);

		am = (AlarmManager) getSystemService(ALARM_SERVICE);

		// cursor to get account info
		Cursor cur = null;
		ContentResolver cr = getContentResolver();
		Uri uri = Calendars.CONTENT_URI;

		String selection = "((" + Calendars.ACCOUNT_NAME + " = ?) AND ("
		+ Calendars.ACCOUNT_TYPE + " = ?) AND ("
		+ Calendars.OWNER_ACCOUNT + " = ?))";
		String[] selectionArgs = new String[] { CalenderEntry.getEmail(this),
				"com.google", CalenderEntry.getEmail(this) };

		// Submit the query and get a Cursor object back.
		cur = cr.query(uri, EVENT_PROJECTION, selection, selectionArgs, null);

		// Use the cursor to step through the returned records
		boolean curs;
		while (cur.moveToNext()) {

			String displayName = null;
			String accountName = null;
			String ownerName = null;
			Log.e("while", "" + cur);
			
			// Get the field values
			calID = cur.getLong(PROJECTION_ID_INDEX);
			displayName = cur.getString(PROJECTION_DISPLAY_NAME_INDEX);
			accountName = cur.getString(PROJECTION_ACCOUNT_NAME_INDEX);
			ownerName = cur.getString(PROJECTION_OWNER_ACCOUNT_INDEX);

		}

		// add entry in calender
		Calendar beginTime = Calendar.getInstance();
		beginTime.set(eYear, eMonth - 1, eDay, startHr, startMin);
		Calendar beforeTime = Calendar.getInstance();
		beforeTime.set(eYear, eMonth - 1, eDay, startHr-5, 00);
		startMillis = beginTime.getTimeInMillis();
		beforeMillis = beforeTime.getTimeInMillis();
		Calendar endTime = Calendar.getInstance();
		endTime.set(eYear, eMonth - 1, eDay, startHr, startMin);
		endMillis = endTime.getTimeInMillis();

		ContentResolver cr1 = getContentResolver();
		ContentValues values = new ContentValues();
		values.put(Events.DTSTART, startMillis);
		values.put(Events.DTEND, endMillis);
		values.put(Events.TITLE, "Train Departure");
		values.put(Events.DESCRIPTION, "Time to leave");
		values.put(Events.CALENDAR_ID, calID);
		TimeZone timeZone = TimeZone.getDefault();
        values.put(CalendarContract.Events.EVENT_TIMEZONE, timeZone.getID());

		//values.put(Events.EVENT_TIMEZONE, "(GMT+5:30) Kolkata");
		values.put(Events.HAS_ALARM, 1);
		Uri uri1 = cr1.insert(Events.CONTENT_URI, values);

		int id = Integer.parseInt(uri1.getLastPathSegment());

		String reminderUriString = "content://com.android.calendar/reminders";

		ContentValues reminders = new ContentValues();
		reminders.put(Reminders.EVENT_ID, id);
		reminders.put(Reminders.METHOD, Reminders.METHOD_ALERT);
		reminders.put(Reminders.MINUTES, 5);

		Uri uri2 = cr.insert(Reminders.CONTENT_URI, reminders);

		Intent intent=new Intent(CalenderEntry.this,Myservice.class);
		intent.putExtra("startHr",startHr );
		intent.putExtra("startMin",startMin );

		PendingIntent pending = PendingIntent.getService(CalenderEntry.this,
				101, in, PendingIntent.FLAG_ONE_SHOT);
		am.set(AlarmManager.RTC, beforeMillis, pending);
	}

	static String getEmail(Context context) {
		AccountManager accountManager = AccountManager.get(context);
		Account account = getAccount(accountManager);

		if (account == null) {
			return null;
		} else {
			return account.name;
		}
	}

	private static Account getAccount(AccountManager accountManager) {
		Account[] accounts = accountManager.getAccountsByType("com.google");
		Account account;
		if (accounts.length > 0) {
			account = accounts[0];
		} else {
			account = null;
		}
		return account;
	}

}
