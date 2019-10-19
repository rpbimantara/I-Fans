package com.alpha.test.persebayaapp;


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
import oogbox.api.odoo.client.helper.OdooErrorException;
import oogbox.api.odoo.client.helper.data.OdooRecord;
import oogbox.api.odoo.client.helper.data.OdooResult;
import oogbox.api.odoo.client.helper.utils.ODomain;
import oogbox.api.odoo.client.helper.utils.OdooFields;
import oogbox.api.odoo.client.listeners.IOdooResponse;
import oogbox.api.odoo.client.listeners.OdooErrorListener;

import static com.alpha.test.persebayaapp.CommonUtils.getOdooConnection;
import static com.alpha.test.persebayaapp.CommonUtils.getOdooConnection1;


/**
 * A simple {@link Fragment} subclass.
 */
public class TeamDetailReviewFragment extends Fragment {

    ArrayList<TeamReview> ArrayListReview = new ArrayList<>();
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
        adapter = new AdapterTeamDetailReview(ArrayListReview);
        rv.setAdapter(adapter);
        client = getOdooConnection1(getContext(), new OdooErrorListener() {
            @Override
            public void onError(OdooErrorException error) {
                swiper.setRefreshing(false);
            }
        });
        swiper.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadReview();
            }
        });
        loadReview();
        return rootView;
    }

    public void loadReview(){
        swiper.setRefreshing(true);
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
                ArrayListReview.clear();
                OdooRecord[] records = result.getRecords();
                for (OdooRecord record : records) {
                    ArrayListReview.add(new TeamReview(
                            record.getInt("id"),
                            record.getInt("rating"),
                            record.getString("review")));
                }
                adapter.notifyDataSetChanged();
                swiper.setRefreshing(false);
            }

            @Override
            public boolean onError(OdooErrorException error) {
                swiper.setRefreshing(false);
                return super.onError(error);
            }
        });
    }

}
