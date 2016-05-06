package bvgiants.diary3;

import android.app.Fragment;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.BaseExpandableListAdapter;
import android.util.SparseArray;
import android.widget.TextView;
import java.util.ArrayList;

/**
 * Created by kenst on 6/05/2016.
 */
public class ExpandableListFragment extends Fragment {

    private SparseArray<FoodItem> foodsToSave = new SparseArray<FoodItem>();
    private ArrayList<String> foodNames;
    private ArrayList<String> foodDesc;
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){

        View v = inflater.inflate(R.layout.expandable_list,null);
        ExpandableListView listView = (ExpandableListView) v.findViewById(R.id.expandable_list_view);
        listView.setAdapter(new SavedTabsListAdapter());
        //foodNames = getArguments().getStringArrayList("selectedFoods");
        //foodDesc = getArguments().getStringArrayList("foodDesc");
        Log.v("INSIDE EXPANDABLELIST"," FUCK THIS SHIT");

        FoodEntryActivity activity = (FoodEntryActivity) getActivity();
        foodsToSave = activity.foodsToPass();
        for(int i = 0; i < foodsToSave.size();i++){
            Log.v(foodsToSave.get(i).toString(), " EXPANDABLE LIST FRAG");
        }
        return v;
    }


    public class SavedTabsListAdapter extends BaseExpandableListAdapter {

        private String[][] children = {
                { "Arnold", "Barry", "Chuck", "David" },
                { "Ace", "Bandit", "Cha-Cha", "Deuce" },
                { "Fluffy", "Snuggles" },
                { "Goldy", "Bubbles" }
        };

        @Override
        public int getGroupCount() {
            return foodsToSave.size();
        }

        @Override
        public int getChildrenCount(int groupPosition) {
            return foodsToSave.get(groupPosition).children.size();
        }

        @Override
        public FoodItem getGroup(int groupPosition) {
            return foodsToSave.get(groupPosition);
        }

        @Override
        public Object getChild(int groupPosition, int childPosition){
            return foodsToSave.get(groupPosition).children.get(childPosition);
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
            TextView textView = new TextView(ExpandableListFragment.this.getActivity());
            textView.setText(getGroup(i).name);
            return textView;
        }

        @Override
        public View getChildView(int i, int i1, boolean b, View view, ViewGroup viewGroup) {
            TextView textView = new TextView(ExpandableListFragment.this.getActivity());
            textView.setText(getChild(i, i1).toString());
            return textView;
        }

        @Override
        public boolean isChildSelectable(int i, int i1) {
            return true;
        }

    }

}
