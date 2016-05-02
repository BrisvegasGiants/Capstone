package bvgiants.diary3;

import android.app.Activity;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.widget.ListView;
import android.app.ListActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Created by kenst on 2/05/2016.
 * This class will be used to select food to add to users diary.
 */
public class FoodEntryActivity extends Activity {

    ListView list;
    public static Context context;
    public SQLiteDatabase lookupFoodDB;
    public LookupFoodDBController lookupFoodDBController;
    public ArrayList <String> foodNames;
    public ArrayList<LookupFoodDBController.FoodItem> allFood = new ArrayList<LookupFoodDBController.FoodItem>();
    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.food_details_fragment);

        //Firstly Load DB
        context = getApplicationContext();
        lookupFoodDBController = new LookupFoodDBController(context);
        lookupFoodDB = lookupFoodDBController.getWritableDatabase();

        allFood = lookupFoodDBController.allFood();
        foodNames = new ArrayList<String>();
        ArrayList<Integer> imageId = new ArrayList<Integer>();
        for(int i=0; i< allFood.size();i++){
            foodNames.add(allFood.get(i).name);
        }
        //Will need another way to get images eventually
        imageId.add(R.drawable.bigmac);
        imageId.add(R.drawable.cheeseburger);
        imageId.add(R.drawable.quarterpounder);

        //Use custom adapter to help display the list and images correctly
        CustomListAdapter adapter=new CustomListAdapter(this, foodNames, imageId);
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
