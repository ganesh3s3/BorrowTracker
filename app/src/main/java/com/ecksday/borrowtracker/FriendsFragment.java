package com.ecksday.borrowtracker;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
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

public class FriendsFragment extends Fragment {

    static RequestQueue requestQueue;
    static String HttpRetrieveFriendsUrl = "http://ecksday.com/btadmin/RetrieveFriends.php";

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View RootView = inflater.inflate(R.layout.fragment_friends, container, false);

        requestQueue = Volley.newRequestQueue(getContext());

        return RootView;
    }

    public static List<Friend> fetchFriendsList(final RequestQueue requestQueue, final String User_Id_Holder) {

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
