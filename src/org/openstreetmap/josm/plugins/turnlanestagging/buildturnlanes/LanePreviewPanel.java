// License: GPL. For details, see LICENSE file.
package org.openstreetmap.josm.plugins.turnlanestagging.buildturnlanes;

import static org.openstreetmap.josm.tools.I18n.tr;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.util.ArrayList;
import java.util.List;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JPanel;

import org.openstreetmap.josm.plugins.turnlanestagging.bean.BLane;
import org.openstreetmap.josm.plugins.turnlanestagging.bean.BLanes;
import org.openstreetmap.josm.plugins.turnlanestagging.bean.BRoad;
import org.openstreetmap.josm.plugins.turnlanestagging.util.Util;
import org.openstreetmap.josm.tools.ImageProvider;

/**
 * Renders a road cross-section so the user can see, at a glance, how many lanes
 * the road has, which side each lane travels on and which turn each lane allows.
 * It complements the per-lane check-box editors by showing the resulting road
 * (including lane-change restrictions) instead of only the editing controls.
 */
public class LanePreviewPanel extends JPanel {

    private BRoad road = null;

    // Colors used to tint a lane according to its travel direction.
    private static final Color COLOR_FORWARD = new Color(120, 170, 255, 110);
    private static final Color COLOR_BACKWARD = new Color(255, 140, 120, 110);
    private static final Color COLOR_BOTH_WAYS = new Color(255, 210, 90, 120);
    private static final Color COLOR_ROAD = new Color(82, 82, 82);
    private static final Color COLOR_DIVIDER = new Color(255, 205, 0);
    private static final Color COLOR_LANE_LINE = Color.WHITE;

    public LanePreviewPanel() {
        super();
        setOpaque(true);
        setBackground(new Color(245, 245, 245));
        setPreferredSize(new Dimension(600, 140));
        setMinimumSize(new Dimension(200, 110));
    }

    /**
     * Sets the road to preview and refreshes the panel.
     *
     * @param road the road to draw, or {@code null} to clear the preview
     */
    public void setRoad(BRoad road) {
        this.road = road;
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g.create();
        try {
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

            List<BLane> lanes = orderedLanes();
            if (lanes.isEmpty()) {
                g2.setColor(Color.DARK_GRAY);
                g2.drawString(tr("No lanes"), 12, getHeight() / 2);
                return;
            }

            int w = getWidth();
            int h = getHeight();
            int roadTop = 14;
            int roadBottom = h - 22;
            int margin = 12;
            int laneW = Math.max(24, (w - 2 * margin) / lanes.size());
            int roadW = laneW * lanes.size();
            int startX = (w - roadW) / 2;

            // Road surface
            g2.setColor(COLOR_ROAD);
            g2.fillRect(startX, roadTop, roadW, roadBottom - roadTop);

            // Direction tint per lane
            for (int i = 0; i < lanes.size(); i++) {
                Color tint = directionColor(lanes.get(i));
                if (tint != null) {
                    g2.setColor(tint);
                    g2.fillRect(startX + i * laneW, roadTop, laneW, roadBottom - roadTop);
                }
            }

            // Lane separators and centre divider. Every boundary is drawn in
            // two halves: solid beside the lane that may not cross it (per
            // change:lanes) and dashed beside the lane that may, so e.g.
            // "not_right|yes" shows a solid half-line next to a dashed one.
            // The centre divider additionally follows the divider setting and
            // is yellow; lane_markings=no hides the white separators.
            String divider = road.getDivider();
            boolean laneMarkings = !"no".equals(road.getLaneMarkings());
            List<Integer> centerAt = yellowBoundaries();
            for (int i = 1; i < lanes.size(); i++) {
                int x = startX + i * laneW;
                BLane left = lanes.get(i - 1);
                BLane right = lanes.get(i);
                boolean leftBlocked = Util.blocksLargerOffsetSide(left);
                boolean rightBlocked = Util.blocksSmallerOffsetSide(right);
                if (centerAt.contains(i)) {
                    drawDivider(g2, x, roadTop, roadBottom, divider, leftBlocked, rightBlocked);
                } else if (laneMarkings) {
                    drawLaneBoundary(g2, x, roadTop, roadBottom, leftBlocked, rightBlocked);
                }
            }

            // Lane contents
            for (int i = 0; i < lanes.size(); i++) {
                int cx = startX + i * laneW + laneW / 2;
                drawLane(g2, lanes.get(i), cx, roadTop, roadBottom, laneW, h);
            }
        } finally {
            g2.dispose();
        }
    }

