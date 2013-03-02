package com.example.virtualwallet;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
//w tej klasie nie ustawilem parentClass, nie wiem, czy to cos daje?
public class CreateWallet extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_create_wallet);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_create_wallet, menu);
		return true;
	}

}
