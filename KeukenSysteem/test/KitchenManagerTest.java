/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.util.ArrayList;
import junit.framework.TestCase;
import main.java.infosys.hartigehap.kitchen.DomainModel.Employee;
import main.java.infosys.hartigehap.kitchen.BusinessLogic.KitchenManager;

/**
 *
 * @author Sjonn
 */
public class KitchenManagerTest extends TestCase {
    private ArrayList<Employee> LoggedInEmployees = new ArrayList<>();
    private boolean createUserCredentials = true;
    private KitchenManager manager;
    private final String passwordString         = "wachtwoord";
    private final String saltPasswordString     = "66626132343963612d616137382d343961372d626330312d336566316438366538393963";
    private final String saltEDPasswordString   = "6b616e6b657266626132343963612d616137382d343961372d626330312d336566316438366538393963";
    private final String hashedPasswordString   = "2efc6a9bb7e429bcb324e155e28f6b93";
    private final byte[] saltedPasswordByteArray;
    
    public KitchenManagerTest(String testName) {
        super(testName);
        KitchenManager manager = new KitchenManager();
        this.saltedPasswordByteArray = saltEDPasswordString.getBytes();
    }
    
    @Override
    protected void setUp() throws Exception {
        super.setUp();
    }
    
    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
    }
    
    protected void testHashPassword(){
        String result = manager.hashPassword(saltEDPasswordString.getBytes());
        assertEquals(result, hashedPasswordString);
    }
    
    protected void testSaltPassword(){
        
    }

    // TODO add test methods here. The name must begin with 'test'. For example:
    // public void testHello() {}
}
