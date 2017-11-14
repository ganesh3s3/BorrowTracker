package com.ecksday.borrowtracker;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

public class OweFriendListAdapter extends RecyclerView.Adapter<OweFriendListAdapter.MyViewHolder> {

    private List<Friend> friendsList;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView friend_fullname, current_total;

        public MyViewHolder(View view) {
            super(view);
            friend_fullname = (TextView) view.findViewById(R.id.friend_fullname);
            current_total = (TextView) view.findViewById(R.id.current_total);
        }
    }


    public OweFriendListAdapter(List<Friend> friendsList) {
        this.friendsList = friendsList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.owe_friend_list_row, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Friend friend = friendsList.get(position);
        holder.friend_fullname.setText(friend.toString());
        holder.current_total.setText(friend.getCurrent_total().toString());
    }

    @Override
    public int getItemCount() {
        return friendsList.size();
    }
}
