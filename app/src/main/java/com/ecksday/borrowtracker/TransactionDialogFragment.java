package com.ecksday.borrowtracker;

import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.text.InputFilter;
import android.text.Spanned;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import fr.ganfra.materialspinner.MaterialSpinner;

import static com.ecksday.borrowtracker.FriendsFragment.fetchFriendsList;

/**
 * Created by G551JK-DM053H on 13-10-2017.
 */

public class TransactionDialogFragment extends DialogFragment {

    RequestQueue requestQueue;

    List<Friend> friendsList = new ArrayList<>();
    String selectedFriend_id;

    public interface TransactionDialogListener {
        void onDialogPositiveClick(DialogFragment dialog, Friend friend, Money transactionAmount);
        void onDialogNegativeClick(DialogFragment dialog);
    }

    // Use this instance of the interface to deliver action events
    TransactionDialogListener mListener = (TransactionDialogListener) getTargetFragment();

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        // Get the layout inflater
        LayoutInflater inflater = getActivity().getLayoutInflater();

        final View RootView = inflater.inflate(R.layout.fragment_transaction_dialog, null);
        requestQueue = Volley.newRequestQueue(getActivity());

        friendsList = fetchFriendsList(requestQueue);

        final Spinner friendPicker = (MaterialSpinner)RootView.findViewById(R.id.friend_picker);

        ArrayAdapter<Friend> dataAdapter = new ArrayAdapter<Friend>(getActivity(), android.R.layout.simple_spinner_item, friendsList);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        friendPicker.setAdapter(dataAdapter);

        final EditText TransactionAmount = (EditText) RootView.findViewById(R.id.transaction_amount);
        TransactionAmount.setFilters(new InputFilter[] {new CurrencyFormatInputFilter()});

        builder.setMessage("Enter Details")
                .setView(RootView)
                // Add action buttons
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        BigDecimal modelVal = new BigDecimal(TransactionAmount.getText().toString());
                        BigDecimal roundedVal = modelVal.setScale(2, RoundingMode.HALF_EVEN);
                        Money transactionAmount = Money.rupees(roundedVal);
                        ((TransactionDialogListener)getTargetFragment()).onDialogPositiveClick(TransactionDialogFragment.this, (Friend)friendPicker.getSelectedItem(), transactionAmount);
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        ((TransactionDialogListener)getTargetFragment()).onDialogNegativeClick(TransactionDialogFragment.this);

                    }
                });
        return builder.create();
    }

    /*// Override the Fragment.onAttach() method to instantiate the NoticeDialogListener
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        // Verify that the host activity implements the callback interface
        try {
            // Instantiate the NoticeDialogListener so we can send events to the host
            mListener = (TransactionDialogListener) context;
        } catch (ClassCastException e) {
            // The activity doesn't implement the interface, throw exception
            throw new ClassCastException(context.toString()
                    + " must implement TransactionDialogListener");
        }
    }*/

    public class CurrencyFormatInputFilter implements InputFilter {

        Pattern mPattern = Pattern.compile("(0|[1-9]+[0-9]*)?(\\.[0-9]{0,2})?");

        @Override
        public CharSequence filter(
                CharSequence source,
                int start,
                int end,
                Spanned dest,
                int dstart,
                int dend) {

            String result =
                    dest.subSequence(0, dstart)
                            + source.toString()
                            + dest.subSequence(dend, dest.length());

            Matcher matcher = mPattern.matcher(result);

            if (!matcher.matches()) return dest.subSequence(dstart, dend);

            return null;
        }
    }

}
