package com.alpha.test.i_fans;


import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.ArrayList;

import oogbox.api.odoo.OdooClient;
import oogbox.api.odoo.client.OdooVersion;
import oogbox.api.odoo.client.helper.OdooErrorException;
import oogbox.api.odoo.client.helper.data.OdooRecord;
import oogbox.api.odoo.client.helper.data.OdooResult;
import oogbox.api.odoo.client.helper.utils.ODomain;
import oogbox.api.odoo.client.helper.utils.OdooFields;
import oogbox.api.odoo.client.listeners.IOdooResponse;
import oogbox.api.odoo.client.listeners.OdooConnectListener;


/**
 * A simple {@link Fragment} subclass.
 */
public class MatchLineUpAwayFragment extends Fragment {

    private View rootView;
    RecyclerView rvLineUpAwayCore,rvLineUpAway;
    int RecyclerViewItemPosition ;
    SwipeRefreshLayout swiper;
    ArrayList<MatchLineUp> ArrayListMatchLineUpAwayCore;
    ArrayList<MatchLineUp> ArrayListMatchLineUpAway;
    OdooClient client;
    SharedPrefManager sharedPrefManager;
    AdapterLineUpHome adapterCore;
    AdapterLineUpHome adapter;

    public MatchLineUpAwayFragment() {
        // Required empty public constructor
    }

