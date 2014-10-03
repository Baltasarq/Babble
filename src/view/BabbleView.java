/*
 * BabbleView.java
 *
 * Created on 4 de junio de 2008, 11:44
 */

package view;

import core.AppInfo;
import core.BibliographicData;
import core.Project;
import core.Util;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.datatransfer.StringSelection;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * This Frame inputs data in order to create the iFiction file.
 * @author  baltasarq
 */
public class BabbleView extends JFrame {
    public static final String Title = "Babelifier";
    public static final String CmdBabel = "babel";
    public static final String CmdBabelOpts = "-blorbs";
    public static final String[] NoProject = {
        "No hay un proyecto abierto",
        "There is no open project"
    };

     public static final String[] AreYouSure = {
        "¿Seguro?",
        "Are your sure?"
    };

    /** Creates new form BabbleView */
    public BabbleView() {
        this.build();
        this.translateViewIntoSpanish();
        this.deactivate();
    }

    public final void deactivate()
    {
        this.prepareGui( false );
    }

    public void activate()
    {
        this.prepareGui( true );
    }
    
    public void prepareGui(boolean active)
    {
        this.getContentPane().setVisible(active);

        this.opSave.setEnabled( active );
        this.opSaveAs.setEnabled( active );
        this.opClose.setEnabled( active );
        this.opGenerate.setEnabled( active );
        this.opCopyIfId.setEnabled( active );
        
        if ( !active ) {
            this.setTitle();
            this.fillFieldsWithDefaultValues();
        }
    }

    /// Ends the app
    private void quit()
    {
        this.setVisible(false);
        this.dispose();
        System.exit( 0 );
    }

    public static void fillComboBox(JComboBox cmb, String values[], int selected) {
        DefaultComboBoxModel items = new DefaultComboBoxModel();
        
        for(int i = 0; i < values.length; ++i) {
            items.addElement( values[ i ] );
        }
        
        cmb.setModel( items );
        cmb.setSelectedIndex( selected );
    }

    private void setTitle() {
        super.setTitle( AppInfo.name + " - []" );
    }

    @Override
    public void setTitle(String name) {
        if ( name != null ) {
            name = name.trim();

            if ( !name.isEmpty() ) {
                super.setTitle( AppInfo.name + " - [ " + name + " ]" );
            }
        }
    }

    private String getReleaseValueAsString() {
        return ( Integer.toString( this.getReleaseValue() ) ).trim();
    }

     private int getReleaseValue() {
        return ( (Integer) edVersion.getValue() ).intValue();
    }

    private void chk() {
        if ( edTitle.getText().trim().isEmpty() ) {
            throw new RuntimeException( "Title field is required" );
        }
        else
        if ( edAuthor.getText().trim().isEmpty() ) {
            throw new RuntimeException( "Author field is required" );
        }
        else
        if ( edDate.getText().trim().isEmpty() ) {
            throw new RuntimeException( "Date field is required" );
        }        
    }

    private BibliographicData syncBibliogaphicData(Project prj) throws Exception {
        BibliographicData toret = prj.getBiblio();

        // Basic attributes
        toret.setAuthor( edAuthor.getText() );
        toret.setSubtitle( edSubtitle.getText() );
        toret.setDesc( edDesc.getText() );
        toret.setEMail( edEmail.getText() );
        toret.setForgiveness(
                BibliographicData.Forgiveness.values()[edForgiveness.getSelectedIndex()]
        );
        toret.setFormat(
                BibliographicData.Format.values()[ edFormat.getSelectedIndex() ]
        );
        toret.setGenre(
                BibliographicData.Genre.values()[ edGenre.getSelectedIndex() ]
        );
        toret.setLanguage(
                Util.Language.values()[ edLanguage.getSelectedIndex() ]
        );
        toret.setRelease( getReleaseValue() );
        toret.setTitle( edTitle.getText() );
        
        // URL
        if ( !edUrl.getText().trim().isEmpty() ) {
            toret.setUrl( new URL( edUrl.getText() ) );
        }
        
        // Date
        DateFormat formatter = new SimpleDateFormat( BibliographicData.FmtDate );
        Date date = formatter.parse( edDate.getText() );
        Calendar cal = Calendar.getInstance();
        cal.setTime( date );
        toret.setDate( cal );
        
        return toret;
    }

