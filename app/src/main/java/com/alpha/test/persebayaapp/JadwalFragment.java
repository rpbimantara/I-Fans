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

import com.jaredrummler.materialspinner.MaterialSpinner;

import java.util.ArrayList;

import oogbox.api.odoo.OdooClient;
import oogbox.api.odoo.client.helper.OdooErrorException;
import oogbox.api.odoo.client.helper.data.OdooRecord;
import oogbox.api.odoo.client.helper.data.OdooResult;
import oogbox.api.odoo.client.helper.utils.OArguments;
import oogbox.api.odoo.client.helper.utils.ODomain;
import oogbox.api.odoo.client.helper.utils.OdooFields;
import oogbox.api.odoo.client.listeners.IOdooResponse;
import oogbox.api.odoo.client.listeners.OdooErrorListener;

import static com.alpha.test.persebayaapp.CommonUtils.getOdooConnection;
import static com.alpha.test.persebayaapp.CommonUtils.getOdooConnection1;
import static com.alpha.test.persebayaapp.CommonUtils.tanggal;
import static com.alpha.test.persebayaapp.CommonUtils.waktu;

/**
 * A simple {@link Fragment} subclass.
 */
public class JadwalFragment extends Fragment {

    ArrayList<Jadwal> ArrayListJadwal;
    ArrayList<Liga> ArrayListLiga;
    SharedPrefManager sharedPrefManager;
    int RecyclerViewItemPosition ;
    RecyclerView rv;
    View rootView;
    RecyclerView.LayoutManager llm;
    SwipeRefreshLayout swiper;
    AdapterJadwal adapter;
    OdooClient client;
    MaterialSpinner ligaSpinner;
    AdapterLiga adapterLiga;


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
            ligaSpinner = rootView.findViewById(R.id.ligaterupdate_spinner);
            rv = rootView.findViewById(R.id.rv_recycler_view_jadwal);
            swiper = rootView.findViewById(R.id.swiperefresh_jadwal);
            llm = new LinearLayoutManager(getActivity());
            rv.setAdapter(adapter);
            rv.setLayoutManager(llm);
            sharedPrefManager = new SharedPrefManager(getActivity());
            client = getOdooConnection1(getContext(), new OdooErrorListener() {
                @Override
                public void onError(OdooErrorException error) {
                    swiper.setRefreshing(false);
                }
            });
            swiper.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {
                    loadJadwal();
                    loadLiga();
//                    new JadwalTask().execute();
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
                        RecyclerViewItemPosition = rv.getChildAdapterPosition(ChildView);
                        Intent intent = new Intent(getActivity(), MatchDetailActivity.class);
                        intent.putExtra("id_jadwal", Integer.valueOf(ArrayListJadwal.get(RecyclerViewItemPosition).getJadwal_id()));
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
            ligaSpinner.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener() {
                @Override
                public void onItemSelected(MaterialSpinner view, int position, long id, Object item) {
                    Liga liga = adapterLiga.getItem(position);
                    sharedPrefManager.saveSPInt(SharedPrefManager.SP_ID_Liga,liga.getId());
                    loadJadwal();
//                    new JadwalTask().execute();
                }
            });
            loadJadwal();
            loadLiga();
//            new JadwalTask().execute();
//            new LigaTask().execute();
        }
        return rootView;
    }

    public void loadJadwal(){
        swiper.setRefreshing(true);
        ArrayListJadwal = new ArrayList<>();
        OArguments arguments = new OArguments();
        arguments.add(sharedPrefManager.getSpIdClub());
        arguments.add(sharedPrefManager.getSPIdLiga());

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

    public void loadLiga(){
        ArrayListLiga = new ArrayList<>();
        ODomain domain = new ODomain();
        domain.add("status_liga", "=", "valid");

        OdooFields fields = new OdooFields();
        fields.addAll("id", "nama", "create_date", "create_uid", "write_date", "write_uid");

        int offset = 0;
        int limit = 0;

        String sorting = "id DESC";

        client.searchRead("persebaya.liga", domain, fields, offset, limit, sorting, new IOdooResponse() {
            @Override
            public void onResult(OdooResult result) {
                OdooRecord[] records = result.getRecords();
                for (OdooRecord record : records) {
                    ArrayListLiga.add(new Liga(
                            record.getInt("id"),
                            record.getString("nama")));

                    adapterLiga = new AdapterLiga(getContext(),android.R.layout.simple_spinner_item,ArrayListLiga);
                    ligaSpinner.setAdapter(adapterLiga);
                    adapterLiga.notifyDataSetChanged();
                }
            }
        });
    }

//    public class JadwalTask extends AsyncTask<Void, Void,Void>{
//        @Override
//        protected void onPreExecute() {
//            swiper.setRefreshing(true);
//        }
//
//
//        @Override
//        protected Void doInBackground(Void... voids) {
//            ArrayListJadwal = new ArrayList<>();
//            client = new OdooClient.Builder(getContext())
//                    .setHost(sharedPrefManager.getSP_Host_url())
//                    .setSession(sharedPrefManager.getSpSessionId())
//                    .setSynchronizedRequests(false)
//                    .setConnectListener(new OdooConnectListener() {
//                        @Override
//                        public void onConnected(OdooVersion version) {
//                            // Success connection
//
//                            OArguments arguments = new OArguments();
//                            arguments.add(sharedPrefManager.getSpIdClub());
//                            arguments.add(sharedPrefManager.getSPIdLiga());
//
//                            client.call_kw("persebaya.jadwal", "list_jadwal", arguments, new IOdooResponse() {
//                                @Override
//                                public void onResult(OdooResult result) {
//                                    // response
//                                    OdooRecord[] Records = result.getRecords();
//                                    for (final OdooRecord record : Records) {
//                                        String tgl = tanggal(record.getString("date").substring(0,10));
//                                        String waktu = waktu(record.getString("date").substring(11,17)) + " "+ "WIB";
//                                        Integer status = getContext().getResources().getIdentifier("ic_away","drawable",getContext().getPackageName());
//                                        if (record.getBoolean("is_home") == false){
//                                            status = getContext().getResources().getIdentifier("ic_away","drawable",getContext().getPackageName());
//                                        }else {
//                                            status = getContext().getResources().getIdentifier("ic_home","drawable",getContext().getPackageName());
//                                        }
//                                        ArrayListJadwal.add(new Jadwal(
//                                        record.getString("nama_club"),
//                                        record.getString("foto_club"),
//                                        status,
//                                        record.getString("liga_id"),
//                                        tgl,
//                                        record.getString("stadion")
//                                        , waktu,
//                                        String.valueOf(record.getInt("id")),
//                                        record.getString("status_jadwal")));
//                                    }
//                                    adapter = new AdapterJadwal(ArrayListJadwal);
//                                    rv.setAdapter(adapter);
//                                    adapter.notifyDataSetChanged();
//                                    swiper.setRefreshing(false);
//                                }
//                            });
//                        }
//                    })
//                    .build();
//           return null;
//        }
//    }
//    public class LigaTask extends AsyncTask<Void, Void, Void>{
//        @Override
//        protected Void doInBackground(Void... voids) {
//            ArrayListLiga = new ArrayList<>();
//            client = new OdooClient.Builder(getContext())
//                    .setHost(sharedPrefManager.getSP_Host_url())
//                    .setSession(sharedPrefManager.getSpSessionId())
//                    .setSynchronizedRequests(false)
//                    .setConnectListener(new OdooConnectListener() {
//                        @Override
//                        public void onConnected(OdooVersion version) {
//                            ODomain domain = new ODomain();
//                            domain.add("status_liga", "=", "valid");
//
//                            OdooFields fields = new OdooFields();
//                            fields.addAll("id", "nama", "create_date", "create_uid", "write_date", "write_uid");
//
//                            int offset = 0;
//                            int limit = 0;
//
//                            String sorting = "id DESC";
//
//                            client.searchRead("persebaya.liga", domain, fields, offset, limit, sorting, new IOdooResponse() {
//                                @Override
//                                public void onResult(OdooResult result) {
//                                    OdooRecord[] records = result.getRecords();
//                                    for (OdooRecord record : records) {
//                                        ArrayListLiga.add(new Liga(
//                                                record.getInt("id"),
//                                                record.getString("nama")));
//
//                                        adapterLiga = new AdapterLiga(getContext(),android.R.layout.simple_spinner_item,ArrayListLiga);
//                                        ligaSpinner.setAdapter(adapterLiga);
//                                        adapterLiga.notifyDataSetChanged();
//                                    }
//                                }
//                            });
//                        }
//                    }).build();
//            return null;
//        }
//    }
}
