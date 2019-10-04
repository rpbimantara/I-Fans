package com.alpha.test.persebayaapp;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import oogbox.api.odoo.OdooClient;
import oogbox.api.odoo.client.helper.data.OdooRecord;
import oogbox.api.odoo.client.helper.data.OdooResult;
import oogbox.api.odoo.client.helper.utils.ODomain;
import oogbox.api.odoo.client.helper.utils.OdooFields;
import oogbox.api.odoo.client.listeners.IOdooResponse;

import static com.alpha.test.persebayaapp.CommonUtils.getOdooConnection;


/**
 * A simple {@link Fragment} subclass.
 */
public class MatchLineUpHomeFragment extends Fragment {

    private View rootView;
    RecyclerView rvLineUpHomeCore,rvLineUpHome;
    int RecyclerViewItemPosition ;
    SwipeRefreshLayout swiper;
    ArrayList<MatchLineUp> ArrayListMatchLineUpHomeCore;
    ArrayList<MatchLineUp> ArrayListMatchLineUpHome;
    OdooClient client;
    SharedPrefManager sharedPrefManager;
    AdapterLineUpHome adapterCore;
    AdapterLineUpHome adapter;

    public MatchLineUpHomeFragment() {
        // Required empty public constructor
    }

    public static MatchLineUpHomeFragment newInstance() {
        Bundle args = new Bundle();
        MatchLineUpHomeFragment fragment = new MatchLineUpHomeFragment();
        fragment.setArguments(args);
        return fragment;
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        if (rootView==null){
            rootView = inflater.inflate(R.layout.fragment_match_line_up_home, container, false);
            rvLineUpHome = rootView.findViewById(R.id.rv_recycler_view_match_line_up_home);
            rvLineUpHomeCore = rootView.findViewById(R.id.rv_recycler_view_match_line_up_home_core);
            swiper = rootView.findViewById(R.id.swiperefresh_match_line_up_home);
            swiper.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {
                    loadLineUpHome();
//                    new LineUpHomeTask().execute();
                }
            });
            sharedPrefManager = new  SharedPrefManager(getContext());
            rvLineUpHome.setAdapter(adapter);
            rvLineUpHome.setLayoutManager(new LinearLayoutManager(getActivity()));
            rvLineUpHomeCore.setAdapter(adapterCore);
            rvLineUpHomeCore.setLayoutManager(new LinearLayoutManager(getActivity()));
            rvLineUpHomeCore.addOnItemTouchListener(new RecyclerView.OnItemTouchListener() {

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
                        intent.putExtra("id_jadwal", Integer.valueOf(ArrayListMatchLineUpHomeCore.get(RecyclerViewItemPosition).getJadwal_id()));
                        intent.putExtra("id_player", ArrayListMatchLineUpHomeCore.get(RecyclerViewItemPosition).getPlayer_id());
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
            rvLineUpHome.addOnItemTouchListener(new RecyclerView.OnItemTouchListener() {

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
                        intent.putExtra("id_jadwal", Integer.valueOf(ArrayListMatchLineUpHome.get(RecyclerViewItemPosition).getJadwal_id()));
                        intent.putExtra("id_player", ArrayListMatchLineUpHome.get(RecyclerViewItemPosition).getPlayer_id());
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
            client = getOdooConnection(getContext());
            loadLineUpHome();
//            new LineUpHomeTask().execute();

        }
        return rootView;
    }

    public void loadLineUpHome(){
        swiper.setRefreshing(true);
        ArrayListMatchLineUpHome = new ArrayList<>();
        ArrayListMatchLineUpHomeCore = new ArrayList<>();
        ODomain domain = new ODomain();
        domain.add("jadwal_id", "=",getActivity().getIntent().getExtras().get("id_jadwal"));

        OdooFields fields = new OdooFields();
        fields.addAll("id","jadwal_id","home", "player_id","department_id","job_id","no_punggung","status_pemain");

        int offset = 0;
        int limit = 0;

        String sorting = "id DESC";

        client.searchRead("persebaya.line.up.home", domain, fields, offset, limit, sorting, new IOdooResponse() {
            @Override
            public void onResult(OdooResult result) {
                OdooRecord[] records = result.getRecords();
                for (OdooRecord record : records) {
                    if (record.getString("status_pemain").equalsIgnoreCase("core")){
                        ArrayListMatchLineUpHomeCore.add(new MatchLineUp(
                                String.valueOf(record.getInt("id")),
                                String.valueOf(record.getInt("jadwal_id")),
                                String.valueOf(record.getInt("player_id")),
                                record.getString("player_id"),
                                String.valueOf(Math.round(record.getFloat("no_punggung"))),
                                record.getString("job_id"),
                                record.getString("home"),
                                record.getString("status_pemain")
                        ));
                    }else {
                        ArrayListMatchLineUpHome.add(new MatchLineUp(
                                String.valueOf(record.getInt("id")),
                                String.valueOf(record.getInt("jadwal_id")),
                                String.valueOf(record.getInt("player_id")),
                                record.getString("player_id"),
                                String.valueOf(Math.round(record.getFloat("no_punggung"))),
                                record.getString("job_id"),
                                record.getString("home"),
                                record.getString("status_pemain")
                        ));
                    }
                }
                adapter = new AdapterLineUpHome(ArrayListMatchLineUpHome);
                adapterCore = new AdapterLineUpHome(ArrayListMatchLineUpHomeCore);
                rvLineUpHome.setAdapter(adapter);
                rvLineUpHomeCore.setAdapter(adapterCore);
                adapter.notifyDataSetChanged();
                adapterCore.notifyDataSetChanged();
                swiper.setRefreshing(false);
            }
        });
    }

//    public class LineUpHomeTask extends AsyncTask<Void,Void,Void>{
//
//        @Override
//        protected void onPreExecute() {
//            swiper.setRefreshing(true);
//            super.onPreExecute();
//        }
//
//        @Override
//        protected Void doInBackground(Void... voids) {
//            ArrayListMatchLineUpHome = new ArrayList<>();
//            ArrayListMatchLineUpHomeCore = new ArrayList<>();
//            client = new OdooClient.Builder(getContext())
//                    .setHost(sharedPrefManager.getSP_Host_url())
//                    .setSession(sharedPrefManager.getSpSessionId())
//                    .setSynchronizedRequests(false)
//                    .setConnectListener(new OdooConnectListener() {
//                        @Override
//                        public void onConnected(OdooVersion version) {
//                            ODomain domain = new ODomain();
//                            domain.add("jadwal_id", "=",getActivity().getIntent().getExtras().get("id_jadwal"));
//
//                            OdooFields fields = new OdooFields();
//                            fields.addAll("id","jadwal_id","home", "player_id","department_id","job_id","no_punggung","status_pemain");
//
//                            int offset = 0;
//                            int limit = 0;
//
//                            String sorting = "id DESC";
//
//                            client.searchRead("persebaya.line.up.home", domain, fields, offset, limit, sorting, new IOdooResponse() {
//                                @Override
//                                public void onResult(OdooResult result) {
//                                    OdooRecord[] records = result.getRecords();
//                                    for (OdooRecord record : records) {
//                                        if (record.getString("status_pemain").equalsIgnoreCase("core")){
//                                            ArrayListMatchLineUpHomeCore.add(new MatchLineUp(
//                                                    String.valueOf(record.getInt("id")),
//                                                    String.valueOf(record.getInt("jadwal_id")),
//                                                    String.valueOf(record.getInt("player_id")),
//                                                    record.getString("player_id"),
//                                                    String.valueOf(Math.round(record.getFloat("no_punggung"))),
//                                                    record.getString("job_id"),
//                                                    record.getString("home"),
//                                                    record.getString("status_pemain")
//                                            ));
//                                        }else {
//                                            ArrayListMatchLineUpHome.add(new MatchLineUp(
//                                                    String.valueOf(record.getInt("id")),
//                                                    String.valueOf(record.getInt("jadwal_id")),
//                                                    String.valueOf(record.getInt("player_id")),
//                                                    record.getString("player_id"),
//                                                    String.valueOf(Math.round(record.getFloat("no_punggung"))),
//                                                    record.getString("job_id"),
//                                                    record.getString("home"),
//                                                    record.getString("status_pemain")
//                                            ));
//                                        }
//                                    }
//                                    adapter = new AdapterLineUpHome(ArrayListMatchLineUpHome);
//                                    adapterCore = new AdapterLineUpHome(ArrayListMatchLineUpHomeCore);
//                                    rvLineUpHome.setAdapter(adapter);
//                                    rvLineUpHomeCore.setAdapter(adapterCore);
//                                    adapter.notifyDataSetChanged();
//                                    adapterCore.notifyDataSetChanged();
//                                    swiper.setRefreshing(false);
//                                }
//                            });
//                        }
//                    }).build();
//            return null;
//        }
//    }

}
