package com.viki.behindwoods;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class Highlight_adapter extends ArrayAdapter<Highlights>{

    Context context; 
    int layoutResourceId;    
    ArrayList<Highlights> data = null;
    
    public Highlight_adapter(Context context, int layoutResourceId, ArrayList<Highlights> data) {
        super(context, layoutResourceId, data);
        this.layoutResourceId = layoutResourceId;
        this.context = context;
        this.data = data;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        HighlightHolder holder = null;
        
        if(row == null)
        {
            LayoutInflater inflater = ((Activity)context).getLayoutInflater();
            row = inflater.inflate(layoutResourceId, parent, false);
            holder = new HighlightHolder();
            holder.txtTitle = (TextView)row.findViewById(R.id.highlight_title);
            holder.ImgImage = (ImageView)row.findViewById(R.id.highlight_image);
            row.setTag(holder);
        }
        else
        {
            holder = (HighlightHolder)row.getTag();
        }
        Highlights highlights = getItem(position);
        if (highlights != null){
        	holder.txtTitle.setText(highlights.highlight_title);
        	holder.ImgImage.setImageBitmap(highlights.highlight_image);
        }
        if (highlights.highlight_title == null)
        	holder.txtTitle.setVisibility(View.GONE);
        return row;
    }
    
	static class HighlightHolder
    {
        TextView txtTitle;
        ImageView ImgImage;
    }
}