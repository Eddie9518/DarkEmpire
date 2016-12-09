package net.macdidi.project111;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.ProtocolException;

/**
 * Created by Eddie84 on 2016/10/13.
 */
public class Login_Activity extends AppCompatActivity {
    private static final String shared_id="ID";
    public static String userid;
    private SharedPreferences settings;
    private static final String data = "DATA";
    private String loginurl="http://140.119.163.40:8080/DarkEmpire/app/login";
    private WebView myBrowser;
//    private SharedPreferences setting2;



    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_web);
        myBrowser = (WebView)findViewById(R.id.login_view);
        myBrowser.getSettings().setJavaScriptEnabled(true);
        myBrowser.getSettings().setDomStorageEnabled(true);
        myBrowser.addJavascriptInterface(new Handler(),"handler");
//        myBrowser.getSettings().setDomStorageEnabled(true);

        myBrowser.loadUrl(loginurl);

        myBrowser.setWebViewClient(new WebViewClient(){
            @Override
            public void onPageStarted(WebView view, String url2, Bitmap favicon){

                if(url2.length()>56){
                    if(url2.substring(0,54).equals("http://140.119.163.40:8080/DarkEmpire/app/authenticate")){
                        String json = "";
                        try{
                            json = Http_Get.httpget(url2);
                        } catch (ProtocolException e) {
                            e.printStackTrace();
                        }
                        try{
//                            userid = new JSONArray(json).getJSONObject(0).getString("user_id");
                            userid = new JSONObject(json).getString("user_id");
                            saveData(userid);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
//                        try {
//                            FileWriter fw = new FileWriter("/sdcard/output.txt", false);
//                            fw.write(userid);
//                        } catch (IOException e) {
//                            e.printStackTrace();
//                        }
                        Intent intent = new Intent();
                        intent.setClass(Login_Activity.this,Story_Activity.class);
                        Login_Activity.this.finish();
                        startActivity(intent);

                    }
                }


            }



        });
    }

    public  String readData(){
        settings = getSharedPreferences(data,0);
        String a =settings.getString("ID","");
        return a;
    }
    public void saveData(String id){
        settings = getSharedPreferences(data,0);
        settings.edit()
                .putString("ID",id)
                .commit();
    }
}

