package skyline.inc.pop_query.model;

import android.widget.Button;

/**
 * 文字按扭輸入
 *
 */

public class WordButton {

        public int mIndex;//索引
        public boolean mIsVisiable; //顯示或隱藏
        public String mWordString; //當前文字

    public Button mViewButton;
    //public android.view.View params;

    public  WordButton(){
        mIsVisiable = true;
        mWordString = "";
    }

}
