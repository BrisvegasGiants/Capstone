package bvgiants.diary3;

/**
 * Created by kenst on 4/05/2016.
 */
public class OrderRow {

    public int orderID;
    public int orderTypeCode;
    public String date;
    public int userID;

    public OrderRow (int orderID, int orderTypeCode, String date, int userID){
        this.orderID = orderID;
        this.orderTypeCode = orderTypeCode;
        this.date = date;
        this.userID = userID;
    }

    public String dbWriteOrdersToFile (){
        return orderID + " " + orderTypeCode + " " + date + " " + userID + "\n";
    }
}
