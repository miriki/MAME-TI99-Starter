package com.miriki.ti99.mame.ui.util;

import javax.swing.BorderFactory;
import javax.swing.UIManager;

import com.miriki.ti99.mame.ui.UiConstants;

/**
 * Applies global UI defaults for Swing components.
 * <p>
 * Centralizes all look-and-feel overrides to ensure consistent styling
 * across the entire application.
 */
public final class UiDefaults {

    private UiDefaults() {}

    /**
     * Applies all UI defaults.
     * <p>
     * This method should be called once during application startup,
     * before any Swing components are created.
     */
    public static void apply() {

        // Remove default ComboBox border
        UIManager.put("ComboBox.border", BorderFactory.createEmptyBorder());

        // Disabled ComboBox colors
        UIManager.put("ComboBox.disabledForeground", UiConstants.COMBOBOX_DISABLED_FOREGROUND);
        UIManager.put("ComboBox.disabledBackground", UiConstants.COMBOBOX_DISABLED_BACKGROUND);

        // Disabled TextField foreground
        UIManager.put("TextField.inactiveForeground", UiConstants.TEXTFIELD_DISABLED_FOREGROUND);
    }
}
