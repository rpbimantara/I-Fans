package com.example.nerita_hendra.i_fans;


import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import oogbox.api.odoo.OdooClient;
import oogbox.api.odoo.client.OdooVersion;
import oogbox.api.odoo.client.helper.OdooErrorException;
import oogbox.api.odoo.client.helper.data.OdooRecord;
import oogbox.api.odoo.client.helper.data.OdooResult;
import oogbox.api.odoo.client.helper.utils.ODomain;
import oogbox.api.odoo.client.helper.utils.OdooFields;
import oogbox.api.odoo.client.listeners.IOdooResponse;
import oogbox.api.odoo.client.listeners.OdooConnectListener;
import oogbox.api.odoo.client.listeners.OdooErrorListener;


/**
 * A simple {@link Fragment} subclass.
 */
public class TeamFragment extends Fragment {

    ArrayList<Team> ArrayListTeam;
    int RecyclerViewItemPosition ;
    SharedPrefManager sharedPrefManager;
    private View rootView;
    RecyclerView rv;
    ProgressDialog progressDialog;
    AdapterTeam adapter;
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
            swiper = rootView.findViewById(R.id.swiperefresh_team);
            sharedPrefManager = new SharedPrefManager(getActivity());
            progressDialog = new ProgressDialog(getActivity());
            adapter = new AdapterTeam(ArrayListTeam);
            rv.setAdapter(adapter);
            rv.setLayoutManager(new GridLayoutManager(getActivity(),2));
            swiper.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {
                    new TeamTask().execute();
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
        protected void onPostExecute(Void aVoid) {
            swiper.setRefreshing(false);
            super.onPostExecute(aVoid);
        }

        @Override
        protected Void doInBackground(Void... voids) {
            ArrayListTeam = new ArrayList<>();
            client = new OdooClient.Builder(getContext())
                    .setHost(sharedPrefManager.getSP_Host_url())
                    .setSession("f35afb7584ea1195be5400d65415d6ab8f7a9440")
                    .setSynchronizedRequests(false)
                    .setConnectListener(new OdooConnectListener() {
                        @Override
                        public void onConnected(OdooVersion version) {
                            ODomain domain = new ODomain();
                            domain.add("club_id", "=", sharedPrefManager.getSpIdClub());

                            OdooFields fields = new OdooFields();
                            fields.addAll("id","image","name", "job_id","status_pemain");

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
                                                ""
                                        ));
                                    }
                                    adapter = new AdapterTeam(ArrayListTeam);
                                    rv.setAdapter(adapter);
                                    adapter.notifyDataSetChanged();
                                }
                            });
                        }
                    }).build();
            return null;
        }
    }

}
