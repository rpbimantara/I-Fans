package com.example.nerita_hendra.i_fans;


import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.youtube.player.YouTubeApiServiceUtil;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerFragment;
import com.google.android.youtube.player.YouTubePlayerSupportFragment;
import com.google.android.youtube.player.YouTubePlayerView;
import com.google.android.youtube.player.YouTubeStandalonePlayer;
import com.google.android.youtube.player.YouTubeThumbnailLoader;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class LiveFragment extends Fragment {

    YouTubePlayerView playerView;
    private final String API_KEY= "AIzaSyDxk3vNgkIDFu-Ai0FhbDvg2N7Mpfql3TY";
    private final String VIDEO_CODE= "W7m1n9jL7ns";
    String URL = "https://www.googleapis.com/youtube/v3/search?part=snippet&" +
            "channelId=UCBcdDjzrNIeIZvATS-l9Wjg&maxResults=50&key=" +
            API_KEY;
    ArrayList<Live> videoDetailsArrayList;

    public LiveFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_live, container, false);
        YouTubePlayerSupportFragment playerFragment = YouTubePlayerSupportFragment.newInstance();
        FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
        transaction.add(R.id.youtube_live,playerFragment).commit();


//        playerView = (YouTubePlayerView) view.findViewById(R.id.youtube_live);
        playerFragment.initialize(API_KEY, new YouTubePlayer.OnInitializedListener() {
            @Override
            public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean b) {
                if(!b){
                    youTubePlayer.loadVideo(VIDEO_CODE);
                    youTubePlayer.setPlayerStyle(YouTubePlayer.PlayerStyle.DEFAULT);
                }
            }

            @Override
            public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {

            }
        });
        new LoadVideoTask().execute();
        return view;
    }


    public class LoadVideoTask extends AsyncTask<Void,Void,Void>{
        @Override
        protected Void doInBackground(Void... voids) {
            videoDetailsArrayList = new ArrayList<>();
            RequestQueue requestQueue = Volley.newRequestQueue(getActivity().getApplicationContext());
            StringRequest stringRequest = new StringRequest(Request.Method.GET, URL, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        JSONArray jsonArray = jsonObject.getJSONArray("items");
                        System.out.println("JSON ARRAY"+jsonArray.length());
                        for (int i=0;i<jsonArray.length();i++){
                            JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                            JSONObject jsonVideoId = jsonObject1.getJSONObject("id");
                            JSONObject jsonSnippet = jsonObject1.getJSONObject("snippet");
                            JSONObject jsonObjectdefault = jsonSnippet.getJSONObject("thumbnails").getJSONObject("medium");

                            videoDetailsArrayList.add(new Live(
                                    jsonSnippet.getString("title"),
                                    jsonSnippet.getString("description"),
                                    jsonObjectdefault.getString("url"),
                                    jsonVideoId.getString("videoId")));
                        }
                    }catch (JSONException err){
                        err.printStackTrace();
                    }

                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                }
            });
            int socketTimeout = 30000;
            RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
            stringRequest.setRetryPolicy(policy);
            requestQueue.add(stringRequest);
            return null;
        }
    }

}
