package com.pennappsf13.cmu.TextBack;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Button;
import com.actionbarsherlock.app.SherlockFragment;
import com.actionbarsherlock.app.SherlockListFragment;
import com.actionbarsherlock.view.MenuItem;

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
    boolean mNew = false;


    public static TemplateEditFragment newInstance() {
        Bundle args = new Bundle();

        TemplateEditFragment fragment = new TemplateEditFragment();
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(getActivity());
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);    //To change body of overridden methods use File | Settings | File Templates.
        mPreferences = getActivity().getSharedPreferences(getString(R.string.shared_pref_name), 0);
        mTemplates = TemplateCollection.get(getActivity()).getTemplates();

        String title = getActivity().getIntent().getStringExtra("templateTitle");
        if(title != null) {
            if (title.equals("")) {
                editingTemplate = new Template("Custom",
                        getActivity().getIntent().getStringExtra("templateText"), false);
            } else {
                editingTemplate = TemplateCollection.get(getActivity()).getTemplate(title);
            }
        } else {
            //new template
            editingTemplate = new Template("New Template", "", false);
            TemplateCollection.get(getActivity()).addTemplate(editingTemplate);
            mNew = true;
        }
        setHasOptionsMenu(true);
        getSherlockActivity().getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        String onFlag = getString(R.string.pref_is_on);

        View v = inflater.inflate(R.layout.fragment_template_edit, container, false);

        // Setup UI Elems
        final EditText editTitle = (EditText) v.findViewById(R.id.editTitle);
        final EditText editText = (EditText) v.findViewById(R.id.editText);
        Button saveButton = (Button) v.findViewById(R.id.saveButton);
        Button deleteButton = (Button) v.findViewById(R.id.template_delete_button);


        editTitle.setText(editingTemplate.getTitle());
        editText.setText(editingTemplate.getText());

        if(editingTemplate.getTitle().equals("Custom")) {
            saveButton.setText("Use");
            saveButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    getActivity().getIntent().putExtra("newText", editText.getText().toString());
                    getActivity().setResult(0, getActivity().getIntent());
                    getActivity().finish();
                }
            });
            deleteButton.setText("Cancel");
            deleteButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    getActivity().setResult(0, getActivity().getIntent());
                    getActivity().finish();
                }
            });
        } else {

            deleteButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    TemplateCollection.get(getActivity()).deleteTemplate(editingTemplate);
                    getActivity().finish();
                }
            });
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
        }

        return v;
    }
}
