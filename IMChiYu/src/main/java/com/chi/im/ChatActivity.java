package com.chi.im;

import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.chi.im.Utils.FileUtils;
import com.chi.im.model.User;

public class ChatActivity extends Activity  implements View.OnClickListener{
    private TextView tvTitle;
    private ImageView ivBack;
    private User friend;
    private String friendName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        init();
        friend= (User) getIntent().getSerializableExtra("friend");
        friendName=friend.getName();
        //
        String userName=FileUtils.getInstance().getUserName(this);
        tvTitle.setText(userName+"(wo)"+"\t\t\t\t"+friendName+"\t\t\t\t"+"聊天");

    }

    private void init() {
        tvTitle= (TextView) findViewById(R.id.tvTitle);
        ivBack= (ImageView) findViewById(R.id.ivBack);
        ivBack.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.ivBack:
                finish();
                break;


        }

    }
}
