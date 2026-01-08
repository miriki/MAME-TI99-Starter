package com.miriki.ti99.mame.ui;

import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Font;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.FocusListener;

import javax.swing.ComboBoxModel;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JMenuBar;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.miriki.ti99.mame.localization.I18n;
import com.miriki.ti99.mame.tools.IconLoader;
import com.miriki.ti99.mame.ui.debug.GridOverlayPanel;
import com.miriki.ti99.mame.ui.mamedevices.PebDevices;
import com.miriki.ti99.mame.ui.mamedevices.PebDevicesController;
import com.miriki.ti99.mame.ui.menu.MenuBarBuilder;
import com.miriki.ti99.mame.ui.util.Listeners;

//############################################################################

public class MainAppFrameBuilder {

	// --------------------------------------------------
	
    private static final Logger log = LoggerFactory.getLogger( MainAppFrameBuilder.class );
	
	private final MainAppFrame frame;
	private final MainAppFrameComponents ui;
	
	// --------------------------------------------------
	
    MainAppFrameBuilder( MainAppFrame frame, MainAppFrameComponents ui ) {

    	log.debug( "----- start: [constructor] MainAppFrameBuilder()" );

        this.frame = frame;
        this.ui = ui;

    	log.debug( "----- end: [constructor] MainAppFrameBuilder()" );
        
    } // [constructor] MainAppFrameBuilder()
	
	// --------------------------------------------------

    private JLabel createLabel( Container parent, String text, int x, int y, int w, int h, JComponent forComponent ) {
    	
    	log.debug( "----- start: createLabel( '{}', '{}', {}, {}, {}, {}, '{}' )", parent, text, x, y, w, h, forComponent );
    	
        JLabel result = new JLabel( text );
        result.setFont( new Font( "Arial", Font.PLAIN, 14 ));
        result.setHorizontalAlignment(SwingConstants.RIGHT);
        result.setBounds( x, y, w, h );
        if ( forComponent != null ) { result.setLabelFor( forComponent ); }
        parent.add( result );
        
    	log.debug( "----- end: createLabel() ---> {}", result );
    	
        return result;
        
    } // createLabel

	// --------------------------------------------------

    /*
    private JTextField createTextBox( Container parent, String name, String text, int x, int y, int w, int h ) {
    	return createTextBox( parent, name, text, x, y, w, h, null ); }
    */
    
    private JTextField createTextBox( Container parent, String name, String text, int x, int y, int w, int h, ActionListener listener ) {
    	
    	log.debug( "----- start: createLabel( '{}', '{}', '{}', {}, {}, {}, {}, '{}' )", parent, name, text, x, y, w, h, listener );
    	
    	JTextField result = new JTextField();
    	result.setName( name );
    	result.setFont( new Font( "Arial", Font.PLAIN, 14 ));
    	result.setText( text );
    	// txt.setColumns( 10 );
    	result.setBounds( x, y, w, h );
    	result.setHorizontalAlignment( SwingConstants.LEFT );
    	
    	if ( listener != null ) {
    		result.getDocument().addDocumentListener( Listeners.documentAsAction( result, listener ));
	    	FocusListener fl = Listeners.normalizeOnFocusLost( result );
	    	result.putClientProperty( "normalizeListener", fl );
	    	result.addFocusListener( fl );
	    }
    	
    	parent.add( result );
    	
    	log.debug( "----- end: createTextBox() ---> {}", result );
    	
        return result;
        
    } // createTextBox

	// --------------------------------------------------

    /*
    private JTextField createTextBoxWithLabel( Container parent, String name, String text, int x, int y, int w, int h, String textl, int wl ) {
    	return createTextBoxWithLabel( parent, name, text, x, y, w, h, null, textl, wl ); }
    */

    private JTextField createTextBoxWithLabel( Container parent, MainAppFrameComponents ui, String name, String text, int x, int y, int w, int h, ActionListener listener, String textl, int wl ) {
    	
    	log.debug( "----- start: createLabel( '{}', '{}', '{}', {}, {}, {}, {}, '{}', '{}', {} )", parent, name, text, x, y, w, h, listener, textl, wl );
    	
    	JTextField txt = createTextBox(parent, name, text, x, y, w, h, listener);
    	String labelName = "lbl" + name.substring(3);
    	JLabel lbl = createLabel(parent, textl, x - 8 - wl, y, wl, h, txt);
        lbl.setName( labelName );
        
        ui.bind( name, txt );
        ui.bind( labelName, lbl );
    	
    	log.debug( "----- end: createTextBoxWithLabel() ---> {}", txt );
    	
        return txt;
        
    } // createTextBoxWithLabel
    
	// --------------------------------------------------

    private JComboBox<String> createComboBox( Container parent, String name, ComboBoxModel<String> model, int x, int y, int w, int h, int selectedIndex, ActionListener listener ) {
    	
    	log.debug( "----- start: createComboBox( '{}', '{}', '{}', {}, {}, {}, {}, '{}', '{}', {} )", parent, name, model, x, y, w, h, selectedIndex, listener );
    	
        JComboBox<String> result = new JComboBox<>( model );
        result.setName( name );
        if (( selectedIndex >= 0 ) && ( selectedIndex < model.getSize() )) {
        	result.setSelectedIndex( selectedIndex );
        } else {
        	result.setSelectedIndex( 0 ); // Fallback auf "------" oder ersten Eintrag
        }
        result.setFont( new Font( "Arial", Font.PLAIN, 14 ));
        result.setBackground( Color.WHITE );
        result.setBounds( x, y, w, h );
        result.addActionListener( Listeners.comboBoxChange( result, listener ));
        parent.add( result );
        
    	log.debug( "----- end: createComboBox() ---> {}", result );
    	
        return result;
        
    } // createComboBox

	// --------------------------------------------------
    
    /*
    private JComboBox<String> createComboBoxWithLabel( Container parent, String name, ComboBoxModel<String> model, int x, int y, int w, int h, int selectedIndex, String textl, int wl ) {
    	return createComboBoxWithLabel( parent, name, model, x, y, w, h, selectedIndex, textl, wl ); }
    */
    
    private JComboBox<String> createComboBoxWithLabel( Container parent, MainAppFrameComponents ui, String name, ComboBoxModel<String> model, int x, int y, int w, int h, int selectedIndex, ActionListener listener, String textl, int wl ) {
    	
    	log.debug( "----- start: createComboBoxWithLabel( '{}', '{}', '{}', {}, {}, {}, {}, '{}', '{}', {}, '{}', {} )", parent, name, model, x, y, w, h, selectedIndex, listener, textl, wl );
    	
    	JComboBox<String> cbx = createComboBox( parent, name, model, x, y, w, h, selectedIndex, listener );
    	
    	String labelName = "lbl" + name.substring( 3 );
    	JLabel lbl = createLabel( parent, textl, x - 8 - wl, y, wl, h, cbx );
        lbl.setName( labelName );
        
        ui.bind( name, cbx );
        ui.bind( labelName, lbl );
    	
    	log.debug( "----- end: createComboBoxWithLabel() ---> {}", cbx );
    	
        return cbx;
        
    } // createComboBoxWithLabel

	// --------------------------------------------------

