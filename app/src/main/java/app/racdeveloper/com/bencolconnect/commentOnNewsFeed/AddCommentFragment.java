package app.racdeveloper.com.bencolconnect.commentOnNewsFeed;


import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import app.racdeveloper.com.bencolconnect.Constants;
import app.racdeveloper.com.bencolconnect.QueryPreferences;
import app.racdeveloper.com.bencolconnect.R;


@SuppressLint("ValidFragment")
public class AddCommentFragment extends Fragment {

    EditText addCommentEditText;
    String feedID;
    @SuppressLint("ValidFragment")
    public AddCommentFragment(String feedID) {
        this.feedID = feedID;
        Log.i("pppp", this.feedID+"");
    }

    interface ListUpdateListener{
        void listRefreshed();
    }
    ListUpdateListener listener;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
//        Activity activity= null;
//        if(context instanceof Activity)
//            activity= (Activity) context;
        this.listener= (ListUpdateListener) context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_add_comment, container, false);

        addCommentEditText = (EditText) v.findViewById(R.id.addCommentEditText);
        addCommentEditText.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View view, MotionEvent motionEvent) {
                    addCommentEditText.setHint("");
                    return false;
                }
        });

        Button addCommentButton = (Button) v.findViewById(R.id.addCommentButton);
        addCommentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (addCommentEditText.getText().toString() != null) {
                    submitComment(addCommentEditText.getText().toString());
                }
            }
        });
        return v;
    }

    public void submitComment(String comment) {

        final RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        String url = Constants.URL + "newsFeeds/comments/post";
        String token = QueryPreferences.getToken(getContext());
//        String feedID = getActivity().getIntent().getExtras().getString("feedID");
//        String token = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOiIxNDA0MzEwMDM1IiwiaXNzIjoiaHR0cDpcL1wvbG9jYWxob3N0XC9sYXJhdmVsLXByb2plY3RzXC93ZWJhcGlcL3B1YmxpY19odG1sXC9hcGlcL2xvZ2luIiwiaWF0IjoxNDc5OTU1NTgyLCJleHAiOjE1MTE0OTE1ODIsIm5iZiI6MTQ3OTk1NTU4MiwianRpIjoiOTY5MDQyZTgzYmRkMTk0ZGZmNzg2MTdiMDA5YjlhZjAifQ.viRCvuaYYTTF2CBCedyYuw-U4a3BpEYUAYf6Jyjm-Co";
//        String feedID="2";
        Map<String, String> param = new HashMap<String, String>();

        Log.i("pppp", feedID+"");

        param.put("feedID",feedID);
        param.put("token", token);
        param.put("content", comment);
        JsonObjectRequest jor = new JsonObjectRequest(Request.Method.POST, url, new JSONObject(param), new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                try {

                    String result = jsonObject.getString("result");

                    if (result.equals("success")) {
                        addCommentEditText.setText("");
                        Toast.makeText(getContext(), "Your comment is successfully submitted", Toast.LENGTH_SHORT).show();
                        listener.listRefreshed();
                    } else {
                        String error = jsonObject.getString("error");
                        Toast.makeText(getContext(), "Failed to add comment because of " + error, Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

            }
        });
        requestQueue.add(jor);
    }
}
