// Babble (c) 2014/24 Baltasar MIT License <baltasarq@gmail.com>


package core;


import java.io.File;
import java.io.IOException;

/**
 * The project file, relating a file and its biblio data.
 * @author baltasarq
 */
public class Project {
    public static final String IFictionExt = "ifiction";
    public static final String IFictionExtExpl = "bibliographic data for interactive fiction";

    public Project(String title)
    {
        biblio = new BibliographicData( title );
        ifFile = null;
    }

    public Project(File f) throws IOException {
        ifFile = f;
        String name;

        // Create initial title using name of the file
        name = f.getName();
        int posPunto = name.indexOf( '.' );

        if ( posPunto > -1 ) {
            name = name.substring( 0, posPunto );
        }

        // Create bibliographic information
        this.biblio = BiblioGraphicDataXML.loadFrom( ifFile );
    }

    public void setIfFile(File ifFile) {
        this.ifFile = ifFile;
    }

    public void syncFileName()
    {
        if ( ifFile == null ) {
            String name = getBiblio().getTitle().trim().replace(  " ", "" );
            ifFile = new File( name.toLowerCase() + '.' + IFictionExt );
        }
    }

    public BibliographicData getBiblio() {
        return biblio;
    }

    /**
     * Returns the ifiction file for the current project
     * @return the name of the project as a String
     */
    public String getName() {
        return biblio.getTitle();
    }

    /**
     * Returns the ifiction file for the current project
     * @return the ifFile as a File object
     * @see File
     */
    public File getIfFile() {
        return ifFile;
    }

    private File ifFile;
    private final BibliographicData biblio;
}
