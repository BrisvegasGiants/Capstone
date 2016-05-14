package bvgiants.diary3;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.media.audiofx.BassBoost;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class EatActivity extends AppCompatActivity {


    Context mContext;

    private ListView listView;
    private Button today;

    public static Context context;
    public SQLiteDatabase db;
    public DatabaseHelper databaseHelper;

    public ArrayList<FoodItem> allFood = new ArrayList<FoodItem>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_eat);

        Toolbar toolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(toolbar);


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);

        startBackgroundProcess(this.findViewById(android.R.id.content), mContext);

        context = getApplicationContext();
        databaseHelper = new DatabaseHelper(context);
        db = databaseHelper.getWritableDatabase();

        showTodaysFood();
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
        startActivity(intent);
    }


    public void addRowsToTable(ArrayList<OrderRow> orders) {
        TableLayout table = (TableLayout) findViewById(R.id.tableLayout);
        table.removeAllViews();

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
        table.addView(row);

       for (int i = 0; i < orders.size(); i++) {
            row = new TableRow(this);
            orderTime = new TextView(this);
            foodName = new TextView(this);
            calories = new TextView(this);

            //orderTime.setText("OrderDate: " + orders.get(i).getDate());
            orderTime.setText("OrderTypeCode: " + String.valueOf(orders.get(i).getOrderTypeCode()));
            foodName.setText("OrderID: " + String.valueOf(orders.get(i).getOrderID()));
            calories.setText("UserID: " + String.valueOf(orders.get(i).getUserID()));

            orderTime.setGravity(Gravity.CENTER);
            foodName.setGravity(Gravity.CENTER);
            calories.setGravity(Gravity.CENTER);

            row.addView(orderTime);
            row.addView(foodName);
            row.addView(calories);
            table.addView(row);
        }


        /*THE BELOW CODE WAS MY PREVIOUS ATTEMPT TO GET IMAGES TO DISPLAY ON THE SEARCH FIELD
        PROBS LEAVE THIS HERE UNTIL WE CONFIRM EVERYTHING IS FUNCTIONING!!!
         */


    }

    public void showTodaysFood() {

        final ArrayList<OrderRow> orders = databaseHelper.getAllUsersFoodConsumed(1);
        today = (Button) findViewById(R.id.buttonToday);
        today.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                addRowsToTable(orders);
            }


        });
        addRowsToTable(orders);
    }

    /*
        BELOW METHODS ARE SIMPLY TO PRINT OUT WHATS IN EACH TABLE!
     */
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

    public void addRowsToMonths(ArrayList<OrderRow> orders) {
        TableLayout table = (TableLayout) findViewById(R.id.tableLayout);
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
            table.addView(row);
        }

    }

}