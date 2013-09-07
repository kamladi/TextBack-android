package com.pennappsf13.cmu.TextBack;

import android.app.IntentService;
import android.content.Intent;
import android.telephony.SmsManager;
import android.util.Log;

/**
 * Created with IntelliJ IDEA.
 * User: dsyang
 * Date: 9/7/13
 * Time: 2:48 AM
 * To change this template use File | Settings | File Templates.
 */
public class SMSService extends IntentService {
    private static final String TAG = "SMSService";
    private SmsManager mSmsManager;

    public SMSService() {
        super(TAG);
        mSmsManager = SmsManager.getDefault();
    }
    @Override
    protected void onHandleIntent(Intent intent) {
        Log.i(TAG, "Received an intent:" + intent);
        String numba = intent.getStringExtra(SMSReceiver.EXTRA_SENDEE_NUMBER);
        String body = intent.getStringExtra(SMSReceiver.EXTRA_MESSAGE_BODY);
        mSmsManager.sendTextMessage(numba, null, body, null, null );
    }
}
