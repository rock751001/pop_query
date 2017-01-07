package skyline.inc.pop_query.myui;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;

import java.util.ArrayList;

import skyline.inc.pop_query.R;
import skyline.inc.pop_query.model.IWordButtonClickListener;
import skyline.inc.pop_query.model.WordButton;
import skyline.inc.pop_query.util.Util;

/**
 * Created by MacintoshHD on 2017/1/1.
 */

public class MyGridView extends GridView {

    public final  static  int COUNTS_WORDS = 24;//設定長量，全大寫

    private ArrayList<WordButton> mArrayList = new ArrayList<WordButton>();//24個文字按鈕的容器

    private MyGridAdapter mAdapter;

    private Context mContext;

    private Animation mScaleAnimation;

    private IWordButtonClickListener mWordButtonListener;

    public MyGridView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        mAdapter = new MyGridAdapter();
        this.setAdapter(mAdapter);//進行關聯
    }

    public void updateData(ArrayList<WordButton> list){//文字內容的變更方法
        mArrayList = list;
        //重新給值
        setAdapter(mAdapter);
    }

    class MyGridAdapter extends BaseAdapter{//用baseAdapter自訂View
        //實現BaseAdapter抽象方法
        @Override
        public int getCount() {
            return mArrayList.size();
        }

        @Override
        public Object getItem(int position) {
            return mArrayList.get(position);//容器內所對應的內容
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int pos, View v, ViewGroup p) {

             final WordButton holder;//文字按鈕
             if(v==null){
                v = Util.getView(mContext, R.layout.self_ui_gridview_item);//如果為空直創建這個view
                 holder = mArrayList.get(pos);//holder對象從容器當中取出
                 // 引入動畫
                 mScaleAnimation = AnimationUtils.loadAnimation(mContext,R.anim.scale);//AnimationUtils為Android動畫工具類，loadAnimation載入xml檔
                 // 延遲動畫時間
                 mScaleAnimation.setStartOffset(pos*100);

                 holder.mIndex = pos;
                 holder.mViewButton = (Button) v.findViewById(R.id.item_btn);//把Button引入
                 holder.mViewButton.setOnClickListener(new View.OnClickListener(){

                     @Override
                     public void onClick(View v) {
                         mWordButtonListener.onWordButtonClick(holder);//點擊事件後觸發接口裡的方法
                     }
                 });
                 v.setTag(holder);//紀錄tag
             }else{
                 holder = (WordButton) v.getTag();//直不為空取出tag值
             }
            holder.mViewButton.setText(holder.mWordString);//按鈕的值顯示出來

            v.startAnimation(mScaleAnimation);//撥放動畫

            return v;
        }
    }

    /**
     * 註冊監聽介面
     * @param listener
     */
    public void  registOnWordButtonClick(IWordButtonClickListener listener){
        mWordButtonListener = listener;
    }
}
