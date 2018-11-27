package com.example.nerita_hendra.i_fans;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class AdapterLelang extends RecyclerView.Adapter<AdapterLelang.LelangViewHolder> {

    private ArrayList<lelang> dataList;
    CountDownTimer countDownTimer;

    public AdapterLelang(ArrayList<lelang> dataList) {
        this.dataList = dataList;
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
        }
    }

    @Override
    public void onBindViewHolder(@NonNull final LelangViewHolder holder, int position) {
        holder.txtNamaLelang.setText(dataList.get(position).getNamalelang());
        holder.btnBidLelang.setText(dataList.get(position).getBidlelang());
        holder.btnBinLelang.setText(dataList.get(position).getBinlelang());
        holder.imageLelang.setImageBitmap(StringToBitMap(dataList.get(position).getLelangimage()));

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
                //txtViewDays.setText(daysLeft);
//                Log.d("daysLeft",daysLeft);

                String hoursLeft = changeTime(String.format("%d", (serverUptimeSeconds % 86400) / 3600));
                //txtViewHours.setText(hoursLeft);
//                Log.d("hoursLeft",hoursLeft);

                String minutesLeft = changeTime(String.format("%d", ((serverUptimeSeconds % 86400) % 3600) / 60));
                //txtViewMinutes.setText(minutesLeft);
//                Log.d("minutesLeft",minutesLeft);

                String secondsLeft = changeTime(String.format("%d", ((serverUptimeSeconds % 86400) % 3600) % 60));
                //txtViewSecond.setText(secondsLeft);
//                Log.d("secondsLeft",secondsLeft);
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
            }
        }.start();
    }

    public String changeTime(String time){
        if(Integer.valueOf(time) > -1 && Integer.valueOf(time) < 9){
         time = "0"+time;
        }
        return time;
    }

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
