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
public class TeamDetailStatisticFragment extends Fragment {

    ProgressBar pbGoalkick,pbGoal,pbPosession,pbAerial,pbPasses,pbKeyPasses,pbAssist,pbTackles,pbSaves,pbFouls,pbYellow,pbRed,pbOffside,pbClearance,pbBlock,pbInterception;
    TextView txtGoalkick,txtGoal,txtPosession,txtAerial,txtPasses,txtKeyPasses,txtAssist,txtTackles,txtSaves,txtFouls,txtYellow,txtRed,txtOffside,txtClearance,txtBlock,txtInterception;
    View rootView;
    OdooClient client;

    public TeamDetailStatisticFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView =  inflater.inflate(R.layout.fragment_team_detail_statistic, container, false);
        pbGoalkick = rootView.findViewById(R.id.progressBarGoalKick);
        pbGoal = rootView.findViewById(R.id.progressBarGoal);
        pbPosession = rootView.findViewById(R.id.progressBarPossessionLoss);
        pbAerial = rootView.findViewById(R.id.progressBarAerial);
        pbPasses = rootView.findViewById(R.id.progressBarPasses);
        pbKeyPasses = rootView.findViewById(R.id.progressBarKeyPasses);
        pbAssist = rootView.findViewById(R.id.progressBarAssist);
        pbTackles = rootView.findViewById(R.id.progressBarTackles);
        pbSaves = rootView.findViewById(R.id.progressBarSaves);
        pbFouls = rootView.findViewById(R.id.progressBarFouls);
        pbYellow = rootView.findViewById(R.id.progressBarYellowCard);
        pbRed = rootView.findViewById(R.id.progressBarRedCard);
        pbOffside = rootView.findViewById(R.id.progressBarOffsides);
        pbClearance = rootView.findViewById(R.id.progressBarClearances);
        pbBlock = rootView.findViewById(R.id.progressBarBlock);
        pbInterception = rootView.findViewById(R.id.progressBarInterception);

        txtGoalkick = rootView.findViewById(R.id.txt_GoalKick);
        txtGoal = rootView.findViewById(R.id.txt_Goal);
        txtPosession = rootView.findViewById(R.id.txt_Poesseion);
        txtAerial = rootView.findViewById(R.id.txt_Aerial);
        txtPasses = rootView.findViewById(R.id.txt_Passes);
        txtKeyPasses = rootView.findViewById(R.id.txt_KeyPasses);
        txtAssist = rootView.findViewById(R.id.txt_Assist);
        txtTackles = rootView.findViewById(R.id.txt_Tackles);
        txtSaves = rootView.findViewById(R.id.txt_Saves);
        txtFouls = rootView.findViewById(R.id.txt_Fouls);
        txtYellow = rootView.findViewById(R.id.txt_Yellow);
        txtRed = rootView.findViewById(R.id.txt_Red);
        txtOffside = rootView.findViewById(R.id.txt_Offsides);
        txtClearance = rootView.findViewById(R.id.txt_Clearances);
        txtBlock = rootView.findViewById(R.id.txt_Block);
        txtInterception = rootView.findViewById(R.id.txt_Interception);

        client = getOdooConnection(getContext());
        loadTeamStatistic();
    return rootView;
    }

    public void loadTeamStatistic(){
        ODomain domain = new ODomain();
        domain.add("id", "=",getActivity().getIntent().getExtras().get("id_atlete"));

        OdooFields fields = new OdooFields();
        fields.addAll("id","gol_kick","gol", "lepas_control","sundulan_kepala","passing_sukses","passing_gagal","assist","tekel_sukses","penyelamatan","pelanggaran","kartu_kuning","kartu_merah","offsides","sapu_bersih","penghadangan","sukses_rebut");

        int offset = 0;
        int limit = 80;

        String sorting = "id ASC";

        client.searchRead("hr.employee", domain, fields, offset, limit, sorting, new IOdooResponse() {
            @Override
            public void onResult(OdooResult result) {
                OdooRecord[] records = result.getRecords();
                System.out.println(records.toString());
                for (OdooRecord record : records) {
                    pbGoalkick.setProgress(record.getInt("gol_kick"));
                    txtGoalkick.setText(String.valueOf(record.getInt("gol_kick")));

                    pbGoal.setProgress(record.getInt("gol"));
                    txtGoal.setText(String.valueOf(record.getInt("gol")));

                    pbPosession.setProgress(record.getInt("lepas_control"));
                    txtPosession.setText(String.valueOf(record.getInt("lepas_control")));

                    pbAerial.setProgress(record.getInt("sundulan_kepala"));
                    txtAerial.setText(String.valueOf(record.getInt("sundulan_kepala")));

                    pbPasses.setProgress(record.getInt("passing_sukses"));
                    txtPasses.setText(String.valueOf(record.getInt("passing_sukses")));

                    pbKeyPasses.setProgress(record.getInt("passing_gagal"));
                    txtKeyPasses.setText(String.valueOf(record.getInt("passing_gagal")));

                    pbAssist.setProgress(record.getInt("assist"));
                    txtAssist.setText(String.valueOf(record.getInt("assist")));

                    pbTackles.setProgress(record.getInt("tekel_sukses"));
                    txtTackles.setText(String.valueOf(record.getInt("tekel_sukses")));

                    pbSaves.setProgress(record.getInt("penyelamatan"));
                    txtSaves.setText(String.valueOf(record.getInt("penyelamatan")));

                    pbFouls.setProgress(record.getInt("pelanggaran"));
                    txtFouls.setText(String.valueOf(record.getInt("pelanggaran")));

                    pbYellow.setProgress(record.getInt("kartu_kuning"));
                    txtYellow.setText(String.valueOf(record.getInt("kartu_kuning")));

                    pbRed.setProgress(record.getInt("kartu_merah"));
                    txtRed.setText(String.valueOf(record.getInt("kartu_merah")));

                    pbOffside.setProgress(record.getInt("offsides"));
                    txtOffside.setText(String.valueOf(record.getInt("offsides")));

                    pbClearance.setProgress(record.getInt("sapu_bersih"));
                    txtClearance.setText(String.valueOf(record.getInt("sapu_bersih")));

                    pbBlock.setProgress(record.getInt("penghadangan"));
                    txtBlock.setText(String.valueOf(record.getInt("penghadangan")));

                    pbInterception.setProgress(record.getInt("sukses_rebut"));
                    txtInterception.setText(String.valueOf(record.getInt("sukses_rebut")));

                }
            }

        });
    }

}
