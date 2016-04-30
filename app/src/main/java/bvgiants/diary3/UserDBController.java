package bvgiants.diary3;

import android.content.res.AssetManager;
import android.os.Environment;
import android.provider.BaseColumns;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
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
 * Class is used to create an SQLite database and add the User table
 * User table stores basic user data primarily used to login to the application, identify their in
 * app alias and to track which team (if any) the user is associated with
 */
public class UserDBController extends SQLiteOpenHelper {

    //Variables for Creation of various database tables
    public static final String DATABASE_NAME = "HealthDiary.db";
    public static final String TABLE_NAME = "User";
    public static final String CONTACTS_COLUMN_ID = "ID";
    public static final String CONTACTS_COLUMN_EMAIL = "EmailAdd";
    public static final String CONTACTS_COLUMN_PW = "Password";
    public static final String CONTACTS_COLUMN_ALIAS = "Alias";
    public static final String CONTACTS_COLUMN_TEAM = "Team";

    public UserDBController(Context context) {

        super(context, DATABASE_NAME, null, 1);
    }

    public void onCreate(SQLiteDatabase db) {
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
    public boolean insertUser(String email, String password, String alias, String team) {
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
    public Cursor getUser(String email, String pw) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select * from User where EmailAdd=" + email + ""
                + "and Password=" + pw + "", null);
        return res;
    }

    //If user needs to update their details.  Will need a to call getUser first to gain user ID!
    //TODO Add feature to enable user to update their details
    public boolean updateUser(Integer id, String email, String password, String alias, String team) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("EmailAdd", email);
        contentValues.put("Password", password);
        contentValues.put("Alias", alias);
        contentValues.put("Team", team);
        db.update("contacts", contentValues, "id = ? ", new String[]{Integer.toString(id)});
        return true;
    }

    //Will read each line of the user.txt file located in the assets folder.
    //Once the line have been read, it will split the line and create new users in the db.
    public void saveDataToUserTable(Context context, String filename) throws IOException {

        ArrayList<String> userString = new ArrayList<String>(); //ArrayList to hold lines of txt file
        String userLine; //Line of txt form txt file

        //Attempt to open file and store lines
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(context.getAssets()
                    .open(filename)));
            String line;
            while ((line = reader.readLine()) != null) {
                userString.add(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        //Split lines and create user entry in user table
        for (int i = 0; i < userString.size(); i++) {
            String[] input;
            userLine = userString.get(i);
            input = userLine.split(",");
            insertUser(input[0],input[1],input[2],input[3]);
        }

    }
}



