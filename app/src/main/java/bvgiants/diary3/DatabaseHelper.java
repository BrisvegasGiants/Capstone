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
import java.io.OutputStreamWriter;
import java.util.ArrayList;

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

    //FoodConsumed Table Columns
    public static final String FOODCONSUMED_COLUMN_FOODID = "FoodID";
    public static final String FOODCONSUMED_COLUMN_LOCATION = "Location";

    //OrderHeader Table Columns
    public static final String ORDERHEADER_COLUMN_ORDERID = "OrderID";
    public static final String ORDERHEADER_COLUMN_ORDERTYPECODE = "OrderTypeCode";
    public static final String ORDERHEADER_COLUMN_ORDERDATE = "OrderDate";
    public static final String ORDERHEADER_COLUMN_USERID = "UserID";

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

    private static final String CREATE_TABLE_FOODCONSUMED = "CREATE TABLE " + TABLE_FOODCONSUMED + "(" +
            KEY_ID + " INTEGER, " + FOODCONSUMED_COLUMN_FOODID + " INTEGER, " + FOODCONSUMED_COLUMN_LOCATION
            + " TEXT);";

    public static final String CREATE_TABLE_ORDERHEADER = "CREATE TABLE " + TABLE_ORDERHEADER + "(" +
            ORDERHEADER_COLUMN_ORDERID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + ORDERHEADER_COLUMN_ORDERTYPECODE
            + " INTEGER, " + ORDERHEADER_COLUMN_ORDERDATE + " DATETIME DEFAULT CURRENT_TIMESTAMP, " +
            ORDERHEADER_COLUMN_USERID + " INTEGER);";

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
    }

    //Add drop table per table in db
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_LOOKUPFOOD);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERTRAITS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_FOODCONSUMED);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ORDERHEADER);
        onCreate(db);
    }

    //Add execSQL statement per db table
    public String delete(SQLiteDatabase db) {

        try{
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_LOOKUPFOOD);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERTRAITS);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_ORDERHEADER);
            return "DELETING OF ALL DB TABLES SUCCESSFUL";
        }catch (SQLiteException e){
            System.out.printf("%s\n\n No Table to delete. ERROR! ",e);
        }
        return "DELETING OF TABLES FAILED!";
    }

    //This method is nessesary to clear the XXXX TABLE each time a mainly because if we don't we'll
    //get duplicate entries in the UserTraits.txt and the read statement will have issues.
    public void recreateUserTraits (){
        SQLiteDatabase db = this.getWritableDatabase();
        try{
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERTRAITS);
            db.execSQL(CREATE_TABLE_USERTRAITS);
        } catch (SQLiteException e){
            System.out.print(e.toString());
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

    public boolean insertFoodConsumed(FoodItem food) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("ID", food.orderID);
        contentValues.put("FoodID", food.foodId);
        contentValues.put("Location", food.location);
        db.insert(TABLE_FOODCONSUMED, null, contentValues);
        return true;
    }

    public boolean insertOrderHeader(int orderTypeCode, String orderDate, int userID) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("OrderTypeCode", orderTypeCode);
        contentValues.put("OrderDate", orderDate);
        contentValues.put("UserID", userID);
        db.insert(TABLE_ORDERHEADER, null, contentValues);
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

    /*GET ALL TABLE ITEMS
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
    public ArrayList<FoodItem> foodSearch(String searchResult) {
        String select = "SELECT * FROM " + TABLE_LOOKUPFOOD + " WHERE Name LIKE " + searchResult + "%;";
        System.out.printf("%s",select);
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

    public ArrayList<OrderRow> getAllUsersFoodConsumed(int userID) {
        ArrayList<OrderRow> allResults = new ArrayList<OrderRow>();
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM " + TABLE_ORDERHEADER + " WHERE UserID= " +
                userID + " AND ORDERTYPECODE= " + LOOKUPORDERTYPE_FOODENTRY;
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
            OrderRow entry = new OrderRow(res.getInt(0), res.getInt(1),res.getString(2),
                    res.getInt(3));
            allRows.add(entry);
            res.close();
            db.close();
        }
        else {
            System.out.print("ORDERHEADER GET ALL ORDERS WASNT FOUND");
            res.close();
            db.close();
        }
        return allRows;
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
            System.out.printf("DB LOOKUPFOOD Insert Statement executed successfully");
        }

    }

    /*THE FOLLOWING METHODS APPLY TO THIS RULE AS THEY WRITE TO TEXT FILES WHICH DEPEND ON DATA WRITTEN
     BY OTHER SQL QUERIES!
    This method is slightly complicated and needs to be done in a few steps.
        1. Read all the current UserTraits.txt content
        2. Save that content as Users in userTraitsInTxtFile ArrayList
        3. Open a new OutputStreamWriter and write blank data to it HOPEFULLY CLEARING THE FILE so
            we can start fresh again.
        4. Delete current UserTraits table
        5. Recreate table and input fresh variables based on those we stored from the origional text
           file, plus the new ones.
        6. Re open the OutputStreamWriter and write all lines to file.



        This is all done to attempt to maintain database integrity so we don't get duplicate ID's
        in the text file.

        Ask Ken if further explanation is required.
      */
    public void saveDataToUserTraitsTable(Context context, String filename, int userId,
                                          User user) throws IOException {

        ArrayList<String> allUserTraits= new ArrayList<String>(); //ArrayList to hold lines of txt file
        ArrayList<User> userTraitsInTxtFile = new ArrayList<User>();
        String userTraits; //Line of txt form txt file

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
            Log.e("Exception,", " File USERTRAITS Write Failed: " + e.toString());

        }
        //STEP 4
        recreateUserTraits();
        //STEP 5
        for (int i = 0; i < allUserTraits.size(); i++) {
            System.out.printf("%s\n",userTraitsInTxtFile.get(i).toString());
            insertUserTraits(userTraitsInTxtFile.get(i));
            System.out.printf("DB USERTRAITS TABLE Insert Statement executed successfully");
        }

        //STEP 6
        try {
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter
                    (context.openFileOutput(filename,context.MODE_PRIVATE));
            for(int i =0; i< userTraitsInTxtFile.size();i++){
                outputStreamWriter.write(userTraitsInTxtFile.get(i).dbWriteUserTraits());
            }
            outputStreamWriter.close();
        } catch (IOException e){
            Log.e("Exception,", " File USERTRAITS Write Failed: " + e.toString());

        }


    }

    public void saveDataToFoodConsumedTable(Context context, String filename, FoodItem
                                            recordedFoodEaten) throws IOException {

        ArrayList<String> allFoodConsumed= new ArrayList<String>(); //ArrayList to hold lines of txt file
        ArrayList<FoodItem> foodConsumedinFile = new ArrayList<FoodItem>();
        String foodConsumed; //Line of txt form txt file

        //STEP 1
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(context.getAssets()
                    .open(filename)));
            String line;
            while ((line = reader.readLine()) != null) {
                allFoodConsumed.add(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        //STEP 2
        for (int i=0; i < allFoodConsumed.size();i++){
            String[] input;
            foodConsumed = allFoodConsumed.get(i);
            input = foodConsumed.split(" ");
            FoodItem foodItem = new FoodItem(Integer.parseInt(input[0]),Integer.parseInt(input[1]),
                    input[2]);
            foodConsumedinFile.add(foodItem);
        }
        foodConsumedinFile.add(recordedFoodEaten);//Add current FoodItem updated object to list for writing
        //STEP 3
        try {
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter
                    (context.openFileOutput(filename,context.MODE_PRIVATE));
            outputStreamWriter.write("");
            outputStreamWriter.close();
        } catch (IOException e){
            Log.e("Exception,", " File FOOD CONSUMED Write Failed: " + e.toString());

        }
        //STEP 4
        recreateFoodConsumed();
        //STEP 5
        for (int i = 0; i < foodConsumedinFile.size(); i++) {
            System.out.printf("%s\n",foodConsumedinFile.get(i).toString());
            insertFoodConsumed(foodConsumedinFile.get(i));
            System.out.printf("DB CONSUMED FOOD Insert Statement executed successfully");
        }


        //STEP 6
        try {
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter
                    (context.openFileOutput(filename,context.MODE_PRIVATE));
            for(int i =0; i< foodConsumedinFile.size();i++){
                outputStreamWriter.write(foodConsumedinFile.get(i).dbWriteFoodConsumed());
            }
            outputStreamWriter.close();
        } catch (IOException e){
            Log.e("Exception,", " File FOOD CONSUMED Write Failed: " + e.toString());

        }
    }

    public void saveDataToOrderHeader(Context context, String filename, int orderTypeCode, String date,
                                      int userID) throws IOException {

        ArrayList<String> allOrders= new ArrayList<String>(); //ArrayList to hold lines of txt file
        ArrayList<OrderRow> ordersPlacedInFile = new ArrayList<OrderRow>();
        String orderLineFromFile; //Line of txt form txt file

        //STEP 1
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(context.getAssets()
                    .open(filename)));
            String line;
            while ((line = reader.readLine()) != null) {
                allOrders.add(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        //STEP 2
        for (int i=0; i < allOrders.size();i++){
            String[] input;
            orderLineFromFile = allOrders.get(i);
            input = orderLineFromFile.split(" ");
            OrderRow orderRow = new OrderRow(Integer.parseInt(input[0]),Integer.parseInt(input[1]),
                    input[2], Integer.parseInt(input[3]));
            ordersPlacedInFile.add(orderRow);
        }
        //ordersPlacedInFile.add(recordedFoodEaten);//Add current FoodItem updated object to list for writing
        //STEP 3
        try {
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter
                    (context.openFileOutput(filename,context.MODE_PRIVATE));
            outputStreamWriter.write("");
            outputStreamWriter.close();
        } catch (IOException e){
            Log.e("Exception,", " File ORDER HEADER Write Failed: " + e.toString());

        }

        //STEP 5
        recreateFoodConsumed();
        //STEP 6
        for (int i = 0; i < allOrders.size(); i++) {
            System.out.printf("%s\n",ordersPlacedInFile.get(i).toString());
            insertOrderHeader(ordersPlacedInFile.get(i).orderTypeCode, ordersPlacedInFile.get(i)
            .date, ordersPlacedInFile.get(i).userID);
            System.out.printf("DB ORDERHEADER Insert Statement executed successfully");
        }
        insertOrderHeader(orderTypeCode,date,userID);
        //STEP 4
        try {
            ArrayList<OrderRow> orders = new ArrayList<OrderRow>();
            orders = getAllOrders();
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter
                    (context.openFileOutput(filename,context.MODE_PRIVATE));
            for(int i =0; i< orders.size();i++){
                outputStreamWriter.write(orders.get(i).dbWriteOrdersToFile());
            }
            outputStreamWriter.close();
        } catch (IOException e){
            Log.e("Exception,", " File ORDERHEADER Write Failed: " + e.toString());

        }

    }
}
