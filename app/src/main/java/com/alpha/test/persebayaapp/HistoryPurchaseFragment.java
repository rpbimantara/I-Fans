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
import oogbox.api.odoo.client.helper.utils.OArguments;
import oogbox.api.odoo.client.helper.utils.OdooValues;
import oogbox.api.odoo.client.listeners.IOdooResponse;
import oogbox.api.odoo.client.listeners.OdooErrorListener;

import static com.alpha.test.persebayaapp.CommonUtils.getOdooConnection1;


/**
 * A simple {@link Fragment} subclass.
 */
public class HistoryPurchaseFragment extends Fragment implements AdapterHistory.ShippingListener {

    ArrayList<History> ArrayListHistory = new ArrayList<>();
    private View rootView;
    RecyclerView rv;
    SharedPrefManager sharedPrefManager;
    OdooClient client;
    SwipeRefreshLayout swiper;
    Context context;
    AdapterHistory adapter;
    ProgressDialog progressDialog;

    public HistoryPurchaseFragment() {
        // Required empty public constructor
    }

    public static HistoryPurchaseFragment newInstance() {
        Bundle args = new Bundle();
        HistoryPurchaseFragment fragment = new HistoryPurchaseFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void receivedCalback(Integer line_id) {
        progressDialog.setMessage("Saving Data........");
        progressDialog.show();
        progressDialog.setCancelable(false);
        System.out.println(line_id);
        OdooValues values = new OdooValues();
        values.put("is_received", true);

        client.write("sale.order.line", new Integer[]{line_id}, values, new IOdooResponse() {
            @Override
            public void onResult(OdooResult result) {
                // Success response
                if (result.getBoolean("result") ==  true){
                    loadPurchase();
                    progressDialog.dismiss();
                }
            }

            @Override
            public boolean onError(OdooErrorException error) {
                Toast.makeText(context, String.valueOf(error.getLocalizedMessage()), Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
                return super.onError(error);
            }
        });
    }

    @Override
    public void sendCallback(Integer line_id) {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        if (rootView==null) {
            rootView = inflater.inflate(R.layout.fragment_history_purchase, container, false);
            context = getContext();
            sharedPrefManager = new SharedPrefManager(context);
            progressDialog = new ProgressDialog(context);
            adapter = new AdapterHistory(ArrayListHistory,HistoryPurchaseFragment.this);
            rv = rootView.findViewById(R.id.rv_recycler_history_purchase);
            rv.setAdapter(adapter);
            rv.setLayoutManager(new LinearLayoutManager(context));
            swiper = rootView.findViewById(R.id.swiperefresh_history_purchase);
            client = getOdooConnection1(getContext(), new OdooErrorListener() {
                @Override
                public void onError(OdooErrorException error) {
                    swiper.setRefreshing(false);
                }
            });
            swiper.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {
                    loadPurchase();
                }
            });
            loadPurchase();
        }
        return rootView;
    }

    public void loadPurchase(){
        swiper.setRefreshing(true);
        OArguments arguments = new OArguments();
        arguments.add(Integer.valueOf(sharedPrefManager.getSpIdPartner()));

        client.call_kw("sale.order.line", "get_purchase_history", arguments, new IOdooResponse() {
            @Override
            public void onResult(OdooResult result) {
                ArrayListHistory.clear();
                // response
                OdooRecord[] Records = result.getRecords();
                for (final OdooRecord record : Records) {
                    ArrayListHistory.add(new History(
                            String.valueOf(record.getInt("id")),
                            record.getString("order_name"),
                            record.getString("name"),
                            record.getString("owner"),
                            "Purchase",
                            String.valueOf(record.getInt("qty")),
                            String.valueOf(Math.round(record.getFloat("harga"))),
                            record.getString("image"),
                            record.getString("date"),
                            record.getBoolean("is_send"),
                            record.getBoolean("is_received")
                    ));
                }
                adapter.notifyDataSetChanged();
                swiper.setRefreshing(false);
            }
        });
    }

}
