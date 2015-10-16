package com.viki.behindwoods;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.google.android.youtube.player.YouTubeApiServiceUtil;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubeStandalonePlayer;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
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
import android.widget.Toast;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;

public class Videos_activity extends ActionBarActivity {
	TextView tv;
	static String url = "http://behindwoods.com/tamil-movies/tamil-trailers.html";
	GridView swipelistview;
	ArrayList<Highlights> data;
	Highlight_adapter adapter;
	ProgressDialog progressDialog,progressDialog1;
	private String[] mItems;
	private Toolbar toolbar;
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle drawerToggle;
    private ListView mDrawerList;
    private ArrayAdapter<String> navigationDrawerAdapter;
    int select_query = 0;
    SharedPreferences prefs;
    Bitmap bm;
    int end;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
	    Spinner spinner_toolbar = (Spinner)findViewById(R.id.spinner_toolbar);
	    spinner_toolbar.setVisibility(View.VISIBLE);
	    SpinnerAdapter spinnerAdapter = ArrayAdapter.createFromResource(this, R.array.videos_array, R.layout.custom_spinner);
	    spinner_toolbar.setAdapter(spinnerAdapter);
        initViews();
        progressDialog = new ProgressDialog(Videos_activity.this); 
        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL); 
        progressDialog.setMessage("Fetching Videos ...");
        data = new ArrayList<Highlights>();
     	adapter = new Highlight_adapter(this, R.layout.list_view_item_row, data);
		bm = BitmapFactory.decodeResource(this.getResources(), R.drawable.other_videos);

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
	    	 case "Trailers":
	    		 url = "http://behindwoods.com/tamil-movies/tamil-trailers.html";
	    		 break;
	    	 case "Other Videos":
	    		 url = "http://behindwoods.com/tamil-movies/videos.html";
	    		 break;
	    	 case "Hindi Trailers":
	    		 url = "http://behindwoods.com/hindi-movies/hindi-trailers.html";
	    		 break;
	    	 case "Short Films":
	    		 select_query = 1;
	    		 url = "http://behindwoods.com/tamil-movies-cinema-shortfilms/short-films.html";
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
            	System.out.println("Position+"+position+" end "+end);
            	if(position == end-1){
            		String URL = data.get(position).content;
                    Intent i = new Intent(Videos_activity.this, Other_videos_activity.class);
                    i.putExtra("content_url", URL);
                    startActivity(i);
                	overridePendingTransition(R.anim.trans_right_in, R.anim.trans_left_out);
            	}else{
	                progressDialog1 = new ProgressDialog(Videos_activity.this); 
	                progressDialog1.setProgressStyle(ProgressDialog.STYLE_SPINNER); 
	                progressDialog1.setMessage("Fetching Video ...");
	                progressDialog1.show();
	            	String URL = data.get(position).content;
	            	new Get_Youtube_link().execute(URL);
            	}
            }
        });
    }

    private class Title extends AsyncTask<Void, Integer, ArrayList<Highlights>>{
    	int progress = 1;
    	@Override
		protected ArrayList<Highlights> doInBackground(Void... params) {
			try {
	            Document doc  = Jsoup.connect(url).get();
	            Elements metaElems;
				if(select_query == 1)
					metaElems = doc.select("div[class^=box_128 float]");
				else
					metaElems = doc.select("div[class^=box_134 float]");
				end = metaElems.size();
				progressDialog.setMax(end);
	            for (Element metaElem : metaElems) {
	            	progress = metaElems.indexOf(metaElem);
	            	publishProgress(progress);
	                String name = metaElem.select("a").attr("title");
	                URL bitmap_url = new URL("http://behindwoods.com"+metaElem.select("img").attr("src"));
	                Bitmap bmp = BitmapFactory.decodeStream(bitmap_url.openConnection().getInputStream());
	                String content_url = "http://behindwoods.com"+metaElem.select("a").attr("href");
	                data.add(new Highlights(name, bmp, content_url));
	            }
			} catch (IOException e) {
				e.printStackTrace();
			}
			data.add(new Highlights("Other Videos", bm, url));
			end = data.size();
			return data;
		}
    	
    	@Override
    	  protected void onProgressUpdate(Integer... values) {
    	   progressDialog.setProgress(values[0]);
    	  }
    	
    	@Override
    	protected void onPostExecute(ArrayList<Highlights> data){
    		super.onPostExecute(data);
            adapter = new Highlight_adapter(Videos_activity.this, R.layout.list_view_item_row, data);
            swipelistview.setAdapter(adapter);
            progressDialog.dismiss();
    	}
    }
    
    private class Get_Youtube_link extends AsyncTask<String, Void, Void>{
    	String youtube_link = null;
    	@Override
		protected Void doInBackground(String... params) {
			try {
	            Document doc  = Jsoup.connect(params[0]).get();
            	youtube_link = doc.select("iframe[src*=www.youtube.com/embed]").first().attr("src");
            	youtube_link = youtube_link.substring(youtube_link.lastIndexOf("/")+1);
			} catch (IOException e) {
				e.printStackTrace();
			}
			return null;
		}
    	
    	@Override
    	protected void onPostExecute(Void params){
    		super.onPostExecute(params);
            progressDialog1.dismiss();
            Intent intent = null;
            prefs = getSharedPreferences("FullScreen", MODE_PRIVATE);
            if(youtube_link != null){
	            if(YouTubeApiServiceUtil.isYouTubeApiServiceAvailable(Videos_activity.this).equals(YouTubeInitializationResult.SUCCESS)){
	            	intent = YouTubeStandalonePlayer.createVideoIntent(Videos_activity.this, "AIzaSyCUcPdchcLddAiWSoXRj45IA2AsP8O7uMs", youtube_link, 0, true, prefs.getBoolean("FS", true));
	            }else
	            	intent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.youtube.com/watch?v=" + youtube_link));
	            startActivity(intent);
            }else{
            	Toast.makeText(Videos_activity.this, "Video Not Found", Toast.LENGTH_LONG).show();
            }
        }
    }
        
    private void selectItem(int position){
    	Intent i;
    	switch(position) {
        case 0:
        	i = new Intent(Videos_activity.this, News_activity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(i);
            overridePendingTransition(R.anim.trans_right_in, R.anim.trans_left_out);
            break;
        case 1:
            break;
        case 2:
        	i = new Intent(Videos_activity.this, Reviews_activity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(i);
            overridePendingTransition(R.anim.trans_right_in, R.anim.trans_left_out);
            break;
        case 3:
        	i = new Intent(Videos_activity.this, Columns_activity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(i);
            overridePendingTransition(R.anim.trans_right_in, R.anim.trans_left_out);
            break;
        case 4:
        	i = new Intent(Videos_activity.this, Contact_activity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
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
	    navigationDrawerAdapter=new ArrayAdapter<String>( Videos_activity.this, R.layout.drawer_list_item, mItems);
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

    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main, menu);
        prefs = getSharedPreferences("FullScreen", MODE_PRIVATE);
        if(prefs.getBoolean("FS", true))
        	menu.findItem(R.id.fullscreen).setChecked(false);
        else
        	menu.findItem(R.id.fullscreen).setChecked(true);
        return true;
    }
    
    public boolean onOptionsItemSelected(MenuItem item) {
    	SharedPreferences.Editor editor = getSharedPreferences("FullScreen", MODE_PRIVATE).edit();
        switch (item.getItemId()) {
            case R.id.fullscreen:
                if (item.isChecked()) {
                	editor.putBoolean("FS", true);
                    item.setChecked(false);
                }
                else {
                	editor.putBoolean("FS", false);
                    item.setChecked(true);
                }
            	editor.commit();
                return true;
            default:
                return super.onOptionsItemSelected(item);
         }
    }
    
    @Override
    public void onBackPressed(){
    	finish();
    	overridePendingTransition(R.anim.trans_top_in, R.anim.trans_bottom_out);
    }

}