package net.macdidi.project111;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import java.net.ProtocolException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by Eddie84 on 2016/12/28.
 */
public class Dedicate_Activity extends AppCompatActivity {
    private String [] pname;
    private Bitmap [] bitmap;
    private String url ="http://140.119.163.40:8080/DarkEmpire/app/ver1.0/forceColor";
    private String ddcurl="http://140.119.163.40:8080/DarkEmpire/app/ver1.0/releaseForce/";
    private String user_id;
    private char [] colororder;
    private Boolean same;
    private ImageView img1;
    private ImageView img2;
    private ImageView img3;
    private ImageView img4;
    private ImageView img5;
    private ImageView img6;
    private ImageView img7;
    private ImageView img8;
    private ImageView imgframe;
    //紀錄api需要的變數
    private String recordurl ="http://140.119.163.40:8080/DarkEmpire/app/ver1.0/storeAction/";
    private double EARTH_RADIUS = 6378.137;
    //資料庫
    private SharedPreferences datab;
    private double pp_longi;
    private double pp_latit;
    //現在位置
    private Context mContext;
    private LocationManager mLocationManager;
    private LocationListenerImpl mLocationListenerImpl;
    private String now_longi;
    private String now_latit;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dedicate);
        SharedPreferences b = getSharedPreferences("DATA", 0);
        //呼叫資料庫
        datab = getSharedPreferences("DATA",0);
        pp_longi = Double.valueOf(datab.getString("p_longi",""));
        pp_latit = Double.valueOf(datab.getString("p_latit",""));

        //找現在位置
        mContext = this;
        initLocationManager(mContext);

        user_id = b.getString("ID", "");
        //紀錄API所需資料
        String recent_longi = b.getString("longi","");
        String recent_latit = b.getString("latit","");
        recordurl = "http://140.119.163.40:8080/DarkEmpire/app/ver1.0/storeAction/"+user_id+"/7/"+recent_longi+"/"+recent_latit+"/";
        imgframe = (ImageView) findViewById(R.id.show_frame);
        img1 = (ImageView) findViewById(R.id.show_img1);
        img2 = (ImageView) findViewById(R.id.show_img2);
        img3 = (ImageView) findViewById(R.id.show_img3);
        img4 = (ImageView) findViewById(R.id.show_img4);
        img5 = (ImageView) findViewById(R.id.show_img5);
        img6 = (ImageView) findViewById(R.id.show_img6);
        img7 = (ImageView) findViewById(R.id.show_img7);
        img8 = (ImageView) findViewById(R.id.show_img8);
        processcontrol();
