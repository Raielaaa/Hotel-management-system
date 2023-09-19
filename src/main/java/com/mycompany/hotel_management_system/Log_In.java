package com.mycompany.hotel_management_system;

import com.formdev.flatlaf.FlatDarkLaf;
import java.awt.Color;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.Timer;

/**
 *
 * @author Daniel
 */
public class Log_In extends javax.swing.JFrame {
    //variables for timer
    int second = 0;
    ArrayList<Integer> firstPic = new ArrayList<>(Arrays.asList(1, 6, 11, 16, 21, 26, 31, 36, 41, 46, 51));
    ArrayList<Integer> secondPic = new ArrayList<>(Arrays.asList(2, 7, 12, 17, 22, 27, 32, 37, 42, 47, 52));
    ArrayList<Integer> thirdPic = new ArrayList<>(Arrays.asList(3, 8, 13, 18, 23, 28, 33, 38, 43, 48, 53));
    ArrayList<Integer> fourthPic = new ArrayList<>(Arrays.asList(4, 9, 14, 19, 24, 29, 34, 39, 44, 49, 54));
    ArrayList<Integer> fifthPic = new ArrayList<>(Arrays.asList(5, 10, 15, 20, 25, 30, 35, 40, 45, 50, 55));
    Timer timer;
    
    //Variable for colored buttons
    ButtonDesign bd = new ButtonDesign();
    
