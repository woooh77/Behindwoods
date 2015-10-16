package com.viki.behindwoods;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.webkit.WebView;
import android.widget.RatingBar;
import android.widget.TextView;

public class Reviews_details_activity extends ActionBarActivity{
	ProgressDialog progressDialog;
	String Url;
	String bitmap;
	RatingBar stars_image;
	WebView Text;
	String Title;
	Object image_url;
	Bitmap bmp;
	TextView title, rating_stars;
	private Toolbar toolbar;

	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.details_layout);
        initViews();
        Intent intent = getIntent();
        Title = intent.getExtras().getString("title");
        Url = intent.getExtras().getString("content_url");
        bmp = (Bitmap) intent.getParcelableExtra("bitmap_url");
		Drawable d = new BitmapDrawable(getResources(),bmp);
        getSupportActionBar().setBackgroundDrawable(d);
		stars_image = (RatingBar)findViewById(R.id.stars_image);
        Text = (WebView)findViewById(R.id.text);
        title = (TextView)findViewById(R.id.title);
        rating_stars = (TextView)findViewById(R.id.rating_number);
        progressDialog = new ProgressDialog(this); 
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER); 
        progressDialog.setMessage("Loading data ..."); 
        progressDialog.show();
        progressDialog.setCancelable(false);
        new Title().execute();
        
	}
    private class Title extends AsyncTask<Void, Void, Void>{
    	Elements metaElems1;
    	float stars = 0;

    	@Override
		protected Void doInBackground(Void... params) {
			try {
	            Document doc  = Jsoup.connect(Url).get();
         		metaElems1 = doc.select("div[class^=top_margin_10]");
         		String star = doc.select("img[src^=/cmsadmin/images/star]").first().attr("src");
         		star = star.replace("/cmsadmin/images/star-", "");
         		star = star.replace(".gif", "");
         		stars = Float.parseFloat(star);
			} catch (Exception e) {
				e.printStackTrace();
			}
			return null;
		}
    	
    	@Override
    	protected void onPostExecute(Void result){
    		String newPage = metaElems1.html();
    		Text.loadData(newPage, "text/html", "utf-8");
    		if(stars != 0) {
    			stars_image.setVisibility(View.VISIBLE);
    			stars_image.setClickable(false);
    			stars_image.setRating(stars);
    	        rating_stars.setVisibility(View.VISIBLE);
    	        rating_stars.setText("RATING - "+stars+"/5.00");
    		}else
    			title.setText(Title);
    		progressDialog.dismiss();
    	}
    }
    
    public void initViews(){
	    toolbar = (Toolbar) findViewById(R.id.toolbar);
	    toolbar.setTitleTextColor(Color.parseColor("#ecf0f1"));

	    if (toolbar != null) {
	    	toolbar.setTitle("");
	        setSupportActionBar(toolbar);
	    }
	    getSupportActionBar().setDisplayHomeAsUpEnabled(true); 
	    getSupportActionBar().setHomeButtonEnabled(true);
    }
        
    @Override
    public void onBackPressed(){
    	finish();
    	overridePendingTransition(R.anim.trans_left_in, R.anim.trans_right_out);
    }
}
