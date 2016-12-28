package net.macdidi.project111;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
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

/**
 * Created by Eddie84 on 2016/11/30.
 */
public class Gaming extends AppCompatActivity {
    private String a;
    private String user_id;
    private String myurl = "http://140.119.163.40:8080/DarkEmpire/app/ver1.0/placeState/list";
    private String wpnurl = "http://140.119.163.40:8080/DarkEmpire/app/ver1.0/userItem/";
    private String imgurl = "http://140.119.163.40:8080/GameImg/image/app/";
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
    private int trying;
    private String wpn;
    private ImageView show_wpn;
    private static String times;
    private ProgressBar hpleft;
    private boolean flag;
    private TextView potion_amt;
    private String max_hp;
    private String r_amt;
    private String y_amt;
    private String b_amt;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gaming);
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
        try {
            String wpncheck = Http_Get.httpget(wpnurl);
            JSONArray object_arr = new JSONArray(wpncheck);
            for(int i = 0 ; i < object_arr.length(); i++){
                String itemid = object_arr.getJSONObject(i).getString("item_id");
                if(itemid.equals("1")){

                }
                else if(itemid.equals("2")){
                    String quants = object_arr.getJSONObject(i).getString("quantity");
                    r_amt = quants;
                    wpn="2";
                }
                else if(itemid.equals("3")){
                    String quants = object_arr.getJSONObject(i).getString("quantity");
                    y_amt = quants;
                }
                else if(itemid.equals("4")){
                    String quants = object_arr.getJSONObject(i).getString("quantity");
                    b_amt = quants;
                }
                else{
                    Log.d("error",itemid);
                }
            }
            //wpn = new JSONObject(wpncheck).getString("item_id");
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
        } catch (ProtocolException e) {
            e.printStackTrace();
        }
        final String bb = b.getString("Firsttime","");
        if(bb.equals("true")){
            if(times!=null) {
                Log.d("000",times);
            }
            else{
                checkdialog();
//                Log.d("111",times);
            }
        }
        else if(bb.equals("false")){
            Log.d("222",bb);
        }
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
//                if(trying==0){
//                    make_dialog();
//                    Log.d("hahaah", "0000");
//                    trying++;
//                }
//                else{
                    String url = "http://140.119.163.40:8080/DarkEmpire/app/ver1.0/checkinPurify/"+user_id+"/"+Game_List.open_id+"/2/"+Game_Activity.open_longitude+"/"+Game_Activity.open_latitude+"/"+wpn;
                    try {
                        String result = Http_Post.httppost(url);
                        Toast.makeText(Gaming.this,result,Toast.LENGTH_SHORT).show();
                        MediaPlayer mediaPlayer01;
                        mediaPlayer01 = MediaPlayer.create(Gaming.this, R.raw.m004);
                        mediaPlayer01.start();
                        Log.d("123",""+aaa);
                        aaa=aaa-100;
                        mana.setText("馬納值 : " + aaa);
                        hpleft.incrementProgressBy(-100);
                    } catch (ProtocolException e) {
                        e.printStackTrace();
                    }
                }
//            }
        });

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
    public void json6(String result){
        try {
            JSONArray object_arr = new JSONArray(result);
            for(int i = 0 ; i<object_arr.length(); i++){
                String place_id = object_arr.getJSONObject(i).getString("place_id");
                if(place_id.equals(Game_List.open_id)){ //如果list裡面的id是現在這個地點的id:
                    String owner_id = object_arr.getJSONObject(i).getString("keeper_id");
                    String mmax_hp = object_arr.getJSONObject(i).getString("maxHp");
                    max_hp = mmax_hp;
                    owner.setText("神殿守護者 : " + owner_id);
//                    totalRecord(owner_id);
                    totalRecord();
                    hp_life = object_arr.getJSONObject(i).getString("hp");
                    String tribes=object_arr.getJSONObject(i).getString("camp_id");
                    hp.setText("生命值 : "+hp_life);
                    if(tribes == "1"){
                        String bb ="安塔雅人";
                        tribe.setText("屬於 : "+bb);
                    }
                    else if(tribes == "2"){
                        String bb ="席奈人";
                        tribe.setText("屬於 : "+bb);
                    }
                    else if(tribes == "3"){
                        String bb ="黑暗勢力";
                        tribe.setText("屬於 : "+bb);
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
//        String url ="http://140.119.163.40:8080/DarkEmpire/app/ver1.0/totalRecord/list";
//        try {
//            String aa = Http_Get.httpget(url);
//            JSONArray object_arr =new JSONArray(aa);
//            for(int i=0;i<object_arr.length();i++){
//                String own_id = object_arr.getJSONObject(i).getString("user_id");
//                if(own_id.equals(result)){
//                    String name = object_arr.getJSONObject(i).getString("user_name");
//
//                    owner.setText("石碑守護者 : "+name);
//
//                }
//                if(own_id.equals(user_id)) {
//                    user_mana = object_arr.getJSONObject(i).getString("mana_now");
//                    aaa = Integer.valueOf(user_mana);
//                    mana.setText("馬納值 : " + user_mana);
//                }
//            }
//        } catch (ProtocolException e) {
//            e.printStackTrace();
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
          String aa = Http_Get.httpget(url);
          JSONObject j_object = new JSONObject(aa);
          user_mana = j_object.getString("mana_now");
          aaa = Integer.valueOf(user_mana);
          mana.setText("馬納值 : " + user_mana);

    }
    public void playgame(View view){
        int id =view.getId();
        String url;
        String result;
        switch (id){
            case R.id.patrol:
                url = "http://140.119.163.40:8080/DarkEmpire/app/ver1.0/checkinPurify/"+user_id+"/"+Game_List.open_id+"/1/"+Game_Activity.open_longitude+"/"+Game_Activity.open_latitude+"/"+1;
                try {
//                    Log.d("12323",url);
                    result = Http_Post.httppost(url);
                    Toast.makeText(this,result,Toast.LENGTH_SHORT).show();
                    if(result.contains("success")) {
                        MediaPlayer mediaPlayer01;
                        mediaPlayer01 = MediaPlayer.create(this, R.raw.m005);
                        mediaPlayer01.start();
                        aaa = aaa + 10;
                        mana.setText("馬納值 : " + aaa);
                        hpleft.incrementProgressBy(10);
                    }
                    else{
                        MediaPlayer mediaPlayer01;
                        mediaPlayer01 = MediaPlayer.create(this, R.raw.m006);
                        mediaPlayer01.start();
                    }
                } catch (ProtocolException e) {
                    e.printStackTrace();
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



}