package com.miriki.ti99.mame.ui.menu;

import javax.swing.*;
import javax.swing.event.*;
import java.nio.file.*;
import java.awt.Component;
import java.awt.Graphics;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.stream.Stream;

import com.miriki.ti99.mame.tools.FileTools;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.miriki.ti99.mame.localization.I18n;
import com.miriki.ti99.mame.persistence.SettingsPathRegistry;
import com.miriki.ti99.mame.persistence.SettingsUsageRegistry;
import com.miriki.ti99.mame.ui.MainAppFrame;
import com.miriki.ti99.mame.ui.MainAppFrameComponents;

//##################################################

public class SettingsMenuBuilder {

    // --------------------------------------------------
    
    private static final Logger log = LoggerFactory.getLogger( SettingsMenuBuilder.class );
	private final MainAppFrameComponents ui; 

    private final MainAppFrame frame;

	// --------------------------------------------------

    public SettingsMenuBuilder( MainAppFrame frame, MainAppFrameComponents ui ) {
    	
        this.frame = frame;
        this.ui = ui;
        
    } // [constructor] SettingsMenuBuilder

	// --------------------------------------------------

    public JMenu build() {

        log.debug( "----- start: build()" );

        ui.menuSettings = new JMenu( I18n.t( "menu.settings" ));

        ui.menuSettings.addMenuListener( new MenuListener() {
            @Override
            public void menuSelected( MenuEvent e ) {
                rebuildSettingsMenu( ui.menuSettings );
            }
            @Override public void menuDeselected( MenuEvent e ) {}
            @Override public void menuCanceled( MenuEvent e ) {}
        });

        // Erste Initialisierung
        rebuildSettingsMenu( ui.menuSettings );

        log.debug( "----- end: build() ---> {}", ui.menuSettings );

        return ui.menuSettings;
        
    } // build

	// --------------------------------------------------

    private void normalizeDirectory( Path dir ) {

    	log.debug( "----- start: normalizeDirectory( '{}' )", dir );
    	
    	try ( Stream<Path> stream = Files.list( dir )) {
            stream
                .filter( p -> p.getFileName().toString().endsWith( ".settings" ))
                .forEach( this::normalizeSingleFile );
        } catch ( IOException ex ) {
            log.warn( "Could not normalize directory '{}': {}", dir, ex );
        }

    	log.debug( "----- end: normalizeDirectory()" );
    	
    } // normalizeDirectory
    
	// --------------------------------------------------

    private Path normalizeSingleFile( Path result ) {

    	log.debug( "----- start: normalizeSingleFile( '{}' )", result );

    	String fileName = result.getFileName().toString();
        String safe = FileTools.safeFileName( fileName );

        if ( ! safe.equals( fileName )) {
            Path newPath = result.resolveSibling( safe );
            try {
                Files.move( result, newPath );
                log.trace( "Normalized filename: {} → {}", fileName, safe );
                SettingsUsageRegistry.renameEntry( result, newPath );
                result = newPath;
            } catch ( IOException ex ) {
                log.warn( "Could not normalize filename {} → {}", result, newPath, ex );
                result = null;
            }
        }
        
    	log.debug( "----- end: normalizeSingleFile() ---> {}", result );
    	
        return result;
        
    } // normalizeSingleFile
    
	// --------------------------------------------------

