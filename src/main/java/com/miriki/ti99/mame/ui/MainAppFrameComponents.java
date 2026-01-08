package com.miriki.ti99.mame.ui;

import java.lang.reflect.Field;

import javax.swing.JButton;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

//############################################################################

// --------------------------------------------------

public class MainAppFrameComponents {
	
    // --------------------------------------------------
    
    private static final Logger log = LoggerFactory.getLogger( MainAppFrameComponents.class );
	
    // --------------------------------------------------
    
	public JMenu menuFile;
	public JMenuItem menuFileExit;

	public JMenu menuLang;
	public JCheckBoxMenuItem menuLangEnglishGB;
	public JCheckBoxMenuItem menuLangEnglishUS;
	public JCheckBoxMenuItem menuLangEnglishAU;
	public JCheckBoxMenuItem menuLangGermanDE;
	public JCheckBoxMenuItem menuLangGermanAT;
	public JCheckBoxMenuItem menuLangGermanCH;
	public JCheckBoxMenuItem menuLangFrenchFR;
	public JCheckBoxMenuItem menuLangItalianIT;

	public JMenu menuSettings;
	public JMenuItem menuSettingsSave;
	public JMenuItem menuSettingsSaveAs;
	public JMenuItem menuSettingsLoad;
	public JMenuItem menuSettingsPick;
	public JMenuItem menuSettingsPickByCount;
	public JMenuItem menuSettingsPickByDate;
	public JMenuItem menuSettingsNoSel;
	public JMenuItem menuSettingsPickLoad;
	public JMenuItem menuSettingsPickDelete;
	public JMenuItem menuSettingsPickRename;
	
	public JMenu menuHelp;
	public JMenuItem menuHelpAbout;

    // --------------------------------------------------
    
	final String[] SIDEPORT_DEVICE_NAMES = {
			UiConstants.CBX_SEL_NONE,
		"peb",
		"speechsyn",
		"splitter",
		"arcturus"
	};
	
	final String[] PEB1_DEVICE_NAMES = {
			UiConstants.CBX_SEL_NONE,
		"not connected",
		"TI99 console",
		"Geneve card"
	};

	final String[] PEB_DEVICE_NAMES = {
			UiConstants.CBX_SEL_NONE,
		"32kmem",
		"bwg",
		"ccdcc",
		"ccfdc",
		"ddcc1",
		"evpc",
		"forti",
		"hfdc",
		"horizon",
		"hsgpl",
		"ide",
		"myarcmem",
		"pcode",
		"pgram",
		"samsmem",
		"sidmaster",
		"speechadapter",
		"tifdc",
		"tipi",
		"tirs232",
		"usbsm",
		"whtscsi"
	};
	
	final String[] MACHINE_NAMES = {
			UiConstants.CBX_SEL_NONE,
		"TI99_4", 
		"TI99_4e", 
		"TI99_4a", 
		"TI99_4ae", 
		"TI99_4ev", 
		"TI99_4p", 
		"TI99_4qi", 
		"TI99_8", 
		"TI99_8e", 
		"geneve", 
		"genmod"
	};
	
	final String[] GROM_PORT_NAMES = {
			UiConstants.CBX_SEL_NONE, 
		"single", 
		"multi", 
		"gkracker"
	};
	
	// public static final CartridgeEntry CARTRIDGE_NONE = CartridgeEntry.none();
	
	final String[] CARTRIDGE_NAMES = {
			UiConstants.CBX_SEL_NONE
		/* UiConstants.CBX_SEL_NONE,
		"ExBasic", 
		"MiniMem", 
		"EditAss" */
	};
	
	final String[] FLOPPYDISKIMAGE_NAMES = {
			UiConstants.CBX_SEL_NONE /*, 
			"ExBasic", 
			"MiniMem", 
			"EditAss" */
		};
		
	final String[] HARDDISKIMAGE_NAMES = {
			UiConstants.CBX_SEL_NONE /*, 
			"ExBasic", 
			"MiniMem", 
			"EditAss" */
		};
		
	final String[] CASSETTEIMAGE_NAMES = {
			UiConstants.CBX_SEL_NONE /*, 
			"ExBasic", 
			"MiniMem", 
			"EditAss" */
		};
		
	final String[] JOYSTICK_PORT_NAMES = {
			UiConstants.CBX_SEL_NONE, 
		"twinjoy"	
	};
	
	final String[] BWG_FDD_NAMES = {
			UiConstants.CBX_SEL_NONE, 
		"35dd",
		"525dd",
		"525qd"
	};
	final String[] CCDCC_FDD_NAMES = BWG_FDD_NAMES;
	final String[] CCFDC_FDD_NAMES = BWG_FDD_NAMES;
	final String[] DDCC1_FDD_NAMES = BWG_FDD_NAMES;

