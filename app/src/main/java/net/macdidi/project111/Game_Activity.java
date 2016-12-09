package net.macdidi.project111;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
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

import java.net.ProtocolException;

/**
 * Created by Eddie84 on 2016/11/10.
 */
public class Game_Activity extends FragmentActivity
        implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener {
    private int change = 0;
    private GoogleMap mMap;
    public static String pid;
    public static double open_latitude;
    public static double open_longitude;

    // Google API用戶端物件
    private GoogleApiClient googleApiClient;

    // Location請求物件
    private LocationRequest locationRequest;

    // 記錄目前最新的位置
    private Location currentLocation;

    // 顯示目前與儲存位置的標記物件
    private Marker currentMarker, itemMarker;
    private String myurl="http://140.119.163.40:8080/DarkEmpire/app/ver1.0/place/list";
    private String a;
    private GroundOverlay bgoverlay;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.game_map);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.game_mapp);
        mapFragment.getMapAsync(this);

        MediaPlayer mediaPlayer01;
        mediaPlayer01 = MediaPlayer.create(this, R.raw.m003);
        mediaPlayer01.start();

        // 建立Google API用戶端物件
        configGoogleApiClient();

        // 建立Location請求物件
        configLocationRequest();
        // 連線到Google API用戶端
        if (!googleApiClient.isConnected()) {
            googleApiClient.connect();
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

        try {
            a=Http_Get.httpget(myurl);
            json3(a);
        } catch (ProtocolException e) {
            e.printStackTrace();
        }
        //LatLng aa = new LatLng(25.0268545,121.5420403);
        //mMap.addMarker(new MarkerOptions().position(aa));
        processController();
    }

    // 移動地圖到參數指定的位置
    private void moveMap(LatLng place) {
        // 建立地圖攝影機的位置物件
        CameraPosition cameraPosition =
                new CameraPosition.Builder()
                        .target(place)
                        .zoom(17)
                        .build();

        // 使用動畫的效果移動地圖
        mMap.moveCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
    }

    // 在地圖加入指定位置與標題的標記
    private void addMarker1(LatLng place,String title) {
        BitmapDescriptor icon1 =
                BitmapDescriptorFactory.fromResource(R.drawable.home2);

        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(place)
                .icon(icon1);
        Log.d("111","11111");

        itemMarker = mMap.addMarker(markerOptions);
        itemMarker.setTitle(title);
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
//        if(imageOverlay!=null) {
//            imageOverlay.remove();
//        }
        currentLocation = location;
        float a = (float) 0.3;
        LatLngBounds newarkBounds = new LatLngBounds(
                new LatLng(location.getLatitude()-0.01,location.getLongitude()-0.01),       // South west corner
                new LatLng(location.getLatitude()+0.01,location.getLongitude()+0.01));
        LatLng latLng = new LatLng(
                location.getLatitude(), location.getLongitude());
        GroundOverlayOptions newarkMap = new GroundOverlayOptions()
                .image(BitmapDescriptorFactory.fromResource(R.drawable.bg))
                .positionFromBounds(newarkBounds)
                .transparency(a);
        if(bgoverlay==null){
            bgoverlay = mMap.addGroundOverlay(newarkMap);
        }
        else{
            bgoverlay.setPosition(latLng);
        }
        if (currentMarker == null) {
            currentMarker = mMap.addMarker(new MarkerOptions().position(latLng));
//            addMarker1(latLng);
        }
        else {
            currentMarker.setPosition(latLng);
            //Log.d("latLng",""+latLng);
        }

            if(change==0) {
                moveMap(latLng);
                change++;
            }
    }

    private void processController() {
        // 標記點擊事件
        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                pid = marker.getTitle();
                //marker.remove();
                Intent intent = new Intent();
                intent.setClass(Game_Activity.this,Game_List.class);
                startActivity(intent);
                return true;

            }




        });
    }
    public void json3(String result) {
        try {
            JSONArray object_arr = new JSONArray(result);
            Log.d("aaaa",""+object_arr.length());
//            JSONObject object2 = new JSONObject(result);
            for(int i=0; i<object_arr.length();i++) {
                String m_id = object_arr.getJSONObject(i).getString("main_id");
                String p_id = object_arr.getJSONObject(i).getString("place_id");
                Log.d("asdfdasf",m_id);
               // String name = object_arr.getJSONObject(i).getString("name");
                if(m_id.equals("0")) {
                    double m_longi = object_arr.getJSONObject(i).getDouble("longitude");
                    double m_latit = object_arr.getJSONObject(i).getDouble("latitude");
                    open_latitude = m_latit;
                    open_longitude = m_longi;
                    LatLng m_place = new LatLng(m_latit, m_longi);
                    //Log.d("132",""+m_longi+"%%%%"+m_latit);
                        addMarker1(m_place,p_id);
                        //mMap.addMarker(new MarkerOptions().position(m_place));
                }

            }
        } catch (JSONException e) {
            Log.d("Not found","QQ");
            e.printStackTrace();
        }
    }
}
