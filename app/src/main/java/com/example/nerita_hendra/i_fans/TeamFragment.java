package com.example.nerita_hendra.i_fans;


import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
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
public class TeamFragment extends Fragment {

    ArrayList<Team> ArrayListTeam;
    int RecyclerViewItemPosition ;
    SharedPrefManager sharedPrefManager;
    View rootView;
    RecyclerView rv;
    ProgressDialog progressDialog;
    AdapterTeam adapter;
    SwipeRefreshLayout swiper;

    public TeamFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_team, container, false);
        rv = rootView.findViewById(R.id.rv_recycler_view_team);
        swiper = rootView.findViewById(R.id.swiperefresh_team);
        sharedPrefManager = new SharedPrefManager(getActivity());
        progressDialog = new ProgressDialog(getActivity());
        adapter = new AdapterTeam(ArrayListTeam);
        rv.setAdapter(adapter);
        rv.setLayoutManager(new GridLayoutManager(getActivity(),2));
        swiper.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new TeamTask().execute();
            }
        });
        new TeamTask().execute();
        return rootView;
    }

    public  class TeamTask extends AsyncTask<Void,Void,Void>{
        @Override
        protected void onPreExecute() {
            swiper.setRefreshing(true);
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            adapter = new AdapterTeam(ArrayListTeam);
            rv.setAdapter(adapter);
            adapter.notifyDataSetChanged();
            swiper.setRefreshing(false);
            super.onPostExecute(aVoid);
        }

        @Override
        protected Void doInBackground(Void... voids) {
            ArrayListTeam = new ArrayList<>();
            try {
                OdooConnect oc = OdooConnect.connect(sharedPrefManager.getSpNamaUser(),sharedPrefManager.getSpPasswordUser());

                Object[] param = {new Object[]{
                        new Object[]{"department_id", "=", 2}}};

                List<HashMap<String, Object>> data = oc.search_read("hr.employee", param, "id","image","name", "job_id","status_pemain");

                for (int i = 0; i < data.size(); ++i) {
                    ArrayListTeam.add(new Team(
                            data.get(i).get("name").toString(),
                            data.get(i).get("image").toString(),
                            data.get(i).get("status_pemain").toString(),
                            data.get(i).get("job_id").toString(),
                            ""
                    ));
                }
            } catch (Exception ex) {
                System.out.println("Error: " + ex);
            }
            return null;
        }
    }

}
