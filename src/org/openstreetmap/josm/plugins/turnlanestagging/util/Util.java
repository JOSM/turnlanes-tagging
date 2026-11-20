// License: GPL. For details, see LICENSE file.
package org.openstreetmap.josm.plugins.turnlanestagging.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Arrays;
import java.util.List;
import java.util.StringTokenizer;

import javax.swing.JOptionPane;

import org.openstreetmap.josm.data.osm.DataSet;
import org.openstreetmap.josm.data.osm.OsmPrimitive;
import org.openstreetmap.josm.plugins.turnlanestagging.bean.BLane;
import org.openstreetmap.josm.gui.MainApplication;
import org.openstreetmap.josm.gui.Notification;
import org.openstreetmap.josm.tools.RightAndLefthandTraffic;

import static org.openstreetmap.josm.tools.I18n.tr;

/**
 *
 * @author ruben
 */
public final class Util {

    private Util() {
        // Hide default constructor for utilities classes
    }

    public static Object clone(Object object) {
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(baos);
            oos.writeObject(object);
            ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
            ObjectInputStream ois = new ObjectInputStream(bais);
            return ois.readObject();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static void alert(Object object) {
        JOptionPane.showMessageDialog(null, object);
    }

    public static void prints(Object object) {
        System.err.println(object);
    }

    public static boolean isInt(String str) {
        return (str.lastIndexOf("-") == 0 && !str.equals("-0")) ? str.replace("-", "").matches(
                "[0-9]+") : str.matches("[0-9]+");
    }

    public static void notification(String str) {
        new Notification(str).show();
    }

    public static boolean isEmptyturnlane(String turns) {
        List<String> turnsList = Arrays.asList(
                "reverse", "sharp_left", "left", "slight_left", "merge_to_right", "through",
                "reversible", "merge_to_left", "slight_right", "right", "sharp_right");
        for (String tl : turnsList) {
            if (turns.contains(tl)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Checks whether a {@code change:lanes} value contains any explicit lane change
     * restriction/allowance, so the caller knows whether the tag should be written.
     *
     * @param changes lane change value string, e.g. {@code "yes|not_left"} or {@code "||"}
     * @return {@code true} if at least one lane carries an explicit change value
     */
    public static boolean isEmptychangelane(String changes) {
        List<String> changesList = Arrays.asList("yes", "no", "not_left", "not_right");
        for (String cl : changesList) {
            if (changes.contains(cl)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Checks whether a {@code change:lanes} value mixes filled and empty lanes,
     * e.g. {@code "yes||no"} or {@code "yes|"}. JOSM/OSM does not handle empty
     * entries in the middle of the list well, so such values should be avoided.
     *
     * @param changes lane change value string, e.g. {@code "yes||no"}
     * @return {@code true} if at least one lane is filled and at least one is empty
     */
    public static boolean hasEmptyChangeLane(String changes) {
        if (changes == null || changes.isEmpty()) {
            return false;
        }
        boolean hasValue = false;
        boolean hasEmpty = false;
        for (String part : changes.split("\\|", -1)) {
            if (part.isEmpty()) {
                hasEmpty = true;
            } else {
                hasValue = true;
            }
        }
        return hasValue && hasEmpty;
    }

    /**
     * Whether a lane may not cross the boundary on its larger-offset side
     * (right side for forward/unidirectional lanes, left side for backward
     * lanes, since they face the opposite direction).
     */
    public static boolean blocksLargerOffsetSide(BLane lane) {
        String c = lane.getChange();
        if (c == null || c.isEmpty()) {
            return false;
        }
        if ("backward".equals(lane.getType())) {
            return "not_left".equals(c) || "no".equals(c);
        }
        return "not_right".equals(c) || "no".equals(c);
    }

    /**
     * Whether a lane may not cross the boundary on its smaller-offset side
     * (left side for forward/unidirectional lanes, right side for backward
     * lanes).
     */
    public static boolean blocksSmallerOffsetSide(BLane lane) {
        String c = lane.getChange();
        if (c == null || c.isEmpty()) {
            return false;
        }
        if ("backward".equals(lane.getType())) {
            return "not_right".equals(c) || "no".equals(c);
        }
        return "not_left".equals(c) || "no".equals(c);
    }

    /**
     * Whether the boundary between the two adjacent lanes may not be crossed,
     * because either lane carries a {@code change:lanes} restriction
     * ({@code no}/{@code not_left}/{@code not_right}) forbidding crossing it.
     */
    public static boolean boundaryChangeRestricted(BLane left, BLane right) {
        return blocksLargerOffsetSide(left) || blocksSmallerOffsetSide(right);
    }

    /**
     * Shows a warning notification when a {@code change:lanes} value has empty
     * lanes mixed with filled lanes, prompting the user to fill in every lane.
     *
     * @param changes lane change value string
     */
    public static void warnIfEmptyChangeLane(String changes) {
        if (hasEmptyChangeLane(changes)) {
            notification(tr("Lane change restriction is empty for some lanes. Fill in a value for every lane."));
        }
    }

    /**
     * Converts a turn lane value to use {@code "none"} instead of the empty string
     * to signify lanes with no turn markings
     *
     * @see <a href="http://wiki.openstreetmap.org/wiki/Key:turn#Turning_indications_per_lane">turn:lanes tagging</a>
     *
     * @param turn lanes value string with each lane separated by a vertical bar {@code "|"}.  For example, {@code "left|through|right"} or {@code "||right"}
     * @return the lane string with empty lanes replaced with "none".  For example, {@code "left||right"} is changed to {@code "left|none|right"}.
     */
    public static String setNoneOnEmpty(String turn) {
            StringTokenizer tokenizer = new StringTokenizer(turn, "|", true);
            String previous = "";
            StringBuilder builder = new StringBuilder();
            while (tokenizer.hasMoreTokens())
            {
                String current = tokenizer.nextToken();
                if (current.equals("|"))
                {
                    if (previous.equals(""))
                    {
                        // blank starting lane
                        builder.append("none");
                    }
                    else if (previous.equals("|"))
                    {
                        // blank middle lane
                        builder.append("none");
                    }
                }
                builder.append(current);
                if (current.equals("|") && !tokenizer.hasMoreTokens())
                {
                    // blank ending lane
                    builder.append("none");
                }
                previous = current;
            }

            return builder.toString();
    }

    public static boolean isRightHandTraffic() {
        DataSet ds = MainApplication.getLayerManager().getEditDataSet();
        boolean flag = false;
        if (ds != null) {
            for (OsmPrimitive element : ds.getSelected()) {
                flag = RightAndLefthandTraffic.isRightHandTraffic(element.getBBox().getCenter());
            }
        }
        return flag;
    }
}
