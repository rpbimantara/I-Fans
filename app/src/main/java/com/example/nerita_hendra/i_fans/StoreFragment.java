package com.example.nerita_hendra.i_fans;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class StoreFragment extends Fragment {

    ArrayList<Store> ArrayListStore;


    public StoreFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_store,container,false);
        RecyclerView rv = (RecyclerView) rootView.findViewById(R.id.rv_recycler_view_store);
        addData();
        AdapterStore adapter = new AdapterStore(ArrayListStore);
        rv.setAdapter(adapter);
        RecyclerView.LayoutManager Glm = new GridLayoutManager(getActivity(),3);
        rv.setLayoutManager(Glm);
        return rootView;
    }

    void addData(){
        ArrayListStore = new ArrayList<>();
        ArrayListStore.add(new Store("Baju","RP. 1.000.000"));
        ArrayListStore.add(new Store("Baju","RP. 2.000.000"));
        ArrayListStore.add(new Store("Baju","RP. 3.000.000"));
        ArrayListStore.add(new Store("Baju","RP. 4.000.000"));
        ArrayListStore.add(new Store("Baju","RP. 5.000.000"));
        ArrayListStore.add(new Store("Baju","RP. 6.000.000"));
        ArrayListStore.add(new Store("Baju","RP. 7.000.000"));
        ArrayListStore.add(new Store("Baju","RP. 8.000.000"));
        ArrayListStore.add(new Store("Baju","RP. 9.000.000"));
        ArrayListStore.add(new Store("Baju","RP. 10.000.000"));
        ArrayListStore.add(new Store("Baju","RP. 11.000.000"));
        ArrayListStore.add(new Store("Baju","RP. 12.000.000"));
        ArrayListStore.add(new Store("Baju","RP. 13.000.000"));
        ArrayListStore.add(new Store("Baju","RP. 14.000.000"));
        ArrayListStore.add(new Store("Baju","RP. 15.000.000"));
    }

}
