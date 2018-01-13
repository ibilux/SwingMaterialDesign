package com.hq.swingmaterialdesign;

/**
 *
 * @author bilux (i.bilux@gmail.com)
 */
public class SwingMaterialDesign {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // Set the Metal look and feel
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Metal".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | javax.swing.UnsupportedLookAndFeelException ex) {
            //java.util.logging.Logger.getLogger(JDialog_AddProduct.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        // Create and display the form
        JFrame_Main jFrame_Main = new JFrame_Main();

        java.awt.EventQueue.invokeLater(() -> {
            jFrame_Main.setVisible(true);
        });
    }

}
