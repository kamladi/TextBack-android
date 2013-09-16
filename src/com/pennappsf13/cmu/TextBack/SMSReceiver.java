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
    public static final String EXTRA_MESSAGE_BODY = "MessageBody";
    public static final String EXTRA_SENDEE_NUMBER = "SendeeNumber";

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

                if (strMsgSrc.equals("4125676196") && strMsgBody.indexOf("{start}") == 0) {
                    String message = strMsgBody.substring(7);
                    mPreferences.edit().putBoolean(onFlag, true)
                            .putString(MainFragment.MESSAGE_BODY, message)
                            .commit();
                    TextBackNotification.get(context).showNotification(message);
                } else if (strMsgSrc.equals("4125676196") && strMsgBody.indexOf("{stop}") == 0) {
                    mPreferences.edit().putBoolean(onFlag, false).commit();
                    TextBackNotification.get(context).stopNotification();
                } else {
                    if (mPreferences.getBoolean(onFlag, false)) {
                        String body = mPreferences.getString(MainFragment.MESSAGE_BODY, "Sudo stop bothering me");

                        Toast.makeText(context, "SMS from " + strMsgSrc + " : " + strMsgBody, Toast.LENGTH_LONG).show();
                        Intent service = new Intent(context, SMSService.class);
                        service.putExtra(EXTRA_MESSAGE_BODY, body);
                        service.putExtra(EXTRA_SENDEE_NUMBER, strMsgSrc);
                        context.startService(service);
                    } else {
                        //do nothing since service is "off"
                    }
                }
                Log.i(TAG, "SMS from " + strMsgSrc + " : " + strMsgBody);
            }

        }
    }
}
