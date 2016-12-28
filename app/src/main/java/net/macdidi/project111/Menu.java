package net.macdidi.project111;

import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

/**
 * Created by Eddie84 on 2016/10/13.
 */
public class Menu extends AppCompatActivity {
    private static final int START_LOCATION = 2;
    private static final int REQUEST_FINE_LOCATION_PERMISSION = 102;
    public static final String App_Dir = "/NCCU_Ingress";
    private MediaPlayer mediaPlayer01;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu);
        mediaPlayer01 = MediaPlayer.create(this, R.raw.m002);
        mediaPlayer01.start();
    }
    public void todata(View view){
        Intent intent = new Intent();
        intent.setClass(this,Personal_Data.class);
        startActivity(intent);
        mediaPlayer01.release();
        //this.finish();
    }
    public void tomap(View view){
        Intent intentMap = new Intent(Menu.this, MapsActivity.class);
        // 啟動地圖元件
        startActivityForResult(intentMap, START_LOCATION);
    }

    public void clickFunction(View view) {
        int id = view.getId();
        Intent intent = new Intent();
        switch (id) {
            case R.id.gain_money_button:
                intent.setClass(this,Rune_Activity.class);
                startActivity(intent);
                //this.finish();
                break;
            case R.id.badge_achievement_button:
                intent.setClass(this,Badge_Activity.class);
                startActivity(intent);
                //this.finish();
                break;
            case R.id.start_game_button:
                intent.setClass(this,Game_Activity.class);
                startActivity(intent);
                //this.finish();
                break;
            case R.id.inter_communication_button:
                intent.setClass(this,Chatroom_Activity.class);
                startActivity(intent);
                break;
        }
    }
    public void settings(View view){
        SharedPreferences b = getSharedPreferences("DATA",0);
        SharedPreferences.Editor editor = b.edit();
        editor.remove("ID");
        editor.apply();
        Intent intent = new Intent();
        intent.setClass(this,MainActivity.class);
        startActivity(intent);
        this.finish();

    }
    public void settings2(View view){
        Intent intent = new Intent();
        intent.setClass(this,Setting_Activity.class);
        startActivity(intent);

    }
}
