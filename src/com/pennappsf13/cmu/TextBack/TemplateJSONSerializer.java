package com.pennappsf13.cmu.TextBack;

import android.content.Context;
import android.util.Log;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONTokener;

import java.io.*;
import java.util.ArrayList;

/**
 * Created with IntelliJ IDEA.
 * User: dsyang
 * Date: 9/7/13
 * Time: 10:02 AM
 * To change this template use File | Settings | File Templates.
 */
public class TemplateJSONSerializer {
    private final String TAG = this.getClass().getSimpleName();

    private Context mContext;
    private String mFilename;

    public TemplateJSONSerializer(Context c, String f) {
        mContext = c;
        mFilename = f;
    }

    public void writeTemplates(ArrayList<Template> templates)
            throws JSONException, IOException {
        JSONArray array = new JSONArray();

        for (Template t : templates) {
            array.put(t.toJSON());
        }

        Writer writer = null;
        try {
            OutputStream out = mContext.openFileOutput(mFilename, Context.MODE_PRIVATE);
            writer = new OutputStreamWriter(out);
            writer.write(array.toString());
            Log.i(TAG, "Written: " + array.toString());
        } finally {
            if (writer != null) {
                writer.close();
            }
        }
    }

    public ArrayList<Template> readTemplates()
            throws JSONException, IOException {
        ArrayList<Template> templates = new ArrayList<Template>();
        BufferedReader reader = null;

        try {
            InputStream in = mContext.openFileInput(mFilename);
            reader = new BufferedReader(new InputStreamReader(in));
            StringBuilder jsonString = new StringBuilder();
            String line = null;
            while ((line = reader.readLine()) != null) {
                jsonString.append(line);
            }

            JSONArray array = (JSONArray) new JSONTokener(jsonString.toString()).nextValue();
            for (int i = 0; i < array.length(); i++) {
                templates.add(new Template(array.getJSONObject(i)));
            }
        } catch (FileNotFoundException e) {
            Log.d(TAG, "File not Found : " + mFilename);
            //ignore; only happens when starting fresh
        } finally {
            if (reader != null) {
                reader.close();
            }

        }
        return templates;
    }
}
