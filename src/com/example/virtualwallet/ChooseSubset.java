package com.example.virtualwallet;

import java.io.ObjectOutputStream.PutField;
import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.SparseBooleanArray;
import android.view.Menu;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class ChooseSubset extends Activity {

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
		//niejawne zalozenie, ze wszystko jest wyczyszczone
		//TODO wyguglowac, ze tak jest :P
		Intent intent = getIntent();
		int cnt = intent.getIntExtra("peopleCount", 0);
		for(int i = 0; i < cnt; i++){
			String nm = intent.getStringExtra("p" + i);
			for(int j = 0; j < peopleNames.length; j++){
				if (peopleNames[j].equals(nm)){
					lv.setItemChecked(j, true);
				}
			}
		}
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_choose_subset, menu);
		return false;
	}
	
	public void exit(View view){
		ListView lv = (ListView) findViewById(R.id.listView1);
		int cnt = lv.getCount();
		SparseBooleanArray checked = lv.getCheckedItemPositions();
		ArrayList<String> taken = new ArrayList<String>();
		for(int i = 0; i < cnt; i++){
			if (checked.get(i)){
				taken.add(peopleNames[i]);
			}
		}
		if (taken.size() == 0){
			MainScreen.showDialog(getString(R.string.at_least_one), this);
			return;
		}
		Intent i = new Intent();
		i.putExtra("peopleCount", taken.size());
		for(int j = 0; j < taken.size(); j++){
			i.putExtra("p" + j, taken.get(j));
		}
		setResult(RESULT_OK, i);
		finish();
	}
	
	//w tym przypadku w sumie nie wiem, czy to potrzebne
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
