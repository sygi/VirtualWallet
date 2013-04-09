package com.example.virtualwallet;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class Data {
	//ta klasa zawiera wszystkie dane przechowywane przez aplikacje, 
	//tak, zeby wszystkie Activities mialy do nich dostep
	//i zapisuje oraz oczytuje je z bazy danych
	public static ArrayList<Wallet> wallet;
	public static Wallet actWal;
	public static DBHelper DBAccess;
	public static Currency[] curs;
	
	public static Currency getCur(String shortName){
		for(Currency c: curs){
			if (c.cut.equals(shortName))
				return c;
		}
		return null;
	}//wlasciwie teraz tego nie uzywam
	
	static void removeActWallet() throws Exception{
		if (actWal == null){
			throw new Exception("There's no actWal");
		}
		for (Wallet w : wallet){
			if (w.equals(actWal)){
				wallet.remove(w);
				actWal = null;
				return;
			}
		}
		throw new Exception("actWal not in wallet list");
	}
	static String readFromDatabase(){ 
		Log.d("sygi", "read from database");
		//TODO zmienic, zeby naprawde to czytalo i pisalo z db
		curs = new Currency[3];
		//PLN
		HashMap<String, Double> mapa = new HashMap<String,Double>();
		mapa.put("USD", 0.3168);
		mapa.put("EUR", 0.2404);
		curs[0] = new Currency("Polish złoty", "PLN", mapa);
		mapa = new HashMap<String,Double>();
		mapa.put("PLN", 3.1564);
		mapa.put("EUR", 0.7663);
		curs[1] = new Currency("United States dollar", "USD", mapa);
		mapa = new HashMap<String,Double>();
		mapa.put("PLN", 4.1605);
		mapa.put("USD", 1.3049);
		curs[2] = new Currency("Euro", "EUR", mapa);
		
		wallet.clear();
		actWal = null;
		Map<Integer, Wallet> wallDict = new HashMap<Integer, Wallet>();
		//String tymczasowo, zeby wypisywac wydruki kontrolne w glownym oknie
		SQLiteDatabase db = DBAccess.getReadableDatabase();
		String projW[] = {DBSchema.Wallet._ID, DBSchema.Wallet.COLUMN_NAME_NAME, DBSchema.Wallet.COLUMN_NAME_CURRENCY, DBSchema.Wallet.COLUMN_NAME_CREATION_DATE};
		Cursor c = db.query(DBSchema.Wallet.TABLE_NAME, projW, null, null, null, null, null);
		String res = "WALLET:\n";
		c.moveToFirst();
		while (!c.isAfterLast()){
			Wallet w = new Wallet(c.getString(c.getColumnIndex(DBSchema.Wallet.COLUMN_NAME_NAME)));
			w.creationTime = new Date(1000 * c.getLong(c.getColumnIndex(DBSchema.Wallet.COLUMN_NAME_CREATION_DATE)));
			/*w.baseCur = null;
			for(Currency cr: curs){
				if (cr.cut.equals(c.getString(c.getColumnIndex(DBSchema.Wallet.COLUMN_NAME_CURRENCY)))){
					w.baseCur = cr;
				}
			}
			if (w.baseCur == null){
				Log.d("sygi", "blad, brak takiej waluty");
			}*/
			for(int i = 0; i < curs.length; i++){
				if (curs[i].cut.equals(c.getString(c.getColumnIndex(DBSchema.Wallet.COLUMN_NAME_CURRENCY)))){
					w.currencyNum = i; 
				}
			}
			wallet.add(w);
			wallDict.put(c.getInt(c.getColumnIndex(DBSchema.Wallet._ID)), w);
			res += c.getInt(c.getColumnIndex(DBSchema.Wallet._ID)) + " ";
			res += c.getString(c.getColumnIndex(DBSchema.Wallet.COLUMN_NAME_NAME)) + " ";
			res += c.getString(c.getColumnIndex(DBSchema.Wallet.COLUMN_NAME_CURRENCY)) + " ";
			res += c.getInt(c.getColumnIndex(DBSchema.Wallet.COLUMN_NAME_CREATION_DATE)) + "\n";
			c.moveToNext();
		}
		c.close();
		
		res += "\nPERSON:\n";
		Map<Integer, Person> perDict = new HashMap<Integer, Person>();
		String projP[] = {DBSchema.Person._ID, DBSchema.Person.COLUMN_NAME_NAME, DBSchema.Person.COLUMN_NAME_MAIL, DBSchema.Person.COLUMN_NAME_PAID, DBSchema.Person.COLUMN_NAME_ACTIVE, DBSchema.Person.COLUMN_NAME_WALLET_ID};
		c = db.query(DBSchema.Person.TABLE_NAME, projP, null, null, null, null, null);
		c.moveToFirst();
		while (!c.isAfterLast()){
			Person p = new Person(c.getString(c.getColumnIndex(DBSchema.Person.COLUMN_NAME_NAME)),
					c.getString(c.getColumnIndex(DBSchema.Person.COLUMN_NAME_MAIL)));
			if (c.getInt(c.getColumnIndex(DBSchema.Person.COLUMN_NAME_ACTIVE)) == 1){
				p.active = true;
			} else {
				p.active = false;
			}
			perDict.put(c.getInt(c.getColumnIndex(DBSchema.Person._ID)), p);
			wallDict.get(c.getInt(c.getColumnIndex(DBSchema.Person.COLUMN_NAME_WALLET_ID))).people.add(p);
			if (p.active){
				wallDict.get(c.getInt(c.getColumnIndex(DBSchema.Person.COLUMN_NAME_WALLET_ID))).activePeople++;
			}
			p.paid = c.getDouble(c.getColumnIndex(DBSchema.Person.COLUMN_NAME_PAID));
			res += c.getInt(c.getColumnIndex(DBSchema.Person._ID)) + 
					" name: " + c.getString(c.getColumnIndex(DBSchema.Person.COLUMN_NAME_NAME)) + "\n";
			res += "mail:" + c.getString(c.getColumnIndex(DBSchema.Person.COLUMN_NAME_MAIL)) + "\n";
			if (c.getInt(c.getColumnIndex(DBSchema.Person.COLUMN_NAME_ACTIVE)) == 1){
				res += "active\n";
			} else {
				res += "not active\n";
			}
			res += "paid:" + c.getDouble(c.getColumnIndex(DBSchema.Person.COLUMN_NAME_PAID)) +
					", walletId = " + c.getInt(c.getColumnIndex(DBSchema.Person.COLUMN_NAME_WALLET_ID)) + "\n";
			c.moveToNext();
		}
		c.close();
		
		res += "\nTRANSACTION\n";
		String projT[] = {DBSchema.Transaction._ID, DBSchema.Transaction.COLUMN_NAME_WHEN, DBSchema.Transaction.COLUMN_NAME_DESC, DBSchema.Transaction.COLUMN_NAME_WALLET_ID};
		c = db.query(DBSchema.Transaction.TABLE_NAME, projT, null, null, null, null, null);
		Map<Integer, Transaction> transDict = new HashMap<Integer, Transaction>();
		c.moveToFirst();
		//zalozenie - transakcja ma conajmniej jedno fee
		while (!c.isAfterLast()){
			Transaction t = new Transaction(c.getString(c.getColumnIndex(DBSchema.Transaction.COLUMN_NAME_DESC)),
					new Date(1000 * c.getLong(c.getColumnIndex(DBSchema.Transaction.COLUMN_NAME_WHEN))));
			transDict.put(c.getInt(c.getColumnIndex(DBSchema.Transaction._ID)), t);
			wallDict.get(c.getInt(c.getColumnIndex(DBSchema.Transaction.COLUMN_NAME_WALLET_ID))).trans.add(t);
			res += c.getInt(c.getColumnIndex(DBSchema.Transaction._ID)) + " ";
			res += c.getString(c.getColumnIndex(DBSchema.Transaction.COLUMN_NAME_DESC)) + " " 
			+ c.getInt(c.getColumnIndex(DBSchema.Transaction.COLUMN_NAME_WHEN)) + "\n";
			c.moveToNext();
		}
		c.close();
		
		res += "\nFEE\n";
		String projF[] = {DBSchema.Fee._ID, DBSchema.Fee.COLUMN_NAME_PERSON_ID, DBSchema.Fee.COLUMN_NAME_TRANS_ID, DBSchema.Fee.COLUMN_NAME_PAID};
		c = db.query(DBSchema.Fee.TABLE_NAME, projF, null, null, null, null, null);
		c.moveToFirst();
		while(!c.isAfterLast()){
			Fee f = new Fee(perDict.get(c.getInt(c.getColumnIndex(DBSchema.Fee.COLUMN_NAME_PERSON_ID))), 
					c.getDouble(c.getColumnIndex(DBSchema.Fee.COLUMN_NAME_PAID)));
			transDict.get(c.getInt(c.getColumnIndex(DBSchema.Fee.COLUMN_NAME_TRANS_ID))).charge.add(f);
			res += c.getInt(c.getColumnIndex(DBSchema.Fee._ID)) + " person:" + 
					c.getInt(c.getColumnIndex(DBSchema.Fee.COLUMN_NAME_PERSON_ID)) + " trans:" + 
					c.getInt(c.getColumnIndex(DBSchema.Fee.COLUMN_NAME_TRANS_ID)) + "\n";
			res += c.getDouble(c.getColumnIndex(DBSchema.Fee.COLUMN_NAME_PAID)) + "\n";
			c.moveToNext();
		}
		c.close();
		db.close();
		Log.d("sygi", res);
		return res;
	}
	static void saveToDatabase(){
		SQLiteDatabase db = DBAccess.getWritableDatabase();
		Log.d("sygi",  "saveToDatabase");
		if (actWal != null){
		Log.d("sygi", "actWal, people:");
		for(Person p : actWal.people){
			Log.d("sygi", p.name);
		}
		}
		//wyczyscic informacje z BD
		db.delete(DBSchema.Fee.TABLE_NAME, null, null);
		db.delete(DBSchema.Transaction.TABLE_NAME, null, null);
		db.delete(DBSchema.Person.TABLE_NAME, null, null);
		db.delete(DBSchema.Wallet.TABLE_NAME, null, null);
		for(Wallet w : wallet){
			ContentValues val = new ContentValues();
			val.put(DBSchema.Wallet.COLUMN_NAME_NAME, w.getName());
			val.put(DBSchema.Wallet.COLUMN_NAME_CREATION_DATE, w.creationTime.getTime()/1000);
			val.put(DBSchema.Wallet.COLUMN_NAME_CURRENCY, curs[w.currencyNum].cut);
			long walletId = db.insert(DBSchema.Wallet.TABLE_NAME, null, val);
			//czyzby problem z synchronizacja?

			for(Person p : w.people){
				val = new ContentValues();
				val.put(DBSchema.Person.COLUMN_NAME_NAME, p.name);
				val.put(DBSchema.Person.COLUMN_NAME_MAIL, p.mail);
				val.put(DBSchema.Person.COLUMN_NAME_PAID, p.paid);
				if (p.active)
					val.put(DBSchema.Person.COLUMN_NAME_ACTIVE, 1);
				else
					val.put(DBSchema.Person.COLUMN_NAME_ACTIVE, 0);
				val.put(DBSchema.Person.COLUMN_NAME_WALLET_ID, walletId);
				Log.d("sygi", "person" + p.name + "idwal" + walletId);
				db.insert(DBSchema.Person.TABLE_NAME, null, val);
			}
			
			for(Transaction t : w.trans){
				val = new ContentValues();
				val.put(DBSchema.Transaction.COLUMN_NAME_WHEN, t.time.getTime() / 1000);
				val.put(DBSchema.Transaction.COLUMN_NAME_DESC, t.desc);
				val.put(DBSchema.Transaction.COLUMN_NAME_WALLET_ID, walletId);
				long transId = db.insert(DBSchema.Transaction.TABLE_NAME, null, val);
				//TODO zalozenie - tylko jedna osoba w danym portfelu o danej nazwie!
				for(Fee f : t.charge){
					String proj[] = {DBSchema.Person._ID};
					String selArg[] = {f.who.name};
					Cursor c = db.query(
							DBSchema.Person.TABLE_NAME,
							proj,
							DBSchema.Person.COLUMN_NAME_NAME + " = ?",
							selArg,
							null, null, null, "1");
					c.moveToFirst();
					long personId = c.getInt(c.getColumnIndex(DBSchema.Person._ID));
					c.close();
					val = new ContentValues();
					val.put(DBSchema.Fee.COLUMN_NAME_PAID, f.paid);
					val.put(DBSchema.Fee.COLUMN_NAME_PERSON_ID, personId);
					val.put(DBSchema.Fee.COLUMN_NAME_TRANS_ID, transId);
					db.insert(DBSchema.Fee.TABLE_NAME, null, val);
				}
			}
		}
		db.close();
		
	}
}
