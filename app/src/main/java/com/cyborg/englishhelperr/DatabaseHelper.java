package com.cyborg.englishhelperr;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper implements BaseColumns {

    public static final String WORD_COLUMN = "WORD";
    public static final String TRANSLATE_COLUMN = "TRANSLATE";
    public static final String OWNER_COLUMN = "OWNER";
    public static final String TABLE_NAME = "WORDS";
    public static final String CREATED_COLUMN = "CREATED";


    private static final String DATABASE_NAME = "englishhelper.db";
    private static final int DATABASE_VERSION = 1;


    DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public DatabaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        sqLiteDatabase.execSQL("CREATE TABLE " + TABLE_NAME + " ("
                + BaseColumns._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + WORD_COLUMN + " TEXT NOT NULL UNIQUE, " + TRANSLATE_COLUMN
                + " TEXT NOT NULL UNIQUE, " + OWNER_COLUMN + " TEXT NOT NULL, " + CREATED_COLUMN + " INTEGER NOT NULL);");

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

    public void addWord(Words word) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(WORD_COLUMN, word.getWord());
        values.put(TRANSLATE_COLUMN, word.getTranslate());
        values.put(OWNER_COLUMN, word.getOwnerId());
        long l = word.getCreated().getTime() / 1000;
        int i = (int) l;
        values.put(CREATED_COLUMN, i);

        // Inserting Row
        db.insert("WORDS", null, values);
        db.close(); // Closing database connection

    }

    public Words getLastWord() {
        String countQuery = "SELECT  * FROM " + "WORDS";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        cursor.moveToLast();

        Words word = new Words();
        word.setId(cursor.getInt(0));
        word.setWord(cursor.getString(1));
        word.setTranslate(cursor.getString(2));
        word.setCreated(new java.util.Date(cursor.getLong(4) * 1000));
        cursor.close();
        return word;
    }

    public void deleteAll() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete("WORDS", null, null);
        db.close();
    }

    public void deleteByWord(String string) {
        SQLiteDatabase db = this.getWritableDatabase();

        db.delete("WORDS",
                WORD_COLUMN + " = ?",
                new String[]{string});
        db.close();
    }

    public List<Words> findAfterDate(long l) {
        SQLiteDatabase db = this.getWritableDatabase();


        List<Words> list = new ArrayList<Words>();
        Cursor cursor = db.query("WORDS",
                new String[]{"_ID", "WORD", "TRANSLATE", "OWNER", "CREATED"},
                "CREATED > ?",
                new String[]{Long.toString(l / 1000)},
                null, null, null);
        cursor.moveToFirst();

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Words word = new Words();
                word.setId(cursor.getInt(0));
                word.setWord(cursor.getString(1));
                word.setTranslate(cursor.getString(2));
                word.setOwnerId(cursor.getString(3));
                word.setCreated(new java.util.Date(cursor.getLong(4) * 1000));
                list.add(word);
            } while (cursor.moveToNext());
        }

        // return contact list
        return list;

    }

    public List<Words> getAllWords() {


        SQLiteDatabase db = this.getWritableDatabase();


        List<Words> list = new ArrayList<Words>();
        Cursor cursor = db.query("WORDS",
                new String[]{"_ID", "WORD", "TRANSLATE", "OWNER", "CREATED"},
                "OWNER = ?",
                new String[]{LoginActivity.objectId},
                null, null, null);
        cursor.moveToFirst();

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Words word = new Words();
                word.setId(cursor.getInt(0));
                word.setWord(cursor.getString(1));
                word.setTranslate(cursor.getString(2));
                word.setOwnerId(cursor.getString(3));
                word.setCreated(new java.util.Date(cursor.getLong(4) * 1000));
                list.add(word);
            } while (cursor.moveToNext());
        }

        return list;
    }

    public int getWordsCountByOwner() {

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query("WORDS",
                new String[]{"_ID", "WORD", "TRANSLATE", "OWNER", "CREATED"},
                "OWNER = ?",
                new String[]{LoginActivity.objectId},
                null, null, null);
        int count = cursor.getCount();

        cursor.close();

        // return count
        return count;
    }

}




//////////////////////////////////////////////////////////////
/*public Words getById(String id) {
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.query("WORDS",
                new String[] {"WORD", "TRANSLATE", "OWNER"},
                "_ID = ?",
                new String[] {String.valueOf(id)},
                null, null, null);
        if(cursor != null)
            cursor.moveToFirst();
        Words word = new Words(cursor.getInt(cursor.getColumnIndex(DatabaseHelper._ID)),
                cursor.getString(cursor.getColumnIndex(DatabaseHelper.WORD_COLUMN)),
                cursor.getString(cursor.getColumnIndex(DatabaseHelper.TRANSLATE_COLUMN)),
                cursor.getString(cursor.getColumnIndex(DatabaseHelper.OWNER_COLUMN)),
                cursor.getInt(cursor.getColumnIndex(DatabaseHelper.CREATED_COLUMN)));

        return word;
    }

  /*

    Word getWord(int id) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query("WORDS", new String[] { ID_COLUMN,
                        WORD_COLUMN, TRANSLATE_COLUMN }, ID_COLUMN + "=?",
                new String[] { String.valueOf(id) }, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();

        Word word = new Word(Integer.parseInt(cursor.getString(0)),
                cursor.getString(1), cursor.getString(2));
        // return contact
        return word;
    }*/


    /*public List<Words> getAllWords() {
        List<Words> wordList = new ArrayList<Words>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + "WORDS";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        cursor.moveToFirst();

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Words word = new Words();
                word.setId(cursor.getInt(0));
                word.setWord(cursor.getString(1));
                word.setTranslate(cursor.getString(2));
                word.setOwnerId(cursor.getString(3));
                word.setCreated(new java.util.Date(cursor.getLong(4) * 1000));
                //System.out.println("курсор DATE!!!!!!!!!!!! = " + cursor.getLong(4)*1000);
                //System.out.println("курсор DATE!!!!!!!!!!!! = " + new java.util.Date(cursor.getLong(4)*1000));
                // Adding contact to list
                wordList.add(word);
            } while (cursor.moveToNext());
        }

        // return contact list
        return wordList;
    }*/


    /*public int getWordsCount() {
        String countQuery = "SELECT  * FROM " + "WORDS";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        int count = cursor.getCount();

        cursor.close();

        // return count
        return count;
    }*/
