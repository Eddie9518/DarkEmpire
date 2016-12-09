package net.macdidi.project111;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.ProtocolException;

/**
 * Created by Eddie84 on 2016/10/13.
 */
public class Personal_Data extends AppCompatActivity {
    private String Name;
    private String StudentID;
    private String Email;
    private String TimeStamp;
    private String myurl ="http://140.119.163.40:8080/DarkEmpire/app/ver1.0/user/";

    private String testing ="http://140.119.163.40:8080/Sample/user/json/16";
    private String Level;
    private String Exp;
    private String Camp;
    private String Votes;
    private TextView PersonID;
    private TextView PersonEmail;
    private TextView PersonMana;
    private TextView PersonLev;
    private TextView PersonExp;
    private String mana;
    private Button Editinf;
    private String a;
//    private Dialog EditView;
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_data);
        SharedPreferences b = getSharedPreferences("DATA",0);
        a = b.getString("ID","");
        mana = ""+21;
        Level=""+0;
        Exp = ""+0;
        PersonID = (TextView)findViewById(R.id.personal_id);
        PersonEmail=(TextView)findViewById(R.id.personal_email);
        PersonMana=(TextView)findViewById(R.id.personal_mana);
        PersonLev=(TextView)findViewById(R.id.personal_lev);
        PersonExp=(TextView)findViewById(R.id.personal_exp);
        Editinf =(Button)findViewById(R.id.edit_person_inf);
        try {
            String result2 = Http_Get.httpget(myurl+a);
//            Log.d("testt",result2);
            json2(result2);
            PersonID.setText(Name);
            PersonEmail.setText(Email);
            PersonMana.setText(mana);
            PersonLev.setText(Level);
            PersonExp.setText(Exp);

        } catch (ProtocolException e) {
            e.printStackTrace();
        }
        Editinf.setOnClickListener(new Button.OnClickListener(){


            @Override
            public void onClick(View view) {
                LayoutInflater inflater = LayoutInflater.from(Personal_Data.this);
                View alert_view = inflater.inflate(R.layout.user_edit,null);

                final AlertDialog.Builder editDialog = new AlertDialog.Builder(Personal_Data.this);
//                EditView = new Dialog(Personal_Data.this);
                editDialog.setTitle("變更資料");
                editDialog.setView(alert_view);
//                EditView.setContentView(R.layout.user_edit);

                final EditText edited_name=(EditText)alert_view.findViewById(R.id.edit_person_name);
                final EditText edited_email=(EditText)alert_view.findViewById(R.id.edit_person_email);
                edited_name.setText(Name);
                edited_email.setText(Email);
                Button editOK =(Button)alert_view.findViewById(R.id.btnOK);
                Button editCancel = (Button)alert_view.findViewById(R.id.btnCancel);
//                editOK.setOnClickListener(editonclick);
//                editCancel.setOnClickListener(editcancelonclick);
//                editDialog.setMessage("請輸入您要變更的暱稱及信箱");
//                final EditText editText = new EditText(Personal_Data.this);
//                editText.setText(Name);
//                editDialog.setView(editText);
//                final EditText editText1 = new EditText(Personal_Data.this);
//                editText1.setText(Email);
//                editDialog.setView(editText1);
                final AlertDialog dialog = editDialog.create();
                dialog.show();
                editOK.setOnClickListener(new View.OnClickListener(){


                    @Override
                    public void onClick(View view) {
                        Name = edited_name.getText().toString();
                        PersonID.setText(Name);
                        Email = edited_email.getText().toString();
                        PersonEmail.setText(Email);
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


    }
    public void json2(String result){
        try {
            String result2 = result.substring(1,result.length()-2);
//            Log.d("testtt",result2);

                JSONObject object2 = new JSONObject(result);
                Name = object2.getString("user_name");
//                StudentID = object2.getString("studentid");
                Email = object2.getString("email");
//                Level = object2.getString("level");
//                Exp = object2.getString("exp");
                Camp = object2.getString("camp");
//                Votes = object2.getString("votes");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }



}
