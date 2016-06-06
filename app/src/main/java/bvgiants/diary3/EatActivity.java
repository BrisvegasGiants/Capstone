package bvgiants.diary3;


import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TableRow;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;


public class EatActivity extends AppCompatActivity {


    Context mContext;
    private int USERID;

    private ListView listView;
    private Button today;
    private Button week;
    private Button month;
    private int SELECTION;

    public static Context context;
    public SQLiteDatabase db;
    public DatabaseHelper databaseHelper;

    public ArrayList<FoodItem> allFood = new ArrayList<FoodItem>();
    public ArrayList<OrderRow> orders = new ArrayList<OrderRow>();
    public ArrayList<OrderRow> todaysOrders = new ArrayList<OrderRow>();
    public ArrayList<OrderRow> weeklyOrders = new ArrayList<>();
    public ArrayList<OrderRow> monthlyOrders = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_eat);

        Toolbar toolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(toolbar);

        USERID = getIntent().getIntExtra("UserID", 0);

        startBackgroundProcess(this.findViewById(android.R.id.content), mContext);

        context = getApplicationContext();
        databaseHelper = new DatabaseHelper(context);
        db = databaseHelper.getWritableDatabase();
        //orders = databaseHelper.getAllUsersFoodConsumed(USERID);
        todaysOrders = databaseHelper.showTodaysFood(USERID);
        weeklyOrders = databaseHelper.allUserFoodOrders(USERID);
        monthlyOrders = databaseHelper.allUserFoodOrders(USERID);
        allFood = databaseHelper.allFood();

        for(int i = 0; i < todaysOrders.size(); i ++){
            Log.v(todaysOrders.get(i).todaysFoodCheck(), "TODAYS FOOD CHECK");
        }

        //addRowsToTable(todaysOrders);

        showTodaysFood();
        showWeeksFood();
        showMonthsFood();
        createFoodFragment();
        //showFoodConsumed();
    } //End onCreate


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.eat_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            Intent startSettings = new Intent(this, SettingsActivity.class);
            Bundle userCreds = new Bundle();
            userCreds.putInt("UserID", USERID);
            startSettings.putExtras(userCreds);
            startActivity(startSettings);
            return true;
        }

        if (id == R.id.action_home) {
            Intent startHome = new Intent(this, MainActivity.class);
            Bundle userCreds = new Bundle();
            userCreds.putInt("UserID", USERID);
            startHome.putExtras(userCreds);
            startActivity(startHome);
            return true;
        }

        if (id == R.id.action_about) {
            Intent startAbout = new Intent(this, about_us.class);
            Bundle userCreds = new Bundle();
            userCreds.putInt("UserID", USERID);
            startAbout.putExtras(userCreds);
            startActivity(startAbout);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void startBackgroundProcess(View view, Context c) {
        startService(new Intent(getBaseContext(), BackgroundService.class));
    }


    public void newEntry(View v) {
        Intent intent = new Intent(this, FoodEntryActivity.class);
        Bundle userCreds = new Bundle();
        userCreds.putInt("UserID", USERID);
        intent.putExtras(userCreds);
        startActivity(intent);
    }


    public void addRowsToTable(ArrayList<OrderRow> orders) {
        //TableLayout table = (TableLayout) findViewById(R.id.tableLayout);
        //table.removeAllViews();

        TableRow row = new TableRow(this);
        View headerLine = new View(this);
        TextView orderTime = new TextView(this);
        TextView foodName = new TextView(this);
        TextView calories = new TextView(this);

        //orderTime.setText("OrderDate: " + orders.get(i).getDate());
        orderTime.setText("Time");
        foodName.setText("Food");
        calories.setText("Calories");

        orderTime.setGravity(Gravity.CENTER);
        foodName.setGravity(Gravity.CENTER);
        calories.setGravity(Gravity.CENTER);

        row.addView(orderTime);
        row.addView(foodName);
        row.addView(calories);
        //table.addView(row);

       for (int i = 0; i < orders.size(); i++) {
            row = new TableRow(this);
            orderTime = new TextView(this);
            foodName = new TextView(this);
            calories = new TextView(this);

            //orderTime.setText("OrderDate: " + orders.get(i).getDate());
            orderTime.setText(String.valueOf(orders.get(i).getUserID()));
            foodName.setText(String.valueOf(orders.get(i).getOrderID()));
            calories.setText(String.valueOf(orders.get(i).getOrderTypeCode()));

            orderTime.setGravity(Gravity.CENTER);
            foodName.setGravity(Gravity.CENTER);
            calories.setGravity(Gravity.CENTER);

            row.addView(orderTime);
            row.addView(foodName);
            row.addView(calories);
           // table.addView(row);
        }

    }

    public void showTodaysFood() {

        for(int i = 0; i < todaysOrders.size(); i ++){
            Log.v(todaysOrders.get(i).dbWriteOrdersToFile(), "CONSUMED TODAY HAS THIS");
        }
        today = (Button) findViewById(R.id.buttonToday);
        today.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //addRowsToTable(todaysOrders);
                SELECTION = 0;
                createFoodFragment();
            }


        });
        //addRowsToTable();
    }

    public void showWeeksFood() {

        week = (Button) findViewById(R.id.buttonWeek);
        week.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                SELECTION = 1;

                createFoodFragment();
            }
        });
    }

    public void showMonthsFood() {

        month = (Button) findViewById(R.id.buttonMonth);
        month.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                SELECTION = 2;
                createFoodFragment();
            }
        });
    }

    /*
        BELOW METHODS ARE SIMPLY TO PRINT OUT WHATS IN EACH TABLE!

    public void showFoodConsumed() {
        final ArrayList<OrderRow> orders = databaseHelper.getAllUsersFoodConsumed(1);
        today = (Button) findViewById(R.id.buttonMonth);
        today.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                addRowsToMonths(orders);
            }
        });
    }
*/
    public void addRowsToMonths(ArrayList<OrderRow> orders) {
        //TableLayout table = (TableLayout) findViewById(R.id.tableLayout);
        for (int i = 0; i < orders.size(); i++) {
            TableRow row = new TableRow(this);
            TextView orderTime = new TextView(this);
            TextView foodName = new TextView(this);
            TextView calories = new TextView(this);

            orderTime.setText("OrderDate: " + orders.get(i).getDate());
            //orderTime.setText("OrderTypeCode: " + String.valueOf(orders.get(i).getOrderTypeCode()));
            foodName.setText("OrderID: " + String.valueOf(orders.get(i).getOrderID()));
            calories.setText("UserID: " + String.valueOf(orders.get(i).getUserID()));

            row.addView(orderTime);
            row.addView(foodName);
            row.addView(calories);
           // table.addView(row);
        }

    }

    public void createFoodFragment(){

        Fragment fragment = new ExpandableListFragmentEAT();
        FragmentManager fm = getFragmentManager();
        FragmentTransaction fragmentTransaction = fm.beginTransaction();
        fragmentTransaction.replace(R.id.fragmentEat,fragment);
        fragmentTransaction.commit();

    }

    public String justGetDate(){
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        Date date = new Date();
        return  dateFormat.format(date);
    }
    public String getLastWeek(){
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        Date date = new Date();
        return  dateFormat.format(date.getTime() - 604800000L);
    }

    public ArrayList<OrderRow> getTodaysOrders(){
        return todaysOrders;
    }

    public ArrayList<OrderRow> getWeeklyOrders(){

        ArrayList<OrderRow> correctDates = new ArrayList<>();
        SimpleDateFormat dateFormatOrder = new SimpleDateFormat("yyyy-MM-dd");

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        Date date = new Date();
        dateFormat.format(date.getTime() - 604800000L); // 7 * 24 * 60 * 60 * 1000

        for(int i = 0; i < weeklyOrders.size(); i++){
            Date convertDate = new Date();
            try {
                convertDate = dateFormatOrder.parse(weeklyOrders.get(i).getDate());
            }catch (ParseException e){
                e.printStackTrace();
            }
            if (convertDate.getTime() <= date.getTime()){
                correctDates.add(weeklyOrders.get(i));
            }
        }
        return correctDates;
    }

    public ArrayList<OrderRow> getMonthlyOrders (){
        ArrayList<OrderRow> correctDates = new ArrayList<>();
        SimpleDateFormat dateFormatOrder = new SimpleDateFormat("yyyy-MM-dd");

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        Date date = new Date();
        dateFormat.format(date.getTime() - 267840000000L);

        for(int i = 0; i < weeklyOrders.size(); i++){
            Date convertDate = new Date();
            try {
                convertDate = dateFormatOrder.parse(weeklyOrders.get(i).getDate());
            }catch (ParseException e){
                e.printStackTrace();
            }
            if (convertDate.getTime() <= date.getTime()){
                correctDates.add(weeklyOrders.get(i));
            }
        }
        return correctDates;
    }
    public ArrayList<FoodItem> getAllFood(){
        return allFood;
    }
    public int getSelection(){return SELECTION;}
}