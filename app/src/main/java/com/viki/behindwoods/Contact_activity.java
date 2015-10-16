package com.viki.behindwoods;

import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class Contact_activity extends ActionBarActivity{
	private String[] mItems;
	private Toolbar toolbar;
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle drawerToggle;
    private ListView mDrawerList;
    private ArrayAdapter<String> navigationDrawerAdapter;
    
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.contact_layout);
		initViews();
        
		LinearLayout mail = (LinearLayout) findViewById(R.id.email);          
		mail.setOnClickListener(new View.OnClickListener() {               
		    public void onClick(View arg0) {
		    	Intent i = new Intent(Intent.ACTION_SEND);
		    	i.setType("message/rfc822");
		    	i.putExtra(Intent.EXTRA_EMAIL  , new String[]{"team@behindwoods.com"});
		    	try {
		    	    startActivity(Intent.createChooser(i, "Send mail..."));
		    	} catch (android.content.ActivityNotFoundException ex) {
		    	    Toast.makeText(Contact_activity.this, "There are no email clients installed.", Toast.LENGTH_SHORT).show();
		    	}
		    }
		});
		LinearLayout call = (LinearLayout) findViewById(R.id.phone);          
		call.setOnClickListener(new View.OnClickListener() {               
		    public void onClick(View arg0) {
		    	Intent i = new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", "+12017747947", null));
		    	startActivity(i);
		    }
		});
	}
    
    private void selectItem(int position){
    	Intent i;
    	switch(position) {
        case 0:
        	i = new Intent(Contact_activity.this, News_activity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(i);
            overridePendingTransition(R.anim.trans_right_in, R.anim.trans_left_out);
            break;
        case 1:
        	i = new Intent(Contact_activity.this, Videos_activity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(i);
            overridePendingTransition(R.anim.trans_right_in, R.anim.trans_left_out);
            break;
        case 2:
        	i = new Intent(Contact_activity.this, Reviews_activity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(i);
            overridePendingTransition(R.anim.trans_right_in, R.anim.trans_left_out);
            break;
        case 3:
        	i = new Intent(Contact_activity.this, Columns_activity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(i);
            overridePendingTransition(R.anim.trans_right_in, R.anim.trans_left_out);
            break;
        case 4:
            break;
        }
    }
	
    public void initViews(){
    	mItems= getResources().getStringArray(R.array.items_array);
    	mDrawerList = (ListView) findViewById(R.id.left_drawer);
	    toolbar = (Toolbar) findViewById(R.id.toolbar);
	    toolbar.setTitleTextColor(Color.parseColor("#ecf0f1"));
	    mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
	    navigationDrawerAdapter=new ArrayAdapter<String>( Contact_activity.this, R.layout.drawer_list_item, mItems);
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
	    	toolbar.setTitle("Contact Us");
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