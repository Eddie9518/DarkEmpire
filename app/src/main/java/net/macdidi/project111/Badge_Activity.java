package net.macdidi.project111;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.FragmentTabHost;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.ProtocolException;
import java.util.ArrayList;

/**
 * Created by Eddie84 on 2016/11/1.
 */
public class Badge_Activity extends AppCompatActivity {

    private String n_myurl = "http://140.119.163.40:8080/DarkEmpire/app/ver1.0/badgeType";
    private String [] classification;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tabs);
        // 將 TextView 加入到 LinearLayout 中
        FragmentTabHost tabHost = (FragmentTabHost)findViewById(android.R.id.tabhost);
        tabHost.setup(this, getSupportFragmentManager(), R.id.realtabcontent);
        //1
        try {
            String a= Http_Get.httpget(n_myurl);
            JSONObject j_obj = new JSONObject(a);
            String testing = j_obj.getString("type");
            Log.d("testing",testing);
            classification = testing.split(",");
            for(int i =0; i<classification.length; i++){
                tabHost.addTab(tabHost.newTabSpec(classification[i])
                                .setIndicator(classification[i]),
                                Tab_Activity.class,
                                null);
            }
        } catch (ProtocolException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    public ArrayList<String> json4(String input,String classes)  {

        ArrayList <String> output = new ArrayList();
        try {

            JSONArray inside_arr = new JSONArray(input);
            Log.d("12323",""+inside_arr.length());
            for(int i=0 ; i < inside_arr.length();i++){
                if(inside_arr.getJSONObject(i).getString("badge_type").equals(classes)){
                    output.add(inside_arr.getJSONObject(i).getString("name"));

                }
            }
            Log.d("12323",""+output.size());
        }
        catch (JSONException e) {
            Log.d("Not found","QQ");
            e.printStackTrace();
        }
        return output;
    }
    public ArrayList<String> json5(String input,String classes){
        ArrayList <String> output2 = new ArrayList<>();
        try {

            JSONArray inside_arr = new JSONArray(input);
            Log.d("12323",""+inside_arr.length());
            for(int i=0 ; i < inside_arr.length();i++){
                if(inside_arr.getJSONObject(i).getString("badge_type").equals(classes)){
                    output2.add(inside_arr.getJSONObject(i).getString("description"));

                }
            }
        }
        catch (JSONException e) {
            Log.d("Not found","QQ");
            e.printStackTrace();
        }
        return output2;
    }
    public ArrayList<String> json6(String input,String classes){
        ArrayList <String> output2 = new ArrayList<>();
        try {

            JSONArray inside_arr = new JSONArray(input);
            Log.d("12323",""+inside_arr.length());
            for(int i=0 ; i < inside_arr.length();i++){
                if(inside_arr.getJSONObject(i).getString("badge_type").equals(classes)){
                    output2.add(inside_arr.getJSONObject(i).getString("badge_id"));

                }
            }
        }
        catch (JSONException e) {
            Log.d("Not found","QQ");
            e.printStackTrace();
        }
        return output2;
    }
    public String getUserid(){
        SharedPreferences b = getSharedPreferences("DATA",0);
        String a = b.getString("ID","");
        return a;
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
            this.finish();
        }

        return false;

    }
}
