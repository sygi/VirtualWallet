package com.example.virtualwallet;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;

public class CreateTransaction extends Activity {

	private static final int CHOOSE_PAYANT = 17; 
	private static final int CHOOSE_SOME = 10;
	Transaction trans;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_create_transaction);
		trans = new Transaction();
		actSubs = new ArrayList<String>();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_create_transaction, menu);
		return true;
	}
	
	public void exit(View view){
		EditText dsc = (EditText) findViewById(R.id.trans_desc);
		if (dsc.getText().toString().equals("")){
			MainScreen.showDialog(getString(R.string.enter_desc), this);
			return;
		}
		if (trans.charge.size() == 0){
			MainScreen.showDialog(getString(R.string.no_payants), this);
			return;
		}
		RadioButton rb3 = (RadioButton) findViewById(R.id.radioButton3);
		if (rb3.isChecked() && actSubs.size() == 0){
			MainScreen.showDialog(getString(R.string.at_least_one), this);
			return;
		}
		trans.desc = dsc.getText().toString();
		Log.d("sygi", "ustawiam koszty");
		Double cost = 0.0;
		for(Fee f : trans.charge){
			cost += f.paid; //zakladam, ze tu na razie jest tylko to, co ludzie zaplacili
		}
		RadioButton rb = (RadioButton) findViewById(R.id.radioButton1);
		if (rb.isChecked()){
			//wszyscy
			cost /= (Data.actWal.people.size()-1); //na osobe, zakladam, ze jest wirtualny portfel
			
			for(Person p : Data.actWal.people){
				if (!p.name.equals("wallet"))
					trans.charge.add(new Fee(p, -cost));
			}
		} else {
			RadioButton rb2 = (RadioButton) findViewById(R.id.radioButton2);
			if (rb2.isChecked()){
				//do portfela
				trans.charge.add(new Fee(Data.actWal.people.get(0), -cost)); //0 - wallet, zakladam ze jest ciagle niedodatni
			} else {
				//TODO - pewien zbior osob
				cost /= actSubs.size();
				for (String s : actSubs){
					try {
						Person p = Data.actWal.findPerson(s);
						trans.charge.add(new Fee(p, -cost));
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				
			}
		}
		
		Log.d("sygi", "dodaje transakcje do porftela");
		//TODO zapisywanie stanu do portfela (Data.actWal)
		Data.actWal.addTransaction(trans);
		
		for(Fee a : trans.charge){
			a.who.paid += a.paid;
		}
		
		setResult(RESULT_OK);
		finish();
	}
	
	public void addPayant(View view){
		Intent i = new Intent(this, ChoosePayant.class);
		startActivityForResult(i, CHOOSE_PAYANT);
	}
	
	protected void onActivityResult(int requestCode, int resultCode, Intent data){
		if (resultCode == RESULT_CANCELED){ 
			//TODO zrobic cos, gdy ktos poprosi o subset, a potem zrezygnuje
			if (requestCode == CHOOSE_SOME){
			}
			return;
		}
		if (requestCode == CHOOSE_PAYANT){
			String name = data.getStringExtra("name");
			Log.d("sygi", "podano osobe " + name);
			Double amount = Double.valueOf(data.getDoubleExtra("amount", 0.0));
			Person p = null;
			try {
				p = Data.actWal.findPerson(name);
			} catch (Exception e) {
				Log.d("sygi", "nie ma takiej osoby " + name);
				e.printStackTrace();
			}
			Fee some = new Fee(p, amount);
			trans.charge.add(some);
			TextView tv = (TextView) findViewById(R.id.payment_list);
			tv.setText(tv.getText() + some.toString());
			return;
		}
		if (requestCode == CHOOSE_SOME){
			Log.d("sygi", "wybrano podzbior osob");
			actSubs = new ArrayList<String>();
			int cnt = data.getIntExtra("peopleCount", 0);
			for(int i = 0; i < cnt; i++){
				actSubs.add(data.getStringExtra("p" + i));
			}
			return;
		}
	}
	
	private ArrayList<String> actSubs;
	public void some(View view){
		Intent i = new Intent(this, ChooseSubset.class);
		if (actSubs != null){
			i.putExtra("peopleCount", actSubs.size());
			for(int j = 0; j < actSubs.size(); j++){
				i.putExtra("p" + j, actSubs.get(j));
			}
		}
		startActivityForResult(i, CHOOSE_SOME);
	}
}
