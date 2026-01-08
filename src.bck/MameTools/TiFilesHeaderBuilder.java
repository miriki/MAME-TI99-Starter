package MameTools;

import java.nio.charset.StandardCharsets;

// import org.slf4j.Logger;
// import org.slf4j.LoggerFactory;

import MameTools.model.TiFileType;
import MameTools.util.FileUtils;

// byte[] data = Files.readAllBytes( file.toPath() );
// byte[] header = TIFilesHeaderBuilder.buildHeader( file.getName(), data.length );
// FIAD-Datei = Header + Daten

public class TiFilesHeaderBuilder {

    // private static final Logger log = LoggerFactory.getLogger(TiFilesHeaderBuilder.class);

    /**
     * Erzeugt einen vollständigen 128-Byte-TIFILES-Header.
     *
     * @param originalName  Name der Datei im Host-FS
     * @param fileSizeBytes Größe der Nutzdaten
     * @return 128-Byte-Header
     */
	public static byte[] buildHeader(String originalName, int fileSizeBytes, TiFileType type, int recordLength, int recordCount) {
        byte[] header = new byte[128];

        // --------------------------------------------------------------------
        // 1. Magic "TIFILES" + 0x00
        // --------------------------------------------------------------------
        byte[] magic = "TIFILES".getBytes(StandardCharsets.US_ASCII);
        System.arraycopy(magic, 0, header, 0, magic.length);
        header[7] = 0x53; // 'S' von "TIFILES" (zur Sicherheit)
        
        // header[8] = 0x00; // Flags: 0x00 = PROGRAM (binär)
        // Flags 
        switch (type) {
        	case PROGRAM:
        		header[8] = 0x00;
        		break;
        	case DISFIX:
        		header[8] = 0x01;
        		break;
        	case DISVAR:
        		header[8] = 0x02;
        		break;
        	case INTFIX:
        		header[8] = 0x03;
        		break;
        	case INTVAR:
        		header[8] = 0x04;
        		break;
        } 

        // --------------------------------------------------------------------
        // 2. Records pro Sektor (für PROGRAM immer 0)
        // --------------------------------------------------------------------
        // header[9] = 0x00;
        // Records per sector 
        if (type == TiFileType.DISFIX) { 
        	header[9] = (byte) (256 / recordLength); 
        } else { 
        	header[9] = 0x00; 
        } 

        // --------------------------------------------------------------------
        // 3. Anzahl Records = Anzahl Sektoren (256 Byte pro Sektor)
        // --------------------------------------------------------------------
        // header[10] = (byte) ((sectors >> 8) & 0xFF);
        // header[11] = (byte) (sectors & 0xFF);
        // Record count 
        header[10] = (byte)((recordCount >> 8) & 0xFF); 
        header[11] = (byte)(recordCount & 0xFF); 

        // --------------------------------------------------------------------
        // 4. Sektorgröße (immer 256 = 0x0100)
        // --------------------------------------------------------------------
        header[12] = 0x01;
        header[13] = 0x00;

        // --------------------------------------------------------------------
        // 5. Anzahl belegter Sektoren
        // --------------------------------------------------------------------
        int sectors = (fileSizeBytes + 255) / 256;
        header[14] = (byte) ((sectors >> 8) & 0xFF);
        header[15] = (byte) (sectors & 0xFF);

        // --------------------------------------------------------------------
        // 6. TI-Dateiname (max. 10 Zeichen, uppercase, ASCII)
        // --------------------------------------------------------------------
        String tiName = FileUtils.toTiName(originalName);
        byte[] nameBytes = tiName.getBytes(StandardCharsets.US_ASCII);
        System.arraycopy(nameBytes, 0, header, 16, nameBytes.length);

        // Rest bleibt 0
        return header;
    }
}
