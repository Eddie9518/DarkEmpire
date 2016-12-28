package net.macdidi.project111;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;

/**
 * Created by Eddie84 on 2016/12/13.
 */
public class Setting_Activity extends AppCompatActivity {
    private Switch sndctrl;
    private Switch vbtctrl;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        sndctrl=(Switch)findViewById(R.id.sound_switch);
        vbtctrl=(Switch)findViewById(R.id.vibrate_switch);
        Button explanation =(Button)findViewById(R.id.explanation);
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
                intent.setClass(Setting_Activity.this,Explanation_Activity.class);
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
        editor.apply();
        Intent intent = new Intent();
        intent.setClass(Setting_Activity.this,MainActivity.class);
        startActivity(intent);
        this.finish();
    }
}