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

    public int getOrderID() {
        return orderID;
    }

    public void setOrderID(int orderID) {
        this.orderID = orderID;
    }

    public int getOrderTypeCode() {
        return orderTypeCode;
    }

    public void setOrderTypeCode(int orderTypeCode) {
        this.orderTypeCode = orderTypeCode;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }
}
