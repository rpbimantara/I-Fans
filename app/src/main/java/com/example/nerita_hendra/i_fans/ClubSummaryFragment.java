package com.example.nerita_hendra.i_fans;


import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;


/**
 * A simple {@link Fragment} subclass.
 */
public class ClubSummaryFragment extends Fragment {
    TextView txtNamaStadion,txtNamaTeam,txtCoach,txtEST,txtCity,txtCEO,txtSupporter,txtAlias;
    ImageView imageclub,imageStadion;
    SharedPrefManager sharedPrefManager;
    public ClubSummaryFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_club_summary, container, false);
        imageclub = view.findViewById(R.id.club_imageView_summary);
        imageStadion = view.findViewById(R.id.summary_imageView);
        txtNamaStadion = view.findViewById(R.id.textView_namastadion_summary);
        txtNamaTeam = view.findViewById(R.id.textView_namateam_summary);
        txtNamaTeam.setText(getActivity().getIntent().getStringExtra("nama"));
        txtCoach = view.findViewById(R.id.textView_head_coach);
        txtEST = view.findViewById(R.id.textView_est);
        txtCity = view.findViewById(R.id.textView_city);
        txtCEO = view.findViewById(R.id.textView_ceo);
        txtSupporter = view.findViewById(R.id.textView_supporter);
        txtAlias = view.findViewById(R.id.textView_alias);
        sharedPrefManager = new SharedPrefManager(getActivity());
        new LoadDataAssyc().execute(getActivity().getIntent().getStringExtra("nama"));
        return view;
    }

    public class LoadDataAssyc extends AsyncTask<String,Void,ArrayList<String>>{
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(ArrayList<String> aVoid) {
            super.onPostExecute(aVoid);
            txtNamaStadion.setText(aVoid.get(0));
            imageclub.setImageBitmap(StringToBitMap(aVoid.get(1)));
            imageStadion.setImageBitmap(StringToBitMap(aVoid.get(8)));
            txtCoach.setText(aVoid.get(2));
            txtEST.setText(tanggal(aVoid.get(3)));
            txtCity.setText(aVoid.get(4));
            txtCEO.setText(aVoid.get(5));
            txtSupporter.setText(aVoid.get(6));
            txtAlias.setText(aVoid.get(7));
        }

        @Override
        protected ArrayList<String> doInBackground(String... voids) {
            ArrayList<String> result = new ArrayList<>();
            try {
                OdooConnect oc = OdooConnect.connect( sharedPrefManager.getSpNamaUser(),sharedPrefManager.getSpPasswordUser());

                Object[] param = {new Object[]{
                        new Object[]{"nama", "=", voids[0]}}};

                List<HashMap<String, Object>> data = oc.search_read("persebaya.club", param, "foto_club","stadion","pelatih","tgl_berdiri","kota","presiden","suporter","julukan");

                Object[] paramStadion = {new Object[]{
                        new Object[]{"nama", "=",data.get(0).get("stadion").toString()}}};

                List<HashMap<String, Object>> dataStadion = oc.search_read("persebaya.stadion", paramStadion, "id","image");

                result.add(data.get(0).get("stadion").toString());
                result.add(data.get(0).get("foto_club").toString());
                result.add(data.get(0).get("pelatih").toString());
                result.add(data.get(0).get("tgl_berdiri").toString());
                result.add(data.get(0).get("kota").toString());
                result.add(data.get(0).get("presiden").toString());
                result.add(data.get(0).get("suporter").toString());
                result.add(data.get(0).get("julukan").toString());
                result.add(dataStadion.get(0).get("image").toString());
            } catch (Exception ex) {
                System.out.println("Error Summary: " + ex);
            }
            return result;
        }
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

    public String tanggal(String tgl){
        try {
            tgl = new SimpleDateFormat("dd MMM yyyy", Locale.US).format(new SimpleDateFormat("yyyy-MM-dd").parse(tgl));
        }catch (Exception ex){
            System.out.println("Error Convert Tanggal: " + ex);
        }

        return tgl;
    }

}
