package com.alpha.test.i_fans;


import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import oogbox.api.odoo.OdooClient;
import oogbox.api.odoo.client.OdooVersion;
import oogbox.api.odoo.client.helper.data.OdooRecord;
import oogbox.api.odoo.client.helper.data.OdooResult;
import oogbox.api.odoo.client.helper.utils.ODomain;
import oogbox.api.odoo.client.helper.utils.OdooFields;
import oogbox.api.odoo.client.listeners.IOdooResponse;
import oogbox.api.odoo.client.listeners.OdooConnectListener;


/**
 * A simple {@link Fragment} subclass.
 */
public class TeamFragment extends Fragment {

    ArrayList<Team> ArrayListTeam;
    ArrayList<Team> ArrayListAthlete;
    int RecyclerViewItemPosition ;
    SharedPrefManager sharedPrefManager;
    private View rootView;
    RecyclerView rv,rvAthlete;
    ProgressDialog progressDialog;
    AdapterTeam adapter;
    AdapterTeam adapterAthlete;
    SwipeRefreshLayout swiper;
    OdooClient client;

    public TeamFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public static TeamFragment newInstance() {
        Bundle args = new Bundle();
        TeamFragment fragment = new TeamFragment();
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        if (rootView == null){
            rootView = inflater.inflate(R.layout.fragment_team, container, false);
            rv = rootView.findViewById(R.id.rv_recycler_view_team);
            rvAthlete = rootView.findViewById(R.id.rv_recycler_view_team_athlete);
            swiper = rootView.findViewById(R.id.swiperefresh_team);
            sharedPrefManager = new SharedPrefManager(getActivity());
            progressDialog = new ProgressDialog(getActivity());
            adapter = new AdapterTeam(ArrayListTeam);
            adapterAthlete = new AdapterTeam(ArrayListAthlete);
            rv.setAdapter(adapter);
            rvAthlete.setAdapter(adapterAthlete);
            rv.setLayoutManager(new LinearLayoutManager(getActivity(),LinearLayoutManager.HORIZONTAL,false));
            rvAthlete.setLayoutManager(new GridLayoutManager(getActivity(),2));
            swiper.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {
                    new TeamTask().execute();
                }
            });
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
            new TeamTask().execute();
        }
        return rootView;
    }

    public  class TeamTask extends AsyncTask<Void,Void,Void>{
        @Override
        protected void onPreExecute() {
            swiper.setRefreshing(true);
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            ArrayListTeam = new ArrayList<>();
            ArrayListAthlete = new ArrayList<>();
            client = new OdooClient.Builder(getContext())
                    .setHost(sharedPrefManager.getSP_Host_url())
                    .setSession(sharedPrefManager.getSpSessionId())
                    .setSynchronizedRequests(false)
                    .setConnectListener(new OdooConnectListener() {
                        @Override
                        public void onConnected(OdooVersion version) {
                            ODomain domain = new ODomain();
                            domain.add("club_id", "=", sharedPrefManager.getSpIdClub());
                            domain.add("department_id", "=", 7);

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
                                }
                            });
                        }
                    }).build();

            client = new OdooClient.Builder(getContext())
                    .setHost(sharedPrefManager.getSP_Host_url())
                    .setSession(sharedPrefManager.getSpSessionId())
                    .setSynchronizedRequests(false)
                    .setConnectListener(new OdooConnectListener() {
                        @Override
                        public void onConnected(OdooVersion version) {
                            ODomain domain = new ODomain();
                            domain.add("club_id", "=", sharedPrefManager.getSpIdClub());
                            domain.add("department_id", "=", 6);

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
                                        ArrayListAthlete.add(new Team(
                                                record.getString("name"),
                                                record.getString("image"),
                                                record.getString("status_pemain"),
                                                record.getString("job_id"),
                                                String.valueOf(record.getInt("no_punggung"))
                                        ));
                                    }
                                    adapterAthlete = new AdapterTeam(ArrayListAthlete);
                                    rvAthlete.setAdapter(adapterAthlete);
                                    adapterAthlete.notifyDataSetChanged();
                                    swiper.setRefreshing(false);
                                }
                            });
                        }
                    }).build();
            return null;
        }
    }

}
