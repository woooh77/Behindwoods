package com.viki.behindwoods;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;

public class Columns_activity extends ActionBarActivity {
	TextView tv;
	static String url = "http://behindwoods.com/tamil-movies-cinema-column/behindwoods-columns.html";
	GridView swipelistview;
	ArrayList<Highlights> data;
	Highlight_adapter adapter;
	ProgressDialog progressDialog;
	private String[] mItems;
	private Toolbar toolbar;
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle drawerToggle;
    private ListView mDrawerList;
    private ArrayAdapter<String> navigationDrawerAdapter;
    int select_query = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
	    Spinner spinner_toolbar = (Spinner)findViewById(R.id.spinner_toolbar);
	    spinner_toolbar.setVisibility(View.VISIBLE);
	    SpinnerAdapter spinnerAdapter = ArrayAdapter.createFromResource(this, R.array.columns_array, R.layout.custom_spinner);
	    spinner_toolbar.setAdapter(spinnerAdapter);
        initViews();
        progressDialog = new ProgressDialog(Columns_activity.this); 
        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL); 
        progressDialog.setMessage("Fetching Columns ...");
        data = new ArrayList<Highlights>();
     	adapter = new Highlight_adapter(this, R.layout.list_view_item_row, data);

	    spinner_toolbar.setOnItemSelectedListener(new OnItemSelectedListener() 
	    {    
	     @Override
	     public void onItemSelected(AdapterView<?> adapter, View v, int i, long lng) { 
	         select_query = 0;
    		 data = new ArrayList<Highlights>();
	    	 progressDialog.show();
	    	 progressDialog.setProgress(0);
             progressDialog.setMax(0);
	         progressDialog.setCancelable(false);
	    	 String selecteditem =  adapter.getItemAtPosition(i).toString();
	    	 switch(selecteditem){
	    	 case "Behindwoods Columns":
	    		 url = "http://behindwoods.com/tamil-movies-cinema-column/behindwoods-columns.html";
	    		 break;
	    	 case "Visitors Columns":
	    		 url = "http://behindwoods.com/tamil-movies-cinema-fans-column/visitor-columns.html";
	    		 break;
	    	 }
	    	 new Title().execute();
	    }@Override     
	    public void onNothingSelected(AdapterView<?> parentView) 
	     {         
	     }
	    });
        swipelistview = (GridView)findViewById(R.id.listView1);
        swipelistview.setOnItemClickListener(new OnItemClickListener() {
            @Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
            	String URL = data.get(position).content;
            	String Title = data.get(position).highlight_title;
            	Bitmap image_url = data.get(position).highlight_image;
                Intent i = new Intent(Columns_activity.this, Reviews_details_activity.class);
                i.putExtra("title_url", Title);
                i.putExtra("content_url", URL);
                i.putExtra("bitmap_url", image_url);
                startActivity(i); 
            	overridePendingTransition(R.anim.trans_right_in, R.anim.trans_left_out);
            }
        });
    }

    private class Title extends AsyncTask<Void, Integer, ArrayList<Highlights>>{
    	int progress = 1, end;
    	@Override
		protected ArrayList<Highlights> doInBackground(Void... params) {
			try {
	             Document doc  = Jsoup.connect(url).get();
	             Elements metaElems;
				 metaElems = doc.select("img[src^=/tamil-movies/cinema-articles-photos/]");
				 end = metaElems.size();
				 progressDialog.setMax(end);
	             for (Element metaElem : metaElems) {
	            	 progress = metaElems.indexOf(metaElem);
	                 publishProgress(progress);
	                 String name = metaElem.parent().select("a").attr("title");
	                 URL bitmap_url = new URL("http://behindwoods.com"+metaElem.select("img").attr("src"));
	                 Bitmap bmp = BitmapFactory.decodeStream(bitmap_url.openConnection().getInputStream());
	                 String content_url = "http://behindwoods.com"+metaElem.parent().select("a").attr("href");
	                 data.add(new Highlights(name, bmp, content_url));
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
    		super.onPostExecute(data);
            adapter = new Highlight_adapter(Columns_activity.this, R.layout.list_view_item_row, data);
            swipelistview.setAdapter(adapter);
            progressDialog.dismiss();
    	}
    }
    
    private void selectItem(int position){
    	Intent i;
    	switch(position) {
        case 0:
        	i = new Intent(Columns_activity.this, News_activity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(i);
            overridePendingTransition(R.anim.trans_right_in, R.anim.trans_left_out);
            break;
        case 1:
        	i = new Intent(Columns_activity.this, Videos_activity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(i);
            overridePendingTransition(R.anim.trans_right_in, R.anim.trans_left_out);
            break;
        case 2:
        	i = new Intent(Columns_activity.this, Reviews_activity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(i);
            overridePendingTransition(R.anim.trans_right_in, R.anim.trans_left_out);
            break;
        case 3:
            break;
        case 4:
        	i = new Intent(Columns_activity.this, Contact_activity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
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
	    navigationDrawerAdapter=new ArrayAdapter<String>( Columns_activity.this, R.layout.drawer_list_item, mItems);
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
	    	toolbar.setTitle("");
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
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
    }
    
    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState){
    	super.onRestoreInstanceState(savedInstanceState);
    }
    
    @Override
    public void onBackPressed(){
    	finish();
    	overridePendingTransition(R.anim.trans_top_in, R.anim.trans_bottom_out);
    }

}