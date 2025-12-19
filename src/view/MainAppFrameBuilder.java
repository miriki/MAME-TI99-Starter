package view;

import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Stream;

import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.ComboBoxModel;
import javax.swing.DefaultComboBoxModel;
import javax.swing.Icon;
import javax.swing.JComboBox;
import javax.swing.JLayeredPane;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.MenuEvent;
import javax.swing.event.MenuListener;

import MameTools.MameTools;
import persistence.SettingsPathRegistry;
import persistence.SettingsUsageRegistry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import FileTools.FileTools;

//############################################################################

public class MainAppFrameBuilder {

	// --------------------------------------------------
	
    private static final Logger log = LoggerFactory.getLogger( MainAppFrameBuilder.class );
	
	private final MainAppFrame frame;
	private final MainAppFrameUI ui;
	
	// private MirikiSideportDevicesController ctlSideportDevices;		
	// private MirikiPebDevicesController ctlPebDevices;
	// private MirikiPebDevicesController ctlPebDevices = new MirikiPebDevicesController();

	// private static final Path SETTINGS_DIR = Paths.get(System.getProperty("user.dir"));
	
	// --------------------------------------------------
	
    MainAppFrameBuilder( MainAppFrame frame, MainAppFrameUI ui ) {

    	log.debug( "[constructor] MainAppFrameBuilder()" );

        this.frame = frame;
        this.ui = ui;
        // this.ctlPebDevices = new MirikiPebDevicesController(ui.tabPebDevices);
        
    } // [constructor] MainAppFrameBuilder()
	
	// --------------------------------------------------

    /*
    private JLabel createLabel( Container parent, String text, int x, int y, int w, int h ) {
    	return createLabel(parent, text, x, y, w, h, null); }
    */

    private JLabel createLabel( Container parent, String text, int x, int y, int w, int h, JComponent forComponent ) {
        JLabel lbl = new JLabel( text );
        lbl.setFont( new Font( "Arial", Font.PLAIN, 14 ));
        lbl.setBounds( x, y, w, h );
        if ( forComponent != null ) { lbl.setLabelFor( forComponent ); }
        parent.add( lbl );
        return lbl;
    } // createLabel

    /*
    private JTextField createTextBox( Container parent, String name, String text, int x, int y, int w, int h ) {
    	return createTextBox(parent, name, text, x, y, w, h, null); }
    */
    
    private JTextField createTextBox( Container parent, String name, String text, int x, int y, int w, int h, ActionListener listener ) {
    	JTextField txt = new JTextField();
    	txt.setName( name );
    	txt.setFont( new Font( "Arial", Font.PLAIN, 14 ));
    	txt.setText( text );
    	// txt.setColumns( 10 );
    	txt.setBounds( x, y, w, h );
    	txt.setHorizontalAlignment( SwingConstants.LEFT );
    	if ( listener != null ) {
	    	txt.getDocument().addDocumentListener( new DocumentListener() {
	    	    private void fire(ActionEvent e) {
	    	        Window w = SwingUtilities.getWindowAncestor(txt);
	    	        if (w instanceof MainAppFrame frame && !frame.isInitializing()) {
	    	            listener.actionPerformed(e);
	    	        }
	    	    }
	    	    @Override
	    	    public void changedUpdate(DocumentEvent e) {
	    	        fire(new ActionEvent(txt, ActionEvent.ACTION_PERFORMED, "changed"));
	    	    }
	    	    @Override
	    	    public void insertUpdate(DocumentEvent e) {
	    	        fire(new ActionEvent(txt, ActionEvent.ACTION_PERFORMED, "insert"));
	    	    }
	    	    @Override
	    	    public void removeUpdate(DocumentEvent e) {
	    	        fire(new ActionEvent(txt, ActionEvent.ACTION_PERFORMED, "remove"));
	    	    }
	    	});    	
	    	txt.addFocusListener(new FocusAdapter() {
	    	    @Override
	    	    public void focusLost(FocusEvent e) {
	    	        String cleaned = FileTools.normalizeMultiPath(txt.getText());
	    	        txt.setText(cleaned);
	    	    }
	    	});
	    }
    	parent.add( txt );
        return txt;
    } // createTextBox

    /*
    private JTextField createTextBoxWithLabel( Container parent, String name, String text, int x, int y, int w, int h, String textl, int wl ) {
    	return createTextBoxWithLabel( parent, name, text, x, y, w, h, null, textl, wl ); }
    */

    private JTextField createTextBoxWithLabel( Container parent, String name, String text, int x, int y, int w, int h, ActionListener listener, String textl, int wl ) {
    	JTextField txt = new JTextField();
        JLabel lbl = new JLabel( textl );
        txt = createTextBox( parent, name, text, x, y, w, h, listener );
    	lbl = createLabel( parent, textl, x - 8 - wl, y, wl, h, txt );
    	lbl.setHorizontalAlignment( SwingConstants.RIGHT );
        // return lbl;
        return txt;
    } // createTextBoxWithLabel
    
    private JComboBox<String> createComboBox( Container parent, String name, ComboBoxModel<String> model, int x, int y, int w, int h, int selectedIndex, ActionListener listener ) {
        JComboBox<String> cbx = new JComboBox<>( model );
        cbx.setName( name );
        // cbx.setSelectedIndex( selectedIndex );
        if (selectedIndex >= 0 && selectedIndex < model.getSize()) {
            cbx.setSelectedIndex(selectedIndex);
        } else {
            cbx.setSelectedIndex(0); // Fallback auf "------" oder ersten Eintrag
        }
        cbx.setFont( new Font("Arial", Font.PLAIN, 14 ));
        cbx.setBackground( Color.WHITE );
        cbx.setBounds( x, y, w, h );
        /*
        cbx.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                JLabel label = (JLabel) super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (!cbx.isEnabled()) {
                    // label.setForeground(new Color(80, 80, 80)); // dunkleres Grau
                    // label.setBackground(new Color(240, 240, 240)); // sanftes Grau
                    label.setForeground(new Color(0, 0, 0)); // dunkleres Grau
                    label.setBackground(new Color(255, 255, 255)); // sanftes Grau
                    label.setOpaque(true);
                }
                return label;
            }
        });
        */
        cbx.addActionListener(e -> {
            Window wnd = SwingUtilities.getWindowAncestor(cbx);
            if (wnd instanceof MainAppFrame frame && !frame.isInitializing()) {
                listener.actionPerformed(e);
            }
        });        
        parent.add( cbx );
        return cbx;
    } // createComboBox

    /*
    private JComboBox<String> createComboBoxWithLabel( Container parent, String name, ComboBoxModel<String> model, int x, int y, int w, int h, int selectedIndex, String textl, int wl ) {
    	return createComboBoxWithLabel( parent, name, model, x, y, w, h, selectedIndex, textl, wl ); }
    */
    
    private JComboBox<String> createComboBoxWithLabel( Container parent, String name, ComboBoxModel<String> model, int x, int y, int w, int h, int selectedIndex, ActionListener listener, String textl, int wl ) {
    	JComboBox<String> cbx = new JComboBox<>( model );
        JLabel lbl = new JLabel( textl );
        cbx = createComboBox( parent, name, model, x, y, w, h, selectedIndex, listener );
    	lbl = createLabel( parent, textl, x - 8 - wl, y, wl, h, cbx );
    	lbl.setHorizontalAlignment( SwingConstants.RIGHT );
        // return lbl;
        return cbx;
    } // createComboBoxWithLabel

    private Path chooseSettingsFile(boolean forSave) {
    	log.debug( "chooseSettingsFile( {} )", forSave );
        JFileChooser chooser = new JFileChooser();
        chooser.setDialogTitle(forSave ? "Save Settings As…" : "Load Settings");
        chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);

