package com.example.virtualwallet;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
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
	private static final int NEW_TRANS = 19;
	private static final int REMOVE_PERSON = 17;
	private static final int NEW_GROUP = 21;
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
		Log.d("sygi", "activePeople = " + Data.actWal.activePeople);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_wallet_screen, menu);
		return false;
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
			if (!p.name.equals("wallet") && p.active)
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
		startActivityForResult(i, NEW_TRANS);
	}
	
	public void addPerson(View view){
		Intent i = new Intent(this, CreatePerson.class);
		startActivityForResult(i, NEW_PERSON);
		//aktualizacja - w onResult
	}
	
	public void addGroup(View view){
		Intent i = new Intent(this, ChooseGroup.class);
		startActivityForResult(i, NEW_GROUP);
	}
	
	public void removeWallet(View view){
		 AlertDialog.Builder build = new AlertDialog.Builder(this);
			build.setMessage(getString(R.string.sure))
					.setPositiveButton("Yes",
							new DialogInterface.OnClickListener() {
								public void onClick(
										DialogInterface dialog, int id) {
									try {
										Data.removeActWallet();
										finish();
									} catch (Exception e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									}
									dialog.dismiss();
								}
							})
					.setNegativeButton("No",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog, int which) {
									dialog.dismiss();
								}
							})
					.show();
	}
	
	public void removePerson(View view){
		Intent i = new Intent(this, RemovePerson.class);
		startActivityForResult(i, REMOVE_PERSON);
	}
	
	public void showHistory(View view){
		MainScreen.showDialog(Data.actWal.getLog(), this);
	}
	
	protected void onActivityResult (int requestCode, int resultCode, Intent data){
		if (resultCode == RESULT_CANCELED){
			Log.d("sygi", "result cancelled");
			return;
		}
		
		if (requestCode == NEW_PERSON){
			for(Person p : Data.actWal.people){
				if (p.name.equals(data.getStringExtra("name"))){
					if (p.active){
						MainScreen.showDialog(getString(R.string.person_exists), this);
						return;
					} else {
						p.active = true;
						Data.actWal.activePeople++;
						p.mail = data.getStringExtra("mail");
						//wlasciwie i tak powinno byc zero (po rozliczeniu)
						p.paid = 0.0;
						actualize();
						return;
					}
				}
			}
			Data.actWal.addPerson(new Person(data.getStringExtra("name"), data.getStringExtra("mail")));
			Log.d("sygi", "dodalem osobe " + data.getStringExtra("name") + " do portfela");
			actualize();
		} else if (requestCode == NEW_TRANS){
			//synchronicznie
			actualize();
		} else if (requestCode == REMOVE_PERSON){
			actualize();
		} else if (requestCode == NEW_GROUP){
			Log.d("sygi", "wybrano grupe");
			PeopleGroup pg = null;
			for(PeopleGroup i : Data.groups){
				if (i.name.equals(data.getStringExtra("name"))){
					pg = i;
					break;
				}
			}
			if (pg == null){
				Log.d("sygi", "nie ma grupy o takiej nazwie");
				return;
			}
			int added = 0, duplicates = 0;
			boolean found;
			for(Person p : pg.people){
				found = false;
				for(Person q : Data.actWal.people){
					if (q.name.equals(p.name)){
						Log.d("sygi", q.name + " jest juz w portfelu");
						found = true;
						if (!q.active){
							q.active = true;
							Data.actWal.activePeople++;
							q.mail = data.getStringExtra("mail");
							//wlasciwie i tak powinno byc zero (po rozliczeniu)
							q.paid = 0.0;
							added++;
						} else {
							duplicates++;
							break;
						}
					}
				}
				if (!found){
					Data.actWal.addPerson(new Person(p.name, p.mail));
					added++;
				}
			}
			actualize();
			if (added == 0 || duplicates == 0)
				return; // nie pisz nic
			MainScreen.showDialog("Added " + added + " people, found " + duplicates + " duplicates.", this);
			
		}
	}

}
