/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main.java.infosys.hartigehap.kitchen.PresentationLayer;

import main.java.infosys.hartigehap.kitchen.BusinessLogic.KitchenManager;
import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

/**
 *
 * @author 23IVP4A3
 */
public class LoginGUI {

    KitchenManager manager;
    JFrame frame;
    JPanel mainPanel;
    JLabel passwordIncorrect, usernameIncorrect;
    JPasswordField password;
    //JTextField password;
    JButton loginButton, newButton;
    JTextField username;

    public LoginGUI(KitchenManager man) {
        manager = man;
        createFrame();
        init();
        frame.revalidate();
        frame.repaint();
    }

    private void createFrame() {
        frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setTitle("Keuken");
        frame.pack();
        frame.setSize(700, 200);
        frame.setVisible(true);
        frame.setResizable(false);
    }

    private void init() {
        mainPanel = createMainPanel();
        frame.setContentPane(mainPanel);
        frame.repaint();
    }

    private JPanel createMainPanel() {
        JPanel panel = new JPanel(new BorderLayout());

        panel.add(createNorthPanel(), BorderLayout.NORTH);
        panel.add(createCenterPanel(), BorderLayout.CENTER);

        return panel;
    }

    private JPanel createNorthPanel() {
        JPanel panel = new JPanel(new GridLayout(1, 3));
        panel.add(new JLabel("image", SwingConstants.CENTER));
        panel.add(new JLabel("De Hartige Hap", SwingConstants.CENTER));
        panel.add(new JLabel(""));
        return panel;
    }

    private JPanel createCenterPanel() {
        JPanel panel = new JPanel(new GridLayout(4, 3, 10, 10));
        usernameIncorrect = new JLabel("");
        passwordIncorrect = new JLabel("");
        loginButton = new JButton("Log in");
        loginButton.setSize(10,10);
        loginButton.addActionListener(new KnopHandler());
        password = new JPasswordField(10);
        //password.setEchoChar('*');
        username = new JTextField(10);

        panel.add(new JLabel(""));
        panel.add(new JLabel("Sign in"));
        panel.add(new JLabel(""));

        panel.add(new JLabel("Username: ", SwingConstants.RIGHT));
        panel.add(username);
        
        panel.add(usernameIncorrect);

        panel.add(new JLabel("Password: ", SwingConstants.RIGHT));
        panel.add(password);
        panel.add(passwordIncorrect);

        panel.add(new JLabel(""));
        panel.add(loginButton);
        
        panel.add(new JLabel(""));
        
        //Check if field in manager is set to create credentials
        if(manager.getCreateUserCredentials()){
            newButton = new JButton("createUserCredentials");
            newButton.setSize(10,10);
            newButton.addActionListener(new KnopHandler());

            panel.add(newButton);
        }

        return panel;
    }

    class KnopHandler implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            if (e.getSource() == loginButton) {
                String typedEmail = username.getText();
                char[] typedPassword = password.getPassword();
                if(manager.logUserIn(typedEmail, typedPassword)){
                    System.out.println("User ingelogd:" + typedEmail);
                }
                else{
                    //notlogeddin
                }
                /*
                if (manager.userExists(typedEmail)) {
                    if(manager.checkPassword(typedPassword, typedEmail)){
                        
                    }
                } else {
                    usernameIncorrect.setText("username incorrect");
                }
                        */
            }else if (e.getSource() == newButton) {
                manager.createLoginCredentials(password.getPassword(), username.getText());
                /*
                String typedEmail = username.getText();
                char[] typedPassword = password.getPassword();
                if(manager.logUserIn(typedEmail, typedPassword)){
                    //Loggedin
                }
                else{
                    //notlogeddin
                }
                /*
                if (manager.userExists(typedEmail)) {
                    if(manager.checkPassword(typedPassword, typedEmail)){
                        
                    }
                } else {
                    usernameIncorrect.setText("username incorrect");
                }
                        */
            }
        }
    }
}
