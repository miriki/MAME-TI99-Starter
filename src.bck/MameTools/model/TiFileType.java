package MameTools.model;

public enum TiFileType {

    PROGRAM(0x00),   // Program file
    DISFIX(0x01),    // Data file, Display, Fixed
    DISVAR(0x81),    // Data file, Display, Variable
    INTFIX(0x03),    // Data file, Internal, Fixed
    INTVAR(0x83);    // Data file, Internal, Variable

    private final int directoryFlags;

    TiFileType(int flags) {
        this.directoryFlags = flags;
    }

    public int getDirectoryFlags() {
        return directoryFlags;
    }
}
