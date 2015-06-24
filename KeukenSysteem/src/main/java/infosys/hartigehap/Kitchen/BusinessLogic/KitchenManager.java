/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main.java.infosys.hartigehap.kitchen.BusinessLogic;

import main.java.infosys.hartigehap.kitchen.DatastoreLayer.EmployeeDAO;
import main.java.infosys.hartigehap.kitchen.DatastoreLayer.IngredientDAO;
import main.java.infosys.hartigehap.kitchen.DatastoreLayer.MealDAO;
import main.java.infosys.hartigehap.kitchen.DatastoreLayer.OrderDAO;
import main.java.infosys.hartigehap.kitchen.DomainModel.Employee;
import main.java.infosys.hartigehap.kitchen.DomainModel.Ingredient;
import main.java.infosys.hartigehap.kitchen.DomainModel.Meal;
import main.java.infosys.hartigehap.kitchen.DomainModel.Order;
import java.util.ArrayList;
import java.security.MessageDigest;
import java.security.SecureRandom;
import static java.util.Collections.list;
import java.util.Iterator;
import java.util.Random;
import java.util.UUID;

/**
 *
 * @author bernd_000
 */
public class KitchenManager {

    private ArrayList<Employee> loggedInEmployees = new ArrayList<>();
    private boolean createUserCredentials = true;
    private boolean debug = true;

    public KitchenManager() {
    }

    /**
     * get the orders with the status 'placed'
     *
     * @return
     */
    public ArrayList<Order> getOrders() {
        OrderDAO orderDAO = new OrderDAO();

        ArrayList<Order> orders;
        orders = orderDAO.getAllOrders();
        return orders;
    }

    public ArrayList<Employee> getLoggedInEmployees() {
        return loggedInEmployees;
    }

    /**
     * get the status of a certain order
     *
     * @param orderNr
     * @return
     */
    public String getStatus(int orderNr) {
        OrderDAO orderDAO = new OrderDAO();
        String status = orderDAO.getStatus(orderNr);
        return status;
    }

    /**
     * get the meals for a certain order
     *
     * @param orderNr
     * @return
     */
    public ArrayList<Meal> getMeals(int orderNr) {
        MealDAO mealDAO = new MealDAO();
        ArrayList<Meal> meals = mealDAO.getMeals(orderNr);
        return meals;
    }

    /**
     * set the status from a order to the next status
     *
     * @param orderNr
     * @param status
     */
    public void setStatus(int orderNr, String status) {
        OrderDAO orderDAO = new OrderDAO();
        orderDAO.setStatus(orderNr, status);
    }

    /**
     * Check if user exists
     *
     * @param username :String
     * @return boolean
     */
    public boolean userExists(String email) {
        EmployeeDAO EmployeeDAO = new EmployeeDAO();
        return EmployeeDAO.userExists(email);
    }

    /**
     * getMakeUserEnabled
     *
     * @param email
     * @return
     */
    public boolean getCreateUserCredentials() {
        return this.createUserCredentials;
    }

    /**
     *
     * @param email
     * @return
     */
    public Employee findUserByEmail(String email) {
        EmployeeDAO dao = new EmployeeDAO();

        return dao.getMedewerkerByEmail(email);
    }

    /**
     * Log User in
     *
     * @param String email
     * @param char[] password
     * @return boolean
     */
    public boolean logUserIn(String email, char[] password) {

        EmployeeDAO EmployeeDAO = new EmployeeDAO();
        Employee employee = EmployeeDAO.getMedewerkerByEmail(email);

        if (employee != null) {
            //Salt password
            String saltedPassword = saltPassword(password, employee.getSalt());

            //hashPassword
            byte[] byteArraySaltedPassword = saltedPassword.getBytes();
            String hashedPassword = hashPassword(byteArraySaltedPassword);

            if (this.debug == true) {
                System.out.println("Salt for user: " + byteArrayToString(employee.getSalt()) + "\n");
                System.out.println("Inserted  hashedPassword: " + hashedPassword);
                System.out.println("Insterted saltedPassword: " + byteArrayToString(byteArraySaltedPassword) + "\n");
                System.out.println("PW from dtb(hashed): " + employee.getPassword());
                System.out.println("Ingevoerd pw: " + hashedPassword + "\n");
            }

            //Check if passwords are the same
            if (employee.getPassword().equals(hashedPassword)) {
                this.loggedInEmployees.add(employee);
                return true;
            }
        }

        return false;
    }

