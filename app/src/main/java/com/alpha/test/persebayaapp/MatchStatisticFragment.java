package com.alpha.test.persebayaapp;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import oogbox.api.odoo.OdooClient;
import oogbox.api.odoo.client.helper.data.OdooRecord;
import oogbox.api.odoo.client.helper.data.OdooResult;
import oogbox.api.odoo.client.helper.utils.ODomain;
import oogbox.api.odoo.client.helper.utils.OdooFields;
import oogbox.api.odoo.client.listeners.IOdooResponse;

import static com.alpha.test.persebayaapp.CommonUtils.getOdooConnection;


/**
 * A simple {@link Fragment} subclass.
 */
public class MatchStatisticFragment extends Fragment {
    ProgressBar pbBallHome,pbBallAway,pbPassingHome,pbPassingAway,pbTotalHome,pbTotalAway,pbTargetHome,pbTargetAway,
            pbCorrnerHome, pbCorrnerAway,pbFoulsHome,pbFoulsAway,pbOffsidesHome,pbOffsidesAway,pbYellowHome,pbYellowAway,pbRedHome,pbRedAway;

    TextView txt_BallHome,txt_BallAway,txt_PassingHome,txt_PassingAway,txt_TotalHome,txt_TotalAway,txt_TargetHome,txt_TargetAway,
            txt_CorrnerHome, txt_CorrnerAway,txt_FoulsHome,txt_FoulsAway,txt_OffsidesHome,txt_OffsidesAway,txt_YellowHome,txt_YellowAway,
            txt_RedHome,txt_RedAway;
    View rootView;
    OdooClient client;