	final String[] TIFDC_FDD_NAMES = {
			UiConstants.CBX_SEL_NONE, 
		"525dd"
	};

	final String[] HFDC_FDD_NAMES = {
			UiConstants.CBX_SEL_NONE, 
		"35dd",
		"35hd",
		"525dd",
		"525qd"
	};

	final String[] HFDC_HDD_NAMES = {
			UiConstants.CBX_SEL_NONE,
		"generic",
		"st213",
		"st225",
		"st251"
	};

	final String[] EVPC_DEVICE_NAMES = {
			UiConstants.CBX_SEL_NONE, 
		"busmouse"
	};

	final String[] IDE_DEVICE_NAMES = {
			UiConstants.CBX_SEL_NONE, 
		"cdrom",
		"cf",
		"cp2024",
		"cr589",
		"hdd",
		"px320a",
		"xm3301",
		"zip100"
	};

	final String[] SCSI_DEVICE_NAMES = {
			UiConstants.CBX_SEL_NONE, 
		"aplcd150",
		"aplcdsc",
		"cdd2000",
		"cdr4210",
		"cdrn820s",
		"cdrom",
		"cdu415",
		"cdu561_25",
		"cdu75s",
		"cfp1080s",
		"crd254sh",
		"cw7501",
		"harddisk",
		"s1410",
		"smoc501",
		"tape"
	};

    // ----- main content pane ---------------------------------------------

	JPanel contentPane;

    JLabel lblEmulator;
    JTextField txtEmulator;

    JLabel lblWorkingDir;
    JTextField txtWorkingDir;
    
    JLabel lblRomPath;
    JTextField txtRomPath;
    
    JLabel lblCartPath;
    JTextField txtCartPath;

    JLabel lblMachine;
    JComboBox<String> cbxMachine;
    
    JLabel lblGromPort;
    JComboBox<String> cbxGromPort;
    // JComboBox<String> cbxCartridge;
    JComboBox<String> cbxCartridge;
    
    JLabel lblJoystick;
    JComboBox<String> cbxJoystick;

    JLabel lblIoPort;
    JComboBox<String> cbxIoPort;

    JLabel lblAddOpt;
    JTextField txtAddOpt;

    JLabel lblFddPath;
    JTextField txtFddPath;
    JLabel lblFiadPath;
    JTextField txtFiadPath;

    JLabel lblFlop;
    JLabel lblFlop1;
    // JTextField txtFlop1;
    JComboBox<String> cbxFlop1;
    JLabel lblFlop2;
    // JTextField txtFlop2;
    JComboBox<String> cbxFlop2;
    JLabel lblFlop3;
    // JTextField txtFlop3;
    JComboBox<String> cbxFlop3;
    JLabel lblFlop4;
    // JTextField txtFlop4;
    JComboBox<String> cbxFlop4;

    JLabel lblHddPath;
    JTextField txtHddPath;

    JLabel lblHard;
    JLabel lblHard1;
    // JTextField txtHard1;
    JComboBox<String> cbxHard1;
    JLabel lblHard2;
    // JTextField txtHard2;
    JComboBox<String> cbxHard2;
    JLabel lblHard3;
    // JTextField txtHard3;
    JComboBox<String> cbxHard3;

    JLabel lblCassPath;
    JTextField txtCassPath;

    JLabel lblCass;
    JLabel lblCass1;
    // JTextField txtCass1;
    JComboBox<String> cbxCass1;
    JLabel lblCass2;
    // JTextField txtCass2;
    JComboBox<String> cbxCass2;

    JTextArea dbgEmulatorOptions;

    JButton btnStartEmulator;

    JButton btnTestFiad;
    
    // ----- tabbed: sideport devices ---------------------------------------------
    
    JTabbedPane tabSideportDevices;

    // ----- sideport: peb -----
    
    JPanel Panel_PEB;
    JTabbedPane tabPebDevices;

    // ----- peb: main (slot selections) -----
    
    JPanel Panel_PEB_PEB;
    JLabel lblPeb;

    JLabel lblSlot1;
    JComboBox<String> cbxSlot1;
    JLabel lblSlot2;
    JComboBox<String> cbxSlot2;
    JLabel lblSlot3;
    JComboBox<String> cbxSlot3;
    JLabel lblSlot4;
    JComboBox<String> cbxSlot4;
    JLabel lblSlot5;
    JComboBox<String> cbxSlot5;
    JLabel lblSlot6;
    JComboBox<String> cbxSlot6;
    JLabel lblSlot7;
    JComboBox<String> cbxSlot7;
    JLabel lblSlot8;
    JComboBox<String> cbxSlot8;

    // ----- peb: 32kmem -----
    
