/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main.java.infosys.hartigehap.kitchen.PresentationLayer;

import main.java.infosys.hartigehap.kitchen.BusinessLogic.KitchenManager;
import main.java.infosys.hartigehap.kitchen.DomainModel.Employee;
import main.java.infosys.hartigehap.kitchen.DomainModel.Ingredient;
import main.java.infosys.hartigehap.kitchen.DomainModel.Meal;
import main.java.infosys.hartigehap.kitchen.DomainModel.Order;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.*;
import java.util.ArrayList;
import javax.swing.*;
import javax.swing.border.TitledBorder;

/**
 *
 * @author bernd_000
 */
public class KeukenGUI2 extends JPanel {

    private int selectedOrder;
    private final KitchenManager manager;
    private JFrame frame;
    private JPanel mainPanel, topPanel, bottomPanel, TLCornerPanel, TCPanel, TRCornerPanel, BLCornerPanel, BRCornerPanel;
    private ArrayList<Order> orders;
    private ArrayList<JComboBox> nameCodes;
    private ArrayList<JLabel> mealNames;
    private JLabel titleLabel, selectedEmployeesLabel = new JLabel("", SwingConstants.CENTER);
    private final float fontSize = 27;
    private final int minGridSize = 12;
    private JButton readyButton, acceptButton, returnButton, logOutButton;
    TimerHandler t = new TimerHandler();
    private boolean changed = false, watchingIngredients = false;
    private String currentMeal;
    private JComboBox logOutEmployee;
    
    /**
     * The constructor creates a manager and a timer
     * The timer is used to refresh the orders every x seconds
     * It also creates a JFrame and initializes it
     * @param kManager 
     */
    public KeukenGUI2(KitchenManager kManager) {
        manager = kManager;
        orders = manager.getOrders();
        selectedEmployeesLabel.setForeground(Color.red);
        //create the frame
        createFrame();
        init();
    }
    
    /**
     * init creates all the toplevel JPanel on the JFrame
     * Three final JPanels which are found on the top panel
     * and three dynamic JPanel which are found on the bottom panel
     * the bottom and top panel are added to the main panel
     * after the creation of the JPanels everything is redrawn
     */
    private void init() {
        //create the main panel
        //this panel is the container for every other panel
        //GridLayout for the 5 subpanels
        mainPanel = new JPanel(new BorderLayout());

        //create the top subpanel
        topPanel = new JPanel(new GridLayout(1, 3));
        bottomPanel = new JPanel(new GridLayout(1, 2));
        //create the 3 subpanels for the TopPanel
        //TL = Top left, TR = Top right ...
        createTopPanelSubpanels();

        //Add the subpanels to the TopPanel
        addPanelsToTopPanel();

        //Create the 2 subpanels for the BottomPanel
        //Bottom left has 1 JPanel
        BLCornerPanel = new JPanel();

        //Bottom right has 3 JPanels underneath each other
        BRCornerPanel = startingBRPanel();

        // add BL and BR to bottom
        bottomPanel.add(BLCornerPanel);
        bottomPanel.add(BRCornerPanel);

        //Add the 2 subpanels to the mainpanel
        mainPanel.add(topPanel, BorderLayout.NORTH);
        mainPanel.add(bottomPanel);

        frame.setContentPane(mainPanel);
        redraw();
    }

    /**
     * createlabel creates custom JLabels with a border
     * @param text
     * @param points
     * @param thickness
     * @param c
     * @return 
     */
    private JLabel createLabel(String text, float points, int thickness, Color c) {
        // make a new JLabel which is centered
        JLabel label = new JLabel(text, SwingConstants.CENTER);
        
        // set the size to the parameter points and create a line border around it with color c and a certain thickness
        label.setFont(label.getFont().deriveFont(points));
        label.setBorder(BorderFactory.createLineBorder(c, thickness));
        
        // return the created JLabel
        return label;
    }

    /**
     * createFrame creates the frame
     */
    private void createFrame() {
        frame = new JFrame();
        frame.setSize(1900, 1000);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setTitle("Keuken");
        frame.setVisible(true);
    }
    
