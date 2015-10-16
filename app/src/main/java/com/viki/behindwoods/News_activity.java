package com.viki.behindwoods;

import java.io.IOException;
import java.util.ArrayList;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;

public class News_activity extends ActionBarActivity {
	TextView tv;
	String url;
	ListView swipelistview;
	ArrayList<Highlights> data;
	NewsAdapter adapter;
	ProgressDialog progressDialog;
	private String[] mItems;
	private Toolbar toolbar;
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle drawerToggle;
    private ListView mDrawerList;
    private ArrayAdapter<String> navigationDrawerAdapter;
    Boolean test = false;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.news_layout);
        Intent intent = getIntent();
       	Bundle extras = intent.getExtras();
       	if(extras != null && extras.containsKey("content_url")) {
       		url = extras.getString("content_url");
       		test = true;
       	}
       	else
       		url="http://behindwoods.com/tamil-movies-cinema-news-15/cinema-news.html";
        initViews();
        progressDialog = new ProgressDialog(this);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL); 
        progressDialog.setMessage("Fetching Articles ..."); 
        progressDialog.show();
        progressDialog.setProgress(0);
        progressDialog.setMax(0);
        progressDialog.setCancelable(false);

        data = new ArrayList<Highlights>();
     	adapter = new NewsAdapter(this, R.layout.news_list_item_row, data);
        swipelistview = (ListView)findViewById(R.id.listView1);
        new Title().execute();
        swipelistview.setOnItemClickListener(new OnItemClickListener() {
            @Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
            	String URL = data.get(position).content;
            	Bitmap image_url = data.get(position).highlight_image;
            	String title = data.get(position).highlight_title;
            	Intent i;
            	if(title.startsWith("More News Stories")){
            		i = new Intent(News_activity.this, News_activity.class);
                    i.putExtra("content_url", URL);
            		finish();
            	}else{
	                i = new Intent(News_activity.this, Details_activity.class);
	                i.putExtra("content_url", URL);
	                i.putExtra("bitmap_url", image_url);
            	}

                startActivity(i); 
            	overridePendingTransition(R.anim.trans_right_in, R.anim.trans_left_out);
            }
        });
    }

    private class Title extends AsyncTask<Void, Integer, ArrayList<Highlights>>{
    	int end,progress=1;
    	@Override
		protected ArrayList<Highlights> doInBackground(Void... params) {
			try {
	             Document doc  = Jsoup.connect(url).get();
	             Elements metaElems = doc.select("a[href^=/tamil-movies-cinema-news-15/]");
	             metaElems.remove(0);
	             metaElems.remove(metaElems.last());
	             end = metaElems.size();
	             progressDialog.setMax(end);
	             for (Element metaElem : metaElems) {
	            	 progress = metaElems.indexOf(metaElem);
	            	 publishProgress(progress);
	                 String name = metaElem.attr("title");
	                 String content_url = "http://behindwoods.com"+metaElem.attr("href");
		             data.add(new Highlights(name, content_url));
	             	}
			} catch (IOException e) {
				e.printStackTrace();
			}
			return data;
		}
    	
    	@Override
  	  	protected void onProgressUpdate(Integer... values) {
    		progressDialog.setProgress(values[0]);
  	  	}
    	
    	@Override
    	protected void onPostExecute(ArrayList<Highlights> data){
            adapter = new NewsAdapter(News_activity.this, R.layout.news_list_item_row, data);
            swipelistview.setAdapter(adapter);
            progressDialog.dismiss();
    	}
    }
    
    private void selectItem(int position){
    	Intent i;
    	switch(position) {
        case 0:
            break;
        case 1:
        	i = new Intent(News_activity.this, Videos_activity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(i);
            overridePendingTransition(R.anim.trans_right_in, R.anim.trans_left_out);
            break;
        case 2:
        	i = new Intent(News_activity.this, Reviews_activity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(i);
            overridePendingTransition(R.anim.trans_right_in, R.anim.trans_left_out);
            break;
        case 3:
        	i = new Intent(News_activity.this, Columns_activity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(i);
            overridePendingTransition(R.anim.trans_right_in, R.anim.trans_left_out);
            break;
        case 4:
        	i = new Intent(News_activity.this, Contact_activity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(i);
            overridePendingTransition(R.anim.trans_right_in, R.anim.trans_left_out);
            break;
        }
    }
    
    public void initViews(){
    	mItems= getResources().getStringArray(R.array.items_array);
    	mDrawerList = (ListView) findViewById(R.id.left_drawer);
	    toolbar = (Toolbar) findViewById(R.id.toolbar);
	    toolbar.setTitleTextColor(Color.parseColor("#ecf0f1"));
	    mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
	    navigationDrawerAdapter=new ArrayAdapter<String>( News_activity.this, R.layout.drawer_list_item, mItems);
	    mDrawerList.setAdapter(navigationDrawerAdapter);
	    mDrawerList.setOnItemClickListener(new OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int pos,long id){
            		selectItem(pos);
                    mDrawerLayout.setDrawerListener( new DrawerLayout.SimpleDrawerListener(){
                            @Override
                            public void onDrawerClosed(View drawerView){
                                    super.onDrawerClosed(drawerView);
                            }
                    });
                    mDrawerLayout.closeDrawer(mDrawerList);
            }
	    });
	    if (toolbar != null) {
	    	toolbar.setTitle("Kollywood News");
	        setSupportActionBar(toolbar);
	    }
	    drawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, toolbar, R.string.drawer_open, R.string.drawer_close) {
	    	@Override
	    	public void onDrawerClosed(View drawerView) {
	    		super.onDrawerClosed(drawerView);
	    	}
	    	@Override
	        public void onDrawerOpened(View drawerView) {
	    		super.onDrawerOpened(drawerView);
	        }
	    };
	    mDrawerLayout.setDrawerListener(drawerToggle);
	    getSupportActionBar().setDisplayHomeAsUpEnabled(true); 
	    getSupportActionBar().setHomeButtonEnabled(true);
    }
    
    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        drawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        drawerToggle.onConfigurationChanged(newConfig);
    }
    
    @Override
    public void onBackPressed(){
    	if(test == true){
    		Intent i = new Intent(News_activity.this, News_activity.class);
    		finish();
    		startActivity(i);
    		overridePendingTransition(R.anim.trans_left_in, R.anim.trans_right_out);
    	}else{
	    	finish();
	    	overridePendingTransition(R.anim.trans_top_in, R.anim.trans_bottom_out);
    	}
    }
}
