package com.example.mobilesafe.domain;

/**
 * Created by li on 2017/5/6.
 */

public class BlackNumInfo {
    String blackNum;
    int mode;

    public BlackNumInfo(String blackNum, int mode) {
        this.blackNum = blackNum;
        this.mode = mode;
    }

    public BlackNumInfo() {
    }

    @Override
    public String toString() {
        return "BlackNumInfo{" +
                "blackNum='" + blackNum + '\'' +
                ", mode=" + mode +
                '}';
    }

    public String getBlackNum() {
        return blackNum;
    }

    public void setBlackNum(String blackNum) {
        this.blackNum = blackNum;
    }

    public int getMode() {
        return mode;
    }

    public void setMode(int mode) {
        this.mode = mode;
    }
}
