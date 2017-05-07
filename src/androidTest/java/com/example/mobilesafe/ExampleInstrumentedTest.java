package com.example.mobilesafe;

import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import com.example.mobilesafe.Service.GPSService;
import com.example.mobilesafe.db.BlackNumOpenHelper;
import com.example.mobilesafe.db.dao.AddressDao;
import com.example.mobilesafe.db.dao.BlackNumDao;
import com.example.mobilesafe.domain.BlackNumInfo;
import com.example.mobilesafe.domain.ContactsInfo;
import com.example.mobilesafe.engine.ContactsEngine;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static org.junit.Assert.*;
import static org.junit.Assert.assertEquals;

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
    @Test
    public void getGPS(){
        Intent intent = new Intent(InstrumentationRegistry.getTargetContext(), GPSService.class);
        InstrumentationRegistry.getTargetContext().startService(intent);
    }
    @Test
    public void openDb(){
        AddressDao dao = new AddressDao();
        String address=dao.queryAddress(InstrumentationRegistry.getTargetContext(),"13027724058");
        System.out.println(address);
    }
    @Test
    public void creatDb(){
        BlackNumOpenHelper helper =new BlackNumOpenHelper(InstrumentationRegistry.getContext());
    }

    @Test
    public void addNum(){
        Context appContext = InstrumentationRegistry.getTargetContext();
        BlackNumDao dao = new BlackNumDao(appContext);
        for(int i =1;i<=200;i++){
            Random random = new Random();
            dao.addBlackNum("130277200"+i,random.nextInt(3));
        }

    }
    @Test
    public void updateNum(){
        Context appContext = InstrumentationRegistry.getTargetContext();
        BlackNumDao dao = new BlackNumDao(appContext);
        dao.updateBlackNum("13027724058",2);
    }
    @Test
    public void queryMode(){
        Context appContext = InstrumentationRegistry.getTargetContext();
        BlackNumDao dao = new BlackNumDao(appContext);
        assertEquals(2,dao.queryBlackNumMode("13027724058"));
    }
    @Test
    public void deleteNum(){
        Context appContext = InstrumentationRegistry.getTargetContext();
        BlackNumDao dao = new BlackNumDao(appContext);
        dao.deleteBlackNum("13027724058");
    }
    @Test
    public void selelctAllNum(){
        Context appContext = InstrumentationRegistry.getTargetContext();
        BlackNumDao dao = new BlackNumDao(appContext);
        List<BlackNumInfo> infos = dao.getAllBlackNum();
        for (BlackNumInfo info : infos){
            System.out.println(info.toString());
        }
    }

}
