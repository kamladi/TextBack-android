package com.pennappsf13.cmu.TextBack;

import java.util.ArrayList;

import android.content.Context;
import android.util.Log;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;

import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.message.BasicNameValuePair;

/**
 * Created with IntelliJ IDEA.
 * User: kamladi
 * Date: 9/7/13
 * Time: 6:45 AM
 * To change this template use File | Settings | File Templates.
 */
public class TemplateCollection {
    private static final String TAG = "TemplateCollection";
    private static final String FILENAME = "templates.json";

    private ArrayList<Template> mTemplates;
    private static TemplateCollection sTemplateCollection;


    private Context mContext;
    private TemplateJSONSerializer mSerializer;

    private TemplateCollection(Context appContext) {
        Log.i(TAG, "Creating a new TemplateCollection");
        mContext = appContext;
        this.fetch();
    }

    public static TemplateCollection get(Context c) {
        if(sTemplateCollection == null) {
            sTemplateCollection = new TemplateCollection(c);
        }
        return sTemplateCollection;

    }

    public void fetch() {
        this.mTemplates = new ArrayList<Template>();
        mTemplates.add(new Template("Meeting", "In a meeting, text you in a bit!", true));
        mTemplates.add(new Template("Shower", "In the shower, I'll text you when I get out", false));
        mTemplates.add(new Template("Lost", "I lost my phone, I'll be sure to reply when I find it", false));
        /*
        mSerializer = new TemplateJSONSerializer(mContext, FILENAME);
        try {
            this.mTemplates = mSerializer.readTemplates();
            Log.i(TAG, "Templates loaded. Found " + mTemplates.size());
        } catch (Exception e) {
            Log.e(TAG, "Cannot load saved templates");
            this.mTemplates = new ArrayList<Template>();
        }     */
    }

    public void addTemplate(Template t) {
        mTemplates.add(t);
        saveTemplates();
    }

    public Template getTemplate(String title) {
        for (Template t: mTemplates) {
            if(t.getTitle().equals(title))
                return t;
        }
        return null;
    }

    public ArrayList<Template> getTemplates() {
        return mTemplates;
    }

    public Template getSelectedTemplate() {
        for (Template t : mTemplates) {
            if (t.getSelected()) {
                return t;
            }
        }
        // if no template is selected, assume first template selected
        if (mTemplates.size() == 0) {
            mTemplates.add(new Template("HERP", "derpaderp", false));
        }
        mTemplates.get(0).setSelected(true);
        return mTemplates.get(0);
    }

    public boolean saveTemplates() {
        try {
            mSerializer.writeTemplates(mTemplates);
            Log.i(TAG, "Templates Written!");
            return true;
        } catch (Exception e) {
            Log.e(TAG, "Exception: " + e + " caught");
            return false;
        }
    }
}
