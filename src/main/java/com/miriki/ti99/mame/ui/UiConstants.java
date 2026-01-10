package com.miriki.ti99.mame.ui;

import java.awt.Color;

/**
 * UI-related constants used across the application.
 */
public final class UiConstants {

    private UiConstants() {
        // utility class
    }

    // --------------------------------------------------

    /** Placeholder text for unselected combo box entries. */
    public static final String CBX_SEL_NONE = "------";

    // --------------------------------------------------

    /** Foreground color for disabled combo boxes. */
    public static final Color COMBOBOX_DISABLED_FOREGROUND  = new Color(95, 95, 95);

    /** Background color for disabled combo boxes. */
    public static final Color COMBOBOX_DISABLED_BACKGROUND  = new Color(231, 231, 231);

    /** Foreground color for disabled text fields. */
    public static final Color TEXTFIELD_DISABLED_FOREGROUND = new Color(63, 63, 63);

    // --------------------------------------------------
}
