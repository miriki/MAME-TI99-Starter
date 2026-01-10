package com.miriki.ti99.mame.ui.debug;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.JPanel;

/**
 * A transparent overlay panel that draws a dashed grid.
 * <p>
 * Useful for debugging absolute layouts and verifying alignment of UI elements.
 */
public class GridOverlayPanel extends JPanel {

    private static final long serialVersionUID = 1L;

    private final int gridX;
    private final int gridY;
    private final int offsX;
    private final int offsY;

    /**
     * Creates a new grid overlay.
     *
     * @param gridX spacing between vertical grid lines
     * @param gridY spacing between horizontal grid lines
     * @param offsX horizontal offset before the first vertical line
     * @param offsY vertical offset before the first horizontal line
     */
    public GridOverlayPanel(int gridX, int gridY, int offsX, int offsY) {
        this.gridX = gridX;
        this.gridY = gridY;
        this.offsX = offsX;
        this.offsY = offsY;

        setOpaque(false);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2 = (Graphics2D) g;
        g2.setColor(Color.LIGHT_GRAY);

        float[] dashPattern = {2f, 6f};
        g2.setStroke(new BasicStroke(
                1f,
                BasicStroke.CAP_BUTT,
                BasicStroke.JOIN_MITER,
                10f,
                dashPattern,
                0f
        ));

        int w = getWidth();
        int h = getHeight();

        // Vertical lines
        for (int x = offsX; x < w; x += gridX) {
            g2.drawLine(x, 0, x, h);
        }

        // Horizontal lines
        for (int y = offsY; y < h; y += gridY) {
            g2.drawLine(0, y, w, y);
        }
    }
}
