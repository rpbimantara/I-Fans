package com.example.nerita_hendra.i_fans;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class JadwalFragment extends Fragment {

    ArrayList<Jadwal> ArrayListJadwal;

    public JadwalFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_jadwal, container, false);
        RecyclerView rv = (RecyclerView) rootView.findViewById(R.id.rv_recycler_view_jadwal);
        addData();
        AdapterJadwal adapter = new AdapterJadwal(ArrayListJadwal);
        rv.setAdapter(adapter);
        RecyclerView.LayoutManager llm = new LinearLayoutManager(getActivity());
        rv.setLayoutManager(llm);
        return rootView;
    }

    void addData(){
        ArrayListJadwal = new ArrayList<>();
        ArrayListJadwal.add(new Jadwal("PERSEBAYA SURABAYA", "GOJEK LIGA I 2018","1 JANUARI 2018","STADION JAKA","18:30 WIB"));
        ArrayListJadwal.add(new Jadwal("PERSIB BANDUNG", "GOJEK LIGA I 2018","2 JANUARI 2018","STADION JAKABARING","18:30 WIB"));
        ArrayListJadwal.add(new Jadwal("AREMA INDONESIA", "GOJEK LIGA I 2018","3 JANUARI 2018","STADION JAKABARING PALEMBANG","18:30 WIB"));
        ArrayListJadwal.add(new Jadwal("AREMA INDONESIA", "GOJEK LIGA I 2018","4 JANUARI 2018","STADION JAKA","18:30 WIB"));
        ArrayListJadwal.add(new Jadwal("AREMA INDONESIA", "GOJEK LIGA I 2018","5 JANUARI 2018","STADION JAKA","18:30 WIB"));
        ArrayListJadwal.add(new Jadwal("AREMA INDONESIA", "GOJEK LIGA I 2018","6 JANUARI 2018","STADION JAKA","18:30 WIB"));
        ArrayListJadwal.add(new Jadwal("AREMA INDONESIA", "GOJEK LIGA I 2018","7 JANUARI 2018","STADION JAKA","18:30 WIB"));
        ArrayListJadwal.add(new Jadwal("AREMA INDONESIA", "GOJEK LIGA I 2018","8 JANUARI 2018","STADION JAKA","18:30 WIB"));
        ArrayListJadwal.add(new Jadwal("AREMA INDONESIA", "GOJEK LIGA I 2018","9 JANUARI 2018","STADION JAKA","18:30 WIB"));
        ArrayListJadwal.add(new Jadwal("AREMA INDONESIA", "GOJEK LIGA I 2018","10 JANUARI 2018","STADION JAKA","18:30 WIB"));
    }

}
