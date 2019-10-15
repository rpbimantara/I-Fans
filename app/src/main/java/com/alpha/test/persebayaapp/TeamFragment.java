package com.alpha.test.persebayaapp;


import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import oogbox.api.odoo.OdooClient;
import oogbox.api.odoo.client.helper.OdooErrorException;
import oogbox.api.odoo.client.helper.data.OdooRecord;
import oogbox.api.odoo.client.helper.data.OdooResult;
import oogbox.api.odoo.client.helper.utils.ODomain;
import oogbox.api.odoo.client.helper.utils.OdooFields;
import oogbox.api.odoo.client.listeners.IOdooResponse;
import oogbox.api.odoo.client.listeners.OdooErrorListener;

import static com.alpha.test.persebayaapp.CommonUtils.getOdooConnection;
import static com.alpha.test.persebayaapp.CommonUtils.getOdooConnection1;


/**
 * A simple {@link Fragment} subclass.
 */
public class TeamFragment extends Fragment {

    ArrayList<Team> ArrayListTeamStaff;
    ArrayList<Team> ArrayListAthlete;
    int RecyclerViewItemPosition ;
    SharedPrefManager sharedPrefManager;
    private View rootView;
    RecyclerView rvStaff,rvAthlete;
    ProgressDialog progressDialog;
    AdapterTeamStaff adapterStaff;
    AdapterTeam adapterAthlete;
    SwipeRefreshLayout swiper;
    OdooClient client;
    TextView txtStaff,txtClub;

    public TeamFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public static TeamFragment newInstance() {
        Bundle args = new Bundle();
        TeamFragment fragment = new TeamFragment();
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        if (rootView == null){
            rootView = inflater.inflate(R.layout.fragment_team, container, false);
            rvStaff = rootView.findViewById(R.id.rv_recycler_view_team);
            rvAthlete = rootView.findViewById(R.id.rv_recycler_view_team_athlete);
            swiper = rootView.findViewById(R.id.swiperefresh_team);
            sharedPrefManager = new SharedPrefManager(getActivity());
            progressDialog = new ProgressDialog(getActivity());
            txtClub = rootView.findViewById(R.id.txt_player_club);
            txtStaff = rootView.findViewById(R.id.txt_staff_club);
            txtClub.setVisibility(View.INVISIBLE);
            txtStaff.setVisibility(View.INVISIBLE);
            rvStaff.setAdapter(adapterStaff);
            rvAthlete.setAdapter(adapterAthlete);
            rvStaff.setLayoutManager(new LinearLayoutManager(getActivity(),LinearLayoutManager.HORIZONTAL,false));
            rvAthlete.setLayoutManager(new GridLayoutManager(getActivity(),3));
            client = getOdooConnection1(getContext(), new OdooErrorListener() {
                @Override
                public void onError(OdooErrorException error) {
                    swiper.setRefreshing(false);
                }
            });
            swiper.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {
                    loadTeamStaff();
                    loadTeamAthlete();
//                    new TeamTask().execute();
                }
            });
            rvAthlete.addOnItemTouchListener(new RecyclerView.OnItemTouchListener() {
                GestureDetector gestureDetector = new GestureDetector(getActivity(), new GestureDetector.SimpleOnGestureListener() {

                    @Override
                    public boolean onSingleTapUp(MotionEvent motionEvent) {

                        return true;
                    }

                });
                @Override
                public boolean onInterceptTouchEvent(@NonNull RecyclerView r, @NonNull MotionEvent e) {
                    View ChildView = rvAthlete.findChildViewUnder(e.getX(), e.getY());

                    if (ChildView != null && gestureDetector.onTouchEvent(e)) {
                        try {
                            RecyclerViewItemPosition = rvAthlete.getChildAdapterPosition(ChildView);
                            Intent intent = new Intent(getActivity(), TeamDetailActivity.class);
                            intent.putExtra("id_atlete", ArrayListAthlete.get(RecyclerViewItemPosition).getId());
//                            Toast.makeText(getContext(),ArrayListAthlete.get(RecyclerViewItemPosition).getNama(),Toast.LENGTH_LONG).show();
                            startActivity(intent);
                        } catch (Exception err) {
                            System.out.println(err);
                        }

                    }
                    return false;
                }

                @Override
                public void onTouchEvent(@NonNull RecyclerView recyclerView, @NonNull MotionEvent motionEvent) {

                }

                @Override
                public void onRequestDisallowInterceptTouchEvent(boolean b) {

                }
            });
            loadTeamStaff();
            loadTeamAthlete();
//            new TeamTask().execute();
        }
        return rootView;
    }


    public void loadTeamStaff(){
        swiper.setRefreshing(true);
        ArrayListTeamStaff = new ArrayList<>();
        ODomain domain = new ODomain();
        domain.add("club_id", "=", sharedPrefManager.getSpIdClub());
        domain.add("department_id", "=", 9);

        OdooFields fields = new OdooFields();
        fields.addAll("id","image","name", "job_id","status_pemain","no_punggung");

        int offset = 0;
        int limit = 80;

        String sorting = "id DESC";

        client.searchRead("hr.employee", domain, fields, offset, limit, sorting, new IOdooResponse() {
            @Override
            public void onResult(OdooResult result) {
                OdooRecord[] records = result.getRecords();
                for (OdooRecord record : records) {
                    ArrayListTeamStaff.add(new Team(
                            record.getInt("id"),
                            record.getString("name"),
                            record.getString("image"),
                            record.getString("status_pemain"),
                            record.getString("job_id"),
                            String.valueOf(record.getInt("no_punggung"))
                    ));
                }
                adapterStaff = new AdapterTeamStaff(ArrayListTeamStaff);
                rvStaff.setAdapter(adapterStaff);
                if (ArrayListTeamStaff.size()> 0){
                    txtStaff.setVisibility(View.VISIBLE);
                }
                adapterStaff.notifyDataSetChanged();
            }
        });
    }

    public void loadTeamAthlete(){
        ArrayListAthlete = new ArrayList<>();
        ODomain domain = new ODomain();
        domain.add("club_id", "=", sharedPrefManager.getSpIdClub());
        domain.add("department_id", "=", 8);

        OdooFields fields = new OdooFields();
        fields.addAll("id","image","name", "job_id","status_pemain","no_punggung");

        int offset = 0;
        int limit = 80;

        String sorting = "job_id";

        client.searchRead("hr.employee", domain, fields, offset, limit, sorting, new IOdooResponse() {
            @Override
            public void onResult(OdooResult result) {
                OdooRecord[] records = result.getRecords();
                for (OdooRecord record : records) {
                    ArrayListAthlete.add(new Team(
                            record.getInt("id"),
                            record.getString("name"),
                            record.getString("image"),
                            record.getString("status_pemain"),
                            record.getString("job_id"),
                            String.valueOf(record.getInt("no_punggung"))
                    ));
                }
                adapterAthlete = new AdapterTeam(ArrayListAthlete);
                rvAthlete.setAdapter(adapterAthlete);
                adapterAthlete.notifyDataSetChanged();
                if (ArrayListAthlete.size()> 0){
                    txtClub.setVisibility(View.VISIBLE);
                }
                swiper.setRefreshing(false);
            }
        });
    }

