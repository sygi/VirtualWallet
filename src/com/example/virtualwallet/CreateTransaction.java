package com.example.virtualwallet;

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
	Transaction trans;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_create_transaction);
		trans = new Transaction();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_create_transaction, menu);
		return true;
	}
	
	public void exit(View view){
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
				if (p.name != "wallet")
				trans.charge.add(new Fee(p, -cost));
			}
		} else {
			RadioButton rb2 = (RadioButton) findViewById(R.id.radioButton2);
			if (rb2.isChecked()){
				//do portfela
				trans.charge.add(new Fee(Data.actWal.people.get(0), -cost)); //0 - wallet, zakladam ze jest ciagle niedodatni
			} else {
				//TODO - pewien zbior osob
			}
		}
		
		Log.d("sygi", "dodaje transakcje do porftela");
		//TODO zapisywanie stanu do portfela (Data.actWal)
		Data.actWal.addTransaction(trans);
		
		for(Fee a : trans.charge){
			a.who.paid += a.paid;
		}
		
		EditText dsc = (EditText) findViewById(R.id.editText2);
		trans.desc = dsc.getText().toString();
		setResult(RESULT_OK);
		finish();
	}
	
	public void addPayant(View view){
		Intent i = new Intent(this, ChoosePayant.class);
		startActivityForResult(i, CHOOSE_PAYANT);
	}
	
	protected void onActivityResult(int requestCode, int resultCode, Intent data){
		if (resultCode == RESULT_CANCELED){
			return;
		}
		if (requestCode == CHOOSE_PAYANT){
			//TODO - zbiera kwote (sum) i imie (name) i dodaje do tworzonej tranzakcji odpowiednie fee
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
		}
	}
}
