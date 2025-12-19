package persistence;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.prefs.Preferences;

public class SettingsUsageRegistry {
    private static final String PREF_KEY = "settingsUsage";
    private static final Preferences PREFS = Preferences.userNodeForPackage(SettingsUsageRegistry.class);

    // Map: Pfad -> Count
    private static Map<Path, Integer> usageMap = new HashMap<>();
    private static Map<Path, LocalDateTime> lastUsedMap = new HashMap<>();

    public enum SortMode { BY_COUNT, BY_DATE }

    private static SortMode sortMode = SortMode.BY_COUNT;

    static {
        load();
    }

    public static void setSortMode(SortMode mode) {
        sortMode = mode;
        save(); // optional, wenn persistent
    }

    public static SortMode getSortMode() {
        return sortMode;
    }
    
    public static void cleanupInvalidEntries() {
        // Kopie der Keys, um ConcurrentModification zu vermeiden
        List<Path> toRemove = new ArrayList<>();
        for (Path p : usageMap.keySet()) {
            if (!Files.exists(p)) {
                toRemove.add(p);
            }
        }
        toRemove.forEach(usageMap::remove);
        save(); // Änderungen persistieren
    } // cleanupInvalidEntries
    
    public static void removeEntry(Path p) {
        usageMap.remove(p);
        save();
    } // removeEntry
    
    private static void load() {
        usageMap.clear();
        String raw = PREFS.get(PREF_KEY, "");
        if (!raw.isBlank()) {
            for (String entry : raw.split(";")) {
                String[] parts = entry.split("\\|");
                if (parts.length == 2) {
                    Path p = Paths.get(parts[0]);
                    int count = Integer.parseInt(parts[1]);
                    usageMap.put(p, count);
                }
            }
        }
    } // load

    private static void save() {
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<Path,Integer> e : usageMap.entrySet()) {
            sb.append(e.getKey().toAbsolutePath()).append("|").append(e.getValue()).append(";");
        }
        PREFS.put(PREF_KEY, sb.toString());
    } // save

    public static void renameEntry(Path oldPath, Path newPath) {
        // Usage-Zähler übernehmen
        Integer count = usageMap.remove(oldPath);
        if (count != null) {
            usageMap.put(newPath, count);
        }
        save(); // Änderungen persistieren
    } // renameEntry

    public static void increment(Path file) {
        usageMap.merge(file, 1, Integer::sum);
        save();
    }

    public static void updateLastUsed(Path p) {
        lastUsedMap.put(p, LocalDateTime.now());
        save();
    }
    
    private static List<Path> getTopByCount(int x) {
        return usageMap.entrySet().stream()
            .sorted((a, b) -> Integer.compare(b.getValue(), a.getValue()))
            .limit(x)
            .map(Map.Entry::getKey)
            .toList();
    }
    
    private static List<Path> getTopByDate(int x) {
        return lastUsedMap.entrySet().stream()
            .sorted((a, b) -> b.getValue().compareTo(a.getValue()))
            .limit(x)
            .map(Map.Entry::getKey)
            .toList();
    }
    
    public static List<Path> getTop(int x) {
        return switch (sortMode) {
            case BY_COUNT -> getTopByCount(x);
            case BY_DATE  -> getTopByDate(x);
        };
    } // getTop
    
}
