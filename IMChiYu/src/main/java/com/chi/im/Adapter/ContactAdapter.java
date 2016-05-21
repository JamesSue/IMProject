package com.chi.im.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.chi.im.R;
import com.chi.im.model.Friend;

import java.util.List;

/**
 * Created by Administrator on 2016/5/20.
 */
public class ContactAdapter extends BaseAdapter {
    private List<Friend> friends;
    private Context context;
    private LayoutInflater inflater;

    public ContactAdapter(List<Friend> friends, Context context){
        this.friends=friends;
        this.context=context;
        inflater=LayoutInflater.from(context);

    }

    @Override
    public int getCount() {
        return friends.size();
    }

    @Override
    public Object getItem(int position) {
        return friends.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }
    public void update(List<Friend> all){
        this.friends=all;
        notifyDataSetChanged();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        convertView= inflater.inflate(R.layout.fragment_contact_item,null);
        TextView tvName= (TextView) convertView.findViewById(R.id.tvName);
        TextView tvUser= (TextView) convertView.findViewById(R.id.tvUser);
        Friend friendItem=friends.get(position);
        tvName.setText(friendItem.getName());
        tvUser.setText(friendItem.getUser());
        return convertView;
    }
}
