package net.macdidi.project111;

import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;

import java.net.ProtocolException;

/**
 * Created by Eddie84 on 2016/11/30.
 */
public class Gaming extends AppCompatActivity {
    private String a;
    private String user_id;
    private String myurl = "http://140.119.163.40:8080/DarkEmpire/app/ver1.0/placeState/list";
    private TextView topic_show;
    private TextView tribe;
    private TextView owner;
    private TextView hp;
    private TextView mana;
    private String hp_life;
    private String user_mana;
    private int aaa;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gaming);
        topic_show = (TextView)findViewById(R.id.topic);
        tribe = (TextView)findViewById(R.id.owner_tribe);
        owner = (TextView)findViewById(R.id.owner);
        hp = (TextView)findViewById(R.id.hp);
        mana = (TextView)findViewById(R.id.mana);
        topic_show.setText(Game_List.open_topic);
        SharedPreferences b = getSharedPreferences("DATA",0);
        user_id = b.getString("ID","");
        try {
            a=Http_Get.httpget(myurl);
            json6(a);
        } catch (ProtocolException e) {
            e.printStackTrace();
        }


    }
    public void json6(String result){
        try {
            JSONArray object_arr = new JSONArray(result);
            for(int i = 0 ; i<object_arr.length(); i++){
                String place_id = object_arr.getJSONObject(i).getString("place_id");
                if(place_id.equals(Game_List.open_id)){
                    String owner_id = object_arr.getJSONObject(i).getString("keeper_id");
                    totalRecord(owner_id);
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
                    else if(tribes == "0"){
                        String bb ="未佔領";
                        tribe.setText("屬於 : "+bb);
                    }

                    break;
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }


    }
    public void totalRecord(String result){
        String url ="http://140.119.163.40:8080/DarkEmpire/app/ver1.0/totalRecord/list";
        try {
            String aa = Http_Get.httpget(url);
            JSONArray object_arr =new JSONArray(aa);
            for(int i=0;i<object_arr.length();i++){
                String own_id = object_arr.getJSONObject(i).getString("user_id");
                if(own_id.equals(result)){
                    String name = object_arr.getJSONObject(i).getString("user_name");

                    owner.setText("石碑守護者 : "+name);

                }
                if(own_id.equals(user_id)) {
                    user_mana = object_arr.getJSONObject(i).getString("mana_now");
                    aaa = Integer.valueOf(user_mana);
                    mana.setText("馬納值 : " + user_mana);
                }
            }
        } catch (ProtocolException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }


    }
    public void playgame(View view){
        int id =view.getId();
        String url;
        String result;
        switch (id){
            case R.id.patrol:
                url = "http://140.119.163.40:8080/DarkEmpire/app/ver1.0/checkinPurify/"+user_id+"/"+Game_List.open_id+"/1/"+Game_Activity.open_longitude+"/"+Game_Activity.open_latitude+"/";
                try {
                    result = Http_Post.httppost(url);
                    Toast.makeText(this,result,Toast.LENGTH_SHORT).show();
                    if(result.contains("success")) {
                        MediaPlayer mediaPlayer01;
                        mediaPlayer01 = MediaPlayer.create(this, R.raw.m005);
                        mediaPlayer01.start();
                        aaa = aaa + 1;
                        mana.setText("馬納值 : " + aaa);
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
            case R.id.pure:
                url = "http://140.119.163.40:8080/DarkEmpire/app/ver1.0/checkinPurify/"+user_id+"/"+Game_List.open_id+"/2/"+Game_Activity.open_longitude+"/"+Game_Activity.open_latitude+"/";
                try {
                    result = Http_Post.httppost(url);
                    Toast.makeText(this,result,Toast.LENGTH_SHORT).show();
                    MediaPlayer mediaPlayer01;
                    mediaPlayer01 = MediaPlayer.create(this, R.raw.m004);
                    mediaPlayer01.start();
                } catch (ProtocolException e) {
                    e.printStackTrace();
                }
                break;
        }



    }



}