    public void initGUI() {

		log.debug( "----- start: initGUI()" );

		UIManager.put( "ComboBox.disabledForeground", new Color( 80, 80, 80 ));    // very dark gray
		UIManager.put( "ComboBox.disabledBackground", new Color( 240, 240, 240 )); // almost full white
		
		// layer 0: main panel
		Container contentPane = frame.getContentPane();
		// JPanel contentPane = new JPanel();
		contentPane.setLayout( null );

		// buildMenuBar();
		JMenuBar bar = new MenuBarBuilder( frame, ui, this ).build(); 
		frame.setJMenuBar( bar );
		
		// ----------
		
			// 2 columns: 112, 704
			// I18n.t( "key" )

			// ui.txtWorkingDir = createTextBoxWithLabel( contentPane, "txtWorkingDir", "C:\\Users\\mritt\\AppData\\Roaming\\TI99MAME\\", 112, 16, 464, 24, e -> frame.collectEmulatorOptions(), "WorkingDir", 88 );
			ui.txtWorkingDir = createTextBoxWithLabel( contentPane, ui, "txtWorkingDir", "<path>", 112, 16, 464, 24, e -> frame.collectEmulatorOptions(), I18n.t( "main.workingdir" ), 88 );
			
			// ui.txtEmulator = createTextBoxWithLabel( contentPane, "txtEmulator", "mame.exe", 704, 16, 464, 24, e -> frame.collectEmulatorOptions(), "Emulator", 88 );
			ui.txtEmulator = createTextBoxWithLabel( contentPane, ui, "txtEmulator", "<executable>", 704, 16, 464, 24, e -> frame.collectEmulatorOptions(), I18n.t( "main.executable" ), 88 );
			ui.txtEmulator.removeFocusListener( (FocusListener) ui.txtEmulator.getClientProperty( "normalizeListener" ));
			
			// ui.txtRomPath = createTextBoxWithLabel( contentPane, "txtRomPath", "ti99_roms\\;ti99_peb\\", 112, 48, 464, 24, e -> frame.collectEmulatorOptions(), "RomPath", 88 );
			ui.txtRomPath = createTextBoxWithLabel( contentPane, ui, "txtRomPath", "<ram path(s)>", 112, 48, 464, 24, e -> frame.collectEmulatorOptions(), I18n.t( "main.rompath" ), 88 );
			// ui.txtCartPath = createTextBoxWithLabel( contentPane, "txtCartPath", "ti99_cart\\;ti99_cart.add\\", 704, 48, 464, 24, e -> frame.collectEmulatorOptions(), "CartPath", 88 );
			ui.txtCartPath = createTextBoxWithLabel( contentPane, ui, "txtCartPath", "<cartridge path(s)>", 704, 48, 464, 24, e -> frame.collectEmulatorOptions(), I18n.t( "main.cartpath" ), 88 );
			
			// ui.cbxMachine = createComboBoxWithLabel( contentPane, "cbxMachine", new DefaultComboBoxModel<>( ui.MACHINE_NAMES ), 112, 80, 144, 22, 5, e -> frame.collectEmulatorOptions(), "Machine", 88 );
			ui.cbxMachine = createComboBoxWithLabel( contentPane, ui, "cbxMachine", new DefaultComboBoxModel<>( ui.MACHINE_NAMES ), 112, 80, 144, 22, 0, e -> frame.collectEmulatorOptions(), I18n.t( "main.machine" ), 88 );

			// ui.cbxGromPort = createComboBoxWithLabel( contentPane, "cbxGromPort", new DefaultComboBoxModel<>( ui.GROM_PORT_NAMES ), 360, 80, 144, 22, 1, e -> frame.collectEmulatorOptions(), "GromPort", 88 );
			ui.cbxGromPort = createComboBoxWithLabel( contentPane, ui, "cbxGromPort", new DefaultComboBoxModel<>( ui.GROM_PORT_NAMES ), 360, 80, 144, 22, 0, e -> frame.collectEmulatorOptions(), I18n.t( "main.gromport" ), 88 );
	
			// ui.cbxCartridge = createComboBox( contentPane, "cbxCartridge", new DefaultComboBoxModel<>( ui.CARTRIDGE_NAMES ), 512, 80, 144, 22, 1, e -> frame.collectEmulatorOptions() );
			ui.cbxCartridge = createComboBox( contentPane, "cbxCartridge", new DefaultComboBoxModel<>( ui.CARTRIDGE_NAMES ), 512, 80, 144, 22, 0, e -> frame.collectEmulatorOptions() );
			
			// ui.cbxJoystick = createComboBoxWithLabel( contentPane, "cbxJoystick", new DefaultComboBoxModel<>( ui.JOYSTICK_PORT_NAMES ), 768, 80, 144, 22, 0, e -> frame.collectEmulatorOptions(), "JoyPort", 88 );
			ui.cbxJoystick = createComboBoxWithLabel( contentPane, ui, "cbxJoystick", new DefaultComboBoxModel<>( ui.JOYSTICK_PORT_NAMES ), 768, 80, 144, 22, 0, e -> frame.collectEmulatorOptions(), I18n.t( "main.joyport" ), 88 );
			
			// ui.cbxIoPort = createComboBoxWithLabel( contentPane, "cbxIoPort", new DefaultComboBoxModel<>( ui.SIDEPORT_DEVICE_NAMES ), 1024, 80, 144, 22, 1, e -> frame.collectEmulatorOptions(), "IoPort", 88 );
			ui.cbxIoPort = createComboBoxWithLabel( contentPane, ui, "cbxIoPort", new DefaultComboBoxModel<>( ui.SIDEPORT_DEVICE_NAMES ), 1024, 80, 144, 22, 0, e -> frame.collectEmulatorOptions(), I18n.t( "main.ioport" ), 88 );
			
			// ui.txtFddPath = createTextBoxWithLabel( contentPane, "txtFddPath", "ti99_fdsk\\", 112, 112, 464, 24, e -> frame.collectEmulatorOptions(), "FddPath", 88 );
			ui.txtFddPath = createTextBoxWithLabel( contentPane, ui, "txtFddPath", "<floppydisk path>", 112, 112, 464, 24, e -> frame.collectEmulatorOptions(), I18n.t( "main.fddpath" ), 88 );
			
			// ui.txtHddPath = createTextBoxWithLabel( contentPane, "txtHddPath", "ti99_hdsk\\", 704, 112, 464, 24, e -> frame.collectEmulatorOptions(), "HddPath", 88 );
			ui.txtHddPath = createTextBoxWithLabel( contentPane, ui, "txtHddPath", "<harddisk path>", 704, 112, 464, 24, e -> frame.collectEmulatorOptions(), I18n.t( "main.hddpath" ), 88 );
			
			// ui.txtCassPath = createTextBoxWithLabel( contentPane, "txtCassPath", "ti99_cass\\", 112, 144, 464, 24, e -> frame.collectEmulatorOptions(), "CassPath", 88 );
			ui.txtCassPath = createTextBoxWithLabel( contentPane, ui, "txtCassPath", "<cassette path>", 112, 144, 464, 24, e -> frame.collectEmulatorOptions(), I18n.t( "main.casspath" ), 88 );

			ui.txtFiadPath = createTextBoxWithLabel( contentPane, ui, "txtFiadPath", "<fiad path>", 704, 144, 464, 24, e -> frame.collectEmulatorOptions(), I18n.t( "main.fiadpath" ), 88 );
			
			// ui.txtFlop1 = createTextBoxWithLabel( contentPane, "txtFlop1", "FlopDsk1.dsk", 112, 176, 216, 24, e -> frame.collectEmulatorOptions(), "Flop 1", 48 );
			// ui.txtFlop2 = createTextBoxWithLabel( contentPane, "txtFlop2", "FlopDsk2.dsk", 112+8+48+8+216, 176, 216, 24, e -> frame.collectEmulatorOptions(), "f2", 24 );
			// ui.txtFlop3 = createTextBoxWithLabel( contentPane, "txtFlop3", "", 112+8+48+216+8+8+48+8+216, 176, 216, 24, e -> frame.collectEmulatorOptions(), "f3", 24 );
			// ui.txtFlop4 = createTextBoxWithLabel( contentPane, "txtFlop4", "", 112+8+48+216+8+8+48+8+216+8+48+8+216, 176, 216, 24, e -> frame.collectEmulatorOptions(), "f4", 24 );
			ui.cbxFlop1 = createComboBoxWithLabel( contentPane, ui, "cbxFlop1", new DefaultComboBoxModel<>( ui.FLOPPYDISKIMAGE_NAMES ), 112, 176, 216, 24, 0, e -> frame.collectEmulatorOptions(), I18n.t( "main.flop1" ), 48 );
			ui.cbxFlop2 = createComboBoxWithLabel( contentPane, ui, "cbxFlop2", new DefaultComboBoxModel<>( ui.FLOPPYDISKIMAGE_NAMES ), 112+8+48+8+216, 176, 216, 24, 0, e -> frame.collectEmulatorOptions(), I18n.t( "main.flop2" ), 24 );
			ui.cbxFlop3 = createComboBoxWithLabel( contentPane, ui, "cbxFlop3", new DefaultComboBoxModel<>( ui.FLOPPYDISKIMAGE_NAMES ), 112+8+48+216+8+8+48+8+216, 176, 216, 24, 0, e -> frame.collectEmulatorOptions(), I18n.t( "main.flop3" ), 24 );
			ui.cbxFlop4 = createComboBoxWithLabel( contentPane, ui, "cbxFlop4", new DefaultComboBoxModel<>( ui.FLOPPYDISKIMAGE_NAMES ), 112+8+48+216+8+8+48+8+216+8+48+8+216, 176, 216, 24, 0, e -> frame.collectEmulatorOptions(), I18n.t( "main.flop4" ), 24 );
	
			// ui.txtHard1 = createTextBoxWithLabel( contentPane, "txtHard1", "HardWds1.chd", 112, 208, 216, 24, e -> frame.collectEmulatorOptions(), "Hard 1", 48 );
			// ui.txtHard2 = createTextBoxWithLabel( contentPane, "txtHard2", "", 112+8+48+8+216, 208, 216, 24, e -> frame.collectEmulatorOptions(), "h2", 24 );
			// ui.txtHard3 = createTextBoxWithLabel( contentPane, "txtHard3", "", 112+8+48+216+8+8+48+8+216, 208, 216, 24, e -> frame.collectEmulatorOptions(), "h3", 24 );
			ui.cbxHard1 = createComboBoxWithLabel( contentPane, ui, "cbxHard1", new DefaultComboBoxModel<>( ui.HARDDISKIMAGE_NAMES ), 112, 208, 216, 24, 0, e -> frame.collectEmulatorOptions(), I18n.t( "main.hard1" ), 48 );
			ui.cbxHard2 = createComboBoxWithLabel( contentPane, ui, "cbxHard2", new DefaultComboBoxModel<>( ui.HARDDISKIMAGE_NAMES ), 112+8+48+8+216, 208, 216, 24, 0, e -> frame.collectEmulatorOptions(), I18n.t( "main.hard2" ), 24 );
			ui.cbxHard3 = createComboBoxWithLabel( contentPane, ui, "cbxHard3", new DefaultComboBoxModel<>( ui.HARDDISKIMAGE_NAMES ), 112+8+48+216+8+8+48+8+216, 208, 216, 24, 0, e -> frame.collectEmulatorOptions(), I18n.t( "main.hard3" ), 24 );
			
			// ui.txtCass1 = createTextBoxWithLabel( contentPane, "txtCass1", "", 112, 240, 216, 24, e -> frame.collectEmulatorOptions(), "Cass 1", 48 );
			// ui.txtCass2 = createTextBoxWithLabel( contentPane, "txtCass2", "", 112+8+48+8+216, 240, 216, 24, e -> frame.collectEmulatorOptions(), "c2", 24 );
			ui.cbxCass1 = createComboBoxWithLabel( contentPane, ui, "cbxCass1", new DefaultComboBoxModel<>( ui.CASSETTEIMAGE_NAMES ), 112, 240, 216, 24, 0, e -> frame.collectEmulatorOptions(), I18n.t( "main.cass1" ), 48 );
			ui.cbxCass2 = createComboBoxWithLabel( contentPane, ui, "cbxCass2", new DefaultComboBoxModel<>( ui.CASSETTEIMAGE_NAMES ), 112+8+48+8+216, 240, 216, 24, 0, e -> frame.collectEmulatorOptions(), I18n.t( "main.cass2" ), 24 );
			
			// ui.txtAddOpt = createTextBoxWithLabel( contentPane, "txtAddOpt", "-skip_gameinfo", 704, 272, 464, 24, e -> frame.collectEmulatorOptions(), "add. opt.", 88 );
			ui.txtAddOpt = createTextBoxWithLabel( contentPane, ui, "txtAddOpt", "", 704, 272, 464, 24, e -> frame.collectEmulatorOptions(), I18n.t( "main.addopt" ), 88 );
			ui.txtAddOpt.removeFocusListener( (FocusListener) ui.txtAddOpt.getClientProperty( "normalizeListener" ));
			
			contentPane.add( ui.dbgEmulatorOptions );
			ui.dbgEmulatorOptions.setFont( new Font( "Arial", Font.PLAIN, 14 ));
			ui.dbgEmulatorOptions.setLineWrap( true );
			ui.dbgEmulatorOptions.setBounds( 616, 304, 464+88, 320-8-32-8-24 );
	
			// TODO: button factory bauen
			
			contentPane.add( ui.btnStartEmulator );
			ui.btnStartEmulator.setName( "btnStartEmulator" );
			ui.btnStartEmulator.setFont( new Font( "Arial", Font.PLAIN, 14 ));
			ui.btnStartEmulator.setBounds( 616, 592-24, 464+88, 32 );
			ui.btnStartEmulator.addMouseListener( Listeners.startEmulatorClick( frame ));

			contentPane.add( ui.btnTestFiad );
			ui.btnTestFiad.setName( "btnTestFiad" );
			ui.btnTestFiad.setFont( new Font( "Arial", Font.PLAIN, 14 ));
			ui.btnTestFiad.setBounds( 616, 240, 464+88, 32 );
			ui.btnTestFiad.addMouseListener( Listeners.testFiadCreate());

			// tabbed "sideport devices"
			// shows tabs "peb", "speechsyn", "splitter", "arcturus"
			ui.contentPane.add( ui.tabSideportDevices );
			ui.tabSideportDevices.setFont( new Font( "Arial", Font.PLAIN, 14 ));
			ui.tabSideportDevices.setBounds( 8, 272, 464+96+32, 352-24 );

			// layer 1: tab "peb"
			ui.tabSideportDevices.addTab( "peb", null, ui.Panel_PEB, null );
			ui.Panel_PEB.setLayout( new CardLayout( 0, 0 ));

					// tabbed "peb"
					// shows tabs "peb" (slots) and "32kmem", ... "whtscsi" (devices)
					ui.Panel_PEB.add( ui.tabPebDevices, "tabPebDevices" );
					ui.tabPebDevices.setTabLayoutPolicy( JTabbedPane.SCROLL_TAB_LAYOUT );
		
					// layer 2: tab "peb"
					ui.tabPebDevices.addTab( "peb", null, ui.Panel_PEB_PEB, null );
					ui.Panel_PEB_PEB.putClientProperty( "deviceKey", "peb" );
					ui.Panel_PEB_PEB.setLayout( null );
			
						// ui.cbxSlot1 = createComboBoxWithLabel( ui.Panel_PEB_PEB, "cbxSlot1", new DefaultComboBoxModel<>(MirikiPebDevices.comboModel()), 82, 12, 256, 22, 0, e -> frame.collectEmulatorOptions(), "Slot #1", 64 );
						ui.cbxSlot1 = createComboBoxWithLabel( ui.Panel_PEB_PEB, ui, "cbxSlot1", new DefaultComboBoxModel<>( ui.PEB1_DEVICE_NAMES ), 82, 12, 256, 22, 0, e -> frame.collectEmulatorOptions(), "Slot #1", 64 );
						ui.cbxSlot1.setEnabled( false );
					
						// ui.cbxSlot2 = createComboBoxWithLabel( ui.Panel_PEB_PEB, "cbxSlot2", new DefaultComboBoxModel<>(MirikiPebDevices.comboModel()), 82, 12, 256, 22, 6, e -> frame.collectEmulatorOptions(), "Slot #2", 64 );
						ui.cbxSlot2 = createComboBoxWithLabel( ui.Panel_PEB_PEB, ui, "cbxSlot2", new DefaultComboBoxModel<>(PebDevices.comboModel()), 82, 44, 256, 22, 0, e -> frame.collectEmulatorOptions(), "Slot #2", 64 );
						
						ui.cbxSlot3 = createComboBoxWithLabel( ui.Panel_PEB_PEB, ui, "cbxSlot3", new DefaultComboBoxModel<>(PebDevices.comboModel()), 82, 76, 256, 22, 0, e -> frame.collectEmulatorOptions(), "Slot #3", 64 );
			
						// ui.cbxSlot4 = createComboBoxWithLabel( ui.Panel_PEB_PEB, "cbxSlot4", new DefaultComboBoxModel<>(MirikiPebDevices.comboModel()), 82, 84, 256, 22, 17, e -> frame.collectEmulatorOptions(), "Slot #4", 64 );
						ui.cbxSlot4 = createComboBoxWithLabel( ui.Panel_PEB_PEB, ui, "cbxSlot4", new DefaultComboBoxModel<>(PebDevices.comboModel()), 82, 108, 256, 22, 0, e -> frame.collectEmulatorOptions(), "Slot #4", 64 );
			
						ui.cbxSlot5 = createComboBoxWithLabel( ui.Panel_PEB_PEB, ui, "cbxSlot5", new DefaultComboBoxModel<>(PebDevices.comboModel()), 82, 140, 256, 22, 0, e -> frame.collectEmulatorOptions(), "Slot #5", 64 );
			
						// ui.cbxSlot6 = createComboBoxWithLabel( ui.Panel_PEB_PEB, "cbxSlot6", new DefaultComboBoxModel<>(MirikiPebDevices.comboModel()), 82, 156, 256, 22, 20, e -> frame.collectEmulatorOptions(), "Slot #6", 64 );
						ui.cbxSlot6 = createComboBoxWithLabel( ui.Panel_PEB_PEB, ui, "cbxSlot6", new DefaultComboBoxModel<>(PebDevices.comboModel()), 82, 172, 256, 22, 0, e -> frame.collectEmulatorOptions(), "Slot #6", 64 );
			
						ui.cbxSlot7 = createComboBoxWithLabel( ui.Panel_PEB_PEB, ui, "cbxSlot7", new DefaultComboBoxModel<>(PebDevices.comboModel()), 82, 204, 256, 22, 0, e -> frame.collectEmulatorOptions(), "Slot #7", 64 );
			
						// ui.cbxSlot8 = createComboBoxWithLabel( ui.Panel_PEB_PEB, "cbxSlot8", new DefaultComboBoxModel<>(MirikiPebDevices.comboModel()), 82, 228, 256, 22, 8, e -> frame.collectEmulatorOptions(), "Slot #8", 64 );
						ui.cbxSlot8 = createComboBoxWithLabel( ui.Panel_PEB_PEB, ui, "cbxSlot8", new DefaultComboBoxModel<>(PebDevices.comboModel()), 82, 236, 256, 22, 0, e -> frame.collectEmulatorOptions(), "Slot #8", 64 );
			
					// layer 2: tab "32kmem"
					ui.tabPebDevices.addTab( "32kmem", null, ui.Panel_PEB_32kMem, null );
					ui.Panel_PEB_32kMem.putClientProperty( "deviceKey", "32kmem" );
					ui.Panel_PEB_32kMem.setLayout( null );
			
					// layer 2: tab "bwg"
					ui.tabPebDevices.addTab( "bwg", null, ui.Panel_PEB_Bwg, null );
					ui.Panel_PEB_Bwg.putClientProperty( "deviceKey", "bwg" );
					ui.Panel_PEB_Bwg.setLayout( null );
			
						// ui.cbxBwg0 = createComboBoxWithLabel( ui.Panel_PEB_Bwg, "cbxBwg0", new DefaultComboBoxModel<>( ui.BWG_FDD_NAMES ), 82, 12, 96, 22, 2, e -> frame.collectEmulatorOptions(), "Flop 1", 64 );
						ui.cbxBwg0 = createComboBoxWithLabel( ui.Panel_PEB_Bwg, ui, "cbxBwg0", new DefaultComboBoxModel<>( ui.BWG_FDD_NAMES ), 82, 12, 96, 22, 0, e -> frame.collectEmulatorOptions(), "Flop 1", 64 );
			
						// ui.cbxBwg1 = createComboBoxWithLabel( ui.Panel_PEB_Bwg, "cbxBwg1", new DefaultComboBoxModel<>( ui.BWG_FDD_NAMES ), 82, 48, 96, 22, 2, e -> frame.collectEmulatorOptions(), "Flop 2", 64 );
						ui.cbxBwg1 = createComboBoxWithLabel( ui.Panel_PEB_Bwg, ui, "cbxBwg1", new DefaultComboBoxModel<>( ui.BWG_FDD_NAMES ), 82, 48, 96, 22, 0, e -> frame.collectEmulatorOptions(), "Flop 2", 64 );
			
						ui.cbxBwg2 = createComboBoxWithLabel( ui.Panel_PEB_Bwg, ui, "cbxBwg2", new DefaultComboBoxModel<>( ui.BWG_FDD_NAMES ), 82, 84, 96, 22, 0, e -> frame.collectEmulatorOptions(), "Flop 3", 64 );
			
						ui.cbxBwg3 = createComboBoxWithLabel( ui.Panel_PEB_Bwg, ui, "cbxBwg3", new DefaultComboBoxModel<>( ui.BWG_FDD_NAMES ), 82, 120, 96, 22, 0, e -> frame.collectEmulatorOptions(), "Flop 4", 64 );
			
					// layer 2: tab "ccdcc"
					ui.tabPebDevices.addTab( "ccdcc", null, ui.Panel_PEB_CcDcc, null );
					ui.Panel_PEB_CcDcc.putClientProperty( "deviceKey", "ccdcc" );
					ui.Panel_PEB_CcDcc.setLayout( null );
			
						// ui.cbxCcdcc0 = createComboBoxWithLabel( ui.Panel_PEB_CcDcc, "cbxCcdcc0", new DefaultComboBoxModel<>( ui.CCDCC_FDD_NAMES ), 82, 12, 96, 22, 2, e -> frame.collectEmulatorOptions(), "Flop 1", 64 );
						ui.cbxCcdcc0 = createComboBoxWithLabel( ui.Panel_PEB_CcDcc, ui, "cbxCcdcc0", new DefaultComboBoxModel<>( ui.CCDCC_FDD_NAMES ), 82, 12, 96, 22, 0, e -> frame.collectEmulatorOptions(), "Flop 1", 64 );
			
						// ui.cbxCcdcc1 = createComboBoxWithLabel( ui.Panel_PEB_CcDcc, "cbxCcdcc1", new DefaultComboBoxModel<>( ui.CCDCC_FDD_NAMES ), 82, 48, 96, 22, 2, e -> frame.collectEmulatorOptions(), "Flop 2", 64 );
						ui.cbxCcdcc1 = createComboBoxWithLabel( ui.Panel_PEB_CcDcc, ui, "cbxCcdcc1", new DefaultComboBoxModel<>( ui.CCDCC_FDD_NAMES ), 82, 48, 96, 22, 0, e -> frame.collectEmulatorOptions(), "Flop 2", 64 );
			
						ui.cbxCcdcc2 = createComboBoxWithLabel( ui.Panel_PEB_CcDcc, ui, "cbxCcdcc2", new DefaultComboBoxModel<>( ui.CCDCC_FDD_NAMES ), 82, 84, 96, 22, 0, e -> frame.collectEmulatorOptions(), "Flop 3", 64 );
			
						ui.cbxCcdcc3 = createComboBoxWithLabel( ui.Panel_PEB_CcDcc, ui, "cbxCcdcc3", new DefaultComboBoxModel<>( ui.CCDCC_FDD_NAMES ), 82, 120, 96, 22, 0, e -> frame.collectEmulatorOptions(), "Flop 4", 64 );
			
					// layer 2: tab "ccfdc"
					ui.tabPebDevices.addTab( "ccfdc", null, ui.Panel_PEB_CcFdc, null );
					ui.Panel_PEB_CcFdc.putClientProperty( "deviceKey", "ccfdc" );
					ui.Panel_PEB_CcFdc.setLayout( null );
			
						// ui.cbxCcfdc0 = createComboBoxWithLabel( ui.Panel_PEB_CcFdc, "cbxCcfdc0", new DefaultComboBoxModel<>( ui.CCFDC_FDD_NAMES ), 82, 12, 96, 22, 2, e -> frame.collectEmulatorOptions(), "Flop 1", 64 );
						ui.cbxCcfdc0 = createComboBoxWithLabel( ui.Panel_PEB_CcFdc, ui, "cbxCcfdc0", new DefaultComboBoxModel<>( ui.CCFDC_FDD_NAMES ), 82, 12, 96, 22, 0, e -> frame.collectEmulatorOptions(), "Flop 1", 64 );
			
						// ui.cbxCcfdc1 = createComboBoxWithLabel( ui.Panel_PEB_CcFdc, "cbxCcfdc1", new DefaultComboBoxModel<>( ui.CCFDC_FDD_NAMES ), 82, 48, 96, 22, 2, e -> frame.collectEmulatorOptions(), "Flop 2", 64 );
						ui.cbxCcfdc1 = createComboBoxWithLabel( ui.Panel_PEB_CcFdc, ui, "cbxCcfdc1", new DefaultComboBoxModel<>( ui.CCFDC_FDD_NAMES ), 82, 48, 96, 22, 0, e -> frame.collectEmulatorOptions(), "Flop 2", 64 );
			
						ui.cbxCcfdc2 = createComboBoxWithLabel( ui.Panel_PEB_CcFdc, ui, "cbxCcfdc2", new DefaultComboBoxModel<>( ui.CCFDC_FDD_NAMES ), 82, 84, 96, 22, 0, e -> frame.collectEmulatorOptions(), "Flop 3", 64 );
			
						ui.cbxCcfdc3 = createComboBoxWithLabel( ui.Panel_PEB_CcFdc, ui, "cbxCcfdc3", new DefaultComboBoxModel<>( ui.CCFDC_FDD_NAMES ), 82, 120, 96, 22, 0, e -> frame.collectEmulatorOptions(), "Flop 4", 64 );
			
					// layer 2: tab "ddcc1"
					ui.tabPebDevices.addTab( "ddcc1", null, ui.Panel_PEB_DDcc1, null );
					ui.Panel_PEB_DDcc1.putClientProperty( "deviceKey", "ddcc1" );
					ui.Panel_PEB_DDcc1.setLayout( null );
			
						// ui.cbxDdcc0 = createComboBoxWithLabel( ui.Panel_PEB_DDcc1, "cbxDdcc0", new DefaultComboBoxModel<>( ui.DDCC1_FDD_NAMES ), 82, 12, 96, 22, 2, e -> frame.collectEmulatorOptions(), "Flop 1", 64 );
						ui.cbxDdcc0 = createComboBoxWithLabel( ui.Panel_PEB_DDcc1, ui, "cbxDdcc0", new DefaultComboBoxModel<>( ui.DDCC1_FDD_NAMES ), 82, 12, 96, 22, 0, e -> frame.collectEmulatorOptions(), "Flop 1", 64 );
			
						// ui.cbxDdcc1 = createComboBoxWithLabel( ui.Panel_PEB_DDcc1, "cbxDdcc1", new DefaultComboBoxModel<>( ui.DDCC1_FDD_NAMES ), 82, 48, 96, 22, 2, e -> frame.collectEmulatorOptions(), "Flop 2", 64 );
						ui.cbxDdcc1 = createComboBoxWithLabel( ui.Panel_PEB_DDcc1, ui, "cbxDdcc1", new DefaultComboBoxModel<>( ui.DDCC1_FDD_NAMES ), 82, 48, 96, 22, 0, e -> frame.collectEmulatorOptions(), "Flop 2", 64 );
			
						ui.cbxDdcc2 = createComboBoxWithLabel( ui.Panel_PEB_DDcc1, ui, "cbxDdcc2", new DefaultComboBoxModel<>( ui.DDCC1_FDD_NAMES ), 82, 84, 96, 22, 0, e -> frame.collectEmulatorOptions(), "Flop 3", 64 );
			
						ui.cbxDdcc3 = createComboBoxWithLabel( ui.Panel_PEB_DDcc1, ui, "cbxDdcc3", new DefaultComboBoxModel<>( ui.DDCC1_FDD_NAMES ), 82, 120, 96, 22, 0, e -> frame.collectEmulatorOptions(), "Flop 4", 64 );
			
					// layer 2: tab "evpc"
					ui.tabPebDevices.addTab( "evpc", null, ui.Panel_PEB_Evpc, null );
					ui.Panel_PEB_Evpc.putClientProperty( "deviceKey", "evpc" );
					ui.Panel_PEB_Evpc.setLayout( null );
			
						ui.cbxColorbus = createComboBoxWithLabel( ui.Panel_PEB_Evpc, ui, "cbxColorbus", new DefaultComboBoxModel<>( ui.EVPC_DEVICE_NAMES ), 82, 12, 256, 22, 0, e -> frame.collectEmulatorOptions(), "ColorBus", 64 );
			
					// layer 2: tab "forti"
					ui.tabPebDevices.addTab( "forti", null, ui.Panel_PEB_Forti, null );
					ui.Panel_PEB_Forti.putClientProperty( "deviceKey", "forti" );
					ui.Panel_PEB_Forti.setLayout( null );
			
					// layer 2: tab "hfdc"
					ui.tabPebDevices.addTab( "hfdc", null, ui.Panel_PEB_HFdc, null );
					ui.Panel_PEB_HFdc.putClientProperty( "deviceKey", "hfdc" );
					ui.Panel_PEB_HFdc.setLayout( null );
			
						// ui.cbxHfdcF1 = createComboBoxWithLabel( ui.Panel_PEB_HFdc, "cbxHfdcF1", new DefaultComboBoxModel<>( ui.HFDC_FDD_NAMES ), 82, 12, 96, 22, 3, e -> frame.collectEmulatorOptions(), "Flop 1", 64 );
						ui.cbxHfdcF1 = createComboBoxWithLabel( ui.Panel_PEB_HFdc, ui, "cbxHfdcF1", new DefaultComboBoxModel<>( ui.HFDC_FDD_NAMES ), 82, 12, 96, 22, 0, e -> frame.collectEmulatorOptions(), "Flop 1", 64 );
			
						// ui.cbxHfdcF2 = createComboBoxWithLabel( ui.Panel_PEB_HFdc, "cbxHfdcF2", new DefaultComboBoxModel<>( ui.HFDC_FDD_NAMES ), 82, 48, 96, 22, 3, e -> frame.collectEmulatorOptions(), "Flop 2", 64 );
						ui.cbxHfdcF2 = createComboBoxWithLabel( ui.Panel_PEB_HFdc, ui, "cbxHfdcF2", new DefaultComboBoxModel<>( ui.HFDC_FDD_NAMES ), 82, 48, 96, 22, 0, e -> frame.collectEmulatorOptions(), "Flop 2", 64 );
			
						ui.cbxHfdcF3 = createComboBoxWithLabel( ui.Panel_PEB_HFdc, ui, "cbxHfdcF3", new DefaultComboBoxModel<>( ui.HFDC_FDD_NAMES ), 82, 84, 96, 22, 0, e -> frame.collectEmulatorOptions(), "Flop 3", 64 );
			
						ui.cbxHfdcF4 = createComboBoxWithLabel( ui.Panel_PEB_HFdc, ui, "cbxHfdcF4", new DefaultComboBoxModel<>( ui.HFDC_FDD_NAMES ), 82, 120, 96, 22, 0, e -> frame.collectEmulatorOptions(), "Flop 4", 64 );
			
						// ui.cbxHfdcH1 = createComboBoxWithLabel( ui.Panel_PEB_HFdc, "cbxHfdcH1", new DefaultComboBoxModel<>( ui.HFDC_HDD_NAMES ), 82, 156, 96, 22, 1, e -> frame.collectEmulatorOptions(), "Hard 1", 64 );
						ui.cbxHfdcH1 = createComboBoxWithLabel( ui.Panel_PEB_HFdc, ui, "cbxHfdcH1", new DefaultComboBoxModel<>( ui.HFDC_HDD_NAMES ), 82, 156, 96, 22, 0, e -> frame.collectEmulatorOptions(), "Hard 1", 64 );
			
						ui.cbxHfdcH2 = createComboBoxWithLabel( ui.Panel_PEB_HFdc, ui, "cbxHfdcH2", new DefaultComboBoxModel<>( ui.HFDC_HDD_NAMES ), 82, 192, 96, 22, 0, e -> frame.collectEmulatorOptions(), "Hard 2", 64 );
			
						ui.cbxHfdcH3 = createComboBoxWithLabel( ui.Panel_PEB_HFdc, ui, "cbxHfdcH3", new DefaultComboBoxModel<>( ui.HFDC_HDD_NAMES ), 82, 228, 96, 22, 0, e -> frame.collectEmulatorOptions(), "Hard 3", 64 );
			
					// layer 2: tab "horizon"
					ui.tabPebDevices.addTab( "horizon", null, ui.Panel_PEB_Horizon, null );
					ui.Panel_PEB_Horizon.putClientProperty( "deviceKey", "horizon" );
					ui.Panel_PEB_Horizon.setLayout( null );
			
					// layer 2: tab "hsgpl"
					ui.tabPebDevices.addTab( "hsgpl", null, ui.Panel_PEB_HsGpl, null );
					ui.Panel_PEB_HsGpl.putClientProperty( "deviceKey", "hsgpl" );
					ui.Panel_PEB_HsGpl.setLayout( null );
			
					// layer 2: tab "ide"
					ui.tabPebDevices.addTab( "ide", null, ui.Panel_PEB_Ide, null );
					ui.Panel_PEB_Ide.putClientProperty( "deviceKey", "ide" );
					ui.Panel_PEB_Ide.setLayout( null );
			
						ui.cbxAta0 = createComboBoxWithLabel( ui.Panel_PEB_Ide, ui, "cbxAta0", new DefaultComboBoxModel<>( ui.IDE_DEVICE_NAMES ), 82, 12, 96, 22, 0, e -> frame.collectEmulatorOptions(), "IDE 1", 64 );
			
						ui.cbxAta1 = createComboBoxWithLabel( ui.Panel_PEB_Ide, ui, "cbxAta1", new DefaultComboBoxModel<>( ui.IDE_DEVICE_NAMES ), 82, 48, 96, 22, 0, e -> frame.collectEmulatorOptions(), "IDE 2", 64 );
			
					// layer 2: tab "myarcmem"
					ui.tabPebDevices.addTab( "myarcmem", null, ui.Panel_PEB_MyarcMem, null );
					ui.Panel_PEB_MyarcMem.putClientProperty( "deviceKey", "myarcmem" );
					ui.Panel_PEB_MyarcMem.setLayout( null );
			
					// layer 2: tab "pcode"
					ui.tabPebDevices.addTab( "pcode", null, ui.Panel_PEB_PCode, null );
					ui.Panel_PEB_PCode.putClientProperty( "deviceKey", "pcode" );
					ui.Panel_PEB_PCode.setLayout( null );
			
					// layer 2: tab "pgram"
					ui.tabPebDevices.addTab( "pgram", null, ui.Panel_PEB_PGram, null );
					ui.Panel_PEB_PGram.putClientProperty( "deviceKey", "pgram" );
					ui.Panel_PEB_PGram.setLayout( null );
			
					// layer 2: tab "samsmem"
					ui.tabPebDevices.addTab( "samsmem", null, ui.Panel_PEB_SamsMem, null );
					ui.Panel_PEB_SamsMem.putClientProperty( "deviceKey", "samsmem" );
					ui.Panel_PEB_SamsMem.setLayout( null );
			
					// layer 2: tab "sidmaster"
					ui.tabPebDevices.addTab( "sidmaster", null, ui.Panel_PEB_SidMaster, null );
					ui.Panel_PEB_SidMaster.putClientProperty( "deviceKey", "sidmaster" );
					ui.Panel_PEB_SidMaster.setLayout( null );
			
					// layer 2: tab "speechadapter"
					ui.tabPebDevices.addTab( "speechadapter", null, ui.Panel_PEB_SpeechAdapter, null );
					ui.Panel_PEB_SpeechAdapter.putClientProperty( "deviceKey", "speechadapter" );
					ui.Panel_PEB_SpeechAdapter.setLayout( null );
			
					// layer 2: tab "tifdc"
					ui.tabPebDevices.addTab( "tifdc", null, ui.Panel_PEB_TiFdc, null );
					ui.Panel_PEB_TiFdc.putClientProperty( "deviceKey", "tifdc" );
					ui.Panel_PEB_TiFdc.setLayout( null );
			
						// ui.cbxTifdc0 = createComboBoxWithLabel( ui.Panel_PEB_TiFdc, "cbxTifdc0", new DefaultComboBoxModel<>( ui.TIFDC_FDD_NAMES ), 82, 12, 96, 22, 1, e -> frame.collectEmulatorOptions(), "Flop 1", 64 );
						ui.cbxTifdc0 = createComboBoxWithLabel( ui.Panel_PEB_TiFdc, ui, "cbxTifdc0", new DefaultComboBoxModel<>( ui.TIFDC_FDD_NAMES ), 82, 12, 96, 22, 0, e -> frame.collectEmulatorOptions(), "Flop 1", 64 );
			
						// ui.cbxTifdc1 = createComboBoxWithLabel( ui.Panel_PEB_TiFdc, "cbxTifdc1", new DefaultComboBoxModel<>( ui.TIFDC_FDD_NAMES ), 82, 48, 96, 22, 1, e -> frame.collectEmulatorOptions(), "Flop 2", 64 );
						ui.cbxTifdc1 = createComboBoxWithLabel( ui.Panel_PEB_TiFdc, ui, "cbxTifdc1", new DefaultComboBoxModel<>( ui.TIFDC_FDD_NAMES ), 82, 48, 96, 22, 0, e -> frame.collectEmulatorOptions(), "Flop 2", 64 );
			
						ui.cbxTifdc2 = createComboBoxWithLabel( ui.Panel_PEB_TiFdc, ui, "cbxTifdc2", new DefaultComboBoxModel<>( ui.TIFDC_FDD_NAMES ), 82, 84, 96, 22, 0, e -> frame.collectEmulatorOptions(), "Flop 3", 64 );
			
					// layer 2: tab "tipi"
					ui.tabPebDevices.addTab( "tipi", null, ui.Panel_PEB_TiPi, null );
					ui.Panel_PEB_TiPi.putClientProperty( "deviceKey", "tipi" );
					ui.Panel_PEB_TiPi.setLayout( null );
			
					// layer 2: tab "tirs232"
					ui.tabPebDevices.addTab( "tirs232", null, ui.Panel_PEB_TiRs232, null );
					ui.Panel_PEB_TiRs232.putClientProperty( "deviceKey", "tirs232" );
					ui.Panel_PEB_TiRs232.setLayout( null );
			
					// layer 2: tab "usbsm"
					ui.tabPebDevices.addTab( "usbsm", null, ui.Panel_PEB_UsbSm, null );
					ui.Panel_PEB_UsbSm.putClientProperty( "deviceKey", "usbsm" );
					ui.Panel_PEB_UsbSm.setLayout( null );
			
					// layer 2: tab "whtscsi"
					ui.tabPebDevices.addTab( "whtscsi", null, ui.Panel_PEB_WhtScsi, null );
					ui.Panel_PEB_WhtScsi.putClientProperty( "deviceKey", "whtscsi" );
					ui.Panel_PEB_WhtScsi.setLayout( null );
			
						ui.cbxScsibus0 = createComboBoxWithLabel( ui.Panel_PEB_WhtScsi, ui, "cbxScsibus0", new DefaultComboBoxModel<>( ui.SCSI_DEVICE_NAMES ), 82, 12, 96, 22, 0, e -> frame.collectEmulatorOptions(), "SCSI 1", 64 );
						ui.cbxScsibus0.setEnabled( false );
						ui.txtScsibus0 = createTextBox( ui.Panel_PEB_WhtScsi, "txtScsibus0", "", 178, 12, 160, 24, e -> frame.collectEmulatorOptions() );
						ui.txtScsibus0.removeFocusListener( (FocusListener)ui.txtScsibus0.getClientProperty( "normalizeListener" ));
						ui.txtScsibus0.setEnabled( false );
			
						ui.cbxScsibus1 = createComboBoxWithLabel( ui.Panel_PEB_WhtScsi, ui, "cbxScsibus1", new DefaultComboBoxModel<>( ui.SCSI_DEVICE_NAMES ), 82, 44, 96, 22, 0, e -> frame.collectEmulatorOptions(), "SCSI 2", 64 );
						ui.cbxScsibus1.setEnabled( false );
						ui.txtScsibus1 = createTextBox( ui.Panel_PEB_WhtScsi, "txtScsibus1", "", 178, 44, 160, 24, e -> frame.collectEmulatorOptions() );
						ui.txtScsibus1.removeFocusListener( (FocusListener)ui.txtScsibus1.getClientProperty( "normalizeListener" ));
						ui.txtScsibus1.setEnabled( false );
			
						ui.cbxScsibus2 = createComboBoxWithLabel( ui.Panel_PEB_WhtScsi, ui, "cbxScsibus2", new DefaultComboBoxModel<>( ui.SCSI_DEVICE_NAMES ), 82, 76, 96, 22, 0, e -> frame.collectEmulatorOptions(), "SCSI 3", 64 );
						ui.cbxScsibus2.setEnabled( false );
						ui.txtScsibus2 = createTextBox( ui.Panel_PEB_WhtScsi, "txtScsibus2", "", 178, 76, 160, 24, e -> frame.collectEmulatorOptions() );
						ui.txtScsibus2.removeFocusListener( (FocusListener)ui.txtScsibus2.getClientProperty( "normalizeListener" ));
						ui.txtScsibus2.setEnabled( false );
			
						ui.cbxScsibus3 = createComboBoxWithLabel( ui.Panel_PEB_WhtScsi, ui, "cbxScsibus3", new DefaultComboBoxModel<>( ui.SCSI_DEVICE_NAMES ), 82, 108, 96, 22, 0, e -> frame.collectEmulatorOptions(), "SCSI 4", 64 );
						ui.cbxScsibus3.setEnabled( false );
						ui.txtScsibus3 = createTextBox( ui.Panel_PEB_WhtScsi, "txtScsibus3", "", 178, 108, 160, 24, e -> frame.collectEmulatorOptions() );
						ui.txtScsibus3.removeFocusListener( (FocusListener)ui.txtScsibus3.getClientProperty( "normalizeListener" ));
						ui.txtScsibus3.setEnabled( false );
			
						ui.cbxScsibus4 = createComboBoxWithLabel( ui.Panel_PEB_WhtScsi, ui, "cbxScsibus4", new DefaultComboBoxModel<>( ui.SCSI_DEVICE_NAMES ), 82, 140, 96, 22, 0, e -> frame.collectEmulatorOptions(), "SCSI 5", 64 );
						ui.cbxScsibus4.setEnabled( false );
						ui.txtScsibus4 = createTextBox( ui.Panel_PEB_WhtScsi, "txtScsibus4", "", 178, 140, 160, 24, e -> frame.collectEmulatorOptions() );
						ui.txtScsibus4.removeFocusListener( (FocusListener)ui.txtScsibus4.getClientProperty( "normalizeListener" ));
						ui.txtScsibus4.setEnabled( false );
			
						ui.cbxScsibus5 = createComboBoxWithLabel( ui.Panel_PEB_WhtScsi, ui, "cbxScsibus5", new DefaultComboBoxModel<>( ui.SCSI_DEVICE_NAMES ), 82, 172, 96, 22, 0, e -> frame.collectEmulatorOptions(), "SCSI 6", 64 );
						ui.cbxScsibus5.setEnabled( false );
						ui.txtScsibus5 = createTextBox( ui.Panel_PEB_WhtScsi, "txtScsibus5", "", 178, 172, 160, 24, e -> frame.collectEmulatorOptions() );
						ui.txtScsibus5.removeFocusListener( (FocusListener)ui.txtScsibus5.getClientProperty( "normalizeListener" ));
						ui.txtScsibus5.setEnabled( false );
			
						ui.cbxScsibus6 = createComboBoxWithLabel( ui.Panel_PEB_WhtScsi, ui, "cbxScsibus6", new DefaultComboBoxModel<>( ui.SCSI_DEVICE_NAMES ), 82, 204, 96, 22, 0, e -> frame.collectEmulatorOptions(), "SCSI 7", 64 );
						ui.cbxScsibus6.setEnabled( false );
						ui.txtScsibus6 = createTextBox( ui.Panel_PEB_WhtScsi, "txtScsibus6", "", 178, 204, 160, 24, e -> frame.collectEmulatorOptions() );
						ui.txtScsibus6.removeFocusListener( (FocusListener)ui.txtScsibus6.getClientProperty( "normalizeListener" ));
						ui.txtScsibus6.setEnabled( false );
			
						ui.cbxScsibus7 = createComboBoxWithLabel( ui.Panel_PEB_WhtScsi, ui, "cbxScsibus7", new DefaultComboBoxModel<>(new String[] {"controller"}), 82, 236, 96, 22, 0, e -> frame.collectEmulatorOptions(), "SCSI 8", 64 );
						ui.cbxScsibus7.setEnabled( false );
						ui.txtScsibus7 = createTextBox( ui.Panel_PEB_WhtScsi, "txtScsibus7", "[internal]", 178, 236, 160, 24, e -> frame.collectEmulatorOptions() );
						ui.txtScsibus7.removeFocusListener( (FocusListener)ui.txtScsibus7.getClientProperty( "normalizeListener" ));
						ui.txtScsibus7.setEnabled( false );

					// layer 2: panel "speechsyn"
					ui.tabSideportDevices.addTab( "speechsyn", null, ui.Panel_SpeechSyn, null );

					// layer 2: panel "splitter"
					ui.tabSideportDevices.addTab( "splitter", null, ui.Panel_Splitter, null );

					// layer 2: panel "arcturus"
					ui.tabSideportDevices.addTab( "arcturus", null, ui.Panel_Arcturus, null );
	
		// private final JTextField localeField = new JTextField();
					
		log.debug( "----- end: initGUI()" );

    } // initGUI()

