package com.chi.im.fragment;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.chi.im.Adapter.ContactAdapter;
import com.chi.im.ChatActivity;
import com.chi.im.R;
import com.chi.im.constant.Constant;
import com.chi.im.model.User;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentContact extends Fragment implements Constant {
    private ListView  listView;
    private ContactAdapter contactAdapter;
    private List<User> friends=new ArrayList<User>();
    private GetRespRosterBroadcast  broadcast;


    public FragmentContact() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_contact, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        listView= (ListView) view.findViewById(R.id.list_friend);
        contactAdapter=new ContactAdapter(friends,getActivity());
        listView.setAdapter(contactAdapter);

        //注册广播
        broadcast=new GetRespRosterBroadcast();
        IntentFilter filter=new IntentFilter(ACTION_RESP_CONTACTS);
        getActivity().registerReceiver(broadcast,filter);


        //获取花名册
        Intent  intent =new Intent(ACTION_REQ_CONTACTS);
        getActivity().sendBroadcast(intent);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
               User friend= friends.get(position);
                Intent  intent=new Intent(getActivity(), ChatActivity.class);
                intent.putExtra("friend",friend);
                startActivity(intent);
            }
        });

    }

    private class GetRespRosterBroadcast extends BroadcastReceiver{
        @Override
        public void onReceive(Context context, Intent intent) {
            String  action=intent.getAction();
            if(action.equals(ACTION_RESP_CONTACTS)){
                friends=(List<User>) intent.getSerializableExtra("friends");
                contactAdapter.update(friends);
                Toast.makeText(context,"=============="+friends.size(),Toast.LENGTH_LONG).show();
            }


        }
    }
}

