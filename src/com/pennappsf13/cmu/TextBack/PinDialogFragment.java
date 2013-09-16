package com.pennappsf13.cmu.TextBack;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import com.actionbarsherlock.app.SherlockDialogFragment;

/**
 * Created with IntelliJ IDEA.
 * User: dsyang
 * Date: 9/7/13
 * Time: 7:26 PM
 * To change this template use File | Settings | File Templates.
 */
public class PinDialogFragment extends SherlockDialogFragment {

    public static final String EXTRA_PIN = "com.pennappsf12.cmu.TextBack.pinCode";
    public static final String EXTRA_MESSAGE = "com.pennappsf12.cmu.TextBack.dialogmessage";

    public static PinDialogFragment newInstance (int pinCode, String message) {
        Bundle args = new Bundle();
        args.putInt(EXTRA_PIN, pinCode);

        if(message != "") {
            args.putString(EXTRA_MESSAGE, message);
        }

        PinDialogFragment fragment = new PinDialogFragment();
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        int pinCode = getArguments().getInt(EXTRA_PIN);
        String message = getArguments().getString(EXTRA_MESSAGE);
        Dialog d = null;

        if (pinCode == -1) {
            if (message == null) {
                message = "You opted not to register, therefore you do not have a pin code.";
            }
            d =  new AlertDialog.Builder(getActivity())
                    .setTitle("Remote Access PIN Code")
                    .setMessage(message)
                    .setPositiveButton(R.string.prompt_reset_pin, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            getActivity().getSharedPreferences(getString(R.string.shared_pref_name), 0).edit()
                                    .putBoolean(getString(R.string.pref_first_time), true).commit();
                            Intent i = new Intent(getActivity(), MainActivity.class);
                            startActivity(i);
                        }
                    })
                    .create();
        } else {
            message = "Your pin number to start this app remotely is: " + pinCode + ".";
            d =  new AlertDialog.Builder(getActivity())
                    .setTitle("Remote Access PIN Code")
                    .setMessage(message)
                    .create();
        }

        return d;

    }
}
