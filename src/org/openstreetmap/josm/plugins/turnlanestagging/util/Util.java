package org.openstreetmap.josm.plugins.turnlanestagging.util;

import javax.swing.JOptionPane;
import org.openstreetmap.josm.gui.Notification;

/**
 *
 * @author ruben
 */
public class Util {

    public static void alert(Object object) {
        JOptionPane.showMessageDialog(null, object);
    }

    public static void print(Object object) {
        System.err.println(object);
    }

    public static boolean isInt(String str) {
        return (str.lastIndexOf("-") == 0 && !str.equals("-0")) ? str.replace("-", "").matches(
                "[0-9]+") : str.matches("[0-9]+");
    }

    public static void notification(String str) {
        new Notification(str).show();
    }
}