    public MatchStatisticFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_match_statistic, container, false);
        pbBallHome = rootView.findViewById(R.id.progressBarBallHome);
        pbBallAway = rootView.findViewById(R.id.progressBarBallAway);
        pbPassingHome = rootView.findViewById(R.id.progressBarPassingHome);
        pbPassingAway = rootView.findViewById(R.id.progressBarPassingAway);
        pbTotalHome = rootView.findViewById(R.id.progressBarTotalHome);
        pbTotalAway = rootView.findViewById(R.id.progressBarTotalAway);
        pbTargetHome = rootView.findViewById(R.id.progressBarTargetHome);
        pbTargetAway = rootView.findViewById(R.id.progressBarTargetAway);
        pbCorrnerHome = rootView.findViewById(R.id.progressBarCornerHome);
        pbCorrnerAway = rootView.findViewById(R.id.progressBarCornerAway);
        pbFoulsHome = rootView.findViewById(R.id.progressBarFoulsHome);
        pbFoulsAway = rootView.findViewById(R.id.progressBarFoulsAway);
        pbOffsidesHome = rootView.findViewById(R.id.progressBarOffsideHome);
        pbOffsidesAway = rootView.findViewById(R.id.progressBarOffsideAway);
        pbYellowHome = rootView.findViewById(R.id.progressBarYellowHome);
        pbYellowAway = rootView.findViewById(R.id.progressBarYellowAway);
        pbRedHome = rootView.findViewById(R.id.progressBarRedHome);
        pbRedAway = rootView.findViewById(R.id.progressBarRedAway);

        txt_BallHome = rootView.findViewById(R.id.txt_BallHome);
        txt_BallAway = rootView.findViewById(R.id.txt_BallAway);
        txt_PassingHome = rootView.findViewById(R.id.txt_PassingHome);
        txt_PassingAway = rootView.findViewById(R.id.txt_PassingAway);
        txt_TotalHome = rootView.findViewById(R.id.txt_TotalHome);
        txt_TotalAway = rootView.findViewById(R.id.txt_TotalAway);
        txt_TargetHome = rootView.findViewById(R.id.txt_TargetHome);
        txt_TargetAway = rootView.findViewById(R.id.txt_TargetAway);
        txt_CorrnerHome = rootView.findViewById(R.id.txt_CornerHome);
        txt_CorrnerAway = rootView.findViewById(R.id.txt_CornerAway);
        txt_FoulsHome = rootView.findViewById(R.id.txt_FoulsHome);
        txt_FoulsAway = rootView.findViewById(R.id.txt_FoulsAway);
        txt_OffsidesHome = rootView.findViewById(R.id.txt_OffsidesHome);
        txt_OffsidesAway = rootView.findViewById(R.id.txt_OffsidesAway);
        txt_YellowHome = rootView.findViewById(R.id.txt_YellowHome);
        txt_YellowAway = rootView.findViewById(R.id.txt_YellowAway);
        txt_RedHome = rootView.findViewById(R.id.txt_RedHome);
        txt_RedAway = rootView.findViewById(R.id.txt_RedAway);
        client = getOdooConnection(getContext());
        loadMatchStatistic();
        return rootView;
    }

    public void loadMatchStatistic(){
        ODomain domain = new ODomain();
        domain.add("id", "=",getActivity().getIntent().getExtras().get("id_jadwal"));

        OdooFields fields = new OdooFields();
        fields.addAll("id","penguasaan_home","penguasaan_away", "tembakan_home","tembakan_away","sudut_home","sudut_away",
                "offside_home","offside_away","pelanggaran_home","pelanggaran_away","kuning_home","kuning_away","merah_home","merah_away");

        int offset = 0;
        int limit = 80;

        String sorting = "id ASC";

        client.searchRead("persebaya.jadwal", domain, fields, offset, limit, sorting, new IOdooResponse() {
            @Override
            public void onResult(OdooResult result) {
                OdooRecord[] records = result.getRecords();
                System.out.println(records.toString());
                for (OdooRecord record : records) {
                    pbBallHome.setProgress(record.getInt("penguasaan_home"));
                    txt_BallHome.setText(String.valueOf(record.getInt("penguasaan_home")));
                    pbBallAway.setProgress(record.getInt("penguasaan_away"));
                    txt_BallAway.setText(String.valueOf(record.getInt("penguasaan_away")));

                    pbTotalHome.setProgress(record.getInt("tembakan_home"));
                    txt_TotalHome.setText(String.valueOf(record.getInt("tembakan_home")));
                    pbTotalAway.setProgress(record.getInt("tembakan_away"));
                    txt_TotalAway.setText(String.valueOf(record.getInt("tembakan_away")));

                    pbCorrnerHome.setProgress(record.getInt("sudut_home"));
                    txt_CorrnerHome.setText(String.valueOf(record.getInt("sudut_home")));
                    pbCorrnerAway.setProgress(record.getInt("sudut_away"));
                    txt_CorrnerAway.setText(String.valueOf(record.getInt("sudut_away")));

                    pbFoulsHome.setProgress(record.getInt("pelanggaran_home"));
                    txt_FoulsHome.setText(String.valueOf(record.getInt("pelanggaran_home")));
                    pbFoulsAway.setProgress(record.getInt("pelanggaran_away"));
                    txt_FoulsAway.setText(String.valueOf(record.getInt("pelanggaran_away")));

                    pbOffsidesHome.setProgress(record.getInt("offside_home"));
                    txt_OffsidesHome.setText(String.valueOf(record.getInt("offside_home")));
                    pbOffsidesAway.setProgress(record.getInt("offside_away"));
                    txt_OffsidesAway.setText(String.valueOf(record.getInt("offside_away")));

                    pbYellowHome.setProgress(record.getInt("kuning_home"));
                    txt_YellowHome.setText(String.valueOf(record.getInt("kuning_home")));
                    pbYellowAway.setProgress(record.getInt("kuning_away"));
                    txt_YellowAway.setText(String.valueOf(record.getInt("kuning_away")));

                    pbRedHome.setProgress(record.getInt("merah_home"));
                    txt_RedHome.setText(String.valueOf(record.getInt("merah_home")));
                    pbRedAway.setProgress(record.getInt("merah_away"));
                    txt_RedAway.setText(String.valueOf(record.getInt("penguasaan_away")));
                }
            }

        });
    }

}
