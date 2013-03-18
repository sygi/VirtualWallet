package com.example.virtualwallet;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
//w tej klasie nie ustawilem parentClass, nie wiem, czy to cos daje?
public class CreateWallet extends Activity {

	//TODO zrobic tak, zeby ok bylo na samym dole, ale zeby sie nie scrollowalo
	Wallet wal;
	RelativeLayout personList;
	public final static int CREATE_PERSON = 12;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_create_wallet);
		wal = new Wallet();
		personList = (RelativeLayout) findViewById(R.id.person_name_layout);
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
		
		//TODO dialog w przypadku pustej nazwy portfela
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
			TextView tv = new TextView(this);
			tv.setId(10004 + wal.personCount());
			tv.setText(data.getStringExtra("name"));
			RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams
					(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
			
			//TODO - zrobic tak, zeby dzialalo :P
			//TODO przycisk do usuwania osob z listy
			if (wal.personCount() == 0){
				lp.addRule(RelativeLayout.ALIGN_PARENT_TOP);
			} else {
				lp.addRule(RelativeLayout.BELOW, 10004 + wal.personCount()-1);
			}
			
			Button ok = (Button) findViewById(R.id.add_person);
			Log.d("sygi", "but" + ok.getText());
			personList.removeView(ok);
			RelativeLayout.LayoutParams buttonP = new RelativeLayout.LayoutParams
					(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
			buttonP.addRule(RelativeLayout.BELOW, 10004 + wal.personCount());
			personList.addView(ok, buttonP);
			
			//lp.addRule(RelativeLayout.ALIGN_PARENT_TOP);
			
			personList.addView(tv, lp);
			wal.addPerson(new Person(data.getStringExtra("name"), data.getStringExtra("mail")));
		}
	}


}
