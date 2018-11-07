package com.example.nerita_hendra.i_fans;


import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static android.app.Activity.RESULT_OK;


/**
 * A simple {@link Fragment} subclass.
 */
public class AccountFragment extends Fragment {
    TextView txtName,txtid,txtKoin,txtFollower,txtFollowing,txtNIK,txtJeniskelamin,txtAlamat,txtTTL,txtemail,txtTelephone,txtKomunitas;
    ImageView imageUser;
    SharedPrefManager sharedPrefManager;
    FloatingActionButton fabImage;
    private Bitmap currentImage;
    Integer IdPartner;

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
        fabImage  = view.findViewById(R.id.image_fab);
        imageUser = view.findViewById(R.id.image_UserAccount);
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
        fabImage.setImageResource(R.drawable.ic_camera);
        fabImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
                photoPickerIntent.setType("image/*");
                startActivityForResult(photoPickerIntent, 1);
            }
        });
        new  AccountTask().execute();
        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK){
            Uri photoUri = data.getData();
            if (photoUri != null){
                try{
                    currentImage = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), photoUri);
                    imageUser.setImageBitmap(currentImage);
                    new SaveImageTask().execute(currentImage);
                }catch (Exception e){
                    e.printStackTrace();
                }
            }

        }
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser && isResumed()){
            HomeActivity fabhome = (HomeActivity) getActivity();
            fabhome.fabBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent EditAccount = new Intent(getActivity(),AccountEditActivity.class);
                    EditAccount.putExtra("name",txtName.getText());
                    EditAccount.putExtra("nik",txtNIK.getText());
                    EditAccount.putExtra("gender",txtJeniskelamin.getText());
                    EditAccount.putExtra("address",txtAlamat.getText());
                    EditAccount.putExtra("date",txtTTL.getText());
                    EditAccount.putExtra("mail",txtemail.getText());
                    EditAccount.putExtra("phone",txtTelephone.getText());
                    EditAccount.putExtra("comunity",txtKomunitas.getText());
                    EditAccount.putExtra("idPartner",IdPartner);
                    startActivity(EditAccount);
                }
            });
        }
    }


    public class SaveImageTask extends AsyncTask<Bitmap,Void,Void>{
        @Override
        protected Void doInBackground(final Bitmap... params) {
            try {

                OdooConnect oc = OdooConnect.connect(sharedPrefManager.getSpNamaUser(),sharedPrefManager.getSpPasswordUser());

                Boolean idW = oc.write("res.users", new Object[]{ sharedPrefManager.getSpIdUser() },
                        new HashMap() {{
                            put("image", getBase64ImageString(params[0]));
                        }});

            } catch (Exception ex) {
                System.out.println("Error: " + ex);
            }
            return null;
        }
    }

    public class AccountTask extends AsyncTask<Void,Void,List<String>>{
        @Override
        protected void onPostExecute(List result) {
            super.onPostExecute(result);
            txtName.setText(nullChecker(result.get(0).toString()));
            txtid.setText(nullChecker(result.get(1).toString()));
            txtNIK.setText(result.get(2).toString());
            txtJeniskelamin.setText(result.get(3).toString());
            txtAlamat.setText(result.get(4).toString());
            txtTTL.setText(result.get(5).toString());
            txtemail.setText(result.get(6).toString());
            txtTelephone.setText(result.get(7).toString());
            txtKomunitas.setText(result.get(8).toString());
            txtKoin.setText(result.get(9).toString());
            imageUser.setImageBitmap(StringToBitMap(result.get(10).toString()));
            IdPartner = Integer.valueOf(result.get(11).toString());
        }

        @Override
        protected List doInBackground(Void... voids) {
            List<String> dataPartner = new ArrayList<>();
            try {
                OdooConnect oc = OdooConnect.connect( sharedPrefManager.getSpNamaUser(),sharedPrefManager.getSpPasswordUser());

                Object[] param = {new Object[]{
                        new Object[]{"user_ids", "=",sharedPrefManager.getSpIdUser()}}};

                List<HashMap<String, Object>> data = oc.search_read("res.partner", param, "id","name","image", "nik","street","tgl_lahir","saldo","email","phone","komunitas","property_account_receivable_id","property_account_payable_id");

                for (int i = 0; i < data.size(); ++i) {
                    dataPartner.add(String.valueOf(data.get(i).get("name")));
                    dataPartner.add(String.valueOf(data.get(i).get("-")));
                    dataPartner.add(String.valueOf(data.get(i).get("nik")));
                    dataPartner.add(String.valueOf(data.get(i).get("-")));
                    dataPartner.add(String.valueOf(data.get(i).get("street")));
                    dataPartner.add(String.valueOf(data.get(i).get("tgl_lahir")));
                    dataPartner.add(String.valueOf(data.get(i).get("email")));
                    dataPartner.add(String.valueOf(data.get(i).get("phone")));
                    dataPartner.add(String.valueOf(data.get(i).get("komunitas")));
                    dataPartner.add(String.valueOf(data.get(i).get("saldo")));
                    dataPartner.add(String.valueOf(data.get(i).get("image")));
                    dataPartner.add(String.valueOf(data.get(i).get("id")));
                    System.out.println(data.get(i).get("property_account_receivable_id").toString());
                    System.out.println(data.get(i).get("property_account_payable_id").toString());
                }

            } catch (Exception ex) {
                System.out.println("Error: " + ex);
            }
            return dataPartner;
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

    public String getBase64ImageString(Bitmap photo) {
        String imgString;
        if(photo != null) {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            photo.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
            byte[] profileImage = outputStream.toByteArray();

            imgString = Base64.encodeToString(profileImage,
                    Base64.NO_WRAP);
        }else{
            imgString = "";
        }

        return imgString;
    }

    public String nullChecker(String param){
        return ((param == "null") || (param == "false") ? "N/A" : param);
    }
}
