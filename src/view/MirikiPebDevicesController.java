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

public class MirikiPebDevicesController {

    // --------------------------------------------------

    private static final Logger log = LoggerFactory.getLogger( MirikiPebDevicesController.class );
	
    private final JTabbedPane myTabbedPane;
    private final Map<String, Component> tabMap = new HashMap<>();

    public Map<String, Component> getTabMap() {
        return tabMap;
    }
    
    // --------------------------------------------------

    // constructor - gets a local copy of the jtabbedpane and stores info about every tab of it
    public MirikiPebDevicesController( JTabbedPane extTabbedPane ) {
    	
		log.debug( "[constructor] MirikiPebDevicesController()" );
		
        myTabbedPane = extTabbedPane;
        init();
        
    } // [constructor] MirikiPebDevicesController

    // --------------------------------------------------
    
    // scans the tabs of the (local) jtabbedpane and stores titles and corresponding components
    
    private void init() {
    	
		log.debug( "init()" );
		
		log.trace( "  TabCount at init = {}", myTabbedPane.getTabCount() );
		
        // Alle Tabs einmal durchgehen und merken
        for ( int i = 0; i < myTabbedPane.getTabCount(); i++ ) {

        	String title = myTabbedPane.getTitleAt( i );
            Component comp = myTabbedPane.getComponentAt( i );
            
            // Schlüssel für Map
            String key = MirikiPebDevices.toParamName( title );
            tabMap.put( key, comp ); // Titel als Schlüssel
            log.trace( "  adding '{}' to map ", key);

            // Pretty-Titel aus Registry
            String pretty = MirikiPebDevices.toPrettyTitle( title );
            
            // Titel setzen
            myTabbedPane.setTitleAt( i, pretty );
            // System.out.printf("   -> tabbedPane.getTitleAt(%d) = '%s'%n", i, myTabbedPane.getTitleAt(i));
            
        }

    } // init()

    // --------------------------------------------------
    
    public void updateTabs( List<String> selectedTitles ) {

		log.debug( "updateTabs( List:{} )", selectedTitles.size() );
		
        myTabbedPane.removeAll();

        /* 
        System.out.println("tabMap Inhalt:");
        for (Map.Entry<String, Component> entry : tabMap.entrySet()) {
            System.out.println("Titel: " + entry.getKey() + ", Komponente: " + entry.getValue());
        }
        */

        for (String title : selectedTitles) {
            String key = title.toLowerCase(); // oder MirikiPebDevices.toParamName(title)
            Component comp = tabMap.get( key );
            String pretty = MirikiPebDevices.toPrettyTitle( title );

            log.trace("  adding tab '{}' -> {}", title, comp);
            if ( comp != null ) {
                // myTabbedPane.addTab(pretty, comp);
                myTabbedPane.addTab( pretty, comp );
            }
        }

    } // updateTabs()

    // --------------------------------------------------
    
} // class MirikiPebDevicesController

//############################################################################
