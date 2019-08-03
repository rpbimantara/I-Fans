package com.alpha.test.i_fans;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;

public class AdapterLiga extends ArrayAdapter<Liga> {
    private Context context;
    private ArrayList<Liga> dataList;

    public AdapterLiga(Context context,int textViewResourceId,ArrayList<Liga> dataList) {
        super(context,textViewResourceId,dataList);
        this.dataList = dataList;
        this.context = context;
    }

    @Override
    public int getCount() {
        return (dataList != null) ? dataList.size() : 0;
    }

    @Nullable
    @android.support.annotation.Nullable
    @Override
    public Liga getItem(int position) {
        return dataList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return dataList.get(position).getId();
    }

    @Override
    public View getView(int position, @Nullable @android.support.annotation.Nullable View convertView, @NonNull @android.support.annotation.NonNull ViewGroup parent) {
        TextView label = (TextView) super.getDropDownView(position, convertView, parent);
        label.setTextColor(Color.RED);
        label.setText(dataList.get(position).getNama());
        return label;

    }

    @Override
    public View getDropDownView(int position, @Nullable @android.support.annotation.Nullable View convertView, @NonNull @android.support.annotation.NonNull ViewGroup parent) {
        TextView label = (TextView) super.getDropDownView(position, convertView, parent);
        label.setTextColor(Color.BLACK);
        label.setText(dataList.get(position).getNama());

        return label;
    }
}
