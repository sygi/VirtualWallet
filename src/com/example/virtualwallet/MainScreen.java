package com.example.virtualwallet;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;

public class MainScreen extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main_screen);
		Data.wallet = new ArrayList<Wallet>();
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
	     Intent i = new Intent(this, CreditsActivity.class);
	     startActivity(i);
	 }
	 
	 public void settings(View view) {
	     Intent i = new Intent(this, SettingsActivity.class);
	     startActivity(i);
	 }
	 
	 public void newWallet(View view) {
	     Intent i = new Intent(this, CreateWallet.class);
	     startActivity(i);
	 }
	 
	 public void loadWallet(View view) {
		 Intent i = new Intent(this, LoadWallet.class);
		 startActivity(i);
	 }

}
