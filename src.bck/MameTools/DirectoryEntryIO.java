package MameTools;

import java.nio.charset.StandardCharsets;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import MameTools.model.DirectoryEntry;
import MameTools.model.TiFileType;

public class DirectoryEntryIO {

    private static final Logger log = LoggerFactory.getLogger(DirectoryEntryIO.class);

	/**
     * Liest einen Directory-Entry aus einem DSK-Image.
     *
     * @param dsk   komplettes Image
     * @param offset Byte-Offset im Image
     */
    public static DirectoryEntry readEntry(byte[] dsk, int offset) {
        DirectoryEntry e = new DirectoryEntry();

        // Name (10 Bytes)
        byte[] nameBytes = new byte[10];
        System.arraycopy(dsk, offset, nameBytes, 0, 10);
        String name = new String(nameBytes, StandardCharsets.US_ASCII).trim();
        e.setTiName(name);

        // Flags
        e.setFlags(dsk[offset + 10] & 0xFF);

        // Records per sector
        e.setRecordsPerSector(dsk[offset + 11] & 0xFF);

        // Record count (2 Bytes, big endian)
        int records = ((dsk[offset + 12] & 0xFF) << 8) | (dsk[offset + 13] & 0xFF);
        e.setRecordCount(records);

        // Start sector
        int start = ((dsk[offset + 14] & 0xFF) << 8) | (dsk[offset + 15] & 0xFF);
        e.setStartSector(start);

        // Cluster list (16 * 2 Bytes)
        int[] clusters = new int[16];
        int pos = offset + 16;
        for (int i = 0; i < 16; i++) {
            int c = ((dsk[pos] & 0xFF) << 8) | (dsk[pos + 1] & 0xFF);
            clusters[i] = c;
            pos += 2;
        }
        e.setClusterList(clusters);

        return e;
    }

    /**
     * Schreibt einen Directory-Entry ins DSK-Image.
     */
    public static void writeEntry(byte[] dsk, int offset, DirectoryEntry e) {
        // Name (10 Bytes, padded mit 0)
        byte[] nameBytes = new byte[10];
        byte[] src = e.getTiName().getBytes(StandardCharsets.US_ASCII);
        System.arraycopy(src, 0, nameBytes, 0, Math.min(src.length, 10));
        System.arraycopy(nameBytes, 0, dsk, offset, 10);

        // Flags
        dsk[offset + 10] = (byte) e.getFlags();

        // Records per sector
        dsk[offset + 11] = (byte) e.getRecordsPerSector();

        // Record count
        dsk[offset + 12] = (byte) ((e.getRecordCount() >> 8) & 0xFF);
        dsk[offset + 13] = (byte) (e.getRecordCount() & 0xFF);

        // Start sector
        dsk[offset + 14] = (byte) ((e.getStartSector() >> 8) & 0xFF);
        dsk[offset + 15] = (byte) (e.getStartSector() & 0xFF);

        // Cluster list
        int[] clusters = e.getClusterList();
        int pos = offset + 16;
        for (int i = 0; i < 16; i++) {
            int c = (clusters != null && i < clusters.length) ? clusters[i] : 0;
            dsk[pos]     = (byte) ((c >> 8) & 0xFF);
            dsk[pos + 1] = (byte) (c & 0xFF);
            pos += 2;
        }
    }

    /**
     * Erzeugt einen Directory-Entry für eine FIAD-Datei (einfacher Fall: linear, keine Fragmentierung).
     */
    public static DirectoryEntry fromFiad(
            String tiName,
            int startSector,
            int sectorCount,
            TiFileType type,
            int recordLength
    ) {
        DirectoryEntry entry = new DirectoryEntry();

        entry.setTiName(tiName);
        entry.setStartSector(startSector);
        entry.setSectorCount(sectorCount);

        // Flags aus dem Typ ableiten
        entry.setFlags(type.getDirectoryFlags());

        // PROGRAM-Dateien haben:
        entry.setRecordLength(recordLength); // = 0
        entry.setRecordsPerSector(0);
        entry.setEofOffset(0);

        log.debug("fromFiad: tiName='{}'", tiName);
        return entry;
    }
}
