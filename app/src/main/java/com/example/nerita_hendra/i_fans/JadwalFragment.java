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

                        RecyclerViewItemPosition = rv.getChildAdapterPosition(ChildView);
                        Intent intent = new Intent(getActivity(), ClubDetailActivity.class);
                        intent.putExtra("nama", ArrayListJadwal.get(RecyclerViewItemPosition).getNamateam());
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
            adapter = new AdapterJadwal(ArrayListJadwal);
            rv.setAdapter(adapter );
            adapter.notifyDataSetChanged();
            swiper.setRefreshing(false);
        }

        @Override
        protected Void doInBackground(Void... voids) {
            ArrayListJadwal = new ArrayList<>();
            try {
                OdooConnect oc = OdooConnect.connect(sharedPrefManager.getSpNamaUser(),sharedPrefManager.getSpPasswordUser());

                Object[] param = {new Object[]{
                        new Object[]{"durasi", "=", "90"}}};

                List<HashMap<String, Object>> dataJadwal = oc.search_read("persebaya.jadwal", param, "id","liga_id", "tgl_main","home","away","stadion_id","status_jadwal");


                for (int i = 0; i < dataJadwal.size(); ++i) {
                    String tgl = tanggal(dataJadwal.get(i).get("tgl_main").toString().substring(0,10));
                    String waktu = waktu(dataJadwal.get(i).get("tgl_main").toString().substring(11,17)) + " "+ "WIB";
                    if (dataJadwal.get(i).get("home").toString().equalsIgnoreCase(sharedPrefManager.getSpNamaClub())){
                        Object[] paramclub = {new Object[]{
                                new Object[]{"nama", "=", dataJadwal.get(i).get("away")}}};

                        List<HashMap<String, Object>> dataclub = oc.search_read("persebaya.club", paramclub, "foto_club");
                        for (int c = 0; c < dataclub.size(); ++c) {
                            ArrayListJadwal.add(new Jadwal(
                                    dataJadwal.get(i).get("away").toString(),
                                    String.valueOf(dataclub.get(c).get("foto_club")),
                                    getContext().getResources().getIdentifier("ic_home","drawable",getContext().getPackageName()),
                                    dataJadwal.get(i).get("liga_id").toString(),
                                    tgl,
                                    dataJadwal.get(i).get("stadion_id").toString()
                                    , waktu,
                                    dataJadwal.get(i).get("id").toString(),
                                    dataJadwal.get(i).get("status_jadwal").toString()));
                        }
                    }else if (dataJadwal.get(i).get("away").toString().equalsIgnoreCase(sharedPrefManager.getSpNamaClub())){
                        Object[] paramclub = {new Object[]{
                                new Object[]{"nama", "=", dataJadwal.get(i).get("home")}}};

                        List<HashMap<String, Object>> dataclub = oc.search_read("persebaya.club", paramclub, "foto_club");
                        for (int c = 0; c < dataclub.size(); ++c) {
                            ArrayListJadwal.add(new Jadwal(
                                    dataJadwal.get(i).get("home").toString(),
                                    String.valueOf(dataclub.get(c).get("foto_club")),
                                    getContext().getResources().getIdentifier("ic_away","drawable",getContext().getPackageName()),
                                    dataJadwal.get(i).get("liga_id").toString(),
                                    tgl,
                                    dataJadwal.get(i).get("stadion_id").toString()
                                    , waktu,
                                    dataJadwal.get(i).get("id").toString(),
                                    dataJadwal.get(i).get("status_jadwal").toString()));
                        }
                    }

                }
            } catch (Exception ex) {
                System.out.println("Error Jadwal Fragment Add data: " + ex);
            }
           return null;
        }
    }

}
