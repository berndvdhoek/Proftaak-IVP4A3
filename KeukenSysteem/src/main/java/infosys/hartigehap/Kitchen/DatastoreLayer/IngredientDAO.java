/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main.java.infosys.hartigehap.kitchen.DatastoreLayer;

import main.java.infosys.hartigehap.kitchen.DomainModel.Ingredient;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 *
 * @author bernd_000
 */
public class IngredientDAO {

    public ArrayList<Ingredient> getIngredients(int mealID) {
        System.out.println("getting ingredients in dao");
        DatabaseConnection connection = new DatabaseConnection();

        ArrayList<Ingredient> ingredients = new ArrayList<>();
        
        if (connection.openConnection()) {
            ResultSet result = connection.executeSQLSelectStatement("SELECT * FROM `ingredient_meal` WHERE `MealProductID` = " + mealID);
            
            try {
                while (result.next()) {
                    int id = result.getInt("IngredientID");
                    int quantity = result.getInt("quantity");
                    System.out.println(id + ": " + quantity);
                    String name = getNameByID(id);
                    ingredients.add(new Ingredient(id, quantity, name));
                }
            } catch (SQLException ex) {
                
            }
        } else {
            
        }

        connection.closeConnection();
        return ingredients;
    }
    
    public String getNameByID(int ingredientID) {
        DatabaseConnection connection = new DatabaseConnection();
        String name = null;
        
        if (connection.openConnection()) {
            ResultSet result = connection.executeSQLSelectStatement("SELECT Name FROM `ingredient` WHERE `IngredientID` = " + ingredientID);
            
            try {
                while (result.next()) {
                    name = result.getString("Name");
                }
            } catch (SQLException ex) {
                
            }
        } else {
            
        }

        connection.closeConnection();
        return name;
    }
    
}
