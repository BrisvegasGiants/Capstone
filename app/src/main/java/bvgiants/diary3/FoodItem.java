package bvgiants.diary3;

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

    public FoodItem(String name, int calories, int sugar, int fat, int energy, int sodium,
                    int protein, String imageLocal) {
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
        return "Food: " +this.name + "Calories: " +this.calories + " This is just an example return. " +
                "Look at LookupFoodDBController -> FoodItem.toString()";
    }

    public String dbWriteFoodConsumed(){
        return this.orderID + " " + this.foodId + " " + this.location +  "\n";
    }
}
