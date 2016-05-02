package bvgiants.diary3;

import android.content.res.AssetManager;
import android.database.sqlite.SQLiteDatabaseLockedException;
import android.database.sqlite.SQLiteException;
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
import android.util.Log;

/**
 * Created by Ken on 26/04/2016.
 * Class is used to create an SQLite database and add the User table
 * User table stores basic user data primarily used to login to the application, identify their in
 * app alias and to track which team (if any) the user is associated with
 */
public class LookupFoodDBController extends SQLiteOpenHelper {

    //Variables for Creation of various database tables
    public static final String DATABASE_NAME = "HealthDiary.db";
    public static final String TABLE_NAME = "LookupFood";
    public static final String CONTACTS_COLUMN_ID = "ID";
    public static final String CONTACTS_COLUMN_NAME = "Name";
    public static final String CONTACTS_COLUMN_CALORIES = "Calories";
    public static final String CONTACTS_COLUMN_SUGAR = "Sugar";
    public static final String CONTACTS_COLUMN_FAT = "Fat";
    public static final String CONTACTS_COLUMN_ENERGY = "Energy";
    public static final String CONTACTS_COLUMN_SODIUM = "SODIUM";
    public static final String CONTACTS_COLUMN_PROTEIN = "Protein";
    public static final String CONTACTS_COLUMN_IMGLOCAL= "ImgLocal";



    public LookupFoodDBController(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    public void onCreate(SQLiteDatabase db) {
        db.execSQL("Create Table LookupFood " +
                "(ID integer primary key autoincrement, Name text, Calories integer, Sugar integer" +
                ", Fat integer, Energy integer, Sodium integer, Protein integer, ImgLocal text);");
        System.out.printf("LookupFoodCreated");
    }

    //This class is used for the creation of a list of foods for users to select from.  It will
    //display a foods nutrition info and enable nutrition calculations
    public class FoodItem {
        public String name;
        public int calories;
        public int sugar;
        public int fat;
        public int energy;
        public int sodium;
        public int protein;
        public String imagelocal;

        public FoodItem(String name, int calories, int sugar, int fat, int energy, int sodium,
                        int protein, String imageLocal) {
            this.name = name;
            this.calories = calories;
            this.sugar = sugar;
            this.fat = fat;
            this.energy = energy;
            this.sodium = sodium;
            this.protein = protein;
            this.imagelocal = imageLocal;
        }

        public String toString(){
            return "Food: " +this.name + "Calories: " +this.calories + " This is just an example return. " +
                    "Look at LookupFoodDBController -> FoodItem.toString()";
        }
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // TODO Auto-generated method stub
        db.execSQL("DROP TABLE IF EXISTS LookupFood");
        onCreate(db);
    }

    public String delete(SQLiteDatabase db){
        String success = " Delete " + TABLE_NAME + " Successful";
        try{
            db.execSQL("delete from " + TABLE_NAME);
            return success;
        }catch (SQLiteException e){
            System.out.printf("%s\n\n No Table to delete",e);
        }
        return TABLE_NAME + "DELETE FAILED!";
    }

    //Inserts a Food if user were to enter one
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
        db.insert("LookupFood", null, contentValues);
        return true;
    }

    //Will use this to verify user exists in database.  If no will enable login
    public boolean getFood(String name) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("SELECT * FROM " + TABLE_NAME + " WHERE Name=?",
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

    public ArrayList<FoodItem> allFood(){

        String select = "SELECT * FROM " + TABLE_NAME;
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

    //If user needs to update their details.  Will need a to call getUser first to gain user ID!
    //TODO Add feature to enable user to update their details
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
        db.update("LookupFood", contentValues, "id = ? ", new String[]{Integer.toString(id)});
        return true;
    }

    //Will read each line of the user.txt file located in the assets folder.
    //Once the line have been read, it will split the line and create new users in the db.
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
            System.out.printf("%s\n%s\n%s\n%s\n",input[0],input[1],input[2],input[3]);
            insertFood(input[0],Integer.parseInt(input[1]),Integer.parseInt(input[2])
                    ,Integer.parseInt(input[3]),Integer.parseInt(input[4]),Integer.parseInt(input[5]),
                    Integer.parseInt(input[6]),input[7]);
            System.out.printf("DB Insert Statement executed successfully");
        }

    }
}



