package com.alpha.test.i_fans;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

public class AdapterStoreVariant extends RecyclerView.Adapter<AdapterStoreVariant.VariantViewHolder>{
    private ArrayList<Variant> dataList;
    private List<String> itemSelected = new ArrayList<>();
    private Context context;

    public AdapterStoreVariant(ArrayList<Variant> dataList) {
        this.dataList = dataList;
    }

    public class VariantViewHolder extends RecyclerView.ViewHolder{
        public TextView txtVariant,txtStock;
        public CheckBox cbVariant;

        public VariantViewHolder(View itemView) {
            super(itemView);
            this.txtVariant = itemView.findViewById(R.id.txt_variant);
            this.txtStock = itemView.findViewById(R.id.txt_stock_variant);
            this.cbVariant = itemView.findViewById(R.id.checkBox_variant);
        }
    }

    @NonNull
    @Override
    public VariantViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_item_store_variant,parent,false);
        context = parent.getContext();
        return new VariantViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final VariantViewHolder holder, final int position) {
        final String itemName = dataList.get(position).getId();
        final SharedPrefManager sharedPrefManager = new SharedPrefManager(context);
        final Gson gson = new Gson();

        holder.txtVariant.setText(dataList.get(position).getVariant_text());
       if (Integer.valueOf(dataList.get(position).getQty()) <= 0){
           holder.txtStock.setText("Stock : 0");
           holder.txtStock.setTextColor(Color.RED);
           holder.txtVariant.setTextColor(Color.RED);
           holder.cbVariant.setEnabled(false);
           holder.cbVariant.setButtonTintList(ColorStateList.valueOf(Color.RED));
       }else{
           holder.txtStock.setText("Stock : "+dataList.get(position).getQty());
           holder.cbVariant.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
               @Override
               public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                   if (b){
                       itemSelected.add(itemName);
                       String jsonString = gson.toJson(getItemSelected());
                       sharedPrefManager.saveSPString(sharedPrefManager.SP_RETURN_FROM_RV,jsonString);
                   }else{
                       itemSelected.remove(itemName);
                       String jsonString = gson.toJson(getItemSelected());
                       sharedPrefManager.saveSPString(sharedPrefManager.SP_RETURN_FROM_RV,jsonString);
                   }
               }
           });
       }
    }

    @Override
    public int getItemCount() {
        return (dataList != null) ? dataList.size() : 0;
    }

    private List<String> getItemSelected(){
        return itemSelected;
    }
}
