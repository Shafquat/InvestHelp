package com.example.stockdata.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class StocksDBOpenHelper extends SQLiteOpenHelper {

	private static final String LOGTAG = "EXPLORECA";

	private static final String DATABASE_NAME = "stock.db";
	private static final int DATABASE_VERSION = 1;
	
	public static final String TABLE_STOCK = "stocks";
	public static final String COLUMN_ID = "stockId";
	public static final String COLUMN_SYMBOL = "symbol";
	public static final String COLUMN_PRICE = "price";
	public static final String COLUMN_CHANGE = "change";
	
	private static final String TABLE_CREATE =
			"CREATE TABLE " + TABLE_STOCK + " (" +
			COLUMN_ID + " INTERGER PRIMARY KEY AUTOINCREMENT, " +
			COLUMN_SYMBOL + " TEXT, " +
			COLUMN_PRICE+ " NUMERIC, " +
			COLUMN_CHANGE + " NUMERIC " +
			")";
	
	public StocksDBOpenHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(TABLE_CREATE);
		Log.i(LOGTAG, "Table has been created");

	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_STOCK);
		onCreate(db);

	}

}
