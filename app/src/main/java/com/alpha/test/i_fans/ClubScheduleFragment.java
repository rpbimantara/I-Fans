package com.alpha.test.i_fans;


import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Locale;

import oogbox.api.odoo.OdooClient;
import oogbox.api.odoo.client.OdooVersion;
import oogbox.api.odoo.client.helper.data.OdooRecord;
import oogbox.api.odoo.client.helper.data.OdooResult;
import oogbox.api.odoo.client.helper.utils.OArguments;
import oogbox.api.odoo.client.listeners.IOdooResponse;
import oogbox.api.odoo.client.listeners.OdooConnectListener;

import static com.alpha.test.i_fans.CommonUtils.tanggal;
import static com.alpha.test.i_fans.CommonUtils.waktu;


/**
 * A simple {@link Fragment} subclass.
 */
public class ClubScheduleFragment extends Fragment {

    ArrayList<Jadwal> ArrayListJadwal;
    SharedPrefManager sharedPrefManager;
    RecyclerView rv;
    View rootView;
    RecyclerView.LayoutManager llm;
    SwipeRefreshLayout swiper;
    AdapterJadwal adapter;
    OdooClient client;

    public ClubScheduleFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_club_schedule, container, false);
        rv = rootView.findViewById(R.id.rv_recycler_view_schedule);
        swiper = rootView.findViewById(R.id.swiperefresh_schedule);
        llm = new LinearLayoutManager(getActivity());
        adapter = new AdapterJadwal(ArrayListJadwal);
        rv.setAdapter(adapter );
        rv.setLayoutManager(llm); swiper.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new ScheduleTask().execute();
            }
        });
        sharedPrefManager = new SharedPrefManager(getActivity());
        new ScheduleTask().execute();
        return rootView;
    }

    public class ScheduleTask extends AsyncTask<Void, Void,Void> {
        @Override
        protected void onPreExecute() {
            swiper.setRefreshing(true);
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
                            arguments.add(getActivity().getIntent().getStringExtra("id"));
                            arguments.add(sharedPrefManager.getSPIdLiga());
                            arguments.add(Arrays.asList("akan","tunda","valid","main"));
                            client.call_kw("persebaya.jadwal", "list_jadwal_club", arguments, new IOdooResponse() {
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
                                    rv.setAdapter(adapter );
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
