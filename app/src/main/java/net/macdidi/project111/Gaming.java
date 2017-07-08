package net.macdidi.project111;

import android.app.AlertDialog;
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
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.ProtocolException;
import java.util.Iterator;
import java.util.List;

/**
 * Created by Eddie84 on 2016/11/30.
 */
public class Gaming extends AppCompatActivity {
    private double EARTH_RADIUS = 6378.137;
    private String a;
    private String user_id;
    private String myurl = "http://140.119.163.40:8080/DarkEmpire/app/ver1.0/placeState/list";
    private String wpnurl = "http://140.119.163.40:8080/DarkEmpire/app/ver1.0/userItem/";
    private String imgurl = "http://140.119.163.40:8080/GameImg/image/app/";
    //紀錄api需要的變數
    private String recordurl ="http://140.119.163.40:8080/DarkEmpire/app/ver1.0/storeAction/";
    private String action_patrol = "2";
    private String action_pure = "3";
    private TextView topic_show;
    private TextView tribe;
    private TextView owner;
    private TextView hp;
    private TextView mana;
    private String hp_life;
    private String user_mana;
    private Button puring;
    private ImageView place_img;
    private int aaa;
    private String wpn;
    private ImageView show_wpn;
    //看是不是第一次玩 跳是參數
    private static String times;
    private ProgressBar hpleft;
    private boolean flag;
    private TextView potion_amt;
    private String max_hp;
    private String r_amt;
    private String y_amt;
    private String b_amt;
    private String belong_who;
    private String check_changed;
    private String recent_longi;
    private String recent_latit;
    private Boolean Internet_connection;
    //現在位置
    private Context mContext;
    private LocationManager mLocationManager;
    private LocationListenerImpl mLocationListenerImpl;
    private String now_longi;
    private String now_latit;
    //資料庫
    private SharedPreferences datab;
    private double pp_longi;
    private double pp_latit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gaming);
        //呼叫資料庫
        datab = getSharedPreferences("DATA",0);
        pp_longi = Double.valueOf(datab.getString("p_longi",""));
        pp_latit = Double.valueOf(datab.getString("p_latit",""));
        //測試網路狀態
        Internet_connection = haveInternet();
        //找現在位置
        mContext = this;
        initLocationManager(mContext);
        Log.d("123232",Game_List.open_id);
        topic_show = (TextView)findViewById(R.id.topic);
        tribe = (TextView)findViewById(R.id.owner_tribe);
        owner = (TextView)findViewById(R.id.owner);
        hp = (TextView)findViewById(R.id.hp);
        mana = (TextView)findViewById(R.id.mana);
        topic_show.setText(Game_List.open_topic);
        show_wpn = (ImageView)findViewById(R.id.choose_potion);
        potion_amt = (TextView)findViewById(R.id.potion_amount);
        place_img = (ImageView)findViewById(R.id.placeImg);
        //決定取im檔的網址
        imgurl = imgurl + Game_List.open_id;
        SharedPreferences b = getSharedPreferences("DATA",0);
        user_id = b.getString("ID","");
        //專屬武器url
        wpnurl = wpnurl+user_id;
        hpleft = (ProgressBar)findViewById(R.id.progressBar);
        flag = hpleft.isIndeterminate();
        potion_amt.setText("不用藥水");
        //拿武器ID
        //更新地點
//        b.edit().putString("longi",""+Game_Activity.open_longitude).apply();
//        b.edit().putString("latit",""+Game_Activity.open_latitude).apply();
        String show_a = b.getString("longi","");
        String show_b = b.getString("latit","");
        recent_longi = show_a;
        recent_latit = show_b;
        Log.d("now_longi",recent_longi);
        Log.d("now_latit",recent_latit);
        try {
            String wpncheck = Http_Get.httpget(wpnurl);
            JSONArray object_arr = new JSONArray(wpncheck);
            for(int i = 0 ; i < object_arr.length(); i++){
                String itemid = object_arr.getJSONObject(i).getString("item_id");
                if(itemid.equals("1")){
                }
                else if(itemid.equals("2")){
                    r_amt = object_arr.getJSONObject(i).getString("quantity");

                }
                else if(itemid.equals("3")){
                    y_amt = object_arr.getJSONObject(i).getString("quantity");
                }
                else if(itemid.equals("4")){
                    b_amt = object_arr.getJSONObject(i).getString("quantity");
                }
                else{
                    Log.d("error",itemid);
                }
            }
            //一開始預設為沒使用武器
            wpn = "1";
        } catch (ProtocolException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        //取完所有字串資料
        try {


            a=Http_Get.httpget(myurl);
            json6(a);
            check_changed = belong_who;
        } catch (ProtocolException e) {
            e.printStackTrace();
        }
        final String bb = b.getString("Firsttime","");
        String firsttime = b.getString("Fp_Gaming","");
        int first_time_press = firsttime.length();
        if(first_time_press==0){
            checkdialog();
            b.edit().putString("Fp_Gaming","pressed").apply();
        }
//        if(bb.equals("true")){
//            if(times!=null) {
//                Log.d("000",times);
//            }
//            else{
//                checkdialog();
////                Log.d("111",times);
//            }
//        }
//        else if(bb.equals("false")){
//            Log.d("222",bb);
//        }
        show_wpn.setImageDrawable(getResources().getDrawable(R.drawable.nopotion));
        show_wpn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                make_dialog();
            }
        });
