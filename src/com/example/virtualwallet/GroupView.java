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

public class GroupView extends Activity {

	private final int CREATE_GROUP = 23;
	private ListView lv;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_group_view);
		lv = (ListView) findViewById(R.id.listView1);
		actualize();
	}
	
	private void actualize(){
		String[] groupNames = new String[Data.groups.size()];
		int iter = 0;
		for(PeopleGroup pg : Data.groups){
			groupNames[iter++] = pg.name;
		}
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1, groupNames);
		
		lv.setAdapter(adapter);
		lv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				String groupName = (String) lv.getItemAtPosition(arg2);
				Log.d("sygi", "kliknieto " + groupName);
				//TODO edytowanie grup
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_group_view, menu);
		return true;
	}
	
	public void newGroup(View view){
		Intent i = new Intent(this, CreateGroup.class);
		startActivityForResult(i, CREATE_GROUP);
	}
	
	protected void onActivityResult (int requestCode, int resultCode, Intent data){
		if (resultCode == RESULT_CANCELED){
			Log.d("sygi", "anulowano tworzenie grupy");
			return;
		}
		if (requestCode == CREATE_GROUP){
			Log.d("sygi", "utworzono grupe");
			actualize();
		}
	}
}
