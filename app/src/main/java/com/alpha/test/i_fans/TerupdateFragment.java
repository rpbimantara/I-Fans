package com.alpha.test.i_fans;


import android.app.ProgressDialog;
import android.content.Context;
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
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.jaredrummler.materialspinner.MaterialSpinner;

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

import static com.alpha.test.i_fans.CommonUtils.StringToBitMap;
import static com.alpha.test.i_fans.CommonUtils.tanggal;


/**
 * A simple {@link Fragment} subclass.
 */
public class TerupdateFragment extends Fragment {

    ArrayList<Terupdate> ArrayListTerupdate;
//    ArrayList<Liga> ArrayListLiga;
    int RecyclerViewItemPosition;
    SharedPrefManager sharedPrefManager;
    ProgressDialog progressDialog;
    RecyclerView rv;
    private View rootView;
    RecyclerView.LayoutManager llm;
    AdapterTerupdate adapter;
//    AdapterLiga adapterLiga;
    SwipeRefreshLayout swiper;
    TextView tglnow, stadionnow, tgllast, tglnext, teamHome, teamAway, teamNext, stadionNext, skornow, homelast, awaylast,skorlast,liga_terupdate;
    ImageView homeImage, awayImage, nextImage, nextStatus, homeImageLast, awayImageLast;
//    MaterialSpinner ligaSpinner;
    LinearLayout lnNow;
    RelativeLayout rlLast, rlNext;
    int id_jadwal_now = 0;
    int id_jadwal_last = 0;
//    int id_jadwal_next = 0;
    OdooClient client;
    Context context;
//    Spinner ligaSpiner;

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
            context = getContext();
            rootView = inflater.inflate(R.layout.fragment_terupdate, container, false);
            rv = rootView.findViewById(R.id.rv_recycler_view_hot_news);
//            ligaSpinner = rootView.findViewById(R.id.ligaterupdate_spinner);
            liga_terupdate = rootView.findViewById(R.id.textView_ligaterupdate);
            lnNow = rootView.findViewById(R.id.linearLayout_now);
            rlLast = rootView.findViewById(R.id.RL_last);
//            rlNext = rootView.findViewById(R.id.RL_next);
            tglnow = rootView.findViewById(R.id.textView_tglharini);
            stadionnow = rootView.findViewById(R.id.textView_stadionharini);
            skornow = rootView.findViewById(R.id.txt_scoreterupdate);
            homelast = rootView.findViewById(R.id.txt_namehomelast);
            awaylast = rootView.findViewById(R.id.txt_nameawaylast);
            homeImageLast = rootView.findViewById(R.id.imageView_homelast);
            awayImageLast = rootView.findViewById(R.id.imageView_awaylast);
            skorlast = rootView.findViewById(R.id.txt_scorelast);
            tgllast = rootView.findViewById(R.id.textView_lastliga);
//            tglnext = rootView.findViewById(R.id.textView_nextliga);
            homeImage = rootView.findViewById(R.id.home_image);
            teamHome = rootView.findViewById(R.id.txt_namateamHome);
            awayImage = rootView.findViewById(R.id.away_image);
            teamAway = rootView.findViewById(R.id.txt_namateamAway);
//            teamNext = rootView.findViewById(R.id.txt_namateamnext);
//            stadionNext = rootView.findViewById(R.id.txt_namastadionnext);
//            nextImage = rootView.findViewById(R.id.imageView_teamnext);
//            nextStatus = rootView.findViewById(R.id.imageView_status_jadwal);
            swiper = rootView.findViewById(R.id.swiperefresh_terupdate);
            swiper.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {
                    new TerupdateTask().execute();
                    getData();
                }
            });
            sharedPrefManager = new SharedPrefManager(getActivity());
            progressDialog = new ProgressDialog(getActivity());
            progressDialog.setMessage("Loading data....");
            progressDialog.show();
            progressDialog.setCancelable(false);
            llm = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
            rv.setAdapter(adapter);
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
//            rlNext.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    if (id_jadwal_next > 0) {
//                        Intent intent = new Intent(getActivity(), MatchDetailActivity.class);
//                        intent.putExtra("id_jadwal", id_jadwal_next);
//                        startActivity(intent);
//                    }else{
//                        Toast.makeText(context, "No Match Data Found!", Toast.LENGTH_SHORT).show();
//                    }
//                }
//            });

            rlLast.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(getActivity(), MatchDetailActivity.class);
                    intent.putExtra("id_jadwal", id_jadwal_last);
                    startActivity(intent);
                }
            });