    /**
     * Hash the password.
     *
     * @param password
     * @param salt
     * @return
     */
    public String saltPassword(char[] password, byte[] salt) {
        //Typecast char arrays to string before concatenating
        String strPassword = String.valueOf(password);
        String strSalt = new String(salt);

        //Salt password & return
        return strPassword + strSalt;
    }

    /**
     * MD5 hashpassword
     *
     * @param saltedPassword
     * @return MD5 hashed password
     */
    public String hashPassword(byte[] saltedPassword) {
        byte[] hashedPassword = new byte[0];

        //Hash password
        try {
            MessageDigest m = MessageDigest.getInstance("MD5");
            hashedPassword = m.digest(saltedPassword);
        } catch (Exception ex) {
        }

        //Make empty string to contain hashedPassword (convert from byte[] to string)
        return byteArrayToString(hashedPassword);
    }

    /**
     * typecast byte[] to String
     *
     * Loop trough every byte to format it to a hexadecimal string
     *
     * @param array
     * @return
     */
    public String byteArrayToString(byte[] array) {
        //Make empty string to contain hashedPassword (convert from byte[] to string)
        String string = "";
        //Loop trough array to format from hex to string
        for (byte b : array) {
            string += String.format("%02x", b);
        }

        return string;
    }

    /**
     * Is not being used
     *
     * @return
     */
    public byte[] createSalt() {
        Random r = new SecureRandom();
        byte[] salt = new byte[32];
        r.nextBytes(salt);
        return salt;
    }

    /**
     * Creates password data for database User has to set this data into
     * database by hand strHashedPassword should be put in the password field
     * strSalt should be put in the salt field
     *
     * @param password
     */
    public void createLoginCredentials(char[] password, String email) {
        //Create salt
        //byte[] salt = createSalt();
        String strSalt = UUID.randomUUID().toString();
        byte[] salt = strSalt.getBytes();

        //Typecast to string so readable
        String strPassword = String.valueOf(password);

        String saltedPassword = saltPassword(password, salt);

        byte[] byteArraySaltedPassword = saltedPassword.getBytes();
        String hashedPassword = hashPassword(byteArraySaltedPassword);
        
        EmployeeDAO EmployeeDAO = new EmployeeDAO();
        
        EmployeeDAO.insertEmployee(email, hashedPassword, "Kitchen", "", strSalt);

        //Printlines are used to deliver data for dtb
        System.out.println("stringPassword: " + strPassword);
        System.out.println("strSalt: " + strSalt);
        System.out.println("hashedPassword: " + hashedPassword);
        System.out.println("saltedPassword: " + saltedPassword);
    }

    public void insertMealEmpoyee(int orderNr, String mealName, String employeeNameCode) {
        String employeeEmail = null;
        int mealID = 0;
        for (Employee e : loggedInEmployees) {
            System.out.println("-- kitchenmanager test part --");
            System.out.println(e.getNameCode());
            System.out.println(employeeNameCode);
            System.out.println("----------");
            if (e.getNameCode().equals(employeeNameCode)) {
                employeeEmail = e.getEmail();
            }
        }
        for (Meal m : getMeals(orderNr)) {
            System.out.println(m.getName());
            System.out.println(mealName);
            System.out.println("-----------");
            if (m.getName().equals(mealName)) {
                mealID = m.getID();
            }
        }

        MealDAO m = new MealDAO();
        System.out.println(mealID + ", " + employeeEmail);
        m.insertMealEmployee(mealID, employeeEmail);
    }

    public ArrayList<Ingredient> getIngredients(String meal, int orderNr) {
        System.out.print("getting ingredients");
        ArrayList<Ingredient> ingredients = new ArrayList<>();
        int mealID = 0;
        IngredientDAO i = new IngredientDAO();
        for (Meal m : getMeals(orderNr)) {
            if (m.getName().equals(meal)) {
                mealID = m.getID();
            }
        }
        ingredients = i.getIngredients(mealID);

        return ingredients;
    }

    public void logUserOut(String nameCode) {
        for (Iterator<Employee> it = loggedInEmployees.iterator(); it.hasNext();) {

            Employee e = it.next();
            if (e.getNameCode().equals(nameCode)) {
                it.remove();
            }
        }
    }
}
