// Babble (c) 2014/24 Baltasar MIT License <baltasarq@gmail.com>


package core;


import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URI;
import java.net.URL;
import java.util.Properties;


/** Handles reading and writing as XML. */
public class BiblioGraphicDataXML {
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

    public BiblioGraphicDataXML(BibliographicData bibData)
    {
        this.bibData = bibData;
    }

    public BibliographicData getBibliographicData()
    {
        return this.bibData;
    }

    public void saveTo(File ifFile) throws IOException
    {
        try {
            final TransformerFactory TFF = TransformerFactory.newInstance();
            final Transformer TF = TFF.newTransformer();
            final DOMSource SOURCE = new DOMSource( this.toXML() );

            TF.setOutputProperty( OutputKeys.ENCODING, "UTF-8" );
            TF.setOutputProperty( OutputKeys.INDENT, "yes" );

            // Dump to file
            final StreamResult RESULT = new StreamResult( ifFile );
            TF.transform( SOURCE, RESULT );
        } catch (IOException ex) {
            throw new IOException(
                    "I/O problem creating file: " + ex.getMessage() );
        } catch (TransformerException e) {
            throw new IOException( e );
        }
    }

    /** Creates a sub-element.
      * @param DOM the XML document.
      * @param OWNER the element owner.
      * @param name the name of the element.
      * @return the created element.
     */
    private Element createElement(final Document DOM, final Element OWNER, String name)
    {
        final Element TORET = DOM.createElement( name );

        OWNER.appendChild( TORET );

        return TORET;
    }

    /** Creates a sub-element with text inside.
      * @param DOM the XML document.
      * @param OWNER the element owner.
      * @param name the name of thw new element.
      * @param value the string inside the new element.
      * @return the created element.
     */
    private Element createElement(final Document DOM, final Element OWNER, String name, String value)
    {
        final Element TORET = DOM.createElement( name );

        TORET.appendChild( DOM.createTextNode( value ) );
        OWNER.appendChild( TORET );

        return TORET;
    }

    public Document toXML() throws IOException
    {
        final DocumentBuilderFactory DBF = DocumentBuilderFactory.newInstance();
        Document toret;

        try {
            final DocumentBuilder DB = DBF.newDocumentBuilder();
            final String STR_FORMAT = this.bibData.getFormat().toString();
            final String STR_IF_ID = this.bibData.getIfID();
            final String STR_DATE = this.bibData.getDate().toString();
            final String STR_RELEASE = Integer.toString( this.bibData.getRelease() );

            toret = DB.newDocument();

            // Root
            final Element IFINDEX = toret.createElement( EtqIfIndex );
            IFINDEX.setAttribute( EtqIFictionVersion, IFictionVersionValue );
            IFINDEX.setAttribute( EtqXmlns, XmlnsValue );
            toret.appendChild( IFINDEX );

            // The co-root element, and id section.
            final Element STORY = this.createElement( toret, IFINDEX, EtqStory );
            final Element IDENTIFICATION = this.createElement( toret, STORY, EtqIdentification );

            this.createElement( toret, IDENTIFICATION, EtqIfID, STR_IF_ID  );
            this.createElement( toret, IDENTIFICATION, EtqFormat, STR_FORMAT );

            // Bibliographic section
            this.writeBibliographicSection( toret, STORY );

            // Contacts
            this.writeContactsSection( toret, STORY );

            // Release
            final Element RELEASE = this.createElement( toret, STORY, EtqRelease );

            this.createElement( toret, RELEASE, EtqVersion, STR_RELEASE );
            this.createElement( toret, RELEASE, EtqReleaseDate, STR_DATE );

            // Colophon
            final Element COLOPHON = this.createElement( toret, STORY, EtqColophon );

            this.createElement( toret, COLOPHON, EtqGenerator, AppInfo.NAME );
            this.createElement( toret, COLOPHON, EtqOriginated, STR_DATE );
        } catch(ParserConfigurationException exc) {
            throw new IOException(
                    "problem creating IFiction file:" + exc.getMessage() );
        }

        return toret;
    }

