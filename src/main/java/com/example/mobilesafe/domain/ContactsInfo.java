package com.example.mobilesafe.domain;

/**
 * Created by li on 2017/4/23.
 */

public class ContactsInfo {
    private String name;
    private String num;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNum() {
        return num;
    }

    public void setNum(String num) {
        this.num = num;
    }

    public ContactsInfo() {
        super();
    }

    public ContactsInfo(String name, String num) {
        this.name = name;
        this.num = num;
    }

    @Override
    public String toString() {
        return "ContactsInfo{" +
                "name='" + name + '\'' +
                ", num='" + num + '\'' +
                '}';
    }
}
