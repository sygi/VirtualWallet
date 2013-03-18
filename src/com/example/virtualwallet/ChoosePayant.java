package com.example.virtualwallet;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.RadioButton;

public class ChoosePayant extends Activity {

	private String[] peopleNames;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_choose_payant);
		peopleNames = Data.actWal.getNames();
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, peopleNames);
		AutoCompleteTextView tv = (AutoCompleteTextView) findViewById(R.id.payantName);
		tv.setAdapter(adapter);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_choose_payant, menu);
		return true;
	}
	
	public void exit(View view){
		//TODO parsowanie
		Intent res = new Intent();
		RadioButton rb = (RadioButton) findViewById(R.id.radio0);
		EditText sum = (EditText) findViewById(R.id.sum);
		Double amo = 0.0;
		if (sum.getText().toString().equals("")){
			MainScreen.showDialog(getString(R.string.no_amount), this);
			return;
		}
		amo = Double.valueOf(sum.getText().toString());
		if (amo <= 0.0){
			MainScreen.showDialog(getString(R.string.negative_value), this);
			return;
		}
		
		if (amo >= 10e9){
			MainScreen.showDialog(getString(R.string.joking), this);
			return;
		}
		
		if (rb.isChecked()){ //placi osoba
			//TODO - sprawdzic, ze jeszcze nie placila = ?
			AutoCompleteTextView tv = (AutoCompleteTextView) findViewById(R.id.payantName);
			boolean found = false;
			for(String s : peopleNames){
				if (s.equals(tv.getText().toString()))
					found = true;
			}
			if (!found){
				MainScreen.showDialog(getString(R.string.no_such_person), this);
				return;
			}
			res.putExtra("name", tv.getText().toString()); //wazne! bez .toString() nie dziala - problem z konwersja typow
			Log.d("sygi", "wybrano osobe " + tv.getText());
		} else {
			Log.d("sygi", "wybrano placenie z portfela");
			res.putExtra("name", "wallet");
			if (amo > -Data.actWal.people.get(0).paid){
				MainScreen.showDialog(getString(R.string.too_much), this);
				return;
			}
		}
		
		res.putExtra("amount", amo);
		
		setResult(RESULT_OK, res);
		finish();
	}

}
