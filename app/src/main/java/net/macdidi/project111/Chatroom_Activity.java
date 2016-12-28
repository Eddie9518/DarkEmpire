package net.macdidi.project111;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.drafts.Draft_17;
import org.java_websocket.handshake.ServerHandshake;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.ProtocolException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;


/**
 * Created by Eddie84 on 2016/12/14.
 */

public class Chatroom_Activity extends AppCompatActivity {
    private WebSocketClient client;
    private String teststr;
    private String myurl = "http://140.119.163.40:9000/WebsocketTest/player/json4/list";
    private String socketurl ="ws://140.119.163.40:9000/WebsocketTest/ws?uid=";
    private String uid;
    private String input;
    private EditText enter_content;
    private Button press;
    private String text;
    private String name;
    private ArrayList<String> content;
    private ArrayList<String> towholist;
    private String enter_towho;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chatroom);
        java.lang.System.setProperty("java.net.preferIPv6Addresses","false");
        SharedPreferences b = getSharedPreferences("DATA",0);
        String id = b.getString("ID","");
        final ListView lv = (ListView)findViewById(R.id.show_chat);
        content = new ArrayList<String>();
        press = (Button)findViewById(R.id.enter_chat);
        enter_content = (EditText)findViewById(R.id.chat_content);
        enter_towho="1";
        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(
                this,
                android.R.layout.simple_list_item_1,
                content);
        lv.setAdapter(arrayAdapter);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Log.d("showchat",content.get(i)+"聊天室7起來");
            }
        });
        //String [] wholist = new String[]{"所有人", "群組", "密語"};
        towholist = new ArrayList<String>();
        towholist.add("所有人");
        towholist.add("群組");
        towholist.add("密語");
        ArrayAdapter<String> towhoAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_dropdown_item,towholist);
        final Spinner towho =(Spinner)findViewById(R.id.spinner);
        towho.setAdapter(towhoAdapter);
        towho.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                switch(position){
                    case 0:
                        Log.d("123",towholist.get(position));
                        break;
                    case 1:
                        Log.d("123",towholist.get(position));
                        towho_alert();
                        break;
                    case 2:
                        Log.d("123",towholist.get(position));
                        towho_alert();
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                    Log.d("no selected","??");
            }
        });

        try {
            String a = Http_Get.httpget(myurl);
            JSONArray object_arr = new JSONArray(a);
            for(int i =0;i<object_arr.length();i++){
                String find_id = object_arr.getJSONObject(i).getString("playerid");
                if(find_id.equals(id)){
                    uid = object_arr.getJSONObject(i).getString("uid");
                }
            }
        } catch (ProtocolException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        final String realurl = socketurl + uid;
        final String chatrecord = "http://140.119.163.40:9000/WebsocketTest/msglist/app/"+ uid +"/100";
        try {
            String a = Http_Get.httpget(chatrecord);
            JSONArray object_arr = new JSONArray(a);
            for(int i =object_arr.length()-1;i>=0;i--){
                String h_name = object_arr.getJSONObject(i).getString("fromName");
                String h_text = object_arr.getJSONObject(i).getString("text");
                content.add(h_name +" : "+h_text);
            }
            arrayAdapter.notifyDataSetChanged();
        } catch (ProtocolException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        try {
            client = new WebSocketClient(new URI(realurl), new Draft_17()) {
                @Override
                public void onOpen(ServerHandshake handshakedata) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Log.d("有連線",realurl);
//                    Log.d("有連線","222");
                        }
                    });
                }

                @Override
                public void onMessage(String message) {
                    final String aa = message;
                    try {
                        JSONObject obj = new JSONObject(aa);
                        name = obj.getString("fromName");
                        text = obj.getString("text");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Log.d("有訊息",aa);
                            Log.d("顯示名字", name);
                            content.add(name+" : "+text);
                            Log.d("lalalal",""+content.toString());
                            arrayAdapter.notifyDataSetChanged();
                            lv.setSelection(content.size()-1);
                        }
                    });
                    Log.d("有訊息", aa);
                }

                @Override
                public void onClose(int code, String reason, boolean remote) {
                    Log.d("onclose()","22");
                    Log.d("斷開:",reason);
                }

                @Override
                public void onError(Exception ex) {
                    Log.d("Error",ex.toString());
                }

            };
            client.connect();

        } catch (URISyntaxException e) {
            Log.d("connect error:",e.toString());
        }


        try{
            press.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(client != null) {
                        input = enter_content.getText().toString();
                        teststr = "{\"fromuid\":" + uid + ",\"fromName\":\"我\",\"touid\":" + enter_towho +",\"text\":" + "\"" + input + "\"}";
                        Log.d("777", teststr);
                        client.send(teststr);
                        enter_content.setText("");
                    }
                }
            });
        }
        catch (Exception e){
            Log.d("press.setOnClickListener exception: ",e.toString());
        }
        lv.setSelection(content.size()-1);

    }
    //左下角按了返回後結束activity
    public boolean onKeyDown(int keyCode, KeyEvent event)
    {
        if (keyCode == KeyEvent.KEYCODE_BACK )
        {
         client.close();
         this.finish();
        }

        return false;

    }
    //左上角按了返回後結束activity
    public boolean onOptionsItemSelected(MenuItem item)
    {
        // TODO Auto-generated method stub
        if(item.getItemId() == android.R.id.home)
        {
            client.close();
            this.finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    public void towho_alert(){
        LayoutInflater inflater = LayoutInflater.from(Chatroom_Activity.this);
        View alert_view = inflater.inflate(R.layout.activity_towho,null);
        final AlertDialog.Builder editDialog = new AlertDialog.Builder(Chatroom_Activity.this);
        final EditText who = (EditText)alert_view.findViewById(R.id.index_towho);
        who.setHint("輸入對象");
        editDialog.setTitle("密語 : ");
        editDialog.setView(alert_view);
        final AlertDialog dialog = editDialog.create();
        editDialog.setPositiveButton("確定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                enter_towho = who.getText().toString();
                dialog.dismiss();
            }
        }).setNegativeButton("取消",new DialogInterface.OnClickListener(){

            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialog.dismiss();
            }
        }).show();


    }
}
