package com.pennappsf13.cmu.TextBack;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import com.actionbarsherlock.app.SherlockListFragment;

import java.util.ArrayList;

/**
 * Created with IntelliJ IDEA.
 * User: kamladi
 * Date: 9/7/13
 * Time: 6:43 AM
 * To change this template use File | Settings | File Templates.
 */
public class TemplateListFragment extends SherlockListFragment {
    private ArrayList<Template> mTemplates;
    private ListView mListView;

    public final String TAG = this.getClass().getSimpleName();

    SharedPreferences mPreferences;
    String pin;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);    //To change body of overridden methods use File | Settings | File Templates.
        mPreferences = getActivity().getSharedPreferences(getString(R.string.shared_pref_name), 0);
        mTemplates = TemplateCollection.get(getActivity()).getTemplates();

        TemplateAdapter adapter = new TemplateAdapter(mTemplates);
        setListAdapter(adapter);


    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        Template t = (Template)(getListAdapter()).getItem(position);
        Log.i(TAG, "Template t: " + t.getTitle() + " was clicked!" );
    }

    @Override
    public void onStart() {
        super.onStart();    //To change body of overridden methods use File | Settings | File Templates.
        mListView = getListView();
        mListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                Template t = (Template)(getListAdapter()).getItem(position);
                Log.i(TAG, "Template t: " + t.getTitle() + " was long clicked!" );

                return true;
            }
        });

    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
    }

    private class TemplateAdapter extends ArrayAdapter<Template> {
        public TemplateAdapter(ArrayList<Template> templates) {
            super(getActivity(), android.R.layout.simple_list_item_1, templates);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = getActivity().getLayoutInflater()
                        .inflate(R.layout.list_item_template, null);
            }

            Template t = getItem(position);


            TextView titleView = (TextView) convertView.findViewById(R.id.template_list_item_titleTextView);
            titleView.setText(t.getTitle());

            TextView templateTextView = (TextView) convertView.findViewById(R.id.template_list_item_textTextView);
            templateTextView.setText(t.getText());

            return convertView;
        }
    }
}
