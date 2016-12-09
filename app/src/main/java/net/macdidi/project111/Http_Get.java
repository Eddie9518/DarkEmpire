package net.macdidi.project111;

import android.annotation.SuppressLint;
import android.os.StrictMode;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

/**
 * Created by Eddie84 on 2016/9/29.
 */
public class Http_Get {
//    private String getUrl;
//    private int[] ID;
//    private String[] Name;
//    private int[] Userid;
//    private String[] Email;
//    private int[] TimeStamp;
//    private String get_result;

    //    public String Get(String url){
//        this.getUrl = url;
//        new Thread(new Runnable() {
//
//            @Override
//            public void run() {
//                //建立HttpClient物件
//                HttpClient httpClient = new DefaultHttpClient();
//                //建立Http Get，並給予要連線的Url
//                HttpGet get = new HttpGet(getUrl);
//                //透過Get跟Http Server連線並取回傳值，並將傳值透過Log顯示出來
//                try {
//                    HttpResponse response = httpClient.execute(get);
//                    HttpEntity resEntity = response.getEntity();
////                    Log.d("Response of GET request", EntityUtils.toString(resEntity));
//                    get_result = EntityUtils.toString(resEntity, HTTP.UTF_8);
//                } catch (ClientProtocolException e) {
//                    // TODO Auto-generated catch block
//                    e.printStackTrace();
//                } catch (IOException e) {
//                    // TODO Auto-generated catch block
//                    e.printStackTrace();
//                }
//            }
//        }).start();
//        if(get_result!=null) {
//            try {
//////                JSONObject object = (JSONObject) new JSONTokener(get_result).nextValue();
//////                ID = object.getInt("id");
//////                Log.d("DataBase", "ID =" + ID);
//////                Name = object.getString("name");
//////                Log.d("DataBase", "name =" + Name);
//////                Userid = object.getInt("userid");
//////                Log.d("DataBase", "userid =" +Userid);
//////                Email = object.getString("email");
//////                Log.d("DataBase", "email =" +Email);
//////                TimeStamp = object.getInt("timestamp");
//////                Log.d("DataBase", "timestamp ="+TimeStamp);
//////                JSONArray jsonArray = (JSONArray) new JSONTokener(get_result).nextValue();
////
//                 JSONArray jsonArray = new JSONArray(get_result);
//
//                for(int i=0;i < jsonArray.length();i++){
//                    JSONObject object2 = jsonArray.getJSONObject(i);
//                    ID[i] = object2.getInt("id");
//                    Log.d("DataBase", "ID =" +ID);
//                    Name[i] = object2.getString("name");
//                    Log.d("DataBase", "name =" + Name);
//                    Userid[i] = object2.getInt("userid");
//                    Log.d("DataBase", "userid =" +Userid);
//                    Email[i] = object2.getString("email");
//                    Log.d("DataBase", "email =" +Email);
//                    TimeStamp[i] = object2.getInt("timestamp");
//                    Log.d("DataBase", "timestamp ="+TimeStamp);
//                    Log.d("test123","123");
//                    Log.d("test",object2.getString("id"));
//                    Log.d("test",jsonArray.length()+"");
//                }
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
//        }
//        return get_result;
//    }
//
//    @Override
//    public IBinder onBind(Intent intent) {
//        // TODO: Return the communication channel to the service.
//        throw new UnsupportedOperationException("Not yet implemented");
//    }
    @SuppressLint("NewApi")
    public static String httpget(String url) throws ProtocolException {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        String aa = "";

        URL obj = null;
        try {
            obj = new URL(url);
            HttpURLConnection con = (HttpURLConnection) obj.openConnection();
            con.setRequestMethod("GET");
            BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream(), "UTF-8"));
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = br.readLine()) != null) {
                sb.append(line + "\n");
            }
            aa = sb.toString();


        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return aa;
    }
}