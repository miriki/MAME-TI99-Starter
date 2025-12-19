package persistence;

import java.nio.file.*;
import java.util.prefs.Preferences;

public final class SettingsPathRegistry {
	
    private static final String PREF_KEY = "lastSettingsFile";
    private static final String DEFAULT_FILE = "MAME_TI99_Starter.settings";
    private static final Preferences PREFS = Preferences.userNodeForPackage(SettingsPathRegistry.class);

    private SettingsPathRegistry() {}

    public static Path getCurrent() {
        String stored = PREFS.get(PREF_KEY, "");
        if (stored == null || stored.isBlank()) {
            return Paths.get(DEFAULT_FILE);
        }
        return Paths.get(stored);
    }

    public static void setCurrent(Path path) {
        if (path != null) {
            PREFS.put(PREF_KEY, path.toAbsolutePath().toString());
        }
    }

    public static void cleanupCurrentIfInvalid() {
        Path current = getCurrent();
        if (current != null && !Files.exists(current)) {
            setCurrent(null); // oder clearCurrent()
        }
    }

}