package net.macdidi.project111;

import android.content.SharedPreferences;
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

public class MapsActivity extends FragmentActivity
        implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener {
    private LatLng user_place;

    private GoogleMap mMap;

    // Google API用戶端物件
    private GoogleApiClient googleApiClient;

    // Location請求物件
    private LocationRequest locationRequest;

    // 記錄目前最新的位置
    private Location currentLocation;

    // 顯示目前與儲存位置的標記物件
    private Marker currentMarker, itemMarker;
    private String myurl="http://140.119.163.40:8080/DarkEmpire/app/ver1.0/runeTradeRecord/search_rune/";
    private String gett;
    private String urll ="http://140.119.163.40:8080/DarkEmpire//app/ver1.0/runeTradeRecord/get_rune/";
    private double m_longitude;
    private double m_latitude;
    private GroundOverlay imageOverlay;
    private GroundOverlay backOverlay;
    private String a;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        SharedPreferences b = getSharedPreferences("DATA",0);
        a = b.getString("ID","");


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
        mMap.getUiSettings().setScrollGesturesEnabled(false);
//        // 讀取記事儲存的座標
//        Intent intent = getIntent();
//        double lat = intent.getDoubleExtra("lat", 0.0);
//        double lng = intent.getDoubleExtra("lng", 0.0);
//
//        // 如果記事已經儲存座標
//        if (lat != 0.0 && lng != 0.0) {
//            // 建立座標物件
//            LatLng itemPlace = new LatLng(lat, lng);
//            moveMap(itemPlace);
//        }
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
    private void addMarker1(LatLng place,String id) {
        BitmapDescriptor icon1 =
                BitmapDescriptorFactory.fromResource(R.drawable.s_money_antaya);

        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(place)
                .icon(icon1);


        itemMarker=mMap.addMarker(markerOptions);
        itemMarker.setTitle(id);


    }

    private void addMarker2(LatLng place,String id){
        BitmapDescriptor icon2 = BitmapDescriptorFactory.fromResource(R.drawable.s_money_sinar);
        MarkerOptions markerOptions2 = new MarkerOptions();
        markerOptions2.position(place)
                .icon(icon2);
        itemMarker=mMap.addMarker(markerOptions2);
        itemMarker.setTitle(id);



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
                googleApiClient, locationRequest, MapsActivity.this);
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


        LatLngBounds newarkBounds = new LatLngBounds(
                new LatLng(location.getLatitude()-0.002,location.getLongitude()-0.002),       // South west corner
                new LatLng(location.getLatitude()+0.002,location.getLongitude()+0.002));      // North east corner
        LatLng latLng = new LatLng(
                location.getLatitude(), location.getLongitude());
//        Log.d("12312","22222");
        GroundOverlayOptions newarkMap = new GroundOverlayOptions()
                .image(BitmapDescriptorFactory.fromResource(R.drawable.magic))
                .positionFromBounds(newarkBounds);
        GroundOverlayOptions backarkMap = new GroundOverlayOptions()
                .image(BitmapDescriptorFactory.fromResource(R.drawable.magicback))
                .positionFromBounds(newarkBounds);
        if (backOverlay == null) {
            backOverlay = mMap.addGroundOverlay(backarkMap);
        }
        else{
            backOverlay.setPosition(latLng);
        }
        if(imageOverlay==null) {
            imageOverlay = mMap.addGroundOverlay(newarkMap);
        }
        else {
            imageOverlay.setPosition(latLng);
        }
//        if(oldOverlay!=null){
//            oldOverlay.remove();
//        }
//        Log.d("123123","2929292");
//        Log.d("66666","2222");
        // 設定目前位置的標記
//        if (currentMarker == null) {
//            currentMarker = mMap.addMarker(new MarkerOptions().position(latLng));
////            addMarker1(latLng);
//        }
//        else {
//            currentMarker.setPosition(latLng);
//        }
        if(user_place==null) {
            user_place = latLng;
            Log.d("yoyoyyoyo",""+user_place.longitude+"/"+user_place.latitude);
            try {
                gett = Http_Get.httpget(myurl + user_place.longitude + "/" + user_place.latitude + "/");
                Log.d("show get",gett);
                json3(gett);
//                Log.d("halo","214");
            } catch (ProtocolException e) {
                e.printStackTrace();
            }


        }
//        oldOverlay = mMap.addGroundOverlay(newarkMap);
//        Log.d("testt",""+user_place.longitude+"&&"+user_place.latitude);
        // 移動地圖到目前的位置
        moveMap(latLng);

    }

    private void processController() {
        // 標記點擊事件
        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
//                 如果是目前位置標記
//                if (marker.equals(currentMarker)) {
//                    AlertDialog.Builder ab = new AlertDialog.Builder(MapsActivity.this);
//
//                    ab.setTitle(R.string.title_current_location)
//                            .setMessage(R.string.message_current_location)
//                            .setCancelable(true);
//
//                    ab.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
//                        @Override
//                        public void onClick(DialogInterface dialog, int which) {
//                            Intent result = new Intent();
//                            result.putExtra("lat", currentLocation.getLatitude());
//                            result.putExtra("lng", currentLocation.getLongitude());
//                            setResult(Activity.RESULT_OK, result);
//                            finish();
//                        }
//                    });
//                    ab.setNegativeButton(android.R.string.cancel, null);
//
//                    ab.show();
//                    if(marker.getTitle().equals("1")){
                MediaPlayer mediaPlayer01;
                mediaPlayer01 = MediaPlayer.create(getApplicationContext(), R.raw.m009);
                mediaPlayer01.start();
                        String posturl1 = urll + marker.getTitle()+"/"+a;
                        Log.d("123",posturl1);
                        try {
                            Http_Post.httppost(posturl1);
                            marker.remove();
                        } catch (ProtocolException e) {
                            e.printStackTrace();
                        }

                        return true;
//                    }
//                    else if(marker.getTitle().equals("2")){

                    }


//                }
//




        });
    }
    public void json3(String result) {
        try {
            JSONArray object_arr = new JSONArray(result);
            Log.d("aaaa",""+object_arr.length());
//            JSONObject object2 = new JSONObject(result);
            for(int i=0; i<object_arr.length();i++) {
                String m_id = object_arr.getJSONObject(i).getString("id");
                int id = object_arr.getJSONObject(i).getInt("rune_id");
                double money_longi = object_arr.getJSONObject(i).getDouble("longitude");
                double money_latit = object_arr.getJSONObject(i).getDouble("latitude");
                LatLng money_place = new LatLng(money_latit,money_longi);
                if(id==1){
                    addMarker1(money_place,m_id);
                }
                else if(id==2){
                    addMarker2(money_place,m_id);
                }

            }
        } catch (JSONException e) {
            Log.d("Not found","QQ");
            e.printStackTrace();
        }
    }

}