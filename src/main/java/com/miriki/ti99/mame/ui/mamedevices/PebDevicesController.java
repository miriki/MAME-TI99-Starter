package com.miriki.ti99.mame.ui.mamedevices;

import javax.swing.*;
import java.awt.Component;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
	
//############################################################################

/*
 * a class for handling the tabs of a jtabbedpane, to selectively show and hide them, set titles etc.
 */

public class PebDevicesController {

    // --------------------------------------------------

    private static final Logger log = LoggerFactory.getLogger( PebDevicesController.class );
	
    private final JTabbedPane myTabbedPane;
    private final Map<String, Component> tabMap = new HashMap<>();

    public Map<String, Component> getTabMap() {
        return tabMap;
    }
    
    // --------------------------------------------------

    // constructor - gets a local copy of the jtabbedpane and stores info about every tab of it
    public PebDevicesController( JTabbedPane extTabbedPane ) {
    	
		log.debug( "----- start: [constructor] MirikiPebDevicesController()" );
		
        myTabbedPane = extTabbedPane;
        init();
        
		log.debug( "----- end: [constructor] MirikiPebDevicesController()" );
		
    } // [constructor] MirikiPebDevicesController

    // --------------------------------------------------
    
    // scans the tabs of the (local) jtabbedpane and stores titles and corresponding components
    
    private void init() {
    	
		log.debug( "----- start: init()" );
		
		log.trace( "  TabCount at init = {}", myTabbedPane.getTabCount() );
		
        // Alle Tabs einmal durchgehen und merken
        for ( int i = 0; i < myTabbedPane.getTabCount(); i++ ) {

        	String title = myTabbedPane.getTitleAt( i );
            Component comp = myTabbedPane.getComponentAt( i );
            
            // Schlüssel für Map
            String key = PebDevices.toParamName( title );
            tabMap.put( key, comp ); // Titel als Schlüssel
            log.trace( "  adding '{}' to map ", key);

            // Pretty-Titel aus Registry
            String pretty = PebDevices.toPrettyTitle( title );
            
            // Titel setzen
            myTabbedPane.setTitleAt( i, pretty );
            // System.out.printf("   -> tabbedPane.getTitleAt(%d) = '%s'%n", i, myTabbedPane.getTitleAt(i));
            
        }

		log.debug( "----- end: init()" );
		
    } // init()

    // --------------------------------------------------
    
    public void updateTabs(List<String> selectedTitles) {

        log.debug( "----- start: updateTabs( List:{} )", selectedTitles.size());

        // 1. Aktuellen Tab merken
        int oldIndex = myTabbedPane.getSelectedIndex();
        String oldKey = null;

        if (oldIndex >= 0) {
            Component oldComp = myTabbedPane.getComponentAt( oldIndex );
            if ( oldComp instanceof JComponent jc ) {
                oldKey = (String) jc.getClientProperty( "deviceKey" );
                log.trace( "  current tab '{}' is saved", oldKey );
            }
        }

        // 2. Ist-Liste der Keys aus dem TabbedPane
        List<String> current = new ArrayList<>();

        for ( int i = 0; i < myTabbedPane.getTabCount(); i++ ) {
            Component c = myTabbedPane.getComponentAt( i );
            if ( c instanceof JComponent jc ) {
                String key = (String) jc.getClientProperty( "deviceKey" );
                current.add( key );
            }
        }

        log.trace( "  current tab list contains {} entries", current.size() );

        // 3. Soll-Liste (selectedTitles enthält bereits die korrekte Reihenfolge)
        List<String> desired = new ArrayList<>( selectedTitles );
        log.trace( "  new tab list contains {} entries", desired.size() );

        // 4. Wenn identisch → nichts tun
        if ( current.equals( desired )) {
            log.trace( "  Tabs unchanged, skipping rebuild" );
            return;
        }

        log.trace( "  Tabs changed, rebuilding" );

        // 5. Neu aufbauen
        myTabbedPane.removeAll();
        log.trace("  removed all existing tabs");

        for ( String key : desired ) {
            Component comp = tabMap.get( key );
            if ( comp == null ) {
                log.warn( "  WARNING: No component found for key '{}'", key );
                continue;
            }

            String pretty = PebDevices.toPrettyTitle( key );

            myTabbedPane.addTab( pretty, comp );

            if ( comp instanceof JComponent jc ) {
                jc.putClientProperty( "deviceKey", key );
            }

            log.trace( "  added tab '{}'", pretty );
        }

        // 6. Alten Tab wiederherstellen
        if ( oldKey != null ) {
            for ( int i = 0; i < myTabbedPane.getTabCount(); i++ ) {
                Component c = myTabbedPane.getComponentAt( i );
                if ( c instanceof JComponent jc ) {
                    String key = (String) jc.getClientProperty( "deviceKey" );
                    if ( oldKey.equals( key )) {
                        myTabbedPane.setSelectedIndex( i );
                        log.trace( "  focus on tab '{}'", key );
                        break;
                    }
                }
            }
        }
        
		log.debug( "----- end: updateTabs()" );
		
    } // updateTabs

    // --------------------------------------------------
    
} // class MirikiPebDevicesController

//############################################################################
