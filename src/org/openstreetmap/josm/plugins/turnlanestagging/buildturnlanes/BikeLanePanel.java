// License: GPL. For details, see LICENSE file.
package org.openstreetmap.josm.plugins.turnlanestagging.buildturnlanes;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

import javax.swing.BorderFactory;
import javax.swing.JPanel;

/**
 * A narrow green panel representing a bicycle lane at the edge of the road,
 * shown next to the general traffic lanes in the lane editor so the position
 * of the cycleway is visible at a glance.
 */
public class BikeLanePanel extends JPanel {

    private static final Color COLOR_BIKE_BG = new Color(160, 220, 160);

    private String mode = "lane";

    public BikeLanePanel() {
        setOpaque(true);
        setBackground(COLOR_BIKE_BG);
        setPreferredSize(new Dimension(54, 60));
        setMinimumSize(new Dimension(54, 40));
        setMaximumSize(new Dimension(54, Integer.MAX_VALUE));
        setBorder(BorderFactory.createLineBorder(new Color(40, 120, 40)));
        setToolTipText("cycleway");
    }

    /**
     * Sets the {@code cycleway} value this panel represents.
     *
     * @param mode the cycleway value (lane, track, shared_lane, ...)
     */
    public void setMode(String mode) {
        this.mode = mode == null ? "lane" : mode;
        setToolTipText("cycleway=" + this.mode);
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g.create();
        try {
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            int cx = getWidth() / 2;
            int cy = getHeight() / 2 - 6;
            g2.setColor(new Color(20, 90, 20));
            g2.setStroke(new BasicStroke(2f));
            // Wheels
            g2.drawOval(cx - 16, cy + 2, 12, 12);
            g2.drawOval(cx + 4, cy + 2, 12, 12);
            // Frame
            g2.drawLine(cx - 10, cy + 8, cx - 2, cy - 10);
            g2.drawLine(cx - 10, cy + 8, cx + 6, cy + 8);
            g2.drawLine(cx - 2, cy - 10, cx + 10, cy + 8);
            g2.drawLine(cx - 2, cy - 10, cx - 6, cy - 10);
            // Mode label
            String label = "shared_lane".equals(mode) ? "shared" : mode;
            g2.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 9));
            FontMetrics fm = g2.getFontMetrics();
            g2.drawString(label, cx - fm.stringWidth(label) / 2, getHeight() - 4);
        } finally {
            g2.dispose();
        }
    }
}