//    public  class TeamTask extends AsyncTask<Void,Void,Void>{
//        @Override
//        protected void onPreExecute() {
//            swiper.setRefreshing(true);
//            super.onPreExecute();
//        }
//
//        @Override
//        protected Void doInBackground(Void... voids) {
//            ArrayListTeamStaff = new ArrayList<>();
//            ArrayListAthlete = new ArrayList<>();
//            client = new OdooClient.Builder(getContext())
//                    .setHost(sharedPrefManager.getSP_Host_url())
//                    .setSession(sharedPrefManager.getSpSessionId())
//                    .setSynchronizedRequests(false)
//                    .setConnectListener(new OdooConnectListener() {
//                        @Override
//                        public void onConnected(OdooVersion version) {
//                            ODomain domain = new ODomain();
//                            domain.add("club_id", "=", sharedPrefManager.getSpIdClub());
//                            domain.add("department_id", "=", 9);
//
//                            OdooFields fields = new OdooFields();
//                            fields.addAll("id","image","name", "job_id","status_pemain","no_punggung");
//
//                            int offset = 0;
//                            int limit = 80;
//
//                            String sorting = "id DESC";
//
//                            client.searchRead("hr.employee", domain, fields, offset, limit, sorting, new IOdooResponse() {
//                                @Override
//                                public void onResult(OdooResult result) {
//                                    OdooRecord[] records = result.getRecords();
//                                    for (OdooRecord record : records) {
//                                        ArrayListTeamStaff.add(new Team(
//                                                record.getInt("id"),
//                                                record.getString("name"),
//                                                record.getString("image"),
//                                                record.getString("status_pemain"),
//                                                record.getString("job_id"),
//                                                String.valueOf(record.getInt("no_punggung"))
//                                        ));
//                                    }
//                                    adapterStaff = new AdapterTeamStaff(ArrayListTeamStaff);
//                                    rvStaff.setAdapter(adapterStaff);
//                                    if (ArrayListTeamStaff.size()> 0){
//                                        txtStaff.setVisibility(View.VISIBLE);
//                                    }
//                                    adapterStaff.notifyDataSetChanged();
//                                }
//                            });
//                        }
//                    }).build();
//
//            client = new OdooClient.Builder(getContext())
//                    .setHost(sharedPrefManager.getSP_Host_url())
//                    .setSession(sharedPrefManager.getSpSessionId())
//                    .setSynchronizedRequests(false)
//                    .setConnectListener(new OdooConnectListener() {
//                        @Override
//                        public void onConnected(OdooVersion version) {
//                            ODomain domain = new ODomain();
//                            domain.add("club_id", "=", sharedPrefManager.getSpIdClub());
//                            domain.add("department_id", "=", 8);
//
//                            OdooFields fields = new OdooFields();
//                            fields.addAll("id","image","name", "job_id","status_pemain","no_punggung");
//
//                            int offset = 0;
//                            int limit = 80;
//
//                            String sorting = "job_id";
//
//                            client.searchRead("hr.employee", domain, fields, offset, limit, sorting, new IOdooResponse() {
//                                @Override
//                                public void onResult(OdooResult result) {
//                                    OdooRecord[] records = result.getRecords();
//                                    for (OdooRecord record : records) {
//                                        ArrayListAthlete.add(new Team(
//                                                record.getInt("id"),
//                                                record.getString("name"),
//                                                record.getString("image"),
//                                                record.getString("status_pemain"),
//                                                record.getString("job_id"),
//                                                String.valueOf(record.getInt("no_punggung"))
//                                        ));
//                                    }
//                                    adapterAthlete = new AdapterTeam(ArrayListAthlete);
//                                    rvAthlete.setAdapter(adapterAthlete);
//                                    adapterAthlete.notifyDataSetChanged();
//                                    if (ArrayListAthlete.size()> 0){
//                                        txtClub.setVisibility(View.VISIBLE);
//                                    }
//                                    swiper.setRefreshing(false);
//                                }
//                            });
//                        }
//                    }).build();
//            return null;
//        }
//    }

}
