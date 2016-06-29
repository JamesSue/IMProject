package com.chi.im;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.RemoteException;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.chi.im.constant.Constant;
import com.chi.im.service.XmppConnectionImp;

import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.tcp.XMPPTCPConnectionConfiguration;


/**
 * Created by Administrator on 2016/5/16.
 */
public class RegistActivity extends Activity implements View.OnClickListener, Constant {
    private Button btnSubmit;
    private EditText etAccount, edtPwd;
    private XmppConnectionImp xmppConnectionImp;
    private String account, pwd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_regist);

        initView();
        btnSubmit.setOnClickListener(this);

    }

    private void initView() {
        btnSubmit = (Button) findViewById(R.id.btnSubmit);
        etAccount = (EditText) findViewById(R.id.etAccount);
        edtPwd = (EditText) findViewById(R.id.edtPwd);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnSubmit:
                account = etAccount.getText().toString().trim();
                if (account == null || account.isEmpty()) {
                    Toast.makeText(this, "请输入用户名", Toast.LENGTH_SHORT).show();
                    return;
                }
                pwd = edtPwd.getText().toString().trim();
                if (pwd == null || pwd.isEmpty()) {
                    Toast.makeText(this, "请输入密码", Toast.LENGTH_SHORT).show();
                    return;
                }
                new RegistAsynicTask().execute();
        }

    }

    class RegistAsynicTask extends AsyncTask<String[], Void, Boolean> {
        @Override
        protected Boolean doInBackground(String[]... params) {
            Boolean isLoginScusess = false;
            // Create a connection to the jabber.org server on a specific port.
            XMPPTCPConnectionConfiguration config = XMPPTCPConnectionConfiguration.builder()
//                    .setUsernameAndPassword(strAcctount, strPwd)
                    .setServiceName("ZGC-20141118TDU")
                    .setHost(IP)
                    .setPort(5222)
                    .setSecurityMode(ConnectionConfiguration.SecurityMode.disabled)
                    .build();

            xmppConnectionImp = new XmppConnectionImp(config, null, null, RegistActivity.this);
            isLoginScusess = xmppConnectionImp.createAccount(account, pwd);

            return isLoginScusess;
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);
            Toast.makeText(RegistActivity.this, "注册----》" + aBoolean, Toast.LENGTH_SHORT).show();

        }

    }


}
