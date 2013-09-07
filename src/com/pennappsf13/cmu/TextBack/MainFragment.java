package com.pennappsf13.cmu.TextBack;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Switch;
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
    Template[] templates;
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
        //templates = new Template[10];
        templates = new Template[] {
            new Template("Meeting", "In a meeting, text you in a bit!", true),
            new Template("Shower", "In the shower, I'll text you when I get out", false),
            new Template("Lost", "I lost my phone, I'll be sure to reply when I find it", false),
        };
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        String onFlag = getString(R.string.pref_is_on);

        View v = inflater.inflate(R.layout.fragment_main, container, false);

        // setup currTemplateField
        currTemplateField = (TextView) v.findViewById(R.id.current_template);
        Template selectedTemplate = getSelectedTemplate();
        currTemplateField.setText(selectedTemplate.getText());

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
                    e.commit();
                    Log.i(TAG, "turing off. isOn = " + mPreferences.getBoolean(onFlag, false));
                }
            }
        });
        return v;
    }

    public void getTemplates() {
        // TODO: send POST request to server, load templates into memory
    }

    public Template getSelectedTemplate() {
        for (Template t : templates) {
            if (t.isSelected()) {
                return t;
            }
        }
        // if no template is selected, assume first template selected
        templates[0].setSelected(true);
        return templates[0];
    }
}
