package com.example.virtualwallet;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class ChooseGroup extends Activity {

	private void finish(String name){
		Intent i = new Intent();
		i.putExtra("name", name);
		setResult(RESULT_OK, i);
		finish();
	}
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_choose_group);
		String[] groupNames = new String[Data.groups.size()];
		int iter = 0;
		for(PeopleGroup pg : Data.groups){
			groupNames[iter++] = pg.name;
		}
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1, groupNames);
		final ListView lv = (ListView) findViewById(R.id.listView);
		lv.setAdapter(adapter);
		lv.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				String groupName = (String) lv.getItemAtPosition(arg2);
				Log.d("sygi", "wybrano " + groupName);
				finish(groupName);
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_choose_group, menu);
		return true;
	}

}
