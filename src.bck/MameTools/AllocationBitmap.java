package MameTools;

import java.util.Arrays;

// import org.slf4j.Logger;
// import org.slf4j.LoggerFactory;

public class AllocationBitmap {

    // private static final Logger log = LoggerFactory.getLogger(AllocationBitmap.class);

    public static final int ABM_OFFSET = 56;
    public static final int ABM_SIZE   = 200; // TI-DOS Standard

    public static void initEmpty(byte[] dsk, int sectorCount) {

        // Immer 200 Bytes löschen – TI-konform
        Arrays.fill(dsk, ABM_OFFSET, ABM_OFFSET + ABM_SIZE, (byte)0x00);

        // Directory-Sektor leeren (Sektor 1)
        Arrays.fill(dsk, 256, 256 + 256, (byte)0x00);

        // Directory-Extension-Sektor leeren (Sektor 2)
        Arrays.fill(dsk, 512, 512 + 256, (byte)0x00);
        
        // Sektor 0 (VIB) und 1 (Directory) und 2 (DirExtended) belegt
        markRange(dsk, 0, 2);
    }

    public static void markRange(byte[] dsk, int startSector, int count) {
        int end = startSector + count;
        for (int s = startSector; s < end; s++) {
            markUsed(dsk, s);
        }
    }

    public static void markUsed(byte[] dsk, int sector) {

        // ClusterSize=1 → 1 Bit pro Sektor
        int bitIndex = sector;

        // Byte innerhalb der ABM
        int abmByteIndex = bitIndex / 8;

        // Wenn außerhalb der 200-Byte-ABM → ignorieren
        if (abmByteIndex >= ABM_SIZE) {
            return;
        }

        int arrayIndex = ABM_OFFSET + abmByteIndex;
        int bitInByte  = 7 - (bitIndex % 8);

        dsk[arrayIndex] |= (byte)(1 << bitInByte);
    }

    public static boolean isFree(byte[] dsk, int sector, int sectorCount) {

        int bitIndex = sector;
        int abmByteIndex = bitIndex / 8;

        // Sektoren außerhalb der ABM gelten als frei
        if (abmByteIndex >= ABM_SIZE) {
            return true;
        }

        int arrayIndex = ABM_OFFSET + abmByteIndex;
        int bitInByte  = 7 - (bitIndex % 8);

        return (dsk[arrayIndex] & (1 << bitInByte)) == 0;
    }
}
