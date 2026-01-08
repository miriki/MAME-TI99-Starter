package com.miriki.ti99.mame.ui.menu;

import javax.swing.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.miriki.ti99.mame.localization.I18n;
import com.miriki.ti99.mame.ui.MainAppFrame;
import com.miriki.ti99.mame.ui.MainAppFrameComponents;

//##################################################

public class HelpMenuBuilder {

    // --------------------------------------------------
    
    private static final Logger log = LoggerFactory.getLogger( HelpMenuBuilder.class );

    private final MainAppFrame frame;
	private final MainAppFrameComponents ui;

    // --------------------------------------------------
    
    public HelpMenuBuilder( MainAppFrame frame, MainAppFrameComponents ui ) {
    	
        this.frame = frame;
    	this.ui = ui;
        
    } // [constructor] HelpMenuBuilder

    // --------------------------------------------------
    
    public JMenu build() {

        log.debug( "----- start: build()" );

        ui.menuHelp = new JMenu( I18n.t( "menu.help" ));

        ui.menuHelpAbout = new JMenuItem( I18n.t( "menu.help.about" ));
        
        ui.menuHelp.add( ui.menuHelpAbout );

        ui.menuHelpAbout.addActionListener( e -> {
            JOptionPane.showMessageDialog(
                frame,
                "MAME TI99 Starter\n\nv0.99.51 (public beta)\n\n(c) 2025 by\n\nMichael 'miriki' Rittweger\n\n[ Silvester Edition ]\n\n",
                "About",
                JOptionPane.INFORMATION_MESSAGE
            );
        });

        log.debug( "----- end: build() ---> {}", ui.menuHelp );

        return ui.menuHelp;
        
    } // build

    // --------------------------------------------------
    
}

//##################################################