	// --------------------------------------------------
	
    public void postGUI() {
    	
		log.debug( "----- start: postGUI()" );
		
        PebDevicesController ctl = new PebDevicesController( ui.tabPebDevices );
        frame.setPebDevicesController( ctl );
    	
        if ( log.isDebugEnabled() || log.isTraceEnabled() ) {
            GridOverlayPanel grid = new GridOverlayPanel( 64, 32, 16, 16 );
            grid.setBounds( 0, 0, frame.getWidth(), frame.getHeight() );
            frame.getLayeredPane().add( grid, JLayeredPane.PALETTE_LAYER );
            frame.addComponentListener( new ComponentAdapter() {
                @Override
                public void componentResized( ComponentEvent e ) {
	    	    	log.debug( "##### event: Component resized" );
                    grid.setBounds( 0, 0, frame.getWidth(), frame.getHeight() );
                }
            });
        }
        
        frame.addWindowListener( Listeners.onOpenMainFrame( frame ));
        // log.info( "onOpenMainFrame registriert" );
        frame.addWindowListener( Listeners.onCloseMainFrame( frame ));
        // log.info( "onCloseMainFrame registriert" );
		
        // refreshUI();
        SwingUtilities.invokeLater(() -> refreshUI());
        
		log.debug( "----- end: postGUI()" );
		
    } // postGUI()

