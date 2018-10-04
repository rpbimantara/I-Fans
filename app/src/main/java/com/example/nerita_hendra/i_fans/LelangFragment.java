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
public class LelangFragment extends Fragment {

    ArrayList<lelang> ArrayListLelang;

    public LelangFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_lelang, container, false);
        RecyclerView rv = (RecyclerView) rootView.findViewById(R.id.rv_recycler_view_lelang);
        addData();
        AdapterLelang adapter = new AdapterLelang(ArrayListLelang);
        rv.setAdapter(adapter);
        RecyclerView.LayoutManager llm = new LinearLayoutManager(getActivity());
        rv.setLayoutManager(llm);
        return rootView;
    }

    void addData(){
        ArrayListLelang = new ArrayList<>();
        ArrayListLelang.add(new lelang("KAOS","01:00","100","3000"));
        ArrayListLelang.add(new lelang("KAOSS","02:00","200","3000"));
        ArrayListLelang.add(new lelang("KAOSSS","03:00","300","3000"));
        ArrayListLelang.add(new lelang("KAOSSSS","04:00","400","3000"));
        ArrayListLelang.add(new lelang("KAOSSSSS","05:00","500","3000"));
        ArrayListLelang.add(new lelang("KAOSSSSSS","06:00","600","3000"));
        ArrayListLelang.add(new lelang("KAOSSSSSSS","07:00","700","3000"));
        ArrayListLelang.add(new lelang("KAOSSSSSSSS","08:00","800","3000"));
        ArrayListLelang.add(new lelang("KAOSSSSSSSSS","09:00","900","3000"));
        ArrayListLelang.add(new lelang("KAOSSSSSSSSSS","10:00","1000","3000"));

    }

}
