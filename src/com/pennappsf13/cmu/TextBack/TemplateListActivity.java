package com.pennappsf13.cmu.TextBack;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import com.actionbarsherlock.app.SherlockFragment;

/**
 * Created with IntelliJ IDEA.
 * User: kamladi
 * Date: 9/7/13
 * Time: 6:36 AM
 * To change this template use File | Settings | File Templates.
 */
public class TemplateListActivity extends SingleFragmentActivity {
    @Override
    protected Fragment createFragment() {
        return new TemplateListFragment();
    }
}