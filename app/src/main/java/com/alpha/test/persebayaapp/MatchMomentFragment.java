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
public class MatchMomentFragment extends Fragment {

    private View rootView;
    RecyclerView rvMoment;
    SwipeRefreshLayout swiper;
    ArrayList<MatchMoment> ArrayListMatchMoment= new ArrayList<>();
    OdooClient client;
    SharedPrefManager sharedPrefManager;
    AdapterMatchMoment adapter;

    public MatchMomentFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public static MatchMomentFragment newInstance() {
        Bundle args = new Bundle();
        MatchMomentFragment fragment = new MatchMomentFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        if (rootView == null) {
            rootView = inflater.inflate(R.layout.fragment_match_moment, container, false);
            rvMoment = rootView.findViewById(R.id.rv_recycler_view_match_moment);
            swiper = rootView.findViewById(R.id.swiperefresh_match_moment);
            swiper.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {
                    loadMoment();
                }
            });
            sharedPrefManager = new SharedPrefManager(getContext());
            adapter = new AdapterMatchMoment(ArrayListMatchMoment);
            rvMoment.setAdapter(adapter);
            rvMoment.setLayoutManager(new LinearLayoutManager(getActivity()));
            client = getOdooConnection1(getContext(), new OdooErrorListener() {
                @Override
                public void onError(OdooErrorException error) {
                    swiper.setRefreshing(false);
                }
            });
            loadMoment();
        }
        return rootView;
    }

    public void loadMoment(){
        swiper.setRefreshing(true);
        ODomain domain = new ODomain();
        domain.add("jadwal_id", "=",getActivity().getIntent().getExtras().get("id_jadwal"));

        OdooFields fields = new OdooFields();
        fields.addAll("id","jadwal_id","time_moments", "moments","club_id","players_moments","supp_players_moments");

        int offset = 0;
        int limit = 0;

        String sorting = "id DESC";

        client.searchRead("persebaya.moments", domain, fields, offset, limit, sorting, new IOdooResponse() {
            @Override
            public void onResult(OdooResult result) {
                ArrayListMatchMoment.clear();
                OdooRecord[] records = result.getRecords();
                for (OdooRecord record : records) {
                    ArrayListMatchMoment.add(new MatchMoment(
                            String.valueOf(record.getInt("id")),
                            record.getString("time_moments"),
                            record.getString("club_id"),
                            record.getString("moments"),
                            record.getString("players_moments"),
                            record.getString("supp_players_moments")
                    ));
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

