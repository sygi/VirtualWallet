package com.example.virtualwallet;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;

public class CreateTransaction extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_create_transaction);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_create_transaction, menu);
		return true;
	}

}
