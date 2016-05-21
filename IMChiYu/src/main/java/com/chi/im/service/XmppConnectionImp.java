package com.chi.im.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.RemoteException;
import android.util.Log;
import android.widget.Toast;

import com.chi.im.constant.Constant;
import com.chi.im.model.Friend;
import com.chi.im.service.aidl.IXmppConnection;

import org.jivesoftware.smack.AbstractXMPPConnection;
import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.ConnectionListener;
import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.roster.Roster;
import org.jivesoftware.smack.roster.RosterEntry;
import org.jivesoftware.smack.roster.RosterGroup;
import org.jivesoftware.smack.roster.packet.RosterPacket;
import org.jivesoftware.smack.tcp.XMPPTCPConnection;
import org.jivesoftware.smack.tcp.XMPPTCPConnectionConfiguration;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * xmpp连接
 * Created by Administrator on 2016/5/16.
 */
public class XmppConnectionImp  implements IXmppConnection,Constant{

    private  XMPPTCPConnection connection;
    private  String  account,password,serviceName;
    private  MConnectionListner connectionListner;
    private ConnectionConfiguration config;
    private String  TAG="imchiyu";
    private ConnectionConfiguration.Builder builder;
    private Context mContext;
    private Handler mHandler;
    private GetRosterBroadCast  getRosterBroadCast;
    private Friend friendItem;
    private List<Friend> friends=new ArrayList<Friend>();




//    public XmppConnectionImp(String serviceName, String  account, String  password ){
//        this.serviceName=serviceName;
//        this.account=account;
//        this.password=password;
////        AbstractXMPPConnection connection1 = new XMPPTCPConnection("mtucker", "password", "jabber.org"); connection1.connect().login();
//
//    }

//    public XmppConnectionImp(ConnectionConfiguration config, String  account, String  password ){
//    }

//    public XmppConnectionImp(XMPPTCPConnection connection, String  account, String  password ){
//        this.connection=connection;
//        this.account=account;
//        this.password=password;
//    }

    public XmppConnectionImp(XMPPTCPConnectionConfiguration config ,Context context,Handler mHandler){

        this.config=config;
        this.mContext=context;
        this.mHandler=mHandler;
        connection=new XMPPTCPConnection(config);
        if(getRosterBroadCast==null){
            getRosterBroadCast=new GetRosterBroadCast();
            IntentFilter intentFilter=new IntentFilter(ACTION_REQ_CONTACTS);
            context.registerReceiver(getRosterBroadCast,intentFilter);
        }

    }




    @Override
    public void basicTypes(int anInt, long aLong, boolean aBoolean, float aFloat, double aDouble, String aString) throws RemoteException {

    }

    @Override
    public void connect() throws RemoteException {
        if(connection.isConnected()){
            connection.disconnect();
        }else{
            try {
                if(connectionListner==null){
                    connectionListner=new MConnectionListner();
                }
                connection.addConnectionListener(connectionListner);
                connection.connect();
            } catch (SmackException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (XMPPException e) {
                e.printStackTrace();
            }
        }

    }

    @Override
    public void login() throws RemoteException {
        Log.d(TAG,"this.is my login");
        if(connection.isConnected()){
            Log.d(TAG,"connection--------->isConnected");
            try {
                connection.login();
            } catch (XMPPException e) {
                e.printStackTrace();
            } catch (SmackException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }else{
            Log.d(TAG,"connection--------->isNotConnected");
            connect();
        }


    }

    @Override
    public void disconnect() throws RemoteException {
        if(connection.isConnected()){
            connection.disconnect();
        }
    }

    @Override
    public IBinder asBinder() {
        return null;
    }


    private class MConnectionListner implements  ConnectionListener{


        @Override
        public void connected(XMPPConnection xmppConnection) {
            Log.d(TAG,"connected-->"+xmppConnection);
            //既然连接成功了，那么调用登陆方法
            try {
                connection.login();
            } catch (XMPPException e) {
                e.printStackTrace();
            } catch (SmackException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }


        }

        @Override
        public void authenticated(XMPPConnection xmppConnection, boolean b) {

            Log.d(TAG,"authenticated-->"+xmppConnection+"--->"+b);

            if(xmppConnection.isAuthenticated()){
                mHandler.sendEmptyMessage(1);

                Log.d(TAG,"authenticated---->isAuthenticated");
            }else{
                Log.d(TAG,"authenticated---->is not Authenticated");
            }
        }

        @Override
        public void connectionClosed() {
            Log.d(TAG,"connectionClosed-->");
        }

        @Override
        public void connectionClosedOnError(Exception e) {
            Log.d(TAG,"connectionClosedOnError-->"+e);
        }

        @Override
        public void reconnectionSuccessful() {
            Log.d(TAG,"reconnectionSuccessful-->");
        }

        @Override
        public void reconnectingIn(int i) {
            Log.d(TAG,"reconnectingIn-->"+i);
        }

        @Override
        public void reconnectionFailed(Exception e) {
            Log.d(TAG,"reconnectionFailed-->"+e);
        }
    }

    public  Roster getRoster(){
        Roster  roster=Roster.getInstanceFor(connection);
        Log.d(TAG,roster.toString());
        return roster;
    }

    //写广播  然后发送数据

    class GetRosterBroadCast extends BroadcastReceiver{
        @Override
        public void onReceive(Context context, Intent intent) {
            String action=intent.getAction();
            if(action.equals(ACTION_REQ_CONTACTS)){
                friends.clear();
                Roster roster=getRoster();
                Set<RosterEntry> set=roster.getEntries();
                for(RosterEntry entry:set){
                    String name=entry.getName();
                    String user=entry.getUser();
                    RosterPacket.ItemType type=entry.getType();
                    RosterPacket.ItemStatus status=entry.getStatus();
                    List<RosterGroup> group=entry.getGroups();
                    friendItem=new Friend();
                    friendItem.setName(name);
                    friendItem.setUser(user);
                    friends.add(friendItem);
                    Log.d(TAG,"name-->"+name);
                    Log.d(TAG,"user-->"+user);
                }
                //发送广播，把联系人发送出去
                Intent  intentFriends=new Intent(ACTION_RESP_CONTACTS);
                intentFriends.putExtra("friends", (Serializable) friends);
                mContext.sendBroadcast(intentFriends);
                Toast.makeText(mContext,"收到获取roster请求",Toast.LENGTH_LONG).show();

            }
        }
    }


}
