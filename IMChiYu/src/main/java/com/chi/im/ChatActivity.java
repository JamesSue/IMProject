package com.chi.im;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.chi.im.Utils.FileUtils;
import com.chi.im.constant.Constant;
import com.chi.im.model.User;

public class ChatActivity extends Activity  implements View.OnClickListener,Constant{
    private TextView tvTitle;
    private ImageView ivBack;
    private User friend;
    private String friendName;
    private EditText etInput;
    private Button btnSend;
    private String  jid;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        init();
        friend= (User) getIntent().getSerializableExtra("friend");
        friendName=friend.getName();
        //
        String userName=FileUtils.getInstance().getUserName(this);
        jid=friend.getUser();
        tvTitle.setText(userName+"(wo)"+"\t\t\t\t"+friendName+"\t\t\t\t"+"聊天");

    }

    private void init() {
        tvTitle= (TextView) findViewById(R.id.tvTitle);
        ivBack= (ImageView) findViewById(R.id.ivBack);
        ivBack.setOnClickListener(this);
        etInput= (EditText) findViewById(R.id.etInput);
        btnSend=(Button) findViewById(R.id.btnSend);

        btnSend.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.ivBack:
                finish();
                break;
            case R.id.btnSend:
                //发送消息
                String msgInput=etInput.getText().toString().trim();
                if(msgInput==null){
                    return;
                }
                //通过广播 发送消息
                Intent  intentSendMsg=new Intent(ACTION_SEND_MESSAGE);
                intentSendMsg.putExtra("jid",jid);
                intentSendMsg.putExtra("msgInput",msgInput);
                sendBroadcast(intentSendMsg);
                break;
        }

    }
}
