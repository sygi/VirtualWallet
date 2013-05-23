package com.example.virtualwallet;

import java.util.ArrayList;

import android.app.Activity;
import android.os.Bundle;
import android.provider.Contacts.People;
import android.view.Menu;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

public class PersonalSettlement extends Activity {

	private ArrayList<String> peopleNames;
	private Spinner peopleSpinner1, peopleSpinner2;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_personal_settlement);
		for(Wallet w : Data.wallet){
			for(String p : w.getNames()){
				if (!peopleNames.contains(p)){
					peopleNames.add(p);
				}
			}
		}
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, peopleNames);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		peopleSpinner1 = (Spinner) findViewById(R.id.spinner1);
		peopleSpinner1.setAdapter(adapter);
		peopleSpinner2 = (Spinner) findViewById(R.id.spinner1);
		peopleSpinner2.setAdapter(adapter);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.personal_settlement, menu);
		return false;
	}
	
	public void exit(View view){
		//TODO does nothing
	}

}