//        trying = 0;
        puring = (Button)findViewById(R.id.pure);
        puring.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(Internet_connection) {
//                    if ((int)
//                            (countdistance(pp_longi, pp_latit, Double.valueOf(now_longi), Double.valueOf(now_latit))) <= 50) {
                        String url = "http://140.119.163.40:8080/DarkEmpire/app/ver1.0/checkinPurify/" + user_id + "/" + Game_List.open_id + "/2/" + now_longi + "/" + now_latit + "/" + wpn;
                        try {
                            String result = Http_Post.httppost(url);
                            Toast.makeText(Gaming.this, result, Toast.LENGTH_SHORT).show();
                            MediaPlayer mediaPlayer01;
                            mediaPlayer01 = MediaPlayer.create(Gaming.this, R.raw.m004);
                            mediaPlayer01.start();
                            //json6(a);
                            result.replace("\n", "").replace(" ", "");
                            //如果mana足夠的話
                            if (result.contains("success")) {
                                a = Http_Get.httpget(myurl);
                                //成功淨化的話重新呼叫後端資訊
                                json6(a);
                                if (wpn == "2") {
                                    int amount = Integer.valueOf(r_amt);
                                    amount--;
                                    r_amt = String.valueOf(amount);
                                    potion_amt.setText(("紅藥水 x " + amount));
                                } else if (wpn == "3") {
                                    int amount = Integer.valueOf(y_amt);
                                    amount--;
                                    y_amt = String.valueOf(amount);
                                    potion_amt.setText("黃藥水 x " + amount);
                                } else if (wpn == "4") {
                                    int amount = Integer.valueOf(b_amt);
                                    amount--;
                                    b_amt = String.valueOf(amount);
                                    potion_amt.setText("藍藥水 x " + amount);
                                }
                                //紀錄淨化成功的api
                                String pure_record = recordurl + user_id + "/" + action_pure + "/" + recent_longi + "/" + recent_latit + "/";
                                Http_Get.httpget(pure_record);
                                Log.d("pure_recordurl", pure_record);
                            }
                        } catch (ProtocolException e) {
                            e.printStackTrace();
                        }
                    }
//                    else {
//                        Toast.makeText(Gaming.this, "您已經脫離此地點囉!", Toast.LENGTH_SHORT).show();
//                    }
//                }
                    else{
                        Toast.makeText(Gaming.this, "請確認網路狀態 再重啟頁面", Toast.LENGTH_SHORT).show();
                    }

                }
