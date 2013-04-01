package com.example.virtualwallet;

import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceChangeListener;
import android.preference.PreferenceActivity;
import android.preference.PreferenceCategory;
import android.preference.PreferenceManager;
import android.support.v4.app.NavUtils;
import android.view.Menu;
import android.view.MenuItem;

public class SettingsActivity extends PreferenceActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	//	setContentView(R.layout.activity_settings);
		//change to onBuild headers for API >= 11
		addPreferencesFromResource(R.xml.preferences);
		// Show the Up button in the action bar.
		ListPreference lp = (ListPreference) findPreference("pref_act_base_cur");
		final SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);

		//TODO wydzielic kod
		String defCur = sp.getString("pref_act_base_cur", "blabla");
		lp.setSummary(defCur);
		PreferenceCategory pc = (PreferenceCategory) findPreference("pref_other_cur");
		pc.removeAll();
		for(Currency c: Data.curs){
			if (!(c.cut.equals(defCur))){
				CheckBoxPreference cbp = new CheckBoxPreference(this);
				cbp.setTitle(c.name);
				cbp.setSummary("1" + c.cut + " = " + c.getOther(defCur) + defCur);
				pc.addPreference(cbp);
			}
		}
		final PreferenceActivity upper = this; 
		final PreferenceCategory prefCat = (PreferenceCategory) findPreference("pref_other_cur");
		lp.setOnPreferenceChangeListener(new OnPreferenceChangeListener() {
			@Override
			public boolean onPreferenceChange(Preference preference, Object newValue) {
//				//String defaultCur = sp.getString("pref_act_base_cur", "blabla");
				preference.setSummary((String)newValue);
				prefCat.removeAll();
				for(Currency c: Data.curs){
					if (!(c.cut.equals((String)newValue))){
						CheckBoxPreference cbp = new CheckBoxPreference(upper);
						cbp.setTitle(c.name);
						cbp.setSummary("1" + c.cut + " = " + c.getOther((String)newValue) + (String)newValue);
						prefCat.addPreference(cbp);
					}
				}			
				return true;
			}
		});
		
		/*
		sp.registerOnSharedPreferenceChangeListener(new OnSharedPreferenceChangeListener() {
			@Override
			public void onSharedPreferenceChanged(SharedPreferences sharedPreferences,
					String key) {

			}
		});*/
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_settings, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			// This ID represents the Home or Up button. In the case of this
			// activity, the Up button is shown. Use NavUtils to allow users
			// to navigate up one level in the application structure. For
			// more details, see the Navigation pattern on Android Design:
			//
			// http://developer.android.com/design/patterns/navigation.html#up-vs-back
			//
			NavUtils.navigateUpFromSameTask(this);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

}
