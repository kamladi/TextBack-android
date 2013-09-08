package com.pennappsf13.cmu.TextBack;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Button;
import com.actionbarsherlock.app.SherlockFragment;
import com.actionbarsherlock.app.SherlockListFragment;

import java.util.ArrayList;

/**
 * Created with IntelliJ IDEA.
 * User: kamladi
 * Date: 9/7/13
 * Time: 3:59 PM
 * To change this template use File | Settings | File Templates.
 */
public class TemplateEditFragment extends SherlockFragment {
    public final String TAG = this.getClass().getSimpleName();

    SharedPreferences mPreferences;
    private ArrayList<Template> mTemplates;
    String pin;

    Template editingTemplate;


    public static TemplateEditFragment newInstance() {
        Bundle args = new Bundle();

        TemplateEditFragment fragment = new TemplateEditFragment();
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);    //To change body of overridden methods use File | Settings | File Templates.
        mPreferences = getActivity().getSharedPreferences(getString(R.string.shared_pref_name), 0);
        mTemplates = TemplateCollection.get(getActivity()).getTemplates();

        String title = getActivity().getIntent().getStringExtra("templateTitle");
        editingTemplate = TemplateCollection.get(getActivity()).getTemplate(title);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        String onFlag = getString(R.string.pref_is_on);

        View v = inflater.inflate(R.layout.fragment_template_edit, container, false);

        // Setup UI Elems
        final EditText editTitle = (EditText) v.findViewById(R.id.editTitle);
        final EditText editText = (EditText) v.findViewById(R.id.editText);
        Button saveButton = (Button) v.findViewById(R.id.saveButton);

        editTitle.setText(editingTemplate.getTitle());
        editText.setText(editingTemplate.getText());

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(TAG, "clicked save button ");
                String newTitle = editTitle.getText().toString();
                String newText = editText.getText().toString();
                editingTemplate.setTitle(newTitle);
                editingTemplate.setText(newText);
                TemplateCollection.get(getActivity()).saveTemplates();
                getActivity().finish();
            }
        });

        pin = mPreferences.getString("pin", "LOL");
        if(pin == "LOL") {
            //TODO: prompt for pin, maybe via popup?
        }

        return v;
    }
}
