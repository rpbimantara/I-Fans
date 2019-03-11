package com.example.nerita_hendra.i_fans;


import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import oogbox.api.odoo.OdooClient;
import oogbox.api.odoo.client.OdooVersion;
import oogbox.api.odoo.client.helper.data.OdooRecord;
import oogbox.api.odoo.client.helper.data.OdooResult;
import oogbox.api.odoo.client.helper.utils.OArguments;
import oogbox.api.odoo.client.listeners.IOdooResponse;
import oogbox.api.odoo.client.listeners.OdooConnectListener;


/**
 * A simple {@link Fragment} subclass.
 */
public class KlasemenFragment extends Fragment {

    ArrayList<Klasemen> ArrayListKlasemen;
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
            rv.setAdapter(adapter);
            rv.setLayoutManager(llm);
            swiper.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {
                    new KlasemenTask().execute();
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
                        Intent intent = new Intent(getActivity(), ClubDetailActivity.class);
                        intent.putExtra("nama", ArrayListKlasemen.get(RecyclerViewItemPosition).getTxtTeamKlasemen());
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
            new KlasemenTask().execute();
        }
        return rootView;
    }


    public class KlasemenTask extends AsyncTask<Void,Void,Void>{
        @Override
        protected void onPreExecute() {
            swiper.setRefreshing(true);
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            swiper.setRefreshing(false);
        }

        @Override
        protected Void doInBackground(Void... voids) {
            ArrayListKlasemen =  new ArrayList<>();
            client = new OdooClient.Builder(getContext())
                    .setHost(sharedPrefManager.getSP_Host_url())
                    .setSession("f35afb7584ea1195be5400d65415d6ab8f7a9440")
                    .setSynchronizedRequests(false)
                    .setConnectListener(new OdooConnectListener() {
                        @Override
                        public void onConnected(OdooVersion version) {
                            // Success connection

                            OArguments arguments = new OArguments();
                            arguments.addNULL();

                            client.call_kw("persebaya.liga", "klasemen", arguments, new IOdooResponse() {
                                @Override
                                public void onResult(OdooResult result) {
                                    // response
                                    OdooRecord[] Records = result.getRecords();
                                    Integer color = getResources().getColor(R.color.colorWhite);

                                    ArrayListKlasemen.add(new Klasemen(
                                            String.valueOf("No."),
                                            String.valueOf("Logo"),
                                            String.valueOf("Club"),
                                            String.valueOf("P"),
                                            String.valueOf("+/-"),
                                            String.valueOf("Pts"),
                                            color));
                                    int i = 1;
                                    Log.w("asdads",String.valueOf(Records.length));
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
                                                String.valueOf(record.getInt("selisih_gol")),
                                                String.valueOf(record.getInt("point")),
                                                color));
                                        i++;
                                    }
                                    adapter = new AdapterKlasemen(ArrayListKlasemen);
                                    rv.setAdapter(adapter);
                                    adapter.notifyDataSetChanged();
                                }
                            });
                        }
                    })
                    .build();
//            Integer color = getResources().getColor(R.color.colorWhite);
//            try {
//                OdooConnect oc = OdooConnect.connect( sharedPrefManager.getSpNamaUser(),sharedPrefManager.getSpPasswordUser());
//
//                Object[] param = {new Object[]{
//                        new Object[]{"liga_id", "=", "1"}}};
//
//                List<HashMap<String, Object>> data = oc.search_read("persebaya.liga.klasemen", param, "id","club_id","club_id.foto_club", "play","win","draw","lose","gm","gk","point");

//                for (int i = 0; i < data.size(); ++i) {
//                    Object[] paramclub = {new Object[]{
//                            new Object[]{"nama", "=", data.get(i).get("club_id")}}};
//
//                    List<HashMap<String, Object>> dataclub = oc.search_read("persebaya.club", paramclub, "foto_club");
//                    for (int c = 0; c < dataclub.size(); ++c) {
//                        if (data.get(i).get("club_id").toString().equalsIgnoreCase("Persebaya")){
//                            color = getResources().getColor(R.color.colorYellow);
//                        }else{
//                            color = getResources().getColor(R.color.colorWhite);
//                        }
//                        ArrayListKlasemen.add(new Klasemen(
//                                String.valueOf(i+1),
//                                String.valueOf(dataclub.get(c).get("foto_club")),
//                                String.valueOf(data.get(i).get("club_id")),
//                                String.valueOf(data.get(i).get("play")),
//                                String.valueOf(data.get(i).get("win")),
//                                String.valueOf(data.get(i).get("draw")),
//                                String.valueOf(data.get(i).get("lose")),
//                                String.valueOf(data.get(i).get("gm")),
//                                String.valueOf( data.get(i).get("point")),
//                                color));
//                    }
//                }
//            } catch (Exception ex) {
//                System.out.println("Error Klasemen Fragment: " + ex);
//            }
            return null;
        }
    }
}
