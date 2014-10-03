package view;

import core.Util;

import javax.swing.*;

/**
 * Triggers the execution
 * Created by baltasarq on 2/10/14.
 */
public class Ppal {
    public static void main(String[] args)
    {
        JFrame mainWindow = null;

        try {
            UIManager.setLookAndFeel(
                    UIManager.getSystemLookAndFeelClassName()
            );
        } catch (Exception e) { }

        // Make sure we have nice window decorations.
        JFrame.setDefaultLookAndFeelDecorated( true );
        JDialog.setDefaultLookAndFeelDecorated( true );

        try {
            mainWindow = new BabbleView();
            mainWindow.setVisible( true );
        } catch(Exception e) {
            Util.msgError(mainWindow, e.getMessage());
        }
    }
}