    private void buildAboutBox()
    {
        JLabel lblAuthor = new JLabel( AppInfo.author );
        final JLabel lblWebPage = new JLabel( AppInfo.web );
        JLabel lblAppName = new JLabel( AppInfo.name + " " + AppInfo.version );
        JButton btOk = new JButton( "Ok" );
        JLabel lblIcon = new JLabel( "" );
        JPanel aboutPanel = new JPanel();
        JPanel infoPanel = new JPanel();

        btOk.setMnemonic( 'o' );
        btOk.addActionListener( new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BabbleView.this.aboutBox.setVisible( false );
            }
        } );
        lblWebPage.setCursor( new Cursor( Cursor.HAND_CURSOR ) );

        if ( this.getIconImage() != null ) {
            lblIcon.setIcon( new ImageIcon( this.getIconImage() ) );
            this.aboutBox.setIconImage( this.getIconImage() );
        } else {
            lblIcon.setText( "" );
        }

        lblAuthor.setFont( new java.awt.Font( "Tahoma", 2, 12 ) );
        lblWebPage.setFont(new java.awt.Font("Times New Roman", 0, 18));
        lblWebPage.setForeground(new java.awt.Color(51, 0, 255));
        lblWebPage.addMouseListener( new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) { openURI( lblWebPage.getText() ); }
        } );
        lblAppName.setFont( new java.awt.Font( "Tahoma", 1, 18 ) );

        this.aboutBox = new javax.swing.JDialog();

        infoPanel.setLayout( new BoxLayout( infoPanel, BoxLayout.PAGE_AXIS ) );
        infoPanel.add( Box.createRigidArea( new Dimension( 0,20 ) ) );
        infoPanel.add( lblAppName );
        infoPanel.add( Box.createRigidArea( new Dimension( 0,5 ) ) );
        infoPanel.add( lblAuthor );
        infoPanel.add( Box.createRigidArea( new Dimension( 0,5 ) ) );
        infoPanel.add( lblWebPage );
        infoPanel.add( Box.createRigidArea( new Dimension( 0,20 ) ) );

        aboutPanel.setLayout( new BorderLayout( 10, 10 ) );
        aboutPanel.setBorder( new EmptyBorder( 10, 10, 10, 10 ) );
        aboutPanel.add( lblIcon, BorderLayout.LINE_START );
        aboutPanel.add( infoPanel, BorderLayout.CENTER  );

        this.aboutBox.setContentPane( aboutPanel );
        this.aboutBox.setTitle( this.opAbout.getText() );
        this.aboutBox.setModal( true );
        this.aboutBox.setResizable( false );
        this.aboutBox.setLocationRelativeTo( this );
        this.aboutBox.pack();
    }

    private void buildMenu()
    {
        this.mMainMenu = new javax.swing.JMenuBar();
        this.mFile = new javax.swing.JMenu();
        this.opNew = new javax.swing.JMenuItem();
        this.opOpen = new javax.swing.JMenuItem();
        this.opSave = new javax.swing.JMenuItem();
        this.opSaveAs = new javax.swing.JMenuItem();
        this.opClose = new javax.swing.JMenuItem();
        this.opQuit = new javax.swing.JMenuItem();
        this.mEdit = new javax.swing.JMenu();
        this.opGenerate = new javax.swing.JMenuItem();
        this.opCopyIfId = new javax.swing.JMenuItem();
        this.mTools = new javax.swing.JMenu();
        this.opLanguageES = new javax.swing.JCheckBoxMenuItem();
        this.opLanguageEN = new javax.swing.JCheckBoxMenuItem();
        this.mHelp = new javax.swing.JMenu();
        this.opAbout = new javax.swing.JMenuItem();

        this.mFile.setText( "File" );

        this.opNew.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_N, java.awt.event.InputEvent.CTRL_MASK));
        this.opNew.setMnemonic('n');
        this.opNew.setText("New");
        this.opNew.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                opNewActionPerformed(evt);
            }
        });
        this.mFile.add( this.opNew );

        this.opOpen.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_O, java.awt.event.InputEvent.CTRL_MASK));
        this.opOpen.setMnemonic('o');
        this.opOpen.setText("Open");
        this.opOpen.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                opOpenActionPerformed(evt);
            }
        });
        this.mFile.add( this.opOpen );

        opSave.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_S, java.awt.event.InputEvent.CTRL_MASK));
        opSave.setMnemonic('s');
        opSave.setText("Save");
        opSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                opSaveActionPerformed(evt);
            }
        });
        this.mFile.add( this.opSave );

        this.opSaveAs.setMnemonic('a');
        this.opSaveAs.setText("Save as...");
        this.opSaveAs.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                opSaveAsActionPerformed(evt);
            }
        });
        this.mFile.add( this.opSaveAs );

        this.opClose.setMnemonic('c');
        this.opClose.setText("Close");
        this.opClose.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                opCloseActionPerformed(evt);
            }
        });
        this.mFile.add( this.opClose );

        this.opQuit.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_Q, java.awt.event.InputEvent.CTRL_MASK));
        this.opQuit.setMnemonic('q');
        this.opQuit.setText("Quit");
        this.opQuit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                quit();
            }
        });
        this.mFile.add( this.opQuit );

        this.mMainMenu.add( this.mFile );

        this.mEdit.setText( "Edit" );

        this.opGenerate.setMnemonic('g');
        this.opGenerate.setText("Generate new IfID");
        this.opGenerate.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                opGenerateActionPerformed(evt);
            }
        });
        this.mEdit.add( this.opGenerate );

        this.opCopyIfId.setMnemonic('c');
        this.opCopyIfId.setText("Copy IfID to Clipboard");
        this.opCopyIfId.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                opCopyIfIdActionPerformed(evt);
            }
        });
        this.mEdit.add( this.opCopyIfId );

        this.mMainMenu.add( this.mEdit );

        this.mTools.setText("Tools");

        this.opLanguageES.setMnemonic('e');
        this.opLanguageES.setSelected(true);
        this.opLanguageES.setText("Español");
        this.opLanguageES.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                opLanguageESActionPerformed(evt);
            }
        });
        this.mTools.add( this.opLanguageES );

        this.opLanguageEN.setMnemonic('n');
        this.opLanguageEN.setText("English");
        this.opLanguageEN.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                opLanguageENActionPerformed(evt);
            }
        });
        this.mTools.add( this.opLanguageEN );

        this.mMainMenu.add( this.mTools );

        this.mHelp.setText("Help");

        this.opAbout.setMnemonic('a');
        this.opAbout.setText("About...");
        this.opAbout.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                opAboutActionPerformed(evt);
            }
        });
        this.mHelp.add( this.opAbout );

        this.mMainMenu.add( this.mHelp );
        this.setJMenuBar( this.mMainMenu );
    }

    private void buildInfoPanel()
    {
        JPanel titleSubPanel = new JPanel();
        JPanel subtitleSubPanel = new JPanel();
        JPanel authorSubPanel = new JPanel();
        JPanel webSubPanel = new JPanel();
        JPanel emailSubPanel = new JPanel();

        this.infoPanel = new javax.swing.JPanel();
        this.lblTitle = new javax.swing.JLabel();
        this.lblSubtitle = new javax.swing.JLabel();
        this.lblIfAuthor = new javax.swing.JLabel();
        this.lblEmail = new javax.swing.JLabel( "E.mail" );
        this.lblURL = new javax.swing.JLabel();
        this.lblTitle.setFont( new java.awt.Font( "DejaVu Sans", 1, 12 ) );
        this.lblIfAuthor.setFont( new java.awt.Font( "DejaVu Sans", 1, 12 ) );

        this.edTitle = new javax.swing.JTextField();
        this.edTitle.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                edTitleKeyReleased(evt);
            }
        });
        Box boxTitle = Box.createVerticalBox();
        boxTitle.add(Box.createVerticalGlue());
        boxTitle.add(this.edTitle);
        boxTitle.add(Box.createVerticalGlue());

        this.edSubtitle = new javax.swing.JTextField();
        Box boxSubtitle = Box.createVerticalBox();
        boxSubtitle.add(Box.createVerticalGlue());
        boxSubtitle.add(this.edSubtitle);
        boxSubtitle.add(Box.createVerticalGlue());

        this.edAuthor = new javax.swing.JTextField();
        Box boxAuthor = Box.createVerticalBox();
        boxAuthor.add(Box.createVerticalGlue());
        boxAuthor.add(this.edAuthor);
        boxAuthor.add(Box.createVerticalGlue());

        this.edEmail = new javax.swing.JTextField();
        Box boxEmail = Box.createVerticalBox();
        boxEmail.add(Box.createVerticalGlue());
        boxEmail.add(this.edEmail);
        boxEmail.add(Box.createVerticalGlue());

        this.edUrl = new javax.swing.JTextField();
        Box boxUrl = Box.createVerticalBox();
        boxUrl.add(Box.createVerticalGlue());
        boxUrl.add(this.edUrl);
        boxUrl.add(Box.createVerticalGlue());


        titleSubPanel.setLayout( new BoxLayout( titleSubPanel, BoxLayout.LINE_AXIS ) );
        titleSubPanel.add( this.lblTitle );
        titleSubPanel.add( Box.createRigidArea( new Dimension( 10, 0 ) ) );
        titleSubPanel.add( boxTitle );

        subtitleSubPanel.setLayout( new BoxLayout( subtitleSubPanel, BoxLayout.LINE_AXIS ) );
        subtitleSubPanel.add( this.lblSubtitle );
        subtitleSubPanel.add( Box.createRigidArea( new Dimension( 10, 0 ) ) );
        subtitleSubPanel.add( boxSubtitle );

        authorSubPanel.setLayout( new BoxLayout( authorSubPanel, BoxLayout.LINE_AXIS ) );
        authorSubPanel.add( this.lblIfAuthor );
        authorSubPanel.add( Box.createRigidArea( new Dimension( 10, 0 ) ) );
        authorSubPanel.add( boxAuthor );

        emailSubPanel.setLayout( new BoxLayout( emailSubPanel, BoxLayout.LINE_AXIS ) );
        emailSubPanel.add( this.lblEmail );
        emailSubPanel.add( Box.createRigidArea( new Dimension( 10, 0 ) ) );
        emailSubPanel.add( boxEmail );

        webSubPanel.setLayout( new BoxLayout( webSubPanel, BoxLayout.LINE_AXIS ) );
        webSubPanel.add( this.lblURL );
        webSubPanel.add( Box.createRigidArea( new Dimension( 10, 0 ) ) );
        webSubPanel.add( boxUrl );

        this.infoPanel.setBorder( javax.swing.BorderFactory.createTitledBorder( "Main" ) );
        this.infoPanel.setLayout( new BoxLayout( this.infoPanel, BoxLayout.PAGE_AXIS ) );
        this.infoPanel.add( titleSubPanel );
        this.infoPanel.add( Box.createRigidArea( new Dimension( 0,2 ) ) );
        this.infoPanel.add( subtitleSubPanel );
        this.infoPanel.add( Box.createRigidArea( new Dimension( 0,2 ) ) );
        this.infoPanel.add( authorSubPanel );
        this.infoPanel.add( Box.createRigidArea( new Dimension( 0,2 ) ) );
        this.infoPanel.add( emailSubPanel );
        this.infoPanel.add( Box.createRigidArea( new Dimension( 0,2 ) ) );
        this.infoPanel.add( webSubPanel );
        this.infoPanel.add( Box.createRigidArea( new Dimension( 0,2 ) ) );
    }

    private void buildFormatPanel()
    {
        JPanel versionSubPanel = new JPanel();
        JPanel dateSubPanel = new JPanel();
        JPanel genreSubPanel = new JPanel();
        JPanel languageSubPanel = new JPanel();
        JPanel forgivenessSubPanel = new JPanel();
        JPanel formatSubPanel = new JPanel();

        this.formatInfoPanel = new javax.swing.JPanel();
        this.lblRelease = new javax.swing.JLabel();
        this.lblDate = new javax.swing.JLabel();
        this.lblGenre = new javax.swing.JLabel();
        this.lblLanguage = new javax.swing.JLabel();
        this.lblForgiveness = new javax.swing.JLabel();
        this.lblFormat = new javax.swing.JLabel();

        this.lblRelease.setFont( new java.awt.Font( "DejaVu Sans", 1, 12 ) );
        this.lblDate.setFont( new java.awt.Font( "DejaVu Sans", 1, 12 ) );
        this.lblGenre.setFont( new java.awt.Font( "DejaVu Sans", 1, 12 ) );
        this.lblLanguage.setFont( new java.awt.Font( "DejaVu Sans", 1, 12 ) );
        this.lblForgiveness.setFont( new java.awt.Font( "DejaVu Sans", 1, 12 ) );
        this.lblFormat.setFont( new java.awt.Font( "DejaVu Sans", 1, 12 ) );

        this.edVersion = new javax.swing.JSpinner();
        this.edVersion.setMaximumSize( new Dimension( Integer.MAX_VALUE, this.edVersion.getPreferredSize().height ) );
        Box boxVersion = Box.createVerticalBox();
        boxVersion.add( Box.createVerticalGlue() );
        boxVersion.add( this.edVersion );
        boxVersion.add( Box.createVerticalGlue() );

        this.edDate = new javax.swing.JTextField();
        this.edDate.setMaximumSize( new Dimension( Integer.MAX_VALUE, this.edDate.getPreferredSize().height ) );
        this.edDate.setHorizontalAlignment( javax.swing.JTextField.RIGHT );
        Box boxDate = Box.createVerticalBox();
        boxDate.add( Box.createVerticalGlue() );
        boxDate.add( this.edDate );
        boxDate.add( Box.createVerticalGlue() );

        this.edGenre = new javax.swing.JComboBox();
        this.edGenre.setMaximumSize( new Dimension( Integer.MAX_VALUE, this.edGenre.getPreferredSize().height ) );
        Box boxGenre = Box.createVerticalBox();
        boxGenre.add( Box.createVerticalGlue() );
        boxGenre.add( this.edGenre );
        boxGenre.add( Box.createVerticalGlue() );

        this.edLanguage = new javax.swing.JComboBox();
        this.edLanguage.setMaximumSize( this.edLanguage.getPreferredSize() );
        this.edLanguage.setMaximumSize( new Dimension( Integer.MAX_VALUE, this.edLanguage.getPreferredSize().height ) );
        Box boxLanguage = Box.createVerticalBox();
        boxLanguage.add( Box.createVerticalGlue() );
        boxLanguage.add( this.edLanguage );
        boxLanguage.add( Box.createVerticalGlue() );

        this.edForgiveness = new javax.swing.JComboBox();
        this.edForgiveness.setMaximumSize( this.edForgiveness.getPreferredSize() );
        this.edForgiveness.setMaximumSize( new Dimension( Integer.MAX_VALUE, this.edForgiveness.getPreferredSize().height ) );
        Box boxForgiveness = Box.createVerticalBox();
        boxForgiveness.add( Box.createVerticalGlue() );
        boxForgiveness.add( this.edForgiveness );
        boxForgiveness.add( Box.createVerticalGlue() );

        this.edFormat = new javax.swing.JComboBox();
        this.edFormat.setMaximumSize( this.edFormat.getPreferredSize() );
        this.edFormat.setMaximumSize( new Dimension( Integer.MAX_VALUE, this.edFormat.getPreferredSize().height ) );
        Box boxFormat = Box.createVerticalBox();
        boxFormat.add( Box.createVerticalGlue() );
        boxFormat.add( this.edFormat );
        boxFormat.add( Box.createVerticalGlue() );

        this.formatInfoPanel.setBorder( javax.swing.BorderFactory.createTitledBorder( "Other" ) );

        versionSubPanel.setLayout( new BoxLayout( versionSubPanel, BoxLayout.LINE_AXIS ) );
        versionSubPanel.add( this.lblRelease );
        versionSubPanel.add(Box.createRigidArea(new Dimension(10, 0)));
        versionSubPanel.add( boxVersion );

        dateSubPanel.setLayout( new BoxLayout( dateSubPanel, BoxLayout.LINE_AXIS ) );
        dateSubPanel.add(this.lblDate);
        dateSubPanel.add( Box.createRigidArea( new Dimension( 10, 0 ) ) );
        dateSubPanel.add( boxDate );

        genreSubPanel.setLayout(new BoxLayout(genreSubPanel, BoxLayout.LINE_AXIS));
        genreSubPanel.add( this.lblGenre );
        genreSubPanel.add(Box.createRigidArea( new Dimension( 10, 0 ) ) );
        genreSubPanel.add( boxGenre );

        languageSubPanel.setLayout( new BoxLayout( languageSubPanel, BoxLayout.LINE_AXIS ) );
        languageSubPanel.add( this.lblLanguage );
        languageSubPanel.add(Box.createRigidArea( new Dimension( 10, 0 ) ) );
        languageSubPanel.add( boxLanguage );

        forgivenessSubPanel.setLayout(new BoxLayout(forgivenessSubPanel, BoxLayout.LINE_AXIS));
        forgivenessSubPanel.add( this.lblForgiveness );
        forgivenessSubPanel.add( Box.createRigidArea(new Dimension( 10, 0 )) );
        forgivenessSubPanel.add( boxForgiveness );

        formatSubPanel.setLayout( new BoxLayout( formatSubPanel, BoxLayout.LINE_AXIS ) );
        formatSubPanel.add( this.lblFormat );
        formatSubPanel.add( Box.createRigidArea( new Dimension( 10, 0 ) ) );
        formatSubPanel.add( boxFormat );

        this.formatInfoPanel.setBorder( javax.swing.BorderFactory.createTitledBorder( "Format" ) );
        this.formatInfoPanel.setLayout( new BoxLayout( this.formatInfoPanel, BoxLayout.PAGE_AXIS ) );
        this.formatInfoPanel.add( versionSubPanel );
        this.formatInfoPanel.add( Box.createRigidArea( new Dimension( 0,2 ) ) );
        this.formatInfoPanel.add( dateSubPanel );
        this.formatInfoPanel.add( Box.createRigidArea( new Dimension( 0,2 ) ) );
        this.formatInfoPanel.add( genreSubPanel );
        this.formatInfoPanel.add( Box.createRigidArea( new Dimension( 0,2 ) ) );
        this.formatInfoPanel.add( languageSubPanel );
        this.formatInfoPanel.add( Box.createRigidArea( new Dimension( 0,2 ) ) );
        this.formatInfoPanel.add( forgivenessSubPanel );
        this.formatInfoPanel.add( Box.createRigidArea( new Dimension( 0,2 ) ) );
        this.formatInfoPanel.add( formatSubPanel );
        this.formatInfoPanel.add( Box.createRigidArea( new Dimension( 0,2 ) ) );
    }

    private void buildDescPanel()
    {
        this.edDesc = new javax.swing.JTextArea();

        this.edDesc.setColumns( 20 );
        this.edDesc.setRows( 5 );
        this.edDesc.setLineWrap(true);
        this.edDesc.setWrapStyleWord( true );

        JScrollPane scrPanel = new javax.swing.JScrollPane( this.edDesc );
        this.descPanel = new javax.swing.JPanel();
        this.descPanel.setLayout( new BorderLayout() );
        this.descPanel.setBorder( javax.swing.BorderFactory.createTitledBorder( "Desc" ) );
        this.descPanel.add( scrPanel, BorderLayout.CENTER );
    }

    private void buildIdPanel()
    {
        this.idPanel = new javax.swing.JPanel();
        this.lblIfId = new javax.swing.JLabel();
        this.lblIfId.setFont( new java.awt.Font( "Cordia New", 1, 18 ) );
        this.lblIfId.setHorizontalAlignment( JLabel.CENTER );

        this.idPanel.setBorder( javax.swing.BorderFactory.createTitledBorder( "IfId" ) );
        this.idPanel.setLayout( new BorderLayout() );
        this.idPanel.add( this.lblIfId, BorderLayout.CENTER );
    }

    private void buildIcon()
    {
        Image img = null;

        try {
            img = Toolkit.getDefaultToolkit().getImage(
                    BabbleView.class.getResource( "/babble.png" ) );

            if ( img != null ) {
                this.setIconImage( img );
            }

        } catch(Exception exc)
        {
            // ignored
        }

        return;
    }

    private void build()
    {
        JPanel panel = new javax.swing.JPanel();

        this.setContentPane( panel );
        panel.setBorder( new EmptyBorder( 10, 10, 10, 10 ) );

        this.buildMenu();
        this.buildAboutBox();
        this.buildInfoPanel();
        this.buildFormatPanel();
        this.buildDescPanel();
        this.buildIdPanel();

        panel.setLayout(new BoxLayout(panel, BoxLayout.PAGE_AXIS));
        panel.add( this.infoPanel);
        panel.add(Box.createRigidArea(new Dimension(0, 5)));
        panel.add(this.formatInfoPanel);
        panel.add(Box.createRigidArea(new Dimension(0, 5)));
        panel.add(this.descPanel);
        panel.add(Box.createRigidArea(new Dimension(0, 5)));
        panel.add(this.idPanel);

        this.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
        this.setMinimumSize( new Dimension( 620, 460 ) );
        this.setTitle();
        this.setLocationRelativeTo( null );
        this.pack();
    }