    /**
     * Collects lanes in the order they are laid out on screen:
     * left block (A), centre block (B) then right block (C) for bidirectional
     * roads, or the single block for unidirectional roads.
     */
    private List<BLane> orderedLanes() {
        List<BLane> result = new ArrayList<>();
        if (road == null) {
            return result;
        }
        if ("Unidirectional".equals(road.getName())) {
            result.addAll(road.getLanesUnid().getLanes());
        } else {
            addBlockLanes(result, road.getLanesA());
            addBlockLanes(result, road.getLanesB());
            addBlockLanes(result, road.getLanesC());
        }
        return result;
    }

    /**
     * Appends one lane block to the result list. Lanes travelling backward are
     * laid out right-to-left on a top-down road view (the same reversal the
     * editor applies), so their list order is reversed to keep the preview
     * aligned with both the editor and the physical road.
     */
    private void addBlockLanes(List<BLane> result, BLanes block) {
        List<BLane> lanes = block.getLanes();
        if ("backward".equals(block.getType())) {
            for (int i = lanes.size() - 1; i >= 0; i--) {
                result.add(lanes.get(i));
            }
        } else {
            result.addAll(lanes);
        }
    }

    /**
     * Returns the lane indices at which a yellow centre divider is drawn.
     * For bidirectional roads these are the boundaries between the left/centre
     * and centre/right blocks, which separate the two directions of travel.
     */
    private List<Integer> yellowBoundaries() {
        List<Integer> result = new ArrayList<>();
        if (road == null || "Unidirectional".equals(road.getName())) {
            return result;
        }
        int nA = road.getLanesA().getLanes().size();
        int nB = road.getLanesB().getLanes().size();
        if (nA > 0 && nB > 0) {
            result.add(nA);
        }
        if (nB > 0 && road.getLanesC().getLanes().size() > 0) {
            result.add(nA + nB);
        }
        if (nB == 0 && nA > 0 && road.getLanesC().getLanes().size() > 0) {
            result.add(nA);
        }
        return result;
    }

    private void drawDashedLaneLine(Graphics2D g2, int x, int y1, int y2) {
        g2.setColor(COLOR_LANE_LINE);
        g2.setStroke(new BasicStroke(2f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL,
                0f, new float[] {6f, 6f}, 0f));
        g2.drawLine(x, y1, x, y2);
    }

    /**
     * Draws a solid white boundary between two lanes, marking a boundary that
     * a {@code change:lanes} restriction forbids crossing.
     */
    private void drawSolidLaneLine(Graphics2D g2, int x, int y1, int y2) {
        g2.setColor(COLOR_LANE_LINE);
        g2.setStroke(new BasicStroke(2.5f));
        g2.drawLine(x, y1, x, y2);
    }

    /**
     * Draws one white boundary between same-direction lanes. Each side of the
     * boundary is rendered separately: solid beside the lane whose
     * {@code change:lanes} value forbids crossing, dashed beside the lane
     * that may still cross, so asymmetric restrictions stay visible.
     */
    private void drawLaneBoundary(Graphics2D g2, int x, int y1, int y2, boolean leftBlocked, boolean rightBlocked) {
        if (leftBlocked && rightBlocked) {
            drawSolidLaneLine(g2, x, y1, y2);
        } else if (!leftBlocked && !rightBlocked) {
            drawDashedLaneLine(g2, x, y1, y2);
        } else {
            drawHalfLine(g2, x - 3, y1, y2, leftBlocked, COLOR_LANE_LINE);
            drawHalfLine(g2, x + 3, y1, y2, rightBlocked, COLOR_LANE_LINE);
        }
    }

