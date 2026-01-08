package MameTools.model;

public enum DiskFormat {

    // Single-Sided Single-Density (90k)
    SSSD(40, 1, 9, 1),

    // Single-Sided Double-Density (180k)
    SSDD(40, 1, 18, 2),

    // Double-Sided Single-Density (180k)
    DSSD(40, 2, 9, 1),

    // Double-Sided Double-Density (360k)
    DSDD(40, 2, 18, 2);

    private final int tracksPerSide;
    private final int sides;
    private final int sectorsPerTrack;
    private final int densityCode;

    DiskFormat(int tracksPerSide, int sides, int sectorsPerTrack, int densityCode) {
        this.tracksPerSide = tracksPerSide;
        this.sides = sides;
        this.sectorsPerTrack = sectorsPerTrack;
        this.densityCode = densityCode;
    }

    public int getTracksPerSide() {
        return tracksPerSide;
    }

    public int getSides() {
        return sides;
    }

    public int getSectorsPerTrack() {
        return sectorsPerTrack;
    }

    public int getDensityCode() {
        return densityCode;
    }

    public int getSectorCount() {
        return tracksPerSide * sides * sectorsPerTrack;
    }
}
