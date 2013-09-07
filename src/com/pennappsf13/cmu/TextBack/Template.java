package com.pennappsf13.cmu.TextBack;

/**
 * Created with IntelliJ IDEA.
 * User: kamladi
 * Date: 9/7/13
 * Time: 4:36 AM
 * To change this template use File | Settings | File Templates.
 */
public class Template {
    private String title;
    private String text;
    private Boolean isSelected;

    public void Template(String title, String text, Boolean isSelected) {
        this.setTitle(title);
        this.setText(text);
        this.setSelected(isSelected);
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Boolean isSelected() {
        return isSelected;
    }

    public void setSelected(Boolean selected) {
        isSelected = selected;
    }
}