    /**
     * Draws one half of a boundary line: solid when {@code solid}, dashed
     * otherwise.
     */
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

    /**
     * Draws the centre divider between opposite directions according to the
     * {@code divider} setting. An empty divider falls back to a dashed yellow
     * centre line so both directions stay distinguishable from white lane
     * separators. {@code change:lanes} restrictions upgrade the affected half
     * of the divider to solid, since that side may not be crossed.
     */
    private void drawDivider(Graphics2D g2, int x, int y1, int y2, String divider, boolean leftBlocked, boolean rightBlocked) {
        if ("double_solid_line".equals(divider)) {
            g2.setColor(COLOR_DIVIDER);
            g2.setStroke(new BasicStroke(3f));
            g2.drawLine(x - 3, y1, x - 3, y2);
            g2.drawLine(x + 3, y1, x + 3, y2);
            return;
        }
        boolean solidLeft = "solid_line".equals(divider) || leftBlocked;
        boolean solidRight = "solid_line".equals(divider) || rightBlocked;
        if (solidLeft && solidRight) {
            g2.setColor(COLOR_DIVIDER);
            g2.setStroke(new BasicStroke(4f));
            g2.drawLine(x, y1, x, y2);
        } else if (!solidLeft && !solidRight) {
            g2.setColor(COLOR_DIVIDER);
            g2.setStroke(new BasicStroke(3f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL,
                    0f, new float[] {8f, 6f}, 0f));
            g2.drawLine(x, y1, x, y2);
        } else {
            drawHalfLine(g2, x - 3, y1, y2, solidLeft, COLOR_DIVIDER);
            drawHalfLine(g2, x + 3, y1, y2, solidRight, COLOR_DIVIDER);
        }
    }

    private Color directionColor(BLane lane) {
        String type = lane.getType();
        if ("forward".equals(type) || "unid".equals(type)) {
            return COLOR_FORWARD;
        } else if ("backward".equals(type)) {
            return COLOR_BACKWARD;
        } else if ("both_ways".equals(type)) {
            return COLOR_BOTH_WAYS;
        }
        return null;
    }

