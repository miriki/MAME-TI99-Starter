package com.miriki.ti99.mame.ui.menu;

import javax.swing.JMenu;
import javax.swing.JMenuBar;

import com.miriki.ti99.mame.ui.MainAppFrame;
import com.miriki.ti99.mame.ui.MainAppFrameBuilder;
import com.miriki.ti99.mame.ui.MainAppFrameComponents;

//##################################################

public class MenuBarBuilder {

    // --------------------------------------------------
    
	private final MainAppFrame frame;
	private final MainAppFrameComponents ui;
	private final MainAppFrameBuilder builder;
	 
    // --------------------------------------------------
    
	public MenuBarBuilder( MainAppFrame frame, MainAppFrameComponents ui, MainAppFrameBuilder builder ) {
		
		this.frame = frame;
    	this.ui = ui;
    	this.builder = builder;
		
	} // [constructor] MenuBarBuilder

    // --------------------------------------------------
    
    public JMenuBar build() {

        JMenuBar menuBar = new JMenuBar();

        JMenu fileMenu = new FileMenuBuilder( ui, frame ).build();
        JMenu languageMenu = new LanguageMenuBuilder( ui, builder ).build();
        JMenu settingsMenu = new SettingsMenuBuilder( frame, ui ).build();
        JMenu helpMenu = new HelpMenuBuilder( frame, ui ).build();

        menuBar.add( fileMenu );
        menuBar.add( settingsMenu );
        menuBar.add( languageMenu );
        menuBar.add( helpMenu );

        return menuBar;
        
    } // build

    // --------------------------------------------------
    
}

//##################################################
