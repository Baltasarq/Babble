// Babble (c) 2014/24 Baltasar MIT License <baltasarq@gmail.com>


package view;


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
            System.setProperty( "swing.aatext", "true" );
            System.setProperty( "awt.useSystemAAFontSettings", "on" );
            UIManager.setLookAndFeel(
                    UIManager.getSystemLookAndFeelClassName()
            );
        } catch (Exception e) {
            System.err.println( "Error setting antialising" );
        }

        // Make sure we have nice window decorations.
        JFrame.setDefaultLookAndFeelDecorated( true );
        JDialog.setDefaultLookAndFeelDecorated( true );

        try {
            mainWindow = new BabbleView();
            mainWindow.setVisible( true );
        } catch(Exception e) {
            String msg = e.getLocalizedMessage();

            if ( msg == null
              || msg.isEmpty() )
            {
                msg = "unexpected app error";
            }

            System.err.println( msg );
            SwingUtil.msgError( mainWindow, msg );
        }
    }
}
