package com.example.virtualwallet;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.text.Html;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
/**
 * Glowny ekran aplikacji - wyswietla aktualny stan portfela, laczny koszt wyjazdu
 * umozliwia dodawanie nowych osob, rozliczanie aktualnych, etc, etc
 * @author Jakub Sygnowski
 */
public class WalletScreen extends Activity {

	private static final int NEW_PERSON = 10;
	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_wallet_screen);
		setTitle(Data.actWal.getName());
		// Show the Up button in the action bar.
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
			// Show the Up button in the action bar.
			getActionBar().setDisplayHomeAsUpEnabled(true);
		}
		actualize();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_wallet_screen, menu);
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
	
	public void actualize(){
		TextView wall = (TextView) findViewById(R.id.textView1);
		if (Data.actWal.people.get(0).paid == 0.0)
			wall.setText(getString(R.string.wallet_state) + " 0.0"); //zeby uniknac -0.0
		else
			wall.setText(getString(R.string.wallet_state) + " " + (-Data.actWal.people.get(0).paid));
		
		TextView table = (TextView) findViewById(R.id.person_table);
		String state = "";
		for(Person p : Data.actWal.people){
			state += p;
		}
		table.setText(Html.fromHtml(state));
		View layout = (View) findViewById(R.id.wallet_layout);
		layout.invalidate();
	}
	
	public void exit(View view){
		finish();
	}
	
	public void newTransaction(View view){
		Intent i = new Intent(this, CreateTransaction.class);
		startActivity(i);
		actualize();
	}
	
	public void addPerson(View view){
		Intent i = new Intent(this, CreatePerson.class);
		startActivityForResult(i, NEW_PERSON);
		//aktualizacja - w onResult
	}
	
	public void removePerson(View view){
		
	}
	
	public void showHistory(View view){
		
	}
	
	protected void onActivityResult (int requestCode, int resultCode, Intent data){
		if (resultCode == RESULT_CANCELED){
			Log.d("sygi", "result cancelled");
			return;
		}
		
		if (requestCode == NEW_PERSON){
			Data.actWal.addPerson(new Person(data.getStringExtra("name"), data.getStringExtra("mail")));
			Log.d("sygi", "dodalem osobe do portfela");
			actualize();
		}
	}

}
