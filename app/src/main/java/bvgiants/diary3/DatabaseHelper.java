package bvgiants.diary3;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.ContactsContract;
import android.util.Log;

import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
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
    public static final String TABLE_USERTRAITS = "UserTraits";

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

    //UserTraits Table Columns
    public static final String UTRAITS_COLUMN_FIRSTNAME = "Firstname";
    public static final String UTRAITS_COLUMN_LASTNAME = "Lastname";
    public static final String UTRAITS_COLUMN_HEIGHT = "Height";
    public static final String UTRAITS_COLUMN_WEIGHT = "Weight";
    public static final String UTRAITS_COLUMN_AGE = "Age";
    public static final String UTRAITS_COLUMN_GENDER = "Gender";

    //Table Create Statements
    private static final String CREATE_TABLE_USERS = "CREATE TABLE " + TABLE_USERS + "(" + KEY_ID +
            " INTEGER PRIMARY KEY AUTOINCREMENT," + USERS_COLUMN_EMAIL + " TEXT," + USERS_COLUMN_PW +
            " TEXT," + USERS_COLUMN_ALIAS + " TEXT, " + USERS_COLUMN_TEAM + " TEXT);";
    private static final String CREATE_TABLE_LOOKUPFOOD = "CREATE TABLE " + TABLE_LOOKUPFOOD + "(" +
            KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + LUPFOOD_COLUMN_NAME + " INTEGER, " +
            LUPFOOD_COLUMN_CALORIES + " INTEGER, " + LUPFOOD_COLUMN_SUGAR + " INTEGER, " + LUPFOOD_COLUMN_FAT
            + " INTEGER, " + LUPFOOD_COLUMN_ENERGY + " INTEGER, " + LUPFOOD_COLUMN_SODIUM + " INTEGER, "
            + LUPFOOD_COLUMN_PROTEIN + " INTEGER, " + LUPFOOD_COLUMN_IMGLOCAL + " TEXT);";

    private static final String CREATE_TABLE_USERTRAITS = "CREATE TABLE " + TABLE_USERTRAITS + "(" + KEY_ID
            + " INTEGER PRIMARY KEY, " + UTRAITS_COLUMN_FIRSTNAME + " TEXT, " + UTRAITS_COLUMN_LASTNAME +
            " TEXT, " + UTRAITS_COLUMN_HEIGHT + " INTEGER, " + UTRAITS_COLUMN_WEIGHT + " INTEGER, " +
            UTRAITS_COLUMN_AGE + " INTEGER, " + UTRAITS_COLUMN_GENDER + " TEXT);";

    public DatabaseHelper (Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    //Create the various tables, add table here as required
    public void onCreate(SQLiteDatabase db){
        db.execSQL(CREATE_TABLE_USERS);
        db.execSQL(CREATE_TABLE_LOOKUPFOOD);
        db.execSQL(CREATE_TABLE_USERTRAITS);
    }

    //Add drop table per table in db
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_LOOKUPFOOD);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERTRAITS);
        onCreate(db);
    }

    //Add execSQL statement per db table
    public String delete(SQLiteDatabase db) {
        String successUsers = " Delete " + TABLE_USERS + " Successful!";
        String successLookupFood = " Delete " + TABLE_LOOKUPFOOD + " Successful!";
        String successUserTraits = " Delete " + TABLE_USERTRAITS + " Successful!";
        try{
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_LOOKUPFOOD);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERTRAITS);
            return successUsers + successLookupFood + successUserTraits;
        }catch (SQLiteException e){
            System.out.printf("%s\n\n No Table to delete",e);
        }
        return "DELETING OF TABLES FAILED!";
    }

    //This method is nessesary to clear the USERTRAITS TABLE each time a user updates their details.
    //Mainly because if we don't we'll get duplicate entries in the UserTraits.txt and the read statement
    //will have issues.
    public void recreateUserTraits (){
        SQLiteDatabase db = this.getWritableDatabase();
        try{
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERTRAITS);
            db.execSQL(CREATE_TABLE_USERTRAITS);
        } catch (SQLiteException e){
            System.out.print(e.toString());
        }
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

    //Inserts a User traits into TABLE USERTRAITS
    //TODO Add feature to enable user to create account
    public boolean insertUserTraits(User user) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("ID", user.id);
        contentValues.put("Firstname", user.firstName);
        contentValues.put("Lastname", user.lastName);
        contentValues.put("Height", user.height);
        contentValues.put("Weight", user.weight);
        contentValues.put("Age", user.age);
        contentValues.put("Gender", user.gender);
        db.insert(TABLE_USERTRAITS, null, contentValues);
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

    public boolean getUserTraits(int userID) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("SELECT * FROM " + TABLE_USERTRAITS + " WHERE ID=?",
                new String[]{String.valueOf(userID)});
        if(res.moveToFirst()) {
            res.close();
            db.close();
            return true;
        }
        else {
            System.out.print("USER TRAITS AREN'T FOUND");
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
        db.update(TABLE_LOOKUPFOOD, contentValues, "id = ? ", new String[]{String.valueOf(id)});
        return true;
    }

    public boolean updateUserTraits(Integer userId, User user) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("ID", userId);
        contentValues.put("Firstname", user.firstName);
        contentValues.put("Lastname", user.lastName);
        contentValues.put("Height", user.height);
        contentValues.put("Weight", user.weight);
        contentValues.put("Age", user.age);
        contentValues.put("Gender", user.gender);
        db.update(TABLE_USERTRAITS, contentValues, "id = ? ", new String[]{String.valueOf(userId)});
        return true;
    }

    /* GET ALL TABLE ITEMS
    GET FOOD
*/
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
    }


        // GET USER SEARCHED ITEMS
    public ArrayList<FoodItem> userSearch(String searchResult) {
        String select = "SELECT * FROM " + TABLE_LOOKUPFOOD + "WHERE Name LIKE" + "'%" + searchResult + "%'";
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

    public void saveDataToUserTraitsTable(Context context, String filename, int userId,
                                          User user) throws IOException {

        ArrayList<String> allUserTraits= new ArrayList<String>(); //ArrayList to hold lines of txt file
        ArrayList<User> userTraitsInTxtFile = new ArrayList<User>();
        String userTraits; //Line of txt form txt file

        /* This method is slightly complicated and needs to be done in a few steps.
        1. Read all the current UserTraits.txt content
        2. Save that content as Users in userTraitsInTxtFile ArrayList
        3. Open a new OutputStreamWriter and write blank data to it HOPEFULLY CLEARING THE FILE so
            we can start fresh again.
        4. Re open the OutputStreamWriter and write all lines to file.
        5. Delete current UserTraits table
        6. Recreate table and input fresh variables based on those we stored from the origional text
            file, plus the new ones.

        This is all done to attempt to maintain database integrity so we don't get duplicate ID's
        in the text file.

        Ask Ken if further explanation is required.

         */
        //STEP 1
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(context.getAssets()
                    .open(filename)));
            String line;
            while ((line = reader.readLine()) != null) {
                allUserTraits.add(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        //STEP 2
        for (int i=0; i < allUserTraits.size();i++){
            String[] input;
            userTraits = allUserTraits.get(i);
            input = userTraits.split(" ");
            User usersTraits = new User(Integer.parseInt(input[0]),input[1],input[2],Integer.parseInt(input[3])
                    ,Integer.parseInt(input[3]),Integer.parseInt(input[5]), input[6]);
            userTraitsInTxtFile.add(usersTraits);
        }
        userTraitsInTxtFile.add(user);//Add current users updated object to list for writing
        //STEP 3
        try {
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter
                    (context.openFileOutput(filename,context.MODE_PRIVATE));
            outputStreamWriter.write("");
            outputStreamWriter.close();
        } catch (IOException e){
            Log.e("Exception,", " File Write Failed: " + e.toString());

        }
        //STEP 4
        try {
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter
                    (context.openFileOutput(filename,context.MODE_PRIVATE));
            for(int i =0; i< userTraitsInTxtFile.size();i++){
                outputStreamWriter.write(userTraitsInTxtFile.get(i).dbWriteUserTraits());
            }
            outputStreamWriter.close();
        } catch (IOException e){
            Log.e("Exception,", " File Write Failed: " + e.toString());

        }
        //STEP 5
        recreateUserTraits();
        //STEP 6
        for (int i = 0; i < allUserTraits.size(); i++) {
            System.out.printf("%s\n",userTraitsInTxtFile.get(i).toString());
            insertUserTraits(userTraitsInTxtFile.get(i));
            System.out.printf("DB Insert Statement executed successfully");
        }

    }
}
