package com.example.virtualwallet;

import android.provider.BaseColumns;

public class DBSchema {
	public static final class Wallet implements BaseColumns{
		private Wallet() {}
		public static final String TABLE_NAME = "wallet";
		public static final String COLUMN_NAME_NAME = "name";
		public static final String COLUMN_NAME_CREATION_DATE = "creation";
	}
	public static final class Person implements BaseColumns{
		private Person() {}
		public static final String TABLE_NAME = "person";
		public static final String COLUMN_NAME_NAME = "name";
		public static final String COLUMN_NAME_WALLET_ID = "walletId";
		public static final String COLUMN_NAME_PAID = "paid";
		public static final String COLUMN_NAME_MAIL = "mail";
	}
	public static final class Transaction implements BaseColumns{
		private Transaction() {}
		public static final String TABLE_NAME = "trans";
		public static final String COLUMN_NAME_DESC = "description";
		public static final String COLUMN_NAME_WHEN = "time";
		public static final String COLUMN_NAME_WALLET_ID = "walletId"; //redundancja, ale ulatwia wczytywanie tego wszystkiego
	}
	public static final class Fee implements BaseColumns{
		private Fee() {}
		public static final String TABLE_NAME = "fee";
		public static final String COLUMN_NAME_PERSON_ID = "personId";
		public static final String COLUMN_NAME_PAID = "paid";
		public static final String COLUMN_NAME_TRANS_ID = "transId";
	}
}
