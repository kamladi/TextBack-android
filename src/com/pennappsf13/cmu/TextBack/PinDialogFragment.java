package com.pennappsf13.cmu.TextBack;

import android.app.AlertDialog;
import android.app.Dialog;
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

    public static PinDialogFragment newInstance (int pinCode) {
        Bundle args = new Bundle();
        args.putInt(EXTRA_PIN, pinCode);

        PinDialogFragment fragment = new PinDialogFragment();
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        int pinCode = getArguments().getInt(EXTRA_PIN);

        return new AlertDialog.Builder(getActivity())
                .setTitle("Remote Access PIN Code")
                .setMessage("Your pin number to start this app remotely is: " + pinCode + ".")
                .create();
    }
}
