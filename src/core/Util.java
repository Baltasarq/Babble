package core;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

import org.w3c.dom.Node;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.util.ArrayList;

/**
 *
 * @author baltasarq
 */
public class Util {
    public static final String TitleSave = "Save";
    public static final String TitleLoad = "Load";

    public static void fillList(JList lst, final JButton bt, String[] vStr) {
        // Fill with items
        DefaultListModel modelList = new DefaultListModel();
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
        
        lst.addMouseListener(  mouseListener );
    }

    /**
     * Asks a question to the user
     * @param window the window this dialog depends on
     * @param msg the question to ask, as a string
     * @return True if the user clicked yes, false otherwise
     */
    public static boolean askIf(JFrame window, String msg) {
        int res = JOptionPane.showConfirmDialog(
                                       window, msg, AppInfo.name,
                                       JOptionPane.YES_NO_OPTION
        );

        return ( res == JOptionPane.YES_OPTION );
    }
    /// @brief Puts an error message on top of the main window
    /// @param msg The message to display
    public static void msgError(JFrame window, String msg)
    {
        JOptionPane.showMessageDialog( window, msg, AppInfo.name + " - Error",
                                       JOptionPane.ERROR_MESSAGE
        );
    }
    
    /// @brief Puts an info message on top of the main window
    /// @param msg The message to display
    public static void msgInfo(JFrame window, String msg)
    {
        JOptionPane.showMessageDialog( window, msg, AppInfo.name + " - Info",
                                       JOptionPane.INFORMATION_MESSAGE
        );
    }

    /// @brief returns the name of a file, without path and extension
    /// @param file The file of which to obtain its name
    /// @return An string with that name
    public static String getFileNameWithoutExtension(File file) {
        String toret = file.getName();
        int pos = toret.lastIndexOf( '.' );
        
        if ( pos > -1 ) {
            toret = toret.substring( 0, pos );
        }
        
        return toret;
    }
    
    /// @brief retrieves a node, child of another node, by its name
    /// @param node The parent node of which the looked for node hangs
    /// @param name The name of the child node to look for
    /// @return The child node if found, null otherwise
    public static Node getXmlChildByName(Node node, String name)
    {
        Node toret = null;
        node = node.getFirstChild();
        
        while( node != null ) {
            if ( node.getNodeName().compareToIgnoreCase( name ) == 0 ) {
                toret = node;
                break;
            }
            
            node = node.getNextSibling();
        }   
        
        return toret;
    }
    
    public static File saveFileDlg(JFrame window, String ext, String extExpl) {
         FileNameExtensionFilter[] filters = {
            new FileNameExtensionFilter( extExpl, ext )
         };

         return saveFileDlg( window, filters );
        }
    
    public static File saveFileDlg(JFrame window, String[] ext, String[] extExpl) {
        FileNameExtensionFilter[] filters =
            new FileNameExtensionFilter[ ext.length ]
        ;
        
        for(int i = 0; i < ext.length; ++i) {
            filters[ i ] = new FileNameExtensionFilter( extExpl[ i ], ext[ i ] );
        }
        
        return saveFileDlg( window, filters );
    }
    
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

    public static File openFileDlg(JFrame window, String ext, String extExpl) {
        FileNameExtensionFilter[] filters = {
            new FileNameExtensionFilter( extExpl, ext )
        };
        
        return openFileDlg( window, filters );
    }
    
    public static File openFileDlg(JFrame window, String[] ext, String[] extExpl) {
        FileNameExtensionFilter[] filters =
            new FileNameExtensionFilter[ ext.length ]
        ;
        
        for(int i = 0; i < ext.length; ++i) {
            filters[ i ] = new FileNameExtensionFilter( extExpl[ i ], ext[ i ] );
        }
        
        return openFileDlg( window, filters );
    }
    
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
    
    public static File openDirDlg(JFrame window)
    {
        // Prepare dialog
        JFileChooser fileSelector = new JFileChooser();
        fileSelector.setDialogTitle( TitleLoad );
        fileSelector.setDialogType( JFileChooser.OPEN_DIALOG );
        
        return selectDirectory( window, fileSelector );
    }
    
    public static File saveDirDlg(JFrame window)
    {
        // Prepare dialog
        JFileChooser fileSelector = new JFileChooser();
        fileSelector.setDialogTitle( TitleSave );
        fileSelector.setDialogType( JFileChooser.SAVE_DIALOG );

        return selectDirectory( window, fileSelector );
    }    
    
