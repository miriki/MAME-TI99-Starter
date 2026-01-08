package MameTools;

import java.io.File;
import java.io.FileOutputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import MameTools.model.FiadFile;
import MameTools.model.DirectoryEntry;
import MameTools.model.DiskFormat;

public class DskImageWriter {

    private static final Logger log = LoggerFactory.getLogger(DskImageWriter.class);

    public static byte[] createEmptyImage(DiskFormat format) {
        return new byte[format.getSectorCount() * 256];
    }

    /*
    public static void addFile(byte[] dsk, FiadFile fiad) {
        // 1. freien Directory-Eintrag finden
        int dirOffset = findFreeDirectoryOffset(dsk);

        // 2. freie Sektoren finden
        int sectorCount = (fiad.getData().length + 255) / 256;
        int startSector = findFreeSectors(dsk, sectorCount);

        // 3. Datei in Sektoren schreiben
        writeFileDataToSectors(dsk, fiad.getData(), startSector);

        // 4. Directory-Entry erzeugen
        DirectoryEntry entry = DirectoryEntryIO.fromFiad(
                fiad.getTiName(),
                startSector,
                sectorCount
        );

        // 5. Directory-Entry ins Image schreiben
        DirectoryEntryIO.writeEntry(dsk, dirOffset, entry);
    }
    */
    
    /*
    public static void addFile(byte[] dsk, FiadFile fiad) {
        int dirOffset = findFreeDirectoryOffset(dsk);
        int startSector = findFreeSectors(dsk, fiad.getSectorCount());
        writeFileDataToSectors(dsk, fiad.getData(), startSector);

        DirectoryEntry entry = DirectoryEntryIO.fromFiad(
            fiad.getTiName(),
            startSector,
            fiad.getSectorCount(),
            fiad.getType(),
            fiad.getRecordLength()
        );

        DirectoryEntryIO.writeEntry(dsk, dirOffset, entry);
    }
    */

    public static void addFile(byte[] dsk, FiadFile fiad) {
        int totalSectors = dsk.length / 256;
        int startSector = findFreeSectors(dsk, fiad.getSectorCount(), totalSectors);

        log.debug(">>> addFile called for {}", fiad.getTiName());
        log.debug("Fiad size={} bytes, sectors={}", fiad.getData().length, fiad.getSectorCount());

        // Daten schreiben
        writeFileDataToSectors(dsk, fiad.getData(), startSector);

        // ABM aktualisieren
        AllocationBitmap.markRange(dsk, startSector, fiad.getSectorCount());

        // Directory-Eintrag
        int dirOffset = findFreeDirectoryOffset(dsk);
        DirectoryEntry entry = DirectoryEntryIO.fromFiad(
            fiad.getTiName(),
            startSector,
            fiad.getSectorCount(),
            fiad.getType(),
            fiad.getRecordLength()
        );
        log.debug("addFile: entry.getTiName()='{}'", entry.getTiName());
        DirectoryEntryIO.writeEntry(dsk, dirOffset, entry);
        log.debug("Marking used sectors {}–{}", startSector, startSector + fiad.getSectorCount() - 1);
        for (int s = startSector; s < startSector + fiad.getSectorCount(); s++) {
            log.debug("ABM after write: sector {} free={}", s, AllocationBitmap.isFree(dsk, s, totalSectors));
        }
    }
    
    public static void writeImage(File out, byte[] image) throws Exception {
        try (FileOutputStream fos = new FileOutputStream(out)) {
            fos.write(image);
        }
    }
    
    private static int findFreeDirectoryOffset(byte[] dsk) {

        // Directory beginnt in Sektor 1 → Offset 256
        int directoryStart = 256;

        // Directory umfasst 2 Sektoren → 512 Bytes
        int directorySize = 2 * 256;

        for (int offset = directoryStart;
             offset < directoryStart + directorySize;
             offset += DirectoryEntry.ENTRY_SIZE) {

            if (dsk[offset] == 0x00) {
                return offset;
            }
        }

        throw new RuntimeException("Directory full – no free entry available");
    }

    /*
    private static int findFreeSectors(byte[] dsk, int needed) {
        int totalSectors = dsk.length / 256;

        int runStart = -1;
        int runLength = 0;

        for (int sector = 2; sector < totalSectors; sector++) {
            int offset = sector * 256;

            boolean empty = true;
            for (int i = 0; i < 256; i++) {
                if (dsk[offset + i] != 0x00) {
                    empty = false;
                    break;
                }
            }

            if (empty) {
                if (runStart == -1) {
                    runStart = sector;
                }
                runLength++;

                if (runLength == needed) {
                    return runStart;
                }
            } else {
                runStart = -1;
                runLength = 0;
            }
        }

        throw new RuntimeException("Not enough contiguous free sectors");
    }
    */
    
    private static int findFreeSectors(byte[] dsk, int needed, int totalSectors) {

        int runStart = -1;
        int runLength = 0;

        // ab Sektor 2 suchen (0 = VIB, 1 = Directory)
        for (int sector = 3; sector < totalSectors; sector++) {

            boolean free = AllocationBitmap.isFree(dsk, sector, totalSectors);

            if (free) {
                if (runStart == -1) {
                    runStart = sector;
                }
                runLength++;

                if (runLength == needed) {
                    return runStart;
                }
            } else {
                runStart = -1;
                runLength = 0;
            }
        }

        throw new RuntimeException("Not enough contiguous free sectors");
    }

    private static void writeFileDataToSectors(byte[] dsk, byte[] data, int startSector) {
        int sector = startSector;
        int pos = 0;

        while (pos < data.length) {
            int offset = sector * 256;

            int remaining = data.length - pos;
            int chunk = Math.min(256, remaining);

            System.arraycopy(data, pos, dsk, offset, chunk);

            // Rest des Sektors bleibt 0x00
            pos += chunk;
            sector++;
        }
    }
}
