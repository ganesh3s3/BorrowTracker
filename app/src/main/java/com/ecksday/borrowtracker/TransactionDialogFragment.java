package com.ecksday.borrowtracker;

import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.text.InputFilter;
import android.text.Spanned;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import fr.ganfra.materialspinner.MaterialSpinner;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by G551JK-DM053H on 13-10-2017.
 */

public class TransactionDialogFragment extends DialogFragment {

    RequestQueue requestQueue;
    String HttpRetrieveFriendsUrl = "http://ecksday.com/btadmin/RetrieveFriends.php";
    List<String> friendsList = new ArrayList<>();
    private HashMap<String, String> hmapcat = new HashMap<>();
    String friend_id;

    SharedPreferences sharedPreferences;
    String User_Id_Holder;

    public interface TransactionDialogListener {
        public void onDialogPositiveClick(DialogFragment dialog, String FriendId, float transactionAmount);
        public void onDialogNegativeClick(DialogFragment dialog);
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
        sharedPreferences = getActivity().getSharedPreferences("logindetails",MODE_PRIVATE);
        User_Id_Holder= sharedPreferences.getString("user_id","");

        final Spinner friendPicker = (MaterialSpinner)RootView.findViewById(R.id.friend_picker);
        friendPicker.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {

                String friend_full_name = friendPicker.getSelectedItem().toString();
                Log.i("Selected item : ", friend_full_name);
                friend_id=hmapcat.get(friend_full_name );  // here you will get ids
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {

            }

        });
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, friendsList);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        friendPicker.setAdapter(dataAdapter);

        final EditText TransactionAmount = (EditText) RootView.findViewById(R.id.transaction_amount);
        TransactionAmount.setFilters(new InputFilter[] {new CurrencyFormatInputFilter()});

        StringRequest stringRequest = new StringRequest(Request.Method.POST, HttpRetrieveFriendsUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String stringResponse) {
                if(stringResponse.equals("No friends. FeelsBadMan :gun:")){
                    Snackbar snackbar = Snackbar
                            .make(RootView, stringResponse, Snackbar.LENGTH_LONG);
                    snackbar.show();
                    friendPicker.setEnabled(false);
                    TransactionAmount.setEnabled(false);
                }
                else {
                    try {
                        JSONArray jFriends = new JSONArray(stringResponse);
                        for (int i = 0; i < jFriends.length(); i++) {
                            JSONObject jFriend = jFriends.getJSONObject(i);
                            String friend_id = jFriend.getString("user_id_2");
                            String friend_full_name = jFriend.getString(("friend_full_name"));
                            friendsList.add(friend_full_name);
                            hmapcat.put(friend_full_name, friend_id);
                        }
                    }
                    catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }

        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                NetworkResponse errorRes = error.networkResponse;
                String stringData = "";
                try{
                    if(errorRes != null && errorRes.data != null){
                        stringData = new String(errorRes.data,"UTF-8");
                    }}
                catch (UnsupportedEncodingException e){

                }
                Log.e("Error",stringData);
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String, String> parameters = new HashMap<String, String>();
                parameters.put("user_id", User_Id_Holder);
                return parameters;
            }
        };

        requestQueue.add(stringRequest);






        builder.setMessage("Enter Details")
                .setView(RootView)
                // Add action buttons
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        final float transactionAmount = Float.parseFloat(TransactionAmount.getText().toString());
                        ((TransactionDialogListener)getTargetFragment()).onDialogPositiveClick(TransactionDialogFragment.this,friend_id, transactionAmount);
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
