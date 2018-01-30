package com.ecksday.borrowtracker;

import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

/**
 * Created by G551JK-DM053H on 10-12-2017.
 */

public class AddFriendDialogFragment extends DialogFragment {

    RequestQueue requestQueue;

    public interface AddFriendDialogListener {
        void onAddDialogPositiveClick(DialogFragment dialog, String friendEmail);
        void onAddDialogNegativeClick(DialogFragment dialog);
    }

    // Use this instance of the interface to deliver action events
    AddFriendDialogFragment.AddFriendDialogListener mListener = (AddFriendDialogFragment.AddFriendDialogListener) getTargetFragment();

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        // Get the layout inflater
        LayoutInflater inflater = getActivity().getLayoutInflater();

        final View RootView = inflater.inflate(R.layout.fragment_add_friend_dialog, null);
        requestQueue = Volley.newRequestQueue(getActivity());


        final EditText NewFriendEmail = (EditText) RootView.findViewById(R.id.enter_new_friend_email);

        builder.setMessage("Add Friend")
                .setView(RootView)
                // Add action buttons
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        ((AddFriendDialogFragment.AddFriendDialogListener)getTargetFragment()).onAddDialogPositiveClick(AddFriendDialogFragment.this, NewFriendEmail.toString());
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        ((AddFriendDialogFragment.AddFriendDialogListener)getTargetFragment()).onAddDialogNegativeClick(AddFriendDialogFragment.this);

                    }
                });
        return builder.create();
    }


}
