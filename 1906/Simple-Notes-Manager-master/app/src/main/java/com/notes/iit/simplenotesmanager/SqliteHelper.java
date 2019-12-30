package com.notes.iit.simplenotesmanager;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class SqliteHelper extends SQLiteOpenHelper {

    //DATABASE NAME
    public static final String DATABASE_NAME = "notes";

    //DATABASE VERSION
    public static final int DATABASE_VERSION = 1;

    //TABLE NAME
    public static final String TABLE_USERS = "users";

    //TABLE USERS COLUMNS
    //ID COLUMN @primaryKey
    public static final String KEY_ID = "id";

    //COLUMN user name
    public static final String KEY_USER_NAME = "username";

    //COLUMN email
    public static final String KEY_EMAIL = "email";

    //COLUMN password
    public static final String KEY_PASSWORD = "password";

    //SQL for creating users table
    public static final String SQL_TABLE_USERS = " CREATE TABLE " + TABLE_USERS
            + " ( "
            + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + KEY_EMAIL + " TEXT, "
            + KEY_PASSWORD + " TEXT"
            + " ) ";
    public static final String TABLE_NOTES ="note" ;
    public static final String KEY_DESCRIPTION ="description" ;
    public static final String KEY_MODIFIEDDATE ="date" ;
    public static final String SQL_TABLE_NOTES = " CREATE TABLE " + TABLE_NOTES
            + " ( "
            + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + KEY_DESCRIPTION + " TEXT, "
            + KEY_MODIFIEDDATE + " STRING"
            + " ) ";

    public SqliteHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(SQL_TABLE_USERS);
        sqLiteDatabase.execSQL(SQL_TABLE_NOTES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL(" DROP TABLE IF EXISTS " + TABLE_USERS);
    }

    public void addNote(Note note) {

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(KEY_DESCRIPTION, note.description);
        values.put(KEY_MODIFIEDDATE, note.date);
        db.insert(TABLE_NOTES, null, values);
    }

    public Cursor retriveAllNotesCursor() {

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cur =  db.rawQuery( "select rowid as _id,"+KEY_DESCRIPTION+","+KEY_MODIFIEDDATE+" from "+ TABLE_NOTES, null);
        return cur;
    }
    public void addUser(User user) {

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(KEY_EMAIL, user.email);

        values.put(KEY_PASSWORD, user.password);

        long todo_id = db.insert(TABLE_USERS, null, values);
    }

    public User retreiveUserByEmail(String email){
        SQLiteDatabase database=this.getReadableDatabase();
        Cursor cursor=database.query(TABLE_USERS,null,KEY_EMAIL+"=?",new String[]{email},null,null,null);
        if(cursor!=null&&cursor.getCount()>0){
            cursor.moveToFirst();
            String mail=cursor.getString(cursor.getColumnIndex(KEY_EMAIL));
            String password=cursor.getString(cursor.getColumnIndex(KEY_PASSWORD));
            User retreivedUser=new User(mail,password);
            return retreivedUser;
        }
        return null;
    }


    public boolean isEmailExists(String email) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_USERS,// Selecting Table
                null,
                KEY_EMAIL + "=?",
                new String[]{email},
                null, null, null);

        if (cursor != null && cursor.moveToFirst()&& cursor.getCount()>0) {
            return true;
        }
        return false;
    }

    public void delete() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NOTES, null, null);
        db.close();
    }

    public void deleteInterestId(long id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NOTES, KEY_ID + "=?", new String[]{String.valueOf(id)});
        db.close();
    }

    public int getRorId(long pos) {
        SQLiteDatabase db = this.getWritableDatabase();

        Cursor cursor = null;
        long id=0;
        try {
            cursor = db.rawQuery("SELECT id FROM note WHERE id=?", new String[] {pos + ""});
            if(cursor.getCount() > 0) {
                cursor.moveToFirst();
                id = cursor.getLong(cursor.getColumnIndex("id"));
            }
            return (int) id;
        }finally {
            cursor.close();
        }
    }
}
