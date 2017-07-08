package net.macdidi.project111;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;

import java.net.ProtocolException;

/**
 * Created by Eddie84 on 2016/12/13.
 */
public class Setting_Activity extends AppCompatActivity {
    private Switch sndctrl;
//    private Switch vbtctrl;
    //紀錄api需要的變數
    private SharedPreferences b;
    private String recordurl ="http://140.119.163.40:8080/DarkEmpire/app/ver1.0/storeAction/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        b=getSharedPreferences("DATA",0);
        String user_id = b.getString("ID","");
        String recent_longi = b.getString("longi","");
        String recent_latit = b.getString("latit","");
        recordurl = "http://140.119.163.40:8080/DarkEmpire/app/ver1.0/storeAction/"+ user_id+"/9/"+recent_longi+"/"+recent_latit+"/";
        sndctrl=(Switch)findViewById(R.id.sound_switch);
//        vbtctrl=(Switch)findViewById(R.id.vibrate_switch);
        Button explanation =(Button)findViewById(R.id.explanation);
        Button dedication = (Button)findViewById(R.id.watch_dedicate);
        final AudioManager audioManager = (AudioManager) getSystemService(AUDIO_SERVICE);
        final SharedPreferences bb = getSharedPreferences("DATA",0);
        String a = bb.getString("Sound","");
        Log.d("show",a);
        if(a.equals("Off")){
            Log.d("123232","222");
            sndctrl.setChecked(false);
        }
        else if(a.equals("On")){
            Log.d("4444","444");
            sndctrl.setChecked(true);
        }
        else{
            Log.d("7777","777");
            bb.edit().putString("Sound","On").apply();
            audioManager.setStreamMute(AudioManager.STREAM_MUSIC,false);
            sndctrl.setChecked(true);
        }
        explanation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setClass(Setting_Activity.this,Instruction_Activity.class);
                startActivity(intent);
            }
        });
        dedication.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setClass(Setting_Activity.this,SeeDedicate_Activity.class);
                startActivity(intent);
            }
        });
        sndctrl.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b==true){
                    bb.edit().putString("Sound","On").apply();
                    audioManager.setStreamMute(AudioManager.STREAM_MUSIC,false);
                    Log.d("111","222");
                }
                else{
                    bb.edit().putString("Sound","Off").apply();
                    audioManager.setStreamMute(AudioManager.STREAM_MUSIC,true);
                    Log.d("3333","23232");
                }
            }
        });

    }
    public void settings(View view){
        new AlertDialog.Builder(this)
                .setTitle("登出")
                .setMessage("您確定要登出嗎")
                .setPositiveButton("確定",new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog,
                                        int whichButton) {
                                logout();
                        try {
                            Http_Get.httpget(recordurl);
                            Log.d("logoutrecord",recordurl);
                        } catch (ProtocolException e) {
                            e.printStackTrace();
                        }
                                /* User clicked OK so do some stuff */
                    }
                })
                .setNegativeButton("取消",new DialogInterface.OnClickListener(){

                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                }).create().show();


    }
    public void logout(){
        SharedPreferences b = getSharedPreferences("DATA",0);
        SharedPreferences.Editor editor = b.edit();
        editor.remove("ID");
        editor.remove("camp_id");
        editor.apply();
        Intent intent = new Intent();
        intent.setClass(Setting_Activity.this,MainActivity.class);
        startActivity(intent);
        this.finish();
    }
    //左上角按了返回後結束activity
    public boolean onOptionsItemSelected(MenuItem item)
    {
        // TODO Auto-generated method stub
        if(item.getItemId() == android.R.id.home)
        {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    //左下角按了返回後結束activity
    public boolean onKeyDown(int keyCode, KeyEvent event)
    {
        if (keyCode == KeyEvent.KEYCODE_BACK )
        {
            finish();
        }

        return false;

    }
}