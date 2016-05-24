package com.chi.im.Utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Administrator on 2016/5/24.
 */
public class FileUtils {



    public static FileUtils fileUtils;

    public  static FileUtils  getInstance(){
        if(fileUtils==null){
            fileUtils=new FileUtils();
        }
        return fileUtils;
    }

    //获取
    public  String getUserName(Context mContext){
        String userName=null;
        SharedPreferences  sfp=mContext.getSharedPreferences("userInfo",Context.MODE_PRIVATE);
        userName=sfp.getString("userName","null");
        return  userName;
    }
    //保存
    public void  setUserName(String uerName,Context mContext){
        SharedPreferences  sfp=mContext.getSharedPreferences("userInfo",Context.MODE_PRIVATE);
        SharedPreferences.Editor  editor=sfp.edit();
        editor.putString("userName",uerName);
        editor.commit();
    }



}
