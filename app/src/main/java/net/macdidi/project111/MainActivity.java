package net.macdidi.project111;

import android.content.Intent;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import java.net.ProtocolException;

public class MainActivity extends AppCompatActivity {
    private SoundPool soundPool;
    private int soundID;
    Handler aHandler;
    private MediaPlayer mediaPlayer01;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.logo_page);
        soundPool = new SoundPool(1, AudioManager.STREAM_MUSIC,0);
//        WebView myBrowser = (WebView)findViewById(R.id.login_view);
//        soundID = soundPool.load(this,R.raw.opening,1);
//        aHandler = new Handler();
//        aHandler.post(runnable);
       // soundPool.play(this.soundID,1,1,0,0,1);

        mediaPlayer01 = MediaPlayer.create(MainActivity.this, R.raw.opening);
        mediaPlayer01.start();

    }
    final Runnable runnable = new Runnable() {
        public void run() {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            soundPool.play(soundID, 1, 1, 0, 1, 1);
        }


    };


    public void jump(View view){
        Log.d("12323","nono");
        SharedPreferences b = getSharedPreferences("DATA",0);
        Log.d("222","NONON");
//        boolean booValue = b.getBoolean("ID", true);
        String a = b.getString("ID","");
//        Log.d("why","www");
//        Log.d("a",a);
        if(a!="") {
            mediaPlayer01.pause();
            mediaPlayer01.release();
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
    public void login(View view) throws ProtocolException {

//        Http_Get.httpget("http://140.119.163.40.8080/Spring08/app/login");
        String url="http://140.119.163.40:8080/DarkEmpire/app/login";

        Intent ie = new Intent(Intent.ACTION_VIEW, Uri.parse(url));

        startActivity(ie);

    }

}
