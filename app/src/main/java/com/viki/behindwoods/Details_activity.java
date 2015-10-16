package com.viki.behindwoods;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.webkit.WebView;
import android.widget.TextView;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.net.URL;

public class Details_activity extends ActionBarActivity{
	ProgressDialog progressDialog;
	String Url;
	String bitmap;
	TextView Title_text;
	WebView Text;
	String title;
	URL image_url;
	Bitmap bmp;
	private Toolbar toolbar;
	
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.details_layout);
        initViews();
        Intent intent = getIntent();
        Url = intent.getExtras().getString("content_url");
        Title_text = (TextView)findViewById(R.id.title);
        Text = (WebView)findViewById(R.id.text);
        progressDialog = new ProgressDialog(this); 
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER); 
        progressDialog.setMessage("Loading News ..."); 
        progressDialog.show();
        progressDialog.setCancelable(false);
        new Title().execute();
	}
    private class Title extends AsyncTask<Void, Void, Void>{
    	Elements metaElems1;
    	@Override
		protected Void doInBackground(Void... params) {
			try {
	            Document doc  = Jsoup.connect(Url).get();
	            Elements metaElems = doc.select("img[src^=images/]");
                image_url = new URL("http://behindwoods.com/tamil-movies-cinema-news-15/"+metaElems.first().attr("src"));
         		title = metaElems.first().attr("title");
         		bmp = BitmapFactory.decodeStream(image_url.openConnection().getInputStream());
         		metaElems1 = doc.select("span[itemprop=articleBody]");
			} catch (IOException e) {
				e.printStackTrace();
			}
			return null;
		}
    	
    	@Override
    	protected void onPostExecute(Void result){
    		Title_text.setText(title);
    		Drawable d = new BitmapDrawable(getResources(),bmp);
    		getSupportActionBar().setBackgroundDrawable(d);
    		String newPage = metaElems1.html();
			Text.loadData(newPage, "text/html", "utf-8");
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
