package view;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
	
//############################################################################

public class MainAppFrameUI {

	// --------------------------------------------------
	
    private static final Logger log = LoggerFactory.getLogger( MainAppFrameUI.class );
	
	final String[] SIDEPORT_DEVICE_NAMES = {
		"------",
		"peb",
		"speechsyn",
		"splitter",
		"arcturus"
	};
	
	final String[] PEB1_DEVICE_NAMES = {
		"------",
		"not connected",
		"TI99 console",
		"Geneve card"
	};

	final String[] PEB_DEVICE_NAMES = {
		"------",
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
		"------",
		"TI99_4", 
		"TI99_4e", 
		"TI99_4a", 
		"TI99_4ae", 
		"TI99_4ev", 
		"TI99_4p", "TI99_4qi", 
		"TI99_8", 
		"TI99_8e", 
		"geneve", 
		"genmod"
	};
	
	final String[] GROM_PORT_NAMES = {
		"------", 
		"single", 
		"multi", 
		"gkracker"
	};
	
	final String[] CARTRIDGE_NAMES = {
		"------" /*, 
		"ExBasic", 
		"MiniMem", 
		"EditAss" */
	};
	
	final String[] FLOPPYDISKIMAGE_NAMES = {
			"------" /*, 
			"ExBasic", 
			"MiniMem", 
			"EditAss" */
		};
		
	final String[] HARDDISKIMAGE_NAMES = {
			"------" /*, 
			"ExBasic", 
			"MiniMem", 
			"EditAss" */
		};
		
	final String[] CASSETTEIMAGE_NAMES = {
			"------" /*, 
			"ExBasic", 
			"MiniMem", 
			"EditAss" */
		};
		
	final String[] JOYSTICK_PORT_NAMES = {
		"------", 
		"twinjoy"	
	};
	
	final String[] BWG_FDD_NAMES = {
		"------", 
		"35dd",
		"525dd",
		"525qd"
	};
	final String[] CCDCC_FDD_NAMES = BWG_FDD_NAMES;
	final String[] CCFDC_FDD_NAMES = BWG_FDD_NAMES;
	final String[] DDCC1_FDD_NAMES = BWG_FDD_NAMES;

	final String[] TIFDC_FDD_NAMES = {
		"------", 
		"525dd"
	};

	final String[] HFDC_FDD_NAMES = {
		"------", 
		"35dd",
		"35hd",
		"525dd",
		"525qd"
	};

	final String[] HFDC_HDD_NAMES = {
		"------",
		"generic",
		"st213",
		"st225",
		"st251"
	};

	final String[] EVPC_DEVICE_NAMES = {
		"------", 
		"busmouse"
	};

	final String[] IDE_DEVICE_NAMES = {
		"------", 
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
		"------", 
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
    JComboBox<String> cbxCartridge;
    
    JLabel lblJoystick;
    JComboBox<String> cbxJoystick;

    JLabel lblIoPort;
    JComboBox<String> cbxIoPort;

    JLabel lblAddOpt;
    JTextField txtAddOpt;

    JLabel lblFddPath;
    JTextField txtFddPath;

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
        
    }

