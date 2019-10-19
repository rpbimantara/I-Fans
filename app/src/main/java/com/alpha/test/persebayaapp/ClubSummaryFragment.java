package com.alpha.test.persebayaapp;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import oogbox.api.odoo.OdooClient;
import oogbox.api.odoo.client.helper.data.OdooRecord;
import oogbox.api.odoo.client.helper.data.OdooResult;
import oogbox.api.odoo.client.helper.utils.OArguments;
import oogbox.api.odoo.client.listeners.IOdooResponse;

import static com.alpha.test.persebayaapp.CommonUtils.StringToBitMap;
import static com.alpha.test.persebayaapp.CommonUtils.getOdooConnection;
import static com.alpha.test.persebayaapp.CommonUtils.tanggal;


/**
 * A simple {@link Fragment} subclass.
 */
public class ClubSummaryFragment extends Fragment {
    TextView txtNamaStadion,txtNamaTeam,txtCoach,txtEST,txtCity,txtCEO,txtSupporter,txtAlias;
    ImageView imageclub,imageStadion;
    SharedPrefManager sharedPrefManager;
    OdooClient client;

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
        client = getOdooConnection(getContext());
        LoadData();
        return view;
    }

    public void LoadData(){
        OArguments arguments = new OArguments();
        arguments.add(getActivity().getIntent().getStringExtra("id"));

        client.call_kw("persebaya.club", "get_summary", arguments, new IOdooResponse() {
            @Override
            public void onResult(OdooResult result) {
                // response
                OdooRecord[] Records = result.getRecords();

                for (final OdooRecord record : Records) {
                    txtNamaStadion.setText(record.getString("stadion"));
                    imageclub.setImageBitmap(StringToBitMap(record.getString("foto_club")));
                    imageStadion.setImageBitmap(StringToBitMap(record.getString("foto_stadion")));
                    txtCoach.setText(record.getString("coach"));
                    txtEST.setText(tanggal(record.getString("est")));
                    txtCity.setText(record.getString("city"));
                    txtCEO.setText(record.getString("ceo"));
                    txtSupporter.setText(record.getString("support"));
                    txtAlias.setText(record.getString("alias"));
                }
            }
        });
    }

}
