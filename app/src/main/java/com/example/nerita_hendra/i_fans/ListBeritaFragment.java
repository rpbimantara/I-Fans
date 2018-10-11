package com.example.nerita_hendra.i_fans;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
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
public class ListBeritaFragment extends Fragment {

    private ArrayList<ListBerita> ArrayListBerita;
    int RecyclerViewItemPosition ;
    SharedPrefManager sharedPrefManager;

    public ListBeritaFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        final View rootView = inflater.inflate(R.layout.fragment_list_berita, container, false);
        RecyclerView rv = (RecyclerView) rootView.findViewById(R.id.rv_recycler_view_list_berita);
//        rv.setHasFixedSize(true);
        sharedPrefManager = new SharedPrefManager(getActivity());
        addData(sharedPrefManager.getSpNamaUser(),sharedPrefManager.getSpPasswordUser(),sharedPrefManager.getSpIdUser());
        AdapterListBerita adapter = new AdapterListBerita(ArrayListBerita);
        rv.setAdapter(adapter);
        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
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
                    intent.putExtra("id",ArrayListBerita.get(RecyclerViewItemPosition).getId());
                    intent.putExtra("title",ArrayListBerita.get(RecyclerViewItemPosition).getTitle());
                    intent.putExtra("kategori",ArrayListBerita.get(RecyclerViewItemPosition).getKategori());
                    intent.putExtra("headline",ArrayListBerita.get(RecyclerViewItemPosition).getHeadline());
                    intent.putExtra("konten",ArrayListBerita.get(RecyclerViewItemPosition).getKonten());
                    intent.putExtra("tanggalbuat",ArrayListBerita.get(RecyclerViewItemPosition).getTanggal());
                    intent.putExtra("penulis",ArrayListBerita.get(RecyclerViewItemPosition).getPenulis());

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
        ArrayListBerita = new ArrayList<>();
        try {
            OdooConnect oc = OdooConnect.connect( user, pass);

            Object[] param = {new Object[]{
                    new Object[]{"create_uid", "=", 1}}};

            List<HashMap<String, Object>> data = oc.search_read("persebaya.berita", param, "id","title", "headline","content","kategori_brita_id","create_date","create_uid","write_date","write_uid");

            for (int i = 0; i < data.size(); ++i) {
                ArrayListBerita.add(new ListBerita(
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