    private Path chooseSettingsFile( boolean forSave ) {
    	
    	log.debug( "----- start: chooseSettingsFile( {} )", forSave );
    	
    	Path result = null;
    	
        JFileChooser chooser = new JFileChooser();
        chooser.setDialogTitle( forSave ? "Save Settings As …" : "Load Settings" );
        chooser.setFileSelectionMode( JFileChooser.FILES_ONLY );

        // Filter auf .settings-Dateien
        chooser.setAcceptAllFileFilterUsed( true );
        chooser.setFileFilter( new javax.swing.filechooser.FileNameExtensionFilter( "Settings files (*.settings)", "settings" ));

        // Vorschlag: aktueller Pfad oder Default
        Path current = SettingsPathRegistry.getCurrent();
        if (( current != null ) && ( Files.exists( current ))) {
            chooser.setSelectedFile( current.toFile() );
        } else {
            SettingsPathRegistry.setCurrent( null );
        }

        int fs = forSave
                ? chooser.showSaveDialog( frame )
                : chooser.showOpenDialog( frame );

        if ( fs == JFileChooser.APPROVE_OPTION ) {
            File file = chooser.getSelectedFile();
            // Für Save: bei fehlender Extension automatisch anhängen
            if (( forSave ) && ( ! file.getName().toLowerCase().endsWith( ".settings" ))) {
                file = new File( file.getParentFile(), file.getName() + ".settings" );
            }
            result = file.toPath();
        }
        
    	log.debug( "----- end: chooseSettingsFile() ---> {}", result );
    	
        return result;
        
    } // chooseSettingsFile
    
	// --------------------------------------------------

    private void doSaveAs() {

    	log.debug( "----- start: doSaveAs()" );
        
    	Path chosen = chooseSettingsFile( true );
        if ( chosen == null ) return;
        // Dateiname aus der Auswahl holen
        String rawName = chosen.getFileName().toString();
        // In sicheren Dateinamen umwandeln (Leerzeichen -> "_", ".settings" anhängen)
        String safeName = FileTools.safeFileName( rawName );
        // Pfad mit sicherem Namen im gleichen Verzeichnis bilden
        Path safePath = chosen.resolveSibling( safeName );
        // Diesen Pfad als aktuellen Settings-Pfad setzen
        SettingsPathRegistry.setCurrent( safePath );
        // Und darunter speichern
        frame.saveSettings();
        
        log.debug( "----- end: doSaveAs()" );
        
    } // doSaveAs

	// --------------------------------------------------

    private void doLoad() {

    	log.debug( "----- start: doLoad()" );
    	
        Path chosen = chooseSettingsFile( false );
        if ( chosen == null ) return;
        // 1. Datei selbst normalisieren
        chosen = normalizeSingleFile( chosen );
        // 2. Ganzes Verzeichnis normalisieren
        normalizeDirectory( chosen.getParent() );
        // 3. Jetzt erst laden
        SettingsPathRegistry.setCurrent( chosen );
        frame.restoreSettings();
        
    	log.debug( "----- end: doSave()" );
    	
    } // doLoad

	// --------------------------------------------------
    
