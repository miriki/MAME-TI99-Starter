package com.miriki.ti99.mame.ui.menu;

import java.awt.Component;
import java.util.Locale;

import javax.swing.ButtonGroup;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JMenu;
import javax.swing.event.MenuEvent;
import javax.swing.event.MenuListener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.miriki.ti99.mame.localization.I18n;
import com.miriki.ti99.mame.ui.MainAppFrameBuilder;
import com.miriki.ti99.mame.ui.MainAppFrameComponents;

import com.miriki.ti99.mame.tools.IconLoader;

//##################################################

public class LanguageMenuBuilder {

    // --------------------------------------------------
    
	private final MainAppFrameComponents ui;
	private final MainAppFrameBuilder builder;
	
    private static final Logger log = LoggerFactory.getLogger( LanguageMenuBuilder.class );

    // --------------------------------------------------
    
    public LanguageMenuBuilder( MainAppFrameComponents ui, MainAppFrameBuilder builder ) {
    	
    	this.ui = ui;
    	this.builder = builder;
        
    } // [constructor] LanguageMenuBuilder

    // --------------------------------------------------
    
    private void updateLanguageSelection() {
    	
        Locale current = I18n.getLocale();
        
        for ( Component c : ui.menuLang.getMenuComponents() ) {
        	
            if ( c instanceof JCheckBoxMenuItem item ) {
            	
                Locale itemLocale = (Locale) item.getClientProperty( "locale" );
                item.setSelected( itemLocale.equals( current ));
            }
            
        }
        
    } // updateLanguageSelection

    // --------------------------------------------------
    
