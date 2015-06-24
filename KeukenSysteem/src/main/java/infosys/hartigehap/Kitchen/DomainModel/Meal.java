/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main.java.infosys.hartigehap.kitchen.DomainModel;

/**
 *
 * @author bernd_000
 */
public class Meal {
    String name;
    int amount;
    int id;
    int preporationTime;
    /**
     * 
     * @param name
     * @param amount 
     * @param preporationTime 
     * @param id 
     */
    public Meal(String name, int amount, int preporationTime, int id) {
        this.name = name;
        this.amount = amount;
        this.preporationTime = preporationTime;
        this.id = id;
    }
    
    /**
     * 
     * @return 
     */
    public String getName() {
        return name;
    }

    /**
     * 
     * @return 
     */
    public int getAmount() {
        return amount;
    }
    
    public int getPreporationTime() {
        return preporationTime;
    }
    
    public int getID() {
        return id;
    }
}
