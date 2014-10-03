/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package core;

import com.sun.org.apache.xml.internal.serialize.OutputFormat;
import com.sun.org.apache.xml.internal.serialize.XMLSerializer;
import core.Util.Language;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.xml.sax.ContentHandler;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.AttributesImpl;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author baltasarq
 */
public class BibliographicData {
    public static final String ExtIfiction = ".ifiction";
    public static final String DefaultAuthor = "Maluva";
    public static final String DefaultTitle = "IF story";
    public static final String DefaultUrl = "http://caad.es/";
    public static final int DefaultRelease = 1;
    public static final Util.Language DefaultLanguage = Util.Language.ES;
    public static final Format DefaultFormat = Format.glulx;
    public static final String FmtDate = "yyyy/MM/dd";
    public static final String MsgPartMissing = " is missing.";
    public static final String MsgPartInvalidField = "Invalid field: ";
    
    public static final String EtqXmlCr = "br";
    public static final String EtqStory = "story";
    public static final String EtqIFictionVersion = "version";
    public static final String IFictionVersionValue = "1.0";
    public static final String EtqXmlns = "xmlns";
    public static final String XmlnsValue = "http://babel.ifarchive.org/protocol/iFiction/";
    public static final String EtqIfIndex = "ifindex";
    public static final String EtqBibliographic = "bibliographic";
    public static final String EtqContacts = "contacts";
    public static final String EtqEmail = "authoremail";
    public static final String EtqRelease = "release";
    public static final String EtqVersion = "version";
    public static final String EtqForgiveness = "forgiveness";
    public static final String EtqOriginated = "originated";
    public static final String EtqGenerator = "generator";
    public static final String EtqUrl = "url";
    public static final String EtqReleaseDate = "releasedate";
    public static final String EtqColophon = "colophon";
    public static final String EtqIdentification = "identification";
    public static final String EtqFormat = "format";
    public static final String EtqIfID = "ifid";
    public static final String EtqTitle = "title";
    public static final String EtqAuthor = "author";
    public static final String EtqLanguage = "language";
    public static final String EtqHeadline = "headline";
    public static final String EtqGenre = "genre";
    public static final String EtqDescription = "description";

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

    private void setIfID(String ifID) {
        this.ifID = ifID.trim().toUpperCase();
    }

    public void setNewIfID() {
        ifID = BibliographicData.generateNewIfId();
    }


    public String getDateAsString() {
        SimpleDateFormat fmt = new SimpleDateFormat( FmtDate );
        return fmt.format( date.getTime() );
    }

    public void setAuthor(String author) {
        this.author = author.trim();
    }

    public String getTitle() {
        return title;
    }

