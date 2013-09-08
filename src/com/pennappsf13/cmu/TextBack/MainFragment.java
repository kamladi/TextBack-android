package com.pennappsf13.cmu.TextBack;

import android.app.Activity;
import android.content.*;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;
import com.actionbarsherlock.app.SherlockFragment;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;

import java.util.Random;

/**
 * Created with IntelliJ IDEA.
 * User: dsyang
 * Date: 9/7/13
 * Time: 2:08 AM
 * To change this template use File | Settings | File Templates.
 */
public class MainFragment extends SherlockFragment {

    public final String TAG = this.getClass().getSimpleName();

    ToggleButton mToggle;
    SharedPreferences mPreferences;
    TemplateCollection mTemplates;
    String pin;

    TextView currTemplateField;

    private BroadcastReceiver mSmsSendReceiver;
    private BroadcastReceiver mSmsDeliverReceiver;

    public static MainFragment newInstance() {
        Bundle args = new Bundle();

        MainFragment fragment = new MainFragment();
        fragment.setArguments(args);

        return fragment;
    }

    public void showPinDialog() {
        int pin = mPreferences.getInt(getString(R.string.pref_pin_code), -1);
        FragmentManager fm = getSherlockActivity().getSupportFragmentManager();
        PinDialogFragment dialog = PinDialogFragment.newInstance(pin);

        dialog.show(fm, "PINDOALOG");
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.fragment_main_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_view_pin:
                showPinDialog();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);    //To change body of overridden methods use File | Settings | File Templates.
        setHasOptionsMenu(true);
        mPreferences = getActivity().getSharedPreferences(getString(R.string.shared_pref_name), 0);

        mTemplates = TemplateCollection.get(getActivity());


        mSmsSendReceiver = new BroadcastReceiver() {
            private boolean doThings() {
                SharedPreferences p = getActivity().getSharedPreferences(getString(R.string.shared_pref_name), 0);
                return p.getBoolean(getString(R.string.pref_is_on), false);
            }

            @Override
            public void onReceive(Context context, Intent intent) {
                if (doThings()) {
                    switch(getResultCode()) {
                        case Activity.RESULT_OK:
                            Toast.makeText(getActivity(), "SMS has been sent", Toast.LENGTH_SHORT).show();
                            break;
                        case SmsManager.RESULT_ERROR_GENERIC_FAILURE:
                            Toast.makeText(getActivity(), "Generic Failure", Toast.LENGTH_SHORT).show();
                            break;
                        case SmsManager.RESULT_ERROR_NO_SERVICE:
                            Toast.makeText(getActivity(), "No Service", Toast.LENGTH_SHORT).show();
                            break;
                        case SmsManager.RESULT_ERROR_NULL_PDU:
                            Toast.makeText(getActivity(), "Null PDU", Toast.LENGTH_SHORT).show();
                            break;
                        case SmsManager.RESULT_ERROR_RADIO_OFF:
                            Toast.makeText(getActivity(), "Radio Off", Toast.LENGTH_SHORT).show();
                            break;
                        default:
                            break;
                    }
                }
            }
        };
        mSmsDeliverReceiver = new BroadcastReceiver() {
            public static final String SMS_URI = "content://sms";
            public static final String ADDRESS = "address";
            public static final String PERSON = "person";
            public static final String DATE = "date";
            public static final String READ = "read";
            public static final String STATUS = "status";
            public static final String TYPE = "type";
            public static final String BODY = "body";
            public static final String SEEN = "seen";

            public static final int MESSAGE_TYPE_SENT = 2;

            public static final int MESSAGE_IS_NOT_READ = 0;
            public static final int MESSAGE_IS_NOT_SEEN = 0;

            private boolean doThings() {
                SharedPreferences p = getActivity().getSharedPreferences(getString(R.string.shared_pref_name), 0);
                return p.getBoolean(getString(R.string.pref_is_on), false);
            }

            public void putIntoDatabase(ContentResolver resolver, SmsMessage msg) {
                ContentValues values = new ContentValues();
                values.put( ADDRESS, msg.getOriginatingAddress() );
                values.put(DATE, msg.getTimestampMillis());
                values.put( READ, MESSAGE_IS_NOT_READ );
                values.put(STATUS, msg.getStatus());
                values.put( TYPE, MESSAGE_TYPE_SENT );
                values.put( SEEN, MESSAGE_IS_NOT_SEEN );
                values.put( BODY, msg.getMessageBody());
                resolver.insert(Uri.parse(SMS_URI), values);
            }

            @Override
            public void onReceive(Context context, Intent intent) {
                if (doThings()) {
                    switch(getResultCode()) {
                        case Activity.RESULT_OK:
                            Toast.makeText(getActivity(), "SMS Delivered", Toast.LENGTH_SHORT).show();
                            Bundle extras = intent.getExtras();
                            Object pdu = extras.get("pdu");
                            SmsMessage msg = SmsMessage.createFromPdu((byte[]) pdu);
                            putIntoDatabase(context.getContentResolver(), msg);
                            break;
                        case Activity.RESULT_CANCELED:
                            Toast.makeText(getActivity(), "SMS not delivered", Toast.LENGTH_SHORT).show();
                            break;
                    }
                }
            }
        };

