package com.example.virtualwallet;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.SparseBooleanArray;
import android.view.Menu;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class RemovePerson extends Activity {

	private String[] peopleNames;
	private String report = "";
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
		Double actBest = 0.01;
		Person res = null;
		for(Person p : Data.actWal.people){
			if (p.paid < actBest){
				actBest = p.paid;
				res = p;
			}
		}
		return res;
	}
	
	private Person highestAccount(){
		Double actBest = -0.01;
		Person res = null;
		for(Person p : Data.actWal.people){
			if (p.paid > actBest){
				actBest = p.paid;
				res = p;
			}
		}
		return res;
	}
	
	private void addToRaport(String person){
		Person p = null;
		try {
			p = Data.actWal.findPerson(person);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//to chyba kiepskie uzycie wyjatkow 
		if (Math.abs(p.paid) < 0.02){
			report += p.name + " has paid the same amount as he gets, so he owes nothing\n";
			return;
		}
		//TODO - dopisac robienie zmian w portfelu przez transakcje
		if (p.paid > 0.0){
			report += p.name + " has paid " + p.paid + " too much, so he gets:\n";
			while(p.paid > 0.0){
				Person loser = biggestDept();
				if (loser.paid + p.paid > 0.0){
					report += -loser.paid + " from " + loser.name + "\n";
					p.paid += loser.paid; //loser.paid < 0.0
				} else {
					report += p.paid + " from " + loser.name + "\n";
					p.paid = 0.0;
				}
			}
			//tu potencjalnie sie moga dziac zle rzeczy
		} else {
			report += p.name + "has paid " + p.paid + " too little, so he owes:\n";
			while(p.paid < 0.0){
				Person winner = highestAccount();
				if (winner.paid + p.paid < 0.0){
					report += winner.paid + " to " + winner.name + "\n";
					p.paid += winner.paid;
				} else {
					report += -p.paid + " to " + winner.name + "\n";
					p.paid = 0.0;
				}
			}
		}
		
	}

	public void exit(View view) {
		//TODO przerzucic do to R.strings, zeby zrobic lokalizacje
		report = "Settlements report\n"; 
		ListView lv = (ListView) findViewById(R.id.listView1);
		int cnt = lv.getCount();
		int countChecked = 0;
		SparseBooleanArray checked = lv.getCheckedItemPositions();
		for(int i = 0; i < cnt; i++){
			if (checked.get(i)){
				addToRaport(peopleNames[i]);
				countChecked++;
			}
		}
		if (countChecked == 0){
			MainScreen.showDialog(getString(R.string.no_checked), this);
			return;
		}
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
	
	public void checkAll(View view){
		//TODO
	}
	
	public void uncheckAll(View view){
		//TODO
	}
	
	

}
