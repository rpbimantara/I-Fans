package com.example.nerita_hendra.i_fans;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class StoreFragment extends Fragment {

    ArrayList<Store> ArrayListStore;
    int RecyclerViewItemPosition ;
    SharedPrefManager sharedPrefManager;


    public StoreFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_store,container,false);
        RecyclerView rv = (RecyclerView) rootView.findViewById(R.id.rv_recycler_view_store);
        sharedPrefManager = new SharedPrefManager(getActivity());
        addData(sharedPrefManager.getSpNamaUser(),sharedPrefManager.getSpPasswordUser(),sharedPrefManager.getSpIdUser());
        AdapterStore adapter = new AdapterStore(ArrayListStore);
        rv.setAdapter(adapter);
        RecyclerView.LayoutManager Glm = new GridLayoutManager(getActivity(),2);
        rv.setLayoutManager(Glm);
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
                    intent.putExtra("nama",ArrayListStore.get(RecyclerViewItemPosition).getNamabarang());
                    intent.putExtra("harga",ArrayListStore.get(RecyclerViewItemPosition).getHargabarang());
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
        ArrayListStore = new ArrayList<>();
        try {
            OdooConnect oc = OdooConnect.connect( user, pass);

            Object[] param = {new Object[]{
                    new Object[]{"active", "=", true}}};

            List<HashMap<String, Object>> data = oc.search_read("persebaya.merchandise", param, "id","nama_barang", "harga_barang","stock_total_barang","status_merch","create_uid");

            for (int i = 0; i < data.size(); ++i) {
                ArrayListStore.add(new Store(
                        String.valueOf(data.get(i).get("nama_barang")),
                        String.valueOf("Rp. " + data.get(i).get("harga_barang"))));
            }
        } catch (Exception ex) {
            System.out.println("Error: " + ex);
        }
    }

}
