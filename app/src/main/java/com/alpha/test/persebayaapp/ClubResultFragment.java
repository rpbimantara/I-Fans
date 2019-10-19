package com.alpha.test.persebayaapp;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;

import oogbox.api.odoo.OdooClient;
import oogbox.api.odoo.client.helper.data.OdooRecord;
import oogbox.api.odoo.client.helper.data.OdooResult;
import oogbox.api.odoo.client.helper.utils.OArguments;
import oogbox.api.odoo.client.listeners.IOdooResponse;

import static com.alpha.test.persebayaapp.CommonUtils.getOdooConnection;
import static com.alpha.test.persebayaapp.CommonUtils.tanggal;
import static com.alpha.test.persebayaapp.CommonUtils.waktu;


/**
 * A simple {@link Fragment} subclass.
 */
public class ClubResultFragment extends Fragment {

    ArrayList<Jadwal> ArrayListJadwal = new ArrayList<>();
    SharedPrefManager sharedPrefManager;
    RecyclerView rv;
    View rootView;
    RecyclerView.LayoutManager llm;
    SwipeRefreshLayout swiper;
    AdapterJadwal adapter;
    OdooClient client;

    public ClubResultFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_club_result, container, false);
        rv = rootView.findViewById(R.id.rv_recycler_view_result);
        swiper = rootView.findViewById(R.id.swiperefresh_result);
        llm = new LinearLayoutManager(getActivity());
        adapter = new AdapterJadwal(ArrayListJadwal);
        rv.setAdapter(adapter);
        client = getOdooConnection(getContext());
        rv.setLayoutManager(llm); swiper.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadResult();
            }
        });
        sharedPrefManager = new SharedPrefManager(getActivity());
        loadResult();
        return rootView;
    }

    public void loadResult(){
        swiper.setRefreshing(true);
        try {
            OArguments arguments = new OArguments();
            arguments.add(getActivity().getIntent().getStringExtra("id"));
            arguments.add(sharedPrefManager.getSPIdLiga());
            arguments.add(Arrays.asList("selesai"));

            client.call_kw("persebaya.jadwal", "list_jadwal_club", arguments, new IOdooResponse() {
                @Override
                public void onResult(OdooResult result) {
                    // response
                    ArrayListJadwal.clear();
                    OdooRecord[] Records = result.getRecords();
                    for (final OdooRecord record : Records) {
                        String date = CommonUtils.convertTime(record.getString("date"));
                        String tgl = tanggal(date.substring(0,10));
                        String waktu = waktu(date.substring(11,17)) + " "+ "WIB";
                        Integer status = getContext().getResources().getIdentifier("ic_away", "drawable", getContext().getPackageName());
                        if (record.getBoolean("is_home") == false) {
                            status = getContext().getResources().getIdentifier("ic_away", "drawable", getContext().getPackageName());
                        } else {
                            status = getContext().getResources().getIdentifier("ic_home", "drawable", getContext().getPackageName());
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
                    adapter.notifyDataSetChanged();
                    swiper.setRefreshing(false);
                }
            });
        }catch (Error error){
            Toast.makeText(getContext(),error.toString(),Toast.LENGTH_SHORT).show();
        }
    }
}