    public Log_In() {
        initComponents();
        
        //Panel slide animation
        panelSlide_Login.init(new PanelTesting_Login("Images/R.jpg"), new PanelTesting_Login("Images/royal-red-bedroom-900x600.jpg"), new PanelTesting_Login("Images/1400963142614.jpeg"), new PanelTesting_Login("Images/IMG_4718-2.jpg"), new PanelTesting_Login("Images/bad70e9d2e77cce8a9a4e9784367d8af.jpg"));
        panelSlide_Login.setSpeed(15);
        
        //Background Image
        backgroundImage();
        
        //Taskbar image
        taskbarIcon();
        
        //Slideshow
        slideShow();
        timer.start();
    }
    //Method for slideshow
    private void slideShow() {
        timer = new Timer(3000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                second++;
                if (firstPic.contains(second)) {
                    button1.doClick();
                }
                if (secondPic.contains(second)) {
                    button2.doClick();
                }
                if (thirdPic.contains(second)) {
                    button3.doClick();
                }
                if (fourthPic.contains(second)) {
                    button4.doClick();
                }
                if (fifthPic.contains(second)) {
                    button5.doClick();
                }
            }
        });
    }
    //Taskbar Icon
    private void taskbarIcon() {
        ImageIcon icon2 = new ImageIcon(getClass().getResource("Images/hotel.png"));
        Image img = icon2.getImage();
        this.setIconImage(img);
    }
    //Background Image
    private void backgroundImage() {
        JLabel label = new JLabel();
        label.setSize(1169, 712);
        label.setIcon(new ImageIcon(getClass().getResource("Images/212849.jpg")));
        this.add(label);
    }
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        jPanel3 = new javax.swing.JPanel();
        jSeparator1 = new javax.swing.JSeparator();
        usernameTXT = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jSeparator2 = new javax.swing.JSeparator();
        passwordTXT = new javax.swing.JPasswordField();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        panelSlide_Login = new com.mycompany.hotel_management_system.PanelSlide_Login();
        button1 = new com.mycompany.hotel_management_system.ButtonDesign();
        button2 = new com.mycompany.hotel_management_system.ButtonDesign();
        button5 = new com.mycompany.hotel_management_system.ButtonDesign();
        button3 = new com.mycompany.hotel_management_system.ButtonDesign();
        button4 = new com.mycompany.hotel_management_system.ButtonDesign();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setUndecorated(true);

        jPanel1.setBackground(new java.awt.Color(51, 51, 51));
        jPanel1.setToolTipText("");
        jPanel1.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 15)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(218, 165, 32));
        jLabel1.setText("X");
        jLabel1.setToolTipText("Exit");
        jLabel1.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jLabel1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel1MouseClicked(evt);
            }
        });

        jLabel2.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(218, 165, 32));
        jLabel2.setText("-");
        jLabel2.setToolTipText("Minimize");
        jLabel2.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jLabel2.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel2MouseClicked(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel2)
                .addGap(18, 18, 18)
                .addComponent(jLabel1)
                .addGap(17, 17, 17))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel2.setBackground(new java.awt.Color(69, 1, 0));

        jPanel3.setBackground(new java.awt.Color(69, 1, 0));
        jPanel3.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(218, 165, 32), 3, true));

        usernameTXT.setBackground(new java.awt.Color(69, 1, 0));
        usernameTXT.setFont(new java.awt.Font("Times New Roman", 0, 14)); // NOI18N
        usernameTXT.setForeground(new java.awt.Color(218, 165, 32));
        usernameTXT.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        usernameTXT.setToolTipText("Enter username");
        usernameTXT.setBorder(null);

        jLabel4.setFont(new java.awt.Font("Times New Roman", 2, 14)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(218, 165, 32));
        jLabel4.setText("Username");

        jLabel5.setFont(new java.awt.Font("Times New Roman", 2, 14)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(218, 165, 32));
        jLabel5.setText("Password");

        passwordTXT.setBackground(new java.awt.Color(69, 1, 0));
        passwordTXT.setFont(new java.awt.Font("Times New Roman", 0, 14)); // NOI18N
        passwordTXT.setForeground(new java.awt.Color(218, 165, 32));
        passwordTXT.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        passwordTXT.setToolTipText("Enter password");
        passwordTXT.setBorder(null);

        jLabel6.setFont(new java.awt.Font("Times New Roman", 3, 14)); // NOI18N
        jLabel6.setForeground(new java.awt.Color(218, 165, 32));
        jLabel6.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel6.setText("Login  |");
        jLabel6.setToolTipText("Login into your account");
        jLabel6.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jLabel6.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel6MouseClicked(evt);
            }
        });

        jLabel7.setFont(new java.awt.Font("Times New Roman", 2, 14)); // NOI18N
        jLabel7.setForeground(new java.awt.Color(218, 165, 32));
        jLabel7.setText("Click Register if you don't have an account yet");

        jLabel8.setFont(new java.awt.Font("Times New Roman", 3, 14)); // NOI18N
        jLabel8.setForeground(new java.awt.Color(218, 165, 32));
        jLabel8.setText("Register");
        jLabel8.setToolTipText("Register new account");
        jLabel8.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jLabel8.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel8MouseClicked(evt);
            }
        });

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(63, 63, 63)
                .addComponent(usernameTXT, javax.swing.GroupLayout.PREFERRED_SIZE, 247, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel4)
                .addGap(158, 158, 158))
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jSeparator2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 305, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                            .addComponent(jLabel5)
                            .addGap(124, 124, 124)))
                    .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 305, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(passwordTXT, javax.swing.GroupLayout.PREFERRED_SIZE, 251, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(61, 61, 61))
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(56, 56, 56)
                .addComponent(jLabel7)
                .addContainerGap(60, Short.MAX_VALUE))
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(137, 137, 137)
                .addComponent(jLabel6)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 52, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(99, 99, 99)
                .addComponent(usernameTXT, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel4)
                .addGap(79, 79, 79)
                .addComponent(passwordTXT, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSeparator2, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel5)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 124, Short.MAX_VALUE)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel6)
                    .addComponent(jLabel8))
                .addGap(35, 35, 35)
                .addComponent(jLabel7)
                .addGap(40, 40, 40))
        );

        jLabel3.setFont(new java.awt.Font("Edwardian Script ITC", 1, 48)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(218, 165, 32));
        jLabel3.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel3.setText("Login");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(57, 57, 57)
                        .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(110, 110, 110)
                        .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 273, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(63, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addGap(16, 16, 16)
                .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, 89, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(34, 34, 34))
        );

        panelSlide_Login.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(102, 102, 102)));

        button1.setBackground(new java.awt.Color(255, 204, 204));
        button1.setRadius(500);
        button1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                button1ActionPerformed(evt);
            }
        });

        button2.setBackground(new java.awt.Color(255, 204, 204));
        button2.setRadius(500);
        button2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                button2ActionPerformed(evt);
            }
        });

        button5.setBackground(new java.awt.Color(255, 204, 204));
        button5.setRadius(500);
        button5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                button5ActionPerformed(evt);
            }
        });

        button3.setBackground(new java.awt.Color(255, 204, 204));
        button3.setRadius(500);
        button3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                button3ActionPerformed(evt);
            }
        });

        button4.setBackground(new java.awt.Color(255, 204, 204));
        button4.setRadius(500);
        button4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                button4ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout panelSlide_LoginLayout = new javax.swing.GroupLayout(panelSlide_Login);
        panelSlide_Login.setLayout(panelSlide_LoginLayout);
        panelSlide_LoginLayout.setHorizontalGroup(
            panelSlide_LoginLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelSlide_LoginLayout.createSequentialGroup()
                .addGap(220, 220, 220)
                .addComponent(button1, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(button2, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(button3, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(button4, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(button5, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(226, Short.MAX_VALUE))
        );
        panelSlide_LoginLayout.setVerticalGroup(
            panelSlide_LoginLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelSlide_LoginLayout.createSequentialGroup()
                .addContainerGap(306, Short.MAX_VALUE)
                .addGroup(panelSlide_LoginLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(button5, javax.swing.GroupLayout.PREFERRED_SIZE, 21, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(button4, javax.swing.GroupLayout.PREFERRED_SIZE, 21, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(button3, javax.swing.GroupLayout.PREFERRED_SIZE, 21, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(button2, javax.swing.GroupLayout.PREFERRED_SIZE, 21, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(button1, javax.swing.GroupLayout.PREFERRED_SIZE, 21, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(19, 19, 19))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(layout.createSequentialGroup()
                .addGap(18, 18, 18)
                .addComponent(panelSlide_Login, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(165, 165, 165)
                        .addComponent(panelSlide_Login, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents
    //Method for exit
    private void jLabel1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel1MouseClicked
        System.exit(0);
    }//GEN-LAST:event_jLabel1MouseClicked
    //Method for minimize
    private void jLabel2MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel2MouseClicked
        this.setState(JFrame.ICONIFIED);
    }//GEN-LAST:event_jLabel2MouseClicked
    //Method for register 
    private void jLabel8MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel8MouseClicked
        Register registerForm = new Register();
        registerForm.show();
        this.dispose();
    }//GEN-LAST:event_jLabel8MouseClicked
    //Method for login
    private void jLabel6MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel6MouseClicked
        if (usernameTXT.getText().equals("") || passwordTXT.getText().equals("")) {
            JOptionPane.showMessageDialog(null, "Please fill all the required details", "User Management System", JOptionPane.WARNING_MESSAGE);
        }
        else {
            
            try {
                Connection connection = connector.getConnection();
                PreparedStatement pst = connection.prepareCall("SELECT * FROM hotel_management_system_login WHERE username = ? AND password = ?");
                
                pst.setString(1, usernameTXT.getText());
                pst.setString(2, passwordTXT.getText());
                pst.executeQuery();
                ResultSet rs = pst.executeQuery();
                
                if (rs.next()) {
                    new Final_Frame().show();
                    JOptionPane.showMessageDialog(null, "Welcome", "Hotel Management System", JOptionPane.INFORMATION_MESSAGE);
                    this.dispose();
                }
                else {
                    JOptionPane.showMessageDialog(null, "Invalid username or password", "Hotel Management System", JOptionPane.WARNING_MESSAGE);
                    usernameTXT.setText("");
                    passwordTXT.setText("");
                    usernameTXT.grabFocus();
                }
                
            } catch(ClassNotFoundException | SQLException e) {
                JOptionPane.showMessageDialog(null, e);
            } catch (Exception ex) {
                Logger.getLogger(Log_In.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }//GEN-LAST:event_jLabel6MouseClicked

    private void button1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_button1ActionPerformed
        panelSlide_Login.show(0);
        button2.setColor(new Color(255,204,204));
        button3.setColor(new Color(255,204,204));
        button4.setColor(new Color(255,204,204));
        button5.setColor(new Color(255,204,204));
        button2.setBorderColor(new Color(255,204,204));
        button3.setBorderColor(new Color(255,204,204));
        button4.setBorderColor(new Color(255,204,204));
        button5.setBorderColor(new Color(255,204,204));
        
        button1.setColor(new Color(69,1,0));
        button1.setBorderColor(new Color(69,1,0));
    }//GEN-LAST:event_button1ActionPerformed

    private void button2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_button2ActionPerformed
        panelSlide_Login.show(1);
        button1.setColor(new Color(255,204,204));
        button3.setColor(new Color(255,204,204));
        button4.setColor(new Color(255,204,204));
        button5.setColor(new Color(255,204,204));
        button1.setBorderColor(new Color(255,204,204));
        button3.setBorderColor(new Color(255,204,204));
        button4.setBorderColor(new Color(255,204,204));
        button5.setBorderColor(new Color(255,204,204));
        
        button2.setColor(new Color(69,1,0));
        button2.setBorderColor(new Color(69,1,0));
    }//GEN-LAST:event_button2ActionPerformed

    private void button3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_button3ActionPerformed
        panelSlide_Login.show(2);
        button1.setColor(new Color(255,204,204));
        button2.setColor(new Color(255,204,204));
        button4.setColor(new Color(255,204,204));
        button5.setColor(new Color(255,204,204));
        button1.setBorderColor(new Color(255,204,204));
        button2.setBorderColor(new Color(255,204,204));
        button4.setBorderColor(new Color(255,204,204));
        button5.setBorderColor(new Color(255,204,204));
        
        button3.setColor(new Color(69,1,0));
        button3.setBorderColor(new Color(69,1,0));
    }//GEN-LAST:event_button3ActionPerformed

    private void button4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_button4ActionPerformed
        panelSlide_Login.show(3);
        button1.setColor(new Color(255,204,204));
        button2.setColor(new Color(255,204,204));
        button3.setColor(new Color(255,204,204));
        button5.setColor(new Color(255,204,204));
        button1.setBorderColor(new Color(255,204,204));
        button2.setBorderColor(new Color(255,204,204));
        button3.setBorderColor(new Color(255,204,204));
        button5.setBorderColor(new Color(255,204,204));
        
        button4.setColor(new Color(69,1,0));
        button4.setBorderColor(new Color(69,1,0));
    }//GEN-LAST:event_button4ActionPerformed

    private void button5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_button5ActionPerformed
        panelSlide_Login.show(4);
        button1.setColor(new Color(255,204,204));
        button2.setColor(new Color(255,204,204));
        button3.setColor(new Color(255,204,204));
        button4.setColor(new Color(255,204,204));
        button1.setBorderColor(new Color(255,204,204));
        button2.setBorderColor(new Color(255,204,204));
        button3.setBorderColor(new Color(255,204,204));
        button4.setBorderColor(new Color(255,204,204));
        
        button5.setColor(new Color(69,1,0));
        button5.setBorderColor(new Color(69,1,0));
    }//GEN-LAST:event_button5ActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        
        FlatDarkLaf.setup();

        
        /* Create and display the form */
        java.awt.EventQueue.invokeLater(() -> {
            new Log_In().setVisible(true);
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private com.mycompany.hotel_management_system.ButtonDesign button1;
    private com.mycompany.hotel_management_system.ButtonDesign button2;
    private com.mycompany.hotel_management_system.ButtonDesign button3;
    private com.mycompany.hotel_management_system.ButtonDesign button4;
    private com.mycompany.hotel_management_system.ButtonDesign button5;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSeparator jSeparator2;
    private com.mycompany.hotel_management_system.PanelSlide_Login panelSlide_Login;
    private javax.swing.JPasswordField passwordTXT;
    private javax.swing.JTextField usernameTXT;
    // End of variables declaration//GEN-END:variables
}