    /* 
        Creates the top panels of the GUI
        The left panel contains an image of 'De Hartige Hap'
        The center panel contains the title
        The right panel contains a button to log out
    */
    private void createTopPanelSubpanels() {
        //Top left corner has a image
        TLCornerPanel = new JPanel();

        //Top right corner has 1 JButton in the top right of the grid
        //This button is used to log out
        TRCornerPanel = new JPanel(new GridLayout(2, 2));
        logOutEmployee = createComboBox();
        logOutButton = new JButton("Log uit");
        logOutButton.addActionListener(new ButtonHandler());
        TRCornerPanel.add(logOutEmployee);
        TRCornerPanel.add(logOutButton);
        TRCornerPanel.add(new JLabel(""));
        TRCornerPanel.add(new JLabel(""));

        //Top center has a JLabel with the name of the restaurant
        titleLabel = new JLabel("De Hartige Hap", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Sans serif", Font.PLAIN, 40));
        
        TCPanel = new JPanel();
        TCPanel.add(titleLabel);
    }
    
    /**
     * adds the three subpanels to the top panel
     */
    private void addPanelsToTopPanel() {
        topPanel.add(TLCornerPanel);
        topPanel.add(TCPanel);
        topPanel.add(TRCornerPanel);
    }

    /**
     * CreateBRPanel creates the panel in which you can either
     * accept the orders or in which you can set the status to 'ready'
     * @return 
     */
    private JPanel createBRPanel() {
        JPanel panel = new JPanel();
        if (!watchingIngredients) { 
            if (selectedOrder != 0) {
                panel = new JPanel(new BorderLayout());

                JPanel subpanel = createBRSubpanel();
                String title;

                if (selectedOrder == 0) {
                    title = "Geen bestelling geselecteerd";
                } else {
                    title = "Geselecteerde bestelling: " + selectedOrder;
                }
                panel.setBorder(BorderFactory.createTitledBorder(null, title, TitledBorder.LEFT, TitledBorder.TOP, new Font("Sans serif", Font.PLAIN, 25)));

                panel.add(subpanel, BorderLayout.CENTER);
            }
        } else {
            String title;
            panel = new JPanel(new GridLayout(12, 1));
            if (selectedOrder == 0) {
                title = "Geen bestelling geselecteerd";
            } else {
                title = "Geselecteerd gerecht: " + currentMeal;
            }
            panel.setBorder(BorderFactory.createTitledBorder(null, title, TitledBorder.LEFT, TitledBorder.TOP, new Font("Sans serif", Font.PLAIN, 25)));
            ArrayList<Ingredient> ingredients = manager.getIngredients(currentMeal, selectedOrder);
            
            JPanel buttonPanel = new JPanel(new GridLayout(2, 5));
            returnButton = new JButton("<<");
            returnButton.addActionListener(new ButtonHandler());
            buttonPanel.add(returnButton);
            buttonPanel.add(new JLabel(""));
            buttonPanel.add(new JLabel(""));
            buttonPanel.add(new JLabel(""));
            buttonPanel.add(new JLabel(""));
            panel.add(buttonPanel);
            for (Ingredient i: ingredients) {
                JPanel subpanel = new JPanel(new GridLayout(0, 3));
                
                JLabel quantity = new JLabel(i.getQuantity() + " gram", SwingConstants.CENTER);
                quantity.setFont(new Font("TimesRoman", Font.PLAIN, 20));
                JLabel name = new JLabel(i.getName(), SwingConstants.CENTER);
                name.setFont(new Font("TimesRoman", Font.PLAIN, 20));
                
                subpanel.add(name);
                subpanel.add(quantity);
                subpanel.add(new JLabel(""));
                panel.add(subpanel);
            }
        }
        return panel;
    }
    
    /**
     * redraw removes the BL/BR CornerPanel
     * after that it updates them and sets them visible again
     * @param status 
     */
    private void redraw() {
        // get the new orders from the database
        orders = manager.getOrders();

        // alter the bottom left panel;
        bottomPanel.remove(BLCornerPanel);
        BLCornerPanel = new JPanel(new GridLayout(0, 2));
        BLCornerPanel.add(addUnacceptedOrders());
        BLCornerPanel.add(addAcceptedOrders());
        bottomPanel.add(BLCornerPanel, BorderLayout.WEST);
        
        // alter the bottom right panel   
        if (selectedOrder != 0) {    
            if (manager.getStatus(selectedOrder).equals("Placed") && changed) {
            } else {
                bottomPanel.remove(BRCornerPanel);
                BRCornerPanel = createBRPanel();
                changed = true;
            }
        } else {
            bottomPanel.remove(BRCornerPanel);
            BRCornerPanel = startingBRPanel();
        }
        bottomPanel.add(BRCornerPanel, BorderLayout.EAST);
        
        // revalidate and repaint the frame
        frame.revalidate();
        frame.repaint();
    }
    
    /**
     * addUnacceptedOrders makes a JPanel for each order
     * The panel is stored in an array with JPanels
     * The orderID is stored in an array with integer at the same index as the JPanel to which it belongs
     * The JPanels all have their own MouseListener to make them selectable
     * Once a order is selected the panels will the redrawn and the selected one will become red 
     * @return 
     */
    private JPanel addUnacceptedOrders() {
        // make a new JPanel which can hold up to 20 orders (10 * 2)
        // 5 pixels between every order
        JPanel panel = new JPanel(new GridLayout(10, 2, 5, 5));
        int x = 0;
        for (Order o : orders) {
            //Check every order if the status = 'placed'
            if (o.getStatus().equals("Placed")) {
                //Add the panel to the panelArray with the same index as the orderNr has in the orderNrArray
                //This way the ordernumber can easily be found if the panel is clicked on
                JPanel orderPanel = new JPanel(new BorderLayout());
                //Create the center panel
                JPanel centerPanel = new JPanel(new GridLayout(2, 1));

                centerPanel.setBackground(Color.white);
                centerPanel.add(createLabel("Tafelnummer: " + o.getTableNr(), fontSize, 2, Color.blue));
                centerPanel.add(createLabel("Bestellingnummer: " + o.getOrderNr(), fontSize, 2, Color.blue));

                orderPanel.add(centerPanel, BorderLayout.CENTER);
                orderPanel.setName(o.getOrderNr() + "");

                // determine if the order is selected and adjust the color
                if (o.getOrderNr() != selectedOrder) {
                    orderPanel.setBorder(BorderFactory.createLineBorder(Color.blue, 5));
                } else {
                    orderPanel.setBorder(BorderFactory.createLineBorder(Color.red, 5));
                }
                // add the mouselisteners to the panels
                orderPanel.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mousePressed(MouseEvent me) {
                        // orderNrArray[number] is the ordernumber from the panel which you clicked on
                        JPanel p = (JPanel) me.getComponent();
                        int orderNr = Integer.parseInt(p.getName());
                        if (selectedOrder != orderNr) {    
                            selectedOrder = orderNr;
                            changed = false;
                            selectedEmployeesLabel.setText("");
                            watchingIngredients = false;
                        }
                        redraw();
                    }
                });

                panel.add(orderPanel);
                x++;
            }
        }
        //Set the panel to have a titled border
        panel.setBorder(BorderFactory.createTitledBorder(null, "Geplaatst", TitledBorder.LEFT, TitledBorder.TOP, new Font("Sans serif", Font.PLAIN, 25)));
        return panel;
    }
    
    /**
     * addAcceptedOrders shows every accepted order in the bottom left panel
     * @return 
     */
    private JPanel addAcceptedOrders() {
        //The ordernumbers from the accepted orders are held in a different array named orderNrArray2
        //The panel from the accepted orders are held in a different array named panelArray2
        JPanel panel = new JPanel(new GridLayout(10, 2, 5, 5));
        int x = 0;
        for (Order o : orders) {
            if (o.getStatus().equals("Accepted")) {

                JPanel orderPanel = new JPanel(new BorderLayout());
                JPanel centerPanel = new JPanel(new GridLayout(2, 1));

                centerPanel.setBackground(Color.white);
                centerPanel.add(createLabel("Tafelnummer: " + o.getTableNr(), fontSize, 2, Color.blue));
                centerPanel.add(createLabel("Bestellingnummer: " + o.getOrderNr(), fontSize, 2, Color.blue));

                orderPanel.add(centerPanel, BorderLayout.CENTER);
                orderPanel.setName(o.getOrderNr() + "");
                // determine if the order is selected and adjust the color
                if (o.getOrderNr() != selectedOrder) {
                    orderPanel.setBorder(BorderFactory.createLineBorder(Color.blue, 5));
                } else {
                    orderPanel.setBorder(BorderFactory.createLineBorder(Color.red, 5));
                }
                // add the mouselisteners to the panels
                orderPanel.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mousePressed(MouseEvent me) {
                        JPanel p = (JPanel) me.getComponent();
                        int orderNr = Integer.parseInt(p.getName());
                        if (selectedOrder != orderNr) {    
                            selectedOrder = orderNr;
                            changed = false;
                            selectedEmployeesLabel.setText("");
                            watchingIngredients = false;
                            redraw();
                        }
                        redraw();
                    }
                });

                panel.add(orderPanel);
                x++;
            }
        }
        panel.setBorder(BorderFactory.createTitledBorder(null, "Geaccepteerd", TitledBorder.LEFT, TitledBorder.TOP, new Font("Sans serif", Font.PLAIN, 25)));
        return panel;
    }
    
    /**
     * shows the details of the selected order
     * @return 
     */
    private JPanel createBRSubpanel() {
        String status = manager.getStatus(selectedOrder);
        JPanel panel;
        ArrayList<Meal> meals = new ArrayList<>();
        mealNames = new ArrayList<>();
        nameCodes = new ArrayList<>();
        int numberOfMeals = meals.size();
        //Check whether the selected order has the status 'placed' or 'accepted'
        if (status.equals("Placed")) {
            if (numberOfMeals < minGridSize) {
                numberOfMeals = minGridSize;
            }
            panel = new JPanel(new GridLayout(numberOfMeals, 2));

            //Get all meals for the selected orders
            meals = manager.getMeals(selectedOrder);
            
            panel.setBorder(BorderFactory.createTitledBorder(null, "Selecteer medewerkers", TitledBorder.LEFT, TitledBorder.TOP, new Font("Sans serif", Font.PLAIN, 20)));
            //Fill the left center and right panel
            int index = 0;
            for (Meal m : meals) {
                JPanel mealPanel = new JPanel(new GridLayout(1, 3, 5, 5));
                JLabel name = new JLabel(m.getName(), SwingConstants.CENTER);
                name.setFont(new Font("TimesRoman", Font.PLAIN, 20));
                mealNames.add(name);
                mealPanel.add(mealNames.get(index));
                
                nameCodes.add(createComboBox());
                mealPanel.add(nameCodes.get(index));
                
                JButton button = new JButton("Toon ingrediënten");
                button.setName(m.getName());
                button.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mousePressed(MouseEvent me) {
                        watchingIngredients = true;
                        currentMeal = me.getComponent().getName();
                        changed = false;
                        redraw();
                    }
                });
                mealPanel.add(button);
                
                panel.add(mealPanel);
                index += 1;
            }
            
            panel.add(selectedEmployeesLabel);

            //Create the button and add a buttonhandler to it
            acceptButton = new JButton("Accepteer bestelling");
            acceptButton.addActionListener(new ButtonHandler());
            
            panel.add(acceptButton, BorderLayout.SOUTH);
            
            return panel;
        } else {
            if (numberOfMeals < minGridSize) {
                numberOfMeals = minGridSize;
            }
            panel = new JPanel(new GridLayout(numberOfMeals, 2, 2, 2));
            //Get all meals for the selected orders
            meals = manager.getMeals(selectedOrder);
            
            panel.setBorder(BorderFactory.createTitledBorder(null, "", TitledBorder.LEFT, TitledBorder.TOP, new Font("Sans serif", Font.PLAIN, 20)));
            //Fill the left center and right panel
            int index = 0;
            for (Meal m : meals) {
                JPanel mealPanel = new JPanel(new GridLayout(1, 3, 5, 5));
                JLabel name = new JLabel(m.getName(), SwingConstants.CENTER);
                name.setFont(new Font("TimesRoman", Font.PLAIN, 20));
                mealPanel.add(name);
                
                JLabel amount = new JLabel("" + m.getAmount(), SwingConstants.CENTER);
                amount.setFont(new Font("TimesRoman", Font.PLAIN, 20));
                mealPanel.add(amount);
                
                JButton button = new JButton("Toon ingrediënten");
                button.setName(m.getName());
                button.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mousePressed(MouseEvent me) {
                        watchingIngredients = true;
                        currentMeal = me.getComponent().getName();
                        changed = false;
                        redraw();
                    }
                });
                mealPanel.add(button);
                
                panel.add(mealPanel);
            }
            panel.add(new JLabel(""));
            
            readyButton = new JButton("Bestelling gereed");
            readyButton.addActionListener(new ButtonHandler());
            panel.add(readyButton, BorderLayout.SOUTH);
            
            return panel;
        }
    }
     /**
      * this panel is shown if there isn't a selected order
      * @return 
      */
    private JPanel startingBRPanel() {
        JPanel panel = new JPanel();
        JTextField field = new JTextField();
        field.setText("U kunt een bestelling selecteren door er op te klikken \n");
        field.setEditable(false);
        field.setBorder(BorderFactory.createLineBorder(Color.yellow, 0));
        panel.setBorder(BorderFactory.createTitledBorder(null, "Geen bestelling geselecteerd", TitledBorder.LEFT, TitledBorder.TOP, new Font("Sans serif", Font.PLAIN, 25)));
        panel.add(field);
     
        return panel;
    }
    
    private JComboBox createComboBox() {
        ArrayList<Employee> employee = manager.getLoggedInEmployees();
        ArrayList<String> employeeCodes = new ArrayList<>();
        for (Employee e: employee) {
            employeeCodes.add(e.getNameCode());
        }
        
        String[] employees = new String[employee.size() + 1];
        int x = 1;
        employees[0] = "Selecteer medewerker";
        
        for (String code: employeeCodes) {
            employees[x] = code;
            x += 1;
        }
        JComboBox nameCodeBox = new JComboBox(employees);
        nameCodeBox.setFont(new Font("TimesRoman", Font.PLAIN, 17));
        return nameCodeBox;
    }
    
    private void redrawLoggedInEmployees() {
        logOutEmployee.removeAllItems();
        logOutEmployee.addItem("Selecteer medewerker");
        for (Employee e: manager.getLoggedInEmployees()) {
            logOutEmployee.addItem(e.getNameCode());
        }
    }
    
    /**
     * the timerhandler redraws the screen every x seconds
     */
    class TimerHandler implements ActionListener {

        Timer t = new Timer(1000, this);
        int passedSeconds = 0;

        public TimerHandler() {
            t.start();
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            passedSeconds += 1;
            if (passedSeconds % 5 == 0 && !"Placed".equals(manager.getStatus(selectedOrder))) {
                redraw();
            }
            if (passedSeconds % 20 == 0) {
                redrawLoggedInEmployees();
            }
        }
    }
    
    /**
     * The buttonhandler gives the buttons their functionalities
     */
    class ButtonHandler implements ActionListener {
        JPanel subPanel;
        String mealName, employeeName;
        Meal meal;
        @Override
        public void actionPerformed(ActionEvent e) {
            if (e.getSource() == acceptButton) {
                for (JLabel j: mealNames) {
                    boolean employeesSelected = true;
                    for (JComboBox box: nameCodes) {
                        if (box.getSelectedIndex() == 0) {
                            employeesSelected = false;
                        }
                    }
                    
                    if (employeesSelected) {
                        int index = 0;
                        for (JComboBox box: nameCodes) {
                            String nameCode = (String) nameCodes.get(index).getSelectedItem();
                            String nameOfMeal = mealNames.get(index).getText();
                            manager.insertMealEmpoyee(selectedOrder, nameOfMeal, nameCode);
                            manager.setStatus(selectedOrder, "Accepted");
                            index += 1;
                        }
                        redraw();
                    } else {
                        selectedEmployeesLabel.setFont(new Font("TimesRoman", Font.PLAIN, 20));
                        selectedEmployeesLabel.setText("Selecteer medewerkers voordat je de bestelling accepteerd");
                    }
                }
            } else if (e.getSource() == readyButton) {
                manager.setStatus(selectedOrder, "Ready");
                selectedOrder = 0;
                redraw();
            } else if (e.getSource() == returnButton) {
                watchingIngredients = false;
                changed = false;
                redraw();
            } else if (e.getSource() == logOutButton) {
                String nameCode = logOutEmployee.getSelectedItem().toString();
                System.out.println(nameCode);
                manager.logUserOut(nameCode);
                redrawLoggedInEmployees();
            }
        }
    }
}