package com.example.nerita_hendra.i_fans;


import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import oogbox.api.odoo.OdooClient;
import oogbox.api.odoo.client.helper.OdooErrorException;
import oogbox.api.odoo.client.helper.data.OdooRecord;
import oogbox.api.odoo.client.helper.data.OdooResult;
import oogbox.api.odoo.client.listeners.IOdooResponse;
import oogbox.api.odoo.client.listeners.OdooErrorListener;


/**
 * A simple {@link Fragment} subclass.
 */
public class JadwalFragment extends Fragment {

    ArrayList<Jadwal> ArrayListJadwal;
    ImageView imagestatus;
    SharedPrefManager sharedPrefManager;
    ProgressDialog progressDialog;
    RecyclerView rv;
    View rootView;
    RecyclerView.LayoutManager llm;
    SwipeRefreshLayout swiper;


    public JadwalFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_jadwal, container, false);
        rv =  rootView.findViewById(R.id.rv_recycler_view_jadwal);
        swiper = rootView.findViewById(R.id.swiperefresh_jadwal);
        swiper.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                load();
            }
        });
        imagestatus = rootView.findViewById(R.id.klasemen_image);
        sharedPrefManager = new SharedPrefManager(getActivity());
        progressDialog = new ProgressDialog(getActivity());
        load();
        return rootView;
    }

    public void load(){
        new JadwalTask().execute();
        llm = new LinearLayoutManager(getActivity());
        rv.setAdapter(new AdapterJadwal(ArrayListJadwal));
        rv.setLayoutManager(llm);
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
        int output = Integer.valueOf(waktu.substring(0,1))+7;
        waktu = String.valueOf(output) + waktu.substring(1,4);
        return waktu;
    }

    public class JadwalTask extends AsyncTask<Void, Void,Void>{
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
            ArrayListJadwal = new ArrayList<>();
            try {
                OdooConnect oc = OdooConnect.connect(sharedPrefManager.getSpNamaUser(),sharedPrefManager.getSpPasswordUser());

                Object[] param = {new Object[]{
                        new Object[]{"create_uid", "=", 1}}};

                List<HashMap<String, Object>> dataJadwal = oc.search_read("persebaya.jadwal", param, "liga_id", "tgl_main","home","away","stadion_id");


                for (int i = 0; i < dataJadwal.size(); ++i) {
                    String tgl = tanggal(dataJadwal.get(i).get("tgl_main").toString().substring(0,10));
                    String waktu = waktu(dataJadwal.get(i).get("tgl_main").toString().substring(12,16)) + " "+ "WIB";
                    if (dataJadwal.get(i).get("home").toString().equalsIgnoreCase(sharedPrefManager.getSpNamaClub())){
                        ArrayListJadwal.add(new Jadwal(
                                dataJadwal.get(i).get("away").toString(),
                                dataJadwal.get(i).get("liga_id").toString(),
                                tgl,
                                dataJadwal.get(i).get("stadion_id").toString()
                                ,waktu));
                    }else if (dataJadwal.get(i).get("away").toString().equalsIgnoreCase(sharedPrefManager.getSpNamaClub())){
                        ArrayListJadwal.add(new Jadwal(
                                dataJadwal.get(i).get("home").toString(),
                                dataJadwal.get(i).get("liga_id").toString(),
                                tgl,
                                dataJadwal.get(i).get("stadion_id").toString()
                                ,waktu));
                    }

                }
            } catch (Exception ex) {
                System.out.println("Error Jadwal Fragment Add data: " + ex);
            }
           return null;
        }
    }

}
