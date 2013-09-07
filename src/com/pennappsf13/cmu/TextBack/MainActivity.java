package com.pennappsf13.cmu.TextBack;

import com.actionbarsherlock.app.SherlockFragment;

/**
 * Created with IntelliJ IDEA.
 * User: dsyang
 * Date: 9/7/13
 * Time: 2:06 AM
 * To change this template use File | Settings | File Templates.
 */
public class MainActivity extends SingleFragmentActivity {
    @Override
    protected SherlockFragment createFragment() {
        return MainFragment.newInstance();
    }
}