package com.example.virtualwallet;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
//w tej klasie nie ustawilem parentClass, nie wiem, czy to cos daje?
public class CreateWallet extends Activity {

	Wallet wal;
	RelativeLayout personList;
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
		startActivityForResult(i, Data.createPerson);
	}
	
	/**
	 * Parsuje to, co użytkownik wpisał i przechodzi (lub nie)
	 * do WalletScreen
	 * @param view
	 */
	public void forward(View view){
		//TODO parsowanie roznych rzeczy, tworzenie portfela, etc
		
		Intent i = new Intent(this, WalletScreen.class);
		startActivity(i);
		finish();
		
	}
	
	protected void onActivityResult (int requestCode, int resultCode, Intent data){
		if (resultCode == RESULT_CANCELED){
			Log.d("sygi", "result cancelled");
			return;
		}
		
		if (requestCode == Data.createPerson){
			Log.d("sygi", "wybrano osobe");
			TextView tv = new TextView(this);
			tv.setText(data.getStringExtra("name"));
			RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams
					(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
			
			//TODO - zrobic tak, zeby dzialalo :P
			//lp.addRule(RelativeLayout.ABOVE, R.id.button5);
			lp.addRule(RelativeLayout.ALIGN_PARENT_TOP);
			
			personList.addView(tv, lp);
			wal.addPerson(new Person(data.getStringExtra("name"), data.getStringExtra("mail")));
		}
	}


}
