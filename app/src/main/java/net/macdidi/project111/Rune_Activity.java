package net.macdidi.project111;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;

import java.net.ProtocolException;
import java.util.Iterator;
import java.util.List;

/**
 * Created by Eddie84 on 2016/10/27.
 */
public class Rune_Activity extends AppCompatActivity {
    private Context mContext;
    private LocationManager mLocationManager;
    private LocationListenerImpl mLocationListenerImpl;
    private TextView Rune1;
    private TextView Rune2;
    private static final int START_LOCATION = 2;
    private static final int REQUEST_FINE_LOCATION_PERMISSION = 102;
    private int rune1_count;
    private int rune2_count;
    private String myurl = "http://140.119.163.40:8080/DarkEmpire/app/ver1.0/userRune/";
    private String get1;
    private String show2 = "您現在有" + rune2_count + "硬幣";
    private String m_longi;
    private String m_latit;
    private TextView show_view;
    private String a;
    private int [] rune_count;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.money_page);
        SharedPreferences b = getSharedPreferences("DATA",0);
        a = b.getString("ID","");
        try {
            get1 = Http_Get.httpget(myurl+a);
        } catch (ProtocolException e) {
            e.printStackTrace();
        }
        json3(get1);
        Rune1 = (TextView) findViewById(R.id.rune1);
        Rune2 = (TextView) findViewById(R.id.rune2);
        Rune1.setText("" + rune1_count);
        Rune2.setText("" + rune2_count);
        mContext = this;
        initLocationManager(mContext);
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
        } else {
            System.out.println("location==null");
        }

        //注册位置监听
        mLocationListenerImpl=new LocationListenerImpl();
        mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 3000, 5, mLocationListenerImpl);
    }


    private class LocationListenerImpl implements LocationListener {
        //当设备位置发生变化时调用该方法
        @Override
        public void onLocationChanged(Location location) {
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
//        String message="经度为:"+longitude+"\n"+"纬度为:"+altitude;

    }
    public void json3(String result) {
        try {
            JSONArray object_arr = new JSONArray(result);
//            JSONObject object2 = new JSONObject(result);
//            for(int i =0 ; i< object_arr.length();i++) {
                rune1_count= object_arr.getJSONObject(0).getInt("stone");
//            Log.d("test", "" + rune1_count);
//                StudentID = object2.getString("studentid");
                rune2_count = object_arr.getJSONObject(1).getInt("stone");
//            Log.d("test", "" + rune2_count);
//                Level = object2.getString("level");
//                Exp = object2.getString("exp");
//                Votes = object2.getString("votes");

    } catch (JSONException e) {
            Log.d("Not found","QQ");
            e.printStackTrace();
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

        // 如果裝置版本是6.0以下，
        // 或是裝置版本是6.0（包含）以上，使用者已經授權，
        // 啟動地圖與定位元件
        mapFunction();

    }

    public void mapFunction() {
        // 啟動地圖元件用的Intent物件
        Intent intentMap = new Intent(Rune_Activity.this, MapsActivity.class);
        // 啟動地圖元件
        Log.d("why", "222");
        startActivityForResult(intentMap, START_LOCATION);
        Rune_Activity.this.finish();
//        MainActivity.this.finish();
    }

    public void moneyclick(View view) {
        int id = view.getId();
//        TextView edited_view = (TextView)findViewById(R.id.show_money);
        switch (id) {
            case R.id.money_map:
                MediaPlayer mediaPlayer01;
                mediaPlayer01 = MediaPlayer.create(this, R.raw.m007);
                mediaPlayer01.start();
                requestLocationPermission();
                break;
            case R.id.rune1:
//                LayoutInflater inflater = LayoutInflater.from(Rune_Activity.this);
//                View alert_view = inflater.inflate(R.layout.money_edit,null);
//                TextView edited_view = (TextView)alert_view.findViewById(R.id.show_money);
//                final AlertDialog.Builder editDialog = new AlertDialog.Builder(Rune_Activity.this);
//                TextView edited_view = (TextView)findViewById(R.id.show_money);
                String show1 = "您現在有" + rune1_count + "硬幣";
//                Log.d("test",show1);
//                edited_view.setText(show1);
//                editDialog.setTitle("丟棄金幣");
//                editDialog.setView(alert_view);
////                EditView.setContentView(R.layout.user_edit);
//
//                final EditText edited_money=(EditText)alert_view.findViewById(R.id.money_throw);
//                Button editOK =(Button)alert_view.findViewById(R.id.m_btnOK);
//                Button editCancel = (Button)alert_view.findViewById(R.id.m_btnCancel);
//                final AlertDialog dialog = editDialog.create();
//                dialog.show();
//                editOK.setOnClickListener(new View.OnClickListener(){
//
//
//                    @Override
//                    public void onClick(View view) {
//                        String amount = edited_money.getText().toString();
//                        String postString="http://140.119.163.40:8080/DarkEmpire/app/ver1.0/userRune/throw_rune/"+a+"/1/"+amount+"/"+m_longi+"/"+m_latit+"/";
//                        try {
//                            Http_Post.httppost(postString);
//                        } catch (ProtocolException e) {
//                            e.printStackTrace();
//                        }
//                        rune1_count=rune1_count-Integer.valueOf(amount);
//                        Rune1.setText(""+rune1_count);
//                        dialog.cancel();
//
//                    }
//                });
//                editCancel.setOnClickListener(new View.OnClickListener(){
//
//
//                    @Override
//                    public void onClick(View view) {
//                        dialog.cancel();
//                    }
//                });
                alert_make(show1,1);
                break;
            case R.id.rune2:
//                LayoutInflater inflater2 = LayoutInflater.from(Rune_Activity.this);
//                View alert_view2 = inflater2.inflate(R.layout.money_edit,null);
//
//                final AlertDialog.Builder editDialog2 = new AlertDialog.Builder(Rune_Activity.this);
//                TextView edited_view2 = (TextView)findViewById(R.id.show_money);
//                edited_view.setText(show2);
                String show2 = "您現在有" + rune2_count + "硬幣";
//
                alert_make(show2,2);
                break;
        }
    }
    public void alert_make(String input,final int input2){
        LayoutInflater inflater = LayoutInflater.from(Rune_Activity.this);
        View alert_view = inflater.inflate(R.layout.money_edit,null);
        TextView edited_view = (TextView)alert_view.findViewById(R.id.show_money);
        final AlertDialog.Builder editDialog = new AlertDialog.Builder(Rune_Activity.this);
//                TextView edited_view = (TextView)findViewById(R.id.show_money);
        Log.d("test",input);
        edited_view.setText(input);
        editDialog.setTitle("丟棄金幣");
        editDialog.setView(alert_view);
//                EditView.setContentView(R.layout.user_edit);

        final EditText edited_money=(EditText)alert_view.findViewById(R.id.money_throw);
        Button editOK =(Button)alert_view.findViewById(R.id.m_btnOK);
        Button editCancel = (Button)alert_view.findViewById(R.id.m_btnCancel);
        final AlertDialog dialog = editDialog.create();
        dialog.show();
        editOK.setOnClickListener(new View.OnClickListener(){


            @Override
            public void onClick(View view) {
                MediaPlayer mediaPlayer01;
                mediaPlayer01 = MediaPlayer.create(getApplicationContext(), R.raw.m008);
                mediaPlayer01.start();
                if (input2 == 1) {
                    String amount = edited_money.getText().toString();
                    String postString = "http://140.119.163.40:8080/DarkEmpire/app/ver1.0/userRune/throw_rune/" + a + "/1/" + amount + "/" + m_longi + "/" + m_latit + "/";
                    try {
                        Http_Post.httppost(postString);
                    } catch (ProtocolException e) {
                        e.printStackTrace();
                    }
                    rune1_count = rune1_count - Integer.valueOf(amount);
                    Rune1.setText("" + rune1_count);
                    dialog.cancel();

                }
                else if(input2 == 2){
                    String amount = edited_money.getText().toString();
                    String postString="http://140.119.163.40:8080/DarkEmpire/app/ver1.0/userRune/throw_rune/"+a+"/2/"+amount+"/"+m_longi+"/"+m_latit+"/";
                    try {
                        Http_Post.httppost(postString);
                    } catch (ProtocolException e) {
                        e.printStackTrace();
                    }
                    rune2_count=rune2_count-Integer.valueOf(amount);
                    Rune2.setText(""+rune2_count);
                    dialog.cancel();


                }
            }
        });
        editCancel.setOnClickListener(new View.OnClickListener(){


            @Override
            public void onClick(View view) {
                dialog.cancel();
            }
        });


    }
}