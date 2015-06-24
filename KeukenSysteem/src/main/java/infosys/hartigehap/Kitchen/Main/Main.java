/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main.java.infosys.hartigehap.kitchen.Main;

import main.java.infosys.hartigehap.Kitchen.PresentationLayer.OrderOverview;
import main.java.infosys.hartigehap.kitchen.BusinessLogic.KitchenManager;
import main.java.infosys.hartigehap.kitchen.PresentationLayer.*;

/**
 *
 * @author Sjonn
 */
public class Main {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        KitchenManager manager = new KitchenManager();
        LoginGUI gui = new LoginGUI(manager);
        KeukenGUI2 gui2 = new KeukenGUI2(manager);
        OrderOverview gui4 = new OrderOverview(manager);
    }
}
