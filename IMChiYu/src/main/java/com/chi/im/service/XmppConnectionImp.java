package com.chi.im.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;
import android.widget.Toast;

import com.chi.im.LoginActivity;
import com.chi.im.MyApplication;
import com.chi.im.Utils.Utils;
import com.chi.im.constant.Constant;
import com.chi.im.db.MySqliteOpenhelper;
import com.chi.im.model.MessageYu;
import com.chi.im.model.User;
import com.chi.im.service.aidl.IXmppConnection;
import com.chi.im.smack.UserExtensionInfo;

import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.ConnectionListener;
import org.jivesoftware.smack.MessageListener;
import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.chat.Chat;
import org.jivesoftware.smack.chat.ChatManager;
import org.jivesoftware.smack.chat.ChatManagerListener;
import org.jivesoftware.smack.chat.ChatMessageListener;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.Stanza;
import org.jivesoftware.smack.provider.ProviderManager;
import org.jivesoftware.smack.roster.Roster;
import org.jivesoftware.smack.roster.RosterEntry;
import org.jivesoftware.smack.roster.RosterGroup;
import org.jivesoftware.smack.roster.packet.RosterPacket;
import org.jivesoftware.smack.tcp.XMPPTCPConnection;
import org.jivesoftware.smack.tcp.XMPPTCPConnectionConfiguration;
import org.jivesoftware.smackx.iqregister.AccountManager;
import org.jivesoftware.smackx.receipts.DeliveryReceipt;
import org.jivesoftware.smackx.receipts.DeliveryReceiptManager;
import org.jivesoftware.smackx.receipts.DeliveryReceiptRequest;
import org.jivesoftware.smackx.receipts.ReceiptReceivedListener;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;



/**
 * xmpp连接
 * Created by Administrator on 2016/5/16.
 */
public class XmppConnectionImp  implements IXmppConnection,Constant{

    public  XMPPTCPConnection connection;
    private String strAccount, strPwd, serviceName;
    private  MConnectionListner connectionListner;
    private ConnectionConfiguration config;
    private String  TAG="imchiyu";
    private ConnectionConfiguration.Builder builder;
    private Context mContext;

    private GetRosterBroadCast  getRosterBroadCast;
    private User friendItem;
    private List<User> friends=new ArrayList<User>();
    private  MyNewMessageListener myNewMessageListener;
    private Cursor cursor;

    private ReconnectionBroadCastReceive reconnctionReceive;





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

