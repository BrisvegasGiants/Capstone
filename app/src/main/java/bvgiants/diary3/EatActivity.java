package bvgiants.diary3;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;


public class EatActivity extends AppCompatActivity {

    private ListView listView;
    public static Context context;

    public ArrayList<FoodItem> allFood = new ArrayList<FoodItem>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_eat);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);

    } //End onCreate

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return true;
    }

    public void newEntry(View v)
    {
        Intent intent = new Intent(this,FoodEntryActivity.class);
        startActivity(intent);


        /*THE BELOW CODE WAS MY PREVIOUS ATTEMPT TO GET IMAGES TO DISPLAY ON THE SEARCH FIELD
        PROBS LEAVE THIS HERE UNTIL WE CONFIRM EVERYTHING IS FUNCTIONING!!!
         */

        /*setContentView(R.layout.food_details_fragment);
        Activity newContext = this;
        context = getApplicationContext();
        lookupFoodDBController = new LookupFoodDBController(context);
        lookupFoodDB = lookupFoodDBController.getWritableDatabase();

        allFood = lookupFoodDBController.allFood();
        ArrayList<Integer> imageId = new ArrayList<Integer>();
        imageId.add(R.drawable.bigmac);
        imageId.add(R.drawable.cheeseburger);
        imageId.add(R.drawable.quarterpounder);

        LayoutInflater inflater = newContext.getLayoutInflater();
        for(int i =0; i < allFood.size(); i++){
            View rowView = inflater.inflate(R.layout.list_single,null,true);
            TextView txtTile = (TextView) rowView.findViewById(R.id.txt);
            ImageView imageView = (ImageView) rowView.findViewById(R.id.img);
            txtTile.setText(allFood.get(i).name);
            imageView.setImageResource(imageId.get(i));
    }

        listView = (ListView)findViewById(R.id.list_foods);

        ArrayAdapter<LookupFoodDBController.FoodItem> arrayAdapter =
                new ArrayAdapter<LookupFoodDBController.FoodItem>(this,
                        android.R.layout.simple_list_item_1,allFood);

        listView.setAdapter(arrayAdapter);
        */
    }

}