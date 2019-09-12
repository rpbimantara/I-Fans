package com.alpha.test.i_fans;


import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Locale;

import oogbox.api.odoo.OdooClient;
import oogbox.api.odoo.client.OdooVersion;
import oogbox.api.odoo.client.helper.data.OdooRecord;
import oogbox.api.odoo.client.helper.data.OdooResult;
import oogbox.api.odoo.client.helper.utils.ODomain;
import oogbox.api.odoo.client.helper.utils.OdooFields;
import oogbox.api.odoo.client.helper.utils.OdooValues;
import oogbox.api.odoo.client.listeners.IOdooResponse;
import oogbox.api.odoo.client.listeners.OdooConnectListener;

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
    LinearLayout lnStore,lnAuction,lnCoin;
    SwipeRefreshLayout swiper;
    Integer IdPartner;
    OdooClient client;
    HomeActivity activity;
    Fragment currentFragment;

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
        currentFragment = getFragmentManager().findFragmentById(R.id.action_account);
        activity = new HomeActivity();
        swiper = view.findViewById(R.id.swiperefresh_account);
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
        lnStore = view.findViewById(R.id.LinearLayoutStore);
        lnAuction = view.findViewById(R.id.LinearLayoutAuction);
        lnCoin = view.findViewById(R.id.LinearLayoutCoin);
        fabImage.setImageResource(R.drawable.ic_camera);
        fabImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
                photoPickerIntent.setType("image/*");
                startActivityForResult(photoPickerIntent, 1);
            }
        });
        getData();
        swiper.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getData();
            }
        });

        lnCoin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent coinIntent = new Intent(getActivity(), AccountCoinActivity.class);
                startActivity(coinIntent);
            }
        });

        lnStore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent storeIntent = new Intent(getActivity(), AccountStoreActivity.class);
                startActivity(storeIntent);
            }
        });

        lnAuction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent auctionIntent = new Intent(getActivity(), AccountAuctionActivity.class);
                startActivity(auctionIntent);
            }
        });
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

    public void getData(){
        client = new OdooClient.Builder(getContext())
                .setHost(sharedPrefManager.getSP_Host_url())
                .setSession(sharedPrefManager.getSpSessionId())
                .setSynchronizedRequests(false)
                .setConnectListener(new OdooConnectListener() {
                    @Override
                    public void onConnected(OdooVersion version) {
                        // Success connection

                        ODomain domain = new ODomain();
                        domain.add("user_ids", "=", sharedPrefManager.getSpIdUser());

                        OdooFields fields = new OdooFields();
                        fields.addAll("id","name","jeniskelamin","image", "nik","street","tgl_lahir","saldo","email","phone","komunitas");

                        int offset = 0;
                        int limit = 80;

                        String sorting = "id DESC";

                        client.searchRead("res.partner", domain, fields, offset, limit, sorting, new IOdooResponse() {
                            @Override
                            public void onResult(OdooResult result) {
                                OdooRecord[] records = result.getRecords();
                                DecimalFormat formatter = (DecimalFormat) NumberFormat.getInstance(Locale.US);
                                DecimalFormatSymbols symbols = formatter.getDecimalFormatSymbols();

                                symbols.setGroupingSeparator('.');
                                formatter.setDecimalFormatSymbols(symbols);
                                for (OdooRecord record : records) {
                                    sharedPrefManager.saveSPInt(SharedPrefManager.SP_ID_PARTNER, record.getInt("id"));
                                    txtName.setText(nullChecker(record.getString("name")));
                                    txtid.setText(nullChecker(String.valueOf(Math.round(record.getFloat("id")))));
                                    if(record.getString("nik")== "false"){
                                        txtNIK.setText("NIK");
                                    }else{
                                        txtNIK.setText(record.getString("nik"));
                                    }
                                    if(record.getString("jeniskelamin")== "false"){
                                        txtJeniskelamin.setText("Gender");
                                    }else{
                                        txtJeniskelamin.setText(record.getString("jeniskelamin"));
                                    }
                                    if(record.getString("street")== "false"){
                                        txtAlamat.setText("Address");
                                    }else{
                                        txtAlamat.setText(record.getString("street"));
                                    }
                                    if(record.getString("tgl_lahir")== "false"){
                                        txtTTL.setText("Birthday");
                                    }else{
                                        txtTTL.setText(tanggal(record.getString("tgl_lahir")));
                                    }
                                    if(record.getString("email")== "false"){
                                        txtemail.setText("E-mail");
                                    }else{
                                        txtemail.setText(record.getString("email"));
                                    }
                                    if(record.getString("phone")== "false"){
                                        txtTelephone.setText("Phone");
                                    }else{
                                        txtTelephone.setText(record.getString("phone"));
                                    }
                                    if(record.getString("komunitas")== "false"){
                                        txtKomunitas.setText("Community");
                                    }else{
                                        txtKomunitas.setText(record.getString("komunitas"));
                                    }
                                    txtKoin.setText(String.valueOf(formatter.format(record.getFloat("saldo"))));
                                    imageUser.setImageBitmap(StringToBitMap(record.getString("image")));
                                    IdPartner = Integer.valueOf(record.getInt("id"));
                                }
                                swiper.setRefreshing(false);
                            }
                        });
                    }
                })
                .build();
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser){
            onResume();
        }
    }


    public class SaveImageTask extends AsyncTask<Bitmap,Void,Void>{
        @Override
        protected Void doInBackground(final Bitmap... params) {
            client = new OdooClient.Builder(getContext())
                    .setHost(sharedPrefManager.getSP_Host_url())
                    .setSession(sharedPrefManager.getSpSessionId())
                    .setSynchronizedRequests(false)
                    .setConnectListener(new OdooConnectListener() {
                        @Override
                        public void onConnected(OdooVersion version) {
                            OdooValues values = new OdooValues();
                            values.put("image", getBase64ImageString(params[0]));

                            client.write("res.users", new Integer[]{sharedPrefManager.getSpIdUser()}, values, new IOdooResponse() {
                                @Override
                                public void onResult(OdooResult result) {
                                    // Success response
                                    Toast.makeText(getContext(),result.toString(),Toast.LENGTH_LONG).show();
                                }
                            });
                        }
                    }).build();
            return null;
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

    public String tanggal(String tgl){
        try {
            tgl = new SimpleDateFormat("dd MMM yyyy", Locale.US).format(new SimpleDateFormat("yyyy-MM-dd").parse(tgl));
        }catch (Exception ex){
            System.out.println("Error Convert Tanggal: " + ex);
        }

        return tgl;
    }
}
