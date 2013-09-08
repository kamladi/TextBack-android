package com.pennappsf13.cmu.TextBack;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;
import com.actionbarsherlock.app.SherlockDialogFragment;
import com.actionbarsherlock.app.SherlockFragment;

/**
 * Created with IntelliJ IDEA.
 * User: dsyang
 * Date: 9/7/13
 * Time: 2:47 PM
 * To change this template use File | Settings | File Templates.
 */
public class RegistrationFragment extends SherlockFragment {
    private final String TAG = this.getClass().getSimpleName();
    private SharedPreferences mPreferences;

    public static RegistrationFragment newInstance() {
        Bundle args = new Bundle();

        RegistrationFragment fragment = new RegistrationFragment();
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);    //To change body of overridden methods use File | Settings | File Templates.
        mPreferences = getActivity().getSharedPreferences(getString(R.string.shared_pref_name), 0);

    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.prompt_pin_code, container, false);


        String pin = mPreferences.getString("passCode", "shi");
        EditText passcode = (EditText) v.findViewById(R.id.prompt_pin_code);
        passcode.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                SharedPreferences.Editor editor = mPreferences.edit();
                editor.putString("passCode", s.toString());
                editor.commit();
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
            @Override
            public void afterTextChanged(Editable s) { }
        });


        Button submitButton = (Button) v.findViewById(R.id.prompt_submit_button);
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(), MainActivity.class);
                startActivity(i);
            }
        });





        return v;
    }

/*
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        LayoutInflater li = LayoutInflater.from(getActivity());
        View prompt = li.inflate(R.layout.prompt_pin_code, null);
        Log.d(TAG, "Activity: " + getActivity() );
        //final EditText pin = (EditText) prompt.findViewById(R.id.prompt_pin_code);

        return new AlertDialog.Builder(getActivity())
                .setView(prompt)
                .setTitle("First Time Registration")
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(getActivity(), "herpader", Toast.LENGTH_LONG);
                        //Log.i(TAG, "OK clicked with passcode: " + pin.getText().toString());
                    }
                })
                .setNegativeButton("Fail", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(getActivity(), "www", Toast.LENGTH_LONG);
                        //Log.i(TAG, "FAIL cut with passcode: " + pin.getText().toString());
                        PinDialog.this.getDialog().cancel();
                    }
                })
                .create();
    }*/
}
