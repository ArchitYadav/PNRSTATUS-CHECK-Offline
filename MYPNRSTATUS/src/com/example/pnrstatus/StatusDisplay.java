package com.example.pnrstatus;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.util.Log;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;
import android.widget.Toast;

public class StatusDisplay extends Activity
{
  Context context;
  TextView textViewPNRStatus;

  protected void onCreate(Bundle savedInstanceState)
  {
    super.onCreate(savedInstanceState); 
    setContentView(R.layout.status);
    textViewPNRStatus = (TextView) findViewById(R.id.messageStatus);
    
    Intent intent = getIntent();
    String data = intent.getStringExtra("Message");
    textViewPNRStatus.setText(data);
  }
}