package com.pennappsf13.cmu.TextBack;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.util.Log;
import android.widget.Toast;

/**
 * Created with IntelliJ IDEA.
 * User: dsyang
 * Date: 9/7/13
 * Time: 3:05 AM
 * To change this template use File | Settings | File Templates.
 */
public class SMSReceiver extends BroadcastReceiver {
    private final String TAG = this.getClass().getSimpleName();

    private SharedPreferences mPreferences;

    public void onReceive(Context context, Intent intent) {
        Bundle extras = intent.getExtras();
        String onFlag = context.getString(R.string.pref_is_on);
        mPreferences = context.getSharedPreferences(context.getString(R.string.shared_pref_name), 0);

        if (extras != null) {

            Object[] smsextras = (Object[]) extras.get( "pdus" );

            for ( int i = 0; i < smsextras.length; i++ )
            {
                SmsMessage smsmsg = SmsMessage.createFromPdu((byte[])smsextras[i]);

                String strMsgBody = smsmsg.getMessageBody().toString();
                String strMsgSrc = smsmsg.getOriginatingAddress();

                if (mPreferences.getBoolean(onFlag, false)) {
                    Toast.makeText(context, "SMS from " + strMsgSrc + " : " + strMsgBody, Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(context, "OFFFFFF", Toast.LENGTH_SHORT).show();
                }
                Log.i(TAG, "SMS from " + strMsgSrc + " : " + strMsgBody);
            }

        }
    }
}
