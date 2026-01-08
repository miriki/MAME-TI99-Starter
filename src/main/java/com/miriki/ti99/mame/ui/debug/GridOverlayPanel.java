package com.miriki.ti99.mame.ui.debug;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.JPanel;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

//############################################################################

public class GridOverlayPanel extends JPanel {

	private static final long serialVersionUID = 1L;
	private final int gridX;
	private final int gridY;
	private final int offsX;
	private final int offsY;

    private static final Logger log = LoggerFactory.getLogger( GridOverlayPanel.class );
	
	// --------------------------------------------------
	
    public GridOverlayPanel( int gridX, int gridY, int offsX, int offsY ) {
    	
		log.debug( "----- start: GridOverlayPanel( {}, {}, {}, {} )", gridX, gridY, offsX, offsY );
		
        this.gridX = gridX;
        this.gridY = gridY;
        this.offsX = offsX;
        this.offsY = offsY;
        setOpaque( false );

        log.debug( "----- end: GridOverlayPanel()" );

    } // [constructor] GridOverlayPanel

	// --------------------------------------------------
	
    @Override
    protected void paintComponent( Graphics g ) {
    
		// log.debug( "----- start: paintComponent( {} )", g );
		    	
    	super.paintComponent( g );
        Graphics2D g2 = ( Graphics2D ) g;
        g2.setColor( Color.LIGHT_GRAY );

        float[] dashPattern = { 2f, 6f };
        g2.setStroke( new BasicStroke( 1f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 10f, dashPattern, 0f ));

        int w = getWidth();
        int h = getHeight();

        for ( int x = offsX; x < w; x += gridX ) {
            g2.drawLine( x, 0, x, h );
        }
        
        for ( int y = offsY; y < h; y += gridY ) {
            g2.drawLine( 0, y, w, y );
        }

        // log.debug( "----- end: paintComponent()" );

    } // paintComponent

	// --------------------------------------------------
	
}

//############################################################################
