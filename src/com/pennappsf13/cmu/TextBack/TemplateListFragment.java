package com.pennappsf13.cmu.TextBack;

import android.os.Bundle;
import com.actionbarsherlock.app.SherlockFragment;

/**
 * Created with IntelliJ IDEA.
 * User: kamladi
 * Date: 9/7/13
 * Time: 6:43 AM
 * To change this template use File | Settings | File Templates.
 */
public class TemplateListFragment extends SherlockFragment {
    public static TemplateListFragment newInstance() {
        Bundle args = new Bundle();

        TemplateListFragment fragment = new TemplateListFragment();
        fragment.setArguments(args);

        return fragment;
    }


}
