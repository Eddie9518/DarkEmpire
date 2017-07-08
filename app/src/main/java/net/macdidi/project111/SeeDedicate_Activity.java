package net.macdidi.project111;

import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import java.net.ProtocolException;

/**
 * Created by Eddie84 on 2017/4/17.
 */
public class SeeDedicate_Activity extends AppCompatActivity {
    //資料庫
    private SharedPreferences datab;
    private ImageView img1;
    private ImageView img2;
    private ImageView img3;
    private ImageView img4;
    private ImageView img5;
    private ImageView img6;
    private ImageView img7;
    private ImageView img8;
    private ImageView imgframe;
    private String [] pname;
    private Bitmap [] bitmap;
    private String url ="http://140.119.163.40:8080/DarkEmpire/app/ver1.0/forceColor";
    private char [] colororder;
    private Boolean same;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dedicate);
        SharedPreferences b = getSharedPreferences("DATA", 0);
        //呼叫資料庫
        datab = getSharedPreferences("DATA", 0);

        Spinner spinner = (Spinner)findViewById(R.id.dedicate_spinner);
        spinner.setVisibility(View.GONE);
        TextView textView = (TextView)findViewById(R.id.dedicate_title);
        textView.setVisibility(View.GONE);
        imgframe = (ImageView) findViewById(R.id.show_frame);
        img1 = (ImageView) findViewById(R.id.show_img1);
        img2 = (ImageView) findViewById(R.id.show_img2);
        img3 = (ImageView) findViewById(R.id.show_img3);
        img4 = (ImageView) findViewById(R.id.show_img4);
        img5 = (ImageView) findViewById(R.id.show_img5);
        img6 = (ImageView) findViewById(R.id.show_img6);
        img7 = (ImageView) findViewById(R.id.show_img7);
        img8 = (ImageView) findViewById(R.id.show_img8);
        processcontrol();

    }
    public void processcontrol(){
        try {
            String a = Http_Get.httpget(url);
            colororder = new char[a.length()];
            for (int i = 0; i < a.length(); i++) {
                colororder[i] = a.charAt(i);

            }
        } catch (ProtocolException e) {
            e.printStackTrace();
        }
        pname = new String[8];
        String k = null;
        bitmap = new Bitmap[8];
        for (int j = 0; j < 8; j++) {
            char r = colororder[j];
            switch (r) {
                case 'R':
                    k = "red";
                    break;
                case 'B':
                    k = "blue";
                    break;
                case 'D':
                    k = "black";
                    break;
                case 'Y':
                    k = "yellow";
                    break;
            }
            pname[j] = k + Integer.toString(j + 1);

            int resID = getResources().getIdentifier(pname[j], "drawable", SeeDedicate_Activity.this.getPackageName());
//            BitmapFactory.Options options = new BitmapFactory.Options();
//            options.inSampleSize = 2;
//            bitmap[j] = BitmapFactory.decodeResource(getResources(), resID, options);
            bitmap[j] = BitmapFactory.decodeResource(getResources(), resID);
            switch (j) {
                case 0:
                    img1.setImageBitmap(bitmap[j]);
                    break;
                case 1:
                    img2.setImageBitmap(bitmap[j]);
                    break;
                case 2:
                    img3.setImageBitmap(bitmap[j]);
                    break;
                case 3:
                    img4.setImageBitmap(bitmap[j]);
                    break;
                case 4:
                    img5.setImageBitmap(bitmap[j]);
                    break;
                case 5:
                    img6.setImageBitmap(bitmap[j]);
                    break;
                case 6:
                    img7.setImageBitmap(bitmap[j]);
                    break;
                case 7:
                    img8.setImageBitmap(bitmap[j]);
                    break;
            }
        }
        same = true;
        Character a = colororder[0];
        for (int j = 0; j < 8; j++) {
            if (colororder[j] != a) {
                same = false;
                break;
            } else {
                Log.d("test", "" + colororder[j] + " &&&" + a);
                continue;
            }
        }
        if (same) {
            int resID = getResources().getIdentifier("puzzle_1pack", "drawable", SeeDedicate_Activity.this.getPackageName());
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inSampleSize = 2;
            Bitmap frame = BitmapFactory.decodeResource(getResources(), resID, options);
            imgframe.setImageBitmap(frame);
        } else {
            int resID = getResources().getIdentifier("puzzle_8pack", "drawable", SeeDedicate_Activity.this.getPackageName());
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inSampleSize = 2;
            Bitmap frame = BitmapFactory.decodeResource(getResources(), resID, options);
            imgframe.setImageBitmap(frame);
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
