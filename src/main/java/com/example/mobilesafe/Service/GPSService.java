package com.example.mobilesafe.Service;

import android.Manifest;
import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;

/**
 * Created by li on 2017/5/2.
 */

public class GPSService extends Service {
    private LocationManager locationManager;
    private MyLocationListener myLocationListener;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        //定位配置
        Criteria criteria = new Criteria();
        myLocationListener = new MyLocationListener();
        criteria.setAltitudeRequired(true);
        String bestProvider = locationManager.getBestProvider(criteria, true);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        locationManager.requestLocationUpdates(bestProvider, 0, 0, myLocationListener);



    }

    private class MyLocationListener implements LocationListener {
        //当位置发生变化时调用 location 具体的位置
        @Override
        public void onLocationChanged(Location location) {
            double latitude = location.getLatitude(); //平行于赤道 纬度
            double longitude = location.getLongitude();//垂直于赤道 经度

            SharedPreferences sharedPreferences = getSharedPreferences("config",MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("latitude",latitude+"");
            editor.putString("longitude",longitude+"");
            System.out.println(latitude+"----"+longitude);
            editor.commit();


        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {

        }

        @Override
        public void onProviderEnabled(String provider) {

        }

        @Override
        public void onProviderDisabled(String provider) {

        }
    }
}
