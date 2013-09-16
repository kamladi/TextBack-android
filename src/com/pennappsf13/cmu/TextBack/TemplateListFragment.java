package com.pennappsf13.cmu.TextBack;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.NavUtils;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.content.Context;
import android.view.ViewGroup;
import android.widget.*;
import com.actionbarsherlock.app.SherlockListFragment;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;

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
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.fragment_list_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_new_template:
                Intent i = new Intent(getActivity(), TemplateEditActivity.class);
                startActivityForResult(i, 2);
                return true;
            case R.id.menu_get_templates:
                mergeServerTemplates();
                return true;
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(getActivity());
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        mTemplates = TemplateCollection.get(getActivity()).getTemplates();
        ((TemplateAdapter) getListAdapter()).notifyDataSetChanged();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);    //To change body of overridden methods use File | Settings | File Templates.
        mPreferences = getActivity().getSharedPreferences(getString(R.string.shared_pref_name), 0);
        mTemplates = TemplateCollection.get(getActivity()).getTemplates();

        TemplateAdapter adapter = new TemplateAdapter(mTemplates);
        setHasOptionsMenu(true);

        getSherlockActivity().getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        setListAdapter(adapter);
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        Template t = (Template)(getListAdapter()).getItem(position);
        if(!t.getSelected()) {
            TemplateCollection.get(getActivity()).getSelectedTemplate().setSelected(false);
            t.setSelected(true);
        }
        Log.i(TAG, "Template t: " + t.getTitle() + " was clicked!" );

        CharSequence toastMsg = "Updating your auto-reply message...";
        Context c = getActivity().getApplicationContext();
        int duration = Toast.LENGTH_SHORT;
        Toast.makeText(c, toastMsg, duration).show();

        getActivity().getIntent().putExtra("selectedTemplate",t.getText());
        getActivity().setResult(0, getActivity().getIntent());
        getActivity().finish();
        ((TemplateAdapter) getListAdapter()).notifyDataSetChanged();
        return;
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
                Intent i = new Intent(getActivity(), TemplateEditActivity.class);
                i.putExtra("templateTitle", t.getTitle());
                startActivityForResult(i, 2);
                ((TemplateAdapter) getListAdapter()).notifyDataSetChanged();
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

    private void mergeServerTemplates() {
        FetchingServerTemplatesTask task = new FetchingServerTemplatesTask();
        int pin = mPreferences.getInt(getString(R.string.pref_pin_code), -1);
        if (pin == -1) {
            PinDialogFragment d = PinDialogFragment.newInstance(pin, "Can't get server templates because you chose not to register.");
            FragmentManager fm = getSherlockActivity().getSupportFragmentManager();
            d.show(fm, "OOPSDIALOG");
        } else {
            task.execute(pin);
        }
    }

    private class FetchingServerTemplatesTask extends AsyncTask<Integer, Void, ArrayList<Template>> {

        @Override
        protected ArrayList<Template> doInBackground(Integer... params) {
            TelephonyManager tmgr = (TelephonyManager) getActivity().getSystemService(Context.TELEPHONY_SERVICE);
            String number = tmgr.getLine1Number();
            String pinCode = params[0].toString();

            ArrayList<Template> server = TemplateCollection.get(getActivity()).readFromServer(number, pinCode);
            ArrayList<Template> client = TemplateCollection.get(getActivity()).readFromDisk();
            if(server != null) {
                for (int i = 0; i < server.size(); i++) {
                    Template s = server.get(i);
                    String title = server.get(i).getTitle();
                    s.setTitle(title+"(Server)");

                }

                client.addAll(server);
            }
            return client;
        }

        @Override
        protected void onPostExecute(ArrayList<Template> templates) {
            TemplateCollection.get(getActivity()).setTemplates(templates);
            mTemplates = templates;
            ((TemplateAdapter) getListAdapter()).notifyDataSetChanged();
            startActivity(new Intent(getActivity(), TemplateListActivity.class));
        }
    }
}
