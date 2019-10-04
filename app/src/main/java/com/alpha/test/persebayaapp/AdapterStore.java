package com.alpha.test.persebayaapp;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import static com.alpha.test.persebayaapp.CommonUtils.StringToBitMap;
import static com.alpha.test.persebayaapp.CommonUtils.formater;

public class AdapterStore extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements Filterable {

    private ArrayList<Store> dataList;
    private ArrayList<Store> dataListFilter;
    static final int VIEW_TYPE_EMPTY = 0;
    static final int VIEW_TYPE_NORMAL = 1;

    public AdapterStore(ArrayList<Store> dataList) {
        this.dataListFilter = dataList;
        this.dataList = dataList;
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();
                if (charString.isEmpty()) {
                    dataListFilter = dataList;
                } else {
                    ArrayList<Store> filteredTemp = new ArrayList<>();
                    for (Store row : dataList) {
                        if (row.getNamabarang().toLowerCase().contains(charString.toLowerCase())) {
                            filteredTemp.add(row);
                        }
                    }
                    dataListFilter = filteredTemp;
                }
                FilterResults filterResults = new FilterResults();
                filterResults.values = dataListFilter;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                dataListFilter = (ArrayList<Store>) filterResults.values;
                notifyDataSetChanged();
            }
        };
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
            holder.txtNamaBarang.setText(dataListFilter.get(position).getNamabarang());
            holder.txtHargaBarang.setText(formater(Float.parseFloat(dataListFilter.get(position).getHargabarang())) );
            holder.imageStore.setImageBitmap(StringToBitMap(dataListFilter.get(position).getImageStore()));
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
        return (this.dataListFilter != null && this.dataListFilter.size() > 0) ? VIEW_TYPE_NORMAL : VIEW_TYPE_EMPTY;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return (this.dataListFilter != null && this.dataListFilter.size() > 0) ? dataListFilter.size() : 1;
    }
}
