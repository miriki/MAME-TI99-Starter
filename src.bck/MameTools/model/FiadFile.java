package MameTools.model;

// import org.slf4j.Logger;
// import org.slf4j.LoggerFactory;

public class FiadFile {

    // private static final Logger log = LoggerFactory.getLogger(FiadFile.class);

    private final String tiName;
    private final TiFileType type;
    private final int recordLength;
    private final int recordCount;

    private final byte[] header;   // 128 bytes
    private final byte[] data;     // encoded data (PROGRAM = raw bytes)

    public FiadFile(String tiName,
                    TiFileType type,
                    int recordLength,
                    int recordCount,
                    byte[] header,
                    byte[] data) {

        this.tiName = tiName;
        this.type = type;
        this.recordLength = recordLength;
        this.recordCount = recordCount;
        this.header = header;
        this.data = data;
    }

    public String getTiName() {
        return tiName;
    }

    public TiFileType getType() {
        return type;
    }

    public int getRecordLength() {
        return recordLength;
    }

    public int getRecordCount() {
        return recordCount;
    }

    public byte[] getHeader() {
        return header;
    }

    public byte[] getData() {
        return data;
    }

    /**
     * Anzahl belegter Sektoren (für DirectoryEntry)
     */
    public int getSectorCount() {
    	int sectors = (data.length + 255) / 256;
    	return Math.max(sectors, 1);
    }
}
