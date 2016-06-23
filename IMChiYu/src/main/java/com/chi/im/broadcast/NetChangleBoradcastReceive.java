package com.chi.im.broadcast;

import android.app.ActivityManager;
import android.app.usage.NetworkStatsManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.Toast;

import com.chi.im.constant.Constant;

/**
 * Created by Administrator on 2016/6/19.
 * 系统广播，监听网络的变化
 */
public class NetChangleBoradcastReceive extends BroadcastReceiver implements Constant {


    @Override
    public void onReceive(Context context, Intent intent) {

        Toast.makeText(context, "网络变化了", Toast.LENGTH_SHORT).show();
        //如果有网络了，说明之前连接断开过，需要重新连接
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetWork = cm.getActiveNetworkInfo();
        if (activeNetWork != null) {//说明有可用的网络
            boolean isConnected = activeNetWork.isConnected();
            if (isConnected) {
                Intent intentNet = new Intent();
                intentNet.setAction(ACTION_RECONNECTION);
                intentNet.putExtra("netIsConnected", isConnected);
                context.sendBroadcast(intentNet);
            }


        }


//            ConnectivityManager cm =
//                    (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
//
//        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
//        boolean isConnected = activeNetwork.isConnectedOrConnecting();
//        确定互联网连接的类型


    }
}
