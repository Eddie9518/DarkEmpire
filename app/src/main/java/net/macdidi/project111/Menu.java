package net.macdidi.project111;

import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

/**
 * Created by Eddie84 on 2016/10/13.
 */
public class Menu extends AppCompatActivity {
    private static final int START_LOCATION = 2;
    private static final int REQUEST_FINE_LOCATION_PERMISSION = 102;
    public static final String App_Dir = "/NCCU_Ingress";
    private MediaPlayer mediaPlayer01;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu);
//        try
//        {
//            File mSDFile = null;
//
//            //檢查有沒有SD卡裝置
//            if(Environment.getExternalStorageState().equals( Environment.MEDIA_REMOVED))
//            {
//                return ;
//            }
//            else
//            {
//                //取得SD卡儲存路徑
//                mSDFile = Environment.getExternalStorageDirectory();
//            }
//
//            //建立文件檔儲存路徑
//            File mFile = new File(mSDFile.getAbsolutePath()+ App_Dir);
//
//            //若沒有檔案儲存路徑時則建立此檔案路徑
//            if(!mFile.exists())
//            {
//                mFile.mkdirs();
//                Log.d("Test",""+Login_Activity.userid);
//            }
//
//            //取得mEdit文字並儲存寫入至SD卡文件裡
//            Log.d("NONONONONO","123");
//            FileWriter mFileWriter = new FileWriter( mFile+"/User.txt",false);
//            BufferedWriter bw = new BufferedWriter(mFileWriter);
//            Log.d("why","123");
//            bw.write(""+Login_Activity.userid);
//            Log.d("whyyy","456");
//            bw.close();
//            Log.d("Testtt",""+Login_Activity.userid);
//        }
//        catch (Exception e)
//        {
//            Log.d("yoyoyo","FAFFAFAFAILED");
//        }
        mediaPlayer01 = MediaPlayer.create(this, R.raw.m002);
        mediaPlayer01.start();
    }
    public void todata(View view){
        Intent intent = new Intent();
        intent.setClass(this,Personal_Data.class);
        startActivity(intent);
        mediaPlayer01.release();
        //this.finish();
    }
    public void tomap(View view){
        Intent intentMap = new Intent(Menu.this, MapsActivity.class);
        // 啟動地圖元件
        startActivityForResult(intentMap, START_LOCATION);
    }

    public void clickFunction(View view) {
        int id = view.getId();
        Intent intent = new Intent();
        switch (id) {
            case R.id.gain_money_button:
                Log.d("goin","888");

                intent.setClass(this,Rune_Activity.class);
                startActivity(intent);
                //this.finish();
                break;
            case R.id.badge_achievement_button:
                Log.d("gobadge","777");
                intent.setClass(this,Badge_Activity.class);
                startActivity(intent);
                //this.finish();
                break;
            case R.id.start_game_button:
                Log.d("hellow","878787");
                intent.setClass(this,Game_Activity.class);
                startActivity(intent);
                //this.finish();
                break;
            case R.id.inter_communication_button:
                Log.d("jlsjdlf","12323");
                break;
        }
    }
    public void settings(View view){
        SharedPreferences b = getSharedPreferences("DATA",0);
        SharedPreferences.Editor editor = b.edit();
        editor.remove("ID");
        editor.apply();
        Intent intent = new Intent();
        intent.setClass(this,MainActivity.class);
        startActivity(intent);
        this.finish();

    }
}
