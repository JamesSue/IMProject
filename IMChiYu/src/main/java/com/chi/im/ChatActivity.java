package com.chi.im;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.chi.im.Utils.FileUtils;
import com.chi.im.constant.Constant;
import com.chi.im.db.MySqliteOpenhelper;
import com.chi.im.model.MessageYu;
import com.chi.im.model.User;

import org.jivesoftware.smack.packet.Message;

import java.util.ArrayList;
import java.util.List;

public class ChatActivity extends Activity  implements View.OnClickListener,Constant{
    private TextView tvTitle;
    private ImageView ivBack;
    private User friend;
    private String friendName;
    private EditText etInput;
    private Button btnSend;
    private String  jid;
    private ListView listView;
    private MyChatAdapter chatAdapter;
    private List<MessageYu> all = new ArrayList<MessageYu>();
    private LayoutInflater inflater;
    private String myUserName;
    private ReceivedAMsgYuReceiver receiver;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        init();
        friend= (User) getIntent().getSerializableExtra("friend");
        friendName=friend.getName();
        //
        myUserName = FileUtils.getInstance().getUserName(this);
        jid=friend.getUser();
        tvTitle.setText(myUserName + "(wo)" + "\t\t\t\t" + friendName + "\t\t\t\t" + "聊天");
        inflater = LayoutInflater.from(this);

        chatAdapter = new MyChatAdapter();
        listView.setAdapter(chatAdapter);


        //读取数据库中的聊天记录
        MyApplication application = (MyApplication) this.getApplicationContext();
        MySqliteOpenhelper sqliteOpenhelper = application.sqliteOpenhelper;
        all.addAll(sqliteOpenhelper.getAll());
        Toast.makeText(this, sqliteOpenhelper.getAll().size() + "个", Toast.LENGTH_LONG).show();
        chatAdapter.notifyDataSetChanged();

        //注册广播
        receiver = new ReceivedAMsgYuReceiver();
        IntentFilter filter = new IntentFilter(ACTION_RECEOVE_A_MESSAGE);
        registerReceiver(receiver, filter);
    }


    private void init() {
        tvTitle= (TextView) findViewById(R.id.tvTitle);
        ivBack= (ImageView) findViewById(R.id.ivBack);
        listView = (ListView) findViewById(R.id.listView);



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
                //将消息保存到数据库中，然后再发送消息

                String date = System.currentTimeMillis() + "";
                String type = "1";
                String from = myUserName;
                String to = jid;
                String body = msgInput;

                //先将数据保存到数据库
                MessageYu msgYu = new MessageYu(date, type, body, to, from);
                MyApplication application = (MyApplication) getApplicationContext();
                application.sqliteOpenhelper.insetData(msgYu);
                //然后更新聊天界面
                all.add(msgYu);
                chatAdapter.notifyDataSetChanged();
                listView.setSelection(all.size() - 1);


                //通过广播 发送消息
                Intent  intentSendMsg=new Intent(ACTION_SEND_MESSAGE);
                intentSendMsg.putExtra("jid",jid);
                intentSendMsg.putExtra("msgInput",msgInput);
                sendBroadcast(intentSendMsg);
                etInput.setText("");
                break;
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (receiver != null) {
            unregisterReceiver(receiver);
        }
    }

    class MyChatAdapter extends BaseAdapter {


        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public Object getItem(int position) {
            return all.get(position);
        }

        @Override
        public int getCount() {
            return all.size();
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            MessageYu msg = all.get(position);
            String from = msg.getFrom();
            String name = null;
            if (from.contains("@")) {
                name = from.substring(0, from.indexOf("@"));
            } else {
                name = from;
            }

            boolean isMe = false;
            //
            if (name.equals(myUserName)) {
                convertView = inflater.inflate(R.layout.layout_chat_right, null);
                isMe = true;

            } else {
                isMe = false;
//                name=myUserName;
                convertView = inflater.inflate(R.layout.layout_chat_left, null);
            }

            TextView tvName = (TextView) convertView.findViewById(R.id.tvName);
            TextView tvBody = (TextView) convertView.findViewById(R.id.tvBody);

            if (isMe) {
                tvName.setText(myUserName);
            } else {
                tvName.setText(name);
            }
            tvName.setText(name);
            tvBody.setText(msg.getBody());


            return convertView;
        }
    }


    class ReceivedAMsgYuReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {

            MessageYu messageYu = (MessageYu) intent.getSerializableExtra("msgYu");
            all.add(messageYu);
            chatAdapter.notifyDataSetChanged();
            listView.setSelection(all.size() - 1);
        }
    }


}
