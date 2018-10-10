package com.example.nerita_hendra.i_fans;


import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
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
public class TerupdateFragment extends Fragment {

    ArrayList<Terupdate> ArrayListTerupdate;

    public TerupdateFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_terupdate, container, false);
        RecyclerView rv = (RecyclerView) rootView.findViewById(R.id.rv_recycler_view_hot_news);
        addData();
        AdapterTerupdate adapter = new  AdapterTerupdate(ArrayListTerupdate);
        rv.setAdapter(adapter);
        RecyclerView.LayoutManager llm = new  LinearLayoutManager(getActivity(),LinearLayoutManager.HORIZONTAL,false);
        rv.setLayoutManager(llm);
//        SharedPrefManager sharedPrefManager =  new SharedPrefManager(getActivity());
//        Toast.makeText(getActivity(), String.valueOf(sharedPrefManager.getSpIdUser()) ,Toast.LENGTH_SHORT).show();
        return rootView;
    }

    void addData(){
        ArrayListTerupdate = new ArrayList<>();
        ArrayListTerupdate.add(new Terupdate("ASDASDASDASDASDDDDDDDDDDDDDDDADS"));
        ArrayListTerupdate.add(new Terupdate("ASDASDASDASDASDDDDDDDDDDDDDDDADS"));
        ArrayListTerupdate.add(new Terupdate("ASDASDASDASDASDDDDDDDDDDDDDDDADS"));
        ArrayListTerupdate.add(new Terupdate("ASDASDASDASDASDDDDDDDDDDDDDDDADS"));
        ArrayListTerupdate.add(new Terupdate("ASDASDASDASDASDDDDDDDDDDDDDDDADS"));
        ArrayListTerupdate.add(new Terupdate("ASDASDASDASDASDDDDDDDDDDDDDDDADS"));
        ArrayListTerupdate.add(new Terupdate("ASDASDASDASDASDDDDDDDDDDDDDDDADS"));
        ArrayListTerupdate.add(new Terupdate("ASDASDASDASDASDDDDDDDDDDDDDDDADS"));
        ArrayListTerupdate.add(new Terupdate("ASDASDASDASDASDDDDDDDDDDDDDDDADS"));
        ArrayListTerupdate.add(new Terupdate("ASDASDASDASDASDDDDDDDDDDDDDDDADS"));

    }

}
