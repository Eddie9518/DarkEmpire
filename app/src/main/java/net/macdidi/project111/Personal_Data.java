package net.macdidi.project111;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.ProtocolException;

/**
 * Created by Eddie84 on 2016/10/13.
 */
public class Personal_Data extends AppCompatActivity {
    private String Name;
    private String Email;
    private String myurl ="http://140.119.163.40:8080/DarkEmpire/app/ver1.0/user/";
    private String gamingurl = "http://140.119.163.40:8080/DarkEmpire/app/ver1.0/totalRecord/list/";
    private String forceurl="http://140.119.163.40:8080/DarkEmpire/app/ver1.0/getForceUser";
    private String Camp;
    private String Level;
    private String Patrol;
    private String Pure;
    private TextView PersonID;
    private TextView PersonEmail;
    private TextView PersonMana;
    private TextView PersonLev;
    private TextView Personptrl;
    private TextView Personpure;
    private ImageView User_frame;
    private String mana;
    private Button Editinf;
    private String a;
    private String Forceuser;
    private TextView PersonTribe;
//    private Dialog EditView;
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.user_data);
        setContentView(R.layout.activity_userdata);
        SharedPreferences b = getSharedPreferences("DATA",0);
        a = b.getString("ID","");
        PersonID = (TextView)findViewById(R.id.personal_id);
        PersonEmail=(TextView)findViewById(R.id.personal_email);
        PersonMana=(TextView)findViewById(R.id.personal_mana);
        PersonLev=(TextView)findViewById(R.id.personal_lev);
        Personptrl = (TextView)findViewById(R.id.personal_patrol);
        Personpure = (TextView)findViewById(R.id.personal_pure);
        Editinf =(Button)findViewById(R.id.edit_person_inf);
        PersonTribe = (TextView)findViewById(R.id.show_tribe);
        User_frame = (ImageView)findViewById(R.id.user_frame);
        int resID1 = getResources().getIdentifier("frame", "drawable", Personal_Data.this.getPackageName());
        BitmapFactory.Options frameoptions = new BitmapFactory.Options();
        frameoptions.inSampleSize = 2;
        Bitmap userframe = BitmapFactory.decodeResource(getResources(), resID1, frameoptions);
        User_frame.setImageBitmap(userframe);
        RelativeLayout rl = (RelativeLayout)findViewById(R.id.viewobj);
        try {
            String game_result = Http_Get.httpget(gamingurl+a);
            JSONObject j_object = new JSONObject(game_result);
            Patrol = j_object.getString("count_checkin");
            Pure = j_object.getString("count_purify");
            mana = j_object.getString("mana_now");
        } catch (ProtocolException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            String result2 = Http_Get.httpget(myurl+a);
            json2(result2);

            PersonID.setText("玩家暱稱 : "+Name);
            PersonEmail.setText("玩家信箱 : \n"+Email);
            PersonMana.setText("馬納數值 : "+mana);
            PersonLev.setText("遊戲等級 : "+Level);
            Personptrl.setText("累積巡邏次數 :"+Patrol);
            Personpure.setText("成功淨化神殿次數 :"+Pure);
        } catch (ProtocolException e) {
            e.printStackTrace();
        }
        Editinf.setOnClickListener(new Button.OnClickListener(){


            @Override
            public void onClick(View view) {
                LayoutInflater inflater = LayoutInflater.from(Personal_Data.this);
                View alert_view = inflater.inflate(R.layout.user_edit,null);

                final AlertDialog.Builder editDialog = new AlertDialog.Builder(Personal_Data.this);
                editDialog.setTitle("變更資料");
                editDialog.setView(alert_view);


                final EditText edited_name=(EditText)alert_view.findViewById(R.id.edit_person_name);
                final EditText edited_email=(EditText)alert_view.findViewById(R.id.edit_person_email);
                edited_name.setText(Name);
                edited_email.setText(Email);
                Button editOK =(Button)alert_view.findViewById(R.id.btnOK);
                Button editCancel = (Button)alert_view.findViewById(R.id.btnCancel);
                final AlertDialog dialog = editDialog.create();
                dialog.show();
                editOK.setOnClickListener(new View.OnClickListener(){


                    @Override
                    public void onClick(View view) {
                        Name = edited_name.getText().toString();
                        PersonID.setText("玩家暱稱 : "+Name);
                        Email = edited_email.getText().toString();
                        PersonEmail.setText("玩家信箱 : \n"+Email);
                        dialog.cancel();
                        String postString="http://140.119.163.40:8080/DarkEmpire/app/ver1.0/user/"+a+"?user_name="+Name+"&email="+Email;
                        try {
                            Http_Post.httppost(postString);
                        } catch (ProtocolException e) {
                            e.printStackTrace();
                        }

                    }
                });
                editCancel.setOnClickListener(new View.OnClickListener(){


                    @Override
                    public void onClick(View view) {
                        dialog.cancel();
                    }
                });
            }

        });
        try {
            String abc = Http_Get.httpget(forceurl);
            JSONArray object_arr = new JSONArray(abc);
            Boolean haveforce = false;
            for(int i =0 ; i < object_arr.length(); i++){
                String f_userid = object_arr.getJSONObject(i).getString("user_id");
                if(f_userid.equals(a)){ //如果user是有超原力的話
                    String user_rf = object_arr.getJSONObject(i).getString("releaseforce");
                    user_rf = user_rf.replace("\n","").replace(" ","");
                    if(user_rf.equals("0")) {
                        haveforce = true;
                    }
                }

            }
            if(haveforce){
                ImageView aaa = new ImageView(this);
                RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(180,180);
                params.addRule(RelativeLayout.BELOW,R.id.user_frame);
//                params.addRule(RelativeLayout.CENTER_HORIZONTAL);
                int distance = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 50, this.getResources().getDisplayMetrics());
                params.leftMargin = distance ;
                int resID=1;
//                if(Camp.equals("1")){
                if(Camp.equals("2")) {
                     PersonTribe.setText("席奈族");
                     resID = getResources().getIdentifier("force_blue", "drawable", getPackageName());

                }
                else if(Camp.equals("1")){
                    PersonTribe.setText("安塔雅族");
                     resID = getResources().getIdentifier("force_red", "drawable", getPackageName());
                }
//                else if(Camp.equals("2")){
//                    resID = getResources().getIdentifier("force_red" , "drawable",getPackageName());
//                }
//                else{
//                    Log.d("123",Camp);
//                }
                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inSampleSize=2;
                Bitmap frame = BitmapFactory.decodeResource(getResources(),resID,options);
//                    imgframe.setImageBitmap(frame);
                aaa.setImageBitmap(frame);
                rl.addView(aaa, params);
                TextView  bbb = new TextView(this);
                RelativeLayout.LayoutParams params2 = new RelativeLayout.LayoutParams(
                        ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                int distance2 = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 140, this.getResources().getDisplayMetrics());
                int distance3 = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 25, this.getResources().getDisplayMetrics());
                params2.leftMargin = distance2;
                params2.topMargin = distance3;
                params2.addRule(RelativeLayout.BELOW,R.id.user_frame);
                bbb.setText("您是超原力使者");
                bbb.setTextSize(25);
                rl.addView(bbb,params2);
            }
            else{
                if(Camp.equals("1")){
                    PersonTribe.setText("安塔雅族");
                }
                else if(Camp.equals("2")){
                    PersonTribe.setText("席奈族");
                }
                TextView aaa = new TextView(this);
                RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                        ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                params.addRule(RelativeLayout.BELOW,R.id.user_frame);
                params.addRule(RelativeLayout.CENTER_HORIZONTAL);
                int distance4 = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,25,this.getResources().getDisplayMetrics());
                params.topMargin =distance4;
                aaa.setText("您還不是超原力使者");
                aaa.setTextSize(25);
                rl.addView(aaa, params);
            }
        } catch (ProtocolException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
    public void json2(String result){
        try {
                JSONObject object2 = new JSONObject(result);
                Name = object2.getString("user_name");
                Email = object2.getString("email");
                String lvcheck = Http_Get.httpget("http://140.119.163.40:8080/DarkEmpire/app/ver1.0/checkLevel/"+a);
                JSONObject j_obj = new JSONObject(lvcheck);
                Level = j_obj.getString("level");
                Camp = object2.getString("camp");
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (ProtocolException e) {
            e.printStackTrace();
        }
    }
    //左上角按了返回後結束activity
    public boolean onOptionsItemSelected(MenuItem item)
    {
        // TODO Auto-generated method stub
        if(item.getItemId() == android.R.id.home)
        {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    //左下角按了返回後結束activity
    public boolean onKeyDown(int keyCode, KeyEvent event)
    {
        if (keyCode == KeyEvent.KEYCODE_BACK )
        {
            finish();
        }

        return false;

    }



}
