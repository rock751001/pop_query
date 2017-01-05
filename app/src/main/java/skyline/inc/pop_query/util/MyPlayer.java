package skyline.inc.pop_query.util;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.media.MediaPlayer;
import android.widget.Toast;

import java.io.IOException;

import skyline.inc.pop_query.ui.MainActivity;

/**
 * 音樂播放類
 */

public class MyPlayer {

    public final  static  int INDEX_STONE_ENTER = 0;
    public final  static  int INDEX_STONE_CANCEL =1;
    public final  static  int INDEX_STONE_COIN = 2;

    private final static String[] SONG_NAMES = {"enter.mp3","cancel.mp3","coin.mp3"};
    //音效
    private  static MediaPlayer[] mToneMediaPlayer = new MediaPlayer[SONG_NAMES.length];
    //歌曲播放
    private  static MediaPlayer mMusicMediaPlayer;

    //
    public static void playTone(Context context,int index){
       AssetManager assetManager = context.getAssets();
        if(mToneMediaPlayer[index]==null){
            mToneMediaPlayer[index] = new MediaPlayer();
            try {
                AssetFileDescriptor fileDescriptor =
                        assetManager.openFd(SONG_NAMES[index]);
                mToneMediaPlayer[index].setDataSource(fileDescriptor.getFileDescriptor(),
                        fileDescriptor.getStartOffset(),fileDescriptor.getLength());
                mToneMediaPlayer[index].prepare();


            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        mToneMediaPlayer[index].start();
    }
    //
    public static void playSong(Context context, String fileName){

        if(mMusicMediaPlayer==null){
            mMusicMediaPlayer = new MediaPlayer();
        }

        // 強制重置
        mMusicMediaPlayer.reset();
        // 加載聲音
        AssetManager assetManager = context.getAssets();
        try {
            //AssetFileDescriptor fileDescriptor = assetManager.openFd(fileName);
            AssetFileDescriptor fileDescriptor = assetManager.openFd(fileName);
            mMusicMediaPlayer.setDataSource(
                    fileDescriptor.getFileDescriptor(),
                    fileDescriptor.getStartOffset(),
                    fileDescriptor.getLength()
            );
            mMusicMediaPlayer.prepare();
            // 聲音播放
            mMusicMediaPlayer.start();



        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void stopTheSong(Context context){
        if(mMusicMediaPlayer!=null){
            mMusicMediaPlayer.stop();
        }
    }
}