private void opOpenActionPerformed(java.awt.event.ActionEvent evt) {
    this.opCloseActionPerformed( evt );

    File ifFile = Util.openFileDlg( this, Project.IFictionExt, Project.IFictionExtExpl );
    
    try {
        if ( ifFile != null ) {
            this.activate();
            prj = new Project( ifFile );

            fillFieldsWith( prj.getBiblio() );
        }
    } catch (Exception ex) {
        Util.msgError( this, ex.getMessage() );
        prj = null;
        this.deactivate();
    }
}

private void opGenerateActionPerformed(java.awt.event.ActionEvent evt) {
    if ( prj != null ) {
        prj.getBiblio().setNewIfID();
        lblIfId.setText(prj.getBiblio().getIfID());
    }
    else Util.msgError( this, this.getNoProjectMsg() );
}

private void opAboutActionPerformed(java.awt.event.ActionEvent evt)
{
    this.aboutBox.setVisible( true );
}

private void openURI(String uri) {
    try {
        Desktop.getDesktop().browse( new URI( uri ) );
    } catch(Exception e) {
        Util.msgError( this, "Open: " + uri );
    }
}

private void opLanguageESActionPerformed(java.awt.event.ActionEvent evt) {
    this.setLanguage( Util.Language.ES );
    opLanguageES.setSelected( true );
    opLanguageEN.setSelected( false );
    this.translateViewIntoSpanish();
}

