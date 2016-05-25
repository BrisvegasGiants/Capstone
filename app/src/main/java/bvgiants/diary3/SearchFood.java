package bvgiants.diary3;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Toast;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.util.ArrayList;

public class SearchFood extends AppCompatActivity implements SearchView.OnQueryTextListener,
        SearchView.OnCloseListener {

    private ListView searchedFood;
    private ListView list;
    private SearchView searchView;
    private ArrayList<FoodItem> foodItems;
    private ArrayList<String> foodNames;
    private Firebase databaseReference;
    private MyCustomAdapter defaultAdapter;

    private final int STATIC_USER_ID = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_food);
        Firebase.setAndroidContext(this);

        databaseReference = new Firebase("https://healthdiary.firebaseio.com/");

        foodItems = new ArrayList<>();
        foodNames = new ArrayList<>();

        // Refer to Firebase DB to get our FoodItems
        getFoodItemsDB();
        searchedFood = (ListView) this.findViewById(R.id.searchedFood);

        searchView = (SearchView) this.findViewById(R.id.search);
        searchView.setIconifiedByDefault(false);
        searchView.setOnQueryTextListener(this);
        searchView.setOnCloseListener(this);


    }

    private void getFoodItemsDB() {

        // Read the data at the location specified in the "databaseReference" URL.
        // Every time data changes in the DB this method will be called to pull the new data
        databaseReference.child("foodItems").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                // snapshot hold the data from the DB at the specified location
                for (DataSnapshot foodItem : snapshot.getChildren()) {
                    try {
                        /*
                        foodItems.add(new FoodItem(
                                foodItem.child("name").getValue().toString(),
                                Integer.parseInt(foodItem.child("calories").getValue().toString()),
                                Integer.parseInt(foodItem.child("sugar").getValue().toString()),
                                Integer.parseInt(foodItem.child("fat").getValue().toString()),
                                Integer.parseInt(foodItem.child("energy").getValue().toString()),
                                Integer.parseInt(foodItem.child("sodium").getValue().toString()),
                                Integer.parseInt(foodItem.child("protein").getValue().toString()),
                                foodItem.child("imageLocal").getValue().toString())); */

                        // I am just adding static values to the Food Items. But the name is actually comign from the DB
                        //foodItems.add(new FoodItem(
                          //      foodItem.getKey().toString(),
                            //    100, 100, 100, 100, 100, 100, "test"));

                        // I am also adding the name to a list for displaying in the List View
                        foodNames.add(foodItem.getKey().toString());
                    } catch (Exception e) {
                        Toast.makeText(SearchFood.this, "Could not parse data to ints", Toast.LENGTH_SHORT).show();
                    }
                }

            }
            @Override public void onCancelled(FirebaseError error) { }
        });
    }

    public boolean onClose() {
        searchedFood.setAdapter(defaultAdapter);
        return false;
    }

    public boolean onQueryTextSubmit(String query) {
        // doSearch(query + "*");
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        if (!newText.isEmpty()) {
            doSearch(newText + "*");
        } else {
            searchedFood.setAdapter(defaultAdapter);
        }

        return false;
    }

    public void doSearch(String query) {

        //Will need another way to get images eventually


        //Use custom adapter to help display the list and images correctly

        CustomListAdapter adapter = new CustomListAdapter(this, foodNames, null);
        list = (ListView) findViewById(R.id.list);
        list.setAdapter(adapter);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                Toast.makeText(getApplicationContext(), position, Toast.LENGTH_SHORT).show();


            }
        });
    }
}
