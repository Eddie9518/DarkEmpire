package net.macdidi.project111;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.MediaPlayer;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;

import java.net.ProtocolException;
import java.util.Iterator;
import java.util.List;

/**
 * Created by Eddie84 on 2016/10/27.
 */
public class Rune_Activity extends AppCompatActivity {
    private SharedPreferences b;
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
    //存現在位置
    private String m_longi;
    private String m_latit;
    //存id
    private String a;
    private Bitmap antaya_money;
    private Bitmap sinar_money;
    //紀錄api需要的變數
    private String recordurl ="http://140.119.163.40:8080/DarkEmpire/app/ver1.0/storeAction/";
    private String action_throw="4";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.money_page);
        b = getSharedPreferences("DATA",0);
        a = b.getString("ID","");
        try {
            get1 = Http_Get.httpget(myurl+a);
        } catch (ProtocolException e) {
            e.printStackTrace();
        }
        json3(get1);
        Rune1 = (TextView) findViewById(R.id.rune1);
//        Rune2 = (TextView) findViewById(R.id.rune2);
        Rune1.setText("" + rune1_count+" 個");
//        Rune2.setText("" + rune2_count+" 個");
        mContext = this;
        initLocationManager(mContext);
        ImageView antaya_money_image = (ImageView)findViewById(R.id.m_antaya);
//        ImageView sinar_money_image = (ImageView)findViewById(R.id.m_sinar);
        int resID1 = getResources().getIdentifier("money_antaya", "drawable", Rune_Activity.this.getPackageName());
        BitmapFactory.Options option_a = new BitmapFactory.Options();
        option_a.inSampleSize = 2;
//        int resID2 = getResources().getIdentifier("money_sinar","drawable",Rune_Activity.this.getPackageName());
//        BitmapFactory.Options option_s = new BitmapFactory.Options();
//        option_s.inSampleSize = 2;
        antaya_money = BitmapFactory.decodeResource(getResources(), resID1, option_a);
//        sinar_money = BitmapFactory.decodeResource(getResources(), resID2, option_s);
        antaya_money_image.setImageBitmap(antaya_money);
//        sinar_money_image.setImageBitmap(sinar_money);

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
        } else {
            System.out.println("location==null");
            //如果沒有找到 預設四維堂的位置
            m_latit = ""+24.98635;
            m_longi = ""+121.575744;
        }

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

    private boolean haveInternet()
    {
        boolean result = false;
        ConnectivityManager connManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info=connManager.getActiveNetworkInfo();
        if (info == null || !info.isConnected())
        {
            result = false;
        }
        else
        {
            if (!info.isAvailable())
            {
                result =false;
            }
            else
            {
                result = true;
            }
        }

        return result;
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
    public void json3(String result) {
        try {
            JSONArray object_arr = new JSONArray(result);
            rune1_count= object_arr.getJSONObject(0).getInt("stone");
//            rune2_count = object_arr.getJSONObject(1).getInt("stone");

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
//        startActivityForResult(intentMap, START_LOCATION);
        startActivity(intentMap);
        Rune_Activity.this.finish();
    }

    public void moneyclick(View view) {
        int id = view.getId();
        switch (id) {
            case R.id.money_map:
                MediaPlayer mediaPlayer01;
                mediaPlayer01 = MediaPlayer.create(this, R.raw.m007);
                mediaPlayer01.start();
                requestLocationPermission();
                break;
            case R.id.rune1:
                String show1 = "您現在有" + rune1_count + "硬幣";
                alert_make(show1,1);
                break;
//            case R.id.rune2:
//                String show2 = "您現在有" + rune2_count + "硬幣";
//                alert_make(show2,2);
//                break;
            case R.id.money_buy:
                Intent intent = new Intent();
                intent.setClass(this,Buypotion_Activity.class);
                startActivity(intent);
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
                if(haveInternet()) {
                    MediaPlayer mediaPlayer01;
                    mediaPlayer01 = MediaPlayer.create(getApplicationContext(), R.raw.m008);
                    mediaPlayer01.start();
                    String amount = edited_money.getText().toString();
                    if (!amount.equals("")) {
                        if (input2 == 1) {
                            if (rune1_count - Integer.valueOf(amount) >= 0) {
                                String postString = "http://140.119.163.40:8080/DarkEmpire/app/ver1.0/userRune/throw_rune/" + a + "/1/" + amount + "/" + m_longi + "/" + m_latit + "/";
                                Log.d("showstring", postString);
                                try {
                                    Http_Post.httppost(postString);
                                    String record_throw = recordurl + a + "/" + action_throw + "/" + m_longi + "/" + m_latit + "/";
                                    Http_Get.httpget(record_throw);
                                    b.edit().putString("longi", m_longi).apply();
                                    b.edit().putString("latit", m_latit).apply();
                                    Log.d("throw_url", record_throw);
                                } catch (ProtocolException e) {
                                    e.printStackTrace();
                                }
                                rune1_count = rune1_count - Integer.valueOf(amount);
                                Rune1.setText("" + rune1_count + " 個");
                                dialog.cancel();
                            } else {
                                rune_not_enough(dialog);

                            }

                        }
//                        else if (input2 == 2) {
//                            if (rune2_count - Integer.valueOf(amount) >= 0) {
//                                String postString = "http://140.119.163.40:8080/DarkEmpire/app/ver1.0/userRune/throw_rune/" + a + "/2/" + amount + "/" + m_longi + "/" + m_latit + "/";
//                                Log.d("showstring", postString);
//                                try {
//                                    Http_Post.httppost(postString);
//                                    String record_throw = recordurl + a + "/" + action_throw + "/" + m_longi + "/" + m_latit + "/";
//                                    Http_Get.httpget(record_throw);
//                                    b.edit().putString("longi", m_longi).apply();
//                                    b.edit().putString("latit", m_latit).apply();
//                                    Log.d("throw_url", record_throw);
//                                } catch (ProtocolException e) {
//                                    e.printStackTrace();
//                                }
//                                rune2_count = rune2_count - Integer.valueOf(amount);
//                                Rune2.setText("" + rune2_count + " 個");
//                                dialog.cancel();
//
//                            }
                            else {
                                rune_not_enough(dialog);
                            }
//                        }
                    } else {
                        Toast.makeText(Rune_Activity.this, "您沒有輸入訊息", Toast.LENGTH_SHORT).show();
                        dialog.cancel();
                    }
                }
                else{
                    Toast.makeText(Rune_Activity.this,"請確認網路狀態 再重啟頁面",Toast.LENGTH_SHORT).show();
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
    private void rune_not_enough(final Dialog dialog){
        new AlertDialog.Builder(Rune_Activity.this)
                .setTitle("錯誤")
                .setMessage("您的金幣不足")
                .setPositiveButton("我知道了", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialog.cancel();
                    }
                }).create().show();
    }
    //左上角按了返回後結束activity
    public boolean onOptionsItemSelected(MenuItem item)
    {
        // TODO Auto-generated method stub
        if(item.getItemId() == android.R.id.home)
        {
//            antaya_money.recycle();
//            sinar_money.recycle();
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    //左下角按了返回後結束activity
    public boolean onKeyDown(int keyCode, KeyEvent event)
    {
        if (keyCode == KeyEvent.KEYCODE_BACK )
        {
            finish();
        }

        return false;

    }
}