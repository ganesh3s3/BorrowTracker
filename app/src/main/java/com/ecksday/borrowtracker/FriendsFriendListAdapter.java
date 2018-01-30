package com.ecksday.borrowtracker;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

public class FriendsFriendListAdapter extends RecyclerView.Adapter<FriendsFriendListAdapter.MyViewHolder> {

    public interface OnItemClickListener {

        void onItemClick(Friend friend);

    }

    private List<Friend> friendsList;
    private final OnItemClickListener listener;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView friend_fullname;

        public MyViewHolder(View view) {
            super(view);
            friend_fullname = (TextView) view.findViewById(R.id.friend_fullname);
        }

        public void bind(final Friend friend, final OnItemClickListener listener) {

            friend_fullname.setText(friend.toString());

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override public void onClick(View v) {

                    listener.onItemClick(friend);

                }

            });
        }

    }

    public FriendsFriendListAdapter(List<Friend> friendsList, OnItemClickListener listener) {
        this.friendsList = friendsList;
        this.listener = listener;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.friends_friend_list_row, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        holder.bind(friendsList.get(position), listener);
    }

    @Override
    public int getItemCount() {
        return friendsList.size();
    }
}
