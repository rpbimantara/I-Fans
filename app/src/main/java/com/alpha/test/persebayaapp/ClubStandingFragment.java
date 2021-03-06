package com.alpha.test.persebayaapp;


import android.app.ProgressDialog;
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
import oogbox.api.odoo.client.helper.utils.OArguments;
import oogbox.api.odoo.client.listeners.IOdooResponse;

import static com.alpha.test.persebayaapp.CommonUtils.getOdooConnection;


/**
 * A simple {@link Fragment} subclass.
 */
public class ClubStandingFragment extends Fragment {

    ArrayList<Klasemen> ArrayListKlasemen =  new ArrayList<>();
    int RecyclerViewItemPosition ;
    SharedPrefManager sharedPrefManager;
    ProgressDialog progressDialog;
    View rootView;
    RecyclerView rv;
    RecyclerView.LayoutManager llm;
    SwipeRefreshLayout swiper;
    AdapterKlasemen adapter;
    OdooClient client;

    public ClubStandingFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_club_standing, container, false);
        rv = rootView.findViewById(R.id.rv_recycler_view_standings);
        swiper = rootView.findViewById(R.id.swiperefresh_standings);
        llm = new LinearLayoutManager(getActivity());
        adapter = new AdapterKlasemen(ArrayListKlasemen);
        rv.setAdapter(adapter);
        rv.setLayoutManager(llm);
        client = getOdooConnection(getContext());
        swiper.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadStandings();
            }
        });
        sharedPrefManager = new SharedPrefManager(getActivity());
        rv.addOnItemTouchListener(new RecyclerView.OnItemTouchListener() {

            GestureDetector gestureDetector = new GestureDetector(getActivity(), new GestureDetector.SimpleOnGestureListener() {

                @Override public boolean onSingleTapUp(MotionEvent motionEvent) {

                    return true;
                }

            });

            @Override
            public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
                View ChildView = rv.findChildViewUnder(e.getX(), e.getY());

                if(ChildView != null && gestureDetector.onTouchEvent(e)) {

                    RecyclerViewItemPosition = rv.getChildAdapterPosition(ChildView);
                    Intent intent = new Intent(getActivity(),ClubDetailActivity.class);
                    intent.putExtra("nama",ArrayListKlasemen.get(RecyclerViewItemPosition).getTxtTeamKlasemen());
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
        loadStandings();
        return rootView;
    }

    public void loadStandings(){
        swiper.setRefreshing(true);
        OArguments arguments = new OArguments();
        arguments.add(sharedPrefManager.getSPIdLiga());

        client.call_kw("persebaya.liga.klasemen", "klasemen", arguments, new IOdooResponse() {
            @Override
            public void onResult(OdooResult result) {
                // response
                ArrayListKlasemen.clear();
                OdooRecord[] Records = result.getRecords();
                Integer color = getResources().getColor(R.color.colorWhite);

                ArrayListKlasemen.add(new Klasemen(
                        String.valueOf("No."),
                        String.valueOf("Logo"),
                        String.valueOf("Club"),
                        String.valueOf("P"),
                        String.valueOf("+/-"),
                        String.valueOf("Pts"),
                        color,0));
                int i = 1;

                for (final OdooRecord record : Records) {
                    if (record.getString("nama_club").equalsIgnoreCase(getActivity().getIntent().getStringExtra("nama"))){
                        color = getResources().getColor(R.color.colorYellow);
                    }else{
                        color = getResources().getColor(R.color.colorWhite);
                    }
                    ArrayListKlasemen.add(new Klasemen(
                            String.valueOf(i),
                            record.getString("foto_club"),
                            record.getString("nama_club"),
                            String.valueOf(record.getInt("play")),
                            String.valueOf(record.getInt("selisih_gol")),
                            String.valueOf(record.getInt("point")),
                            color,
                            record.getInt("id")));
                    i++;
                }
                adapter.notifyDataSetChanged();
                swiper.setRefreshing(false);
            }
        });
    }

//    public class StandingTask extends AsyncTask<Void,Void,Void> {
//        @Override
//        protected void onPreExecute() {
//            swiper.setRefreshing(true);
//        }
//
//
//        @Override
//        protected Void doInBackground(Void... voids) {
//            ArrayListKlasemen =  new ArrayList<>();
//            client = new OdooClient.Builder(getContext())
//                    .setHost(sharedPrefManager.getSP_Host_url())
//                    .setSession(sharedPrefManager.getSpSessionId())
//                    .setSynchronizedRequests(false)
//                    .setConnectListener(new OdooConnectListener() {
//                        @Override
//                        public void onConnected(OdooVersion version) {
//                            // Success connection
//
//                            OArguments arguments = new OArguments();
//                            arguments.add(sharedPrefManager.getSPIdLiga());
//
//                            client.call_kw("persebaya.liga.klasemen", "klasemen", arguments, new IOdooResponse() {
//                                @Override
//                                public void onResult(OdooResult result) {
//                                    // response
//                                    OdooRecord[] Records = result.getRecords();
//                                    Integer color = getResources().getColor(R.color.colorWhite);
//
//                                    ArrayListKlasemen.add(new Klasemen(
//                                            String.valueOf("No."),
//                                            String.valueOf("Logo"),
//                                            String.valueOf("Club"),
//                                            String.valueOf("P"),
//                                            String.valueOf("+/-"),
//                                            String.valueOf("Pts"),
//                                            color,0));
//                                    int i = 1;
//
//                                    for (final OdooRecord record : Records) {
//                                        if (record.getString("nama_club").equalsIgnoreCase(getActivity().getIntent().getStringExtra("nama"))){
//                                            color = getResources().getColor(R.color.colorYellow);
//                                        }else{
//                                            color = getResources().getColor(R.color.colorWhite);
//                                        }
//                                        ArrayListKlasemen.add(new Klasemen(
//                                                String.valueOf(i),
//                                                record.getString("foto_club"),
//                                                record.getString("nama_club"),
//                                                String.valueOf(record.getInt("play")),
//                                                String.valueOf(record.getInt("selisih_gol")),
//                                                String.valueOf(record.getInt("point")),
//                                                color,
//                                                record.getInt("id")));
//                                        i++;
//                                    }
//                                    adapter = new AdapterKlasemen(ArrayListKlasemen);
//                                    rv.setAdapter(adapter);
//                                    adapter.notifyDataSetChanged();
//                                    swiper.setRefreshing(false);
//                                }
//                            });
//                        }
//                    })
//                    .build();
//            return null;
//        }
//    }
}
