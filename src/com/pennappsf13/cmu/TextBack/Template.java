package com.pennappsf13.cmu.TextBack;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created with IntelliJ IDEA.
 * User: kamladi
 * Date: 9/7/13
 * Time: 4:36 AM
 * To change this template use File | Settings | File Templates.
 */
public class Template {
    private String mTitle;
    private String mText;
    private Boolean mIsSelected;

    private static final String JSON_TITLE = "title";
    private static final String JSON_TEXT = "text";
    private static final String JSON_IS_SELECTED = "isSelected";


    public Template(String title, String text, Boolean isSelected) {
        this.setTitle(title);
        this.setText(text);
        this.setSelected(isSelected);
    }

    public Template(JSONObject obj) throws JSONException {
        mTitle = obj.getString(JSON_TITLE);
        mText = obj.getString(JSON_TEXT);
        mIsSelected = obj.getBoolean(JSON_IS_SELECTED);

    }

    public JSONObject toJSON() throws JSONException {
        JSONObject json = new JSONObject();
        json.put(JSON_TITLE, mTitle);
        json.put(JSON_TEXT, mText);
        json.put(JSON_IS_SELECTED, mIsSelected);

        return json;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        mTitle = title;
    }

    public String getText() {
        return mText;
    }

    public void setText(String text) {
        mText = text;
    }

    public Boolean getSelected() {
        return mIsSelected;
    }

    public void setSelected(Boolean isSelected) {
        mIsSelected = isSelected;
    }
}
