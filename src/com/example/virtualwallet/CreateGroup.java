package com.example.virtualwallet;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

public class CreateGroup extends Activity {

	private final int CREATE_PERSON = 32;
	private PeopleGroup group;
	private int personIterator = 1;
	private TableLayout personList;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_create_group);
		group = new PeopleGroup("");
		personList = (TableLayout) findViewById(R.id.peopleList);
	}
	
	public void getPerson(View view){
		Intent i = new Intent(this, CreatePerson.class);
		startActivityForResult(i, CREATE_PERSON);
	}
	
	public void forward(View view){
		Log.d("sygi", "probuje skonczyc tworzenie grupy");
		EditText et = (EditText) findViewById(R.id.group_name);
		
		if (et.getText().toString().equals("")){
			MainScreen.showDialog(getString(R.string.no_group_name), this);
			return;
		}
		group.name = (et.getText().toString());
		Data.groups.add(group);
		setResult(RESULT_OK);
		finish();
	}
	
	protected void onActivityResult (int requestCode, int resultCode, Intent data){
		if (resultCode == RESULT_CANCELED){
			Log.d("sygi", "result cancelled");
			return;
		}
		
		if (requestCode == CREATE_PERSON){
			Log.d("sygi", "wybrano osobe");
			for(Person p : group.people){
				if (p.name.equals(data.getStringExtra("name"))){
					MainScreen.showDialog(getString(R.string.person_exists_in_group), this);
					return;
				}
			}
			
			TableRow row = new TableRow(this);
			row.setId(personIterator + 20000);
			TableRow.LayoutParams lp = new TableRow.LayoutParams
					(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT);
			
			TextView tv = new TextView(this);
			tv.setId(personIterator);
			tv.setText(data.getStringExtra("name"));
			
			row.addView(tv);
			Button remove = new Button(this);
			
			final int id = personIterator;
			remove.setText("remove"); //TODO hard coded string
			remove.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					Log.d("sygi", "remove clicked");
					TextView tv = (TextView) findViewById(id);
					String nm = tv.getText().toString();
					group.removePerson(nm);
					personList.removeView(findViewById(20000 + id));
				}
			});
			row.addView(remove);
			
			personList.addView(row, lp);
			group.people.add(new Person(data.getStringExtra("name"), data.getStringExtra("mail")));
			personIterator++;
		}
	}

}
