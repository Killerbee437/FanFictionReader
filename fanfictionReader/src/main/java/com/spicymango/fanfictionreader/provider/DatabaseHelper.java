package com.spicymango.fanfictionreader.provider;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper  implements SqlConstants{
	
	private static final int DATABASE_VERSION = 9; //Database version 8
	private static final String DATABASE_NAME = "library.db";
	
	//The name of the FanFiction table
	protected static final String FANFICTION_TABLE = "library";
	protected static final String FICTIONPRESS_TABLE = "fictionpress_library";	
	
	public DatabaseHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	private static final String TABLE_DEF = "("
            + KEY_STORY_ID + " INTEGER PRIMARY KEY ON CONFLICT REPLACE,"
            + KEY_TITLE + " TEXT NOT NULL,"
            + KEY_AUTHOR + " TEXT NOT NULL,"
            + KEY_AUTHOR_ID + " TEXT,"
            + KEY_RATING + " TEXT,"
            + KEY_GENRE + " TEXT,"
            + KEY_LANGUAGE + " TEXT,"
            + KEY_CATEGORY + " TEXT,"
            + KEY_CHAPTER + " INTEGER,"
            + KEY_LENGTH + " INTEGER,"
            + KEY_FAVORITES + " INTEGER,"
            + KEY_FOLLOWERS + " INTEGER,"
            + KEY_PUBLISHED + " INTEGER,"
            + KEY_UPDATED + " INTEGER,"
            + KEY_SUMMARY + " TEXT NOT NULL,"
            + KEY_LAST + " INTEGER,"
            + KEY_COMPLETE + " BOOLEAN,"
            + KEY_OFFSET + " INTEGER,"
			+ KEY_CHARACTERS + " TEXT,"
			+ KEY_REVIEWS + " INTEGER"
            + ")";
	
	@Override
	public void onCreate(SQLiteDatabase db) {		
		String fanFicTable = "CREATE TABLE " + FANFICTION_TABLE + TABLE_DEF;
		String fictionPressTable = "CREATE TABLE " + FICTIONPRESS_TABLE + TABLE_DEF;
		db.execSQL(fanFicTable);
		db.execSQL(fictionPressTable);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int arg2) {
		if (oldVersion < 3) {
			db.execSQL("DROP TABLE IF EXISTS " + FANFICTION_TABLE);
			onCreate(db);
		}
		if (oldVersion < 4) {
			// In this version, the internal representation of lengths, favorites, and follows was
			// changed from text entries to integer entries
			db.execSQL("UPDATE " + FANFICTION_TABLE + " SET " + KEY_LENGTH + " = REPLACE(" + KEY_LENGTH+ ",',','')");
			db.execSQL("UPDATE " + FANFICTION_TABLE + " SET " + KEY_FAVORITES + " = REPLACE(" + KEY_FAVORITES+ ",',','')");
			db.execSQL("UPDATE " + FANFICTION_TABLE + " SET " + KEY_FOLLOWERS + " = REPLACE(" + KEY_FOLLOWERS+ ",',','')");
			
			db.execSQL("CREATE TABLE IF NOT EXISTS " +"tmp" + "("
	                + KEY_STORY_ID + " INTEGER PRIMARY KEY ON CONFLICT REPLACE,"
	                + KEY_TITLE + " TEXT NOT NULL,"
	                + KEY_AUTHOR + " TEXT NOT NULL,"
	                + KEY_AUTHOR_ID + " TEXT,"
	                + KEY_RATING + " TEXT,"
	                + KEY_GENRE + " TEXT,"
	                + KEY_LANGUAGE + " TEXT,"
	                + KEY_CATEGORY + " TEXT,"
	                + KEY_CHAPTER + " INTEGER,"
	                + KEY_LENGTH + " INTEGER,"
	                + KEY_FAVORITES + " INTEGER,"
	                + KEY_FOLLOWERS + " INTEGER,"
	                + KEY_PUBLISHED + " INTEGER,"
	                + KEY_UPDATED + " INTEGER,"
	                + KEY_SUMMARY + " TEXT NOT NULL,"
	                + KEY_LAST + " INTEGER"
					+ ")");
			db.execSQL("INSERT INTO tmp SELECT * FROM " + FANFICTION_TABLE);
			db.execSQL("DROP TABLE " + FANFICTION_TABLE);
			db.execSQL("ALTER TABLE tmp RENAME TO " + FANFICTION_TABLE);
		}
		if (oldVersion < 5) {
			// A column was added in order to remember if a story was completed or not
			db.execSQL("ALTER TABLE " + FANFICTION_TABLE + " ADD COLUMN " + KEY_COMPLETE + " BOOLEAN DEFAULT 0");
		}
		if (oldVersion < 6) {
			// A column was added in order to remember the position of the scroll bar in the story
			db.execSQL("ALTER TABLE " + FANFICTION_TABLE + " ADD COLUMN " + KEY_OFFSET + " TEXT DEFAULT ''");
		}
		if (oldVersion < 7) {
			// A FictionPress table was defined.
			db.execSQL("CREATE TABLE " + FICTIONPRESS_TABLE + TABLE_DEF);
		}
		if (oldVersion < 8){
			// A column for the different characters was added
			db.execSQL("ALTER TABLE " + FANFICTION_TABLE + " ADD COLUMN " + KEY_CHARACTERS + " TEXT DEFAULT ''");
		}
		if (oldVersion < 9){
			// A column for the different characters was added
			db.execSQL("ALTER TABLE " + FANFICTION_TABLE + " ADD COLUMN " + KEY_REVIEWS + " INTEGER DEFAULT 0");
		}
	}

}
