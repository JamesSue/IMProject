package com.chi.im;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.RemoteException;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.chi.im.Utils.FileUtils;
import com.chi.im.Utils.Utils;
import com.chi.im.constant.Constant;
import com.chi.im.model.User;
import com.chi.im.service.XmppConnectionImp;

import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.compress.packet.Compress;
import org.jivesoftware.smack.roster.Roster;
import org.jivesoftware.smack.roster.RosterEntry;
import org.jivesoftware.smack.roster.RosterGroup;
import org.jivesoftware.smack.roster.packet.RosterPacket;
import org.jivesoftware.smack.tcp.XMPPTCPConnectionConfiguration;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Created by Administrator on 2016/5/16.
 */
public class LoginActivity extends Activity implements  View.OnClickListener  , Constant {
    private Button btnSubmit, btnRegist;
    private EditText etAccount,etPwd;
    String  strAcctount;
    String  strPwd;
    String serviceName="online.chiyu";
    LoginSucessesBroadcast  loginBroadcast;
    XmppConnectionImp xmppConnectionImp;
    private List<User>  friends=new ArrayList<>();
    User friendItem;
    private Handler mHandler;



//    @Override
//    public void onCreate(Bundle savedInstanceState, PersistableBundle persistentState) {
    //这个重写的方法是干啥的？不明白
//        super.onCreate(savedInstanceState, persistentState);
//        setContentView(R.layout.activity_login);
//        initView();
//
//        loginBroadcast=new LoginSucessesBroadcast();
//        IntentFilter filter=new IntentFilter(LOGIN_SUCCESS);
//        registerReceiver(loginBroadcast,filter);
//
//
//        btnSubmit.setOnClickListener(this);
//    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initView();

        loginBroadcast=new LoginSucessesBroadcast();
        IntentFilter filter=new IntentFilter(LOGIN_SUCCESS);
        registerReceiver(loginBroadcast,filter);
        btnSubmit.setOnClickListener(this);


//        mHandler=new Handler(){
//            @Override
//            public void handleMessage(Message msg) {
//                super.handleMessage(msg);
//                Toast.makeText(LoginActivity.this,"授权成功",Toast.LENGTH_LONG).show();
//                LoginActivity.this.sendBroadcast(new Intent(LOGIN_SUCCESS));
//
//                Roster roster=xmppConnectionImp.getRoster();
//                Set<RosterEntry> set=roster.getEntries();
//                for(RosterEntry entry:set){
//                    String name=entry.getName();
//                    String user=entry.getUser();
//                    RosterPacket.ItemType type=entry.getType();
//                    RosterPacket.ItemStatus status=entry.getStatus();
//                    List<RosterGroup> group=entry.getGroups();
//                    friendItem=new User();
//                    friendItem.setName(name);
//                    friendItem.setUser(user);
//                    friends.add(friendItem);
//                    Log.d(TAG,"name-->"+name);
//                    Log.d(TAG,"user-->"+user);
//                }
//
//                Roster.getInstanceFor(xmppConnectionImp.connection);
//            }
//        };



    }

    private void initView(){
        etAccount=(EditText) findViewById(R.id.etAccount);
        etPwd=(EditText) findViewById(R.id.etPwd);
        btnSubmit= (Button) findViewById(R.id.btnSubmit);
        btnRegist = (Button) findViewById(R.id.btnRegist);

        btnRegist.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnSubmit:
                  strAcctount=etAccount.getText().toString().trim();
                  strPwd=etPwd.getText().toString().trim();
                //点击登录 ，我就保存
                FileUtils.getInstance().setUserName(strAcctount,LoginActivity.this);
//                String  cc=FileUtils.getInstance().getUserName(LoginActivity.this);
//                Utils.showToast(cc,LoginActivity.this);




                //登陆
//                String  strAcctount=etAccount.getText().toString().trim();
//                String  strPwd=etPwd.getText().toString().trim();
//                String [] data =new String [2];
//                data[0]=strAcctount;
//                data[1]=strPwd;
                  new LoaginAsynicTask().execute();
                  break;
            case R.id.btnRegist://注册
                startActivity(new Intent(LoginActivity.this, RegistActivity.class));
                break;
        }

    }
    class LoaginAsynicTask extends AsyncTask<String[] ,Void ,Boolean>{
        @Override
        protected Boolean doInBackground(String[]... params) {
            Boolean isLoginScusess=false;



            // Create a connection to the jabber.org server on a specific port.
            XMPPTCPConnectionConfiguration config = XMPPTCPConnectionConfiguration.builder()
//                    .setUsernameAndPassword(strAcctount, strPwd)
                    .setServiceName("ZGC-20141118TDU")
                    .setHost(IP)
                    .setPort(5222)
                    .setSecurityMode(ConnectionConfiguration.SecurityMode.disabled)
                    .build();
//            SASLAuthentication.blacklistSASLMechanism("DIGEST-MD5");
//            XmppConnectionImp xmppConnectionImp=new XmppConnectionImp(config);
            try {
                xmppConnectionImp = new XmppConnectionImp(config, strAcctount, strPwd, LoginActivity.this);
                 xmppConnectionImp.login();
            } catch (RemoteException e) {
                e.printStackTrace();
            }
            return isLoginScusess;
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);

        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(loginBroadcast!=null){
            unregisterReceiver(loginBroadcast);
        }
    }

    class LoginSucessesBroadcast extends BroadcastReceiver{

        @Override
        public void onReceive(Context context, Intent intent) {
            if(intent!=null){
                if(intent.getAction().equals(LOGIN_SUCCESS)){
                    //登录成功了，那么保存数据
                    Toast.makeText(LoginActivity.this,"success",Toast.LENGTH_LONG);
                    Intent loginIntent=new Intent(LoginActivity.this,MainActivity.class);
                    User user=new User();
                    user.setName(strAcctount);
                    loginIntent.putExtra("user",user);
                    startActivity(loginIntent);
                }
            }
        }
    }
}
