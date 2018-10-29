package com.example.nerita_hendra.i_fans;


import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class LelangFragment extends Fragment {

    ArrayList<lelang> ArrayListLelang;
    int RecyclerViewItemPosition ;
    SharedPrefManager sharedPrefManager;
    RecyclerView.LayoutManager llm;
    RecyclerView rv;
    View rootView;
    ProgressDialog progressDialog;
    SwipeRefreshLayout swiper;
    AdapterLelang adapter;

    public LelangFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_lelang, container, false);
        rv =  rootView.findViewById(R.id.rv_recycler_view_lelang);
        swiper = rootView.findViewById(R.id.swiperefresh_lelang);
        adapter = new AdapterLelang(ArrayListLelang);
        rv.setAdapter(adapter);
        llm = new LinearLayoutManager(getActivity());
        rv.setLayoutManager(llm);
        swiper.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new LelangAsyncTask().execute();
            }
        });
        sharedPrefManager = new SharedPrefManager(getActivity());
        progressDialog = new ProgressDialog(getActivity());
        rv.addOnItemTouchListener(new RecyclerView.OnItemTouchListener() {

            GestureDetector gestureDetector = new GestureDetector(getActivity(), new GestureDetector.SimpleOnGestureListener() {

                @Override public boolean onSingleTapUp(MotionEvent motionEvent) {

                    return true;
                }

            });

            @Override
            public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
                View ChildView = rv.findChildViewUnder(e.getX(), e.getY());

                if(ChildView != null && gestureDetector.onTouchEvent(e)) {

                    RecyclerViewItemPosition = rv.getChildAdapterPosition(ChildView);
                    Intent intent = new Intent(getActivity(),LelangDetailActivity.class);
                    intent.putExtra("nama",ArrayListLelang.get(RecyclerViewItemPosition).getNamalelang());
                    startActivity(intent);
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
        new LelangAsyncTask().execute();
        return rootView;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser && isResumed()){
            HomeActivity fabhome = (HomeActivity) getActivity();
            fabhome.fabBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    System.out.println("Lelang");
                }
            });
        }
    }

    public class LelangAsyncTask extends AsyncTask<Void,Void,Void>{
        @Override
        protected void onPreExecute() {
            swiper.setRefreshing(true);
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            adapter = new AdapterLelang(ArrayListLelang);
            rv.setAdapter(adapter);
            adapter.notifyDataSetChanged();
            swiper.setRefreshing(false);
        }

        @Override
        protected Void doInBackground(Void... voids) {
            ArrayListLelang = new ArrayList<>();
            try {
                OdooConnect oc = OdooConnect.connect(sharedPrefManager.getSpNamaUser(),sharedPrefManager.getSpPasswordUser());

                Object[] param = {new Object[]{
                        new Object[]{"status_lelang", "=", "jalan"}}};

                List<HashMap<String, Object>> data = oc.search_read("persebaya.lelang", param, "id","image","nama_barang", "ob","inc","binow","create_uid");

                for (int i = 0; i < data.size(); ++i) {
                    ArrayListLelang.add(new lelang(
                            String.valueOf(data.get(i).get("nama_barang")),
                            String.valueOf(data.get(i).get("image")),
                            String.valueOf("24"),
                            String.valueOf(data.get(i).get("ob")),
                            String.valueOf(data.get(i).get("binow")),
                            String.valueOf(data.get(i).get("inc")),
                            String.valueOf( data.get(i).get("create_uid"))));
                }

            } catch (Exception ex) {
                System.out.println("Error: " + ex);
            }
            return null;
        }
    }

}
