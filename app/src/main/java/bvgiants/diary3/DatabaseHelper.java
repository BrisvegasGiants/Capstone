package bvgiants.diary3;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.ContactsContract;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

/**
 * Created by kenst on 2/05/2016.
 */
public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String LOG = "DatabaseHelper";

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME ="HealthDiary";

    //Table Names
    private static final String TABLE_USERS = "Users";
    private static final String TABLE_LOOKUPFOOD = "LookupFood";

    //Common column names
    private static final String KEY_ID = "ID";

    //Users Table Columns
    public static final String USERS_COLUMN_EMAIL = "EmailAdd";
    public static final String USERS_COLUMN_PW = "Password";
    public static final String USERS_COLUMN_ALIAS = "Alias";
    public static final String USERS_COLUMN_TEAM = "Team";

    //LookupFood Table Columns
    public static final String LUPFOOD_COLUMN_NAME = "Name";
    public static final String LUPFOOD_COLUMN_CALORIES = "Calories";
    public static final String LUPFOOD_COLUMN_SUGAR = "Sugar";
    public static final String LUPFOOD_COLUMN_FAT = "Fat";
    public static final String LUPFOOD_COLUMN_ENERGY = "Energy";
    public static final String LUPFOOD_COLUMN_SODIUM = "Sodium";
    public static final String LUPFOOD_COLUMN_PROTEIN = "Protein";
    public static final String LUPFOOD_COLUMN_IMGLOCAL= "ImgLocal";

    //Table Create Statements
    private static final String CREATE_TABLE_USERS = "CREATE TABLE " + TABLE_USERS + "(" + KEY_ID +
            " INTEGER PRIMARY KEY AUTOINCREMENT," + USERS_COLUMN_EMAIL + " TEXT," + USERS_COLUMN_PW +
            " TEXT," + USERS_COLUMN_ALIAS + " TEXT, " + USERS_COLUMN_TEAM + " TEXT);";
    private static final String CREATE_TABLE_LOOKUPFOOD = "CREATE TABLE " + TABLE_LOOKUPFOOD + "(" +
            KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + LUPFOOD_COLUMN_NAME + " INTEGER, " +
            LUPFOOD_COLUMN_CALORIES + " INTEGER, " + LUPFOOD_COLUMN_SUGAR + " INTEGER, " + LUPFOOD_COLUMN_FAT
            + " INTEGER, " + LUPFOOD_COLUMN_ENERGY + " INTEGER, " + LUPFOOD_COLUMN_SODIUM + " INTEGER, "
            + LUPFOOD_COLUMN_PROTEIN + " INTEGER, " + LUPFOOD_COLUMN_IMGLOCAL + " TEXT);";

    public DatabaseHelper (Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    //Create the various tables, add table here as required
    public void onCreate(SQLiteDatabase db){
        db.execSQL(CREATE_TABLE_USERS);
        db.execSQL(CREATE_TABLE_LOOKUPFOOD);
    }

    //Add drop table per table in db
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_LOOKUPFOOD);
        onCreate(db);
    }

    //Add execSQL statement per db table
    public String delete(SQLiteDatabase db) {
        String successUsers = " Delete " + TABLE_USERS + " Successful";
        String successLookupFood = " Delete " + TABLE_LOOKUPFOOD + " Successful";
        try{
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_LOOKUPFOOD);
            return successUsers + successLookupFood;
        }catch (SQLiteException e){
            System.out.printf("%s\n\n No Table to delete",e);
        }
        return "DELETING OF TABLES FAILED!";
    }

    /* INSERT STATEMENTS
    INSERT USERS
    INSERT LOOKUPFOOD
     */
    //Inserts a User into USERS TABLE
    //TODO Add feature to enable user to create account
    public boolean insertUser(String email, String password, String alias, String team) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("EmailAdd", email);
        contentValues.put("Password", password);
        contentValues.put("Alias", alias);
        contentValues.put("Team", team);
        db.insert(TABLE_USERS, null, contentValues);
        return true;
    }

    //Inserts a Food into LOOKUPFOOD
    //TODO Add feature to enable user to create account
    public boolean insertFood(String name, int calories, int sugar, int fat, int energy, int sodium,
                              int protein, String imageLocal) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("Name", name);
        contentValues.put("Calories", calories);
        contentValues.put("Sugar", sugar);
        contentValues.put("Fat", fat);
        contentValues.put("Energy", energy);
        contentValues.put("Sodium", sodium);
        contentValues.put("Protein", protein);
        contentValues.put("ImgLocal", imageLocal);
        db.insert(TABLE_LOOKUPFOOD, null, contentValues);
        return true;
    }

    /* GET STATEMENTS
    GET USER
    GET FOOD
     */

    public boolean getUser(String email, String pw) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("SELECT * FROM " + TABLE_USERS + " WHERE EmailAdd=? AND Password=?",
                new String[]{email,pw});
        if(res.moveToFirst()) {
            res.close();
            db.close();
            return true;
        }
        else {
            System.out.print("USER ISN'T FOUND");
            res.close();
            db.close();
            return false;
        }
    }

    public boolean getFood(String name) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("SELECT * FROM " + TABLE_LOOKUPFOOD + " WHERE Name=?",
                new String[]{name});
        if(res.moveToFirst()) {
            res.close();
            db.close();
            return true;
        }
        else {
            System.out.print("FOOD ISN'T FOUND");
            res.close();
            db.close();
            return false;
        }
    }

    /* UPDATE STATEMENTS
    UPDATE USER
    UPDATE FOOD
     */

    public boolean updateUser(Integer id, String email, String password, String alias, String team) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("EmailAdd", email);
        contentValues.put("Password", password);
        contentValues.put("Alias", alias);
        contentValues.put("Team", team);
        db.update(TABLE_USERS, contentValues, "id = ? ", new String[]{Integer.toString(id)});
        return true;
    }

    public boolean updateFood(Integer id, String name, int calories, int sugar, int fat, int energy,
                              int sodium, int protein, String imageLocal) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("Name", name);
        contentValues.put("Calories", calories);
        contentValues.put("Sugar", sugar);
        contentValues.put("Fat", fat);
        contentValues.put("Energy", energy);
        contentValues.put("Sodium", sodium);
        contentValues.put("Protein", protein);
        contentValues.put("ImgLocal", imageLocal);
        db.update(TABLE_LOOKUPFOOD, contentValues, "id = ? ", new String[]{Integer.toString(id)});
        return true;
    }

    /* GET ALL TABLE ITEMS
    GET FOOD

    public ArrayList<FoodItem> allFood(){

        String select = "SELECT * FROM " + TABLE_LOOKUPFOOD;
        ArrayList<FoodItem> results = new ArrayList<FoodItem>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery(select,null);
        if(res.moveToFirst()){
            do {
                FoodItem food = new FoodItem(res.getString(1),res.getInt(2),res.getInt(3),res.getInt(4),
                        res.getInt(5),res.getInt(6),res.getInt(7),res.getString(8));
                results.add(food);
            }while(res.moveToNext());
            if(res != null && !res.isClosed())
                res.close();
        }
        return results;
    } */


        // GET USER SEARCHED ITEMS
    public ArrayList<FoodItem> userSearch(String searchResult) {
        String select = "SELECT * FROM " + TABLE_LOOKUPFOOD + "WHERE Name LIKE" + searchResult;
        ArrayList<FoodItem> results = new ArrayList<FoodItem>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery(select,null);
        if(res.moveToFirst()){
            do {
                FoodItem food = new FoodItem(res.getString(1),res.getInt(2),res.getInt(3),res.getInt(4),
                        res.getInt(5),res.getInt(6),res.getInt(7),res.getString(8));
                results.add(food);
            }while(res.moveToNext());
            if(res != null && !res.isClosed())
                res.close();
        }
        return results;
    }

    /*SAVE DATA TO TABLES
    SAVE USERS DATA
    SAVE FOOD DATA
    Will read each line of the XXX.txt file located in the assets folder.
    Once the line have been read, it will split the line and create new users in the db.
     */

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
            input = userLine.split(" ");
            System.out.printf("%s\n%s\n%s\n%s\n",input[0],input[1],input[2],input[3]);
            insertUser(input[0],input[1],input[2],input[3]);
        }

    }

    public void saveDataToLookupFoodTable(Context context, String filename) throws IOException {

        ArrayList<String> foodString = new ArrayList<String>(); //ArrayList to hold lines of txt file
        String foodLine; //Line of txt form txt file

        //Attempt to open file and store lines
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(context.getAssets()
                    .open(filename)));
            String line;
            while ((line = reader.readLine()) != null) {
                foodString.add(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        //Split lines and create user entry in user table
        for (int i = 0; i < foodString.size(); i++) {
            String[] input;
            foodLine = foodString.get(i);
            input = foodLine.split(" ");
            System.out.printf("%s\n%s\n%s\n%s\n",input[0],input[1],input[2],input[3],input[5],
                    input[6],input[7]);
            insertFood(input[0],Integer.parseInt(input[1]),Integer.parseInt(input[2])
                    ,Integer.parseInt(input[3]),Integer.parseInt(input[4]),Integer.parseInt(input[5]),
                    Integer.parseInt(input[6]),input[7]);
            System.out.printf("DB Insert Statement executed successfully");
        }

    }
}
