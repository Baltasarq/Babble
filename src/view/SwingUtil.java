// Babble (c) 2014/24 Baltasar MIT License <baltasarq@gmail.com>


package view;


import core.AppInfo;
import org.w3c.dom.Node;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;

public class SwingUtil {
    public static final String TitleSave = "Save";
    public static final String TitleLoad = "Load";

    public static void fillList(JList<String> lst, final JButton bt, String[] vStr) {
        // Fill with items
        DefaultListModel<String> modelList = new DefaultListModel<>();
        modelList.removeAllElements();

        for(final String item : vStr ) {
            modelList.addElement( item );
        }

        // Prepare the list
        lst.setModel( modelList );
        lst.setSelectedIndex( 0 );

        // Add support for double-click
        MouseListener mouseListener = new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    bt.doClick();
                }
            }
        };

        lst.addMouseListener( mouseListener );
    }

    /**
     * Asks a question to the user
     * @param window the window this dialog depends on
     * @param msg the question to ask, as a string
     * @return True if the user clicked yes, false otherwise
     */
    public static boolean askIf(JFrame window, String msg) {
        int res = JOptionPane.showConfirmDialog(
                window, msg, AppInfo.NAME,
                JOptionPane.YES_NO_OPTION
        );

        return ( res == JOptionPane.YES_OPTION );
    }
    /// Puts an error message on top of the main window
    /// @param msg The message to display
    public static void msgError(JFrame window, String msg)
    {
        JOptionPane.showMessageDialog( window, msg, AppInfo.NAME + " - Error",
                JOptionPane.ERROR_MESSAGE
        );
    }

    /// Puts an info message on top of the main window
    /// @param msg The message to display
    public static void msgInfo(JFrame window, String msg)
    {
        JOptionPane.showMessageDialog( window, msg, AppInfo.NAME + " - Info",
                JOptionPane.INFORMATION_MESSAGE
        );
    }

    /** Runs a save file dialog for a file with an extension.
     * @param window the owner of the fialog
     * @param ext the extension of the file
     * @param extExpl the explanation for that extension
     * @return the file chosen.
     */
    public static File saveFileDlg(JFrame window, String ext, String extExpl) {
        FileNameExtensionFilter[] filters = {
                new FileNameExtensionFilter( extExpl, ext )
        };

        return saveFileDlg( window, filters );
    }

    /** Runs a save dialog for a file of various possible extensions.
     * @param window the owner of the dialog
     * @param ext a vector of extensions
     * @param extExpl a vector of explanations, parallel to ext.
     * @return the file chosen.
     */
    public static File saveFileDlg(JFrame window, String[] ext, String[] extExpl) {
        FileNameExtensionFilter[] filters =
                new FileNameExtensionFilter[ ext.length ]
                ;

        for(int i = 0; i < ext.length; ++i) {
            filters[ i ] = new FileNameExtensionFilter( extExpl[ i ], ext[ i ] );
        }

        return saveFileDlg( window, filters );
    }

    /** Runs a save dialog for a file of various possible extensions.
     * @param window the owner of the dialog
     * @param filters the file extensions and its explanations.
     * @return the file chosen.
     */
    public static File saveFileDlg(JFrame window, FileNameExtensionFilter[] filters) {
        // Create save dialog
        JFileChooser fileSelector = new JFileChooser();
        fileSelector.setDialogType( JFileChooser.SAVE_DIALOG );
        fileSelector.setDialogTitle( TitleSave );

        // Add filters
        for( FileNameExtensionFilter filter : filters) {
            fileSelector.addChoosableFileFilter( filter );
        }
        fileSelector.setFileFilter( filters[ 0 ] );

        // Locate the last directory, if any
        if ( getLastFile() != null ) {
            fileSelector.setSelectedFile( getLastFile());
        }

        // run
        if ( fileSelector.showSaveDialog( window ) == JFileChooser.APPROVE_OPTION ) {
            setLastFile(fileSelector.getSelectedFile());
        }

        return getLastFile();
    }

    /** Runs an open file dialog for files of a single extension.
     * @param window the owner of this sialog
     * @param ext the extension
     * @param extExpl the explanation for that extension.
     * @return the chosen file.
     */
    public static File openFileDlg(JFrame window, String ext, String extExpl) {
        FileNameExtensionFilter[] filters = {
                new FileNameExtensionFilter( extExpl, ext )
        };

        return openFileDlg( window, filters );
    }

    /** Runs an open file dialog for files of various extensions.
     * @param window the owner of the dialog
     * @param ext a vector of extensions
     * @param extExpl a parallel vector of explanations for ext
     * @return the chosen file.
     */
    public static File openFileDlg(JFrame window, String[] ext, String[] extExpl) {
        FileNameExtensionFilter[] filters =
                new FileNameExtensionFilter[ ext.length ];

        for(int i = 0; i < ext.length; ++i) {
            filters[ i ] = new FileNameExtensionFilter( extExpl[ i ], ext[ i ] );
        }

        return openFileDlg( window, filters );
    }

    /** Runs an open file dialog for files of various extensions.
     * @param window the owner of the dialog
     * @param filters the set of filters for extensions.
     * @return the chosen file.
     */
    public static File openFileDlg(JFrame window, FileNameExtensionFilter[] filters) {
        // Create open dialog
        JFileChooser fileSelector = new JFileChooser();
        fileSelector.setDialogType( JFileChooser.OPEN_DIALOG );
        fileSelector.setDialogTitle( TitleLoad );

        // Add filters
        for( FileNameExtensionFilter filter : filters) {
            fileSelector.addChoosableFileFilter( filter );
        }
        fileSelector.setFileFilter( filters[ 0 ] );

        // Locate the last directory, if any
        if ( getLastFile() != null ) {
            if ( getLastFile().isDirectory() )
                fileSelector.setCurrentDirectory( getLastFile());
            else    fileSelector.setSelectedFile( getLastFile());
        }

        // run
        if ( fileSelector.showOpenDialog( window ) == JFileChooser.APPROVE_OPTION ) {
            setLastFile(fileSelector.getSelectedFile());
        }

        return getLastFile();
    }

    /** Runs a dialog to select a directory.
     * @param window the owner of the dialog
     * @param fileSelector the dialog to run, now prepared.
     * @return the directory chosen.
     */
    private static File selectDirectory(JFrame window, JFileChooser fileSelector)
    {
        // Locate the last directory, if any
        if ( getLastFile() != null ) {
            if ( getLastFile().isDirectory() )
                fileSelector.setCurrentDirectory( getLastFile());
            else    fileSelector.setCurrentDirectory( getLastFile().getParentFile() );
        }

        // run
        fileSelector.setFileSelectionMode( JFileChooser.DIRECTORIES_ONLY );
        if ( fileSelector.showDialog( window, "Select directory" )
                == JFileChooser.APPROVE_OPTION )
        {
            setLastFile( fileSelector.getSelectedFile() );
        }

        return getLastFile();
    }

    /** Obtain the last file chosen in a dialog.
     * @return the lastFile
     */
    public static File getLastFile() {
        return lastFile;
    }

    /** Change the last file chosen in a dialog.
     * @param aLastFile the lastFile to set
     */
    public static void setLastFile(File aLastFile) {
        lastFile = aLastFile;
    }

    private static File lastFile = null;
}
