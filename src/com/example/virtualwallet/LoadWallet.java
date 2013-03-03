package com.example.virtualwallet;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class LoadWallet extends Activity {

	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_load_wallet);
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
			// Show the Up button in the action bar.
			getActionBar().setDisplayHomeAsUpEnabled(true);
		}
		//tworze liste walletow
		String[] list = new String[Data.wallet.size()];
		for(int i = 0; i < Data.wallet.size(); i++){
			list[i] = Data.wallet.get(i).getName();
		}
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, 
		        android.R.layout.simple_list_item_1, list);
		ListView lv = (ListView) findViewById(R.id.listView1);
		lv.setAdapter(adapter);
		lv.setOnItemClickListener(mMessageClickedHandler); 
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_load_wallet, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			// This ID represents the Home or Up button. In the case of this
			// activity, the Up button is shown. Use NavUtils to allow users
			// to navigate up one level in the application structure. For
			// more details, see the Navigation pattern on Android Design:
			//
			// http://developer.android.com/design/patterns/navigation.html#up-vs-back
			//
			NavUtils.navigateUpFromSameTask(this);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	private OnItemClickListener mMessageClickedHandler = new OnItemClickListener() {
	    public void onItemClick(AdapterView parent, View v, int position, long id) {
	        //TODO - wybieranie potfela
	    	Log.d("sygi", position + "kliknieta");
	    }
	};

	
	
	public void exit(View view){
		finish();
	}

}
