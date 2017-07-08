package net.macdidi.project111;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import java.net.ProtocolException;
import java.util.Iterator;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private MediaPlayer mediaPlayer01;
    private SharedPreferences b;
    private Context mContext;
    private LocationManager mLocationManager;
    private LocationListenerImpl mLocationListenerImpl;
    //存現在位置
    private String m_longi;
    private String m_latit;
    private String userid;
    private String action;
    private static final int REQUEST_FINE_LOCATION_PERMISSION = 102;
    private String recordurl ="http://140.119.163.40:8080/DarkEmpire/app/ver1.0/storeAction/";
//    private String recordurl ="http://140.119.163.40:8080/DarkEmpire/app/ver1.0/storeAction/{user_id}/{action}/{longitude}/{latitude}/"
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.logo_page);
        AudioManager audioManager = (AudioManager) getSystemService(AUDIO_SERVICE);
        b = getSharedPreferences("DATA",0);
        String have_sound = b.getString("Sound","");
        //記錄api 所需的資訊
        userid = b.getString("ID","");
        action = "1";
        mContext = this;
        initLocationManager(mContext);
            mediaPlayer01 = MediaPlayer.create(MainActivity.this, R.raw.opening);
            mediaPlayer01.start();
            mediaPlayer01.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mediaPlayer) {
                    mediaPlayer01.start();
                }
            });
        if(have_sound.equals("Off")) {
            audioManager.setStreamMute(AudioManager.STREAM_MUSIC,true);
        }
        requestLocationPermission();

    }

    //使用LocationManager去取得目前位置
    private void initLocationManager(Context context) {

        mLocationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);

        //获取可用的位置信息Provider.即passive,network,gps中的一个或几个
        List<String> providerList = mLocationManager.getProviders(true);
        for (Iterator<String> iterator = providerList.iterator(); iterator.hasNext(); ) {
            String provider = (String) iterator.next();
            System.out.println("provider=" + provider);
        }


        //在此采用GPS的方式获取位置信息
        String GPSProvider = LocationManager.GPS_PROVIDER;
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        Location location = mLocationManager.getLastKnownLocation(GPSProvider);
        if (location!=null) {
            double longitude=location.getLongitude();
            double latitude=location.getLatitude();
//            System.out.println("longitude="+longitude+",altitude="+altitude);
            m_longi = ""+longitude;
            m_latit = ""+latitude;
            Log.d("location find ",m_longi + "&& " +m_latit);
            recordurl ="http://140.119.163.40:8080/DarkEmpire/app/ver1.0/storeAction/"+userid +"/"+action+"/"+m_longi+"/"+m_latit+"/";

        } else {
            System.out.println("location==null");
            //如果沒有找到 預設四維堂的位置
            m_latit = ""+24.98635;
            m_longi = ""+121.575744;
            recordurl ="http://140.119.163.40:8080/DarkEmpire/app/ver1.0/storeAction/"+userid +"/"+action+"/"+m_longi+"/"+m_latit+"/";
        }
//        try {
//            Log.d("position test",recordurl);
//            if(userid!=null) {
//                Http_Get.httpget(recordurl);
//            }
//                b.edit().putString("longi", m_longi).apply();
//                b.edit().putString("latit", m_latit).apply();
//
//        } catch (ProtocolException e) {
//            e.printStackTrace();
//        }
        //注册位置监听
        mLocationListenerImpl=new LocationListenerImpl();
        mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 3000, 5, mLocationListenerImpl);
    }


    private class LocationListenerImpl implements LocationListener {
        //当设备位置发生变化时调用该方法
        @Override
        public void onLocationChanged(Location location) {
            //位置更新
            if (location!=null) {
                showLocation(location);
            }
        }

        //当provider的状态发生变化时调用该方法.比如GPS从可用变为不可用.
        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {

        }

        //当provider被打开的瞬间调用该方法.比如用户打开GPS
        @Override
        public void onProviderEnabled(String provider) {

        }

        //当provider被关闭的瞬间调用该方法.比如关闭打开GPS
        @Override
        public void onProviderDisabled(String provider) {

        }

    }


    private void showLocation(Location location) {
        // 获取经度
        double longitude = location.getLongitude();
        // 获取纬度
        double latitude = location.getLatitude();
        m_longi=""+longitude;
        m_latit=""+latitude;
        Log.d("another show",m_longi+" && "+m_latit);
//        String message="经度为:"+longitude+"\n"+"纬度为:"+altitude;

    }
    public void jump(View view) throws ProtocolException {
        //從資料庫拿id 看是不是已登入狀態 選擇進入對應的activity
//        b = getSharedPreferences("DATA",0);
        String a = b.getString("ID","");

        if(a!="") {
            String init_url = "http://140.119.163.40:8080/DarkEmpire/app/ver1.0/init/"+a;
            Http_Get.httpget(init_url);
//            mediaPlayer01.pause();
//            mediaPlayer01.release();
            try {
                Log.d("position test",recordurl);
                if(userid!=null) {
                    Http_Get.httpget(recordurl);
                }
                b.edit().putString("longi", m_longi).apply();
                b.edit().putString("latit", m_latit).apply();

            } catch (ProtocolException e) {
                e.printStackTrace();
            }
            b.edit().putString("Firsttime","false").apply();
            Intent intent = new Intent();
            intent.setClass(this, Menu.class);
            startActivity(intent);

            //this.finish();
        }
        else {
//            mediaPlayer01.pause();
//            mediaPlayer01.release();
            Intent intent = new Intent();
            intent.setClass(this, Login_Activity.class);
            startActivity(intent);
            //this.finish();
        }
    }
    private void requestLocationPermission() {
        Log.d("why", "111");
        // 如果裝置版本是6.0（包含）以上
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            // 取得授權狀態，參數是請求授權的名稱
            int hasPermission = checkSelfPermission(
                    android.Manifest.permission.ACCESS_FINE_LOCATION);

            // 如果未授權
            if (hasPermission != PackageManager.PERMISSION_GRANTED) {
                // 請求授權
                //     第一個參數是請求授權的名稱
                //     第二個參數是請求代碼
                requestPermissions(
                        new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                        REQUEST_FINE_LOCATION_PERMISSION);
                return;
            }
        }


    }


}