    public JMenu build() {

        log.debug( "----- start: build()" );

        ui.menuLang = new JMenu( I18n.t( "menu.lang" ));

        ui.menuLangEnglishGB = new JCheckBoxMenuItem( I18n.t( "menu.lang.english_gb" ));
        ui.menuLangEnglishGB.setIcon(IconLoader.load( "flag_en_gb.png" ));
        ui.menuLangEnglishGB.putClientProperty( "locale", I18n.LOCALE_EN_GB );
        ui.menuLangEnglishUS = new JCheckBoxMenuItem( I18n.t( "menu.lang.english_us" ));
        ui.menuLangEnglishUS.setIcon(IconLoader.load( "flag_en_us.png" ));
        ui.menuLangEnglishUS.putClientProperty( "locale", I18n.LOCALE_EN_US );
    	ui.menuLangEnglishUS.setEnabled( false );
        ui.menuLangEnglishAU = new JCheckBoxMenuItem( I18n.t( "menu.lang.english_au" ));
        ui.menuLangEnglishAU.setIcon(IconLoader.load( "flag_en_au.png" ));
        ui.menuLangEnglishAU.putClientProperty( "locale", I18n.LOCALE_EN_AU );
    	ui.menuLangEnglishAU.setEnabled( false );
        
        ui.menuLangGermanDE = new JCheckBoxMenuItem( I18n.t( "menu.lang.german_de" ));
        ui.menuLangGermanDE.setIcon(IconLoader.load( "flag_de_de.png" ));
        ui.menuLangGermanDE.putClientProperty( "locale", I18n.LOCALE_DE_DE );
        ui.menuLangGermanAT = new JCheckBoxMenuItem( I18n.t( "menu.lang.german_at" ));
        ui.menuLangGermanAT.setIcon(IconLoader.load( "flag_de_at.png" ));
        ui.menuLangGermanAT.putClientProperty( "locale", I18n.LOCALE_DE_AT );
    	ui.menuLangGermanAT.setEnabled( false );
        ui.menuLangGermanCH = new JCheckBoxMenuItem( I18n.t( "menu.lang.german_ch" ));
        ui.menuLangGermanCH.setIcon(IconLoader.load( "flag_de_ch.png" ));
        ui.menuLangGermanCH.putClientProperty( "locale", I18n.LOCALE_DE_CH );
    	ui.menuLangGermanCH.setEnabled( false );

        ui.menuLangFrenchFR = new JCheckBoxMenuItem( I18n.t( "menu.lang.french_fr" ));
        ui.menuLangFrenchFR.setIcon(IconLoader.load( "flag_fr_fr.png" ));
        ui.menuLangFrenchFR.putClientProperty( "locale", I18n.LOCALE_FR_FR );
    	ui.menuLangFrenchFR.setEnabled( false );

        ui.menuLangItalianIT = new JCheckBoxMenuItem( I18n.t( "menu.lang.italian_it" ));
        ui.menuLangItalianIT.setIcon(IconLoader.load( "flag_it_it.png" ));
        ui.menuLangItalianIT.putClientProperty( "locale", I18n.LOCALE_IT_IT );
    	ui.menuLangItalianIT.setEnabled( false );

        ButtonGroup grp = new ButtonGroup();
        grp.add( ui.menuLangEnglishGB );
        grp.add( ui.menuLangEnglishUS );
        grp.add( ui.menuLangEnglishAU );
        grp.add( ui.menuLangGermanDE );
        grp.add( ui.menuLangGermanAT );
        grp.add( ui.menuLangGermanCH );
        grp.add( ui.menuLangFrenchFR );
        grp.add( ui.menuLangItalianIT );
        
        ui.menuLang.add( ui.menuLangEnglishGB );
        ui.menuLang.add( ui.menuLangEnglishUS );
        ui.menuLang.add( ui.menuLangEnglishAU );
        ui.menuLang.add( ui.menuLangGermanDE ); 
        ui.menuLang.add( ui.menuLangGermanAT ); 
        ui.menuLang.add( ui.menuLangGermanCH ); 
        ui.menuLang.add( ui.menuLangFrenchFR );
        ui.menuLang.add( ui.menuLangItalianIT );
        
        ui.menuLangEnglishGB.addActionListener( e -> { 
        	I18n.setLocale( I18n.LOCALE_EN_GB ); 
        	builder.refreshUI(); 
        });

        ui.menuLangEnglishUS.addActionListener( e -> { 
        	I18n.setLocale( I18n.LOCALE_EN_US ); 
        	builder.refreshUI(); 
        });

        ui.menuLangEnglishAU.addActionListener( e -> { 
        	I18n.setLocale( I18n.LOCALE_EN_AU ); 
        	builder.refreshUI(); 
        });

        ui.menuLangGermanDE.addActionListener( e -> { 
        	I18n.setLocale( I18n.LOCALE_DE_DE ); 
        	builder.refreshUI(); 
        });

        ui.menuLangGermanAT.addActionListener( e -> { 
        	I18n.setLocale( I18n.LOCALE_DE_AT ); 
        	builder.refreshUI(); 
        });

        ui.menuLangGermanCH.addActionListener( e -> { 
        	I18n.setLocale( I18n.LOCALE_DE_CH ); 
        	builder.refreshUI(); 
        });

        ui.menuLangFrenchFR.addActionListener( e -> { 
        	I18n.setLocale( I18n.LOCALE_FR_FR ); 
        	builder.refreshUI(); 
        });

        ui.menuLangItalianIT.addActionListener( e -> { 
        	I18n.setLocale( I18n.LOCALE_IT_IT ); 
        	builder.refreshUI(); 
        });

        updateLanguageSelection();
        
        ui.menuLang.addMenuListener( new MenuListener() {
        	
            @Override
            public void menuSelected( MenuEvent e ) {
            	
                updateLanguageSelection();
                
            }
            
            @Override public void menuDeselected( MenuEvent e ) {}
            
            @Override public void menuCanceled( MenuEvent e ) {}
            
        });
        
        log.debug( "----- end: build() ---> {}", ui.menuLang );

        return ui.menuLang;
        
    } // build

    // --------------------------------------------------
    
}

//##################################################
