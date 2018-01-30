package com.ecksday.borrowtracker;

import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

/**
 * Created by G551JK-DM053H on 10-12-2017.
 */

public class RemoveFriendDialogFragment extends DialogFragment {
    Friend friend;
    RequestQueue requestQueue;

    public static RemoveFriendDialogFragment newInstance(Friend receivedFriend) {
        RemoveFriendDialogFragment fragment = new RemoveFriendDialogFragment();

        Bundle args = new Bundle();
        fragment.setArguments(args);
        fragment.setFriend(receivedFriend);
        return fragment;
    }

    private void setFriend(Friend receivedFriend){
        this.friend = receivedFriend;
    }

    public interface RemoveFriendDialogListener {
        void onRemoveDialogPositiveClick(DialogFragment dialog, Friend friend);
        void onRemoveDialogNegativeClick(DialogFragment dialog);
    }

    // Use this instance of the interface to deliver action events
    RemoveFriendDialogFragment.RemoveFriendDialogListener mListener = (RemoveFriendDialogFragment.RemoveFriendDialogListener) getTargetFragment();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        // Get the layout inflater
        LayoutInflater inflater = getActivity().getLayoutInflater();

        final View RootView = inflater.inflate(R.layout.fragment_simple_yesno_dialog, null);
        requestQueue = Volley.newRequestQueue(getActivity());

        builder.setMessage("Are you sure you want to unfriend " + friend.toString() + "?")
                .setView(RootView)
                // Add action buttons
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        ((RemoveFriendDialogFragment.RemoveFriendDialogListener)getTargetFragment()).onRemoveDialogPositiveClick(RemoveFriendDialogFragment.this, friend);
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        ((RemoveFriendDialogFragment.RemoveFriendDialogListener)getTargetFragment()).onRemoveDialogNegativeClick(RemoveFriendDialogFragment.this);

                    }
                });
        return builder.create();
    }


}
