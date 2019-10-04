package com.alpha.test.persebayaapp;


import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
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
import oogbox.api.odoo.client.helper.utils.OdooValues;
import oogbox.api.odoo.client.listeners.IOdooResponse;

import static com.alpha.test.persebayaapp.CommonUtils.getOdooConnection;


/**
 * A simple {@link Fragment} subclass.
 */
public class LelangFragment extends Fragment implements InterfaceLelang {

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

    public static LelangFragment newInstance() {
        Bundle args = new Bundle();
        LelangFragment fragment = new LelangFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public void Addbidder(final String idlelang, final String nilai, final String status, final Context context, final SharedPrefManager sharedPrefManager){
        if (sharedPrefManager.getSpUserState().equalsIgnoreCase("draft")){
            Toast.makeText(context, "Update your profile first!", Toast.LENGTH_SHORT).show();
        }else {
            OdooValues values = new OdooValues();
            values.put("product_id", idlelang);
            values.put("user_bid", sharedPrefManager.getSpIdUser());
            values.put("nilai", Integer.valueOf(nilai));
            values.put("keterang", status);

            client.create("persebaya.lelang.bid", values, new IOdooResponse() {
                @Override
                public void onResult(OdooResult result) {
                    int serverId = result.getInt("result");
                }

                @Override
                public boolean onError(OdooErrorException error) {
                    Toast.makeText(context, String.valueOf(error.getMessage()), Toast.LENGTH_LONG).show();
                    return super.onError(error);
                }
            });
        }
//        client = new OdooClient.Builder(context)
//                .setHost(sharedPrefManager.getSP_Host_url())
//                .setSession(sharedPrefManager.getSpSessionId())
//                .setSynchronizedRequests(false)
//                .setConnectListener(new OdooConnectListener() {
//                    @Override
//                    public void onConnected(OdooVersion version) {
//                        OdooValues values = new OdooValues();
//                        values.put("product_id", idlelang);
//                        values.put("user_bid", sharedPrefManager.getSpIdUser());
//                        values.put("nilai", Integer.valueOf(nilai));
//                        values.put("keterang", status);
//
//                        client.create("persebaya.lelang.bid", values, new IOdooResponse() {
//                            @Override
//                            public void onResult(OdooResult result) {
//                                int serverId = result.getInt("result");
//                            }
//
//                            @Override
//                            public boolean onError(OdooErrorException error) {
//                                Toast.makeText(context,String.valueOf(error.getMessage()),Toast.LENGTH_LONG).show();
//                                return super.onError(error);
//                            }
//                        });
//                    }
//
//                }).build();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_lelang, container, false);
        rv =  rootView.findViewById(R.id.rv_recycler_view_lelang);
        swiper = rootView.findViewById(R.id.swiperefresh_lelang);
        rv.setAdapter(adapter);
        llm = new LinearLayoutManager(getActivity());
        rv.setLayoutManager(llm);
        swiper.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
//                new LelangAsyncTask().execute();
                loadLelang();
            }
        });
        sharedPrefManager = new SharedPrefManager(getActivity());
        progressDialog = new ProgressDialog(getActivity());
        client = getOdooConnection(getContext());
        loadLelang();
//        new LelangAsyncTask().execute();
        return rootView;
    }

    public void loadLelang(){
        swiper.setRefreshing(true);
        ArrayListLelang = new ArrayList<>();
        ODomain domain = new ODomain();
        domain.add("status_lelang", "=", "jalan");
        domain.add("type", "=", "lelang");

        OdooFields fields = new OdooFields();
        fields.addAll("id","image_medium","name", "ob","inc","binow","due_date","create_uid");

        int offset = 0;
        int limit = 80;

        String sorting = "due_date ASC";

        client.searchRead("product.template", domain, fields, offset, limit, sorting,new IOdooResponse() {
            @Override
            public void onResult(OdooResult result) {
                OdooRecord[] Records = result.getRecords();
                for (final OdooRecord record : Records) {
                    ArrayListLelang.add(new lelang(
                            String.valueOf(record.getInt("id")),
                            record.getString("name"),
                            record.getString("image_medium"),
                            record.getString("due_date"),
                            String.valueOf(Math.round(record.getFloat("ob"))),
                            String.valueOf(Math.round(record.getFloat("binow"))),
                            String.valueOf(Math.round(record.getFloat("inc"))),
                            String.valueOf(record.getInt("create_uid"))));
                }
                adapter = new AdapterLelang(ArrayListLelang,getContext(),LelangFragment.newInstance());
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


//    @Override
//    public void setUserVisibleHint(boolean isVisibleToUser) {
//        super.setUserVisibleHint(isVisibleToUser);
//        if (isVisibleToUser && isResumed()){
//            onResume();
//        }
//    }
//
//    @Override
//    public void onAttachFragment(Fragment childFragment) {
//        super.onAttachFragment(childFragment);
//        HomeActivity fabhome = (HomeActivity) getActivity();
//        fabhome.fabBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent AddLelang = new Intent(getActivity(),LelangAddActivity.class);
//                startActivity(AddLelang);
//            }
//        });
//    }
//
//    @Override
//    public void onResume() {
//        super.onResume();
//        if (!getUserVisibleHint())
//        {
//            return;
//        }
//
//    }

//    public class LelangAsyncTask extends AsyncTask<Void,Void,Void>{
//        @Override
//        protected void onPreExecute() {
//            swiper.setRefreshing(true);
//        }
//
//        @Override
//        protected Void doInBackground(Void... voids) {
//            ArrayListLelang = new ArrayList<>();
//            client = new OdooClient.Builder(getContext())
//                    .setHost(sharedPrefManager.getSP_Host_url())
//                    .setSession(sharedPrefManager.getSpSessionId())
//                    .setSynchronizedRequests(false)
//                    .setConnectListener(new OdooConnectListener() {
//                        @Override
//                        public void onConnected(OdooVersion version) {
//                            ODomain domain = new ODomain();
//                            domain.add("status_lelang", "=", "jalan");
//                            domain.add("type", "=", "lelang");
//
//                            OdooFields fields = new OdooFields();
//                            fields.addAll("id","image_medium","name", "ob","inc","binow","due_date","create_uid");
//
//                            int offset = 0;
//                            int limit = 80;
//
//                            String sorting = "due_date ASC";
//
//                            client.searchRead("product.template", domain, fields, offset, limit, sorting,new IOdooResponse() {
//                                @Override
//                                public void onResult(OdooResult result) {
//                                    OdooRecord[] Records = result.getRecords();
//                                    for (final OdooRecord record : Records) {
//                                        ArrayListLelang.add(new lelang(
//                                                String.valueOf(record.getInt("id")),
//                                                record.getString("name"),
//                                                record.getString("image_medium"),
//                                                record.getString("due_date"),
//                                                String.valueOf(Math.round(record.getFloat("ob"))),
//                                                String.valueOf(Math.round(record.getFloat("binow"))),
//                                                String.valueOf(Math.round(record.getFloat("inc"))),
//                                                String.valueOf(record.getInt("create_uid"))));
//                                    }
//                                    adapter = new AdapterLelang(ArrayListLelang,getContext(),LelangFragment.newInstance());
//                                    rv.setAdapter(adapter);
//                                    adapter.notifyDataSetChanged();
//                                    swiper.setRefreshing(false);
//                                }
//
//                                @Override
//                                public boolean onError(OdooErrorException error) {
//                                    Toast.makeText(getActivity(),error.toString(),Toast.LENGTH_SHORT).show();
//                                    return super.onError(error);
//                                }
//                            });
//                        }
//                    }).build();
//            return null;
//        }
//    }

}

