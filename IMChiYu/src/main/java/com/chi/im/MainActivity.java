package com.chi.im;



import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.chi.im.fragment.FragmentContact;
import com.chi.im.fragment.FragmentMe;

import org.jivesoftware.smack.roster.Roster;

public class MainActivity extends FragmentActivity implements View.OnClickListener {

    private FragmentContact fragmentContact;
    private FragmentMe fragmentMe;
    private LinearLayout frameLayout_main;
    private Button     btnRoster,btnMe;
    private FragmentTransaction transAction;
    private FragmentManager manager;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//初始化
        frameLayout_main= (LinearLayout) findViewById(R.id.frameLayout_main);
        btnRoster= (Button) findViewById(R.id.btnRoster);
        btnMe= (Button) findViewById(R.id.btnMe);

        fragmentContact=new FragmentContact();
        fragmentMe=new FragmentMe();

         manager=getSupportFragmentManager();
        transAction=manager.beginTransaction();
        transAction.add(R.id.frameLayout_main,fragmentContact);
        transAction.add(R.id.frameLayout_main,fragmentMe);
        transAction.commit();

        btnRoster.setOnClickListener(this);
        btnMe.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        FragmentManager manager=getSupportFragmentManager();
        FragmentTransaction transAction =manager.beginTransaction();
        switch (v.getId()){
            case R.id.btnRoster://
//                manager=getSupportFragmentManager();
//                transAction=manager.beginTransaction();
                transAction.show(fragmentContact);
                transAction.hide(fragmentMe);
                transAction.commit();

                break;
            case R.id.btnMe:
//                manager=getSupportFragmentManager();
//                transAction=manager.beginTransaction();
                transAction.show(fragmentMe);
                transAction.hide(fragmentContact);;
                transAction.commit();
                break;
        }

    }
}
