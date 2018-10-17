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
import org.openstreetmap.josm.gui.MainApplication;
import org.openstreetmap.josm.gui.Notification;
import org.openstreetmap.josm.tools.RightAndLefthandTraffic;

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
