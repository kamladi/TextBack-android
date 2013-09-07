package com.pennappsf13.cmu.TextBack;

import java.util.ArrayList;
import java.util.List;

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
    public List<Template> templates;
    public TemplateCollection() {
        this.templates = new ArrayList<Template>(3);
    }

    public void fetch() {
        /*
        TODO: http shit
         */
        this.addTemplate(new Template("Meeting", "In a meeting, text you in a bit!", true));
        this.addTemplate(new Template("Shower", "In the shower, I'll text you when I get out", false));
        this.addTemplate(new Template("Lost", "I lost my phone, I'll be sure to reply when I find it", false));
    }

    public void addTemplate(Template t) {
        this.templates.add(t);
    }

    public Template getSelectedTemplate() {
        for (Template t : this.templates) {
            if (t.isSelected()) {
                return t;
            }
        }
        // if no template is selected, assume first template selected
        this.templates.get(0).setSelected(true);
        return this.templates.get(0);
    }
}
