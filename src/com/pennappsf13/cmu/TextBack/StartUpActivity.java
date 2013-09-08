package com.pennappsf13.cmu.TextBack;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import com.actionbarsherlock.app.SherlockFragmentActivity;

/**
 * Created with IntelliJ IDEA.
 * User: dsyang
 * Date: 9/7/13
 * Time: 4:41 PM
 * To change this template use File | Settings | File Templates.
 */
public class StartUpActivity extends SherlockFragmentActivity {

    private SharedPreferences mPreferences;

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        mPreferences = getSharedPreferences(getString(R.string.shared_pref_name), 0);

        setTheme(R.style.Theme_Sherlock);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragment);

        FragmentManager manager = getSupportFragmentManager();
        Fragment fragment = manager.findFragmentById(R.id.fragmentContainer);

        if (savedInstanceState == null && fragment == null) {
            if (mPreferences.getBoolean("firstRun", true)) {
                fragment = RegistrationFragment.newInstance();
            } else {
                fragment = MainFragment.newInstance();
            }

            manager.beginTransaction()
                    .add(R.id.fragmentContainer, fragment)
                    .commit();
        }

    }

}
