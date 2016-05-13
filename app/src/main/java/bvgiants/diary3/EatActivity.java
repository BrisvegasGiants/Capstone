package bvgiants.diary3;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.media.audiofx.BassBoost;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class EatActivity extends ActionBarActivity {

    private Toolbar toolbar;

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



        toolbar = (Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);



        setContentView(R.layout.activity_eat);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);

        startBackgroundProcess(this.findViewById(android.R.id.content), mContext);

        context = getApplicationContext();
        databaseHelper = new DatabaseHelper(context);
        db = databaseHelper.getWritableDatabase();

        showTodaysFood();
        showFoodConsumed();
    } //End onCreate


    public void startBackgroundProcess(View view, Context c){
        startService(new Intent(getBaseContext(), BackgroundService.class));
    }


    public void newEntry(View v)
    {
        Intent intent = new Intent(this,FoodEntryActivity.class);
        startActivity(intent);
    }


    public void addRowsToTable(ArrayList<OrderRow> orders){
        TableLayout table = (TableLayout) findViewById(R.id.tableLayout);
        for(int i = 0; i < orders.size();i++) {
            TableRow row = new TableRow(this);
            TextView orderTime = new TextView(this);
            TextView foodName = new TextView(this);
            TextView calories = new TextView(this);

            //orderTime.setText("OrderDate: " + orders.get(i).getDate());
            orderTime.setText("OrderTypeCode: " + String.valueOf(orders.get(i).getOrderTypeCode()));
            foodName.setText("OrderID: " + String.valueOf(orders.get(i).getOrderID()));
            calories.setText("UserID: " + String.valueOf(orders.get(i).getUserID()));

            row.addView(orderTime);
            row.addView(foodName);
            row.addView(calories);
            table.addView(row);
        }


        /*THE BELOW CODE WAS MY PREVIOUS ATTEMPT TO GET IMAGES TO DISPLAY ON THE SEARCH FIELD
        PROBS LEAVE THIS HERE UNTIL WE CONFIRM EVERYTHING IS FUNCTIONING!!!
         */


    }
    public void showTodaysFood(){

        final ArrayList<OrderRow> orders = databaseHelper.getAllOrders();
        today = (Button) findViewById(R.id.buttonToday);
        today.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                addRowsToTable(orders);
            }
        });
    }

    /*
        BELOW METHODS ARE SIMPLY TO PRINT OUT WHATS IN EACH TABLE!
     */
    public void showFoodConsumed(){
        final ArrayList<OrderRow> orders = databaseHelper.getAllUsersFoodConsumed(1);
        today = (Button) findViewById(R.id.buttonMonth);
        today.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                addRowsToMonths(orders);
            }
        });
    }

    public void addRowsToMonths(ArrayList<OrderRow> orders){
        TableLayout table = (TableLayout) findViewById(R.id.tableLayout);
        for(int i = 0; i < orders.size();i++) {
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