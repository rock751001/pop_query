package skyline.inc.pop_query.ui;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;
import android.widget.Toast;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import skyline.inc.pop_query.R;
import skyline.inc.pop_query.data.Const;
import skyline.inc.pop_query.model.IAlertDialogButtonListener;
import skyline.inc.pop_query.model.IWordButtonClickListener;
import skyline.inc.pop_query.model.Song;
import skyline.inc.pop_query.model.WordButton;
import skyline.inc.pop_query.myui.MyGridView;
import skyline.inc.pop_query.util.MyLog;
import skyline.inc.pop_query.util.MyPlayer;
import skyline.inc.pop_query.util.Util;

public class MainActivity extends AppCompatActivity implements IWordButtonClickListener {//implements IWordButtonClickListener 實現接口裡的方法
    public  final  static String TAG = "MainActivity";
    /**
     *    答案狀態 正確
     */
    public final static int STATUS_ANSWER_RIGHT = 1;
    /**
     *  答案狀態 錯誤
     */
    public final static int STATUS_ANSWER_WRONG = 2;
    /**
     *  答案狀態 不完整
     */
    public final static int STATUS_ANSWER_LACK = 3;

    //    閃爍次數

    public final static int SPASH_TIMES = 6;

    //
    public  final  static  int ID_DIALOG_DELETE_WORD = 1;

    public  final  static  int ID_DIALOG_TIP_ANSWER = 2;

    public  final  static  int ID_DIALOG_LACK_COINS = 3;

    //    唱片動畫
    private Animation mPanAnim;
    private LinearInterpolator mPanLin;//LinearInterpolator動畫速度，對動畫做加減速度，可以讓這個動畫勻速執行(系統預設先加速後減速)

    private Animation mBarInAnim;
    private LinearInterpolator mBarInLin;

    private Animation mBarOutAnim;
    private LinearInterpolator mBarOutLin;


    //  當前關索引
    private TextView mCurrentStagePassView;
    //  當前歌曲名稱
    private TextView mCurrentSongNamePassView;
    //
    private  TextView mCurrentStageView;
    //   唱片原件
    private ImageView mViewPan;
    //   撥桿原件
    private ImageView mViewPanBar;

    //    Play按鈕事件
    private ImageButton mBtnPlayStart;

    //    過關界面
    private View mPassView;

    //    標識(判斷當前唱盤是否在轉動)
    private boolean mIsRunning = false;

    //    文字盒子容器 (待選筐)
    private ArrayList<WordButton> mAllWords;

    //    (已選筐)(答案框)
    private  ArrayList<WordButton> mBtnSelectWords;

    //
    private  MyGridView mMyGridView;

    //     存放被選取文字的UI容器
    private LinearLayout mViewWordsContainer;
    //      當前的歌曲
    private Song mCurrentSong;
    //      當前關的索引
    private int mCurrentStatgeIndex = -1;
    //      當前金幣數量
    private  int  mCurrentCoins = Const.TOTAL_COINS;
    //     金幣View
    private TextView mViewCurrentCoins;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //讀取遊戲進度
        //int[] datas = Util.loadData(this);
        //mCurrentStatgeIndex = datas[Const.INDEX_LOAD_DATA_STAGE];
       // mCurrentCoins = datas[Const.INDEX_LOAD_DATA_COINS];
        //  控件初始化

        mViewPan = (ImageView) findViewById(R.id.imageView1);//唱片元件

        mViewPanBar = (ImageView) findViewById(R.id.imageView2);//撥桿元件

        mMyGridView = (MyGridView) findViewById(R.id.gridview);//文字按鈕

        mViewCurrentCoins = (TextView) findViewById(R.id.txt_bar_coins);
        mViewCurrentCoins.setText(mCurrentCoins+"");
        //註冊監聽器
        mMyGridView.registOnWordButtonClick(this);

        mViewWordsContainer = (LinearLayout) findViewById(R.id.word_select_container);