    private void rebuildSettingsMenu( JMenu result ) {

    	log.debug( "----- start: rebuildSettingsMenu( {} )", result );

        result.removeAll();

        ui.menuSettingsSave = new JMenuItem( I18n.t( "menu.settings.save" ));
        ui.menuSettingsSave.addActionListener( e -> {
            Path current = SettingsPathRegistry.getCurrent();
            if ( ! Files.exists( current )) {
                doSaveAs();
            } else {
                frame.saveSettings();
            }
        });
        result.add( ui.menuSettingsSave );

        ui.menuSettingsSaveAs = new JMenuItem( I18n.t( "menu.settings.saveas" ));
        ui.menuSettingsSaveAs.addActionListener( e -> doSaveAs() );
        result.add( ui.menuSettingsSaveAs );

        result.addSeparator();

        ui.menuSettingsLoad = new JMenuItem( I18n.t( "menu.settings.load" ));
        ui.menuSettingsLoad.addActionListener( e -> doLoad() );
        result.add( ui.menuSettingsLoad );

        result.addSeparator();

        // Registry aufräumen
        SettingsUsageRegistry.cleanupInvalidEntries();
        SettingsPathRegistry.cleanupCurrentIfInvalid();

        int maxPicklist = 10;
        List<Path> top = SettingsUsageRegistry.getTop( maxPicklist );

        if ( top.isEmpty() ) {
        	ui.menuSettingsNoSel = new JMenuItem( I18n.t( "menu.settings.nosel" ));
        	ui.menuSettingsNoSel.setEnabled( false );
            result.add( ui.menuSettingsNoSel );
            return;
        }

        ui.menuSettingsPick = new JMenu( I18n.t( "menu.settings.pick" ));
        ui.menuSettingsPickByCount = new JMenuItem( I18n.t( "menu.settings.pick.bycount" ));
        ui.menuSettingsPickByCount.addActionListener( e -> {
            SettingsUsageRegistry.setSortMode( SettingsUsageRegistry.SortMode.BY_COUNT );
            rebuildSettingsMenu( result );
        });
        ui.menuSettingsPick.add( ui.menuSettingsPickByCount );
        ui.menuSettingsPickByDate = new JMenuItem( I18n.t( "menu.settings.pick.bydate" ));
        ui.menuSettingsPickByDate.addActionListener( e -> {
            SettingsUsageRegistry.setSortMode( SettingsUsageRegistry.SortMode.BY_DATE );
            rebuildSettingsMenu( result );
        });
        ui.menuSettingsPick.add( ui.menuSettingsPickByDate );
        result.add( ui.menuSettingsPick );

        for ( Path p : top ) {
            if ( Files.exists( p )) {
                String label = FileTools.prettyName( p );
                Path current = SettingsPathRegistry.getCurrent();

                JMenu configMenu = new JMenu( label );

                // Optional: Radio-Marker
                if ( p.equals( current )) {
                	Icon bulletIcon = new Icon() {
                	    @Override public int getIconWidth() { return 8; }
                	    @Override public int getIconHeight() { return 8; }
                	    @Override
                	    public void paintIcon( Component c, Graphics g, int x, int y ) {
                	        g.fillOval( x, y, 7, 7 );
                	    }
                	};
                    configMenu.setIcon( bulletIcon );
                }

                // --- Load-Eintrag ganz oben ---
                ui.menuSettingsPickLoad = new JMenuItem( I18n.t( "menu.settings.pick.load" ));
                ui.menuSettingsPickLoad.addActionListener( e -> {
                    frame.saveSettings();
                    SettingsPathRegistry.setCurrent( p );
                    frame.restoreSettings();
                    frame.updateFrameTitle();
                    log.info( "Switched settings to '{}'", p );
                });
                configMenu.add( ui.menuSettingsPickLoad );

                configMenu.addSeparator();

                // --- Delete ---
                ui.menuSettingsPickDelete = new JMenuItem( I18n.t( "menu.settings.pick.delete" ));
                ui.menuSettingsPickDelete.addActionListener( e -> {
                    try {
                        Files.deleteIfExists( p );
                        SettingsUsageRegistry.removeEntry( p );
                        rebuildSettingsMenu( result );
                    } catch ( IOException ex ) {
                        log.warn( "Error deleting '{}'", p, ex );
                    }
                });
                configMenu.add( ui.menuSettingsPickDelete );

                // --- Rename ---
                ui.menuSettingsPickRename = new JMenuItem( I18n.t( "menu.settings.pick.rename" ));
                ui.menuSettingsPickRename.addActionListener( e -> {
                	String oldName = FileTools.editName( p );
                	String newName = JOptionPane.showInputDialog( frame, "Rename config:", oldName );
                	if ( newName != null && ! newName.isBlank() ) {
                        String safe = FileTools.safeFileName( newName );
                        Path newPath = p.resolveSibling( safe );

                        try {
                            Files.move( p, newPath );
                            SettingsUsageRegistry.renameEntry( p, newPath );
                            SettingsPathRegistry.setCurrent( newPath );
                            frame.restoreSettings();
                            frame.updateFrameTitle();
                            rebuildSettingsMenu( result );

                        } catch ( IOException ex ) {
                            log.warn( "Rename failed: {} → {}", p, newPath, ex );
                            // JOptionPane.showMessageDialog( frame,
                            //     "Could not rename file:\n" + ex.getMessage(),
                            //     "Rename Error",
                            //     JOptionPane.ERROR_MESSAGE );
                        }
                        
                    }
                });
                configMenu.add( ui.menuSettingsPickRename );

                result.add( configMenu );
                
            }
            
        }

        result.revalidate();
        result.repaint();
        
    	log.debug( "----- end: rebuildSettingsMenu()" );

    } // rebuildSettingsMenu
    
	// --------------------------------------------------
    
}

//##################################################
