package com.example.virtualwallet;

import java.util.ArrayList;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class Data {
	//ta klasa zawiera wszystkie dane przechowywane przez aplikacje, 
	//tak, zeby wszystkie Activities mialy do nich dostep
	//i zapisuje oraz oczytuje je z bazy danych
	public static ArrayList<Wallet> wallet;
	public static Wallet actWal;
	public static DBHelper DBAccess;
	static String readFromDatabase(){ 
		//String tymczasowo, zeby wypisywac wydruki kontrolne w glownym oknie
		SQLiteDatabase db = DBAccess.getReadableDatabase();
		String projW[] = {DBSchema.Wallet._ID, DBSchema.Wallet.COLUMN_NAME_NAME, DBSchema.Wallet.COLUMN_NAME_CREATION_DATE};
		Cursor c = db.query(DBSchema.Wallet.TABLE_NAME, projW, null, null, null, null, null);
		String res = "WALLET:\n";
		c.moveToFirst();
		while (!c.isAfterLast()){
			res += c.getInt(c.getColumnIndex(DBSchema.Wallet._ID)) + " ";
			res += c.getString(c.getColumnIndex(DBSchema.Wallet.COLUMN_NAME_NAME)) + " ";
			res += c.getInt(c.getColumnIndex(DBSchema.Wallet.COLUMN_NAME_CREATION_DATE)) + "\n";
			c.moveToNext();
		}
		return res;
	}
	static void saveToDatabase(){
		SQLiteDatabase db = DBAccess.getWritableDatabase();
		//wyczyscic informacje z BD
		db.delete(DBSchema.Fee.TABLE_NAME, null, null);
		db.delete(DBSchema.Transaction.TABLE_NAME, null, null);
		db.delete(DBSchema.Person.TABLE_NAME, null, null);
		db.delete(DBSchema.Wallet.TABLE_NAME, null, null);
		for(Wallet w : wallet){
			ContentValues val = new ContentValues();
			val.put(DBSchema.Wallet.COLUMN_NAME_NAME, w.getName());
			val.put(DBSchema.Wallet.COLUMN_NAME_CREATION_DATE, w.creationTime.getTime()/1000);
			db.insert(DBSchema.Wallet.TABLE_NAME, null, val);
			//czyzby problem z synchronizacja?
			//zalozenie - jeden portfel o danej nazwie
			
			String[] proj = {DBSchema.Wallet._ID};
			Cursor c = db.query(
					DBSchema.Wallet.TABLE_NAME,
					proj,
					DBSchema.Wallet.COLUMN_NAME_NAME + " = " + w.getName(),
					null, null, null, null);
			c.moveToFirst();
			int idW = c.getInt(c.getColumnIndex(DBSchema.Wallet._ID));
			
			for(Person p : w.people){
				val = new ContentValues();
				val.put(DBSchema.Person.COLUMN_NAME_NAME, p.name);
				val.put(DBSchema.Person.COLUMN_NAME_MAIL, p.mail);
				val.put(DBSchema.Person.COLUMN_NAME_PAID, p.paid);
				val.put(DBSchema.Person.COLUMN_NAME_WALLET_ID, idW);
				db.insert(DBSchema.Person.TABLE_NAME, null, val);
			}
		}
		
	}
}
