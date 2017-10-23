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

import com.android.volley.RequestQueue;

/**
 * Created by G551JK-DM053H on 13-10-2017.
 */

public class BLViewFragment extends Fragment implements TransactionDialogFragment.TransactionDialogListener {

    RequestQueue requestQueue;
    TextView POY_Text_View, POY_Num_View, YOP_Text_View, YOP_Num_View;
    Button GiveButton, CollectButton;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View RootView = inflater.inflate(R.layout.fragment_b_l_view, container, false);

        POY_Text_View = (TextView)RootView.findViewById(R.id.poy_text_view);
        POY_Num_View = (TextView)RootView.findViewById(R.id.poy_num_view);
        YOP_Text_View = (TextView)RootView.findViewById(R.id.yop_text_view);
        YOP_Num_View = (TextView)RootView.findViewById(R.id.yop_num_view);
        GiveButton = (Button)RootView.findViewById(R.id.give_button);
        CollectButton = (Button)RootView.findViewById(R.id.collect_button);

        POY_Text_View.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        YOP_Text_View.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

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
    public void onDialogPositiveClick(DialogFragment dialog, String friend_id, float transactionAmount) {
        /*requestQueue = Volley.newRequestQueue(getActivity());
        StringRequest stringRequest = new StringRequest(Request.Method.POST, HttpRetrieveFriendsUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String stringResponse) {
                if(stringResponse.equals("No friends. FeelsBadMan :gun:")){
                    Snackbar snackbar = Snackbar
                            .make(getView(), stringResponse, Snackbar.LENGTH_LONG);
                    snackbar.show();
                }
                else {
                    try {

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

        requestQueue.add(stringRequest);*/


        // User touched the dialog's positive button
        if(dialog.getTag().equals("give")){
            float owedAmount = Float.parseFloat(YOP_Num_View.getText().toString());
            if(owedAmount>0f) {
                owedAmount -= transactionAmount;
                if(owedAmount>=0) {
                    setYOP(owedAmount);
                }
                else {
                    setYOP(0f);
                    setPOY(Math.abs(owedAmount));
                }
            }
            else {
                owedAmount = Float.parseFloat(POY_Num_View.getText().toString());
                owedAmount+=transactionAmount;
                setPOY(owedAmount);
            }
        }

        else if(dialog.getTag().equals("collect")){
            float owedAmount = Float.parseFloat(POY_Num_View.getText().toString());
            if(owedAmount>0f) {
                owedAmount -= transactionAmount;
                if(owedAmount>=0) {
                    setPOY(owedAmount);
                }
                else {
                    setPOY(0f);
                    setYOP(Math.abs(owedAmount));
                }
            }
            else {
                owedAmount = Float.parseFloat(YOP_Num_View.getText().toString());
                owedAmount+=transactionAmount;
                setYOP(owedAmount);
            }
        }



    }

    @Override
    public void onDialogNegativeClick(DialogFragment dialog) {
        // User touched the dialog's negative button
        dialog.dismiss();
    }

    private void setPOY(float owedAmount) {
        POY_Num_View.setText(String.valueOf(owedAmount));
    }

    private void setYOP(float owedAmount){

        YOP_Num_View.setText(String.valueOf(owedAmount));
    }

}
