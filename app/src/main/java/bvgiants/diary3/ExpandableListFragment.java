package bvgiants.diary3;

import android.app.Fragment;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
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
import android.widget.ImageView;
import android.widget.TextView;
import java.util.ArrayList;

/**
 * Created by kenst on 6/05/2016.
 */
public class ExpandableListFragment extends Fragment {

    private SparseArray<FoodItem> foodsToSave = new SparseArray<FoodItem>();
    public ArrayList<Integer> imgid = new ArrayList<Integer>();
    public ArrayList<String> itemname = new ArrayList<String>();
    private FoodEntryActivity activity;
    LayoutInflater inflater;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        this.inflater = inflater;
       // View v = inflater.inflate(R.layout.expandable_list,container,false);
        View v = inflater.inflate(R.layout.expandable_list,null);
        ExpandableListView listView = (ExpandableListView) v.findViewById(R.id.expandable_list_view);
        listView.setAdapter(new SavedTabsListAdapter());
        Log.v("INSIDE EXPANDABLELIST"," FUCK THIS SHIT");

        activity = (FoodEntryActivity) getActivity();
        foodsToSave = activity.foodsToPass();
        imgid = activity.getImageId();
        itemname = activity.getFoodNames();
        for(int i = 0; i < foodsToSave.size();i++){
            Log.v(foodsToSave.get(i).toString(), " EXPANDABLE LIST FRAG");
        }
        return v;
    }


    public class SavedTabsListAdapter extends BaseExpandableListAdapter {

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
            //TextView textView = new TextView(ExpandableListFragment.this.getActivity());
            //textView.setText(getGroup(i).name);

            View rowView=inflater.inflate(R.layout.consumed_foods, null,true);
            TextView txtTitle = (TextView) rowView.findViewById(R.id.txt);
            if (imgid != null) {
                ImageView imageView = (ImageView) rowView.findViewById(R.id.img);
                imageView.setImageResource(imgid.get(i));
            }
            TextView extratxt = (TextView) rowView.findViewById(R.id.textView1);

            txtTitle.setText(itemname.get(i));
            extratxt.setText("Description "+itemname.get(i));
            return rowView;

            //return textView;
        }

        @Override
        public View getChildView(int i, int i1, boolean b, View view, ViewGroup viewGroup) {
            TextView textView = new TextView(ExpandableListFragment.this.getActivity());
            textView.setText(getChild(i, i1).toString());
            textView.setBackgroundColor(Color.parseColor("#CDDC39"));
            return textView;
        }

        @Override
        public boolean isChildSelectable(int i, int i1) {
            return true;
        }

    }

}
