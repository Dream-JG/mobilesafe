package com.example.mobilesafe.untils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringWriter;

/**
 * 把流转换成String
 * Created by li on 2017/3/27.
 */

public class StreamUtils {
    public static String parserStream(InputStream inputStream) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(
                new InputStreamReader(inputStream));
        StringWriter stringWriter = new StringWriter();
        String string = null;
        while((string=bufferedReader.readLine())!=null){
            stringWriter.write(string);
        }
        bufferedReader.close();
        return stringWriter.toString();
    }
}
