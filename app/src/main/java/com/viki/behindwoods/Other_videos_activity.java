package com.viki.behindwoods;

import java.io.IOException;
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
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class Other_videos_activity extends ActionBarActivity{
	ProgressDialog progressDialog, progressDialog1;
	String Url;
	ArrayList<Highlights> data;
	NewsAdapter adapter;
	ListView swipelistview;
	private String[] mItems;
	private Toolbar toolbar;
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle drawerToggle;
    private ListView mDrawerList;
    private ArrayAdapter<String> navigationDrawerAdapter;
    SharedPreferences prefs;
    
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.news_layout);
        initViews();
        data = new ArrayList<Highlights>();
     	adapter = new NewsAdapter(this, R.layout.news_list_item_row, data);
     	swipelistview = (ListView)findViewById(R.id.listView1);
        Intent intent = getIntent();
        Url = intent.getExtras().getString("content_url");
        progressDialog = new ProgressDialog(this); 
        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL); 
        progressDialog.setMessage("Loading List ..."); 
        progressDialog.show();
        progressDialog.setCancelable(false);
        new Title().execute();
        swipelistview.setOnItemClickListener(new OnItemClickListener() {
            @Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
                progressDialog1 = new ProgressDialog(Other_videos_activity.this); 
                progressDialog1.setProgressStyle(ProgressDialog.STYLE_SPINNER); 
                progressDialog1.setMessage("Fetching Video ...");
                progressDialog1.show();
            	String URL = data.get(position).content;
            	new Get_Youtube_link().execute(URL);
            }
        });
	}
	
	private class Title extends AsyncTask<Void, Integer, ArrayList<Highlights>>{
    	int end,progress=1;
    	@Override
		protected ArrayList<Highlights> doInBackground(Void... params) {
			try {
	             Document doc  = Jsoup.connect(Url).get();
	             Elements metaElems = doc.select("ul[class^=bullet_list red_small_arrow]");
	             end = metaElems.size();
	             progressDialog.setMax(end);
	             for (Element metaElem : metaElems) {
	            	 progress = metaElems.indexOf(metaElem);
	            	 publishProgress(progress);
	            	 for (Element metaElema : metaElem.select("li")){
		                 String name = metaElema.select("a").text();
		                 String content_url = "http://behindwoods.com"+metaElema.select("a").attr("href");
			             data.add(new Highlights(name, content_url));
	             		}
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
            adapter = new NewsAdapter(Other_videos_activity.this, R.layout.news_list_item_row, data);
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
	            if(YouTubeApiServiceUtil.isYouTubeApiServiceAvailable(Other_videos_activity.this).equals(YouTubeInitializationResult.SUCCESS)){
	            	intent = YouTubeStandalonePlayer.createVideoIntent(Other_videos_activity.this, "AIzaSyCUcPdchcLddAiWSoXRj45IA2AsP8O7uMs", youtube_link, 0, true, prefs.getBoolean("FS", true));
	            }else
	            	intent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.youtube.com/watch?v=" + youtube_link));
	            startActivity(intent);
            }else{
            	Toast.makeText(Other_videos_activity.this, "Video Not Found", Toast.LENGTH_LONG).show();
            }
        }
    }
	
	public void initViews(){
	    	mItems= getResources().getStringArray(R.array.items_array);
	    	mDrawerList = (ListView) findViewById(R.id.left_drawer);
		    toolbar = (Toolbar) findViewById(R.id.toolbar);
		    toolbar.setTitleTextColor(Color.parseColor("#ecf0f1"));
		    mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
		    navigationDrawerAdapter=new ArrayAdapter<String>( Other_videos_activity.this, R.layout.drawer_list_item, mItems);
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
		    	toolbar.setTitle("Other Videos");
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

    private void selectItem(int position){
    	Intent i;
    	switch(position) {
        case 0:
            break;
        case 1:
        	i = new Intent(Other_videos_activity.this, Videos_activity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(i);
            overridePendingTransition(R.anim.trans_right_in, R.anim.trans_left_out);
            break;
        case 2:
        	i = new Intent(Other_videos_activity.this, Reviews_activity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(i);
            overridePendingTransition(R.anim.trans_right_in, R.anim.trans_left_out);
            break;
        case 3:
        	i = new Intent(Other_videos_activity.this, Columns_activity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(i);
            overridePendingTransition(R.anim.trans_right_in, R.anim.trans_left_out);
            break;
        case 4:
        	i = new Intent(Other_videos_activity.this, Contact_activity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(i);
            overridePendingTransition(R.anim.trans_right_in, R.anim.trans_left_out);
            break;
        }
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
    	overridePendingTransition(R.anim.trans_left_in, R.anim.trans_right_out);
    }
}