    JPanel Panel_PEB_32kMem;
    JLabel lbl32kMem;

    // ----- peb: bwg -----
    
    JPanel Panel_PEB_Bwg;
    JLabel lblBwg;
    
    JLabel lblBwg0;
    JComboBox<String> cbxBwg0;
    JLabel lblBwg1;
    JComboBox<String> cbxBwg1;
    JLabel lblBwg2;
    JComboBox<String> cbxBwg2;
    JLabel lblBwg3;
    JComboBox<String> cbxBwg3;

    // ----- peb: ccdcc -----
    
    JPanel Panel_PEB_CcDcc;
    JLabel lblCcdcc;
    
    JLabel lblCcdcc0;
    JComboBox<String> cbxCcdcc0;
    JLabel lblCcdcc1;
    JComboBox<String> cbxCcdcc1;
    JLabel lblCcdcc2;
    JComboBox<String> cbxCcdcc2;
    JLabel lblCcdcc3;
    JComboBox<String> cbxCcdcc3;

    // ----- peb: ccfdc -----
    
    JPanel Panel_PEB_CcFdc;
    JLabel lblCcfdc;
    
    JLabel lblCcfdc0;
    JComboBox<String> cbxCcfdc0;
    JLabel lblCcfdc1;
    JComboBox<String> cbxCcfdc1;
    JLabel lblCcfdc2;
    JComboBox<String> cbxCcfdc2;
    JLabel lblCcfdc3;
    JComboBox<String> cbxCcfdc3;

    // ----- peb: ddcc1 -----
    
    JPanel Panel_PEB_DDcc1;
    JLabel lblDdcc;
    
    JLabel lblDdcc0;
    JComboBox<String> cbxDdcc0;
    JLabel lblDdcc1;
    JComboBox<String> cbxDdcc1;
    JLabel lblDdcc2;
    JComboBox<String> cbxDdcc2;
    JLabel lblDdcc3;
    JComboBox<String> cbxDdcc3;

    // ----- peb: evpc -----
    
    JPanel Panel_PEB_Evpc;
    JLabel lblEvpc;
    
    JLabel lblColorbus;
    JComboBox<String> cbxColorbus;

    // ----- peb: forti -----
    
    JPanel Panel_PEB_Forti;
    JLabel lblForti;

    // ----- peb: hfdc -----
    
    JPanel Panel_PEB_HFdc;
    JLabel lblHfdc;
    
    JLabel lblHfdcF1;
    JComboBox<String> cbxHfdcF1;
    JLabel lblHfdcF2;
    JComboBox<String> cbxHfdcF2;
    JLabel lblHfdcF3;
    JComboBox<String> cbxHfdcF3;
    JLabel lblHfdcF4;
    JComboBox<String> cbxHfdcF4;
    
    JLabel lblHfdcH1;
    JComboBox<String> cbxHfdcH1;
    JLabel lblHfdcH2;
    JComboBox<String> cbxHfdcH2;
    JLabel lblHfdcH3;
    JComboBox<String> cbxHfdcH3;

    // ----- peb: horizon -----
    
    JPanel Panel_PEB_Horizon;
    JLabel lblHorizon;

    // ----- peb: hsgpl -----
    
    JPanel Panel_PEB_HsGpl;
    JLabel lblHsgpl;

    // ----- peb: ide -----
    
    JPanel Panel_PEB_Ide;
    JLabel lblIde;
    
    JLabel lblAta0;
    JComboBox<String> cbxAta0;
    JLabel lblAta1;
    JComboBox<String> cbxAta1;

    // ----- peb: myarcmem -----
    
    JPanel Panel_PEB_MyarcMem;
    JLabel lblMyarcmem;

    // ----- peb: pcode -----
    
    JPanel Panel_PEB_PCode;
    JLabel lblPcode;

    // ----- peb: pgram -----
    
    JPanel Panel_PEB_PGram;
    JLabel lblPgram;

    // ----- peb: samsmem -----
    
    JPanel Panel_PEB_SamsMem;
    JLabel lblSamsmem;

    // ----- peb: sidmaster -----
    
    JPanel Panel_PEB_SidMaster;
    JLabel lblSidmaster;

    // ----- peb: speechadapter -----
    
    JPanel Panel_PEB_SpeechAdapter;
    JLabel lblSpeechadaptger;

    // ----- peb: tifdc -----
    
    JPanel Panel_PEB_TiFdc;
    JLabel lblTifdc;
    
    JLabel lblTifdc0;
    JComboBox<String> cbxTifdc0;
    JLabel lblTifdc1;
    JComboBox<String> cbxTifdc1;
    JLabel lblTifdc2;
    JComboBox<String> cbxTifdc2;

    // ----- peb: tipi -----
    
    JPanel Panel_PEB_TiPi;
    JLabel lblTipi;

