package com.example.nerita_hendra.i_fans;


import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import oogbox.api.odoo.OdooClient;
import oogbox.api.odoo.client.OdooVersion;
import oogbox.api.odoo.client.helper.data.OdooRecord;
import oogbox.api.odoo.client.helper.data.OdooResult;
import oogbox.api.odoo.client.helper.utils.ODomain;
import oogbox.api.odoo.client.helper.utils.OdooFields;
import oogbox.api.odoo.client.listeners.IOdooResponse;
import oogbox.api.odoo.client.listeners.OdooConnectListener;


/**
 * A simple {@link Fragment} subclass.
 */
public class StoreFragment extends Fragment {

    ArrayList<Store> ArrayListStore;
    int RecyclerViewItemPosition ;
    SharedPrefManager sharedPrefManager;
    View rootView;
    RecyclerView rv;
    ProgressDialog progressDialog;
    AdapterStore adapter;
    SwipeRefreshLayout swiper;
    OdooClient client;


    public StoreFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_store,container,false);
        rv =  rootView.findViewById(R.id.rv_recycler_view_store);
        swiper = rootView.findViewById(R.id.swiperefresh_store);
        sharedPrefManager = new SharedPrefManager(getActivity());
        progressDialog = new ProgressDialog(getActivity());
        adapter = new AdapterStore(ArrayListStore);
        rv.setAdapter(adapter);
        rv.setLayoutManager(new GridLayoutManager(getActivity(),3));
        swiper.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new StoreTask().execute();
            }
        });
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
                    Intent intent = new Intent(getActivity(),StoreDetailActivity.class);
                    intent.putExtra("id",ArrayListStore.get(RecyclerViewItemPosition).getId());
                    intent.putExtra("nama",ArrayListStore.get(RecyclerViewItemPosition).getNamabarang());
                    intent.putExtra("harga",ArrayListStore.get(RecyclerViewItemPosition).getHargabarang());
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
        new StoreTask().execute();
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
                    Intent AddStore = new Intent(getActivity(),StoreAddActivity.class);
                    startActivity(AddStore);
                }
            });
        }
    }

    public class StoreTask extends AsyncTask<Void,Void,Void>{
        @Override
        protected void onPreExecute() {
            swiper.setRefreshing(true);
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            swiper.setRefreshing(false);
        }
        @Override
        protected Void doInBackground(Void... voids) {
            ArrayListStore = new ArrayList<>();
            client = new OdooClient.Builder(getContext())
                    .setHost(sharedPrefManager.getSP_Host_url())
                    .setSession("f35afb7584ea1195be5400d65415d6ab8f7a9440")
                    .setSynchronizedRequests(false)
                    .setConnectListener(new OdooConnectListener() {
                        @Override
                        public void onConnected(OdooVersion version) {
                            ODomain domain = new ODomain();
                            domain.add("active", "=", true);
                            domain.add("type", "=", "product");

                            OdooFields fields = new OdooFields();
                            fields.addAll("id","image_medium","name", "type","default_code","cated_ig","list_price");

                            int offset = 0;
                            int limit = 80;

                            String sorting = "id ASC";

                            client.searchRead("product.template", domain, fields, offset, limit, sorting, new IOdooResponse() {
                                @Override
                                public void onResult(OdooResult result) {
                                    OdooRecord[] records = result.getRecords();
                                    for (OdooRecord record : records) {
                                        String code = " ";
                                        if (!record.getString("default_code").equalsIgnoreCase("false") || !record.getString("default_code").equalsIgnoreCase(" ")){
                                            code = "["+record.getString("default_code") +"] ";
                                        }

                                        ArrayListStore.add(new Store(
                                               String.valueOf(record.getInt("id")),
                                                record.getString("image_medium"),
                                                code +record.getString("name"),
                                                String.valueOf(Math.round(record.getFloat("list_price")))));
                                    }
                                    adapter = new AdapterStore(ArrayListStore);
                                    rv.setAdapter(adapter);
                                    adapter.notifyDataSetChanged();
                                }

                            });
                        }
                    }).build();
            return null;
        }
    }

}
