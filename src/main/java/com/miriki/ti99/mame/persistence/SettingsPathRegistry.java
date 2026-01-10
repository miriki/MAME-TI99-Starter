package com.miriki.ti99.mame.persistence;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.prefs.Preferences;

/**
 * Stores and retrieves the path of the currently active settings file.
 * <p>
 * The path is persisted using {@link Preferences} so the application can
 * restore the last used settings file across sessions.
 */
public final class SettingsPathRegistry {

    private static final String PREF_KEY = "lastSettingsFile";
    private static final String DEFAULT_FILE = "MAME_TI99_Starter.settings";

    private static final Preferences PREFS =
            Preferences.userNodeForPackage(SettingsPathRegistry.class);

    private SettingsPathRegistry() {
        // Utility class
    }

    // -------------------------------------------------------------------------
    // Access
    // -------------------------------------------------------------------------

    /**
     * Returns the currently stored settings file path.
     * <p>
     * If no path is stored, the default file name is returned.
     */
    public static Path getCurrent() {
        String stored = PREFS.get(PREF_KEY, "");

        if (stored == null || stored.isBlank()) {
            return Paths.get(DEFAULT_FILE);
        }
        return Paths.get(stored);
    }

    /**
     * Stores the given path as the current settings file.
     *
     * @param path the settings file path, or {@code null} to clear the entry
     */
    public static void setCurrent(Path path) {
        if (path != null) {
            PREFS.put(PREF_KEY, path.toAbsolutePath().toString());
        }
    }

    // -------------------------------------------------------------------------
    // Validation
    // -------------------------------------------------------------------------

    /**
     * Clears the stored settings path if it no longer points to an existing file.
     */
    public static void cleanupCurrentIfInvalid() {
        Path current = getCurrent();

        if (current != null && !Files.exists(current)) {
            setCurrent(null); // or clearCurrent() if implemented later
        }
    }
}