//            }
        });
        //若為水平
        if(!flag) {
            hpleft.setMax(Integer.valueOf(max_hp));
            hpleft.setProgress(Integer.valueOf(hp_life));
        }
        //load 圖檔

            try {
                String encodedImage = Http_Get.httpget(imgurl);
                byte[] decodedString = Base64.decode(encodedImage, Base64.DEFAULT);
                Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                place_img.setImageBitmap(decodedByte);
                imgurl = null;

            } catch (ProtocolException e) {
                e.printStackTrace();
            }


    }
    //取得所有資訊的function
    public void json6(String result){
        try {
            Log.d("call json6","success");
            JSONArray object_arr = new JSONArray(result);
            for(int i = 0 ; i<object_arr.length(); i++){
                String place_id = object_arr.getJSONObject(i).getString("place_id");
                if(place_id.equals(Game_List.open_id)){ //如果list裡面的id是現在這個地點的id:
                    String owner_name = object_arr.getJSONObject(i).getString("keeperName");
                    String mmax_hp = object_arr.getJSONObject(i).getString("maxHp");
                    max_hp = mmax_hp;
                    owner.setText("神殿守護者 : " + owner_name);
//                    totalRecord(owner_id);
                    totalRecord();
                    hp_life = object_arr.getJSONObject(i).getString("hp");
                    String tribes=object_arr.getJSONObject(i).getString("camp_id");
                    belong_who = tribes;
                    hp.setText("神殿的馬納值 : "+hp_life);
                    Log.d("hpset","finished");
                    Log.d("hplife",""+hp_life);
                    if(tribes.equals("1")){
                        String bb ="安塔雅人";
                        tribe.setText("屬於 : "+bb);
                    }
                    else if(tribes.equals("2")){
                        String bb ="席奈人";
                        tribe.setText("屬於 : "+bb);
                    }
                    else if(tribes.equals("3")){
                        String bb ="黑暗勢力";
                        tribe.setText("屬於 : "+bb);
                    }
                    if(!flag) {
                        hpleft.setMax(Integer.valueOf(max_hp));
                        hpleft.setProgress(Integer.valueOf(hp_life));
                    }
                    break;
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (ProtocolException e) {
            e.printStackTrace();
        }


    }
    public void totalRecord() throws ProtocolException, JSONException {
        String url ="http://140.119.163.40:8080/DarkEmpire/app/ver1.0/totalRecord/list/"+user_id;
          String aa = Http_Get.httpget(url);
          JSONObject j_object = new JSONObject(aa);
          user_mana = j_object.getString("mana_now");
          aaa = Integer.valueOf(user_mana);
          mana.setText("您的馬納值 : " + user_mana);

    }
    public void playgame(View view){
        int id =view.getId();
        String url;
        String result;
        switch (id){
            case R.id.patrol:
                if(Internet_connection) {
//                    if ((int)
//                            (countdistance(pp_longi, pp_latit, Double.valueOf(now_longi), Double.valueOf(now_latit))) <= 50) {
                        url = "http://140.119.163.40:8080/DarkEmpire/app/ver1.0/checkinPurify/" + user_id + "/" + Game_List.open_id + "/1/" + now_longi + "/" + now_latit + "/" + 1;
                        try {
//                    Log.d("12323",url);
                            result = Http_Post.httppost(url);
                            Toast.makeText(this, result, Toast.LENGTH_SHORT).show();
                            if (result.contains("success")) {
                                //播放巡邏成功音效
                                MediaPlayer mediaPlayer01;
                                mediaPlayer01 = MediaPlayer.create(this, R.raw.m005);
                                mediaPlayer01.start();
                                //連接後端進行確認
                                a = Http_Get.httpget(myurl);
                                json6(a);
                                //紀錄patrol成功的api
                                String patrol_record = recordurl + user_id + "/" + action_patrol + "/" + recent_longi + "/" + recent_latit + "/";
                                Http_Get.httpget(patrol_record);
                                Log.d("patrol_record", patrol_record);

                            } else {
                                //播放巡邏失敗音效
                                MediaPlayer mediaPlayer01;
                                mediaPlayer01 = MediaPlayer.create(this, R.raw.m006);
                                mediaPlayer01.start();
                            }
                        } catch (ProtocolException e) {
                            e.printStackTrace();
                        }
                    }
//                    else{
//                        Toast.makeText(Gaming.this,"您已經脫離此地點囉!",Toast.LENGTH_SHORT).show();
//                    }
//                }
                else{
                    Toast.makeText(Gaming.this, "請確認網路狀態 再重啟頁面", Toast.LENGTH_SHORT).show();
                }
                break;
//            case R.id.pure:
//                url = "http://140.119.163.40:8080/DarkEmpire/app/ver1.0/checkinPurify/"+user_id+"/"+Game_List.open_id+"/2/"+Game_Activity.open_longitude+"/"+Game_Activity.open_latitude+"/";
//                try {
//                    result = Http_Post.httppost(url);
//                    Toast.makeText(this,result,Toast.LENGTH_SHORT).show();
//                    MediaPlayer mediaPlayer01;
//                    mediaPlayer01 = MediaPlayer.create(this, R.raw.m004);
//                    mediaPlayer01.start();
//                } catch (ProtocolException e) {
//                    e.printStackTrace();
//                }
//                break;
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
    //選藥水的表格
    public void make_dialog(){
        LayoutInflater inflater = LayoutInflater.from(Gaming.this);
        View alert_view = inflater.inflate(R.layout.activity_potion,null);
        final AlertDialog.Builder editDialog = new AlertDialog.Builder(Gaming.this);
        editDialog.setTitle("選擇藥水");
        editDialog.setView(alert_view);
        final AlertDialog dialog = editDialog.create();
        dialog.show();
        ImageView np = (ImageView)alert_view.findViewById(R.id.n_potion);
        ImageView rp = (ImageView)alert_view.findViewById(R.id.r_potion);
        ImageView yp = (ImageView)alert_view.findViewById(R.id.y_potion);
        ImageView bp = (ImageView)alert_view.findViewById(R.id.b_potion);
        TextView snp = (TextView)alert_view.findViewById(R.id.show_np);
        TextView srp = (TextView)alert_view.findViewById(R.id.show_rp);
        TextView syp = (TextView)alert_view.findViewById(R.id.show_yp);
        TextView sbp = (TextView)alert_view.findViewById(R.id.show_bp);
        srp.setText("紅藥水 x "+r_amt);
        syp.setText("黃藥水 x "+y_amt);
        sbp.setText("藍藥水 x "+b_amt);
        np.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                show_wpn.setImageDrawable(getResources().getDrawable(R.drawable.nopotion));
                potion_amt.setText("不用藥水");
                wpn = "1";
                dialog.cancel();
            }
        });
        rp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                show_wpn.setImageDrawable(getResources().getDrawable(R.drawable.redpotion));
                potion_amt.setText("紅藥水 x "+r_amt);
                wpn = "2";
                dialog.cancel();
            }
        });
        yp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                show_wpn.setImageDrawable(getResources().getDrawable(R.drawable.yellowpotion));
                potion_amt.setText("黃藥水 x "+y_amt);
                wpn = "3";
                dialog.cancel();
            }
        });
        bp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                show_wpn.setImageDrawable(getResources().getDrawable(R.drawable.bluepotion));
                potion_amt.setText("藍藥水 x "+b_amt);
                wpn = "4";
                dialog.cancel();
            }
        });



    }
    public void checkdialog(){
        LayoutInflater inflater = LayoutInflater.from(Gaming.this);
        View alert_view = inflater.inflate(R.layout.gaming_checkbox,null);
        final AlertDialog.Builder editDialog = new AlertDialog.Builder(Gaming.this);
        TextView aa = (TextView)alert_view.findViewById(R.id.patrol_content);
        TextView bb = (TextView)alert_view.findViewById(R.id.place_choose);
        String place = "您點選的是"+Game_List.open_topic;
        String content = "選擇「巡邏」增加馬納值來守護神殿\n當不幸神殿守護者是敵方的時候以\n「淨化」掠取，但是會減少馬納值。\n要注意有秒數限制哦！><\n\n開始吧！勇士！";
        aa.setText(content);
        bb.setText(place);
        editDialog.setTitle("遊戲規則");
        editDialog.setView(alert_view);
        final AlertDialog dialog = editDialog.create();
        CheckBox can_start = (CheckBox)alert_view.findViewById(R.id.checkBox);
        if(can_start.isChecked()){
            Log.d("123","222");
        }
        editDialog.setPositiveButton("開始遊戲", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
              times = "unable";
              dialog.dismiss();
            }
        }).show();


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
    //若有變動再重load 不然就直接finish
    public boolean onOptionsItemSelected(MenuItem item)
    {
        // TODO Auto-generated method stub
        if(item.getItemId() == android.R.id.home)
        {
            if(check_changed.equals(belong_who)) {
                Log.d("nochanged","77");
                finish();

            }
            else{
                //結束先前的Game_Activity 再開個新的
                Game_Activity.fa.finish();
                Log.d("yahchanged","66");
                Intent intent = new Intent();
                intent.setClass(Gaming.this,Game_Activity.class);
                startActivity(intent);
                Gaming.this.finish();

            }

            return true;
        }
        return super.onOptionsItemSelected(item);

    }
    //左下角按了返回後結束activity
    public boolean onKeyDown(int keyCode, KeyEvent event)
    {
        if (keyCode == KeyEvent.KEYCODE_BACK )
        {   if(check_changed.equals(belong_who)) {
            Log.d("nochanged","77");
            Gaming.this.finish();

        }
            else{
            Log.d("yahchanged","66");
            //結束先前的Game_Activity 再開個新的
            Game_Activity.fa.finish();
            Intent intent = new Intent();
            intent.setClass(Gaming.this,Game_Activity.class);
            startActivity(intent);
            Gaming.this.finish();

        }

        }
        return false;

    }



}