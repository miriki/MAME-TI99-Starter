package MameTools.util;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

public class FileUtils {

    /**
     * Liest eine Datei vollständig als Byte-Array.
     */
    public static byte[] readAllBytes(File file) throws IOException {
        return Files.readAllBytes(file.toPath());
    }

    /**
     * Schreibt ein Byte-Array in eine Datei (überschreibt existierende Datei).
     */
    public static void writeBytes(File file, byte[] data) throws IOException {
        Files.write(file.toPath(), data);
    }

    /**
     * Entfernt die Dateiendung eines Dateinamens.
     */
    public static String stripExtension(String name) {
        int idx = name.lastIndexOf('.');
        return (idx > 0) ? name.substring(0, idx) : name;
    }

    /**
     * Wandelt einen beliebigen Dateinamen in einen TI-kompatiblen Namen um.
     * Maximal 10 Zeichen, nur ASCII, keine Punkte.
     */
    public static String toTiName(String name) {
        String base = stripExtension(name)
                .replaceAll("[^A-Za-z0-9_]", "") // nur sichere Zeichen
                .toUpperCase();

        if (base.length() > 10) {
            base = base.substring(0, 10);
        }

        return base;
    }

    /**
     * Erzeugt den Ausgabepfad für das DSK-Image.
     * Beispiel:
     *   Input:  C:/TI99/FIAD/SomeDiskTest/
     *   Output: C:/TI99/FIAD/SomeDiskTest.dsk
     */
    public static File buildOutputDiskFile(File directory) {
        File parent = directory.getParentFile();
        String name = directory.getName() + ".dsk";
        return new File(parent, name);
    }

    /**
     * Stellt sicher, dass ein Verzeichnis existiert.
     */
    public static void ensureDirectory(File dir) {
        if (!dir.exists()) {
            dir.mkdirs();
        }
    }
}