        // Filter auf .settings-Dateien
        chooser.setAcceptAllFileFilterUsed(true);
        chooser.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter(
                "Settings files (*.settings)", "settings"));

        // chooser.setSelectedFile(current.toFile());
        // Vorschlag: aktueller Pfad oder Default
        Path current = SettingsPathRegistry.getCurrent();
        if (current != null && Files.exists(current)) {
            chooser.setSelectedFile(current.toFile());
        } else {
            // chooser.setSelectedFile(null);
            SettingsPathRegistry.setCurrent(null);
        }

        int result = forSave
                ? chooser.showSaveDialog(frame)
                : chooser.showOpenDialog(frame);

        if (result == JFileChooser.APPROVE_OPTION) {
            File file = chooser.getSelectedFile();
            // Für Save: bei fehlender Extension automatisch anhängen
            if (forSave && !file.getName().toLowerCase().endsWith(".settings")) {
                file = new File(file.getParentFile(), file.getName() + ".settings");
            }
            return file.toPath();
        }
        return null;
    } // chooseSettingsFile
    
    private void doSaveAs() {
        log.debug("doSaveAs()");
        Path chosen = chooseSettingsFile(true);
        if (chosen == null) return;
        // Dateiname aus der Auswahl holen
        String rawName = chosen.getFileName().toString();
        // In sicheren Dateinamen umwandeln (Leerzeichen -> "_", ".settings" anhängen)
        String safeName = FileTools.safeFileName(rawName);
        // Pfad mit sicherem Namen im gleichen Verzeichnis bilden
        Path safePath = chosen.resolveSibling(safeName);
        // Diesen Pfad als aktuellen Settings-Pfad setzen
        SettingsPathRegistry.setCurrent(safePath);
        // Und darunter speichern
        frame.saveSettings();
    } // doSaveAs

    private void doLoad() {
    	log.debug( "doLoad()" );
        Path chosen = chooseSettingsFile(false);
        if (chosen == null) return;
        // 1. Datei selbst normalisieren
        chosen = normalizeSingleFile(chosen);
        // 2. Ganzes Verzeichnis normalisieren
        normalizeDirectory(chosen.getParent());
        // 3. Jetzt erst laden
        SettingsPathRegistry.setCurrent(chosen);
        frame.restoreSettings();
    } // doLoad
    
    private JMenu buildMenuFile() {
    	log.debug( "buildMenuFile()" );
    	JMenu result = new JMenu( "File" );

        JMenuItem exitItem = new JMenuItem("Exit");
        exitItem.addActionListener(e -> System.exit(0));
        result.add(exitItem);
    	
    	return result;
    } // buildMenuFile
    
    private JMenu buildMenuSettings() {
    	log.debug( "buildMenuSettings()" );
    	JMenu result = new JMenu( "Settings" );

    	result.addMenuListener(new MenuListener() {
	        @Override
	        public void menuSelected(MenuEvent e) {
	            rebuildSettingsMenu(result);
	        }
	        @Override public void menuDeselected(MenuEvent e) {}
	        @Override public void menuCanceled(MenuEvent e) {}
	    });
	    
        // Erste Initialisierung (z. B. leeres Gerüst)
        rebuildSettingsMenu( result );
        
        return result;
    } // buildMenuSettings

    private void normalizeDirectory(Path dir) {
        try (Stream<Path> stream = Files.list(dir)) {
            stream
                .filter(p -> p.getFileName().toString().endsWith(".settings"))
                .forEach(this::normalizeSingleFile);
        } catch (IOException ex) {
            log.error("Could not normalize directory {}", dir, ex);
        }
    }
    
    private Path normalizeSingleFile(Path p) {
        String fileName = p.getFileName().toString();
        String safe = FileTools.safeFileName(fileName);

        if (!safe.equals(fileName)) {
            Path newPath = p.resolveSibling(safe);
            try {
                Files.move(p, newPath);
                log.info("Normalized filename: {} → {}", fileName, safe);
                SettingsUsageRegistry.renameEntry(p, newPath);
                return newPath;
            } catch (IOException ex) {
                log.error("Could not normalize filename {} → {}", p, newPath, ex);
            }
        }
        return p;
    }
    
    private void rebuildSettingsMenu(JMenu result) {

        result.removeAll();

        JMenuItem saveItem = new JMenuItem("Save");
        saveItem.addActionListener(e -> {
            Path current = SettingsPathRegistry.getCurrent();
            if (!Files.exists(current)) {
                doSaveAs();
            } else {
                frame.saveSettings();
            }
        });
        result.add(saveItem);

        JMenuItem saveAsItem = new JMenuItem("Save As …");
        saveAsItem.addActionListener(e -> doSaveAs());
        result.add(saveAsItem);

        result.addSeparator();

        JMenuItem loadItem = new JMenuItem("Load");
        loadItem.addActionListener(e -> doLoad());
        result.add(loadItem);

        result.addSeparator();

        // Registry aufräumen
        SettingsUsageRegistry.cleanupInvalidEntries();
        SettingsPathRegistry.cleanupCurrentIfInvalid();

        // normalizeSettingsFiles();
        
        int maxPicklist = 5;
        List<Path> top = SettingsUsageRegistry.getTop(maxPicklist);

        if (top.isEmpty()) {
            JMenuItem none = new JMenuItem("No frequently used settings");
            none.setEnabled(false);
            result.add(none);
            return;
        }

        JMenu pickHeader = new JMenu("Pick Items");
        JMenuItem byCount = new JMenuItem("By Count");
        byCount.addActionListener(e -> {
            SettingsUsageRegistry.setSortMode(SettingsUsageRegistry.SortMode.BY_COUNT);
            rebuildSettingsMenu(result);
        });
        pickHeader.add(byCount);
        JMenuItem byDate = new JMenuItem("By Date");
        byDate.addActionListener(e -> {
            SettingsUsageRegistry.setSortMode(SettingsUsageRegistry.SortMode.BY_DATE);
            rebuildSettingsMenu(result);
        });
        pickHeader.add(byDate);
        result.add(pickHeader);

        for (Path p : top) {
            if (Files.exists(p)) {
                String label = FileTools.prettyName(p);
                Path current = SettingsPathRegistry.getCurrent();

                JMenu configMenu = new JMenu(label);

                // Optional: Radio-Marker
                if (p.equals(current)) {
                	Icon bulletIcon = new Icon() {
                	    @Override public int getIconWidth() { return 8; }
                	    @Override public int getIconHeight() { return 8; }
                	    @Override
                	    public void paintIcon(Component c, Graphics g, int x, int y) {
                	        g.fillOval(x, y, 7, 7);
                	    }
                	};
                    configMenu.setIcon(bulletIcon);
                }

                // --- Load-Eintrag ganz oben ---
                JMenuItem pickItem = new JMenuItem("Load");
                pickItem.addActionListener(e -> {
                    frame.saveSettings();
                    SettingsPathRegistry.setCurrent(p);
                    frame.restoreSettings();
                    frame.updateFrameTitle();
                    log.info("Switched settings to '{}'", p);
                });
                configMenu.add(pickItem);

                configMenu.addSeparator();

                // --- Delete ---
                JMenuItem deleteItem = new JMenuItem("Delete");
                deleteItem.addActionListener(e -> {
                    try {
                        Files.deleteIfExists(p);
                        SettingsUsageRegistry.removeEntry(p);
                        rebuildSettingsMenu(result);
                    } catch (IOException ex) {
                        log.error("Error deleting '{}'", p, ex);
                    }
                });
                configMenu.add(deleteItem);

                // --- Rename ---
                JMenuItem renameItem = new JMenuItem("Rename");
                renameItem.addActionListener(e -> {
                	String oldName = FileTools.editName(p);
                	String newName = JOptionPane.showInputDialog(frame, "Rename config:", oldName);
                	if (newName != null && !newName.isBlank()) {
                        String safe = FileTools.safeFileName(newName);
                        Path newPath = p.resolveSibling(safe);

                        try {
                            Files.move(p, newPath);
                            SettingsUsageRegistry.renameEntry(p, newPath);
                            SettingsPathRegistry.setCurrent(newPath);
                            frame.restoreSettings();
                            frame.updateFrameTitle();
                            rebuildSettingsMenu(result);

                        } catch (IOException ex) {
                            log.error("Rename failed: {} → {}", p, newPath, ex);
                            JOptionPane.showMessageDialog(frame,
                                "Could not rename file:\n" + ex.getMessage(),
                                "Rename Error",
                                JOptionPane.ERROR_MESSAGE);
                        }
                        
                    }
                });
                configMenu.add(renameItem);

                result.add(configMenu);
            }
        }

        result.revalidate();
        result.repaint();
    } // rebuildSettingsMenu
    
    private JMenu buildMenuHelp() {
    	JMenu result = new JMenu( "?" );

        JMenuItem aboutItem = new JMenuItem("About");
        aboutItem.addActionListener(e -> {
            JOptionPane.showMessageDialog(
                frame,
                "MAME TI99 Starter\n\nv0.99.27\n\n(c) 2025 by\n\nMichael 'miriki' Rittweger",
                "About",
                JOptionPane.INFORMATION_MESSAGE
            );
        });
        result.add(aboutItem);

    	return result;
    } // buildMenuHelp
    
   	private void buildMenuBar() {
    	
        // Menüleiste erstellen
        JMenuBar menuBar = new JMenuBar();

	        JMenu fileMenu = buildMenuFile();
		    JMenu settingsMenu = buildMenuSettings();
	        JMenu helpMenu = buildMenuHelp();

        // Menüs zur Menüleiste hinzufügen
        menuBar.add(fileMenu);
        menuBar.add(settingsMenu);
        menuBar.add(helpMenu);

        // Menüleiste ins Frame setzen
        frame.setJMenuBar(menuBar);

    } // buildMenuBar
    
    public void initGUI() {

		log.debug( "initGUI()" );

		UIManager.put("ComboBox.disabledForeground", new Color(80, 80, 80));     // Voll-Schwarz
		UIManager.put("ComboBox.disabledBackground", new Color(240, 240, 240)); // Voll-Weiß
		
		// layer 0: main panel
		Container contentPane = frame.getContentPane();
		// JPanel contentPane = new JPanel();
		contentPane.setLayout(null);

		buildMenuBar();
		
		// ----------
		
			// 2 columns: 112, 704

			// ui.txtWorkingDir = createTextBoxWithLabel( contentPane, "txtWorkingDir", "C:\\Users\\mritt\\AppData\\Roaming\\TI99MAME\\", 112, 16, 464, 24, e -> frame.collectEmulatorOptions(), "WorkingDir", 88 );
			ui.txtWorkingDir = createTextBoxWithLabel( contentPane, "txtWorkingDir", "<path>", 112, 16, 464, 24, e -> frame.collectEmulatorOptions(), "WorkingDir", 88 );
			// ui.txtEmulator = createTextBoxWithLabel( contentPane, "txtEmulator", "mame.exe", 704, 16, 464, 24, e -> frame.collectEmulatorOptions(), "Emulator", 88 );
			ui.txtEmulator = createTextBoxWithLabel( contentPane, "txtEmulator", "<executable>", 704, 16, 464, 24, e -> frame.collectEmulatorOptions(), "Emulator", 88 );
			
			// ui.txtRomPath = createTextBoxWithLabel( contentPane, "txtRomPath", "ti99_roms\\;ti99_peb\\", 112, 48, 464, 24, e -> frame.collectEmulatorOptions(), "RomPath", 88 );
			ui.txtRomPath = createTextBoxWithLabel( contentPane, "txtRomPath", "<ram path(s)>", 112, 48, 464, 24, e -> frame.collectEmulatorOptions(), "RomPath", 88 );
			// ui.txtCartPath = createTextBoxWithLabel( contentPane, "txtCartPath", "ti99_cart\\;ti99_cart.add\\", 704, 48, 464, 24, e -> frame.collectEmulatorOptions(), "CartPath", 88 );
			ui.txtCartPath = createTextBoxWithLabel( contentPane, "txtCartPath", "<cartridge path(s)>", 704, 48, 464, 24, e -> frame.collectEmulatorOptions(), "CartPath", 88 );
			
			// ui.cbxMachine = createComboBoxWithLabel( contentPane, "cbxMachine", new DefaultComboBoxModel<>( ui.MACHINE_NAMES ), 112, 80, 144, 22, 5, e -> frame.collectEmulatorOptions(), "Machine", 88 );
			ui.cbxMachine = createComboBoxWithLabel( contentPane, "cbxMachine", new DefaultComboBoxModel<>( ui.MACHINE_NAMES ), 112, 80, 144, 22, 0, e -> frame.collectEmulatorOptions(), "Machine", 88 );

			// ui.cbxGromPort = createComboBoxWithLabel( contentPane, "cbxGromPort", new DefaultComboBoxModel<>( ui.GROM_PORT_NAMES ), 360, 80, 144, 22, 1, e -> frame.collectEmulatorOptions(), "GromPort", 88 );
			ui.cbxGromPort = createComboBoxWithLabel( contentPane, "cbxGromPort", new DefaultComboBoxModel<>( ui.GROM_PORT_NAMES ), 360, 80, 144, 22, 0, e -> frame.collectEmulatorOptions(), "GromPort", 88 );
	
			// ui.cbxCartridge = createComboBox( contentPane, "cbxCartridge", new DefaultComboBoxModel<>( ui.CARTRIDGE_NAMES ), 512, 80, 144, 22, 1, e -> frame.collectEmulatorOptions() );
			ui.cbxCartridge = createComboBox( contentPane, "cbxCartridge", new DefaultComboBoxModel<>( ui.CARTRIDGE_NAMES ), 512, 80, 144, 22, 0, e -> frame.collectEmulatorOptions() );
			
			// ui.cbxJoystick = createComboBoxWithLabel( contentPane, "cbxJoystick", new DefaultComboBoxModel<>( ui.JOYSTICK_PORT_NAMES ), 768, 80, 144, 22, 0, e -> frame.collectEmulatorOptions(), "JoyPort", 88 );
			ui.cbxJoystick = createComboBoxWithLabel( contentPane, "cbxJoystick", new DefaultComboBoxModel<>( ui.JOYSTICK_PORT_NAMES ), 768, 80, 144, 22, 0, e -> frame.collectEmulatorOptions(), "JoyPort", 88 );
			
			// ui.cbxIoPort = createComboBoxWithLabel( contentPane, "cbxIoPort", new DefaultComboBoxModel<>( ui.SIDEPORT_DEVICE_NAMES ), 1024, 80, 144, 22, 1, e -> frame.collectEmulatorOptions(), "IoPort", 88 );
			ui.cbxIoPort = createComboBoxWithLabel( contentPane, "cbxIoPort", new DefaultComboBoxModel<>( ui.SIDEPORT_DEVICE_NAMES ), 1024, 80, 144, 22, 0, e -> frame.collectEmulatorOptions(), "IoPort", 88 );
			
			// ui.txtFddPath = createTextBoxWithLabel( contentPane, "txtFddPath", "ti99_fdsk\\", 112, 112, 464, 24, e -> frame.collectEmulatorOptions(), "FddPath", 88 );
			ui.txtFddPath = createTextBoxWithLabel( contentPane, "txtFddPath", "<floppydisk path>", 112, 112, 464, 24, e -> frame.collectEmulatorOptions(), "FddPath", 88 );
			
			// ui.txtHddPath = createTextBoxWithLabel( contentPane, "txtHddPath", "ti99_hdsk\\", 704, 112, 464, 24, e -> frame.collectEmulatorOptions(), "HddPath", 88 );
			ui.txtHddPath = createTextBoxWithLabel( contentPane, "txtHddPath", "<harddisk path>", 704, 112, 464, 24, e -> frame.collectEmulatorOptions(), "HddPath", 88 );
			
			// ui.txtCassPath = createTextBoxWithLabel( contentPane, "txtCassPath", "ti99_cass\\", 112, 144, 464, 24, e -> frame.collectEmulatorOptions(), "CassPath", 88 );
			ui.txtCassPath = createTextBoxWithLabel( contentPane, "txtCassPath", "<cassette path>", 112, 144, 464, 24, e -> frame.collectEmulatorOptions(), "CassPath", 88 );
	
			// ui.txtFlop1 = createTextBoxWithLabel( contentPane, "txtFlop1", "FlopDsk1.dsk", 112, 176, 216, 24, e -> frame.collectEmulatorOptions(), "Flop 1", 48 );
			// ui.txtFlop2 = createTextBoxWithLabel( contentPane, "txtFlop2", "FlopDsk2.dsk", 112+8+48+8+216, 176, 216, 24, e -> frame.collectEmulatorOptions(), "f2", 24 );
			// ui.txtFlop3 = createTextBoxWithLabel( contentPane, "txtFlop3", "", 112+8+48+216+8+8+48+8+216, 176, 216, 24, e -> frame.collectEmulatorOptions(), "f3", 24 );
			// ui.txtFlop4 = createTextBoxWithLabel( contentPane, "txtFlop4", "", 112+8+48+216+8+8+48+8+216+8+48+8+216, 176, 216, 24, e -> frame.collectEmulatorOptions(), "f4", 24 );
			ui.cbxFlop1 = createComboBoxWithLabel( contentPane, "cbxFlop1", new DefaultComboBoxModel<>( ui.FLOPPYDISKIMAGE_NAMES ), 112, 176, 216, 24, 0, e -> frame.collectEmulatorOptions(), "Flop 1", 48 );
			ui.cbxFlop2 = createComboBoxWithLabel( contentPane, "cbxFlop2", new DefaultComboBoxModel<>( ui.FLOPPYDISKIMAGE_NAMES ), 112+8+48+8+216, 176, 216, 24, 0, e -> frame.collectEmulatorOptions(), "f2", 24 );
			ui.cbxFlop3 = createComboBoxWithLabel( contentPane, "cbxFlop3", new DefaultComboBoxModel<>( ui.FLOPPYDISKIMAGE_NAMES ), 112+8+48+216+8+8+48+8+216, 176, 216, 24, 0, e -> frame.collectEmulatorOptions(), "f3", 24 );
			ui.cbxFlop4 = createComboBoxWithLabel( contentPane, "cbxFlop4", new DefaultComboBoxModel<>( ui.FLOPPYDISKIMAGE_NAMES ), 112+8+48+216+8+8+48+8+216+8+48+8+216, 176, 216, 24, 0, e -> frame.collectEmulatorOptions(), "f4", 24 );
	
			// ui.txtHard1 = createTextBoxWithLabel( contentPane, "txtHard1", "HardWds1.chd", 112, 208, 216, 24, e -> frame.collectEmulatorOptions(), "Hard 1", 48 );
			// ui.txtHard2 = createTextBoxWithLabel( contentPane, "txtHard2", "", 112+8+48+8+216, 208, 216, 24, e -> frame.collectEmulatorOptions(), "h2", 24 );
			// ui.txtHard3 = createTextBoxWithLabel( contentPane, "txtHard3", "", 112+8+48+216+8+8+48+8+216, 208, 216, 24, e -> frame.collectEmulatorOptions(), "h3", 24 );
			ui.cbxHard1 = createComboBoxWithLabel( contentPane, "cbxHard1", new DefaultComboBoxModel<>( ui.HARDDISKIMAGE_NAMES ), 112, 208, 216, 24, 0, e -> frame.collectEmulatorOptions(), "Hard 1", 48 );
			ui.cbxHard2 = createComboBoxWithLabel( contentPane, "cbxHard2", new DefaultComboBoxModel<>( ui.HARDDISKIMAGE_NAMES ), 112+8+48+8+216, 208, 216, 24, 0, e -> frame.collectEmulatorOptions(), "h2", 24 );
			ui.cbxHard3 = createComboBoxWithLabel( contentPane, "cbxHard3", new DefaultComboBoxModel<>( ui.HARDDISKIMAGE_NAMES ), 112+8+48+216+8+8+48+8+216, 208, 216, 24, 0, e -> frame.collectEmulatorOptions(), "h3", 24 );
			
			// ui.txtCass1 = createTextBoxWithLabel( contentPane, "txtCass1", "", 112, 240, 216, 24, e -> frame.collectEmulatorOptions(), "Cass 1", 48 );
			// ui.txtCass2 = createTextBoxWithLabel( contentPane, "txtCass2", "", 112+8+48+8+216, 240, 216, 24, e -> frame.collectEmulatorOptions(), "c2", 24 );
			ui.cbxCass1 = createComboBoxWithLabel( contentPane, "cbxCass1", new DefaultComboBoxModel<>( ui.CASSETTEIMAGE_NAMES ), 112, 240, 216, 24, 0, e -> frame.collectEmulatorOptions(), "Cass 1", 48 );
			ui.cbxCass2 = createComboBoxWithLabel( contentPane, "cbxCass2", new DefaultComboBoxModel<>( ui.CASSETTEIMAGE_NAMES ), 112+8+48+8+216, 240, 216, 24, 0, e -> frame.collectEmulatorOptions(), "c2", 24 );
			
			// ui.txtAddOpt = createTextBoxWithLabel( contentPane, "txtAddOpt", "-skip_gameinfo", 704, 272, 464, 24, e -> frame.collectEmulatorOptions(), "add. opt.", 88 );
			ui.txtAddOpt = createTextBoxWithLabel( contentPane, "txtAddOpt", "", 704, 272, 464, 24, e -> frame.collectEmulatorOptions(), "add. opt.", 88 );
			
			contentPane.add( ui.dbgEmulatorOptions );
			ui.dbgEmulatorOptions.setFont( new Font("Arial", Font.PLAIN, 14 ));
			ui.dbgEmulatorOptions.setLineWrap( true );
			ui.dbgEmulatorOptions.setBounds( 616, 304, 464+88, 320-8-32-8-24 );
	
			contentPane.add( ui.btnStartEmulator );
			ui.btnStartEmulator.setName( "btnStartEmulator" );
			ui.btnStartEmulator.setFont( new Font("Arial", Font.PLAIN, 14 ));
			ui.btnStartEmulator.setBounds( 616, 592-24, 464+88, 32 );
			ui.btnStartEmulator.addMouseListener( new MouseAdapter() {
				@Override
				public void mouseClicked( MouseEvent e ) {
					MameTools.emulatorStartProgram( frame.collectEmulatorOptions() );
			        // aktuellen Settings-Pfad holen und hochzählen
			        Path current = SettingsPathRegistry.getCurrent();
			        SettingsUsageRegistry.increment(current);
			        SettingsUsageRegistry.updateLastUsed(SettingsPathRegistry.getCurrent());
				}
			});

			// tabbed "sideport devices"
			// shows tabs "peb", "speechsyn", "splitter", "arcturus"
			ui.contentPane.add(ui.tabSideportDevices);
			ui.tabSideportDevices.setFont(new Font("Arial", Font.PLAIN, 14));
			ui.tabSideportDevices.setBounds(8, 272, 464+96+32, 352-24);

			// layer 1: tab "peb"
			ui.tabSideportDevices.addTab("peb", null, ui.Panel_PEB, null);
			ui.Panel_PEB.setLayout(new CardLayout(0, 0));

					// tabbed "peb"
					// shows tabs "peb" (slots) and "32kmem", ... "whtscsi" (devices)
					ui.Panel_PEB.add(ui.tabPebDevices, "tabPebDevices");
					ui.tabPebDevices.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);
		
					// layer 2: tab "peb"
					ui.tabPebDevices.addTab("peb", null, ui.Panel_PEB_PEB, null);
					ui.Panel_PEB_PEB.setLayout(null);
			
						// ui.cbxSlot1 = createComboBoxWithLabel( ui.Panel_PEB_PEB, "cbxSlot1", new DefaultComboBoxModel<>(MirikiPebDevices.comboModel()), 82, 12, 256, 22, 0, e -> frame.collectEmulatorOptions(), "Slot #1", 64 );
						ui.cbxSlot1 = createComboBoxWithLabel( ui.Panel_PEB_PEB, "cbxSlot1", new DefaultComboBoxModel<>( ui.PEB1_DEVICE_NAMES ), 82, 12, 256, 22, 0, e -> frame.collectEmulatorOptions(), "Slot #1", 64 );
						ui.cbxSlot1.setEnabled(false);
					
						// ui.cbxSlot2 = createComboBoxWithLabel( ui.Panel_PEB_PEB, "cbxSlot2", new DefaultComboBoxModel<>(MirikiPebDevices.comboModel()), 82, 12, 256, 22, 6, e -> frame.collectEmulatorOptions(), "Slot #2", 64 );
						ui.cbxSlot2 = createComboBoxWithLabel( ui.Panel_PEB_PEB, "cbxSlot2", new DefaultComboBoxModel<>(MirikiPebDevices.comboModel()), 82, 44, 256, 22, 0, e -> frame.collectEmulatorOptions(), "Slot #2", 64 );
						
						ui.cbxSlot3 = createComboBoxWithLabel( ui.Panel_PEB_PEB, "cbxSlot3", new DefaultComboBoxModel<>(MirikiPebDevices.comboModel()), 82, 76, 256, 22, 0, e -> frame.collectEmulatorOptions(), "Slot #3", 64 );
			
						// ui.cbxSlot4 = createComboBoxWithLabel( ui.Panel_PEB_PEB, "cbxSlot4", new DefaultComboBoxModel<>(MirikiPebDevices.comboModel()), 82, 84, 256, 22, 17, e -> frame.collectEmulatorOptions(), "Slot #4", 64 );
						ui.cbxSlot4 = createComboBoxWithLabel( ui.Panel_PEB_PEB, "cbxSlot4", new DefaultComboBoxModel<>(MirikiPebDevices.comboModel()), 82, 108, 256, 22, 0, e -> frame.collectEmulatorOptions(), "Slot #4", 64 );
			
						ui.cbxSlot5 = createComboBoxWithLabel( ui.Panel_PEB_PEB, "cbxSlot5", new DefaultComboBoxModel<>(MirikiPebDevices.comboModel()), 82, 140, 256, 22, 0, e -> frame.collectEmulatorOptions(), "Slot #5", 64 );
			
						// ui.cbxSlot6 = createComboBoxWithLabel( ui.Panel_PEB_PEB, "cbxSlot6", new DefaultComboBoxModel<>(MirikiPebDevices.comboModel()), 82, 156, 256, 22, 20, e -> frame.collectEmulatorOptions(), "Slot #6", 64 );
						ui.cbxSlot6 = createComboBoxWithLabel( ui.Panel_PEB_PEB, "cbxSlot6", new DefaultComboBoxModel<>(MirikiPebDevices.comboModel()), 82, 172, 256, 22, 0, e -> frame.collectEmulatorOptions(), "Slot #6", 64 );
			
						ui.cbxSlot7 = createComboBoxWithLabel( ui.Panel_PEB_PEB, "cbxSlot7", new DefaultComboBoxModel<>(MirikiPebDevices.comboModel()), 82, 204, 256, 22, 0, e -> frame.collectEmulatorOptions(), "Slot #7", 64 );
			
						// ui.cbxSlot8 = createComboBoxWithLabel( ui.Panel_PEB_PEB, "cbxSlot8", new DefaultComboBoxModel<>(MirikiPebDevices.comboModel()), 82, 228, 256, 22, 8, e -> frame.collectEmulatorOptions(), "Slot #8", 64 );
						ui.cbxSlot8 = createComboBoxWithLabel( ui.Panel_PEB_PEB, "cbxSlot8", new DefaultComboBoxModel<>(MirikiPebDevices.comboModel()), 82, 236, 256, 22, 0, e -> frame.collectEmulatorOptions(), "Slot #8", 64 );
			
					// layer 2: tab "32kmem"
					ui.tabPebDevices.addTab("32kmem", null, ui.Panel_PEB_32kMem, null);
					ui.Panel_PEB_32kMem.setLayout(null);
			
					// layer 2: tab "bwg"
					ui.tabPebDevices.addTab("bwg", null, ui.Panel_PEB_Bwg, null);
					ui.Panel_PEB_Bwg.setLayout(null);
			
						// ui.cbxBwg0 = createComboBoxWithLabel( ui.Panel_PEB_Bwg, "cbxBwg0", new DefaultComboBoxModel<>( ui.BWG_FDD_NAMES ), 82, 12, 96, 22, 2, e -> frame.collectEmulatorOptions(), "Flop 1", 64 );
						ui.cbxBwg0 = createComboBoxWithLabel( ui.Panel_PEB_Bwg, "cbxBwg0", new DefaultComboBoxModel<>( ui.BWG_FDD_NAMES ), 82, 12, 96, 22, 0, e -> frame.collectEmulatorOptions(), "Flop 1", 64 );
			
						// ui.cbxBwg1 = createComboBoxWithLabel( ui.Panel_PEB_Bwg, "cbxBwg1", new DefaultComboBoxModel<>( ui.BWG_FDD_NAMES ), 82, 48, 96, 22, 2, e -> frame.collectEmulatorOptions(), "Flop 2", 64 );
						ui.cbxBwg1 = createComboBoxWithLabel( ui.Panel_PEB_Bwg, "cbxBwg1", new DefaultComboBoxModel<>( ui.BWG_FDD_NAMES ), 82, 48, 96, 22, 0, e -> frame.collectEmulatorOptions(), "Flop 2", 64 );
			
						ui.cbxBwg2 = createComboBoxWithLabel( ui.Panel_PEB_Bwg, "cbxBwg2", new DefaultComboBoxModel<>( ui.BWG_FDD_NAMES ), 82, 84, 96, 22, 0, e -> frame.collectEmulatorOptions(), "Flop 3", 64 );
			
						ui.cbxBwg3 = createComboBoxWithLabel( ui.Panel_PEB_Bwg, "cbxBwg3", new DefaultComboBoxModel<>( ui.BWG_FDD_NAMES ), 82, 120, 96, 22, 0, e -> frame.collectEmulatorOptions(), "Flop 4", 64 );
			
					// layer 2: tab "ccdcc"
					ui.tabPebDevices.addTab("ccdcc", null, ui.Panel_PEB_CcDcc, null);
					ui.Panel_PEB_CcDcc.setLayout(null);
			
						// ui.cbxCcdcc0 = createComboBoxWithLabel( ui.Panel_PEB_CcDcc, "cbxCcdcc0", new DefaultComboBoxModel<>( ui.CCDCC_FDD_NAMES ), 82, 12, 96, 22, 2, e -> frame.collectEmulatorOptions(), "Flop 1", 64 );
						ui.cbxCcdcc0 = createComboBoxWithLabel( ui.Panel_PEB_CcDcc, "cbxCcdcc0", new DefaultComboBoxModel<>( ui.CCDCC_FDD_NAMES ), 82, 12, 96, 22, 0, e -> frame.collectEmulatorOptions(), "Flop 1", 64 );
			
						// ui.cbxCcdcc1 = createComboBoxWithLabel( ui.Panel_PEB_CcDcc, "cbxCcdcc1", new DefaultComboBoxModel<>( ui.CCDCC_FDD_NAMES ), 82, 48, 96, 22, 2, e -> frame.collectEmulatorOptions(), "Flop 2", 64 );
						ui.cbxCcdcc1 = createComboBoxWithLabel( ui.Panel_PEB_CcDcc, "cbxCcdcc1", new DefaultComboBoxModel<>( ui.CCDCC_FDD_NAMES ), 82, 48, 96, 22, 0, e -> frame.collectEmulatorOptions(), "Flop 2", 64 );
			
						ui.cbxCcdcc2 = createComboBoxWithLabel( ui.Panel_PEB_CcDcc, "cbxCcdcc2", new DefaultComboBoxModel<>( ui.CCDCC_FDD_NAMES ), 82, 84, 96, 22, 0, e -> frame.collectEmulatorOptions(), "Flop 3", 64 );
			
						ui.cbxCcdcc3 = createComboBoxWithLabel( ui.Panel_PEB_CcDcc, "cbxCcdcc3", new DefaultComboBoxModel<>( ui.CCDCC_FDD_NAMES ), 82, 120, 96, 22, 0, e -> frame.collectEmulatorOptions(), "Flop 4", 64 );
			
					// layer 2: tab "ccfdc"
					ui.tabPebDevices.addTab("ccfdc", null, ui.Panel_PEB_CcFdc, null);
					ui.Panel_PEB_CcFdc.setLayout(null);
			
						// ui.cbxCcfdc0 = createComboBoxWithLabel( ui.Panel_PEB_CcFdc, "cbxCcfdc0", new DefaultComboBoxModel<>( ui.CCFDC_FDD_NAMES ), 82, 12, 96, 22, 2, e -> frame.collectEmulatorOptions(), "Flop 1", 64 );
						ui.cbxCcfdc0 = createComboBoxWithLabel( ui.Panel_PEB_CcFdc, "cbxCcfdc0", new DefaultComboBoxModel<>( ui.CCFDC_FDD_NAMES ), 82, 12, 96, 22, 0, e -> frame.collectEmulatorOptions(), "Flop 1", 64 );
			
						// ui.cbxCcfdc1 = createComboBoxWithLabel( ui.Panel_PEB_CcFdc, "cbxCcfdc1", new DefaultComboBoxModel<>( ui.CCFDC_FDD_NAMES ), 82, 48, 96, 22, 2, e -> frame.collectEmulatorOptions(), "Flop 2", 64 );
						ui.cbxCcfdc1 = createComboBoxWithLabel( ui.Panel_PEB_CcFdc, "cbxCcfdc1", new DefaultComboBoxModel<>( ui.CCFDC_FDD_NAMES ), 82, 48, 96, 22, 0, e -> frame.collectEmulatorOptions(), "Flop 2", 64 );
			
						ui.cbxCcfdc2 = createComboBoxWithLabel( ui.Panel_PEB_CcFdc, "cbxCcfdc2", new DefaultComboBoxModel<>( ui.CCFDC_FDD_NAMES ), 82, 84, 96, 22, 0, e -> frame.collectEmulatorOptions(), "Flop 3", 64 );
			
						ui.cbxCcfdc3 = createComboBoxWithLabel( ui.Panel_PEB_CcFdc, "cbxCcfdc3", new DefaultComboBoxModel<>( ui.CCFDC_FDD_NAMES ), 82, 120, 96, 22, 0, e -> frame.collectEmulatorOptions(), "Flop 4", 64 );
			
					// layer 2: tab "ddcc1"
					ui.tabPebDevices.addTab("ddcc1", null, ui.Panel_PEB_DDcc1, null);
					ui.Panel_PEB_DDcc1.setLayout(null);
			
						// ui.cbxDdcc0 = createComboBoxWithLabel( ui.Panel_PEB_DDcc1, "cbxDdcc0", new DefaultComboBoxModel<>( ui.DDCC1_FDD_NAMES ), 82, 12, 96, 22, 2, e -> frame.collectEmulatorOptions(), "Flop 1", 64 );
						ui.cbxDdcc0 = createComboBoxWithLabel( ui.Panel_PEB_DDcc1, "cbxDdcc0", new DefaultComboBoxModel<>( ui.DDCC1_FDD_NAMES ), 82, 12, 96, 22, 0, e -> frame.collectEmulatorOptions(), "Flop 1", 64 );
			
						// ui.cbxDdcc1 = createComboBoxWithLabel( ui.Panel_PEB_DDcc1, "cbxDdcc1", new DefaultComboBoxModel<>( ui.DDCC1_FDD_NAMES ), 82, 48, 96, 22, 2, e -> frame.collectEmulatorOptions(), "Flop 2", 64 );
						ui.cbxDdcc1 = createComboBoxWithLabel( ui.Panel_PEB_DDcc1, "cbxDdcc1", new DefaultComboBoxModel<>( ui.DDCC1_FDD_NAMES ), 82, 48, 96, 22, 0, e -> frame.collectEmulatorOptions(), "Flop 2", 64 );
			
						ui.cbxDdcc2 = createComboBoxWithLabel( ui.Panel_PEB_DDcc1, "cbxDdcc2", new DefaultComboBoxModel<>( ui.DDCC1_FDD_NAMES ), 82, 84, 96, 22, 0, e -> frame.collectEmulatorOptions(), "Flop 3", 64 );
			
						ui.cbxDdcc3 = createComboBoxWithLabel( ui.Panel_PEB_DDcc1, "cbxDdcc3", new DefaultComboBoxModel<>( ui.DDCC1_FDD_NAMES ), 82, 120, 96, 22, 0, e -> frame.collectEmulatorOptions(), "Flop 4", 64 );
			
					// layer 2: tab "evpc"
					ui.tabPebDevices.addTab("evpc", null, ui.Panel_PEB_Evpc, null);
					ui.Panel_PEB_Evpc.setLayout(null);
			
						ui.cbxColorbus = createComboBoxWithLabel( ui.Panel_PEB_Evpc, "cbxColorbus", new DefaultComboBoxModel<>( ui.EVPC_DEVICE_NAMES ), 82, 12, 256, 22, 0, e -> frame.collectEmulatorOptions(), "ColorBus", 64 );
			
					// layer 2: tab "forti"
					ui.tabPebDevices.addTab("forti", null, ui.Panel_PEB_Forti, null);
					ui.Panel_PEB_Forti.setLayout(null);
			
					// layer 2: tab "hfdc"
					ui.tabPebDevices.addTab("hfdc", null, ui.Panel_PEB_HFdc, null);
					ui.Panel_PEB_HFdc.setLayout(null);
			
						// ui.cbxHfdcF1 = createComboBoxWithLabel( ui.Panel_PEB_HFdc, "cbxHfdcF1", new DefaultComboBoxModel<>( ui.HFDC_FDD_NAMES ), 82, 12, 96, 22, 3, e -> frame.collectEmulatorOptions(), "Flop 1", 64 );
						ui.cbxHfdcF1 = createComboBoxWithLabel( ui.Panel_PEB_HFdc, "cbxHfdcF1", new DefaultComboBoxModel<>( ui.HFDC_FDD_NAMES ), 82, 12, 96, 22, 0, e -> frame.collectEmulatorOptions(), "Flop 1", 64 );
			
						// ui.cbxHfdcF2 = createComboBoxWithLabel( ui.Panel_PEB_HFdc, "cbxHfdcF2", new DefaultComboBoxModel<>( ui.HFDC_FDD_NAMES ), 82, 48, 96, 22, 3, e -> frame.collectEmulatorOptions(), "Flop 2", 64 );
						ui.cbxHfdcF2 = createComboBoxWithLabel( ui.Panel_PEB_HFdc, "cbxHfdcF2", new DefaultComboBoxModel<>( ui.HFDC_FDD_NAMES ), 82, 48, 96, 22, 0, e -> frame.collectEmulatorOptions(), "Flop 2", 64 );
			
						ui.cbxHfdcF3 = createComboBoxWithLabel( ui.Panel_PEB_HFdc, "cbxHfdcF3", new DefaultComboBoxModel<>( ui.HFDC_FDD_NAMES ), 82, 84, 96, 22, 0, e -> frame.collectEmulatorOptions(), "Flop 3", 64 );
			
						ui.cbxHfdcF4 = createComboBoxWithLabel( ui.Panel_PEB_HFdc, "cbxHfdcF4", new DefaultComboBoxModel<>( ui.HFDC_FDD_NAMES ), 82, 120, 96, 22, 0, e -> frame.collectEmulatorOptions(), "Flop 4", 64 );
			
						// ui.cbxHfdcH1 = createComboBoxWithLabel( ui.Panel_PEB_HFdc, "cbxHfdcH1", new DefaultComboBoxModel<>( ui.HFDC_HDD_NAMES ), 82, 156, 96, 22, 1, e -> frame.collectEmulatorOptions(), "Hard 1", 64 );
						ui.cbxHfdcH1 = createComboBoxWithLabel( ui.Panel_PEB_HFdc, "cbxHfdcH1", new DefaultComboBoxModel<>( ui.HFDC_HDD_NAMES ), 82, 156, 96, 22, 0, e -> frame.collectEmulatorOptions(), "Hard 1", 64 );
			
						ui.cbxHfdcH2 = createComboBoxWithLabel( ui.Panel_PEB_HFdc, "cbxHfdcH2", new DefaultComboBoxModel<>( ui.HFDC_HDD_NAMES ), 82, 192, 96, 22, 0, e -> frame.collectEmulatorOptions(), "Hard 2", 64 );
			
						ui.cbxHfdcH3 = createComboBoxWithLabel( ui.Panel_PEB_HFdc, "cbxHfdcH3", new DefaultComboBoxModel<>( ui.HFDC_HDD_NAMES ), 82, 228, 96, 22, 0, e -> frame.collectEmulatorOptions(), "Hard 3", 64 );
			
					// layer 2: tab "horizon"
					ui.tabPebDevices.addTab("horizon", null, ui.Panel_PEB_Horizon, null);
					ui.Panel_PEB_Horizon.setLayout(null);
			
					// layer 2: tab "hsgpl"
					ui.tabPebDevices.addTab("hsgpl", null, ui.Panel_PEB_HsGpl, null);
					ui.Panel_PEB_HsGpl.setLayout(null);
			
					// layer 2: tab "ide"
					ui.tabPebDevices.addTab("ide", null, ui.Panel_PEB_Ide, null);
					ui.Panel_PEB_Ide.setLayout(null);
			
						ui.cbxAta0 = createComboBoxWithLabel( ui.Panel_PEB_Ide, "cbxAta0", new DefaultComboBoxModel<>( ui.IDE_DEVICE_NAMES ), 82, 12, 96, 22, 0, e -> frame.collectEmulatorOptions(), "IDE 1", 64 );
			
						ui.cbxAta1 = createComboBoxWithLabel( ui.Panel_PEB_Ide, "cbxAta1", new DefaultComboBoxModel<>( ui.IDE_DEVICE_NAMES ), 82, 48, 96, 22, 0, e -> frame.collectEmulatorOptions(), "IDE 2", 64 );
			
					// layer 2: tab "myarcmem"
					ui.tabPebDevices.addTab("myarcmem", null, ui.Panel_PEB_MyarcMem, null);
					ui.Panel_PEB_MyarcMem.setLayout(null);
			
					// layer 2: tab "pcode"
					ui.tabPebDevices.addTab("pcode", null, ui.Panel_PEB_PCode, null);
					ui.Panel_PEB_PCode.setLayout(null);
			
					// layer 2: tab "pgram"
					ui.tabPebDevices.addTab("pgram", null, ui.Panel_PEB_PGram, null);
					ui.Panel_PEB_PGram.setLayout(null);
			
					// layer 2: tab "samsmem"
					ui.tabPebDevices.addTab("samsmem", null, ui.Panel_PEB_SamsMem, null);
					ui.Panel_PEB_SamsMem.setLayout(null);
			
					// layer 2: tab "sidmaster"
					ui.tabPebDevices.addTab("sidmaster", null, ui.Panel_PEB_SidMaster, null);
					ui.Panel_PEB_SidMaster.setLayout(null);
			
					// layer 2: tab "speechadapter"
					ui.tabPebDevices.addTab("speechadapter", null, ui.Panel_PEB_SpeechAdapter, null);
					ui.Panel_PEB_SpeechAdapter.setLayout(null);
			
					// layer 2: tab "tifdc"
					ui.tabPebDevices.addTab("tifdc", null, ui.Panel_PEB_TiFdc, null);
					ui.Panel_PEB_TiFdc.setLayout(null);
			
						// ui.cbxTifdc0 = createComboBoxWithLabel( ui.Panel_PEB_TiFdc, "cbxTifdc0", new DefaultComboBoxModel<>( ui.TIFDC_FDD_NAMES ), 82, 12, 96, 22, 1, e -> frame.collectEmulatorOptions(), "Flop 1", 64 );
						ui.cbxTifdc0 = createComboBoxWithLabel( ui.Panel_PEB_TiFdc, "cbxTifdc0", new DefaultComboBoxModel<>( ui.TIFDC_FDD_NAMES ), 82, 12, 96, 22, 0, e -> frame.collectEmulatorOptions(), "Flop 1", 64 );
			
						// ui.cbxTifdc1 = createComboBoxWithLabel( ui.Panel_PEB_TiFdc, "cbxTifdc1", new DefaultComboBoxModel<>( ui.TIFDC_FDD_NAMES ), 82, 48, 96, 22, 1, e -> frame.collectEmulatorOptions(), "Flop 2", 64 );
						ui.cbxTifdc1 = createComboBoxWithLabel( ui.Panel_PEB_TiFdc, "cbxTifdc1", new DefaultComboBoxModel<>( ui.TIFDC_FDD_NAMES ), 82, 48, 96, 22, 0, e -> frame.collectEmulatorOptions(), "Flop 2", 64 );
			
						ui.cbxTifdc2 = createComboBoxWithLabel( ui.Panel_PEB_TiFdc, "cbxTifdc2", new DefaultComboBoxModel<>( ui.TIFDC_FDD_NAMES ), 82, 84, 96, 22, 0, e -> frame.collectEmulatorOptions(), "Flop 3", 64 );
			
					// layer 2: tab "tipi"
					ui.tabPebDevices.addTab("tipi", null, ui.Panel_PEB_TiPi, null);
					ui.Panel_PEB_TiPi.setLayout(null);
			
					// layer 2: tab "tirs232"
					ui.tabPebDevices.addTab("tirs232", null, ui.Panel_PEB_TiRs232, null);
					ui.Panel_PEB_TiRs232.setLayout(null);
			
					// layer 2: tab "usbsm"
					ui.tabPebDevices.addTab("usbsm", null, ui.Panel_PEB_UsbSm, null);
					ui.Panel_PEB_UsbSm.setLayout(null);
			
					// layer 2: tab "whtscsi"
					ui.tabPebDevices.addTab("whtscsi", null, ui.Panel_PEB_WhtScsi, null);
					ui.Panel_PEB_WhtScsi.setLayout(null);
			
						ui.cbxScsibus0 = createComboBoxWithLabel( ui.Panel_PEB_WhtScsi, "cbxScsibus0", new DefaultComboBoxModel<>( ui.SCSI_DEVICE_NAMES ), 82, 12, 96, 22, 0, e -> frame.collectEmulatorOptions(), "SCSI 1", 64 );
						ui.cbxScsibus0.setEnabled(false);
						ui.txtScsibus0 = createTextBox( ui.Panel_PEB_WhtScsi, "txtScsibus0", "", 178, 12, 160, 24, e -> frame.collectEmulatorOptions() );
						ui.txtScsibus0.setEnabled(false);
			
						ui.cbxScsibus1 = createComboBoxWithLabel( ui.Panel_PEB_WhtScsi, "cbxScsibus1", new DefaultComboBoxModel<>( ui.SCSI_DEVICE_NAMES ), 82, 44, 96, 22, 0, e -> frame.collectEmulatorOptions(), "SCSI 2", 64 );
						ui.cbxScsibus1.setEnabled(false);
						ui.txtScsibus1 = createTextBox( ui.Panel_PEB_WhtScsi, "txtScsibus1", "", 178, 44, 160, 24, e -> frame.collectEmulatorOptions() );
						ui.txtScsibus1.setEnabled(false);
			
						ui.cbxScsibus2 = createComboBoxWithLabel( ui.Panel_PEB_WhtScsi, "cbxScsibus2", new DefaultComboBoxModel<>( ui.SCSI_DEVICE_NAMES ), 82, 76, 96, 22, 0, e -> frame.collectEmulatorOptions(), "SCSI 3", 64 );
						ui.cbxScsibus2.setEnabled(false);
						ui.txtScsibus2 = createTextBox( ui.Panel_PEB_WhtScsi, "txtScsibus2", "", 178, 76, 160, 24, e -> frame.collectEmulatorOptions() );
						ui.txtScsibus2.setEnabled(false);
			
						ui.cbxScsibus3 = createComboBoxWithLabel( ui.Panel_PEB_WhtScsi, "cbxScsibus3", new DefaultComboBoxModel<>( ui.SCSI_DEVICE_NAMES ), 82, 108, 96, 22, 0, e -> frame.collectEmulatorOptions(), "SCSI 4", 64 );
						ui.cbxScsibus3.setEnabled(false);
						ui.txtScsibus3 = createTextBox( ui.Panel_PEB_WhtScsi, "txtScsibus3", "", 178, 108, 160, 24, e -> frame.collectEmulatorOptions() );
						ui.txtScsibus3.setEnabled(false);
			
						ui.cbxScsibus4 = createComboBoxWithLabel( ui.Panel_PEB_WhtScsi, "cbxScsibus4", new DefaultComboBoxModel<>( ui.SCSI_DEVICE_NAMES ), 82, 140, 96, 22, 0, e -> frame.collectEmulatorOptions(), "SCSI 5", 64 );
						ui.cbxScsibus4.setEnabled(false);
						ui.txtScsibus4 = createTextBox( ui.Panel_PEB_WhtScsi, "txtScsibus4", "", 178, 140, 160, 24, e -> frame.collectEmulatorOptions() );
						ui.txtScsibus4.setEnabled(false);
			
						ui.cbxScsibus5 = createComboBoxWithLabel( ui.Panel_PEB_WhtScsi, "cbxScsibus5", new DefaultComboBoxModel<>( ui.SCSI_DEVICE_NAMES ), 82, 172, 96, 22, 0, e -> frame.collectEmulatorOptions(), "SCSI 6", 64 );
						ui.cbxScsibus5.setEnabled(false);
						ui.txtScsibus5 = createTextBox( ui.Panel_PEB_WhtScsi, "txtScsibus5", "", 178, 172, 160, 24, e -> frame.collectEmulatorOptions() );
						ui.txtScsibus5.setEnabled(false);
			
						ui.cbxScsibus6 = createComboBoxWithLabel( ui.Panel_PEB_WhtScsi, "cbxScsibus6", new DefaultComboBoxModel<>( ui.SCSI_DEVICE_NAMES ), 82, 204, 96, 22, 0, e -> frame.collectEmulatorOptions(), "SCSI 7", 64 );
						ui.cbxScsibus6.setEnabled(false);
						ui.txtScsibus6 = createTextBox( ui.Panel_PEB_WhtScsi, "txtScsibus6", "", 178, 204, 160, 24, e -> frame.collectEmulatorOptions() );
						ui.txtScsibus6.setEnabled(false);
			
						ui.cbxScsibus7 = createComboBoxWithLabel( ui.Panel_PEB_WhtScsi, "cbxScsibus7", new DefaultComboBoxModel<>(new String[] {"controller"}), 82, 236, 96, 22, 0, e -> frame.collectEmulatorOptions(), "SCSI 8", 64 );
						ui.cbxScsibus7.setEnabled(false);
						ui.txtScsibus7 = createTextBox( ui.Panel_PEB_WhtScsi, "txtScsibus7", "[internal]", 178, 236, 160, 24, e -> frame.collectEmulatorOptions() );
						ui.txtScsibus7.setEnabled(false);

					// layer 2: panel "speechsyn"
					ui.tabSideportDevices.addTab("speechsyn", null, ui.Panel_SpeechSyn, null);

					// layer 2: panel "splitter"
					ui.tabSideportDevices.addTab("splitter", null, ui.Panel_Splitter, null);

					// layer 2: panel "arcturus"
					ui.tabSideportDevices.addTab("arcturus", null, ui.Panel_Arcturus, null);
	
    } // initGUI()

	// --------------------------------------------------
	
    public void postGUI() {
    	
		log.debug( "postGUI()" );
		
		long t0 = System.currentTimeMillis();
        // this.ctlPebDevices = new MirikiPebDevicesController(ui.tabPebDevices);
        MirikiPebDevicesController ctl = new MirikiPebDevicesController( ui.tabPebDevices );
    	// log.warn( "    PebDevicesController init: {} ms", System.currentTimeMillis() - t0 );
        frame.setPebDevicesController( ctl );
    	
        if ( log.isDebugEnabled() || log.isTraceEnabled() ) {
    		t0 = System.currentTimeMillis();
            GridOverlayPanel grid = new GridOverlayPanel( 64, 32, 16, 16 );
        	log.warn( "    GridOverlayPanel init: {} ms", System.currentTimeMillis() - t0 );
            grid.setBounds( 0, 0, frame.getWidth(), frame.getHeight() );
            frame.getLayeredPane().add( grid, JLayeredPane.PALETTE_LAYER );
            frame.addComponentListener( new ComponentAdapter() {
                @Override
                public void componentResized( ComponentEvent e ) {
                    grid.setBounds( 0, 0, frame.getWidth(), frame.getHeight() );
                }
            });
        }
        
        frame.addWindowListener( new WindowAdapter() {

        	@Override
		    public void windowClosing( WindowEvent e ) {
		        frame.saveSettings();
		    }
		      
	        @Override
	        public void windowOpened(WindowEvent e) {
	            // Jetzt ist das Fenster sichtbar, Initialisierung abgeschlossen
	            frame.setInitializing(false);
	            frame.collectEmulatorOptions();
	            // log.warn("Initialization finished, flag set to false");
	        }

        });
		
    } // postGUI()

	// --------------------------------------------------
	
} // class MainAppFrameBuilder

//############################################################################
