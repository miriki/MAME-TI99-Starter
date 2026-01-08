package MameTools.model;

// import org.slf4j.Logger;
// import org.slf4j.LoggerFactory;

public class DirectoryEntry {

    // private static final Logger log = LoggerFactory.getLogger(DirectoryEntry.class);

    public static final int ENTRY_SIZE = 38;

    private String tiName;
    private int flags;            // PROGRAM/DATA etc.
    private int recordsPerSector; // 0 für PROGRAM
    private int recordCount;
    private int startSector;      // Startsektor
    private int[] clusterList; // Sektornummern
    private int sectorCount;      // Anzahl belegter Sektoren
    private int recordLength;     // 0 für PROGRAM
    private int eofOffset;        // 0 für PROGRAM


    public DirectoryEntry() {
        this.clusterList = new int[16]; // 16 * 2 Bytes = 32 Bytes
    }

    public String getTiName() { return tiName; }
    public void setTiName(String tiName) { this.tiName = tiName; }

    public int getFlags() { return flags; }
    public void setFlags(int flags) { this.flags = flags; }

    public int getRecordsPerSector() { return recordsPerSector; }
    public void setRecordsPerSector(int recordsPerSector) { this.recordsPerSector = recordsPerSector; }

    public int getRecordCount() { return recordCount; }
    public void setRecordCount(int recordCount) { this.recordCount = recordCount; }

    public int getStartSector() { return startSector; }
    public void setStartSector(int startSector) { this.startSector = startSector; }

    public int[] getClusterList() { return clusterList; }
    public void setClusterList(int[] clusterList) { this.clusterList = clusterList; }

    // public String getFileName() { return tiName; }
    // public void setFileName(String fileName) { this.tiName = fileName; }

    public int getSectorCount() { return sectorCount; }
    public void setSectorCount(int sectorCount) { this.sectorCount = sectorCount; }

    public int getRecordLength() { return recordLength; }
    public void setRecordLength(int recordLength) { this.recordLength = recordLength; }

    public int getEofOffset() { return eofOffset; }
    public void setEofOffset(int eofOffset) { this.eofOffset = eofOffset; }

}
