package MameTools;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;

// import org.slf4j.Logger;
// import org.slf4j.LoggerFactory;

import MameTools.model.DiskFormat;

public class VIBWriter {

    // private static final Logger log = LoggerFactory.getLogger(VIBWriter.class);

    public static void writeVIB(byte[] dsk, DiskFormat format, String volumeName) {

        // 1. Volume Name (Bytes 0–9, max 10 chars)
        byte[] nameBytes = volumeName.toUpperCase().getBytes(StandardCharsets.US_ASCII);
        int len = Math.min(nameBytes.length, 10);
        System.arraycopy(nameBytes, 0, dsk, 0, Math.min(nameBytes.length, 10));

        // 2. Total sectors (Bytes 10–11)
        int sectors = format.getSectorCount();
        dsk[10] = (byte)((sectors >> 8) & 0xFF);
        dsk[11] = (byte)(sectors & 0xFF);

        // 3. Sectors per track (Byte 12)
        dsk[12] = (byte) format.getSectorsPerTrack();

        // 4. "DSK" signature (Bytes 14–15)
        dsk[13] = 'D';
        dsk[14] = 'S';
        dsk[15] = 'K';

        dsk[16] = 0; // reserved
        
        // 5. Tracks per side (Byte 16)
        dsk[17] = (byte) format.getTracksPerSide();

        // 6. Number of sides (Byte 18)
        dsk[18] = (byte) format.getSides();

        // 7. Density (Byte 19)
        dsk[19] = (byte) format.getDensityCode();

        // 8. Allocation Bitmap initialisieren (Sektor 0 = belegt)
        AllocationBitmap.initEmpty(dsk, sectors);
		    
		// 9. Root-Directory-Eintrag im VIB setzen (Directory 1)
		
		// Name "DSKDIR" oder "ROOT" – dein Tool liest 10 Zeichen
		byte[] dirName = "DSKDIR".getBytes(StandardCharsets.US_ASCII);
		Arrays.fill(dsk, 20, 30, (byte) 0x20);          // mit Spaces füllen
		System.arraycopy(dirName, 0, dsk, 20, dirName.length);
		
		// FDI = 1 → Verzeichnis liegt in Sektor 1
		dsk[30] = 0x00;
		dsk[31] = 0x01;

     // Directory 2 und 3 kannst du erstmal leer lassen (0)

    }
}
