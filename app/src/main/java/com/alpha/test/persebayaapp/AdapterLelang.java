package com.alpha.test.persebayaapp;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
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

import oogbox.api.odoo.OdooClient;

import static com.alpha.test.persebayaapp.CommonUtils.StringToBitMap;
import static com.alpha.test.persebayaapp.CommonUtils.changeTime;
import static com.alpha.test.persebayaapp.CommonUtils.formater;
import static com.alpha.test.persebayaapp.CommonUtils.getOdooConnection;

public class AdapterLelang extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private ArrayList<lelang> dataList;
    private Context context;
    CountDownTimer countDownTimer;
    SharedPrefManager sharedPrefManager;
    OdooClient client;
    static final int VIEW_TYPE_EMPTY = 0;
    static final int VIEW_TYPE_NORMAL = 1;
    private InterfaceLelang listener;

    public AdapterLelang(ArrayList<lelang> dataList,Context context, InterfaceLelang listener) {
        this.dataList = dataList;
        this.context = context;
        this.listener = listener;
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
            client = getOdooConnection(context);
            sharedPrefManager = new SharedPrefManager(context);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder viewHolder, final int position) {
        if (getItemViewType(position) == VIEW_TYPE_NORMAL) {
            final LelangViewHolder holder = ((LelangViewHolder) viewHolder);
            if (dataList.get(position).getPemiliklelang().equalsIgnoreCase(String.valueOf(sharedPrefManager.getSpIdUser()))){
                holder.btnBidLelang.setEnabled(false);
                holder.btnBinLelang.setEnabled(false);
            }
            holder.txtNamaLelang.setText(dataList.get(position).getNamalelang());
            holder.btnBidLelang.setText(formater(Float.parseFloat(dataList.get(position).getBidlelang())));
            holder.btnBinLelang.setText(formater(Float.parseFloat(dataList.get(position).getBinlelang())));
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
                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    builder.setTitle(R.string.app_name);
                    builder.setMessage("Are You Sure to Bid This Item?");
                    builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            listener.Addbidder(dataList.get(position).getIdlelang(),dataList.get(position).getBidlelang(),"BID",context,sharedPrefManager,client);
                            dialogInterface.dismiss();
                        }
                    });
                    builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                        }
                    });
                    AlertDialog alertDialog = builder.create();
                    alertDialog.show();
                }
            });

            holder.btnBinLelang.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    builder.setTitle(R.string.app_name);
                    builder.setMessage("Do You Want to  Buy It Now?");
                    builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            listener.Addbidder(dataList.get(position).getIdlelang(),dataList.get(position).getBinlelang(),"BIN",context,sharedPrefManager,client);
                            dialogInterface.dismiss();
                        }
                    });
                    builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                        }
                    });
                    AlertDialog alertDialog = builder.create();
                    alertDialog.show();
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
        }else{
            ((CommonUtils.Emptyholder) viewHolder).onBind(position);
        }

    }



    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        switch (viewType) {
            case VIEW_TYPE_NORMAL:
                return new LelangViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.card_item_lelang, parent, false));
            default:
                return new CommonUtils.Emptyholder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_empty_view, parent, false),"OOPS!","Auction list is empty!");
        }
    }

    @Override
    public int getItemViewType(int position) {
        return (this.dataList != null && this.dataList.size() > 0) ? VIEW_TYPE_NORMAL : VIEW_TYPE_EMPTY;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return (this.dataList != null && this.dataList.size() > 0) ? dataList.size() : 1;
    }
}