    private void drawLane(Graphics2D g2, BLane lane, int cx, int roadTop, int roadBottom, int laneW, int height) {
        // Direction arrow near the top of the lane.
        drawDirectionArrow(g2, cx, roadTop + 4, lane.getType());

        // Turn arrow icons: a single icon is centred; several are laid out in
        // one horizontal row (scaled down if the lane is too narrow) so that
        // they always stay inside the road surface.
        List<Icon> icons = turnIcons(lane);
        if (!icons.isEmpty()) {
            int iconTop = roadTop + 22;
            if (icons.size() == 1) {
                Icon icon = icons.get(0);
                icon.paintIcon(this, g2, cx - icon.getIconWidth() / 2, iconTop);
            } else {
                drawTurnRow(g2, icons, cx, iconTop, laneW);
            }
        }

        // Lane-change restriction near the bottom of the lane.
        String change = lane.getChange();
        if (change != null && !change.isEmpty()) {
            g2.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 9));
            FontMetrics fm = g2.getFontMetrics();
            g2.setColor(changeColor(change));
            int tw = fm.stringWidth(change);
            g2.drawString(change, cx - tw / 2, height - 8);
        }
    }

    /**
     * Lays out several turn icons in a single horizontal row centred in the
     * lane, scaling them down when the lane is too narrow for their natural
     * size.
     */
    private void drawTurnRow(Graphics2D g2, List<Icon> icons, int cx, int top, int laneW) {
        int gap = 2;
        int total = 0;
        for (Icon icon : icons) {
            total += icon.getIconWidth();
        }
        int need = total + gap * (icons.size() - 1);
        if (need <= laneW - 4) {
            int x = cx - need / 2;
            for (Icon icon : icons) {
                icon.paintIcon(this, g2, x, top);
                x += icon.getIconWidth() + gap;
            }
        } else {
            int size = Math.max(8, (laneW - 4 - gap * (icons.size() - 1)) / icons.size());
            int x = cx - (size * icons.size() + gap * (icons.size() - 1)) / 2;
            for (Icon icon : icons) {
                drawScaledIcon(g2, icon, x, top, size);
                x += size + gap;
            }
        }
    }

    private void drawScaledIcon(Graphics2D g2, Icon icon, int x, int y, int size) {
        if (icon instanceof ImageIcon) {
            g2.drawImage(((ImageIcon) icon).getImage(), x, y, size, size, null);
        } else {
            icon.paintIcon(this, g2, x, y);
        }
    }

    private void drawDirectionArrow(Graphics2D g2, int cx, int top, String type) {
        g2.setColor(new Color(30, 30, 30));
        if ("backward".equals(type)) {
            fillTriangle(g2, cx, top + 12, cx - 6, top, cx + 6, top);
        } else if ("both_ways".equals(type)) {
            fillTriangle(g2, cx, top + 16, cx - 6, top + 4, cx + 6, top + 4);
            fillTriangle(g2, cx, top, cx - 6, top + 12, cx + 6, top + 12);
        } else {
            // forward and unidirectional
            fillTriangle(g2, cx, top, cx - 6, top + 12, cx + 6, top + 12);
        }
    }

    private void fillTriangle(Graphics2D g2, int x1, int y1, int x2, int y2, int x3, int y3) {
        g2.fillPolygon(new int[] {x1, x2, x3}, new int[] {y1, y2, y3}, 3);
    }

    /**
     * Maps a lane's turn value(s) to the arrow icons already shipped with the
     * plugin. A lane may carry several turns separated by {@code ;}.
     */
    private List<Icon> turnIcons(BLane lane) {
        List<Icon> result = new ArrayList<>();
        String turn = lane.getTurn();
        if (turn == null || turn.isEmpty()) {
            return result;
        }
        for (String part : turn.split(";")) {
            String name = iconName(part.trim(), lane.getType());
            if (name != null) {
                Icon icon = ImageProvider.get("types", name);
                if (icon != null) {
                    result.add(icon);
                }
            }
        }
        return result;
    }

    private String iconName(String turn, String type) {
        String dir = "backward".equals(type) ? "backward" : "forward";
        switch (turn) {
            case "left":
            case "sharp_left":
                return "left-" + dir + ".png";
            case "through":
                return "through-" + dir + ".png";
            case "right":
            case "sharp_right":
                return "right-" + dir + ".png";
            case "slight_left":
                return "slight_left-" + dir + ".png";
            case "slight_right":
                return "slight_right-" + dir + ".png";
            case "merge_to_left":
                return "merge_to_left-" + dir + ".png";
            case "merge_to_right":
                return "merge_to_right-" + dir + ".png";
            case "reversible":
                return "reversible-both_ways.png";
            case "reverse":
                if ("unid".equals(type)) {
                    return "reverse-unid.png";
                }
                String hand = Util.isRightHandTraffic() ? "righthand" : "lefthand";
                return "reverse-" + hand + "-" + dir + ".png";
            default:
                return null;
        }
    }

    private Color changeColor(String change) {
        switch (change) {
            case "yes":
                return new Color(0, 128, 0);
            case "no":
                return new Color(200, 0, 0);
            case "not_left":
            case "not_right":
                return new Color(200, 120, 0);
            default:
                return Color.DARK_GRAY;
        }
    }
}
