// Babble (c) 2014/24 Baltasar MIT License <baltasarq@gmail.com>


package core;


import core.I18n.Language;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;


/**
 * BibliographicData holds the info needed for the Babel Treaty.
 * @author baltasarq
 */
public class BibliographicData {
    public static final String ExtIfiction = ".ifiction";
    public static final String DefaultAuthor = "Maluva";
    public static final String DefaultTitle = "IF story";
    public static final String DefaultUrl = "http://caad.es/";
    public static final int DefaultRelease = 1;
    public static final Language DefaultLanguage = Language.ES;
    public static final Format DefaultFormat = Format.glulx;

    public enum Genre {
        children_Fiction, collegiate_fiction, comedy, erotica,
        fairy_Tale, fantasy, fiction, historical,
        horror, mistery, non_fiction, other,
        religious_fiction, romance, science_fiction, surreal,
        western
    };

    public static String[] GenresNamesEN = {
        "Children's Fiction", "Collegiate Fiction", "Comedy", "Erotica",
        "Fairy Tale", "Fantasy", "Fiction", "Historical",
        "Horror", "Mistery", "Non-fiction", "Other",
        "Religious Fiction", "Romance", "Science Fiction", "Surreal",
        "Western"
    };

    public static String[] GenresNamesES = {
        "Ficción para niños", "Ficción de universidad", "Comedia", "Erótica",
        "Cuento de hadas", "Fantasía", "Ficción", "Histórica",
        "Terror", "Misterio", "Costumbrista", "Otra",
        "Ficción religiosa", "Romance", "Ciencia-Ficción", "Surrealista",
        "Indios y vaqueros"
    };

    public enum Forgiveness {
        merciful, polite, tough, nasty, cruel
    };

    public static String[] ForgivenessNamesEN = {
            "Merciful", "Polite", "Tough", "Nasty", "Cruel"
    };

    public static String[] ForgivenessNamesES = {
            "Misericordiosa", "Educada", "Dura", "Apestosa", "Cruel"
    };

    public enum Format {
        glulx, zcode, tads2, tads3, hugo, alan, adrift,
        level9, agt, magscrolls, advsys, executable, html
    };

    public static String[] FormatsEN = {
            "Glulx", "Z Code", "Tads 2", "Tads 3", "Hugo", "Alan", "Adrift",
            "Level 9", "Agt", "Magnetic Scrolls", "AdvSys", "Executable", "Html"
    };

    public static String[] FormatsES = {
            "Glulx", "Z Code", "Tads 2", "Tads 3", "Hugo", "Alan", "Adrift",
            "Level 9", "Agt", "Magnetic Scrolls", "AdvSys", "Ejecutable", "Html"
    };

    public String getAuthor() {
        return author;
    }

    public String getIfID() {
        return ifID;
    }

    void setIfID(String ifID) {
        this.ifID = ifID.trim().toUpperCase();
    }

    public void setNewIfID() {
        ifID = BibliographicData.generateNewIfId();
    }

    public void setAuthor(String author) {
        this.author = author.trim();
    }

    public String getTitle() {
        return title;
    }

    public void setDate(ISODate date)
    {
        if ( date != null ) {
            this.date = date;
        }
    }

    public void setTitle(String title) {
        this.title = title.trim();
    }

    public String getSubtitle() {
        return subtitle;
    }

    public void setSubtitle(String subtitle) {
        this.subtitle = subtitle.trim();
    }

    public URL getUrl() {
        return url;
    }

    public void setUrl(URL url) {
        if ( url != null ) {
            this.url = url;
        }
    }

    public String getEMail() {
        return eMail;
    }

    public void setEMail(String eMail) {
        this.eMail = eMail.trim();
    }

    public int getRelease() {
        return release;
    }

    public void setRelease(int release) {
        this.release = release;
    }

    public ISODate getDate() {
        return date;
    }

    public void setDate(int year, int month, int day)
    {
        this.date = new ISODate( year, month, day );
    }

    public Genre getGenre() {
        return genre;
    }

