package view;

import javax.swing.*;
import java.awt.Component;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
	
//############################################################################

/*
 * a class for handling the tabs of a jtabbedpane, to selectively show and hide them, set titles etc.
 */

public class MirikiSideportDevicesController {

	// --------------------------------------------------
	
    private static final Logger log = LoggerFactory.getLogger( MirikiSideportDevicesController.class );
	
    private final JTabbedPane myTabbedPane;
    private final Map<String, Component> tabMap = new HashMap<>();

    // --------------------------------------------------

    // constructor - gets a local copy of the jtabbedpane and stores info about every tab of it
    public MirikiSideportDevicesController( JTabbedPane extTabbedPane ) {
    	
		log.debug( "[constructor] MirikiSideportDevicesController()" );
		
        myTabbedPane = extTabbedPane;
        init();
        
    } // [constructor] MirikiSideportDevicesController()

    // --------------------------------------------------
    
    // scans the tabs of the (local) jtabbedpane and stores titles and corresponding components
    
    private void init() {
    	
		log.debug( "init()" );
		
        // Alle Tabs einmal durchgehen und merken
        for ( int i = 0; i < myTabbedPane.getTabCount(); i++ ) {

        	String title = myTabbedPane.getTitleAt( i );
            Component comp = myTabbedPane.getComponentAt( i );
            
            // Schlüssel für Map
            String key = MirikiPebDevices.toParamName( title );
            tabMap.put( key, comp ); // Titel als Schlüssel

            // Pretty-Titel aus Registry
            String pretty = MirikiPebDevices.toPrettyTitle( title );
            
            // Titel setzen
            myTabbedPane.setTitleAt( i, pretty );
            // System.out.printf("   -> tabbedPane.getTitleAt(%d) = '%s'%n", i, myTabbedPane.getTitleAt(i));
            
        }

    } // init()

    // --------------------------------------------------
    
    public void updateTabs( List<String> selectedTitles ) {

		log.debug( "updateTabs()" );
		
        myTabbedPane.removeAll();

        for (String title : selectedTitles) {
            String key = title.toLowerCase(); // oder MirikiSideportDevices.toParamName(title)
            Component comp = tabMap.get( key );
            String pretty = MirikiPebDevices.toPrettyTitle( title );

            if ( comp != null ) {
                // myTabbedPane.addTab(pretty, comp);
                myTabbedPane.addTab( pretty, comp );
            }
        }

    } // updateTabs()

    // --------------------------------------------------
    
} // MirikiSideportDevicesController

//############################################################################