private void opLanguageENActionPerformed(java.awt.event.ActionEvent evt) {
    this.setLanguage( Util.Language.EN );
    opLanguageES.setSelected( false );
    opLanguageEN.setSelected( true );
    this.translateViewIntoEnglish();
}

private void opNewActionPerformed(java.awt.event.ActionEvent evt) {
    this.opCloseActionPerformed( evt );
    
    activate();
    prj = new Project( BibliographicData.DefaultTitle );
    this.fillFieldsWith( prj.getBiblio() );
    this.setTitle( BibliographicData.DefaultTitle );
}

private void opSaveActionPerformed(java.awt.event.ActionEvent evt) {
    File ifFile = null;

    if ( prj == null ) {
        Util.msgError( this, this.getNoProjectMsg() );
        return;
    }

    try {
        chk();
        this.syncUIToProject();

        if ( prj.getIfFile() == null ) {
            // Put the filename synced with the title of the project
            prj.syncFileName();
            Util.setLastFile( prj.getIfFile() );

            // Trigger the save dialog
            ifFile = Util.saveFileDlg( this, Project.IFictionExt, Project.IFictionExtExpl );

            if ( ifFile != null ) {
                if ( ifFile.exists() ) {
                    if ( !Util.askIf( this, this.getAreYouSureMsg() + ifFile.getAbsolutePath() ) )
                    {
                        return;
                    }
                }

                prj.setIfFile( ifFile );
                Util.setLastFile( ifFile );
            }
            else return;
        }

        try {
            prj.getBiblio().saveTo( prj.getIfFile() );
        } catch (IOException ex) {
            Util.msgError( this, ex.getMessage() );
        }
    } catch(Exception e) {
        Util.msgError( this, e.getMessage() );
    }
}

