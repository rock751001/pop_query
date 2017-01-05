package skyline.inc.pop_query.util;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import skyline.inc.pop_query.R;
import skyline.inc.pop_query.data.Const;
import skyline.inc.pop_query.model.IAlertDialogButtonListener;

/**
 * Created by MacintoshHD on 2017/1/1.
 */

public class Util {

    private static AlertDialog mAlertDialog;

    public static View getView(Context context,int layoutId){
        LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View layout = inflater.inflate(layoutId,null);

        return layout;
    }

    /**
     * 見面跳轉
     * @param context
     * @param desti
     */
    public static void startActitvity(Context context,Class desti){
      Intent intent = new Intent();
        intent.setClass(context,desti);
        context.startActivity(intent);
        //關閉當前的Activity
        ((Activity)context).finish();
    }
    /**
     *  顯示自定義對話框
     */
    public static void showDialog(final Context context, String message,
                                  final IAlertDialogButtonListener listener){
        View  dialogView = null;
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        dialogView = getView(context, R.layout.dialog_view);

        ImageButton btnOkView = (ImageButton) dialogView.findViewById(R.id.btn_dialog_ok);
        ImageButton btnCancelView = (ImageButton) dialogView.findViewById(R.id.btn_dialog_cancel);

        TextView textMessageView = (TextView) dialogView.findViewById(R.id.text_dialog_message);

        textMessageView.setText(message);

        btnOkView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //關閉對話筐
                if(mAlertDialog!=null){
                   mAlertDialog.cancel();
                }
                //事件回調
                if(listener!=null){
                    listener.onClick();
                }

                //播放音效
                MyPlayer.playTone(context,MyPlayer.INDEX_STONE_ENTER);
            }
        });

        btnCancelView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //關閉對話筐
                if(mAlertDialog!=null){
                    mAlertDialog.cancel();
                }
                MyPlayer.playTone(context,MyPlayer.INDEX_STONE_CANCEL);

            }
        });
        builder.setView(dialogView);
        mAlertDialog = builder.create();
        //顯示對話框
        mAlertDialog.show();
    }

    public static void saveData(Context context,int stageIndex,int coins){
        FileOutputStream fis = null;
        try {
            fis = context.openFileOutput(Const.FILE_NAME_SAVE_DATA,
                    Context.MODE_PRIVATE);
            DataOutputStream dos = new DataOutputStream(fis);

            dos.writeInt(stageIndex);
            dos.writeInt(coins);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if(fis!=null){
                try {
                    fis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 讀取儲存資料
     * @param context
     * @return
     */
    public static int[] loadData(Context context){
        FileInputStream fis = null;
        int[] datas = {-1,Const.TOTAL_COINS};

        try {
            fis = context.openFileInput(Const.FILE_NAME_SAVE_DATA);
            DataInputStream dis = new DataInputStream(fis);

            datas[Const.INDEX_LOAD_DATA_STAGE] = dis.readInt();
            datas[Const.INDEX_LOAD_DATA_COINS] = dis.readInt();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e){
            e.printStackTrace();
        } finally {
            if(fis!=null){
                try {
                    fis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return datas;
    }
}
