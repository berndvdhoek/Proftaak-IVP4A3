/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main.java.infosys.hartigehap.kitchen.DomainModel;

import java.util.ArrayList;

/**
 *
 * @author bernd_000
 */
public class Order {
    ArrayList<Meal> meals;
    int tableNr, orderNr;
    String status;

    /**
     * 
     * @param meals
     * @param tableNr
     * @param orderNr
     * @param status 
     */
    public Order(ArrayList<Meal> meals, int tableNr, int orderNr, String status) {
        this.meals = meals;
        this.tableNr = tableNr;
        this.orderNr = orderNr;
        this.status = status;
    }
    /**
     * 
     * @return 
     */
    public ArrayList<Meal> getMeals() {
        return meals;
    }
    /**
     * 
     * @return 
     */
    public int getTableNr() {
        return tableNr;
    }
    /**
     * 
     * @return 
     */
    public int getOrderNr() {
        return orderNr;
    }
    /**
     * 
     * @return 
     */
    public String getStatus() {
        return status;
    }
    
}
