package com.example.nerita_hendra.i_fans;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class KlasemenFragment extends Fragment {

    ArrayList<Klasemen> ArrayListKlasemen;
    int RecyclerViewItemPosition ;
    SharedPrefManager sharedPrefManager;

    public KlasemenFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_klasemen, container, false);
        RecyclerView rv = (RecyclerView) rootView.findViewById(R.id.rv_recycler_view_klasemen);
        sharedPrefManager = new SharedPrefManager(getActivity());
        addData(sharedPrefManager.getSpNamaUser(),sharedPrefManager.getSpPasswordUser(),sharedPrefManager.getSpIdUser());
        AdapterKlasemen adapter = new AdapterKlasemen(ArrayListKlasemen);
        rv.setAdapter(adapter);
        RecyclerView.LayoutManager llm = new LinearLayoutManager(getActivity());
        rv.setLayoutManager(llm);
        return rootView;
    }


    void addData(String user, String pass,Integer IdUser){
        ArrayListKlasemen =  new ArrayList<>();
        try {
            OdooConnect oc = OdooConnect.connect( user, pass);

            Object[] param = {new Object[]{
                    new Object[]{"liga_id", "=", "1"}}};

            List<HashMap<String, Object>> data = oc.search_read("persebaya.liga.klasemen", param, "id","club_id", "play","win","draw","lose","gm","gk","point");
            ArrayListKlasemen.add(new Klasemen(
                    String.valueOf("No."),
                    String.valueOf("Club"),
                    String.valueOf("P"),
                    String.valueOf("Win"),
                    String.valueOf("Draw"),
                    String.valueOf("Lose"),
                    String.valueOf("+/-"),
                    String.valueOf("Pts")));
            for (int i = 0; i < data.size(); ++i) {
                ArrayListKlasemen.add(new Klasemen(
                        String.valueOf(i+1),
                        String.valueOf(data.get(i).get("club_id")),
                        String.valueOf(data.get(i).get("play")),
                        String.valueOf(data.get(i).get("win")),
                        String.valueOf(data.get(i).get("draw")),
                        String.valueOf(data.get(i).get("lose")),
                        String.valueOf(data.get(i).get("gm")),
                        String.valueOf( data.get(i).get("point"))));
            }
        } catch (Exception ex) {
            System.out.println("Error: " + ex);
        }
    }
}
