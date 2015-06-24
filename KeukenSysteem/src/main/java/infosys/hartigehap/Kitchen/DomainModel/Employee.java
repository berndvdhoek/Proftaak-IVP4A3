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
public class Employee {
    private String email, password, function, nameCode;
    private byte[] salt;
    
    public Employee(String email, String password, String function, byte[] salt, String nameCode){
        this.email = email;
        this.password = password;
        this.function = function;
        this.salt       = salt;
        this.nameCode = nameCode;
    }
    
    public String getEmail(){
        return this.email;
    }
    
    public String getPassword(){
        return this.password;
    }
    
    public String getFunction(){
        return this.function;
    }
    
    public byte[] getSalt(){
        return this.salt;
    }
    
    public String getNameCode() {
        return this.nameCode;
    }
}