    public static MatchLineUpAwayFragment newInstance() {
        Bundle args = new Bundle();
        MatchLineUpAwayFragment fragment = new MatchLineUpAwayFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        if (rootView==null){
            rootView = inflater.inflate(R.layout.fragment_match_line_up_away, container, false);
            rvLineUpAway = rootView.findViewById(R.id.rv_recycler_view_match_line_up_away);
            rvLineUpAwayCore = rootView.findViewById(R.id.rv_recycler_view_match_line_up_away_core);
            swiper = rootView.findViewById(R.id.swiperefresh_match_line_up_away);
            swiper.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {
                    new LineUpAwayTask().execute();
                }
            });
            sharedPrefManager = new  SharedPrefManager(getContext());
            adapter = new AdapterLineUpHome(ArrayListMatchLineUpAway);
            adapterCore = new AdapterLineUpHome(ArrayListMatchLineUpAwayCore);
            rvLineUpAway.setAdapter(adapter);
            rvLineUpAway.setLayoutManager(new LinearLayoutManager(getActivity()));
            rvLineUpAwayCore.setAdapter(adapterCore);
            rvLineUpAwayCore.setLayoutManager(new LinearLayoutManager(getActivity()));
            rvLineUpAwayCore.addOnItemTouchListener(new RecyclerView.OnItemTouchListener() {

                GestureDetector gestureDetector = new GestureDetector(getActivity(), new GestureDetector.SimpleOnGestureListener() {

                    @Override
                    public boolean onSingleTapUp(MotionEvent motionEvent) {

                        return true;
                    }

                });

                @Override
                public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
                    View ChildView = rv.findChildViewUnder(e.getX(), e.getY());

                    if (ChildView != null && gestureDetector.onTouchEvent(e)) {
                        RecyclerViewItemPosition = rv.getChildAdapterPosition(ChildView);
                        Intent intent = new Intent(getActivity(), RatingLineUpActivity.class);
                        intent.putExtra("id_jadwal", ArrayListMatchLineUpAwayCore.get(RecyclerViewItemPosition).getJadwal_id());
                        intent.putExtra("id_player", ArrayListMatchLineUpAwayCore.get(RecyclerViewItemPosition).getPlayer_id());
//                        cekRating(Integer.valueOf(ArrayListMatchLineUpAwayCore.get(RecyclerViewItemPosition).getJadwal_id()),Integer.valueOf(ArrayListMatchLineUpAwayCore.get(RecyclerViewItemPosition).getPlayer_id()));
                        startActivity(intent);
                    }
                    return false;
                }

                @Override
                public void onTouchEvent(RecyclerView rv, MotionEvent e) {
                }

                @Override
                public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

                }
            });
            new LineUpAwayTask().execute();

        }
        return rootView;
    }

    public class LineUpAwayTask extends AsyncTask<Void,Void,Void>{
        @Override
        protected void onPreExecute() {
            swiper.setRefreshing(true);
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            ArrayListMatchLineUpAway = new ArrayList<>();
            ArrayListMatchLineUpAwayCore = new ArrayList<>();
            client = new OdooClient.Builder(getContext())
                    .setHost(sharedPrefManager.getSP_Host_url())
                    .setSession(sharedPrefManager.getSpSessionId())
                    .setSynchronizedRequests(false)
                    .setConnectListener(new OdooConnectListener() {
                        @Override
                        public void onConnected(OdooVersion version) {
                            ODomain domain = new ODomain();
                            domain.add("jadwal_id", "=",getActivity().getIntent().getExtras().get("id_jadwal"));

                            OdooFields fields = new OdooFields();
                            fields.addAll("id","jadwal_id","home", "player_id","department_id","job_id","no_punggung","club_id","status_pemain");

                            int offset = 0;
                            int limit = 0;

                            String sorting = "id DESC";

                            client.searchRead("persebaya.line.up.away", domain, fields, offset, limit, sorting, new IOdooResponse() {
                                @Override
                                public void onResult(OdooResult result) {
                                    OdooRecord[] records = result.getRecords();
                                    for (OdooRecord record : records) {
                                        if (record.getString("status_pemain").equalsIgnoreCase("core")){
                                            ArrayListMatchLineUpAwayCore.add(new MatchLineUp(
                                                    String.valueOf(record.getInt("id")),
                                                    String.valueOf(record.getInt("jadwal_id")),
                                                    String.valueOf(record.getInt("player_id")),
                                                    record.getString("player_id"),
                                                    String.valueOf(Math.round(record.getFloat("no_punggung"))),
                                                    record.getString("job_id"),
                                                    record.getString("club_id"),
                                                    record.getString("status_pemain")
                                            ));
                                        }else {
                                            ArrayListMatchLineUpAway.add(new MatchLineUp(
                                                    String.valueOf(record.getInt("id")),
                                                    String.valueOf(record.getInt("jadwal_id")),
                                                    String.valueOf(record.getInt("player_id")),
                                                    record.getString("player_id"),
                                                    String.valueOf(Math.round(record.getFloat("no_punggung"))),
                                                    record.getString("job_id"),
                                                    record.getString("club_id"),
                                                    record.getString("status_pemain")
                                            ));
                                        }
                                    }
                                    adapter = new AdapterLineUpHome(ArrayListMatchLineUpAway);
                                    adapterCore = new AdapterLineUpHome(ArrayListMatchLineUpAwayCore);
                                    rvLineUpAway.setAdapter(adapter);
                                    rvLineUpAwayCore.setAdapter(adapterCore);
                                    adapter.notifyDataSetChanged();
                                    adapterCore.notifyDataSetChanged();
                                    swiper.setRefreshing(false);
                                }
                            });
                        }
                    }).build();
            return null;

        }
    }

    public void cekRating(final Integer id_jadwal, final Integer id_player){
        client = new OdooClient.Builder(getContext())
                .setHost(sharedPrefManager.getSP_Host_url())
                .setSession(sharedPrefManager.getSpSessionId())
                .setSynchronizedRequests(false)
                .setConnectListener(new OdooConnectListener() {
                    @Override
                    public void onConnected(OdooVersion version) {
                        ODomain domain = new ODomain();
                        domain.add("employee_id", "=", id_player);
                        domain.add("create_uid", "=", sharedPrefManager.getSpIdUser());

                        OdooFields fields = new OdooFields();
                        fields.addAll("id");

                        int offset = 0;
                        int limit = 80;

                        String sorting = "id DESC";

                        client.searchRead("persebaya.rating", domain, fields, offset, limit, sorting, new IOdooResponse() {
                            @Override
                            public void onResult(OdooResult result) {
                                OdooRecord[] records = result.getRecords();
                                for (OdooRecord record : records) {
                                    if (record.getInt("id") > 0 ){
                                        Toast.makeText(getContext(),"Rating has been created!",Toast.LENGTH_LONG).show();
                                    }else{
//                                        intent = new Intent(getActivity(), RatingLineUpActivity.class);
//                                        intent.putExtra("id_jadwal", id_jadwal);
//                                        intent.putExtra("id_player", id_player);
                                    }
                                }

                            }

                            @Override
                            public boolean onError(OdooErrorException error) {
                                Toast.makeText(getContext(),error.toString(),Toast.LENGTH_LONG).show();
                                return true;
                            }
                        });
                    }
                }).build();
    }


}
