package com.mycompany.hotel_management_system;

import java.awt.Color;
import java.awt.Image;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

public class Loading_screen extends javax.swing.JFrame {

    public Loading_screen() {
        initComponents();
        
        //Background image
        backgroundImage();
        
        //mainPanel's background color
        mainPanel.setBackground(new Color(0, 0, 0, 200));
        
        //Taskbar Icon
        taskbarIcon();
    }
    //Taskbar Icon
    private void taskbarIcon() {
        ImageIcon icon2 = new ImageIcon(getClass().getResource("Images/hotel.png"));
        Image img = icon2.getImage();
        this.setIconImage(img);
    }
    //Setting backgroundImage
    private void backgroundImage() {
        ImageIcon img = new ImageIcon(getClass().getResource("Images/212849.jpg"));
        JLabel label = new JLabel();
        label.setSize(this.getWidth(), this.getHeight());
        label.setIcon(img);
        this.add(label);
    }
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        progressBar = new javax.swing.JProgressBar();
        message = new javax.swing.JLabel();
        percent = new javax.swing.JLabel();
        mainPanel = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jSeparator1 = new javax.swing.JSeparator();
        jLabel2 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setUndecorated(true);

        message.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        message.setText("Loading...");

        percent.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        percent.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        percent.setText("0%");

        mainPanel.setBackground(new java.awt.Color(0, 0, 0));

        jLabel1.setFont(new java.awt.Font("Edwardian Script ITC", 1, 60)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(218, 165, 32));
        jLabel1.setText("Hotel Management System");

        jSeparator1.setBackground(new java.awt.Color(218, 165, 32));
        jSeparator1.setForeground(new java.awt.Color(218, 165, 32));

        jLabel2.setFont(new java.awt.Font("Baskerville Old Face", 2, 18)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(218, 165, 32));
        jLabel2.setText("Created by : Ralph Daniel Honra");

        javax.swing.GroupLayout mainPanelLayout = new javax.swing.GroupLayout(mainPanel);
        mainPanel.setLayout(mainPanelLayout);
        mainPanelLayout.setHorizontalGroup(
            mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, mainPanelLayout.createSequentialGroup()
                .addContainerGap(123, Short.MAX_VALUE)
                .addComponent(jLabel1)
                .addGap(123, 123, 123))
            .addGroup(mainPanelLayout.createSequentialGroup()
                .addGroup(mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(mainPanelLayout.createSequentialGroup()
                        .addGap(41, 41, 41)
                        .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 699, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(mainPanelLayout.createSequentialGroup()
                        .addGap(257, 257, 257)
                        .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 254, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        mainPanelLayout.setVerticalGroup(
            mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(mainPanelLayout.createSequentialGroup()
                .addGap(105, 105, 105)
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel2)
                .addContainerGap(139, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(progressBar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(layout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addComponent(message, javax.swing.GroupLayout.PREFERRED_SIZE, 257, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(percent, javax.swing.GroupLayout.PREFERRED_SIZE, 77, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(28, 28, 28))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(167, Short.MAX_VALUE)
                .addComponent(mainPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(167, 167, 167))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(105, Short.MAX_VALUE)
                .addComponent(mainPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(89, 89, 89)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(message)
                    .addComponent(percent))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(progressBar, javax.swing.GroupLayout.PREFERRED_SIZE, 13, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    public static void main(String args[]) {
        //Theme
//        com.formdev.flatlaf.FlatDarculaLaf.setup();
        com.formdev.flatlaf.FlatDarkLaf.setup();
        
        Loading_screen ls = new Loading_screen();
        ls.setVisible(true);
        
        try {
            for (int i = 0; i <= 100; i++) {
                Thread.sleep(100);
                ls.percent.setText(i + "%");
                
                switch (i) {
                    case 10:
                        ls.message.setText("Turning on modules...");
                        break;
                    case 20:
                        ls.message.setText("Loading modules...");
                        break;
                    case 50:
                        ls.message.setText("Connecting to database...");
                        break;
                    case 70:
                        ls.message.setText("Connection successful");
                        break;
                    case 80:
                        ls.message.setText("Launching application...");
                        break;
                }
                ls.progressBar.setValue(i);
            }
            ls.dispose();
            new Log_In().show();
            
        } catch(InterruptedException e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JPanel mainPanel;
    private javax.swing.JLabel message;
    private javax.swing.JLabel percent;
    private javax.swing.JProgressBar progressBar;
    // End of variables declaration//GEN-END:variables
}