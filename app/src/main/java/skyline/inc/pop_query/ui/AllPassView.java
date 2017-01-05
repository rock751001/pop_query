package skyline.inc.pop_query.ui;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.FrameLayout;

import skyline.inc.pop_query.R;

/**
 * 通關界面
 * Created by MacintoshHD on 2017/1/4.
 */

public class AllPassView extends AppCompatActivity{

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.all_pass_view);
        //隱藏右上角的金幣
        FrameLayout view = (FrameLayout) findViewById(R.id.layout_bar_coin);
        view.setVisibility(View.INVISIBLE);
    }
}
