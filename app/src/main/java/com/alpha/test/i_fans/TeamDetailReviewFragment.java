package com.alpha.test.i_fans;


import android.app.ProgressDialog;
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
        rv =  rootView.findViewById(R.id.rv_recycler_view_lelang);
        swiper = rootView.findViewById(R.id.swiperefresh_lelang);
        sharedPrefManager = new SharedPrefManager(getActivity());
        return rootView;
    }

}
