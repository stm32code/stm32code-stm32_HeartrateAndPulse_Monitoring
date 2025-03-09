package com.example.healthmanagement.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;


import com.example.healthmanagement.R;

import java.util.List;

public class MyAdapter extends ArrayAdapter<ContentItem> {


    public MyAdapter(Context context, List<ContentItem> objects) {
        super(context, 0, objects);


    }

    @SuppressLint("InflateParams")
    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {

        ContentItem c = getItem(position);
        ViewHolder holder;
        holder = new ViewHolder();
        convertView = LayoutInflater.from(getContext()).inflate(R.layout.listview_item, null);
        holder.tvName = convertView.findViewById(R.id.tvName);
        holder.tvDesc = convertView.findViewById(R.id.tvDesc);
        convertView.setTag(holder);
        holder.tvDesc.setTextSize(15);
        holder.tvName.setTextSize(20);
        holder.tvName.setText(c != null ? c.name : null);
        holder.tvDesc.setText(c != null ? c.desc : null);
        return convertView;
    }

    private class ViewHolder {
        TextView tvName, tvDesc;
    }
}
