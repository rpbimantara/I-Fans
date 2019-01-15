package com.example.nerita_hendra.i_fans;


import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
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
    View rootView;
    RecyclerView rv;
    ProgressDialog progressDialog;
    AdapterStore adapter;
    SwipeRefreshLayout swiper;


    public StoreFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_store,container,false);
        rv =  rootView.findViewById(R.id.rv_recycler_view_store);
        swiper = rootView.findViewById(R.id.swiperefresh_store);
        sharedPrefManager = new SharedPrefManager(getActivity());
        progressDialog = new ProgressDialog(getActivity());
        adapter = new AdapterStore(ArrayListStore);
        rv.setAdapter(adapter);
        rv.setLayoutManager(new GridLayoutManager(getActivity(),3));
        swiper.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new StoreTask().execute();
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
                    intent.putExtra("nama",ArrayListStore.get(RecyclerViewItemPosition).getNamabarang());
                    intent.putExtra("harga",ArrayListStore.get(RecyclerViewItemPosition).getHargabarang());
                    intent.putExtra("deskripsi",ArrayListStore.get(RecyclerViewItemPosition).getDeskripsi());
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
        new StoreTask().execute();
        return rootView;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser && isResumed()){
            HomeActivity fabhome = (HomeActivity) getActivity();
            fabhome.fabBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent AddStore = new Intent(getActivity(),StoreAddActivity.class);
                    startActivity(AddStore);
                }
            });
        }
    }

    public class StoreTask extends AsyncTask<Void,Void,Void>{
        @Override
        protected void onPreExecute() {
            swiper.setRefreshing(true);
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            adapter = new AdapterStore(ArrayListStore);
            rv.setAdapter(adapter);
            adapter.notifyDataSetChanged();
            swiper.setRefreshing(false);
        }
        @Override
        protected Void doInBackground(Void... voids) {
            ArrayListStore = new ArrayList<>();
            try {
                OdooConnect oc = OdooConnect.connect(sharedPrefManager.getSpNamaUser(),sharedPrefManager.getSpPasswordUser());

                Object[] param = {new Object[]{
                        new Object[]{"active", "=", true},
                        new Object[]{"type", "!=", "service"}}};

                List<HashMap<String, Object>> data = oc.search_read("product.template", param, "id","image_medium","name", "type","default_code","cated_ig","list_price","description_sale","description_picking","create_uid");

                for (int i = 0; i < data.size(); ++i) {
                    String code = " ";
                    if (String.valueOf(data.get(i).get("default_code")).equalsIgnoreCase("false")){
                        code = "";
                    }else{
                        code = "["+String.valueOf(data.get(i).get("default_code")) +"] ";
                    }
                    ArrayListStore.add(new Store(
                            String.valueOf(data.get(i).get("image_medium")),
                            code +String.valueOf(data.get(i).get("name")),
                            String.valueOf("Rp. " + data.get(i).get("list_price")),
                            String.valueOf(data.get(i).get("description_sale"))));
                }
            } catch (Exception ex) {
                System.out.println("Error: " + ex);
            }
            return null;
        }
    }

}
