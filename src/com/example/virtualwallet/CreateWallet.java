package com.example.virtualwallet;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
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
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
//w tej klasie nie ustawilem parentClass, nie wiem, czy to cos daje?
public class CreateWallet extends Activity {

	//TODO zrobic tak, zeby ok bylo na samym dole, ale zeby sie nie scrollowalo
	Wallet wal;
	TableLayout personList;
	private int personIterator = 10004;
	public final static int CREATE_PERSON = 12;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_create_wallet);
		wal = new Wallet();
		//personList = (RelativeLayout) findViewById(R.id.person_name_layout);
		personList = (TableLayout) findViewById(R.id.peopleList);
		/*String[] currencies = {"PLN", "USD", "EUR"};
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, currencies);*/
		Spinner spin = (Spinner) findViewById(R.id.spinner1);
		SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
		spin.setSelection(0);
		for(int i = 0; i < spin.getCount(); i++){
			if (spin.getItemAtPosition(i).equals(sp.getString("pref_act_base_cur", "PLN")))
				spin.setSelection(i);
		} //to chyba daloby sie zrobic ladniej, ustawiajac jakies wartosci w settings
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
		EditText et = (EditText) findViewById(R.id.wallet_name);
		
		if (et.getText().toString().equals("")){
			MainScreen.showDialog(getString(R.string.no_wallet_name), this);
			return;
		}
		wal.setName(et.getText().toString());
		Spinner spin = (Spinner) findViewById(R.id.spinner1);
		wal.currencyNum = spin.getSelectedItemPosition(); 
		Data.wallet.add(wal);
		Data.actWal = wal;
		
		Intent i = new Intent(this, WalletScreen.class);
		startActivity(i);
		finish();
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
					wal.removePerson(nm);
					personList.removeView(findViewById(20000 + id));
				}
			});
			row.addView(remove);
			
			personList.addView(row, lp);
			wal.addPerson(new Person(data.getStringExtra("name"), data.getStringExtra("mail")));
			personIterator++;
		}
	}


}
