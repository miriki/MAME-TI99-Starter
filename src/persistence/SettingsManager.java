package persistence;

import java.awt.Component;
import java.awt.Container;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import javax.swing.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import view.util.ControlCollector;

//############################################################################

public class SettingsManager {

	// --------------------------------------------------
	
    private static final Logger log = LoggerFactory.getLogger( SettingsManager.class );
	
    // private static final String DEFAULT_SETTINGS_FILE = "MAME_TI99_Starter.settings";
    
	// --------------------------------------------------
	
    public static void save( Map<String,JComponent> controls, Map<String,Component> tabMap, Path targetFile ) throws IOException {
    	
		log.debug( "save()" );
		
        Properties props = new Properties();

        // 1. Alle Controls aus dem Containerbaum
        for ( Map.Entry<String, JComponent> entry : controls.entrySet() ) {

            String key = entry.getKey();
            JComponent comp = entry.getValue();

            log.trace( "  Having control {} -> '{}'", key, props.getProperty(key) );
            
            if ( comp instanceof JTextField tf ) {
            	
                props.setProperty( key, tf.getText() );
                
                log.trace( "    saved (txt): {} = '{}'", key, tf.getText() );
            
            } else if ( comp instanceof JComboBox<?> cbx ) {
            	
                Object sel = cbx.getSelectedItem();
                
                if ( sel != null ) {
                	
                    props.setProperty( key, sel.toString() );
                    
                    log.trace( "    saved (cbx): {} = '{}'", key, sel );
                    
                }
            }
            
        }

        // 2. Zusätzlich alle Controls aus tabMap durchlaufen
        for (Map.Entry<String, Component> entry : tabMap.entrySet()) {
            Component comp = entry.getValue();
            if (comp instanceof Container child) {
                log.trace( "  Having container '{}'", comp.getName() );
                Map<String, JComponent> extraControls = new HashMap<>();
                ControlCollector.collectRecursive(child, extraControls);

                for ( Map.Entry<String, JComponent> e2 : extraControls.entrySet() ) {
                    String key = e2.getKey();
                    JComponent jc = e2.getValue();

                    log.trace( "    Having control {} -> '{}'", key, props.getProperty(key) );

                    if ( jc instanceof JTextField tf ) {
                        props.setProperty( key, tf.getText() );
                        log.trace( "      saved (txt/tabMap): {} = '{}'", key, tf.getText() );
                    } else if ( jc instanceof JComboBox<?> cbx ) {
                        Object sel = cbx.getSelectedItem();
                        if ( sel != null ) {
                            props.setProperty( key, sel.toString() );
                            log.trace( "      saved (cbx/tabMap): {} = '{}'", key, sel );
                        }
                    }
                }
            }
        }

        // try ( FileOutputStream out = new FileOutputStream( SETTINGS_FILE )) {
        try (OutputStream out = Files.newOutputStream(targetFile)) {
            props.store( out, "Emulator GUI Settings" );
        }
        
    } // save()

	// --------------------------------------------------
	
    public static Properties  restore( Map<String, JComponent> controls, Path sourceFile ) throws IOException {
    	
		log.debug( "restore()" );
		
        Properties props = new Properties();
        
        // try ( FileInputStream in = new FileInputStream( SETTINGS_FILE )) {
        try (InputStream in = Files.newInputStream(sourceFile)) {
            props.load( in );
        }

        for ( Map.Entry<String, JComponent> entry : controls.entrySet() ) {
            String key = entry.getKey();
            JComponent comp = entry.getValue();
            String value = props.getProperty( key );

            // log.trace("  Having control {} with value '{}'", key, value);
            
            if ( value != null ) {
                
            	if ( comp instanceof JTextField tf ) {
            		
                    tf.setText( value );
                
                    // log.trace( "    restored: txt" );
                    
                } else if ( comp instanceof JComboBox<?> cbx ) {
                    
                    cbx.setSelectedItem( value );

                    // log.trace( "    restored: cbx" );
                	
                }
            	
            }
            
        }
        
        return props;
        
    } // restore()

	// --------------------------------------------------
	
    public static void saveToCurrent(Map<String, JComponent> controls, Map<String, Component> tabMap) throws IOException {
    	save(controls, tabMap, SettingsPathRegistry.getCurrent());
	}
	
	// --------------------------------------------------
	
	public static Properties restoreFromCurrent(Map<String, JComponent> controls) throws IOException {
		return restore(controls, SettingsPathRegistry.getCurrent());
	}

	// --------------------------------------------------
	
} // class SettingsManager

//############################################################################
