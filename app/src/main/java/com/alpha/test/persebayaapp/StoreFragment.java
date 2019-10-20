package com.alpha.test.persebayaapp;


import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
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

import static com.alpha.test.persebayaapp.CommonUtils.getOdooConnection1;


/**
 * A simple {@link Fragment} subclass.
 */
public class StoreFragment extends Fragment implements HomeActivity.StoreReloadCalled {

    ArrayList<Store> ArrayListStore = new ArrayList<>();
    int RecyclerViewItemPosition ;
    SharedPrefManager sharedPrefManager;
    View rootView;
    RecyclerView rv;
    ProgressDialog progressDialog;
    AdapterStore adapter;
    SwipeRefreshLayout swiper;
    OdooClient client;
    EditText editText_Search;


    public StoreFragment() {
        // Required empty public constructor
    }


    public static StoreFragment newInstance() {
        Bundle args = new Bundle();
        StoreFragment fragment = new StoreFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_store,container,false);
        rv =  rootView.findViewById(R.id.rv_recycler_view_store);
        swiper = rootView.findViewById(R.id.swiperefresh_store);
        editText_Search = rootView.findViewById(R.id.editText_Search);
        sharedPrefManager = new SharedPrefManager(getActivity());
        progressDialog = new ProgressDialog(getActivity());
        adapter = new AdapterStore(ArrayListStore);
        rv.setAdapter(adapter);
        rv.setLayoutManager(new GridLayoutManager(getActivity(),3));
        client = getOdooConnection1(getContext(), new OdooErrorListener() {
            @Override
            public void onError(OdooErrorException error) {
                swiper.setRefreshing(false);
            }
        });
        swiper.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadStore();
            }
        });
        editText_Search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                adapter.getFilter().filter(editable.toString());
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
                    Integer requestCode = 1;
                    startActivityForResult(intent,requestCode);
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
        loadStore();
        return rootView;
    }

    public void loadStore(){
        swiper.setRefreshing(true);
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
                ArrayListStore.clear();
                for (OdooRecord record : records) {
                    String code = " ";
                    if (record.getString("default_code").equalsIgnoreCase("false") || record.getString("default_code").equalsIgnoreCase("")){
                        code = "";
                    }else{
                        code = "["+record.getString("default_code") +"] ";
                    }

                    ArrayListStore.add(new Store(
                            String.valueOf(record.getInt("id")),
                            record.getString("image_medium"),
                            code +record.getString("name"),
                            String.valueOf(Math.round(record.getFloat("list_price")))));
                }
                adapter.notifyDataSetChanged();
                swiper.setRefreshing(false);
            }

        });
    }

    @Override
    public void onReloadCalled() {
        loadStore();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        ((HomeActivity)getActivity()).setReloadCallback(this);
    }
}
