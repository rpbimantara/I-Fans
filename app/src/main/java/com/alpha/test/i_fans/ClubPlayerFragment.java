package com.alpha.test.i_fans;


import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
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
            adapter = new AdapterTeam(ArrayListTeam);
            rv.setAdapter(adapter);
            rv.setLayoutManager(new GridLayoutManager(getActivity(),2));
            swiper.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {
                    new ClubPlayerTask().execute();
                }
            });
            new ClubPlayerTask().execute();
        }
        return rootView;
    }

    public class ClubPlayerTask extends AsyncTask<Void,Void,Void>{
        @Override
        protected void onPreExecute() {
            swiper.setRefreshing(true);
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            ArrayListTeam = new ArrayList<>();
            client = new OdooClient.Builder(getContext())
                    .setHost(sharedPrefManager.getSP_Host_url())
                    .setSession(sharedPrefManager.getSpSessionId())
                    .setSynchronizedRequests(false)
                    .setConnectListener(new OdooConnectListener() {
                        @Override
                        public void onConnected(OdooVersion version) {
                            ODomain domain = new ODomain();
                            domain.add("club_id", "=", getActivity().getIntent().getIntExtra("id",0));

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
                                    swiper.setRefreshing(false);
                                }
                            });
                        }
                    }).build();
            return null;
        }
    }

}
