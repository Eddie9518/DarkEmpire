package net.macdidi.project111;

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
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.story_page);
        SharedPreferences b = getSharedPreferences("DATA",0);
        a = b.getString("ID","");

    }
    public void to_menu(View view) throws ProtocolException {
        int id = view.getId();
        switch (id){
            case R.id.sinai:
                Http_Post.httppost(myurl+a+"/2");
            case R.id.antaya:
                Http_Post.httppost(myurl+a+"/1");

        }
        Intent intent = new Intent();
        intent.setClass(this,Menu.class);
        startActivity(intent);
        this.finish();

    }
}
