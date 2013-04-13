package com.example.virtualwallet;

import java.util.ArrayList;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.Menu;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class RemovePerson extends Activity {

	private String[] peopleNames;
	private String report = "";
	private String mail = "";
	private ArrayList<String> addresses = new ArrayList<String>();
	private Transaction t;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_remove_person);
		peopleNames = Data.actWal.getNames();
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, 
		        android.R.layout.simple_list_item_multiple_choice, peopleNames);
		ListView lv = (ListView) findViewById(R.id.listView1);
		lv.setAdapter(adapter);
		lv.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
		//lv.setOnItemClickListener(mMessageClickedHandler); 
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_remove_person, menu);
		return true;
	}
	private Person biggestDept(){
		Log.d("sygi", "biggestDept ");
		Double actBest = 0.01;
		Person res = null;
		for(Person p : Data.actWal.people){
			if ((p.active || p.name.equals("wallet")) && p.paid < actBest){
				actBest = p.paid;
				res = p;
			}
		}
		Log.d("sygi", "ma " + res.name);
		return res;
	}
	
	private void placeToMail(Person pers){
		if (pers.mail.equals(""))
			return;
		if (!addresses.contains(pers.mail)){
			addresses.add(pers.mail);
		}
	}
	
	private Person highestAccount(){
		Log.d("sygi", "highestAccount ");
		Double actBest = -0.01;
		Person res = null;
		for(Person p : Data.actWal.people){
			if (p.active && p.paid > actBest){
				actBest = p.paid;
				res = p;
			}
		}
		Log.d("sygi", "ma " + res.name);
		return res;
	}
	/**
	 * @return czy dodalem ta osobe do raportu 
	 */
	private boolean addToRaport(String person){
		Person p = null;
		try {
			p = Data.actWal.findPerson(person);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//to chyba kiepskie uzycie wyjatkow 
		if (Math.abs(p.paid) < 0.02){
			report += p.name + " has paid the same amount as (s)he gets, so (s)he owes nothing\n";
			return false;
		}
		mail += "Settlement of " + p.name + ":\n======================\n";
		placeToMail(p);
		//TODO - poprawic -0.0 u osób
		//TODO zrobic to ladniej (report i mail)
		if (p.paid > 0.0){
			report += p.name + " has paid " + String.format("%.2f", p.paid) + " too much, so (s)he gets:\n";
			mail += p.name + " has paid " + String.format("%.2f", p.paid) + " too much, so (s)he gets:\n";
			while(p.paid > 0.01){
				Person loser = biggestDept();
				if (loser.paid + p.paid > 0.0){
					report += String.format("%.2f", -loser.paid) + " from " + loser.name + "\n";
					mail += String.format("%.2f", -loser.paid) + " from " + loser.name + "\n";
					Fee f = new Fee(p, loser.paid);
					t.charge.add(f);
					f = new Fee(loser, -loser.paid);
					t.charge.add(f);
					p.paid += loser.paid; //loser.paid < 0.0
					loser.paid = 0.0;
				} else {
					Fee f = new Fee(p, -p.paid);
					t.charge.add(f);
					f = new Fee(loser, p.paid);
					t.charge.add(f);
					report += String.format("%.2f", p.paid) + " from " + loser.name + "\n";
					mail += String.format("%.2f", p.paid) + " from " + loser.name + "\n";
					loser.paid += p.paid;
					p.paid = 0.0;
				}
				placeToMail(loser);
			}
			//tu potencjalnie sie moga dziac zle rzeczy
		} else {
			report += p.name + " has paid " + String.format("%.2f", p.paid) + " too little, so (s)he owes:\n";
			mail += p.name + " has paid " + String.format("%.2f", p.paid) + " too little, so (s)he owes:\n";
			while(p.paid < -0.01){
				Person winner = highestAccount();
				Log.d("sygi", "winner ma " + winner.paid + ", a osoba " + p.paid);
				if (winner.paid + p.paid < 0.0){
					Log.d("sygi", "to za malo");
					Fee f = new Fee(winner, -winner.paid);
					t.charge.add(f);
					f = new Fee(p, winner.paid);
					t.charge.add(f);
					report += String.format("%.2f", winner.paid) + " to " + winner.name + "\n";
					mail += String.format("%.2f", winner.paid) + " to " + winner.name + "\n";
					p.paid += winner.paid;
					winner.paid = 0.0;
				} else {
					Log.d("sygi", "to wystarczy");
					report += String.format("%.2f", -p.paid) + " to " + winner.name + "\n";
					mail += String.format("%.2f", -p.paid) + " to " + winner.name + "\n";
					Fee f = new Fee(winner, p.paid);
					t.charge.add(f);
					f = new Fee(p, -p.paid);
					t.charge.add(f);
					winner.paid += p.paid;
					p.paid = 0.0;
				}
				placeToMail(winner);
			}
		}
		mail += "\n";
		return true;
	}

	public void exit(View view) throws Exception {
		//TODO przerzucic do to R.strings, zeby zrobic lokalizacje
		report = "Settlements report\n"; 
		mail = "Report:\n\n";
		addresses = new ArrayList<String>();
		ListView lv = (ListView) findViewById(R.id.listView1);
		int cnt = lv.getCount();
		int countChecked = 0;
		SparseBooleanArray checked = lv.getCheckedItemPositions();
		boolean someData = false;
		t = new Transaction("Settlement");
		//budowanie raportu i transakcji
		for(int i = 0; i < cnt; i++){
			if (checked.get(i)){
				if (addToRaport(peopleNames[i])){
					someData = true;
					mail += "\n";
				}
				countChecked++;
			}
		}
		if (countChecked == 0){
			MainScreen.showDialog(getString(R.string.no_checked), this);
			return;
		}
		if (someData)
			Data.actWal.trans.add(t);
		
		//usuwanie osob
		for(int i = 0; i < cnt; i++){
			if (checked.get(i)){
				if (!Data.actWal.removePerson(peopleNames[i])){
					throw new Exception("Problem removing people");
				}
			}
		}
		if (someData && addresses.size() > 0){
			AlertDialog.Builder build = new AlertDialog.Builder(this);
			build.setMessage(report)
					.setPositiveButton("OK",
							new DialogInterface.OnClickListener() {
								public void onClick(
										DialogInterface dialog, int id) {
									dialog.cancel();
									setResult(RESULT_OK);
									finish();
								}
							})
					.setNeutralButton("Send through mail", 
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog, int which) {
									Intent i = new Intent(Intent.ACTION_SEND);
									i.setType("message/rfc822");
									i.putExtra(Intent.EXTRA_EMAIL, addresses.toArray(new String[0]));
									i.putExtra(Intent.EXTRA_SUBJECT, "Settlement of " + Data.actWal.getName());
									i.putExtra(Intent.EXTRA_TEXT, mail);
									startActivity(Intent.createChooser(i, "Send mail..."));
									dialog.cancel();
									setResult(RESULT_OK);
									finish();
								}
							})
					.show();
		} else {
		AlertDialog.Builder build = new AlertDialog.Builder(this);
			build.setMessage(report)
					.setPositiveButton("OK",
							new DialogInterface.OnClickListener() {
								public void onClick(
										DialogInterface dialog, int id) {
									dialog.cancel();
									setResult(RESULT_OK);
									finish();
								}
							}).show();
		}
			
	}
	
	public void checkAll(View view){
		checkUncheck(true);
	}
	
	private void checkUncheck(boolean side){
		ListView lv = (ListView) findViewById(R.id.listView1);
		int cnt = lv.getCount();
		for(int i = 0; i < cnt; i++){
			lv.setItemChecked(i, side);
		}
	}
	
	public void uncheckAll(View view){
		checkUncheck(false);
	}
}
