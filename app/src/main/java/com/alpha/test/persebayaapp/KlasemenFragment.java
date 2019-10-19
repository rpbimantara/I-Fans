package com.alpha.test.persebayaapp;


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
import java.util.Collections;

import oogbox.api.odoo.OdooClient;
import oogbox.api.odoo.client.helper.data.OdooRecord;
import oogbox.api.odoo.client.helper.data.OdooResult;
import oogbox.api.odoo.client.helper.utils.OArguments;
import oogbox.api.odoo.client.listeners.IOdooResponse;

import static com.alpha.test.persebayaapp.CommonUtils.getOdooConnection;


/**
 * A simple {@link Fragment} subclass.
 */
public class KlasemenFragment extends Fragment {

    ArrayList<Klasemen> ArrayListKlasemen =  new ArrayList<>();
    int RecyclerViewItemPosition ;
    SharedPrefManager sharedPrefManager;
    View rootView;
    RecyclerView rv;
    RecyclerView.LayoutManager llm;
    SwipeRefreshLayout swiper;
    AdapterKlasemen adapter;
    OdooClient client;


    public KlasemenFragment() {
        // Required empty public constructor
    }

    public static KlasemenFragment newInstance() {
        Bundle args = new Bundle();
        KlasemenFragment fragment = new KlasemenFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        if (rootView == null) {
            rootView = inflater.inflate(R.layout.fragment_klasemen, container, false);
            rv = rootView.findViewById(R.id.rv_recycler_view_klasemen);
            swiper = rootView.findViewById(R.id.swiperefresh_klasemen);
            llm = new LinearLayoutManager(getActivity());
            adapter = new AdapterKlasemen(ArrayListKlasemen);
            Collections.sort(ArrayListKlasemen);
            rv.setAdapter(adapter);
            rv.setLayoutManager(llm);
            client = getOdooConnection(getContext());
            swiper.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {
                    loadKlasemen();
                }
            });
            sharedPrefManager = new SharedPrefManager(getActivity());
            rv.addOnItemTouchListener(new RecyclerView.OnItemTouchListener() {

                GestureDetector gestureDetector = new GestureDetector(getActivity(), new GestureDetector.SimpleOnGestureListener() {

                    @Override
                    public boolean onSingleTapUp(MotionEvent motionEvent) {

                        return true;
                    }

                });

                @Override
                public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
                    View ChildView = rv.findChildViewUnder(e.getX(), e.getY());

                    if (ChildView != null && gestureDetector.onTouchEvent(e)) {

                        RecyclerViewItemPosition = rv.getChildAdapterPosition(ChildView);
//                        if (RecyclerViewItemPosition > 0) {
                            Intent intent = new Intent(getActivity(), ClubDetailActivity.class);
                            intent.putExtra("id", String.valueOf(ArrayListKlasemen.get(RecyclerViewItemPosition).getId_club()));
                            intent.putExtra("nama", ArrayListKlasemen.get(RecyclerViewItemPosition).getTxtTeamKlasemen());
                            startActivity(intent);
//                        }
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
            loadKlasemen();
        }
        return rootView;
    }

    public void loadKlasemen(){
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
                int i = 1;
                for (final OdooRecord record : Records) {
                    if (record.getString("nama_club").equalsIgnoreCase("Persebaya")){
                        color = getResources().getColor(R.color.colorYellow);
                    }else{
                        color = getResources().getColor(R.color.colorWhite);
                    }
                    ArrayListKlasemen.add(new Klasemen(
                            String.valueOf(i),
                            record.getString("foto_club"),
                            record.getString("nama_club"),
                            String.valueOf(record.getInt("play")),
                            String.valueOf(record.getString("selisih_gol")),
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
}
