package com.miriki.ti99.mame.localization;

import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Centralized internationalization helper.
 * <p>
 * Provides access to the active {@link ResourceBundle}, manages the current
 * locale and offers a simple translation method {@link #t(String)}.
 */
public final class I18n {

    // -------------------------------------------------------------------------
    // Supported locales
    // -------------------------------------------------------------------------

    public static final Locale LOCALE_EN_US = Locale.forLanguageTag("en-US");
    public static final Locale LOCALE_EN_GB = Locale.forLanguageTag("en-GB");
    public static final Locale LOCALE_EN_AU = Locale.forLanguageTag("en-AU");

    public static final Locale LOCALE_DE_DE = Locale.forLanguageTag("de-DE");
    public static final Locale LOCALE_DE_AT = Locale.forLanguageTag("de-AT");
    public static final Locale LOCALE_DE_CH = Locale.forLanguageTag("de-CH");

    public static final Locale LOCALE_FR_FR = Locale.forLanguageTag("fr-FR");
    public static final Locale LOCALE_IT_IT = Locale.forLanguageTag("it-IT");

    // -------------------------------------------------------------------------
    // State
    // -------------------------------------------------------------------------

    private static Locale currentLocale = LOCALE_DE_DE;
    private static ResourceBundle bundle = loadBundle(currentLocale);

    private static final Logger log = LoggerFactory.getLogger(I18n.class);

    // -------------------------------------------------------------------------

    private I18n() {
        // Utility class
    }

    // -------------------------------------------------------------------------
    // Bundle access
    // -------------------------------------------------------------------------

    /**
     * Returns the currently active resource bundle.
     */
    public static ResourceBundle getBundle() {
        return bundle;
    }

    private static ResourceBundle loadBundle(Locale locale) {
        return ResourceBundle.getBundle("localization.messages", locale);
    }

    // -------------------------------------------------------------------------
    // Locale management
    // -------------------------------------------------------------------------

    /**
     * Sets the active locale and reloads the resource bundle.
     *
     * @param locale the new locale
     */
    public static void setLocale(Locale locale) {
        currentLocale = locale;
        bundle = loadBundle(locale);
        log.info("Locale switched to: {}", locale);
    }

    /**
     * Returns the currently active locale.
     */
    public static Locale getLocale() {
        return currentLocale;
    }

    /**
     * Returns the current language code in the form {@code xx_YY}.
     */
    public static String getCurrentLanguageCode() {
        return currentLocale.toLanguageTag()
                .toLowerCase()
                .replace('-', '_');
    }

    // -------------------------------------------------------------------------
    // Translation
    // -------------------------------------------------------------------------

    /**
     * Translates the given key using the active resource bundle.
     * <p>
     * If the key is missing, a placeholder of the form {@code ??key??} is returned.
     *
     * @param key the translation key
     * @return the translated text or a placeholder if missing
     */
    public static String t(String key) {
        try {
            return bundle.getString(key);
        } catch (MissingResourceException e) {
            return "??" + key + "??";
        }
    }
}
