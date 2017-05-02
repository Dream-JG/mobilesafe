package com.example.mobilesafe;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import com.example.mobilesafe.domain.ContactsInfo;
import com.example.mobilesafe.engine.ContactsEngine;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Instrumentation test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class ExampleInstrumentedTest {
    @Test
    public void useAppContext() throws Exception {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getTargetContext();

        assertEquals("com.example.mobilesafe", appContext.getPackageName());
    }
    @Test
    public void getContacts(){
        ContactsEngine contactsEngine = new ContactsEngine();
        List<ContactsInfo> contactsInfoList = new ArrayList<ContactsInfo>();
        contactsInfoList =contactsEngine.getAllContactsInfos(InstrumentationRegistry.getTargetContext());
        for(ContactsInfo c: contactsInfoList){
            System.out.println(c.toString());
        }
    }
}