    public MainAppFrameUI() {
		
		log.debug( "[constructor] MainAppFrameUI()" );
		
	    contentPane = new JPanel();
        contentPane.setBorder( new EmptyBorder( 5, 5, 5, 5 ));
        contentPane.setLayout( null );

	    lblEmulator = new JLabel( "Emulator" );
	    txtEmulator = new JTextField();
	    
	    lblWorkingDir = new JLabel( "Working dir" );
	    txtWorkingDir = new JTextField();
	    
	    lblRomPath = new JLabel( "ROM path" );
	    txtRomPath = new JTextField();
	    
	    lblCartPath = new JLabel( "Cart. path" );
	    txtCartPath = new JTextField();

	    lblMachine = new JLabel( "Machine" );
	    cbxMachine = new JComboBox<>();
	    
	    lblGromPort = new JLabel( "GROM port" );
	    cbxGromPort = new JComboBox<>();
	    cbxCartridge = new JComboBox<>();
	    
	    lblJoystick = new JLabel( "Joystick" );
	    cbxJoystick = new JComboBox<>();
	    
	    lblIoPort = new JLabel( "I/O port" );
	    cbxIoPort = new JComboBox<>();

	    lblAddOpt = new JLabel( "add. opt." );
	    txtAddOpt = new JTextField();

	    lblFddPath = new JLabel( "DSKx path" );
	    txtFddPath = new JTextField();

	    // lblFlop = new JLabel( "DSKx Media" );
	    lblFlop1 = new JLabel( "1" );
	    // txtFlop1 = new JTextField();
	    cbxFlop1 = new JComboBox<>();
	    lblFlop2 = new JLabel( "2" );
	    // txtFlop2 = new JTextField();
	    cbxFlop2 = new JComboBox<>();
	    lblFlop3 = new JLabel( "3" );
	    // txtFlop3 = new JTextField();
	    cbxFlop3 = new JComboBox<>();
	    lblFlop4 = new JLabel( "4" );
	    // txtFlop4 = new JTextField();
	    cbxFlop4 = new JComboBox<>();

	    lblHddPath = new JLabel( "WDSx path" );
	    txtHddPath = new JTextField();

	    // lblHard = new JLabel( "WDSx Media" );
	    lblHard1 = new JLabel( "1" );
	    // txtHard1 = new JTextField();
	    cbxHard1 = new JComboBox<>();
	    lblHard2 = new JLabel( "2" );
	    // txtHard2 = new JTextField();
	    cbxHard2 = new JComboBox<>();
	    lblHard3 = new JLabel( "3" );
	    // txtHard3 = new JTextField();
	    cbxHard3 = new JComboBox<>();

	    lblCassPath = new JLabel( "CSx path" );
	    txtCassPath = new JTextField();

	    // lblCass = new JLabel( "CSx Media" );
	    lblCass1 = new JLabel( "1" );
	    // txtCass1 = new JTextField();
	    cbxCass1 = new JComboBox<>();
	    lblCass2 = new JLabel( "2" );
	    // txtCass2 = new JTextField();
	    cbxCass2 = new JComboBox<>();

	    dbgEmulatorOptions = new JTextArea();
	    btnStartEmulator = new JButton( "start" );

	    // -----
	    
	    tabSideportDevices = new JTabbedPane( JTabbedPane.TOP );

	    Panel_PEB = new JPanel();
	    tabPebDevices = new JTabbedPane( JTabbedPane.TOP );

	    Panel_PEB_PEB = new JPanel();
	    lblPeb = new JLabel( "peb" );
	    lblSlot2 = new JLabel( "2" );
	    cbxSlot2 = new JComboBox<>();
	    lblSlot3 = new JLabel( "3" );
	    cbxSlot3 = new JComboBox<>();
	    lblSlot4 = new JLabel( "4" );
	    cbxSlot4 = new JComboBox<>();
	    lblSlot5 = new JLabel( "5" );
	    cbxSlot5 = new JComboBox<>();
	    lblSlot6 = new JLabel( "6" );
	    cbxSlot6 = new JComboBox<>();
	    lblSlot7 = new JLabel( "7" );
	    cbxSlot7 = new JComboBox<>();
	    lblSlot8 = new JLabel( "8" );
	    cbxSlot8 = new JComboBox<>();

	    Panel_PEB_32kMem = new JPanel();
	    lbl32kMem = new JLabel( "32kmem" );

	    Panel_PEB_Bwg = new JPanel();
	    lblBwg = new JLabel( "bwg" );
	    lblBwg0 = new JLabel( "0" );
	    cbxBwg0 = new JComboBox<>();
	    lblBwg1 = new JLabel( "1" );
	    cbxBwg1 = new JComboBox<>();
	    lblBwg2 = new JLabel( "2" );
	    cbxBwg2 = new JComboBox<>();
	    lblBwg3 = new JLabel( "3" );
	    cbxBwg3 = new JComboBox<>();

	    Panel_PEB_CcDcc = new JPanel();
	    lblCcdcc = new JLabel( "ccdcc" );
	    lblCcdcc0 = new JLabel( "0" );
	    cbxCcdcc0 = new JComboBox<>();
	    lblCcdcc1 = new JLabel( "1" );
	    cbxCcdcc1 = new JComboBox<>();
	    lblCcdcc2 = new JLabel( "2" );
	    cbxCcdcc2 = new JComboBox<>();
	    lblCcdcc3 = new JLabel( "3" );
	    cbxCcdcc3 = new JComboBox<>();

	    Panel_PEB_CcFdc = new JPanel();
	    lblCcfdc = new JLabel( "ccfdc" );
	    lblCcfdc0 = new JLabel( "0" );
	    cbxCcfdc0 = new JComboBox<>();
	    lblCcfdc1 = new JLabel( "1" );
	    cbxCcfdc1 = new JComboBox<>();
	    lblCcfdc2 = new JLabel( "2" );
	    cbxCcfdc2 = new JComboBox<>();
	    lblCcfdc3 = new JLabel( "3" );
	    cbxCcfdc3 = new JComboBox<>();

	    Panel_PEB_DDcc1 = new JPanel();
	    lblDdcc = new JLabel( "ddcc1" );
	    lblDdcc0 = new JLabel( "0" );
	    cbxDdcc0 = new JComboBox<>();
	    lblDdcc1 = new JLabel( "1" );
	    cbxDdcc1 = new JComboBox<>();
	    lblDdcc2 = new JLabel( "2" );
	    cbxDdcc2 = new JComboBox<>();
	    lblDdcc3 = new JLabel( "3" );
	    cbxDdcc3 = new JComboBox<>();

	    Panel_PEB_Evpc = new JPanel();
	    lblEvpc = new JLabel( "evpc" );
	    lblColorbus = new JLabel( "colorbus" );
	    cbxColorbus = new JComboBox<>();

	    Panel_PEB_Forti = new JPanel();
	    lblForti = new JLabel( "forti" );

	    Panel_PEB_HFdc = new JPanel();
	    lblHfdc = new JLabel( "hfdc" );
	    lblHfdcF1 = new JLabel( "F1" );
	    cbxHfdcF1 = new JComboBox<>();
	    lblHfdcF2 = new JLabel( "F2" );
	    cbxHfdcF2 = new JComboBox<>();
	    lblHfdcF3 = new JLabel( "F3" );
	    cbxHfdcF3 = new JComboBox<>();
	    lblHfdcF4 = new JLabel( "F4" );
	    cbxHfdcF4 = new JComboBox<>();
	    lblHfdcH1 = new JLabel( "H1" );
	    cbxHfdcH1 = new JComboBox<>();
	    lblHfdcH2 = new JLabel( "H2" );
	    cbxHfdcH2 = new JComboBox<>();
	    lblHfdcH3 = new JLabel( "H3" );
	    cbxHfdcH3 = new JComboBox<>();

	    Panel_PEB_Horizon = new JPanel();
	    lblHorizon = new JLabel( "horizon" );

	    Panel_PEB_HsGpl = new JPanel();
	    lblHsgpl = new JLabel( "hsgpl" );

	    Panel_PEB_Ide = new JPanel();
	    lblIde = new JLabel( "ide" );
	    lblAta0 = new JLabel( "0" );
	    cbxAta0 = new JComboBox<>();
	    lblAta1 = new JLabel( "1" );
	    cbxAta1 = new JComboBox<>();

	    Panel_PEB_MyarcMem = new JPanel();
	    lblMyarcmem = new JLabel( "myarcmem" );

	    Panel_PEB_PCode = new JPanel();
	    lblPcode = new JLabel( "pcode" );

	    Panel_PEB_PGram = new JPanel();
	    lblPgram = new JLabel( "pgram" );

	    Panel_PEB_SamsMem = new JPanel();
	    lblSamsmem = new JLabel( "samsmem" );

	    Panel_PEB_SidMaster = new JPanel();
	    lblSidmaster = new JLabel( "sidmaster" );

	    Panel_PEB_SpeechAdapter = new JPanel();
	    lblSpeechadaptger = new JLabel( "speechadapter" );

	    Panel_PEB_TiFdc = new JPanel();
	    lblTifdc = new JLabel( "tifdc" );
	    lblTifdc0 = new JLabel( "0" );
	    cbxTifdc0 = new JComboBox<>();
	    lblTifdc1 = new JLabel( "1" );
	    cbxTifdc1 = new JComboBox<>();
	    lblTifdc2 = new JLabel( "2" );
	    cbxTifdc2 = new JComboBox<>();

	    Panel_PEB_TiPi = new JPanel();
	    lblTipi = new JLabel( "tipi" );

	    Panel_PEB_TiRs232 = new JPanel();
	    lblTirs232 = new JLabel( "tirs232" );

	    Panel_PEB_UsbSm = new JPanel();
	    lblUsbsm = new JLabel( "usbsm" );

	    Panel_PEB_WhtScsi = new JPanel();
	    lblWhtscsi = new JLabel( "whtscsi" );
	    lblScsibus0 = new JLabel( "0" );
	    cbxScsibus0 = new JComboBox<>();
	    txtScsibus0 = new JTextField();
	    lblScsibus1 = new JLabel( "1" );
	    cbxScsibus1 = new JComboBox<>();
	    txtScsibus1 = new JTextField();
	    lblScsibus2 = new JLabel( "2" );
	    cbxScsibus2 = new JComboBox<>();
	    txtScsibus2 = new JTextField();
	    lblScsibus3 = new JLabel( "3" );
	    cbxScsibus3 = new JComboBox<>();
	    txtScsibus3 = new JTextField();
	    lblScsibus4 = new JLabel( "4" );
	    cbxScsibus4 = new JComboBox<>();
	    txtScsibus4 = new JTextField();
	    lblScsibus5 = new JLabel( "5" );
	    cbxScsibus5 = new JComboBox<>();
	    txtScsibus5 = new JTextField();
	    lblScsibus6 = new JLabel( "6" );
	    cbxScsibus6 = new JComboBox<>();
	    txtScsibus6 = new JTextField();
	    lblScsibus7 = new JLabel( "7" );
	    cbxScsibus7 = new JComboBox<>();
	    txtScsibus7 = new JTextField();

	    // Sideport Panels
	    Panel_SpeechSyn = new JPanel();
	    
	    Panel_Splitter = new JPanel();
	    
	    Panel_Arcturus = new JPanel();
	    
	} // [constructor] MainAppFrameUI()

	// --------------------------------------------------
	
} // class MainAppFrameUI

//############################################################################
