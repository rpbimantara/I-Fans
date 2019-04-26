package com.alpha.test.i_fans;


import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;

import oogbox.api.odoo.OdooClient;
import oogbox.api.odoo.client.OdooVersion;
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
public class TerupdateFragment extends Fragment {

    ArrayList<Terupdate> ArrayListTerupdate;
    int RecyclerViewItemPosition;
    SharedPrefManager sharedPrefManager;
    ProgressDialog progressDialog;
    RecyclerView rv;
    private View rootView;
    RecyclerView.LayoutManager llm;
    AdapterTerupdate adapter;
    SwipeRefreshLayout swiper;
    TextView tglnow, stadionnow, tgllast, tglnext, teamHome, teamAway, teamNext, stadionNext, skornow, homelast, awaylast;
    ImageView homeImage, awayImage, nextImage, nextStatus, homeImageLast, awayImageLast;
    LinearLayout lnNow;
    RelativeLayout rlLast, rlNext;
    int id_jadwal_now = 0;
    int id_jadwal_last = 0;
    int id_jadwal_next = 0;
    OdooClient client;
    Spinner ligaSpiner;

    public TerupdateFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public static TerupdateFragment newInstance() {
        Bundle args = new Bundle();
        TerupdateFragment fragment = new TerupdateFragment();
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        if (rootView == null){
            rootView = inflater.inflate(R.layout.fragment_terupdate, container, false);
            rv = rootView.findViewById(R.id.rv_recycler_view_hot_news);
            lnNow = rootView.findViewById(R.id.linearLayout_now);
            rlLast = rootView.findViewById(R.id.RL_last);
            rlNext = rootView.findViewById(R.id.RL_next);
//            liganow = rootView.findViewById(R.id.textView_namaligaterupdate);
            ligaSpiner = rootView.findViewById(R.id.ligaterupdate_spinner);
            tglnow = rootView.findViewById(R.id.textView_tglharini);
            stadionnow = rootView.findViewById(R.id.textView_stadionharini);
            skornow = rootView.findViewById(R.id.txt_scoreterupdate);
            homelast = rootView.findViewById(R.id.txt_skorhomelast);
            awaylast = rootView.findViewById(R.id.txt_skorawaylast);
            homeImageLast = rootView.findViewById(R.id.imageView_homelast);
            awayImageLast = rootView.findViewById(R.id.imageView_awaylast);
            tgllast = rootView.findViewById(R.id.textView_lastliga);
            tglnext = rootView.findViewById(R.id.textView_nextliga);
            homeImage = rootView.findViewById(R.id.home_image);
            teamHome = rootView.findViewById(R.id.txt_namateamHome);
            awayImage = rootView.findViewById(R.id.away_image);
            teamAway = rootView.findViewById(R.id.txt_namateamAway);
            teamNext = rootView.findViewById(R.id.txt_namateamnext);
            stadionNext = rootView.findViewById(R.id.txt_namastadionnext);
            nextImage = rootView.findViewById(R.id.imageView_teamnext);
            nextStatus = rootView.findViewById(R.id.imageView_status_jadwal);
            swiper = rootView.findViewById(R.id.swiperefresh_terupdate);
            swiper.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {
                    new TerupdateTask().execute();
                    new MatchTask().execute();
                }
            });
            sharedPrefManager = new SharedPrefManager(getActivity());
            progressDialog = new ProgressDialog(getActivity());
            progressDialog.setMessage("Loading data....");
            progressDialog.show();
            llm = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
            rv.setAdapter(new AdapterTerupdate(ArrayListTerupdate));
            rv.setLayoutManager(llm);
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
                        try {
                            RecyclerViewItemPosition = rv.getChildAdapterPosition(ChildView);
                            Intent intent = new Intent(getActivity(), BeritaDetailActivity.class);
                            intent.putExtra("id", ArrayListTerupdate.get(RecyclerViewItemPosition).getId());
                            startActivity(intent);
                        } catch (Exception err) {
                            System.out.println(err);
                        }

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
            lnNow.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(getActivity(), MatchDetailActivity.class);
                    intent.putExtra("id_jadwal", id_jadwal_now);
                    startActivity(intent);
                }
            });
            rlNext.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(getActivity(), MatchDetailActivity.class);
                    intent.putExtra("id_jadwal", id_jadwal_next);
                    startActivity(intent);
                }
            });

            rlLast.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(getActivity(), MatchDetailActivity.class);
                    intent.putExtra("id_jadwal", id_jadwal_last);
                    startActivity(intent);
                }
            });

            new TerupdateTask().execute();
            new MatchTask().execute();
        }
        return rootView;
    }

    public String tanggal(String tgl) {
        try {
            tgl = new SimpleDateFormat("dd MMM yyyy", Locale.US).format(new SimpleDateFormat("yyyy-MM-dd").parse(tgl));
        } catch (Exception ex) {
            System.out.println("Error Convert Tanggal: " + ex);
        }

        return tgl;
    }

    public Bitmap StringToBitMap(String encodedString) {
        try {
            byte[] encodeByte = Base64.decode(encodedString, Base64.DEFAULT);
            Bitmap bitmap = BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
            return bitmap;
        } catch (Exception e) {
            e.getMessage();
            return null;
        }
    }

    public String nullChecker(String param) {
        return ((param == "null") || (param == "false") ? "0" : param);
    }

    public class MatchTask extends AsyncTask<Void, Void, String> {
        @Override
        protected void onPostExecute(String aVoid) {
            super.onPostExecute(aVoid);
        }

        @Override
        protected String doInBackground(Void... voids) {
            client = new OdooClient.Builder(getContext())
                    .setHost(sharedPrefManager.getSP_Host_url())
                    .setSession(sharedPrefManager.getSpSessionId())
                    .setSynchronizedRequests(false)
                    .setConnectListener(new OdooConnectListener() {
                        @Override
                        public void onConnected(OdooVersion version) {
                            // Success connection

                            OArguments arguments = new OArguments();
                            arguments.addNULL();

                            client.call_kw("persebaya.jadwal", "jadwal_terkini", arguments, new IOdooResponse() {
                                @Override
                                public void onResult(OdooResult result) {
                                    // response
                                    OdooRecord[] records = result.getRecords();
                                    for (OdooRecord record : records) {
                                        Log.w("asdasdads",record.getString("id"));
                                    }
                                }
                            });
                        }
                    })
                    .build();
            return null;
        }
    }

    public class TerupdateTask extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPostExecute(Void aVoid) {
            swiper.setRefreshing(false);
            super.onPostExecute(aVoid);
        }

        @Override
        protected Void doInBackground(Void... voids) {
            ArrayListTerupdate = new ArrayList<>();
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
                                        ArrayListTerupdate.add(new Terupdate(
                                                record.getInt("id"),
                                                record.getString("image"),
                                                record.getString("kategori_brita_id"),
                                                record.getString("headline")));
                                    }
                                    adapter = new AdapterTerupdate(ArrayListTerupdate);
                                    rv.setAdapter(adapter);
                                    adapter.notifyDataSetChanged();
                                    progressDialog.dismiss();
                                }
                            });
                        }
                    })
                    .build();

            return null;
        }
    }

}
