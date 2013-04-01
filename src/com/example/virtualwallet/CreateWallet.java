package com.example.virtualwallet;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
//w tej klasie nie ustawilem parentClass, nie wiem, czy to cos daje?
public class CreateWallet extends Activity {

	//TODO zrobic tak, zeby ok bylo na samym dole, ale zeby sie nie scrollowalo
	Wallet wal;
	RelativeLayout personList;
	private int personIterator = 10004;
	public final static int CREATE_PERSON = 12;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_create_wallet);
		wal = new Wallet();
		personList = (RelativeLayout) findViewById(R.id.person_name_layout);
		String[] currencies = {"PLN", "USD", "EUR"};
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, currencies);
		Spinner sp = (Spinner) findViewById(R.id.spinner1);
		sp.setAdapter(adapter);
		sp.setSelection(0);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_create_wallet, menu);
		return true;
	}
	
	public void getPerson(View view){
		//startActivityForResult
		Intent i = new Intent(this, CreatePerson.class);
		startActivityForResult(i, CREATE_PERSON);
	}
	
	/**
	 * Parsuje to, co użytkownik wpisał i przechodzi (lub nie)
	 * do WalletScreen
	 * @param view
	 */
	public void forward(View view){
		Log.d("sygi", "probuje skonczyc tworzenie portfela");
		//TODO parsowanie roznych rzeczy, tworzenie portfela, etc
		EditText et = (EditText) findViewById(R.id.wallet_name);
		
		if (et.getText().toString().equals("")){
			MainScreen.showDialog(getString(R.string.no_wallet_name), this);
			return;
		}
		wal.setName(et.getText().toString());
		Data.wallet.add(wal);
		Data.actWal = wal;
		
		Intent i = new Intent(this, WalletScreen.class);
		startActivity(i);
		finish();
	}
	
	int nearestAbove(int number){
		number += 20000;
		for(int i = -1; i > -100; i--){
			if (findViewById(number + i) != null)
				return number + i;
		}
		return -1;
	}
	
	int nearestBelow(int number){
		number += 20000;
		for(int i = 1; i < 100; i++){
			if (findViewById(number + i) != null)
				return number + i;
		}
		return R.id.add_person;
	}
	
	void removePerson(int number){
		Log.d("sygi", "removing" + number);
		TextView tv = (TextView) findViewById(number);
		String nm = tv.getText().toString();
		wal.removePerson(nm);
		
		personList.removeView(findViewById(20000 + number));
		int above = nearestAbove(number), below = nearestBelow(number);
		RelativeLayout.LayoutParams belowLP = new RelativeLayout.LayoutParams
				(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
		
		View belowView = findViewById(below);
		personList.removeView(belowView);
		if (above == -1)
			belowLP.addRule(RelativeLayout.ALIGN_PARENT_TOP);
		else
			belowLP.addRule(RelativeLayout.BELOW, above);
		Log.d("sygi", "below: " + below + " above: " + above);
		personList.addView(belowView, belowLP);
	}
	
	protected void onActivityResult (int requestCode, int resultCode, Intent data){
		if (resultCode == RESULT_CANCELED){
			Log.d("sygi", "result cancelled");
			return;
		}
		
		if (requestCode == CREATE_PERSON){
			Log.d("sygi", "wybrano osobe");
			for(Person p : wal.people){
				if (p.name.equals(data.getStringExtra("name"))){
					MainScreen.showDialog(getString(R.string.person_exists), this);
					return;
				}
			}
			LinearLayout ll = new LinearLayout(this);
			ll.setId(personIterator + 20000);
			ll.setOrientation(LinearLayout.HORIZONTAL);
			RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams
					(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
			lp.addRule(RelativeLayout.CENTER_HORIZONTAL);
			
			
			TextView tv = new TextView(this);
			tv.setId(personIterator);
			tv.setText(data.getStringExtra("name"));
			LinearLayout.LayoutParams llp = new LinearLayout.LayoutParams
					(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
			
			ll.addView(tv, llp);
			Button remove = new Button(this);
			remove.setId(10000 + personIterator);
			final int id = personIterator;
			remove.setText("remove"); //TODO hard coded string
			remove.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					Log.d("sygi", "remove clicked");
					removePerson(id);
				}
			});
			ll.addView(remove, llp);
			
			//TODO - zrobic tak, zeby ladnie wygladalo :P
			//TODO przycisk do usuwania osob z listy
			
			boolean found = false;
			for(int i = -1; i>-100; i--){
				if (findViewById(personIterator + i) != null){
					lp.addRule(RelativeLayout.BELOW, 20000 + personIterator + i);
					found = true;
					break;
				}
			}
			if (!found)
				lp.addRule(RelativeLayout.ALIGN_PARENT_TOP);
			
			
			Button ok = (Button) findViewById(R.id.add_person);
			Log.d("sygi", "but" + ok.getText());
			personList.removeView(ok);
			RelativeLayout.LayoutParams buttonP = new RelativeLayout.LayoutParams
					(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
			buttonP.addRule(RelativeLayout.BELOW, 20000 + personIterator);
			personList.addView(ok, buttonP);
			
			//lp.addRule(RelativeLayout.ALIGN_PARENT_TOP);
			
			personList.addView(ll, lp);
			wal.addPerson(new Person(data.getStringExtra("name"), data.getStringExtra("mail")));
			personIterator++;
		}
	}


}