        getActivity().registerReceiver(mSmsSendReceiver, new IntentFilter("SMS_SENT"));
        getActivity().registerReceiver(mSmsDeliverReceiver, new IntentFilter("SMS_DELIVERED"));

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        String onFlag = getString(R.string.pref_is_on);

        View v = inflater.inflate(R.layout.fragment_main, container, false);

        if (mPreferences.getBoolean(getString(R.string.pref_first_time), true)) {
            final Random rand = new Random( System.currentTimeMillis());
            int pin = rand.nextInt(80000) + 10000;
            mPreferences.edit().putInt(getString(R.string.pref_pin_code), pin)
                    .putBoolean(getString(R.string.pref_first_time), false)
                    .commit();

            showPinDialog();
        }

        // setup currTemplateField
        currTemplateField = (TextView) v.findViewById(R.id.current_template);
        Template selectedTemplate = mTemplates.getSelectedTemplate();
        currTemplateField.setText(selectedTemplate.getText());

        //set up click listener to go to templateList
        currTemplateField.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(TAG, "clicked template ");
                Intent i = new Intent(getActivity(), TemplateListActivity.class);
                startActivityForResult(i, 0);
            }
        });

        mToggle = (ToggleButton) v.findViewById(R.id.main_toggle);
        if (mPreferences.getBoolean(onFlag, false)) {
            mToggle.setChecked(true);
        } else {
            mToggle.setChecked(false);
        }

        mToggle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            private String onFlag = getString(R.string.pref_is_on);
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    SharedPreferences.Editor e = mPreferences.edit();
                    e.putBoolean(onFlag, true);
                    e.commit();

                    Log.i(TAG, "isOn = " + mPreferences.getBoolean(onFlag, false));
                } else {
                    SharedPreferences.Editor e = mPreferences.edit();
                    e.putBoolean(onFlag, false);
                    e.putString("pin", pin);
                    e.commit();
                    Log.i(TAG, "turning off. isOn = " + mPreferences.getBoolean(onFlag, false));
                }
            }
        });
        return v;
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
    }

    @Override
    public void onPause() {
        super.onPause();
        mTemplates.saveTemplates();
    }

    @Override
    public void onDestroy() {
        getActivity().unregisterReceiver(mSmsSendReceiver);
        getActivity().unregisterReceiver(mSmsDeliverReceiver);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == 0) {
            if(resultCode == 0) {
                String newSelectedTemplate = data.getStringExtra("selectedTemplate");
                if (newSelectedTemplate != null) {
                    currTemplateField.setText(newSelectedTemplate);
                }
            }
        }
    }
}
