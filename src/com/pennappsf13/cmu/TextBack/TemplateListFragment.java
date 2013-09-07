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
 * User: kamladi
 * Date: 9/7/13
 * Time: 6:43 AM
 * To change this template use File | Settings | File Templates.
 */
public class TemplateListFragment extends SherlockFragment {

    public final String TAG = this.getClass().getSimpleName();

    SharedPreferences mPreferences;
    TemplateCollection templates;
    String pin;

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
        templates = new TemplateCollection();


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        String onFlag = getString(R.string.pref_is_on);

        View v = inflater.inflate(R.layout.fragment_template_list, container, false);

        pin = mPreferences.getString("pin", "LOL");
        if(pin == "LOL") {
            //TODO: prompt for pin, maybe via popup?
        }
        templates.fetch();
        return v;
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
    }
}
