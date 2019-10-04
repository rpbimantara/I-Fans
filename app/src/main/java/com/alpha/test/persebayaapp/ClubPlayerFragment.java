package com.alpha.test.persebayaapp;


import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
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
public class ClubPlayerFragment extends Fragment {

    ArrayList<Team> ArrayListTeam;
    int RecyclerViewItemPosition ;
    SharedPrefManager sharedPrefManager;
    private View rootView;
    RecyclerView rv;
    ProgressDialog progressDialog;
    AdapterTeam adapter;
    SwipeRefreshLayout swiper;
    OdooClient client;

    public ClubPlayerFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        if (rootView == null){
            rootView = inflater.inflate(R.layout.fragment_club_player, container, false);
            rv = rootView.findViewById(R.id.rv_recycler_view_club_player);
            swiper = rootView.findViewById(R.id.swiperefresh_club_player);
            sharedPrefManager = new SharedPrefManager(getActivity());
            progressDialog = new ProgressDialog(getActivity());
            rv.setAdapter(adapter);
            rv.setLayoutManager(new GridLayoutManager(getActivity(),3));
            rv.addOnItemTouchListener(new RecyclerView.OnItemTouchListener() {
                GestureDetector gestureDetector = new GestureDetector(getActivity(), new GestureDetector.SimpleOnGestureListener() {

                    @Override
                    public boolean onSingleTapUp(MotionEvent motionEvent) {

                        return true;
                    }

                });
                @Override
                public boolean onInterceptTouchEvent(@NonNull RecyclerView r, @NonNull MotionEvent e) {
                    View ChildView = rv.findChildViewUnder(e.getX(), e.getY());

                    if (ChildView != null && gestureDetector.onTouchEvent(e)) {
                        try {
                            RecyclerViewItemPosition = rv.getChildAdapterPosition(ChildView);
                            Intent intent = new Intent(getActivity(), TeamDetailActivity.class);
                            intent.putExtra("id_atlete", ArrayListTeam.get(RecyclerViewItemPosition).getId());
                            startActivity(intent);
                        } catch (Exception err) {
                            System.out.println(err);
                        }

                    }
                    return false;
                }

                @Override
                public void onTouchEvent(@NonNull RecyclerView recyclerView, @NonNull MotionEvent motionEvent) {

                }

                @Override
                public void onRequestDisallowInterceptTouchEvent(boolean b) {

                }
            });
            client = getOdooConnection(getContext());
            swiper.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {
                    loadClubPlayer();
//                    new ClubPlayerTask().execute();
                }
            });
            loadClubPlayer();
//            new ClubPlayerTask().execute();
        }
        return rootView;
    }

    void loadClubPlayer(){
        swiper.setRefreshing(true);
        ArrayListTeam = new ArrayList<>();
        ODomain domain = new ODomain();
        domain.add("club_id", "=", Integer.valueOf(getActivity().getIntent().getStringExtra("id")));

        OdooFields fields = new OdooFields();
        fields.addAll("id","image","name", "job_id","status_pemain","no_punggung");

        int offset = 0;
        int limit = 80;

        String sorting = "id DESC";

        client.searchRead("hr.employee", domain, fields, offset, limit, sorting, new IOdooResponse() {
            @Override
            public void onResult(OdooResult result) {
                OdooRecord[] records = result.getRecords();
                for (OdooRecord record : records) {
                    ArrayListTeam.add(new Team(
                            record.getInt("id"),
                            record.getString("name"),
                            record.getString("image"),
                            record.getString("status_pemain"),
                            record.getString("job_id"),
                            String.valueOf(record.getInt("no_punggung"))
                    ));
                }
                adapter = new AdapterTeam(ArrayListTeam);
                rv.setAdapter(adapter);
                adapter.notifyDataSetChanged();
                swiper.setRefreshing(false);
            }
        });
    }

//    public class ClubPlayerTask extends AsyncTask<Void,Void,Void>{
//        @Override
//        protected void onPreExecute() {
//            swiper.setRefreshing(true);
//            super.onPreExecute();
//        }
//
//        @Override
//        protected Void doInBackground(Void... voids) {
//            ArrayListTeam = new ArrayList<>();
//            client = new OdooClient.Builder(getContext())
//                    .setHost(sharedPrefManager.getSP_Host_url())
//                    .setSession(sharedPrefManager.getSpSessionId())
//                    .setSynchronizedRequests(false)
//                    .setConnectListener(new OdooConnectListener() {
//                        @Override
//                        public void onConnected(OdooVersion version) {
//                            ODomain domain = new ODomain();
//                            domain.add("club_id", "=", Integer.valueOf(getActivity().getIntent().getStringExtra("id")));
//
//                            OdooFields fields = new OdooFields();
//                            fields.addAll("id","image","name", "job_id","status_pemain","no_punggung");
//
//                            int offset = 0;
//                            int limit = 80;
//
//                            String sorting = "id DESC";
//
//                            client.searchRead("hr.employee", domain, fields, offset, limit, sorting, new IOdooResponse() {
//                                @Override
//                                public void onResult(OdooResult result) {
//                                    OdooRecord[] records = result.getRecords();
//                                    for (OdooRecord record : records) {
//                                        ArrayListTeam.add(new Team(
//                                                record.getInt("id"),
//                                                record.getString("name"),
//                                                record.getString("image"),
//                                                record.getString("status_pemain"),
//                                                record.getString("job_id"),
//                                                String.valueOf(record.getInt("no_punggung"))
//                                        ));
//                                    }
//                                    adapter = new AdapterTeam(ArrayListTeam);
//                                    rv.setAdapter(adapter);
//                                    adapter.notifyDataSetChanged();
//                                    swiper.setRefreshing(false);
//                                }
//                            });
//                        }
//                    }).build();
//            return null;
//        }
//    }

}