    public void setDate(Calendar calendar) {
        if ( calendar != null ) {
            this.date = calendar;
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

    public Calendar getDate() {
        return date;
    }

    public void setDate(int year, int month, int day) {
        this.date.set( year, month, day );
    }

    public Genre getGenre() {
        return genre;
    }

    public void setGenre(Genre genre) {
        if ( genre != null ) {
            this.genre = genre;
        }
    }

    public Util.Language getLanguage() {
        return language;
    }

    public void setLanguage(Util.Language language) {
        this.language = language;
    }

    public Forgiveness getForgiveness() {
        return forgiveness;
    }

    public void setForgiveness(Forgiveness forgiveness) {
        if ( forgiveness != null ) {
            this.forgiveness = forgiveness;
        }
    }

    public Format getFormat() {
        return format;
    }

    public void setFormat(Format format) {
        if ( format != null ) {
            this.format = format;
        }
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc.trim();
    }

    private void setDate(String strDate) throws ParseException {
        SimpleDateFormat fmt = new SimpleDateFormat( FmtDate );
        date.setTime( fmt.parse( strDate ) );
    }

    public static String[] getGenresNames(Util.Language language)
    {
        if ( language == Util.Language.ES )
                return GenresNamesES;
        else    return GenresNamesEN;
    }

    public static Genre getGenreByName(String name, Util.Language language)
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

    public static String[] getForgivenessNames(Util.Language language)
    {
        if ( language == Util.Language.ES )
                return ForgivenessNamesES;
        else    return ForgivenessNamesEN;
    }

    public static Forgiveness getForgivenessByName(String name, Util.Language language)
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

    public static String[] getFormatNames(Language language) {
        if ( language == Util.Language.ES )
                return FormatsES;
        else    return FormatsEN;
    }

    public static Format getFormatByName(String name, Util.Language language)
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
    public static String getIFictionFileAsString(File file) {
        return Util.getCompletePathWithouExtension( file ) + ExtIfiction;
    }

    /// Converts a file's extension to IFiction file
    /// @see getIFictionFileAsString
    /// @return the new file as a File object
    public static File getIFictionFile(File file) {
        return new File( getIFictionFileAsString( file ) );
    }

    private void init()
    {
        try {
            author = DefaultAuthor;
            title = DefaultTitle;
            release = DefaultRelease;
            date = Calendar.getInstance();
            language = DefaultLanguage;
            format = DefaultFormat;
            ifID = BibliographicData.generateNewIfId();
            url = new URL( DefaultUrl );
        } catch (MalformedURLException ex) {
            Logger.getLogger( BibliographicData.class.getName() ).log( Level.SEVERE, null, ex );
        }
    }
    public BibliographicData()
    {
        this.init();
    }

    public BibliographicData(String name) {
        this.init();
        title = name;
    }

    public void saveTo(File ifFile) throws IOException
    {
        FileOutputStream fos = null;
        DateFormat formatter = new SimpleDateFormat( FmtDate );

        try {
            fos = new FileOutputStream( ifFile );
            OutputFormat of = new OutputFormat( "XML", "UTF-8", true );
            of.setIndent( 4 );
            of.setIndenting( true );
            XMLSerializer serializer = new XMLSerializer( fos, of );
            ContentHandler hd = serializer.asContentHandler();
            hd.startDocument();

            // Ifiction meta id
            AttributesImpl atts = new AttributesImpl();
            atts.clear();
            atts.addAttribute( "", "", EtqIFictionVersion, "CDATA", IFictionVersionValue );
            atts.addAttribute( "", "", EtqXmlns, "CDATA", XmlnsValue);
            hd.startElement( "","", EtqIfIndex, atts );

            // Begin of IFiction file
            hd.startElement( "","", EtqStory, null );

            // Identification
            String strFormat = this.getFormat().toString();
            String strIfID = this.getIfID().toString();
            hd.startElement( "","", EtqIdentification, null );
            hd.startElement( "","", EtqIfID, null );
            hd.characters( strIfID.toCharArray(), 0, strIfID.length() );
            hd.endElement( "","", EtqIfID );
            hd.startElement( "","", EtqFormat, null );
            hd.characters( strFormat.toCharArray(), 0, strFormat.length() );
            hd.endElement( "","", EtqFormat );
            hd.endElement( "","", EtqIdentification );

            // Bibliographic section
            writeBibliographicSection( hd );

            // Contacts
            writeContactsSection( hd );

            // Release
            String strDate = formatter.format( this.getDate().getTime() );
            String strRelease = Integer.toString( this.getRelease() );
            hd.startElement( "","", EtqRelease, null );
            hd.startElement( "","", EtqVersion, null );
            hd.characters( strRelease.toCharArray(), 0, strRelease.length() );
            hd.endElement( "","", EtqVersion );
            hd.startElement( "","", EtqReleaseDate, null );
            hd.characters( strDate.toCharArray(), 0, strDate.length() );
            hd.endElement( "","", EtqReleaseDate );
            hd.endElement( "","", EtqRelease );

            // Colophon
            hd.startElement( "","", EtqColophon, null );
            hd.startElement( "","", EtqGenerator, null );
            hd.characters( AppInfo.name.toCharArray(), 0, AppInfo.name.length() );
            hd.endElement( "","", EtqGenerator );
            hd.startElement( "","", EtqOriginated, null );
            hd.characters( strDate.toCharArray(), 0, strDate.length() );
            hd.endElement( "","", EtqOriginated );
            hd.endElement( "","", EtqColophon );

            hd.endElement( "","", EtqStory );
            hd.endElement( "", "", EtqIfIndex );
            hd.endDocument();
        } catch (Exception ex) {
            throw new IOException( "impossibe to create IFiction file:\n"
                                + ex.getMessage()
            );
        } finally {
            try {
                fos.close();
            } catch (IOException ex) {
                throw new IOException( "impossibe to write IFiction file:\n"
                                + ex.getMessage()
                );
            }
        }
    }

    private void writeBibliographicSection(ContentHandler hd) throws SAXException {
        hd.startElement( "","", EtqBibliographic, null );
        String str;

        // Title
        hd.startElement( "","", EtqTitle, null );
        hd.characters( title.toCharArray(), 0, title.length() );
        hd.endElement(  "","", EtqTitle );

        // Author
        hd.startElement(  "","", EtqAuthor, null );
        hd.characters( author.toCharArray(), 0, author.length() );
        hd.endElement(  "","", EtqAuthor );

        // Language
        hd.startElement(  "","", EtqLanguage, null );
        str = language.toString();
        hd.characters( str.toCharArray(), 0, str.length() );
        hd.endElement(  "","", EtqLanguage );

        // headline
        if ( !subtitle.isEmpty() ) {
            hd.startElement(  "","", EtqHeadline, null );
            hd.characters( subtitle.toCharArray(), 0, subtitle.length() );
            hd.endElement(  "","", EtqHeadline );
        }

        // genre
        if ( genre != null ) {
            str = genre.toString();
            hd.startElement(  "","", EtqGenre, null );
            hd.characters( str.toCharArray(), 0, str.length() );
            hd.endElement(  "","", EtqGenre );
        }

        // forgiveness
        if ( forgiveness != null ) {
            str = forgiveness.toString();
            hd.startElement(  "","", EtqForgiveness, null );
            hd.characters( str.toCharArray(), 0, str.length() );
            hd.endElement(  "","", EtqForgiveness );
        }

        // description
        String[] descLines = desc.trim().split( "\n" );
        if ( str.length() > 0 ) {
            hd.startElement(  "","", EtqDescription, null );
            int numLine = 0;
            for(String descLine: descLines) {
                hd.characters( descLine.toCharArray(), 0, descLine.length() );
                
                if ( numLine < ( descLines.length -1 ) ) {
                    hd.startElement( "", "", "br", null );
                    hd.endElement( "", "", "br" );
                }
                ++numLine;
            }
            hd.endElement(  "","", EtqDescription );
        }

        hd.endElement( "","", EtqBibliographic );
    }

    private void writeContactsSection(ContentHandler hd) throws SAXException {
        String str;

        if ( url != null
          || !eMail.isEmpty() )
        {
            hd.startElement( "","", EtqContacts, null );

            if ( !eMail.isEmpty() ) {
                hd.startElement( "","", EtqEmail, null );
                hd.characters( eMail.toCharArray(), 0, eMail.length() );
                hd.endElement( "","", EtqEmail );
            }

            if ( url != null ) {
                hd.startElement( "","", EtqUrl, null );
                str = url.toString();
                hd.characters( str.toCharArray(), 0, str.length() );
                hd.endElement( "","", EtqUrl );
            }

            hd.endElement( "","", EtqContacts );
        }
    }

    public static BibliographicData loadFrom(File ifFile) throws IOException
    {
        BibliographicData toret = new BibliographicData();

        try {
            DocumentBuilderFactory factory =  DocumentBuilderFactory.newInstance();
            DocumentBuilder loader = factory.newDocumentBuilder();
            Document document = loader.parse( ifFile );
            Element tree = document.getDocumentElement();
            Node nodeStory = Util.getXmlChildByName( tree, EtqStory );
            Node node;

            if ( nodeStory != null ) {
                // Load format and IfID
                Node nodeIdentification = Util.getXmlChildByName( nodeStory, EtqIdentification );
                if ( nodeIdentification != null ) {
                    // Load format
                    node = Util.getXmlChildByName( nodeIdentification, EtqFormat );
                    Format format = Format.valueOf(
                        node.getTextContent().trim().toLowerCase()
                    );

                    if ( format != null )
                            toret.setFormat( format );
                    else    throw new IOException( MsgPartInvalidField + EtqFormat );

                    // Load ifID
                    node = Util.getXmlChildByName( nodeIdentification, EtqIfID );
                    String ifID = node.getTextContent().trim().toLowerCase();

                    if ( ifID != null
                      && !ifID.isEmpty() )
                    {
                        toret.setIfID( ifID );
                    } else throw new IOException( MsgPartInvalidField + EtqIfID );
                }

                // Load biblio node
                Node nodeBiblio = Util.getXmlChildByName( nodeStory, EtqBibliographic );

                if ( nodeBiblio == null ) {
                    throw new IOException( EtqBibliographic + MsgPartMissing );
                }

                // Load title
                node = Util.getXmlChildByName( nodeBiblio, EtqTitle );

                if ( node != null )
                        toret.setTitle( node.getTextContent() );
                else    throw new IOException( EtqTitle + MsgPartMissing );

                // Load Genre
                node = Util.getXmlChildByName( nodeBiblio, EtqGenre );
                Genre genre = Genre.valueOf( node.getTextContent().trim().toLowerCase() );

                if ( genre != null ) {
                        toret.setGenre( genre );
                }

                // Load author
                node = Util.getXmlChildByName( nodeBiblio, EtqAuthor );

                if ( node != null )
                     toret.setAuthor( node.getTextContent() );
                else throw new IOException( EtqAuthor + MsgPartMissing );

                // Load subtitle
                node = Util.getXmlChildByName( nodeBiblio, EtqHeadline );

                if ( node != null ) {
                     toret.setSubtitle( node.getTextContent() );
                }

                // Load forgiveness
                node = Util.getXmlChildByName( nodeBiblio, EtqForgiveness );

                if ( node != null ) {
                    toret.setForgiveness( Forgiveness.valueOf( node.getTextContent().trim().toLowerCase() ) );
                }

                // Load language
                node = Util.getXmlChildByName( nodeBiblio, EtqLanguage );
                Util.Language lang = Util.getLanguageByIso639Name( node.getTextContent() );

                if ( lang != null ) {
                        toret.setLanguage( lang );
                }

                // Load description
                node = Util.getXmlChildByName( nodeBiblio, EtqDescription );

                if ( node != null ) {
                    StringBuilder strDesc = new StringBuilder();
                    
                    // Retrieve the text desc with the <br>'s
                    for(int i = 0; i < node.getChildNodes().getLength(); ++i) {
                        Node n = node.getChildNodes().item( i );
                        if ( n.getNodeName().toLowerCase().equals( EtqXmlCr ) )
                        {
                            strDesc.append( "\n" );
                        } else {
                            strDesc.append( n.getTextContent()  );
                        }
                    }
                    
                    toret.setDesc( strDesc.toString() );
                }

                // Load release section
                Node nodeRelease = Util.getXmlChildByName( nodeStory, EtqRelease );

                if ( nodeRelease != null ) {
                    // Load version
                    node = Util.getXmlChildByName( nodeRelease, EtqVersion );

                    if ( node != null ) {
                        toret.setRelease(
                                Integer.parseInt( node.getTextContent() )
                        );
                    }

                    // Load release date
                    node = Util.getXmlChildByName( nodeRelease, EtqReleaseDate );

                    if ( node != null ) {
                        toret.setDate( node.getTextContent() );
                    }
                }

                // Load contacts section
                Node nodeContacts = Util.getXmlChildByName( nodeStory, EtqContacts );

                if ( nodeContacts != null ) {
                    // Load email
                    node = Util.getXmlChildByName( nodeContacts, EtqEmail );

                    if ( node != null ) {
                        toret.setEMail( node.getTextContent() );
                    }

                    // URL
                    node = Util.getXmlChildByName( nodeContacts, EtqUrl );

                    if ( node != null ) {
                        toret.setUrl( new URL( node.getTextContent() ) );
                    }
                }
            }
            else throw new IOException( "Malformed description file" );
        }
        catch(Exception ex) {
            toret = null;
            throw new IOException( "Impossible to load XML:\n" + ex.getMessage() );
        }

        return toret;
    }

    public static String generateNewIfId() {
        return UUID.randomUUID().toString().toUpperCase();
    }

    private String author;
    private String ifID;
    private String title;
    private String subtitle = "";
    private URL url;
    private String eMail = "";
    private int release;
    private Calendar date;
    private Genre genre = Genre.fiction;
    private Util.Language language;
    private Forgiveness forgiveness = Forgiveness.polite;
    private Format format = Format.glulx;
    private String desc = "";
}
