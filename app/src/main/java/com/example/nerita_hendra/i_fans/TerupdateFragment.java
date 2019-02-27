package com.example.nerita_hendra.i_fans;


import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
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
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;


/**
 * A simple {@link Fragment} subclass.
 */
public class TerupdateFragment extends Fragment {

    ArrayList<Terupdate> ArrayListTerupdate;
    ArrayList<Jadwal> ArrayListJadwal;
    int RecyclerViewItemPosition ;
    SharedPrefManager sharedPrefManager;
    ProgressDialog progressDialog;
    RecyclerView rv;
    View rootView;
    RecyclerView.LayoutManager llm;
    AdapterTerupdate adapter;
    SwipeRefreshLayout swiper;
    TextView liganow,tglnow,stadionnow,tgllast,tglnext,teamHome,teamAway,teamNext,stadionNext,skornow,homelast,awaylast;
    ImageView homeImage,awayImage,nextImage,nextStatus,homeImageLast,awayImageLast;
    LinearLayout lnNow;
    RelativeLayout rlLast,rlNext;
    int id_jadwal_now = 0;
    int id_jadwal_last = 0;
    int id_jadwal_next = 0;

    public TerupdateFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_terupdate, container, false);
        rv = rootView.findViewById(R.id.rv_recycler_view_hot_news);
        lnNow = rootView.findViewById(R.id.linearLayout_now);
        rlLast = rootView.findViewById(R.id.RL_last);
        rlNext = rootView.findViewById(R.id.RL_next);
        liganow = rootView.findViewById(R.id.textView_namaligaterupdate);
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
        llm = new  LinearLayoutManager(getActivity(),LinearLayoutManager.HORIZONTAL,false);
        rv.setAdapter(new  AdapterTerupdate(ArrayListTerupdate));
        rv.setLayoutManager(llm);
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
                    try {
                        RecyclerViewItemPosition = rv.getChildAdapterPosition(ChildView);
                        Intent intent = new Intent(getActivity(),BeritaDetailActivity.class);

                        intent.putExtra("id",ArrayListTerupdate.get(RecyclerViewItemPosition).getId());
                        intent.putExtra("title",ArrayListTerupdate.get(RecyclerViewItemPosition).getTitle());
                        intent.putExtra("kategori",ArrayListTerupdate.get(RecyclerViewItemPosition).getKategori());
                        intent.putExtra("headline",ArrayListTerupdate.get(RecyclerViewItemPosition).getHeadline());
                        intent.putExtra("konten",ArrayListTerupdate.get(RecyclerViewItemPosition).getKonten());
                        intent.putExtra("tanggalbuat",ArrayListTerupdate.get(RecyclerViewItemPosition).getTanggal());
                        intent.putExtra("penulis",ArrayListTerupdate.get(RecyclerViewItemPosition).getPenulis());
                        sharedPrefManager.saveSPString(SharedPrefManager.SP_IMAGE_NEWS,ArrayListTerupdate.get(RecyclerViewItemPosition).getImageTerupdate());
                        startActivity(intent);
                    }catch (Exception err){
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
                Intent intent = new Intent(getActivity(),MatchDetailActivity.class);
                intent.putExtra("id_jadwal",id_jadwal_now);
                startActivity(intent);
            }
        });
        rlNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(),MatchDetailActivity.class);
                intent.putExtra("id_jadwal",id_jadwal_next);
                startActivity(intent);
            }
        });

        rlLast.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(),MatchDetailActivity.class);
                intent.putExtra("id_jadwal",id_jadwal_last);
                startActivity(intent);
            }
        });
        new TerupdateTask().execute();
        new MatchTask().execute();
        return rootView;
    }

    public String tanggal(String tgl){
        try {
            tgl = new SimpleDateFormat("dd MMM yyyy", Locale.US).format(new SimpleDateFormat("yyyy-MM-dd").parse(tgl));
        }catch (Exception ex){
            System.out.println("Error Convert Tanggal: " + ex);
        }

        return tgl;
    }

    public  String waktu(String waktu){
        int output = Integer.valueOf(waktu.substring(0,1));
        waktu = String.valueOf(output) + waktu.substring(1,4);
        return waktu;
    }

    public Bitmap StringToBitMap(String encodedString){
        try{
            byte [] encodeByte= Base64.decode(encodedString,Base64.DEFAULT);
            Bitmap bitmap= BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
            return bitmap;
        }catch(Exception e){
            e.getMessage();
            return null;
        }
    }
    public String nullChecker(String param){
        return ((param == "null") || (param == "false") ? "0" : param);
    }

    public class SkorTask extends AsyncTask<Integer,Void,String>{
        @Override
        protected void onPostExecute(String s) {
            skornow.setText(s);
            swiper.setRefreshing(false);
            super.onPostExecute(s);
        }

        @Override
        protected String doInBackground(Integer... integers) {
            OdooConnect oc = OdooConnect.connect(sharedPrefManager.getSpNamaUser(),sharedPrefManager.getSpPasswordUser());
            Object[] param = {new Object[]{
                    new Object[]{"id", "=", integers}}};

            List<HashMap<String, Object>> dataJadwal = oc.search_read("persebaya.jadwal", param, "id","ft_home", "ft_away");

            String result = nullChecker(dataJadwal.get(0).get("ft_home").toString()) +" - " + nullChecker(dataJadwal.get(0).get("ft_away").toString());
            return result;
        }
    }

    public class LastMatchTask extends AsyncTask<Integer,Void,String[]>{
        @Override
        protected void onPostExecute(String[] s) {
            homelast.setText(s[0]);
            awaylast.setText(s[1]);
        }

        @Override
        protected String[] doInBackground(Integer... integers) {
            OdooConnect oc = OdooConnect.connect(sharedPrefManager.getSpNamaUser(),sharedPrefManager.getSpPasswordUser());
            Object[] param = {new Object[]{
                    new Object[]{"id", "=", integers}}};

            List<HashMap<String, Object>> dataJadwal = oc.search_read("persebaya.jadwal", param, "id","ft_home", "ft_away");

//            Object[] paramclub = {new Object[]{
//                    new Object[]{"nama", "=", dataJadwal.get(i).get("away")}}};
//
//            List<HashMap<String, Object>> dataclub = oc.search_read("persebaya.club", paramclub, "foto_club");

//            String result = nullChecker(dataJadwal.get(0).get("ft_home").toString()) +" - " + nullChecker(dataJadwal.get(0).get("ft_away").toString());
            return new String[]{dataJadwal.get(0).get("ft_home").toString(),dataJadwal.get(0).get("ft_away").toString()};
        }
    }

    public class MatchTask extends AsyncTask<Void,Void,String>{
        @Override
        protected void onPostExecute(String aVoid) {
                try {
                    SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy", Locale.US);
                    int counter = 0;
                    for (int j = 0; j < ArrayListJadwal.size(); ++j) {
                        Date dateMatch = sdf.parse(ArrayListJadwal.get(j).getTglmain());
                        Date dateNow = new Date();
                        if (dateMatch.before(dateNow)){
                            tglnow.setText(ArrayListJadwal.get(j).getTglmain());
                            liganow.setText(ArrayListJadwal.get(j).getNamaliga());
                            stadionnow.setText(ArrayListJadwal.get(j).getNamastadion());
                            if (ArrayListJadwal.get(j).getStatusimage() == getContext().getResources().getIdentifier("ic_home","drawable",getContext().getPackageName())){
                                homeImage.setImageBitmap(StringToBitMap(aVoid));
                                teamHome.setText(sharedPrefManager.getSpNamaClub());
                                awayImage.setImageBitmap(StringToBitMap(ArrayListJadwal.get(j).getFototeam()));
                                teamAway.setText(ArrayListJadwal.get(j).getNamateam());
                            }else {
                                homeImage.setImageBitmap(StringToBitMap(ArrayListJadwal.get(j).getFototeam()));
                                teamHome.setText(ArrayListJadwal.get(j).getNamateam());
                                awayImage.setImageBitmap(StringToBitMap(aVoid));
                                teamAway.setText(sharedPrefManager.getSpNamaClub());
                            }
                            counter = j;
                        }
                        if (dateMatch.equals(dateNow)){
                            tglnow.setText(ArrayListJadwal.get(j).getTglmain());
                            counter = j;
                        }
                    }
                    Log.e("Terupdate datematch : ", String.valueOf(counter));
                    new SkorTask().execute(Integer.valueOf(ArrayListJadwal.get(counter).getJadwal_id()));
                    id_jadwal_now = Integer.valueOf(ArrayListJadwal.get(counter).getJadwal_id());
                    if (counter == 0){
                        tgllast.setText(ArrayListJadwal.get(counter).getTglmain());
//                        new LastMatchTask().execute(Integer.valueOf(counter));
                    }else{
                        tgllast.setText(ArrayListJadwal.get(counter-1).getTglmain());
//                        new LastMatchTask().execute(Integer.valueOf(counter-1));
                    }
//                    homeImageLast.setImageBitmap(StringToBitMap(ArrayListJadwal.get(counter-1).getFototeam()));
//                    awayImageLast.setImageBitmap(StringToBitMap(ArrayListJadwal.get(counter-1).getFototeam()));
//                    homelast.setText("0");
//                    awaylast.setText("0");
                    if (counter < ArrayListJadwal.size()-1){
                        tglnext.setText(ArrayListJadwal.get(counter+1).getTglmain());
                        teamNext.setText(ArrayListJadwal.get(counter+1).getNamateam());
                        stadionNext.setText(ArrayListJadwal.get(counter+1).getNamastadion());
                        nextImage.setImageBitmap(StringToBitMap(ArrayListJadwal.get(counter+1).getFototeam()));
                        nextStatus.setImageResource(ArrayListJadwal.get(counter+1).getStatusimage());
                        id_jadwal_next = Integer.valueOf(ArrayListJadwal.get(counter+1).getJadwal_id());
                    }else{
                        tglnext.setText(ArrayListJadwal.get(counter).getTglmain());
                        teamNext.setText(ArrayListJadwal.get(counter).getNamateam());
                        stadionNext.setText(ArrayListJadwal.get(counter).getNamastadion());
                        nextImage.setImageBitmap(StringToBitMap(ArrayListJadwal.get(counter).getFototeam()));
                        nextStatus.setImageResource(ArrayListJadwal.get(counter).getStatusimage());
                        id_jadwal_next = Integer.valueOf(ArrayListJadwal.get(counter).getJadwal_id());
                    }

                } catch (ParseException e) {
                    e.printStackTrace();
                }
            super.onPostExecute(aVoid);
        }

        @Override
        protected String doInBackground(Void... voids) {
            ArrayListJadwal = new ArrayList<>();
            String fotoclub="";
            try {
                OdooConnect oc = OdooConnect.connect(sharedPrefManager.getSpNamaUser(),sharedPrefManager.getSpPasswordUser());
                Object[] paramclubactive = {new Object[]{
                        new Object[]{"nama", "=", sharedPrefManager.getSpNamaClub()}}};

                List<HashMap<String, Object>> clubactive = oc.search_read("persebaya.club", paramclubactive, "foto_club");
                fotoclub = clubactive.get(0).get("foto_club").toString();

                Object[] param = {new Object[]{
                        new Object[]{"durasi", "=", 90}}};

                List<HashMap<String, Object>> dataJadwal = oc.search_read("persebaya.jadwal", param, "id","liga_id", "tgl_main","home","away","stadion_id","status_jadwal");

                for (int i = 0; i < dataJadwal.size(); ++i) {
                    String tgl = tanggal(dataJadwal.get(i).get("tgl_main").toString().substring(0,10));
                    String waktu = waktu(dataJadwal.get(i).get("tgl_main").toString().substring(11,17)) + " "+ "WIB";
                    if (dataJadwal.get(i).get("home").toString().equalsIgnoreCase(sharedPrefManager.getSpNamaClub())){
                        Object[] paramclub = {new Object[]{
                                new Object[]{"nama", "=", dataJadwal.get(i).get("away")}}};

                        List<HashMap<String, Object>> dataclub = oc.search_read("persebaya.club", paramclub, "foto_club");
                        for (int c = 0; c < dataclub.size(); ++c) {
                            ArrayListJadwal.add(new Jadwal(
                                    dataJadwal.get(i).get("away").toString(),
                                    String.valueOf(dataclub.get(c).get("foto_club")),
                                    getContext().getResources().getIdentifier("ic_home","drawable",getContext().getPackageName()),
                                    dataJadwal.get(i).get("liga_id").toString(),
                                    tgl,
                                    dataJadwal.get(i).get("stadion_id").toString()
                                    , waktu,
                                    dataJadwal.get(i).get("id").toString(),
                                    dataJadwal.get(i).get("status_jadwal").toString()));
                        }
                    }else if (dataJadwal.get(i).get("away").toString().equalsIgnoreCase(sharedPrefManager.getSpNamaClub())){
                        Object[] paramclub = {new Object[]{
                                new Object[]{"nama", "=", dataJadwal.get(i).get("home")}}};

                        List<HashMap<String, Object>> dataclub = oc.search_read("persebaya.club", paramclub, "foto_club");
                        for (int c = 0; c < dataclub.size(); ++c) {
                            ArrayListJadwal.add(new Jadwal(
                                    dataJadwal.get(i).get("home").toString(),
                                    String.valueOf(dataclub.get(c).get("foto_club")),
                                    getContext().getResources().getIdentifier("ic_away","drawable",getContext().getPackageName()),
                                    dataJadwal.get(i).get("liga_id").toString(),
                                    tgl,
                                    dataJadwal.get(i).get("stadion_id").toString()
                                    , waktu,
                                    dataJadwal.get(i).get("id").toString(),
                                    dataJadwal.get(i).get("status_jadwal").toString()));
                        }
                    }

                }

            } catch (Exception ex) {
                System.out.println("Error Match Task: " + ex);
            }
            return fotoclub;
        }
    }

    public class TerupdateTask extends AsyncTask<Void,Void,Void>{
        @Override
        protected void onPreExecute() {
            swiper.setRefreshing(true);
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            adapter = new AdapterTerupdate(ArrayListTerupdate);
            rv.setAdapter(adapter);
            adapter.notifyDataSetChanged();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            ArrayListTerupdate = new ArrayList<>();
            try {
                OdooConnect oc = OdooConnect.connect( sharedPrefManager.getSpNamaUser(),sharedPrefManager.getSpPasswordUser());

                Object[] param = {new Object[]{
                        new Object[]{"create_uid", "=", 1}}};

                List<HashMap<String, Object>> data = oc.search_read("persebaya.berita", param, "id","image","title", "headline","content","kategori_brita_id","create_date","create_uid","write_date","write_uid");

                for (int i = 0; i < data.size(); ++i) {
                    ArrayListTerupdate.add(new Terupdate(
                            (Integer) data.get(i).get("id"),
                            String.valueOf(data.get(i).get("image")),
                            String.valueOf(data.get(i).get("title")),
                            String.valueOf(data.get(i).get("kategori_brita_id")),
                            String.valueOf(data.get(i).get("headline")),
                            String.valueOf(data.get(i).get("content")),
                            tanggal(String.valueOf(data.get(i).get("create_date")).substring(0,10)),
                            String.valueOf(data.get(i).get("create_uid"))));
                }
            } catch (Exception ex) {
                System.out.println("Error Terupdate TASK: " + ex);
            }
            return null;
        }
    }

}
