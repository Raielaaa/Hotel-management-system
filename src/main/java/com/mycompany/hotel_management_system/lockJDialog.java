package com.mycompany.hotel_management_system;

import java.awt.Color;
import javax.swing.JOptionPane;

/**
 *
 * @author Daniel
 */
public class lockJDialog extends javax.swing.JDialog {

    public lockJDialog(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
        
        warning.setForeground(Color.RED);
        this.getContentPane().setBackground(new Color(204, 204, 204));
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        warning = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        securityCode = new javax.swing.JPasswordField();
        button = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setUndecorated(true);

        warning.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        warning.setText("WARNING!");

        jLabel1.setForeground(new java.awt.Color(0, 0, 0));
        jLabel1.setText("Your free trial has ended. Please enter the security code to continue.");

        jLabel2.setForeground(new java.awt.Color(0, 0, 0));
        jLabel2.setText("Security code :");

        securityCode.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        securityCode.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                securityCodeActionPerformed(evt);
            }
        });

        button.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        button.setText("OK");
        button.setToolTipText("Submits the inputted code");
        button.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        button.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(21, 21, 21)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(button, javax.swing.GroupLayout.PREFERRED_SIZE, 86, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(warning, javax.swing.GroupLayout.PREFERRED_SIZE, 129, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGroup(layout.createSequentialGroup()
                            .addComponent(jLabel2)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(securityCode, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addComponent(jLabel1)))
                .addContainerGap(21, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(18, 18, 18)
                .addComponent(warning, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(29, 29, 29)
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(securityCode, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(button)
                .addContainerGap(16, Short.MAX_VALUE))
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void securityCodeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_securityCodeActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_securityCodeActionPerformed

    private void buttonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonActionPerformed
        if ("".equals(securityCode.getText())) {
            JOptionPane.showMessageDialog(this, "Please input a security code", "Hotel Management System", JOptionPane.OK_OPTION);
            securityCode.grabFocus();
        }
        else {
            if (!securityCode.getText().equals("020703")) {
                JOptionPane.showConfirmDialog(this, "Security code not found!", "Hotel Mananagement System", JOptionPane.WARNING_MESSAGE);
                securityCode.setText("");
                securityCode.grabFocus();
            }
            else {
                this.dispose();
            }
        }
    }//GEN-LAST:event_buttonActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        com.formdev.flatlaf.FlatDarkLaf.setup();

        /* Create and display the dialog */
        java.awt.EventQueue.invokeLater(() -> {
            lockJDialog dialog = new lockJDialog(new javax.swing.JFrame(), true);
            dialog.addWindowListener(new java.awt.event.WindowAdapter() {
                @Override
                public void windowClosing(java.awt.event.WindowEvent e) {
                    System.exit(0);
                }
            });
            dialog.setVisible(true);
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton button;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JPasswordField securityCode;
    private javax.swing.JLabel warning;
    // End of variables declaration//GEN-END:variables
}