package bvgiants.diary3;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

/**
 * Created by kenst on 2/05/2016.
 * This is a pretty epic file, but I believe it to be nessesary due to the complexity of the proposed
 * system and lack of access to QUT database to store tables, stored procedures/views.
 */
public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String LOG = "DatabaseHelper";

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME ="HealthDiary";

    //Table Names
    private static final String TABLE_USERS = "Users";
    private static final String TABLE_LOOKUPFOOD = "LookupFood";
    public static final String TABLE_USERTRAITS = "UserTraits";
    public static final String TABLE_FOODCONSUMED = "FoodConsumed";
    public static final String TABLE_ORDERHEADER = "OrderHeader";
    public static final String TABLE_USERGOALS = "UserGoals";

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
    public static final String LUPFOOD_COLUMN_KJ = "Kj";
    public static final String LUPFOOD_COLUMN_PROTEIN = "Protein";
    public static final String LUPFOOD_COLUMN_FAT = "Fat";
    public static final String LUPFOOD_COLUMN_SUGAR = "Sugar";
    public static final String LUPFOOD_COLUMN_SODIUM = "Sodium";


    //UserTraits Table Columns
    public static final String UTRAITS_COLUMN_FIRSTNAME = "Firstname";
    public static final String UTRAITS_COLUMN_LASTNAME = "Lastname";
    public static final String UTRAITS_COLUMN_HEIGHT = "Height";
    public static final String UTRAITS_COLUMN_WEIGHT = "Weight";
    public static final String UTRAITS_COLUMN_AGE = "Age";
    public static final String UTRAITS_COLUMN_GENDER = "Gender";

    //FoodConsumed Table Columns
    public static final String FOODCONSUMED_COLUMN_FOODID = "FoodID";
    public static final String FOODCONSUMED_COLUMN_LOCATION = "Location";

    //OrderHeader Table Columns
    public static final String ORDERHEADER_COLUMN_ORDERID = "OrderID";
    public static final String ORDERHEADER_COLUMN_ORDERTYPECODE = "OrderTypeCode";
    public static final String ORDERHEADER_COLUMN_ORDERDATE = "OrderDate";
    public static final String ORDERHEADER_COLUMN_ORDERTIME = "OrderTime";
    public static final String ORDERHEADER_COLUMN_USERID = "UserID";

    //UserGoals Table Columns
    public static final String USERGOALS_COLUMN_USERID = "UserID";
    public static final String USERGOALS_COLUMN_SUGAR = "Sugar";
    public static final String USERGOALS_COLUMN_STEPS = "Steps";
    public static final String USERGOALS_COLUMN_KILOJOULES = "Kilojoules";
    public static final String USERGOALS_COLUMN_CALORIES = "Calories";


    //Table Create Statements
    private static final String CREATE_TABLE_USERS = "CREATE TABLE " + TABLE_USERS + "(" + KEY_ID +
            " INTEGER PRIMARY KEY, " + USERS_COLUMN_EMAIL + " TEXT," + USERS_COLUMN_PW +
            " TEXT," + USERS_COLUMN_ALIAS + " TEXT, " + USERS_COLUMN_TEAM + " TEXT);";

    private static final String CREATE_TABLE_LOOKUPFOOD = "CREATE TABLE " + TABLE_LOOKUPFOOD + "(" +
            KEY_ID + " INTEGER PRIMARY KEY," + LUPFOOD_COLUMN_NAME + " TEXT, "
            + LUPFOOD_COLUMN_KJ + " INTEGER, " + LUPFOOD_COLUMN_CALORIES + " INTEGER, " +
            LUPFOOD_COLUMN_PROTEIN + " INTEGER, "+ LUPFOOD_COLUMN_FAT + " INTEGER, "
            + LUPFOOD_COLUMN_SUGAR + " INTEGER, "   + LUPFOOD_COLUMN_SODIUM + " INTEGER);";

    private static final String CREATE_TABLE_USERTRAITS = "CREATE TABLE " + TABLE_USERTRAITS + "(" + KEY_ID
            + " INTEGER PRIMARY KEY, " + UTRAITS_COLUMN_FIRSTNAME + " TEXT, " + UTRAITS_COLUMN_LASTNAME +
            " TEXT, " + UTRAITS_COLUMN_HEIGHT + " INTEGER, " + UTRAITS_COLUMN_WEIGHT + " INTEGER, " +
            UTRAITS_COLUMN_AGE + " INTEGER, " + UTRAITS_COLUMN_GENDER + " TEXT);";

    private static final String CREATE_TABLE_FOODCONSUMED = "CREATE TABLE " + TABLE_FOODCONSUMED + "(" +
            KEY_ID + " INTEGER, " + FOODCONSUMED_COLUMN_FOODID + " INTEGER, " + FOODCONSUMED_COLUMN_LOCATION
            + " TEXT);";

    public static final String CREATE_TABLE_ORDERHEADER = "CREATE TABLE " + TABLE_ORDERHEADER + "(" +
            ORDERHEADER_COLUMN_ORDERID + " INTEGER PRIMARY KEY, " + ORDERHEADER_COLUMN_ORDERTYPECODE
            + " INTEGER, " + ORDERHEADER_COLUMN_ORDERDATE + " DATETIME DEFAULT CURRENT_TIMESTAMP, " +
            ORDERHEADER_COLUMN_ORDERTIME + " DATETIME DEFAULT CURRENT_TIMESTAMP, "
            + ORDERHEADER_COLUMN_USERID + " INTEGER);";

    private static final String CREATE_TABLE_USERGOALS = "CREATE TABLE " + TABLE_USERGOALS + " (" +
            USERGOALS_COLUMN_USERID + " INTEGER PRIMARY KEY, " + USERGOALS_COLUMN_SUGAR + " INTEGER, "
            + USERGOALS_COLUMN_STEPS + " INTEGER, " + USERGOALS_COLUMN_KILOJOULES + " INTEGER, " +
            USERGOALS_COLUMN_CALORIES + " INTEGER);";

    //To save creating table LOOKUPORDERTYPE, the below variables take the place of the table.
    //add more as required.

    private static final int LOOKUPORDERTYPE_FOODENTRY = 1;
    private static final int LOOKUPORDERTYPE_LOCATIONENTRY = 2;


    public DatabaseHelper (Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    //Create the various tables, add table here as required
    public void onCreate(SQLiteDatabase db){
        db.execSQL(CREATE_TABLE_USERS);
        db.execSQL(CREATE_TABLE_LOOKUPFOOD);
        db.execSQL(CREATE_TABLE_USERTRAITS);
        db.execSQL(CREATE_TABLE_FOODCONSUMED);
        db.execSQL(CREATE_TABLE_ORDERHEADER);
        db.execSQL(CREATE_TABLE_USERGOALS);
    }

    //Add drop table per table in db
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_LOOKUPFOOD);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERTRAITS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_FOODCONSUMED);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ORDERHEADER);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERGOALS);
        onCreate(db);
    }

    //Add execSQL statement per db table
    public String delete() {
        SQLiteDatabase db = this.getWritableDatabase();
        try{
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_LOOKUPFOOD);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERTRAITS);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_FOODCONSUMED);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_ORDERHEADER);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERGOALS);
            return "DELETING OF ALL DB TABLES SUCCESSFUL";
        }catch (SQLiteException e){
            System.out.printf("%s\n\n No Table to delete. ERROR! ",e);
        }
        return "DELETING OF TABLES FAILED!";
    }

    //This method is nessesary to clear the XXXX TABLE each time a mainly because if we don't we'll
    //get duplicate entries in the UserTraits.txt and the read statement will have issues.
    public String recreateUserTraits (){
        SQLiteDatabase db = this.getWritableDatabase();
        try{
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERTRAITS);
            db.execSQL(CREATE_TABLE_USERTRAITS);
            return "DELETING OF USERTRAITS SUCCESS!";
        } catch (SQLiteException e){
            System.out.print(e.toString());
            return "DELETING OF USERTRAITS FAILED!";
        }
    }

    public void recreateFoodConsumed (){
        SQLiteDatabase db = this.getWritableDatabase();
        try{
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_FOODCONSUMED);
            db.execSQL(CREATE_TABLE_FOODCONSUMED);
        } catch (SQLiteException e){
            System.out.print(e.toString());
        }
    }

    public void recreateOrderHeader (){
        SQLiteDatabase db = this.getWritableDatabase();
        try{
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_ORDERHEADER);
            db.execSQL(CREATE_TABLE_ORDERHEADER);
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
    public boolean insertUser( User user) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("ID", user.getId());
        contentValues.put("EmailAdd", user.getEmail());
        contentValues.put("Password", user.getPassword());
        contentValues.put("Alias", user.getAlias());
        contentValues.put("Team", "ATeam");//Set as default for now as we don't have team registration
        db.insert(TABLE_USERS, null, contentValues);
        return true;
    }

    //Inserts a Food into LOOKUPFOOD
    //@// TODO: 8/05/2016 MAKE THIS CONSTRUCTER TAKE A FOOD ITEM!
    public boolean insertFood(int id, String name, int calories, int sugar, int fat, int kj, int sodium,
                              int protein) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("ID", id);
        contentValues.put("Name", name);
        contentValues.put("Kj", kj);
        contentValues.put("Calories", calories);
        contentValues.put("Protein", protein);
        contentValues.put("Fat", fat);
        contentValues.put("Sugar", sugar);
        contentValues.put("Sodium", sodium);
        db.insert(TABLE_LOOKUPFOOD, null, contentValues);
        return true;
    }

    //Inserts a User traits into TABLE USERTRAITS
    //TODO Add feature to enable user to create account
    public boolean insertUserTraits(User user) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("ID", user.getId());
        contentValues.put("Firstname", user.getFirstName());
        contentValues.put("Lastname", user.getLastName());
        contentValues.put("Height", user.getHeight());
        contentValues.put("Weight", user.getWeight());
        contentValues.put("Age", user.getAge());
        contentValues.put("Gender", user.getGender());
        db.insert(TABLE_USERTRAITS, null, contentValues);
        Log.v("INSERT TRAITS SUCCESS", "HAPPY DAYS");
        return true;
    }

    public boolean insertFoodConsumed(FoodItem food) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("ID", food.getOrderID());
        contentValues.put("FoodID", food.getFoodId());
        contentValues.put("Location", food.getLocation());
        Log.v(food.toString(), "FOOD INSERTED");
        db.insert(TABLE_FOODCONSUMED, null, contentValues);
        Log.v("AFTER DB INSERT ", "FOOD INSERTED");
        return true;
    }

    public boolean insertOrderHeader(int orderID,int orderTypeCode, String orderDate,
                                     String orderTime,int userID) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("OrderID", orderID);
        contentValues.put("OrderTypeCode", orderTypeCode);
        contentValues.put("OrderDate", orderDate);
        contentValues.put("OrderTime", orderTime);
        contentValues.put("UserID", userID);
        db.insert(TABLE_ORDERHEADER, null, contentValues);
        return true;
    }

    public boolean insertUserGoals(User user){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("UserID", user.getId());
        contentValues.put("Sugar", user.getSugarGoal());
        contentValues.put("Steps", user.getStepGoal());
        contentValues.put("Kilojoules", user.getKilojoulesGoal());
        contentValues.put("Calories", user.getCalorieGoal());
        db.insert(TABLE_USERGOALS, null, contentValues);
        Log.v("INSERT GOALS SUCCESS", "HAPPY DAYS");
        return true;
    }

    /* GET STATEMENTS
    GET USER
    GET FOOD
     */

    public User getUserGoals(int userID){
        SQLiteDatabase db = this.getReadableDatabase();
        Log.v("GOAL ACTIVITY USERID= ", String.valueOf(userID));
        User user;
        Cursor res = db.rawQuery("SELECT * FROM " + TABLE_USERGOALS + " WHERE UserID=?",
                new String[]{String.valueOf(userID)});
        if(res.moveToFirst()) {
            user = new User (res.getInt(0),res.getInt(1), res.getInt(2),
                    res.getInt(3), res.getInt(4));
            Log.v("USER GOALS FOUND", user.userGoals());
            res.close();
            db.close();
        }
        else {
            Log.v("USER GOALS AREN'T FOUND", "ERROR");
            user = new User (0,0,0,0,0);
            res.close();
            db.close();
        }
        return user;
    }

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

    public User loggedInUser(String email, String pw){
        SQLiteDatabase db = this.getReadableDatabase();
        User user;
        Cursor res = db.rawQuery("SELECT * FROM " + TABLE_USERS + " WHERE EmailAdd=? AND Password=?",
                new String[]{email,pw});
        if(res.moveToFirst()) {
            user = new User (res.getInt(0),res.getString(1), res.getString(2),
                    res.getString(3), res.getString(4));
            res.close();
            db.close();
            return user;
        }
        else {
            System.out.print("USER ISN'T FOUND");
            res.close();
            db.close();
            return null;
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

    public User getUserTraits(int userID) {
        SQLiteDatabase db = this.getReadableDatabase();
        Log.v("getUserTraits USERID= ", String.valueOf(userID));
        User user;
        Cursor res = db.rawQuery("SELECT * FROM " + TABLE_USERTRAITS + " WHERE ID=?",
                new String[]{String.valueOf(userID)});
        if(res.moveToFirst()) {
            user = new User (res.getInt(0),res.getString(1), res.getString(2),
                    res.getInt(3), res.getInt(4),res.getInt(5), res.getString(6));
            Log.v("USER TRAITS FOUND", user.userTraits());
            res.close();
            db.close();
        }
        else {
            Log.v("TRAITS AREN'T FOUND", "ERROR");
            user = new User (0,"","",0,0,0,"");
            res.close();
            db.close();
        }
        return user;
    }

    public boolean getFoodConsumed(int orderID) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("SELECT * FROM " + TABLE_FOODCONSUMED + " WHERE ID=?",
                new String[]{String.valueOf(orderID)});
        if(res.moveToFirst()) {
            res.close();
            db.close();
            return true;
        }
        else {
            System.out.print("FOOD CONSUMED WASNT FOUND");
            res.close();
            db.close();
            return false;
        }
    }

    /* UPDATE STATEMENTS
    UPDATE USER
    UPDATE FOOD
    @todo use Objects rather than construction method!
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
        contentValues.put("Energy", energy);
        contentValues.put("Protein", protein);
        contentValues.put("Fat", fat);
        contentValues.put("Sugar", sugar);
        contentValues.put("Sodium", sodium);

        db.update(TABLE_LOOKUPFOOD, contentValues, "id = ? ", new String[]{String.valueOf(id)});
        return true;
    }

    public boolean updateUserTraits(Integer userId, User user) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("ID", userId);
        contentValues.put("Firstname", user.getFirstName());
        contentValues.put("Lastname", user.getLastName());
        contentValues.put("Height", user.getHeight());
        contentValues.put("Weight", user.getWeight());
        contentValues.put("Age", user.getAge());
        contentValues.put("Gender", user.getGender());
        db.update(TABLE_USERTRAITS, contentValues, " ID=? ", new String[]{String.valueOf(userId)});
        return true;
    }

    public boolean updateUserGoals(Integer userId, User user) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("UserID", userId);
        contentValues.put("Sugar", user.getSugarGoal());
        contentValues.put("Steps", user.getStepGoal());
        contentValues.put("Kilojoules", user.getKilojoulesGoal());
        contentValues.put("Calories", user.getCalorieGoal());
        db.update(TABLE_USERGOALS, contentValues, " UserID=? ", new String[]{String.valueOf(userId)});
        return true;
    }

    /*GET ALL TABLE ITEMS
    GET FOOD
    */
    public ArrayList<FoodItem> allFood(){

        String select = "SELECT * FROM " + TABLE_LOOKUPFOOD;
        ArrayList<FoodItem> results = new ArrayList<FoodItem>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery(select,null);
        Log.e("FOOD = ", "ALL FOOD");
        if(res.moveToFirst()){
            do {
                FoodItem food = new FoodItem(res.getInt(0),res.getString(1),res.getInt(2),res.getInt(3),res.getInt(4),
                        res.getInt(5),res.getInt(6),res.getInt(7));
                Log.e("FOOD = ", food.dbWriteFoodConsumed());
                results.add(food);
            }while(res.moveToNext());
            if(res != null && !res.isClosed())
                res.close();
        }
        return results;
    }

    // GET USER SEARCHED ITEMS
    public ArrayList<FoodItem> foodSearch(String searchResult) {
        String select = "SELECT * FROM " + TABLE_LOOKUPFOOD + " WHERE Name LIKE " + searchResult + "%";

        ArrayList<FoodItem> results = new ArrayList<FoodItem>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("SELECT * FROM " + TABLE_LOOKUPFOOD + " WHERE Name LIKE ?",
                new String[]{searchResult + "%"});
        if(res.moveToFirst()){
            do {
                FoodItem food = new FoodItem(res.getInt(0),res.getString(1),res.getInt(2),res.getInt(3),res.getInt(4),
                        res.getInt(5),res.getInt(6),res.getInt(7));
                results.add(food);
                Log.v("FOOD SEARCH FOUND: ", food.getName());
            }while(res.moveToNext());
            if(res != null && !res.isClosed())
                res.close();
        }
        res.close();
        return results;
    }

        // GET USER SEARCHED ITEMS
    public ArrayList<FoodItem> userSearch(String searchResult) {
        String select = "SELECT * FROM " + TABLE_LOOKUPFOOD + "WHERE Name LIKE "  + searchResult + "%";

        ArrayList<FoodItem> results = new ArrayList<FoodItem>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery(select,null);
        if(res.moveToFirst()){
            do {
                FoodItem food = new FoodItem(res.getInt(0),res.getString(1),res.getInt(2),res.getInt(3),res.getInt(4),
                        res.getInt(5),res.getInt(6),res.getInt(7));
                results.add(food);
            }while(res.moveToNext());
            if(res != null && !res.isClosed())
                res.close();
        }
        return results;
    }

    public ArrayList<OrderRow> getAllUsersFoodConsumed(int userID) {
        Log.v("USER ID = ", String.valueOf(userID));
        ArrayList<OrderRow> allResults = new ArrayList<OrderRow>();
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT oh.OrderDate, oh.OrderTime, fc.FoodID, fc.Location" +
                " FROM " + TABLE_ORDERHEADER + " oh" +
                " LEFT OUTER JOIN "+ TABLE_FOODCONSUMED + " fc" +
                " ON oh.OrderID = fc.ID";
        //String query = "SELECT * FROM " + TABLE_ORDERHEADER + " WHERE UserID= " +
          //     userID + " AND ORDERTYPECODE= " + LOOKUPORDERTYPE_FOODENTRY;
        Cursor res = db.rawQuery(query, null);
        if(res.moveToFirst()) {
            do {
                OrderRow entry = new OrderRow(res.getString(0), res.getString(1), res.getInt(2),
                        res.getString(3));
                allResults.add(entry);
            }while(res.moveToNext());

        }
        else {
            System.out.print("ORDERHEADER GET USERORDERS WASNT FOUND");
            res.close();
            db.close();
        }
        res.close();
        db.close();
        return allResults;
    }

    public ArrayList<OrderRow> NEWgetAllUsersFoodConsumed(int userID) {
        ArrayList<OrderRow> allResults = new ArrayList<OrderRow>();
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM " + TABLE_FOODCONSUMED + " WHERE UserID= " +
                userID;
            Cursor res = db.rawQuery(query, null);
            if(res.moveToFirst()) {
            OrderRow entry = new OrderRow(res.getInt(0), res.getInt(1),res.getString(2),
                    res.getInt(3));
            allResults.add(entry);
            res.close();
            db.close();
        }
        else {
            System.out.print("ORDERHEADER GET USERORDERS WASNT FOUND");
            res.close();
            db.close();
        }
        return allResults;
    }

    public ArrayList<OrderRow> getAllOrders(){
        ArrayList<OrderRow> allRows = new ArrayList<OrderRow>();
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM " + TABLE_ORDERHEADER;
        Cursor res = db.rawQuery(query, null);
        if(res.moveToFirst()) {
            do {
                OrderRow entry = new OrderRow(res.getInt(0), res.getInt(1), res.getString(2),
                        res.getInt(3));
                allRows.add(entry);
            }while(res.moveToNext());
        }
        else {
            System.out.print("ORDERHEADER GET ALL ORDERS WASNT FOUND");
            res.close();
            db.close();
        }
        res.close();
        db.close();
        return allRows;
    }

    public String getUserAlias (int userID){
        SQLiteDatabase db = this.getReadableDatabase();
        String alias;
        String query = "SELECT Alias FROM " + TABLE_USERS + " WHERE ID=?";
        Cursor res = db.rawQuery(query, new String[]{String.valueOf(userID)});
        if(res.moveToFirst()) {
            alias = res.getString(0);
        }
        else {
            alias = "Unknown User";
            System.out.print("ORDERHEADER GET ALL ORDERS WASNT FOUND");
            res.close();
            db.close();
        }
        res.close();
        db.close();
        return alias;
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
            User newUser = new User(Integer.parseInt(input[0]),input[1],input[2],input[3],input[4]);
            insertUser(newUser);
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
            Log.v("input0 = ", input[0]);Log.v("input0 = ", input[1]);Log.v("input0 = ", input[2]);Log.v("input0 = ", input[3]);
            Log.v("input0 = ", input[4]);Log.v("input0 = ", input[5]);Log.v("input0 = ", input[6]);Log.v("input0 = ", input[7]);
            insertFood(Integer.parseInt(input[0]),input[1],Integer.parseInt(input[2]),Integer.parseInt(input[3])
                    ,Integer.parseInt(input[4]),Integer.parseInt(input[5]),Integer.parseInt(input[6]),
                    Integer.parseInt(input[7]));
            System.out.printf("DB LOOKUPFOOD Insert Statement executed successfully");
        }

    }

    /*
    SAVE DATA TO VARIOUS SQLITE DB TABLES.
    NOTE: IT IS IMPOSSIBLE TO SAVE THESE DB TABLES TO TXT FILES, AS ANYTHING IN THE ASSESTS FOLDER
    CANNOT BE MODIFIED AFTER RUNTIME!

    ONLY OPTION WOULD BE TO SAVE A TXT FILE ON THE LOCAL DEVICE OR USER SERVER HOSTED DB.
      */
    public void saveDataToUserTraits(Context context, String filename, int userId,
                                     User user) throws IOException {

            System.out.printf("%s\n",user.toString());
            insertUserTraits(user);
            System.out.printf("DB USERTRAITS TABLE Insert Statement executed successfully");

    }

    public void saveDataToFoodConsumedTable(ArrayList<FoodItem> recordedFoodEaten, int userID)
            throws IOException {

        //Create a new orderID

        //Create Order @// TODO: 9/05/2016 CURRENT JUST ADDS KEN USER
        int orderID = 0;

        for(int i = 0; i < recordedFoodEaten.size();i++){
            orderID = createOrderID();
            saveDataToOrderHeader(orderID,LOOKUPORDERTYPE_FOODENTRY,justGetDate(), justGetTime(),userID);
            recordedFoodEaten.get(i).setOrderID(orderID);
            recordedFoodEaten.get(i).setLocation("FAKE LOCATION");
            Log.v(recordedFoodEaten.get(i).dbWriteFoodConsumed(), "WRITE TO DB");
            insertFoodConsumed(recordedFoodEaten.get(i));
        }

    }

    public void saveDataToOrderHeader(int orderID, int orderTypeCode, String date, String time,
                                      int userID) throws IOException {

            insertOrderHeader(orderID,orderTypeCode,date,time,userID);

    }

    public Cursor searchLookupFood (String inputText) throws SQLiteException {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("SELECT * FROM " + TABLE_LOOKUPFOOD + " WHERE Name LIKE=?",
                new String[]{inputText,"%"});

        if(res != null){
            res.moveToFirst();
        }
        return res;
    }


    public ArrayList<OrderRow> showTodaysFood(int userID) throws SQLiteException{
        SQLiteDatabase db = this.getReadableDatabase();
        //Select * from OrderHeader
        //left outer join FoodConsumed on OrderHeader.OrderID = FoodConsumed.ID
        //where OrderHeader.Date = todays date
        String date = justGetDate();
        ArrayList<OrderRow> allRows = new ArrayList<OrderRow>();
        String query = "SELECT oh.OrderDate, oh.OrderTime, fc.FoodID, fc.Location" +
                " FROM " + TABLE_ORDERHEADER + " oh" +
                " LEFT OUTER JOIN "+ TABLE_FOODCONSUMED + " fc" +
                " ON oh.OrderID = fc.ID" +
                " WHERE oh.OrderDate=?" +
                " AND oh.UserID=?";
        //= date('now','-1 day');";

        Cursor res = db.rawQuery(query, new String[]{date,String.valueOf(userID)});

        if(res.moveToFirst()) {
            do {
                OrderRow entry = new OrderRow(res.getString(0), res.getString(1), res.getInt(2),
                        res.getString(3));
                //Log.v("RES 0 " , res.getString(0));
                //Log.v("RES 1 ", res.getString(1));
                //Log.v("RES 2 ", res.getString(2));
                //Log.v("RES 3 ", res.getString(3));
                allRows.add(entry);

            }while(res.moveToNext());
        }
        else {
            System.out.print("ORDERHEADER GET ALL ORDERS WASNT FOUND");
            res.close();
            db.close();
        }
        res.close();
        db.close();
        return allRows;
    }

    public ArrayList<OrderRow> allUserFoodOrders(int userID) throws SQLiteException{
        SQLiteDatabase db = this.getReadableDatabase();
        //Select * from OrderHeader
        //left outer join FoodConsumed on OrderHeader.OrderID = FoodConsumed.ID
        //where OrderHeader.Date = todays date
        String date = justGetDate();
        String pastDate = getLastWeek();
        Log.v("LastWeek", pastDate);
        ArrayList<OrderRow> allRows = new ArrayList<OrderRow>();
        String query = "SELECT oh.OrderDate, oh.OrderTime, fc.FoodID, fc.Location" +
                " FROM " + TABLE_ORDERHEADER + " oh" +
                " LEFT OUTER JOIN "+ TABLE_FOODCONSUMED + " fc" +
                " ON oh.OrderID = fc.ID" +
                " WHERE oh.UserID=?";
        String query2 = "SELECT oh.OrderDate, oh.OrderTime, fc.FoodID, fc.Location" +
                " FROM " + TABLE_ORDERHEADER + " oh" +
                " LEFT OUTER JOIN "+ TABLE_FOODCONSUMED + " fc" +
                " ON oh.OrderID = fc.ID" +
                " WHERE oh.OrderDate BETWEEN " + date + " AND " + pastDate +
                " AND oh.UserID=" + userID;
        //= date('now','-1 day');";

        Cursor res = db.rawQuery(query, new String[]{String.valueOf(userID)});
        //Cursor res = db.rawQuery(query2, null);
        if(res.moveToFirst()) {
            do {
                OrderRow entry = new OrderRow(res.getString(0), res.getString(1), res.getInt(2),
                        res.getString(3));
                Log.v("RES 0 " , res.getString(0));
                Log.v("RES 1 ", res.getString(1));
                Log.v("RES 2 ", res.getString(2));
                Log.v("RES 3 ", res.getString(3));
                allRows.add(entry);

            }while(res.moveToNext());
        }
        else {
            System.out.print("ORDERHEADER GET ALL ORDERS WASNT FOUND");
            res.close();
            db.close();
        }
        res.close();
        db.close();
        return allRows;
    }


    /*
    HELPER METHODS
     */
    public String getDateTime() {
        SimpleDateFormat dateFormat = new SimpleDateFormat(
                "yyyy-MM-dd hh:mm", Locale.getDefault());
        Date date = new Date();
        return dateFormat.format(date);
    }

    public String getLastWeek(){
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        Date date = new Date();
        return  dateFormat.format(date.getTime() - 604800000L);
    }

    public String justGetDate(){
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        Date date = new Date();
        return  dateFormat.format(date);
    }

    public String justGetTime(){
        SimpleDateFormat dateFormat = new SimpleDateFormat("hh:mm", Locale.getDefault());
        Date date = new Date();
        return  dateFormat.format(date);
    }

    public int createOrderID(){
        int orderID = (int) (Math.random() * 10000 + 1);
        return orderID;
    }

}
