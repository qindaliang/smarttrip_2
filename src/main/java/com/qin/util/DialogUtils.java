package com.qin.util;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;

import com.qin.R;

/**
 * Created by Administrator on 2018/3/27 0027.
 */

public class DialogUtils {
    private static DialogUtils instance;
    private  Dialog dialog;
    private DialogUtils(Context context) {
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(0,0);
        params.width = 400;
        params.height =400;
        dialog = new Dialog(context);
        View view = View.inflate(context, R.layout.dialog_loading, null);
        dialog.setContentView(view);
        dialog.setContentView(view, params);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(true);
    }

    public static DialogUtils getInstance(Context context) {
        if(instance==null){
            instance=new DialogUtils(context);
        }
        return instance;
    }

    public void showDialog(){
        if (!dialog.isShowing()){
            dialog.show();
        }
    }
    public void dismissDialog(){
        if (dialog.isShowing()){
            dialog.dismiss();
        }
    }
}
