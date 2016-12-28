package net.macdidi.project111;

import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import java.net.ProtocolException;

public class MainActivity extends AppCompatActivity {
    private MediaPlayer mediaPlayer01;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.logo_page);


        mediaPlayer01 = MediaPlayer.create(MainActivity.this, R.raw.opening);
        mediaPlayer01.start();

    }


    public void jump(View view) throws ProtocolException {
        //從資料庫拿id 看是不是已登入狀態 選擇進入對應的activity
        SharedPreferences b = getSharedPreferences("DATA",0);
        String a = b.getString("ID","");
        if(a!="") {
            String init_url = "http://140.119.163.40:8080/DarkEmpire/app/ver1.0/init/"+a;
            Http_Get.httpget(init_url);
            mediaPlayer01.pause();
            mediaPlayer01.release();
            b.edit().putString("Firsttime","false").apply();
            Intent intent = new Intent();
            intent.setClass(this, Menu.class);
            startActivity(intent);

            //this.finish();
        }
        else {
            mediaPlayer01.pause();
            mediaPlayer01.release();
            Intent intent = new Intent();
            intent.setClass(this, Login_Activity.class);
            startActivity(intent);
            //this.finish();
        }
    }


}
