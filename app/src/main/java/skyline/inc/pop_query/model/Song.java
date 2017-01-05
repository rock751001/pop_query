package skyline.inc.pop_query.model;

/**
 * Created by MacintoshHD on 2017/1/2.
 */

public class Song {

    //歌曲名稱
    private String mSongName;
    //歌曲檔名
    private String mSongFileName;
    //歌曲名字長度
    private int mNameLength;
    //拆字串
    public char[] getNameCharacters(){
        return mSongName.toCharArray();
    }

    public String getSongName() {
        return mSongName;
    }

    public void setSongName(String songName) {
        this.mSongName = songName;
        this.mNameLength =songName.length();
    }

    public String getSongFileName() {
        return mSongFileName;
    }

    public void setSongFileName(String songFileName) {
        this.mSongFileName = songFileName;
    }

    public int getNameLength() {
        return mNameLength;
    }

}