//        ArrayList<Integer> dedicate_number_list = new ArrayList<Integer>();
        final Spinner dedicate_which = (Spinner)findViewById(R.id.dedicate_spinner);
        final ArrayList<Integer> dedicate_number_list = new ArrayList<>();
        final String [] number ={"請選擇","1","2","3","4","5","6","7","8"};
        for (int i = 1; i < 9; i++){
            dedicate_number_list.add(i);
        }
        ArrayAdapter<String> ddcAdapter = new ArrayAdapter<String>(this,R.layout.myspinnerstyle,number);
        ddcAdapter.setDropDownViewResource(R.layout.myspinnerstyle);
        dedicate_which.setAdapter(ddcAdapter);
        dedicate_which.setSelection(0);
        dedicate_which.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if(i==0){
                    Log.d("genius",number[i]);
                }
                else {
                    Log.d("show", "" + number[i]);
                    number_alert_make(number[i]);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }
    public void number_alert_make(String number){
        final String newurl = ddcurl + user_id +"/"+ number ;
        new AlertDialog.Builder(Dedicate_Activity.this)
                .setTitle("選擇奉獻的號碼")
                .setMessage("確定選擇"+number+"號 ? ")
                .setPositiveButton("確定",new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,
                                        int whichButton) {
                        if ((int)
                                (countdistance(pp_longi, pp_latit, Double.valueOf(now_longi), Double.valueOf(now_latit))) <= 100) {
                            try {
                                String result = Http_Get.httpget(newurl);
                                result = result.replace("\n", "").replace(" ", "");
                                Log.d("123", "" + result);
                                Log.d("222", "" + result.length());
                                if (result.equals("success")) {
                                    Log.d("inside", "nono");
                                    Toast.makeText(Dedicate_Activity.this, "奉獻成功", Toast.LENGTH_LONG).show();
                                    Http_Get.httpget(recordurl);
                                    Log.d("dedicateurl", recordurl);
                                    processcontrol();
                                } else {
                                    Toast.makeText(Dedicate_Activity.this, "您不是超原力使者", Toast.LENGTH_LONG).show();
                                }

                            } catch (ProtocolException e) {
                                e.printStackTrace();
                            }
                        }
                    else
                    {
                        Toast.makeText(Dedicate_Activity.this, "您已經離開神殿的範圍 請重新刷新位置", Toast.LENGTH_SHORT).show();
                    }
                }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                })
                .create().show();
    }
    public void processcontrol(){
        try {
            String a = Http_Get.httpget(url);
            colororder = new char[a.length()];
            for (int i = 0; i < a.length(); i++) {
                colororder[i] = a.charAt(i);

            }
        } catch (ProtocolException e) {
            e.printStackTrace();
        }
        pname = new String[8];
        String k = null;
        bitmap = new Bitmap[8];
        for (int j = 0; j < 8; j++) {
            char r = colororder[j];
            switch (r) {
                case 'R':
                    k = "red";
                    break;
                case 'B':
                    k = "blue";
                    break;
                case 'D':
                    k = "black";
                    break;
                case 'Y':
                    k = "yellow";
                    break;
            }
            pname[j] = k + Integer.toString(j + 1);

            int resID = getResources().getIdentifier(pname[j], "drawable", Dedicate_Activity.this.getPackageName());
//            BitmapFactory.Options options = new BitmapFactory.Options();
//            options.inSampleSize = 2;
//            bitmap[j] = BitmapFactory.decodeResource(getResources(), resID, options);
            bitmap[j] = BitmapFactory.decodeResource(getResources(), resID);
            switch (j) {
                case 0:
                    img1.setImageBitmap(bitmap[j]);
                    break;
                case 1:
                    img2.setImageBitmap(bitmap[j]);
                    break;
                case 2:
                    img3.setImageBitmap(bitmap[j]);
                    break;
                case 3:
                    img4.setImageBitmap(bitmap[j]);
                    break;
                case 4:
                    img5.setImageBitmap(bitmap[j]);
                    break;
                case 5:
                    img6.setImageBitmap(bitmap[j]);
                    break;
                case 6:
                    img7.setImageBitmap(bitmap[j]);
                    break;
                case 7:
                    img8.setImageBitmap(bitmap[j]);
                    break;
            }
        }
        same = true;
        Character a = colororder[0];
        for (int j = 0; j < 8; j++) {
            if (colororder[j] != a) {
                same = false;
                break;
            } else {
                Log.d("test", "" + colororder[j] + " &&&" + a);
                continue;
            }
        }
        if (same) {
            int resID = getResources().getIdentifier("puzzle_1pack", "drawable", Dedicate_Activity.this.getPackageName());
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inSampleSize = 2;
            Bitmap frame = BitmapFactory.decodeResource(getResources(), resID, options);
            imgframe.setImageBitmap(frame);
        } else {
            int resID = getResources().getIdentifier("puzzle_8pack", "drawable", Dedicate_Activity.this.getPackageName());
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inSampleSize = 2;
            Bitmap frame = BitmapFactory.decodeResource(getResources(), resID, options);
            imgframe.setImageBitmap(frame);
        }
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
            now_longi = ""+longitude;
            now_latit = ""+latitude;
            Log.d("location find ",now_longi + "&& " +now_latit);
        } else {
            System.out.println("location==null");
            //如果沒有找到 預設四維堂的位置
            now_latit = ""+24.98635;
            now_longi = ""+121.575744;
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
    private void showLocation(Location location) {
        // 获取经度
        double longitude = location.getLongitude();
        // 获取纬度
        double latitude = location.getLatitude();
        now_longi=""+longitude;
        now_latit=""+latitude;
        Log.d("another show",now_longi+" && "+now_latit);
//        String message="经度为:"+longitude+"\n"+"纬度为:"+altitude;

    }
    private static double rad(double d)
    {
        return d * Math.PI / 180.0;
    }
    public double countdistance(double p_longi, double p_latit, double n_longi, double n_latit){
        Double result=0.0;
        double radLat1 = rad(p_latit);
        double radLat2 = rad(n_latit);
        double radlatit_dis =rad(p_latit) - rad(n_latit);
        double radlongi_dis = rad(p_longi) - rad(n_longi);
        result = 2 * Math.asin(Math.sqrt(Math.pow(Math.sin(radlatit_dis/2),2) +
                Math.cos(radLat1)*Math.cos(radLat2)*Math.pow(Math.sin(radlongi_dis/2),2)));
        result = result * EARTH_RADIUS * 1000;
        Log.d("show distance result",""+result);
        return result;
    };
    //左上角按了返回後結束activity
    public boolean onOptionsItemSelected(MenuItem item)
    {
        // TODO Auto-generated method stub
        if(item.getItemId() == android.R.id.home)
        {
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
            this.finish();
        }

        return false;

    }
}
