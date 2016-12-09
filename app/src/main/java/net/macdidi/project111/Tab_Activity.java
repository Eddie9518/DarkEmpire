package net.macdidi.project111;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.ProtocolException;
import java.util.ArrayList;

/**
 * Created by Eddie84 on 2016/11/10.
 */
public class Tab_Activity extends Fragment {
    private TextView tab_content;
    private String value="";
    private String myurl = "http://140.119.163.40:8080/Spring08/app/badge";
    private String result;
    private String arr_result;
    private ArrayList <String> aa;
    private ArrayList <String> des;
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        value = "";

        try {
            result = Http_Get.httpget(myurl);
            JSONObject first_j = new JSONObject(result);
//            first_j = new JSONObject(result);
            arr_result = first_j.getString("badge");
        } catch (ProtocolException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Badge_Activity mainActivity = (Badge_Activity) activity;
        aa = mainActivity.json4(arr_result,getTag());
        des = mainActivity.json5(arr_result,getTag());
        if(aa.size()>0){
            value =""+ aa.size();
        }
        else {
            value = "trytry123";
        }


    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        MediaPlayer mediaPlayer01;
        mediaPlayer01 = MediaPlayer.create(getActivity(), R.raw.m002);
        mediaPlayer01.start();
        return inflater.inflate(R.layout.frg_apple, container, false);
    }
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        //TextView txtResult = (TextView) this.getView().findViewById(R.id.textView1);
        //txtResult.setText(value);
//        tab_content = (TextView)this.getView().findViewById(R.id.badge_Content);
//        start();
//        tab_content.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Log.d("222",""+tab_content.length());
//            }
//        });
        ListView lv = (ListView)getActivity().findViewById(R.id.badge_content);
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(
                getActivity(),
                android.R.layout.simple_list_item_1,
                aa );
        myadapter adapter = new myadapter(aa,getActivity());
        lv.setAdapter(adapter);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                alert_make(aa.get(i),des.get(i));
            }
        });

    }
    public void start(){

        int count = 0;
        while (aa.size() > count) {
            tab_content.append("\n"+aa.get(count).toString());

            count++;

        }
    }
     public class myadapter extends BaseAdapter{

        ArrayList<String> bb;
         private LayoutInflater mInflater = null;
         public myadapter(ArrayList<String> result, Context context) {
             this.mInflater = LayoutInflater.from(context);
             bb=result;
         }
             @Override
             public int getCount () {
                 return bb.size();
             }

             @Override
             public Object getItem ( int i){
                 return i;
             }

             @Override
             public long getItemId ( int i){
                 return i;
             }
             class Holder {
                 ImageView image;
                 TextView text;
             }
             @Override
             public View getView ( int i, View convertView, ViewGroup parent){
                 View v = convertView;
                 Holder holder;
                 if (v == null) {
                     v = mInflater.inflate(R.layout.list_adapter, null);
                     holder = new Holder();
                     holder.image = (ImageView) v.findViewById(R.id.image_list);
                     holder.text = (TextView) v.findViewById(R.id.image_text);

                     v.setTag(holder);
                 } else {
                     holder = (Holder) v.getTag();
                 }
                 holder.text.setText(bb.get(i));
                 return v;
             }

    }
    public void alert_make(String title,String describe){
        Log.d("yess","yyy");
         new AlertDialog.Builder(getActivity())
                .setTitle(title)
                 .setMessage(describe)
                 .setPositiveButton("我知道了",new DialogInterface.OnClickListener() {
                     public void onClick(DialogInterface dialog,
                                         int whichButton) {

                                /* User clicked OK so do some stuff */
                     }
                 }).create().show();
        Log.d("no","nnn");

    }

}
