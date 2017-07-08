package net.macdidi.project111;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.net.ProtocolException;

/**
 * Created by Eddie84 on 2017/2/15.
 */
public class Buypotion_Activity extends AppCompatActivity {
    private static String testing;
    private String a;
    private Button red_add;
    private Button red_minus;
    private Button blue_add;
    private Button blue_minus;
    private Button yellow_add;
    private Button yellow_minus;
    private TextView show_buy;
    private Boolean isNeedAdd;
    private Boolean isNeedMinus;
    private int check_amount;
    private int red_amount;
    private int blue_amount;
    private int yellow_amount;
    //紀錄api需要的變數
    private String recordurl ="http://140.119.163.40:8080/DarkEmpire/app/ver1.0/storeAction/";
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buypotion);
        SharedPreferences b = getSharedPreferences("DATA",0);
        a = b.getString("ID","");
        //紀錄API需要的資訊
        String recent_longi = b.getString("longi","");
        String recent_latit = b.getString("latit","");
        recordurl = "http://140.119.163.40:8080/DarkEmpire/app/ver1.0/storeAction/"+a+"/6/"+recent_longi+"/"+recent_latit+"/";
        red_amount = 0;
        yellow_amount = 0;
        blue_amount = 0;
        Button sure = (Button)findViewById(R.id.buy_check);
        show_buy = (TextView)findViewById(R.id.buy_content);
        red_add =(Button)findViewById(R.id.r_add_button);
        red_minus = (Button)findViewById(R.id.r_minus_button);
        yellow_add = (Button)findViewById(R.id.y_add_button);
        yellow_minus = (Button)findViewById(R.id.y_minus_button);
        blue_add = (Button)findViewById(R.id.b_add_button);
        blue_minus = (Button)findViewById(R.id.b_minus_button);
        MyAddOnTouch(red_add,1);
        MyAddOnTouch(yellow_add,2);
        MyAddOnTouch(blue_add,3);
        MyMinusOnTouch(red_minus,1);
        MyMinusOnTouch(yellow_minus,2);
        MyMinusOnTouch(blue_minus,3);
        isNeedAdd = false;
        isNeedMinus = false;
        show_buy.setText(" 您要購買: \n"+"紅水"+red_amount+"瓶，"+"黃水"+yellow_amount+"瓶，"+"藍水"+blue_amount+"瓶");
        sure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String posturl = "http://140.119.163.40:8080/DarkEmpire/app/ver1.0/buyItem/"+a+"/"+red_amount+"/"+yellow_amount+"/"+blue_amount;
                try {
                    String result = Http_Post.httppost(posturl);
                    if(result.contains("success")){
                        Toast.makeText(Buypotion_Activity.this,"購買成功",Toast.LENGTH_LONG).show();
                        Http_Get.httpget(recordurl);
                        Log.d("pickupurl",recordurl);
                    }
                    else{
                        Toast.makeText(Buypotion_Activity.this,"購買失敗",Toast.LENGTH_LONG).show();
                        Log.d("show result",""+result);
                        Log.d("show amount","red amount="+red_amount+"   yellow amount = "+yellow_amount +"  blue amount = "+blue_amount);

                    }
                } catch (ProtocolException e) {
                    e.printStackTrace();
                }
            }
        });
        //若是第一次登入會有checkbox
        String firsttime = b.getString("Fp_potion_buy","");
        int  first_time_press = firsttime.length();
        if(first_time_press==0){
            sb_instruction();
            b.edit().putString("Fp_potion_buy","pressed").apply();
        }
        else{
            Log.d("not equal","0");
        }
    }
    private void MyAddOnTouch(final Button btn, final int check_num){
        btn.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if(motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                    isNeedAdd = true;
                    check_amount = check_num;
                    new Thread(addRunnable).start();
//                    show_buy.setText("您要購買: \n"+"紅水"+red_amount+"瓶，"+"藍水"+blue_amount+"瓶，"+"黃水"+yellow_amount+"瓶");
                }
                if(motionEvent.getAction() == MotionEvent.ACTION_UP){
                    isNeedAdd = false;
                    Log.d("press check:","up");
                }
                return false;
            }
        });
    }
    private void MyMinusOnTouch(final Button btn, final int check_num){
        btn.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if(motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                    isNeedMinus = true;
                    check_amount = check_num;
                    new Thread(minusRunnable).start();
//                    show_buy.setText("您要購買: \n"+"紅水"+red_amount+"瓶，"+"藍水"+blue_amount+"瓶，"+"黃水"+yellow_amount+"瓶");
                }
                if(motionEvent.getAction() == MotionEvent.ACTION_UP){
                    isNeedMinus = false;
                    Log.d("press minus:","up");
                }
                return false;
            }
        });
    }
    private Handler uiHandler = new Handler(){
        @Override
        public void handleMessage(Message msg){
            // TODO Auto-generated method stub
            super.handleMessage(msg);
            show_buy.setText(" 您要購買: \n"+"紅水"+red_amount+"瓶，"+"黃水"+yellow_amount+"瓶，"+"藍水"+blue_amount+"瓶");

        }
    };
    private Runnable addRunnable = new Runnable() {
    @Override
    public void run() {
        while(isNeedAdd){
            Log.d("try 1 ","hi");
//            uiHandler.sendEmptyMessage(0);
            Log.d("hihi",""+check_amount);
            if(check_amount == 1){
                if(red_amount<99) {
                    red_amount++;
                    Log.d("show amount red", "" + red_amount);
//                show_buy.setText("您要購買: \n"+"紅水"+red_amount+"瓶，"+"藍水"+blue_amount+"瓶，"+"黃水"+yellow_amount+"瓶");
                }
            }
            else if(check_amount == 2){
                if(yellow_amount<99) {
                    yellow_amount++;
                    Log.d("show amount yellow", "" + yellow_amount);
                }
            }
            else if(check_amount == 3){
                if(blue_amount<99) {
                    blue_amount++;
                    Log.d("show amount blue", "" + blue_amount);
                }

            }
            else {
                Log.d("no plus","nonono");
            }
            uiHandler.sendEmptyMessage(0);
            try{
                Thread.sleep(200l);
            }catch (InterruptedException e){
                e.printStackTrace();
            }
        }
    }
};
    private Runnable minusRunnable = new Runnable() {
        @Override
        public void run() {
            while(isNeedMinus){

                if(check_amount == 1){
                    if(red_amount>0) {
                        red_amount--;
                        Log.d("now red amount:", "" + red_amount);
                    }
                }
                else if(check_amount == 2){
                    if(yellow_amount>0) {
                        yellow_amount--;
                        Log.d("now yellow amount: ", "" + yellow_amount);
                    }
                }
                else if(check_amount == 3){
                    if(blue_amount>0) {
                        blue_amount--;
                        Log.d("now blue amount ", "" + blue_amount);
                    }
                }
                uiHandler.sendEmptyMessage(0);
                try{
                    Thread.sleep(200l);
                }catch (InterruptedException e){
                    e.printStackTrace();
                }
            }
        }
    };
    public void sb_instruction (){
        new AlertDialog.Builder(Buypotion_Activity.this)
                .setTitle("金幣說明")
                .setMessage("如何獲得金幣\n" +
                        "1.佔領神殿\n" +
                        "連續佔一神殿5天，第5天獲得金幣100枚，第6天200枚，第7天300枚，第8天400枚，第9天500枚。第10天起，每天維持500枚獎勵。\n" +
                        "2.成為超原力使者\n" +
                        "成為超原力使者，立即獲得1000枚金幣。\n" +
                        "\n" +
                        "金幣用途\n" +
                        "每1000枚金幣可以購買1個藍聖水\n" +
                        "每500枚金幣可以購買1個黃聖水\n" +
                        "每200枚金幣可以購買1個紅聖水")
                .setPositiveButton("確定",new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,
                                        int whichButton) {

                    }
                }).create().show();
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
            this.finish();
        }

        return false;

    }
}
