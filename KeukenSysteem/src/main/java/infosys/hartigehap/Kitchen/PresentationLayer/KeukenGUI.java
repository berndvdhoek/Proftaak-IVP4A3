/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main.java.infosys.hartigehap.kitchen.PresentationLayer;

import main.java.infosys.hartigehap.kitchen.BusinessLogic.KitchenManager;
import main.java.infosys.hartigehap.kitchen.DomainModel.Meal;
import main.java.infosys.hartigehap.kitchen.DomainModel.Order;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.*;
import java.util.ArrayList;
import javax.swing.*;

/**
 *
 * @author bernd_000
 */
public class KeukenGUI extends JPanel {

    private int selectedOrder;
    private final KitchenManager manager;
    private JFrame frame;
    private JPanel[] panelArray = new JPanel[30];
    private JPanel mainPanel, topPanel, BLCornerPanel, BRCornerPanel;
    private ArrayList<Order> orders;
    private float fontSize = 27;

    public KeukenGUI(KitchenManager kManager) {
        manager = kManager;
        TimerHandler t = new TimerHandler();
        orders = manager.getOrders();
        //create the frame
        createFrame();
        init();
    }

    private void init() {
        //create the main panel
        //this panel is the container for every other panel
        //GridLayout for the 5 subpanels
        mainPanel = new JPanel(new BorderLayout());

        //Create the 2 subpanels for the BottomPanel
        //Bottom left has 1 JPanel
        BLCornerPanel = addOrders();

        //Bottom right has 3 JPanels underneath each other
        createBRPanel();

        //Add the 3 subpanels to the mainpanel
        mainPanel.add(topPanel, BorderLayout.NORTH);
        mainPanel.add(BLCornerPanel, BorderLayout.CENTER);
        //mainPanel.add(BRCornerPanel, BorderLayout.EAST);

        frame.setContentPane(mainPanel);

        redraw();
    }

    private JPanel addOrders() {
        JPanel panel = new JPanel(new GridLayout(0, 3, 5, 5));
        int x = 0;
        for (Order o : orders) {
            if (o.getStatus().equals("Accepted")) {

                panelArray[x] = new JPanel(new BorderLayout());
                JPanel northPanel = new JPanel(new GridLayout(1, 1));
                JPanel centerPanel = new JPanel(new GridLayout(0, 1));
                JPanel eastPanel = new JPanel(new GridLayout(0, 2));

                // north panel
                northPanel.setBackground(Color.white);
                //northPanel.add(createLabel("TafelNr: " + o.getTableNr(), fontSize, 2));
                //northPanel.add(createLabel("", fontSize, 2));
                northPanel.add(createLabel("Bestellingnummer " + o.getOrderNr(), fontSize, 2));

                // center panel
                centerPanel.setBackground(Color.white);
                eastPanel.setBackground(Color.white);

                if (o.getMeals().size() > 6 && o.getMeals().size() <= 8) {
                    fontSize /= 1.2;
                } else if (o.getMeals().size() > 8) {
                    fontSize /= 1.5;
                }

                for (Meal m : o.getMeals()) {
                    centerPanel.add(createLabel(" " + m.getName(), fontSize, 1));
                    eastPanel.add(createLabel(Integer.toString(m.getAmount()), fontSize, 1));
                    eastPanel.add(createLabel(m.getPreporationTime() + " minuten", fontSize, 1));
                }
                
                fontSize = 27;

                panelArray[x].add(northPanel, BorderLayout.NORTH);
                panelArray[x].add(centerPanel, BorderLayout.CENTER);
                panelArray[x].add(eastPanel, BorderLayout.EAST);

                // determine if the order is selected and adjust the color
                if (o.getOrderNr() != selectedOrder) {
                    panelArray[x].setBorder(BorderFactory.createLineBorder(Color.blue, 5));
                } else {
                    panelArray[x].setBorder(BorderFactory.createLineBorder(Color.red, 5));
                }
                panel.add(panelArray[x]);
                x++;
            }
        }
        return panel;
    }

    private JLabel createLabel(String text, float points, int thickness) {
        JLabel label = new JLabel(text, SwingConstants.CENTER);
        label.setFont(label.getFont().deriveFont(points));
        label.setBorder(BorderFactory.createLineBorder(Color.blue, thickness));
        return label;
    }

    private void createFrame() {
        frame = new JFrame();
        frame.setSize(1900, 1000);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setTitle("Keuken");
        frame.setVisible(true);
    }

    private void createBRPanel() {
        BRCornerPanel = new JPanel();
        BRCornerPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        BRCornerPanel.setLayout(new BorderLayout());
        BRCornerPanel.add(new JButton("Bestelling gereed"), BorderLayout.SOUTH);
    }

    private void redraw() {
        // get the new orders from the database
        orders = manager.getOrders();

        // repaint
        frame.getContentPane().remove(BLCornerPanel);
        BLCornerPanel = addOrders();
        frame.add(BLCornerPanel);
        frame.revalidate();
        frame.repaint();
    }

    class TimerHandler implements ActionListener {

        Timer t = new Timer(1000, this);
        int passedSeconds = 0;

        public TimerHandler() {
            t.start();
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            passedSeconds += 1;

            if (passedSeconds % 1 == 0) {
                System.out.println("1 second passed");
                redraw();
            }
        }
    }
}