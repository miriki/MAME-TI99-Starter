package com.miriki.ti99.mame.localization;

import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

// ##################################################

public final class I18n {

    public static final Locale LOCALE_EN_US = Locale.forLanguageTag( "en-US" );
    public static final Locale LOCALE_EN_GB = Locale.forLanguageTag( "en-GB" );
    public static final Locale LOCALE_EN_AU = Locale.forLanguageTag( "en-AU" );
    public static final Locale LOCALE_DE_DE = Locale.forLanguageTag( "de-DE" );
    public static final Locale LOCALE_DE_AT = Locale.forLanguageTag( "de-AT" );
    public static final Locale LOCALE_DE_CH = Locale.forLanguageTag( "de-CH" );
    public static final Locale LOCALE_FR_FR = Locale.forLanguageTag( "fr-FR" );
    public static final Locale LOCALE_IT_IT = Locale.forLanguageTag( "it-IT" );
    
    private static Locale currentLocale = LOCALE_DE_DE;
    
    private static ResourceBundle bundle = loadBundle( currentLocale );

    private static final Logger log = LoggerFactory.getLogger( I18n.class );

    // --------------------------------------------------
    
    private I18n() {}

    // --------------------------------------------------
    
    private static ResourceBundle loadBundle( Locale locale ) {
    	
        return ResourceBundle.getBundle( "localization.messages", locale );
        
    } // loadBundle

    // --------------------------------------------------
    
    public static void setLocale( Locale locale ) {
    	
        currentLocale = locale;
        bundle = loadBundle( locale );
    	log.info( "Locale switched to: {}", locale );
    	
    } // setLocale

    // --------------------------------------------------
    
    public static Locale getLocale() {
    	
        return currentLocale;
        
    } // getLocale

    // --------------------------------------------------
    
    public static String getCurrentLanguageCode() {
    	
        return currentLocale.toLanguageTag().toLowerCase().replace('-', '_');
        
    } // getCurrentLanguageCode

    // --------------------------------------------------
    
    public static String t( String key ) {
    	
        try {
            return bundle.getString( key );
            
        } catch ( MissingResourceException e ) {
            return "??" + key + "??";
            
        }
        
    } // t
    
    // --------------------------------------------------
    
} // class I18n

//##################################################
