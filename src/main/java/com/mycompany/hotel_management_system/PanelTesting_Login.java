package com.mycompany.hotel_management_system;

import java.awt.Color;
import java.awt.Image;
import javax.swing.ImageIcon;
import javax.swing.JLabel;

/**
 *
 * @author Daniel
 */
public class PanelTesting_Login extends javax.swing.JPanel {

    public PanelTesting_Login(String text) {
        initComponents();
        JLabel label = new JLabel();
        label.setSize(635, 348);

        //Scaling the image size relative to the size of the panel
        ImageIcon icon = new ImageIcon(getClass().getResource(text));
        Image img = icon.getImage();
        Image imgScale = img.getScaledInstance(635, 348, Image.SCALE_SMOOTH);
        label.setIcon(new ImageIcon(imgScale));
        
        this.add(label);
    }
    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        setBackground(new java.awt.Color(0, 102, 204));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 635, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 348, Short.MAX_VALUE)
        );
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
}