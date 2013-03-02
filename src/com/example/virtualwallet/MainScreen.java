package com.example.virtualwallet;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.View;

public class MainScreen extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main_screen);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_main_screen, menu);
		return true;
	}
	
	 public void exit(View view) {
	     finish();
	 }
	 
	 public void authors(View view) {
	     
	 }
	 
	 public void settings(View view) {
	     
	 }
	 
	 public void newWallet(View view) {
	     
	 }
	 
	 public void loadWallet(View view) {

	 }

}
