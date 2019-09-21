package com.alpha.test.i_fans;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

import static com.alpha.test.i_fans.CommonUtils.StringToBitMap;

public class AdapterStore extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private ArrayList<Store> dataList;
    static final int VIEW_TYPE_EMPTY = 0;
    static final int VIEW_TYPE_NORMAL = 1;

    public AdapterStore(ArrayList<Store> dataList) {
        this.dataList = dataList;
    }

    public class StoreViewHolder extends RecyclerView.ViewHolder{
        public TextView txtNamaBarang,txtHargaBarang;
        public ImageView imageStore;

        public StoreViewHolder(View itemView) {
            super(itemView);
            this.txtNamaBarang = (TextView) itemView.findViewById(R.id.txt_namaBarangStore);
            this.txtHargaBarang = (TextView) itemView.findViewById(R.id.txt_hargaBarangStore);
            this.imageStore = (ImageView) itemView.findViewById(R.id.store_image);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position) {
        if (getItemViewType(position) == VIEW_TYPE_NORMAL) {
            StoreViewHolder holder = ((StoreViewHolder) viewHolder);
            holder.txtNamaBarang.setText(dataList.get(position).getNamabarang());
            holder.txtHargaBarang.setText(dataList.get(position).getHargabarang());
            holder.imageStore.setImageBitmap(StringToBitMap(dataList.get(position).getImageStore()));
        }else{
            ((CommonUtils.Emptyholder) viewHolder).onBind(position);
        }
    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        switch (viewType) {
            case VIEW_TYPE_NORMAL:
                return new StoreViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.card_item_store, parent, false));
            default:
                return new CommonUtils.Emptyholder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_empty_view, parent, false),"OOPS!","Store list is empty!!");
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
