package com.maangalabs.transitnow;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.maangalabs.transitnow.RoutesAdapter.WeatherHolder;

public class StopAdapter extends ArrayAdapter<Routes>{

    Context context;
    int layoutResourceId;   
   Routes data[] = null;
   
    public StopAdapter(Context context, int layoutResourceId, Routes[] data) {
        super(context, layoutResourceId, data);
        this.layoutResourceId = layoutResourceId;
        this.context = context;
        this.data = data;
        
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        WeatherHolder holder = null;
       
        if(row == null)
        {
            LayoutInflater inflater = ((Activity)context).getLayoutInflater();
            row = inflater.inflate(layoutResourceId, parent, false);
           
            holder = new WeatherHolder();
           
            holder.txtTitle = (TextView)row.findViewById(R.id.txtTitle11);
            holder.txtTime = (TextView)row.findViewById(R.id.textView11);
            row.setTag(holder);
        }
        else
        {
            holder = (WeatherHolder)row.getTag();
        }
       
        Routes weather = data[position];
        holder.txtTitle.setText(weather.title);
        holder.txtTime.setText(weather.time);
       
        return row;
    }
   
    static class WeatherHolder
    {
        TextView txtTime;
        TextView txtTitle;
    }
}