package net.macdidi.project111;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTabHost;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.ProtocolException;
import java.util.ArrayList;

/**
 * Created by Eddie84 on 2016/11/1.
 */
public class Badge_Activity extends FragmentActivity {

    private String myurl = "http://140.119.163.40:8080/Spring08/app/badge";
    private String result;
    private String arr_result;
    private String  clsic;
    private String [] carry;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tabs);
        // 將 TextView 加入到 LinearLayout 中
        FragmentTabHost tabHost = (FragmentTabHost)findViewById(android.R.id.tabhost);
        tabHost.setup(this, getSupportFragmentManager(), R.id.realtabcontent);
        //1

        try {
            result = Http_Get.httpget(myurl);
            JSONObject first_j = new JSONObject(result);
            arr_result = first_j.getString("badge");
            clsic = first_j.getString("classification");
            carry = clsic.split(",");
            String aa = "[1131313]";
            Log.d("12323224",""+carry[0] +"&&"+carry[1]+"&&"+carry[2]);
            Log.d("22222323",aa);
            String [] result2 = new String [carry.length];
            for(int i = 0 ; i< carry.length;i++){
                    if(i==0 ) {
                        result2[i] = carry[i].substring(2, carry[i].length() - 1);
                    }
                    else if (i==(carry.length-1)){
                        result2[i] = carry[i].substring(1, carry[i].length() - 2);

                    }
                else {
                        result2[i] = carry[i].substring(1,carry[i].length()-1);
                    }
                tabHost.addTab(tabHost.newTabSpec(result2[i])
                                .setIndicator(result2[i]),
                        Tab_Activity.class,
                        null);

                }


//            clsic = first_j.getString("classification");
//            Log.d("123",clsic[0]);
           // json4(arr_result);
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
                if(inside_arr.getJSONObject(i).getString("classification").equals(classes)){
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
                if(inside_arr.getJSONObject(i).getString("classification").equals(classes)){
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
}
