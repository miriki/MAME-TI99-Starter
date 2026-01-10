package com.miriki.ti99.mame.persistence;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.prefs.Preferences;

/**
 * Tracks usage statistics for settings files.
 * <p>
 * The registry stores how often a settings file has been used and when it was
 * last accessed. Only the usage count is persisted; timestamps are kept
 * in-memory only.
 */
public class SettingsUsageRegistry {

    private static final String PREF_KEY = "settingsUsage";
    private static final Preferences PREFS =
            Preferences.userNodeForPackage(SettingsUsageRegistry.class);

    /** Map: settings file → usage count (persisted). */
    private static final Map<Path, Integer> usageMap = new HashMap<>();

    /** Map: settings file → last used timestamp (not persisted). */
    private static final Map<Path, LocalDateTime> lastUsedMap = new HashMap<>();

    public enum SortMode { BY_COUNT, BY_DATE }

    private static SortMode sortMode = SortMode.BY_COUNT;

    // -------------------------------------------------------------------------
    // Initialization
    // -------------------------------------------------------------------------

    static {
        load();
    }

    // -------------------------------------------------------------------------
    // Sort mode
    // -------------------------------------------------------------------------

    public static void setSortMode(SortMode mode) {
        sortMode = mode;
        save(); // optional persistence
    }

    public static SortMode getSortMode() {
        return sortMode;
    }

    // -------------------------------------------------------------------------
    // Cleanup
    // -------------------------------------------------------------------------

    /**
     * Removes entries whose files no longer exist.
     */
    public static void cleanupInvalidEntries() {
        List<Path> toRemove = new ArrayList<>();

        for (Path p : usageMap.keySet()) {
            if (!Files.exists(p)) {
                toRemove.add(p);
            }
        }

        toRemove.forEach(usageMap::remove);
        save();
    }

    /**
     * Removes a single entry from the registry.
     */
    public static void removeEntry(Path p) {
        usageMap.remove(p);
        save();
    }

    // -------------------------------------------------------------------------
    // Persistence
    // -------------------------------------------------------------------------

    private static void load() {
        usageMap.clear();
        lastUsedMap.clear();

        String raw = PREFS.get(PREF_KEY, "");
        if (raw.isBlank()) {
            return;
        }

        for (String entry : raw.split(";")) {
            String[] parts = entry.split("\\|");

            if (parts.length == 2) {
                Path p = Paths.get(parts[0]);
                int count = Integer.parseInt(parts[1]);
                usageMap.put(p, count);

                // ensure lastUsedMap has the same key
                lastUsedMap.putIfAbsent(p, null);
            }
        }
    }

    private static void save() {
        StringBuilder sb = new StringBuilder();

        for (var e : usageMap.entrySet()) {
            sb.append(e.getKey().toAbsolutePath())
              .append("|")
              .append(e.getValue())
              .append(";");
        }

        PREFS.put(PREF_KEY, sb.toString());
    }

    // -------------------------------------------------------------------------
    // Modification
    // -------------------------------------------------------------------------

    /**
     * Renames an entry while preserving its usage count.
     */
    public static void renameEntry(Path oldPath, Path newPath) {
        Integer count = usageMap.remove(oldPath);

        if (count != null) {
            usageMap.put(newPath, count);
        }

        save();
    }

    /**
     * Increments the usage counter for the given file.
     */
    public static void increment(Path file) {
        usageMap.merge(file, 1, Integer::sum);
        save();
    }

    /**
     * Updates the last-used timestamp (not persisted).
     */
    public static void updateLastUsed(Path p) {
        lastUsedMap.put(p, LocalDateTime.now());
        save();
    }

    // -------------------------------------------------------------------------
    // Query
    // -------------------------------------------------------------------------

    private static List<Path> getTopByCount(int x) {
        return usageMap.entrySet().stream()
                .sorted((a, b) -> Integer.compare(b.getValue(), a.getValue()))
                .limit(x)
                .map(Map.Entry::getKey)
                .toList();
    }

    /*
    private static List<Path> getTopByDate(int x) {
        return lastUsedMap.entrySet().stream()
                .sorted((a, b) -> b.getValue().compareTo(a.getValue()))
                .limit(x)
                .map(Map.Entry::getKey)
                .toList();
    }
    */
    private static List<Path> getTopByDate(int x) {
        return lastUsedMap.entrySet().stream()
                .sorted(Comparator.comparing(
                        Map.Entry<Path, LocalDateTime>::getValue,
                        Comparator.nullsLast(Comparator.naturalOrder())
                ).reversed())
                .limit(x)
                .map(Map.Entry::getKey)
                .toList();
    }


    /**
     * Returns the top X entries according to the current sort mode.
     */
    public static List<Path> getTop(int x) {
        return switch (sortMode) {
            case BY_COUNT -> getTopByCount(x);
            case BY_DATE  -> getTopByDate(x);
        };
    }
}
