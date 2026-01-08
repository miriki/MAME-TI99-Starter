package com.miriki.ti99.mame.tools;

import java.awt.Image;

import javax.swing.ImageIcon;

// https://openmoji.org/library/#group=flags

//##################################################

public class IconLoader {

    private static final int MENU_ICON_SIZE = 20;

    // --------------------------------------------------
    
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
        
    } // load
    
    // --------------------------------------------------
    
}

//##################################################
