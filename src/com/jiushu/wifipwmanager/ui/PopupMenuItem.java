package com.jiushu.wifipwmanager.ui;

import android.content.Intent;
import android.graphics.drawable.Drawable;

/**
 * Created by simon on 19/11/15.
 */
public class PopupMenuItem {
    private int itemId;
    private String title;
    private Drawable icon;
    private Intent intent;

    public void setItemId(int itemId) {
        this.itemId = itemId;
    }
    public int getItemId() {
        return itemId;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public String getTitle() {
        return title;
    }
    public void setIcon(Drawable icon) {
        this.icon = icon;
    }
    public Drawable getIcon() {
        return icon;
    }
    public void setIntent(Intent intent) {
        this.intent = intent;
    }
    public Intent getIntent() {
        return intent;
    }
}
