package com.ecksday.borrowtracker;

import android.app.DialogFragment;
import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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

import static com.ecksday.borrowtracker.MainActivity.User_Id_Holder;

public class FriendsFragment extends Fragment implements AddFriendDialogFragment.AddFriendDialogListener,RemoveFriendDialogFragment.RemoveFriendDialogListener {

    static RequestQueue requestQueue;
    static String HttpAddFriendUrl = "http://ecksday.com/btadmin/AddFriend.php";
    static String HttpRemoveFriendUrl = "http://ecksday.com/btadmin/RemoveFriend.php";
    static String HttpRetrieveFriendsUrl = "http://ecksday.com/btadmin/RetrieveFriends.php";
    static FriendsFriendListAdapter mAdapter=null;
    FloatingActionButton floatingActionButton;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View RootView = inflater.inflate(R.layout.fragment_friends, container, false);

        requestQueue = Volley.newRequestQueue(getContext());

        RecyclerView recyclerView = (RecyclerView) RootView.findViewById(R.id.friends_recycler_view);
        floatingActionButton = (FloatingActionButton) RootView.findViewById(R.id.floatingActionButton);


        List<Friend> friendsList = fetchFriendsList(requestQueue);


        mAdapter = new FriendsFriendListAdapter(friendsList, new FriendsFriendListAdapter.OnItemClickListener() {
            @Override public void onItemClick(Friend friend) {

                DialogFragment newFragment = RemoveFriendDialogFragment.newInstance(friend);
                newFragment.setTargetFragment(FriendsFragment.this, 0);
                newFragment.show(getFragmentManager(), "remove");

            }

        });
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(mLayoutManager);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(), mLayoutManager.getOrientation());
        recyclerView.addItemDecoration(dividerItemDecoration);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment newFragment = new AddFriendDialogFragment();
                newFragment.setTargetFragment(FriendsFragment.this, 0);
                newFragment.show(getFragmentManager(), "add");
            }
        });

        return RootView;
    }

    @Override
    public void onAddDialogPositiveClick(final DialogFragment dialog, final String friendEmail) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, HttpAddFriendUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String stringResponse) {
                if(stringResponse.equals("Friend Added")){
                    Log.e("Add Friend", "Successful");
                }
                else if(stringResponse.equals("Could not connect to database!")) {
                    Log.e("Add Friend", "Connection Failure");
                }
                else if(stringResponse.equals("User does not exist!")) {
                    Log.e("Add Friend", "User does not exist!");
                }
                else if(stringResponse.equals("User already a friend!")) {
                    Log.e("Add Friend", "User already a friend!");
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
                parameters.put("friend_email", friendEmail);
                return parameters;
            }
        };
        requestQueue.add(stringRequest);
    }

    @Override
    public void onAddDialogNegativeClick(DialogFragment dialog) {
        // User touched the dialog's negative button
        dialog.dismiss();
    }

    @Override
    public void onRemoveDialogPositiveClick(final DialogFragment dialog, final Friend friend){

        StringRequest stringRequest = new StringRequest(Request.Method.POST, HttpRemoveFriendUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String stringResponse) {
                if(stringResponse.equals("Friend Removed")){
                    Log.e("Remove Friend", "Successful");
                }
                else if(stringResponse.equals("Could not connect to database!")) {
                    Log.e("Remove Friend", "Connection Failure");
                }
                else if(stringResponse.equals("User does not exist!")) {
                    Log.e("Remove Friend", "User does not exist!");
                }
                else if(stringResponse.equals("User not in friend list!")) {
                    Log.e("Remove Friend", "User not in friend list!");
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
                parameters.put("friend_id", friend.getFriend_id());
                return parameters;
            }
        };
        requestQueue.add(stringRequest);
    }

    @Override
    public void onRemoveDialogNegativeClick(final DialogFragment dialog){
        // User touched the dialog's negative button
        dialog.dismiss();
    }

    public static List<Friend> fetchFriendsList(final RequestQueue requestQueue) {

        final List<Friend> friendsList = new ArrayList<>();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, HttpRetrieveFriendsUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String stringResponse) {
                if(stringResponse.equals("No friends. FeelsBadMan :gun:")){
                }
                else {
                    try {
                        JSONArray jFriends = new JSONArray(stringResponse);
                        for (int i = 0; i < jFriends.length(); i++) {
                            Friend friend= new Friend();
                            JSONObject jFriend = jFriends.getJSONObject(i);
                            String friend_id = jFriend.getString("friend_id");
                            String friend_firstname = jFriend.getString(("friend_firstname"));
                            String friend_lastname = jFriend.getString(("friend_lastname"));
                            friend.setFriend_id(friend_id);
                            friend.setFriend_firstname(friend_firstname);
                            friend.setFriend_lastname(friend_lastname);
                            friendsList.add(friend);
                        }

                        if(mAdapter!=null) {
                            mAdapter.notifyDataSetChanged();
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
        return friendsList;
    }
}
