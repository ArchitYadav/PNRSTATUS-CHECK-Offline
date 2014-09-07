package com.example.pnrstatus;

import java.util.HashMap;
import java.util.Iterator;
import java.util.regex.MatchResult;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;
import android.text.Editable;
import android.text.method.KeyListener;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class PnrDetailsMainActivity extends Activity implements KeyListener
{
	static String PNR;
	String mySmsText;
	  static final String PREFSFILE = "PnrautoCompleteValues";
	  String[] autoCompleteValues;
	  SharedPreferences autoCompleteValuesPrefs;
	  final Context context = this;
	   AutoCompleteTextView pnrAutoCompleteTextView;
	  EditText editTextPnrNumber;
	  Button buttonStatus;
	  Button buttonDetails;
	  AlertDialog alertDialogAlert;
	  MessageBroadcastReceiver smsReciever=new MessageBroadcastReceiver();
	  IntentFilter filter=new IntentFilter();
	  PNRController pnrcontroller=new PNRController(this);
	  
	@Override
	public void clearMetaKeyState(View view, Editable content, int states) 
	{
		
	}

	@Override
	public int getInputType() 
	{
		return 0;
	}
	
	 public void onCreate(Bundle savedInstanceState)
	 {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.activity_main);
	    
	    editTextPnrNumber = (EditText) findViewById(R.id.pnrText);
	    buttonStatus = (Button) findViewById(R.id.pnrButton);
	    buttonDetails=(Button) findViewById(R.id.detailsButton);
	    
	    buttonDetails.setOnClickListener(new OnClickListener() 
	    {
			
			@Override
			public void onClick(View v) 
			{
				Intent in=new Intent(PnrDetailsMainActivity.this,DetailsActivity.class);
				startActivity(in);
			}
		});
	    
	    buttonStatus.setOnClickListener(new OnClickListener()
	    {		
			@Override
			public void onClick(View v) 
			{
				final String pnrStr=editTextPnrNumber.getText().toString();
				if(pnrStr.length()==10)
				{
					AlertDialog.Builder builder = new AlertDialog.Builder(PnrDetailsMainActivity.this);
					builder.setTitle("Mode selected SMS");
					builder.setMessage("Are you sure to use SMS mode.the standard charges may apply(usual rate by govt of India is Rs 3.00, but it totally depends on your network operator)");
					builder.setCancelable(false);
				
					builder.setPositiveButton("Continue",new DialogInterface.OnClickListener() 
					{
				
						public void onClick(DialogInterface dialog, int which) 
						{
								sendSMS("139", "PNR " + editTextPnrNumber.getText().toString());
								Toast.makeText(context, "139", Toast.LENGTH_LONG).show();
								
						}
					
				});
				builder.setNegativeButton("Cancel",	new DialogInterface.OnClickListener() 
				{
					public void onClick(DialogInterface dialog, int which) 
					{
						alertDialogAlert.dismiss();
					}
				});
				alertDialogAlert = builder.create();
				alertDialogAlert.show();
			}
			else
			{
				Toast.makeText(PnrDetailsMainActivity.this,"INVALID PNR",Toast.LENGTH_SHORT).show();
			}
			}
		});
	    filter.addAction("android.provider.Telephony.SMS_RECEIVED");
		registerReceiver(smsReciever, filter);
		
	  }
	 
	  private void sendSMS(String phoneNumber, String message) 
	  {
		  String SENT = "SMS_SENT";
		  String DELIVERED = "SMS_DELIVERED";

		  PendingIntent sentPI = PendingIntent.getBroadcast(this, 0, new Intent(SENT), 0);
		  PendingIntent deliveredPI = PendingIntent.getBroadcast(this, 0,
    	  new Intent(DELIVERED), 0);
		  Toast.makeText(this, "Msg", Toast.LENGTH_LONG).show();
		  SmsManager sms = SmsManager.getDefault();
			sms.sendTextMessage("139", null, message, sentPI, deliveredPI);
			 Toast.makeText(this, "Msg Send", Toast.LENGTH_LONG).show();
		  // ---when the SMS has been sent---
		  registerReceiver(new BroadcastReceiver() 
		  {
			  @Override
			  public void onReceive(Context arg0, Intent arg1) 
			  {
					switch (getResultCode()) 
					{
					 	case Activity.RESULT_OK:
						Toast.makeText(getBaseContext(), "SMS sent",
								Toast.LENGTH_SHORT).show();
						break;
					 	case SmsManager.RESULT_ERROR_GENERIC_FAILURE:
						Toast.makeText(getBaseContext(), "Generic failure",
								Toast.LENGTH_SHORT).show();
						break;
					 	case SmsManager.RESULT_ERROR_NO_SERVICE:
						Toast.makeText(getBaseContext(), "No service",
								Toast.LENGTH_SHORT).show();
						break;
					 	case SmsManager.RESULT_ERROR_NULL_PDU:
						Toast.makeText(getBaseContext(), "Null PDU",
								Toast.LENGTH_SHORT).show();
						break;
					 	case SmsManager.RESULT_ERROR_RADIO_OFF:
						Toast.makeText(getBaseContext(), "Radio off",
								Toast.LENGTH_SHORT).show();
						break;
					}
				}
			}, new IntentFilter(SENT));

			// ---when the SMS has been delivered---
			registerReceiver(new BroadcastReceiver() 
			{
				@Override
				public void onReceive(Context arg0, Intent arg1) 
				{
					switch (getResultCode()) 
					{
					case Activity.RESULT_OK:
						Toast.makeText(getBaseContext(), "SMS delivered",
								Toast.LENGTH_SHORT).show();
						break;
					case Activity.RESULT_CANCELED:
						Toast.makeText(getBaseContext(), "SMS not delivered",
								Toast.LENGTH_SHORT).show();
						break;
					}
				}
			}, new IntentFilter(DELIVERED));

			sms = SmsManager.getDefault();
			sms.sendTextMessage(phoneNumber, null, message, sentPI, deliveredPI);
		}
	 
	 @Override
	public boolean onCreateOptionsMenu(Menu menu) 
	 {
		 getMenuInflater().inflate(R.menu.main, menu);
		 return super.onCreateOptionsMenu(menu);
	}	 

	@Override
	 public boolean onKeyDown(int paramInt, KeyEvent paramKeyEvent)
	 {
	    if (paramInt == 4)
	    {
	      finish();
	    }
	    return super.onKeyDown(paramInt, paramKeyEvent);
	 }

	@Override
	public boolean onKeyOther(View view, Editable text, KeyEvent event) 
	{
		return false;
	}

	@Override
	public boolean onKeyUp(View view, Editable text, int keyCode, KeyEvent event) 
	{
		return false;
	}
	
	public boolean onOptionsItemSelected(MenuItem paramMenuItem)
	{
	    return super.onOptionsItemSelected(paramMenuItem);
	}

	@Override
	public boolean onKeyDown(View view, Editable text, int keyCode,
			KeyEvent event) 
	{
		return false;
	}
	
	public class MessageBroadcastReceiver extends BroadcastReceiver 
	{
		@Override		
		 public void onReceive(Context context, Intent intent) 
		{
            Bundle bundle = intent.getExtras();
            if (bundle != null) 
            {
            	Object[] pdus = (Object[])bundle.get("pdus");
            	final SmsMessage[] messages = new SmsMessage[pdus.length];
            	for (int i = 0; i < pdus.length; i++) 
            	{
            		messages[i] = SmsMessage.createFromPdu((byte[])pdus[i]);
            	}
            	StringBuffer content = new StringBuffer();
            	if (messages.length > 0) 
            	{
            		for (int i = 0; i < messages.length; i++) 
            		{
                      content.append(messages[i].getMessageBody());
            		}
                }
            	mySmsText= content.toString();
                
   				if(mySmsText.startsWith("PNR" ))
   				{
   					String pnrNo="",TrainNumber="",date="",status="",time="";
   					Pattern p = Pattern.compile("\\d{10}");
   					Matcher m = p.matcher(mySmsText);
                
   					if (m.find()) 
   					{
   						MatchResult mr=m.toMatchResult();
   						pnrNo= mr.group(0);
   					}
                 //-----------------------------------------------------------------------------// 
                 
   					Pattern p1 = Pattern.compile("\\d{5}");
   					Matcher m1 = p1.matcher(mySmsText);
                 
   					if (m1.find()) 
   					{
   						MatchResult mr1=m1.toMatchResult();
   						TrainNumber=mr1.group(0);
                	
   					}
                
                
                //-----------------------------------------------------------------------------//                
             
                
   					Pattern p2 = Pattern.compile("\\d{1,2}(-)\\d{1,2}(-)\\d{4}");
   					Matcher m2 = p2.matcher(mySmsText);

   					if (m2.find()) 
   					{
   						MatchResult mr2=m2.toMatchResult();
   						date= mr2.group(0);
   					}	
                
                
//-----------------------------------------------------------------------------//
                
                
   					Pattern p3 = Pattern.compile("CLASS\\s+(.*?)\\s+Chart");
   					Matcher m3 = p3.matcher(mySmsText);
                 
   					if (m3.find()) 
   					{
   						MatchResult mr3=m3.toMatchResult();
   						status = mr3.group(0);
   						status=status.replaceAll("CLASS :","").replaceAll("Chart", "");
   					}
              
//-----------------------------------------------------------------------------//
                
   					Pattern p4 = Pattern.compile("\\d{1,2}(:)\\d{2}");
   					Matcher m4 = p4.matcher(mySmsText);
            
   					if (m4.find()) 
   					{
   						MatchResult mr4=m4.toMatchResult();
   						time = mr4.group(0);
   					}
                    
   					if(status.contains("W/L") || status.contains("W"))
   					{
   						Intent in=new Intent(PnrDetailsMainActivity.this,TimerService.class);
   						in.putExtra("pnr", pnrNo);
   						startService(in);
   					}

   					PnrDetails pnrD=new PnrDetails(pnrNo,TrainNumber,date,status,time);
   					long r = pnrcontroller.insertPNR(pnrD);

   					if(r!=-1)
   					{
   						Toast.makeText(PnrDetailsMainActivity.this, "Reminder is set.", Toast.LENGTH_LONG).show();
   					}
				
				//Toast.makeText(PnrDetailsMainActivity.this, "Done", Toast.LENGTH_LONG).show();
				
   					Intent in=new Intent(PnrDetailsMainActivity.this,CalenderEntry.class);
   					in.putExtra("pnr", pnrNo);
   					in.putExtra("train", TrainNumber);
   					in.putExtra("date", date);
   					in.putExtra("time", time);
   					startActivityForResult(in,101);
   				}
            }
		}
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) 
	{
		super.onActivityResult(requestCode, resultCode, data);
		
		if(requestCode==101)
		{
			Intent open = new Intent(context,StatusDisplay.class);
			open.putExtra("Message", mySmsText);
			context.startActivity(open);
		}
	}
}