package com.ecksday.borrowtracker;

import android.app.DialogFragment;
import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

/**
 * Created by G551JK-DM053H on 13-10-2017.
 */

public class BLViewFragment extends Fragment implements TransactionDialogFragment.TransactionDialogListener {

    TextView OweTextView, OweNumView;
    Button GiveButton, CollectButton;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View RootView = inflater.inflate(R.layout.fragment_b_l_view, container, false);

        OweTextView = (TextView)RootView.findViewById(R.id.owe_text_view);
        OweNumView = (TextView)RootView.findViewById(R.id.owe_num_view);
        GiveButton = (Button)RootView.findViewById(R.id.give_button);
        CollectButton = (Button)RootView.findViewById(R.id.collect_button);



        GiveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment newFragment = new TransactionDialogFragment();
                newFragment.setTargetFragment(BLViewFragment.this, 0);
                newFragment.show(getFragmentManager(), "give");
            }
        });

        CollectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment newFragment = new TransactionDialogFragment();
                newFragment.setTargetFragment(BLViewFragment.this, 0);
                newFragment.show(getFragmentManager(), "collect");
            }
        });

        return RootView;
    }

    @Override
    public void onDialogPositiveClick(DialogFragment dialog, String friendName, float transactionAmount) {

        // User touched the dialog's positive button
        float owedAmount = Float.parseFloat(OweNumView.getText().toString());
        if(dialog.getTag().equals("give")){
            owedAmount +=transactionAmount;
        }
        else if(dialog.getTag().equals("collect")){
            owedAmount -=transactionAmount;
        }

        OweNumView.setText(String.valueOf(owedAmount));

    }

    @Override
    public void onDialogNegativeClick(DialogFragment dialog) {
        // User touched the dialog's negative button
        dialog.dismiss();
    }

}
