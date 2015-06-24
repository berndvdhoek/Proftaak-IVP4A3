/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main.java.infosys.hartigehap.Kitchen.PresentationLayer;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.Timer;
import main.java.infosys.hartigehap.kitchen.BusinessLogic.KitchenManager;
import main.java.infosys.hartigehap.kitchen.DomainModel.Meal;
import main.java.infosys.hartigehap.kitchen.DomainModel.Order;

/**
 *
 * @author bernd_000
 */
public class OrderOverview {
    private final KitchenManager manager;
    private JFrame frame;
    private ArrayList<Order> orders;
    private float fontSize = 27;
    private Timer t;
    
    public OrderOverview(KitchenManager m) {
        manager = m;
        init();
    }
    
    private void init() {
        createFrame();
        Timer t = new Timer(1000, new TimerHandler());
        t.start();
        redraw();
    }
    
    private void createFrame() {
        frame = new JFrame();
        frame.setSize(1900, 1000);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setTitle("Keuken");
        frame.setVisible(true);
    }
    
    private void redraw() {
        orders = manager.getOrders();
        JPanel mainPanel = new JPanel(new GridLayout(3, 3, 3, 3));
        int x = 0;
        for (Order o: orders) {
            if (x < 10) {
                if (o.getStatus().equals("Accepted")) {
                    if (o.getMeals().size() > 6 && o.getMeals().size() <= 8) {
                        fontSize /= 1.2;
                    } else if (o.getMeals().size() > 8) {
                        fontSize /= 1.5;
                    }
                    
                    JPanel panel = new JPanel(new BorderLayout());
                    panel.add(createLabel(o.getOrderNr() + "", fontSize, 5), BorderLayout.NORTH);

                    JPanel centerPanel = new JPanel(new GridLayout(0, 1));
                    JPanel eastPanel = new JPanel(new GridLayout(0, 2));
                    for (Meal m: o.getMeals()) {
                        centerPanel.add(createLabel(m.getName(), fontSize, 2));
                        eastPanel.add(createLabel(m.getAmount() + "", fontSize, 2));
                        eastPanel.add(createLabel(" " + m.getPreporationTime() + " minuten ", fontSize, 2));
                    }
                    panel.add(centerPanel, BorderLayout.CENTER);
                    panel.add(eastPanel, BorderLayout.EAST);

                    mainPanel.add(panel);
                    fontSize = 27;
                    x += 1;
                }
            } else {
                break;
            }
        }
        while (x < 9) {
            mainPanel.add(new JLabel(""));
            x += 1;
        }
        frame.add(mainPanel);
        frame.revalidate();
        frame.repaint();
    }
    
    private JLabel createLabel(String text, float points, int thickness) {
        JLabel label = new JLabel(text, SwingConstants.CENTER);
        label.setFont(label.getFont().deriveFont(points));
        label.setBorder(BorderFactory.createLineBorder(Color.blue, thickness));
        return label;
    }
    
    class TimerHandler implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            redraw();
        }
    }
}
