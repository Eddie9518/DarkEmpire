package net.macdidi.project111;

import android.annotation.SuppressLint;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.Message;
import android.os.StrictMode;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Eddie84 on 2016/9/29.
 */
public class Http_Post extends Service {

    String strTxt=null;
    String postUrl=null;
    String strResult=null;

    public  void Post(String StrTxt, final String PostUrl){
        this.strTxt = StrTxt;
        this.postUrl = PostUrl;

        new Thread(new Runnable() {

            @Override
            public void run() {
                //建立HttpClient物件
                HttpClient httpClient = new DefaultHttpClient();
                //建立一個Post物件，並給予要連線的Url
                HttpPost httpPost = new HttpPost(postUrl);
                //建立一個ArrayList且需是NameValuePair，此ArrayList是用來傳送給Http server端的訊息
                List params = new ArrayList();
                params.add(new BasicNameValuePair("data", strTxt));

                try{
                    //發送Http Request，內容為params，且為UTF8格式
                    httpPost.setEntity(new UrlEncodedFormEntity(params,HTTP.UTF_8));
                    //接收Http Server的回應
                    HttpResponse httpResponse = new DefaultHttpClient().execute(httpPost);
                    //判斷Http Server是否回傳OK(200)
                    if(httpResponse.getStatusLine().getStatusCode() == 200){
                        //將Post回傳的值轉為String，將轉回來的值轉為UTF8，否則若是中文會亂碼
                        strResult = EntityUtils.toString(httpResponse.getEntity(), HTTP.UTF_8);

                        Message msg = Message.obtain();
                        //設定Message的內容
                        msg.what = 123;
                        msg.obj=strResult;
                        //使用MainActivity的static handler來丟Message
                        DataBase.handler.sendMessage(msg);
                    }

                }catch (IOException e) {
                    // Log exception
                    e.printStackTrace();
                }
            }}).start();

    }

    @SuppressLint("NewApi")
    public static String httppost(String url) throws ProtocolException {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        String aa = "";

        URL obj = null;
        try {
            obj = new URL(url);
            HttpURLConnection con = (HttpURLConnection) obj.openConnection();
            con.setRequestMethod("POST");
//            DataOutputStream wr = new DataOutputStream(con.getOutputStream());
//            wr.writeBytes(urlParameters);
//            wr.flush();
//            wr.close();
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
    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }
}