package com.example.mobilesafe.domain;

import android.graphics.drawable.Drawable;

/**
 * Created by li on 2017/5/7.
 */

public class AppInfo {
    private String name;
    private String versionName;
    private Drawable icon;
    private String packageName;
    private boolean isSd;
    private boolean isUser;

    public AppInfo() {
        super();
    }

    public AppInfo(String name, String versionName, Drawable icon, String packageName, boolean isSd, boolean isUser) {
        this.name = name;
        this.versionName = versionName;
        this.icon = icon;
        this.packageName = packageName;
        this.isSd = isSd;
        this.isUser = isUser;
    }

    @Override
    public String toString() {
        return "AppInfo{" +
                "name='" + name + '\'' +
                ", versionName='" + versionName + '\'' +
                ", icon=" + icon +
                ", packageName='" + packageName + '\'' +
                ", isSd=" + isSd +
                ", isUser=" + isUser +
                '}';
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getVersionName() {
        return versionName;
    }

    public void setVersionName(String versionName) {
        this.versionName = versionName;
    }

    public Drawable getIcon() {
        return icon;
    }

    public void setIcon(Drawable icon) {
        this.icon = icon;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public boolean isSd() {
        return isSd;
    }

    public void setSd(boolean sd) {
        isSd = sd;
    }

    public boolean isUser() {
        return isUser;
    }

    public void setUser(boolean user) {
        isUser = user;
    }
}

