package net.macdidi.project111;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;

import java.net.ProtocolException;
import java.util.ArrayList;

/**
 * Created by Eddie84 on 2016/11/30.
 */
public class Game_List extends AppCompatActivity {
    public static String open_topic;
    public static String open_id;
    private ListView lv;
    private ArrayList<String> place_list = new ArrayList<>();
    private ArrayList<String> id_list = new ArrayList<>();
    private String myurl="http://140.119.163.40:8080/DarkEmpire/app/ver1.0/place/list";
    private String a;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gamelist);

        lv = (ListView) findViewById(R.id.listing);
        //Log.d("12323232",Game_Activity.pid);
        json5();
        //Log.d("5555",""+place_list.size());
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(
                this,
                android.R.layout.simple_list_item_1,
                place_list );

        lv.setAdapter(arrayAdapter);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView arg0, View arg1, int arg2,
                                    long arg3) {
                // TODO Auto-generated method stub
//                ListView listView = (ListView) arg0;
//                Toast.makeText(
//                        Game_List.this,
//                        "ID：" + arg3 +
//                                "   選單文字："+ listView.getItemAtPosition(arg2).toString(),
//                        Toast.LENGTH_LONG).show();
                open_topic = place_list.get(arg2);
                open_id = id_list.get(arg2);
                Intent intent = new Intent();
                intent.setClass(Game_List.this,Gaming.class);
                startActivity(intent);
            }
        });
    }

    public void json5(){
        try {
            a=Http_Get.httpget(myurl);
            JSONArray object_arr = new JSONArray(a);
            for(int i=0; i<object_arr.length();i++) {
                String m_id = object_arr.getJSONObject(i).getString("main_id");
                String p_id = object_arr.getJSONObject(i).getString("place_id");
                String p_name = object_arr.getJSONObject(i).getString("name");
                if(p_id.equals(Game_Activity.pid)){
                    place_list.add(p_name);
                    id_list.add(p_id);
                }
                if(m_id.equals(Game_Activity.pid)) {
                    place_list.add(p_name);
                    id_list.add(p_id);
                }

            }
        } catch (ProtocolException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }


    }
}