        // 動畫初始化 // 設置監聽器(盤桿置放後唱盤才開始動)
        mPanAnim = AnimationUtils.loadAnimation(this, R.anim.rotate);//AnimationUtils為Android中動畫工具類，loadAnimation方法用於加載XML動畫配置文件
        mPanLin = new LinearInterpolator();
        mPanAnim.setInterpolator(mPanLin);//把速度和動畫關聯起來
        //setAnimationListener  對Animation設置監聽器，在動畫效果開始執行時、執行結束、重複執行 可以觸發監聽器
        mPanAnim.setAnimationListener(new Animation.AnimationListener(){

            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {//唱片動畫結束啟動撥桿回去動畫
                  mViewPanBar.startAnimation(mBarOutAnim);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        mBarInAnim = AnimationUtils.loadAnimation(this, R.anim.rotate_45);
        mBarInLin = new LinearInterpolator();
        mBarInAnim.setFillAfter(true);//setFillAfter表示使動畫撥完後保持在最後終止的位置，true表示使用該效果
        mBarInAnim.setInterpolator(mBarInLin);
        mBarInAnim.setAnimationListener(new Animation.AnimationListener(){

            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {//撥桿動畫結束後啟動唱片動畫
              mViewPan.startAnimation(mPanAnim);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        mBarOutAnim = AnimationUtils.loadAnimation(this, R.anim.rotate_d_45);
        mBarOutLin = new LinearInterpolator();
        mBarOutAnim.setFillAfter(true);
        mBarOutAnim.setInterpolator(mBarOutLin);
        mBarOutAnim.setAnimationListener(new Animation.AnimationListener(){

            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                mIsRunning = false;  //撥桿回到原位的動畫結束後，唱片可以繼續撥放
                mBtnPlayStart.setVisibility(View.VISIBLE);//顯示唱片撥放紐
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        mBtnPlayStart = (ImageButton) findViewById(R.id.btn_play_start);
        //按播放鍵所觸發的事件
        mBtnPlayStart.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                handlePlayButton();
            }
        });
        //初始化遊戲數據
        initCurrentStageData();
        //處理刪除按鍵事件
        handleDeleteWord();
        //處理提示按鍵事件
        handleTipAnswer();
        //載入時播放音樂
        handlePlayButton();

    }
    @Override
    public void onWordButtonClick(WordButton wordButton){
        //Toast.makeText(this,wordButton.mIndex+"",Toast.LENGTH_SHORT).show();
        setSelectWord(wordButton);
        //獲得答案狀態
        int checkResult = checkTheAnswer();
        // 檢查答案
        if(checkResult==STATUS_ANSWER_RIGHT){
            //Toast.makeText(this,"STATUS_ANSWER_RIGHT",Toast.LENGTH_SHORT).show();
            // 過關並獲得獎勵
            handlePassEvent();
        }else if(checkResult==STATUS_ANSWER_WRONG){
            // 閃爍文字並提示用戶
            sparkTheWrods();
        }else if(checkResult==STATUS_ANSWER_LACK){
           //  文字顏色設為白
            for(int i=0;i<mBtnSelectWords.size();i++){
                mBtnSelectWords.get(i).mViewButton.setTextColor(Color.WHITE);
            }
        }
    }

    /**
     *  處理過關界面與事件
     */
    private  void  handlePassEvent(){
    mPassView = (LinearLayout)this.findViewById(R.id.pass_view);
        //顯示過關界面
        mPassView.setVisibility(View.VISIBLE);
        //未完成的動畫
        mViewPan.clearAnimation();
        //停止正在播放的聲音
        MyPlayer.stopTheSong(MainActivity.this);
        //播放音效
        MyPlayer.playTone(MainActivity.this,MyPlayer.INDEX_STONE_COIN);
        //當前關的索引
        mCurrentStagePassView = (TextView) findViewById(R.id.text_current_stage_pass);
        if(mCurrentStagePassView!=null){
            mCurrentStagePassView.setText((mCurrentStatgeIndex+1)+"");
        }
        //顯示歌曲名稱
        mCurrentSongNamePassView = (TextView) findViewById(R.id.text_current_song_name_pass);
        if(mCurrentSongNamePassView!=null){
            mCurrentSongNamePassView.setText(mCurrentSong.getSongName());
        }
        //下一關按鍵處理
        ImageButton btnPass = (ImageButton) findViewById(R.id.btn_next);
        btnPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               if(judgeAppPassed()){
                   //進入過關界面
                   Util.startActitvity(MainActivity.this,AllPassView.class);
               }else{
                   //開始新一關
                   mPassView.setVisibility(View.GONE);
                   //加載關卡數據
                   initCurrentStageData();
                   mCurrentCoins += 30;

                   mViewCurrentCoins.setText(mCurrentCoins+"");
               }
            }
        });
    }

    /**
     * 判斷是否通關
     * @return
     */
    private boolean judgeAppPassed(){
        return (mCurrentStatgeIndex == Const.SONG_INFO.length-1);
    }
    /**
     * 清除答案
     * @param wordButton
     */
    private  void clearTheAnswer(WordButton wordButton){
        wordButton.mViewButton.setText("");//設定文字內容空字串
        wordButton.mWordString="";//設定文字字串為空
        wordButton.mIsVisiable = false;//答案框框可見性不可見
        //待選筐的可見性
        setButtonVisiable(mAllWords.get(wordButton.mIndex),View.VISIBLE);//取出索引所代表的待選Button設可見性為可見

    }
    /**
     * 設置答案
     * @param wordButton
     */
    private void setSelectWord(WordButton wordButton){
        for(int i=0;i<mBtnSelectWords.size();i++){
            if(mBtnSelectWords.get(i).mWordString.length()==0){//從第一個開始取，判斷是否為空字符
                //設置答案文字匡內容及可見性
                mBtnSelectWords.get(i).mViewButton.setText(wordButton.mWordString);//答案文字框內容來自帶選文字框內容
                mBtnSelectWords.get(i).mIsVisiable = true;
                mBtnSelectWords.get(i).mWordString = wordButton.mWordString;
                //記錄索引
                mBtnSelectWords.get(i).mIndex = wordButton.mIndex;
                //Log
                MyLog.d(TAG,mBtnSelectWords.get(i).mIndex + "");
                //設置待選筐的可見性
                setButtonVisiable(wordButton, View.INVISIBLE);//不可見
                break;
            }
        }
    }

    /**
     * 設置待選文字匡是否可見
     * @param button
     * @param visibility
     */
    private  void setButtonVisiable(WordButton button , int visibility){
        button.mViewButton.setVisibility(visibility);//為button設可見性
        button.mIsVisiable = (visibility==View.VISIBLE)?true:false;//設可見性的屬性
        //Log
        MyLog.d(TAG,button.mIsVisiable + "");
    }

    /**
     * 處理圓盤中的播放按鈕
     */
    private void handlePlayButton(){
        if(mViewPanBar!=null) {
            if (!mIsRunning) {  //當前唱片處於非運行時，可以開始撥放
                mIsRunning = true;
                mViewPanBar.startAnimation(mBarInAnim);//按下撥放鈕後運行撥桿動畫
                mBtnPlayStart.setVisibility(View.INVISIBLE);//隱藏唱片撥放紐 setVisibility()  visible：顯示invisible：隱藏，但畫面會保留該物件的位置gone：隱藏，不會保留位置
                //播放音樂
                MyPlayer.playSong(MainActivity.this,mCurrentSong.getSongFileName());


            }
        }
    }

    @Override
    protected void onPause() {//應用程式中斷 或退出時 進行暫停
        //
        Util.saveData(MainActivity.this,mCurrentStatgeIndex-1,
                mCurrentCoins);
        //
        mViewPan.clearAnimation();//動畫停止
        //暫停音樂
        MyPlayer.stopTheSong(MainActivity.this);

        super.onPause();
    }
    private  Song loadStageSongInfo(int stageIndex){//讀取當前關卡歌曲信息方法，stageIndex當前關卡索引
        Song song = new Song();
        String[] stage = Const.SONG_INFO[stageIndex];//從Const裡面讀取索引
        song.setSongFileName(stage[Const.INDEX_FILE_NAME]);//讀取歌的黨名
        song.setSongName(stage[Const.INDEX_SONG_NAME]);//讀取歌名

        return song;
    }

    /**
     * 加載當前關的數據
     */
    private  void  initCurrentStageData(){//初始化當前關的數據方法
        //讀取當前關的歌曲訊息後得到當前關卡的歌曲對象
        mCurrentSong = loadStageSongInfo(++mCurrentStatgeIndex);
        //初始化答案筐
        mBtnSelectWords = initWordSelect();
        //增加新的答案筐
        LayoutParams params = new LayoutParams(140, 140);//Params答案框的資料參數
        //清空原來的答案
        mViewWordsContainer.removeAllViews();
        //增加答案筐
        for(int i = 0; i<mBtnSelectWords.size();i++){
            mViewWordsContainer.addView(mBtnSelectWords.get(i).mViewButton,params);//添加的子視圖、子視圖的布局參數
        }
        //當前的索引
        mCurrentStageView= (TextView) findViewById(R.id.text_current_stage);
        if(mCurrentStageView!=null){
            mCurrentStageView.setText((mCurrentStatgeIndex+1)+"");
        }

        //獲得數據
        mAllWords = initAllWord();
        // 更新文字數據
        mMyGridView.updateData(mAllWords);
        // 載入時播放歌曲
        handlePlayButton();
    }

    /**
     * 初始化待選取文字框
     * @return
     */
    private ArrayList<WordButton> initAllWord(){//獲得文字數據的方法
        ArrayList<WordButton> data = new ArrayList<WordButton>();
        //獲得所有待選文字
        String[] words = generateWords();

        for(int i = 0; i < MyGridView.COUNTS_WORDS; i++){
            WordButton button = new WordButton();
            button.mWordString = words[i];
            data.add(button);//加入數值

        }

        //Toast.makeText(MainActivity.this,"生成按鈕有執行", Toast.LENGTH_LONG).show();

        return data;
    }
    /**
     * 初始已選取文字框 (答案框)
     * @return
     */
    private ArrayList<WordButton> initWordSelect() {
        ArrayList<WordButton> data = new ArrayList<WordButton>();

        for (int i = 0; i < mCurrentSong.getNameLength(); i++) {//對應歌曲名子長度生成答案框
            View view = Util.getView(MainActivity.this, R.layout.self_ui_gridview_item);

            final WordButton holder = new WordButton();

            holder.mViewButton = (Button)view.findViewById(R.id.item_btn);
            holder.mViewButton.setTextColor(Color.WHITE);//設定顏色
            holder.mViewButton.setText("");//初始值空
            holder.mIsVisiable = false;//隱藏

            holder.mViewButton.setBackgroundResource(R.drawable.game_wordblank);//設定背景
            holder.mViewButton.setOnClickListener(new View.OnClickListener(){//答案框點擊事件
                @Override
                public void onClick(View v) {
                    clearTheAnswer(holder);
                }
            });

            data.add(holder);//加入數值
        }

        return data;
    }

    private  String[] generateWords(){//生成所有的待選文字方法

        Random random = new Random();

        String[] words = new String[MyGridView.COUNTS_WORDS];

        //存入歌名
        for(int i = 0; i<mCurrentSong.getNameLength();i++){ //i<當前歌曲
            words[i] = mCurrentSong.getNameCharacters()[i]+"";//把歌曲名子轉成相應字符
        }
        //獲取隨機文字並存入陣列
        for(int i = mCurrentSong.getNameLength();
            i<MyGridView.COUNTS_WORDS;i++){
           words[i] = getRandomChar() + "";
        }
        //打亂文字順序:隨機選取一個字與第一個交換
        //然後在第二個之後隨機選擇一個與第二個交換，直到最後一個
        for(int i = MyGridView.COUNTS_WORDS-1;i>=0;i--){
           int index = random.nextInt(i+1);
            String buf = words[index];
            words[index] = words[i];
            words[i] = buf;
        }
        return words;
    }
    /**
     * 生成隨機漢字
     * @return
     */
    private  char getRandomChar(){
        String str = "";
        int highPos;//漢字高位
        int lowPos;//漢字低位

        Random random = new Random();
        highPos = (176+Math.abs(random.nextInt(20)));//abs為絕對值
        lowPos = (161+Math.abs(random.nextInt(93)));

        byte[] b = new byte[2];//一個中文字由2個字節組成，把高位和低位組合起來
        b[0] = (Integer.valueOf(highPos)).byteValue();//byteValue()返回字節一個字節的值
        b[1] = (Integer.valueOf(lowPos)).byteValue();

        try {
          str = new String(b,"Big5");//轉換字符的類型
        }catch (UnsupportedEncodingException e){
            e.printStackTrace();
        }

        return  str.charAt(0);//取出字串 位置在索引為0的字元
    }

    /**
     * 檢查答案
     * @return
     */
    private  int checkTheAnswer(){
        //檢查長度
        for(int i = 0;i<mBtnSelectWords.size();i++){
            if(mBtnSelectWords.get(i).mWordString.length()==0){
                return STATUS_ANSWER_LACK;
            }
        }
        //答案完整
        StringBuffer sb = new StringBuffer();
        for(int i=0;i<mBtnSelectWords.size();i++){
            sb.append(mBtnSelectWords.get(i).mWordString);
        }
        return  (sb.toString().equals(mCurrentSong.getSongName()))?
                STATUS_ANSWER_RIGHT:STATUS_ANSWER_WRONG;
    }

    /**
     *文字閃爍
     */
    private  void  sparkTheWrods(){
       //定時器
        TimerTask task = new TimerTask() {
            boolean mChange = false;
            int mSpardTimes = 0;
            @Override
            public void run() {
               runOnUiThread(new Runnable() {
                   @Override
                   public void run() {
                      if(++mSpardTimes>SPASH_TIMES){
                          return;
                      }
                       for(int i=0;i<mBtnSelectWords.size();i++){
                           mBtnSelectWords.get(i).mViewButton.setTextColor(mChange?Color.RED:Color.WHITE);
                       }
                       mChange = !mChange;
                   }
               });
            }
        };

        Timer timer = new Timer();
        timer.schedule(task,1,150);
    }

    /**
     * 自動選擇一個答案
     */
    private void tipAnswer(){

        boolean tipWord = false;
        for(int i=0;i<mBtnSelectWords.size();i++){
            if(mBtnSelectWords.get(i).mWordString.length()==0){
                //根據當前的答案匡條件選擇對應的文字填入
                onWordButtonClick(findIsAnswerWord(i));

                tipWord = true;
                //減少金幣數量
                if(!handleCoins(-getTipCoins())){
                    //金幣數量不夠時提示
                    showConfirmDialog(ID_DIALOG_LACK_COINS);
                    return;
                }
                break;
            }
        }

        //  沒有找到可以填充的答案
        if(!tipWord){
            sparkTheWrods();
        }

    }
    /**
     * 刪除文字
     */
    private  void deleteOneWord(){
        //減少金幣
        if(!handleCoins(-getDeleteWordCoins())){
            //金幣不夠
            showConfirmDialog(ID_DIALOG_LACK_COINS);
          return;
        }
        setButtonVisiable(findNotAnswerWord(),View.INVISIBLE);
    }
    private  WordButton findNotAnswerWord(){
        Random random = new Random();
        WordButton buf = null;

        while(true){
            int index = random.nextInt(MyGridView.COUNTS_WORDS);
            buf = mAllWords.get(index);
            if(buf.mIsVisiable&&!isTheAnswerWord(buf)){
                return  buf;
            }
        }
    }

    /**
     * 找到怡個答案文字
     * @param index  當前需要填入答案筐的索引
     * @return
     */
    private WordButton findIsAnswerWord(int index){
       WordButton buf = null;
        for(int i = 0 ; i <MyGridView.COUNTS_WORDS;i++){
            buf = mAllWords.get(i);
            if(buf.mWordString.equals(""+mCurrentSong.getNameCharacters()[index])){
                return buf;
            }
        }
        return null;
    }
    private  boolean isTheAnswerWord(WordButton word){
       boolean result = false;
        for(int i = 0; i< mCurrentSong.getNameLength();i++){
            if(word.mWordString.equals(""+mCurrentSong.getNameCharacters()[i])){
                    result = true;

                break;
            }
        }

        return result;
    }

    /**
     * 增加或減少指定數量的金幣
     * @param data
     * @return true
     */
    private boolean handleCoins(int data){
        //判斷當前金幣數量是否可以被減少
        if(mCurrentCoins+data>=0){
            mCurrentCoins += data;

            mViewCurrentCoins.setText(mCurrentCoins+"");
            return true;
        }else{
            return false;
        }

    }

    /**
     *  讀取刪除操作所要用的金幣
     * @return
     */
    private int getDeleteWordCoins(){
        return this.getResources().getInteger(R.integer.pay_delete_word);
    }
     /**
     *  讀取提示操作所要用的金幣
     * @return
     */
    private int getTipCoins(){
        return this.getResources().getInteger(R.integer.pay_tip_answer);
    }
    /**
     *  處理刪除按鍵
     */
    private  void  handleDeleteWord(){
        ImageButton button = (ImageButton) findViewById(R.id.btn_delete_word);
        button.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
               // deleteOneWord();
                showConfirmDialog(ID_DIALOG_DELETE_WORD);
            }
        });

    }
    /**
     *  處理提示按鍵
     */
    private  void  handleTipAnswer(){
        ImageButton button = (ImageButton) findViewById(R.id.btn_tip_answer);
        button.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
               //tipAnswer();
                showConfirmDialog(ID_DIALOG_TIP_ANSWER);
            }
        });

    }
    /**
     * 自定義AlertDialog事件響應
     *
     */
    //刪除錯誤答案
    public IAlertDialogButtonListener mBtnOkDeleteWordListener =
            new IAlertDialogButtonListener() {
                @Override
                public void onClick() {
                    deleteOneWord();
                }
            };
    //答案提示
    public IAlertDialogButtonListener mBtnOkTipAnswerListener =
            new IAlertDialogButtonListener() {
                @Override
                public void onClick() {
                    tipAnswer();
                }
            };
    //金幣不足
    public IAlertDialogButtonListener mBtnOkLackCoinsListener =
            new IAlertDialogButtonListener() {
                @Override
                public void onClick() {

                }
            };

    /**
     * 顯示對畫框
     * @param id
     */
    private  void  showConfirmDialog(int id){
        switch (id){
            case ID_DIALOG_DELETE_WORD:
                Util.showDialog(MainActivity.this,"是否花掉"+getDeleteWordCoins()+"金幣刪除一個文字框"
                        ,mBtnOkDeleteWordListener);
                break;
            case ID_DIALOG_TIP_ANSWER:
                Util.showDialog(MainActivity.this,"是否花掉掉"+getTipCoins()+"金幣獲得一個文字提示"
                        ,mBtnOkTipAnswerListener);
                break;
            case ID_DIALOG_LACK_COINS:
                Util.showDialog(MainActivity.this,"金幣不足,去商店補充"
                        ,mBtnOkLackCoinsListener);
                break;
        }
    }
}
