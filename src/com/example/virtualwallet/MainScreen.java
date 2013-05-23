package com.example.virtualwallet;

import java.util.ArrayList;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;

public class MainScreen extends Activity {

	public static void showDialog(String x, Activity a){
		 AlertDialog.Builder build = new AlertDialog.Builder(a);
			build.setMessage(x)
					.setPositiveButton("OK",
							new DialogInterface.OnClickListener() {
								public void onClick(
										DialogInterface dialog, int id) {
									dialog.cancel();
								}
							}).show();
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main_screen);
		Data.wallet = new ArrayList<Wallet>();
		Data.groups = new ArrayList<PeopleGroup>();
		Data.DBAccess = new DBHelper(this); 
		Data.readFromDatabase();
	}
	
	@Override
	protected void onStop(){
		super.onStop();
		Log.d("sygi", "onMain stop");
		Data.saveToDatabase();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_main_screen, menu);
		return false;
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
	 
	 public void openGroups(View view){
		 Intent i = new Intent(this, GroupView.class);
		 startActivity(i);
	 }
	 
	 //DEBUG
	 public void debRead(View view){
		 showDialog(Data.readFromDatabase(), this);
	 }
	 
	 public void debWrite(View view){
		 Data.saveToDatabase();
	 }
	 
	 

}
