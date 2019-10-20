package com.alpha.test.persebayaapp;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import oogbox.api.odoo.OdooClient;
import oogbox.api.odoo.client.helper.OdooErrorException;
import oogbox.api.odoo.client.helper.data.OdooRecord;
import oogbox.api.odoo.client.helper.data.OdooResult;
import oogbox.api.odoo.client.helper.utils.OArguments;
import oogbox.api.odoo.client.listeners.IOdooResponse;
import oogbox.api.odoo.client.listeners.OdooErrorListener;

import static com.alpha.test.persebayaapp.CommonUtils.getOdooConnection1;


/**
 * A simple {@link Fragment} subclass.
 */
public class HistoryPurchaseFragment extends Fragment {


    public HistoryPurchaseFragment() {
        // Required empty public constructor
    }

    public static HistoryPurchaseFragment newInstance() {
        Bundle args = new Bundle();
        HistoryPurchaseFragment fragment = new HistoryPurchaseFragment();
        fragment.setArguments(args);
        return fragment;
    }
    ArrayList<History> ArrayListHistory = new ArrayList<>();
    private View rootView;
    RecyclerView rv;
    SharedPrefManager sharedPrefManager;
    OdooClient client;
    SwipeRefreshLayout swiper;
    Context context;
    AdapterHistory adapter;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        if (rootView==null) {
            rootView = inflater.inflate(R.layout.fragment_history_purchase, container, false);
            context = getContext();
            sharedPrefManager = new SharedPrefManager(context);
            adapter = new AdapterHistory(ArrayListHistory);
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
                            record.getString("date")
                    ));
                }
                adapter.notifyDataSetChanged();
                swiper.setRefreshing(false);
            }
        });
    }

}
