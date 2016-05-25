package bvgiants.diary3;

import java.util.ArrayList;
import java.util.List;
import android.util.SparseArray;
/**
 * Created by kenst on 2/05/2016.
 * Object class for an item of Food
 */
public class FoodItem {

    //Vars for FoodConsumed Table
    public int orderID;
    public int foodId;
    public String location;

    //Vars for a LookupFood Table
    public String name;
    public int calories;
    public int sugar;
    public int fat;
    public int energy;
    public int sodium;
    public int protein;
    public String imagelocal;

    public final List<String> children = new ArrayList<String>();
    public String string;

    public FoodItem( int foodId, String name, int calories, int sugar, int fat, int energy, int sodium,
                    int protein, String imageLocal) {
        this.foodId = foodId;
        this.name = name;
        this.calories = calories;
        this.sugar = sugar;
        this.fat = fat;
        this.energy = energy;
        this.sodium = sodium;
        this.protein = protein;
        this.imagelocal = imageLocal;
    }

    public FoodItem(int orderID,int foodID, String location){
        this.orderID = orderID;
        this.foodId = foodID;
        this.location = location;
    }

    public String toString(){
        return "CALORIES: " +this.calories + " ENERGY: " + this.energy + " FAT: " + this.fat + "\n" +
                "SUGAR: " + this.sugar + " SODIUM: " + this.sodium + " PROTEIN: " + this.protein;
    }

    public String dbWriteFoodConsumed(){
        return this.orderID + " " + this.foodId + " " + this.location +  "\n";
    }

    public void setOrderID(int orderID) {
        this.orderID = orderID;
    }

    public int getFoodId() {
        return foodId;
    }

    public void setFoodId(int foodId) {
        this.foodId = foodId;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public int getOrderID() {
        return orderID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getCalories() {
        return calories;
    }

    public void setCalories(int calories) {
        this.calories = calories;
    }

    public int getSugar() {
        return sugar;
    }

    public void setSugar(int sugar) {
        this.sugar = sugar;
    }

    public int getFat() {
        return fat;
    }

    public void setFat(int fat) {
        this.fat = fat;
    }

    public int getEnergy() {
        return energy;
    }

    public void setEnergy(int energy) {
        this.energy = energy;
    }

    public int getSodium() {
        return sodium;
    }

    public void setSodium(int sodium) {
        this.sodium = sodium;
    }

    public int getProtein() {
        return protein;
    }

    public void setProtein(int protein) {
        this.protein = protein;
    }

    public String getImagelocal() {
        return imagelocal;
    }

    public void setImagelocal(String imagelocal) {
        this.imagelocal = imagelocal;
    }
}
