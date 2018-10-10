package com.example.nerita_hendra.i_fans;


import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.HashMap;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class AccountFragment extends Fragment {
    TextView txtName,txtid,txtKoin,txtFollower,txtFollowing,txtNIK,txtJeniskelamin,txtAlamat,txtTTL,txtemail,txtTelephone,txtKomunitas;

    SharedPrefManager sharedPrefManager;

    public static AccountFragment newInstance(){
        AccountFragment fragment = new AccountFragment();
        return fragment;
    }

    public AccountFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_account, container, false);
        Fragment currentFragment = getFragmentManager().findFragmentById(R.id.action_account);
        txtName = (TextView) view.findViewById(R.id.txt_namaAccount);
        txtid = (TextView) view.findViewById(R.id.txt_IdAccount);
        txtNIK = (TextView) view.findViewById(R.id.txt_NIKAccount);
        txtJeniskelamin = (TextView) view.findViewById(R.id.txt_kelaminAccount);
        txtAlamat = (TextView) view.findViewById(R.id.txt_alamatAccount);
        txtTTL = (TextView) view.findViewById(R.id.txt_ttlAccount);
        txtemail = (TextView) view.findViewById(R.id.txt_emailAccount);
        txtTelephone = (TextView) view.findViewById(R.id.txt_phoneAccount);
        txtKomunitas = (TextView) view.findViewById(R.id.txt_komunitasAccount);
        txtKoin = (TextView) view.findViewById(R.id.txt_koinAccount);
        sharedPrefManager = new SharedPrefManager(getActivity());
        AccountData(sharedPrefManager.getSpNamaUser(),sharedPrefManager.getSpPasswordUser(),sharedPrefManager.getSpIdUser());
        FloatingActionButton mSharedFAB = ((HomeActivity) getActivity()).getFabBtn();
        mSharedFAB.show();
        return view;
    }

    void AccountData(String user, String pass,Integer IdUser){
        try {
            OdooConnect oc = OdooConnect.connect( user, pass);

            Object[] param = {new Object[]{
                    new Object[]{"user_ids", "=", IdUser}}};

            List<HashMap<String, Object>> data = oc.search_read("res.partner", param, "name", "nik","street","tgl_lahir","saldo","email","phone","komunitas");

            for (int i = 0; i < data.size(); ++i) {
                txtName.setText(String.valueOf(data.get(i).get("name")));
                txtid.setText(String.valueOf(data.get(i).get("-")));
                txtNIK.setText(String.valueOf(data.get(i).get("nik")));
                txtJeniskelamin.setText(String.valueOf(data.get(i).get("-")));
                txtAlamat.setText(String.valueOf(data.get(i).get("street")));
                txtTTL.setText(String.valueOf(data.get(i).get("tgl_lahir")));
                txtemail.setText(String.valueOf(data.get(i).get("email")));
                txtTelephone.setText(String.valueOf(data.get(i).get("phone")));
                txtKomunitas.setText(String.valueOf(data.get(i).get("komunitas")));
                txtKoin.setText(String.valueOf(data.get(i).get("saldo")));
            }

        } catch (Exception ex) {
            System.out.println("Error: " + ex);
        }
    }
}
