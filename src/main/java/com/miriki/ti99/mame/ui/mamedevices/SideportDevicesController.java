package com.miriki.ti99.mame.ui.mamedevices;

import javax.swing.*;
import java.awt.Component;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SideportDevicesController {

    private final JTabbedPane myTabbedPane;
    private final Map<String, Component> tabMap = new HashMap<>();

    public SideportDevicesController( JTabbedPane extTabbedPane ) {
    	
        myTabbedPane = extTabbedPane;
        init();

    }

    private void init() {
    	
        for ( int i = 0; i < myTabbedPane.getTabCount(); i++ ) {

        	String title = myTabbedPane.getTitleAt( i );
            Component comp = myTabbedPane.getComponentAt( i );
            
            String key = PebDevices.toParamName( title );
            tabMap.put( key, comp );

            String pretty = PebDevices.toPrettyTitle( title );
            
            myTabbedPane.setTitleAt( i, pretty );
            
        }

    }

    public void updateTabs( List<String> selectedTitles ) {

        myTabbedPane.removeAll();

        for (String title : selectedTitles) {
            String key = title.toLowerCase(); // oder MirikiSideportDevices.toParamName(title)
            Component comp = tabMap.get( key );
            String pretty = PebDevices.toPrettyTitle( title );

            if ( comp != null ) {
                // myTabbedPane.addTab(pretty, comp);
                myTabbedPane.addTab( pretty, comp );
            }
        }

    }

}
