package com.chi.im.Utils;

import android.content.Context;
import android.widget.Toast;

/**
 * Created by Administrator on 2016/5/24.
 */
public class Utils {
    public   static void showToast(String msg, Context context){
        Toast.makeText(context,msg,Toast.LENGTH_LONG).show();
    }


}
