package com.miriki.ti99.mame.ui.util;

import java.awt.Component;
import java.awt.Window;
import java.awt.event.*;
import java.nio.file.Files;
// import java.io.File;
import java.nio.file.Path;
import java.util.Objects;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.miriki.ti99.mame.persistence.SettingsPathRegistry;
import com.miriki.ti99.mame.persistence.SettingsUsageRegistry;
import com.miriki.ti99.mame.ui.MainAppFrame;

import com.miriki.ti99.imagetools.domain.DiskFormat;
import com.miriki.ti99.imagetools.domain.DiskFormatPreset;
import com.miriki.ti99.imagetools.domain.Ti99File;
import com.miriki.ti99.imagetools.domain.Ti99Image;
import com.miriki.ti99.imagetools.domain.io.ImageFormatter;
import com.miriki.ti99.imagetools.fs.FileImporter;
import com.miriki.ti99.mame.tools.EmulatorStart;
import com.miriki.ti99.mame.tools.FileTools;

//############################################################################

public final class Listeners {

    private static final Logger log = LoggerFactory.getLogger( Listeners.class );
	
    // --------------------------------------------------
    
    private Listeners() {}

    // --------------------------------------------------
    // ACTION LISTENER
    // --------------------------------------------------
    

    public static ActionListener comboBoxChange( JComboBox<?> combo, ActionListener delegate ) {
        
    	 final Object[] lastValue = { combo.getSelectedItem() }; 
    	 
    	 return e -> {
    		
            log.debug( "##### event: ComboBox change at {}", combo.getName() );
            
            Window wnd = SwingUtilities.getWindowAncestor( combo );

        	if ( wnd instanceof MainAppFrame ) {
            	Object newValue = combo.getSelectedItem(); 
            	if ( ! Objects.equals(lastValue[0], newValue)) { 
            		lastValue[0] = newValue;
            // 	    Listeners.withEventsSuspended( frame, () -> { 
            		    delegate.actionPerformed( e );
        	// 	    });
            	}
        	}
                
        };
        
    } // comboBoxChange

    // --------------------------------------------------
    // DOCUMENT LISTENER
    // --------------------------------------------------
    
    public static DocumentListener documentAsAction( JTextField field, ActionListener delegate ) {
        
    	return new DocumentListener() {

            private void fire( String command ) {
            	
                log.debug( "##### event: TextBox changed / insert / remove at {}", field.getName() );
                
                Window w = SwingUtilities.getWindowAncestor( field );

                if ( w instanceof MainAppFrame frame && ! frame.getEventsSuspended() ) {
                    delegate.actionPerformed(
                        new ActionEvent( field, ActionEvent.ACTION_PERFORMED, command )
                    );
                }
                
            }

            @Override
            public void changedUpdate( DocumentEvent e ) {
            	
                fire( "changed" );
                
            }

            @Override
            public void insertUpdate( DocumentEvent e ) {
            	
                fire( "insert" );
                
            }

            @Override
            public void removeUpdate( DocumentEvent e ) {
            	
                fire( "remove" );
                
            }
            
        };
        
    } // documentAsAction

    // --------------------------------------------------
    // FOCUS LISTENER
    // --------------------------------------------------
    
    public static FocusListener normalizeOnFocusLost( JTextField field ) {
    	
        return new FocusAdapter() {
        	
            @Override
            public void focusLost( FocusEvent e ) {
            	
                log.debug( "##### event: TextBox leave at {}", field.getName() );
                
                String cleaned = FileTools.normalizeMultiPath( field.getText() );
                
                if ( ! cleaned.equals( field.getText() )) {
                	
                	Window wnd = SwingUtilities.getWindowAncestor( field );
                	
                	if ( wnd instanceof MainAppFrame frame ) {
                		Listeners.withEventsSuspended( frame, () -> { 
                			field.setText( cleaned ); 
                		});
                	}
                    
                }
                
            }
            
        };
        
    } // normalizeOnFocusLost

    // --------------------------------------------------
    // WINDOW LISTENER
    // --------------------------------------------------
    
    public static WindowListener onOpenMainFrame( MainAppFrame frame ) {
    	
        return new WindowAdapter() {
        	
            @Override
            public void windowOpened( WindowEvent e ) {
                log.debug("##### event: Window opened");
                
                frame.setEventsSuspended( false );
                
                SwingUtilities.invokeLater(() -> {
                	frame.collectEmulatorOptions();
                });
                
            }
            
        };
        
    } // onOpenMainFrame

    // --------------------------------------------------
        
    public static WindowListener onCloseMainFrame( MainAppFrame frame ) {
    	
        return new WindowAdapter() {
        	
            @Override
            public void windowClosing( WindowEvent e ) {
            	
                log.debug("##### event: Window closing");
                
                frame.saveSettings();
                
            }
            
        };
        
    } // onCloseMainFrame
    
    // --------------------------------------------------
    // BUTTON LISTENER
    // --------------------------------------------------
    
    public static MouseAdapter startEmulatorClick(MainAppFrame frame) { 
    
    	return new MouseAdapter() {
    		
			@Override
			public void mouseClicked( MouseEvent e ) {
		    	log.debug( "##### event: Button click at {}", ( (Component) e.getSource() ).getName() );
				EmulatorStart.emulatorStartProgram( frame.collectEmulatorOptions() );
		        // aktuellen Settings-Pfad holen und hochzählen
		        Path current = SettingsPathRegistry.getCurrent();
		        SettingsUsageRegistry.increment( current );
		        SettingsUsageRegistry.updateLastUsed(SettingsPathRegistry.getCurrent());
			}
			
		};

    } // startEmulatorClick

