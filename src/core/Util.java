// Babble (c) 2014/24 Baltasar MIT License <baltasarq@gmail.com>


package core;


import org.w3c.dom.Node;
import java.io.File;


/**
 * Utils not belonging to anywhere else.
 * @author baltasarq
 */
public class Util {
    /// Returns the name of a file, without path and extension
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

    /// Retrieves a node, child of another node, by its name
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

    /** Converts a complete path in a complete path without extension
     * @param f the file to obtain the path without extension from.
     * @return the file name (without extension) as a string
     */
    public static String getCompletePathWithoutExtension(File f)
    {
        String fileName = f.getAbsolutePath();
        int pos = fileName.lastIndexOf( '.' );

        if ( pos > -1 ) {
            fileName = fileName.substring( 0, pos );
        }

        return fileName;
    }

    /// Find the position of a string in a vector of strings
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
}

