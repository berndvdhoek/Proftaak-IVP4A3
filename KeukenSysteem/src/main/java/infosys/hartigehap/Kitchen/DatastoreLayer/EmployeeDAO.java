/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main.java.infosys.hartigehap.kitchen.DatastoreLayer;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import main.java.infosys.hartigehap.kitchen.DomainModel.Employee;

/**
 *
 * @author bernd_000
 */
public class EmployeeDAO {


    
    public Employee getMedewerkerByEmail(String email){
        DatabaseConnection connection = new DatabaseConnection();

        if (connection.openConnection()) {
            ResultSet result = connection.executeSQLSelectStatement("SELECT * FROM `Employee` WHERE `Email` = '" + email + "';");
            
            try {
                if (result.next()) {
                    byte[] salt = result.getString("Password_Salt").getBytes();
                    Employee employee = new Employee(result.getString("Email"), result.getString("Password"), result.getString("Function"), salt, result.getString("NameCode"));
                    connection.closeConnection();
                    return employee;
                }
            } catch (SQLException ex) {
                Logger.getLogger(EmployeeDAO.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        connection.closeConnection();
        
        return null;
    }
    
    /**
     * Check if user exists in DTB
     * 
     * @param username :String
     * @return boolean
     */
    public boolean userExists(String email) {
        DatabaseConnection connection = new DatabaseConnection();
        
        if (connection.openConnection()) {

            ResultSet result = connection.executeSQLSelectStatement("SELECT * FROM `Employee` WHERE `email` = '" + email + "';");

            if(result == null){
                connection.closeConnection();
                return false;
            }

            connection.closeConnection();
            return true;
        }

        return false;
    }
    
        public void insertEmployee(String email, String password, String function, String nameCode, String salt) {
        DatabaseConnection connection = new DatabaseConnection();
        
        if (connection.openConnection()) {
            connection.executeUpdateStatement("INSERT INTO `Employee` (Email, Password, Function, NameCode, Password_Salt) VALUES ('" + email + "', '" + password + "', '" + function + "', '" + nameCode + "', '" + salt + "')");
        }
        
        connection.closeConnection();
        
    }

}