    private void writeBibliographicSection(final Document DOM, final Element STORY)
    {
        final Element BIBLIO = this.createElement( DOM, STORY, EtqBibliographic );

        this.createElement( DOM, BIBLIO, EtqTitle, this.bibData.getTitle() );
        this.createElement( DOM, BIBLIO, EtqAuthor, this.bibData.getAuthor() );
        this.createElement( DOM, BIBLIO, EtqLanguage, this.bibData.getLanguage().toString() );

        if ( !this.bibData.getSubtitle().isEmpty() ) {
            this.createElement( DOM, BIBLIO, EtqHeadline, this.bibData.getSubtitle() );
        }

        this.createElement( DOM, BIBLIO, EtqGenre, this.bibData.getGenre().toString() );
        this.createElement( DOM, BIBLIO, EtqForgiveness, this.bibData.getForgiveness().toString() );
        this.createElement( DOM, BIBLIO, EtqDescription,
                this.bibData.getDesc().replace( "\n", EtqXmlCr ) );
    }

    private void writeContactsSection(final Document DOM, final Element STORY)
    {
        final URL URL = this.bibData.getUrl();
        final String EMail = this.bibData.getEMail();

        if ( URL != null
          || !EMail.isEmpty() )
        {
            final Element CONTACTS = this.createElement( DOM, STORY, EtqContacts );

            if ( !EMail.isEmpty() ) {
                this.createElement( DOM, CONTACTS, EtqEmail, EMail );
            }

            if ( URL != null ) {
                this.createElement( DOM, CONTACTS, EtqUrl, URL.toString() );
            }
        }
    }

    public static BibliographicData loadFrom(File ifFile) throws IOException
    {
        BibliographicData toret = new BibliographicData();

        try {
            final DocumentBuilderFactory DBF =  DocumentBuilderFactory.newInstance();
            final DocumentBuilder LD = DBF.newDocumentBuilder();
            final Document DOM = LD.parse( ifFile );
            final Element ROOT = DOM.getDocumentElement();
            final Node NODE_STORY = Util.getXmlChildByName( ROOT, EtqStory );
            Node node;

            if ( NODE_STORY != null ) {
                // Load format and IfID
                Node nodeIdentification = Util.getXmlChildByName( NODE_STORY, EtqIdentification );

                if ( nodeIdentification != null ) {
                    // Load format
                    node = Util.getXmlChildByName( nodeIdentification, EtqFormat );
                    toret.setFormat( BibliographicData.Format.valueOf(
                            node.getTextContent().trim().toLowerCase() ));

                    // Load ifID
                    node = Util.getXmlChildByName( nodeIdentification, EtqIfID );
                    String ifID = node.getTextContent().trim().toLowerCase();

                    if ( !ifID.isEmpty() ) {
                        toret.setIfID( ifID );
                    } else {
                        throw new IOException( MsgPartInvalidField + EtqIfID );
                    }
                }

                // Load biblio node
                Node nodeBiblio = Util.getXmlChildByName( NODE_STORY, EtqBibliographic );

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
                toret.setGenre( BibliographicData.Genre.valueOf( node.getTextContent().trim().toLowerCase() ) );

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
                    toret.setForgiveness( BibliographicData.Forgiveness.valueOf( node.getTextContent().trim().toLowerCase() ) );
                }

                // Load language
                node = Util.getXmlChildByName( nodeBiblio, EtqLanguage );
                I18n.Language lang = I18n.getLanguageByIso639Name( node.getTextContent() );

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
                        if ( n.getNodeName().equalsIgnoreCase( EtqXmlCr ) )
                        {
                            strDesc.append( "\n" );
                        } else {
                            strDesc.append( n.getTextContent()  );
                        }
                    }

                    toret.setDesc( strDesc.toString() );
                }

                // Load release section
                Node nodeRelease = Util.getXmlChildByName( NODE_STORY, EtqRelease );

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
                        toret.setDate( ISODate.fromString( node.getTextContent() ) );
                    }
                }

                // Load contacts section
                Node nodeContacts = Util.getXmlChildByName( NODE_STORY, EtqContacts );

                if ( nodeContacts != null ) {
                    // Load email
                    node = Util.getXmlChildByName( nodeContacts, EtqEmail );

                    if ( node != null ) {
                        toret.setEMail( node.getTextContent() );
                    }

                    // URL
                    node = Util.getXmlChildByName( nodeContacts, EtqUrl );

                    if ( node != null ) {
                        toret.setUrl( new URI( node.getTextContent() ).toURL() );
                    }
                }
            }
            else {
                throw new IOException( "Malformed description file" );
            }
        }
        catch(Exception ex) {
            throw new IOException( "Impossible to load XML:\n" + ex.getMessage() );
        }

        return toret;
    }

    private final BibliographicData bibData;
}
