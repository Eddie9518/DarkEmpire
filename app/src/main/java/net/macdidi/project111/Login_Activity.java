package net.macdidi.project111;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.ProtocolException;

/**
 * Created by Eddie84 on 2016/10/13.
 */
public class Login_Activity extends AppCompatActivity {
    public static String userid;
    // 令一個資料庫的變數
    private SharedPreferences settings;
    private static final String data = "DATA";
    private String loginurl="http://140.119.163.40:8080/DarkEmpire/app/login";
    private WebView myBrowser;
    private String inf = "http://140.119.163.40:8080/DarkEmpire/app/ver1.0/user/";
    private String camp;
    //紀錄api需要的變數
    private String recordurl ="http://140.119.163.40:8080/DarkEmpire/app/ver1.0/storeAction/";



    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_web);
        myBrowser = (WebView)findViewById(R.id.login_view);
        myBrowser.getSettings().setJavaScriptEnabled(true);
        myBrowser.getSettings().setDomStorageEnabled(true);
        myBrowser.addJavascriptInterface(new Handler(),"handler");
        //做一個webview 把login url灌進去
        myBrowser.loadUrl(loginurl);
        myBrowser.setWebViewClient(new WebViewClient(){
            @Override
            public void onPageStarted(WebView view, String url2, Bitmap favicon){
                //比對登入網址的前56碼 若網址正確則進行json格式的比對
                if(url2.length()>56){
                    if(url2.substring(0,54).equals("http://140.119.163.40:8080/DarkEmpire/app/authenticate")) {
                        String json = "";
                        try {
                            json = Http_Get.httpget(url2);
                        } catch (ProtocolException e) {
                            e.printStackTrace();
                        }
                        try {
                            userid = new JSONObject(json).getString("user_id");
                            int idcheck = Integer.valueOf(userid);
                            if (idcheck < 1000) {//若小於1000則為無效ID
                                Toast.makeText(Login_Activity.this, "無效的ID 登入失敗", Toast.LENGTH_SHORT).show();
                                Login_Activity.this.finish();
                            } else {
                                saveData(userid);
                                String initurl = "http://140.119.163.40:8080/DarkEmpire/app/ver1.0/init/" + userid;
                                Http_Get.httpget(initurl);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        } catch (ProtocolException e) {
                            e.printStackTrace();
                        }
                        settings = getSharedPreferences(data, 0);
                        //查詢user是否已有登記camp 選擇跳到的activity
                        //address 為 get camp 的網址
                        String address = inf + userid;
                        if (Integer.valueOf(userid) >= 1000) {//為有效ＩＤ的話才執行
                            try {
                                //紀錄登入次數
                                String recent_longi = settings.getString("longi", "");
                                String recent_latit = settings.getString("latit", "");
                                String loginrecord = recordurl + userid + "/8/" + recent_longi + "/" + recent_latit + "/";
                                Http_Get.httpget(loginrecord);
                                Log.d("loginrecord", loginrecord);
                                String aa = Http_Get.httpget(address);
                                camp = new JSONObject(aa).getString("camp");
                                if (camp.equals("0")) {
                                    settings.edit().putString("Firsttime", "true").apply();
                                    Intent intent = new Intent();
                                    intent.setClass(Login_Activity.this, Story_Activity.class);
                                    Login_Activity.this.finish();
                                    startActivity(intent);
                                } else {
                                    settings.edit().putString("Firsttime", "false").apply();
                                    Intent intent = new Intent();
                                    intent.setClass(Login_Activity.this, Menu.class);
                                    Login_Activity.this.finish();
                                    startActivity(intent);
                                }
                            } catch (ProtocolException e) {
                                e.printStackTrace();
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }


            }



        });
    }
    //一個function 登入後把id寫入設定
    public void saveData(String id){
        settings = getSharedPreferences(data,0);
        settings.edit()
                .putString("ID",id)
                .apply();
    }

}

