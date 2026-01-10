package com.miriki.ti99.mame.tools;

import java.awt.Image;
import javax.swing.ImageIcon;

/**
 * Utility for loading and scaling icons from the application's resource path.
 * <p>
 * Icons are expected to be located under <code>/icons/</code> in the classpath.
 * All loaded icons are scaled to a fixed menu size.
 */
public class IconLoader {

    private static final int MENU_ICON_SIZE = 20;

    /**
     * Loads an icon from the <code>/icons/</code> resource directory and scales
     * it to the standard menu icon size.
     *
     * @param name the filename of the icon (e.g. "flag_de.png")
     * @return a scaled {@link ImageIcon}, or {@code null} if the resource is missing
     */
    public static ImageIcon load(String name) {
        var url = IconLoader.class.getResource("/icons/" + name);

        if (url == null) {
            System.err.println("Icon not found: " + name);
            return null;
        }

        ImageIcon original = new ImageIcon(url);
        Image scaled = original.getImage().getScaledInstance(
                MENU_ICON_SIZE,
                MENU_ICON_SIZE,
                Image.SCALE_SMOOTH
        );

        return new ImageIcon(scaled);
    }
}
