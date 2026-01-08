package com.miriki.ti99.mame.ui.util;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

//############################################################################

public class ControlCollector {

	// --------------------------------------------------
	
    private static final Logger log = LoggerFactory.getLogger(ControlCollector.class);

	// --------------------------------------------------

    public static Map<String, JComponent> collectControls( Container root ) {

		log.debug( "collectControls( root={} )", root.getName() );

    	Map<String, JComponent> map = new HashMap<>();
        collectRecursive( root, map );

        log.debug("  return: {} controls collected", map.size());
        int nr = 1;
        for (Map.Entry<String, JComponent> entry : map.entrySet()) {
            JComponent comp = entry.getValue();
            log.trace("    {}: {} -> {} (name={})",
                      nr,
                      entry.getKey(),
                      comp.getClass().getSimpleName(),
                      comp.getName());
            nr++;
        }
        
        return map;
        
    } // collectControls

	// --------------------------------------------------
	
    public static void collectRecursive(Container container, Map<String, JComponent> map) {
    	
		log.debug( "collectRecursive( container={} [{}] )", container.getName() != null ? container.getName() : "<unnamed>", container.getClass().getSimpleName() );
       
		for ( Component comp : container.getComponents() ) {
            // log.trace( "  look at '{}' [{}]", comp.getClass().getSimpleName(), comp.getName() );

            if ( comp instanceof JComponent jc ) {
                String name = jc.getName();
                if ( name != null ) {
                	// log.trace( "    has a name" );
                    if (name.startsWith("cbx") || name.startsWith("txt")) {
                        map.put( name, jc );
                    	// log.trace( "    '{}' [{}] added to map to have now {} entries", comp.getClass().getSimpleName(), comp.getName(), map.size() );
                    }
                }
            }
            
            if (comp instanceof JTabbedPane tabs) {
                int count = tabs.getTabCount();
                for ( int i = 0; i < count; i++ ) {
                    Component tabComp = tabs.getComponentAt( i );
                    if ( tabComp instanceof Container child ) {
                    	// log.trace("    entering Tab '{}' [{}]", tabs.getTitleAt( i ), tabComp.getClass().getSimpleName() );
                        collectRecursive( child, map );
                    }
                }
            }
            
            else if (comp instanceof Container child && child.getComponentCount() > 0) {
            	// log.trace("    entering Container '{}' [{}]", comp.getName(), child.getClass().getSimpleName() );
                collectRecursive(child, map);
            }
            
        }
		
    } // collectRecursive()
	
	// --------------------------------------------------
	
} // class ControlCollector

//############################################################################
