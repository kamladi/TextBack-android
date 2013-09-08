package com.pennappsf13.cmu.TextBack;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.ToggleButton;
import com.actionbarsherlock.app.SherlockFragment;

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

    public static MainFragment newInstance() {
        Bundle args = new Bundle();

        MainFragment fragment = new MainFragment();
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);    //To change body of overridden methods use File | Settings | File Templates.
        mPreferences = getActivity().getSharedPreferences(getString(R.string.shared_pref_name), 0);

        mTemplates = TemplateCollection.get(getActivity());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        String onFlag = getString(R.string.pref_is_on);

        View v = inflater.inflate(R.layout.fragment_main, container, false);

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

        pin = mPreferences.getString("pin", "LOL");
        if(pin == "LOL") {
            //TODO: prompt for pin, maybe via popup?
        }
        mTemplates.fetch();

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