    public static String[] cutCmd(String cmd) {
        String[] toret = new String[ 1 ];
        toret[ 0 ] = cmd;
        ArrayList<Integer> positions = new ArrayList<Integer>();
        boolean readingQuotes = false;
        
        // store parts' positions
        cmd = cmd.trim();
        for(int pos = 0; pos < cmd.length(); ++pos ) {
            if ( cmd.charAt( pos ) != ' '
              && cmd.charAt( pos ) != '"' )
            {
                continue;
            }

            char ch = cmd.charAt( pos );
            if ( ch == ' '
              && readingQuotes == false ) 
            {
                positions.add( pos );
            }
            else
            if ( ch == '"'
              && readingQuotes == true )
            {
                readingQuotes = false;
                positions.add( pos + 1 );
            }
            else
            if ( ch == '"'
              && readingQuotes == false )
            {
                readingQuotes = true;
                
                positions.add( pos );
            }
        }
        
        // create parts
        if ( positions.size() > 0 ) {
            int oldPos = 0;
            int i = 0;
            ArrayList<String> cmdParts = new ArrayList<String>();
            String part;
            
            // Jump first position (if needed)
            if ( positions.get( 0 ) == 0 ) {
                i = 1;
            }
            
            for(; i < positions.size(); ++i) {
                int newPos = positions.get( i );
                
                // Save us from empty lines
                if ( newPos == oldPos
                  || oldPos > newPos )
                {
                    continue;
                }
                
                // Add the new part
                part = cmd.substring( oldPos, newPos ).trim();
                if ( part.length() > 0 ) {
                    if ( part.charAt( 0 ) == '"' ) {
                        part = part.substring( 1, part.length() - 1 );
                    }
                    cmdParts.add( part );
                }
                oldPos = newPos;
            }
            
            // Add the last part
            part = cmd.substring( oldPos ).trim();
            if ( part.length() > 0 ) {
                    if ( part.charAt( 0 ) == '"' ) {
                        part = part.substring( 1, part.length() - 1 );
                    }
                    cmdParts.add( part );
            }
            
            // Trim the vector
            toret = cmdParts.toArray( toret );
        }
                
        return toret;
    }
    
    public static boolean execute(String[] cmd, StringBuilder output, boolean wait)
    {
        Boolean result = false;
        StringBuilder error = new StringBuilder();
        
        try {
          // Show the command to execute
          output.append( "Executing: " );
          for(int i = 0; i < cmd.length; ++i ) {
              output.append( cmd[ i ] + ' ' );
          }
          output.append( "\n" );
          
          // Execute the process
          Process p = Runtime.getRuntime().exec( cmd );
          if ( wait ) {
              result = ( waitFor( output, error, p ) == 0 );

              output.append( error.toString() );

              if ( result )
                        output.append( "\nExecution ... ok." );
              else      output.append( "\nExecution ... failed." );
          }
        }
        catch (Exception err) {
            output.append( "\nError:\n" + err.getMessage() );
        }
        
        return result;
    }    

    public static boolean execute(String line, StringBuilder output, boolean b) {
        return execute( cutCmd( line ), output, b );
    }
    
    public static int waitFor(StringBuilder output, StringBuilder error, Process p) {
        boolean isOver = false;
        int toret = -1;
        String line;
                    
        while( !isOver ) {
            try {
                while ( p.getInputStream().available() > 0 ) {
                  output.append( ( (char) p.getInputStream().read() ) );
                }

                while ( p.getErrorStream().available() > 0 ) {
                  error.append( ( (char) p.getErrorStream().read() ) );
                }
                
                toret = p.exitValue();
                p.getInputStream().close();
                p.getErrorStream().close();
                isOver = true;
            }
            catch(Exception e)
            {
                try {
                    Thread.sleep( 500 );
                } catch (InterruptedException ex) {
                }
            }
        }
        
        return toret;
    }    
    
    /// Converts a complete path in a complete path without extension
    /// @return the file name (without extension) as a string
    public static String getCompletePathWithouExtension(File f)
    {
        String fileName = f.getAbsolutePath();
        int pos = fileName.lastIndexOf( '.' );
        
        if ( pos > -1 ) {
            fileName = fileName.substring( 0, pos );
        }
        
        return fileName;
    }
    
    /// Extracts the extension (without dot) from a path, complete or not
    /// @return the extension as a string
    public static String getPathExt(File f)
    {
        String toret = f.getName();
        int pos = toret.lastIndexOf( '.' );
        
        if ( pos > -1 ) {
            toret = toret.substring( pos + 1 );
        }
        
        return toret;
    }

    /// Extracts the directory from a given path
    /// @return The directory as a File object
    public static File getCompletePathFolder(File f) {
        File toret = f.getAbsoluteFile();
        int posPathSeparator = f.getAbsolutePath().lastIndexOf( File.separator );
        
        if ( posPathSeparator > -1 ) {
            toret = new File (
                f.getAbsolutePath().substring( 0, posPathSeparator )
            );
        }
        
        return toret;
    }

