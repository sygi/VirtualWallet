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
		AutoCompleteTextView tv = (AutoCompleteTextView) findViewById(R.id.payantName);
		res.putExtra("name", tv.getText().toString()); //wazne! bez .toString() nie dziala - problem z konwersja typow
		Log.d("sygi", "wybrano osobe " + tv.getText());
		
		EditText sum = (EditText) findViewById(R.id.sum);
		if (sum.getText().toString().equals(""))
			res.putExtra("amount", 0.0);
		else 
			res.putExtra("amount", Double.valueOf(sum.getText().toString()));
		
		setResult(RESULT_OK, res);
		finish();
	}

}