//            ligaSpinner.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener() {
//                @Override
//                public void onItemSelected(MaterialSpinner view, int position, long id, Object item) {
//                    Liga liga = adapterLiga.getItem(position);
//                    sharedPrefManager.saveSPInt(SharedPrefManager.SP_ID_Liga,liga.getId());
//                    getData();
//                }
//            });
//            new LigaTask().execute();
            new TerupdateTask().execute();
            getData();
        }
        return rootView;
    }

    public void getData(){
        client = new OdooClient.Builder(getContext())
                .setHost(sharedPrefManager.getSP_Host_url())
                .setSession(sharedPrefManager.getSpSessionId())
                .setSynchronizedRequests(false)
                .setConnectListener(new OdooConnectListener() {
                    @Override
                    public void onConnected(OdooVersion version) {
                        // Success connection

                        OArguments arguments = new OArguments();
                        arguments.add(sharedPrefManager.getSpIdClub());
                        arguments.add(sharedPrefManager.getSPIdLiga());

                        client.call_kw("persebaya.jadwal", "jadwal_terkini", arguments, new IOdooResponse() {
                            @Override
                            public void onResult(OdooResult result) {
                                // response
                                OdooRecord[] records = result.getRecords();
                                    for (OdooRecord record : records) {
                                        homeImage.setImageBitmap(StringToBitMap(record.getString("image_home")));
                                        awayImage.setImageBitmap(StringToBitMap(record.getString("image_away")));
                                        teamHome.setText(record.getString("home"));
                                        teamAway.setText(record.getString("away"));
                                        id_jadwal_now = record.getInt("id");
                                        tglnow.setText(tanggal(record.getString("date")));
                                        stadionnow.setText(record.getString("stadion"));
                                        skornow.setText(record.getString("skornow") );
                                        liga_terupdate.setText(record.getString("liga"));

                                        homeImageLast.setImageBitmap(StringToBitMap(record.getString("image_home_last")));
                                        homelast.setText(record.getString("home_last"));
                                        awayImageLast.setImageBitmap(StringToBitMap(record.getString("image_away_last")));
                                        awaylast.setText(record.getString("away_last"));
                                        id_jadwal_last = record.getInt("id_last");
                                        skorlast.setText(record.getString("ft_home_last") +" - " + record.getString("ft_away_last"));
                                        tgllast.setText(tanggal(record.getString("date_last")));

//                                        if (record.getString("id_next") != null) {
//                                            id_jadwal_next = record.getInt("id_next");
//                                            stadionNext.setText(record.getString("stadion_next"));
//                                            tglnext.setText(tanggal(record.getString("date_next")));
//                                            if (record.getString("home_next").equalsIgnoreCase(sharedPrefManager.getSpNamaClub())) {
//                                                nextImage.setImageBitmap(StringToBitMap(record.getString("image_away_next")));
//                                                teamNext.setText(record.getString("away_next"));
//                                                nextStatus.setImageResource(getContext().getResources().getIdentifier("ic_home", "drawable", getContext().getPackageName()));
//                                            } else {
//                                                nextImage.setImageBitmap(StringToBitMap(record.getString("image_home_next")));
//                                                teamNext.setText(record.getString("home_next"));
//                                                nextStatus.setImageResource(getContext().getResources().getIdentifier("ic_away", "drawable", getContext().getPackageName()));
//                                            }
//                                        }
                                    }
                            }
                        });
                    }

                })
                .build();
    }

//    public class LigaTask extends AsyncTask<Void, Void, Void>{
//        @Override
//        protected Void doInBackground(Void... voids) {
//            ArrayListLiga = new ArrayList<>();
//            client = new OdooClient.Builder(getContext())
//                    .setHost(sharedPrefManager.getSP_Host_url())
//                    .setSession(sharedPrefManager.getSpSessionId())
//                    .setSynchronizedRequests(false)
//                    .setConnectListener(new OdooConnectListener() {
//                        @Override
//                        public void onConnected(OdooVersion version) {
//                            ODomain domain = new ODomain();
//                            domain.add("status_liga", "=", "valid");
//
//                            OdooFields fields = new OdooFields();
//                            fields.addAll("id", "nama", "create_date", "create_uid", "write_date", "write_uid");
//
//                            int offset = 0;
//                            int limit = 0;
//
//                            String sorting = "id DESC";
//
//                            client.searchRead("persebaya.liga", domain, fields, offset, limit, sorting, new IOdooResponse() {
//                                @Override
//                                public void onResult(OdooResult result) {
//                                    OdooRecord[] records = result.getRecords();
//                                    for (OdooRecord record : records) {
//                                        ArrayListLiga.add(new Liga(
//                                                record.getInt("id"),
//                                                record.getString("nama")));
//
//                                        adapterLiga = new AdapterLiga(context,android.R.layout.simple_spinner_item,ArrayListLiga);
//                                        ligaSpinner.setAdapter(adapterLiga);
//                                        adapterLiga.notifyDataSetChanged();
//                                    }
//                                }
//                            });
//                        }
//                    }).build();
//            return null;
//        }
//    }

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