	// --------------------------------------------------
	
    public void refreshUI() {

		log.debug( "----- start: refreshUI()" );
		
        // File-Menü
        ui.menuFile.setText( I18n.t( "menu.file" ));
        ui.menuFileExit.setText( I18n.t( "menu.file.exit" ));

        // Language-Menü
        ui.menuLang.setText( I18n.t( "menu.lang" ));
        ui.menuLangEnglishGB.setText( I18n.t( "menu.lang.english_gb" ));
        ui.menuLangEnglishUS.setText( I18n.t( "menu.lang.english_us" ));
        ui.menuLangEnglishAU.setText( I18n.t( "menu.lang.english_au" ));
        ui.menuLangGermanDE.setText( I18n.t( "menu.lang.german_de" ));
        ui.menuLangGermanAT.setText( I18n.t( "menu.lang.german_at" ));
        ui.menuLangGermanCH.setText( I18n.t( "menu.lang.german_ch" ));
        ui.menuLangFrenchFR.setText( I18n.t( "menu.lang.french_fr" ));
        ui.menuLangItalianIT.setText( I18n.t( "menu.lang.italian_it" ));
        ui.menuLang.setIcon( IconLoader.load( "flag_" + I18n.getCurrentLanguageCode() + ".png" ));

        // Settings-Menü
        ui.menuSettings.setText( I18n.t( "menu.settings" ));
        ui.menuSettingsSave.setText( I18n.t( "menu.settings.save" ));
        ui.menuSettingsSaveAs.setText( I18n.t( "menu.settings.saveas" ));
        ui.menuSettingsLoad.setText( I18n.t( "menu.settings.load" ));
        if ( ui.menuSettingsPick != null ) {
        	ui.menuSettingsPick.setText( I18n.t( "menu.settings.pick" ));
	        ui.menuSettingsPickByCount.setText( I18n.t( "menu.settings.pick.bycount" ));
	        ui.menuSettingsPickByDate.setText( I18n.t( "menu.settings.pick.bydate" ));
	        if ( ui.menuSettingsNoSel != null ) {
	        	ui.menuSettingsNoSel.setText( I18n.t( "menu.settings.nosel" ));
	        }
	        ui.menuSettingsPickLoad.setText( I18n.t( "menu.settings.pick.load" ));
	        ui.menuSettingsPickDelete.setText( I18n.t( "menu.settings.pick.delete" ));
	        ui.menuSettingsPickRename.setText( I18n.t( "menu.settings.pick.rename" ));
        }
        
        // Hilfe-Menü
        ui.menuHelp.setText( I18n.t( "menu.help" ));
        ui.menuHelpAbout.setText( I18n.t( "menu.help.about" ));

        frame.setJMenuBar(frame.getJMenuBar());

        ui.lblWorkingDir.setText( I18n.t( "main.workingdir" ));
        ui.lblEmulator.setText( I18n.t( "main.executable" ));
        ui.lblRomPath.setText( I18n.t( "main.rompath" ));
        ui.lblCartPath.setText( I18n.t( "main.cartpath" ));
        ui.lblMachine.setText( I18n.t( "main.machine" ));
        ui.lblWorkingDir.setText( I18n.t( "main.workingdir" ));
        ui.lblGromPort.setText( I18n.t( "main.gromport" ));
        // ui.lblCartridge.setText( I18n.t( "main.cartridge" ));
        ui.lblJoystick.setText( I18n.t( "main.joyport" ));
        ui.lblIoPort.setText( I18n.t( "main.ioport" ));
        ui.lblFddPath.setText( I18n.t( "main.fddpath" ));
        ui.lblFlop1.setText( I18n.t( "main.flop1" ));
        ui.lblFlop2.setText( I18n.t( "main.flop2" ));
        ui.lblFlop3.setText( I18n.t( "main.flop3" ));
        ui.lblFlop4.setText( I18n.t( "main.flop4" ));
        ui.lblHddPath.setText( I18n.t( "main.hddpath" ));
        ui.lblHard1.setText( I18n.t( "main.hard1" ));
        ui.lblHard2.setText( I18n.t( "main.hard2" ));
        ui.lblHard3.setText( I18n.t( "main.hard3" ));
        ui.lblCassPath.setText( I18n.t( "main.casspath" ));
        ui.lblCass1.setText( I18n.t( "main.cass1" ));
        ui.lblCass2.setText( I18n.t( "main.cass2" ));
        ui.lblAddOpt.setText( I18n.t( "main.addopt" ));

        frame.revalidate();
        frame.repaint();

    	log.debug( "----- end: refreshUI()" );
    	
    }
    
	// --------------------------------------------------
	
} // class MainAppFrameBuilder

//############################################################################
