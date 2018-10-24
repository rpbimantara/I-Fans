package com.example.nerita_hendra.i_fans;


import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;


/**
 * A simple {@link Fragment} subclass.
 */
public class ClubSummaryFragment extends Fragment {
    TextView txtNamaStadion,txtNamaTeam,txtCoach,txtEST,txtCity,txtCEO,txtSupporter,txtAlias;
    ImageView imageclub;
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
        loadData();
        return view;
    }

    public void loadData(){
        try {
            OdooConnect oc = OdooConnect.connect( sharedPrefManager.getSpNamaUser(),sharedPrefManager.getSpPasswordUser());

            Object[] param = {new Object[]{
                    new Object[]{"nama", "=", getActivity().getIntent().getStringExtra("nama")}}};

            List<HashMap<String, Object>> data = oc.search_read("persebaya.club", param, "foto_club","stadion", "pelatih","tgl_berdiri","kota","presiden","suporter","julukan");
            System.out.printf("datasize : "+data.size());
            for (int i = 0; i < data.size(); ++i) {
                txtNamaStadion.setText(String.valueOf(data.get(i).get("stadion")));
                imageclub.setImageBitmap(StringToBitMap(String.valueOf(data.get(i).get("foto_club"))));
                txtCoach.setText(String.valueOf(data.get(i).get("pelatih")));
                txtEST.setText(tanggal(String.valueOf(data.get(i).get("tgl_berdiri"))));
                txtCity.setText(String.valueOf(data.get(i).get("kota")));
                txtCEO.setText(String.valueOf(data.get(i).get("presiden")));
                txtSupporter.setText(String.valueOf(data.get(i).get("suporter")));
                txtAlias.setText(String.valueOf(data.get(i).get("julukan")));
            }

        } catch (Exception ex) {
            System.out.println("Error: " + ex);
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
