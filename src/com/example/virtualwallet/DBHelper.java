package com.example.virtualwallet;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DBHelper extends SQLiteOpenHelper {

	private static final String DB_NAME = "wallet.db";
	public DBHelper(Context context) {
		super(context, DB_NAME, null, 7);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		final String CREATE_WALLET =
		"CREATE TABLE " + DBSchema.Wallet.TABLE_NAME + " (" +
		DBSchema.Wallet._ID + " INTEGER PRIMARY KEY," + 
		DBSchema.Wallet.COLUMN_NAME_NAME + " TEXT," + 
		DBSchema.Wallet.COLUMN_NAME_CURRENCY + " TEXT," + 
		DBSchema.Wallet.COLUMN_NAME_CREATION_DATE + " INTEGER);";
		
		final String CREATE_PERSON =
		"CREATE TABLE " + DBSchema.Person.TABLE_NAME + " (" + 
		DBSchema.Person._ID + " INTEGER PRIMARY KEY," +
		DBSchema.Person.COLUMN_NAME_NAME + " TEXT," +
		DBSchema.Person.COLUMN_NAME_MAIL + " TEXT," +
		DBSchema.Person.COLUMN_NAME_PAID + " REAL," +
		DBSchema.Person.COLUMN_NAME_ACTIVE + " INTEGER," +
		DBSchema.Person.COLUMN_NAME_WALLET_ID + " INTEGER," +
		"FOREIGN KEY(" + DBSchema.Person.COLUMN_NAME_WALLET_ID + 
		") REFERENCES " + DBSchema.Wallet.TABLE_NAME + "(" +
		DBSchema.Wallet._ID + "));";
		
		final String CREATE_TRANS = 
		"CREATE TABLE " + DBSchema.Transaction.TABLE_NAME + " (" +
		DBSchema.Transaction._ID + " INTEGER PRIMARY KEY," +
		DBSchema.Transaction.COLUMN_NAME_WHEN + " INTEGER," +
		DBSchema.Transaction.COLUMN_NAME_DESC + " TEXT," +
		DBSchema.Transaction.COLUMN_NAME_WALLET_ID + " INTEGER," +
		"FOREIGN KEY(" + DBSchema.Transaction.COLUMN_NAME_WALLET_ID + 
		") REFERENCES " + DBSchema.Wallet.TABLE_NAME + "(" +
		DBSchema.Wallet._ID + "));";
		
		final String CREATE_FEE = 
		"CREATE TABLE " + DBSchema.Fee.TABLE_NAME + " (" +
		DBSchema.Fee._ID + " INTEGER PRIMARY KEY," +
		DBSchema.Fee.COLUMN_NAME_PERSON_ID + " INTEGER," + 
		DBSchema.Fee.COLUMN_NAME_TRANS_ID + " INTEGER," +
		DBSchema.Fee.COLUMN_NAME_PAID + " REAL," + 
		"FOREIGN KEY(" + DBSchema.Fee.COLUMN_NAME_TRANS_ID + 
		") REFERENCES " + DBSchema.Transaction.TABLE_NAME + "(" +
		DBSchema.Transaction._ID + ")," +
		"FOREIGN KEY(" + DBSchema.Fee.COLUMN_NAME_PERSON_ID + 
		") REFERENCES " + DBSchema.Person.TABLE_NAME + "(" + 
		DBSchema.Person._ID + "));";
		
		final String CREATE_GROUP = 
		"CREATE TABLE " + DBSchema.Group.TABLE_NAME + " (" +
		DBSchema.Group._ID + " INTEGER PRIMARY KEY," +
		DBSchema.Group.COLUMN_NAME_NAME + " TEXT);";
		
		final String CREATE_BELONGING =
		"CREATE TABLE " + DBSchema.Belonging.TABLE_NAME + " (" +
		DBSchema.Belonging._ID + " INTEGER PRIMARY KEY," + 
		DBSchema.Belonging.COLUMN_NAME_PERSON_ID + " INTEGER," +
		DBSchema.Belonging.COLUMN_NAME_GROUP_ID + " INTEGER," +
		"FOREIGN KEY(" + DBSchema.Belonging.COLUMN_NAME_PERSON_ID + 
		") REFERENCES " + DBSchema.Person.TABLE_NAME + "(" +
		DBSchema.Person._ID + ")," +
		"FOREIGN KEY(" + DBSchema.Belonging.COLUMN_NAME_GROUP_ID + 
		") REFERENCES " + DBSchema.Group.TABLE_NAME + "(" +
		DBSchema.Group._ID + "));";
		
		Log.d("sygi", CREATE_WALLET);
		Log.d("sygi", CREATE_GROUP);
		
		db.execSQL(CREATE_WALLET);
		db.execSQL(CREATE_PERSON);
		db.execSQL(CREATE_TRANS);
		db.execSQL(CREATE_FEE);
		db.execSQL(CREATE_GROUP);
		db.execSQL(CREATE_BELONGING);
		db.execSQL("PRAGMA foreign_keys = ON;");
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		Log.d("sygi", "Database - on upgrade");
		db.execSQL("DROP TABLE IF EXISTS " + DBSchema.Belonging.TABLE_NAME);
		db.execSQL("DROP TABLE IF EXISTS " + DBSchema.Group.TABLE_NAME);
		db.execSQL("DROP TABLE IF EXISTS " + DBSchema.Fee.TABLE_NAME);
		db.execSQL("DROP TABLE IF EXISTS " + DBSchema.Person.TABLE_NAME);
		db.execSQL("DROP TABLE IF EXISTS " + DBSchema.Transaction.TABLE_NAME);
		db.execSQL("DROP TABLE IF EXISTS " + DBSchema.Wallet.TABLE_NAME);
		onCreate(db);
	}
	
	@Override
	public void onDowngrade(SQLiteDatabase db, int oldV, int newV){
		Log.d("sygi", "Database - on downgrade");
		db.execSQL("DROP TABLE IF EXISTS " + DBSchema.Belonging.TABLE_NAME);
		db.execSQL("DROP TABLE IF EXISTS " + DBSchema.Group.TABLE_NAME);
		db.execSQL("DROP TABLE IF EXISTS " + DBSchema.Fee.TABLE_NAME);
		db.execSQL("DROP TABLE IF EXISTS " + DBSchema.Person.TABLE_NAME);
		db.execSQL("DROP TABLE IF EXISTS " + DBSchema.Transaction.TABLE_NAME);
		db.execSQL("DROP TABLE IF EXISTS " + DBSchema.Wallet.TABLE_NAME);
		onCreate(db);
	}

}
