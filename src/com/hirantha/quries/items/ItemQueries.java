package com.hirantha.quries.items;

import com.hirantha.models.data.item.Item;
import com.hirantha.quries.DBConnection;
import com.hirantha.quries.FDBConnection;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ItemQueries {

    public static String ITEMS_TABLE = "items";

    public static String ITEM_ID = "item_id"; // == item code
    public static String NAME = "item_name";
    public static String CATEGORY = "category";
    public static String UNIT = "unit";
    public static String RECEIPT_PRICE = "receipt_price";
    public static String MARKED_PRICE = "marked_price";
    public static String SELLING_PRICE = "selling_price";
    public static String PERCENTAGE = "percentage_flag";
    public static String RANK1 = "rank1";
    public static String RANK2 = "rank2";
    public static String RANK3 = "rank3";
    public static String DELETE_FLAG = "delete_flag";


    private static ItemQueries instance;

    private ItemQueries() {
    }

    public static ItemQueries getInstance() {
        if (instance == null) instance = new ItemQueries();
        return instance;
    }

    public void insertItem(Item item) {

        String query = "INSERT INTO " + ITEMS_TABLE
                + " VALUES ("
                + "'" + item.getItemCode() +  "',"
                + "'" + item.getName() + "',"
                + "'" + item.getCategory() + "',"
                + "'" + item.getUnit() + "',"
                + item.getReceiptPrice() + ","
                + item.getMarkedPrice() + ","
                + item.getSellingPrice() + ","
                + item.isPercentage() + ","
                + item.getRank1() + ","
                + item.getRank2() + ","
                + item.getRank3() + ","
                + "0);";

       
        DBConnection.executeQuery(query, false);
        
        FDBConnection.executeQuery(query, false);
        
    }

    public List<String> getUnits() {
        List<String> units = new ArrayList<>();
        String query = "SELECT DISTINCT " + UNIT + " FROM " + ITEMS_TABLE + ";";

        ResultSet resultSet = DBConnection.executeQuery(query);
        try {
            if (resultSet != null) {
                while (resultSet.next()) {
                    units.add(resultSet.getString(UNIT));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return units;
    }

    public List<String> getCategories() {
        List<String> categories = new ArrayList<>();
        String query = "SELECT DISTINCT " + CATEGORY + " FROM " + ITEMS_TABLE + ";";

        ResultSet resultSet = DBConnection.executeQuery(query);
        try {
            if (resultSet != null) {
                while (resultSet.next()) {
                    categories.add(resultSet.getString(CATEGORY));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return categories;
    }

    public List<Item> getItems() {
        List<Item> items = new ArrayList<>();

        String query = "SELECT * FROM " + ITEMS_TABLE + " WHERE " + DELETE_FLAG +"=0;";

        ResultSet resultSet = DBConnection.executeQuery(query);
        try {
            if (resultSet != null) {
                while (resultSet.next()) {
                    String itemID = resultSet.getString(ITEM_ID);
                    String name = resultSet.getString(NAME);
                    String category = resultSet.getString(CATEGORY);
                    String unit = resultSet.getString(UNIT);
                    double receiptPrice = resultSet.getDouble(RECEIPT_PRICE);
                    double markedPrice = resultSet.getDouble(MARKED_PRICE);
                    double sellingPrice = resultSet.getDouble(SELLING_PRICE);
                    boolean isPercentage = resultSet.getBoolean(PERCENTAGE);
                    double rank1 = resultSet.getDouble(RANK1);
                    double rank2 = resultSet.getDouble(RANK2);
                    double rank3 = resultSet.getDouble(RANK3);
                    items.add(new Item(itemID, name, category, unit, receiptPrice, markedPrice, sellingPrice, isPercentage, rank1, rank2, rank3));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return items;
    }

    public void deleteItem(Item item) {
        String query = "UPDATE items SET "+ DELETE_FLAG +" = 1 WHERE " + ITEM_ID + "='" + item.getItemCode() + "';";
        DBConnection.executeQuery(query, false);
        FDBConnection.executeQuery(query, false);
    }
    public void deleteItem(String id) {
        String query = "UPDATE items SET "+ DELETE_FLAG +" = 1 WHERE " + ITEM_ID + "='" + id + "';";
        DBConnection.executeQuery(query, false);
        FDBConnection.executeQuery(query, false);
    }

    public void updateItem(Item oldItem,Item item) {
        String oldItemCode = oldItem.getItemCode();
        oldItem.setItemCode(oldItemCode + "~" + String.valueOf(new Random().nextInt(9999)));
        insertItem(oldItem);
        
        String query = "UPDATE " + ITEMS_TABLE
                + " SET "
                + ITEM_ID + "='" + item.getItemCode()+ "', "
                + NAME + "='" + item.getName() + "', "
                + CATEGORY + "='" + item.getCategory() + "', "
                + UNIT + "='" + item.getUnit() + "', "
                + RECEIPT_PRICE + "=" + item.getReceiptPrice() + ", "
                + MARKED_PRICE + "=" + item.getMarkedPrice() + ", "
                + SELLING_PRICE + "=" + item.getSellingPrice() + ", "
                + PERCENTAGE + "=" + item.isPercentage() + ", "
                + RANK1 + "=" + item.getRank1() + ", "
                + RANK2 + "=" + item.getRank2() + ", "
                + RANK3 + "=" + item.getRank3() + ","
                + DELETE_FLAG + "=0" 
                + " WHERE " + ITEM_ID + "='" + oldItemCode + "';";

        DBConnection.executeQuery(query, false);
        FDBConnection.executeQuery(query, false);
        
        deleteItem(oldItem.getItemCode());
    }
    
    public boolean isItemCodeAvailable(String itemCode){
        String query = "SELECT * FROM " + ITEMS_TABLE + " WHERE " + DELETE_FLAG +"=0 AND "+ITEM_ID+"='"+itemCode+"';";
        ResultSet resultSet = DBConnection.executeQuery(query);
        try {
            return resultSet.next();
        } catch (SQLException ex) {
            Logger.getLogger(ItemQueries.class.getName()).log(Level.SEVERE, null, ex);
        }
        return true;
    }
}
