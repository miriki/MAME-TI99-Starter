package com.miriki.ti99.mame.ui.menu;

import java.awt.event.WindowEvent;

import javax.swing.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.miriki.ti99.mame.localization.I18n;
import com.miriki.ti99.mame.ui.MainAppFrame;
import com.miriki.ti99.mame.ui.MainAppFrameComponents;

//##################################################

public class FileMenuBuilder {

    // --------------------------------------------------
    
	private final MainAppFrameComponents ui;
	private final MainAppFrame frame;

    private static final Logger log = LoggerFactory.getLogger( FileMenuBuilder.class );

    // private final MainAppFrame frame;

    // --------------------------------------------------
    
    public FileMenuBuilder( MainAppFrameComponents ui, MainAppFrame frame ) {
    	
    	this.ui = ui;
    	this.frame = frame;
        
    } // [ûconstructor] FileMenuBuilder

    // --------------------------------------------------
    
    public JMenu build() {

        log.debug( "----- start: build()" );

        // JMenu result = new JMenu( "File" );
        ui.menuFile = new JMenu( I18n.t( "menu.file" ));

        ui.menuFileExit = new JMenuItem( I18n.t( "menu.file.exit" ));
        ui.menuFileExit.addActionListener( e -> System.exit( 0 ));
        ui.menuFileExit.addActionListener(e -> {
            frame.dispatchEvent(new WindowEvent(frame, WindowEvent.WINDOW_CLOSING));
        });
        ui.menuFile.add( ui.menuFileExit );

        log.debug( "----- end: build() ---> {}", ui.menuFile );

        return ui.menuFile;
        
    } // build

    // --------------------------------------------------
    
}

//##################################################