    /// @brief Find the position of a string in a vector of strings
    /// @param v the vector in which to look for (all members should uppercase'd)
    /// @param name the string to look for in v
    /// @return the position of name if foundm -1 otherwise.
    public static int getPosInStringVectorOf(String[] v, String name) {
        String lookFor = name.trim();
        int toret = -1;
        
        for(int i = 0; i < v.length; ++i) {
            if ( lookFor.compareToIgnoreCase( v[ i ] ) == 0 ) {
                toret = i;
                break;
            }
        }
        
        return toret;
    }

    /**
     * @return the lastFile
     */
    public static File getLastFile() {
        return lastFile;
    }

    /**
     * @param aLastFile the lastFile to set
     */
    public static void setLastFile(File aLastFile) {
        lastFile = aLastFile;
    }
    
    /// @brief possible language codes as in ISO 639
    public static enum Language {
        ES,
        EN,
        IT,
        DE,
        FR,
        NL,
        SL
    };
    
    public static final String[] LanguageES = {
        "Español",
        "Inglés",
        "Italiano",
        "Alemán",
        "Francés",
        "Holandés",
        "Esloveno"
    };
    
    public static final String[] LanguageEN = {
        "Spanish",
        "English",
        "Italian",
        "German",
        "French",
        "Dutch",
        "Slovenian"
    };
    
    public static Language getLanguageByIso639Name(String str)
    {
        Language toret = Language.EN;
        str = str.trim().toUpperCase();
        
        for(Language v : Language.values() ) {
            if ( v.toString().compareToIgnoreCase( str ) == 0 ) {
                toret = v;
                break;
            }
        }
        
        return toret;
    }        
    
    public static String[] getStrLanguages(Language language)
    {
        if ( language == Language.ES )
                return LanguageES;
        else    return LanguageEN;
    }
    
    public static Language getLanguageByName(String str, Language language)
    {
        String[] strLanguages = getStrLanguages( language );
        str = str.trim();
        Language toret = null;
        int pos = getPosInStringVectorOf( strLanguages, str );
        
        if ( pos >= 0 ) {
            toret = Language.values()[ pos ];
        }

        return toret;
    }    
        
    public static enum Color {
        BLACK, BLUE, CYAN, DARK_GRAY, GRAY, GREEN,
        LIGHT_GRAY, MAGENTA, PINK, RED, WHITE, YELLOW
    };
    
    public static final java.awt.Color Colors[] = {
        java.awt.Color.BLACK, java.awt.Color.BLUE, java.awt.Color.CYAN,
        java.awt.Color.DARK_GRAY, java.awt.Color.GRAY, java.awt.Color.GREEN,
        java.awt.Color.LIGHT_GRAY, java.awt.Color.MAGENTA, java.awt.Color.PINK,
        java.awt.Color.RED, java.awt.Color.WHITE, java.awt.Color.YELLOW    
    };

    public static String[] ColorsEN = {
        "BLACK", "BLUE", "CYAN", "DARK GRAY", "GRAY", "GREEN",
        "LIGHT GRAY", "MAGENTA", "PINK", "RED", "WHITE", "YELLOW"
    };
    
    public static String[] ColorsES = {
        "NEGRO", "AZUL", "CYAN", "GRIS OSCURO", "GRIS", "VERDE",
        "GRIS CLARO", "MAGENTA", "ROSA", "ROJO", "BLANCO", "AMARILLO"
    };            
        
    public static String[] getColors(Language language)
    {
        if ( language == Language.ES )
                return ColorsES;
        else    return ColorsEN;
    }
       
    public static Color getColorByName(String name, Language language)
    {
        Color toret = null;
        String[] colorNames = getColors( language );
                
        int pos = getPosInStringVectorOf( colorNames, name );
        if ( pos > -1 ) {
            toret = Color.values()[ pos ];
        }
        
        return toret;
    }
    
    public static java.awt.Color getAwtColorFromColor(Color c)
    {
        return Colors[ c.ordinal() ];
    }
    
    public static void centerOnScreen(Component c)
    {
        Dimension dimC = c.getSize();
        Point p = new Point();
        
        // get local graphics environment
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
		
        // get maximum window bounds
        Rectangle max = ge.getMaximumWindowBounds();
        
        // Positionate on center
        p.x = ( max.width - dimC.width ) / 2;
        p.y = ( max.height - dimC.height ) / 2;
        c.setLocation( p );
    }
    
    public static void setLocationCenteredOverMe(Component host, Component c)
    {
        Point toret = new Point( c.getLocation() );
        Point locThis = host.getLocation();
        Dimension dimThis = host.getSize();
        Dimension dimThat = c.getSize();
        
        toret.x = locThis.x + ( ( dimThis.width - dimThat.width ) / 2 );
        toret.y = locThis.y + ( ( dimThis.height - dimThat.height ) / 2 );        
        
        c.setLocation( toret );
    }

    private static File lastFile = null;
}

