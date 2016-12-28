package net.macdidi.project111;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import java.net.ProtocolException;

/**
 * Created by Eddie84 on 2016/10/26.
 */
public class Story_Activity extends AppCompatActivity {
    private String myurl = "http://140.119.163.40:8080/DarkEmpire/app/ver1.0/user/camp/";
    private String a;
    private SharedPreferences b;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.story_page);
        b = getSharedPreferences("DATA",0);
        a = b.getString("ID","");

        //呼叫故事劇情的dialog
        explanation();

    }
    public void to_menu(View view) throws ProtocolException {
        //根據選擇的陣營 post 到對應的網址
        int id = view.getId();
        switch (id){
            case R.id.sinai:
                Http_Post.httppost(myurl+a+"/2");
                b.edit().putString("camp_id","2").apply();
            case R.id.antaya:
                Http_Post.httppost(myurl+a+"/1");
                b.edit().putString("camp_id","1").apply();

        }
        //選完進入menu的activity
        Intent intent = new Intent();
        intent.setClass(this,Menu.class);
        startActivity(intent);
        this.finish();

    }
    public void explanation(){
        String aa = "在拳杉堡對抗暗黑勢力的你\n" +
                "將選擇加入席奈或是安塔雅族\n\n" +
                "你必須巡邏並淨化神殿\n" +
                "以捍衛您族人的勢力\n\n" +
                "暗黑勢力是兩族共同的敵人\n" +
                "當所有神殿遭暗黑勢力全面入侵時\n" +
                "你必須與族人合作\n" +
                "到大神殿救援拳杉堡\n\n" +
                "當你成為超原力使者時\n" +
                "你將負起保護大神殿的任務";
        new AlertDialog.Builder(this)
                .setTitle("遊戲說明")
                .setMessage(aa)
                .setPositiveButton("我知道了",new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog,
                                        int whichButton) {
                                /* User clicked OK so do some stuff */
                    }
                })
                .create().show();


    }
}
