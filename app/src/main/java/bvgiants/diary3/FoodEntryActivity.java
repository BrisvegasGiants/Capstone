package bvgiants.diary3;


import android.app.Activity;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.*;
import android.support.v4.widget.SimpleCursorAdapter;
import android.app.Activity;
import android.app.SearchManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.database.Cursor;

import android.provider.ContactsContract;
//import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.menu.MenuItemImpl;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.app.SearchManager;
import android.widget.SearchView;
import android.widget.SearchView.OnQueryTextListener;

import android.widget.ListView;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;

import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SearchView;

import android.widget.Toast;

import java.util.ArrayList;

/**
 * Created by kenst on 2/05/2016.
 * This class will be used to select food to add to users diary.
 */
public class FoodEntryActivity extends Activity implements SearchView.OnQueryTextListener,
        SearchView.OnCloseListener {

    ListView list;
    public static Context context;
    public SQLiteDatabase db;
    public DatabaseHelper databaseHelper;
    public ArrayList<String> foodNames = new ArrayList<String>();


    public ArrayList<Integer> imageId = new ArrayList<Integer>();

    private ListView myList;
    private SearchView searchView;
    private MyCustomAdapter defaultAdapter;
    private ArrayList<FoodItem> allFood = new ArrayList<FoodItem>();

    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_food_entry);

        //Firstly Load DB
        context = getApplicationContext();
        databaseHelper = new DatabaseHelper(context);
        db = databaseHelper.getWritableDatabase();

        allFood = databaseHelper.allFood();
        for (int i = 0; i < allFood.size(); i++) {
            foodNames.add(allFood.get(i).name);
        }


        myList = (ListView) findViewById(R.id.list);

        //defaultAdapter = new MyCustomAdapter(FoodEntryActivity.this, foodNames);
        //myList.setAdapter(defaultAdapter);

        searchView = (SearchView) findViewById(R.id.search);
        searchView.setIconifiedByDefault(false);
        searchView.setOnQueryTextListener(this);
        searchView.setOnCloseListener(this);

    }

    public boolean onClose() {
        myList.setAdapter(defaultAdapter);
        return false;
    }

    public boolean onQueryTextSubmit(String query) {
        doSearch(query + "*");
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        if (!newText.isEmpty()) {
            doSearch(newText + "*");
        } else {
            myList.setAdapter(defaultAdapter);
        }

        return false;
    }
    public void doSearch(String query) {

        //Will need another way to get images eventually
        imageId.add(R.drawable.bigmac);
        imageId.add(R.drawable.cheeseburger);
        imageId.add(R.drawable.quarterpounder);

        //Use custom adapter to help display the list and images correctly

        CustomListAdapter adapter = new CustomListAdapter(this, foodNames, imageId);
        list = (ListView) findViewById(R.id.list);

        //CustomListAdapter adapter = new CustomListAdapter(this, foodNames, imageId);
        //list = (ListView) findViewById(R.id.list_foods);

        list.setAdapter(adapter);
        list.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                // TODO Auto-generated method stub
                String Selecteditem = foodNames.get(position);
                Toast.makeText(getApplicationContext(), Selecteditem, Toast.LENGTH_SHORT).show();

            }
        });
    }
}


/*

BELOW HERE IS USELESS CRAP THAT I'M SCARED TO DELETE DUE TO THE FUCK AROUND TO GET THIS WORKING!

 */
    /*private void displayResults(String query) {

        Cursor cursor = databaseHelper.searchLookupFood((query != null ? query : "@@@@"));

        if (cursor != null) {
            // Specify the view where we want the results to go
            int[] to = new int[]{R.id.search_result_text_view};
            String[] from = new String[]{"FOOD NAME"};
            // Create a simple cursor adapter to keep the search data
            SimpleCursorAdapter cursorAdapter = new SimpleCursorAdapter(this, R.layout.result_search_item, cursor, from, to, 1);
            myList.setAdapter(cursorAdapter);

            // Click listener for the searched item that was selected
            myList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                    // Get the cursor, positioned to the corresponding row in the result set
                    Cursor cursor = (Cursor) myList.getItemAtPosition(position);

                    // Get the state's capital from this row in the database.
                    String selectedName = cursor.getString(cursor.getColumnIndexOrThrow("name"));
                    Toast.makeText(FoodEntryActivity.this, selectedName, Toast.LENGTH_SHORT).show();

                    // Set the default adapter
                    myList.setAdapter(defaultAdapter);

                    // Find the position for the original list by the selected name from search
                    for (int pos = 0; pos < foodNames.size(); pos++) {
                        if (foodNames.get(pos).equals(selectedName)) {
                            position = pos;
                            break;
                        }
                    }

                    // Create a handler. This is necessary because the adapter has just been set on the list again and
                    // the list might not be finished setting the adapter by the time we perform setSelection.
                    Handler handler = new Handler();
                    final int finalPosition = position;
                    handler.post(new Runnable() {
                        @Override
                        public void run() {

                            myList.setSelection(finalPosition);
                        }
                    });

                    searchView.setQuery("", true);
                }
            });

        }
    }

    /*

    // Create the search menu.
    // D

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.food_search_menu, menu);
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        MenuItem searchActionBarItem = menu.findItem(R.id.search);
        SearchView searchView = null;
        if(searchActionBarItem != null)
            searchView = (SearchView) searchActionBarItem.getActionView();
        if(searchView != null)
            searchView.setSearchableInfo(searchManager.getSearchableInfo(FoodEntryActivity.this.getComponentName()));
        //searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        return super.onCreateOptionsMenu(menu);
    }
    */

/*
    // Is called whenever the user hits submit.
    @Override
    public boolean onQueryTextSubmit(String query) {
        doSearch(query);
        return false;
    }
    //Is called whenever the user changes texts ins search bar.
    @Override
    public boolean onQueryTextChange(String newText) {
        doSearch(newText);
            return false;
        }

    }

*/

