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

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import fr.ganfra.materialspinner.MaterialSpinner;

/**
 * Created by G551JK-DM053H on 13-10-2017.
 */

public class TransactionDialogFragment extends DialogFragment {

    public interface TransactionDialogListener {
        public void onDialogPositiveClick(DialogFragment dialog, String friendName, float transactionAmount);
        public void onDialogNegativeClick(DialogFragment dialog);
    }

    // Use this instance of the interface to deliver action events
    TransactionDialogListener mListener = (TransactionDialogListener) getTargetFragment();

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        // Get the layout inflater
        LayoutInflater inflater = getActivity().getLayoutInflater();

        View RootView = inflater.inflate(R.layout.fragment_transaction_dialog, null);

        String[] friendArray = {"Mani", "Ananthi", "Devaki"};

        final Spinner locationSpinner = (MaterialSpinner)RootView.findViewById(R.id.friend_picker);
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, friendArray);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        locationSpinner.setAdapter(dataAdapter);

        final EditText TransactionAmount = (EditText) RootView.findViewById(R.id.transaction_amount);
        TransactionAmount.setFilters(new InputFilter[] {new CurrencyFormatInputFilter()});


        builder.setMessage("Enter Details")
                .setView(RootView)
                // Add action buttons
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        final float transactionAmount = Float.parseFloat(TransactionAmount.getText().toString());
                        ((TransactionDialogListener)getTargetFragment()).onDialogPositiveClick(TransactionDialogFragment.this, locationSpinner.getSelectedItem().toString(), transactionAmount);
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
