package net.macdidi.project111;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.widget.TextView;

/**
 * Created by Eddie84 on 2016/12/13.
 */
public class Explanation_Activity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_explanation);
        TextView content = (TextView)findViewById(R.id.explanation_text);
        content.setText(
                "\n" +
                "   拳杉堡是新世界的聖地。3000年前，拳杉堡原是兩大勢力的停戰線。這兩大勢力是兩個部落，他們曾經打了1000年的仗。\n\n" +
                "   這兩大勢力一方是來自暗黑大陸，受印琛大帝迫害的脫逃者席奈族(Sinae)，席奈人帶著具有族人神力的馬納圖騰「超源力」(Super Force)逃至新世界躲避追殺。另一方則是來自海龍王後代，尋找新世界的安塔雅(Antayen)。安塔雅人早在50000年前便找到了這個新世界，定居在此；一直到4000年前，席奈人逃離暗黑大陸，來此落地生根，也因而與安塔雅人開啟了長達1000年的衝突。兩軍最後在拳杉堡協議停戰，劃定停戰線，維持了將近2500年的和平。維持他們世世代代和平的首領與將軍，分別在拳杉堡樹立了和平的誓言與族訓，他們的神靈也棲息在這些碑言與神殿之中，無時無刻不提醒著子民們不能再掀起戰爭，兩族應和平共處。而這些子民後代，則共享了超源力的庇佑，每個子民身上都有馬納。\n\n" +
                "   500年前，印琛(Jinzen)的第七代傳人發生內戰，戰敗的蚋轅(Ruyen)決定尋找4000年前脫逃者所帶走的超源力，因而一路找尋到新世界。來到新世界的蚋轅為了在拳杉堡建立基地，殺害了無數安塔雅人與席奈人，甚至挑起兩族仇恨，擾亂了2500年來的和平。但是，直到臨終前蚋轅都沒有找到超源力，於是他與惡魔立約，如果能讓他找到超源力，他的靈魂願意成為惡魔的坐騎。惡魔於是結束了蚋轅的生命，將他化為一匹黑馬，並開始在拳杉堡四處潛伏，搜尋超源力。而席奈人與阿塔亞人也為了信守族訓，聯合起來保護超源力，並驅趕暗黑勢力。\n\n" +
                "   參與這場保衛戰的你我，都是席奈人與安塔雅人的後代，我們同時流著兩族人的血脈。不管你投入哪一方，只代表你決定在這場戰役中扮演甚麼任務。這場戰役，只有靠我們通力合作，才可能守住拳杉堡、保護超原力不被奪走。\n");
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
