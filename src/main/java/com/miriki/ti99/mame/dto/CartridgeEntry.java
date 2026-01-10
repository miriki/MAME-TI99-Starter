package com.miriki.ti99.mame.dto;

import java.nio.file.Path;

import com.miriki.ti99.mame.domain.MediaExtensions;

/**
 * Represents a cartridge media entry.
 * <p>
 * Cartridges may be stored as plain files or as pathless container formats
 * (e.g. ZIP archives). This class overrides path resolution accordingly and
 * provides a special {@code none()} entry used for UI selection lists.
 */
public class CartridgeEntry extends MediaEntry {

    /**
     * Creates a new cartridge entry.
     *
     * @param basePath the base directory
     * @param baseName the media name
     * @param baseExt  the file extension
     */
    public CartridgeEntry(String basePath, String baseName, String baseExt) {
        super(basePath, baseName, baseExt);
    }

    /**
     * Returns the full path to the cartridge file.
     * <p>
     * For pathless formats (e.g. ZIP), {@code null} is returned because such
     * entries do not correspond to a single concrete file path.
     */
    @Override
    public Path getFullPath() {
        if (MediaExtensions.isPathless(getMediaExt())) {
            return null;
        }
        return super.getFullPath();
    }

    /**
     * Returns whether this entry represents the special "none" placeholder.
     *
     * @return {@code true} if this entry is the placeholder, otherwise {@code false}
     */
    public boolean isNone() {
        return "------".equals(mediaName);
    }

    /**
     * Creates the special placeholder entry used in UI selection lists.
     *
     * @return a new placeholder cartridge entry
     */
    public static CartridgeEntry none() {
        return new CartridgeEntry("", "------", "");
    }
}
