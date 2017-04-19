package com.example.mobilesafe.untils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by li on 2017/4/12.
 */

public class MD5Utils {
    public static String digestPwd(String pwd) {
        String string;
        try {
            MessageDigest digest = MessageDigest.getInstance("MD5");
            byte[] flag = digest.digest(pwd.getBytes());
            StringBuilder stringBuilder = new StringBuilder();
            for (int i = 0; i < flag.length; i++) {
                int result = flag[i] & 0xff;
                String hexString = Integer.toHexString(result);
                if (hexString.length() < 2) {
                    stringBuilder.append("0");
                } else {
                    stringBuilder.append(hexString);
                }
                string = stringBuilder.toString();
                return string;

            }
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
        return null;
    }
}
