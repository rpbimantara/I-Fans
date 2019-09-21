package com.alpha.test.i_fans;


import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;

import oogbox.api.odoo.OdooClient;
import oogbox.api.odoo.client.OdooVersion;
import oogbox.api.odoo.client.helper.data.OdooRecord;
import oogbox.api.odoo.client.helper.data.OdooResult;
import oogbox.api.odoo.client.helper.utils.ODomain;
import oogbox.api.odoo.client.helper.utils.OdooFields;
import oogbox.api.odoo.client.listeners.IOdooResponse;
import oogbox.api.odoo.client.listeners.OdooConnectListener;

import static com.alpha.test.i_fans.CommonUtils.tanggal;
import static com.alpha.test.i_fans.CommonUtils.waktu;


/**
 * A simple {@link Fragment} subclass.
 */
public class ListBeritaFragment extends Fragment {

    ArrayList<ListBerita> ArrayListBerita;
    int RecyclerViewItemPosition ;
    SharedPrefManager sharedPrefManager;
    ProgressDialog progressDialog;
    RecyclerView rv;
    View rootView;
    RecyclerView.LayoutManager llm;
    SwipeRefreshLayout swiper;
    AdapterListBerita adapter;
    OdooClient client;

    public ListBeritaFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public static ListBeritaFragment newInstance() {
        Bundle args = new Bundle();
        ListBeritaFragment fragment = new ListBeritaFragment();
        fragment.setArguments(args);
        return fragment;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        if (rootView == null) {
            rootView = inflater.inflate(R.layout.fragment_list_berita, container, false);
            rv = rootView.findViewById(R.id.rv_recycler_view_list_berita);
            swiper = rootView.findViewById(R.id.swiperefresh_list_berita);
            llm = new LinearLayoutManager(getActivity());
            adapter = new AdapterListBerita(ArrayListBerita);
            rv.setAdapter(adapter);
            rv.setLayoutManager(llm);
            swiper.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {
                    new BeritaTask().execute();
                }
            });
            sharedPrefManager = new SharedPrefManager(getActivity());
            progressDialog = new ProgressDialog(getActivity());

            rv.addOnItemTouchListener(new RecyclerView.OnItemTouchListener() {

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
                        Intent intent = new Intent(getActivity(), BeritaDetailActivity.class);
                        intent.putExtra("id", ArrayListBerita.get(RecyclerViewItemPosition).getId());
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
            new BeritaTask().execute();
        }
        return rootView;
    }

    public class BeritaTask extends AsyncTask<Void,Void,Void>{

        @Override
        protected void onPreExecute() {
            swiper.setRefreshing(true);
        }

        @Override
        protected Void doInBackground(Void... voids) {
            ArrayListBerita = new ArrayList<>();
            client = new OdooClient.Builder(getContext())
                    .setHost(sharedPrefManager.getSP_Host_url())
                    .setSession(sharedPrefManager.getSpSessionId())
                    .setSynchronizedRequests(false)
                    .setConnectListener(new OdooConnectListener() {
                        @Override
                        public void onConnected(OdooVersion version) {
                            // Success connection

                            ODomain domain = new ODomain();
                            domain.add("create_uid", "=", 1);

                            OdooFields fields = new OdooFields();
                            fields.addAll("id", "image", "title", "headline", "content", "kategori_brita_id", "create_date", "create_uid", "write_date", "write_uid");

                            int offset = 0;
                            int limit = 5;

                            String sorting = "create_date DESC";

                            client.searchRead("persebaya.berita", domain, fields, offset, limit, sorting, new IOdooResponse() {
                                @Override
                                public void onResult(OdooResult result) {
                                    OdooRecord[] records = result.getRecords();
                                    for (OdooRecord record : records) {
                                        String tgl = tanggal(record.getString("create_date").substring(0,10));
                                        String waktu = waktu(record.getString("create_date").substring(11,17)) + " "+ "WIB";
                                        ArrayListBerita.add(new ListBerita(
                                                record.getInt("id"),
                                                record.getString("image"),
                                                record.getString("kategori_brita_id"),
                                                record.getString("headline"),
                                                tgl.concat(" ").concat(waktu)));
                                    }
                                    adapter = new AdapterListBerita(ArrayListBerita);
                                    rv.setAdapter(adapter);
                                    adapter.notifyDataSetChanged();
                                    swiper.setRefreshing(false);
                                }
                            });
                        }
                    })
                    .build();
            return null;
        }
    }

}
