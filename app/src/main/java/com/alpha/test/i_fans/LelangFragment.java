package com.alpha.test.i_fans;


import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.SyncStateContract;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.ArrayList;

import oogbox.api.odoo.OdooClient;
import oogbox.api.odoo.client.OdooVersion;
import oogbox.api.odoo.client.helper.OdooErrorException;
import oogbox.api.odoo.client.helper.data.OdooRecord;
import oogbox.api.odoo.client.helper.data.OdooResult;
import oogbox.api.odoo.client.helper.utils.OArguments;
import oogbox.api.odoo.client.helper.utils.ODomain;
import oogbox.api.odoo.client.helper.utils.OdooFields;
import oogbox.api.odoo.client.listeners.IOdooResponse;
import oogbox.api.odoo.client.listeners.OdooConnectListener;


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
    OdooClient client;

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
        adapter = new AdapterLelang(ArrayListLelang,getContext());
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
                    Intent AddLelang = new Intent(getActivity(),LelangAddActivity.class);
                    startActivity(AddLelang);
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

        }

        @Override
        protected Void doInBackground(Void... voids) {

            ArrayListLelang = new ArrayList<>();
            client = new OdooClient.Builder(getContext())
                    .setHost(sharedPrefManager.getSP_Host_url())
                    .setSession(sharedPrefManager.getSpSessionId())
                    .setSynchronizedRequests(false)
                    .setConnectListener(new OdooConnectListener() {
                        @Override
                        public void onConnected(OdooVersion version) {
                            OArguments arguments = new OArguments();
                            arguments.addNULL();

                            client.call_kw("persebaya.lelang", "list_lelang", arguments, new IOdooResponse() {
                                @Override
                                public void onResult(OdooResult result) {
                                    OdooRecord[] Records = result.getRecords();
                                    for (final OdooRecord record : Records) {
                                        ArrayListLelang.add(new lelang(
                                                String.valueOf(record.getInt("idlelang")),
                                                record.getString("namalelang"),
                                                record.getString("lelangimage"),
                                                record.getString("waktulelang"),
                                                String.valueOf(Math.round(record.getFloat("bidlelang"))),
                                                String.valueOf(Math.round(record.getFloat("binlelang"))),
                                                String.valueOf(Math.round(record.getFloat("inclelang"))),
                                                String.valueOf(record.getInt("pemiliklelang"))));
                                    }
                                    adapter = new AdapterLelang(ArrayListLelang,getContext());
                                    rv.setAdapter(adapter);
                                    adapter.notifyDataSetChanged();
                                    swiper.setRefreshing(false);
                                }

                                @Override
                                public boolean onError(OdooErrorException error) {
                                    Toast.makeText(getActivity(),error.toString(),Toast.LENGTH_SHORT).show();
                                    return super.onError(error);
                                }
                            });
                        }
                    }).build();
            return null;
        }
    }

}

