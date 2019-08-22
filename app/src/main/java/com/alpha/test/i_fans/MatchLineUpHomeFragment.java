package com.alpha.test.i_fans;


import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
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
public class MatchLineUpHomeFragment extends Fragment {

    private View rootView;
    RecyclerView rvLineUpHomeCore,rvLineUpHome;
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
                    new LineUpHomeTask().execute();
                }
            });
            sharedPrefManager = new  SharedPrefManager(getContext());
            adapter = new AdapterLineUpHome(ArrayListMatchLineUpHome);
            adapterCore = new AdapterLineUpHome(ArrayListMatchLineUpHomeCore);
            rvLineUpHome.setAdapter(adapter);
            rvLineUpHome.setLayoutManager(new LinearLayoutManager(getActivity()));
            rvLineUpHomeCore.setAdapter(adapterCore);
            rvLineUpHomeCore.setLayoutManager(new LinearLayoutManager(getActivity()));
            new LineUpHomeTask().execute();

        }
        return rootView;
    }

    public class LineUpHomeTask extends AsyncTask<Void,Void,Void>{

        @Override
        protected void onPreExecute() {
            swiper.setRefreshing(true);
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            ArrayListMatchLineUpHome = new ArrayList<>();
            ArrayListMatchLineUpHomeCore = new ArrayList<>();
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

                            client.searchRead("persebaya.line.up.home", domain, fields, offset, limit, sorting, new IOdooResponse() {
                                @Override
                                public void onResult(OdooResult result) {
                                    OdooRecord[] records = result.getRecords();
                                    for (OdooRecord record : records) {
                                        if (record.getString("status_pemain").equalsIgnoreCase("core")){
                                            ArrayListMatchLineUpHomeCore.add(new MatchLineUp(
                                                    String.valueOf(record.getInt("id")),
                                                    record.getString("jadwal_id"),
                                                    String.valueOf(record.getInt("player_id")),
                                                    record.getString("player_id"),
                                                    String.valueOf(Math.round(record.getFloat("no_punggung"))),
                                                    record.getString("job_id"),
                                                    record.getString("club_id"),
                                                    record.getString("status_pemain")
                                            ));
                                        }else {
                                            ArrayListMatchLineUpHome.add(new MatchLineUp(
                                                    String.valueOf(record.getInt("id")),
                                                    record.getString("jadwal_id"),
                                                    String.valueOf(record.getInt("player_id")),
                                                    record.getString("player_id"),
                                                    String.valueOf(Math.round(record.getFloat("no_punggung"))),
                                                    record.getString("job_id"),
                                                    record.getString("club_id"),
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
                    }).build();
            return null;
        }
    }

}
