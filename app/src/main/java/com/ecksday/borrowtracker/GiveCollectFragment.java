package com.ecksday.borrowtracker;

import android.app.DialogFragment;
import android.app.Fragment;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.CardView;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

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
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.ecksday.borrowtracker.MainActivity.User_Id_Holder;

/**
 * Created by G551JK-DM053H on 13-10-2017.
 */

public class GiveCollectFragment extends Fragment implements TransactionDialogFragment.TransactionDialogListener {

    SharedPreferences sharedPreferences;
    RequestQueue requestQueue;
    TextView POY_Num_View, YOP_Num_View;
    CardView POY_Card, YOP_Card;
    Button GiveButton, CollectButton;
    String HttpTransactionUrl = "http://ecksday.com/btadmin/Transaction.php";
    String HttpRetrieveFriendTotalsUrl = "http://ecksday.com/btadmin/RetrieveFriendTotals.php";

    private List<Friend> friendsList = new ArrayList<>();;
    private OweFriendListAdapter mAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View RootView = inflater.inflate(R.layout.fragment_give_collect, container, false);

        requestQueue = Volley.newRequestQueue(getActivity());
        POY_Card = (CardView) RootView.findViewById(R.id.POY_card);
        POY_Num_View = (TextView)RootView.findViewById(R.id.poy_num_view);
        YOP_Card = (CardView) RootView.findViewById(R.id.YOP_card);
        YOP_Num_View = (TextView)RootView.findViewById(R.id.yop_num_view);
        GiveButton = (Button)RootView.findViewById(R.id.give_button);
        CollectButton = (Button)RootView.findViewById(R.id.collect_button);

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());

        RecyclerView recyclerView = (RecyclerView) RootView.findViewById(R.id.owe_recycler_view);

        mAdapter = new OweFriendListAdapter(friendsList);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(mLayoutManager);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(), mLayoutManager.getOrientation());
        recyclerView.addItemDecoration(dividerItemDecoration);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);

        POY_Card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                prepareTotalList(0);
            }
        });

        YOP_Card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                prepareTotalList(1);
            }
        });

        GiveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment newFragment = new TransactionDialogFragment();
                newFragment.setTargetFragment(GiveCollectFragment.this, 0);
                newFragment.show(getFragmentManager(), "give");
            }
        });

        CollectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment newFragment = new TransactionDialogFragment();
                newFragment.setTargetFragment(GiveCollectFragment.this, 0);
                newFragment.show(getFragmentManager(), "collect");
            }
        });

        updateOweNum();

        return RootView;
    }

    @Override
    public void onDialogPositiveClick(final DialogFragment dialog, final Friend friend, final Money transactionAmount) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, HttpTransactionUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String stringResponse) {
                if(stringResponse.equals("Transaction successful!")){
                    Log.e("Transaction Status", "Successful");
                    updateOweNum();
                }
                else if(stringResponse.equals("Could not connect to database!")) {
                    Log.e("Transaction Status", "Failure");
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
                parameters.put("user_id_1", User_Id_Holder);
                parameters.put("user_id_2", friend.getFriend_id());
                parameters.put("transaction_amount", transactionAmount.getAmount().toString());
                Log.e("transactionAmount",transactionAmount.getAmount().toString());
                if(dialog.getTag().equals("give")){
                    parameters.put("transaction_code", "1");
                }
                else if(dialog.getTag().equals("collect")){
                    parameters.put("transaction_code", "0");
                }
                return parameters;
            }
        };
        requestQueue.add(stringRequest);
    }

    @Override
    public void onDialogNegativeClick(DialogFragment dialog) {
        // User touched the dialog's negative button
        dialog.dismiss();
    }


    private void prepareTotalList(final int cardClicked) {
        friendsList.clear();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, HttpRetrieveFriendTotalsUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String stringResponse) {
                if(stringResponse.equals("No transactions found!")){
                    Snackbar snackbar = Snackbar
                            .make(getView(), stringResponse, Snackbar.LENGTH_LONG);
                    snackbar.show();
                }
                else {
                    try{
                        JSONArray jTotals = new JSONArray(stringResponse);
                        for (int i = 0; i < jTotals.length(); i++) {
                            Friend friend = new Friend();
                            JSONObject jTotal = jTotals.getJSONObject(i);
                            String friend_firstname = jTotal.getString("user_firstname");
                            String friend_lastname = jTotal.getString("user_lastname");
                            String current_total_string = jTotal.getString("current_total");
                            BigDecimal current_total = new BigDecimal(current_total_string);
                            Money current_total_money = Money.rupees(current_total.abs());
                            if(cardClicked==0 && current_total.compareTo(BigDecimal.ZERO)>0) {
                                friend.setFriend_firstname(friend_firstname);
                                friend.setFriend_lastname(friend_lastname);
                                friend.setCurrent_total(current_total_money);
                                friendsList.add(friend);
                            }
                            else if(cardClicked==1 && current_total.compareTo(BigDecimal.ZERO)<0){
                                friend.setFriend_firstname(friend_firstname);
                                friend.setFriend_lastname(friend_lastname);
                                friend.setCurrent_total(current_total_money);
                                friendsList.add(friend);
                            }
                        }
                        mAdapter.notifyDataSetChanged();
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

    }


    private void updateOweNum(){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, HttpRetrieveFriendTotalsUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String stringResponse) {
                if(stringResponse.equals("No transactions found!")){
                    Snackbar snackbar = Snackbar
                            .make(getView(), stringResponse, Snackbar.LENGTH_LONG);
                    snackbar.show();
                }
                else {
                    try{
                        JSONArray jTotals = new JSONArray(stringResponse);
                        BigDecimal YOP_Num= new BigDecimal("0");
                        BigDecimal POY_Num = new BigDecimal("0");
                        for (int i = 0; i < jTotals.length(); i++) {
                            JSONObject jTotal = jTotals.getJSONObject(i);
                            String current_total_string = jTotal.getString("current_total");
                            BigDecimal current_total = new BigDecimal(current_total_string);
                            if(current_total.compareTo(BigDecimal.ZERO)>0){
                                POY_Num = POY_Num.add(current_total);
                            }
                            else {
                                YOP_Num = YOP_Num.add(current_total);
                            }
                        }
                        Money POY_Money = Money.rupees(POY_Num);
                        Money YOP_Money = Money.rupees(YOP_Num.abs());
                        POY_Num_View.setText(POY_Money.toString());
                        YOP_Num_View.setText(YOP_Money.toString());
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
    }
}