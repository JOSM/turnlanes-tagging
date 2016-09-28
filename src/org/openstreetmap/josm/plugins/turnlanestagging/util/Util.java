// License: GPL. For details, see LICENSE file.
package org.openstreetmap.josm.plugins.turnlanestagging.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import javax.swing.JOptionPane;

import org.openstreetmap.josm.Main;
import org.openstreetmap.josm.data.osm.OsmPrimitive;
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

    public static String setNoneOnEmpty(String turn) {
        if (turn.startsWith("|")) {
            turn = "none" + turn;
        }
        if (turn.endsWith("|")) {
            turn = turn + "none";
        }
        return turn.replaceAll("(\\|)(\\|)", "$1none$2");
    }

    public static boolean isRightHandTraffic() {
        Collection<OsmPrimitive> selection = Main.getLayerManager().getEditDataSet().getSelected();
        boolean flag = false;
        for (OsmPrimitive element : selection) {
            flag = RightAndLefthandTraffic.isRightHandTraffic(element.getBBox().getCenter());
        }
        return flag;
    }
}
