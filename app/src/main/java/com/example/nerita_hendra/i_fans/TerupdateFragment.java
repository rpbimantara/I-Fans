package com.example.nerita_hendra.i_fans;


import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;


/**
 * A simple {@link Fragment} subclass.
 */
public class TerupdateFragment extends Fragment {

    ArrayList<Terupdate> ArrayListTerupdate;
    int RecyclerViewItemPosition ;
    SharedPrefManager sharedPrefManager;

    public TerupdateFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_terupdate, container, false);
        RecyclerView rv = (RecyclerView) rootView.findViewById(R.id.rv_recycler_view_hot_news);
        sharedPrefManager = new SharedPrefManager(getActivity());
        addData(sharedPrefManager.getSpNamaUser(),sharedPrefManager.getSpPasswordUser(),sharedPrefManager.getSpIdUser());
        AdapterTerupdate adapter = new  AdapterTerupdate(ArrayListTerupdate);
        rv.setAdapter(adapter);
        RecyclerView.LayoutManager llm = new  LinearLayoutManager(getActivity(),LinearLayoutManager.HORIZONTAL,false);
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

                    RecyclerViewItemPosition = rv.getChildAdapterPosition(ChildView);
                    Intent intent = new Intent(getActivity(),BeritaDetailActivity.class);
                    intent.putExtra("id",ArrayListTerupdate.get(RecyclerViewItemPosition).getId());
                    intent.putExtra("title",ArrayListTerupdate.get(RecyclerViewItemPosition).getTitle());
                    intent.putExtra("kategori",ArrayListTerupdate.get(RecyclerViewItemPosition).getKategori());
                    intent.putExtra("headline",ArrayListTerupdate.get(RecyclerViewItemPosition).getHeadline());
                    intent.putExtra("konten",ArrayListTerupdate.get(RecyclerViewItemPosition).getKonten());
                    intent.putExtra("tanggalbuat",ArrayListTerupdate.get(RecyclerViewItemPosition).getTanggal());
                    intent.putExtra("penulis",ArrayListTerupdate.get(RecyclerViewItemPosition).getPenulis());

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
        return rootView;
    }

    void addData(String user, String pass,Integer IdUser){
        ArrayListTerupdate = new ArrayList<>();
        try {
            OdooConnect oc = OdooConnect.connect( user, pass);

            Object[] param = {new Object[]{
                    new Object[]{"create_uid", "=", 1}}};

            List<HashMap<String, Object>> data = oc.search_read("persebaya.berita", param, "id","title", "headline","content","kategori_brita_id","create_date","create_uid","write_date","write_uid");

            for (int i = 0; i < data.size(); ++i) {
                ArrayListTerupdate.add(new Terupdate(
                        (Integer) data.get(i).get("id"),
                        String.valueOf(data.get(i).get("title")),
                        String.valueOf(data.get(i).get("kategori_brita_id")),
                        String.valueOf(data.get(i).get("headline")),
                        String.valueOf(data.get(i).get("content")),
                        tanggal(String.valueOf(data.get(i).get("create_date")).substring(0,10)),
                        String.valueOf(data.get(i).get("create_uid"))));
            }
        } catch (Exception ex) {
            System.out.println("Error: " + ex);
        }
    }

    public String tanggal(String tgl){
        try {
            tgl = new SimpleDateFormat("dd MMM yyyy", Locale.US).format(new SimpleDateFormat("yyyy-MM-dd").parse(tgl));
        }catch (Exception ex){
            System.out.println("Error Convert Tanggal: " + ex);
        }

        return tgl;
    }

}
