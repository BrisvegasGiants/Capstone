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
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;


public class EatActivity extends AppCompatActivity {


    Context mContext;
    private int USERID;

    private ListView listView;
    private Button today;

    public static Context context;
    public SQLiteDatabase db;
    public DatabaseHelper databaseHelper;

    public ArrayList<FoodItem> allFood = new ArrayList<FoodItem>();
    public ArrayList<OrderRow> orders = new ArrayList<OrderRow>();
    public ArrayList<OrderRow> todaysOrders = new ArrayList<OrderRow>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_eat);

        Toolbar toolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(toolbar);

        USERID = getIntent().getIntExtra("UserID", 0);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);

        startBackgroundProcess(this.findViewById(android.R.id.content), mContext);

        context = getApplicationContext();
        databaseHelper = new DatabaseHelper(context);
        db = databaseHelper.getWritableDatabase();
        //orders = databaseHelper.getAllUsersFoodConsumed(USERID);
        todaysOrders = databaseHelper.showTodaysFood();
        allFood = databaseHelper.allFood();

        for(int i = 0; i < todaysOrders.size(); i ++){
            Log.v(todaysOrders.get(i).todaysFoodCheck(), "TODAYS FOOD CHECK");
        }
        //addRowsToTable(todaysOrders);

        showTodaysFood();
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
            startActivity(startSettings);
            return true;
        }

        if (id == R.id.action_home) {
            Intent startHome = new Intent(this, MainActivity.class);
            startActivity(startHome);
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
                createFoodFragment();
            }


        });
        //addRowsToTable();
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

    public ArrayList<OrderRow> getTodaysOrders(){
        return todaysOrders;
    }
    public ArrayList<FoodItem> getAllFood(){
        return allFood;
    }
}