private void opCloseActionPerformed(java.awt.event.ActionEvent evt) {
    if ( prj != null ) {
        if ( !Util.askIf( this, this.getAreYouSureMsg() + " (" + prj.getName() + ")" ) )
        {
            return;
        }
        
        this.opSaveActionPerformed( evt );
    }

    prj = null;
    this.deactivate();
}

private void edTitleKeyReleased(java.awt.event.KeyEvent evt) {
    if ( prj != null ) {
        String newTitle = edTitle.getText().trim();

        if ( !newTitle.isEmpty() ) {
            prj.getBiblio().setTitle( newTitle );
            this.setTitle( newTitle );
        }
    }
}

private void opSaveAsActionPerformed(java.awt.event.ActionEvent evt) {
    if ( prj != null ) {
        Util.setLastFile( prj.getIfFile() );
        prj.setIfFile( null );
        opSaveActionPerformed( evt );
    } else Util.msgError( this, this.getNoProjectMsg() );
}

    private void opCopyIfIdActionPerformed(java.awt.event.ActionEvent evt) {
        StringSelection ss = new StringSelection( this.lblIfId.getText() );
        Toolkit.getDefaultToolkit().getSystemClipboard().setContents( ss, null );
    }

    /**
     * Fills all the view with the data from babeldata.
     * @param babelData The Babel Treaty data loaded from file.
     */
    private void fillFieldsWith(BibliographicData babelData)
    {
        // Load
        this.edAuthor.setText( babelData.getAuthor() );
        this.edTitle.setText( babelData.getTitle() );
        this.edSubtitle.setText( babelData.getSubtitle() );
        this.edDesc.setText( babelData.getDesc() );
        this.edVersion.setValue(new Integer(babelData.getRelease()));
        this.edDate.setText( babelData.getDateAsString() );
        this.edUrl.setText( babelData.getUrl().toString() );
        this.edEmail.setText( babelData.getEMail() );
        this.edLanguage.setSelectedIndex( babelData.getLanguage().ordinal() );
        this.edGenre.setSelectedIndex( babelData.getGenre().ordinal() );
        this.edFormat.setSelectedIndex( babelData.getFormat().ordinal() );
        this.edForgiveness.setSelectedIndex(babelData.getForgiveness().ordinal());
        this.lblIfId.setText(babelData.getIfID());

        // Polish
        this.setTitle( babelData.getTitle() );
        return;
    }

    /**
     * Loads default values (enough for saving) in the view.
     */
    private void fillFieldsWithDefaultValues()
    {
        // Fill fields
        DateFormat dateFormat = new SimpleDateFormat( BibliographicData.FmtDate );
        Date date = new Date();
        
        lblIfId.setText("");
        edTitle.setText( "" );
        edSubtitle.setText( "" );
        edAuthor.setText( "" );
        edDate.setText( dateFormat.format(date) );
        edEmail.setText( "" );
        edVersion.setValue(new Integer(1));
        edDesc.setText( "" );
        edUrl.setText( "" );
        
        lblIfId.setEnabled(false);
    }

    /**
     * Returns the language used in the view
     * @return the language
     */
    public Util.Language getLanguage()
    {
        return language;
    }

    /**
     * Sets the language to use in the view
     * @param language the language to set
     */
    public void setLanguage(Util.Language language)
    {
        this.language = language;
    }

    /**
     * Fills the combos in the view with the legal possibilites.
     */
    private void fillCombos()
    {
        fillComboBox( edGenre, BibliographicData.getGenresNames( getLanguage() ), 0 );
        fillComboBox( edLanguage, Util.getStrLanguages( getLanguage() ), 0 );
        fillComboBox(edForgiveness, BibliographicData.getForgivenessNames( getLanguage() ), 0 );
        fillComboBox( edFormat, BibliographicData.getFormatNames( getLanguage() ), 0 );
    }

    /**
     * Translates the view into english on-the-fly
     */
    private void translateViewIntoEnglish() {
        // Main menu
       mFile.setText( "File" );
       mFile.setMnemonic( 'f' );
       opOpen.setText( "Open" );
       opOpen.setMnemonic( 'o' );
       opSave.setText( "Save" );
       opSave.setMnemonic( 's' );
       opSaveAs.setText( "Save as..." );
       opSaveAs.setMnemonic( 'a' );
       opClose.setText( "Close" );
       opClose.setMnemonic( 'c' );
       opNew.setText( "New" );
       opNew.setMnemonic( 'c' );
       opQuit.setText( "Quit" );
       opQuit.setMnemonic( 'q' );
       mEdit.setText( "Edit" );
       mEdit.setMnemonic( 'e' );
       opGenerate.setText( "Generate new IfID" );
       opGenerate.setMnemonic( 'g' );
       opCopyIfId.setText( "Copy IfID to Clipboard" );
       opCopyIfId.setMnemonic( 'c' );
       mTools.setText( "Tools" );
       mTools.setMnemonic( 't' );
       mHelp.setText( "Help" );
       mHelp.setMnemonic( 'h' );
       opAbout.setText( "About..." );
       opAbout.setMnemonic( 'a' );

       // Labels
       lblTitle.setText( "Title" );
       lblSubtitle.setText( "Subtitle" );
       lblURL.setText( "Author's web" );
       lblIfAuthor.setText( "Author" );
       lblRelease.setText( "Release" );
       lblDate.setText( "Date" );
       lblGenre.setText( "Genre" );
       lblLanguage.setText( "Language" );
       lblForgiveness.setText( "Forgiveness" );
       lblFormat.setText( "Format" );
       
       // Border titles
       ( (TitledBorder) infoPanel.getBorder() ).setTitle( "Main" );
       this.infoPanel.repaint();
       ( (TitledBorder) formatInfoPanel.getBorder() ).setTitle( "Format" );
       this.formatInfoPanel.repaint();
       ( (TitledBorder) descPanel.getBorder() ).setTitle( "Description" );
       this.descPanel.repaint();

       // Finally
       this.fillCombos();
    }

    /**
     * Translates the view to spanish on the fly
     */
    private void translateViewIntoSpanish()
    {
       // Main menu
       mFile.setText( "Archivo" );
       mFile.setMnemonic( 'a' );
       opOpen.setText( "Abrir" );
       opOpen.setMnemonic( 'a' );
       opSave.setText( "Guardar" );
       opSave.setMnemonic( 'g' );
       opSaveAs.setText( "Guardar como..." );
       opSaveAs.setMnemonic( 'o' );
       opClose.setText( "Cerrar" );
       opClose.setMnemonic( 'c' );
       opNew.setText( "Nuevo" );
       opNew.setMnemonic( 'c' );
       opQuit.setText( "Salir" );
       opQuit.setMnemonic( 's' );
       mEdit.setText( "Editar" );
       mEdit.setMnemonic( 'e' );
       opGenerate.setText( "Generar nuevo IfID" );
       opGenerate.setMnemonic( 'g' );
       opCopyIfId.setText( "Copiar IfID al portapapeles" );
       opCopyIfId.setMnemonic( 'c' );
       mTools.setText( "Herramientas" );
       mTools.setMnemonic( 'h' );
       mHelp.setText( "Ayuda" );
       mHelp.setMnemonic( 'y' );
       opAbout.setText( "Acerca de..." );
       opAbout.setMnemonic( 'a' );

       // Labels
       lblTitle.setText( "Título" );
       lblSubtitle.setText( "Subtítulo" );
       lblURL.setText( "Web del autor" );
       lblIfAuthor.setText( "Autor" );
       lblRelease.setText( "Versión" );
       lblDate.setText( "Fecha" );
       lblGenre.setText( "Género" );
       lblLanguage.setText( "Idioma" );
       lblForgiveness.setText( "Permisividad" );
       lblFormat.setText( "Formato" );
       
       // Border titles
       ( (TitledBorder) infoPanel.getBorder() ).setTitle( "Principal" );
       this.infoPanel.repaint();
       ( (TitledBorder) formatInfoPanel.getBorder() ).setTitle( "Formato" );
       this.formatInfoPanel.repaint();
       ( (TitledBorder) descPanel.getBorder() ).setTitle( "Descripción" );
       this.descPanel.repaint();

       // Finally
       this.fillCombos();
    }

    void syncUIToProject() throws MalformedURLException, ParseException
    {
        if ( prj == null ) {
            Util.msgError( this, this.getNoProjectMsg() );
            return;
        }

        BibliographicData toret = prj.getBiblio();

        // Basic attributes
        toret.setTitle( edTitle.getText() );
        toret.setSubtitle( edSubtitle.getText() );
        toret.setRelease( this.getReleaseValue() );
        toret.setAuthor( edAuthor.getText() );
        toret.setEMail( edEmail.getText() );
        toret.setDesc( edDesc.getText() );
        toret.setForgiveness(
                BibliographicData.Forgiveness.values()[edForgiveness.getSelectedIndex()]
        );
        toret.setFormat(
                BibliographicData.Format.values()[ edFormat.getSelectedIndex() ]
        );
        toret.setGenre(
                BibliographicData.Genre.values()[ edGenre.getSelectedIndex() ]
        );
        toret.setLanguage(
                Util.Language.values()[ edLanguage.getSelectedIndex() ]
        );

        // URL
        if ( !edUrl.getText().trim().isEmpty() ) {
            toret.setUrl( new URL( edUrl.getText() ) );
        }

        // Date
        DateFormat formatter = new SimpleDateFormat( BibliographicData.FmtDate );
        Date date = formatter.parse( edDate.getText() );
        Calendar cal = Calendar.getInstance();
        cal.setTime( date );
        toret.setDate( cal );
    }

    public String getNoProjectMsg()
    {
        return BabbleView.getNoProjectMsg( this.getLanguage() );
    }

    public static String getNoProjectMsg(Util.Language lang) {
        int op = 0;
        if ( lang == Util.Language.EN ) {
            op = 1;
        }

        return NoProject[ op ];
    }

    public String getAreYouSureMsg()
    {
        return BabbleView.getAreYouSureMsg( this.getLanguage() );
    }

    public static String getAreYouSureMsg(Util.Language lang) {
        int op = 0;
        if ( lang == Util.Language.EN ) {
            op = 1;
        }

        return AreYouSure[ op ];
    }

    private Project prj = null;
    private Util.Language language = Util.Language.ES;

    private javax.swing.JDialog aboutBox;
    private javax.swing.JPanel formatInfoPanel;
    private javax.swing.JPanel descPanel;
    private javax.swing.JPanel idPanel;
    private javax.swing.JPanel infoPanel;
    private javax.swing.JTextField edAuthor;
    private javax.swing.JTextField edDate;
    private javax.swing.JTextArea edDesc;
    private javax.swing.JTextField edEmail;
    private javax.swing.JComboBox edForgiveness;
    private javax.swing.JComboBox edFormat;
    private javax.swing.JComboBox edGenre;
    private javax.swing.JComboBox edLanguage;
    private javax.swing.JSpinner edVersion;
    private javax.swing.JTextField edSubtitle;
    private javax.swing.JTextField edTitle;
    private javax.swing.JTextField edUrl;
    private javax.swing.JLabel lblDate;
    private javax.swing.JLabel lblEmail;
    private javax.swing.JLabel lblForgiveness;
    private javax.swing.JLabel lblFormat;
    private javax.swing.JLabel lblGenre;
    private javax.swing.JLabel lblIfAuthor;
    private javax.swing.JLabel lblLanguage;
    private javax.swing.JLabel lblRelease;
    private javax.swing.JLabel lblSubtitle;
    private javax.swing.JLabel lblTitle;
    private javax.swing.JLabel lblURL;
    private javax.swing.JLabel lblIfId;
    private javax.swing.JMenuBar mMainMenu;
    private javax.swing.JMenu mEdit;
    private javax.swing.JMenu mFile;
    private javax.swing.JMenu mHelp;
    private javax.swing.JMenu mTools;
    private javax.swing.JMenuItem opAbout;
    private javax.swing.JMenuItem opClose;
    private javax.swing.JMenuItem opCopyIfId;
    private javax.swing.JMenuItem opGenerate;
    private javax.swing.JCheckBoxMenuItem opLanguageEN;
    private javax.swing.JCheckBoxMenuItem opLanguageES;
    private javax.swing.JMenuItem opNew;
    private javax.swing.JMenuItem opOpen;
    private javax.swing.JMenuItem opQuit;
    private javax.swing.JMenuItem opSave;
    private javax.swing.JMenuItem opSaveAs;
}
