package com.example.nerita_hendra.i_fans;


import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
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
import java.util.HashMap;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class ClubStandingFragment extends Fragment {

    ArrayList<Klasemen> ArrayListKlasemen;
    int RecyclerViewItemPosition ;
    SharedPrefManager sharedPrefManager;
    ProgressDialog progressDialog;
    View rootView;
    RecyclerView rv;
    RecyclerView.LayoutManager llm;
    SwipeRefreshLayout swiper;
    AdapterKlasemen adapter;

    public ClubStandingFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_club_standing, container, false);
        rv = rootView.findViewById(R.id.rv_recycler_view_standings);
        swiper = rootView.findViewById(R.id.swiperefresh_standings);
        llm = new LinearLayoutManager(getActivity());
        adapter = new AdapterKlasemen(ArrayListKlasemen);
        rv.setAdapter(adapter);
        rv.setLayoutManager(llm);
        swiper.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new StandingTask().execute();
            }
        });
        sharedPrefManager = new SharedPrefManager(getActivity());
        rv.addOnItemTouchListener(new RecyclerView.OnItemTouchListener() {

            GestureDetector gestureDetector = new GestureDetector(getActivity(), new GestureDetector.SimpleOnGestureListener() {

                @Override public boolean onSingleTapUp(MotionEvent motionEvent) {

                    return true;
                }

            });

            @Override
            public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
                View ChildView = rv.findChildViewUnder(e.getX(), e.getY());

                if(ChildView != null && gestureDetector.onTouchEvent(e)) {

                    RecyclerViewItemPosition = rv.getChildAdapterPosition(ChildView);
                    Intent intent = new Intent(getActivity(),ClubDetailActivity.class);
                    intent.putExtra("nama",ArrayListKlasemen.get(RecyclerViewItemPosition).getTxtTeamKlasemen());
                    startActivity(intent);
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
        new StandingTask().execute();
        return rootView;
    }

    public class StandingTask extends AsyncTask<Void,Void,Void> {
        @Override
        protected void onPreExecute() {
            swiper.setRefreshing(true);
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            adapter = new AdapterKlasemen(ArrayListKlasemen);
            rv.setAdapter(adapter);
            adapter.notifyDataSetChanged();
            swiper.setRefreshing(false);
        }

        @Override
        protected Void doInBackground(Void... voids) {
            ArrayListKlasemen =  new ArrayList<>();
            try {
                OdooConnect oc = OdooConnect.connect( sharedPrefManager.getSpNamaUser(),sharedPrefManager.getSpPasswordUser());

                Object[] param = {new Object[]{
                        new Object[]{"liga_id", "=", "1"}}};

                List<HashMap<String, Object>> data = oc.search_read("persebaya.liga.klasemen", param, "id","club_id","club_id.foto_club", "play","win","draw","lose","gm","gk","point");
//                ArrayListKlasemen.add(new Klasemen(
//                        String.valueOf("No."),
//                        String.valueOf("Logo"),
//                        String.valueOf("Club"),
//                        String.valueOf("P"),
//                        String.valueOf("Win"),
//                        String.valueOf("Draw"),
//                        String.valueOf("Lose"),
//                        String.valueOf("+/-"),
//                        String.valueOf("Pts")));
                for (int i = 0; i < data.size(); ++i) {
                    Object[] paramclub = {new Object[]{
                            new Object[]{"nama", "=", data.get(i).get("club_id")}}};

                    List<HashMap<String, Object>> dataclub = oc.search_read("persebaya.club", paramclub, "foto_club");
                    for (int c = 0; c < dataclub.size(); ++c) {
                        ArrayListKlasemen.add(new Klasemen(
                                String.valueOf(i+1),
                                String.valueOf(dataclub.get(c).get("foto_club")),
                                String.valueOf(data.get(i).get("club_id")),
                                String.valueOf(data.get(i).get("play")),
                                String.valueOf(data.get(i).get("win")),
                                String.valueOf(data.get(i).get("draw")),
                                String.valueOf(data.get(i).get("lose")),
                                String.valueOf(data.get(i).get("gm")),
                                String.valueOf( data.get(i).get("point"))));
                    }
                }
            } catch (Exception ex) {
                System.out.println("Error Klasemen Fragment: " + ex);
            }
            return null;
        }
    }
}
