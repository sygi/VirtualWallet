package com.example.virtualwallet;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.CheckedTextView;
import android.widget.ListView;

public class RemovePerson extends Activity {

	private String[] peopleNames;
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

	public void exit(View view) {
		
	}
	
	public void checkAll(View view){
		
	}
	
	public void uncheckAll(View view){
		
	}
	
	

}
