package bvgiants.diary3;

import android.app.Activity;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.widget.ListView;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Created by kenst on 2/05/2016.
 * This class will be used to select food to add to users diary.
 */
public class FoodEntryActivity extends Activity {

    ListView list;
    public static Context context;
    public SQLiteDatabase db;
    public DatabaseHelper databaseHelper;
    public ArrayList <String> foodNames;
    public ArrayList<FoodItem> allFood = new ArrayList<FoodItem>();
    public ArrayList<FoodItem> foodSearch = new ArrayList<FoodItem>();
    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.food_details_fragment);

        //Firstly Load DB
        context = getApplicationContext();
        databaseHelper = new DatabaseHelper(context);
        db = databaseHelper.getWritableDatabase();
        ArrayList<Integer> imageId = new ArrayList<Integer>();
        //Gets all food items
        /*allFood = databaseHelper.allFood();
        foodNames = new ArrayList<String>();

        for(int i=0; i< allFood.size();i++){
            foodNames.add(allFood.get(i).name);
        }
        */

        // Get food items based on search query
        Intent intent = getIntent();
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);
            foodSearch = databaseHelper.foodSearch(query);
        }

        foodNames = new ArrayList<String>();
        for (int i = 0; i < foodSearch.size(); i++) {
            foodNames.add(foodSearch.get(i).name);
        }

        //Will need another way to get images eventually
        imageId.add(R.drawable.bigmac);
        imageId.add(R.drawable.cheeseburger);
        imageId.add(R.drawable.quarterpounder);

        //Use custom adapter to help display the list and images correctly
        CustomListAdapter adapter= new CustomListAdapter(this, foodNames, imageId);
        list=(ListView)findViewById(R.id.list_foods);
        list.setAdapter(adapter);

        list.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                // TODO Auto-generated method stub
                String Selecteditem= foodNames.get(position);
                Toast.makeText(getApplicationContext(), Selecteditem, Toast.LENGTH_SHORT).show();

            }
        });
    }
}
