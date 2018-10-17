package com.example.nerita_hendra.i_fans;


import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
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
public class KlasemenFragment extends Fragment {

    ArrayList<Klasemen> ArrayListKlasemen;
    int RecyclerViewItemPosition ;
    SharedPrefManager sharedPrefManager;
    ProgressDialog progressDialog;
    View rootView;
    RecyclerView rv;
    AdapterKlasemen adapter;

    public KlasemenFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_klasemen, container, false);
        rv = (RecyclerView) rootView.findViewById(R.id.rv_recycler_view_klasemen);
        sharedPrefManager = new SharedPrefManager(getActivity());
        progressDialog = new ProgressDialog(getActivity());
        RecyclerView.LayoutManager llm = new LinearLayoutManager(getActivity());
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
                    Intent intent = new Intent(getActivity(),ClubDetailActivity.class);
//                    intent.putExtra("id",ArrayListBerita.get(RecyclerViewItemPosition).getId());
//                    intent.putExtra("title",ArrayListBerita.get(RecyclerViewItemPosition).getTitle());
//                    intent.putExtra("kategori",ArrayListBerita.get(RecyclerViewItemPosition).getKategori());
//                    intent.putExtra("headline",ArrayListBerita.get(RecyclerViewItemPosition).getHeadline());
//                    intent.putExtra("konten",ArrayListBerita.get(RecyclerViewItemPosition).getKonten());
//                    intent.putExtra("tanggalbuat",ArrayListBerita.get(RecyclerViewItemPosition).getTanggal());
//                    intent.putExtra("penulis",ArrayListBerita.get(RecyclerViewItemPosition).getPenulis());

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
        new KlasemenTask().execute();
        adapter = new AdapterKlasemen(ArrayListKlasemen);
        rv.setAdapter(adapter);
        return rootView;
    }

    public class KlasemenTask extends AsyncTask<Void,Void,Void>{
        @Override
        protected void onPreExecute() {
            progressDialog.setIndeterminate(true);
            progressDialog.setMessage("Loading...");
            progressDialog.show();
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            progressDialog.dismiss();
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected Void doInBackground(Void... voids) {
            ArrayListKlasemen =  new ArrayList<>();
            try {
                OdooConnect oc = OdooConnect.connect( sharedPrefManager.getSpNamaUser(),sharedPrefManager.getSpPasswordUser());

                Object[] param = {new Object[]{
                        new Object[]{"liga_id", "=", "1"}}};

                List<HashMap<String, Object>> data = oc.search_read("persebaya.liga.klasemen", param, "id","club_id", "play","win","draw","lose","gm","gk","point");
                ArrayListKlasemen.add(new Klasemen(
                        String.valueOf("No."),
                        String.valueOf("Club"),
                        String.valueOf("P"),
                        String.valueOf("Win"),
                        String.valueOf("Draw"),
                        String.valueOf("Lose"),
                        String.valueOf("+/-"),
                        String.valueOf("Pts")));
                for (int i = 0; i < data.size(); ++i) {
                    ArrayListKlasemen.add(new Klasemen(
                            String.valueOf(i+1),
                            String.valueOf(data.get(i).get("club_id")),
                            String.valueOf(data.get(i).get("play")),
                            String.valueOf(data.get(i).get("win")),
                            String.valueOf(data.get(i).get("draw")),
                            String.valueOf(data.get(i).get("lose")),
                            String.valueOf(data.get(i).get("gm")),
                            String.valueOf( data.get(i).get("point"))));
                }
            } catch (Exception ex) {
                System.out.println("Error Klasemen Fragment: " + ex);
            }
            return null;
        }
    }
}
