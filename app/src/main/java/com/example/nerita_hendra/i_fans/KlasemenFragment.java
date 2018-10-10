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
public class KlasemenFragment extends Fragment {

    ArrayList<Klasemen> ArrayListKlasemen;

    public KlasemenFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_klasemen, container, false);
        RecyclerView rv = (RecyclerView) rootView.findViewById(R.id.rv_recycler_view_klasemen);
        addData();
        AdapterKlasemen adapter = new AdapterKlasemen(ArrayListKlasemen);
        rv.setAdapter(adapter);
        RecyclerView.LayoutManager llm = new LinearLayoutManager(getActivity());
        rv.setLayoutManager(llm);
        return rootView;
    }


    void addData(){
        ArrayListKlasemen =  new ArrayList<>();
        ArrayListKlasemen.add(new Klasemen("1","PERSIB BANDUNG","23","37-21","44"));
        ArrayListKlasemen.add(new Klasemen("2","PERSIB BANDUNG","23","37-21","44"));
        ArrayListKlasemen.add(new Klasemen("3","PERSIB BANDUNG","23","37-21","44"));
        ArrayListKlasemen.add(new Klasemen("4","PERSIB BANDUNG","23","37-21","44"));
        ArrayListKlasemen.add(new Klasemen("5","PERSIB BANDUNG","23","37-21","44"));
        ArrayListKlasemen.add(new Klasemen("6","PERSIB BANDUNG","23","37-21","44"));
        ArrayListKlasemen.add(new Klasemen("7","PERSIB BANDUNG","23","37-21","44"));
        ArrayListKlasemen.add(new Klasemen("8","PERSIB BANDUNG","23","37-21","44"));
        ArrayListKlasemen.add(new Klasemen("9","PERSIB BANDUNG","23","37-21","44"));
        ArrayListKlasemen.add(new Klasemen("10","PERSIB BANDUNG","23","37-21","44"));
    }
}
