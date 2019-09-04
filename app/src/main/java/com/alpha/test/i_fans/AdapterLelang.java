package com.alpha.test.i_fans;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import oogbox.api.odoo.OdooClient;
import oogbox.api.odoo.client.OdooVersion;
import oogbox.api.odoo.client.helper.OdooErrorException;
import oogbox.api.odoo.client.helper.data.OdooResult;
import oogbox.api.odoo.client.helper.utils.OdooValues;
import oogbox.api.odoo.client.listeners.IOdooResponse;
import oogbox.api.odoo.client.listeners.OdooConnectListener;

public class AdapterLelang extends RecyclerView.Adapter<AdapterLelang.LelangViewHolder> {

    private ArrayList<lelang> dataList;
    private Context context;
    CountDownTimer countDownTimer;
    SharedPrefManager sharedPrefManager;
    OdooClient client;

    public AdapterLelang(ArrayList<lelang> dataList,Context context) {
        this.dataList = dataList;
        this.context = context;
    }

    public class LelangViewHolder extends RecyclerView.ViewHolder{
        public TextView txtNamaLelang,txtWaktuLelang;
        public Button btnBidLelang,btnBinLelang;
        public ImageView imageLelang;

        public LelangViewHolder(View itemView) {
            super(itemView);
            this.txtNamaLelang = (TextView) itemView.findViewById(R.id.txt_namalelang);
            this.txtWaktuLelang = (TextView) itemView.findViewById(R.id.txt_waktulelang);
            this.btnBidLelang = (Button) itemView.findViewById(R.id.button_bid);
            this.btnBinLelang = (Button) itemView.findViewById(R.id.button_bin);
            this.imageLelang = (ImageView) itemView.findViewById(R.id.lelang_image);
            sharedPrefManager = new SharedPrefManager(context);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull final LelangViewHolder holder, final int position) {
        if (dataList.get(position).getPemiliklelang().equalsIgnoreCase(String.valueOf(sharedPrefManager.getSpIdUser()))){
            holder.btnBidLelang.setEnabled(false);
            holder.btnBinLelang.setEnabled(false);
        }
        holder.txtNamaLelang.setText(dataList.get(position).getNamalelang());
        holder.btnBidLelang.setText(dataList.get(position).getBidlelang());
        holder.btnBinLelang.setText(dataList.get(position).getBinlelang());
        holder.imageLelang.setImageBitmap(StringToBitMap(dataList.get(position).getLelangimage()));
        holder.imageLelang.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Intent intent = new Intent(context,LelangDetailActivity.class);
                intent.putExtra("id",dataList.get(position).getIdlelang());
                context.startActivity(intent);
                return false;
            }
        });
        holder.btnBidLelang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(view.getContext(),dataList.get(position).getBidlelang(),Toast.LENGTH_LONG).show();
//                addBidder(dataList.get(position).getIdlelang(),dataList.get(position).getBidlelang(),"BID");
            }
        });

        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        long milliseconds=0;
        Date endDate;
        try {
            endDate = formatter.parse(dataList.get(position).getWaktulelang());
            milliseconds = endDate.getTime();

        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        final long[] startTime = {System.currentTimeMillis()};
        countDownTimer = new CountDownTimer(milliseconds,1000){
            @Override
            public void onTick(long millisUntilFinished) {
                startTime[0] = startTime[0] -1;
                Long serverUptimeSeconds =
                        (millisUntilFinished - startTime[0]) / 1000;

                String daysLeft = String.format("%d", serverUptimeSeconds / 86400);
                String hoursLeft = changeTime(String.format("%d", (serverUptimeSeconds % 86400) / 3600));
                String minutesLeft = changeTime(String.format("%d", ((serverUptimeSeconds % 86400) % 3600) / 60));
                String secondsLeft = changeTime(String.format("%d", ((serverUptimeSeconds % 86400) % 3600) % 60));

                if (Integer.valueOf(daysLeft) > 0){
                    holder.txtWaktuLelang.setText(daysLeft + " Days " + hoursLeft + " : " + minutesLeft + " : " + secondsLeft);
                }else{
                    holder.txtWaktuLelang.setText(hoursLeft + " : " + minutesLeft + " : " + secondsLeft);
                }

                if (serverUptimeSeconds < 900){
                    holder.txtWaktuLelang.setTextColor(Color.RED);
                }

                if (serverUptimeSeconds < 0 ){
                    onFinish();
                }
            }

            @Override
            public void onFinish() {
                holder.txtWaktuLelang.setText("Expired");
                holder.txtWaktuLelang.setTextColor(Color.GREEN);
                holder.btnBidLelang.setEnabled(false);
                holder.btnBinLelang.setEnabled(false);
            }
        }.start();
    }

    public String changeTime(String time){
        if(Integer.valueOf(time) > -1 && Integer.valueOf(time) < 9){
         time = "0"+time;
        }
        return time;
    }
//
//    public void addBidder(final String idlelang, final String nilai, final String status){
//        client = new OdooClient.Builder(context)
//                .setHost(sharedPrefManager.getSP_Host_url())
//                .setSession(sharedPrefManager.getSpSessionId())
//                .setSynchronizedRequests(false)
//                .setConnectListener(new OdooConnectListener() {
//                    @Override
//                    public void onConnected(OdooVersion version) {
//                        OdooValues values = new OdooValues();
//                        values.put("lelang_id", idlelang);
//                        values.put("user_bid", sharedPrefManager.getSpIdUser());
//                        values.put("nilai", Integer.valueOf(nilai));
//                        values.put("keterang", status);
//
//                        client.create("persebaya.lelang.bid", values, new IOdooResponse() {
//                            @Override
//                            public void onResult(OdooResult result) {
//                                int serverId = result.getInt("result");
//                            }
//
//                            @Override
//                            public boolean onError(OdooErrorException error) {
//                                Toast.makeText(context,String.valueOf(error.getMessage()),Toast.LENGTH_LONG).show();
//                                return super.onError(error);
//                            }
//                        });
//                    }
//
//                }).build();
//    }

    @NonNull
    @Override
    public LelangViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_item_lelang,parent,false);
        return new LelangViewHolder(view);
    }

    @Override
    public int getItemCount() {
        return (dataList != null) ? dataList.size() : 0;
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
}
