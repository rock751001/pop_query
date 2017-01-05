package skyline.inc.pop_query.model;

import android.widget.Button;

/**
 * 文字按扭輸入
 *
 */

public class WordButton {

        public int mIndex;
        public boolean mIsVisiable;
        public String mWordString;

    public Button mViewButton;
    //public android.view.View params;

    public  WordButton(){
        mIsVisiable = true;
        mWordString = "";
    }

}
