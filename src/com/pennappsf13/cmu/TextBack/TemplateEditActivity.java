package com.pennappsf13.cmu.TextBack;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;

/**
 * Created with IntelliJ IDEA.
 * User: kamladi
 * Date: 9/7/13
 * Time: 3:58 PM
 * To change this template use File | Settings | File Templates.
 */
public class TemplateEditActivity extends SingleFragmentActivity {
    @Override
    protected Fragment createFragment() {
        return new TemplateEditFragment();
    }
}