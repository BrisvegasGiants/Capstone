package bvgiants.diary3;

import android.app.Activity;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.provider.ContactsContract;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.app.ListActivity;
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
public class FoodEntryActivity extends AppCompatActivity
        implements SearchView.OnQueryTextListener {

    ListView list;
    public static Context context;
    public SQLiteDatabase db;
    public DatabaseHelper databaseHelper;
    public ArrayList <String> foodNames;
    public ArrayList<FoodItem> allFood = new ArrayList<FoodItem>();
    public ArrayList<FoodItem> userSearch = new ArrayList<FoodItem>();
    public ArrayList<Integer> imageId = new ArrayList<Integer>();

    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_food_entry);

        //Firstly Load DB
        context = getApplicationContext();
        databaseHelper = new DatabaseHelper(context);
        db = databaseHelper.getWritableDatabase();

      /* //Gets all food items
        allFood = databaseHelper.allFood();
        foodNames = new ArrayList<String>();
        imageId = new ArrayList<Integer>();
        for (int i = 0; i < allFood.size(); i++) {
            foodNames.add(allFood.get(i).name);

        } */
    }

    // Create the search menu.
    // D

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.food_search, menu);
        MenuItem searchItem = menu.findItem(R.id.search);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        searchView.setOnQueryTextListener(this);
        return true;
    }

    public void doSearch(String query) {

        userSearch = databaseHelper.userSearch(query);
        foodNames = new ArrayList<String>();
        imageId = new ArrayList<Integer>();
        for (int i = 0; i < userSearch.size(); i++) {
            foodNames.add(userSearch.get(i).name);
        }
        //Will need another way to get images eventually
        imageId.add(R.drawable.bigmac);
        imageId.add(R.drawable.cheeseburger);
        imageId.add(R.drawable.quarterpounder);

        //Use custom adapter to help display the list and images correctly
        CustomListAdapter adapter = new CustomListAdapter(this, foodNames, imageId);
        list = (ListView) findViewById(R.id.list_foods);
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



