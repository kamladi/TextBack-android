package com.pennappsf13.cmu.TextBack;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.util.ArrayList;

import android.content.Context;
import android.util.Log;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;

import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONTokener;

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
    private static final String GETALL_ENDPOINT = "http://textbackweb.appspot.com/templates/android"; //post
    private static final String NEW_ENDPOINT = "http://textbackweb.appspot.com/templates/new"; //post
    private static final String EDIT_ENDPOINT = "http://textbackweb.appspot.com/templates/"; //post

    private ArrayList<Template> mTemplates;
    private static TemplateCollection sTemplateCollection;


    private Context mContext;
    private TemplateJSONSerializer mSerializer;

    private TemplateCollection(Context appContext) {
        mContext = appContext;
        mSerializer = new TemplateJSONSerializer(mContext, FILENAME);
        this.fetch();
    }

    public static TemplateCollection get(Context c) {
        if(sTemplateCollection == null) {
            sTemplateCollection = new TemplateCollection(c);
        }
        return sTemplateCollection;

    }

    public ArrayList<Template> readFromDisk() {
        ArrayList<Template> templates = null;
        try {
            templates = mSerializer.readTemplates();
            Log.i(TAG, "Templates loaded. Found " + templates.size());
        } catch (Exception e) {
            Log.e(TAG, "Cannot load saved templates");
        }
        return templates;
    }

    public ArrayList<Template> readFromServer(String number, String pinCode) {
        HttpClient client = new DefaultHttpClient();
        HttpPost post = new HttpPost(URI.create(GETALL_ENDPOINT));
        ArrayList<Template> serverTemplates = new ArrayList<Template>();
        try {
            ArrayList<NameValuePair> data = new ArrayList<NameValuePair>(2);
            data.add(new BasicNameValuePair("phone", number));
            data.add(new BasicNameValuePair("password",pinCode));
            post.setEntity(new UrlEncodedFormEntity(data));
            HttpResponse resp = client.execute(post);
            if (resp.getStatusLine().getStatusCode() == 200) {
                ResponseHandler<String> handler = new BasicResponseHandler();
                String body = handler.handleResponse(resp);
                try {
                    JSONArray array = (JSONArray) new JSONTokener(body).nextValue();
                    for (int i = 0; i < array.length(); i++) {
                        serverTemplates.add(new Template(array.getJSONObject(i)));
                    }
                    return serverTemplates;
                } catch (JSONException e) {
                    e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                }
            } else {
                return null;
            }
        } catch (ClientProtocolException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        return null;

    }

    public void fetch() {
        this.mTemplates = readFromDisk();
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

    public void deleteTemplate(Template t) {
        int idx = 0;
        for (int i = 0; i < mTemplates.size(); i++) {
            Template tx = mTemplates.get(i);
            if(t.getTitle().equals(tx.getTitle()) &&
                    t.getText().equals(tx.getText())) {
                idx = i;
                break;
            }
        }
        mTemplates.remove(idx);
    }
    public ArrayList<Template> getTemplates() {
        return mTemplates;
    }

    public void setTemplates(ArrayList<Template> templates) {
        mTemplates = templates;
        saveTemplates();
    }
    public Template getSelectedTemplate() {
        for (Template t : mTemplates) {
            if (t.getSelected()) {
                return t;
            }
        }
        // if no template is selected, assume first template selected
        if (mTemplates.size() == 0) {
            mTemplates.add(new Template("Go Away", "Sudo leave me alone", false));
            mTemplates.add(new Template("Sleep", "I'm Sleeping, zzzzz. Text you when I wake", false));
            mTemplates.add(new Template("Reddit-ing", "Busy redditing. I'll get back to you later", false));
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
