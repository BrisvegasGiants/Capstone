package bvgiants.diary3;

import android.provider.BaseColumns;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteDatabase;

/**
 * Created by Ken on 26/04/2016.
 */
public class UserDBController extends SQLiteOpenHelper{

    //Variables for Creation of various database tables
    public static final String DATABASE_NAME = "HealthDiary.db";
    public static final String TABLE_NAME = "User";
    public static final String CONTACTS_COLUMN_ID = "ID";
    public static final String CONTACTS_COLUMN_EMAIL = "EmailAdd";
    public static final String CONTACTS_COLUMN_PW = "Password";
    public static final String CONTACTS_COLUMN_ALIAS = "Alias";
    public static final String CONTACTS_COLUMN_TEAM = "Team";

    public UserDBController(Context context){

        super(context, DATABASE_NAME, null, 1);
    }

    public void onCreate(SQLiteDatabase db){
        db.execSQL("Create Table User " +
                "ID integer primary key, EmailAdd text, Password text, Alias text,Team text");
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // TODO Auto-generated method stub
        db.execSQL("DROP TABLE IF EXISTS User");
        onCreate(db);
    }

    //Inserts a User if one were to register
    //TODO Add feature to enable user to create account
    public boolean insertUser  (String email, String password, String alias, String team)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("EmailAdd", email);
        contentValues.put("Password", password);
        contentValues.put("Alias", alias);
        contentValues.put("Team", team);
        db.insert("User", null, contentValues);
        return true;
    }

    //Will use this to verify user exists in database.  If no will enable login
    public Cursor getUser(String email, String pw){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select * from User where EmailAdd="+email+""
                +"and Password="+pw+"", null );
        return res;
    }

    //If user needs to update their details.  Will need a to call getUser first to gain user ID!
    //TODO Add feature to enable user to update their details
    public boolean updateUser (Integer id, String email, String password, String alias, String team)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("EmailAdd", email);
        contentValues.put("Password", password);
        contentValues.put("Alias", alias);
        contentValues.put("Team", team);
        db.update("contacts", contentValues, "id = ? ", new String[] { Integer.toString(id) } );
        return true;
    }
}
