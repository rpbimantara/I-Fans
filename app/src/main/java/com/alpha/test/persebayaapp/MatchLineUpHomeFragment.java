package com.alpha.test.persebayaapp;


import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

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
public class MatchLineUpHomeFragment extends Fragment {

    private View rootView;
    RecyclerView rvLineUpHomeCore,rvLineUpHome;
    int RecyclerViewItemPosition ;
    SwipeRefreshLayout swiper;
    ArrayList<MatchLineUp> ArrayListMatchLineUpHomeCore = new ArrayList<>();
    ArrayList<MatchLineUp> ArrayListMatchLineUpHome = new ArrayList<>();
    OdooClient client;
    SharedPrefManager sharedPrefManager;
    AdapterLineUpHome adapterCore;
    AdapterLineUpHome adapter;
    Context context;

    public MatchLineUpHomeFragment() {
        // Required empty public constructor
    }

    public static MatchLineUpHomeFragment newInstance() {
        Bundle args = new Bundle();
        MatchLineUpHomeFragment fragment = new MatchLineUpHomeFragment();
        fragment.setArguments(args);
        return fragment;
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        if (rootView==null){
            rootView = inflater.inflate(R.layout.fragment_match_line_up_home, container, false);
            rvLineUpHome = rootView.findViewById(R.id.rv_recycler_view_match_line_up_home);
            rvLineUpHomeCore = rootView.findViewById(R.id.rv_recycler_view_match_line_up_home_core);
            swiper = rootView.findViewById(R.id.swiperefresh_match_line_up_home);
            context = getContext();
            swiper.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {
                    loadLineUpHome();
//                    new LineUpHomeTask().execute();
                }
            });
            sharedPrefManager = new  SharedPrefManager(getContext());
            adapter = new AdapterLineUpHome(ArrayListMatchLineUpHome);
            adapterCore = new AdapterLineUpHome(ArrayListMatchLineUpHomeCore);
            rvLineUpHome.setAdapter(adapter);
            rvLineUpHome.setLayoutManager(new LinearLayoutManager(getActivity()));
            rvLineUpHomeCore.setAdapter(adapterCore);
            rvLineUpHomeCore.setLayoutManager(new LinearLayoutManager(getActivity()));
            rvLineUpHomeCore.addOnItemTouchListener(new RecyclerView.OnItemTouchListener() {

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
//                        Intent intent = new Intent(getActivity(), RatingLineUpActivity.class);
//                        intent.putExtra("id_jadwal", Integer.valueOf(ArrayListMatchLineUpHomeCore.get(RecyclerViewItemPosition).getJadwal_id()));
//                        intent.putExtra("id_player", ArrayListMatchLineUpHomeCore.get(RecyclerViewItemPosition).getPlayer_id());
                        cekRating(Integer.valueOf(ArrayListMatchLineUpHomeCore.get(RecyclerViewItemPosition).getJadwal_id()),Integer.valueOf(ArrayListMatchLineUpHomeCore.get(RecyclerViewItemPosition).getPlayer_id()));
//                        startActivity(intent);
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
            rvLineUpHome.addOnItemTouchListener(new RecyclerView.OnItemTouchListener() {

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
//                        Intent intent = new Intent(getActivity(), RatingLineUpActivity.class);
//                        intent.putExtra("id_jadwal", Integer.valueOf(ArrayListMatchLineUpHome.get(RecyclerViewItemPosition).getJadwal_id()));
//                        intent.putExtra("id_player", ArrayListMatchLineUpHome.get(RecyclerViewItemPosition).getPlayer_id());
                        cekRating(Integer.valueOf(ArrayListMatchLineUpHome.get(RecyclerViewItemPosition).getJadwal_id()),Integer.valueOf(ArrayListMatchLineUpHome.get(RecyclerViewItemPosition).getPlayer_id()));
//                        startActivity(intent);
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
            client = getOdooConnection1(getContext(), new OdooErrorListener() {
                @Override
                public void onError(OdooErrorException error) {
                    swiper.setRefreshing(false);
                }
            });
            loadLineUpHome();
        }
        return rootView;
    }

    public void loadLineUpHome(){
        swiper.setRefreshing(true);
        ODomain domain = new ODomain();
        domain.add("jadwal_id", "=",getActivity().getIntent().getExtras().get("id_jadwal"));

        OdooFields fields = new OdooFields();
        fields.addAll("id","jadwal_id","home", "player_id","department_id","job_id","no_punggung","status_pemain");

        int offset = 0;
        int limit = 0;

        String sorting = "id DESC";

        client.searchRead("persebaya.line.up.home", domain, fields, offset, limit, sorting, new IOdooResponse() {
            @Override
            public void onResult(OdooResult result) {
                ArrayListMatchLineUpHome.clear();
                ArrayListMatchLineUpHomeCore.clear();
                OdooRecord[] records = result.getRecords();
                for (OdooRecord record : records) {
                    if (record.getString("status_pemain").equalsIgnoreCase("core")){
                        ArrayListMatchLineUpHomeCore.add(new MatchLineUp(
                                String.valueOf(record.getInt("id")),
                                String.valueOf(record.getInt("jadwal_id")),
                                String.valueOf(record.getInt("player_id")),
                                record.getString("player_id"),
                                String.valueOf(Math.round(record.getFloat("no_punggung"))),
                                record.getString("job_id"),
                                record.getString("home"),
                                record.getString("status_pemain")
                        ));
                    }else {
                        ArrayListMatchLineUpHome.add(new MatchLineUp(
                                String.valueOf(record.getInt("id")),
                                String.valueOf(record.getInt("jadwal_id")),
                                String.valueOf(record.getInt("player_id")),
                                record.getString("player_id"),
                                String.valueOf(Math.round(record.getFloat("no_punggung"))),
                                record.getString("job_id"),
                                record.getString("home"),
                                record.getString("status_pemain")
                        ));
                    }
                }
                adapter.notifyDataSetChanged();
                adapterCore.notifyDataSetChanged();
                swiper.setRefreshing(false);
            }
            @Override
            public boolean onError(OdooErrorException error) {
                swiper.setRefreshing(false);
                return super.onError(error);
            }
        });
    }


    public void cekRating(final Integer id_jadwal, final Integer id_player){
        ODomain domain = new ODomain();
        domain.add("employee_id", "=", id_player);
        domain.add("jadwal_id", "=", id_jadwal);
        domain.add("create_uid", "=", sharedPrefManager.getSpIdUser());

        OdooFields fields = new OdooFields();
        fields.addAll("id");

        int offset = 0;
        int limit = 80;

        String sorting = "id DESC";

        client.searchRead("persebaya.rating", domain, fields, offset, limit, sorting, new IOdooResponse() {
            @Override
            public void onResult(OdooResult result) {
                OdooRecord[] Records = result.getRecords();
                System.out.println(result.toString());
                if (result.getFloat("length") > 0 ){
                    for (final OdooRecord record : Records) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(context);
                        builder.setTitle(R.string.app_name);
                        builder.setMessage("You Have been Give Rate, wanna edit now?");
                        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                Intent intent = new Intent(getActivity(), RatingLineUpActivity.class);
                                intent.putExtra("id_jadwal", id_jadwal);
                                intent.putExtra("id_player", id_player);
                                intent.putExtra("id_rating", record.getInt("id"));
                                startActivity(intent);
                                dialogInterface.dismiss();
                            }
                        });
                        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                            }
                        });
                        final AlertDialog alertDialog = builder.create();
                        alertDialog.show();
                    }
                }else{
                    Intent intent = new Intent(getActivity(), RatingLineUpActivity.class);
                    intent.putExtra("id_jadwal", id_jadwal);
                    intent.putExtra("id_player", id_player);
                    intent.putExtra("id_rating", 0);
                    startActivity(intent);
                }
            }

            @Override
            public boolean onError(OdooErrorException error) {
                Toast.makeText(getContext(),error.toString(),Toast.LENGTH_LONG).show();
                return true;
            }
        });
    }



}
