package com.viki.behindwoods;

import android.graphics.Bitmap;

public class Highlights {
    public String highlight_title;
    public Bitmap highlight_image;
    public String content;
    
    public Highlights(){
        super();
    }
    
    public Highlights(String title, String content) {
    	super();
    	this.highlight_title = title;
    	this.content = content;
    	this.highlight_image = null;
    }
    public Highlights(String title, Bitmap image, String content) {
        super();
        this.highlight_title = title;
        this.highlight_image = image;
        this.content = content;
    }
}