    public XmppConnectionImp(XMPPTCPConnectionConfiguration config, String strAccount, String strPwd, Context context) {
        this.strAccount = strAccount;
        this.strPwd = strPwd;
        this.config=config;
        this.mContext=context;
        if (connection == null) {
            connection = new XMPPTCPConnection(config);
        }
        if(getRosterBroadCast==null){
            getRosterBroadCast=new GetRosterBroadCast();
            IntentFilter intentFilter=new IntentFilter();
            intentFilter.addAction(ACTION_REQ_CONTACTS);
            intentFilter.addAction(ACTION_SEND_MESSAGE);
            intentFilter.addAction(ACTION_DISCONNECT);
            context.registerReceiver(getRosterBroadCast,intentFilter);
        }

        if (reconnctionReceive == null) {
            reconnctionReceive = new ReconnectionBroadCastReceive();
            IntentFilter intentFilterNet = new IntentFilter();
            intentFilterNet.addAction(ACTION_RECONNECTION);
            context.registerReceiver(reconnctionReceive, intentFilterNet);
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
        //登录，
        if (connection.isConnected() || connection.isAuthenticated()) {
            connection.disconnect();
        }
        connect();
    }

    public boolean createAccount(String accountRegist, String pwdRegist) {
        Log.d(TAG, "createAccount----------》");
        if (connection == null) {
            Log.d(TAG, "createAccount----------》connection=null");
            return false;
        }
        if (!connection.isConnected()) {
            Log.d(TAG, "createAccount----------》connection  is  not connected");

        }

        try {
            Log.d(TAG, "createAccount----------》connection  开始连接........");
            connection.connect();
        } catch (SmackException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (XMPPException e) {
            e.printStackTrace();
        }

        if(connection.isConnected()){
            Log.d(TAG, "createAccount----------》connection  连接成功");
        }else{
            Log.d(TAG, "createAccount----------》connection   连接失败........");
            return false;
        }


        AccountManager accountManager = AccountManager.getInstance(connection);
        try {
            accountManager.createAccount(accountRegist, pwdRegist);
            return true;
        } catch (SmackException.NoResponseException e) {
            e.printStackTrace();
        } catch (XMPPException.XMPPErrorException e) {
            e.printStackTrace();
        } catch (SmackException.NotConnectedException e) {
            e.printStackTrace();
        } catch (Exception e) {
            return false;
        }
        return false;
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
                connection.login(strAccount, strPwd);
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
                mContext.sendBroadcast(new Intent(LOGIN_SUCCESS));
                incomeChat();
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
                    friendItem=new User();
                    friendItem.setName(name);
                    friendItem.setUser(user);
                    friends.add(friendItem);

                }
                //发送广播，把联系人发送出去
                Intent  intentFriends=new Intent(ACTION_RESP_CONTACTS);
                intentFriends.putExtra("friends", (Serializable) friends);
                mContext.sendBroadcast(intentFriends);
                Toast.makeText(mContext,"收到获取roster请求",Toast.LENGTH_LONG).show();

            }else if(action.equals(ACTION_SEND_MESSAGE)){//说明有消息发送
                Intent  intentSendMsg=new Intent(ACTION_SEND_MESSAGE);
                String msgInput=intent.getStringExtra("msgInput");
                String jid=intent.getStringExtra("jid");
                //调用发送消息的方法
                UserExtensionInfo userExtensionInfo= (UserExtensionInfo) intentSendMsg.getSerializableExtra("userExtensionInfo");
                sendInputMessage(jid,msgInput,userExtensionInfo);

            } else if (action.equals(ACTION_DISCONNECT)) {
                if (connection != null && connection.isConnected()) {
                    connection.disconnect();
                }
            }
        }
    }

    //发送消息
    private void sendInputMessage(String jid, String msgInput, UserExtensionInfo userExtensionInfo){
        if(connection!=null&&connection.isConnected()){
            Chat chat=ChatManager.getInstanceFor(connection).createChat(jid, new ChatMessageListener() {
                @Override
                public void processMessage(Chat chat, Message message) {

                }
            });
            try {
//                if(!connection.isConnected()){
//                    try {
//                        Utils.showToast("connect开始重连", mContext);
//                        connect();
//                    } catch (RemoteException e) {
//                        e.printStackTrace();
//                    }
//                }
                if (connection == null) {
                    Utils.showToast("conn==null", mContext);
                }

//                Utils.showToast(connection.isConnected()+"",mContext);

                if(!connection.isConnected()){
                    Utils.showToast("conn 没连接------》", mContext);
                }
//                chat.sendMessage(msgInput);
//                //测试自定义的message,添加参数
                Message message=new Message();
//                if(userExtensionInfo!=null){
//                    message.addExtension(userExtensionInfo);
//                }

                UserExtensionInfo extensionInfo=new UserExtensionInfo();
                extensionInfo.setHeadUrl("www.hao123.com");
                extensionInfo.setMotto("有志者事竟成");
                message.addExtension(extensionInfo);
                message.setBody(msgInput);

                DeliveryReceiptRequest  drr=new DeliveryReceiptRequest();
                message.addExtension(drr);
//                ProviderManager p
//                String messageId=message.getStanzaId();
//                DeliveryReceipt deliveryReceipt=new DeliveryReceipt(messageId);
//                message.addExtension(deliveryReceipt);
                //添加回执请求
//                DeliveryReceiptRequest.addTo(message);
//                connection.sendStanza(message);

//                DeliveryReceiptManager.addDeliveryReceiptRequest(message);
//                DeliveryReceiptManager deliveryReceiptManager=   DeliveryReceiptManager.getInstanceFor(connection);
//                deliveryReceiptManager.autoAddDeliveryReceiptRequests();
//                deliveryReceiptManager.addReceiptReceivedListener(new ReceiptReceivedListener(){
//                    @Override
//                    public void onReceiptReceived(String s, String s1, String s2, Stanza stanza) {
//                        Log.d(TAG,"===========================================================");
//                        Log.d(TAG,s);
//                        Log.d(TAG,s1);
//                        Log.d(TAG,s2);
//                        Log.d(TAG,s2);
//                        Log.d(TAG,stanza.toXML().toString());
//                        Log.d(TAG,"===========================================================");
//
//                    }
//                });

                Log.d(TAG,"**********发送的数据*********\n\n\n"+message.toString()+"\n\n");
                chat.sendMessage(message);
            } catch (SmackException.NotConnectedException e) {
                e.printStackTrace();
            }

        }

    }

    //接收消息
    private  void incomeChat(){
        ChatManager  chatManager=ChatManager.getInstanceFor(connection);
        chatManager.addChatListener(new ChatManagerListener() {
            @Override
            public void chatCreated(Chat chat, boolean b) {
                if(!b){
//                    showToastMain();
                    chat.addMessageListener(new MyNewMessageListener());
                }

            }
        });

    }
    class  MyNewMessageListener     implements ChatMessageListener{
        @Override
        public void processMessage(Chat chat, Message message) {
            Log.d(TAG,"收到消息了\n\n\n"+message.toString()+"\n\n");
//            Log.d(TAG, "Ｉｎｃｏｍｅ－－body－＞" + "/n/n" + message.());
//            Log.d(TAG, "Ｉｎｃｏｍｅ－－frome－＞" + "/n/n" + message.getFrom());
            //收到消息了，收到消息后进行保到数据库
            //如果收到的消息内容不是null 那么回执 ，我收到了消息了
            String  body=message.getBody();
            String from = message.getFrom();
            String to = message.getTo();
            if(body!=null){
                DeliveryReceipt receipt=new DeliveryReceipt(message.getStanzaId());
                message.addExtension(receipt);
                try {
                    chat.sendMessage(message);
                } catch (SmackException.NotConnectedException e) {
                    e.printStackTrace();
                }
            }




//            Date date=new Date();
            String date = System.currentTimeMillis() + "";
            String type;
            if (message.getType().equals(Message.Type.chat)) {
                type = "1";
            } else {
                type = "2";
            }

            //我收到了消息

//            chat.sendMessage();



            //毫秒作为id
//            long   id=System.currentTimeMillis();


            MessageYu msgYu = new MessageYu(date, type, body, to, from);

            MyApplication application = (MyApplication) mContext.getApplicationContext();
//          MySqliteOpenhelper sqliteOpenhelper = application.sqliteOpenhelper;
            MySqliteOpenhelper sqliteOpenhelper = application.getMySqliteOpenhelper();
            //保存到数据库
            sqliteOpenhelper.insetData(msgYu);
            //发送广播，将收到的消息的内容发送到 chatActivty中去
            if (body != null && !body.equals("null")) {
                Intent intent = new Intent(ACTION_RECEOVE_A_MESSAGE);
                intent.putExtra("msgYu", msgYu);
                mContext.sendBroadcast(intent);
            }


//            sqliteOpenhelper.getAll();


        }
    }




    //自己接受自己发的广播，而且传了值 ，网络是否可用
    class ReconnectionBroadCastReceive extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            Boolean netIsConnected = intent.getBooleanExtra("netIsConnected", false);
            if (netIsConnected) {
                new ReconnectionAsynicTask().execute();
            }
        }
    }


    class ReconnectionAsynicTask extends AsyncTask<String[], Void, Boolean> {

        @Override
        protected Boolean doInBackground(String[]... params) {

            if (connection != null) {
                Log.d(TAG, "重新连接---》");
                Log.d(TAG, "重新连接connection---》" + connection.isConnected());
                Log.d(TAG, "重新连接isAuthenticated---》" + connection.isAuthenticated());
                Log.d(TAG, "重新连接isSmAvailable---》" + connection.isSmAvailable());
                try {
                    connection.connect();
                    try {
                        Thread.sleep(4000);
                        connection.connect();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                } catch (SmackException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (XMPPException e) {
                    e.printStackTrace();
                }

            } else {
                Log.d(TAG, "重新连接connection---》null");
            }
            return false;
        }
    }







}
