package com.example.mobilesafe.domain;

import android.graphics.drawable.Drawable;

/**
 * Created by li on 2017/5/10.
 */

public class TaskInfo {
    private String packageName;
    private String name;
    private Drawable icon;
    private boolean isUser;
    private long memory;
    private boolean checked;

    public TaskInfo() {
        super();
    }


    public TaskInfo(String packageName, String name, Drawable icon, boolean isUser, long memory) {
        this.packageName = packageName;
        this.name = name;
        this.icon = icon;
        this.isUser = isUser;
        this.memory = memory;
    }

    @Override
    public String toString() {
        return "TaskInfo{" +
                "packageName='" + packageName + '\'' +
                ", name='" + name + '\'' +
                ", icon=" + icon +
                ", isUser=" + isUser +
                ", memory=" + memory +
                '}';
    }

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Drawable getIcon() {
        return icon;
    }

    public void setIcon(Drawable icon) {
        this.icon = icon;
    }

    public boolean isUser() {
        return isUser;
    }

    public void setUser(boolean user) {
        isUser = user;
    }

    public long getMemory() {
        return memory;
    }

    public void setMemory(long memory) {
        this.memory = memory;
    }
}
