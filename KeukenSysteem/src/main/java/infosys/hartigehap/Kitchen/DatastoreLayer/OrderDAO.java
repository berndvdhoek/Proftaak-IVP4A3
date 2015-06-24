/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main.java.infosys.hartigehap.kitchen.DatastoreLayer;

import main.java.infosys.hartigehap.kitchen.DomainModel.Meal;
import main.java.infosys.hartigehap.kitchen.DomainModel.Order;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author bernd_000
 */
public class OrderDAO {
    MealDAO mealDAO = new MealDAO();
    /**
     * gets all the orders with the status 'placed' or 'accepted'
     * @return 
     */
    public ArrayList<Order> getAllOrders() {
        DatabaseConnection connection = new DatabaseConnection();

        ArrayList<Order> orders = new ArrayList<>();
        
        if (connection.openConnection()) {
            ResultSet result = connection.executeSQLSelectStatement("SELECT * FROM `kitchenOrders` WHERE `Status` = 'Placed' OR `Status` = 'Accepted'");
            
            try {
                while (result.next()) {
                    int tableNr = result.getInt("TableNr");
                    int orderID = result.getInt("OrderID");
                    String status = result.getString("Status");
                    ArrayList<Meal> meals = mealDAO.getMeals(orderID);
                    
                    orders.add(new Order(meals, tableNr, orderID, status));
                }
            } catch (SQLException ex) {
                
            }
        } else {
            
        }

        connection.closeConnection();
        return orders;
    }
    /**
     * get the status of a order
     * @param orderNr
     * @return 
     */
    public String getStatus(int orderNr) {
        DatabaseConnection connection = new DatabaseConnection();

        String status = null;
        
        if (connection.openConnection()) {
            ResultSet result = connection.executeSQLSelectStatement("SELECT Status FROM `order` WHERE `OrderID` = " + orderNr + "");
            
            try {
                while (result.next()) {
                    status = result.getString("Status");
                }
            } catch (SQLException ex) {
                Logger.getLogger(OrderDAO.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        connection.closeConnection();
        return status;
    }
    /**
     * sets the status of a order to the given status
     * @param orderNr
     * @param status 
     */
    public void setStatus(int orderNr, String status) {
        DatabaseConnection connection = new DatabaseConnection();
        
        if (connection.openConnection()) {
            connection.executeUpdateStatement("UPDATE `order` SET Status = '" + status + "' WHERE OrderID = " + orderNr + "");
        }

        connection.closeConnection();
    }
}