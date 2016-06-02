package bvgiants.diary3;

import android.app.Fragment;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import java.util.ArrayList;

/**
 * Created by kenst on 6/05/2016.
 */
public class ExpandableListFragmentEAT extends Fragment {

    private ArrayList<OrderRow> todaysFoodOrders = new ArrayList<>();
    private ArrayList<OrderRow> weeklyFoodOrders = new ArrayList<>();
    private ArrayList<OrderRow> monthlyFoodOrders = new ArrayList<>();
    private ArrayList<FoodItem> allFood = new ArrayList<>();
    private ArrayList<FoodItem> foodDisplayed = new ArrayList<>();
    public ArrayList<Integer> foodDisplayedImages = new ArrayList<Integer>();
    public ArrayList<String> itemname = new ArrayList<String>();
    public ArrayList<Integer> imageId = new ArrayList<Integer>();
    private EatActivity activity;
    LayoutInflater inflater;
    private int SELECTION;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        this.inflater = inflater;
        // View v = inflater.inflate(R.layout.expandable_list,container,false);
        activity = (EatActivity) getActivity();
        todaysFoodOrders = activity.getTodaysOrders();
        weeklyFoodOrders = activity.getWeeklyOrders();
        monthlyFoodOrders = activity.getMonthlyOrders();
        SELECTION = activity.getSelection();
        allFood = activity.getAllFood();
        imageId.add(R.drawable.bigmac);
        imageId.add(R.drawable.cheeseburger);
        imageId.add(R.drawable.quarterpounder);
        imageId.add(R.drawable.coke);
        imageId.add(R.drawable.kebab);
        imageId.add(R.drawable.subway);
        imageId.add(R.drawable.boost);

        return workOutWhichTimePeriod();

    }


    public class SavedTabsListAdapter extends BaseExpandableListAdapter {

        @Override
        public int getGroupCount() {
            return foodDisplayed.size();
        }

        @Override
        public int getChildrenCount(int groupPosition) {
            return foodDisplayed.get(groupPosition).children.size();
        }

        @Override
        public FoodItem getGroup(int groupPosition) {
            return foodDisplayed.get(groupPosition);
        }

        @Override
        public Object getChild(int groupPosition, int childPosition){
            return foodDisplayed.get(groupPosition).children.get(childPosition);
        }

        @Override
        public long getGroupId(int i) {
            return i;
        }

        @Override
        public long getChildId(int i, int i1) {
            return i1;
        }

        @Override
        public boolean hasStableIds() {
            return true;
        }

        @Override
        public View getGroupView(int i, boolean b, View view, ViewGroup viewGroup) {
            //TextView textView = new TextView(ExpandableListFragment.this.getActivity());
            //textView.setText(getGroup(i).name);


            View rowView = inflater.inflate(R.layout.consumed_foods_eat_activity, null, true);
            TextView txtTitle = (TextView) rowView.findViewById(R.id.consumed_txt);
            if (foodDisplayedImages != null) {
                ImageView imageView = (ImageView) rowView.findViewById(R.id.consumed_img);
                imageView.setImageResource(foodDisplayedImages.get(i));
            }
            TextView extratxt = (TextView) rowView.findViewById(R.id.consumed_textView1);

            txtTitle.setText("Food\n" + foodDisplayed.get(i).getName());
            extratxt.setText("When: " + todaysFoodOrders.get(i).getTime() +
                             "\nWhere: " + todaysFoodOrders.get(i).getLocation());
            return rowView;

        }

        @Override
        public View getChildView(int i, int i1, boolean b, View view, ViewGroup viewGroup) {
            TextView textView = new TextView(ExpandableListFragmentEAT.this.getActivity());
            textView.setText(getChild(i, i1).toString());
            textView.setBackgroundColor(Color.parseColor("#B2EBF2"));
            return textView;
        }

        @Override
        public boolean isChildSelectable(int i, int i1) {
            return true;
        }

    }

    public View workOutWhichTimePeriod(){

        Log.v("SELECTION =", String.valueOf(SELECTION));
        if (SELECTION == 0) {
            for (int i = 0; i < todaysFoodOrders.size(); i++) {
                for (int k = 0; k < allFood.size(); k++) {
                    if (todaysFoodOrders.get(i).getFoodId() == allFood.get(k).getFoodId()) {
                        foodDisplayed.add(allFood.get(k));
                        foodDisplayedImages.add(imageId.get(k));
                    }
                }
            }
            for (int i = 0; i < foodDisplayed.size(); i++) {
                if(foodDisplayed.get(i).children.size() <=0)
                    foodDisplayed.get(i).children.add(foodDisplayed.get(i).toString());
            }

            if (todaysFoodOrders.isEmpty() == false) {
                View v = inflater.inflate(R.layout.expandable_list, null);
                ExpandableListView listView = (ExpandableListView) v.findViewById(R.id.expandable_list_view);
                listView.setAdapter(new SavedTabsListAdapter());
                return v;
            } else {
                View vv = inflater.inflate(R.layout.expandable_list, null);
                return vv;
            }
        }
        else if (SELECTION == 1) {
            for (int i = 0; i < weeklyFoodOrders.size(); i++) {
                for (int k = 0; k < allFood.size(); k++) {
                    if (weeklyFoodOrders.get(i).getFoodId() == allFood.get(k).getFoodId()) {
                        foodDisplayed.add(allFood.get(k));
                        foodDisplayedImages.add(imageId.get(k));
                        Log.v("WEEKLY FOOD ORDERS ", weeklyFoodOrders.get(i).dbWriteOrdersToFile());
                    }
                }
            }
            for (int i = 0; i < foodDisplayed.size(); i++) {
                if(foodDisplayed.get(i).children.size() <=0)
                    foodDisplayed.get(i).children.add(foodDisplayed.get(i).toString());
            }

            if (weeklyFoodOrders.isEmpty() == false) {
                View v = inflater.inflate(R.layout.expandable_list, null);
                ExpandableListView listView = (ExpandableListView) v.findViewById(R.id.expandable_list_view);
                listView.setAdapter(new SavedTabsListAdapter());
                return v;
            } else {
                View vv = inflater.inflate(R.layout.expandable_list, null);
                return vv;
            }
        }
        else{
            for (int i = 0; i < monthlyFoodOrders.size(); i++) {
                for (int k = 0; k < allFood.size(); k++) {
                    if (monthlyFoodOrders.get(i).getFoodId() == allFood.get(k).getFoodId()) {
                        foodDisplayed.add(allFood.get(k));
                        foodDisplayedImages.add(imageId.get(k));
                        Log.v("MONTHLY FOOD ORDERS ", monthlyFoodOrders.get(i).dbWriteOrdersToFile());
                    }
                }
            }
            for (int i = 0; i < foodDisplayed.size(); i++) {
                if(foodDisplayed.get(i).children.size() <=0)
                    foodDisplayed.get(i).children.add(foodDisplayed.get(i).toString());
            }

            if (monthlyFoodOrders.isEmpty() == false) {
                View v = inflater.inflate(R.layout.expandable_list, null);
                ExpandableListView listView = (ExpandableListView) v.findViewById(R.id.expandable_list_view);
                listView.setAdapter(new SavedTabsListAdapter());
                return v;
            } else {
                View vv = inflater.inflate(R.layout.expandable_list, null);
                return vv;
            }
        }

    }

}
