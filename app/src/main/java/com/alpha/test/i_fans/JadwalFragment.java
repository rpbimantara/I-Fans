package com.alpha.test.i_fans;


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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;

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
public class JadwalFragment extends Fragment {

    ArrayList<Jadwal> ArrayListJadwal;
    SharedPrefManager sharedPrefManager;
    int RecyclerViewItemPosition ;
    RecyclerView rv;
    View rootView;
    RecyclerView.LayoutManager llm;
    SwipeRefreshLayout swiper;
    AdapterJadwal adapter;
    OdooClient client;


    public JadwalFragment() {
        // Required empty public constructor
    }

    public static JadwalFragment newInstance() {
        Bundle args = new Bundle();
        JadwalFragment fragment = new JadwalFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        if (rootView == null) {
            rootView = inflater.inflate(R.layout.fragment_jadwal, container, false);
            rv = rootView.findViewById(R.id.rv_recycler_view_jadwal);
            swiper = rootView.findViewById(R.id.swiperefresh_jadwal);
            llm = new LinearLayoutManager(getActivity());
            adapter = new AdapterJadwal(ArrayListJadwal);
            rv.setAdapter(adapter);
            rv.setLayoutManager(llm);
            swiper.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {

                    new JadwalTask().execute();
                }
            });
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

//                        RecyclerViewItemPosition = rv.getChildAdapterPosition(ChildView);
//                        Intent intent = new Intent(getActivity(), ClubDetailActivity.class);
//                        intent.putExtra("nama", ArrayListJadwal.get(RecyclerViewItemPosition).getNamateam());
//                        startActivity(intent);
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
            sharedPrefManager = new SharedPrefManager(getActivity());
            new JadwalTask().execute();
        }
        return rootView;
    }

    public String tanggal(String tgl){
        try {
           tgl = new SimpleDateFormat("dd MMM yyyy",Locale.US).format(new SimpleDateFormat("yyyy-MM-dd").parse(tgl));
        }catch (Exception ex){
            System.out.println("Error Convert Tanggal: " + ex);
        }

        return tgl;
    }

    public  String waktu(String waktu){
        int output = Integer.valueOf(waktu.substring(0,1));
        waktu = String.valueOf(output) + waktu.substring(1,5);
        return waktu;
    }

    public class JadwalTask extends AsyncTask<Void, Void,Void>{
        @Override
        protected void onPreExecute() {
            swiper.setRefreshing(true);
        }

        @Override
        protected void onPostExecute(Void aVoid) {

        }

        @Override
        protected Void doInBackground(Void... voids) {
            ArrayListJadwal = new ArrayList<>();
            client = new OdooClient.Builder(getContext())
                    .setHost(sharedPrefManager.getSP_Host_url())
                    .setSession(sharedPrefManager.getSpSessionId())
                    .setSynchronizedRequests(false)
                    .setConnectListener(new OdooConnectListener() {
                        @Override
                        public void onConnected(OdooVersion version) {
                            // Success connection

                            OArguments arguments = new OArguments();
                            arguments.add(sharedPrefManager.getSpIdClub());
                            arguments.add(sharedPrefManager.getSPIdLiga());
                            Log.e("Jadwal liga",String.valueOf(sharedPrefManager.getSPIdLiga()));
                            client.call_kw("persebaya.jadwal", "list_jadwal", arguments, new IOdooResponse() {
                                @Override
                                public void onResult(OdooResult result) {
                                    // response
                                    OdooRecord[] Records = result.getRecords();
                                    for (final OdooRecord record : Records) {
                                        String tgl = tanggal(record.getString("date").substring(0,10));
                                        String waktu = waktu(record.getString("date").substring(11,17)) + " "+ "WIB";
                                        Integer status = getContext().getResources().getIdentifier("ic_away","drawable",getContext().getPackageName());
                                        if (record.getBoolean("is_home") == false){
                                            status = getContext().getResources().getIdentifier("ic_away","drawable",getContext().getPackageName());
                                        }else {
                                            status = getContext().getResources().getIdentifier("ic_home","drawable",getContext().getPackageName());
                                        }
                                        ArrayListJadwal.add(new Jadwal(
                                        record.getString("nama_club"),
                                        record.getString("foto_club"),
                                        status,
                                        record.getString("liga_id"),
                                        tgl,
                                        record.getString("stadion")
                                        , waktu,
                                        String.valueOf(record.getInt("id")),
                                        record.getString("status_jadwal")));
                                    }
                                    adapter = new AdapterJadwal(ArrayListJadwal);
                                    rv.setAdapter(adapter);
                                    adapter.notifyDataSetChanged();
                                    swiper.setRefreshing(false);
                                }
                            });
                        }
                    })
                    .build();
           return null;
        }
    }

}
