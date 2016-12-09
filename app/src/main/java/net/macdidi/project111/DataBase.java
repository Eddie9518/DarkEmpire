package net.macdidi.project111;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.ProtocolException;

/**
 * Created by Eddie84 on 2016/9/21.
 */
public class DataBase extends Activity {
    //設定HTTP Get & Post要連線的Url
    private String postUrl = "http://140.119.163.40:8080/Sample/user/test/add";
    private String getUrl = "http://140.119.163.40:8080/Sample/user/json/list";

    private EditText txtMessage;
    private Button postBtn;
    private Button getBtn;
    private TextView getMessage;
    private EditText txtUserID;
    private EditText txtName;
    private EditText txtEmail;
    private String msg = null;  //存放要Post的訊息
    private String result;  //存放Post回傳值
    private int [] ID;
    private String [] Name;
    private int [] Userid;
    private String [] Email;
    private String [] TimeStamp;
    private ListView listv;
    private ListView listv2;

    Http_Post HP;
    Http_Get HG;

    static Handler handler; //宣告成static讓service可以直接使用

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_list);

        HG = new Http_Get();
        HP = new Http_Post();

//        txtMessage = (EditText) findViewById(R.id.txt_message);
//        postBtn = (Button) findViewById(R.id.http_post_btn);
//        getBtn = (Button) findViewById(R.id.http_get_btn);
//        getMessage = (TextView)findViewById(R.id.get_text);
//        txtUserID = (EditText) findViewById(R.id.enter_user_id);
//        txtName = (EditText)findViewById(R.id.enter_name);
//        txtEmail = (EditText)findViewById(R.id.enter_email);
        listv=(ListView)findViewById(R.id.listView2);
        listv2=(ListView)findViewById(R.id.listView);
//        ArrayAdapter<Integer> adapter2 = new ArrayAdapter<Integer>(this,android.R.layout.simple_list_item_1,ID);
//        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.activity_list_item,Name);

//        listv.setAdapter(mAdapter);
        //讓多個Button共用一個Listener，在Listener中再去設定各按鈕要做的事
//        getBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//                String aa =HG.Get(getUrl);
//                getMessage.setText(aa);
//                Toast.makeText(DataBase.this,aa,Toast.LENGTH_LONG).show();
                try {
                    String result = HG.httpget(getUrl);
//                    getMessage.setText (result);
                    json(result);

                } catch (ProtocolException e) {
                    e.printStackTrace();
                }
        ListAdapter mAdapter = new ArrayAdapter<>(this,android.R.layout.simple_list_item_1,Name);
        ArrayAdapter kAdapter = new ArrayAdapter<>(this,android.R.layout.simple_list_item_1,Email);
        listv.setAdapter(mAdapter);
        listv2.setAdapter(kAdapter);
//            }
//        });
//        postBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if (txtMessage != null) {
////                    if(txtUserID !=null && txtName !=null && txtEmail !=null){
//                    //取得EditText的內容
//                    msg = txtMessage.getEditableText().toString();
//                    HP.Post(msg,postUrl);
//                }
//            }
//        });
//
//
//        //接收service傳出Post的到的回傳訊息，並透過Toast顯示出來
//        handler = new Handler(){
//            public void handleMessage(Message msg){
//                switch (msg.what){
//                    case 123:
//                        String ss = (String)msg.obj;
//                        Toast.makeText(DataBase.this, ss,Toast.LENGTH_LONG).show();
//                        break;
//                }
//            }
//        };
    }
    //依照按下的按鈕去做相對應的任務
//    public void onClick(View v){
//        switch (v.getId()){
//            case R.id.http_get_btn:
//                HG.Get(getUrl);
//                break;
//            case R.id.http_post_btn:
//                if (txtMessage != null) {
//                    //取得EditText的內容
//                    msg = txtMessage.getEditableText().toString();
//                    HP.Post(msg,postUrl);
//                }
//                break;
//        }
//    }
//    @SuppressLint("NewApi")
//    public static String httpget(String url) throws ProtocolException {
//        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
//        StrictMode.setThreadPolicy(policy);
//        String aa = "";
//
//        URL obj = null;
//        try {
//            obj = new URL(url);
//            HttpURLConnection con = (HttpURLConnection) obj.openConnection();
//            con.setRequestMethod("GET");
//            BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream(), "UTF-8"));
//            StringBuilder sb = new StringBuilder();
//            String line;
//            while ((line = br.readLine()) != null) {
//                sb.append(line + "\n");
//            }
//            aa = sb.toString();
//
//
//        } catch (MalformedURLException e) {
//            e.printStackTrace();
//        } catch (UnsupportedEncodingException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
//
//        return aa;
//    }
    public void json(String result){
        try {
            JSONArray jsonArray = new JSONArray(result);
            ID = new int [jsonArray.length()];
            Name = new String [jsonArray.length()];
            Userid= new int [jsonArray.length()];
            Email = new String[jsonArray.length()];
            TimeStamp = new String [jsonArray.length()];
            for(int i=0;i < jsonArray.length();i++){
                JSONObject object2 = jsonArray.getJSONObject(i);
                ID[i]=object2.getInt("id");
//                Log.d("DataBase", "ID =" +ID[i]);
                Name[i] = object2.getString("name");
//                Log.d("DataBase", "name =" + Name[i]);
                Userid[i] = object2.getInt("userid");
//                Log.d("DataBase", "userid =" +Userid[i]);
                Email[i] = object2.getString("email");
//                Log.d("DataBase", "email =" +Email[i]);
                TimeStamp[i] = object2.getString("timestamp");
//                Log.d("DataBase", "timestamp ="+TimeStamp[i]);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}

