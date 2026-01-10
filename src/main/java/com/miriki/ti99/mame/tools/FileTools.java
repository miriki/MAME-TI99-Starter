package com.miriki.ti99.mame.tools;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.text.Collator;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;

import com.miriki.ti99.mame.ui.UiConstants;

/**
 * Utility functions for file and directory handling.
 * <p>
 * This class provides helpers for checking file/directory existence,
 * scanning directories for media files, normalizing paths and validating
 * write permissions.
 */
public class FileTools {

    // -------------------------------------------------------------------------
    // Existence checks
    // -------------------------------------------------------------------------

    /**
     * Returns whether the given path points to an existing file.
     */
    public static boolean fileExists(String path) {
        File chk = new File(path);
        return chk.exists() && chk.isFile();
    }

    /**
     * Returns whether the given directory exists.
     */
    public static boolean dirExists(String dir) {
        return dirExists(dir, "");
    }

    /**
     * Returns whether all directories in a semicolon-separated list exist.
     *
     * @param subDirList semicolon-separated list of directories
     * @param baseDir    optional base directory for relative paths
     */
    public static boolean dirExists(String subDirList, String baseDir) {
        Path basePath = Paths.get(baseDir.trim()).normalize();
        String[] subDirs = subDirList.split(";");

        for (String subDir : subDirs) {
            subDir = subDir.trim();
            if (subDir.isEmpty()) continue;

            Path subPath = Paths.get(subDir).normalize();
            Path fullPath = subPath.isAbsolute() ? subPath : basePath.resolve(subPath);

            if (!Files.exists(fullPath) || !Files.isDirectory(fullPath)) {
                return false;
            }
        }

        return true;
    }

    // -------------------------------------------------------------------------
    // Directory scanning
    // -------------------------------------------------------------------------

    /**
     * Scans directories for files matching the given extensions.
     * <p>
     * Returns a sorted list of unique base names (without extension),
     * with {@link UiConstants#CBX_SEL_NONE} inserted at index 0.
     */
    public static List<String> scanDirectories(String basePath,
                                               String subPath,
                                               String... fileExt) {

        Map<String, String> unique = new LinkedHashMap<>();

        for (String dir : subPath.split(";")) {
            File folder = new File(basePath, dir.trim());
            if (!folder.isDirectory()) continue;

            File[] files = folder.listFiles((d, name) -> {
                String lower = name.toLowerCase(Locale.ROOT);
                for (String ext : fileExt) {
                    if (lower.endsWith("." + ext)) return true;
                }
                return false;
            });

            if (files == null) continue;

            for (File file : files) {
                String original = file.getName().trim();
                String lower = original.toLowerCase(Locale.ROOT);

                for (String ext : fileExt) {
                    String dotExt = "." + ext.toLowerCase(Locale.ROOT);
                    if (lower.endsWith(dotExt)) {
                        original = original.substring(0, original.length() - dotExt.length()).trim();
                        break;
                    }
                }

                if (!original.isEmpty()) {
                    unique.putIfAbsent(original.toLowerCase(Locale.ROOT), original);
                }
            }
        }

        List<String> sorted = new ArrayList<>(unique.values());
        Collator collator = Collator.getInstance(Locale.getDefault());
        collator.setStrength(Collator.PRIMARY);
        sorted.sort(collator::compare);

        sorted.add(0, UiConstants.CBX_SEL_NONE);
        return sorted;
    }

    // -------------------------------------------------------------------------
    // Name formatting
    // -------------------------------------------------------------------------

    /**
     * Returns a human-readable name for a settings file.
     */
    public static String prettyName(Path p) {
        String result = p.getFileName().toString();

        if (result.endsWith(".settings")) {
            result = result.substring(0, result.length() - ".settings".length());
        }

        return result.replace('_', ' ');
    }

    /**
     * Returns the editable base name of a settings file (underscores preserved).
     */
    public static String editName(Path p) {
        String result = p.getFileName().toString();

        if (result.endsWith(".settings")) {
            result = result.substring(0, result.length() - ".settings".length());
        }

        return result;
    }

    /**
     * Converts user input into a safe settings filename.
     */
    public static String safeFileName(String userInput) {
        String result = userInput.trim().replace(" ", "_");

        if (!result.endsWith(".settings")) {
            result += ".settings";
        }

        return result;
    }

    // -------------------------------------------------------------------------
    // Path normalization
    // -------------------------------------------------------------------------

    private static String normalizeDir(String dir) {
        Path p = Paths.get(dir).normalize();
        String result = p.toString();

        if (!result.endsWith(File.separator)) {
            result += File.separator;
        }

        return result;
    }

    /**
     * Normalizes a semicolon-separated list of directories.
     */
    public static String normalizeMultiPath(String multi) {
        if (multi == null || multi.isBlank()) {
            return "";
        }

        return Arrays.stream(multi.split(";"))
                .map(String::trim)
                .filter(s -> !s.isEmpty())
                .map(FileTools::normalizeDir)
                .collect(Collectors.joining(";"));
    }

    // -------------------------------------------------------------------------
    // Write permission check
    // -------------------------------------------------------------------------

    /**
     * Returns whether the given file can be written to.
     * <p>
     * If the directory does not exist, it attempts to create it.
     * If the file does not exist, it attempts to create and delete it.
     */
    public static boolean canWriteFile(Path file) {
        try {
            Path dir = file.getParent();
            if (dir == null) return false;

            if (!Files.exists(dir)) {
                Files.createDirectories(dir);
            }

            if (!Files.isWritable(dir)) {
                return false;
            }

            if (Files.exists(file)) {
                return Files.isWritable(file);
            }

            try (OutputStream out = Files.newOutputStream(
                    file,
                    StandardOpenOption.CREATE_NEW,
                    StandardOpenOption.WRITE)) {
                // created successfully
            }

            Files.deleteIfExists(file);
            return true;

        } catch (IOException e) {
            return false;
        }
    }
}
