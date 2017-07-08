package net.macdidi.project111;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

/**
 * Created by Eddie84 on 2016/10/13.
 */
public class Menu extends AppCompatActivity {
    private static final int START_LOCATION = 2;
    private static final int REQUEST_FINE_LOCATION_PERMISSION = 102;
    public static final String App_Dir = "/NCCU_Ingress";
    private MediaPlayer mediaPlayer01;
    private Boolean Internet_connection;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu);
        Internet_connection = haveInternet();
        mediaPlayer01 = MediaPlayer.create(this, R.raw.m002);
        mediaPlayer01.start();
        requestLocationPermission();
    }
    public void todata(View view){
        Intent intent = new Intent();
        intent.setClass(this,Personal_Data.class);
        startActivity(intent);
        mediaPlayer01.release();
        //this.finish();
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
    public void clickFunction(View view) {
        int id = view.getId();
        Intent intent = new Intent();
        if(Internet_connection) {
            switch (id) {
                case R.id.personal_inf_button:
                    intent.setClass(this, Personal_Data.class);
                    startActivity(intent);
                    mediaPlayer01.release();
                    break;
                case R.id.gain_money_button:
                    intent.setClass(this, Rune_Activity.class);
                    startActivity(intent);
                    mediaPlayer01.release();
                    //this.finish();
                    break;
                case R.id.badge_achievement_button:
                    intent.setClass(this, Badge_Activity.class);
                    startActivity(intent);
                    mediaPlayer01.release();
                    //this.finish();
                    break;
                case R.id.start_game_button:
                    requestLocationPermission();
                    intent.setClass(this, Game_Activity.class);
                    startActivity(intent);
                    mediaPlayer01.release();
                    //this.finish();
                    break;
                case R.id.inter_communication_button:
                    intent.setClass(this, Chatroom_Activity.class);
                    startActivity(intent);
                    mediaPlayer01.release();
                    break;
                case R.id.story_background_button:
                    intent.setClass(this, Explanation_Activity.class);
                    startActivity(intent);
                    mediaPlayer01.release();
                    break;
            }
        }
        else{
            Toast.makeText(Menu.this,"請確認網路狀況 然後重啟遊戲",Toast.LENGTH_SHORT).show();
        }
    }
//    public void settings(View view){
//        SharedPreferences b = getSharedPreferences("DATA",0);
//        SharedPreferences.Editor editor = b.edit();
//        editor.remove("ID");
//        editor.apply();
//        Intent intent = new Intent();
//        intent.setClass(this,MainActivity.class);
//        startActivity(intent);
//        this.finish();
//
//    }
    public void settings2(View view){
        Intent intent = new Intent();
        intent.setClass(this,Setting_Activity.class);
        startActivity(intent);

    }
    private void requestLocationPermission() {
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


    }
}
