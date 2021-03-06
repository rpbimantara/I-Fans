package com.alpha.test.persebayaapp;


import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.ArrayList;

import oogbox.api.odoo.OdooClient;
import oogbox.api.odoo.client.helper.OdooErrorException;
import oogbox.api.odoo.client.helper.data.OdooRecord;
import oogbox.api.odoo.client.helper.data.OdooResult;
import oogbox.api.odoo.client.helper.utils.OArguments;
import oogbox.api.odoo.client.helper.utils.ODomain;
import oogbox.api.odoo.client.helper.utils.OdooFields;
import oogbox.api.odoo.client.helper.utils.OdooValues;
import oogbox.api.odoo.client.listeners.IOdooResponse;
import oogbox.api.odoo.client.listeners.OdooErrorListener;

import static com.alpha.test.persebayaapp.CommonUtils.getOdooConnection;
import static com.alpha.test.persebayaapp.CommonUtils.getOdooConnection1;
import static com.alpha.test.persebayaapp.CommonUtils.getSaldo;


/**
 * A simple {@link Fragment} subclass.
 */
public class LelangFragment extends Fragment implements AdapterLelang.InterfaceLelang, HomeActivity.LelangReloadCalled {
    public final String TAG = this.getClass().getSimpleName();
    ArrayList<lelang> ArrayListLelang = new ArrayList<>();
    int RecyclerViewItemPosition ;
    SharedPrefManager sharedPrefManager;
    RecyclerView.LayoutManager llm;
    RecyclerView rv;
    View rootView;
    ProgressDialog progressDialog;
    SwipeRefreshLayout swiper;
    AdapterLelang adapter;
    OdooClient client;
    Context context;

    public LelangFragment() {
        // Required empty public constructor
    }

    public static LelangFragment newInstance() {
        Bundle args = new Bundle();
        LelangFragment fragment = new LelangFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void Addbidder(final String idlelang, final String nilai, final String status, final ProgressDialog pgdialog) {
        getSaldo(context, new IOdooResponse() {
            @Override
            public void onResult(OdooResult result) {
                OdooRecord[] Records = result.getRecords();
                for (final OdooRecord record : Records) {
                    if (record.getString("state").equalsIgnoreCase("draft")){
                        pgdialog.dismiss();
                        Toast.makeText(context, "Update your profile first!", Toast.LENGTH_SHORT).show();
                    }else  if(record.getFloat("saldo") < Float.parseFloat(nilai)){
                        pgdialog.dismiss();
                        Toast.makeText(context, "Top up your coin to finish this transaction!", Toast.LENGTH_SHORT).show();
                    }else {
                        OdooValues values = new OdooValues();
                        values.put("product_id", idlelang);
                        values.put("user_bid", sharedPrefManager.getSpIdUser());
                        values.put("nilai", Integer.valueOf(nilai));
                        values.put("keterang", status);
                        client.create("persebaya.lelang.bid", values, new IOdooResponse() {
                            @Override
                            public void onResult(OdooResult result) {
                                final int serverId = result.getInt("result");
                                if (serverId >0){
                                    OArguments arguments = new OArguments();
                                    arguments.add(sharedPrefManager.getSpIdPartner());
                                    arguments.add(Integer.valueOf(idlelang));
                                    arguments.add(Integer.valueOf(nilai));
                                    arguments.add(status);
                                    client.call_kw("sale.order", "create_so_lelang", arguments, new IOdooResponse() {
                                        @Override
                                        public void onResult(OdooResult result) {
                                            int soId = 0;
                                            OdooRecord[] records = result.getRecords();
                                            for (OdooRecord record : records) {
                                                soId = soId+record.getInt("id");
                                            }
                                            Log.d(TAG,"Lelang SO : " + soId);
                                            loadLelang();
                                            if (soId > 0) {
                                                Confirm_so(soId,status);
                                            }else {
                                                if (status.equalsIgnoreCase("BID")) {
                                                    Toast.makeText(context, "Bid Added!", Toast.LENGTH_LONG).show();
                                                }else{
                                                    Toast.makeText(context, "Product Purchased!", Toast.LENGTH_LONG).show();
                                                }
                                            }
                                            pgdialog.dismiss();
                                        }

                                        @Override
                                        public boolean onError(OdooErrorException error) {
                                            pgdialog.dismiss();
                                            return super.onError(error);
                                        }
                                    });
                                }
                            }

                            @Override
                            public boolean onError(OdooErrorException error) {
                                pgdialog.dismiss();
                                Toast.makeText(context, String.valueOf(error.getMessage()), Toast.LENGTH_LONG).show();
                                return super.onError(error);
                            }
                        });
                    }
                }
            }
        });
    }

    public void Confirm_so(Integer order_id, final String status){
        OArguments arguments = new OArguments();
        arguments.add(order_id);
        client.call_kw("sale.order", "confirm_so", arguments, new IOdooResponse() {
            @Override
            public void onResult(OdooResult result) {
                Log.d(TAG,"Confirm SO LELANG : " + status);
                Toast.makeText(context, "Bid Added!", Toast.LENGTH_LONG).show();
                progressDialog.dismiss();
            }

            @Override
            public boolean onError(OdooErrorException error) {
                progressDialog.dismiss();
                return super.onError(error);
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_lelang, container, false);
        rv =  rootView.findViewById(R.id.rv_recycler_view_lelang);
        swiper = rootView.findViewById(R.id.swiperefresh_lelang);
        context = getContext();
        adapter = new AdapterLelang(ArrayListLelang,context,LelangFragment.this);
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
        sharedPrefManager = new SharedPrefManager(context);
        progressDialog = new ProgressDialog(context);
        client = getOdooConnection1(context, new OdooErrorListener() {
            @Override
            public void onError(OdooErrorException error) {
                swiper.setRefreshing(false);
                progressDialog.dismiss();
            }
        });
        loadLelang();
//        new LelangAsyncTask().execute();
        return rootView;
    }

    public void loadLelang(){
        swiper.setRefreshing(true);
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
                ArrayListLelang.clear();
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

    @Override
    public void onReloadCalled() {
        loadLelang();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        ((HomeActivity)getActivity()).setLelangReloadCallback(this);
    }
}