    public void setGenre(Genre genre) {
        if ( genre != null ) {
            this.genre = genre;
        }
    }

    public I18n.Language getLanguage()
    {
        return language;
    }

    public void setLanguage(I18n.Language language)
    {
        this.language = language;
    }

    public Forgiveness getForgiveness()
    {
        return forgiveness;
    }

    public void setForgiveness(Forgiveness forgiveness)
    {
        if ( forgiveness != null ) {
            this.forgiveness = forgiveness;
        }
    }

    public Format getFormat()
    {
        return format;
    }

    public void setFormat(Format format)
    {
        if ( format != null ) {
            this.format = format;
        }
    }

    public String getDesc()
    {
        return desc;
    }

    public void setDesc(String desc)
    {
        this.desc = desc.trim();
    }

    public static String[] getGenresNames(I18n.Language language)
    {
        if ( language == I18n.Language.ES )
                return GenresNamesES;
        else    return GenresNamesEN;
    }

    public static Genre getGenreByName(String name, I18n.Language language)
    {
        String[] strGenres = getGenresNames( language );
        Genre toret = null;
        name = name.trim();
        int pos = Util.getPosInStringVectorOf( strGenres, name );

        if ( pos >= 0 ) {
            toret = Genre.values()[ pos ];
        }

        return toret;
    }

    public static String[] getForgivenessNames(I18n.Language language)
    {
        if ( language == I18n.Language.ES )
                return ForgivenessNamesES;
        else    return ForgivenessNamesEN;
    }

    public static Forgiveness getForgivenessByName(String name, I18n.Language language)
    {
        String[] strForgiveness = getForgivenessNames( language );
        Forgiveness toret = null;
        name = name.trim();
        int pos = Util.getPosInStringVectorOf( strForgiveness, name );

        if ( pos >= 0 ) {
            toret = Forgiveness.values()[ pos ];
        }

        return toret;
    }

    public static String[] getFormatNames(Language language)
    {
        String[] toret = FormatsES;

        if ( language == I18n.Language.EN ) {
            toret = FormatsEN;
        }

        return toret;
    }

    public static Format getFormatByName(String name, I18n.Language language)
    {
        String[] strFormats = getFormatNames( language );
        Format toret = null;
        name = name.trim();
        int pos = Util.getPosInStringVectorOf( strFormats, name );

        if ( pos >= 0 ) {
            toret = Format.values()[ pos ];
        }

        return toret;
    }

    /// Converts a file's extension to IFiction file
    /// @return the new file name as a string
    public static String getIFictionFileAsString(File file)
    {
        return Util.getCompletePathWithoutExtension( file ) + ExtIfiction;
    }

    /// Converts a file's extension to IFiction file
    /// @see BibliographicData::getIFictionFileAsString
    /// @return the new file as a File object
    public static File getIFictionFile(File file)
    {
        return new File( getIFictionFileAsString( file ) );
    }

    private void init()
    {
        try {
            author = DefaultAuthor;
            title = DefaultTitle;
            release = DefaultRelease;
            date = new ISODate();
            language = DefaultLanguage;
            format = DefaultFormat;
            ifID = BibliographicData.generateNewIfId();

            try {
                url = new URI( DefaultUrl ).toURL();
            } catch(URISyntaxException e) {
                System.err.println( e.getMessage() );
            }
        } catch (MalformedURLException ex) {
            Logger.getLogger( BibliographicData.class.getName() ).log( Level.SEVERE, null, ex );
        }
    }
    public BibliographicData()
    {
        this.init();
    }

    public BibliographicData(String name)
    {
        this.init();
        title = name;
    }

    public static String generateNewIfId()
    {
        return UUID.randomUUID().toString().toUpperCase();
    }

    private String author;
    private String ifID;
    private String title;
    private String subtitle = "";
    private URL url;
    private String eMail = "";
    private int release;
    private ISODate date;
    private Genre genre = Genre.fiction;
    private I18n.Language language;
    private Forgiveness forgiveness = Forgiveness.polite;
    private Format format = Format.glulx;
    private String desc = "";
}
