package net.macdidi.project111;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.GroundOverlay;
import com.google.android.gms.maps.model.GroundOverlayOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.ProtocolException;

/**
 * Created by Eddie84 on 2016/11/10.
 */
public class Game_Activity extends AppCompatActivity
        implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener {
    private int change = 0;
    private static String testing;
    private GoogleMap mMap;
    public static String pid;
    public static double open_latitude;
    public static double open_longitude;
    private double EARTH_RADIUS = 6378.137;
    // Google API用戶端物件
    private GoogleApiClient googleApiClient;

    // Location請求物件
    private LocationRequest locationRequest;

    // 記錄目前最新的位置
    private Location currentLocation;

    // 顯示目前與儲存位置的標記物件
    private Marker currentMarker, itemMarker;
    private String myurl = "http://140.119.163.40:8080/DarkEmpire/app/ver1.0/place/list";
    private String camp_url = "http://140.119.163.40:8080/DarkEmpire/app/ver1.0/placeState/";
    private String a;
    private GroundOverlay bgoverlay;
    private LatLng userLocation;
    private double m_latitude;
    private double m_longitude;
    private double s;
    private SharedPreferences datab;
    //做一個代表上傳的activity
    public static Activity fa;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.game_map);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.game_mapp);
        mapFragment.getMapAsync(this);
        //代表此Activity
        fa = this;
        //音效檔
        MediaPlayer mediaPlayer01;
        mediaPlayer01 = MediaPlayer.create(this, R.raw.m003);
        mediaPlayer01.start();
        //若是沒讀到現在位置 將位置訂在四維堂
        userLocation = new LatLng(24.98635,121.575744);


        configGoogleApiClient();
        // 建立Location請求物件
        configLocationRequest();
        // 連線到Google API用戶端


        //若是第一次登入會有checkbox
         datab = getSharedPreferences("DATA",0);
        String first_time = datab.getString("Fp_Game_Activity","");
        int first_time_press = first_time.length();
        if(first_time_press==0) {
            checkbox();
            datab.edit().putString("Fp_Game_Activity", "pressed").apply();
        }
        }

    // 建立Google API用戶端物件
    private synchronized void configGoogleApiClient() {
        googleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
    }

    // 建立Location請求物件
    private void configLocationRequest() {
        locationRequest = new LocationRequest();
        // 設定讀取位置資訊的間隔時間為一秒（1000ms）
        locationRequest.setInterval(1000);
        // 設定讀取位置資訊最快的間隔時間為一秒（1000ms）
        locationRequest.setFastestInterval(1000);
        // 設定優先讀取高精確度的位置資訊（GPS）
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }

    @Override
    protected void onResume() {
        super.onResume();

        // 連線到Google API用戶端
        if (!googleApiClient.isConnected() && currentMarker != null) {
            googleApiClient.connect();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();

        // 移除位置請求服務
        if (googleApiClient.isConnected()) {
            LocationServices.FusedLocationApi.removeLocationUpdates(
                    googleApiClient, this);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();

        // 移除Google API用戶端連線
        if (googleApiClient.isConnected()) {
            googleApiClient.disconnect();
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
//        mMap.getUiSettings().setScrollGesturesEnabled(false);
//        mMap.getUiSettings().setZoomGesturesEnabled(false);
        //進行googleApiClient的連線
        if (!googleApiClient.isConnected()) {
            googleApiClient.connect();
            //連線時抓取現在位置
        }
        try {
            a=Http_Get.httpget(myurl);
            json3(a);
        } catch (ProtocolException e) {
            e.printStackTrace();
        }
        //LatLng aa = new LatLng(25.0268545,121.5420403);
        //mMap.addMarker(new MarkerOptions().position(aa));
//        processController();
//        moveMap(userLocation);
    }

    // 移動地圖到參數指定的位置
    private void moveMap(LatLng place) {
        // 建立地圖攝影機的位置物件
        CameraPosition cameraPosition =
                new CameraPosition.Builder()
                        .target(place)
                        .zoom(34)
                        .build();

        // 使用動畫的效果移動地圖
        mMap.moveCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
    }
    private void moveMap2(LatLng place){
        CameraPosition cameraPosition2 =new CameraPosition.Builder().target(place).zoom(mMap.getCameraPosition().zoom).bearing(mMap.getCameraPosition().bearing).build();
        mMap.moveCamera(CameraUpdateFactory.newCameraPosition(cameraPosition2));
    }

    // 在地圖加入指定位置與標題的標記
    private void addMarker1(LatLng place,String title) {
        String markerurl = camp_url + title;
        String camp = "";
        try {
            String a = Http_Get.httpget(markerurl);
            JSONObject j_obj = new JSONObject(a);
            camp = j_obj.getString("camp_id");
            BitmapDescriptor icon1 = null;
            //如果是大神殿
            if(title.equals("51")){
                if(camp.equals("1")){
                    icon1 = BitmapDescriptorFactory.fromResource(R.drawable.bt_red);
                }
                else if(camp.equals("2")){
                    icon1 = BitmapDescriptorFactory.fromResource(R.drawable.bt_blue);
                }
                else if(camp.equals("3")){
                    icon1 = BitmapDescriptorFactory.fromResource(R.drawable.bt_black);
                }
            }
            //其他marker的話
            else if(camp.equals("1")){
                icon1 = BitmapDescriptorFactory.fromResource(R.drawable.temple_red);
            }
            else if(camp.equals("2")) {
                icon1 = BitmapDescriptorFactory.fromResource(R.drawable.temple_blue);
            }
            else if(camp.equals("3")){
                icon1 = BitmapDescriptorFactory.fromResource(R.drawable.temple_black);
            }
            MarkerOptions markerOptions = new MarkerOptions();
            markerOptions.position(place)
                    .icon(icon1);
            itemMarker = mMap.addMarker(markerOptions);
            itemMarker.setTitle(title);

        } catch (ProtocolException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
//        BitmapDescriptor icon1 =
//                BitmapDescriptorFactory.fromResource(R.drawable.home2);




    }
    // ConnectionCallbacks
    @Override
    public void onConnected(Bundle bundle) {
        // 已經連線到Google Services
        // 啟動位置更新服務
        // 位置資訊更新的時候，應用程式會自動呼叫LocationListener.onLocationChanged
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
        LocationServices.FusedLocationApi.requestLocationUpdates(
                googleApiClient, locationRequest, Game_Activity.this);
        Location mLastLocation = LocationServices.FusedLocationApi.getLastLocation(googleApiClient);
        if(mLastLocation!=null) {
            m_latitude = 0.0;
            m_longitude = 0.0;
            m_latitude = mLastLocation.getLatitude();
            m_longitude = mLastLocation.getLongitude();
            Log.d("1stuserLocation", "" + userLocation);
            if (m_latitude != 0.0 && m_longitude != 0.0) {
                userLocation = new LatLng(m_latitude, m_longitude);
            }
        }
        float a = (float) 0.3;
        LatLngBounds newarkBounds = new LatLngBounds(
                new LatLng(userLocation.latitude-0.1,userLocation.longitude-0.1),
                new LatLng(userLocation.latitude+0.1,userLocation.longitude+0.1));
        GroundOverlayOptions newarkMap = new GroundOverlayOptions()
                .image(BitmapDescriptorFactory.fromResource(R.drawable.bg))
                .positionFromBounds(newarkBounds)
                .transparency(a);
        Log.d("2nduserLocation",""+userLocation);
        processController();
        bgoverlay = mMap.addGroundOverlay(newarkMap);
        //紀錄api 寫入目前位置
//        SharedPreferences datab = getSharedPreferences("DATA",0);
        datab.edit().putString("longi",""+userLocation.longitude).apply();
        datab.edit().putString("latit",""+userLocation.latitude).apply();
        moveMap(userLocation);


    }

    // ConnectionCallbacks
    @Override
    public void onConnectionSuspended(int i) {
        // Google Services連線中斷
        // int參數是連線中斷的代號
    }

    // OnConnectionFailedListener
    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        // Google Services連線失敗
        // ConnectionResult參數是連線失敗的資訊
        int errorCode = connectionResult.getErrorCode();

        // 裝置沒有安裝Google Play服務
        if (errorCode == ConnectionResult.SERVICE_MISSING) {
            Toast.makeText(this, R.string.google_play_service_missing,
                    Toast.LENGTH_LONG).show();
        }
    }

    // LocationListener
    @Override
    public void onLocationChanged(Location location) {
        // 位置改變
        // Location參數是目前的位置
        currentLocation = location;
        LatLng latLng = new LatLng(
                location.getLatitude(), location.getLongitude());
        bgoverlay.setPosition(latLng);
        //現在位置的游標
        if (currentMarker == null) {
            currentMarker = mMap.addMarker(new MarkerOptions().position(latLng));
        }
        else {
            currentMarker.setPosition(latLng);
        }
        //移動攝影機到現在的位置
        //更新位置資訊 存到資料庫
        datab.edit().putString("longi",""+currentLocation.getLongitude()).apply();
        datab.edit().putString("latit",""+currentLocation.getLatitude()).apply();
//        moveMap2(latLng);
    }

    private void processController() {
        // 標記點擊事件
        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                pid = marker.getTitle();
                String checkdis = checkdistance(pid);
                int needdis;
//                if(checkdis.equals("success")) {
                    if (pid.equals("51")) {
                        Intent intent = new Intent();
                        intent.setClass(Game_Activity.this, Dedicate_Activity.class);
                        startActivity(intent);
                        return true;
                    }
                    //marker.remove();
                    else {
                        Intent intent = new Intent();
                        intent.setClass(Game_Activity.this, Game_List.class);
                        startActivity(intent);
                        return true;
                    }
//                }
//                else{
//                    if(pid.equals("51")){
//                        needdis = (int)s-99;
//                    }
//                    else {
//                        needdis = (int) s - 49;
//                    }
//                    String print_result = "與該地點距離太遠 還要再"+needdis+"公尺";
//                    Toast.makeText(Game_Activity.this,print_result,Toast.LENGTH_SHORT).show();
//                    return true;
//                }
            }




        });
    }
    public void json3(String result) {
        try {
            JSONArray object_arr = new JSONArray(result);
//            JSONObject object2 = new JSONObject(result);
            for(int i=0; i<object_arr.length();i++) {
                String m_id = object_arr.getJSONObject(i).getString("main_id");
                String p_id = object_arr.getJSONObject(i).getString("place_id");

                if(m_id.equals("0")) { //如果id=0(不是依附在其他點上）則加marker
                    double m_longi = object_arr.getJSONObject(i).getDouble("longitude");
                    double m_latit = object_arr.getJSONObject(i).getDouble("latitude");
                    open_latitude = m_latit;
                    open_longitude = m_longi;
                    LatLng m_place = new LatLng(m_latit, m_longi);
                        addMarker1(m_place,p_id);
                }

            }
        } catch (JSONException e) {
            Log.d("Not found","QQ");
            e.printStackTrace();
        }
    }
    private static double rad(double d)
    {
        return d * Math.PI / 180.0;
    }
    public String checkdistance(String placeid){
        String distance_result="success";
        try {
            String secondcheck = Http_Get.httpget(myurl);
            JSONArray object_arr = new JSONArray(secondcheck);
            for(int i=0; i<object_arr.length();i++){
                String p_id = object_arr.getJSONObject(i).getString("place_id");
                if(p_id.equals(placeid)) {
                    double pressed_longi = object_arr.getJSONObject(i).getDouble("longitude");
                    double pressed_latit = object_arr.getJSONObject(i).getDouble("latitude");
                    //存下現在點的marker的經緯度
                    datab.edit().putString("p_longi", "" + pressed_longi).apply();
                    datab.edit().putString("p_latit", "" + pressed_latit).apply();
                    s = countdistance(pressed_longi, pressed_latit, currentLocation.getLongitude(), currentLocation.getLatitude());
                    //大神殿要設100meter;
                    if (placeid.equals("51")) {
                        if (s > 100) {
                            distance_result = "failed";
                        } else {
                            distance_result = "success";
                        }
                    }
                    else{
                        if (s > 50) {
                            distance_result = "failed";
                            Log.d("failed ab", "" + s);
                        } else {
                            distance_result = "success";
                            Log.d("success ab", "" + s);
                        }
                    }
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (ProtocolException e) {
            e.printStackTrace();
        }
        return distance_result;
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
    public void checkbox(){
        LayoutInflater inflater = LayoutInflater.from(Game_Activity.this);
        View alert_view = inflater.inflate(R.layout.gameactivity_checkbox,null);
        final AlertDialog.Builder editDialog = new AlertDialog.Builder(Game_Activity.this);
        editDialog.setTitle("攻塔攻略");
        editDialog.setView(alert_view);
//        CheckBox aa = (CheckBox)alert_view.findViewById(R.id.checkBox2);
        final AlertDialog dialog = editDialog.create();
        editDialog.setPositiveButton("開始探險", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                //代表第一次登入顯示完checkout
                testing = "unable";
                dialog.dismiss();
            }
        }).show();

    }
    //左上角按了返回後結束activity
    public boolean onOptionsItemSelected(MenuItem item)
    {
        // TODO Auto-generated method stub
        if(item.getItemId() == android.R.id.home)
        {
            Game_Activity.this.finish();
            Log.d("game_activity finished","1");
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    //左下角按了返回後結束activity
    public boolean onKeyDown(int keyCode, KeyEvent event)
    {
        if (keyCode == KeyEvent.KEYCODE_BACK )
        {
            Game_Activity.this.finish();
        }

        return false;

    }

}
