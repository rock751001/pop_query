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

    public final  static  int COUNTS_WORDS = 24;

    private ArrayList<WordButton> mArrayList = new ArrayList<WordButton>();

    private MyGridAdapter mAdapter;

    private Context mContext;

    private Animation mScaleAnimation;

    private IWordButtonClickListener mWordButtonListener;

    public MyGridView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        mAdapter = new MyGridAdapter();
        this.setAdapter(mAdapter);
    }

    public void updateData(ArrayList<WordButton> list){
        mArrayList = list;
        //重新設置資料源
        setAdapter(mAdapter);
    }

    class MyGridAdapter extends BaseAdapter{

        @Override
        public int getCount() {
            return mArrayList.size();
        }

        @Override
        public Object getItem(int position) {
            return mArrayList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int pos, View v, ViewGroup p) {

             final WordButton holder;
             if(v==null){
                v = Util.getView(mContext, R.layout.self_ui_gridview_item);
                 holder = mArrayList.get(pos);
                 // 引入動畫
                 mScaleAnimation = AnimationUtils.loadAnimation(mContext,R.anim.scale);
                 // 延遲動畫時間
                 mScaleAnimation.setStartOffset(pos*100);

                 holder.mIndex = pos;
                 holder.mViewButton = (Button) v.findViewById(R.id.item_btn);
                 holder.mViewButton.setOnClickListener(new View.OnClickListener(){

                     @Override
                     public void onClick(View v) {
                         mWordButtonListener.onWordButtonClick(holder);
                     }
                 });
                 v.setTag(holder);
             }else{
                 holder = (WordButton) v.getTag();
             }
            holder.mViewButton.setText(holder.mWordString);

            v.startAnimation(mScaleAnimation);

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
