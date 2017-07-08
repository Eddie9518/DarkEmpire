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
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.net.ProtocolException;
import java.util.ArrayList;

/**
 * Created by Eddie84 on 2016/11/10.
 */
public class Tab_Activity extends Fragment {
    private TextView tab_content;
    private String value="";
    private String myurl = "http://140.119.163.40:8080/Spring08/app/badge";
    private String badge_myurl ="http://140.119.163.40:8080/DarkEmpire/app/ver1.0/badge/list";
    private String achieve_url = "http://140.119.163.40:8080/DarkEmpire/app/ver1.0/userBadge/";
    private String result;
    private String arr_result;
    private ArrayList <String> aa;
    private ArrayList <String> des;
    private ArrayList <String> badge_id;
    private String [] havebadge;
    private int resID;
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        value = "";

        try {
            arr_result = Http_Get.httpget(badge_myurl);
        } catch (ProtocolException e) {
            e.printStackTrace();
        }
        Badge_Activity mainActivity = (Badge_Activity) activity;
        aa = mainActivity.json4(arr_result,getTag());
        des = mainActivity.json5(arr_result,getTag());
        badge_id = mainActivity.json6(arr_result,getTag());
        if(aa.size()>0){
            value =""+ aa.size();
        }
        else {
            value = "trytry123";
        }
        achieve_url = achieve_url + mainActivity.getUserid();
        try {
            String a =Http_Get.httpget(achieve_url);
            String b =a.substring(13,a.length()-2);
            havebadge = b.split(",");
        } catch (ProtocolException e) {
            e.printStackTrace();
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
        ListView lv = (ListView)getActivity().findViewById(R.id.badge_content);
        myadapter adapter = new myadapter(aa,getActivity());
        lv.setAdapter(adapter);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                alert_make(aa.get(i),des.get(i));
            }
        });

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
                 holder.image.setImageResource(R.drawable.cleanbadge);
                 //壓縮圖檔
//                 if(getTag().equals("校園尋奇")){
//                     resID = getResources().getIdentifier("badge_campus", "drawable", getActivity().getPackageName());
//                 }
//                 else if(getTag().equals("專家")){
//                     resID = getResources().getIdentifier("badge_expert", "drawable", getActivity().getPackageName());
//                 }
//                 else if(getTag().equals("探索")){
//                     resID = getResources().getIdentifier("badge_search", "drawable", getActivity().getPackageName());
//                 }
////                 int resID1 = getResources().getIdentifier("badge_campus", "drawable", getActivity().getPackageName());
////                 int resID2 = getResources().getIdentifier("badge_expert", "drawable", getActivity().getPackageName());
////                 int resID3 = getResources().getIdentifier("badge_search", "drawable", getActivity().getPackageName());
//                 BitmapFactory.Options options = new BitmapFactory.Options();
//                 options.inSampleSize = 2;
////                 Bitmap b_campus = BitmapFactory.decodeResource(getResources(), resID1,options);
////                 Bitmap b_expert = BitmapFactory.decodeResource(getResources(), resID2,options);
////                 Bitmap b_search = BitmapFactory.decodeResource(getResources(), resID3,options);
//                 Bitmap result = BitmapFactory.decodeResource(getResources(),resID,options);
                 for(int k = 0 ; k < havebadge.length; k++){
                     if(badge_id.get(i).equals(havebadge[k])){
                         if(getTag().equals("校園尋奇")) {
                             holder.image.setImageResource(R.drawable.badge_campus);
//                             holder.image.setImageBitmap(result);
                             break;
                         }
                         else if(getTag().equals("專家")){
                             holder.image.setImageResource(R.drawable.badge_expert);
//                             holder.image.setImageBitmap(result);
                             break;
                         }
                         else if(getTag().equals("探索")){
                             holder.image.setImageResource(R.drawable.badge_search);
//                             holder.image.setImageBitmap(result);
                             break;
                         }
                         else{
                             Log.d("123123",getTag());
                         }
                     }
                 }

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
