package bvgiants.diary3;


import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.app.FragmentManager;
import android.support.v7.widget.*;
import android.util.Log;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import android.support.v4.widget.SimpleCursorAdapter;
import android.app.Activity;
import android.app.SearchManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

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
import android.util.SparseArray;
import android.widget.ListView;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;

import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SearchView;

import android.widget.Toast;


import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by kenst on 2/05/2016.
 * This class will be used to select food to add to users diary.
 */
public class FoodEntryActivity extends AppCompatActivity implements SearchView.OnQueryTextListener,
        SearchView.OnCloseListener {

    ListView list;
    public CustomListAdapter adapter;
    public static Context context;
    public SQLiteDatabase db;
    public DatabaseHelper databaseHelper;
    public ArrayList<String> foodNames = new ArrayList<String>();
    public ArrayList<String> foodDesc = new ArrayList<String>();
    public ArrayList<String> chosenFoods = new ArrayList<String>();

    public ArrayList<Integer> imageId = new ArrayList<Integer>();
    public ArrayList<Integer> imageIdToAdd = new ArrayList<Integer>();

    private ListView myList;
    private SearchView searchView;
    private ArrayList<FoodItem> allFood = new ArrayList<FoodItem>();

    public SparseArray<FoodItem> foodsToSave = new SparseArray<FoodItem>();
    private ArrayList<FoodItem> usersFoods = new ArrayList<FoodItem>();

    private Button addToDiary;

    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_entry);

        Toolbar toolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(toolbar);

        //Firstly Load DB
        context = getApplicationContext();
        databaseHelper = new DatabaseHelper(context);
        db = databaseHelper.getWritableDatabase();

        //Will need another way to get images eventually
        imageId.add(R.drawable.bigmac);
        imageId.add(R.drawable.cheeseburger);
        imageId.add(R.drawable.quarterpounder);

        allFood = databaseHelper.allFood();
        for (int i = 0; i < allFood.size(); i++) {
            foodNames.add(allFood.get(i).name);
        }
        for(int i = 0; i < allFood.size(); i++){
            foodDesc.add(allFood.get(i).toString());
        }

        myList = (ListView) findViewById(R.id.list);

        adapter = new CustomListAdapter(this, foodNames, imageId);
        list = (ListView) findViewById(R.id.list);
        list.setAdapter(adapter);
        list.setVisibility(View.GONE);


        searchView = (SearchView) findViewById(R.id.search);
        searchView.setIconifiedByDefault(false);
        searchView.setOnQueryTextListener(this);
        searchView.setOnCloseListener(this);
        addToDiary();


    }


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

        if (id ==R.id.action_home) {
            Intent startHome = new Intent(this, MainActivity.class);
            startActivity(startHome);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void addToDiary (){
        final Intent loadEat = new Intent(this, EatActivity.class);
        addToDiary = (Button) findViewById(R.id.save_to_diary);
        addToDiary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Log.v("ADD TO DIARY! ", " I'VE REGISTERED A CLICK!");
                try{
                    databaseHelper.saveDataToFoodConsumedTable(usersFoods);
                    startActivity(loadEat);
                } catch (IOException e){
                    Log.v(e.toString(), " THERE WAS AN ERROR!");
                }


            }
        });
    }
    public boolean onClose() {
        list.setVisibility(View.GONE);
        searchView.refreshDrawableState();
        createUsersSelectedFoods();
        return false;
    }

    public boolean onQueryTextSubmit(String query) {
        doSearch(query + "*");

        list.setVisibility(View.GONE);
        searchView.refreshDrawableState();
        createUsersSelectedFoods();
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        list.setVisibility(View.VISIBLE);
        if (!newText.isEmpty()) {
            doSearch(newText + "*");
        } else {
            list.setAdapter(adapter);
        }
        //createUsersSelectedFoods();
        return false;
    }


    public SparseArray<FoodItem> foodsToPass(){
        return foodsToSave;
    }

    public ArrayList<Integer> getImageId(){return imageId;}
    public ArrayList<String> getFoodNames(){return foodNames;}
    public void createUsersSelectedFoods(){

        for (int i = 0; i < usersFoods.size(); i++) {
            usersFoods.get(i).children.add(usersFoods.get(i).toString());
        }

        Fragment fragment = new ExpandableListFragment();
        FragmentManager fm = getFragmentManager();
        FragmentTransaction fragmentTransaction = fm.beginTransaction();
        fragmentTransaction.replace(R.id.fragment,fragment);
        fragmentTransaction.commit();

        /*CustomListAdapter userAdapter = new CustomListAdapter(this, chosenFoods, imageIdToAdd);
        list = (ListView) findViewById(R.id.listTwo);
        list.setAdapter(userAdapter); */
    }
    public void doSearch(String query) {

        list.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {


                // selectedItem holds a FoodItem object of the item selected from the list
                FoodItem selecteditem = allFood.get(position);

                // Check if the selected item is in the list of selected foods.
                // If the item is in the list, take it off the list, and change the background back to white
                // Otherwise, add the item to the list and make background Blue
                if (usersFoods.contains(selecteditem)) {
                    parent.getChildAt(position).setBackgroundColor(Color.WHITE);
                    usersFoods.remove(selecteditem);
                    foodsToSave.remove(usersFoods.size()-1);
                } else {
                    Toast.makeText(getApplicationContext(), selecteditem.getName(), Toast.LENGTH_SHORT).show();
                    usersFoods.add(selecteditem);
                    Log.v("userFoods size = ", String.valueOf(usersFoods.size()));
                    foodsToSave.append(usersFoods.size()-1,selecteditem);
                    parent.getChildAt(position).setBackgroundColor(Color.BLUE);
                }

            }
        });
    }
}


