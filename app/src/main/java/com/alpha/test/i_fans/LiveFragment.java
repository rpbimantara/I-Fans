package com.alpha.test.i_fans;


import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class LiveFragment extends Fragment {

    private View rootView;
//    private static YouTube youtube;
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
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public static LiveFragment newInstance() {
        Bundle args = new Bundle();
        LiveFragment fragment = new LiveFragment();
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        if (rootView == null) {
            rootView = inflater.inflate(R.layout.fragment_live, container, false);
        }

//        YouTubePlayerSupportFragment playerFragment = YouTubePlayerSupportFragment.newInstance();
//        FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
//        transaction.add(R.id.youtube_live,playerFragment).commit();


//        playerView = (YouTubePlayerView) view.findViewById(R.id.youtube_live);
//        playerFragment.initialize(API_KEY, new YouTubePlayer.OnInitializedListener() {
//            @Override
//            public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean b) {
//                if(!b){
//                    youTubePlayer.loadVideo(VIDEO_CODE);
//                    youTubePlayer.setPlayerStyle(YouTubePlayer.PlayerStyle.DEFAULT);
//                }
//            }
//
//            @Override
//            public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {
//
//            }
//        });
//        new LoadVideoTask().execute();
//        callYoutube();
        return rootView;
    }

//    public void callYoutube(){
//
//        List<String> scopes = Lists.newArrayList("https://www.googleapis.com/auth/youtube.readonly");
//        try {
//            // Authorize the request.
//            Credential credential = Auth.authorize(scopes, "myuploads");
//
//            // This object is used to make YouTube Data API requests.
//            youtube = new YouTube.Builder(Auth.HTTP_TRANSPORT, Auth.JSON_FACTORY, credential).setApplicationName(
//                    "youtube-cmdline-myuploads-sample").build();
//
//            // Call the API's channels.list method to retrieve the
//            // resource that represents the authenticated user's channel.
//            // In the API response, only include channel information needed for
//            // this use case. The channel's contentDetails part contains
//            // playlist IDs relevant to the channel, including the ID for the
//            // list that contains videos uploaded to the channel.
//            YouTube.Channels.List channelRequest = youtube.channels().list("contentDetails");
//            channelRequest.setMine(true);
//            channelRequest.setFields("items/contentDetails,nextPageToken,pageInfo");
//            ChannelListResponse channelResult = channelRequest.execute();
//
//            List<Channel> channelsList = channelResult.getItems();
//
//            if (channelsList != null) {
//                // The user's default channel is the first item in the list.
//                // Extract the playlist ID for the channel's videos from the
//                // API response.
//                String uploadPlaylistId =
//                        channelsList.get(0).getContentDetails().getRelatedPlaylists().getUploads();
//
//                // Define a list to store items in the list of uploaded videos.
//                List<PlaylistItem> playlistItemList = new ArrayList<PlaylistItem>();
//
//                // Retrieve the playlist of the channel's uploaded videos.
//                YouTube.PlaylistItems.List playlistItemRequest =
//                        youtube.playlistItems().list("id,contentDetails,snippet");
//                playlistItemRequest.setPlaylistId(uploadPlaylistId);
//
//                // Only retrieve data used in this application, thereby making
//                // the application more efficient. See:
//                // https://developers.google.com/youtube/v3/getting-started#partial
//                playlistItemRequest.setFields(
//                        "items(contentDetails/videoId,snippet/title,snippet/publishedAt),nextPageToken,pageInfo");
//
//                String nextToken = "";
//
//                // Call the API one or more times to retrieve all items in the
//                // list. As long as the API response returns a nextPageToken,
//                // there are still more items to retrieve.
//                do {
//                    playlistItemRequest.setPageToken(nextToken);
//                    PlaylistItemListResponse playlistItemResult = playlistItemRequest.execute();
//
//                    playlistItemList.addAll(playlistItemResult.getItems());
//
//                    nextToken = playlistItemResult.getNextPageToken();
//                } while (nextToken != null);
//
//                // Prints information about the results.
//                prettyPrint(playlistItemList.size(), playlistItemList.iterator());
//            }
//
//        } catch (GoogleJsonResponseException e) {
//            e.printStackTrace();
//            System.err.println("There was a service error: " + e.getDetails().getCode() + " : "
//                    + e.getDetails().getMessage());
//
//        } catch (Throwable t) {
//            t.printStackTrace();
//        }
//    }
//
//    private static void prettyPrint(int size, Iterator<PlaylistItem> playlistEntries) {
//        System.out.println("=============================================================");
//        System.out.println("\t\tTotal Videos Uploaded: " + size);
//        System.out.println("=============================================================\n");
//
//        while (playlistEntries.hasNext()) {
//            PlaylistItem playlistItem = playlistEntries.next();
//            System.out.println(" video name  = " + playlistItem.getSnippet().getTitle());
//            System.out.println(" video id    = " + playlistItem.getContentDetails().getVideoId());
//            System.out.println(" upload date = " + playlistItem.getSnippet().getPublishedAt());
//            System.out.println("\n-------------------------------------------------------------\n");
//        }
//    }

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
