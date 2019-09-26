package com.alpha.test.i_fans;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.view.View;
import android.widget.TextView;

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
import oogbox.api.odoo.client.listeners.IOdooResponse;
import oogbox.api.odoo.client.listeners.OdooConnectListener;

public class CommonUtils {
    static SharedPrefManager sharedPrefManager;
    static OdooClient client;


    public static class Emptyholder extends RecyclerView.ViewHolder{
        public TextView txtTitle,txtSubtitle;
        String title,subtitle;

        public Emptyholder(View itemView, String Title, String Subtitle) {
            super(itemView);
            this.title = Title;
            this.subtitle = Subtitle;
            this.txtTitle = (TextView) itemView.findViewById(R.id.tvEmptyTitle);
            this.txtSubtitle = (TextView) itemView.findViewById(R.id.tvEmptySubtitle);
        }

        public void onBind(int i) {
            txtTitle.setText(title);
            txtSubtitle.setText(subtitle);
        }
    }

    public static Bitmap StringToBitMap(String encodedString){
        try{
            byte [] encodeByte= Base64.decode(encodedString,Base64.DEFAULT);
            Bitmap bitmap= BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
            return bitmap;
        }catch(Exception e){
            e.getMessage();
            return null;
        }
    }

    public static String getBase64ImageString(Bitmap photo) {
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

    public static String nullChecker(String param){
        return ((param == "null") || (param == "false") ? "N/A" : param);
    }

    public static String tanggal(String tgl){
        try {
            tgl = new SimpleDateFormat("dd MMM yyyy", Locale.US).format(new SimpleDateFormat("yyyy-MM-dd").parse(tgl));
        }catch (Exception ex){
            System.out.println("Error Convert Tanggal: " + ex);
        }

        return tgl;
    }

    public static String waktu(String waktu){
        int output = Integer.valueOf(waktu.substring(0,1));
        waktu = String.valueOf(output) + waktu.substring(1,5);
        return waktu;
    }

    public static String formater(Float currency){
        DecimalFormat formatter = (DecimalFormat) NumberFormat.getInstance(Locale.US);
        DecimalFormatSymbols symbols = formatter.getDecimalFormatSymbols();

        symbols.setGroupingSeparator('.');
        formatter.setDecimalFormatSymbols(symbols);
        return formatter.format(currency);
    }


    public static String changeTime(String time){
        if(Integer.valueOf(time) > -1 && Integer.valueOf(time) < 9){
            time = "0"+time;
        }
        return time;
    }

    public static Integer getSaldo(Context context){
        sharedPrefManager = new SharedPrefManager(context);
        client = new OdooClient.Builder(context)
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

                                for (OdooRecord record : records) {
                                    sharedPrefManager.saveSPInt(SharedPrefManager.SP_ID_PARTNER, record.getInt("id"));
                                    sharedPrefManager.saveSPInt(SharedPrefManager.SP_COIN_USER, record.getInt("saldo"));
                                    System.out.println("SDASDSADKNSAKDNK!H@*&$IGIU@!IU@!G#@!");

                                }
                            }
                        });
                    }
                })
                .build();
        return sharedPrefManager.getSpCoinUser();
    }
}
