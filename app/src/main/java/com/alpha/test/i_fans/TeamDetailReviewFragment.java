package com.alpha.test.i_fans;


import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
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
public class TeamDetailReviewFragment extends Fragment {

    ArrayList<TeamReview> ArrayListReview;
    SharedPrefManager sharedPrefManager;
    RecyclerView rv;
    View rootView;
    SwipeRefreshLayout swiper;
    AdapterTeamDetailReview adapter;
    OdooClient client;

    public TeamDetailReviewFragment() {
        // Required empty public constructor
    }

    public static TeamDetailReviewFragment newInstance() {
        Bundle args = new Bundle();
        TeamDetailReviewFragment fragment = new TeamDetailReviewFragment();
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_team_detail_review, container, false);
        rv =  rootView.findViewById(R.id.rv_recycler_view_team_review);
        swiper = rootView.findViewById(R.id.swiperefresh_team_review);
        sharedPrefManager = new SharedPrefManager(getActivity());
        rv.setLayoutManager(new LinearLayoutManager(getActivity()));
        rv.setAdapter(adapter);
        new ReviewTask().execute();
        return rootView;
    }

    public class ReviewTask extends AsyncTask<Void,Void,Void> {
        @Override
        protected void onPreExecute() {
            swiper.setRefreshing(true);
        }

        @Override
        protected Void doInBackground(Void... voids) {
            ArrayListReview = new ArrayList<>();
            client = new OdooClient.Builder(getContext())
                    .setHost(sharedPrefManager.getSP_Host_url())
                    .setSession(sharedPrefManager.getSpSessionId())
                    .setSynchronizedRequests(false)
                    .setConnectListener(new OdooConnectListener() {
                        @Override
                        public void onConnected(OdooVersion version) {
                            ODomain domain = new ODomain();
                            domain.add("employee_id", "=",getActivity().getIntent().getExtras().get("id_atlete"));

                            OdooFields fields = new OdooFields();
                            fields.addAll("id","jadwal_id","rating", "review");

                            int offset = 0;
                            int limit = 80;

                            String sorting = "id ASC";

                            client.searchRead("persebaya.rating", domain, fields, offset, limit, sorting, new IOdooResponse() {
                                @Override
                                public void onResult(OdooResult result) {
                                    OdooRecord[] records = result.getRecords();
                                    System.out.println(records.toString());
                                    for (OdooRecord record : records) {
                                        ArrayListReview.add(new TeamReview(
                                                record.getInt("id"),
                                                record.getInt("rating"),
                                                record.getString("review")));
                                    }
                                    adapter = new AdapterTeamDetailReview(ArrayListReview);
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
