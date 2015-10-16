package com.viki.behindwoods;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class NewsAdapter extends ArrayAdapter<Highlights>{

    Context context; 
    int layoutResourceId;    
    ArrayList<Highlights> data = null;
    
    public NewsAdapter(Context context, int layoutResourceId, ArrayList<Highlights> data) {
        super(context, layoutResourceId, data);
        this.layoutResourceId = layoutResourceId;
        this.context = context;
        this.data = data;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        NewsHolder holder = null;
        
        if(row == null)
        {
            LayoutInflater inflater = ((Activity)context).getLayoutInflater();
            row = inflater.inflate(layoutResourceId, parent, false);
            holder = new NewsHolder();
            holder.txtTitle = (TextView)row.findViewById(R.id.highlight_title);
            row.setTag(holder);
        }
        else
        {
            holder = (NewsHolder)row.getTag();
        }
        Highlights news = getItem(position);
        if (news != null){
        	holder.txtTitle.setText(news.highlight_title);
        }
        return row;
    }
    
    static class NewsHolder
    {
        TextView txtTitle;
    }
}