// License: GPL. For details, see LICENSE file.
package org.openstreetmap.josm.plugins.turnlanestagging.buildturnlanes;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

import javax.swing.JPanel;

/**
 * A narrow strip placed between two lane-editor panels that shows the road
 * marking separating them: dashed where lane changes are allowed, solid
 * beside a lane that may not cross (per {@code change:lanes}), and the
 * yellow centre divider between the two directions of travel, following the
 * {@code divider} setting.
 */
public class LaneBoundaryStrip extends JPanel {

    private static final Color COLOR_DIVIDER = new Color(255, 205, 0);
    private static final Color COLOR_LANE_LINE = new Color(70, 70, 70);
    private static final Color COLOR_BIKE = new Color(70, 190, 70);

    private boolean leftBlocked;
    private boolean rightBlocked;
    private boolean centre;
    private String divider = "";
    private boolean laneMarkings = true;
    private boolean bike;

    public LaneBoundaryStrip() {
        setOpaque(false);
        setPreferredSize(new Dimension(16, 60));
        setMinimumSize(new Dimension(10, 40));
        setMaximumSize(new Dimension(16, Integer.MAX_VALUE));
    }

    /**
     * Updates the marking drawn by this strip and repaints it.
     *
     * @param leftBlocked whether the lane on the left may not cross to the right
     * @param rightBlocked whether the lane on the right may not cross to the left
     * @param centre whether this boundary separates the two directions of travel
     * @param divider the {@code divider} setting for centre boundaries
     * @param laneMarkings whether lane markings are drawn at all
     */
    public void setState(boolean leftBlocked, boolean rightBlocked, boolean centre, String divider, boolean laneMarkings) {
        this.leftBlocked = leftBlocked;
        this.rightBlocked = rightBlocked;
        this.centre = centre;
        this.divider = divider == null ? "" : divider;
        this.laneMarkings = laneMarkings;
        repaint();
    }

    /**
     * Marks this strip as a green bicycle-lane edge marker.
     */
    public void setBike() {
        this.bike = true;
        setPreferredSize(new Dimension(10, 60));
        setMinimumSize(new Dimension(6, 40));
        setMaximumSize(new Dimension(10, Integer.MAX_VALUE));
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g.create();
        try {
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            int x = getWidth() / 2;
            int y1 = 4;
            int y2 = getHeight() - 4;
            if (bike) {
                g2.setColor(COLOR_BIKE);
                g2.fillRect(x - 2, y1, 4, y2 - y1);
            } else if (centre) {
                drawCentre(g2, x, y1, y2);
            } else if (laneMarkings) {
                drawLaneBoundary(g2, x, y1, y2);
            }
        } finally {
            g2.dispose();
        }
    }

    private void drawLaneBoundary(Graphics2D g2, int x, int y1, int y2) {
        if (leftBlocked && rightBlocked) {
            drawHalfLine(g2, x, y1, y2, true, COLOR_LANE_LINE);
        } else if (!leftBlocked && !rightBlocked) {
            drawHalfLine(g2, x, y1, y2, false, COLOR_LANE_LINE);
        } else {
            drawHalfLine(g2, x - 3, y1, y2, leftBlocked, COLOR_LANE_LINE);
            drawHalfLine(g2, x + 3, y1, y2, rightBlocked, COLOR_LANE_LINE);
        }
    }

    private void drawCentre(Graphics2D g2, int x, int y1, int y2) {
        if ("double_solid_line".equals(divider)) {
            drawHalfLine(g2, x - 3, y1, y2, true, COLOR_DIVIDER);
            drawHalfLine(g2, x + 3, y1, y2, true, COLOR_DIVIDER);
            return;
        }
        boolean solidLeft = "solid_line".equals(divider) || leftBlocked;
        boolean solidRight = "solid_line".equals(divider) || rightBlocked;
        if (solidLeft && solidRight) {
            g2.setColor(COLOR_DIVIDER);
            g2.setStroke(new BasicStroke(4f));
            g2.drawLine(x, y1, x, y2);
        } else if (!solidLeft && !solidRight) {
            drawHalfLine(g2, x, y1, y2, false, COLOR_DIVIDER);
        } else {
            drawHalfLine(g2, x - 3, y1, y2, solidLeft, COLOR_DIVIDER);
            drawHalfLine(g2, x + 3, y1, y2, solidRight, COLOR_DIVIDER);
        }
    }

    private void drawHalfLine(Graphics2D g2, int x, int y1, int y2, boolean solid, Color color) {
        g2.setColor(color);
        if (solid) {
            g2.setStroke(new BasicStroke(2.5f));
        } else {
            g2.setStroke(new BasicStroke(2f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL,
                    0f, new float[] {6f, 6f}, 0f));
        }
        g2.drawLine(x, y1, x, y2);
    }
}
