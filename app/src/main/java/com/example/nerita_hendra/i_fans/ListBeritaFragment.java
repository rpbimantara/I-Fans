package com.example.nerita_hendra.i_fans;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class ListBeritaFragment extends Fragment {

    private ArrayList<ListBerita> ArrayListBerita;

    public ListBeritaFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View rootView = inflater.inflate(R.layout.fragment_list_berita, container, false);
        RecyclerView rv = (RecyclerView) rootView.findViewById(R.id.rv_recycler_view_list_berita);
//        rv.setHasFixedSize(true);
        addData();
        AdapterListBerita adapter = new AdapterListBerita(ArrayListBerita);
        rv.setAdapter(adapter);
        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        rv.setLayoutManager(llm);
        return rootView;
    }

    void addData(){
        ArrayListBerita = new ArrayList<>();
        ArrayListBerita.add(new ListBerita("Persebaya","PERSEBAYA","01-01-1990"));
        ArrayListBerita.add(new ListBerita("Persebaya U-19","PERSEBAYA","01-01-1990"));
        ArrayListBerita.add(new ListBerita("Persebaya Junior","PERSEBAYA","01-01-1990"));
        ArrayListBerita.add(new ListBerita("Persebaya U-17","PERSEBAYA","01-01-1990"));
        ArrayListBerita.add(new ListBerita("Persebaya U-16","PERSEBAYA","01-01-1990"));
        ArrayListBerita.add(new ListBerita("Persebaya U-15","PERSEBAYA","01-01-1990"));
        ArrayListBerita.add(new ListBerita("Persebaya","PERSEBAYA","01-01-1990"));
        ArrayListBerita.add(new ListBerita("Persebaya","PERSEBAYA","01-01-1990"));
        ArrayListBerita.add(new ListBerita("Persebaya","PERSEBAYA","01-01-1990"));
    }

}
