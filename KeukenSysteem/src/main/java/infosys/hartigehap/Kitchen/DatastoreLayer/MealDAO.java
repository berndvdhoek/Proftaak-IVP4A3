/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main.java.infosys.hartigehap.kitchen.DatastoreLayer;

import main.java.infosys.hartigehap.kitchen.DomainModel.Employee;
import main.java.infosys.hartigehap.kitchen.DomainModel.Meal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author bernd_000
 */
public class MealDAO {
    /**
     * returns all the meals for a certain order
     * @param orderID
     * @return 
     */
    public ArrayList<Meal> getMeals(int orderID)  {
        DatabaseConnection connection = new DatabaseConnection();

        ArrayList<Meal> meals = new ArrayList<>();
        
        if (connection.openConnection()) {
            ResultSet result = connection.executeSQLSelectStatement("SELECT MealProductID FROM kitchen_order_meal WHERE OrderID = " + orderID + "");
            
            try {
                while (result.next()) {
                    int mealProductID = result.getInt("MealProductID");
                    meals.add(new Meal(getMealName(mealProductID), getMealQuantity(mealProductID, orderID), getMealPreporationtime(mealProductID), mealProductID));
                }
            } catch (SQLException ex) {
                Logger.getLogger(MealDAO.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        connection.closeConnection();
        return meals;
    }
    /**
     * 
     * @param mealProductID
     * @return 
     */
    private String getMealName(int mealProductID) {
        DatabaseConnection connection = new DatabaseConnection();

        String name = null;
        
        if (connection.openConnection()) {
            ResultSet result = connection.executeSQLSelectStatement("SELECT Name FROM Meal WHERE ProductID = " + mealProductID + "");
            
            try {
                while (result.next()) {
                    name = result.getString("Name");
                }
            } catch (SQLException ex) {
                Logger.getLogger(MealDAO.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        connection.closeConnection();
        return name;
    }
    /**
     * gets the quantity of a meal in a certain order
     * @param mealProductID
     * @param orderID
     * @return 
     */
    private int getMealQuantity(int mealProductID, int orderID) {
        DatabaseConnection connection = new DatabaseConnection();

        int quantity = 0;
        
        if (connection.openConnection()) {
            ResultSet result = connection.executeSQLSelectStatement("SELECT Quantity FROM kitchen_order_meal WHERE OrderID = " + orderID + " AND MealProductID = " + mealProductID + "");
            
            try {
                while (result.next()) {
                    quantity = result.getInt("Quantity");
                }
            } catch (SQLException ex) {
                Logger.getLogger(MealDAO.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        connection.closeConnection();
        return quantity;
    }

    public void insertMealEmployee(int mealID, String employeeEmail) {
        DatabaseConnection connection = new DatabaseConnection();
        
        if (connection.openConnection()) {
            connection.executeUpdateStatement("INSERT INTO `employee_meal` (MealProductID, EmployeeEmail) VALUES (" + mealID + ", '" + employeeEmail + "')");
        }
        
        connection.closeConnection();
        
    }

    private int getMealPreporationtime(int mealProductID) {
        DatabaseConnection connection = new DatabaseConnection();

        int id = 0;
        
        if (connection.openConnection()) {
            ResultSet result = connection.executeSQLSelectStatement("SELECT Preporationtime FROM Meal WHERE ProductID = " + mealProductID + " ORDER BY Preporationtime ASC");
            
            try {
                while (result.next()) {
                    id = result.getInt("Preporationtime");
                }
            } catch (SQLException ex) {
                Logger.getLogger(MealDAO.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        connection.closeConnection();
        return id;
        
    }
    
    
}