package com.example.virtualwallet;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;

/**
 * Umozliwia tworzenie nowych osob
 * osoba ma tylko dwie dane - nazwe (imie i nazwisko)
 * oraz mail (opcjonalny)
 * 
 * @author Jakub Sygnowski
 *
 */
public class CreatePerson extends Activity {

	static final int PICK_CONTACT = 11;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_create_person);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_create_person, menu);
		return true;
	}

	public void send(View view){
		Intent i = new Intent();
		EditText nm = (EditText) findViewById(R.id.editText1);
		EditText ml = (EditText) findViewById(R.id.editText2);
		//TODO zabezpieczyc sie przed pustym nm.getText().toString()
		//TODO i przed osoba o imieniu 'wallet'
		//TODO i przed wieloma osobami o tym samym imieniu
		i.putExtra("name", nm.getText().toString());
		i.putExtra("mail", ml.getText().toString());
		setResult(RESULT_OK, i);
		finish();
	}
	
	public void contacts(View view){
		Intent pickContact = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
		//czy wybierac tylko tych z e-mailem?
		startActivityForResult(pickContact, PICK_CONTACT);
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data){
		if (resultCode == RESULT_CANCELED){
			return;
		} 
		if (requestCode == PICK_CONTACT){
			Log.d("sygi", "pick contact - odebralem jakies dane");
			Uri contactUri = data.getData();
			
			String[] projection = {ContactsContract.Contacts.DISPLAY_NAME, ContactsContract.Contacts._ID};
			Cursor cr = getContentResolver().query(contactUri, projection, null, null, null);
			
			cr.moveToFirst();
			String name = cr.getString(
					cr.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
			String id = cr.getString(
					cr.getColumnIndex(ContactsContract.Contacts._ID));
			Cursor mail = getContentResolver().query(
					ContactsContract.CommonDataKinds.Email.CONTENT_URI, null,
					ContactsContract.CommonDataKinds.Email.CONTACT_ID + " = " + id, null, null);
			
			if (mail.getCount() != 0){
				mail.moveToFirst();
				String email = mail.getString(
					mail.getColumnIndex(ContactsContract.CommonDataKinds.Email.ADDRESS));
				EditText editEmail = (EditText) findViewById(R.id.editText2);
				editEmail.setText(email);
			}
			
				EditText editName = (EditText) findViewById(R.id.editText1);
				editName.setText(name);
		}
	}
}
