package com.example.virtualwallet;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;

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
		//TODO
		Intent res = new Intent();
//		res.putExtra(name, value);
		setResult(RESULT_OK, res);
		finish();
	}

}