    // ----- peb: tirs232 -----
    
    JPanel Panel_PEB_TiRs232;
    JLabel lblTirs232;

    // ----- peb: usbsm -----
    
    JPanel Panel_PEB_UsbSm;
    JLabel lblUsbsm;

    // ----- peb: whtscsi -----
    
    JPanel Panel_PEB_WhtScsi;
    JLabel lblWhtscsi;
    
    JLabel lblScsibus0;
    JComboBox<String> cbxScsibus0;
    JTextField txtScsibus0;
    JLabel lblScsibus1;
    JComboBox<String> cbxScsibus1;
    JTextField txtScsibus1;
    JLabel lblScsibus2;
    JComboBox<String> cbxScsibus2;
    JTextField txtScsibus2;
    JLabel lblScsibus3;
    JComboBox<String> cbxScsibus3;
    JTextField txtScsibus3;
    JLabel lblScsibus4;
    JComboBox<String> cbxScsibus4;
    JTextField txtScsibus4;
    JLabel lblScsibus5;
    JComboBox<String> cbxScsibus5;
    JTextField txtScsibus5;
    JLabel lblScsibus6;
    JComboBox<String> cbxScsibus6;
    JTextField txtScsibus6;
    JLabel lblScsibus7;
    JComboBox<String> cbxScsibus7;
    JTextField txtScsibus7;

    // ----- sideport: speechsyn -----
    
    JPanel Panel_SpeechSyn;

    // ----- sideport: splitter -----
    
    JPanel Panel_Splitter;

    // ----- sideport: arcturus -----
    
    JPanel Panel_Arcturus;

	// --------------------------------------------------
	
    public JPanel getContentPane() {
    	
        return contentPane;
        
    } // getContentPane

    // --------------------------------------------------
    
    public void bind(String fieldName, JComponent comp) {
    	
        try {
            Field f = this.getClass().getDeclaredField( fieldName );
            f.setAccessible( true );
            f.set( this, comp );
            
        } catch ( Exception e ) {
            throw new RuntimeException( "Failed to bind UI component: " + fieldName, e );
            
        }
        
    } // bind
    
    // --------------------------------------------------
    
    public MainAppFrameComponents() {
		
		log.debug( "----- start: [constructor] MainAppFrameUI()" );

		// Main Content
		
	    contentPane = new JPanel();
        contentPane.setBorder( new EmptyBorder( 5, 5, 5, 5 ));
        contentPane.setLayout( null );

	    dbgEmulatorOptions = new JTextArea();
	    btnStartEmulator = new JButton( "start" );
	    btnTestFiad = new JButton( "test fiad" );

	    // Sideport Devices
	    
	    tabSideportDevices = new JTabbedPane( JTabbedPane.TOP );

	    // Sideport: PEB
	    
	    Panel_PEB = new JPanel();

	    // PEB Devices
	    
	    tabPebDevices = new JTabbedPane( JTabbedPane.TOP );

	    Panel_PEB_PEB = new JPanel();
	    Panel_PEB_32kMem = new JPanel();
	    Panel_PEB_Bwg = new JPanel();
	    Panel_PEB_CcDcc = new JPanel();
	    Panel_PEB_CcFdc = new JPanel();
	    Panel_PEB_DDcc1 = new JPanel();
	    Panel_PEB_Evpc = new JPanel();
	    Panel_PEB_Forti = new JPanel();
	    Panel_PEB_HFdc = new JPanel();
	    Panel_PEB_Horizon = new JPanel();
	    Panel_PEB_HsGpl = new JPanel();
	    Panel_PEB_Ide = new JPanel();
	    Panel_PEB_MyarcMem = new JPanel();
	    Panel_PEB_PCode = new JPanel();
	    Panel_PEB_PGram = new JPanel();
	    Panel_PEB_SamsMem = new JPanel();
	    Panel_PEB_SidMaster = new JPanel();
	    Panel_PEB_SpeechAdapter = new JPanel();
	    Panel_PEB_TiFdc = new JPanel();
	    Panel_PEB_TiPi = new JPanel();
	    Panel_PEB_TiRs232 = new JPanel();
	    Panel_PEB_UsbSm = new JPanel();
	    Panel_PEB_WhtScsi = new JPanel();

	    // Sideport: Speech

	    Panel_SpeechSyn = new JPanel();
	    
	    // Sideport: Splitter

	    Panel_Splitter = new JPanel();
	    
	    // Sideport: Arcturus

	    Panel_Arcturus = new JPanel();
	    	    
		log.debug( "----- end: [constructor] MainAppFrameUI()" );
		
	} // [constructor] MainAppFrameComponents

	// --------------------------------------------------
	
} // class MainAppFrameUI

//############################################################################
