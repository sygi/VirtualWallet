package com.example.virtualwallet;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

public class ChoosePayant extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_choose_payant);
		String[] peopleNames = Data.actWal.getNames();
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
			AlertDialog.Builder build = new AlertDialog.Builder(this);
			build.setMessage(R.string.no_amount)
					.setPositiveButton("OK",
							new DialogInterface.OnClickListener() {
								public void onClick(
										DialogInterface dialog, int id) {
									dialog.cancel();
								}
							}).show();
			return;
		}
		amo = Double.valueOf(sum.getText().toString());
		if (amo <= 0.0){
			AlertDialog.Builder build = new AlertDialog.Builder(this);
			build.setMessage(R.string.negative_value)
					.setPositiveButton("OK",
							new DialogInterface.OnClickListener() {
								public void onClick(
										DialogInterface dialog, int id) {
									dialog.cancel();
								}
							}).show();
			return;
		}
		
		if (rb.isChecked()){ //placi osoba
			//TODO - sprawdzic, ze jeszcze nie placila
			AutoCompleteTextView tv = (AutoCompleteTextView) findViewById(R.id.payantName);
			res.putExtra("name", tv.getText().toString()); //wazne! bez .toString() nie dziala - problem z konwersja typow
			Log.d("sygi", "wybrano osobe " + tv.getText());
		} else {
			Log.d("sygi", "wybrano placenie z portfela");
			res.putExtra("name", "wallet");
			if (amo > -Data.actWal.people.get(0).paid){
			AlertDialog.Builder build = new AlertDialog.Builder(this);
			build.setMessage(R.string.too_much)
					.setPositiveButton("OK",
							new DialogInterface.OnClickListener() {
								public void onClick(
										DialogInterface dialog, int id) {
									dialog.cancel();
								}
							}).show();
			return;
			}
		}
		
		//TODO sprawdzic, ze wprowadzone sa sensowne (<10^9) dane
		res.putExtra("amount", amo);
		
		setResult(RESULT_OK, res);
		finish();
	}

}