    // Erstes Verzeichnis
    // File chessDir = new File("C:/Users/mritt/AppData/Roaming/TI99MAME/ti99_fiad/chess");
    // new DiskBuilder(DiskFormat.DSDD, false).buildDiskFromDirectory(chessDir);

    // Zweites Verzeichnis
    // File xdtDir = new File("C:/Users/mritt/AppData/Roaming/TI99MAME/ti99_fiad/ti99xdt");
    // new DiskBuilder(DiskFormat.DSDD, false).buildDiskFromDirectory(xdtDir);

    public static MouseAdapter testFiadCreate() { 
        
        return new MouseAdapter() {
                
        	@Override
        	public void mouseClicked(MouseEvent e) {
        	    log.debug("##### event: Button click at {}", ((Component) e.getSource()).getName());

        	    try {

        	    	// --------------------
        	    	
        	    	Path targetDir = Path.of("C:/Users/mritt/AppData/Roaming/TI99MAME/ti99_fiad/chess");
        	        Path imagePath = targetDir.resolve("chess.dsk");

        	        // Image laden
        	        DiskFormatPreset preset = DiskFormatPreset.TI_SSSD;
        	        DiskFormat format = preset.getFormat();
        	        Ti99Image image = new Ti99Image(format);
        	        ImageFormatter.initialize(image);

        	    	// --------------------
        	    	
        	        // Host-Datei lesen
        	        Path hostFile = targetDir.resolve("br.svg");
        	        byte[] content = Files.readAllBytes(hostFile);

        	        // Ti99File erzeugen
        	        Ti99File tiFile = new Ti99File();
        	        tiFile.setFileName("BR");          // TI-99 Name (max 10 chars)
        	        tiFile.setFileType("PROGRAM");     // oder DIS/FIX etc.
        	        tiFile.setRecordLength(0);
        	        tiFile.setContent(content);

        	        // Datei importieren
        	        FileImporter.importFile(image, tiFile);

        	        // . . . . .
        	        
        	        // Host-Datei lesen
        	        hostFile = targetDir.resolve("bp.svg");
        	        content = Files.readAllBytes(hostFile);

        	        // Ti99File erzeugen
        	        tiFile = new Ti99File();
        	        tiFile.setFileName("BP");          // TI-99 Name (max 10 chars)
        	        tiFile.setFileType("PROGRAM");     // oder DIS/FIX etc.
        	        tiFile.setRecordLength(0);
        	        tiFile.setContent(content);

        	        // Datei importieren
        	        FileImporter.importFile(image, tiFile);

        	        // . . . . .
        	        
        	        // Host-Datei lesen
        	        hostFile = targetDir.resolve("wq.svg");
        	        content = Files.readAllBytes(hostFile);

        	        // Ti99File erzeugen
        	        tiFile = new Ti99File();
        	        tiFile.setFileName("WQ");          // TI-99 Name (max 10 chars)
        	        tiFile.setFileType("PROGRAM");     // oder DIS/FIX etc.
        	        tiFile.setRecordLength(0);
        	        tiFile.setContent(content);

        	        // Datei importieren
        	        FileImporter.importFile(image, tiFile);

        	        // . . . . .
        	        
        	        // Host-Datei lesen
        	        hostFile = targetDir.resolve("bk.svg");
        	        content = Files.readAllBytes(hostFile);

        	        // Ti99File erzeugen
        	        tiFile = new Ti99File();
        	        tiFile.setFileName("BK");          // TI-99 Name (max 10 chars)
        	        tiFile.setFileType("PROGRAM");     // oder DIS/FIX etc.
        	        tiFile.setRecordLength(0);
        	        tiFile.setContent(content);

        	        // Datei importieren
        	        FileImporter.importFile(image, tiFile);

        	        // . . . . .
        	        
        	        // Host-Datei lesen
        	        hostFile = targetDir.resolve("wn.svg");
        	        content = Files.readAllBytes(hostFile);

        	        // Ti99File erzeugen
        	        tiFile = new Ti99File();
        	        tiFile.setFileName("WN");          // TI-99 Name (max 10 chars)
        	        tiFile.setFileType("PROGRAM");     // oder DIS/FIX etc.
        	        tiFile.setRecordLength(0);
        	        tiFile.setContent(content);

        	        // Datei importieren
        	        FileImporter.importFile(image, tiFile);

        	        // . . . . .
        	    	
        	    	targetDir = Path.of("C:/Users/mritt/AppData/Roaming/TI99MAME/ti99_fiad/ti99xdt");

        	        // Host-Datei lesen
        	        hostFile = targetDir.resolve("xbas99.py");
        	        content = Files.readAllBytes(hostFile);

        	        // Ti99File erzeugen
        	        tiFile = new Ti99File();
        	        tiFile.setFileName("XBAS");          // TI-99 Name (max 10 chars)
        	        tiFile.setFileType("PROGRAM");     // oder DIS/FIX etc.
        	        tiFile.setRecordLength(0);
        	        tiFile.setContent(content);

        	        // Datei importieren
        	        FileImporter.importFile(image, tiFile);
        	        
        	        // --------------------
        	    	
        	        // Image speichern
        	        Files.write(imagePath, image.getRawData());

        	        log.info("Import abgeschlossen: {}", imagePath);

        	    } catch (Exception ex) {
        	        log.error("Fehler beim Erstellen der DSK-Images", ex);
        	    }
        	}
            
        };
    }
    
    // --------------------------------------------------
    
    public static void withEventsSuspended( MainAppFrame frame, Runnable action ) {
    	
    	boolean evtSuspOld = frame.getEventsSuspended();
        frame.setEventsSuspended( true );
        
        try {
            action.run();
            
        } finally {
            frame.setEventsSuspended( evtSuspOld );
            
        }
        
    } // withEventsSuspended
    
    // --------------------------------------------------
    
} // class Listeners

//############################################################################
