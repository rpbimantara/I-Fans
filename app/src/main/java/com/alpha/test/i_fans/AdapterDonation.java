package com.alpha.test.i_fans;

import android.graphics.Color;
import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import static com.alpha.test.i_fans.CommonUtils.changeTime;
import static com.alpha.test.i_fans.CommonUtils.formater;

public class AdapterDonation extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private ArrayList<Donation> dataList;
    static final int VIEW_TYPE_EMPTY = 0;
    static final int VIEW_TYPE_NORMAL = 1;

    public AdapterDonation(ArrayList<Donation> dataList) {
        this.dataList = dataList;
    }

    public class DonationViewHolder extends RecyclerView.ViewHolder{
        public TextView txt_name,txt_pemilik,txt_collected,txt_time;
        public ImageView img_item;
        public DonationViewHolder(View itemView) {
            super(itemView);
            img_item = itemView.findViewById(R.id.donasi_image);
            txt_name = itemView.findViewById(R.id.txt_namadonasi);
            txt_pemilik = itemView.findViewById(R.id.txt_nama_pembuat_donasi);
            txt_collected = itemView.findViewById(R.id.txt_donasi_terkumpul);
            txt_time = itemView.findViewById(R.id.txt_donasi_sisa_hari);
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {switch (viewType) {
        case VIEW_TYPE_NORMAL:
            return new DonationViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.card_item_donasi, parent, false));
        default:
            return new CommonUtils.Emptyholder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_empty_view, parent, false),"OOPS!","Donation list is empty!");
    }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
        if (getItemViewType(i) == VIEW_TYPE_NORMAL) {
            final DonationViewHolder holder = ((DonationViewHolder) viewHolder);
            holder.img_item.setImageBitmap(CommonUtils.StringToBitMap(dataList.get(i).getImageDonation()));
            holder.txt_name.setText(dataList.get(i).getNamaDonation());
            holder.txt_pemilik.setText(dataList.get(i).getPemilikDonation());
            holder.txt_collected.setText(formater(Float.parseFloat(dataList.get(i).getTerkumpulDonation())));

            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

            long milliseconds=0;
            Date endDate;
            try {
                endDate = formatter.parse(dataList.get(i).getWaktuDonation());
                milliseconds = endDate.getTime();

            } catch (ParseException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            final long[] startTime = {System.currentTimeMillis()};
            CountDownTimer countDownTimer = new CountDownTimer(milliseconds, 1000) {
                @Override
                public void onTick(long millisUntilFinished) {
                    startTime[0] = startTime[0] - 1;
                    Long serverUptimeSeconds =
                            (millisUntilFinished - startTime[0]) / 1000;

                    String daysLeft = String.format("%d", serverUptimeSeconds / 86400);
                    String hoursLeft = changeTime(String.format("%d", (serverUptimeSeconds % 86400) / 3600));
                    String minutesLeft = changeTime(String.format("%d", ((serverUptimeSeconds % 86400) % 3600) / 60));
                    String secondsLeft = changeTime(String.format("%d", ((serverUptimeSeconds % 86400) % 3600) % 60));

                    if (Integer.valueOf(daysLeft) > 0) {
                        holder.txt_time.setText(daysLeft + " Days ");
                    } else {
                        holder.txt_time.setText(hoursLeft + " : " + minutesLeft + " : " + secondsLeft);
                    }

                    if (serverUptimeSeconds < 900) {
                        holder.txt_time.setTextColor(Color.RED);
                    }

                    if (serverUptimeSeconds < 0) {
                        onFinish();
                    }
                }

                @Override
                public void onFinish() {
                    holder.txt_time.setText("Expired");
                }
            }.start();
        }else{
            ((CommonUtils.Emptyholder) viewHolder).onBind(i);
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
